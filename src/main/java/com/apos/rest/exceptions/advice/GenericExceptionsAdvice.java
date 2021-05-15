package com.apos.rest.exceptions.advice;

import java.util.NoSuchElementException;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.apos.plugins.PluginLoaderException;
import com.apos.rest.exceptions.DataFormatException;
import com.apos.socket.ClientSessionException;

@ControllerAdvice
public class GenericExceptionsAdvice {

	public JSONObject createErrorObject(int status , String message,String error) {
		JSONObject object = new JSONObject();
		object.put("status", status);
		object.put("error", error);
		object.put("message", message);

		return object;
	}
	
	@ResponseBody
	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String noSuchElement(NoSuchElementException ex) {
		return createErrorObject(HttpStatus.NOT_FOUND.value(), ex.getMessage() , "element not found").toString() ;
	}
	@ResponseBody
	@ExceptionHandler(DataFormatException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String dataFormatExeption(DataFormatException ex) {
		return createErrorObject(HttpStatus.BAD_REQUEST.value(), ex.getMessage() , "bad format").toString() ;
	}
	
	@ResponseBody
	@ExceptionHandler(PluginLoaderException.class)
	@ResponseStatus(HttpStatus.OK)
	String dataFormatExeption(Exception ex) {
		return createErrorObject(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage() , "error loading plugins").toString() ;
	}
	
}
