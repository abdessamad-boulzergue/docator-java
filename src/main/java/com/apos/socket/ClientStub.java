package com.apos.socket;

import java.util.HashMap;
import java.util.Iterator;

import com.apos.plugins.RemoteShadowPlugin;
import com.apos.utils.JsonUtils;
import com.sefas.workflow.runtime.PersistentPluginData;

public class ClientStub {

	private static final String DEFAULT_ENCODING = "utf-8";
	private static final String _BLOB_START_ = "<BLOB>";
	private static final String _BLOB_END_ = "</BLOB>";
	private String host;
	private int port;
	private String  encoding = DEFAULT_ENCODING;
	private ClientSession session;
	
	public ClientStub(String host, int port,String encoding) {
		this.port=port;
		this.host=host;
		this.encoding=encoding;
		this.session = new ClientSession();
	}
	public ClientStub(String host, int port) {
		this.port=port;
		this.host=host;
		this.encoding=DEFAULT_ENCODING;
		this.session = new ClientSession();
	}
	
	public void startSession() {
		synchronized(session) {
			
			if(!session.isRunning()) {
				session.start(this.host,this.port,this.encoding);
			}
			
		}
	}
	public void stopSession() {
		synchronized(session) {
			
			if(session.isRunning()) {
				session.stop();
			}
			
		}
	}
	
	//test Client Connection
	// send command
	public static void main(String[] args) {
	    ClientStub stub = new ClientStub("127.0.0.1", 29900, "UTF-8");

	    try {
	      stub.startSession();
	      String jsonCommand="{\"json_command\":[\"ALC CLASS:/com.sefas.workflowservice.WorkflowFactory\",\"MTH loadPlugins\",\"DPRM args s\",\"SPRM args = 'SFS-68cb3330@17401169338@-7ff8'\",\"DPRM args s\",\"SPRM args = 'toolboxes'\",\"DPRM args s\",\"SPRM args = ''\",\"BCALL\\n\"]}";
	      String result = stub.sendAndReceiveBlob("JSON " + jsonCommand + "\n");
	      System.out.println(result);
	      
	      
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
	        String key = it.next();
	        String serialized = remoteH.get(key);
	     
	        PersistentPluginData dataInstance = RemoteShadowPlugin.deserializeInstance(serialized);
	        System.out.println(dataInstance.getName());
	      }
	      
	      stub.stopSession();
	    } catch (Exception e) {
	       e.printStackTrace();
	      System.out.println(e.getMessage());
	    }
	  }
	
	
	private static void getPluginFromSerialized(String key, String cur) {
		
	}
	private String sendAndWaitReceive(String str) {
		synchronized(this.session) {
			try {
				if(!str.endsWith(ClientSession.EOT)) {
					str=str.concat(ClientSession.EOT);
				}
				session.send(str);
				String received = session.receive();
				return received;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public String sendAndReceiveBlob(String str) {
		synchronized(this.session) {
			try {
				if(!str.endsWith(ClientSession.EOT)) {
					str = str.concat(ClientSession.EOT);
				}
				session.send(str);
				String received = session.receiveBlobStr();
				return received;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
}
