package com.eliemarbueno.catalog.application.exception.v1.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ErrorResponse(
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssxxx") OffsetDateTime date,
		int statusCode, String errorCode, String message, String requestedPath) {
}
