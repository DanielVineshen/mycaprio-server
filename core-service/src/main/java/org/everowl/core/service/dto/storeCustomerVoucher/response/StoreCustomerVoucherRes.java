package org.everowl.core.service.dto.storeCustomerVoucher.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreCustomerVoucherRes {
    private List<StoreCustomerVoucherDetailsRes> vouchers;
}
