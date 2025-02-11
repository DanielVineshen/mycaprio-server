package org.everowl.scheduler.service.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.everowl.database.service.entity.CustomerEntity;
import org.everowl.database.service.entity.StoreCustomerEntity;
import org.everowl.database.service.repository.CustomerRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExclusiveVoucherJob {
    private final CustomerRepository customerRepository;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Kuala_Lumpur")
    @Transactional(readOnly = true)
    public void birthdayVoucherJob() {
        log.info("Birthday Voucher Job Scheduler started at {}", LocalDateTime.now());

        List<CustomerEntity> customerEntityList = customerRepository.findAll();
        for (CustomerEntity customer : customerEntityList) {
            if (isOlderThanSixMonths(customer.getCreatedAt())) {
                // Date is older than 6 months in Malaysian time
                if (isDateWithinBirthdayWindow(customer.getDateOfBirth())) {
                    // The birthday falls within the window
                    log.info("Customer ID: {} birthday is {}", customer.getCustId(), customer.getDateOfBirth());

                    List<StoreCustomerEntity> storeCustomerEntityList = customer.getStoreCustomers();

                }
            }
        }
    }

    private boolean isOlderThanSixMonths(Date createdAt) {
        // Define Malaysian time zone
        ZoneId malaysiaZone = ZoneId.of("Asia/Kuala_Lumpur");

        // Convert Date to LocalDateTime in Malaysian time
        LocalDateTime createdAtMalaysia = createdAt.toInstant()
                .atZone(malaysiaZone)
                .toLocalDateTime();

        // Get date 6 months ago in Malaysian time
        LocalDateTime sixMonthsAgo = LocalDateTime.now(malaysiaZone)
                .minusMonths(6);

        // Compare dates
        return createdAtMalaysia.isBefore(sixMonthsAgo);
    }

    private boolean isDateWithinBirthdayWindow(String dateOfBirth) {
        ZoneId malaysiaZone = ZoneId.of("Asia/Kuala_Lumpur");

        // Parse dateOfBirth format YYYYMMDD
        LocalDate birthDate = LocalDate.parse(dateOfBirth,
                DateTimeFormatter.ofPattern("yyyyMMdd"));

        // Get current date in Malaysia
        LocalDate today = LocalDate.now(malaysiaZone);

        // Create this year's birthday
        LocalDate thisBirthday = birthDate.withYear(today.getYear());

        // Calculate the window (7 days before birthday)
        LocalDate sevenDaysBefore = thisBirthday.minusDays(7);
        LocalDate eightDaysBefore = thisBirthday.minusDays(8);

        // Check if today falls between 8 and 7 days before birthday
        return today.isAfter(eightDaysBefore) && today.isBefore(sevenDaysBefore.plusDays(1));
    }
}
