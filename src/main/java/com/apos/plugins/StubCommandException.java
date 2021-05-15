package com.apos.plugins;

public class StubCommandException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	StubCommandException(Exception e){
		super(e);
	}
}
