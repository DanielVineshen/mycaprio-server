package org.everowl.shared.service.config;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Existing String to String converter for date format conversion
        modelMapper.addConverter(new AbstractConverter<>() {
            @Override
            protected String convert(String source) {
                if (source == null) return null;
                try {
                    String pattern = source.length() == 8 ? "yyyyMMdd" : "yyyyMMddHHmmss";
                    String outputPattern = source.length() == 8 ? "yyyy-MM-dd" : "yyyy-MM-dd HH:mm:ss";
                    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(pattern);
                    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(outputPattern);
                    LocalDateTime date = pattern.equals("yyyyMMdd") ?
                            LocalDate.parse(source, inputFormatter).atStartOfDay() :
                            LocalDateTime.parse(source, inputFormatter);
                    return date.format(outputFormatter);
                } catch (Exception e) {
                    return source;
                }
            }
        }, String.class, String.class);

        // New Date to String converter (UTC to Malaysia time)
        modelMapper.addConverter(new AbstractConverter<Date, String>() {
            @Override
            protected String convert(Date source) {
                if (source == null) return null;
                try {
                    // Convert from UTC to Malaysia time zone
                    ZonedDateTime utcDateTime = source.toInstant().atZone(ZoneId.of("UTC"));
                    ZonedDateTime malaysiaDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("Asia/Kuala_Lumpur"));

                    // Format to yyyy-MM-dd HH:mm:ss
                    return malaysiaDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                } catch (Exception e) {
                    return source.toString();
                }
            }
        });

        return modelMapper;
    }
}