package org.everowl.core.service.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.auth.request.AuthReq;
import org.everowl.core.service.dto.auth.request.CreateCustomerProfileReq;
import org.everowl.core.service.dto.auth.request.ResetCustomerPasswordReq;
import org.everowl.core.service.dto.auth.response.AuthRes;
import org.everowl.core.service.service.AuthDomain;
import org.everowl.core.service.service.shared.SmsService;
import org.everowl.database.service.entity.*;
import org.everowl.database.service.repository.*;
import org.everowl.core.service.security.CustomUserDetails;
import org.everowl.core.service.security.CustomUserDetailsService;
import org.everowl.core.service.security.JwtTokenProvider;
import org.everowl.shared.service.dto.GenericMessage;
import org.everowl.shared.service.enums.ErrorCode;
import org.everowl.shared.service.enums.UserType;
import org.everowl.shared.service.exception.BadRequestException;
import org.everowl.shared.service.exception.ForbiddenException;
import org.everowl.shared.service.exception.NotFoundException;
import org.everowl.shared.service.util.UniqueIdGenerator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.everowl.shared.service.enums.ErrorCode.*;
import static org.everowl.shared.service.util.JsonConverterUtils.convertObjectToJsonString;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthDomainImpl implements AuthDomain {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;
    private final TokenRepository tokenRepository;
    private final SmsService smsService;
    private final PasswordEncoder passwordEncoder;
    private final StoreRepository storeRepository;
    private final TierRepository tierRepository;
    private final StoreCustomerRepository storeCustomerRepository;
    private final AuditLogRepository auditLogRepository;

    private static final String BEARER = "Bearer ";
    private static final int BEARER_LENGTH = BEARER.length();

    @Override
    public AuthRes authenticate(AuthReq request, String ipAddress, String userAgent) {
        Authentication authentication = authenticateUser(request);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        validateTokens(userDetails.getUsername(), userDetails.getUserType().toString());
        TokenEntity token = generateUserTokens(userDetails, ipAddress, userAgent);

        AuditLogEntity auditLogEntity = new AuditLogEntity();
        auditLogEntity.setLoginId("system");
        auditLogEntity.setPerformedBy("system");
        auditLogEntity.setAuthorityLevel("SYSTEM");
        auditLogEntity.setBeforeChanged(null);
        auditLogEntity.setAfterChanged(convertObjectToJsonString(new Object[]{token}));
        auditLogEntity.setLogType("CREATE_USER_LOGIN");
        auditLogEntity.setLogAction("CREATE");
        auditLogRepository.save(auditLogEntity);

        return createAuthenticationResponse(userDetails, token);
    }

    private Authentication authenticateUser(AuthReq request) {
        try {
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (Exception e) {
            throw new ForbiddenException(INVALID_CREDENTIALS);
        }
    }

    private void validateTokens(String loginId, String userType) {
        List<TokenEntity> tokens = tokenRepository.findAllValidTokensByUser(loginId, userType);
        if (tokens.size() >= 5) {
            tokenRepository.deleteById(tokens.getFirst().getTokenId());
        }
    }

    private TokenEntity generateUserTokens(CustomUserDetails user, String ipAddress, String userAgent) {
        String jwtToken = jwtTokenService.generateToken(user);
        String refreshToken = jwtTokenService.generateRefreshToken(user);
        return saveUserToken(user, jwtToken, ipAddress, userAgent, refreshToken);
    }

    private TokenEntity saveUserToken(CustomUserDetails user, String jwtToken, String ipAddress, String userAgent, String refreshToken) {
        TokenEntity token = TokenEntity.builder()
                .loginId(user.getUsername())
                .userType(user.getUserType().toString())
                .accessToken(jwtToken)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .refreshToken(refreshToken)
                .build();

        return tokenRepository.save(token);
    }

    @Override
    public GenericMessage validateUserToken(HttpServletRequest request) {
        String accessToken = extractTokenFromHeader(request);
        boolean isTokenValid = isAccessTokenValid(accessToken);
        return GenericMessage.builder()
                .status(isTokenValid)
                .build();
    }

    @Override
    public GenericMessage validateCustomerLoginId(String custLoginId) {
        Optional<CustomerEntity> customer = customerRepository.findByUsername(custLoginId);

        boolean status = customer.isEmpty();

        return GenericMessage.builder()
                .status(status)
                .build();
    }

    @Override
    @Transactional
    public GenericMessage createCustomerProfile(CreateCustomerProfileReq createCustomerProfileReq) {
        String loginId = createCustomerProfileReq.getLoginId();
        String password = createCustomerProfileReq.getPassword();
        String smsCode = createCustomerProfileReq.getSmsCode();

        Optional<CustomerEntity> customerCheck = customerRepository.findByUsername(loginId);

        if (loginId != null && password == null && smsCode == null) {
            if (customerCheck.isPresent() && customerCheck.get().getPassword() != null) {
                throw new ForbiddenException(LOGIN_ID_EXIST);
            }

            String datetime = LocalDateTime.now(ZoneId.of("Asia/Kuala_Lumpur")).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String newSmsCode = UniqueIdGenerator.generateSmsCode();
            if (customerCheck.isEmpty()) {
                CustomerEntity newCustomer = new CustomerEntity();
                newCustomer.setCustId(UniqueIdGenerator.generateMyCaprioIdWithTimestamp());
                newCustomer.setLoginId(loginId);
                newCustomer.setSmsCode(newSmsCode);
                newCustomer.setSmsAttempt(1);
                newCustomer.setSmsLastDatetime(datetime);
                newCustomer = customerRepository.save(newCustomer);

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
                storeCustomerRepository.saveAll(storeCustomerEntityList);
            } else {
                SmsService.SmsValidationResult smsValidationResult = smsService.canMakeSmsRequest(customerCheck.get().getSmsLastDatetime(), customerCheck.get().getSmsAttempt());
                log.info(smsValidationResult.getMessage());
                if (smsValidationResult.isAllowed()) {
                    customerCheck.get().setSmsCode(newSmsCode);
                    customerCheck.get().setSmsLastDatetime(datetime);

                    if (smsValidationResult.isShouldResetCounter()) {
                        customerCheck.get().setSmsAttempt(1);
                    } else {
                        customerCheck.get().setSmsAttempt(customerCheck.get().getSmsAttempt() + 1);
                    }

                    customerRepository.save(customerCheck.get());
                } else {
                    throw new ForbiddenException(SMS_QUOTA_REACHED);
                }
            }

            smsService.sendSms(loginId, newSmsCode);
        }

        if (loginId != null && smsCode != null) {
            if (customerCheck.isEmpty()) {
                throw new NotFoundException(USER_NOT_EXIST);
            }

            if (customerCheck.get().getPassword() != null) {
                throw new ForbiddenException(USER_NOT_PERMITTED);
            }

            if (!smsCode.equals(customerCheck.get().getSmsCode())) {
                throw new ForbiddenException(SMS_NOT_VALID);
            }

            if (password != null) {
                String encodedPassword = passwordEncoder.encode(password);
                customerCheck.get().setSmsCode(null);
                customerCheck.get().setPassword(encodedPassword);
                customerCheck.get().setSmsAttempt(0);
                customerRepository.save(customerCheck.get());
            }
        }

        return GenericMessage.builder()
                .status(true)
                .build();
    }

    @Override
    public GenericMessage resetCustomerPassword(ResetCustomerPasswordReq resetCustomerPasswordReq) {
        String loginId = resetCustomerPasswordReq.getLoginId();
        String password = resetCustomerPasswordReq.getPassword();
        String smsCode = resetCustomerPasswordReq.getSmsCode();

        Optional<CustomerEntity> customerCheck = customerRepository.findByUsername(loginId);

        if (loginId != null && password == null && smsCode == null) {
            if (customerCheck.isEmpty() || customerCheck.get().getPassword() == null) {
                throw new ForbiddenException(USER_NOT_EXIST);
            }

            String datetime = LocalDateTime.now(ZoneId.of("Asia/Kuala_Lumpur")).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String newSmsCode = UniqueIdGenerator.generateSmsCode();

            SmsService.SmsValidationResult smsValidationResult = smsService.canMakeSmsRequest(customerCheck.get().getSmsLastDatetime(), customerCheck.get().getSmsAttempt());
            log.info(smsValidationResult.getMessage());
            if (smsValidationResult.isAllowed()) {
                customerCheck.get().setSmsCode(newSmsCode);
                customerCheck.get().setSmsLastDatetime(datetime);

                if (smsValidationResult.isShouldResetCounter()) {
                    customerCheck.get().setSmsAttempt(1);
                } else {
                    customerCheck.get().setSmsAttempt(customerCheck.get().getSmsAttempt() + 1);
                }

                customerRepository.save(customerCheck.get());
            } else {
                throw new ForbiddenException(SMS_QUOTA_REACHED);
            }

            smsService.sendSms(loginId, newSmsCode);
        }

        if (loginId != null && smsCode != null) {
            if (customerCheck.isEmpty()) {
                throw new NotFoundException(USER_NOT_EXIST);
            }

            if (customerCheck.get().getPassword() == null) {
                throw new ForbiddenException(USER_NOT_PERMITTED);
            }

            if (!smsCode.equals(customerCheck.get().getSmsCode())) {
                throw new ForbiddenException(SMS_NOT_VALID);
            }

            if (password != null) {
                String encodedPassword = passwordEncoder.encode(password);
                customerCheck.get().setSmsCode(null);
                customerCheck.get().setPassword(encodedPassword);
                customerCheck.get().setSmsAttempt(0);
                customerRepository.save(customerCheck.get());
            }
        }

        return GenericMessage.builder()
                .status(true)
                .build();
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            throw new BadRequestException(MISSING_AUTH_TOKEN_HEADER);
        }
        return authHeader.substring(BEARER_LENGTH);
    }

    private boolean isAccessTokenValid(String accessToken) {
        try {
            String username = jwtTokenService.extractUsername(accessToken);
            UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
            Optional<TokenEntity> tokenOptional = tokenRepository.findByAccessToken(accessToken);
            return jwtTokenService.isTokenValid(accessToken, userDetails) && tokenOptional.isPresent();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public AuthRes refreshToken(HttpServletRequest request) {
        String refreshToken = extractTokenFromHeader(request);
        TokenEntity token = validateRefreshToken(refreshToken);

        CustomUserDetails userDetails;
        if (UserType.valueOf(token.getUserType()) == UserType.CUSTOMER) {
            CustomerEntity customer = customerRepository.findByUsername(token.getLoginId())
                    .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));
            userDetails = new CustomUserDetails(customer, UserType.CUSTOMER);
        } else {
            AdminEntity staff = adminRepository.findByUsername(token.getLoginId())
                    .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));
            userDetails = new CustomUserDetails(staff, UserType.STAFF);
        }

        String accessToken = jwtTokenService.generateToken(userDetails);
        token.setAccessToken(accessToken);
        tokenRepository.save(token);

        return createAuthenticationResponse(userDetails, token);
    }

    private TokenEntity validateRefreshToken(String refreshToken) {
        Optional<TokenEntity> token = tokenRepository.findByRefreshToken(refreshToken);
        if (token.isEmpty()) {
            throw new ForbiddenException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        return token.get();
    }

    private AuthRes createAuthenticationResponse(CustomUserDetails user, TokenEntity token) {
        AuthRes authRes = AuthRes.builder()
                .tokenId(token.getTokenId())
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .loginId(user.getUsername())
                .userType(user.getUserType().toString())
                .fullName(user.getFullName())
                .build();

        if (user.getUserType().toString().equals("CUSTOMER")) {
            Optional<CustomerEntity> customer = customerRepository.findByUsername(user.getUsername());
            if (customer.isPresent()) {
                authRes.setCustId(customer.get().getCustId());
                authRes.setEmailAddress(customer.get().getEmailAddress());
                authRes.setGender(customer.get().getGender());
                String dob = customer.get().getDateOfBirth();
                String formattedDob = dob.substring(0, 4) + "-" + dob.substring(4, 6) + "-" + dob.substring(6, 8);
                authRes.setDateOfBirth(formattedDob);
            }
        }

        return authRes;
    }
}