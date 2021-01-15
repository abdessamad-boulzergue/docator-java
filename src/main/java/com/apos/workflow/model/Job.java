package com.apos.workflow.model;

import java.util.Date;

public class Job implements JobInterface{

	private String contextID;

	@Override
	public String getContextID() {
		 return contextID;
	}

	@Override
	public String getID() {
		return null;
	}

	@Override
	public Date getStartTime() {
		return null;
	}

	@Override
	public Date getEndTime() {
		return null;
	}
	  
}
