package com.eliemarbueno.catalog.application.api.category.v1.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryUpdateDTO {

	private String id;
	@NotBlank
	private String name;

	public CategoryUpdateDTO(String name) {
		this.name = name;
	}
}
