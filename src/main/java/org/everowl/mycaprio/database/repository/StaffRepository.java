package org.everowl.mycaprio.database.repository;

import org.everowl.mycaprio.database.entity.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<StaffEntity, Integer> {
    @Query(value = """
             SELECT s FROM Staff s
             WHERE s.loginId = :loginId
            """
    )
    Optional<StaffEntity> findByUsername(String loginId);
}
