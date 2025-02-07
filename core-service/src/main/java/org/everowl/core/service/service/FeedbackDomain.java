package org.everowl.core.service.service;

import org.everowl.core.service.dto.feedback.request.CreateFeedbackReq;
import org.everowl.core.service.dto.feedback.request.DeleteFeedbackReq;
import org.everowl.core.service.dto.feedback.response.FeedbackRes;
import org.everowl.shared.service.dto.GenericMessage;

public interface FeedbackDomain {
    FeedbackRes getStoreFeedbacks(String loginId);

    GenericMessage createFeedback(CreateFeedbackReq request);

    GenericMessage deleteFeedback(DeleteFeedbackReq request, String loginId);
}
