package com.eliemarbueno.catalog.domain.category.v1.repository;

import com.eliemarbueno.catalog.domain.category.v1.entity.Category;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
	Page<Category> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

	Optional<Category> findByNameIgnoreCase(String name);

	Optional<Category> findByNameIgnoreCaseAndIdNot(String name, String id);
}
