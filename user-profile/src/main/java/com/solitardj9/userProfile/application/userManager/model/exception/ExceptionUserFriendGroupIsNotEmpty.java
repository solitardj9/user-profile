package com.solitardj9.userProfile.application.userManager.model.exception;

import org.springframework.http.HttpStatus;

import com.solitardj9.userProfile.application.common.CustomException;

public class ExceptionUserFriendGroupIsNotEmpty extends Exception implements CustomException {
    //
	private static final long serialVersionUID = 7822598506883277671L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionUserFriendGroupIsNotEmpty() {
		//
    	super(ExceptionCodeUser.User_Friend_Group_Is_Not_Empty.getMessage());
    	errCode = ExceptionCodeUser.User_Friend_Group_Is_Not_Empty.getCode();
    	httpStatus = ExceptionCodeUser.User_Friend_Group_Is_Not_Empty.getHttpStatus();
    }
    
	public ExceptionUserFriendGroupIsNotEmpty(Throwable cause) {
		//
		super(ExceptionCodeUser.User_Friend_Group_Is_Not_Empty.getMessage(), cause);
		errCode = ExceptionCodeUser.User_Friend_Group_Is_Not_Empty.getCode();
		httpStatus = ExceptionCodeUser.User_Friend_Group_Is_Not_Empty.getHttpStatus();
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