package org.everowl.core.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.admin.request.CreateStaffProfile;
import org.everowl.core.service.dto.admin.request.UpdateStaffProfile;
import org.everowl.core.service.dto.admin.response.AdminProfile;
import org.everowl.core.service.dto.admin.response.StaffsProfiles;
import org.everowl.core.service.service.AdminDomain;
import org.everowl.database.service.repository.AdminRepository;
import org.everowl.shared.service.dto.GenericMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminDomainImpl implements AdminDomain {
    private final AdminRepository adminRepository;

    @Override
    public AdminProfile getAdminProfile(String loginId) {
        return null;
    }

    @Override
    public StaffsProfiles getStaffProfiles(String loginId) {
        return null;
    }

    @Override
    public GenericMessage createStaffProfile(String loginId, CreateStaffProfile createStaffProfile) {
        return null;
    }

    @Override
    public GenericMessage updateStaffProfile(String loginId, UpdateStaffProfile updateStaffProfile) {
        return null;
    }
}
