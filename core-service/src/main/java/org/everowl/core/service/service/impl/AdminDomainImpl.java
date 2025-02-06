package org.everowl.core.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.admin.request.CreateStaffProfileReq;
import org.everowl.core.service.dto.admin.request.UpdateStaffProfileReq;
import org.everowl.core.service.dto.admin.response.AdminProfileRes;
import org.everowl.core.service.dto.admin.response.StaffsProfilesRes;
import org.everowl.core.service.service.AdminDomain;
import org.everowl.database.service.entity.AdminEntity;
import org.everowl.database.service.repository.AdminRepository;
import org.everowl.shared.service.dto.GenericMessage;
import org.everowl.shared.service.exception.ForbiddenException;
import org.everowl.shared.service.exception.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.everowl.shared.service.enums.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminDomainImpl implements AdminDomain {
    private final BCryptPasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final ModelMapper modelMapper;

    @Override
    public AdminProfileRes getAdminProfile(String loginId) {
        AdminEntity admin = adminRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        return modelMapper.map(admin, AdminProfileRes.class);
    }

    @Override
    public StaffsProfilesRes getStaffProfiles(String loginId) {
        AdminEntity owner = adminRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        StaffsProfilesRes staffsProfilesRes = new StaffsProfilesRes();

        List<AdminEntity> staffs = adminRepository.findStaffsByStoreId(owner.getStore().getStoreId());

        List<AdminProfileRes> adminProfileRes = new ArrayList<>();
        for (AdminEntity adminEntity : staffs) {
            AdminProfileRes staffProfile = modelMapper.map(adminEntity, AdminProfileRes.class);
            adminProfileRes.add(staffProfile);
        }

        staffsProfilesRes.setStaffsProfiles(adminProfileRes);

        return staffsProfilesRes;
    }

    @Override
    public GenericMessage createStaffProfile(String loginId, CreateStaffProfileReq createStaffProfileReq) {
        AdminEntity owner = adminRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        Optional<AdminEntity> checkLoginIdEligibility = adminRepository.findByUsername(createStaffProfileReq.getLoginId());
        if (checkLoginIdEligibility.isPresent()) {
            throw new ForbiddenException(LOGIN_ID_EXIST);
        }

        String hashedPassword = passwordEncoder.encode(createStaffProfileReq.getPassword());

        AdminEntity adminEntity = new AdminEntity();
        adminEntity.setPassword(hashedPassword);
        adminEntity.setRole("STAFF");
        adminEntity.setLoginId(createStaffProfileReq.getLoginId());
        adminEntity.setStore(owner.getStore());
        adminEntity.setFullName(createStaffProfileReq.getFullName());

        adminRepository.save(adminEntity);

        return GenericMessage.builder()
                .status(true)
                .build();
    }

    @Override
    public GenericMessage updateStaffProfile(String loginId, UpdateStaffProfileReq updateStaffProfileReq) {
        AdminEntity owner = adminRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        AdminEntity staff = adminRepository.findByAdminId(updateStaffProfileReq.getAdminId())
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        if (!owner.getStore().equals(staff.getStore())) {
            throw new ForbiddenException(USER_NOT_PERMITTED);
        }

        staff.setFullName(updateStaffProfileReq.getFullName());

        if (updateStaffProfileReq.getPassword() != null) {
            String hashedPassword = passwordEncoder.encode(updateStaffProfileReq.getPassword());
            staff.setPassword(hashedPassword);
        }

        adminRepository.save(staff);

        return GenericMessage.builder()
                .status(true)
                .build();
    }
}
