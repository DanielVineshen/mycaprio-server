package org.everowl.core.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.storeCustomer.response.StoreCustomerRes;
import org.everowl.core.service.service.StoreCustomerDomain;
import org.everowl.core.service.service.shared.StoreCustomerService;
import org.everowl.database.service.entity.CustomerEntity;
import org.everowl.database.service.entity.StoreCustomerEntity;
import org.everowl.database.service.entity.StoreEntity;
import org.everowl.database.service.repository.CustomerRepository;
import org.everowl.database.service.repository.StoreRepository;
import org.everowl.shared.service.exception.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import static org.everowl.shared.service.enums.ErrorCode.STORE_NOT_EXIST;
import static org.everowl.shared.service.enums.ErrorCode.USER_NOT_EXIST;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreCustomerImpl implements StoreCustomerDomain {
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final ModelMapper modelMapper;
    private final StoreCustomerService storeCustomerService;

    @Override
    public StoreCustomerRes getStoreCustomerDetails(Integer storeId, String loginId) {
        CustomerEntity customer = customerRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException(STORE_NOT_EXIST));

        StoreCustomerEntity storeCustomer = storeCustomerService.getOrCreateStoreCustomer(customer, store);

        return modelMapper.map(storeCustomer, StoreCustomerRes.class);
    }
}
