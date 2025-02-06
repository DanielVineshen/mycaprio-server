package org.everowl.core.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.storeCustomerVoucher.response.StoreCustomerVoucherDetailsRes;
import org.everowl.core.service.dto.storeCustomerVoucher.response.StoreCustomerVoucherRes;
import org.everowl.core.service.service.StoreCustomerVoucherDomain;
import org.everowl.core.service.service.shared.StoreCustomerService;
import org.everowl.database.service.entity.CustomerEntity;
import org.everowl.database.service.entity.StoreCustomerEntity;
import org.everowl.database.service.entity.StoreCustomerVoucherEntity;
import org.everowl.database.service.entity.StoreEntity;
import org.everowl.database.service.repository.CustomerRepository;
import org.everowl.database.service.repository.StoreCustomerVoucherRepository;
import org.everowl.database.service.repository.StoreRepository;
import org.everowl.shared.service.exception.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.everowl.shared.service.enums.ErrorCode.STORE_NOT_EXIST;
import static org.everowl.shared.service.enums.ErrorCode.USER_NOT_EXIST;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreCustomerVoucherImpl implements StoreCustomerVoucherDomain {
    private final StoreCustomerVoucherRepository storeCustomerVoucherRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final ModelMapper modelMapper;
    private final StoreCustomerService storeCustomerService;

    @Override
    public StoreCustomerVoucherRes getCustomerVoucher(Integer storeId, String loginId) {
        CustomerEntity customer = customerRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException(STORE_NOT_EXIST));

        StoreCustomerEntity storeCustomerEntity = storeCustomerService.getOrCreateStoreCustomer(customer, store);

        List<StoreCustomerVoucherEntity> storeVouchers = storeCustomerVoucherRepository.findByStoreCustId(storeCustomerEntity.getStoreCustId());

        List<StoreCustomerVoucherDetailsRes> storeCustomerVoucherDetailsResList = new ArrayList<>();
        for (StoreCustomerVoucherEntity storeCustomerVoucherEntity : storeVouchers) {
            StoreCustomerVoucherDetailsRes storeCustomerVoucherDetailsRes = modelMapper.map(storeCustomerVoucherEntity, StoreCustomerVoucherDetailsRes.class);
            storeCustomerVoucherDetailsResList.add(storeCustomerVoucherDetailsRes);
        }

        StoreCustomerVoucherRes vouchers = new StoreCustomerVoucherRes();
        vouchers.setVouchers(storeCustomerVoucherDetailsResList);

        return vouchers;
    }
}
