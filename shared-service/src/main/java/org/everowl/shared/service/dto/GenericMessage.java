package org.everowl.shared.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenericMessage {
    private boolean status;
}
