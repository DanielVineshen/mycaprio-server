package org.everowl.core.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.storeCustomerVoucher.response.StoreCustomerVoucherRes;
import org.everowl.core.service.service.StoreCustomerVoucherDomain;
import org.everowl.database.service.entity.CustomerEntity;
import org.everowl.database.service.entity.StoreCustomerEntity;
import org.everowl.database.service.entity.StoreCustomerVoucherEntity;
import org.everowl.database.service.repository.CustomerRepository;
import org.everowl.database.service.repository.StoreCustomerRepository;
import org.everowl.database.service.repository.StoreCustomerVoucherRepository;
import org.everowl.shared.service.exception.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.everowl.shared.service.enums.ErrorCode.USER_NOT_EXIST;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreCustomerVoucherImpl implements StoreCustomerVoucherDomain {
    private final StoreCustomerVoucherRepository storeCustomerVoucherRepository;
    private final CustomerRepository customerRepository;
    private final StoreCustomerRepository storeCustomerRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<StoreCustomerVoucherRes> getCustomerVoucher(Integer storeId, String loginId) {
        CustomerEntity customer = customerRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        Optional<StoreCustomerEntity> storeCustomerEntity = storeCustomerRepository.findCustomerStoreProfile(customer.getCustId(), storeId);

        if (storeCustomerEntity.isEmpty()) {
            throw new NotFoundException(USER_NOT_EXIST);
        }

        List<StoreCustomerVoucherEntity> storeVouchers = storeCustomerVoucherRepository.findByStoreCustId(storeCustomerEntity.get().getStoreCustId());

        List<StoreCustomerVoucherRes> storeCustomerVoucherResList = new ArrayList<>();
        for (StoreCustomerVoucherEntity storeCustomerVoucherEntity: storeVouchers) {
            StoreCustomerVoucherRes storeCustomerVoucherRes = modelMapper.map(storeCustomerVoucherEntity, StoreCustomerVoucherRes.class);
            storeCustomerVoucherResList.add(storeCustomerVoucherRes);
        }

        return storeCustomerVoucherResList;
    }
}
