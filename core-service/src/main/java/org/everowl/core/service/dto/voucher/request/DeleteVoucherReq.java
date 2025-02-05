package org.everowl.core.service.dto.voucher.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.everowl.shared.service.annotation.ValidInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteVoucherReq {
    @ValidInteger(message = "Please ensure a valid voucher ID is provided")
    private String voucherId;
}
