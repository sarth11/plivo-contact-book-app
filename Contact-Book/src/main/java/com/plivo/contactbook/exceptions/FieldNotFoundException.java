package com.plivo.contactbook.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FieldNotFoundException extends RuntimeException{
	
	private String resourceName;
	
	public FieldNotFoundException(String resourceName) {
		super(String.format("%s Field missing", resourceName));
		this.resourceName = resourceName;
	}

	public String getResourceName() {
		return resourceName;
	}

}
