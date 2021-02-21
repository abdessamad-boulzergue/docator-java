package com.apos.wizard;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandArgs {

	private String prefix;
	private String name;
	boolean evaluated=false;
	public CommandArgs(String prefix, String name) {
		this.prefix=prefix;
		Pattern ptr = Pattern.compile("(\\%\\w+\\%)");
		Matcher match = ptr.matcher(name);
		if(match.find())
		   this.name= name.substring(match.start()+1,match.end()-1);
		else {
			this.name=name;
			evaluated =true;
		}
	}

	public String getValue(Executor executor) {
		return evaluateParam(executor,name);
	}

	private String evaluateParam(Executor executor,String paramName) {
		return evaluated? this.name :  executor.getParam(paramName);
	}

	public String getPrefix() {
		return this.prefix;
	}


}
