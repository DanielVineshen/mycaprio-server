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
import org.everowl.shared.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.everowl.shared.service.enums.ErrorCode.FILE_NOT_FOUND;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class BannerController {
    private final BannerDomain bannerDomain;

    @Value("${app.attachment-storage.banner-path}")
    private String storagePath;

    @PostMapping(value = "/owner/banner", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<BaseSuccessResponseBodyModel> createBanner(@RequestParam @NotNull(message = "Please ensure the attachment is not blank") MultipartFile attachment,
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
    public ResponseEntity<BaseSuccessResponseBodyModel> updateBanner(@RequestParam @ValidInteger(message = "Please ensure a valid attachment ID is provided") @NotBlank(message = "Please ensure the attachment ID is not blank") String attachmentId,
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

    @GetMapping(path = "/public/banner/{attachmentName}")
    public ResponseEntity<Resource> getAttachment(@PathVariable @NotBlank(message = "Please ensure attachment name is not blank") String attachmentName) {

        // Get the content type of the attachment
        String contentType = bannerDomain.getBannerAttachment(attachmentName);
        MediaType mediaType = Objects.equals(contentType, ".png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG;

        try {
            // Construct the file path and create a resource
            Path filePath = Paths.get(storagePath + "/" + attachmentName);
            Resource fileResource = new UrlResource(filePath.toUri());

            // Return the image resource with appropriate headers
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + attachmentName + "\"")
                    .body(fileResource);

        } catch (Exception e) {
            // If the file is not found, throw a NotFoundException
            throw new NotFoundException(FILE_NOT_FOUND);
        }
    }
}
