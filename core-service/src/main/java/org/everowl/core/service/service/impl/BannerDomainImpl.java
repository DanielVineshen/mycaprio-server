package org.everowl.core.service.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.banner.request.CreateBannerReq;
import org.everowl.core.service.dto.banner.request.DeleteBannerReq;
import org.everowl.core.service.dto.banner.request.UpdateBannerReq;
import org.everowl.core.service.dto.banner.response.BannerRes;
import org.everowl.core.service.service.BannerDomain;
import org.everowl.database.service.entity.AdminEntity;
import org.everowl.database.service.entity.BannerAttachmentEntity;
import org.everowl.database.service.repository.AdminRepository;
import org.everowl.database.service.repository.BannerAttachmentRepository;
import org.everowl.shared.service.dto.GenericMessage;
import org.everowl.shared.service.exception.BadRequestException;
import org.everowl.shared.service.exception.NotFoundException;
import org.everowl.shared.service.exception.RunTimeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.everowl.shared.service.enums.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BannerDomainImpl implements BannerDomain {
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB in bytes
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/jpeg",
            "image/png"
    );
    private final BannerAttachmentRepository bannerAttachmentRepository;
    private final AdminRepository adminRepository;
    @Value("${app.attachment-storage.banner-path}")
    private String storagePath;

    @Override
    @Transactional
    public GenericMessage createBanner(CreateBannerReq bannerReq, String username) {
        AdminEntity admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(USER_NOT_AUTHORIZED));

        MultipartFile file = bannerReq.getAttachment();

        String fileName = uploadAttachmentFile(file);
        String filePath = storagePath + fileName;
        Integer fileSize = (int) bannerReq.getAttachment().getSize();

        BannerAttachmentEntity banner = new BannerAttachmentEntity();
        banner.setStore(admin.getStore());
        banner.setAttachmentName(fileName);
        banner.setAttachmentPath(filePath);
        banner.setAttachmentSize(fileSize);
        banner.setAttachmentType(bannerReq.getAttachmentType());

        bannerAttachmentRepository.save(banner);

        return GenericMessage.builder()
                .status(true)
                .build();
    }

    @Override
    public GenericMessage updateBanner(UpdateBannerReq bannerReq, String username) {
        AdminEntity admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(USER_NOT_AUTHORIZED));

        BannerAttachmentEntity banner = bannerAttachmentRepository.findById(bannerReq.getAttachmentId())
                .orElseThrow(() -> new NotFoundException(FILE_NOT_FOUND));

        MultipartFile file = bannerReq.getAttachment();

        //Delete existing before saving new banner
        deleteAttachmentFile(banner.getAttachmentName());
        String fileName = uploadAttachmentFile(file);
        String filePath = storagePath + fileName;
        Integer fileSize = (int) bannerReq.getAttachment().getSize();

        banner.setAttachmentId(banner.getAttachmentId());
        banner.setStore(admin.getStore());
        banner.setAttachmentName(fileName);
        banner.setAttachmentPath(filePath);
        banner.setAttachmentSize(fileSize);
        banner.setAttachmentType(bannerReq.getAttachmentType());

        bannerAttachmentRepository.save(banner);

        return GenericMessage.builder()
                .status(true)
                .build();
    }

    @Override
    public GenericMessage deleteBanner(DeleteBannerReq bannerReq, String username) {
        AdminEntity admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(USER_NOT_AUTHORIZED));

        BannerAttachmentEntity banner = bannerAttachmentRepository.findById(bannerReq.getAttachmentId())
                .orElseThrow(() -> new NotFoundException(FILE_NOT_FOUND));

        deleteAttachmentFile(banner.getAttachmentName());

        bannerAttachmentRepository.deleteById(bannerReq.getAttachmentId());

        return GenericMessage.builder()
                .status(true)
                .build();
    }

    @Override
    public List<BannerRes> getAllBanners(Integer storeId) {


        return List.of();
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
