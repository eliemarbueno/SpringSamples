package com.eliemarbueno.catalog.application.api.category.v1.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eliemarbueno.catalog.application.api.category.v1.dto.CategoryCreateDTO;
import com.eliemarbueno.catalog.application.api.category.v1.dto.CategoryRetrieveDTO;
import com.eliemarbueno.catalog.application.api.category.v1.dto.CategoryUpdateDTO;
import com.eliemarbueno.catalog.domain.category.v1.service.CategoryService;
import com.eliemarbueno.catalog.shared.constant.v1.ConstantsApiEndpoints;

import jakarta.validation.Valid;

@RestController
@RequestMapping(ConstantsApiEndpoints.CATEGORY_V1)
public class CategoryController {

	private final CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping
	public Page<CategoryRetrieveDTO> findAll(@RequestParam(required = false) String name, Pageable pageable) {
		return categoryService.findAllCategories(name, pageable);
	}

	@PostMapping
	public ResponseEntity<CategoryRetrieveDTO> create(@Valid @RequestBody CategoryCreateDTO categoryCreateDTO) {
		var categorySaved = categoryService.create(categoryCreateDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(categorySaved);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoryRetrieveDTO> findById(@PathVariable String id) {
		return ResponseEntity.ok(categoryService.findById(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<CategoryRetrieveDTO> update(@Valid @PathVariable String id,
			@RequestBody CategoryUpdateDTO categoryUpdateDTO) {
		return ResponseEntity.ok(categoryService.update(id, categoryUpdateDTO));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		return categoryService.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	}
}
