package com.eliemarbueno.catalog.application.api.common.v1.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ApiInfoResponseDTO(String name, String description, String version, String status,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssxxx") OffsetDateTime date) {
}