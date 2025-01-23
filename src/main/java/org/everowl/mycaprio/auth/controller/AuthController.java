package org.everowl.mycaprio.auth.controller;

import lombok.RequiredArgsConstructor;
import org.everowl.mycaprio.auth.dto.AuthResponse;
import org.everowl.mycaprio.auth.dto.LoginRequest;
import org.everowl.mycaprio.shared.enums.UserType;
import org.everowl.mycaprio.shared.service.CustomUserDetails;
import org.everowl.mycaprio.shared.service.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String token = jwtTokenProvider.generateToken(userDetails);

            revokeAllUserTokens(userDetails.getUsername(), userDetails.getUserType());
            saveToken(userDetails.getUsername(), userDetails.getUserType(), token);

            return ResponseEntity.ok(new AuthResponse(token));

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password");
        }
    }

    private void saveToken(String loginId, UserType userType, String jwtToken) {
        TokenEntity token = TokenEntity.builder()
                .loginId(loginId)
                .userType(userType)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(String loginId, UserType userType) {
        var validTokens = tokenRepository.findAllValidTokensByUser(loginId, userType);
        if (validTokens.isEmpty()) return;

        validTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validTokens);
    }
}