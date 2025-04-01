package com.eliemarbueno.catalog.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.eliemarbueno.catalog.application.api.category.v1.dto.CategoryCreateDTO;
import com.eliemarbueno.catalog.application.api.category.v1.dto.CategoryUpdateDTO;
import com.eliemarbueno.catalog.domain.category.v1.repository.CategoryRepository;
import com.eliemarbueno.catalog.shared.constant.v1.ConstantsApiEndpoints;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CategoryIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private CategoryRepository categoryRepository;

	@BeforeEach
	void setup() {
		categoryRepository.deleteAll();
	}

	@Test
	@DisplayName("Should create, retrieve, update and delete category - Full Lifecycle")
	void shouldManageCategoryLifecycle() throws Exception {
		// GIVEN - Prepare test data
		String categoryName = "Eletronics";
		CategoryCreateDTO createDTO = new CategoryCreateDTO(categoryName);

		// STEP 1: Create Category
		MvcResult createResult = mockMvc
				.perform(post(ConstantsApiEndpoints.CATEGORY_V1).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(createDTO)))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.name", is(categoryName))).andReturn();

		// Extract ID from response
		String responseContent = createResult.getResponse().getContentAsString();
		JsonNode root = objectMapper.readTree(responseContent);
		String categoryId = root.get("id").asText();

		assertNotNull(categoryId);

		// STEP 2: Retrieve Category
		mockMvc.perform(
				get(ConstantsApiEndpoints.CATEGORY_V1 + "/{id}", categoryId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(categoryId)))
				.andExpect(jsonPath("$.name", is(categoryName)));

		// STEP 3: Find All Categories (should include our category)
		mockMvc.perform(get(ConstantsApiEndpoints.CATEGORY_V1).param("page", "0").param("size", "10")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.content", hasSize(1))).andExpect(jsonPath("$.content[0].id", is(categoryId)))
				.andExpect(jsonPath("$.content[0].name", is(categoryName)));

		// STEP 4: Update Category
		String updatedName = "Eletronics updated";
		CategoryUpdateDTO updateDTO = new CategoryUpdateDTO(updatedName);

		mockMvc.perform(put(ConstantsApiEndpoints.CATEGORY_V1 + "/{id}", categoryId)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateDTO)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(categoryId)))
				.andExpect(jsonPath("$.name", is(updatedName)));

		// STEP 5: Verify Update was successful
		mockMvc.perform(
				get(ConstantsApiEndpoints.CATEGORY_V1 + "/{id}", categoryId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.name", is(updatedName)));

		// STEP 6: Delete Category
		mockMvc.perform(
				delete(ConstantsApiEndpoints.CATEGORY_V1 + "/{id}", categoryId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		// STEP 7: Verify Deletion was successful - Should receive 404
		mockMvc.perform(
				get(ConstantsApiEndpoints.CATEGORY_V1 + "/{id}", categoryId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("Should handle pagination and filtering correctly")
	void shouldHandlePaginationAndFiltering() throws Exception {
		// Create multiple categories
		createCategory("Eletronics");
		createCategory("Books");
		createCategory("Eletronics - PCs");
		createCategory("Eletronics - Mobiles");
		createCategory("Clothes");

		// Test pagination - Page 0, size 2
		mockMvc.perform(get(ConstantsApiEndpoints.CATEGORY_V1).param("page", "0").param("size", "2")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.content", hasSize(2))).andExpect(jsonPath("$.totalElements", is(5)))
				.andExpect(jsonPath("$.totalPages", is(3))).andExpect(jsonPath("$.size", is(2)));

		// Test filtering by name
		mockMvc.perform(get(ConstantsApiEndpoints.CATEGORY_V1).param("name", "Eletronics").param("page", "0")
				.param("size", "10").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.content", hasSize(3))).andExpect(jsonPath("$.totalElements", is(3)));
	}

	@Test
	@DisplayName("Should handle validation errors correctly")
	void shouldHandleValidationErrors() throws Exception {
		// Create category with empty name
		CategoryCreateDTO invalidDTO = new CategoryCreateDTO("");

		mockMvc.perform(post(ConstantsApiEndpoints.CATEGORY_V1).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidDTO))).andExpect(status().isBadRequest());

		// Create category, then try to create another with the same name
		String name = "Unique Category";
		createCategory(name);

		mockMvc.perform(post(ConstantsApiEndpoints.CATEGORY_V1).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new CategoryCreateDTO(name))))
				.andExpect(status().isConflict());
	}

	private void createCategory(String name) throws Exception {
		CategoryCreateDTO dto = new CategoryCreateDTO(name);
		mockMvc.perform(post(ConstantsApiEndpoints.CATEGORY_V1).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isCreated());
	}
}