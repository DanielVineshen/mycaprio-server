package org.everowl.core.service.dto.storeCustomerVoucher.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreCustomerVoucherDetailsRes {
    private Integer storeCustVoucherId;
    private Integer minTierLevel;
    private Integer pointsRequired;
    private Integer quantityTotal;
    private Integer quantityLeft;
    private String validDate;
    private String voucherName;
    private String voucherDesc;
    private String tncDesc;
    private Boolean isExclusive;
    private Integer lifeSpan;
    private String metaTag;
}
