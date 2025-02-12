package org.everowl.core.service.dto.voucher.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateVoucherReq {
    private Integer voucherId;
    private Integer minTierLevel;
    private String voucherName;
    private String voucherDesc;
    private String voucherType;
    private Integer pointsRequired;
    private MultipartFile attachment;
    private Boolean isAvailable;
    private String tncDesc;
    private Boolean isExclusive;
    private Integer lifeSpan;
    private String metaTag;
    private Integer quantityTotal;
}
