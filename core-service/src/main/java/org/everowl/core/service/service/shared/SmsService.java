package org.everowl.core.service.service.shared;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.everowl.shared.service.exception.RunTimeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.everowl.shared.service.enums.ErrorCode.SMS_SEND_EXCEPTION;

@Service
@RequiredArgsConstructor
public class SmsService {
    @Value("${sms.api.username}")
    private String username;

    @Value("${sms.api.password}")
    private String password;

    private final RestTemplate restTemplate;

    private static final int MAX_ATTEMPTS = 5;

    @Data
    @Builder
    public static class SmsValidationResult {
        private boolean allowed;
        private String message;
        private boolean shouldResetCounter;
        private int remainingAttempts;
    }

    public SmsValidationResult canMakeSmsRequest(String smsLastDateTime, int smsAttempt) {
        // If no previous attempts
        if (smsLastDateTime == null || smsLastDateTime.isEmpty()) {
            return SmsValidationResult.builder()
                    .allowed(true)
                    .message("First SMS request")
                    .shouldResetCounter(false)
                    .remainingAttempts(MAX_ATTEMPTS - 1)
                    .build();
        }

        try {
            // Parse the last SMS datetime
            LocalDateTime lastSmsDate = LocalDateTime.parse(smsLastDateTime, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            // Get current datetime in Malaysia timezone
            LocalDateTime nowInMalaysia = LocalDateTime.now(ZoneId.of("Asia/Kuala_Lumpur"));

            // Check if the dates are the same (comparing year, month, and day only)
            boolean isSameDay = lastSmsDate.toLocalDate().equals(nowInMalaysia.toLocalDate());

            // If it's a different day, signal reset and treat as first attempt
            if (!isSameDay) {
                return SmsValidationResult.builder()
                        .allowed(true)
                        .message("New day detected - SMS attempt counter should be reset")
                        .shouldResetCounter(true)
                        .remainingAttempts(MAX_ATTEMPTS - 1)
                        .build();
            }

            // Calculate remaining attempts for today
            int remainingAttempts = MAX_ATTEMPTS - smsAttempt;

            // If no attempts remaining for today, deny
            if (remainingAttempts <= 0) {
                return SmsValidationResult.builder()
                        .allowed(false)
                        .message(String.format("Daily limit of %d SMS requests reached. Please try again tomorrow.", MAX_ATTEMPTS))
                        .shouldResetCounter(false)
                        .remainingAttempts(0)
                        .build();
            }

            // Allow with remaining attempts info
            return SmsValidationResult.builder()
                    .allowed(true)
                    .message(String.format("SMS request allowed. %d of %d attempts remaining for today.",
                            remainingAttempts - 1, MAX_ATTEMPTS))
                    .shouldResetCounter(false)
                    .remainingAttempts(remainingAttempts - 1)
                    .build();

        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid datetime format. Expected format: YYYYMMDDHHMMSS", e);
        }
    }

    public String sendSms(String phoneNumber, String smsCode) {
        try {
            URI uri = UriComponentsBuilder
                    .newInstance()
                    .scheme("https")
                    .host("smsportal.exabytes.my")
                    .path("/isms_send_all_id.php")
                    .queryParam("un", username)
                    .queryParam("pwd", password)
                    .queryParam("dstno", phoneNumber)
                    .queryParam("msg", "Your OTP Code: " + smsCode)
                    .queryParam("type", "1")
                    .queryParam("sendid", "MYCAPRIO")
                    .queryParam("agreedterm", "YES")
                    .build()
                    .encode()
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            return response.getBody();
        } catch (Exception e) {
            throw new RunTimeException(SMS_SEND_EXCEPTION);
        }
    }
}