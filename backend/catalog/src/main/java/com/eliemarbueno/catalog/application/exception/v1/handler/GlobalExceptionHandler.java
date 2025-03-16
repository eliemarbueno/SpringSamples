package com.eliemarbueno.catalog.application.exception.v1.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.MDC;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.eliemarbueno.catalog.shared.util.v1.LogFunctions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex) {
		log.error(LogFunctions.getErrorMessage(ex), MDC.getCopyOfContextMap());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}
	
	
	@ExceptionHandler(IOException.class)
	public ResponseEntity<Object> handleIOException(IOException ex) {
		log.error(LogFunctions.getErrorMessage(ex), MDC.getCopyOfContextMap());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}
	
}