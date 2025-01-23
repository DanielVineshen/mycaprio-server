package org.everowl.mycaprio.database.repository;

import org.everowl.mycaprio.database.entity.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<StaffEntity, Integer> {
}
