package org.everowl.core.service.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCustomerProfileReq {
    @NotBlank(message = "Please ensure the field login ID is not blank")
    @Size(min = 1, max = 255, message = "Please ensure the field login ID is 1 to 255 characters in length")
    private String loginId;

    @Size(min = 1, max = 255, message = "Please ensure the field password is 1 to 255 characters in length")
    private String password;

    @Size(min = 8, max = 8, message = "Please ensure the field sms code is 8 characters in length")
    private String smsCode;
}
