package com.apos.plugins;

import java.util.List;

public interface IPluginSource {
	public IPlugin get(String key) ;
	public List<IPlugin> getAll() ;
}
