package org.everowl.scheduler.service.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.database.service.entity.*;
import org.everowl.database.service.repository.*;
import org.everowl.scheduler.service.model.exclusiveVoucher.StoreVoucherModel;
import org.everowl.scheduler.service.model.exclusiveVoucher.VoucherModel;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.everowl.shared.service.util.JsonConverterUtils.convertObjectToJsonString;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExclusiveVoucherJob {
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final VoucherRepository voucherRepository;
    private final StoreCustomerVoucherRepository storeCustomerVoucherRepository;
    private final AuditLogRepository auditLogRepository;
    private final ModelMapper modelMapper;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Kuala_Lumpur")
    @Transactional()
    public void birthdayVoucherJob() {
        log.info("Birthday Voucher Job Scheduler started at {}", LocalDateTime.now());

        Map<Integer, StoreVoucherModel> storeVoucherMap = getAllStoresWithMetaTagVouchers("birthday");

        List<CustomerEntity> customerEntityList = customerRepository.findAll();
        for (CustomerEntity customer : customerEntityList) {
            // Date is older than 1 month in Malaysian time
            if (isOlderThanOneMonth(customer.getCreatedAt())) {
                // The birthday falls within the window
                if (isDateWithinBirthdayWindow(customer.getDateOfBirth())) {
                    log.info("Customer ID: {} birthday is {}", customer.getCustId(), customer.getDateOfBirth());

                    List<StoreCustomerEntity> storeCustomerEntityList = customer.getStoreCustomers();
                    for (StoreCustomerEntity storeCustomer : storeCustomerEntityList) {
                        TierEntity currentCustomerTier = storeCustomer.getTier();

                        StoreVoucherModel store = storeVoucherMap.get(storeCustomer.getStore().getStoreId());
                        List<StoreCustomerVoucherEntity> storeCustomerVoucherEntityList = new ArrayList<>();
                        for (VoucherModel voucherModel : store.getVouchers()) {
                            if (currentCustomerTier.getTierLevel() >= voucherModel.getMinTierLevel()) {
                                StoreCustomerVoucherEntity storeCustomerVoucherEntity = new StoreCustomerVoucherEntity();
                                storeCustomerVoucherEntity.setStoreCustomer(storeCustomer);
                                storeCustomerVoucherEntity.setMinTierLevel(voucherModel.getMinTierLevel());
                                storeCustomerVoucherEntity.setPointsRequired(voucherModel.getPointsRequired());
                                storeCustomerVoucherEntity.setQuantityTotal(voucherModel.getQuantityTotal());
                                storeCustomerVoucherEntity.setQuantityLeft(voucherModel.getQuantityTotal());
                                String validDate = addDaysToToday(voucherModel.getLifeSpan());
                                storeCustomerVoucherEntity.setValidDate(validDate);
                                storeCustomerVoucherEntity.setVoucherName(voucherModel.getVoucherName());
                                storeCustomerVoucherEntity.setVoucherDesc(voucherModel.getVoucherDesc());
                                storeCustomerVoucherEntity.setVoucherType(voucherModel.getVoucherType());
                                storeCustomerVoucherEntity.setVoucherValue(voucherModel.getVoucherValue());
                                storeCustomerVoucherEntity.setAttachmentName(voucherModel.getAttachmentName());
                                storeCustomerVoucherEntity.setAttachmentPath(voucherModel.getAttachmentPath());
                                storeCustomerVoucherEntity.setAttachmentSize(voucherModel.getAttachmentSize());
                                storeCustomerVoucherEntity.setTncDesc(voucherModel.getTncDesc());
                                storeCustomerVoucherEntity.setIsExclusive(voucherModel.getIsExclusive());
                                storeCustomerVoucherEntity.setLifeSpan(voucherModel.getLifeSpan());
                                storeCustomerVoucherEntity.setMetaTag(voucherModel.getMetaTag());
                                storeCustomerVoucherEntityList.add(storeCustomerVoucherEntity);
                            }
                        }
                        List<StoreCustomerVoucherEntity> savedStoreCustomerVouchers = storeCustomerVoucherRepository.saveAll(storeCustomerVoucherEntityList);

                        String afterChanged = convertObjectToJsonString(savedStoreCustomerVouchers);

                        AuditLogEntity auditLogEntity = new AuditLogEntity();
                        auditLogEntity.setLoginId("system");
                        auditLogEntity.setPerformedBy("system");
                        auditLogEntity.setAuthorityLevel("SYSTEM");
                        auditLogEntity.setBeforeChanged(null);
                        auditLogEntity.setAfterChanged(afterChanged);
                        auditLogEntity.setLogType("CREATE_CUSTOMER_BIRTHDAY_VOUCHERS");
                        auditLogEntity.setLogAction("UPDATE");
                        auditLogRepository.save(auditLogEntity);
                    }
                }
            }
        }
    }

    public Map<Integer, StoreVoucherModel> getAllStoresWithMetaTagVouchers(String metaTag) {
        // Get all stores
        List<StoreEntity> storeEntities = storeRepository.findAll();

        // Map stores and their vouchers
        return storeEntities.stream()
                .map(storeEntity -> {
                    // Map basic store info
                    StoreVoucherModel storeVoucherModel =
                            modelMapper.map(storeEntity, StoreVoucherModel.class);

                    // Get and map vouchers for this store
                    List<VoucherModel> vouchers = voucherRepository
                            .findAvailableStoreMetaTagVouchers(storeEntity.getStoreId(), metaTag)
                            .stream()
                            .map(voucherEntity ->
                                    modelMapper.map(voucherEntity, VoucherModel.class))
                            .collect(Collectors.toList());

                    // Set vouchers to the model
                    storeVoucherModel.setVouchers(vouchers);

                    return storeVoucherModel;
                })
                .collect(Collectors.toMap(
                        StoreVoucherModel::getStoreId,  // Key mapper
                        storeVoucherModel -> storeVoucherModel  // Value mapper
                ));
    }

    private boolean isOlderThanOneMonth(Date createdAt) {
        // Define Malaysian time zone
        ZoneId malaysiaZone = ZoneId.of("Asia/Kuala_Lumpur");

        // Convert Date to LocalDateTime in Malaysian time
        LocalDateTime createdAtMalaysia = createdAt.toInstant()
                .atZone(malaysiaZone)
                .toLocalDateTime();

        // Get date 1 month ago in Malaysian time
        LocalDateTime oneMonthsAgo = LocalDateTime.now(malaysiaZone)
                .minusMonths(1);

        // Compare dates
        return createdAtMalaysia.isBefore(oneMonthsAgo);
    }

    private boolean isDateWithinBirthdayWindow(String dateOfBirth) {
        ZoneId malaysiaZone = ZoneId.of("Asia/Kuala_Lumpur");

        // Parse dateOfBirth format YYYYMMDD to get month and day
        LocalDate birthDate = LocalDate.parse(dateOfBirth,
                DateTimeFormatter.ofPattern("yyyyMMdd"));
        MonthDay birthMonthDay = MonthDay.from(birthDate);

        // Get current date and the date 7 days ago
        LocalDate today = LocalDate.now(malaysiaZone);
        LocalDate sevenDaysAgo = today.minusDays(7);

        // Create MonthDay object for comparison
        MonthDay sevenDaysAgoMonthDay = MonthDay.from(sevenDaysAgo);

        // Check if birth month/day is exactly 7 days ago
        return birthMonthDay.equals(sevenDaysAgoMonthDay);
    }

    public String addDaysToToday(int days) {
        ZoneId malaysiaZone = ZoneId.of("Asia/Kuala_Lumpur");
        LocalDateTime futureDateTime = LocalDateTime.now(malaysiaZone).plusDays(days);

        return futureDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
}
