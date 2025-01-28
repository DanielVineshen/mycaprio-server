package org.everowl.database.service.repository;

import org.everowl.database.service.entity.StoreCustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreCustomerRepository extends JpaRepository<StoreCustomerEntity, Integer> {
}
