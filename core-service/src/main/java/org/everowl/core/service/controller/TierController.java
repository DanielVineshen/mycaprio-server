package org.everowl.core.service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.tier.response.TierRes;
import org.everowl.core.service.service.TierDomain;
import org.everowl.shared.service.annotation.ValidInteger;
import org.everowl.shared.service.dto.BaseSuccessResponseBodyModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TierController {
    private final TierDomain tierDomain;

    @GetMapping(value = "/public/tiers")
    public ResponseEntity<BaseSuccessResponseBodyModel> getAllTiers(@Valid @RequestParam(value = "storeId")
                                                                    @ValidInteger(message = "Please ensure a valid store ID is provided")
                                                                    @NotBlank(message = "Please ensure the store ID is not blank") String storeId) {

        List<TierRes> response = tierDomain.getAllTiers(Integer.parseInt(storeId));

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
