package org.everowl.core.service.dto.feedback.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackRes {
    private List<FeedbackDetailsRes> feedbacks;
}
