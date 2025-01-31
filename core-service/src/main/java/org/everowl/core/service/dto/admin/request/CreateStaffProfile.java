package org.everowl.core.service.dto.admin.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateStaffProfile {
    @NotBlank(message = "Please ensure the field login ID is not blank")
    @Size(min = 1, max = 255, message = "Please ensure the field login ID is 1 to 255 characters in length")
    private String loginId;

    @NotBlank(message = "Please ensure the field password is not blank")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)\\S{12,}$", message = "Password provided is not valid")
    private String password;

    private String fullName;
}
