package org.everowl.core.service.service;

import org.everowl.core.service.dto.voucherRedemption.request.CustomerVoucherPurchaseReq;
import org.everowl.core.service.dto.voucherRedemption.request.CustomerVoucherRedemptionReq;
import org.everowl.core.service.dto.voucherRedemption.response.CustomerVoucherPurchaseDetailsRes;
import org.everowl.shared.service.dto.GenericMessage;

public interface VoucherRedemptionDomain {
    CustomerVoucherPurchaseDetailsRes createCustomerVoucherPurchase(String loginId, CustomerVoucherPurchaseReq customerVoucherPurchaseReq);

    GenericMessage createCustomerVoucherRedemption(String loginId, CustomerVoucherRedemptionReq customerVoucherRedemptionReq);
}
