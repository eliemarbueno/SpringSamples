package com.eliemarbueno.catalog.application.exception.v1.handler;

public class ItemNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final String entity;
	private final String field;
	private final String value;

	public ItemNotFoundException(String entity, String field, String value) {
		// super(String.format("Entity '%s' already exists with %s '%s'.", entity,
		// field, value));
		super(String.format("%s with %s '%s' not found.", entity, field, value));
		this.entity = entity;
		this.field = field;
		this.value = value;
	}

	public ItemNotFoundException(String entity) {
		// super(String.format("Entity '%s' already exists with %s '%s'.", entity,
		// field, value));
		super(String.format("%s not found.", entity));
		this.entity = entity;
		this.field = null;
		this.value = null;
	}
}
