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
	@Qualifier("PluginDatasource")
	JsonNode pluginDatasource;
    
	
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
		
		IPluginSource plugSrc = getSource("src1");
		IPlugin plug=null;
		if(plugSrc!=null) {
			plug  = plugSrc.get(key);
		}
		return plug;
		
	}
	
	@Override
	@PostConstruct
	public void init() {
		JsonNode sourcesNode = pluginDatasource.get("sources");
		Iterator<JsonNode> sourceIter = sourcesNode.elements();
		sourceIter.forEachRemaining(src->{
		
			String id = src.get("id").asText();
			String host = src.get("address").asText();
			Integer port = Integer.valueOf(src.get("port").asText());
			
			sources.put(id, new PluginSocketLoader(host, port ));
			
		});
		

	}
	@Override
	public List<IPlugin> load() {
		List<IPlugin> plugins = new ArrayList<>();
		sources.forEach((key,src)->{
			plugins.addAll( src.getAll() );
		});
	
		return plugins;
	}

}
