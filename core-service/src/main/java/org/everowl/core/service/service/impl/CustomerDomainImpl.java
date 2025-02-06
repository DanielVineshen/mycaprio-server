package org.everowl.core.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.customer.request.UpdateCustomerPasswordReq;
import org.everowl.core.service.dto.customer.request.UpdateCustomerProfileReq;
import org.everowl.core.service.dto.customer.response.CustomerProfileRes;
import org.everowl.core.service.service.CustomerDomain;
import org.everowl.database.service.entity.CustomerEntity;
import org.everowl.database.service.repository.CustomerRepository;
import org.everowl.shared.service.dto.GenericMessage;
import org.everowl.shared.service.exception.BadRequestException;
import org.everowl.shared.service.exception.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static org.everowl.shared.service.enums.ErrorCode.INVALID_CREDENTIALS;
import static org.everowl.shared.service.enums.ErrorCode.USER_NOT_EXIST;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerDomainImpl implements CustomerDomain {
    private final BCryptPasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    @Override
    public CustomerProfileRes getCustomerProfile(String loginId) {
        CustomerEntity customer = customerRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        return modelMapper.map(customer, CustomerProfileRes.class);
    }

    @Override
    public GenericMessage updateCustomerProfile(String loginId, UpdateCustomerProfileReq updateCustomerProfileReq) {
        CustomerEntity customer = customerRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        customer.setEmailAddress(updateCustomerProfileReq.getEmailAddress());
        customer.setFullName(updateCustomerProfileReq.getFullName());
        customer.setGender(updateCustomerProfileReq.getGender());
        customer.setDateOfBirth(updateCustomerProfileReq.getDateOfBirth());

        customerRepository.save(customer);

        return GenericMessage.builder()
                .status(true)
                .build();
    }

    @Override
    public GenericMessage updateCustomerPassword(String loginId, UpdateCustomerPasswordReq updateCustomerPasswordReq) {
        CustomerEntity customer = customerRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        boolean matches = passwordEncoder.matches(updateCustomerPasswordReq.getOldPassword(), customer.getPassword());

        if (!matches) {
            throw new BadRequestException(INVALID_CREDENTIALS);
        }

        String hashedPassword = passwordEncoder.encode(updateCustomerPasswordReq.getNewPassword());

        customer.setPassword(hashedPassword);

        customerRepository.save(customer);

        return GenericMessage.builder()
                .status(true)
                .build();
    }
}
