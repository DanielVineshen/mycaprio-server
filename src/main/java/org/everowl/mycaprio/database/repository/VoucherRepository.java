package org.everowl.mycaprio.database.repository;

import org.everowl.mycaprio.database.entity.VoucherRedemptionEntity;
import org.everowl.mycaprio.database.entity.composite_keys.VoucherRedemptionPKs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<VoucherRedemptionEntity, VoucherRedemptionPKs> {
}
