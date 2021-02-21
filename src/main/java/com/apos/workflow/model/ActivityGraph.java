package com.apos.workflow.model;

import java.util.ArrayList;
import java.util.List;

public class ActivityGraph {
	
	List<Transition> from = new ArrayList<>();
	List<Transition> to = new ArrayList<>();
	
	public List<Transition> getTransitionsFromMe(){
		return this.from;
	}
	public List<Transition> getTransitionsToMe(){
		return this.to;
	}
	
	public void addTransitionFromMe(Transition tr) {
		this.from.add(tr);
	}
	public void addTransitionToMe(Transition tr) {
		this.to.add(tr);
	}
}
