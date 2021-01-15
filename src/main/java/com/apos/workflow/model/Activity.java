package com.apos.workflow.model;

public class Activity {

	private Implementation  implementation   = null;
	
	public void execute() {
		implementation.execute();
	}
}
