package com.solitardj9.userProfile.serviceInterface.core.thingManagerInterface.model.exception;

import org.springframework.http.HttpStatus;

public class ExceptionThingControllerBadRequest extends Exception{
    //
	private static final long serialVersionUID = -4252045591775220254L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionThingControllerBadRequest() {
		//
    	super(ExceptionCodeThingController.Bad_Request.getMessage());
    	errCode = ExceptionCodeThingController.Bad_Request.getCode();
    	httpStatus = ExceptionCodeThingController.Bad_Request.getHttpStatus();
    }
    
	public ExceptionThingControllerBadRequest(Throwable cause) {
		//
		super(ExceptionCodeThingController.Bad_Request.getMessage(), cause);
		errCode = ExceptionCodeThingController.Bad_Request.getCode();
		httpStatus = ExceptionCodeThingController.Bad_Request.getHttpStatus();
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