package com.apos.wizard;

import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;

public class ActionController {
	JsonNode node;
    String type;
	private static String _TYPE_="type";
	String callTime;
	private static String _CALL_TIME_="callTime";
	private final static String _CALL_TIME_AFTER_="after";
	private final static String _TYPE_ENGINE_="engine";
	private ActionCaller caller;
	public ActionController(JsonNode node) {
		this.node =node;
		this.type = this.node.get(_TYPE_).asText();
		this.callTime = this.node.get(_CALL_TIME_).asText();
				
		
	}

	public boolean isAfter() {
		return _CALL_TIME_AFTER_.equals(this.callTime);
	}

	public IActionExecutionResult execute(HashMap<String, String> params) {
		switch(type) {
		case _TYPE_ENGINE_:
			return executeEngine(params);
		}
		return null;
	}

	private IActionExecutionResult executeEngine(HashMap<String, String> params) {
		IActionExecutionResult result=null;
		
			if(this.caller ==null) {
				this.caller = new ActionCaller();
			    this.caller.init(node,params);
			}
		
		
		return caller.execute(params);
	}
}
