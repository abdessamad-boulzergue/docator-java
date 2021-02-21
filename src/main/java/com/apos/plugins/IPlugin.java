package com.apos.plugins;

import org.json.JSONObject;

import com.apos.workflow.plugin.IApplication;

public interface IPlugin {
	int FACTORY_TYPE_PLUGIN_JAVA = 0;
	String PLUGIN_JAVA = "pluginJava";
	int FACTORY_TYPE_PLUGIN_PYTHON = 1;
	String PLUGIN_PYTHON = "pluginPython";
	String PLUGIN_SRC_ID = "pluginSourceId";
	
	public String getName();
	public Object getIcon();
	public String getType();
	public String getId();
	public String getUId();
	public boolean isStarting();
	public EnginesScriptlet getImplementation();
	public void setApplicationParent(IApplication application);
	public void addFactoryInformation(JSONObject extendedAttributes);
	public void clear();

}
