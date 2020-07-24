package com.apos.parser;

import java.util.Hashtable;

public abstract class HierarchieParse implements Comparable<HierarchieParse> {

	public final static String    ENCODING_UTF8            = "UTF-8";
	public final static String    ENCODING_DEFAULT            = ENCODING_UTF8;
	public static final String ENCODING_NONE = null;
	 protected String              encoding                 = ENCODING_DEFAULT;
     protected Hashtable<String,String> _attributes=null;

	@Override
	public int compareTo(HierarchieParse o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	abstract void init();
	abstract String getType();

}
