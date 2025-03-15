package com.eliemarbueno.catalog.application.api.common.v1.dto;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public record ApiInfoResponseDTO(
    String name,
    String description,
    String version,
    String status,
    long timestamp
) {
    public ZonedDateTime getFormattedTimestamp() {
        return ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(timestamp),
            ZoneId.systemDefault()
        );
    }
}