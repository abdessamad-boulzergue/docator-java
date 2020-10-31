package com.apos.rest.exceptions;

public class AposValidationException extends RuntimeException{

	public AposValidationException(Class cls, String msg) {
		super(" invalide enity  : " + cls + " , error : "+msg);
	}
	
}