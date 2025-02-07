package org.everowl.core.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.feedback.request.CreateFeedbackReq;
import org.everowl.core.service.dto.feedback.request.DeleteFeedbackReq;
import org.everowl.core.service.dto.feedback.response.FeedbackDetailsRes;
import org.everowl.core.service.dto.feedback.response.FeedbackRes;
import org.everowl.core.service.service.FeedbackDomain;
import org.everowl.database.service.entity.AdminEntity;
import org.everowl.database.service.entity.FeedbackEntity;
import org.everowl.database.service.entity.StoreEntity;
import org.everowl.database.service.repository.AdminRepository;
import org.everowl.database.service.repository.FeedbackRepository;
import org.everowl.database.service.repository.StoreRepository;
import org.everowl.shared.service.dto.GenericMessage;
import org.everowl.shared.service.enums.ErrorCode;
import org.everowl.shared.service.exception.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.everowl.shared.service.enums.ErrorCode.USER_NOT_AUTHORIZED;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackDomainImpl implements FeedbackDomain {
    private final StoreRepository storeRepository;
    private final FeedbackRepository feedbackRepository;
    private final AdminRepository adminRepository;
    private final ModelMapper modelMapper;

    @Override
    public FeedbackRes getStoreFeedbacks(String loginId) {
        // Validate store owner
        AdminEntity admin = adminRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_AUTHORIZED));

        // Validate feedback
        List<FeedbackEntity> feedbacks = feedbackRepository.findAllByStoreId(admin.getStore().getStoreId());

        List<FeedbackDetailsRes> feedbackList = new ArrayList<>();
        for (FeedbackEntity feedback : feedbacks) {
            FeedbackDetailsRes feedbackDetailsRes = modelMapper.map(feedback, FeedbackDetailsRes.class);
            feedbackList.add(feedbackDetailsRes);
        }

        FeedbackRes feedback = new FeedbackRes();
        feedback.setFeedbacks(feedbackList);

        return feedback;
    }

    @Override
    public GenericMessage createFeedback(CreateFeedbackReq request) {
        StoreEntity store = storeRepository.findById(Integer.parseInt(request.getStoreId()))
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_EXIST));

        FeedbackEntity feedback = new FeedbackEntity();
        feedback.setStore(store);
        feedback.setEmailAddress(request.getEmailAddress());
        feedback.setFullName(request.getFullName());
        feedback.setContactNo(request.getContactNo());
        feedback.setMsgTitle(request.getMsgTitle());
        feedback.setMsgContent(request.getMsgContent());

        feedbackRepository.save(feedback);

        return GenericMessage.builder()
                .status(true)
                .build();
    }

    @Override
    public GenericMessage deleteFeedback(DeleteFeedbackReq request, String loginId) {
        // Validate store owner
        AdminEntity admin = adminRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_AUTHORIZED));

        // Validate feedback
        FeedbackEntity feedback = feedbackRepository.findById(Integer.parseInt(request.getFeedbackId()))
                .orElseThrow(() -> new NotFoundException(USER_NOT_AUTHORIZED));

        feedbackRepository.deleteById(Integer.parseInt(request.getFeedbackId()));

        return GenericMessage.builder()
                .status(true)
                .build();
    }
}
