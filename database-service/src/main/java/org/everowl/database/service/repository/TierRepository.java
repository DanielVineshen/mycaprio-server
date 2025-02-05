package org.everowl.database.service.repository;

import org.everowl.database.service.entity.TierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TierRepository extends JpaRepository<TierEntity, Integer> {
    @Query(value = """
             SELECT t FROM Tier t
             WHERE t.store.storeId = :storeId
            """
    )
    List<TierEntity> findAllByStoreId(Integer storeId);

    @Query(value = """
             SELECT t FROM Tier t
             WHERE t.store.storeId = :storeId AND t.tierLevel = 1
             ORDER BY t.tierId ASC
             LIMIT 1
            """
    )
    Optional<TierEntity> findStoreDefaultTier(Integer storeId);

    @Query(value = """
             SELECT t FROM Tier t
             WHERE t.store.storeId = :storeId AND t.tierLevel > :tierLevel
             ORDER BY t.tierId ASC
             LIMIT 1
            """
    )
    Optional<TierEntity> findStoreNextTier(Integer storeId, Integer tierLevel);
}
