package com.apos.rest.exceptions;

public class DataFormatException  extends RuntimeException{

	private DataFormatException(String msg) {
		super(msg);
	}
	
	public static  DataFormatException getException(Object obj, Class expectedCls) {
		String type = expectedCls.getName();
		if(expectedCls == Long.class || expectedCls == Integer.class ) {
			type = "entier";
		}
		return new DataFormatException(" invalide format   : " + obj + " not type of  : "+type);
	}
}