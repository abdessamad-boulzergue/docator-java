package com.apos.workflow.model;

import org.json.JSONArray;

import com.apos.plugins.IPluginLoad;
import com.apos.utils.ResourceTools;
import com.apos.workflow.plugin.IApplication;
import com.apos.workflow.runtime.JobTicketData;
import com.apos.workflow.runtime.JobTicketRunner;

public class Implementation {
	
	  public final static int TOOL             = 2;

	  
	private Application application        = null;
	private Activity parentActivity;
	private int implementationType = TOOL;
	
	public Implementation(Activity activity) {
		parentActivity = activity;
	}

	

	public void load(IPluginLoad loader, JSONArray jsonImplement) {
		JSONArray jsTool = ResourceTools.getChildNodeOfType(jsonImplement, ResourceTools.WF_Tool_TYPE);
		application = new Application(this);
        application.load(loader,jsTool);
	}

	public boolean isStarter() {
		return application.isStarter();
	}

	public void execute(JobTicketRunner jobTicketRunner, JobTicketData jobTicketData) {
		 if (application != null) {
		      application.execute(jobTicketRunner, jobTicketData, null);
		 }
	}

	public Application getApplication() {
		return this.application;
	}
	public Activity getActivity() {
		return this.parentActivity;
	}
}
