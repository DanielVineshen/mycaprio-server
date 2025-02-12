package org.everowl.core.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.customer.request.CreateCustomerProfileReq;
import org.everowl.core.service.dto.customer.request.ResetCustomerPasswordReq;
import org.everowl.core.service.dto.customer.request.UpdateCustomerPasswordReq;
import org.everowl.core.service.dto.customer.request.UpdateCustomerProfileReq;
import org.everowl.core.service.dto.customer.response.CustomerProfileRes;
import org.everowl.core.service.service.CustomerDomain;
import org.everowl.database.service.entity.CustomerEntity;
import org.everowl.database.service.entity.StoreCustomerEntity;
import org.everowl.database.service.entity.StoreEntity;
import org.everowl.database.service.entity.TierEntity;
import org.everowl.database.service.repository.CustomerRepository;
import org.everowl.database.service.repository.StoreCustomerRepository;
import org.everowl.database.service.repository.StoreRepository;
import org.everowl.database.service.repository.TierRepository;
import org.everowl.shared.service.dto.GenericMessage;
import org.everowl.shared.service.exception.BadRequestException;
import org.everowl.shared.service.exception.NotFoundException;
import org.everowl.shared.service.util.UniqueIdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.everowl.shared.service.enums.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerDomainImpl implements CustomerDomain {
    private final BCryptPasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final TierRepository tierRepository;
    private final StoreCustomerRepository storeCustomerRepository;
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
        if (customer.getDateOfBirth() == null) {
            customer.setDateOfBirth(updateCustomerProfileReq.getDateOfBirth());
        }

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

    @Override
    @Transactional
    public GenericMessage createCustomerProfile(CreateCustomerProfileReq createCustomerProfileReq) {
        String loginId = createCustomerProfileReq.getLoginId();
        String password = createCustomerProfileReq.getPassword();
        String encodedPassword = passwordEncoder.encode(password);

        // Check if customer exists
        Optional<CustomerEntity> customerCheck = customerRepository.findByUsername(loginId);
        if (customerCheck.isPresent()) {
            throw new BadRequestException(LOGIN_ID_EXIST);
        }

        // Generate ID and verify it's unique
        String custId = UniqueIdGenerator.generateMyCaprioIdWithTimestamp();
        while (customerRepository.findById(custId).isPresent()) {
            custId = UniqueIdGenerator.generateMyCaprioIdWithTimestamp();
        }

        // Create and save customer first
        CustomerEntity newCustomer = new CustomerEntity();
        newCustomer.setCustId(custId);
        newCustomer.setLoginId(loginId);
        newCustomer.setPassword(encodedPassword);
        newCustomer.setSmsAttempt(0);

        // Save and flush to ensure the customer is persisted
        newCustomer = customerRepository.saveAndFlush(newCustomer);

        // Create store customers in a separate transaction
        List<StoreCustomerEntity> storeCustomerEntityList = new ArrayList<>();
        List<StoreEntity> storeEntityList = storeRepository.findAll();

        for (StoreEntity store : storeEntityList) {
            Optional<TierEntity> tier = tierRepository.findStoreDefaultTier(store.getStoreId());
            if (tier.isPresent()) {
                StoreCustomerEntity storeCustomer = new StoreCustomerEntity();
                storeCustomer.setCustomer(newCustomer);
                storeCustomer.setStore(store);
                storeCustomer.setTier(tier.get());
                storeCustomer.setTierPoints(0);
                storeCustomer.setAvailablePoints(0);
                storeCustomer.setAccumulatedPoints(0);
                storeCustomer.setStoreCustomerVouchers(new ArrayList<>());
                storeCustomer.setPointsActivities(new ArrayList<>());
                storeCustomerEntityList.add(storeCustomer);
            }
        }

        // Save all store customers
        storeCustomerRepository.saveAll(storeCustomerEntityList);

        return GenericMessage.builder()
                .status(true)
                .build();
    }

    @Override
    public GenericMessage resetCustomerPassword(ResetCustomerPasswordReq resetCustomerPasswordReq) {
        String loginId = resetCustomerPasswordReq.getLoginId();
        String password = resetCustomerPasswordReq.getPassword();
        String encodedPassword = passwordEncoder.encode(password);

        Optional<CustomerEntity> customerCheck = customerRepository.findByUsername(loginId);
        if (customerCheck.isEmpty()) {
            throw new BadRequestException(USER_NOT_EXIST);
        }

        customerCheck.get().setPassword(encodedPassword);
        customerRepository.save(customerCheck.get());

        return GenericMessage.builder()
                .status(true)
                .build();
    }
}
