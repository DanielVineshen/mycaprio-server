package org.everowl.database.service.repository;

import org.everowl.database.service.entity.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Integer> {
    @Query(value = """
             SELECT f FROM Feedback f
             WHERE f.store.storeId = :storeId
            """
    )
    List<FeedbackEntity> findAllByStoreId(Integer storeId);
}
