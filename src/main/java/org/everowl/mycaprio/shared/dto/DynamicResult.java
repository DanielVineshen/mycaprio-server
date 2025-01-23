package org.everowl.mycaprio.shared.dto;

import lombok.Data;

import java.util.Map;

@Data
public class DynamicResult {
    private String message;
    private Map<String, String> placeholders;

    public DynamicResult(String message, Map<String, String> placeholders) {
        this.message = message;
        this.placeholders = placeholders;
    }
}