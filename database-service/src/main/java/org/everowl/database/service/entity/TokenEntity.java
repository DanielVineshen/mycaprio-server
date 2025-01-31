package org.everowl.database.service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity(name = "Token")
@Table(name = "`Token`", uniqueConstraints = {
        @UniqueConstraint(name = "UK_Token_access_token", columnNames = "access_token"),
        @UniqueConstraint(name = "UK_Token_refresh_token", columnNames = "refresh_token")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Integer tokenId;

    @Column(name = "login_id", nullable = false)
    private String loginId;

    @Column(name = "user_type", nullable = false)
    private String userType;

    @Column(name = "access_token", nullable = false, length = 510, unique = true)
    private String accessToken;

    @Column(name = "refresh_token", nullable = false, length = 510, unique = true)
    private String refreshToken;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent", length = 510)
    private String userAgent;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private Date updatedAt;
}
