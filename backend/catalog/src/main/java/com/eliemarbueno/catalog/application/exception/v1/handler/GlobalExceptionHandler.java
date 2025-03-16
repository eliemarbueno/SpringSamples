package com.eliemarbueno.catalog.application.exception.v1.handler;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.eliemarbueno.catalog.application.exception.v1.dto.ErrorResponse;
import com.eliemarbueno.catalog.shared.util.v1.DateFunctions;
import com.eliemarbueno.catalog.shared.util.v1.LogFunctions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex) {
//		log.error(LogFunctions.getErrorMessage(ex), MDC.getCopyOfContextMap());
//		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		String requestedPath = ex.getResourcePath();

		String message = String.format("Resource not found : %s. check source at src/main/resources/static or public",
				requestedPath);

		log.error(LogFunctions.getErrorMessage(ex) + message, MDC.getCopyOfContextMap());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(DateFunctions.getNow(),
				HttpStatus.NOT_FOUND.value(), "RESOURCE_NOT_FOUND", message, requestedPath));
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<Object> handleIOException(IOException ex) {
		log.error(LogFunctions.getErrorMessage(ex), MDC.getCopyOfContextMap());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

}