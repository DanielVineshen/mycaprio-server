package org.everowl.core.service.dto.customer.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.everowl.shared.service.annotation.ValidBirthDate;

@Data
public class UpdateCustomerProfileReq {
    private String emailAddress;
    private String fullName;
    private String gender;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Please ensure the date of birth is in YYYY-MM-DD format")
    @ValidBirthDate(message = "Please ensure the date of birth provided is a valid birth date")
    private String dateOfBirth;
}
