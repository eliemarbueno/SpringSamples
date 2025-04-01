package com.eliemarbueno.catalog.application.api.category.v1.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreateDTO(@NotBlank String name) {
}
