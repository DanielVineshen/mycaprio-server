package org.everowl.core.service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.customer.request.CreateCustomerProfileReq;
import org.everowl.core.service.dto.customer.request.ResetCustomerPasswordReq;
import org.everowl.core.service.dto.customer.request.UpdateCustomerPasswordReq;
import org.everowl.core.service.dto.customer.request.UpdateCustomerProfileReq;
import org.everowl.core.service.dto.customer.response.CustomerProfileRes;
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

        CustomerProfileRes response = customerDomain.getCustomerProfile(loginId);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PutMapping(value = "/customer/profile")
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> updateCustomerProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                            @Valid @RequestBody UpdateCustomerProfileReq updateCustomerProfileReq) {
        String loginId = userDetails.getUsername();

        GenericMessage response = customerDomain.updateCustomerProfile(loginId, updateCustomerProfileReq);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PutMapping(value = "/customer/password")
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> updateCustomerPassword(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                             @Valid @RequestBody UpdateCustomerPasswordReq updateCustomerPasswordReq) {
        String loginId = userDetails.getUsername();

        GenericMessage response = customerDomain.updateCustomerPassword(loginId, updateCustomerPasswordReq);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping(value = "/staff/customer/profile")
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> getACustomerProfile(@Valid @RequestParam @NotBlank(message = "Please ensure the field customer ID is not blank")
                                                                                          @Size(min = 1, max = 255, message = "Please ensure the field customer ID is 1 to 255 characters in length")
                                                                                          String custId) {

        CustomerProfileRes response = customerDomain.getACustomerProfile(custId);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping(value = "/admin/customer/create")
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> createCustomerProfileManual(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                                  @Valid @RequestBody CreateCustomerProfileReq createCustomerProfileReq) {
        String loginId = userDetails.getUsername();

        GenericMessage response = customerDomain.createCustomerProfile(createCustomerProfileReq, loginId);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PutMapping(value = "/owner/customer/password")
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> updateCustomerPasswordManual(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                                   @Valid @RequestBody ResetCustomerPasswordReq resetCustomerPasswordReq) {
        String loginId = userDetails.getUsername();

        GenericMessage response = customerDomain.resetCustomerPassword(resetCustomerPasswordReq, loginId);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
