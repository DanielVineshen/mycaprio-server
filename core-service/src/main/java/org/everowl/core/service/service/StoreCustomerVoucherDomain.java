package org.everowl.core.service.service;

import org.everowl.core.service.dto.storeCustomerVoucher.response.StoreCustomerVoucherRes;

public interface StoreCustomerVoucherDomain {
    StoreCustomerVoucherRes getCustomerVoucher(Integer storeId, String loginId);
}
