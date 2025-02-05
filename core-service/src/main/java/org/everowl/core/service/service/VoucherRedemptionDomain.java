package org.everowl.core.service.service;

import org.everowl.core.service.dto.voucherRedemption.request.CustomerVoucherPurchase;
import org.everowl.core.service.dto.voucherRedemption.request.CustomerVoucherRedemption;
import org.everowl.core.service.dto.voucherRedemption.response.CustomerVoucherPurchaseDetails;
import org.everowl.shared.service.dto.GenericMessage;

public interface VoucherRedemptionDomain {
    CustomerVoucherPurchaseDetails createCustomerVoucherPurchase(String loginId, CustomerVoucherPurchase customerVoucherPurchase);

    GenericMessage createCustomerVoucherRedemption(String loginId, CustomerVoucherRedemption customerVoucherRedemption);
}
