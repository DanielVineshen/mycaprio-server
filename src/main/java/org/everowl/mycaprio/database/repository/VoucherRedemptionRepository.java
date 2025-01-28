package org.everowl.mycaprio.database.repository;

import org.everowl.mycaprio.database.entity.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRedemptionRepository extends JpaRepository<VoucherEntity, Integer> {
}
