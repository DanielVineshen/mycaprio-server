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

@Entity(name = "Voucher_Metadata")
@Table(name = "`Voucher_Metadata`")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherMetadataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucher_metadata_id")
    private Integer voucherMetadataId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference(value = "voucherMetadata-storeCustomer")
    @JoinColumn(name = "store_cust_id",
            nullable = false,
            updatable = false,
            referencedColumnName = "store_cust_id",
            foreignKey = @ForeignKey(name = "FK_VOUCHER_METADATA_STORE_CUSTOMER")
    )
    private StoreCustomerEntity storeCustomer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference(value = "voucherMetadata-voucher")
    @JoinColumn(name = "voucher_id",
            nullable = false,
            updatable = false,
            referencedColumnName = "voucher_id",
            foreignKey = @ForeignKey(name = "FK_VOUCHER_METADATA_VOUCHER")
    )
    private VoucherEntity voucher;

    @Column(name = "total_purchase", nullable = false, columnDefinition = "integer default 0")
    private Integer totalPurchase = 0;

    @CreationTimestamp
    @Column(updatable = false, nullable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private Date updatedAt;
}
