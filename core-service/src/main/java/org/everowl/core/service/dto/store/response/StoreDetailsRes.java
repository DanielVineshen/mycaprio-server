package org.everowl.core.service.dto.store.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.everowl.core.service.dto.banner.response.BannerRes;
import org.everowl.core.service.dto.voucher.response.VoucherDetailsRes;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreDetailsRes {
    private Integer storeId;
    private String storeName;
    private List<VoucherDetailsRes> vouchers;
    private List<BannerRes> banners;
}
