package com.apos.workflow.runtime;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;

import com.apos.plugins.IPluginLoad;
import com.apos.plugins.PluginLoader;
import com.apos.utils.FileUtils;
import com.apos.utils.ResourceTools;
import com.apos.utils.WorkflowResourceTools;

public class WorkflowModel {

	private PluginLoader pluginLoader;
	private File modelFile;
	private JSONArray model;
	private String workflowName;
	private boolean isChanged;
	private WorkflowProcess _root;
	
	public WorkflowModel(PluginLoader loader) {
		  pluginLoader = loader;
	}

	public static String getWorflowNameFromModelFile(String absolutePath, String encode) {
		
		String content = FileUtils.readFile(new File(absolutePath));
		JSONArray json = new JSONArray(content);
		return ResourceTools.getAttribute(json, ResourceTools.ATTR_NAME);
	}

	public void load(String absolutePath, String  encode, Map envParams) {
		String workflowContent = FileUtils.readFile(new File(absolutePath));
		loadModel(workflowContent,encode,envParams);
	}

	private void loadModel(String workflowContent, String encode, Map envParams) {
		 model = new JSONArray(workflowContent);
		loadModel(envParams);
	}

	private void loadModel(Map envParams) {
		workflowName = getWorkflowName();
		JSONArray pluginSrcTag = WorkflowResourceTools.getWorkflowPluginSources(model);
		JSONArray pluginSrc =ResourceTools.getChildren(pluginSrcTag);
		Iterator<Object> pluginSrcIter = pluginSrc.iterator();
        IPluginLoad singleLoader = PluginLoaderFactory.getPluginLoaderSingleton();

		while(pluginSrcIter.hasNext()) {
			JSONArray plugSrc = (JSONArray) pluginSrcIter.next();
			pluginLoader.configureSource(ResourceTools.getAttributes(plugSrc));
			singleLoader.configureSource(ResourceTools.getAttributes(plugSrc));
		}
		JSONArray processTag = WorkflowResourceTools.getWorkflowProcesses(model);
		JSONArray processes =ResourceTools.getChildren(processTag);
		Iterator<Object> processIter = processes.iterator();
		while(processIter.hasNext()) {
			JSONArray workflowProcess = (JSONArray) processIter.next();
			if (_root == null) {
				setRoot(new WorkflowProcess(singleLoader ));
		     }
			_root.setRunningEnvironment(envParams);
	        _root.load(workflowProcess);	        
		}
		isChanged = false;
	}

	private void setRoot(WorkflowProcess workflowProcessRoot) {
		 _root = workflowProcessRoot;
	}

	private JSONArray getPackageTag(JSONArray jsArray) {
		if(ResourceTools.WF_XPDL_PACKAGE_TYPE.equals(ResourceTools.getResourceType(jsArray))){
			return jsArray;
		}
		JSONArray child = ResourceTools.getChildren(jsArray);
		Iterator<Object> iter = child.iterator();
		if(iter.hasNext())
		    return getPackageTag((JSONArray) iter.next());
		else 
			return null;
	}

	public String getWorkflowName(){
		return ResourceTools.getAttribute(model, ResourceTools.ATTR_NAME);
	}

	public void load(String absolutePath, String encoding) {
		load(absolutePath, encoding, null);
	}

	public WorkflowProcess getRoot() {
		return _root;
	}

}
