package com.apos.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class PluginLoader implements IPluginLoad{
	

	static Logger  logger    = Logger.getLogger(PluginLoader.class);

	private static PluginLoader singleton;

	@Autowired
	@Qualifier("workflow_connection")
	JsonNode pluginDatasource;
	Map<String, IPlugin> plugins = new HashMap<>();
    HashMap<String, IPluginSource> sources = new HashMap<>() ;
	
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
		IPlugin plugin =null;
		Set<String> keys = sources.keySet();
		for(String key : keys) {
			IPluginSource loader = sources.get(key);
			if(loader.getKey().equals(srcKey)) {
				plugin=loader.get(pluginKey);
				break;
			}
		}
			
		
		return plugin;
	}
	
	@Override
	@PostConstruct
	public void init() {
		JsonNode sourcesNode = pluginDatasource.get("pluginSources");
		sourcesNode.forEach(src->{
			Iterator<Entry<String, JsonNode>> fields = src.fields();
			HashMap<String, String> srcParams = new HashMap<>();
			while(fields.hasNext()) {
				Entry<String, JsonNode> field = fields.next();
				String key = field.getKey();
				srcParams.put(key , field.getValue().asText());
			}
			 IPluginSource pluginSrc = PluginSourceFactory.getSource(srcParams);
			 if(pluginSrc!=null) {
			     sources.put(pluginSrc.getKey(),pluginSrc);
			 }else {
				 logger.warn("invalide plugin source, got null : params : ".concat(srcParams.toString()));
			 }
			});
		
		

	}
	@Override
	public List<IPlugin> load()  {
		
		try {
			sources.forEach((key,src)->{
				Map<String, IPlugin> srcPlugins = src.getAll();
				if(srcPlugins!=null && srcPlugins.size()>0)
				    plugins.putAll( srcPlugins);
			});
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	
		List<IPlugin> returns = new ArrayList<IPlugin>();
		plugins.forEach((uid, plug)->{
			returns.add(plug);
		});
		return returns;
	}
	public static PluginLoader getPluginLoader() {
	    if (singleton == null) {
	      singleton = new PluginLoader();
	    }
	    return singleton;
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
		    	  logger.error(e.getMessage());
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
		String ctx=null;
		try {
			ctx =  src.init();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return ctx;
	}

}
