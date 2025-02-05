package org.everowl.core.service.dto.storeCustomerVoucher.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.everowl.database.service.entity.StoreCustomerVoucherEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreCustomerVoucherRes {
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

    public StoreCustomerVoucherRes(StoreCustomerVoucherEntity customerVoucher) {
        setStoreCustVoucherId(customerVoucher.getStoreCustVoucherId());
        setMinTierLevel(customerVoucher.getMinTierLevel());
        setPointsRequired(customerVoucher.getPointsRequired());
        setQuantityTotal(customerVoucher.getQuantityTotal());
        setQuantityLeft(customerVoucher.getQuantityLeft());
        setValidDate(customerVoucher.getValidDate());
        setVoucherName(customerVoucher.getVoucherName());
        setVoucherDesc(customerVoucher.getVoucherDesc());
        setTncDesc(customerVoucher.getTncDesc());
        setIsExclusive(customerVoucher.getIsExclusive());
        setLifeSpan(customerVoucher.getLifeSpan());
        setMetaTag(customerVoucher.getMetaTag());
    }

    public static List<StoreCustomerVoucherRes> fromStoreCustomerVoucherList(List<StoreCustomerVoucherEntity> customerVouchers) {
        return customerVouchers.stream()
                .map(StoreCustomerVoucherRes::new)
                .collect(Collectors.toList());
    }

}
