package com.apos.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class PluginLoader implements IPluginLoad{
    Map<String, IPluginSource> sources = new HashMap<>();
    
    public PluginLoader() {
		init();
	}
    IPluginSource getSource(String key) {
    	IPluginSource plugSrc =null;
    	if(sources.containsKey(key)) {
			 plugSrc = sources.get(key);
    	}
    	return plugSrc;
    }
	@Override
	public IPlugin load(String key) {
		
		IPluginSource plugSrc = getSource("src1");
		IPlugin plug=null;
		if(plugSrc!=null) {
			plug  = plugSrc.get(key);
		}
		return plug;
		
	}
	
	@Override
	public void init() {
		sources.put("src1", new PluginSocketLoader("127.0.0.1", 19902));
	}
	@Override
	public List<IPlugin> load() {
		IPluginSource plugSrc = getSource("src1");
		List<IPlugin> plugins = new ArrayList<>();
		if(plugSrc!=null)
			plugins = plugSrc.getAll();
		return plugins;
	}

}
