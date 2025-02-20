package org.everowl.core.service.dto.store.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoresDetailsRes {
    private int storeId;
    private String storeName;
    private String createdAt;
}
