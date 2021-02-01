package com.solitardj9.userProfile.application.core.groupManager.model.exception;

import org.springframework.http.HttpStatus;

public class ExceptionGroupNotFound extends Exception{
    //
	private static final long serialVersionUID = -3494134810296213776L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionGroupNotFound() {
		//
    	super(ExceptionCodeGroup.Group_Not_Found.getMessage());
    	errCode = ExceptionCodeGroup.Group_Not_Found.getCode();
    	httpStatus = ExceptionCodeGroup.Group_Not_Found.getHttpStatus();
    }
    
	public ExceptionGroupNotFound(Throwable cause) {
		//
		super(ExceptionCodeGroup.Group_Not_Found.getMessage(), cause);
		errCode = ExceptionCodeGroup.Group_Not_Found.getCode();
		httpStatus = ExceptionCodeGroup.Group_Not_Found.getHttpStatus();
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