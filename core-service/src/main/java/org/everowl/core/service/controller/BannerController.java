package org.everowl.core.service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.banner.request.CreateBannerReq;
import org.everowl.core.service.dto.banner.request.DeleteBannerReq;
import org.everowl.core.service.dto.banner.request.UpdateBannerReq;
import org.everowl.core.service.security.CustomUserDetails;
import org.everowl.core.service.service.BannerDomain;
import org.everowl.shared.service.annotation.ValidInteger;
import org.everowl.shared.service.dto.BaseSuccessResponseBodyModel;
import org.everowl.shared.service.dto.GenericMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BannerController {
    private final BannerDomain bannerDomain;

    @PostMapping(value = "/owner/banner", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<BaseSuccessResponseBodyModel> createBanner(@Valid @RequestParam @NotNull(message = "Please ensure the attachment is not blank") MultipartFile attachment,
                                                                     @RequestParam @NotNull(message = "Please ensure the availability status is not blank") String attachmentType,
                                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        String loginId = userDetails.getUsername();

        CreateBannerReq request = new CreateBannerReq();
        request.setAttachment(attachment);
        request.setAttachmentType(attachmentType);

        GenericMessage response = bannerDomain.createBanner(request, loginId);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PutMapping(value = "/owner/banner")
    public ResponseEntity<BaseSuccessResponseBodyModel> updateBanner(@Valid @RequestParam @ValidInteger(message = "Please ensure a valid attachment ID is provided") @NotBlank(message = "Please ensure the attachment ID is not blank") String attachmentId,
                                                                     @RequestParam @NotNull(message = "Please ensure the attachment is not blank") MultipartFile attachment,
                                                                     @RequestParam @NotBlank(message = "Please ensure the availability status is not blank") String attachmentType,
                                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        String loginId = userDetails.getUsername();

        UpdateBannerReq request = new UpdateBannerReq();
        request.setAttachmentId(Integer.parseInt(attachmentId));
        request.setAttachment(attachment);
        request.setAttachmentType(attachmentType);

        GenericMessage response = bannerDomain.updateBanner(request, loginId);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @DeleteMapping(value = "/owner/banner")
    public ResponseEntity<BaseSuccessResponseBodyModel> deleteBanner(@Valid @RequestBody DeleteBannerReq deleteVoucherReq,
                                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        String loginId = userDetails.getUsername();

        GenericMessage response = bannerDomain.deleteBanner(deleteVoucherReq, loginId);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
