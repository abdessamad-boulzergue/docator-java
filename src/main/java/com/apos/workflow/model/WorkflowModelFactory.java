package com.apos.workflow.model;

import org.json.JSONObject;

import com.apos.plugins.IPlugin;
import com.apos.plugins.IPluginLoad;

public class WorkflowModelFactory {

	private WorkflowModelFactory() {
		
	}
	public static IPlugin buildPluginComponent(IPluginLoad loader, JSONObject extendedAttributes)  {
		 int factoryType = WorkflowModelFactory.getFactoryType(extendedAttributes);
		 switch (factoryType) {
		        case IPlugin.FACTORY_TYPE_PLUGIN_JAVA:
		          return loader.getJavaPlugin(IPlugin.PLUGIN_JAVA, extendedAttributes.getString(IPlugin.PLUGIN_JAVA));
		        case IPlugin.FACTORY_TYPE_PLUGIN_PYTHON:
		              return loader.getPlugin(extendedAttributes.getString(IPlugin.PLUGIN_SRC_ID),extendedAttributes.getString(IPlugin.PLUGIN_PYTHON), null);
		 }
		 
		return null;
	}

	private static int getFactoryType(JSONObject attributes) {
		if(attributes.has(IPlugin.PLUGIN_JAVA)){
            return 0;
        }else if(attributes.has(IPlugin.PLUGIN_PYTHON)){
            return 1;
        }
		return -1;
	}

}
