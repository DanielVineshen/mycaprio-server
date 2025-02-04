package org.everowl.database.service.entity.compositeKeys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class VoucherRedemptionPKs implements Serializable {
    @Column(name = "store_cust_voucher_id")
    private Integer storeCustVoucherId;

    @Column(name = "admin_id")
    private Integer adminId;
}
