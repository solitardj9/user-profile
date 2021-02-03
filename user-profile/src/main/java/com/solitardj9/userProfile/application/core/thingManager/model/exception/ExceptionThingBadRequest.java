package com.solitardj9.userProfile.application.core.thingManager.model.exception;

import org.springframework.http.HttpStatus;

import com.solitardj9.userProfile.application.common.CustomException;

public class ExceptionThingBadRequest extends Exception implements CustomException {
	//
	private static final long serialVersionUID = 5583726440141738994L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionThingBadRequest() {
		//
    	super(ExceptionCodeThing.Thing_Bad_Request.getMessage());
    	errCode = ExceptionCodeThing.Thing_Bad_Request.getCode();
    	httpStatus = ExceptionCodeThing.Thing_Bad_Request.getHttpStatus();
    }
    
	public ExceptionThingBadRequest(Throwable cause) {
		//
		super(ExceptionCodeThing.Thing_Bad_Request.getMessage(), cause);
		errCode = ExceptionCodeThing.Thing_Bad_Request.getCode();
		httpStatus = ExceptionCodeThing.Thing_Bad_Request.getHttpStatus();
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