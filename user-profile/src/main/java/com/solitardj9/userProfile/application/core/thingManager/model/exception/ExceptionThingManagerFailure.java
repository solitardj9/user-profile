package com.solitardj9.userProfile.application.core.thingManager.model.exception;

import org.springframework.http.HttpStatus;

public class ExceptionThingManagerFailure extends Exception{
    //
	private static final long serialVersionUID = 7357666297530064739L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionThingManagerFailure() {
		//
    	super(ExceptionCodeThing.Thing_Manager_Failure.getMessage());
    	errCode = ExceptionCodeThing.Thing_Manager_Failure.getCode();
    	httpStatus = ExceptionCodeThing.Thing_Manager_Failure.getHttpStatus();
    }
    
	public ExceptionThingManagerFailure(Throwable cause) {
		//
		super(ExceptionCodeThing.Thing_Manager_Failure.getMessage(), cause);
		errCode = ExceptionCodeThing.Thing_Manager_Failure.getCode();
		httpStatus = ExceptionCodeThing.Thing_Manager_Failure.getHttpStatus();
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