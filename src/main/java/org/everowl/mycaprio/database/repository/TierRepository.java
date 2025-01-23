package org.everowl.mycaprio.database.repository;

import org.everowl.mycaprio.database.entity.TierEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TierRepository extends JpaRepository<TierEntity, Integer> {
}
