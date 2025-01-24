package org.everowl.mycaprio.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.mycaprio.database.repository.CustomerRepository;
import org.everowl.mycaprio.database.repository.AdminRepository;
import org.everowl.mycaprio.database.repository.TokenRepository;
import org.everowl.mycaprio.shared.dto.BaseSuccessResponseBodyModel;
import org.everowl.mycaprio.shared.enums.UserType;
import org.everowl.mycaprio.shared.security.CustomUserDetails;
import org.everowl.mycaprio.shared.service.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StoreController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;

    @GetMapping("/info")
    public ResponseEntity<BaseSuccessResponseBodyModel> info(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String username = userDetails.getUsername();
        UserType userType = userDetails.getUserType();
        log.info("username: {}", username);
        log.info("userType: {}", userType);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(true);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}