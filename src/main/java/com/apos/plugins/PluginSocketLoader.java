package com.apos.plugins;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apos.socket.ClientStub;
import com.apos.utils.Base64;
import com.apos.utils.JsonUtils;
import com.apos.workflow.runtime.PersistentPluginData;

public class PluginSocketLoader implements IPluginSource {
	
	
    private String host ;
	private Integer port;
	private ClientStub stub;
	String contextWf;
	String uid =null;
	private static Logger logger = LoggerFactory.getLogger(PluginSocketLoader.class);
	public PluginSocketLoader(ClientStub stub) {
		this.stub = stub;
	    this.uid  = UUID.randomUUID().toString();
	}
		public PluginSocketLoader(String id, String host, Integer port) {
		this.host = host;
		this.port = port;
	    this.stub = new ClientStub(this.host, this.port);
	    this.uid  = id;//UUID.randomUUID().toString();
	}
	public void close() {
		if(stub!=null)
		 stub.stopSession();
	}
	public IPlugin get(String key) {

		IPlugin plug =null;
	    try {
	    	Map<String, IPlugin> pluginsMap = getAll();
	      Iterator<String> keys = pluginsMap.keySet().iterator();
	      while (keys.hasNext()) {
		        String mapKey = keys.next();
		        if(mapKey.equals(key)) {
		        	return pluginsMap.get(mapKey);
		        }
	      }
	      
	    } catch (JSONException e) {
	       e.printStackTrace();
	    }
		return plug;
	  
	}

	@Override
	public Map<String, IPlugin> getAll() {
		return loadPluginsIn("apos");
	}
	
	
	public String initContextWf(){
		if(this.contextWf==null || this.contextWf.trim().isEmpty()) {
			contextWf  = runCommand("initContext",Arrays.asList());
			logger.debug(contextWf);
		}
		return this.contextWf;
	}
	String runCommand(String mth , List<Object> params) {
		synchronized(stub) {
			String result = new String() ;
		stub.startSession();
		result = stub.runCommand(mth, params);
		close();

	     return result;
		}
		
	}
	public Map<String, IPlugin> loadPluginsIn(String location){
	    //this.uid.concat("-").concat(Base64.encode(pluginKey.getBytes()));

	     initContextWf();
		
		String result = runCommand("loadPlugins",Arrays.asList(contextWf,location));
		Map<String,IPlugin> plugins = new HashMap<>();

		if(result!=null && !result.isEmpty()) {
			Map<String, JSONObject> remoteH = JsonUtils.fromJsonHashMap("plugins", result);
			Iterator<String> it = remoteH.keySet().iterator();
			while (it.hasNext()) {
			        String pluginKey = it.next();
			        String plugUID = pluginKey; 
			        JSONObject serialized = remoteH.get(pluginKey);	
			        serialized.put("uid",plugUID );
			        PersistentPluginData dataInstance = RemoteShadowPlugin.deserializeInstance(serialized);
			        RemoteShadowPlugin plugin = new RemoteShadowPlugin( pluginKey, dataInstance);
			        plugin.setPluginSource(stub);
			        plugins.put(plugUID, plugin);
		      }
		}
		return plugins;
	}
	
	
	
	@Override
	public String init() {
		return initContextWf();
	}
	@Override
	public String getKey() {
		return this.uid;
	}

}