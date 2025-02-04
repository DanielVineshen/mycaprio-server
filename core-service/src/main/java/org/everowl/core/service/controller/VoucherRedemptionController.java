package org.everowl.core.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.voucherRedemption.request.CustomerVoucherPurchase;
import org.everowl.core.service.dto.voucherRedemption.request.CustomerVoucherRedemption;
import org.everowl.core.service.security.CustomUserDetails;
import org.everowl.core.service.service.VoucherRedemptionDomain;
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
public class VoucherRedemptionController {
    private final VoucherRedemptionDomain voucherRedemptionDomain;

    @PostMapping(value = "/customer/voucherRedemption/purchase")
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> createCustomerVoucherPurchase(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                                    @Valid @RequestBody CustomerVoucherPurchase customerVoucherPurchase) {
        String loginId = userDetails.getUsername();

        GenericMessage response = voucherRedemptionDomain.createCustomerVoucherPurchase(loginId, customerVoucherPurchase);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping(value = "/staff/voucherRedemption/redeem")
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> createCustomerVoucherRedemption(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                                      @Valid @RequestBody CustomerVoucherRedemption customerVoucherRedemption) {
        String loginId = userDetails.getUsername();

        GenericMessage response = voucherRedemptionDomain.createCustomerVoucherRedemption(loginId, customerVoucherRedemption);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
