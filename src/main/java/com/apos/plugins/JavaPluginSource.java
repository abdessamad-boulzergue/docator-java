package com.apos.plugins;

import java.util.Map;

import com.apos.utils.Base64;

public class JavaPluginSource implements IPluginSource{
	private String pluginsPackage;
	
	public JavaPluginSource(String pckg){
		this.pluginsPackage = pckg;
	}
	@Override
	public String getKey() {
		return  Base64.encode(pluginsPackage.getBytes());
	}

	@Override
	public String init() throws Exception {
		return null;
	}

	@Override
	public void close() {
		
	}

	@Override
	public IPlugin get(String key) throws RuntimeException {
		return null;
	}

	@Override
	public Map<String, IPlugin> getAll() throws RuntimeException {
		return null;
	}

}
