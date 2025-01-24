package org.everowl.mycaprio.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import org.everowl.mycaprio.auth.dto.AuthenticationRequest;
import org.everowl.mycaprio.auth.dto.AuthenticationResponse;
import org.everowl.mycaprio.shared.dto.GenericMessage;

public interface AuthDomain {
    AuthenticationResponse authenticate(AuthenticationRequest request, String ipAddress, String userAgent);

    AuthenticationResponse refreshToken(HttpServletRequest request);

    GenericMessage validateUserToken(HttpServletRequest request);
}
