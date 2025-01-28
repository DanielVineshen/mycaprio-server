package org.everowl.mycaprio.database.repository;

import org.everowl.mycaprio.database.entity.StoreCustomerVoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreCustomerVoucherRepository extends JpaRepository<StoreCustomerVoucherEntity, Integer> {
}
