package org.everowl.core.service.dto.customer.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCustomerProfileReq {
    @NotBlank(message = "Please ensure the field login ID is not blank")
    private String loginId;

    @Size(min = 1, max = 64, message = "Please ensure the field password is 1 to 64 characters in length")
    private String password;
}