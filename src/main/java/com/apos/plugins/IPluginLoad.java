package com.apos.plugins;

import java.util.List;

import org.json.JSONObject;

public interface IPluginLoad {

	public List<IPlugin> load() ;
	public void init() ;
	public IPlugin getJavaPlugin(String pluginJava, String object);
	public IPlugin getPlugin(String srcKey,String pythonPluginKey, String contextId) throws RuntimeException;
	IPlugin load(String srcKey, String pluginKey) throws RuntimeException;
	public void configureSource(JSONObject attributes);
}
