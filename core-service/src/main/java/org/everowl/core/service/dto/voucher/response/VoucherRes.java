package org.everowl.core.service.dto.voucher.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.everowl.database.service.entity.VoucherEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoucherRes {
    private Integer voucherId;
    private Integer minTierLevel;
    private String voucherName;
    private String voucherDesc;
    private Integer pointsRequired;
    private String attachmentName;
    private Boolean isAvailable;
    private String tncDesc;
    private Boolean isExclusive;
    private Integer lifeSpan;
    private String metaTag;

    public VoucherRes(VoucherEntity voucher) {
        setVoucherId(voucher.getVoucherId());
        setMinTierLevel(voucher.getMinTierLevel());
        setVoucherName(voucher.getVoucherName());
        setVoucherDesc(voucher.getVoucherDesc());
        setPointsRequired(voucher.getPointsRequired());
        setAttachmentName(voucher.getAttachmentName());
        setIsAvailable(voucher.getIsAvailable());
        setTncDesc(voucher.getTncDesc());
        setIsExclusive(voucher.getIsExclusive());
        setLifeSpan(voucher.getLifeSpan());
        setMetaTag(voucher.getMetaTag());
    }

    public static List<VoucherRes> fromVoucherList(List<VoucherEntity> vouchers) {
        return vouchers.stream()
                .map(VoucherRes::new)
                .collect(Collectors.toList());
    }
}
