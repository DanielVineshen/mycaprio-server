package org.everowl.mycaprio.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.mycaprio.auth.dto.AuthenticationRequest;
import org.everowl.mycaprio.auth.dto.AuthenticationResponse;
import org.everowl.mycaprio.auth.service.AuthDomain;
import org.everowl.mycaprio.shared.dto.BaseSuccessResponseBodyModel;
import org.everowl.mycaprio.shared.dto.GenericMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthDomain authenticationDomain;

    @PostMapping(path = "/token", produces = {
            MediaType.APPLICATION_JSON_VALUE
    })
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> getToken(@RequestBody AuthenticationRequest authRequest,
                                                                               HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        // Authenticate the user and generate a token
        AuthenticationResponse response = authenticationDomain.authenticate(authRequest, ipAddress, userAgent);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping(value = "/refreshToken", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> refreshToken(HttpServletRequest request) {
        // Refresh the authentication token
        AuthenticationResponse response = authenticationDomain.refreshToken(request);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping(value = "/validate")
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> validateToken(HttpServletRequest request) {
        // Validate the user token
        GenericMessage response = authenticationDomain.validateUserToken(request);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}