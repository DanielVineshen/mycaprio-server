package org.everowl.mycaprio.database.repository;

import jakarta.transaction.Transactional;
import org.everowl.mycaprio.database.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Integer> {
    @Query(value = """
             SELECT t FROM Token t
             WHERE t.accessToken = :accessToken
            """
    )
    Optional<TokenEntity> findByToken(String accessToken);

    @Query(value = """
             SELECT t FROM Token t
             WHERE t.loginId = :loginId AND t.userType = :userType
            """
    )
    List<TokenEntity> findAllValidTokensByUser(String loginId, String userType);

    @Modifying
    @Transactional
    @Query(value = """
             DELETE FROM Token t
             WHERE t.accessToken = :accessToken
            """
    )
    void deleteTokenByAccessToken(@Param("accessToken") String accessToken);
}
