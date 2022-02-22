package com.apos.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPatterns {

	
	public static void main(String[] args) {
		replacePattern();
	}
	public static void replacePattern() {
		
		byte data[] = new byte[0];
		System.out.println("data : hell");
		System.out.println(data[0]);
		System.out.println(data[1]);
		
		System.out.println("--------------fin data : ");
		
		String txt ="WEB_COMPONENT_P3_1_2_1_4 (18)";
		String pattern=" \\(\\d{1,3}\\)";
		Matcher matcher = Pattern.compile(pattern).matcher(txt);
		if(matcher.find())
		System.out.println(matcher.group());
		System.out.println(txt.replace(" ", ""));
		}
		
}
