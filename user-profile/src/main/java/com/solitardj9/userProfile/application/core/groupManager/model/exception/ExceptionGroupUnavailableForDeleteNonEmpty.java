package com.solitardj9.userProfile.application.core.groupManager.model.exception;

import org.springframework.http.HttpStatus;

import com.solitardj9.userProfile.application.common.CustomException;

public class ExceptionGroupUnavailableForDeleteNonEmpty extends Exception implements CustomException {
    //
	private static final long serialVersionUID = 720571016780341857L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionGroupUnavailableForDeleteNonEmpty() {
		//
    	super(ExceptionCodeGroup.Group_Unavailable_For_Delete_Non_Empty.getMessage());
    	errCode = ExceptionCodeGroup.Group_Unavailable_For_Delete_Non_Empty.getCode();
    	httpStatus = ExceptionCodeGroup.Group_Unavailable_For_Delete_Non_Empty.getHttpStatus();
    }
    
	public ExceptionGroupUnavailableForDeleteNonEmpty(Throwable cause) {
		//
		super(ExceptionCodeGroup.Group_Unavailable_For_Delete_Non_Empty.getMessage(), cause);
		errCode = ExceptionCodeGroup.Group_Unavailable_For_Delete_Non_Empty.getCode();
		httpStatus = ExceptionCodeGroup.Group_Unavailable_For_Delete_Non_Empty.getHttpStatus();
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