package org.everowl.core.service.service;

import org.everowl.core.service.dto.pointsActivity.request.CreateCustomerPointsAwardManualReq;
import org.everowl.core.service.dto.pointsActivity.request.CreateCustomerPointsAwardScanReq;
import org.everowl.core.service.dto.pointsActivity.response.PointsActivitiesDetailsRes;
import org.everowl.shared.service.dto.GenericMessage;

public interface PointsActivityDomain {
    PointsActivitiesDetailsRes getCustomerPointsActivitiesDetails(String loginId, Integer storeId);

    GenericMessage createCustomerPointsAwardScan(String loginId, CreateCustomerPointsAwardScanReq createCustomerPointsAwardScanReq);

    GenericMessage createCustomerPointsAwardManual(String loginId, CreateCustomerPointsAwardManualReq createCustomerPointsAwardManualReq);
}
