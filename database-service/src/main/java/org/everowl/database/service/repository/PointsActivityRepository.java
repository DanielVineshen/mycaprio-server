package org.everowl.database.service.repository;

import org.everowl.database.service.entity.PointsActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointsActivityRepository extends JpaRepository<PointsActivityEntity, Integer> {
    @Query(value = """
             SELECT pa FROM Points_Activity pa
             WHERE pa.storeCustomer.storeCustId = :storeCustId
             AND pa.activityType = :activityType
             ORDER BY pa.activityDate ASC
            """
    )
    List<PointsActivityEntity> findByStoreCustIdAndActivityType(Integer storeCustId, String activityType);
}
