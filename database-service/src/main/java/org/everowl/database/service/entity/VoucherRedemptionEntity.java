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

@Entity(name = "Voucher_Redemption")
@Table(name = "`Voucher_Redemption`")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherRedemptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucher_redemption_id")
    private Integer voucherRedemptionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference(value = "voucherRedemption-storeCustomerVoucher")
    @JoinColumn(name = "store_cust_voucher_id",
            nullable = false,
            updatable = false,
            referencedColumnName = "store_cust_voucher_id",
            foreignKey = @ForeignKey(name = "FK_VOUCHER_REDEMPTION_STORE_CUSTOMER_VOUCHER")
    )
    private StoreCustomerVoucherEntity storeCustomerVoucher;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference(value = "voucherRedemption-admin")
    @JoinColumn(name = "admin_id",
            nullable = false,
            updatable = false,
            referencedColumnName = "admin_id",
            foreignKey = @ForeignKey(name = "FK_VOUCHER_REDEMPTION_ADMIN")
    )
    private AdminEntity admin;

    @CreationTimestamp
    @Column(updatable = false, nullable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private Date updatedAt;
}
