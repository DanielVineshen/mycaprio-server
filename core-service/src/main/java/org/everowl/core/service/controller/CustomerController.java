package org.everowl.core.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.customer.CustomerProfile;
import org.everowl.core.service.security.CustomUserDetails;
import org.everowl.core.service.service.CustomerDomain;
import org.everowl.shared.service.dto.BaseSuccessResponseBodyModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerDomain customerDomain;

    @GetMapping(value = "/profile")
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> getCustomerProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String loginId = userDetails.getUsername();

        // Validate the user token
        CustomerProfile response = customerDomain.getCustomerProfile(loginId);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
