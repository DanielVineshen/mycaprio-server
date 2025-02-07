package org.everowl.core.service.dto.feedback.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.everowl.shared.service.annotation.ValidInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteFeedbackReq {
    @ValidInteger(message = "Please ensure a valid feedback ID is provided")
    private String feedbackId;
}
