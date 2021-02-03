package com.solitardj9.userProfile.application.userManager.model.exception;

import org.springframework.http.HttpStatus;

import com.solitardj9.userProfile.application.common.CustomException;

public class ExceptionUserBadRequest extends Exception implements CustomException {
	//
	private static final long serialVersionUID = 5039979337561167635L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionUserBadRequest() {
		//
    	super(ExceptionCodeUser.User_Bad_Request.getMessage());
    	errCode = ExceptionCodeUser.User_Bad_Request.getCode();
    	httpStatus = ExceptionCodeUser.User_Bad_Request.getHttpStatus();
    }
    
	public ExceptionUserBadRequest(Throwable cause) {
		//
		super(ExceptionCodeUser.User_Bad_Request.getMessage(), cause);
		errCode = ExceptionCodeUser.User_Bad_Request.getCode();
		httpStatus = ExceptionCodeUser.User_Bad_Request.getHttpStatus();
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