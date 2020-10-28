package com.apos.rest.exceptions;

public class ResourceAccessException extends RuntimeException{

	public ResourceAccessException(String  resource) {
		super(" could not access  resource :"+resource);
	}
	
}
