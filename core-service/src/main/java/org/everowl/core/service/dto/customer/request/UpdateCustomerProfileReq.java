package org.everowl.core.service.dto.customer.request;

import lombok.Data;

@Data
public class UpdateCustomerProfileReq {
    private String emailAddress;
    private String fullName;
    private String gender;
    private String dateOfBirth;
}
