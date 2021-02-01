package com.solitardj9.userProfile.application.core.thingManager.model.exception;

import org.springframework.http.HttpStatus;

public class ExceptionThingNotFound extends Exception{
    //
	private static final long serialVersionUID = 4373100817960844011L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionThingNotFound() {
		//
    	super(ExceptionCodeThing.Thing_Not_Found.getMessage());
    	errCode = ExceptionCodeThing.Thing_Not_Found.getCode();
    	httpStatus = ExceptionCodeThing.Thing_Not_Found.getHttpStatus();
    }
    
	public ExceptionThingNotFound(Throwable cause) {
		//
		super(ExceptionCodeThing.Thing_Not_Found.getMessage(), cause);
		errCode = ExceptionCodeThing.Thing_Not_Found.getCode();
		httpStatus = ExceptionCodeThing.Thing_Not_Found.getHttpStatus();
	}
	
	public int getErrCode() {
		//
		return errCode;
    }
	
	public HttpStatus getHttpStatus() {
		//
		return httpStatus;
    }
}