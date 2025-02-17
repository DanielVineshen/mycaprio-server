package org.everowl.database.service.repository;

import org.everowl.database.service.entity.VoucherRedemptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRedemptionRepository extends JpaRepository<VoucherRedemptionEntity, Integer> {
}
