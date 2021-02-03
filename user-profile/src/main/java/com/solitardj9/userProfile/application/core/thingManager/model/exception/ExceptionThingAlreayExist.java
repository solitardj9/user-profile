package com.solitardj9.userProfile.application.core.thingManager.model.exception;

import org.springframework.http.HttpStatus;

import com.solitardj9.userProfile.application.common.CustomException;

public class ExceptionThingAlreayExist extends Exception implements CustomException {
    //
	private static final long serialVersionUID = 1912212101694560496L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionThingAlreayExist() {
		//
    	super(ExceptionCodeThing.Thing_Alreay_Exist.getMessage());
    	errCode = ExceptionCodeThing.Thing_Alreay_Exist.getCode();
    	httpStatus = ExceptionCodeThing.Thing_Alreay_Exist.getHttpStatus();
    }
    
	public ExceptionThingAlreayExist(Throwable cause) {
		//
		super(ExceptionCodeThing.Thing_Alreay_Exist.getMessage(), cause);
		errCode = ExceptionCodeThing.Thing_Alreay_Exist.getCode();
		httpStatus = ExceptionCodeThing.Thing_Alreay_Exist.getHttpStatus();
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