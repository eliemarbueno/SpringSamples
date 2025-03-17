package com.eliemarbueno.catalog.application.exception.v1.handler;

import lombok.Getter;

@Getter
public class ItemAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final String entity;
	private final String field;
	private final String value;

	public ItemAlreadyExistsException(String entity, String field, String value) {
//		super(String.format("Entity '%s' already exists with %s '%s'.", entity, field, value));
		super(String.format("%s with %s '%s' already exists.", entity, field, value));
		this.entity = entity;
		this.field = field;
		this.value = value;
	}
}