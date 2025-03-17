package com.eliemarbueno.catalog.domain.category.v1.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eliemarbueno.catalog.application.api.category.v1.dto.CategoryCreateDTO;
import com.eliemarbueno.catalog.application.api.category.v1.dto.CategoryRetrieveDTO;
import com.eliemarbueno.catalog.application.api.category.v1.dto.CategoryUpdateDTO;
import com.eliemarbueno.catalog.application.exception.v1.handler.ItemAlreadyExistsException;
import com.eliemarbueno.catalog.application.exception.v1.handler.ItemNotFoundException;
import com.eliemarbueno.catalog.domain.category.v1.entity.Category;
import com.eliemarbueno.catalog.domain.category.v1.repository.CategoryRepository;

import jakarta.transaction.Transactional;

@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Transactional
	public CategoryRetrieveDTO create(CategoryCreateDTO categoryDTO) {
		var category = new Category(categoryDTO.name());
		prePersist(category);
		var categorySaved = categoryRepository.save(category);

		return new CategoryRetrieveDTO(categorySaved.getId(), categorySaved.getName());
	}

	public CategoryRetrieveDTO findById(String id) {
		var category = categoryRepository.findById(id)
				.orElseThrow(() -> new ItemNotFoundException("Category", "id", id));
		return new CategoryRetrieveDTO(category.getId(), category.getName());
	}

	public Category getById(String id) {
		return categoryRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Category", "id", id));
	}

	@Transactional
	public CategoryRetrieveDTO update(String id, CategoryUpdateDTO category) {
		var existingCategory = getById(id);
		existingCategory.setName(category.getName());
		preUpdate(existingCategory);
		var categorySaved = categoryRepository.save(existingCategory);
		return new CategoryRetrieveDTO(categorySaved.getId(), categorySaved.getName());
	}

	@Transactional
	public boolean deleteCategory(String id) {
		var category = getById(id);
		if (category == null) {
			return false;
		}

		categoryRepository.deleteById(id);
		return true;
	}

	public Page<CategoryRetrieveDTO> findAllCategories(String name, Pageable pageable) {
		var page = categoryRepository.findAllByNameContainingIgnoreCase(name, pageable);

		return page.map(this::toDTO);
	}

	private CategoryRetrieveDTO toDTO(Category category) {
		return new CategoryRetrieveDTO(category.getId(), category.getName());
	}

	public void prePersist(Category category) {
		if (categoryRepository.findByNameIgnoreCase(category.getName()).isPresent()) {
			throw new ItemAlreadyExistsException("Category", "name", category.getName());
		}
	}

	public void preUpdate(Category category) {
		if (categoryRepository.findByNameIgnoreCaseAndIdNot(category.getName(), category.getId()).isPresent()) {
			throw new ItemAlreadyExistsException("Category", "name", category.getName());
		}
	}
}
