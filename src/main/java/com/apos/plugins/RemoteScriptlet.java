package com.apos.plugins;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.apos.socket.ClientStub;
import com.apos.workflow.plugin.WorkflowScriptlet;

public class RemoteScriptlet extends WorkflowScriptlet {

	ClientStub stub;
	@Override
	public void init() {
		super.init();
	}
	@Override
	public void run() {
		
		this.initStub();
		Map<String, String> param = new HashMap<>();
		param.put("folderPath", "D:/");
		param.put("fileList", "[\"*.pdf\"]");
		Map<String, String> params = new HashMap<>();
		stub.startSession();
		params.put("path", "apos");
		params.put("plugin", "FilesPlugin");
		String contextWf  = stub.initContext(Arrays.asList());
		stub.runCommand("runPlugin", Arrays.asList(contextWf, "apos","FilesPlugin",new JSONObject(param).toString()));
		stub.stopSession();
	}
	private void initStub() {}
	public void setPluginSource(String obj) {
		
	}
	public void setPluginSource(ClientStub stub) {
		this.stub =stub;
	}

}
