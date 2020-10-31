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

import com.apos.rest.exceptions.SocketSendReceiveException;
import com.apos.socket.ClientStub;
import com.apos.utils.Base64;
import com.apos.utils.JsonUtils;
import com.sefas.workflow.runtime.PersistentPluginData;

public class PluginSocketLoader implements IPluginSource {
	
	private static final String BLOB_START = "<BLOB>";
	private static final String BLOB_END = "</BLOB>";
	private static final  String RETOK = "<OK>";
    private static final  String ENDRETOK = "</OK>";
    private String host ;
	private Integer port;
	private ClientStub stub;
	String contextWf;
	String uid =null;
	private static Logger logger = LoggerFactory.getLogger(PluginSocketLoader.class);
	public PluginSocketLoader(String host, Integer port) {
		this.host=host;
		this.port = port;
	    stub = new ClientStub(this.host, this.port);
	    this.uid = UUID.randomUUID().toString();
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
	protected String unmarshall(String content) throws UnmarshallException {
	    int whereRet = content.indexOf(RETOK);

	    if (whereRet == -1) {
	      throw new UnmarshallException(" failed : " +content);
	    }

	    int whereEndRet = content.indexOf(ENDRETOK);
	    int start = whereRet + RETOK.length();

	    return content.substring(start, whereEndRet);
	  }
	public void initContextWf(){
		contextWf  = runCommand("initContext",Arrays.asList());
		logger.debug(contextWf);
		
	}
	String runCommand(String mth , List<Object> params) {
		synchronized(stub) {
			String result = new String() ;
		try {
			stub.startSession();
			stub.marshall("ALC");
			stub.marshall("MTH ".concat(mth));
			params.stream().forEach(param->{
				try {
						if(param instanceof String) {
							stub.marshall("DPRM args s");
						}
						else if(param instanceof Integer) {
							stub.marshall("DPRM args d");	
						}
						stub.marshall("SPRM args = '".concat(String.valueOf(param)).concat("'"));
				} catch (SocketSendReceiveException e) {
					logger.error(e.getMessage());
				}
			});
			 result  = stub.marshall("CALL\n");
			close();
			
				return unmarshall(result);
				
			} catch (UnmarshallException | SocketSendReceiveException e) {
				logger.error(e.getMessage());
			}

	     return result;
		}
		
	}
	public Map<String, IPlugin> loadPluginsIn(String location){
		
		if(this.contextWf==null || this.contextWf.trim().isEmpty()) {
			initContextWf();
		}
		String result = runCommand("loadPlugins",Arrays.asList(contextWf,location));
		Map<String,IPlugin> plugins = new HashMap<>();

		if(result!=null && !result.isEmpty()) {
			Map<String, JSONObject> remoteH = JsonUtils.fromJsonHashMap("plugins", result);
			Iterator<String> it = remoteH.keySet().iterator();
			while (it.hasNext()) {
			        String pluginKey = it.next();
			        String plugUID = this.uid.concat("-").concat(Base64.encode(pluginKey.getBytes()));
			        JSONObject serialized = remoteH.get(pluginKey);	
			        serialized.put("uid",plugUID );
			        PersistentPluginData dataInstance = RemoteShadowPlugin.deserializeInstance(serialized);
			        plugins.put(plugUID, new RemoteShadowPlugin( pluginKey, dataInstance));
		      }
		}
		return plugins;
	}
	@Override
	public void init() {
		initContextWf();
	}

}