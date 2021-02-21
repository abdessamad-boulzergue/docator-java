package com.apos.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class PluginLoader implements IPluginLoad{
	
	private static PluginLoader _singleton;
	@Autowired
	@Qualifier("workflow_connection")
	JsonNode pluginDatasource;
	Map<String, IPlugin> plugins = new HashMap();
    HashMap<String, IPluginSource> sources = new HashMap<String, IPluginSource>() ;
	
    private String sessionID;
    
    
    public PluginLoader(String sessionID) {
    	 this.sessionID = sessionID;
    }
	public PluginLoader() {
	}
	IPluginSource getSource(String key) {
  
    	return sources.get(key);
    }
	@Override
	public IPlugin load(String srcKey , String pluginKey) {
		return sources.get(srcKey).get(pluginKey);
		
	}
	
	@Override
	@PostConstruct
	public void init() {
		JsonNode sourcesNode = pluginDatasource.get("pluginSources");
		sourcesNode.forEach(src->{
			String id = src.get("id").asText();
			String host = src.get("address").asText();
			Integer port = Integer.valueOf(src.get("port").asText());
			sources.put(id, new PluginSocketLoader(id,host, port ));
		});
		
		

	}
	@Override
	public List<IPlugin> load() {
		
		plugins.putAll( sources.get("src_1").getAll());
	
		List<IPlugin> returns = new ArrayList<IPlugin>();
		plugins.forEach((uid, plug)->{
			returns.add(plug);
		});
		return returns;
	}
	public static PluginLoader getPluginLoader() {
	    if (_singleton == null) {
	      _singleton = new PluginLoader();
	    }
	    return _singleton;
	  }
	@Override
	public IPlugin getJavaPlugin(String pluginJava, String instanceClassName) {
		 return getInstanceOfClass(IPlugin.PLUGIN_JAVA, instanceClassName);
	}
	private IPlugin getInstanceOfClass(String pluginJava, String instanceClassName) {
		if (instanceClassName != null) {
		      try {
		        Class<?> classInstance = Class.forName(instanceClassName);
		        if (classInstance != null) {
		          Object newInstance = classInstance.newInstance();
		          if (newInstance instanceof IPlugin) { return (IPlugin) newInstance; }
		          System.out.println(instanceClassName.concat(" not instance of IPlugin"));
		        }
		      }catch(Exception e) {
		    	  e.printStackTrace();
		      }
		}
		return null;
	}
	@Override
	public IPlugin getPlugin(String srcKey , String pythonPluginKey, String contextId) {
		return load(srcKey,pythonPluginKey);
	}
	
	public void configureSource(JSONObject attributes) {
		String host  = attributes.getString("host");
		Integer port = Integer.parseInt(attributes.getString("port"));
		String id = attributes.getString("Id");
		sources.put(id ,  new PluginSocketLoader(id,host, port ));
	}
	
	public String getNewRunningContext(String keySrc) {
		IPluginSource src = sources.get(keySrc);
		return src.init();
	}

}
