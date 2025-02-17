package org.everowl.database.service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity(name = "Voucher")
@Table(name = "`Voucher`")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "voucher_id")
    private Integer voucherId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference(value = "store-voucher")
    @JoinColumn(name = "store_id",
            nullable = false,
            referencedColumnName = "store_id",
            foreignKey = @ForeignKey(name = "FK_STORE_VOUCHER")
    )
    private StoreEntity store;

    @Column(name = "min_tier_level", nullable = false)
    private Integer minTierLevel;

    @Column(name = "voucher_name", nullable = false)
    private String voucherName;

    @Column(name = "voucher_desc", nullable = false, columnDefinition = "TEXT")
    private String voucherDesc;

    @Column(name = "voucher_type", nullable = false)
    private String voucherType;

    @Column(name = "voucher_value", nullable = false)
    private String voucherValue;

    @Column(name = "points_required", nullable = false)
    private Integer pointsRequired;

    @Column(name = "attachment_name")
    private String attachmentName;

    @Column(name = "attachment_path")
    private String attachmentPath;

    @Column(name = "attachment_size", columnDefinition = "BIGINT")
    private Integer attachmentSize;

    @Column(name = "is_available", nullable = false, columnDefinition = "BOOLEAN")
    private Boolean isAvailable = false;

    @Column(name = "tnc_desc", nullable = false, columnDefinition = "TEXT")
    private String tncDesc;

    @Column(name = "is_exclusive", nullable = false, columnDefinition = "BOOLEAN")
    private Boolean isExclusive = false;

    @Column(name = "life_span", nullable = false)
    private Integer lifeSpan;

    @Column(name = "meta_tag")
    private String metaTag;

    @Column(name = "quantity_total", nullable = false, columnDefinition = "integer default 1")
    private Integer quantityTotal = 1;

    @CreationTimestamp
    @Column(updatable = false, nullable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private Date updatedAt;
}
