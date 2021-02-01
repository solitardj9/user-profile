package com.solitardj9.userProfile.application.core.groupManager.model.exception;

import org.springframework.http.HttpStatus;

public class ExceptionGroupBadRequest extends Exception{
	//
	private static final long serialVersionUID = 1320124312301679075L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionGroupBadRequest() {
		//
    	super(ExceptionCodeGroup.Group_Bad_Request.getMessage());
    	errCode = ExceptionCodeGroup.Group_Bad_Request.getCode();
    	httpStatus = ExceptionCodeGroup.Group_Bad_Request.getHttpStatus();
    }
    
	public ExceptionGroupBadRequest(Throwable cause) {
		//
		super(ExceptionCodeGroup.Group_Bad_Request.getMessage(), cause);
		errCode = ExceptionCodeGroup.Group_Bad_Request.getCode();
		httpStatus = ExceptionCodeGroup.Group_Bad_Request.getHttpStatus();
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