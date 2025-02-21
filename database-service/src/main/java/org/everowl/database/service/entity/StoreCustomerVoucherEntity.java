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

@Entity(name = "Store_Customer_Voucher")
@Table(name = "`Store_Customer_Voucher`")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreCustomerVoucherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_cust_voucher_id")
    private Integer storeCustVoucherId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference(value = "storeCustomer-voucher")
    @JoinColumn(name = "store_cust_id",
            nullable = false,
            referencedColumnName = "store_cust_id",
            foreignKey = @ForeignKey(name = "FK_STORE_CUSTOMER_VOUCHER")
    )
    private StoreCustomerEntity storeCustomer;

    @Column(name = "min_tier_level", nullable = false)
    private Integer minTierLevel;

    @Column(name = "points_required", nullable = false)
    private Integer pointsRequired;

    @Column(name = "quantity_total", nullable = false)
    private Integer quantityTotal;

    @Column(name = "quantity_left", nullable = false)
    private Integer quantityLeft;

    @Column(name = "valid_date", nullable = false, length = 14)
    private String validDate;

    @Column(name = "voucher_name", nullable = false)
    private String voucherName;

    @Column(name = "voucher_desc", nullable = false, columnDefinition = "TEXT")
    private String voucherDesc;

    @Column(name = "voucher_type", nullable = false)
    private String voucherType;

    @Column(name = "voucher_value", nullable = false)
    private String voucherValue;

    @Column(name = "attachment_name")
    private String attachmentName;

    @Column(name = "attachment_path")
    private String attachmentPath;

    @Column(name = "attachment_size", columnDefinition = "BIGINT")
    private Long attachmentSize;

    @Column(name = "tnc_desc", nullable = false, columnDefinition = "TEXT")
    private String tncDesc;

    @Column(name = "is_exclusive", nullable = false, columnDefinition = "BOOLEAN")
    private Boolean isExclusive = false;

    @Column(name = "life_span", nullable = false)
    private Integer lifeSpan;

    @Column(name = "meta_tag")
    private String metaTag;

    @OneToMany(mappedBy = "storeCustomerVoucher", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "storeCustomerVoucher-voucherRedemption")
    private List<VoucherRedemptionEntity> voucherRedemptions;

    @CreationTimestamp
    @Column(updatable = false, nullable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private Date updatedAt;
}