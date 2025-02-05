package org.everowl.core.service.service;

import org.everowl.core.service.dto.storeCustomerVoucher.response.StoreCustomerVoucherRes;

import java.util.List;

public interface StoreCustomerVoucherDomain {
    List<StoreCustomerVoucherRes> getCustomerVoucher(Integer storeId, String username);
}
