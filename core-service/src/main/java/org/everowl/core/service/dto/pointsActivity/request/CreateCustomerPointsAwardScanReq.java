package org.everowl.core.service.dto.pointsActivity.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerPointsAwardScanReq {
    @NotBlank(message = "Please ensure the customer ID is not empty")
    private String custId;

    @NotNull(message = "Please ensure the amount spent is not empty")
    @Positive(message = "Please ensure the amount spent is greater than 0")
    private BigDecimal amountSpent;
}
