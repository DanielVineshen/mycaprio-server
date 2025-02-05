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
            """
    )
    List<VoucherEntity> findAllByStoreId(Integer storeId);

    Optional<VoucherEntity> findByAttachmentName(String attachmentName);
}
