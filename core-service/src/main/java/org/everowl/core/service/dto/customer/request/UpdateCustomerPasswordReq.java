package org.everowl.core.service.dto.customer.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCustomerPasswordReq {
    @NotBlank(message = "Please ensure the field old password is not blank")
    @Size(min = 8, max = 64, message = "Please ensure the field old password is 8 to 64 characters in length")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)\\S{8,}$", message = "Old password provided is not valid")
    private String oldPassword;

    @NotBlank(message = "Please ensure the field new password is not blank")
    @Size(min = 8, max = 64, message = "Please ensure the field new password is 8 to 64 characters in length")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)\\S{8,}$", message = "New Password provided is not valid")
    private String newPassword;
}
