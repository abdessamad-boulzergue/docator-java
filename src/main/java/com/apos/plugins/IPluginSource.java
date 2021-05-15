package com.apos.plugins;

import java.util.Map;

public interface IPluginSource {
	public String getKey();
	public String init() throws Exception;
	public void close();
	public IPlugin get(String key) throws RuntimeException ;
	public Map<String, IPlugin> getAll() throws RuntimeException ;
}
