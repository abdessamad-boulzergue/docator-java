package com.apos.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

public class TestPatterns {

	
	@Test
	public void testPattern2() {
		String pattern="\\w+=\\w+";
		String msg="key2=value";
		 String patternOpertor = "(!=|<=|>=|=|<|>)";
		if(Pattern.matches(pattern, msg)) {
			  Matcher matcher = Pattern.compile(patternOpertor).matcher(msg);
			while (matcher.find()) {
				String matchedText = matcher.group(); 
				String key = msg.substring(0, matcher.start());
				String value = msg.substring(matcher.end(),msg.length());
				System.out.println(String.format("key : %s , value: %s , op: %s",key, value,matchedText));
			}
		}
	}
	public void testPattern() {

	
	String pattern3 = "^\\[(\\p{L}+[=<>]{1,2}(\\p{L}+|\\d+|_+)(\\p{L}*\\d*_*)+)(,\\p{L}+[=<>]{1,2}(\\p{L}|\\d+|_+)(\\p{L}*\\d*_*)+)*\\]$"; 
	String pattern5 = "\\p{L}+(=|<|>|!=|<=|>=)(\\p{L}+|\\d+|_+)(\\p{L}*\\d*_*)"; 
	String pattern6 = "(=|<|>|!=|<=|>=)"; 
	String pattern4 = "^\\[(\\p{L}+= {1}(\\p{L}+|\\d+|_+)(\\p{L}*\\d*_*)+)(,\\p{L}+=(\\p{L}|\\d+|_+)(\\p{L}*\\d*_*)+)*\\]$"; 
	Pattern regPat = Pattern.compile(pattern3);

	 List<String> textsKo = Arrays.asList("[ff<<<sdd,kjhs=95dfd_]","[ff=sd\"d,kjhs=95dfd_]","[lp:sdd,kjhs=p5dfd_]","[w=_6k__,=p8]","[=_6k__]","[w=_6:k__]","[w=_6#k__]","[w:_6k__]","[]","[w=6,k=","w=6k=_p6l_","[w=6k=_p6l_]","[w=6,k=_p6l_","w=6,k=_p6l_]",
			 "[w=6,k=]","[=6,k=_p6l_]","[w=,k=_p6l_]","[w=6,,k=_p6l_]");
	for(String text : textsKo) {
		Matcher matcher = regPat.matcher(text);
		display(matcher);
		assertEquals(false, regPat.matcher(text).find());
	}
	
	List<String> textsOk = Arrays.asList("[ws>=sdd,hs<dd_]","[ws>sdd,hs<dd_]","[ws>=sdd,hs=<dd_]","[wsds=sdd,kjhs=95dfd_]","[w=sdd]","[w=_6k__]","[w=6,o=6p_p6l_]","[w=6k,o=p_p6l_]","[w=6k,o=_p6l_]");
	for(String text : textsOk) {
		Matcher matcher = regPat.matcher(text);
		//display(matcher);
		display(Pattern.compile(pattern5).matcher(text));
		display(Pattern.compile(pattern6).matcher(text));
		assertEquals(true, regPat.matcher(text).find());

	}
	
	}
	void display(Matcher matcher) {
		while (matcher.find()) {   
			int start = matcher.start();
			int end = matcher.end();
			String matchedText = matcher.group();   
			System.out.println("match - " + matchedText); 
			//System.out.println(" start match end  - " + text.subSequence(start, end)); 
			}
	}
}
