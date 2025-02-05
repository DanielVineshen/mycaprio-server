package org.everowl.core.service.service;

import org.everowl.core.service.dto.banner.request.CreateBannerReq;
import org.everowl.core.service.dto.banner.request.DeleteBannerReq;
import org.everowl.core.service.dto.banner.request.UpdateBannerReq;
import org.everowl.shared.service.dto.GenericMessage;

public interface BannerDomain {
    GenericMessage createBanner(CreateBannerReq bannerReq, String username);

    GenericMessage updateBanner(UpdateBannerReq bannerReq, String username);

    GenericMessage deleteBanner(DeleteBannerReq bannerReq, String username);

    String getBannerAttachment(String attachmentName);
}
