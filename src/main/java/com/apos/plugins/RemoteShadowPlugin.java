package com.apos.plugins;

import org.json.JSONObject;

import com.apos.utils.DataSerializer;
import com.apos.utils.SerializerException;
import com.apos.workflow.runtime.PersistentPluginData;

public class RemoteShadowPlugin implements IPlugin {

	private PersistentPluginData localIntance;

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

}
