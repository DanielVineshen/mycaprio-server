package org.everowl.core.service.dto.customer.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TierProfile {
    private Integer tierId;
    private String tierLevel;
    private String tierName;
    private BigDecimal tierMultiplier;
    private Boolean isDefault;
    private Integer pointsNeeded;
}
