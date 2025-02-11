package org.everowl.scheduler.service.model.exclusiveVoucher;

import lombok.Data;

@Data
public class VoucherModel {
    private Integer voucherId;
    private Integer storeId;
    private Integer minTierLevel;
    private String voucherName;
    private String voucherDesc;
    private Integer pointsRequired;
    private String attachmentName;
    private String attachmentPath;
    private Long attachmentSize;
    private Boolean isAvailable;
    private String tncDesc;
    private Boolean isExclusive;
    private Integer lifeSpan;
    private String metaTag;
    private Integer quantityTotal;
}