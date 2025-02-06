package org.everowl.core.service.dto.tier.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TierDetailsRes {
    private Integer tierId;
    private Integer tierLevel;
    private String tierName;
    private BigDecimal tierMultiplier;
    private Boolean isDefault;
    private Integer pointsNeeded;
}
