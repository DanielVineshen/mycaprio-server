package org.everowl.core.service.service;

import org.everowl.core.service.dto.tier.response.TierRes;

import java.util.List;

public interface TierDomain {
    List<TierRes> getAllTiers(Integer storeId);
}
