package org.everowl.mycaprio.database.repository;

import org.everowl.mycaprio.database.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<TokenEntity, Integer> {
}
