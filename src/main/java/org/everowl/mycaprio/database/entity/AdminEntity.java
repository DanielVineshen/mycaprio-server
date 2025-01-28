package org.everowl.mycaprio.database.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity(name = "Admin")
@Table(name = "`Admin`")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("ADMIN")
public class AdminEntity extends BaseUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Integer adminId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference(value = "store-admin")
    @JoinColumn(name = "store_id",
            nullable = false,
            referencedColumnName = "store_id",
            foreignKey = @ForeignKey(name = "FK_STORE_ADMIN")
    )
    private StoreEntity store;

    @Column(name = "role", nullable = false)
    private String role;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "admin-pointsActivity")
    private List<PointsActivityEntity> pointsActivities;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "admin-voucherRedemption")
    private List<VoucherRedemptionEntity> voucherRedemptions;

    @CreationTimestamp
    @Column(updatable = false, nullable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private Date updatedAt;
}
