package com.solitardj9.userProfile.application.userManager.model.exception;

import org.springframework.http.HttpStatus;

import com.solitardj9.userProfile.application.common.CustomException;

public class ExceptionUserAlreayExist extends Exception implements CustomException {
    //
	private static final long serialVersionUID = -8905042274081202071L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionUserAlreayExist() {
		//
    	super(ExceptionCodeUser.User_Alreay_Exist.getMessage());
    	errCode = ExceptionCodeUser.User_Alreay_Exist.getCode();
    	httpStatus = ExceptionCodeUser.User_Alreay_Exist.getHttpStatus();
    }
    
	public ExceptionUserAlreayExist(Throwable cause) {
		//
		super(ExceptionCodeUser.User_Alreay_Exist.getMessage(), cause);
		errCode = ExceptionCodeUser.User_Alreay_Exist.getCode();
		httpStatus = ExceptionCodeUser.User_Alreay_Exist.getHttpStatus();
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