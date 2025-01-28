package org.everowl.database.service.repository;

import org.everowl.database.service.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Integer> {
    @Query(value = """
             SELECT a FROM Admin a
             WHERE a.loginId = :loginId
            """
    )
    Optional<AdminEntity> findByUsername(String loginId);
}
