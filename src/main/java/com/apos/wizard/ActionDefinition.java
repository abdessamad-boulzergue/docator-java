package com.apos.wizard;

import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;

public class ActionDefinition {
	String key;
	String action;
	
	private static String _KEY_="key";
	public  static String ENGINE_CONTROL="EngineControl";
	private static String EXECUTION_LIST="ExecutionList";
	private ArrayList<ActionController> actionsAfter = new ArrayList<>();
	private ArrayList<ActionController> actionsBefore = new ArrayList<>();
	public ActionDefinition(JsonNode json){
		key = json.get(_KEY_).asText();
		initEngineExecution(json.get(ENGINE_CONTROL));
		initExecutionList(json.get(EXECUTION_LIST));
	}
	private void initExecutionList(JsonNode jsonNode) {
		if(jsonNode!=null ) {
			jsonNode.elements().forEachRemaining(node->{
				ActionController controller = new ActionController(node);
				if(controller.isAfter())
				   actionsAfter.add(controller);
				else
				   actionsBefore.add(controller);
			});
		}
	}
	public String getKey() {
		return this.key;
	}
	private void initEngineExecution(JsonNode jsonNode) {
		
		if(jsonNode==null || jsonNode.size()==0) {
			action="init";
		}else {
			action = jsonNode.get("action").asText();
		}
	}
	public void execute(HashMap<String, String> params) {
		IActionExecutionResult result = execute(actionsBefore , params);
		 result = execute(actionsAfter , params);
	}
	private IActionExecutionResult execute(ArrayList<ActionController> actions, HashMap<String, String> params) {
		IActionExecutionResult result;
		for(ActionController action : actions) {
			result = action.execute(params);
		}
		
		return null;
	}
	
}
