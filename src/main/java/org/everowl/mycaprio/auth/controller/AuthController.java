package org.everowl.mycaprio.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.mycaprio.auth.dto.AuthResponse;
import org.everowl.mycaprio.auth.dto.LoginRequest;
import org.everowl.mycaprio.database.entity.TokenEntity;
import org.everowl.mycaprio.database.repository.CustomerRepository;
import org.everowl.mycaprio.database.repository.StaffRepository;
import org.everowl.mycaprio.database.repository.TokenRepository;
import org.everowl.mycaprio.shared.dto.BaseSuccessResponseBodyModel;
import org.everowl.mycaprio.shared.enums.UserType;
import org.everowl.mycaprio.shared.exception.NotFoundException;
import org.everowl.mycaprio.shared.service.CustomUserDetails;
import org.everowl.mycaprio.shared.service.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.everowl.mycaprio.shared.enums.ErrorCode.USER_NOT_EXIST;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;

    @PostMapping("/login")
    public ResponseEntity<BaseSuccessResponseBodyModel> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String token = jwtTokenProvider.generateToken(userDetails);

            revokeAllUserTokens(userDetails.getUsername(), userDetails.getUserType());
            saveToken(userDetails.getUsername(), userDetails.getUserType(), token);

            AuthResponse response = AuthResponse.builder().token(token).build();
            BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new NotFoundException(USER_NOT_EXIST);
        }
    }

    private void saveToken(String loginId, UserType userType, String jwtToken) {
        TokenEntity token = TokenEntity.builder()
                .loginId(loginId)
                .userType(userType.toString())
                .accessToken(jwtToken)
                .refreshToken(jwtToken)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(String loginId, UserType userType) {
        List<TokenEntity> validTokens = tokenRepository.findAllValidTokensByUser(loginId, userType.toString());
        if (validTokens.isEmpty()) return;
        tokenRepository.deleteAll(validTokens);
    }
}