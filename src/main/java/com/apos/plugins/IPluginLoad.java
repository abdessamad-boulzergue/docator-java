package com.apos.plugins;

import java.util.List;

public interface IPluginLoad {

	public IPlugin load(String key) ;
	public List<IPlugin> load() ;
	public void init() ;
}
