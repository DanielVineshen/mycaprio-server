package org.everowl.core.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.customer.request.CreateCustomerProfileReq;
import org.everowl.core.service.dto.customer.request.ResetCustomerPasswordReq;
import org.everowl.core.service.dto.customer.request.UpdateCustomerPasswordReq;
import org.everowl.core.service.dto.customer.request.UpdateCustomerProfileReq;
import org.everowl.core.service.dto.customer.response.CustomerProfileRes;
import org.everowl.core.service.service.CustomerDomain;
import org.everowl.database.service.entity.*;
import org.everowl.database.service.repository.*;
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
import static org.everowl.shared.service.util.JsonConverterUtils.convertObjectToJsonString;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerDomainImpl implements CustomerDomain {
    private final BCryptPasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final TierRepository tierRepository;
    private final StoreCustomerRepository storeCustomerRepository;
    private final AuditLogRepository auditLogRepository;
    private final ModelMapper modelMapper;
    private final AuditLogRepository auditLogRepository;

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

        String beforeChanged = convertObjectToJsonString(customer);

        customer.setEmailAddress(updateCustomerProfileReq.getEmailAddress());
        customer.setFullName(updateCustomerProfileReq.getFullName());
        customer.setGender(updateCustomerProfileReq.getGender());
        if (customer.getDateOfBirth() == null) {
            customer.setDateOfBirth(updateCustomerProfileReq.getDateOfBirth());
        }

        CustomerEntity savedCustomer = customerRepository.save(customer);

        String afterChanged = convertObjectToJsonString(savedCustomer);

        AuditLogEntity auditLogEntity = new AuditLogEntity();
        auditLogEntity.setLoginId(customer.getLoginId());
        auditLogEntity.setPerformedBy(customer.getFullName());
        auditLogEntity.setAuthorityLevel("CUSTOMER");
        auditLogEntity.setBeforeChanged(beforeChanged);
        auditLogEntity.setAfterChanged(afterChanged);
        auditLogEntity.setLogType("UPDATE_CUSTOMER_PROFILE");
        auditLogEntity.setLogAction("UPDATE");
        auditLogRepository.save(auditLogEntity);

        return GenericMessage.builder()
                .status(true)
                .build();
    }

    @Override
    public GenericMessage updateCustomerPassword(String loginId, UpdateCustomerPasswordReq updateCustomerPasswordReq) {
        CustomerEntity customer = customerRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        String beforeChanged = convertObjectToJsonString(customer);

        boolean matches = passwordEncoder.matches(updateCustomerPasswordReq.getOldPassword(), customer.getPassword());

        if (!matches) {
            throw new BadRequestException(INVALID_CREDENTIALS);
        }

        String hashedPassword = passwordEncoder.encode(updateCustomerPasswordReq.getNewPassword());

        customer.setPassword(hashedPassword);

        CustomerEntity savedCustomer = customerRepository.save(customer);

        String afterChanged = convertObjectToJsonString(savedCustomer);

        AuditLogEntity auditLogEntity = new AuditLogEntity();
        auditLogEntity.setLoginId(customer.getLoginId());
        auditLogEntity.setPerformedBy(customer.getFullName());
        auditLogEntity.setAuthorityLevel("CUSTOMER");
        auditLogEntity.setBeforeChanged(beforeChanged);
        auditLogEntity.setAfterChanged(afterChanged);
        auditLogEntity.setLogType("UPDATE_CUSTOMER_PASSWORD");
        auditLogEntity.setLogAction("UPDATE");
        auditLogRepository.save(auditLogEntity);

        return GenericMessage.builder()
                .status(true)
                .build();
    }

    @Override
    @Transactional
    public GenericMessage createCustomerProfile(CreateCustomerProfileReq createCustomerProfileReq, String adminLoginId) {
        String loginId = createCustomerProfileReq.getLoginId();
        String password = createCustomerProfileReq.getPassword();
        String encodedPassword = passwordEncoder.encode(password);

        AdminEntity admin = adminRepository.findByUsername(adminLoginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_AUTHORIZED));

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

        newCustomer.setStoreCustomers(storeCustomerEntityList);
        customerRepository.save(newCustomer);

        AuditLogEntity auditLogEntity = new AuditLogEntity();
        auditLogEntity.setLoginId(adminLoginId);
        auditLogEntity.setPerformedBy(admin.getFullName());
        auditLogEntity.setAuthorityLevel("ADMIN");
        auditLogEntity.setBeforeChanged(null);
        auditLogEntity.setAfterChanged(convertObjectToJsonString(new Object[]{newCustomer}));
        auditLogEntity.setLogType("CREATE_STORE_CUSTOMER_BY_ADMIN");
        auditLogEntity.setLogAction("CREATE");
        auditLogEntity.setLogDesc("A store customer was manually created by an admin");

        auditLogRepository.save(auditLogEntity);

        return GenericMessage.builder()
                .status(true)
                .build();
    }

    @Override
    @Transactional
    public GenericMessage resetCustomerPassword(ResetCustomerPasswordReq resetCustomerPasswordReq, String adminLoginId) {
        String loginId = resetCustomerPasswordReq.getLoginId();
        String password = resetCustomerPasswordReq.getNewPassword();
        String encodedPassword = passwordEncoder.encode(password);

        AdminEntity admin = adminRepository.findByUsername(adminLoginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_AUTHORIZED));

        Optional<CustomerEntity> customerCheck = customerRepository.findByUsername(loginId);
        if (customerCheck.isEmpty()) {
            throw new BadRequestException(USER_NOT_EXIST);
        }

        //Save original copy for audit log
        String beforeChange = convertObjectToJsonString(new Object[]{customerCheck.get()});

        customerCheck.get().setPassword(encodedPassword);
        customerRepository.save(customerCheck.get());

        AuditLogEntity auditLogEntity = new AuditLogEntity();
        auditLogEntity.setLoginId(adminLoginId);
        auditLogEntity.setPerformedBy(admin.getFullName());
        auditLogEntity.setAuthorityLevel("OWNER");
        auditLogEntity.setBeforeChanged(convertObjectToJsonString(new Object[]{beforeChange}));
        auditLogEntity.setAfterChanged(convertObjectToJsonString(new Object[]{customerCheck}));
        auditLogEntity.setLogType("UPDATE_CUSTOMER_PASSWORD_BY_OWNER");
        auditLogEntity.setLogAction("UPDATE");
        auditLogEntity.setLogDesc("A customer account password was manually updated by an owner");

        auditLogRepository.save(auditLogEntity);

        return GenericMessage.builder()
                .status(true)
                .build();
    }
}
