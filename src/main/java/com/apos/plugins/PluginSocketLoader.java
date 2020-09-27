package com.apos.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.apos.socket.ClientStub;
import com.apos.utils.JsonUtils;
import com.sefas.workflow.runtime.PersistentPluginData;

public class PluginSocketLoader implements IPluginSource {
	
	private static final String _BLOB_START_ = "<BLOB>";
	private static final String _BLOB_END_ = "</BLOB>";
    private String host ;
	private Integer port;
	private ClientStub stub;
	
	public PluginSocketLoader(String host, Integer port) {
		this.host=host;
		this.port = port;
	    stub = new ClientStub(host, port);

	}
	
	public IPlugin get(String key) {
		

	      RemoteShadowPlugin plug =null;

	    try {
	      stub.startSession();
	      String jsonCommand="{\"json_command\":[\"ALC CLASS:/com.sefas.workflowservice.WorkflowFactory\",\"MTH loadPlugins\",\"DPRM args s\",\"SPRM args = 'SFS8bae9d8@174cbdb309f@-7fbc'\",\"DPRM args s\",\"SPRM args = 'toolboxes'\",\"DPRM args s\",\"SPRM args = ''\",\"BCALL\\n\"]}";
	      String result = stub.sendAndReceiveBlob("JSON " + jsonCommand + "\n");	      
	      
	      if (result.startsWith(_BLOB_START_)) {
	          StringBuffer buf = new StringBuffer(result);
	          int length = buf.length();
	          int offset = length - _BLOB_END_.length();
	          buf.delete(offset, length); // cleanup end
	          buf.delete(0, _BLOB_START_.length()); // cleanup start
	          result =  buf.toString();
	        }
	      
	      HashMap<String, String> remoteH= JsonUtils.fromJsonHashMap("plugins", result);
	      Iterator<String> it = remoteH.keySet().iterator();
	      while (it.hasNext()) {
		        String pluginKey = it.next();
		        String serialized = remoteH.get(pluginKey);
		     
		        PersistentPluginData dataInstance = RemoteShadowPlugin.deserializeInstance(serialized);
		        System.out.println(dataInstance.getName());
		        
		        switch (dataInstance.getType()) {
		        default:
		        	if(pluginKey.equals(key))
		        	plug =  new RemoteShadowPlugin( pluginKey, dataInstance);
		      }
	      }
	      stub.stopSession();
	    } catch (Exception e) {
	       e.printStackTrace();
	      System.out.println(e.getMessage());
	    }
		return plug;
	  
	}

	@Override
	public List<IPlugin> getAll() {
		

	      RemoteShadowPlugin plug =null;
			List<IPlugin> plugins= new ArrayList<IPlugin>();

	    try {
	      stub.startSession();
	      String jsonCommand="{\"json_command\":[\"ALC CLASS:/com.sefas.workflowservice.WorkflowFactory\",\"MTH loadPlugins\",\"DPRM args s\",\"SPRM args = 'SFS8bae9d8@174cbdb309f@-7fbc'\",\"DPRM args s\",\"SPRM args = 'sefasplugins'\",\"DPRM args s\",\"SPRM args = ''\",\"BCALL\\n\"]}";
	      String result = stub.sendAndReceiveBlob("JSON " + jsonCommand + "\n");	      
	      
	      if (result.startsWith(_BLOB_START_)) {
	          StringBuffer buf = new StringBuffer(result);
	          int length = buf.length();
	          int offset = length - _BLOB_END_.length();
	          buf.delete(offset, length); // cleanup end
	          buf.delete(0, _BLOB_START_.length()); // cleanup start
	          result =  buf.toString();
	        }
	      
	      HashMap<String, String> remoteH= JsonUtils.fromJsonHashMap("plugins", result);
	      Iterator<String> it = remoteH.keySet().iterator();
	      while (it.hasNext()) {
		        String pluginKey = it.next();
		        String serialized = remoteH.get(pluginKey);
		     
		        PersistentPluginData dataInstance = RemoteShadowPlugin.deserializeInstance(serialized);
		        System.out.println(dataInstance.getName());
		        plugins.add( new RemoteShadowPlugin( pluginKey, dataInstance));
	      }
	      stub.stopSession();
	    } catch (Exception e) {
	       e.printStackTrace();
	      System.out.println(e.getMessage());
	    }
		return plugins;
	  
	}

}
