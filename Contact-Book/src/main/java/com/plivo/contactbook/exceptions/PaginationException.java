package com.plivo.contactbook.exceptions;

import com.plivo.contactbook.response.PagingErrorResponse;

public class PaginationException extends RuntimeException{
	
	private static final long serialVersionUID = -123L;
	
	private String errorMessage;
	
	@Override
	public String getMessage() {
	        return errorMessage;
	}
	
	public PaginationException() {
		super();
	}
	
	public PaginationException(PagingErrorResponse reponse) {
		super(reponse.getMessage());
		this.errorMessage = reponse.getMessage();
	}

}
