package com.apos.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class PluginLoader implements IPluginLoad{
	
	@Autowired
	@Qualifier("workflow_connection")
	JsonNode pluginDatasource;
	Map<String, IPlugin> plugins = new HashMap();
	
    Map<String, IPluginSource> sources = new HashMap<>();
    
    
    IPluginSource getSource(String key) {
    	IPluginSource plugSrc =null;
    	if(sources.containsKey(key)) {
			 plugSrc = sources.get(key);
    	}
    	return plugSrc;
    }
	@Override
	public IPlugin load(String key) {
		
		
		return plugins.get(key);
		
	}
	
	@Override
	@PostConstruct
	public void init() {
		JsonNode src = pluginDatasource.get("workflow_server");
		
			String id = src.get("id").asText();
			String host = src.get("address").asText();
			Integer port = Integer.valueOf(src.get("port").asText());
			
			sources.put(id, new PluginSocketLoader(host, port ));
		

	}
	@Override
	public List<IPlugin> load() {
		
		sources.forEach((key,src)->{
			plugins.putAll( src.getAll());
		});
	
		List<IPlugin> returns = new ArrayList<IPlugin>();
		plugins.forEach((uid, plug)->{
			returns.add(plug);
		});
		return returns;
	}

}
