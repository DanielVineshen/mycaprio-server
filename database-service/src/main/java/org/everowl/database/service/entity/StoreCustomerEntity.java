package org.everowl.database.service.entity;

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

@Entity(name = "Store_Customer")
@Table(name = "`Store_Customer`")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreCustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_cust_id")
    private Integer storeCustId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference(value = "customer-storeCustomer")
    @JoinColumn(name = "cust_id",
            nullable = false,
            referencedColumnName = "cust_id",
            foreignKey = @ForeignKey(name = "FK_CUSTOMER_STORE_CUSTOMER")
    )
    private CustomerEntity customer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference(value = "store-storeCustomer")
    @JoinColumn(name = "store_id",
            nullable = false,
            referencedColumnName = "store_id",
            foreignKey = @ForeignKey(name = "FK_STORE_STORE_CUSTOMER")
    )
    private StoreEntity store;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference(value = "tier-storeCustomer")
    @JoinColumn(name = "tier_id",
            nullable = false,
            referencedColumnName = "tier_id",
            foreignKey = @ForeignKey(name = "FK_TIER_STORE_CUSTOMER")
    )
    private TierEntity tier;

    @Column(name = "tier_points", nullable = false)
    private Integer tierPoints;

    @Column(name = "available_points", nullable = false)
    private Integer availablePoints;

    @Column(name = "last_trans_date", length = 14)
    private String lastTransDate;

    @Column(name = "accumulated_points", nullable = false)
    private Integer accumulatedPoints;

    @OneToMany(mappedBy = "storeCustomer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "storeCustomer-pointsActivity")
    private List<PointsActivityEntity> pointsActivities;

    @OneToMany(mappedBy = "storeCustomer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "storeCustomer-voucher")
    private List<StoreCustomerVoucherEntity> storeCustomerVouchers;

    @CreationTimestamp
    @Column(updatable = false, nullable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private Date updatedAt;
}
