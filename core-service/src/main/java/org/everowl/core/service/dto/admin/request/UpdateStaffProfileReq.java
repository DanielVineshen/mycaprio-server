package org.everowl.core.service.dto.admin.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateStaffProfileReq {
    @NotBlank(message = "Please ensure the field admin ID is not blank")
    @Size(min = 1, max = 255, message = "Please ensure the field admin ID is 1 to 255 characters in length")
    private String adminId;

    @NotBlank(message = "Please ensure the field password is not blank")
    @Size(min = 8, max = 64, message = "Please ensure the field password is 8 to 64 characters in length")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)\\S{8,}$", message = "Password provided is not valid")
    private String password;

    private String fullName;
}
