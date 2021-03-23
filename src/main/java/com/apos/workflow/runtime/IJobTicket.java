package com.apos.workflow.runtime;

public interface IJobTicket {

    public void addListener(JobTicketListener listener);
	public String getId();
	public String getContext();
}
