package org.everowl.core.service.service;

import org.everowl.core.service.dto.store.response.StoreRes;
import org.everowl.core.service.dto.store.response.StoresRes;

import java.util.List;

public interface StoreDomain {
    StoreRes getStoreDetails(Integer storeId);

    List<StoresRes> getStores();
}
