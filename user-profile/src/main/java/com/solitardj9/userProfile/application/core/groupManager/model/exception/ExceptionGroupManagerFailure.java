package com.solitardj9.userProfile.application.core.groupManager.model.exception;

import org.springframework.http.HttpStatus;

import com.solitardj9.userProfile.application.common.CustomException;

public class ExceptionGroupManagerFailure extends Exception implements CustomException {
    //
	private static final long serialVersionUID = 5675360279553281702L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionGroupManagerFailure() {
		//
    	super(ExceptionCodeGroup.Group_Manager_Failure.getMessage());
    	errCode = ExceptionCodeGroup.Group_Manager_Failure.getCode();
    	httpStatus = ExceptionCodeGroup.Group_Manager_Failure.getHttpStatus();
    }
    
	public ExceptionGroupManagerFailure(Throwable cause) {
		//
		super(ExceptionCodeGroup.Group_Manager_Failure.getMessage(), cause);
		errCode = ExceptionCodeGroup.Group_Manager_Failure.getCode();
		httpStatus = ExceptionCodeGroup.Group_Manager_Failure.getHttpStatus();
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