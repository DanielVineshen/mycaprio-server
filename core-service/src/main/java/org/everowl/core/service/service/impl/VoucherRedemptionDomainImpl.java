package org.everowl.core.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.voucherRedemption.request.CustomerVoucherPurchaseReq;
import org.everowl.core.service.dto.voucherRedemption.request.CustomerVoucherRedemptionReq;
import org.everowl.core.service.dto.voucherRedemption.response.*;
import org.everowl.core.service.service.VoucherRedemptionDomain;
import org.everowl.core.service.service.shared.EncryptionService;
import org.everowl.core.service.service.shared.StoreCustomerService;
import org.everowl.core.service.service.shared.VoucherMetadataService;
import org.everowl.database.service.entity.*;
import org.everowl.database.service.repository.*;
import org.everowl.shared.service.exception.ForbiddenException;
import org.everowl.shared.service.exception.NotFoundException;
import org.everowl.shared.service.exception.RunTimeException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.everowl.shared.service.enums.ErrorCode.*;
import static org.everowl.shared.service.util.JsonConverterUtils.convertObjectToJsonString;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoucherRedemptionDomainImpl implements VoucherRedemptionDomain {
    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;
    private final StoreCustomerRepository storeCustomerRepository;
    private final VoucherRepository voucherRepository;
    private final StoreCustomerVoucherRepository storeCustomerVoucherRepository;
    private final VoucherRedemptionRepository voucherRedemptionRepository;
    private final PointsActivityRepository pointsActivityRepository;
    private final ModelMapper modelMapper;
    private final StoreCustomerService storeCustomerService;
    private final AuditLogRepository auditLogRepository;
    private final EncryptionService encryptionService;
    private final VoucherMetadataService voucherMetadataService;
    private final VoucherMetadataRepository voucherMetadataRepository;

    @Override
    @Transactional
    public CustomerVoucherPurchaseDetailsRes createCustomerVoucherPurchase(String loginId, CustomerVoucherPurchaseReq customerVoucherPurchaseReq) {
        CustomerEntity customer = customerRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        VoucherEntity voucher = voucherRepository.findById(customerVoucherPurchaseReq.getVoucherId())
                .orElseThrow(() -> new NotFoundException(VOUCHER_NOT_EXIST));

        StoreEntity store = voucher.getStore();

        StoreCustomerEntity storeCustomer = storeCustomerService.getOrCreateStoreCustomer(customer, store);

        VoucherMetadataEntity voucherMetadataEntity = voucherMetadataService.getOrCreateVoucherMetadata(storeCustomer, voucher);

        String beforeChanged = convertObjectToJsonString(new Object[]{voucher, voucherMetadataEntity, storeCustomer});

        validateVoucherPurchaseEligibility(voucher, storeCustomer);

        String voucherValidDate = ZonedDateTime.now(ZoneId.of("Asia/Kuala_Lumpur"))
                .plusDays(voucher.getLifeSpan())
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        voucher.setTotalPurchase(voucher.getTotalPurchase() + 1);
        VoucherEntity savedVoucher = voucherRepository.save(voucher);

        voucherMetadataEntity.setTotalPurchase(voucherMetadataEntity.getTotalPurchase() + 1);
        VoucherMetadataEntity savedVoucherMetadata = voucherMetadataRepository.save(voucherMetadataEntity);

        StoreCustomerVoucherEntity storeCustomerVoucherEntity = new StoreCustomerVoucherEntity();
        storeCustomerVoucherEntity.setStoreCustomer(storeCustomer);
        storeCustomerVoucherEntity.setMinTierLevel(voucher.getMinTierLevel());
        storeCustomerVoucherEntity.setPointsRequired(voucher.getPointsRequired());
        storeCustomerVoucherEntity.setQuantityTotal(voucher.getQuantityTotal());
        storeCustomerVoucherEntity.setQuantityLeft(voucher.getQuantityTotal());
        storeCustomerVoucherEntity.setValidDate(voucherValidDate);
        storeCustomerVoucherEntity.setVoucherName(voucher.getVoucherName());
        storeCustomerVoucherEntity.setVoucherDesc(voucher.getVoucherDesc());
        storeCustomerVoucherEntity.setVoucherType(voucher.getVoucherType());
        storeCustomerVoucherEntity.setVoucherValue(voucher.getVoucherValue());
        storeCustomerVoucherEntity.setAttachmentName(voucher.getAttachmentName());
        storeCustomerVoucherEntity.setAttachmentPath(voucher.getAttachmentPath());
        storeCustomerVoucherEntity.setAttachmentSize(voucher.getAttachmentSize());
        storeCustomerVoucherEntity.setTncDesc(voucher.getTncDesc());
        storeCustomerVoucherEntity.setIsExclusive(voucher.getIsExclusive());
        storeCustomerVoucherEntity.setLifeSpan(voucher.getLifeSpan());
        storeCustomerVoucherEntity.setMetaTag(voucher.getMetaTag());
        StoreCustomerVoucherEntity savedCustomerVoucher = storeCustomerVoucherRepository.save(storeCustomerVoucherEntity);

        storeCustomer.setAvailablePoints(storeCustomer.getAvailablePoints() - voucher.getPointsRequired());
        StoreCustomerEntity updatedStoreCustomer = storeCustomerRepository.save(storeCustomer);

        String currentDate = ZonedDateTime.now(ZoneId.of("Asia/Kuala_Lumpur"))
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        PointsActivityEntity pointsActivityEntity = new PointsActivityEntity();
        pointsActivityEntity.setStoreCustomer(storeCustomer);
        pointsActivityEntity.setCustExistingPoints(storeCustomer.getAvailablePoints());
        pointsActivityEntity.setOriginalPoints(voucher.getPointsRequired());
        pointsActivityEntity.setPointsMultiplier(BigDecimal.valueOf(1));
        pointsActivityEntity.setFinalisedPoints(voucher.getPointsRequired());
        pointsActivityEntity.setActivityType("REDEEM");
        pointsActivityEntity.setActivityDesc(String.format("Redeemed %s for %d points.", voucher.getVoucherName(), voucher.getPointsRequired()));
        pointsActivityEntity.setActivityDate(currentDate);
        PointsActivityEntity savedPointsActivity = pointsActivityRepository.save(pointsActivityEntity);

        CustomerVoucherPurchaseDetailsRes customerVoucherPurchaseDetailsRes = new CustomerVoucherPurchaseDetailsRes();
        CustomerVoucherDetailsRes customerVoucherDetailsRes = modelMapper.map(storeCustomerVoucherEntity, CustomerVoucherDetailsRes.class);
        customerVoucherPurchaseDetailsRes.setCustomerVoucherPurchase(customerVoucherDetailsRes);

        String afterChanged = convertObjectToJsonString(new Object[]{savedVoucher, savedVoucherMetadata, savedCustomerVoucher, updatedStoreCustomer, savedPointsActivity});

        AuditLogEntity auditLogEntity = new AuditLogEntity();
        auditLogEntity.setLoginId(customer.getLoginId());
        auditLogEntity.setPerformedBy(customer.getFullName());
        auditLogEntity.setAuthorityLevel("CUSTOMER");
        auditLogEntity.setBeforeChanged(beforeChanged);
        auditLogEntity.setAfterChanged(afterChanged);
        auditLogEntity.setLogType("CREATE_CUSTOMER_VOUCHER");
        auditLogEntity.setLogAction("CREATE");
        auditLogRepository.save(auditLogEntity);

        return customerVoucherPurchaseDetailsRes;
    }

    private void validateVoucherPurchaseEligibility(VoucherEntity voucher, StoreCustomerEntity storeCustomer) {
        if (voucher.getIsExclusive() ||
                !voucher.getIsAvailable() ||
                voucher.getIsDeleted() ||
                isCustomerTierTooLow(storeCustomer, voucher)) {
            throw new ForbiddenException(VOUCHER_CANNOT_PURCHASE);
        }
    }

    private boolean isCustomerTierTooLow(StoreCustomerEntity storeCustomer, VoucherEntity voucher) {
        return storeCustomer.getTier().getTierLevel() < voucher.getMinTierLevel();
    }

    @Override
    @Transactional
    public CustomerVoucherRedemptionDetailsRes createCustomerVoucherRedemption(String loginId, CustomerVoucherRedemptionReq customerVoucherRedemptionReq) {
        try {
            String decrypted = encryptionService.decryptCompact(customerVoucherRedemptionReq.getCode());
            String[] trimmedParts = decrypted.split("\\|");
            String[] trimmedArray = Arrays.stream(trimmedParts)
                    .map(String::trim)
                    .toArray(String[]::new);

            boolean validationCheck = Integer.parseInt(trimmedArray[0]) == customerVoucherRedemptionReq.getStoreCustVoucherId()
                    && isWithinFiveMinutes(Long.parseLong(trimmedArray[1]));

            if (!validationCheck) {
                throw new ForbiddenException(USER_NOT_PERMITTED);
            }
        } catch (Exception e) {
            log.info("Exception decoding code: {}", e.toString());
//            throw new RunTimeException(DECRYPTING_CODE_EXCEPTION);
            throw new ForbiddenException(VOUCHER_CODE_EXPIRED);
        }

        AdminEntity staff = adminRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        StoreCustomerVoucherEntity storeCustomerVoucher = storeCustomerVoucherRepository.findById(customerVoucherRedemptionReq.getStoreCustVoucherId())
                .orElseThrow(() -> new NotFoundException(STORE_CUSTOMER_VOUCHER_NOT_EXIST));

        String beforeChanged = convertObjectToJsonString(storeCustomerVoucher);

        boolean isValidDateOlder = LocalDateTime.parse(storeCustomerVoucher.getValidDate(), DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                .atZone(ZoneId.of("Asia/Kuala_Lumpur"))
                .isBefore(ZonedDateTime.now(ZoneId.of("Asia/Kuala_Lumpur")));

        if (storeCustomerVoucher.getQuantityLeft() == 0 || isValidDateOlder) {
            throw new ForbiddenException(VOUCHER_REDEEMED_EXPIRED);
        }

        VoucherRedemptionEntity voucherRedemptionEntity = new VoucherRedemptionEntity();
        voucherRedemptionEntity.setStoreCustomerVoucher(storeCustomerVoucher);
        voucherRedemptionEntity.setAdmin(staff);
        VoucherRedemptionEntity savedVoucherRedemption = voucherRedemptionRepository.save(voucherRedemptionEntity);

        storeCustomerVoucher.setQuantityLeft(storeCustomerVoucher.getQuantityLeft() - 1);
        StoreCustomerVoucherEntity savedCustomerVoucher = storeCustomerVoucherRepository.save(storeCustomerVoucher);

        String afterChanged = convertObjectToJsonString(new Object[]{savedCustomerVoucher, savedVoucherRedemption, staff});

        AuditLogEntity auditLogEntity = new AuditLogEntity();
        auditLogEntity.setLoginId(staff.getLoginId());
        auditLogEntity.setPerformedBy(staff.getFullName());
        auditLogEntity.setAuthorityLevel("STAFF");
        auditLogEntity.setBeforeChanged(beforeChanged);
        auditLogEntity.setAfterChanged(afterChanged);
        auditLogEntity.setLogType("CREATE_VOUCHER_REDEEM");
        auditLogEntity.setLogAction("CREATE");
        auditLogRepository.save(auditLogEntity);

        CustomerVoucherRedemptionDetailsRes customerVoucherRedemptionDetailsRes = new CustomerVoucherRedemptionDetailsRes();
        CustomerVoucherDetailsRes customerVoucherDetailsRes = modelMapper.map(storeCustomerVoucher, CustomerVoucherDetailsRes.class);
        customerVoucherRedemptionDetailsRes.setCustomerVoucherRedemption(customerVoucherDetailsRes);

        return customerVoucherRedemptionDetailsRes;
    }

    private boolean isWithinFiveMinutes(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long diffInMillis = currentTime - timestamp;
        return diffInMillis < (5 * 60 * 1000);
    }

    @Override
    public CustomerVoucherCodeDetailsRes generateCustomerVoucherCode(String loginId, Integer storeCustomerVoucherId) {
        customerRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        StoreCustomerVoucherEntity storeCustomerVoucher = storeCustomerVoucherRepository.findById(storeCustomerVoucherId)
                .orElseThrow(() -> new NotFoundException(STORE_CUSTOMER_VOUCHER_NOT_EXIST));

        boolean isValidDateOlder = LocalDateTime.parse(storeCustomerVoucher.getValidDate(), DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                .atZone(ZoneId.of("Asia/Kuala_Lumpur"))
                .isBefore(ZonedDateTime.now(ZoneId.of("Asia/Kuala_Lumpur")));

        if (storeCustomerVoucher.getQuantityLeft() == 0 || isValidDateOlder) {
            throw new ForbiddenException(VOUCHER_REDEEMED_EXPIRED);
        }

        VoucherCodeDetails voucherCodeDetails = new VoucherCodeDetails();
        try {
            String sensitiveData = storeCustomerVoucherId + "|" + System.currentTimeMillis();
            String encrypted = encryptionService.encryptCompact(sensitiveData);
            voucherCodeDetails.setCode(encrypted);
        } catch (Exception e) {
            throw new RunTimeException(ENCRYPTING_CODE_EXCEPTION);
        }

        CustomerVoucherCodeDetailsRes customerVoucherCodeDetailsRes = new CustomerVoucherCodeDetailsRes();
        customerVoucherCodeDetailsRes.setVoucherCode(voucherCodeDetails);

        return customerVoucherCodeDetailsRes;
    }
}
