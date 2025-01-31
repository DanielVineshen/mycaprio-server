package org.everowl.core.service.service;

import org.everowl.core.service.dto.voucher.request.CreateVoucherReq;
import org.everowl.core.service.dto.voucher.request.DeleteVoucherReq;
import org.everowl.core.service.dto.voucher.request.UpdateVoucherReq;
import org.everowl.core.service.dto.voucher.response.VoucherRes;
import org.everowl.shared.service.dto.GenericMessage;

import java.util.List;

public interface VoucherDomain {
    GenericMessage createVoucher(CreateVoucherReq voucherReq, String username);
    GenericMessage updateVoucher(UpdateVoucherReq voucherReq, String username);
    GenericMessage deleteVoucher(DeleteVoucherReq voucherReq, String username);
    List<VoucherRes> getAllVouchers(Integer storeId);
}
