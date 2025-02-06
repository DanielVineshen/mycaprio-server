package org.everowl.core.service.service;

import org.everowl.core.service.dto.storeCustomer.response.StoreCustomerRes;

public interface StoreCustomerDomain {
    StoreCustomerRes getStoreCustomerDetails(Integer storeId, String loginId);
}
