package org.everowl.core.service.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.auth.request.AuthReq;
import org.everowl.core.service.dto.auth.request.CreateCustomerProfileReq;
import org.everowl.core.service.dto.auth.request.ResetCustomerPasswordReq;
import org.everowl.core.service.dto.auth.response.AuthRes;
import org.everowl.core.service.service.AuthDomain;
import org.everowl.shared.service.dto.BaseSuccessResponseBodyModel;
import org.everowl.shared.service.dto.GenericMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthDomain authenticationDomain;

    @PostMapping(path = "/token", produces = {
            MediaType.APPLICATION_JSON_VALUE
    })
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> getToken(@Valid @RequestBody AuthReq authRequest,
                                                                               HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        // Authenticate the user and generate a token
        AuthRes response = authenticationDomain.authenticate(authRequest, ipAddress, userAgent);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping(value = "/refreshToken", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> refreshToken(HttpServletRequest request) {
        // Refresh the authentication token
        AuthRes response = authenticationDomain.refreshToken(request);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping(value = "/validate/token")
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> validateToken(HttpServletRequest request) {
        // Validate the user token
        GenericMessage response = authenticationDomain.validateUserToken(request);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping(value = "/validate/customer/loginId")
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> validateCustomerLoginId(@Valid @RequestParam @NotBlank(message = "Please ensure the field login ID is not blank")
                                                                                              @Size(min = 1, max = 255, message = "Please ensure the field login ID is 1 to 255 characters in length")
                                                                                              String loginId) {
        GenericMessage response = authenticationDomain.validateCustomerLoginId(loginId);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping(value = "/customer/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> createCustomerProfile(@Valid @RequestBody CreateCustomerProfileReq createCustomerProfileReq) {
        // Refresh the authentication token
        GenericMessage response = authenticationDomain.createCustomerProfile(createCustomerProfileReq);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PutMapping(value = "/customer/password", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> resetCustomerPassword(@Valid @RequestBody ResetCustomerPasswordReq resetCustomerPasswordReq) {
        // Refresh the authentication token
        GenericMessage response = authenticationDomain.resetCustomerPassword(resetCustomerPasswordReq);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}