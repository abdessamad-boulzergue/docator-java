package com.apos.wizard;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.AccessException;

import com.apos.plugins.GeneralConfigs;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WizardActionManager {

	private   File            definitionsFile        = null;
	
	@Autowired
	GeneralConfigs config;
	
	  private HashMap<String, JsonNode> actionList;
	public WizardActionManager() throws AccessException {
		actionList = new HashMap<>();
		initActionDefinitions();
	}

	private void initActionDefinitions() throws AccessException {

		
		if(definitionsFile ==null) {
			definitionsFile = new File(config.getActionsDefinitionPath());
			if(!definitionsFile.isFile() || definitionsFile.canRead()) {
				definitionsFile = null;
			}else {
				throw new AccessException("can't acces definitions file : ".concat(definitionsFile.getPath()));
			}
		}
		
		if(definitionsFile != null) {
			try {				
				JsonNode json = new ObjectMapper().readTree(definitionsFile);
				json.get("updatetime");
				JsonNode definitionList = json.get("DefinitionList");
				definitionList.elements().forEachRemaining(action->{
					actionList.put(action.get("key").asText(), action);
				});
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
}
