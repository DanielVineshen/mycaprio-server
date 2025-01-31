package org.everowl.core.service.dto.customer.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateCustomerPassword {
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)\\S{12,}$", message = "Old password provided is not valid")
    private String oldPassword;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)\\S{12,}$", message = "New password provided is not valid")
    private String newPassword;
}
