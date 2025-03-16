package com.eliemarbueno.catalog.application.exception.v1.dto;

import java.time.OffsetDateTime;

public record ErrorResponse(
    OffsetDateTime date,
	int statusCode,
    String errorCode,
    String message,
    String requestedPath
) {}