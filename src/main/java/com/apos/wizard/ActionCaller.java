package com.apos.wizard;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.apos.plugins.GeneralConfigs;
import com.fasterxml.jackson.databind.JsonNode;

public class ActionCaller {

	private final static String _ENGINE_ = "ENGINE";
	private JsonNode node;
	private String engineKey;
	private String definitionFileName;
	ArrayList<JsonNode> systemParams = new ArrayList<>();
	@Autowired
	GeneralConfigs cfg;
	@Autowired
	GeneralConfigs configs;
	private Object engineEnvironmentDefinition;

	public ActionCaller() {

	}

	public void init(JsonNode node, HashMap<String, String> params) {
		this.node = node.get(_ENGINE_);
		if (this.node != null) {
			this.engineKey = this.node.get("key").asText();

			this.definitionFileName = params.get("home").concat("/") + this.node.get("definitionFile").asText();
			this.definitionFileName = params.get("home").concat("/")
					+ this.node.get("engineEnvironmentDefinition").asText();

			this.node.get("SYSTEM").elements().forEachRemaining(param -> {
				systemParams.add(param);
			});
			this.node.get("PARAM").elements().forEachRemaining(param -> {
				systemParams.add(param);
			});

		}
	}

	public IActionExecutionResult execute(HashMap<String, String> params) {

		Executor execEngine = new Executor();
		initEngine(execEngine);
		applyParams(execEngine, params);
		execute(execEngine, params);
		return execEngine;
	}

	private void execute(Executor execEngine, HashMap<String, String> params) {
		String prefixEngine = execEngine.getEnginePrefix(engineKey);
		execEngine.buildCommand(prefixEngine);
	}

	private void applyParams(Executor execEngine, HashMap<String, String> params) {
		for (JsonNode param : systemParams) {
			String type = param.has("type") ? param.get("type").asText() : null;
			String name = param.has("name") ? param.get("name").asText() : null;
			String key = param.has("key") ? param.get("key").asText() : null;
			if (name != null && type != null) {
				if ("PARAM".equals(type)) {
					execEngine.applyParam(key, name, params);
				} else if ("SYSTEM".equals(type)) {

				}
			}
		}
	}

	private void initEngine(Executor execEngine) {
		execEngine.loadEngineCommande(definitionFileName);
		execEngine.loadSystemVars(engineEnvironmentDefinition);

	}

}
