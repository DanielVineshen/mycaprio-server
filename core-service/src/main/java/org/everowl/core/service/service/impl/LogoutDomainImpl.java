package org.everowl.core.service.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.everowl.database.service.entity.AuditLogEntity;
import org.everowl.database.service.entity.TokenEntity;
import org.everowl.database.service.repository.AuditLogRepository;
import org.everowl.database.service.repository.TokenRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static org.everowl.shared.service.util.JsonConverterUtils.convertObjectToJsonString;

/**
 * This class represents a LogoutService that implements the LogoutHandler interface.
 * It provides functionality to log out the user by deleting the access token from the token repository
 * and writing a response to the client.
 */
@Service
@RequiredArgsConstructor
public class LogoutDomainImpl implements LogoutHandler {
    private final TokenRepository tokenRepository;
    private final AuditLogRepository auditLogRepository;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";

    /**
     * Logs out the user by deleting the access token from the token repository and writing a response to the client.
     *
     * @param request        the HttpServletRequest object containing the authorization header
     * @param response       the HttpServletResponse object to write the logout result
     * @param authentication the Authentication object representing the current user's authentication details (not used in this implementation)
     */
    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        // Check if the Authorization header is present and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            writeResponse(response, false);
            return;
        }

        // Extract the JWT token from the Authorization header
        final String jwt = authHeader.substring(BEARER.length());

        TokenEntity token = tokenRepository.findByAccessToken(jwt)
                .orElse(null);

        // Delete the token from the token repository
        tokenRepository.deleteTokenByAccessToken(jwt);

        if (token != null) {
            String beforeDeleted = convertObjectToJsonString(token);

            AuditLogEntity auditLogEntity = new AuditLogEntity();
            auditLogEntity.setLoginId("system");
            auditLogEntity.setPerformedBy("system");
            auditLogEntity.setAuthorityLevel("SYSTEM");
            auditLogEntity.setBeforeChanged(beforeDeleted);
            auditLogEntity.setAfterChanged(null);
            auditLogEntity.setLogType("USER_LOGOUT");
            auditLogEntity.setLogAction("DELETE");
            auditLogRepository.save(auditLogEntity);
        }

        // Write a successful logout response
        writeResponse(response, true);
    }

    /**
     * Sets the response status to SC_OK, and the content type to "application/json".
     * It then writes a JSON response to the HttpServletResponse object based on the provided status parameter.
     * The JSON response contains a "data" object with "respCode" and "respDesc" properties,
     * and a "result" object with a "status" property indicating the success or failure based on the status parameter.
     *
     * @param response The HttpServletResponse object to write the response to.
     * @param status   The status parameter indicating the success or failure of the logout operation.
     * @throws RuntimeException If an IOException occurs while writing the response.
     */
    private void writeResponse(HttpServletResponse response, boolean status) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        try {
            String jsonResponse = status ?
                    "{\"data\":{\"respCode\":2000,\"respDesc\":\"Success\",\"result\":{\"status\":true}}}" :
                    "{\"data\":{\"respCode\":2000,\"respDesc\":\"Success\",\"result\":{\"status\":false}}}";
            response.getWriter().print(jsonResponse);
            response.getWriter().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}