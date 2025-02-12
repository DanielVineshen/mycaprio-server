package org.everowl.core.service.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetCustomerPasswordReq {
    @NotBlank(message = "Please ensure the field login ID is not blank")
    @Pattern(regexp = "^\\+60(1[0-9])[0-9]{7,8}$", message = "Please enter a valid Malaysian mobile number starting with +60")
    private String loginId;

    @Size(min = 1, max = 64, message = "Please ensure the field password is 1 to 64 characters in length")
    private String password;

    @Size(min = 6, max = 6, message = "Please ensure the field sms code is 6 characters in length")
    private String smsCode;
}
