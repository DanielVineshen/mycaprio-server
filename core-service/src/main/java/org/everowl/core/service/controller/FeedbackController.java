package org.everowl.core.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.feedback.request.CreateFeedbackReq;
import org.everowl.core.service.dto.feedback.request.DeleteFeedbackReq;
import org.everowl.core.service.dto.feedback.response.FeedbackRes;
import org.everowl.core.service.security.CustomUserDetails;
import org.everowl.core.service.service.FeedbackDomain;
import org.everowl.shared.service.dto.BaseSuccessResponseBodyModel;
import org.everowl.shared.service.dto.GenericMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class FeedbackController {
    private final FeedbackDomain feedbackDomain;

    @GetMapping(value = "/owner/feedbacks")
    public ResponseEntity<BaseSuccessResponseBodyModel> getStoreFeedbacks(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String loginId = userDetails.getUsername();

        FeedbackRes response = feedbackDomain.getStoreFeedbacks(loginId);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping(value = "/customer/feedback")
    public ResponseEntity<BaseSuccessResponseBodyModel> createFeedback(@Valid @RequestBody CreateFeedbackReq createFeedbackReq) {

        GenericMessage response = feedbackDomain.createFeedback(createFeedbackReq);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @DeleteMapping(value = "/owner/feedback")
    public ResponseEntity<BaseSuccessResponseBodyModel> deleteFeedback(@Valid @RequestBody DeleteFeedbackReq deleteFeedbackReq,
                                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        String loginId = userDetails.getUsername();

        GenericMessage response = feedbackDomain.deleteFeedback(deleteFeedbackReq, loginId);

        BaseSuccessResponseBodyModel responseBody = new BaseSuccessResponseBodyModel(response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
