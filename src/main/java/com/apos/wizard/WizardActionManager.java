package com.apos.wizard;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.AccessException;
import org.springframework.stereotype.Service;

import com.apos.plugins.GeneralConfigs;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class WizardActionManager {

	private   File            definitionsFile        = null;
	
	@Autowired
	GeneralConfigs configs;
	
	private HashMap<String, ActionDefinition> actionList;

	private ActionDefinition currentAction;

	private HashMap<String, String> callParams;
	
	public WizardActionManager() throws AccessException {
		actionList = new HashMap<>();
	}
	public HashMap<String, ActionDefinition> getActionList(){
		return this.actionList;
	}
	public void setAction(String key,HashMap<String, String> params) {
		this.currentAction = getAction(key);
		this.callParams = params;
	}
	public void initActionDefinitions() {

		
		if(definitionsFile ==null) {
			definitionsFile = new File(configs.getActionsDefinitionPath());
			if(!definitionsFile.isFile() || !definitionsFile.canRead()) {
				definitionsFile = null;
			}
		}
		
		if(definitionsFile != null) {
			try {				
				JsonNode json = new ObjectMapper().readTree(definitionsFile);
				JsonNode definitionList = json.get("ActionDefinitionList");
				definitionList.get("actions").elements().forEachRemaining(action->{
					ActionDefinition definition = new ActionDefinition(action);
					actionList.put(definition.getKey(), definition);
				});
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	public ActionDefinition getAction(String key) {
		return actionList.get(key);
	}
}
