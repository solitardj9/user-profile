package com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.exception;

import org.springframework.http.HttpStatus;

public enum ExceptionCodeGroupController {
    //
	Bad_Request(400, "BadRequest.", HttpStatus.BAD_REQUEST)
    ;
 
    private Integer code;
    private String message;
    private HttpStatus httpStatus;
 
    ExceptionCodeGroupController(Integer code, String msg, HttpStatus httpStatus) {
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