package com.apos.plugins;

import java.util.Map;

public interface IPluginSource {
	public void init();
	public void close();
	public IPlugin get(String key) ;
	public Map<String, IPlugin> getAll() ;
}
