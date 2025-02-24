package org.everowl.core.service.service.shared;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.database.service.entity.*;
import org.everowl.database.service.repository.VoucherMetadataRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoucherMetadataService {
    private final VoucherMetadataRepository voucherMetadataRepository;

    public VoucherMetadataEntity getOrCreateVoucherMetadata(StoreCustomerEntity storeCustomer, VoucherEntity voucher) {
        Optional<VoucherMetadataEntity> voucherMetadataEntity = voucherMetadataRepository.findCustomerVoucherMetadata(storeCustomer.getStoreCustId(), voucher.getVoucherId());

        return voucherMetadataEntity.orElseGet(() -> {
            VoucherMetadataEntity newVoucherMetadata = new VoucherMetadataEntity();
            newVoucherMetadata.setStoreCustomer(storeCustomer);
            newVoucherMetadata.setVoucher(voucher);
            newVoucherMetadata.setTotalPurchase(0);
            return voucherMetadataRepository.save(newVoucherMetadata);
        });
    }
}
