package org.everowl.core.service.dto.admin.response;

import lombok.Data;

@Data
public class AdminProfile {
    private Integer adminId;
    private Integer storeId;
    private String loginId;
    private String fullName;
    private String role;
    private Boolean isDisabled;
}
