package org.everowl.core.service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.pointsActivity.request.CreateCustomerPointsAwardManual;
import org.everowl.core.service.dto.pointsActivity.request.CreateCustomerPointsAwardScan;
import org.everowl.core.service.dto.pointsActivity.response.PointsActivitiesDetails;
import org.everowl.core.service.security.CustomUserDetails;
import org.everowl.core.service.service.PointsActivityDomain;
import org.everowl.shared.service.annotation.ValidInteger;
import org.everowl.shared.service.dto.BaseSuccessResponseBodyModel;
import org.everowl.shared.service.dto.GenericMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

        PointsActivitiesDetails response = pointsActivityDomain.getCustomerPointsActivitiesDetails(loginId, Integer.valueOf(storeId));

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping(value = "/staff/pointsActivity/scan")
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> createCustomerPointsAwardScan(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                                    @Valid @RequestBody CreateCustomerPointsAwardScan createCustomerPointsAwardScan) {
        String loginId = userDetails.getUsername();

        GenericMessage response = pointsActivityDomain.createCustomerPointsAwardScan(loginId, createCustomerPointsAwardScan);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping(value = "/staff/pointsActivity/manual")
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> createCustomerPointsAwardManual(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                                      @Valid @RequestBody CreateCustomerPointsAwardManual createCustomerPointsAwardManual) {
        String loginId = userDetails.getUsername();

        GenericMessage response = pointsActivityDomain.createCustomerPointsAwardManual(loginId, createCustomerPointsAwardManual);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
