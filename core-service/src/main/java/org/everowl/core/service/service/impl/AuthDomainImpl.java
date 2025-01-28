package org.everowl.core.service.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.auth.request.AuthReq;
import org.everowl.core.service.dto.auth.response.AuthRes;
import org.everowl.core.service.service.AuthDomain;
import org.everowl.database.service.entity.CustomerEntity;
import org.everowl.database.service.entity.AdminEntity;
import org.everowl.database.service.entity.TokenEntity;
import org.everowl.database.service.repository.CustomerRepository;
import org.everowl.database.service.repository.AdminRepository;
import org.everowl.database.service.repository.TokenRepository;
import org.everowl.core.service.security.CustomUserDetails;
import org.everowl.core.service.security.CustomUserDetailsService;
import org.everowl.core.service.security.JwtTokenProvider;
import org.everowl.shared.service.dto.GenericMessage;
import org.everowl.shared.service.enums.ErrorCode;
import org.everowl.shared.service.enums.UserType;
import org.everowl.shared.service.exception.BadRequestException;
import org.everowl.shared.service.exception.ForbiddenException;
import org.everowl.shared.service.exception.NotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.everowl.shared.service.enums.ErrorCode.*;

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
    public AuthRes authenticate(AuthReq request, String ipAddress, String userAgent) {
        Authentication authentication = authenticateUser(request);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        validateTokens(userDetails.getUsername(), userDetails.getUserType().toString());
        TokenEntity token = generateUserTokens(userDetails, ipAddress, userAgent);

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
        return AuthRes.builder()
                .tokenId(token.getTokenId())
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .loginId(user.getUsername())
                .userType(user.getUserType().toString())
                .fullName(user.getFullName())
                .build();
    }
}