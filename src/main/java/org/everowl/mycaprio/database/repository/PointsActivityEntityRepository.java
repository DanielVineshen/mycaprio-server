package org.everowl.mycaprio.database.repository;

import org.everowl.mycaprio.database.entity.PointsActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointsActivityEntityRepository extends JpaRepository<PointsActivityEntity, Integer> {
}
