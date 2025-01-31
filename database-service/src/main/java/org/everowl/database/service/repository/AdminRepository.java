package org.everowl.database.service.repository;

import org.everowl.database.service.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Integer> {
    @Query(value = """
             SELECT a FROM Admin a
             WHERE a.loginId = :loginId
            """
    )
    Optional<AdminEntity> findByUsername(String loginId);

    @Query(value = """
             SELECT a FROM Admin a
             WHERE a.adminId = :adminId
            """
    )
    Optional<AdminEntity> findByAdminId(String adminId);

    @Query(value = """
             SELECT a FROM Admin a
             LEFT JOIN a.store s
             WHERE s.storeId = :storeId AND a.role = 'STAFF'
            """
    )
    List<AdminEntity> findStaffsByStoreId(Integer storeId);
}
