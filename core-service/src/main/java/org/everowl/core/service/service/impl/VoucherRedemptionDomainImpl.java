package org.everowl.core.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.voucherRedemption.request.CustomerVoucherPurchaseReq;
import org.everowl.core.service.dto.voucherRedemption.request.CustomerVoucherRedemptionReq;
import org.everowl.core.service.dto.voucherRedemption.response.CustomerVoucherDetailsRes;
import org.everowl.core.service.dto.voucherRedemption.response.CustomerVoucherPurchaseDetailsRes;
import org.everowl.core.service.service.VoucherRedemptionDomain;
import org.everowl.core.service.service.shared.StoreCustomerService;
import org.everowl.database.service.entity.*;
import org.everowl.database.service.entity.compositeKeys.VoucherRedemptionPKs;
import org.everowl.database.service.repository.*;
import org.everowl.shared.service.dto.GenericMessage;
import org.everowl.shared.service.exception.ForbiddenException;
import org.everowl.shared.service.exception.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.everowl.shared.service.enums.ErrorCode.*;

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

    @Override
    @Transactional
    public CustomerVoucherPurchaseDetailsRes createCustomerVoucherPurchase(String loginId, CustomerVoucherPurchaseReq customerVoucherPurchaseReq) {
        CustomerEntity customer = customerRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        VoucherEntity voucher = voucherRepository.findById(customerVoucherPurchaseReq.getVoucherId())
                .orElseThrow(() -> new NotFoundException(VOUCHER_NOT_EXIST));

        StoreEntity store = voucher.getStore();

        StoreCustomerEntity storeCustomer = storeCustomerService.getOrCreateStoreCustomer(customer, store);

        validateVoucherPurchaseEligibility(voucher, storeCustomer);

        String voucherValidDate = ZonedDateTime.now(ZoneId.of("Asia/Kuala_Lumpur"))
                .plusDays(voucher.getLifeSpan())
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        StoreCustomerVoucherEntity storeCustomerVoucherEntity = new StoreCustomerVoucherEntity();
        storeCustomerVoucherEntity.setStoreCustomer(storeCustomer);
        storeCustomerVoucherEntity.setMinTierLevel(voucher.getMinTierLevel());
        storeCustomerVoucherEntity.setPointsRequired(voucher.getPointsRequired());
        storeCustomerVoucherEntity.setQuantityTotal(voucher.getQuantityTotal());
        storeCustomerVoucherEntity.setQuantityLeft(voucher.getQuantityTotal());
        storeCustomerVoucherEntity.setValidDate(voucherValidDate);
        storeCustomerVoucherEntity.setVoucherName(voucher.getVoucherName());
        storeCustomerVoucherEntity.setVoucherDesc(voucher.getVoucherDesc());
        storeCustomerVoucherEntity.setTncDesc(voucher.getTncDesc());
        storeCustomerVoucherEntity.setIsExclusive(voucher.getIsExclusive());
        storeCustomerVoucherEntity.setLifeSpan(voucher.getLifeSpan());
        storeCustomerVoucherEntity.setMetaTag(voucher.getMetaTag());
        storeCustomerVoucherRepository.save(storeCustomerVoucherEntity);

        storeCustomer.setAvailablePoints(storeCustomer.getAvailablePoints() - voucher.getPointsRequired());
        storeCustomerRepository.save(storeCustomer);

        String currentDate = ZonedDateTime.now(ZoneId.of("Asia/Kuala_Lumpur"))
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        PointsActivityEntity pointsActivityEntity = new PointsActivityEntity();
        pointsActivityEntity.setStoreCustomer(storeCustomer);
        pointsActivityEntity.setCustExistingPoints(storeCustomer.getAvailablePoints());
        pointsActivityEntity.setOriginalPoints(voucher.getPointsRequired());
        pointsActivityEntity.setPointsMultiplier(BigDecimal.valueOf(1));
        pointsActivityEntity.setFinalisedPoints(voucher.getPointsRequired());
        pointsActivityEntity.setActivityType("REDEEM");
        pointsActivityEntity.setActivityDate(currentDate);
        pointsActivityRepository.save(pointsActivityEntity);

        CustomerVoucherPurchaseDetailsRes customerVoucherPurchaseDetailsRes = new CustomerVoucherPurchaseDetailsRes();
        CustomerVoucherDetailsRes customerVoucherDetailsRes = modelMapper.map(storeCustomerVoucherEntity, CustomerVoucherDetailsRes.class);
        customerVoucherPurchaseDetailsRes.setCustomerVoucherPurchase(customerVoucherDetailsRes);

        return customerVoucherPurchaseDetailsRes;
    }

    private void validateVoucherPurchaseEligibility(VoucherEntity voucher, StoreCustomerEntity storeCustomer) {
        if (voucher.getIsExclusive() ||
                !voucher.getIsAvailable() ||
                isCustomerTierTooLow(storeCustomer, voucher)) {
            throw new ForbiddenException(VOUCHER_CANNOT_PURCHASE);
        }
    }

    private boolean isCustomerTierTooLow(StoreCustomerEntity storeCustomer, VoucherEntity voucher) {
        return storeCustomer.getTier().getTierLevel() < voucher.getMinTierLevel();
    }

    @Override
    @Transactional
    public GenericMessage createCustomerVoucherRedemption(String loginId, CustomerVoucherRedemptionReq customerVoucherRedemptionReq) {
        AdminEntity staff = adminRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        StoreCustomerVoucherEntity storeCustomerVoucher = storeCustomerVoucherRepository.findById(customerVoucherRedemptionReq.getStoreCustVoucherId())
                .orElseThrow(() -> new NotFoundException(STORE_CUSTOMER_VOUCHER_NOT_EXIST));

        boolean isValidDateOlder = LocalDateTime.parse(storeCustomerVoucher.getValidDate(), DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                .atZone(ZoneId.of("Asia/Kuala_Lumpur"))
                .isBefore(ZonedDateTime.now(ZoneId.of("Asia/Kuala_Lumpur")));

        if (storeCustomerVoucher.getQuantityLeft() == 0 || isValidDateOlder) {
            throw new ForbiddenException(VOUCHER_REDEEMED_EXPIRED);
        }

        VoucherRedemptionEntity voucherRedemptionEntity = new VoucherRedemptionEntity();
        VoucherRedemptionPKs voucherRedemptionPKs = new VoucherRedemptionPKs();
        voucherRedemptionPKs.setStoreCustVoucherId(storeCustomerVoucher.getStoreCustVoucherId());
        voucherRedemptionPKs.setAdminId(staff.getAdminId());
        voucherRedemptionEntity.setVoucherRedemptionPKs(voucherRedemptionPKs);
        voucherRedemptionEntity.setStoreCustomerVoucher(storeCustomerVoucher);
        voucherRedemptionEntity.setAdmin(staff);
        voucherRedemptionRepository.save(voucherRedemptionEntity);

        storeCustomerVoucher.setQuantityLeft(storeCustomerVoucher.getQuantityLeft() - 1);
        storeCustomerVoucherRepository.save(storeCustomerVoucher);

        return GenericMessage.builder()
                .status(true)
                .build();
    }
}
