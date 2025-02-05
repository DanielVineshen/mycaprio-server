package org.everowl.core.service.service.shared;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.database.service.entity.CustomerEntity;
import org.everowl.database.service.entity.StoreCustomerEntity;
import org.everowl.database.service.entity.StoreEntity;
import org.everowl.database.service.entity.TierEntity;
import org.everowl.database.service.repository.StoreCustomerRepository;
import org.everowl.database.service.repository.TierRepository;
import org.everowl.shared.service.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

import static org.everowl.shared.service.enums.ErrorCode.TIER_NOT_EXIST;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreCustomerService {
    private final StoreCustomerRepository storeCustomerRepository;
    private final TierRepository tierRepository;

    public StoreCustomerEntity getOrCreateStoreCustomer(CustomerEntity customer, StoreEntity store) {
        Optional<StoreCustomerEntity> storeCustomerEntity = storeCustomerRepository.findCustomerStoreProfile(customer.getCustId(), store.getStoreId());

        return storeCustomerEntity.orElseGet(() -> {
            TierEntity tier = tierRepository.findStoreDefaultTier(store.getStoreId())
                    .orElseThrow(() -> new NotFoundException(TIER_NOT_EXIST));

            StoreCustomerEntity newStoreCustomer = new StoreCustomerEntity();
            newStoreCustomer.setCustomer(customer);
            newStoreCustomer.setStore(store);
            newStoreCustomer.setTier(tier);
            newStoreCustomer.setTierPoints(0);
            newStoreCustomer.setAvailablePoints(0);
            newStoreCustomer.setAccumulatedPoints(0);
            newStoreCustomer.setStoreCustomerVouchers(new ArrayList<>());
            newStoreCustomer.setPointsActivities(new ArrayList<>());
            return storeCustomerRepository.save(newStoreCustomer);
        });
    }
}
