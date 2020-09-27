package com.apos.utils;

import java.io.UnsupportedEncodingException;

public class Base64 {

	 public static String encode(byte[] raw) {
		 //MO-7194
		 String result = null;
		 try {
			 result = new String(org.apache.commons.codec.binary.Base64.encodeBase64(raw), "UTF-8");
		 } catch (UnsupportedEncodingException e) {
			 e.printStackTrace();
		 }
		 return result;
	 }
	 
	  public static byte[] decode(String base64) {
		//MO-7194
	    return org.apache.commons.codec.binary.Base64.decodeBase64(base64.getBytes());
	  }
}
