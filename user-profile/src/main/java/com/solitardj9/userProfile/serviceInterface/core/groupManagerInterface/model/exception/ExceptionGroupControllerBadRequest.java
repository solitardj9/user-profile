package com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.exception;

import org.springframework.http.HttpStatus;

public class ExceptionGroupControllerBadRequest extends Exception{
    //
	private static final long serialVersionUID = -8641117992385689736L;
	
	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionGroupControllerBadRequest() {
		//
    	super(ExceptionCodeGroupController.Bad_Request.getMessage());
    	errCode = ExceptionCodeGroupController.Bad_Request.getCode();
    	httpStatus = ExceptionCodeGroupController.Bad_Request.getHttpStatus();
    }
    
	public ExceptionGroupControllerBadRequest(Throwable cause) {
		//
		super(ExceptionCodeGroupController.Bad_Request.getMessage(), cause);
		errCode = ExceptionCodeGroupController.Bad_Request.getCode();
		httpStatus = ExceptionCodeGroupController.Bad_Request.getHttpStatus();
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