package org.everowl.core.service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.voucher.request.CreateVoucherReq;
import org.everowl.core.service.dto.voucher.request.DeleteVoucherReq;
import org.everowl.core.service.dto.voucher.request.UpdateVoucherReq;
import org.everowl.core.service.dto.voucher.response.VoucherRes;
import org.everowl.core.service.security.CustomUserDetails;
import org.everowl.core.service.service.VoucherDomain;
import org.everowl.shared.service.annotation.BooleanValidation;
import org.everowl.shared.service.annotation.ValidInteger;
import org.everowl.shared.service.dto.BaseSuccessResponseBodyModel;
import org.everowl.shared.service.dto.GenericMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VoucherController {
    private final VoucherDomain voucherDomain;

    @GetMapping(value = "/public/vouchers")
    public ResponseEntity<BaseSuccessResponseBodyModel> getAllVouchers(@Valid @RequestParam(value = "storeId")
                                                                       @ValidInteger(message = "Please ensure a valid store ID is provided")
                                                                       @NotBlank(message = "Please ensure the store ID is not blank") String storeId) {

        List<VoucherRes> response = voucherDomain.getAllVouchers(Integer.parseInt(storeId));

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping(value = "/owner/voucher", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<BaseSuccessResponseBodyModel> createVoucher(@Valid @RequestParam @ValidInteger(message = "Please ensure a valid min tier level is provided") @NotBlank(message = "Please ensure the min tier level is not blank") String minTierLevel,
                                                                      @RequestParam @NotBlank(message = "Please ensure the voucher name is not blank") String voucherName,
                                                                      @RequestParam @NotBlank(message = "Please ensure the voucher description is not blank") String voucherDesc,
                                                                      @RequestParam @ValidInteger(message = "Please ensure a valid points required value is provided") @NotBlank(message = "Please ensure the points required value is not blank") String pointsRequired,
                                                                      @RequestParam MultipartFile attachment,
                                                                      @RequestParam @BooleanValidation(message = "Please ensure the availability status is not blank") String isAvailable,
                                                                      @RequestParam @NotBlank(message = "Please ensure the tnc description is not blank") String tncDesc,
                                                                      @RequestParam @BooleanValidation(message = "Please ensure the exclusive status is not blank") String isExclusive,
                                                                      @RequestParam @ValidInteger(message = "Please ensure a valid life span is provided") @NotBlank(message = "Please ensure the life span is not blank") String lifeSpan,
                                                                      @RequestParam @NotBlank(message = "Please ensure the meta tag is not blank") String metaTag,
                                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        String loginId = userDetails.getUsername();

        CreateVoucherReq request = new CreateVoucherReq();
        request.setMinTierLevel(Integer.parseInt(minTierLevel));
        request.setVoucherName(voucherName);
        request.setVoucherDesc(voucherDesc);
        request.setPointsRequired(Integer.parseInt(pointsRequired));
        request.setAttachment(attachment);
        request.setIsAvailable(Boolean.getBoolean(isAvailable));
        request.setTncDesc(tncDesc);
        request.setIsExclusive(Boolean.getBoolean(isExclusive));
        request.setLifeSpan(Integer.parseInt(lifeSpan));
        request.setMetaTag(metaTag);

        GenericMessage response = voucherDomain.createVoucher(request, loginId);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PutMapping(value = "/owner/voucher")
    public ResponseEntity<BaseSuccessResponseBodyModel> updateVoucher(@Valid @RequestParam @ValidInteger(message = "Please ensure a valid voucher ID is provided") @NotBlank(message = "Please ensure the voucher ID is not blank") String voucherId,
                                                                      @RequestParam @ValidInteger(message = "Please ensure a valid min tier level is provided") @NotBlank(message = "Please ensure the min tier level is not blank") String minTierLevel,
                                                                      @RequestParam @NotBlank(message = "Please ensure the voucher name is not blank") String voucherName,
                                                                      @RequestParam @NotBlank(message = "Please ensure the voucher description is not blank") String voucherDesc,
                                                                      @RequestParam @ValidInteger(message = "Please ensure a valid points required value is provided") @NotBlank(message = "Please ensure the points required value is not blank") String pointsRequired,
                                                                      @RequestParam MultipartFile attachment,
                                                                      @RequestParam @BooleanValidation(message = "Please ensure the availability status is not blank") String isAvailable,
                                                                      @RequestParam @NotBlank(message = "Please ensure the tnc description is not blank") String tncDesc,
                                                                      @RequestParam @BooleanValidation(message = "Please ensure the exclusive status is not blank") String isExclusive,
                                                                      @RequestParam @ValidInteger(message = "Please ensure a valid life span is provided") @NotBlank(message = "Please ensure the life span is not blank") String lifeSpan,
                                                                      @RequestParam @NotBlank(message = "Please ensure the meta tag is not blank") String metaTag,
                                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        String loginId = userDetails.getUsername();

        UpdateVoucherReq request = new UpdateVoucherReq();
        request.setVoucherId(Integer.parseInt(voucherId));
        request.setMinTierLevel(Integer.parseInt(minTierLevel));
        request.setVoucherName(voucherName);
        request.setVoucherDesc(voucherDesc);
        request.setPointsRequired(Integer.parseInt(pointsRequired));
        request.setAttachment(attachment);
        request.setIsAvailable(Boolean.getBoolean(isAvailable));
        request.setTncDesc(tncDesc);
        request.setIsExclusive(Boolean.getBoolean(isExclusive));
        request.setLifeSpan(Integer.parseInt(lifeSpan));
        request.setMetaTag(metaTag);

        GenericMessage response = voucherDomain.updateVoucher(request, loginId);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @DeleteMapping(value = "/owner/voucher")
    public ResponseEntity<BaseSuccessResponseBodyModel> deleteVoucher(@Valid @RequestBody DeleteVoucherReq deleteVoucherReq,
                                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        String loginId = userDetails.getUsername();

        GenericMessage response = voucherDomain.deleteVoucher(deleteVoucherReq, loginId);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
