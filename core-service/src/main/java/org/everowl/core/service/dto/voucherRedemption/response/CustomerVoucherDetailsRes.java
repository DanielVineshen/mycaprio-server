package org.everowl.core.service.dto.voucherRedemption.response;

import lombok.Data;

@Data
public class CustomerVoucherDetailsRes {
    private Integer storeCustVoucherId;
    private Integer minTierLevel;
    private Integer pointsRequired;
    private Integer quantityTotal;
    private Integer quantityLeft;
    private String validDate;
    private String voucherName;
    private String voucherDesc;
    private String voucherType;
    private String voucherValue;
    private String attachmentName;
    private String tncDesc;
    private Boolean isExclusive;
    private Integer lifeSpan;
    private String metaTag;
}
