package org.everowl.core.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.customer.CustomerProfile;
import org.everowl.core.service.service.CustomerDomain;
import org.everowl.database.service.entity.CustomerEntity;
import org.everowl.database.service.repository.CustomerRepository;
import org.everowl.shared.service.exception.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import static org.everowl.shared.service.enums.ErrorCode.USER_NOT_EXIST;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerDomainImpl implements CustomerDomain {
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    @Override
    public CustomerProfile getCustomerProfile(String profileId) {
        CustomerEntity customer = customerRepository.findByUsername(profileId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        return modelMapper.map(customer, CustomerProfile.class);
    }
}
