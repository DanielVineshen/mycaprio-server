package org.everowl.shared.service.config;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
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
        return modelMapper;
    }
}