package org.everowl.core.service.service;

import org.everowl.core.service.dto.admin.request.CreateStaffProfileReq;
import org.everowl.core.service.dto.admin.request.UpdateStaffProfileReq;
import org.everowl.core.service.dto.admin.response.AdminProfileRes;
import org.everowl.core.service.dto.admin.response.StaffsProfilesRes;
import org.everowl.shared.service.dto.GenericMessage;

public interface AdminDomain {
    AdminProfileRes getAdminProfile(String loginId);

    StaffsProfilesRes getStaffProfiles(String loginId);

    GenericMessage createStaffProfile(String loginId, CreateStaffProfileReq createStaffProfileReq);

    GenericMessage updateStaffProfile(String loginId, UpdateStaffProfileReq updateStaffProfileReq);
}
