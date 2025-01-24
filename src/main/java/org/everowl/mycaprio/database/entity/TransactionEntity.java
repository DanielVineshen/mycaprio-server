package org.everowl.mycaprio.database.entity;

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

@Entity(name = "Transaction")
@Table(name = "`Transaction`")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trans_id")
    private Integer transId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference(value = "storeCustomer-transaction")
    @JoinColumn(name = "store_cust_id",
            nullable = false,
            referencedColumnName = "store_cust_id",
            foreignKey = @ForeignKey(name = "FK_STORE_CUSTOMER_TRANSACTION")
    )
    private StoreCustomerEntity storeCustomer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference(value = "admin-transaction")
    @JoinColumn(name = "admin_id",
            nullable = false,
            referencedColumnName = "admin_id",
            foreignKey = @ForeignKey(name = "FK_ADMIN_TRANSACTION")
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

    @Column(name = "trans_type", nullable = false)
    private String transType;

    @Column(name = "trans_desc", columnDefinition = "TEXT")
    private String transDesc;

    @Column(name = "trans_date", length = 14)
    private String transDate;

    @CreationTimestamp
    @Column(updatable = false, nullable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private Date updatedAt;
}
