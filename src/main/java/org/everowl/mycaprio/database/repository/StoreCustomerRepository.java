package org.everowl.mycaprio.database.repository;

import org.everowl.mycaprio.database.entity.StoreCustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreCustomerRepository extends JpaRepository<StoreCustomerEntity, Integer> {
}
