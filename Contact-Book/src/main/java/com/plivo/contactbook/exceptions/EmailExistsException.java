package com.plivo.contactbook.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EmailExistsException extends RuntimeException{
	
	private String resourceName;
	private String fieldName;
	private Object fieldValue;

	public EmailExistsException(String resourceName, String fieldName, Object fieldValue) {
		super(String.format("%s already exists with %s : '%s'", resourceName, fieldName, fieldValue));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public String getResourceName() {
		return resourceName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public Object getFieldValue() {
		return fieldValue;
	}

}
