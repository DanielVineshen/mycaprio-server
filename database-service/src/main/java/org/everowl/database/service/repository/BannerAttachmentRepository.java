package org.everowl.database.service.repository;

import org.everowl.database.service.entity.BannerAttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerAttachmentRepository extends JpaRepository<BannerAttachmentEntity, Integer> {
}
