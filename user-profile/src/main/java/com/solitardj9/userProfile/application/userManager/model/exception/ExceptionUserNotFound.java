package com.solitardj9.userProfile.application.userManager.model.exception;

import org.springframework.http.HttpStatus;

import com.solitardj9.userProfile.application.common.CustomException;

public class ExceptionUserNotFound extends Exception implements CustomException {
    //
	private static final long serialVersionUID = -1951746001980764917L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionUserNotFound() {
		//
    	super(ExceptionCodeUser.User_Not_Found.getMessage());
    	errCode = ExceptionCodeUser.User_Not_Found.getCode();
    	httpStatus = ExceptionCodeUser.User_Not_Found.getHttpStatus();
    }
    
	public ExceptionUserNotFound(Throwable cause) {
		//
		super(ExceptionCodeUser.User_Not_Found.getMessage(), cause);
		errCode = ExceptionCodeUser.User_Not_Found.getCode();
		httpStatus = ExceptionCodeUser.User_Not_Found.getHttpStatus();
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