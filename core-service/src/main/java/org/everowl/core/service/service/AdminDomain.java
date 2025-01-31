package org.everowl.core.service.service;

import org.everowl.core.service.dto.admin.request.CreateStaffProfile;
import org.everowl.core.service.dto.admin.request.UpdateStaffProfile;
import org.everowl.core.service.dto.admin.response.AdminProfile;
import org.everowl.core.service.dto.admin.response.StaffsProfiles;
import org.everowl.shared.service.dto.GenericMessage;

public interface AdminDomain {
    AdminProfile getAdminProfile(String loginId);

    StaffsProfiles getStaffProfiles(String loginId);

    GenericMessage createStaffProfile(String loginId, CreateStaffProfile createStaffProfile);

    GenericMessage updateStaffProfile(String loginId, UpdateStaffProfile updateStaffProfile);
}
