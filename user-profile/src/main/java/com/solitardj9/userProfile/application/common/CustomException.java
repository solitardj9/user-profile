package com.solitardj9.userProfile.application.common;

import org.springframework.http.HttpStatus;

public interface CustomException {
	//
	public int getErrCode();
	
	public HttpStatus getHttpStatus();
}