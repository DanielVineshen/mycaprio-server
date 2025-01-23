package org.everowl.mycaprio.shared.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenericMessage {
    private boolean status;
}
