package org.everowl.core.service.service.impl;

import lombok.RequiredArgsConstructor;
import org.everowl.core.service.dto.pointsActivity.request.CreateCustomerPointsAwardManualReq;
import org.everowl.core.service.dto.pointsActivity.request.CreateCustomerPointsAwardScanReq;
import org.everowl.core.service.dto.pointsActivity.response.PointsActivitiesDetailsRes;
import org.everowl.core.service.dto.pointsActivity.response.PointsActivityDetailsRes;
import org.everowl.core.service.service.PointsActivityDomain;
import org.everowl.core.service.service.shared.StoreCustomerService;
import org.everowl.database.service.entity.*;
import org.everowl.database.service.repository.*;
import org.everowl.shared.service.dto.GenericMessage;
import org.everowl.shared.service.exception.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.everowl.shared.service.enums.ErrorCode.*;
import static org.everowl.shared.service.util.JsonConverterUtils.convertObjectToJsonString;

@Service
@RequiredArgsConstructor
public class PointsActivityDomainImpl implements PointsActivityDomain {
    private final CustomerRepository customerRepository;
    private final PointsActivityRepository pointsActivityRepository;
    private final StoreCustomerRepository storeCustomerRepository;
    private final TierRepository tierRepository;
    private final StoreRepository storeRepository;
    private final ModelMapper modelMapper;
    private final AdminRepository adminRepository;
    private final StoreCustomerService storeCustomerService;
    private final AuditLogRepository auditLogRepository;

    @Override
    public PointsActivitiesDetailsRes getCustomerPointsActivitiesDetails(String loginId, Integer storeId) {
        CustomerEntity customer = customerRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException(STORE_NOT_EXIST));

        StoreCustomerEntity storeCustomer = storeCustomerService.getOrCreateStoreCustomer(customer, store);

        PointsActivitiesDetailsRes pointsActivitiesDetailsRes = new PointsActivitiesDetailsRes();

        List<PointsActivityEntity> pointsActivities = storeCustomer.getPointsActivities();
        pointsActivities.sort(Comparator.comparing(PointsActivityEntity::getCreatedAt, Date::compareTo).reversed());
        List<PointsActivityDetailsRes> pointsActivityDetailsResList = new ArrayList<>();
        for (PointsActivityEntity pointsActivity : pointsActivities) {
            PointsActivityDetailsRes pointsActivityDetailsRes = modelMapper.map(pointsActivity, PointsActivityDetailsRes.class);
            pointsActivityDetailsResList.add(pointsActivityDetailsRes);
        }

        pointsActivitiesDetailsRes.setPointsActivities(pointsActivityDetailsResList);

