package org.everowl.core.service.dto.feedback.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDetailsRes {
    private Integer feedbackId;
    private String emailAddress;
    private String fullName;
    private String contactNo;
    private String msgTitle;
    private String msgContent;
    private String createdAt;
}
