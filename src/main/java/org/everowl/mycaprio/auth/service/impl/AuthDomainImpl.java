package org.everowl.mycaprio.auth.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.mycaprio.auth.dto.AuthenticationRequest;
import org.everowl.mycaprio.auth.dto.AuthenticationResponse;
import org.everowl.mycaprio.auth.service.AuthDomain;
import org.everowl.mycaprio.database.entity.CustomerEntity;
import org.everowl.mycaprio.database.entity.AdminEntity;
import org.everowl.mycaprio.database.entity.TokenEntity;
import org.everowl.mycaprio.database.repository.CustomerRepository;
import org.everowl.mycaprio.database.repository.AdminRepository;
import org.everowl.mycaprio.database.repository.TokenRepository;
import org.everowl.mycaprio.shared.dto.GenericMessage;
import org.everowl.mycaprio.shared.enums.ErrorCode;
import org.everowl.mycaprio.shared.enums.UserType;
import org.everowl.mycaprio.shared.exception.BadRequestException;
import org.everowl.mycaprio.shared.exception.ForbiddenException;
import org.everowl.mycaprio.shared.exception.NotFoundException;
import org.everowl.mycaprio.shared.security.CustomUserDetails;
import org.everowl.mycaprio.shared.security.CustomUserDetailsService;
import org.everowl.mycaprio.shared.service.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.everowl.mycaprio.shared.enums.ErrorCode.USER_NOT_EXIST;

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

    private static final String BEARER = "Bearer ";
    private static final int BEARER_LENGTH = BEARER.length();

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request, String ipAddress, String userAgent) {
        Authentication authentication = authenticateUser(request);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        validateTokens(userDetails.getUsername(), userDetails.getUserType().toString());
        TokenEntity token = generateUserTokens(userDetails, ipAddress, userAgent);

        return createAuthenticationResponse(userDetails, token);
    }

    private Authentication authenticateUser(AuthenticationRequest request) {
        try {
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (Exception e) {
            throw new ForbiddenException(ErrorCode.INVALID_CREDENTIALS);
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

    private String extractTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            throw new BadRequestException(ErrorCode.MISSING_AUTH_TOKEN_HEADER);
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
    public AuthenticationResponse refreshToken(HttpServletRequest request) {
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

    private AuthenticationResponse createAuthenticationResponse(CustomUserDetails user, TokenEntity token) {
        return AuthenticationResponse.builder()
                .tokenId(token.getTokenId())
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .loginId(user.getUsername())
                .userType(user.getUserType().toString())
                .fullName(user.getFullName())
                .build();
    }
}