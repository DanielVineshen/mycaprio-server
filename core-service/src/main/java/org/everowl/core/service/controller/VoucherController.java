package org.everowl.core.service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.voucher.request.CreateVoucherReq;
import org.everowl.core.service.dto.voucher.request.DeleteVoucherReq;
import org.everowl.core.service.dto.voucher.request.UpdateVoucherReq;
import org.everowl.core.service.dto.voucher.response.VoucherRes;
import org.everowl.core.service.security.CustomUserDetails;
import org.everowl.core.service.service.VoucherDomain;
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
    public ResponseEntity<BaseSuccessResponseBodyModel> getAllVouchers(@RequestParam(value = "storeId")
                                                                       @Min(value = 1, message = "Please ensure a valid store ID is provided")
                                                                       @NotNull(message = "Please ensure the store ID is not blank") Integer storeId) {

        List<VoucherRes> response = voucherDomain.getAllVouchers(storeId);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping(value = "/owner/voucher", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<BaseSuccessResponseBodyModel> createVoucher(@RequestParam @Min(value = 1, message = "Please ensure a valid min tier level is provided") @NotNull(message = "Please ensure the min tier level is not blank") Integer minTierLevel,
                                                                      @RequestParam @NotBlank(message = "Please ensure the voucher name is not blank") String voucherName,
                                                                      @RequestParam @NotBlank(message = "Please ensure the voucher description is not blank") String voucherDesc,
                                                                      @RequestParam @Min(value = 1, message = "Please ensure a valid points required value is provided") @NotNull(message = "Please ensure the points required value is not blank") Integer pointsRequired,
                                                                      @RequestParam MultipartFile attachment,
                                                                      @RequestParam @NotNull(message = "Please ensure the availability status is not blank") Boolean isAvailable,
                                                                      @RequestParam @NotBlank(message = "Please ensure the tnc description is not blank") String tncDesc,
                                                                      @RequestParam @NotNull(message = "Please ensure the exclusive status is not blank") Boolean isExclusive,
                                                                      @RequestParam @Min(value = 1, message = "Please ensure a valid life span is provided") @NotNull(message = "Please ensure the life span is not blank") Integer lifeSpan,
                                                                      @RequestParam @NotBlank(message = "Please ensure the meta tag is not blank") String metaTag,
                                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        String loginId = userDetails.getUsername();

        CreateVoucherReq request = new CreateVoucherReq();
        request.setMinTierLevel(minTierLevel);
        request.setVoucherName(voucherName);
        request.setVoucherDesc(voucherDesc);
        request.setPointsRequired(pointsRequired);
        request.setAttachment(attachment);
        request.setIsAvailable(isAvailable);
        request.setTncDesc(tncDesc);
        request.setIsExclusive(isExclusive);
        request.setLifeSpan(lifeSpan);
        request.setMetaTag(metaTag);

        GenericMessage response = voucherDomain.createVoucher(request, loginId);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PutMapping(value = "/owner/voucher")
    public ResponseEntity<BaseSuccessResponseBodyModel> updateVoucher(@RequestParam @Min(value = 1, message = "Please ensure a valid voucher ID is provided") @NotNull(message = "Please ensure the voucher ID is not blank") Integer voucherId,
                                                                      @RequestParam @Min(value = 1, message = "Please ensure a valid min tier level is provided") @NotNull(message = "Please ensure the min tier level is not blank") Integer minTierLevel,
                                                                      @RequestParam @NotBlank(message = "Please ensure the voucher name is not blank") String voucherName,
                                                                      @RequestParam @NotBlank(message = "Please ensure the voucher description is not blank") String voucherDesc,
                                                                      @RequestParam @Min(value = 1, message = "Please ensure a valid points required value is provided") @NotNull(message = "Please ensure the points required value is not blank") Integer pointsRequired,
                                                                      @RequestParam MultipartFile attachment,
                                                                      @RequestParam @NotNull(message = "Please ensure the availability status is not blank") Boolean isAvailable,
                                                                      @RequestParam @NotBlank(message = "Please ensure the tnc description is not blank") String tncDesc,
                                                                      @RequestParam @NotNull(message = "Please ensure the exclusive status is not blank") Boolean isExclusive,
                                                                      @RequestParam @Min(value = 1, message = "Please ensure a valid life span is provided") @NotNull(message = "Please ensure the life span is not blank") Integer lifeSpan,
                                                                      @RequestParam @NotBlank(message = "Please ensure the meta tag is not blank") String metaTag,
                                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        String loginId = userDetails.getUsername();

        UpdateVoucherReq request = new UpdateVoucherReq();
        request.setVoucherId(voucherId);
        request.setMinTierLevel(minTierLevel);
        request.setVoucherName(voucherName);
        request.setVoucherDesc(voucherDesc);
        request.setPointsRequired(pointsRequired);
        request.setAttachment(attachment);
        request.setIsAvailable(isAvailable);
        request.setTncDesc(tncDesc);
        request.setIsExclusive(isExclusive);
        request.setLifeSpan(lifeSpan);
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
