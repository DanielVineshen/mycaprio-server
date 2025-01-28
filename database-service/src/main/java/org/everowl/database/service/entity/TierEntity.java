package org.everowl.database.service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity(name = "Tier")
@Table(name = "`Tier`")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TierEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tier_id")
    private Integer tierId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference(value = "store-tier")
    @JoinColumn(name = "store_id",
            nullable = false,
            referencedColumnName = "store_id",
            foreignKey = @ForeignKey(name = "FK_STORE_TIER")
    )
    private StoreEntity store;

    @Column(name = "tier_level", nullable = false)
    private Integer tierLevel;

    @Column(name = "tier_name", nullable = false)
    private String tierName;

    @Column(name = "tier_multiplier", nullable = false, columnDefinition = "DECIMAL(5,2)")
    private BigDecimal tierMultiplier = BigDecimal.valueOf(0);

    @Column(name = "is_default", nullable = false, columnDefinition = "BOOLEAN")
    private Boolean isDefault = false;

    @Column(name = "points_needed", nullable = false)
    private Integer pointsNeeded;

    @OneToMany(mappedBy = "tier", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "tier-storeCustomer")
    private List<StoreCustomerEntity> storeCustomers;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private Date updatedAt;
}
