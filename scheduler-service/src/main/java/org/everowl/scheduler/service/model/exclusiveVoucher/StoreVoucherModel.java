package org.everowl.scheduler.service.model.exclusiveVoucher;

import lombok.Data;

import java.util.List;

@Data
public class StoreVoucherModel {
    private Integer storeId;
    private String storeName;
    private List<VoucherModel> vouchers;
}
