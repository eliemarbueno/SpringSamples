package com.eliemarbueno.catalog.application.exception.v1.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.OffsetDateTime;

public record ErrorResponse(
  @JsonFormat(
    shape = JsonFormat.Shape.STRING,
    pattern = "yyyy-MM-dd'T'HH:mm:ssxxx"
  )
  OffsetDateTime date,
  int statusCode,
  String errorCode,
  String message,
  String requestedPath
) {}
