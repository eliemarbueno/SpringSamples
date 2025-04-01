package com.eliemarbueno.catalog.application.api.category.v1.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.eliemarbueno.catalog.application.api.category.v1.dto.CategoryCreateDTO;
import com.eliemarbueno.catalog.application.api.category.v1.dto.CategoryRetrieveDTO;
import com.eliemarbueno.catalog.application.api.category.v1.dto.CategoryUpdateDTO;
import com.eliemarbueno.catalog.domain.category.v1.service.CategoryService;
import com.eliemarbueno.catalog.shared.constant.v1.ConstantsApiEndpoints;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles("test")
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CategoryService categoryService;

	@Test
	@DisplayName("Should create a category when valid data is provided")
	void shouldCreateCategory() throws Exception {
		// Given
		String name = "Eletronics";
		CategoryCreateDTO createDTO = new CategoryCreateDTO(name);
		String id = UUID.randomUUID().toString();
		CategoryRetrieveDTO retrieveDTO = new CategoryRetrieveDTO(id, name);

		when(categoryService.create(any(CategoryCreateDTO.class))).thenReturn(retrieveDTO);

		// When/Then
		mockMvc.perform(post(ConstantsApiEndpoints.CATEGORY_V1).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createDTO))).andDo(print()).andExpect(status().isCreated())
				.andExpect(jsonPath("$.id", is(id))).andExpect(jsonPath("$.name", is(name)));
	}

	@Test
	@DisplayName("Should get a category by id when it exists")
	void shouldGetCategoryById() throws Exception {
		// Given
		String id = UUID.randomUUID().toString();
		String name = "Eletronics";
		CategoryRetrieveDTO retrieveDTO = new CategoryRetrieveDTO(id, name);

		when(categoryService.findById(id)).thenReturn(retrieveDTO);

		// When/Then
		mockMvc.perform(get(ConstantsApiEndpoints.CATEGORY_V1 + "/{id}", id).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.id", is(id)))
				.andExpect(jsonPath("$.name", is(name)));
	}

	@Test
	@DisplayName("Should get all categories with pagination")
	void shouldGetAllCategoriesWithPagination() throws Exception {
		// Given
		String id1 = UUID.randomUUID().toString();
		String id2 = UUID.randomUUID().toString();
		List<CategoryRetrieveDTO> categories = List.of(new CategoryRetrieveDTO(id1, "Eletronics"),
				new CategoryRetrieveDTO(id2, "Books"));

		Page<CategoryRetrieveDTO> page = new PageImpl<>(categories, PageRequest.of(0, 10), categories.size());

		when(categoryService.findAllCategories(anyString(), any(Pageable.class))).thenReturn(page);

		// When/Then
		mockMvc.perform(get(ConstantsApiEndpoints.CATEGORY_V1).param("name", "").param("page", "0").param("size", "10")
				.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.content", hasSize(2))).andExpect(jsonPath("$.content[0].id", is(id1)))
				.andExpect(jsonPath("$.content[0].name", is("Eletronics")))
				.andExpect(jsonPath("$.content[1].id", is(id2))).andExpect(jsonPath("$.content[1].name", is("Books")))
				.andExpect(jsonPath("$.totalElements", is(2))).andExpect(jsonPath("$.totalPages", is(1)))
				.andExpect(jsonPath("$.size", is(10)));
	}

	@Test
	@DisplayName("Should get categories filtered by name")
	void shouldGetCategoriesFilteredByName() throws Exception {
		// Given
		String id1 = UUID.randomUUID().toString();
		List<CategoryRetrieveDTO> categories = List.of(new CategoryRetrieveDTO(id1, "Eletronics"));

		Page<CategoryRetrieveDTO> page = new PageImpl<>(categories, PageRequest.of(0, 10), categories.size());

		when(categoryService.findAllCategories(eq("Eletronics"), any(Pageable.class))).thenReturn(page);

		// When/Then
		mockMvc.perform(get(ConstantsApiEndpoints.CATEGORY_V1).param("name", "Eletronics").param("page", "0")
				.param("size", "10").contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.content", hasSize(1))).andExpect(jsonPath("$.content[0].id", is(id1)))
				.andExpect(jsonPath("$.content[0].name", is("Eletronics")))
				.andExpect(jsonPath("$.totalElements", is(1)));
	}

	@Test
	@DisplayName("Should update a category when valid data is provided")
	void shouldUpdateCategory() throws Exception {
		// Given
		String id = UUID.randomUUID().toString();
		String updatedName = "Eletronics updated";
		CategoryUpdateDTO updateDTO = new CategoryUpdateDTO(updatedName);
		CategoryRetrieveDTO retrieveDTO = new CategoryRetrieveDTO(id, updatedName);

		when(categoryService.update(eq(id), any(CategoryUpdateDTO.class))).thenReturn(retrieveDTO);

		// When/Then
		mockMvc.perform(put(ConstantsApiEndpoints.CATEGORY_V1 + "/{id}", id).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateDTO))).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(id))).andExpect(jsonPath("$.name", is(updatedName)));
	}

	@Test
	@DisplayName("Should delete a category when it exists")
	void shouldDeleteCategory() throws Exception {
		// Given
		String id = UUID.randomUUID().toString();
		when(categoryService.delete(id)).thenReturn(true);

		// When/Then
		mockMvc.perform(delete(ConstantsApiEndpoints.CATEGORY_V1 + "/{id}", id).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isNoContent());
	}
}