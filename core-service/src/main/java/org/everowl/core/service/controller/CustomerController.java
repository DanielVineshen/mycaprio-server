package org.everowl.core.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.customer.request.UpdateCustomerPassword;
import org.everowl.core.service.dto.customer.request.UpdateCustomerProfile;
import org.everowl.core.service.dto.customer.response.CustomerProfile;
import org.everowl.core.service.security.CustomUserDetails;
import org.everowl.core.service.service.CustomerDomain;
import org.everowl.shared.service.dto.BaseSuccessResponseBodyModel;
import org.everowl.shared.service.dto.GenericMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class CustomerController {
    private final CustomerDomain customerDomain;

    @GetMapping(value = "/customer/profile")
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> getCustomerProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String loginId = userDetails.getUsername();

        CustomerProfile response = customerDomain.getCustomerProfile(loginId);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PutMapping(value = "/customer/profile")
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> updateCustomerProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                            @Valid @RequestBody UpdateCustomerProfile updateCustomerProfile) {
        String loginId = userDetails.getUsername();

        GenericMessage response = customerDomain.updateCustomerProfile(loginId, updateCustomerProfile);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PutMapping(value = "/customer/password")
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> updateCustomerPassword(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                            @Valid @RequestBody UpdateCustomerPassword updateCustomerPassword) {
        String loginId = userDetails.getUsername();

        GenericMessage response = customerDomain.updateCustomerPassword(loginId, updateCustomerPassword);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