        return pointsActivitiesDetailsRes;
    }

    @Override
    @Transactional
    public GenericMessage createCustomerPointsAwardScan(String loginId, CreateCustomerPointsAwardScanReq createCustomerPointsAwardScanReq) {
        AdminEntity staff = adminRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        CustomerEntity customer = customerRepository.findById(createCustomerPointsAwardScanReq.getCustId())
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        return processPointsAward(staff, customer, createCustomerPointsAwardScanReq.getAmountSpent());
    }

    @Override
    @Transactional
    public GenericMessage createCustomerPointsAwardManual(String loginId, CreateCustomerPointsAwardManualReq createCustomerPointsAwardManualReq) {
        AdminEntity staff = adminRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        CustomerEntity customer = customerRepository.findByUsername(createCustomerPointsAwardManualReq.getCustLoginId())
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        return processPointsAward(staff, customer, createCustomerPointsAwardManualReq.getAmountSpent());
    }

    private PointsActivityEntity createPointsActivity(StoreCustomerEntity storeCustomer, AdminEntity staff,
                                                      BigDecimal originalPoints, BigDecimal finalisedPoints, BigDecimal multiplier) {
        String currentDate = getCurrentFormattedDate();

        PointsActivityEntity pointsActivityEntity = new PointsActivityEntity();
        pointsActivityEntity.setStoreCustomer(storeCustomer);
        pointsActivityEntity.setAdmin(staff);
        pointsActivityEntity.setCustExistingPoints(storeCustomer.getAvailablePoints());
        pointsActivityEntity.setOriginalPoints(originalPoints.intValue());
        pointsActivityEntity.setPointsMultiplier(multiplier);
        pointsActivityEntity.setFinalisedPoints(finalisedPoints.intValue());
        pointsActivityEntity.setActivityType("AWARD");
        pointsActivityEntity.setActivityDesc(String.format("Awarded %s points for spending.", finalisedPoints.intValue()));
        pointsActivityEntity.setActivityDate(currentDate);

        return pointsActivityRepository.save(pointsActivityEntity);
    }

    private StoreCustomerEntity updateCustomerTierAndPoints(StoreCustomerEntity storeCustomer, BigDecimal finalisedPoints) {
        int newTierPoints = storeCustomer.getTierPoints() + finalisedPoints.intValue();
        storeCustomer.setTierPoints(newTierPoints);

        // Process tier upgrades recursively
        processNextTier(storeCustomer, newTierPoints);

        // Update points and save
        storeCustomer.setAvailablePoints(storeCustomer.getAvailablePoints() + finalisedPoints.intValue());
        storeCustomer.setAccumulatedPoints(storeCustomer.getAccumulatedPoints() + finalisedPoints.intValue());
        storeCustomer.setLastTransDate(getCurrentFormattedDate());

        return storeCustomerRepository.save(storeCustomer);
    }

    private void processNextTier(StoreCustomerEntity storeCustomer, int currentPoints) {
        TierEntity currentTier = storeCustomer.getTier();
        Optional<TierEntity> nextTier = tierRepository.findStoreNextTier(
                storeCustomer.getStore().getStoreId(),
                currentTier.getTierLevel()
        );

        //No next tier or not enough points for next tier
        if (nextTier.isEmpty() || currentPoints < nextTier.get().getPointsNeeded()) {
            storeCustomer.setTierPoints(currentPoints);
            return;
        }

        // Process tier upgrade
        TierEntity newTier = nextTier.get();
        int remainingPoints = currentPoints - newTier.getPointsNeeded();
        storeCustomer.setTier(newTier);

        // Recursive call to check for further tier upgrades
        processNextTier(storeCustomer, remainingPoints);
    }

    private String getCurrentFormattedDate() {
        return ZonedDateTime.now(ZoneId.of("Asia/Kuala_Lumpur"))
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    private BigDecimal calculatePoints(BigDecimal amountSpent, BigDecimal multiplier) {
        BigDecimal originalPoints = amountSpent
                .multiply(BigDecimal.valueOf(10))
                .multiply(multiplier);

        return originalPoints
                .multiply(multiplier)
                .setScale(0, RoundingMode.CEILING);
    }

    private GenericMessage processPointsAward(AdminEntity staff, CustomerEntity customer, BigDecimal amountSpent) {
        StoreCustomerEntity storeCustomer = storeCustomerService.getOrCreateStoreCustomer(customer, staff.getStore());

        TierEntity currentTier = storeCustomer.getTier();

        String beforeChanged = convertObjectToJsonString(new Object[]{storeCustomer, currentTier});

        BigDecimal originalPoints = amountSpent.multiply(BigDecimal.valueOf(10)).setScale(0, RoundingMode.CEILING);
        BigDecimal finalisedPoints = calculatePoints(amountSpent, currentTier.getTierMultiplier());

        PointsActivityEntity savedPointsActivity = createPointsActivity(storeCustomer, staff, originalPoints, finalisedPoints, currentTier.getTierMultiplier());
        StoreCustomerEntity savedStoreCustomer = updateCustomerTierAndPoints(storeCustomer, finalisedPoints);
        TierEntity newCurrentTier = savedStoreCustomer.getTier();

        String afterChanged = convertObjectToJsonString(new Object[]{savedStoreCustomer, newCurrentTier, savedPointsActivity});

        AuditLogEntity auditLogEntity = new AuditLogEntity();
        auditLogEntity.setLoginId(staff.getLoginId());
        auditLogEntity.setPerformedBy(staff.getFullName());
        auditLogEntity.setAuthorityLevel("STAFF");
        auditLogEntity.setBeforeChanged(beforeChanged);
        auditLogEntity.setAfterChanged(afterChanged);
        auditLogEntity.setLogType("CREATE_POINTS_AWARD");
        auditLogEntity.setLogAction("CREATE");
        auditLogRepository.save(auditLogEntity);

        return GenericMessage.builder()
                .status(true)
                .build();
    }
}
