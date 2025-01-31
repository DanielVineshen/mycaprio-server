package org.everowl.core.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.admin.request.CreateStaffProfile;
import org.everowl.core.service.dto.admin.request.UpdateStaffProfile;
import org.everowl.core.service.dto.admin.response.AdminProfile;
import org.everowl.core.service.dto.admin.response.StaffsProfiles;
import org.everowl.core.service.security.CustomUserDetails;
import org.everowl.core.service.service.AdminDomain;
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
public class AdminController {
    private final AdminDomain adminDomain;

    @GetMapping(value = "/admin/profile")
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> getAdminProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String loginId = userDetails.getUsername();

        AdminProfile response = adminDomain.getAdminProfile(loginId);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping(value = "/owner/staffs")
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> getStaffsProfiles(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String loginId = userDetails.getUsername();

        StaffsProfiles response = adminDomain.getStaffProfiles(loginId);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping(value = "/owner/staff")
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> createStaffProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                         @Valid @RequestBody CreateStaffProfile createStaffProfile) {
        String loginId = userDetails.getUsername();

        GenericMessage response = adminDomain.createStaffProfile(loginId, createStaffProfile);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PutMapping(value = "/owner/staff")
    public @ResponseBody ResponseEntity<BaseSuccessResponseBodyModel> updateStaffProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                         @Valid @RequestBody UpdateStaffProfile updateStaffProfile) {
        String loginId = userDetails.getUsername();

        GenericMessage response = adminDomain.updateStaffProfile(loginId, updateStaffProfile);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
