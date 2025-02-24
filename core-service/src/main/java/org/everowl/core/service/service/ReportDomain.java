package org.everowl.core.service.service;

import org.everowl.core.service.service.impl.ReportDomainImpl;

import java.io.IOException;

public interface ReportDomain {
    ReportDomainImpl.ExcelReport generateCustomerSpendingReport(String loginId) throws IOException;

    ReportDomainImpl.ExcelReport generateStoreVoucherPurchaseAnalysisReport(String loginId) throws IOException;
}
