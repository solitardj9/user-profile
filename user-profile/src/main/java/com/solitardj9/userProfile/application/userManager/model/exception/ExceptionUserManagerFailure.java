package com.solitardj9.userProfile.application.userManager.model.exception;

import org.springframework.http.HttpStatus;

import com.solitardj9.userProfile.application.common.CustomException;

public class ExceptionUserManagerFailure extends Exception implements CustomException {
    //
	private static final long serialVersionUID = 583503969245227795L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionUserManagerFailure() {
		//
    	super(ExceptionCodeUser.User_Manager_Failure.getMessage());
    	errCode = ExceptionCodeUser.User_Manager_Failure.getCode();
    	httpStatus = ExceptionCodeUser.User_Manager_Failure.getHttpStatus();
    }
    
	public ExceptionUserManagerFailure(Throwable cause) {
		//
		super(ExceptionCodeUser.User_Manager_Failure.getMessage(), cause);
		errCode = ExceptionCodeUser.User_Manager_Failure.getCode();
		httpStatus = ExceptionCodeUser.User_Manager_Failure.getHttpStatus();
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