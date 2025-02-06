package org.everowl.core.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.storeCustomer.response.StoreCustomerRes;
import org.everowl.core.service.dto.storeCustomerVoucher.response.StoreCustomerVoucherRes;
import org.everowl.core.service.dto.tier.response.TierRes;
import org.everowl.core.service.service.StoreCustomerDomain;
import org.everowl.database.service.entity.CustomerEntity;
import org.everowl.database.service.entity.StoreCustomerEntity;
import org.everowl.database.service.repository.CustomerRepository;
import org.everowl.database.service.repository.StoreCustomerRepository;
import org.everowl.shared.service.exception.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.everowl.shared.service.enums.ErrorCode.USER_NOT_EXIST;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreCustomerImpl implements StoreCustomerDomain {
    private final StoreCustomerRepository storeCustomerRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    @Override
    public StoreCustomerRes getStoreCustomerDetails(Integer storeId, String loginId) {
        CustomerEntity customer = customerRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        Optional<StoreCustomerEntity> storeCustomerEntity = storeCustomerRepository.findCustomerStoreProfile(customer.getCustId(), storeId);

        if (storeCustomerEntity.isEmpty()) {
            throw new NotFoundException(USER_NOT_EXIST);
        }

        StoreCustomerEntity storeCustomer = storeCustomerEntity.get();

        StoreCustomerRes storeCustomerRes = modelMapper.map(storeCustomer, StoreCustomerRes.class);

        return storeCustomerRes;
    }
}
