package org.everowl.core.service.service;

import org.everowl.core.service.dto.pointsActivity.request.CreateCustomerPointsAwardManual;
import org.everowl.core.service.dto.pointsActivity.request.CreateCustomerPointsAwardScan;
import org.everowl.core.service.dto.pointsActivity.response.PointsActivitiesDetails;
import org.everowl.shared.service.dto.GenericMessage;

public interface PointsActivityDomain {
    PointsActivitiesDetails getCustomerPointsActivitiesDetails(String loginId, Integer storeId);

    GenericMessage createCustomerPointsAwardScan(String loginId, CreateCustomerPointsAwardScan createCustomerPointsAwardScan);

    GenericMessage createCustomerPointsAwardManual(String loginId, CreateCustomerPointsAwardManual createCustomerPointsAwardManual);
}
