package org.everowl.database.service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "Points_Activity")
@Table(name = "`Points_Activity`")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PointsActivityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "points_activity_id")
    private Integer pointsActivityId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference(value = "storeCustomer-pointsActivity")
    @JoinColumn(name = "store_cust_id",
            nullable = false,
            referencedColumnName = "store_cust_id",
            foreignKey = @ForeignKey(name = "FK_STORE_CUSTOMER_POINTS_ACTIVITY")
    )
    private StoreCustomerEntity storeCustomer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference(value = "admin-pointsActivity")
    @JoinColumn(name = "admin_id",
            nullable = false,
            referencedColumnName = "admin_id",
            foreignKey = @ForeignKey(name = "FK_ADMIN_POINTS_ACTIVITY")
    )
    private AdminEntity admin;

    @Column(name = "cust_existing_points", nullable = false)
    private Integer custExistingPoints;

    @Column(name = "original_points", nullable = false)
    private Integer originalPoints;

    @Column(name = "points_multiplier", nullable = false, columnDefinition = "DECIMAL(5,2)")
    private BigDecimal pointsMultiplier = BigDecimal.valueOf(0);

    @Column(name = "awarded_points", nullable = false)
    private Integer awardedPoints;

    @Column(name = "activity_type", nullable = false)
    private String activityType;

    @Column(name = "activity_desc", columnDefinition = "TEXT")
    private String activityDesc;

    @Column(name = "activity_date", length = 14)
    private String activityDate;

    @CreationTimestamp
    @Column(updatable = false, nullable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private Date updatedAt;
}
