package com.apos.plugins;

import java.util.Map;

public class PluginSourceFactory {

	private PluginSourceFactory() {
		
	}
	public static IPluginSource getSource(String type,Map<String, String> params) {
		
		if("socket".equals(type)) {
			return getSocketSource(params);
		}
		if("java".equals(type)) {
			return getJavaSource(params);
		}else {
			throw new IllegalArgumentException("unknow type of plugin source : " + type);
		}
		
	}

	private static IPluginSource getJavaSource(Map<String, String> params) {
		String pckg = params.get("package");
		return new JavaPluginSource(pckg);
	}
	private static IPluginSource getSocketSource(Map<String, String> params) {

		String id = params.get("id");
		String host = params.get("host");
		Integer port = Integer.valueOf(params.get("port"));
		return new PluginSocketLoader(id,host, port );
		
	}
	public static IPluginSource getSource(Map<String, String> params) {
		return getSource(params.get("type"), params);
	}
}
