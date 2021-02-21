package com.apos.parser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import com.apos.wizard.CommandArgs;

public class TestEngineArgsParse {

	@Test
	void testParser() {
		String cmd= "--cmd Save --user %user%[ --st %savedTemplate%][ --sp %savePath%] --sd sessions[ --sn %saveName%] -d %data% -t %template% --ctx %ctxFile%";
		StringBuilder cmdLine =new StringBuilder();
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
				System.out.println(val +" " +cmdVal);
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
		
		System.out.println(cmdLine);
	
	}
	void testParser1() {
		String cmd= "--cmd Save --user %user%[ --st %savedTemplate%][ --sp %savePath%] --sd sessions[ --sn %saveName%] -d %data% -t %template% --ctx %ctxFile%";
	
		Pattern ptr = Pattern.compile("(\\%\\w+\\%)+");
		Matcher match = ptr.matcher(cmd);
		while(match.find()) {
			String val = cmd.substring(match.start()+1,match.end()-1);
			System.out.println(val);
		}
	}
}
