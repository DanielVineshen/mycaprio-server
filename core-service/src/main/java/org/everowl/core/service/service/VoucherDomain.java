package org.everowl.core.service.service;

import org.everowl.core.service.dto.voucher.request.CreateVoucherReq;
import org.everowl.core.service.dto.voucher.request.DeleteVoucherReq;
import org.everowl.core.service.dto.voucher.request.UpdateVoucherReq;
import org.everowl.core.service.dto.voucher.response.VoucherRes;
import org.everowl.shared.service.dto.GenericMessage;

import java.util.List;

public interface VoucherDomain {
    List<VoucherRes> getAllVouchers(Integer storeId);

    GenericMessage createVoucher(CreateVoucherReq voucherReq, String loginId);

    GenericMessage updateVoucher(UpdateVoucherReq voucherReq, String loginId);

    GenericMessage deleteVoucher(DeleteVoucherReq voucherReq, String loginId);

    String getVoucherAttachment(String attachmentName);
}
