package org.everowl.core.service.dto.pointsActivity.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PointsActivityDetailsRes {
    private Integer pointsActivityId;
    private Integer custExistingPoints;
    private Integer originalPoints;
    private BigDecimal pointsMultiplier;
    private Integer finalisedPoints;
    private String activityType;
    private String activityDesc;
    private String activityDate;
}
