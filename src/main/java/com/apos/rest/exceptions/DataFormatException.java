package com.apos.rest.exceptions;

public class DataFormatException  extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8360573609387169902L;

	private DataFormatException(String msg) {
		super(msg);
	}
	
	public static  DataFormatException getException(Object obj, @SuppressWarnings("rawtypes") Class expectedCls) {
		String type = expectedCls.getName();
		if(expectedCls == Long.class || expectedCls == Integer.class ) {
			type = "entier";
		}
		return new DataFormatException(" invalide format   : " + obj + " not type of  : "+type);
	}
}