package org.everowl.core.service.dto.customer.response;

import lombok.Data;

@Data
public class CustomerProfileRes {
    private String custId;
    private String loginId;
    private String emailAddress;
    private String fullName;
    private String gender;
    private String dateOfBirth;
}
