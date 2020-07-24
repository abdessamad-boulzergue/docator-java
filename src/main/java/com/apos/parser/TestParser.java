package com.apos.parser;

public class TestParser {

	public static void main(String[] args) {
		
		DomHierarchieParse dom = new DomHierarchieParse("<myNode><a id='5'><![CDATA[DEFAULT_PROCESSING_TASKS]]></a></myNode>");
		
		XMLtoJSON xToJs = new XMLtoJSON(dom);
		
		System.out.println(xToJs.toJson());
	}
	
}
