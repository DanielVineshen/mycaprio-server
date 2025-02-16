package org.everowl.database.service.repository;

import org.everowl.database.service.entity.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<VoucherEntity, Integer> {
    @Query(value = """
             SELECT v FROM Voucher v
             WHERE v.store.storeId = :storeId
             ORDER BY v.voucherId ASC
            """
    )
    List<VoucherEntity> findByStoreId(Integer storeId);

    @Query(value = """
             SELECT v FROM Voucher v
             WHERE v.store.storeId = :storeId
             AND v.metaTag = :metaTag
             AND v.isAvailable = true
             AND v.isExclusive = true
            """
    )
    List<VoucherEntity> findAvailableStoreMetaTagVouchers(Integer storeId, String metaTag);

    Optional<VoucherEntity> findByAttachmentName(String attachmentName);
}
