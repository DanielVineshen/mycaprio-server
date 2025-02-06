package org.everowl.core.service.dto.voucherRedemption.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerVoucherRedemptionReq {
    @Min(value = 1, message = "Please ensure the store customer voucher ID is valid")
    @NotNull(message = "Please ensure the store customer voucher ID is not empty")
    private Integer storeCustVoucherId;
}
