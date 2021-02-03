package com.solitardj9.userProfile.application.userManager.model.exception;

import org.springframework.http.HttpStatus;

import com.solitardj9.userProfile.application.common.CustomException;

public class ExceptionUserFriendGroupHasChildGroup extends Exception implements CustomException {
    //
	private static final long serialVersionUID = 5586053370873186107L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionUserFriendGroupHasChildGroup() {
		//
    	super(ExceptionCodeUser.User_Friend_Group_Has_Child_Group.getMessage());
    	errCode = ExceptionCodeUser.User_Friend_Group_Has_Child_Group.getCode();
    	httpStatus = ExceptionCodeUser.User_Friend_Group_Has_Child_Group.getHttpStatus();
    }
    
	public ExceptionUserFriendGroupHasChildGroup(Throwable cause) {
		//
		super(ExceptionCodeUser.User_Friend_Group_Has_Child_Group.getMessage(), cause);
		errCode = ExceptionCodeUser.User_Friend_Group_Has_Child_Group.getCode();
		httpStatus = ExceptionCodeUser.User_Friend_Group_Has_Child_Group.getHttpStatus();
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