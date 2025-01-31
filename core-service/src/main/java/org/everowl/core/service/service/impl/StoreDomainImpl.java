package org.everowl.core.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.banner.response.BannerRes;
import org.everowl.core.service.dto.store.response.StoreRes;
import org.everowl.core.service.dto.store.response.StoresRes;
import org.everowl.core.service.dto.voucher.response.VoucherRes;
import org.everowl.core.service.service.StoreDomain;
import org.everowl.database.service.entity.AdminEntity;
import org.everowl.database.service.entity.BannerAttachmentEntity;
import org.everowl.database.service.entity.StoreEntity;
import org.everowl.database.service.entity.VoucherEntity;
import org.everowl.database.service.repository.*;
import org.everowl.shared.service.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.everowl.shared.service.enums.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreDomainImpl implements StoreDomain {
    private final AdminRepository adminRepository;

    private final StoreRepository storeRepository;

    private final VoucherRepository voucherRepository;

    private final BannerAttachmentRepository bannerAttachmentRepository;

    @Override
    public StoreRes getStoreDetails(Integer storeId, String username) {
        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException(STORE_NOT_EXIST));

        List<VoucherEntity> vouchers = voucherRepository.findAllByStoreId(storeId);

        List<BannerAttachmentEntity> banners = bannerAttachmentRepository.findAllByStoreId(storeId);

        StoreRes storeDetails = new StoreRes();
        storeDetails.setStoreId(store.getStoreId());
        storeDetails.setStoreName(store.getStoreName());
        storeDetails.setVouchers(VoucherRes.fromVoucherList(vouchers));
        storeDetails.setBanners(BannerRes.fromBannerList(banners));

        return storeDetails;
    }

    @Override
    public List<StoresRes> getStores(String username) {
        AdminEntity admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(USER_NOT_AUTHORIZED));

        List<StoreEntity> stores = storeRepository.findAll();

        return StoresRes.fromStoreList(stores);
    }
}
