package com.eliemarbueno.catalog.domain.category.v1.entity;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@Table(
//  uniqueConstraints = {
//    @UniqueConstraint(
//      name = "category_name_unique",
//      columnNames = "name"
//    ),
//  }
//)
public class Category {

  @Id
  @GeneratedValue(generator = "uuid")
  @UuidGenerator
  @Column(
    name = "category_id",
    updatable = false,
    nullable = false,
    length = 36
  )
  private String id;

  @Column(name = "category_name", nullable = false, length = 100)
  private String name;

  public Category(String name) {
    this.name = name;
  }
}
