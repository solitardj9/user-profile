package com.solitardj9.userProfile.application.core.groupManager.model.exception;

import org.springframework.http.HttpStatus;

public class ExceptionGroupAlreayExist extends Exception{
    //
	private static final long serialVersionUID = 9103682951043439590L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionGroupAlreayExist() {
		//
    	super(ExceptionCodeGroup.Group_Alreay_Exist.getMessage());
    	errCode = ExceptionCodeGroup.Group_Alreay_Exist.getCode();
    	httpStatus = ExceptionCodeGroup.Group_Alreay_Exist.getHttpStatus();
    }
    
	public ExceptionGroupAlreayExist(Throwable cause) {
		//
		super(ExceptionCodeGroup.Group_Alreay_Exist.getMessage(), cause);
		errCode = ExceptionCodeGroup.Group_Alreay_Exist.getCode();
		httpStatus = ExceptionCodeGroup.Group_Alreay_Exist.getHttpStatus();
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