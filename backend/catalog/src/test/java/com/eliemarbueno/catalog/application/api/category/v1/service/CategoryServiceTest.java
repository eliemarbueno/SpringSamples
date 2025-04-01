package com.eliemarbueno.catalog.application.api.category.v1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.eliemarbueno.catalog.application.api.category.v1.dto.CategoryCreateDTO;
import com.eliemarbueno.catalog.application.api.category.v1.dto.CategoryRetrieveDTO;
import com.eliemarbueno.catalog.application.api.category.v1.dto.CategoryUpdateDTO;
import com.eliemarbueno.catalog.application.exception.v1.handler.ItemAlreadyExistsException;
import com.eliemarbueno.catalog.application.exception.v1.handler.ItemNotFoundException;
import com.eliemarbueno.catalog.domain.category.v1.entity.Category;
import com.eliemarbueno.catalog.domain.category.v1.repository.CategoryRepository;
import com.eliemarbueno.catalog.domain.category.v1.service.CategoryService;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

	@Mock
	private CategoryRepository categoryRepository;

	@InjectMocks
	private CategoryService categoryService;

	private Category category;
	private String categoryId;
	private String categoryName;

	@BeforeEach
	void setUp() {
		categoryId = UUID.randomUUID().toString();
		categoryName = "Eletronics";
		category = new Category(categoryName);
		category.setId(categoryId);
	}

	@Test
	@DisplayName("Should create category successfully")
	void shouldCreateCategory() {
		// Given
		CategoryCreateDTO createDTO = new CategoryCreateDTO(categoryName);
		when(categoryRepository.findByNameIgnoreCase(categoryName)).thenReturn(Optional.empty());
		when(categoryRepository.save(any(Category.class))).thenReturn(category);

		// When
		CategoryRetrieveDTO result = categoryService.create(createDTO);

		// Then
		assertNotNull(result, "Object not null");
		assertEquals(categoryId, result.id(), "Object with correct id");
		assertEquals(categoryName, result.name(), "Object with correct name");
		verify(categoryRepository).findByNameIgnoreCase(categoryName);
		verify(categoryRepository).save(any(Category.class));
	}

	@Test
	@DisplayName("Should throw exception when creating category with existing name")
	void shouldThrowExceptionWhenCreatingCategoryWithExistingName() {
		// Given
		CategoryCreateDTO createDTO = new CategoryCreateDTO(categoryName);
		when(categoryRepository.findByNameIgnoreCase(categoryName)).thenReturn(Optional.of(category));

		// When/Then
		assertThrows(ItemAlreadyExistsException.class, () -> categoryService.create(createDTO));
		verify(categoryRepository).findByNameIgnoreCase(categoryName);
		verify(categoryRepository, never()).save(any(Category.class));
	}

	@Test
	@DisplayName("Should find category by id")
	void shouldFindCategoryById() {
		// Given
		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

		// When
		CategoryRetrieveDTO result = categoryService.findById(categoryId);

		// Then
		assertNotNull(result);
		assertEquals(categoryId, result.id());
		assertEquals(categoryName, result.name());
		verify(categoryRepository).findById(categoryId);
	}

	@Test
	@DisplayName("Should throw exception when category not found by id")
	void shouldThrowExceptionWhenCategoryNotFoundById() {
		// Given
		String nonExistentId = "non-existent-id";
		when(categoryRepository.findById(nonExistentId)).thenReturn(Optional.empty());

		// When/Then
		assertThrows(ItemNotFoundException.class, () -> categoryService.findById(nonExistentId));
		verify(categoryRepository).findById(nonExistentId);
	}

	@Test
	@DisplayName("Should update category successfully")
	void shouldUpdateCategory() {
		// Given
		String newName = "Eletrônicos Atualizados";
		CategoryUpdateDTO updateDTO = new CategoryUpdateDTO(newName);
		Category updatedCategory = new Category(newName);
		updatedCategory.setId(categoryId);

		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
		when(categoryRepository.findByNameIgnoreCaseAndIdNot(newName, categoryId)).thenReturn(Optional.empty());
		when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

		// When
		CategoryRetrieveDTO result = categoryService.update(categoryId, updateDTO);

		// Then
		assertNotNull(result);
		assertEquals(categoryId, result.id());
		assertEquals(newName, result.name());
		verify(categoryRepository).findById(categoryId);
		verify(categoryRepository).findByNameIgnoreCaseAndIdNot(newName, categoryId);
		verify(categoryRepository).save(any(Category.class));
	}

	@Test
	@DisplayName("Should throw exception when updating with non-existent id")
	void shouldThrowExceptionWhenUpdatingWithNonExistentId() {
		// Given
		String nonExistentId = "non-existent-id";
		CategoryUpdateDTO updateDTO = new CategoryUpdateDTO("New Name");
		when(categoryRepository.findById(nonExistentId)).thenReturn(Optional.empty());

		// When/Then
		assertThrows(ItemNotFoundException.class, () -> categoryService.update(nonExistentId, updateDTO));
		verify(categoryRepository).findById(nonExistentId);
		verify(categoryRepository, never()).save(any(Category.class));
	}

	@Test
	@DisplayName("Should throw exception when updating with existing name")
	void shouldThrowExceptionWhenUpdatingWithExistingName() {
		// Given
		String newName = "Existing Name";
		CategoryUpdateDTO updateDTO = new CategoryUpdateDTO(newName);
		Category existingCategory = new Category(newName);
		existingCategory.setId(UUID.randomUUID().toString());

		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
		when(categoryRepository.findByNameIgnoreCaseAndIdNot(newName, categoryId))
				.thenReturn(Optional.of(existingCategory));

		// When/Then
		assertThrows(ItemAlreadyExistsException.class, () -> categoryService.update(categoryId, updateDTO));
		verify(categoryRepository).findById(categoryId);
		verify(categoryRepository).findByNameIgnoreCaseAndIdNot(newName, categoryId);
		verify(categoryRepository, never()).save(any(Category.class));
	}

	@Test
	@DisplayName("Should delete category successfully")
	void shouldDeleteCategory() {
		// Given
		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
		doNothing().when(categoryRepository).deleteById(categoryId);

		// When
		boolean result = categoryService.delete(categoryId);

		// Then
		assertTrue(result);
		verify(categoryRepository).findById(categoryId);
		verify(categoryRepository).deleteById(categoryId);
	}

	@Test
	@DisplayName("Should return false when deleting non-existent category")
	void shouldReturnFalseWhenDeletingNonExistentCategory() {
		// Given
		String nonExistentId = "non-existent-id";
		when(categoryRepository.findById(nonExistentId)).thenReturn(Optional.empty());

		// When/Then
		assertThrows(ItemNotFoundException.class, () -> categoryService.delete(nonExistentId));
		verify(categoryRepository).findById(nonExistentId);
		verify(categoryRepository, never()).deleteById(anyString());
	}

	@Test
	@DisplayName("Should find all categories")
	void shouldFindAllCategories() {
		// Given
		Pageable pageable = PageRequest.of(0, 10);
		List<Category> categories = List.of(category, new Category("Books"));
		Page<Category> page = new PageImpl<>(categories, pageable, categories.size());
		when(categoryRepository.findAll(pageable)).thenReturn(page);

		// When
		Page<CategoryRetrieveDTO> result = categoryService.findAllCategories(null, pageable);

		// Then
		assertNotNull(result);
		assertEquals(2, result.getTotalElements());
		assertEquals(categories.get(0).getName(), result.getContent().get(0).name());
		verify(categoryRepository).findAll(pageable);
	}

	@Test
	@DisplayName("Should find categories by name")
	void shouldFindCategoriesByName() {
		// Given
		String nameFilter = "Elet";
		Pageable pageable = PageRequest.of(0, 10);
		List<Category> categories = List.of(category);
		Page<Category> page = new PageImpl<>(categories, pageable, categories.size());
		when(categoryRepository.findAllByNameContainingIgnoreCase(nameFilter, pageable)).thenReturn(page);

		// When
		Page<CategoryRetrieveDTO> result = categoryService.findAllCategories(nameFilter, pageable);

		// Then
		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
		assertEquals(categoryName, result.getContent().get(0).name());
		verify(categoryRepository).findAllByNameContainingIgnoreCase(nameFilter, pageable);
	}

	@Test
	@DisplayName("Should throw exception when no categories found by name")
	void shouldThrowExceptionWhenNoCategoriesFoundByName() {
		// Given
		String nameFilter = "NonExistent";
		Pageable pageable = PageRequest.of(0, 10);
		Page<Category> emptyPage = new PageImpl<>(List.of(), pageable, 0);
		when(categoryRepository.findAllByNameContainingIgnoreCase(nameFilter, pageable)).thenReturn(emptyPage);

		// When/Then
		assertThrows(ItemNotFoundException.class, () -> categoryService.findAllCategories(nameFilter, pageable));
		verify(categoryRepository).findAllByNameContainingIgnoreCase(nameFilter, pageable);
	}

	@Test
	@DisplayName("Should throw exception when no categories exist")
	void shouldThrowExceptionWhenNoCategoriesExist() {
		// Given
		Pageable pageable = PageRequest.of(0, 10);
		Page<Category> emptyPage = new PageImpl<>(List.of(), pageable, 0);
		when(categoryRepository.findAll(pageable)).thenReturn(emptyPage);

		// When/Then
		assertThrows(ItemNotFoundException.class, () -> categoryService.findAllCategories(null, pageable));
		verify(categoryRepository).findAll(pageable);
	}
}