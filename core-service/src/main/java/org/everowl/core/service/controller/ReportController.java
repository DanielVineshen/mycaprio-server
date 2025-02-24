package org.everowl.core.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.core.service.security.CustomUserDetails;
import org.everowl.core.service.service.ReportDomain;
import org.everowl.core.service.service.impl.ReportDomainImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class ReportController {
    private final ReportDomain reportDomain;

    @GetMapping(value = "/owner/customerSpendingReport")
    public @ResponseBody ResponseEntity<byte[]> generateCustomerSpendingReport(@AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {
        String loginId = userDetails.getUsername();

        ReportDomainImpl.ExcelReport report = reportDomain.generateCustomerSpendingReport(loginId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", report.fileName() + ".xlsx");

        return new ResponseEntity<>(report.data().toByteArray(), headers, HttpStatus.OK);
    }

    @GetMapping(value = "/owner/storeVoucherPurchaseAnalysis")
    public @ResponseBody ResponseEntity<byte[]> generateStoreVoucherPurchaseAnalysisReport(@AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {
        String loginId = userDetails.getUsername();

        ReportDomainImpl.ExcelReport report = reportDomain.generateStoreVoucherPurchaseAnalysisReport(loginId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", report.fileName() + ".xlsx");

        return new ResponseEntity<>(report.data().toByteArray(), headers, HttpStatus.OK);
    }
}
