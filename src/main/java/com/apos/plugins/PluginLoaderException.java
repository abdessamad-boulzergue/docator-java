package com.apos.plugins;

public class PluginLoaderException extends RuntimeException {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PluginLoaderException(Exception e) {
		super(e);
	}

	public PluginLoaderException(String message) {
		super(message);
	}
}
