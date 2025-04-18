package org.everowl.database.service.repository;

import org.everowl.database.service.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, String> {
    @Query(value = """
             SELECT c FROM Customer c
             WHERE c.loginId = :loginId
            """
    )
    Optional<CustomerEntity> findByUsername(String loginId);
}
