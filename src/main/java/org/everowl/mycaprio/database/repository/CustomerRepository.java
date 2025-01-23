package org.everowl.mycaprio.database.repository;

import org.everowl.mycaprio.database.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {
}
