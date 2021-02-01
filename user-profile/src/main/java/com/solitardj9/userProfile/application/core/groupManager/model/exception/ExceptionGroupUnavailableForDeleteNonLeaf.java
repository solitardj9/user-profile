package com.solitardj9.userProfile.application.core.groupManager.model.exception;

import org.springframework.http.HttpStatus;

public class ExceptionGroupUnavailableForDeleteNonLeaf extends Exception{
    //
	private static final long serialVersionUID = 682345054698465032L;

	private final int errCode;
	
	private final HttpStatus httpStatus;
	
	public ExceptionGroupUnavailableForDeleteNonLeaf() {
		//
    	super(ExceptionCodeGroup.Group_Unavailable_For_Delete_Non_Leaf.getMessage());
    	errCode = ExceptionCodeGroup.Group_Unavailable_For_Delete_Non_Leaf.getCode();
    	httpStatus = ExceptionCodeGroup.Group_Unavailable_For_Delete_Non_Leaf.getHttpStatus();
    }
    
	public ExceptionGroupUnavailableForDeleteNonLeaf(Throwable cause) {
		//
		super(ExceptionCodeGroup.Group_Unavailable_For_Delete_Non_Leaf.getMessage(), cause);
		errCode = ExceptionCodeGroup.Group_Unavailable_For_Delete_Non_Leaf.getCode();
		httpStatus = ExceptionCodeGroup.Group_Unavailable_For_Delete_Non_Leaf.getHttpStatus();
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