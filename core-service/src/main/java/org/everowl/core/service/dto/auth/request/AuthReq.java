package org.everowl.core.service.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthReq {
    @NotBlank(message = "Please ensure the field username is not blank")
    @Size(min = 1, max = 255, message = "Please ensure the field username is 1 to 255 characters in length")
    private String username;

    @NotBlank(message = "Please ensure the field password is not blank")
    @Size(min = 1, max = 255, message = "Please ensure the field password is 1 to 255 characters in length")
    private String password;
}
