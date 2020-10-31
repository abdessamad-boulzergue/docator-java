package com.apos.rest.exceptions;

public class AposTransactionException extends RuntimeException{

	public AposTransactionException() {
		super(" transaction error occured ");
	}
	
}