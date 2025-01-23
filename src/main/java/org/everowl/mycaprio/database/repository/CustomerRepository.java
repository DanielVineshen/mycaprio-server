package org.everowl.mycaprio.database.repository;

import org.everowl.mycaprio.database.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {
    @Query(value = """
             SELECT c FROM Customer c
             WHERE c.loginId = :loginId
            """
    )
    Optional<CustomerEntity> findByUsername(String loginId);
}
