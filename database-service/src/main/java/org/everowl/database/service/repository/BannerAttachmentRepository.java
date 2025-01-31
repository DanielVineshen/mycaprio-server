package org.everowl.database.service.repository;

import org.everowl.database.service.entity.BannerAttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerAttachmentRepository extends JpaRepository<BannerAttachmentEntity, Integer> {
    @Query(value = """
             SELECT ba FROM Banner_Attachment ba
             WHERE ba.store.storeId = :storeId
            """
    )
    List<BannerAttachmentEntity> findAllByStoreId(Integer storeId);
}
