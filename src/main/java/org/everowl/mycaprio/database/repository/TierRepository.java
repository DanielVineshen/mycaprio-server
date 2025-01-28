package org.everowl.mycaprio.database.repository;

import org.everowl.mycaprio.database.entity.TierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TierRepository extends JpaRepository<TierEntity, Integer> {
}
