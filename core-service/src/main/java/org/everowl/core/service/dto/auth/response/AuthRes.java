package org.everowl.core.service.dto.auth.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRes {
    private Integer tokenId;
    private String accessToken;
    private String refreshToken;
    private String custId;
    private String loginId;
    private String userType;
    private String fullName;
    private String emailAddress;
    private String gender;
    private String dateOfBirth;
}