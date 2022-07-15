package com.apos.rest.exceptions;

public class AposValidationException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2294909718644096299L;

	public AposValidationException(@SuppressWarnings("rawtypes") Class cls, String msg) {
		super(" invalide enity  : " + cls + " , error : "+msg);
	}
	
}