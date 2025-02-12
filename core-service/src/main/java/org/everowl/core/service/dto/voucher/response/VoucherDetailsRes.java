package org.everowl.core.service.dto.voucher.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoucherDetailsRes {
    private Integer voucherId;
    private Integer minTierLevel;
    private String voucherName;
    private String voucherDesc;
    private String voucherType;
    private Integer pointsRequired;
    private String attachmentName;
    private Boolean isAvailable;
    private String tncDesc;
    private Boolean isExclusive;
    private Integer lifeSpan;
    private String metaTag;
    private Integer quantityTotal;
}
