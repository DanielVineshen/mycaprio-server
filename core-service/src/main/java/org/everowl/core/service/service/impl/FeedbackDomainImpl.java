package org.everowl.core.service.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.feedback.request.CreateFeedbackReq;
import org.everowl.core.service.dto.feedback.request.DeleteFeedbackReq;
import org.everowl.core.service.dto.feedback.response.FeedbackDetailsRes;
import org.everowl.core.service.dto.feedback.response.FeedbackRes;
import org.everowl.core.service.service.FeedbackDomain;
import org.everowl.database.service.entity.*;
import org.everowl.database.service.repository.*;
import org.everowl.shared.service.dto.GenericMessage;
import org.everowl.shared.service.enums.ErrorCode;
import org.everowl.shared.service.exception.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.everowl.shared.service.enums.ErrorCode.USER_NOT_AUTHORIZED;
import static org.everowl.shared.service.enums.ErrorCode.USER_NOT_EXIST;
import static org.everowl.shared.service.util.JsonConverterUtils.convertObjectToJsonString;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackDomainImpl implements FeedbackDomain {
    private final StoreRepository storeRepository;
    private final FeedbackRepository feedbackRepository;
    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final AuditLogRepository auditLogRepository;
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
    @Transactional
    public GenericMessage createFeedback(CreateFeedbackReq request, String loginId) {
        StoreEntity store = storeRepository.findById(Integer.parseInt(request.getStoreId()))
                .orElseThrow(() -> new NotFoundException(ErrorCode.STORE_NOT_EXIST));

        CustomerEntity customer = customerRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        FeedbackEntity feedback = new FeedbackEntity();
        feedback.setStore(store);
        feedback.setEmailAddress(request.getEmailAddress());
        feedback.setFullName(request.getFullName());
        feedback.setContactNo(request.getContactNo());
        feedback.setMsgTitle(request.getMsgTitle());
        feedback.setMsgContent(request.getMsgContent());

        feedbackRepository.save(feedback);

        AuditLogEntity auditLogEntity = new AuditLogEntity();
        auditLogEntity.setLoginId(loginId);
        auditLogEntity.setPerformedBy(customer.getFullName());
        auditLogEntity.setAuthorityLevel("CUSTOMER");
        auditLogEntity.setBeforeChanged(null);
        auditLogEntity.setAfterChanged(convertObjectToJsonString(new Object[]{feedback}));
        auditLogEntity.setLogType("CREATE_FEEDBACK");
        auditLogEntity.setLogAction("CREATE");
        auditLogEntity.setLogDesc("A feedback was created");

        auditLogRepository.save(auditLogEntity);

        return GenericMessage.builder()
                .status(true)
                .build();
    }

    @Override
    @Transactional
    public GenericMessage deleteFeedback(DeleteFeedbackReq request, String loginId) {
        // Validate store owner
        AdminEntity admin = adminRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_AUTHORIZED));

        // Validate feedback
        FeedbackEntity feedback = feedbackRepository.findById(Integer.parseInt(request.getFeedbackId()))
                .orElseThrow(() -> new NotFoundException(USER_NOT_AUTHORIZED));

        feedbackRepository.deleteById(Integer.parseInt(request.getFeedbackId()));

        AuditLogEntity auditLogEntity = new AuditLogEntity();
        auditLogEntity.setLoginId(loginId);
        auditLogEntity.setPerformedBy(admin.getFullName());
        auditLogEntity.setAuthorityLevel("OWNER");
        auditLogEntity.setBeforeChanged(convertObjectToJsonString(new Object[]{feedback}));
        auditLogEntity.setAfterChanged(null);
        auditLogEntity.setLogType("DELETE_FEEDBACK");
        auditLogEntity.setLogAction("DELETE");
        auditLogEntity.setLogDesc("A feedback was deleted");

        auditLogRepository.save(auditLogEntity);

        return GenericMessage.builder()
                .status(true)
                .build();
    }
}
