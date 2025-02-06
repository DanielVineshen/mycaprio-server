package org.everowl.core.service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.pointsActivity.request.CreateCustomerPointsAwardManualReq;
import org.everowl.core.service.dto.pointsActivity.request.CreateCustomerPointsAwardScanReq;
import org.everowl.core.service.dto.pointsActivity.response.PointsActivitiesDetailsRes;
import org.everowl.core.service.security.CustomUserDetails;
import org.everowl.core.service.service.PointsActivityDomain;
import org.everowl.shared.service.annotation.ValidInteger;
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
public class PointsActivityController {
    private final PointsActivityDomain pointsActivityDomain;

    @GetMapping(value = "/customer/pointsActivities")
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> getCustomerPointsActivitiesDetails(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                                         @Valid @RequestParam(value = "storeId") @ValidInteger(message = "Please ensure a valid store ID is provided") @NotBlank(message = "Please ensure the store ID is not blank") String storeId) {
        String loginId = userDetails.getUsername();

        PointsActivitiesDetailsRes response = pointsActivityDomain.getCustomerPointsActivitiesDetails(loginId, Integer.valueOf(storeId));

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping(value = "/staff/pointsActivity/scan")
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> createCustomerPointsAwardScan(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                                    @Valid @RequestBody CreateCustomerPointsAwardScanReq createCustomerPointsAwardScanReq) {
        String loginId = userDetails.getUsername();

        GenericMessage response = pointsActivityDomain.createCustomerPointsAwardScan(loginId, createCustomerPointsAwardScanReq);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping(value = "/staff/pointsActivity/manual")
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> createCustomerPointsAwardManual(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                                      @Valid @RequestBody CreateCustomerPointsAwardManualReq createCustomerPointsAwardManualReq) {
        String loginId = userDetails.getUsername();

        GenericMessage response = pointsActivityDomain.createCustomerPointsAwardManual(loginId, createCustomerPointsAwardManualReq);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
