package org.everowl.mycaprio.database.repository;

import org.everowl.mycaprio.database.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<StoreEntity, Integer> {
}
