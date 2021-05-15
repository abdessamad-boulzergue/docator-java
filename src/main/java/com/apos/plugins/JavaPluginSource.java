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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
	}

	@Override
	public IPlugin get(String key) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, IPlugin> getAll() throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

}
