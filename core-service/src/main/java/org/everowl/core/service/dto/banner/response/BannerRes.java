package org.everowl.core.service.dto.banner.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.everowl.database.service.entity.BannerAttachmentEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BannerRes {
    private Integer attachmentId;
    private String attachmentName;
    private String attachmentPath;
    private String attachmentType;

    public BannerRes(BannerAttachmentEntity banner) {
        setAttachmentId(banner.getAttachmentId());
        setAttachmentName(banner.getAttachmentName());
        setAttachmentPath(banner.getAttachmentPath());
        setAttachmentType(banner.getAttachmentType());
    }

    public static List<BannerRes> fromBannerList(List<BannerAttachmentEntity> banners) {
        return banners.stream()
                .map(BannerRes::new)
                .collect(Collectors.toList());
    }
}
