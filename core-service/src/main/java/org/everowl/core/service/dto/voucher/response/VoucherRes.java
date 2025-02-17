package org.everowl.core.service.dto.voucher.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoucherRes {
    public List<VoucherDetailsRes> vouchers;
}
