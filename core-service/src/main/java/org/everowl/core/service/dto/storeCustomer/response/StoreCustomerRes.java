package org.everowl.core.service.dto.storeCustomer.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.everowl.core.service.dto.tier.response.TierRes;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreCustomerRes {
    private Integer storeCustId;
    private Integer custId;
    private TierRes tier;
    private Integer tierPoints;
    private Integer availablePoints;
    private String lastTransDate;
    private Integer accumulatedPoints;
}
