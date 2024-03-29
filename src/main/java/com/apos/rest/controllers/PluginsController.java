package com.apos.rest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apos.plugins.IPlugin;
import com.apos.plugins.IPluginLoad;

@RestController
@RequestMapping("/plugin")
@CrossOrigin
public class PluginsController {
	@Autowired
	IPluginLoad _ploader;
	
	@GetMapping
	public ResponseEntity<IPlugin> getPlugin(@RequestParam(name = "srcKey") String srcKey,@RequestParam(name = "key") String pluginKey){

		IPlugin plugin = _ploader.load(srcKey,pluginKey);
	     return ResponseEntity.status(HttpStatus.OK).body(plugin) ; 
	}
	
	@GetMapping(path = "/all")
	public ResponseEntity<List<IPlugin>> getPlugins(){
		List<IPlugin> plugin = _ploader.load();
	     return ResponseEntity.status(HttpStatus.OK).body(plugin) ; 
	}
}
