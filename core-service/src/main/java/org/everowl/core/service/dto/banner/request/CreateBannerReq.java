package org.everowl.core.service.dto.banner.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBannerReq {
    private MultipartFile attachment;
    private String attachmentType;
}
