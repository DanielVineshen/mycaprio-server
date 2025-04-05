package org.everowl.database.service.repository;

import org.everowl.database.service.entity.StoreCustomerVoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreCustomerVoucherRepository extends JpaRepository<StoreCustomerVoucherEntity, Integer> {
    @Query(value = """
             SELECT scv FROM Store_Customer_Voucher scv
             WHERE scv.storeCustomer.storeCustId = :storeCustId
            """
    )
    List<StoreCustomerVoucherEntity> findByStoreCustId(Integer storeCustId);

    @Query(value = """
             SELECT scv FROM Store_Customer_Voucher scv
             WHERE scv.storeCustomer.storeCustId = :storeCustId AND scv.isExclusive = false
            """
    )
    List<StoreCustomerVoucherEntity> findByStoreCustIdIsNotExclusive(Integer storeCustId);
}
