package org.everowl.core.service.service;

import org.everowl.core.service.dto.banner.request.CreateBannerReq;
import org.everowl.core.service.dto.banner.request.DeleteBannerReq;
import org.everowl.core.service.dto.banner.request.UpdateBannerReq;
import org.everowl.shared.service.dto.GenericMessage;

public interface BannerDomain {
    GenericMessage createBanner(CreateBannerReq bannerReq, String loginId);

    GenericMessage updateBanner(UpdateBannerReq bannerReq, String loginId);

    GenericMessage deleteBanner(DeleteBannerReq bannerReq, String loginId);

    String getBannerAttachment(String attachmentName);
}
