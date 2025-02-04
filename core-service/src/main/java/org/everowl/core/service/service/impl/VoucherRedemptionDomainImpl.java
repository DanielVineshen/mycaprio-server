package org.everowl.core.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.voucherRedemption.request.CustomerVoucherPurchase;
import org.everowl.core.service.dto.voucherRedemption.request.CustomerVoucherRedemption;
import org.everowl.core.service.service.VoucherRedemptionDomain;
import org.everowl.database.service.entity.*;
import org.everowl.database.service.repository.*;
import org.everowl.shared.service.dto.GenericMessage;
import org.everowl.shared.service.exception.ForbiddenException;
import org.everowl.shared.service.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.everowl.shared.service.enums.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VoucherRedemptionDomainImpl implements VoucherRedemptionDomain {
    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;
    private final StoreCustomerRepository storeCustomerRepository;
    private final VoucherRepository voucherRepository;
    private final StoreCustomerVoucherRepository storeCustomerVoucherRepository;
    private final VoucherRedemptionRepository voucherRedemptionRepository;
    private final TierRepository tierRepository;
    private final PointsActivityRepository pointsActivityRepository;

    @Override
    @Transactional
    public GenericMessage createCustomerVoucherPurchase(String loginId, CustomerVoucherPurchase customerVoucherPurchase) {
        CustomerEntity customer = customerRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        VoucherEntity voucher = voucherRepository.findById(customerVoucherPurchase.getVoucherId())
                .orElseThrow(() -> new NotFoundException(VOUCHER_NOT_EXIST));

        StoreEntity store = voucher.getStore();

        Optional<StoreCustomerEntity> storeCustomerEntity = storeCustomerRepository.findCustomerStoreProfile(customer.getCustId(), store.getStoreId());

        StoreCustomerEntity storeCustomer = storeCustomerEntity.orElseGet(() -> {
            TierEntity tier = tierRepository.findStoreDefaultTier(store.getStoreId())
                    .orElseThrow(() -> new NotFoundException(TIER_NOT_EXIST));

            StoreCustomerEntity newStoreCustomer = new StoreCustomerEntity();
            newStoreCustomer.setCustomer(customer);
            newStoreCustomer.setStore(store);
            newStoreCustomer.setTier(tier);
            newStoreCustomer.setTierPoints(0);
            newStoreCustomer.setAvailablePoints(0);
            newStoreCustomer.setAccumulatedPoints(0);
            return storeCustomerRepository.save(newStoreCustomer);
        });

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

        return GenericMessage.builder()
                .status(true)
                .build();
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
    public GenericMessage createCustomerVoucherRedemption(String loginId, CustomerVoucherRedemption customerVoucherRedemption) {
        AdminEntity staff = adminRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        StoreCustomerVoucherEntity storeCustomerVoucher = storeCustomerVoucherRepository.findById(customerVoucherRedemption.getStoreCustVoucherId())
                .orElseThrow(() -> new NotFoundException(STORE_CUSTOMER_VOUCHER_NOT_EXIST));

        boolean isValidDateOlder = LocalDateTime.parse(storeCustomerVoucher.getValidDate(), DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                .atZone(ZoneId.of("Asia/Kuala_Lumpur"))
                .isBefore(ZonedDateTime.now(ZoneId.of("Asia/Kuala_Lumpur")));

        if (storeCustomerVoucher.getQuantityLeft() == 0 || isValidDateOlder) {
            throw new ForbiddenException(VOUCHER_REDEEMED_EXPIRED);
        }

        VoucherRedemptionEntity voucherRedemptionEntity = new VoucherRedemptionEntity();
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
