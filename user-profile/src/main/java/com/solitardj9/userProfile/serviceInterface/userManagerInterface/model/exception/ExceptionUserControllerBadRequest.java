package com.solitardj9.userProfile.serviceInterface.userManagerInterface.model.exception;

import org.springframework.http.HttpStatus;

public class ExceptionUserControllerBadRequest extends Exception{
    //
	private static final long serialVersionUID = 6178713394714533600L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionUserControllerBadRequest() {
		//
    	super(ExceptionCodeUserController.Bad_Request.getMessage());
    	errCode = ExceptionCodeUserController.Bad_Request.getCode();
    	httpStatus = ExceptionCodeUserController.Bad_Request.getHttpStatus();
    }
    
	public ExceptionUserControllerBadRequest(Throwable cause) {
		//
		super(ExceptionCodeUserController.Bad_Request.getMessage(), cause);
		errCode = ExceptionCodeUserController.Bad_Request.getCode();
		httpStatus = ExceptionCodeUserController.Bad_Request.getHttpStatus();
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