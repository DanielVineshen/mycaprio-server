package org.everowl.database.service.repository;

import org.everowl.database.service.entity.StoreCustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreCustomerRepository extends JpaRepository<StoreCustomerEntity, Integer> {
    @Query(value = """
             SELECT sc FROM Store_Customer sc
             WHERE sc.customer.custId = :custId AND sc.store.storeId = :storeId
            """
    )
    Optional<StoreCustomerEntity> findCustomerStoreProfile(String custId, Integer storeId);
}
