package com.eliemarbueno.catalog.application.api.common.v1.controller;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eliemarbueno.catalog.application.api.common.v1.dto.ApiInfoResponseDTO;
import com.eliemarbueno.catalog.shared.constant.v1.ConstantsApiEndpoints;
import com.eliemarbueno.catalog.shared.util.v1.DateFunctions;
import com.eliemarbueno.catalog.shared.util.v1.LogFunctions;
import com.eliemarbueno.catalog.shared.util.v1.VersionFunctions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ApiInfoController {

	@Value("${application.name:Catalog}")
	private String applicationName;

	@Value("${application.description:Spring boot application test of Eliemar Bueno.}")
	private String applicationDescription;

	@GetMapping(ConstantsApiEndpoints.API_INFO)
	public ResponseEntity<ApiInfoResponseDTO> getApiInfo() {
		log.info(LogFunctions.getMethodWithClass() + " getting Api Info.", MDC.getCopyOfContextMap());
		var version = new VersionFunctions();

		return ResponseEntity.ok(new ApiInfoResponseDTO(applicationName, applicationDescription, version.getVersion(),
				"UP", DateFunctions.getNow()));
	}
}