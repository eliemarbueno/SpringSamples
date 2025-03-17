package com.eliemarbueno.catalog.application.api.category.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryUpdateDTO {

  private String id;
  private String name;

  public CategoryUpdateDTO(String name) {
    this.name = name;
  }
}
