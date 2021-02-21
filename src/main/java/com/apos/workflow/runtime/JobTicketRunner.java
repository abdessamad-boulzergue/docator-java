package com.apos.workflow.runtime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.apos.workflow.model.Activity;
import com.apos.workflow.model.Transition;

public class JobTicketRunner implements IJobTicketRunner{

	private JobTicketData jobTicketData;
	private JobTicket jobTicket;
	private Runner runner;
	private String id;
	public boolean killed=false;
	public Activity runningActivity;

	public JobTicketRunner(JobTicket jobTicket, JobTicketData jobTicketData) {
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
				runningActivity.setRunner(JobTicketRunner.this);
				runningActivity.execute(JobTicketRunner.this, jobTicketData);
				
				List<Transition> transitions = runningActivity.getGraph().getTransitionsFromMe();
				currentActivity = getNext(transitions);
			}
		}
	}

	public Activity getNext(List<Transition> transitions) {
		for(Transition tr : transitions) {
			return tr.getTargetActivity();
		}
		return null;
	}
	@Override
	public IJobTicket getJobTicket() {
		return this.jobTicket;
	}
}
