package org.everowl.core.service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.store.response.StoreRes;
import org.everowl.core.service.dto.store.response.StoresRes;
import org.everowl.core.service.security.CustomUserDetails;
import org.everowl.core.service.service.StoreDomain;
import org.everowl.shared.service.annotation.ValidInteger;
import org.everowl.shared.service.dto.BaseSuccessResponseBodyModel;
import org.everowl.shared.service.enums.UserType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StoreController {
    private final StoreDomain storeDomain;

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
        List<StoresRes> response = storeDomain.getStores();

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/public/store")
    public ResponseEntity<BaseSuccessResponseBodyModel> getStoreDetails(@Valid @RequestParam(value = "storeId")
                                                                        @ValidInteger(message = "Please ensure a valid store ID is provided")
                                                                        @NotBlank(message = "Please ensure the store ID is not blank") String storeId,
                                                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        StoreRes response = storeDomain.getStoreDetails(Integer.parseInt(storeId));

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}