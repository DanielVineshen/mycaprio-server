package org.everowl.core.service.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.storeCustomerVoucher.response.StoreCustomerVoucherRes;
import org.everowl.core.service.security.CustomUserDetails;
import org.everowl.core.service.service.StoreCustomerVoucherDomain;
import org.everowl.shared.service.annotation.ValidInteger;
import org.everowl.shared.service.dto.BaseSuccessResponseBodyModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class StoreCustomerVoucherController {
    private final StoreCustomerVoucherDomain storeCustomerVoucherDomain;

    @GetMapping("/customer/storeCustomerVouchers")
    public ResponseEntity<BaseSuccessResponseBodyModel> getStoreCustomerVouchers(@RequestParam(value = "storeId")
                                                                                 @ValidInteger(message = "Please ensure a valid store ID is provided")
                                                                                 @NotBlank(message = "Please ensure the store ID is not blank") String storeId,
                                                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        String loginId = userDetails.getUsername();
        List<StoreCustomerVoucherRes> response = storeCustomerVoucherDomain.getCustomerVoucher(Integer.parseInt(storeId), loginId);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
