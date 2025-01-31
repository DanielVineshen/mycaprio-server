package org.everowl.core.service.service;

import org.everowl.core.service.dto.customer.request.UpdateCustomerPassword;
import org.everowl.core.service.dto.customer.request.UpdateCustomerProfile;
import org.everowl.core.service.dto.customer.response.CustomerProfile;
import org.everowl.shared.service.dto.GenericMessage;

public interface CustomerDomain {
    CustomerProfile getCustomerProfile(String profileId);

    GenericMessage updateCustomerProfile(String profileId, UpdateCustomerProfile updateCustomerProfile);

    GenericMessage updateCustomerPassword(String profileId, UpdateCustomerPassword updateCustomerPassword);
}
