package com.eliemarbueno.catalog.application.api.common.v1.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.eliemarbueno.catalog.shared.constant.v1.ConstantsApiEndpoints;
import com.eliemarbueno.catalog.shared.util.v1.VersionFunctions;

@ActiveProfiles("test")
@WebMvcTest(ApiInfoController.class)
class ApiInfoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private VersionFunctions versionFunctions;

	@Value("${application.name:Catalog}")
	private String applicationName;

	@Value("${application.description:Spring boot application test of Eliemar Bueno.}")
	private String applicationDescription;

	@Test
	void testGetApiInfo() throws Exception {
		String dynamicVersion = "0.0.1";

		when(versionFunctions.getVersion()).thenReturn(dynamicVersion);

		mockMvc.perform(get(ConstantsApiEndpoints.API_INFO)).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value(applicationName))
				.andExpect(jsonPath("$.description").value(applicationDescription))
//                .andExpect(jsonPath("$.version").value(dynamicVersion))
				.andExpect(jsonPath("$.version").exists()).andExpect(jsonPath("$.status").value("UP"))
				.andExpect(jsonPath("$.date").exists());
	}
}
