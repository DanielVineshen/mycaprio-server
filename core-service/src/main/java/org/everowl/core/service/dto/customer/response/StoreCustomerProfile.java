package org.everowl.core.service.dto.customer.response;

import lombok.Data;

@Data
public class StoreCustomerProfile {
    private Integer storeCustId;
    private TierProfile tier;
    private Integer tierPoints;
    private Integer availablePoints;
    private String lastTransDate;
    private Integer accumulatedPoints;
}
