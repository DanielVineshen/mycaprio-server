package org.everowl.core.service.dto.feedback.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.everowl.shared.service.annotation.ValidInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFeedbackReq {
    @ValidInteger(message = "Please ensure a valid store ID is provided")
    private String storeId;

    @Email(message = "Please ensure a valid email is provided")
    private String emailAddress;

    private String fullName;
    private String contactNo;

    @NotBlank(message = "Please ensure the message title is not empty")
    private String msgTitle;

    @NotBlank(message = "Please ensure the message content is not empty")
    private String msgContent;
}
