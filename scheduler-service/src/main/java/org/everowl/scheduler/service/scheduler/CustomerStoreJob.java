package org.everowl.scheduler.service.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.database.service.entity.StoreCustomerEntity;
import org.everowl.database.service.entity.StoreEntity;
import org.everowl.database.service.entity.TierEntity;
import org.everowl.database.service.repository.StoreCustomerRepository;
import org.everowl.database.service.repository.StoreRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerStoreJob {
    private final StoreRepository storeRepository;
    private final StoreCustomerRepository storeCustomerRepository;

    @Scheduled(fixedDelay = 60000)
    @Transactional(readOnly = true)
    public void customerTierMaintainJob() {
        log.info("Customer Tier Maintain Job Scheduler started at {}", LocalDateTime.now());

        List<StoreEntity> storeEntityList = storeRepository.findAll();
        for (StoreEntity store : storeEntityList) {
            List<TierEntity> tierEntityList = store.getTiers();

            List<StoreCustomerEntity> storeCustomerEntityList = store.getStoreCustomers();
            for (StoreCustomerEntity storeCustomer : storeCustomerEntityList) {
                if (storeCustomer.getLastTransDate() == null) {
                    log.warn("customer {} has no last transaction date", storeCustomer.getStoreCustId());
                    continue;  // skip this customer
                }

                if (isOlderThanFourMonths(storeCustomer.getLastTransDate())) {
                    TierEntity customerTier = storeCustomer.getTier();

                    log.info("evaluating store: {} for customer {}", store.getStoreName(), storeCustomer.getCustomer().getCustId());

                    // Date is older than 4 months
                    TierEntity lowerTier = findNextLowerTier(tierEntityList, customerTier.getTierLevel());
                    if (lowerTier != null) {
                        // Found a lower tier
                        Integer newTierLevel = lowerTier.getTierLevel();
                        log.info("found new tier level: {}", newTierLevel);

                        // Set the new lower tier
                        storeCustomer.setTier(lowerTier);
                    } else {
                        // Customer is already at lowest tier
                        log.info("no tier level found");
                    }

                    // Update the last transaction date to right now
                    String newLastTransDate = getCurrentFormattedDate();
                    storeCustomer.setLastTransDate(newLastTransDate);

                    storeCustomerRepository.save(storeCustomer);
                }
            }
        }
    }

    public boolean isOlderThanFourMonths(String lastTransDate) {
        // Convert the char(14) string to LocalDateTime
        LocalDateTime transactionDate = LocalDateTime.parse(
                lastTransDate,
                DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        );

        // Get current time in Malaysia timezone
        ZonedDateTime nowMalaysia = ZonedDateTime.now(ZoneId.of("Asia/Kuala_Lumpur"));

        // Get date 4 months ago
        ZonedDateTime fourMonthsAgo = nowMalaysia.minusMonths(4);

        // Compare dates
        return transactionDate.atZone(ZoneId.of("Asia/Kuala_Lumpur")).isBefore(fourMonthsAgo);
    }

    public TierEntity findNextLowerTier(List<TierEntity> tierEntityList, Integer currentTierLevel) {
        return tierEntityList.stream()
                .filter(tier -> tier.getTierLevel() < currentTierLevel)  // get all lower tiers
                .max(Comparator.comparing(TierEntity::getTierLevel))     // get the highest among lower tiers
                .orElse(null);  // return null if no lower tier exists
    }

    public String getCurrentFormattedDate() {
        // Get current time in Malaysia timezone
        ZonedDateTime nowMalaysia = ZonedDateTime.now(ZoneId.of("Asia/Kuala_Lumpur"));

        // Format it to match your char(14) format
        return nowMalaysia.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
}
