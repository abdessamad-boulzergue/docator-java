package com.apos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.apos.plugins.IPlugin;
import com.apos.plugins.PluginSocketLoader;

public class SocketPluginLoader {

	@Test
	void loadPlugins() {
		PluginSocketLoader loader = new PluginSocketLoader("127.0.0.1", 19902);
		loader.init();
		Map<String, IPlugin> plugs = loader.getAll();
		loader.close();
		assertNotNull(plugs);
		assertEquals(true, plugs.size()!=0);
		loader.close();
	}
	
}
