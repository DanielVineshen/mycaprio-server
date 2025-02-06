package org.everowl.core.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.dto.banner.response.BannerRes;
import org.everowl.core.service.dto.store.response.StoreDetailsRes;
import org.everowl.core.service.dto.store.response.StoreRes;
import org.everowl.core.service.dto.store.response.StoresDetailsRes;
import org.everowl.core.service.dto.store.response.StoresRes;
import org.everowl.core.service.dto.voucher.response.VoucherDetailsRes;
import org.everowl.core.service.service.StoreDomain;
import org.everowl.database.service.entity.BannerAttachmentEntity;
import org.everowl.database.service.entity.StoreEntity;
import org.everowl.database.service.entity.VoucherEntity;
import org.everowl.database.service.repository.*;
import org.everowl.shared.service.exception.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.everowl.shared.service.enums.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreDomainImpl implements StoreDomain {
    private final StoreRepository storeRepository;
    private final VoucherRepository voucherRepository;
    private final BannerAttachmentRepository bannerAttachmentRepository;
    private final ModelMapper modelMapper;

    @Override
    public StoreRes getStoreDetails(Integer storeId) {
        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException(STORE_NOT_EXIST));

        List<VoucherEntity> vouchers = voucherRepository.findAllByStoreId(storeId);
        List<VoucherDetailsRes> voucherList = new ArrayList<>();
        for (VoucherEntity voucher : vouchers) {
            VoucherDetailsRes voucherDetailsRes = modelMapper.map(voucher, VoucherDetailsRes.class);
            voucherList.add(voucherDetailsRes);
        }

        List<BannerAttachmentEntity> banners = bannerAttachmentRepository.findAllByStoreId(storeId);
        List<BannerRes> bannerList = new ArrayList<>();
        for (BannerAttachmentEntity banner : banners) {
            BannerRes bannerRes = modelMapper.map(banner, BannerRes.class);
            bannerList.add(bannerRes);
        }

        StoreDetailsRes storeDetails = new StoreDetailsRes();
        storeDetails.setStoreId(store.getStoreId());
        storeDetails.setStoreName(store.getStoreName());
        storeDetails.setVouchers(voucherList);
        storeDetails.setBanners(bannerList);

        StoreRes storeDetailList = new StoreRes();
        storeDetailList.setStore(storeDetails);

        return storeDetailList;
    }

    @Override
    public StoresRes getStores() {
        List<StoreEntity> stores = storeRepository.findAll();

        List<StoresDetailsRes> storeList = new ArrayList<>();
        for (StoreEntity store : stores) {
            StoresDetailsRes storesDetailsRes = modelMapper.map(store, StoresDetailsRes.class);
            storeList.add(storesDetailsRes);
        }

        StoresRes storeRes = new StoresRes();
        storeRes.setStores(storeList);

        return storeRes;
    }
}
