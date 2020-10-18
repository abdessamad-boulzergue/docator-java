package com.apos.plugins;

import java.util.List;

public interface IPluginSource {
	public void init();
	public void close();
	public IPlugin get(String key) ;
	public List<IPlugin> getAll() ;
}
