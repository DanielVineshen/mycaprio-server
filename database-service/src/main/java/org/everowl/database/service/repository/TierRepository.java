package org.everowl.database.service.repository;

import org.everowl.database.service.entity.TierEntity;
import org.everowl.database.service.entity.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TierRepository extends JpaRepository<TierEntity, Integer> {
    @Query(value = """
             SELECT t FROM Tier t
             WHERE t.store.storeId = :storeId
            """
    )
    List<TierEntity> findAllByStoreId(Integer storeId);
}
