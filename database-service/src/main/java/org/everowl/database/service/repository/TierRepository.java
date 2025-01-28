package org.everowl.database.service.repository;

import org.everowl.database.service.entity.TierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TierRepository extends JpaRepository<TierEntity, Integer> {
}
