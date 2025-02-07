package org.everowl.core.service.service;

import jakarta.servlet.http.HttpServletRequest;
import org.everowl.core.service.dto.auth.request.AuthReq;
import org.everowl.core.service.dto.auth.request.CreateCustomerProfileReq;
import org.everowl.core.service.dto.auth.request.ResetCustomerPasswordReq;
import org.everowl.core.service.dto.auth.response.AuthRes;
import org.everowl.shared.service.dto.GenericMessage;

public interface AuthDomain {
    AuthRes authenticate(AuthReq request, String ipAddress, String userAgent);

    AuthRes refreshToken(HttpServletRequest request);

    GenericMessage validateUserToken(HttpServletRequest request);

    GenericMessage validateCustomerLoginId(String custLoginId);

    GenericMessage createCustomerProfile(CreateCustomerProfileReq createCustomerProfileReq);

    GenericMessage resetCustomerPassword(ResetCustomerPasswordReq resetCustomerPasswordReq);
}
