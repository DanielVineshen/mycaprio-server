package org.everowl.core.service.dto.banner.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.everowl.shared.service.annotation.ValidInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteBannerReq {
    @ValidInteger(message = "Please ensure a valid attachment ID is provided")
    private Integer attachmentId;
}
