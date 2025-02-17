package org.everowl.core.service.service;

import org.everowl.core.service.dto.voucherRedemption.request.CustomerVoucherPurchaseReq;
import org.everowl.core.service.dto.voucherRedemption.request.CustomerVoucherRedemptionReq;
import org.everowl.core.service.dto.voucherRedemption.response.CustomerVoucherCodeDetailsRes;
import org.everowl.core.service.dto.voucherRedemption.response.CustomerVoucherPurchaseDetailsRes;
import org.everowl.core.service.dto.voucherRedemption.response.CustomerVoucherRedemptionDetailsRes;
import org.everowl.shared.service.dto.GenericMessage;

public interface VoucherRedemptionDomain {
    CustomerVoucherPurchaseDetailsRes createCustomerVoucherPurchase(String loginId, CustomerVoucherPurchaseReq customerVoucherPurchaseReq);

    CustomerVoucherRedemptionDetailsRes createCustomerVoucherRedemption(String loginId, CustomerVoucherRedemptionReq customerVoucherRedemptionReq);

    CustomerVoucherCodeDetailsRes generateCustomerVoucherCode(String loginId, Integer storeCustomerVoucherId);
}
