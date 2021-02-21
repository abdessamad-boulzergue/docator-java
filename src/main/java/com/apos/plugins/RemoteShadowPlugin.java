package com.apos.plugins;

import org.json.JSONObject;

import com.apos.socket.ClientStub;
import com.apos.utils.DataSerializer;
import com.apos.utils.SerializerException;
import com.apos.workflow.plugin.IApplication;
import com.apos.workflow.runtime.PersistentPluginData;

public class RemoteShadowPlugin implements IPlugin {

	private PersistentPluginData localIntance;
    private  RemoteScriptlet implementation;
	private ClientStub stub;

	public RemoteShadowPlugin(String pluginKey, PersistentPluginData dataInstance) {
		this.localIntance = dataInstance;
	}

	public static PersistentPluginData deserializeInstance(String serialized) throws SerializerException  {
		return (PersistentPluginData) DataSerializer.deserialize(serialized);
	}

	@Override
	  public byte[] getIcon() {
	    if (localIntance == null) { return null; }
	    return localIntance.getIcon();
	  }

	  @Override
	  public String getName() {
	    if (localIntance == null) { return null; }
	    return localIntance.getName();
	  }
	  
	  @Override
	public String getType() {
		  if (localIntance == null) { return null; }
		    return String.valueOf(localIntance.getType());
	}

	@Override
	public String getId() {
		return localIntance.getPythonFileName();
	}
	@Override
	public String getUId() {
		return localIntance.getUId();
	}

	public static PersistentPluginData deserializeInstance(JSONObject serialized) {
		return  PersistentPluginData.fromJson(serialized);
	}

	@Override
	public boolean isStarting() {
		return false;
	}

	@Override
	public EnginesScriptlet getImplementation() {
		if(implementation==null) {
			implementation = new RemoteScriptlet();
			implementation.setPluginSource(stub);
		}
		implementation.init();
		return implementation;
	}

	@Override
	public void setApplicationParent(IApplication application) {
		
	}

	@Override
	public void addFactoryInformation(JSONObject extendedAttributes) {
	}

	@Override
	public void clear() {		
	}

	public void setPluginSource(ClientStub stub) {
		this.stub = stub;
	}

}
