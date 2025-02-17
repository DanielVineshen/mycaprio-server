package org.everowl.core.service.dto.customer.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerStoreProfileRes extends CustomerProfileRes {
    private StoreCustomerProfile storeCustomer;
}
