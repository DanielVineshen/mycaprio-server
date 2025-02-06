package org.everowl.core.service.dto.storeCustomer.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.everowl.core.service.dto.tier.response.TierDetailsRes;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreCustomerDetailsRes {
    private Integer storeCustId;
    private TierDetailsRes tier;
    private Integer tierPoints;
    private Integer availablePoints;
    private String lastTransDate;
    private Integer accumulatedPoints;
}
