package org.everowl.mycaprio.database.repository;

import org.everowl.mycaprio.database.entity.CatalogueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogueRepository extends JpaRepository<CatalogueEntity, Integer> {
}
