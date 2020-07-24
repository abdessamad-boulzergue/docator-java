package com.apos.rest.exceptions;

public class EntityNotFoundException extends RuntimeException{

	public EntityNotFoundException(Long id) {
		super(" Entity not found, id :"+id);
	}
	
}
