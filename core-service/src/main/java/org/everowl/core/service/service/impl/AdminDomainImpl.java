package org.everowl.core.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.admin.request.CreateStaffProfile;
import org.everowl.core.service.dto.admin.request.UpdateStaffProfile;
import org.everowl.core.service.dto.admin.response.AdminProfile;
import org.everowl.core.service.dto.admin.response.StaffsProfiles;
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
    public AdminProfile getAdminProfile(String loginId) {
        AdminEntity admin = adminRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        return modelMapper.map(admin, AdminProfile.class);
    }

    @Override
    public StaffsProfiles getStaffProfiles(String loginId) {
        AdminEntity owner = adminRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        StaffsProfiles staffsProfiles = new StaffsProfiles();

        List<AdminEntity> staffs = adminRepository.findStaffsByStoreId(owner.getStore().getStoreId());

        List<AdminProfile> adminProfiles = new ArrayList<>();
        for (AdminEntity adminEntity : staffs) {
            AdminProfile staffProfile = modelMapper.map(adminEntity, AdminProfile.class);
            adminProfiles.add(staffProfile);
        }

        staffsProfiles.setStaffsProfiles(adminProfiles);

        return staffsProfiles;
    }

    @Override
    public GenericMessage createStaffProfile(String loginId, CreateStaffProfile createStaffProfile) {
        AdminEntity owner = adminRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        Optional<AdminEntity> checkLoginIdEligibility = adminRepository.findByUsername(createStaffProfile.getLoginId());
        if (checkLoginIdEligibility.isPresent()) {
            throw new ForbiddenException(LOGIN_ID_EXIST);
        }

        String hashedPassword = passwordEncoder.encode(createStaffProfile.getPassword());

        AdminEntity adminEntity = new AdminEntity();
        adminEntity.setPassword(hashedPassword);
        adminEntity.setRole("STAFF");
        adminEntity.setLoginId(createStaffProfile.getLoginId());
        adminEntity.setStore(owner.getStore());
        adminEntity.setFullName(createStaffProfile.getFullName());

        adminRepository.save(adminEntity);

        return GenericMessage.builder()
                .status(true)
                .build();
    }

    @Override
    public GenericMessage updateStaffProfile(String loginId, UpdateStaffProfile updateStaffProfile) {
        AdminEntity owner = adminRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        AdminEntity staff = adminRepository.findByAdminId(updateStaffProfile.getAdminId())
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        if (!owner.getStore().equals(staff.getStore())) {
            throw new ForbiddenException(USER_NOT_PERMITTED);
        }

        staff.setFullName(updateStaffProfile.getFullName());

        if (updateStaffProfile.getPassword() != null) {
            String hashedPassword = passwordEncoder.encode(updateStaffProfile.getPassword());
            staff.setPassword(hashedPassword);
        }

        adminRepository.save(staff);

        return GenericMessage.builder()
                .status(true)
                .build();
    }
}
