package org.everowl.core.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.everowl.core.service.service.ReportDomain;
import org.everowl.database.service.entity.*;
import org.everowl.database.service.repository.*;
import org.everowl.shared.service.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static org.everowl.shared.service.enums.ErrorCode.USER_NOT_EXIST;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportDomainImpl implements ReportDomain {
    private final AdminRepository adminRepository;
    private final PointsActivityRepository pointsActivityRepository;
    private final VoucherRepository voucherRepository;
    private final StoreCustomerVoucherRepository storeCustomerVoucherRepository;

    // Average days in a month (more precise value)
    private final BigDecimal avgDaysInMonth = new BigDecimal("30.436875"); // 365.2425/12

    public record ExcelReport(ByteArrayOutputStream data, String fileName) {
    }

    @Override
    public ExcelReport generateCustomerSpendingReport(String loginId) throws IOException {
        AdminEntity owner = adminRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        StoreEntity store = owner.getStore();

        List<StoreCustomerEntity> storeCustomerList = store.getStoreCustomers();

        String localDate = getLocalDate();

        String excelName = store.getStoreName() + " - Customer Spending Report - " + localDate;

        Workbook workbook = new XSSFWorkbook(); // Create new Excel workbook
        Sheet sheet = workbook.createSheet("Report");

        CellStyle headerStyle = getCellHeaderStyle(workbook);

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] columns = {"Customer ID", "Full Name", "Contact No.", "Date of Birth", "Total No. Awarded", "Lifetime Points", "Lifetime Spending (RM)",
                "Balance Points", "First Visit", "Last Visit", "Duration (Months)", "Monthly Average Points", "Monthly Average Spending (RM)",
                "Overall Average Spending (RM)", "Frequency (Days)", "Days Since Last Visit"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        // Create data rows
        int rowNum = 1;
        for (StoreCustomerEntity storeCustomer : storeCustomerList) {
            CustomerEntity customer = storeCustomer.getCustomer();

            // Ignore customer that have not fully registered yet
            if (customer.getPassword() == null) {
                continue;
            }

            List<PointsActivityEntity> pointsActivityList = pointsActivityRepository.findByStoreCustIdAndActivityType(storeCustomer.getStoreCustId(), "AWARD");
            BigDecimal totalVisits = new BigDecimal(pointsActivityList.size());
            BigDecimal totalOriginalAccumulatedPoints = pointsActivityList.stream()
                    .map(PointsActivityEntity::getOriginalPoints)
                    .map(BigDecimal::valueOf)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalFinalisedAccumulatedPoints = pointsActivityList.stream()
                    .map(PointsActivityEntity::getFinalisedPoints)
                    .map(BigDecimal::valueOf)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(customer.getCustId());
            row.createCell(1).setCellValue(customer.getFullName());
            row.createCell(2).setCellValue(customer.getLoginId());
            if (customer.getDateOfBirth() != null) {
                setCellDateValue(customer.getDateOfBirth(), "yyyyMMdd", row, workbook, 3);
            }
            row.createCell(4).setCellValue(totalVisits.intValue());
            row.createCell(5).setCellValue(totalFinalisedAccumulatedPoints.intValue());
            row.createCell(6).setCellValue(totalOriginalAccumulatedPoints.divide(BigDecimal.TEN, 2, RoundingMode.HALF_UP).intValue());
            row.createCell(7).setCellValue(storeCustomer.getAvailablePoints());
            if (!pointsActivityList.isEmpty()) {
                String formattedFirstDate = pointsActivityList.getFirst().getActivityDate();
                String formattedLastDate = pointsActivityList.getLast().getActivityDate();

                setCellDateValue(formattedFirstDate, "yyyyMMddHHmmss", row, workbook, 8);
                setCellDateValue(formattedLastDate, "yyyyMMddHHmmss", row, workbook, 9);
                BigDecimal totalDuration = calculateDurationInMonths(formattedFirstDate, formattedLastDate);
                row.createCell(10).setCellValue(totalDuration.doubleValue());
                row.createCell(11).setCellValue(totalFinalisedAccumulatedPoints.divide(totalDuration, 2, RoundingMode.HALF_UP).doubleValue());
                row.createCell(12).setCellValue(totalOriginalAccumulatedPoints.divide(totalDuration, 2, RoundingMode.HALF_UP)
                        .divide(BigDecimal.TEN, 2, RoundingMode.HALF_UP).doubleValue());
                BigDecimal totalVisitsDiv = totalVisits.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : totalVisits;
                row.createCell(13).setCellValue(totalOriginalAccumulatedPoints.divide(totalVisitsDiv, 2, RoundingMode.HALF_UP).doubleValue());
                row.createCell(14).setCellValue(totalDuration.multiply(avgDaysInMonth).divide(totalVisitsDiv, 2, RoundingMode.HALF_UP).doubleValue());
                BigDecimal daysSinceLastVisit = calculateDurationInDays(formattedFirstDate, formattedLastDate);
                row.createCell(15).setCellValue(daysSinceLastVisit.doubleValue());
            } else {
                row.createCell(8).setCellValue("");
                row.createCell(9).setCellValue("");
                row.createCell(10).setCellValue(0);
                row.createCell(11).setCellValue(0);
                row.createCell(12).setCellValue(0);
                row.createCell(13).setCellValue(0);
                row.createCell(14).setCellValue(0);
                row.createCell(15).setCellValue(0);
            }
        }

        setSheetColumnWidth(columns, sheet, headerStyle);

        // Write to ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return new ExcelReport(outputStream, excelName);
    }

    @Override
    public ExcelReport generateStoreVoucherPurchaseAnalysisReport(String loginId) throws IOException {
        AdminEntity owner = adminRepository.findByUsername(loginId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_EXIST));

        StoreEntity store = owner.getStore();

        List<StoreCustomerEntity> storeCustomerList = store.getStoreCustomers();

        List<VoucherEntity> voucherList = voucherRepository.findAvailableStorePurchasableVouchers(store.getStoreId());
        voucherList.sort(Comparator.comparing(VoucherEntity::getTotalPurchase).reversed());

        String localDate = getLocalDate();

        String excelName = store.getStoreName() + " - Store Voucher Purchase Analysis Report - " + localDate;

        Workbook workbook = new XSSFWorkbook(); // Create new Excel workbook
        Sheet sheet1 = workbook.createSheet("Accumulate Purchase Stats");
        Sheet sheet2 = workbook.createSheet("Customer Purchase Stats");

        CellStyle headerStyle = getCellHeaderStyle(workbook);

        // Create header row
        Row headerRow1 = sheet1.createRow(0);
        String[] columns1 = {"Voucher Name", "Points Required", "No. of Purchase", "Minimum Tier Level", "Available", "Exclusive", "Meta Tag", "Lifespan"};
        for (int i = 0; i < columns1.length; i++) {
            Cell cell = headerRow1.createCell(i);
            cell.setCellValue(columns1[i]);
            cell.setCellStyle(headerStyle);
        }
        Row headerRow2 = sheet2.createRow(0);
        String[] columns2 = {"Customer ID", "Full Name", "Contact No.", "Email Address", "Date of Birth", "Gender", "No. of Vouchers Purchase",
                "Top 1 - Voucher Name", "Top 1 - Quantity Purchase", "Top 2 - Voucher Name", "Top 2 - Quantity Purchase", "Top 3 - Voucher Name", "Top 3 - Quantity Purchase",
                "Top 4 - Voucher Name", "Top 4 - Quantity Purchase", "Top 5 - Voucher Name", "Top 5 - Quantity Purchase"};
        for (int i = 0; i < columns2.length; i++) {
            Cell cell = headerRow2.createCell(i);
            cell.setCellValue(columns2[i]);
            cell.setCellStyle(headerStyle);
        }

        // Create data rows
        int rowNum1 = 1;
        for (VoucherEntity voucher : voucherList) {
            Row row = sheet1.createRow(rowNum1++);

            row.createCell(0).setCellValue(voucher.getVoucherName());
            row.createCell(1).setCellValue(voucher.getPointsRequired());
            row.createCell(2).setCellValue(voucher.getTotalPurchase());
            row.createCell(3).setCellValue(voucher.getMinTierLevel());
            row.createCell(4).setCellValue(voucher.getIsAvailable().equals(true) ? "yes" : "no");
            row.createCell(5).setCellValue(voucher.getIsExclusive().equals(true) ? "yes" : "no");
            row.createCell(6).setCellValue(voucher.getMetaTag());
            row.createCell(7).setCellValue(voucher.getLifeSpan());
        }
        int rowNum2 = 1;
        for (StoreCustomerEntity storeCustomer : storeCustomerList) {
            CustomerEntity customer = storeCustomer.getCustomer();

            List<StoreCustomerVoucherEntity> storeCustomerVoucherList = storeCustomerVoucherRepository.findByStoreCustIdIsNotExclusive(storeCustomer.getStoreCustId());

            List<VoucherMetadataEntity> voucherMetadataList = storeCustomer.getVoucherMetadata();
            voucherMetadataList.sort(Comparator.comparing(VoucherMetadataEntity::getTotalPurchase).reversed());

            // Ignore customer that have not fully registered yet
            if (customer.getPassword() == null) {
                continue;
            }

            Row row = sheet2.createRow(rowNum2++);

            row.createCell(0).setCellValue(customer.getCustId());
            row.createCell(1).setCellValue(customer.getFullName());
            row.createCell(2).setCellValue(customer.getLoginId());
            row.createCell(3).setCellValue(customer.getEmailAddress());
            if (customer.getDateOfBirth() != null) {
                setCellDateValue(customer.getDateOfBirth(), "yyyyMMdd", row, workbook, 4);
            }
            row.createCell(5).setCellValue(customer.getGender());
            row.createCell(6).setCellValue(storeCustomerVoucherList.size());
            if (!voucherMetadataList.isEmpty()) {
                row.createCell(7).setCellValue(voucherMetadataList.getFirst().getVoucher().getVoucherName());
                row.createCell(8).setCellValue(voucherMetadataList.getFirst().getTotalPurchase());

                if (voucherMetadataList.size() >= 2) {
                    row.createCell(9).setCellValue(voucherMetadataList.get(1).getVoucher().getVoucherName());
                    row.createCell(10).setCellValue(voucherMetadataList.get(1).getTotalPurchase());
                }

                if (voucherMetadataList.size() >= 3) {
                    row.createCell(11).setCellValue(voucherMetadataList.get(2).getVoucher().getVoucherName());
                    row.createCell(12).setCellValue(voucherMetadataList.get(2).getTotalPurchase());
                }

                if (voucherMetadataList.size() >= 4) {
                    row.createCell(13).setCellValue(voucherMetadataList.get(3).getVoucher().getVoucherName());
                    row.createCell(14).setCellValue(voucherMetadataList.get(3).getTotalPurchase());
                }

                if (voucherMetadataList.size() >= 5) {
                    row.createCell(15).setCellValue(voucherMetadataList.get(4).getVoucher().getVoucherName());
                    row.createCell(16).setCellValue(voucherMetadataList.get(4).getTotalPurchase());
                }
            }
        }

        setSheetColumnWidth(columns1, sheet1, headerStyle);
        setSheetColumnWidth(columns2, sheet2, headerStyle);

        // Write to ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return new ExcelReport(outputStream, excelName);
    }

    private static String getLocalDate() {
        ZoneId malaysiaZone = ZoneId.of("Asia/Kuala_Lumpur");
        LocalDateTime malaysiaDateTime = LocalDateTime.now(malaysiaZone);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String localDate = malaysiaDateTime.format(formatter);
        return localDate;
    }

    private static CellStyle getCellHeaderStyle(Workbook workbook) {
        // Create header style
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return headerStyle;
    }

    private static void setSheetColumnWidth(String[] columns, Sheet sheet, CellStyle headerStyle) {
        // Autosize columns with minimum header width
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);

            // Create a temporary row and cell to measure header width
            Row tempRow = sheet.createRow(sheet.getLastRowNum() + 1);
            Cell tempCell = tempRow.createCell(i);
            tempCell.setCellValue(columns[i]);
            tempCell.setCellStyle(headerStyle);

            // Autosize based on both header and data
            sheet.autoSizeColumn(i);

            // Get width after autosize
            int currentWidth = sheet.getColumnWidth(i);

            // Add padding (2 characters worth)
            int padding = 2 * 256;
            sheet.setColumnWidth(i, currentWidth + padding);

            // Remove temporary row
            sheet.removeRow(tempRow);
        }
    }

    private void setCellDateValue(String dateStr, String inputFormat, Row row, Workbook workbook, int cellIndex) {
        try {
            Date date;
            if (inputFormat.contains("HH")) {  // Check if format includes time
                LocalDateTime localDateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(inputFormat));
                date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            } else {
                LocalDate localDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(inputFormat));
                date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            }

            Cell dateCell = row.createCell(cellIndex);
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(workbook.createDataFormat().getFormat("dd-MM-yyyy"));
            dateCell.setCellStyle(dateStyle);
            dateCell.setCellValue(date);
        } catch (Exception e) {
            log.error("Error parsing date: {} with format {} - {}", dateStr, inputFormat, e.getMessage());
            Cell dateCell = row.createCell(cellIndex);
            dateCell.setCellValue("Invalid Date");
        }
    }

    public BigDecimal calculateDurationInMonths(String startDateStr, String endDateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime startDate = LocalDateTime.parse(startDateStr, formatter);
            LocalDateTime endDate = LocalDateTime.parse(endDateStr, formatter);

            // Calculate days between dates
            BigDecimal days = new BigDecimal(ChronoUnit.DAYS.between(startDate, endDate));

            // Calculate months with higher precision
            BigDecimal months = days.divide(avgDaysInMonth, 2, RoundingMode.HALF_UP);
            // Ensure minimum 1 month is provided to avoid divison error
            if (months.compareTo(BigDecimal.ZERO) == 0) {
                months = BigDecimal.ONE;
            }
            return months;
        } catch (Exception e) {
            log.error("Error calculating duration: {}", e.getMessage());
            return BigDecimal.ONE;
        }
    }

    public static BigDecimal calculateDurationInDays(String startDateStr, String endDateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime startDate = LocalDateTime.parse(startDateStr, formatter);
            LocalDateTime endDate = LocalDateTime.parse(endDateStr, formatter);

            // Calculate total hours between dates for more precision
            long hours = ChronoUnit.HOURS.between(startDate, endDate);
            long remainingMinutes = ChronoUnit.MINUTES.between(startDate.plusHours(hours), endDate);

            // Convert to days with decimal points (24 hours = 1 day)
            BigDecimal totalHours = new BigDecimal(hours)
                    .add(new BigDecimal(remainingMinutes).divide(new BigDecimal("60"), 10, RoundingMode.HALF_UP));
            BigDecimal days = totalHours.divide(new BigDecimal("24"), 2, RoundingMode.HALF_UP);
            return days;
        } catch (Exception e) {
            log.error("Error calculating duration: {}", e.getMessage());
            return BigDecimal.ZERO;
        }
    }
}
