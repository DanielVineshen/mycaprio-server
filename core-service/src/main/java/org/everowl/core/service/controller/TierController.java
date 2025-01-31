package org.everowl.core.service.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.service.TierDomain;
import org.everowl.shared.service.dto.BaseSuccessResponseBodyModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class TierController {
    private final TierDomain tierDomain;

//    @GetMapping(value = "/tiers")
//    public ResponseEntity<BaseSuccessResponseBodyModel> getAllTiers(@RequestParam(value = "storeId") @NotBlank(message = "Please ensure the store ID is not blank") String storeId,
//                                                                                  HttpServletRequest request) {
//
//
//
//
//        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
//        return new ResponseEntity<>(responseBody, HttpStatus.OK);
//    }

}
