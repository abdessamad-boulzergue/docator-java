package com.apos.workflow.plugin;

import org.json.JSONObject;

import com.apos.plugins.EnginesScriptlet;
import com.apos.plugins.IPlugin;
import com.apos.workflow.model.Application;

abstract public class GenericPlugin implements IPlugin{

	private Application parentApplication;
	  protected EnginesScriptlet                    scriptlet          = null;

	@Override
	public void setApplicationParent(IApplication application) {
		parentApplication = (Application) application;
	}

	@Override
	public void addFactoryInformation(JSONObject extendedAttributes) {
		
	}
	@Override
	public void clear() {
		if(scriptlet!=null) {
			scriptlet.clear();
		}
	}
}
