package org.everowl.core.service.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.voucher.request.CreateVoucherReq;
import org.everowl.core.service.dto.voucher.request.DeleteVoucherReq;
import org.everowl.core.service.dto.voucher.request.UpdateVoucherReq;
import org.everowl.core.service.dto.voucher.response.VoucherDetailsRes;
import org.everowl.core.service.dto.voucher.response.VoucherRes;
import org.everowl.core.service.service.VoucherDomain;
import org.everowl.database.service.entity.AdminEntity;
import org.everowl.database.service.entity.AuditLogEntity;
import org.everowl.database.service.entity.VoucherEntity;
import org.everowl.database.service.repository.AdminRepository;
import org.everowl.database.service.repository.AuditLogRepository;
import org.everowl.database.service.repository.VoucherRepository;
import org.everowl.shared.service.dto.GenericMessage;
import org.everowl.shared.service.exception.BadRequestException;
import org.everowl.shared.service.exception.NotFoundException;
import org.everowl.shared.service.exception.RunTimeException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static org.everowl.shared.service.enums.ErrorCode.*;
import static org.everowl.shared.service.util.JsonConverterUtils.convertObjectToJsonString;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoucherDomainImpl implements VoucherDomain {
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB in bytes
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/jpeg",
            "image/png"
    );
    private final VoucherRepository voucherRepository;
    private final AdminRepository adminRepository;
    private final AuditLogRepository auditLogRepository;
    private final ModelMapper modelMapper;
    @Value("${app.attachment-storage.voucher-path}")
    private String storagePath;

    @Override
    public VoucherRes getAllVouchers(Integer storeId) {
        List<VoucherEntity> vouchers = voucherRepository.findByStoreId(storeId);

        List<VoucherDetailsRes> voucherList = new ArrayList<>();
        for (VoucherEntity voucher : vouchers) {
            VoucherDetailsRes voucherDetailsRes = modelMapper.map(voucher, VoucherDetailsRes.class);
            voucherList.add(voucherDetailsRes);
        }

        VoucherRes voucher = new VoucherRes();
        voucher.setVouchers(voucherList);

        return voucher;
    }

    @Override
    @Transactional
    public GenericMessage createVoucher(CreateVoucherReq voucherReq, String loginId) {
        AdminEntity admin = adminRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_AUTHORIZED));

        MultipartFile file = voucherReq.getAttachment();
        String fileName;
        String filePath;
        int fileSize;

        // Handle file upload and validation
        if (file != null && !file.isEmpty()) {
            fileName = uploadAttachmentFile(file);
            filePath = storagePath + fileName;
            fileSize = (int) voucherReq.getAttachment().getSize();
        } else {
            throw new BadRequestException(FILE_NOT_FOUND);
        }

        VoucherEntity voucher = new VoucherEntity();
        voucher.setStore(admin.getStore());
        voucher.setMinTierLevel(voucherReq.getMinTierLevel());
        voucher.setVoucherName(voucherReq.getVoucherName());
        voucher.setVoucherDesc(voucherReq.getVoucherDesc());
        voucher.setVoucherType(voucherReq.getVoucherType());
        voucher.setVoucherValue(voucherReq.getVoucherValue());
        voucher.setPointsRequired(voucherReq.getPointsRequired());
        voucher.setAttachmentName(fileName);
        voucher.setAttachmentPath(filePath);
        voucher.setAttachmentSize(fileSize);
        voucher.setIsAvailable(voucherReq.getIsAvailable());
        voucher.setTncDesc(voucherReq.getTncDesc());
        voucher.setIsExclusive(voucherReq.getIsExclusive());
        voucher.setLifeSpan(voucherReq.getLifeSpan());
        voucher.setMetaTag(voucherReq.getMetaTag());
        voucher.setQuantityTotal(voucherReq.getQuantityTotal());

        voucherRepository.save(voucher);

        AuditLogEntity auditLogEntity = new AuditLogEntity();
        auditLogEntity.setLoginId(loginId);
        auditLogEntity.setPerformedBy(admin.getFullName());
        auditLogEntity.setAuthorityLevel("OWNER");
        auditLogEntity.setBeforeChanged(null);
        auditLogEntity.setAfterChanged(convertObjectToJsonString(new Object[]{voucher}));
        auditLogEntity.setLogType("CREATE_STORE_VOUCHER");
        auditLogEntity.setLogAction("CREATE");
        auditLogEntity.setLogDesc("A store voucher was created");

        auditLogRepository.save(auditLogEntity);

        return GenericMessage.builder()
                .status(true)
                .build();
    }

    @Override
    @Transactional
    public GenericMessage updateVoucher(UpdateVoucherReq voucherReq, String loginId) {
        // Validate store owner
        AdminEntity admin = adminRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_AUTHORIZED));

        // Validate voucher
        VoucherEntity voucher = voucherRepository.findById(voucherReq.getVoucherId())
                .orElseThrow(() -> new BadRequestException(VOUCHER_NOT_EXIST));

        //Save original copy for audit log
        String beforeChange = convertObjectToJsonString(new Object[]{voucher});

        MultipartFile file = voucherReq.getAttachment();

        // Only handle attachment if a new file is provided
        if (file != null && !file.isEmpty()) {
            // Delete existing attachment if it exists
            if (voucher.getAttachmentName() != null) {
                deleteAttachmentFile(voucher.getAttachmentName());
            }

            // Upload new file and update attachment details
            String fileName = uploadAttachmentFile(file);
            String filePath = storagePath + fileName;
            Integer fileSize = (int) file.getSize();

            // Update attachment fields only when new file is provided
            voucher.setAttachmentName(fileName);
            voucher.setAttachmentPath(filePath);
            voucher.setAttachmentSize(fileSize);
        }

        voucher.setVoucherId(voucher.getVoucherId());
        voucher.setStore(voucher.getStore());
        voucher.setMinTierLevel(voucherReq.getMinTierLevel());
        voucher.setVoucherName(voucherReq.getVoucherName());
        voucher.setVoucherDesc(voucherReq.getVoucherDesc());
        voucher.setVoucherType(voucherReq.getVoucherType());
        voucher.setVoucherValue(voucherReq.getVoucherValue());
        voucher.setPointsRequired(voucherReq.getPointsRequired());
        voucher.setIsAvailable(voucherReq.getIsAvailable());
        voucher.setTncDesc(voucherReq.getTncDesc());
        voucher.setIsExclusive(voucherReq.getIsExclusive());
        voucher.setLifeSpan(voucherReq.getLifeSpan());
        voucher.setMetaTag(voucherReq.getMetaTag());
        voucher.setQuantityTotal(voucherReq.getQuantityTotal());

        voucherRepository.save(voucher);

        AuditLogEntity auditLogEntity = new AuditLogEntity();
        auditLogEntity.setLoginId(loginId);
        auditLogEntity.setPerformedBy(admin.getFullName());
        auditLogEntity.setAuthorityLevel("OWNER");
        auditLogEntity.setBeforeChanged(beforeChange);
        auditLogEntity.setAfterChanged(convertObjectToJsonString(new Object[]{voucher}));
        auditLogEntity.setLogType("UPDATE_STORE_VOUCHER");
        auditLogEntity.setLogAction("UPDATE");
        auditLogEntity.setLogDesc("A store voucher was updated");

        auditLogRepository.save(auditLogEntity);

        return GenericMessage.builder()
                .status(true)
                .build();
    }

    @Override
    @Transactional
    public GenericMessage deleteVoucher(DeleteVoucherReq voucherReq, String loginId) {
        // Validate store owner
        AdminEntity admin = adminRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_AUTHORIZED));

        // Validate voucher
        VoucherEntity voucher = voucherRepository.findById(Integer.parseInt(voucherReq.getVoucherId()))
                .orElseThrow(() -> new BadRequestException(VOUCHER_NOT_EXIST));

        if (voucher.getAttachmentName() != null) {
            deleteAttachmentFile(voucher.getAttachmentName());
        }

        voucherRepository.deleteById(Integer.parseInt(voucherReq.getVoucherId()));

        AuditLogEntity auditLogEntity = new AuditLogEntity();
        auditLogEntity.setLoginId(loginId);
        auditLogEntity.setPerformedBy(admin.getFullName());
        auditLogEntity.setAuthorityLevel("OWNER");
        auditLogEntity.setBeforeChanged(convertObjectToJsonString(new Object[]{voucher}));
        auditLogEntity.setAfterChanged(null);
        auditLogEntity.setLogType("DELETE_STORE_VOUCHER");
        auditLogEntity.setLogAction("DELETE");
        auditLogEntity.setLogDesc("A store voucher was deleted");

        auditLogRepository.save(auditLogEntity);

        return GenericMessage.builder()
                .status(true)
                .build();
    }

    @Override
    public String getVoucherAttachment(String attachmentName) {
        VoucherEntity voucher = voucherRepository.findByAttachmentName(attachmentName)
                .orElseThrow(() -> new NotFoundException(FILE_NOT_FOUND));

        return voucher.getAttachmentName();
    }

    public String uploadAttachmentFile(MultipartFile file) {
        try {
            // Validate the image file before uploading
            validateFile(file);

//            // Get file extension
//            String originalFilename = file.getOriginalFilename();
//            if (originalFilename == null || originalFilename.isEmpty()) {
//                throw new BadRequestException(FILE_NOT_FOUND);
//            }
//            String extension = FilenameUtils.getExtension(originalFilename).toLowerCase();

            // Generate a unique file name
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String uniqueFileName = UUID.randomUUID() + "-" + fileName;

            // Resolve the file storage location and copy the file
            Path fileStorageLocation = Paths.get(storagePath).toAbsolutePath().normalize();
            Path targetLocation = fileStorageLocation.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return uniqueFileName;

        } catch (Exception e) {
            throw new RunTimeException(FILE_UPLOAD_ERROR);
        }
    }

    public void deleteAttachmentFile(String uniqueFileName) {
        try {
            // Resolve the path of the file to be deleted
            Path fileToDeletePath = Paths.get(storagePath + "/" + uniqueFileName);

            // Delete the file if it exists
            Files.deleteIfExists(fileToDeletePath);
        } catch (Exception e) {
            throw new RunTimeException(FILE_DELETE_ERROR);
        }
    }

    public void validateFile(MultipartFile file) {
        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException(FILE_SIZE_ERROR);
        }

        // Check file type
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new BadRequestException(FILE_TYPE_ERROR);
        }
    }
}
