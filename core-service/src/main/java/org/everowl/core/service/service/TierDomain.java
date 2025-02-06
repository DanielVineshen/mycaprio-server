package org.everowl.core.service.service;

import org.everowl.core.service.dto.tier.response.TierRes;

public interface TierDomain {
    TierRes getAllTiers(Integer storeId);
}
