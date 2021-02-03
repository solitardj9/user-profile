package com.solitardj9.userProfile.application.userManager.model.exception;

import org.springframework.http.HttpStatus;

public enum ExceptionCodeUser {
    //
	User_Bad_Request(400, "UserBadRequest.", HttpStatus.BAD_REQUEST),
	User_Not_Found(404, "UserNotFound.", HttpStatus.NOT_FOUND),
	User_Alreay_Exist(409, "UserAlreayExist.", HttpStatus.CONFLICT),
	User_Manager_Failure(500, "UserManagerFailure.", HttpStatus.INTERNAL_SERVER_ERROR),
	User_Friend_Group_Has_Child_Group(451, "UserFriendGroupHasChildGroup.", HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS),
	User_Friend_Group_Is_Not_Empty(451, "UserFriendGroupIsNotEmpty.", HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS)
    ;
 
    private Integer code;
    private String message;
    private HttpStatus httpStatus;
 
    ExceptionCodeUser(Integer code, String msg, HttpStatus httpStatus) {
        this.code = code;
        this.message = msg;
        this.httpStatus = httpStatus;
    }
    
    public Integer getCode() {
        return this.code;
    }
    
    public String getMessage() {
        return this.message;
    }

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}