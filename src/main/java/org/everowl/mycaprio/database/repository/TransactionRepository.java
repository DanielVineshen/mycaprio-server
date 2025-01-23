package org.everowl.mycaprio.database.repository;

import org.everowl.mycaprio.database.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {
}
