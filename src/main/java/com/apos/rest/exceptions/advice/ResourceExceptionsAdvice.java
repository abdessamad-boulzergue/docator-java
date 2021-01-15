package com.apos.rest.exceptions.advice;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.ResourceAccessException;

import com.apos.rest.exceptions.AposTransactionException;
import com.apos.rest.exceptions.AposValidationException;
import com.apos.rest.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class ResourceExceptionsAdvice {

	private static final String NOT_FOUND="Resource not found";
	private static final String ACCESS_FAILED="resource access failed";
	private static final String VALIDATE_FAILED="entity validation error";
	public JSONObject createErrorObject(int status , String message,String error) {
		JSONObject object = new JSONObject();
		object.put("status", status);
		object.put("error", error);
		object.put("message", message);

		return object;
	}
	
	@ResponseBody
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String entityNotFound( ResourceNotFoundException ex) {
		return createErrorObject(HttpStatus.NOT_FOUND.value(), ex.getMessage(), NOT_FOUND).toString();
	}
	
	@ResponseBody
	@ExceptionHandler(ResourceAccessException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	String  resourceAccessException( ResourceAccessException ex) {
		return  createErrorObject(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), ACCESS_FAILED).toString();
	}
	
	@ResponseBody
	@ExceptionHandler(AposTransactionException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	String  transactionException( AposTransactionException ex) {
		return  createErrorObject(HttpStatus.INTERNAL_SERVER_ERROR.value()," transaction error occured ", ACCESS_FAILED).toString();
	}
	
	@ResponseBody
	@ExceptionHandler(AposValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String  validationException( AposValidationException ex) {
		return  createErrorObject(HttpStatus.INTERNAL_SERVER_ERROR.value(), "invalide user's data" , VALIDATE_FAILED).toString();
	}
}
