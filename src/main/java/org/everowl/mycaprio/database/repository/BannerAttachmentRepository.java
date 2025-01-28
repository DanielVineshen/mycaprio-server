package org.everowl.mycaprio.database.repository;

import org.everowl.mycaprio.database.entity.BannerAttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerAttachmentRepository extends JpaRepository<BannerAttachmentEntity, Integer> {
}
