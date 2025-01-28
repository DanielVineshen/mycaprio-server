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
    private String loginId;
    private String userType;
    private String fullName;
}