package org.everowl.database.service.repository;

import org.everowl.database.service.entity.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRedemptionRepository extends JpaRepository<VoucherEntity, Integer> {
}
