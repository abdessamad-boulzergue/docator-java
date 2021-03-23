package com.apos.workflow.runtime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.UUID;

import com.apos.workflow.model.Activity;
import com.apos.workflow.model.Transition;
import com.apos.workflow.runtime.utils.WorkflowEventRuntimeBuilder;

public class JobTicketRunner extends Observable implements IJobTicketRunner {

	private JobTicketData jobTicketData;
	private IJobTicket jobTicket;
	private Runner runner;
	private String id;
	public boolean killed=false;
	public Activity runningActivity;

	public JobTicketRunner(IJobTicket jobTicket, JobTicketData jobTicketData) {
		this.jobTicket = jobTicket;
		this.jobTicketData = jobTicketData;
		this.id = computeJobTicketID(this.jobTicket.getId(),new Date());
	}
	private String computeJobTicketID(String id, Date start) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssS");
		return id.concat("_").concat(dateFormat.format(start)).concat(UUID.randomUUID().toString());
	}
	public Runner startThread(Activity startingActivity) {
		runner = new Runner(getId(), startingActivity);
		runner.start();
		return runner;
	}

	private String getId() {
		return id;
	}

	class Runner extends Thread{
		private static final int RUNNING = 1;
		private Activity starter;
		Runner(String id , Activity activity){
			super("RUNNER_"+id);
			this.starter = activity;
		}
		@Override
		public void run() {
			
			execute(starter);
		}
		private void execute(Activity currentActivity) {
			while(currentActivity!=null && !killed) {
				
					runningActivity = currentActivity;
					changeActivityState(RUNNING,currentActivity);
					runningActivity.setRunner(JobTicketRunner.this);
					runningActivity.execute(JobTicketRunner.this, jobTicketData);
					
					List<Transition> transitions = runningActivity.getGraph().getTransitionsFromMe();
					changeActivityState(0,currentActivity);

					currentActivity = getNext(transitions);

				
			}
		}
	}

	public Activity getNext(List<Transition> transitions) {
		Activity activity = null;
		for(Transition tr : transitions) {
			activity= tr.getTargetActivity();
			if(activity!=null)
				break;
		}
		return activity;
	}
	public void changeActivityState(int state, Activity activity) {
		WorkflowEventRuntimeBuilder builder = new WorkflowEventRuntimeBuilder();
		builder.setActivity(activity)
		       .setState(state)
		       .setContext(this.jobTicket.getContext());
		
		setChanged();
		notifyObservers(builder.build());
	}
	@Override
	public IJobTicket getJobTicket() {
		return this.jobTicket;
	}
}
