package org.everowl.core.service.dto.customer.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateCustomerPasswordReq {
    @NotBlank(message = "Please ensure the field old password is not blank")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)\\S{12,}$", message = "Old password provided is not valid")
    private String oldPassword;

    @NotBlank(message = "Please ensure the field new password is not blank")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)\\S{12,}$", message = "New password provided is not valid")
    private String newPassword;
}
