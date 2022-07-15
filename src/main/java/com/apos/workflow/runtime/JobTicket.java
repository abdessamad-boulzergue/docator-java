package com.apos.workflow.runtime;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.apos.plugins.PluginLoader;
import com.apos.workflow.model.Activity;

public class JobTicket implements IJobTicket, Observer{
	  static Logger  logger        = Logger.getLogger(JobTicket.class);

	private String _contextID;
	private JobTicketData jobTicketData;
	private Date startTime;
	private String workflowName;
	private String jobTicketId;
	private WorkflowModel workflowModel;
	private WorkflowProcess belongingProcess;
	private PluginLoader pluginLoader;
	private WorkflowMonitorContext monitorRunningContext;
	private JobTicketRunner runner;
	private ArrayList<JobTicketListener> jobTicketListeners = new ArrayList<>();

	public JobTicket(String modelPath, String jobTicketDataFilePath, String contextId,
			Map<String, String> mapParameters) {
		
		_contextID = contextId;
	    initJobTicketDataFromPath(jobTicketDataFilePath);
	    addJobTicketMapParams(mapParameters);
	    startTime = new Date();
	    loadModel(modelPath);
	    init();
	}

	private void init() {
		belongingProcess = workflowModel.getRoot();
		 SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMdd");
	      SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmssS");
	      jobTicketData.setData("StartDate", dataFormat.format(startTime));
	      jobTicketData.setData("StartTime", timeFormat.format(startTime));
	      
	      addListener(WorkflowMonitor.getMonitor());
	}

	private void loadModel(String modelPath) {
	    if (modelPath == null) { throw new IllegalArgumentException("null value : model source file is missing"); }
	    File modelFile = new File(modelPath);
	    workflowName = WorkflowModel.getWorflowNameFromModelFile(modelFile.getAbsolutePath(), null);
	    jobTicketId = computeJobTicketID(workflowName, startTime);
	    pluginLoader = new PluginLoader(jobTicketId);
	    workflowModel = new WorkflowModel(pluginLoader);
	    if (jobTicketData != null) {
	        workflowModel.load(modelFile.getAbsolutePath(), null, jobTicketData.getRunningEnvironment());
	      } else {
	        workflowModel.load(modelFile.getAbsolutePath(), null);
	      }
	}

	private String computeJobTicketID(String id, Date start) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssS");
		return id.concat("_").concat(dateFormat.format(start)).concat(UUID.randomUUID().toString());
	}

	private void addJobTicketMapParams(Map<String, String> mapParameters) {

	    mapParameters= mapParameters;
	    if (jobTicketData!=null)
	    {
	    	jobTicketData.loadParameters(mapParameters);
	    }
	  
	}

	public JobTicket(String modelPath, Object object, String _serializedJobTicketData, String contextId,
			Map<String, String> parameters) {
	}

	private void initJobTicketDataFromPath(String jobTicketDataFilePath)  {
		jobTicketData = new JobTicketData();
	    if (jobTicketDataFilePath.endsWith(".json")) {
	    	System.out.println("Not implemented");
	    } else {
	      jobTicketData.load(new File(jobTicketDataFilePath));
	    }
	}

	public void start() {
		if (runner == null) {
		      initMainJobTicketRunner();
		    }
		
		Activity startingActivity = belongingProcess.getStarter();
		startTime = new Date();
		runner.startThread(startingActivity);
	}
	private void initMainJobTicketRunner() {
	    runner = new JobTicketRunner(this, jobTicketData);
	    runner.addObserver(this);
	}

	public String getId() {
		return jobTicketId;
	}
	public void setMonitorRunningContext(WorkflowMonitorContext context) {
		monitorRunningContext = context;
		addListener(context);
	}

	@Override
	public void update(Observable o, Object arg) {
		notifyLiteners(arg);
	}

	private void notifyLiteners(Object arg) {
		for(JobTicketListener l : jobTicketListeners) {
			l.runtimeChange(arg);
		}
	}

	@Override
	public void addListener(JobTicketListener listener) {
		if(listener!=null && !jobTicketListeners.contains(listener)) {
			jobTicketListeners.add(listener);
		}
	}

	@Override
	public String getContext() {
		return _contextID;
	}

}
