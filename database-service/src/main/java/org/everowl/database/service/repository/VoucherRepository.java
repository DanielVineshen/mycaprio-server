package org.everowl.database.service.repository;

import org.everowl.database.service.entity.VoucherRedemptionEntity;
import org.everowl.database.service.entity.composite_keys.VoucherRedemptionPKs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<VoucherRedemptionEntity, VoucherRedemptionPKs> {
}
