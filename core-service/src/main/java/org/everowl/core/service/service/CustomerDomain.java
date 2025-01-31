package org.everowl.core.service.service;

import org.everowl.core.service.dto.customer.CustomerProfile;

public interface CustomerDomain {
    CustomerProfile getCustomerProfile(String profileId);
}
