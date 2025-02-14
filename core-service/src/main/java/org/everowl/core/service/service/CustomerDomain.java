package org.everowl.core.service.service;

import org.everowl.core.service.dto.customer.request.CreateCustomerProfileReq;
import org.everowl.core.service.dto.customer.request.ResetCustomerPasswordReq;
import org.everowl.core.service.dto.customer.request.UpdateCustomerPasswordReq;
import org.everowl.core.service.dto.customer.request.UpdateCustomerProfileReq;
import org.everowl.core.service.dto.customer.response.CustomerProfileRes;
import org.everowl.shared.service.dto.GenericMessage;

public interface CustomerDomain {
    CustomerProfileRes getCustomerProfile(String loginId);

    GenericMessage updateCustomerProfile(String loginId, UpdateCustomerProfileReq updateCustomerProfileReq);

    GenericMessage updateCustomerPassword(String loginId, UpdateCustomerPasswordReq updateCustomerPasswordReq);

    GenericMessage createCustomerProfile(CreateCustomerProfileReq createCustomerProfileReq, String loginId);

    GenericMessage resetCustomerPassword(ResetCustomerPasswordReq resetCustomerPasswordReq, String loginId);
}
