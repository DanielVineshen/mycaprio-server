package org.everowl.core.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.security.CustomUserDetails;
import org.everowl.shared.service.dto.BaseSuccessResponseBodyModel;
import org.everowl.shared.service.enums.UserType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StoreController {
    @GetMapping("/info")
    public ResponseEntity<BaseSuccessResponseBodyModel> info(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String loginId = userDetails.getUsername();
        String fullName = userDetails.getFullName();
        UserType userType = userDetails.getUserType();
        log.info("loginId: {}", loginId);
        log.info("userType: {}", userType);
        log.info("fullName: {}", fullName);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(true);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/public/stores")
    public ResponseEntity<BaseSuccessResponseBodyModel> getAllStores(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String username = userDetails.getUsername();
        String fullName = userDetails.getFullName();
        UserType userType = userDetails.getUserType();
        log.info("username: {}", username);
        log.info("userType: {}", userType);
        log.info("fullName: {}", fullName);



        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(true);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}