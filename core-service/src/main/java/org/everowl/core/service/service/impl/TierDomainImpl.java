package org.everowl.core.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.tier.response.TierRes;
import org.everowl.core.service.service.TierDomain;
import org.everowl.database.service.entity.TierEntity;
import org.everowl.database.service.repository.TierRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TierDomainImpl implements TierDomain {
    private final TierRepository tierRepository;

    @Override
    public List<TierRes> getAllTiers(Integer storeId) {
        List<TierEntity> tiers = tierRepository.findAllByStoreId(storeId);

        List<TierRes> tierList = new ArrayList<>();

        for (TierEntity tier : tiers) {

            TierRes tierDetails = new TierRes();
            tierDetails.setTierId(tier.getTierId());
            tierDetails.setTierLevel(tier.getTierLevel());
            tierDetails.setTierName(tier.getTierName());
            tierDetails.setTierMultiplier(tier.getTierMultiplier());
            tierDetails.setIsDefault(tier.getIsDefault());
            tierDetails.setPointsNeeded(tier.getPointsNeeded());

            tierList.add(tierDetails);
        }

        return tierList;
    }
}
