package com.apos.plugins;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import com.apos.socket.ClientStub;
import com.apos.utils.JsonUtils;
import com.apos.workflow.plugin.WorkflowScriptlet;

public class RemoteScriptlet extends WorkflowScriptlet {

	ClientStub stub;
	private String pluginName;
	@Override
	public void init() {
		super.init();
	}
	public void setPluginName(String pluginName){
		this.pluginName = pluginName;
	}
	@Override
	public void run() {
		
		this.initStub();
		Map<String, Object> param = new HashMap<>();
		try {
			param = this.getScripletArgs();
		} catch (Exception e) {
			e.printStackTrace();
		}
		stub.startSession();
		logInfo(String.format("run remote scriplet : %s", pluginName));
		String contextWf  = stub.initContext(Arrays.asList());
		String result = stub.runCommand("runPlugin", Arrays.asList(contextWf, "apos",pluginName,new JSONObject(param).toString()));
		Map<String, Object>  outputMap = JsonUtils.fromJsonHashMap("output", result);
		try {
			updateOutputParams(outputMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		stub.stopSession();
	}
	private void updateOutputParams(Map<String, Object> outputMap) throws Exception {
		if(outputMap!=null && outputMap.size()>0) {
		Iterator<String> iter = outputMap.keySet().iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			Object value = outputMap.get(key);
			if(value!=null) {
				this.setOutputArgs(key, value);
			}
		}
		}
	}
	private void initStub() {}
	public void setPluginSource(String obj) {
		
	}
	public void setPluginSource(ClientStub stub) {
		this.stub =stub;
	}

}
