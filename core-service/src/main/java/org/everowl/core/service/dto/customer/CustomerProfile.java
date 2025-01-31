package org.everowl.core.service.dto.customer;

import lombok.Data;

@Data
public class CustomerProfile {
    private String custId;
    private String loginId;
    private String emailAddress;
    private String fullName;
    private String gender;
    private String dateOfBirth;
}
