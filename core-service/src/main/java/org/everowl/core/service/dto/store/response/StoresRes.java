package org.everowl.core.service.dto.store.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.everowl.database.service.entity.StoreEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoresRes {
    private int storeId;
    private String storeName;

    public StoresRes(StoreEntity store) {
        setStoreId(store.getStoreId());
        setStoreName(store.getStoreName());
    }

    public static List<StoresRes> fromStoreList(List<StoreEntity> stores) {
        return stores.stream()
                .map(StoresRes::new)
                .collect(Collectors.toList());
    }
}
