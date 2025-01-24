package org.everowl.mycaprio.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private Integer tokenId;
    private String accessToken;
    private String refreshToken;
    private String loginId;
    private String userType;
    private String fullName;
}