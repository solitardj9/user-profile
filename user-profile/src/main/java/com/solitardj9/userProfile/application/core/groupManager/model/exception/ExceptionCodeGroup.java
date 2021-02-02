package com.solitardj9.userProfile.application.core.groupManager.model.exception;

import org.springframework.http.HttpStatus;

public enum ExceptionCodeGroup {
    //
	Group_Bad_Request(400, "GroupBadRequest.", HttpStatus.BAD_REQUEST),
	Group_Not_Found(404, "GroupNotFound.", HttpStatus.NOT_FOUND),
	Group_Alreay_Exist(409, "GroupAlreayExist.", HttpStatus.CONFLICT),
	Group_Manager_Failure(500, "GroupManagerFailure.", HttpStatus.INTERNAL_SERVER_ERROR),
	Group_Unavailable_For_Delete_Non_Leaf(451, "GroupUnavailableForDeleteNonLeaf.", HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS),
	Group_Unavailable_For_Delete_Non_Empty(451, "GroupUnavailableForDeleteNonEmpty.", HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS)
    ;
 
    private Integer code;
    private String message;
    private HttpStatus httpStatus;
 
    ExceptionCodeGroup(Integer code, String msg, HttpStatus httpStatus) {
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