package org.everowl.database.service.repository;

import org.everowl.database.service.entity.VoucherMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VoucherMetadataRepository extends JpaRepository<VoucherMetadataEntity, Integer> {
    @Query(value = """
             SELECT vm FROM Voucher_Metadata vm
             WHERE vm.storeCustomer.storeCustId = :storeCustId AND vm.voucher.voucherId = :voucherId
            """
    )
    Optional<VoucherMetadataEntity> findCustomerVoucherMetadata(Integer storeCustId, Integer voucherId);
}
