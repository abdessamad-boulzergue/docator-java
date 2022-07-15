package com.apos.wizard;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Executor implements IActionExecutionResult {
	String definitionFileName;
	Properties engineCmds;
	private Properties enginesArgs;
	public void loadEngineCommande(String definitionFileName) {
		this.definitionFileName = definitionFileName;
		engineCmds = new Properties();
		Reader inStream;
		try {
			
			inStream = new FileReader(new File(definitionFileName));
			engineCmds.load(inStream );
			inStream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void applyParam(String key,String name, HashMap<String,String> values) {
		String value = values.get(key);
		if (enginesArgs==null) 
			enginesArgs=new Properties();
		if(value!=null && name!=null)
			enginesArgs.put(name,value);  
	}
	public String getEnginePrefix(String engineKey) {
		return null;
	}
	public String buildCommand(String prefixEngine) {
		String command = null;
		StringBuilder commandLine = new StringBuilder();
		prefixEngine="assembly";
		if(engineCmds!=null && !engineCmds.isEmpty()) {
			command = (String)engineCmds.get(prefixEngine);
		}
		
		if(command!=null && !command.trim().isEmpty()) {
			ArrayList<CommandArgs> args = parseCommand(command);
			for(CommandArgs arg : args) {
				String value= arg.getValue(this);
				commandLine.append(arg.getPrefix()).append(" ").append(value);
			}
		}
		return commandLine.toString();
	}
	private ArrayList<CommandArgs> parseCommand(String cmd) {
		ArrayList<CommandArgs> args = new ArrayList<>();
		CommandArgs arg =null;
		Pattern ptr = Pattern.compile("(--\\w+|-\\w+)");
		Matcher match = ptr.matcher(cmd);
		int start=-1;
		int end=-1;
		String cmdVal="";
		String val="";
		while(match.find()) {
			if(end!=-1) {
			     cmdVal = cmd.substring(end+1, match.start()-1);
				  arg = new CommandArgs(val, cmdVal);
				  args.add(arg);
			}
			start = match.start();
			end = match.end();
			 val = cmd.substring(start,end);
						
		}
		if(match.hitEnd()) {
			cmdVal = cmd.substring(end+1, cmd.length());
			arg = new CommandArgs(val, cmdVal);
			args.add(arg);
         }
		return args;
	}
	public String getParam(String paramName) {
		return (enginesArgs!=null)? enginesArgs.getProperty(paramName) : null;
	}
	public void loadSystemVars(Object engineEnvironmentDefinition) {
	
		
	}
}
