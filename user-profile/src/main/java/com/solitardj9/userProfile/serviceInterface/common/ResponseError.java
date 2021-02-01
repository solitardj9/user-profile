package com.solitardj9.userProfile.serviceInterface.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ResponseError extends ResponseDefault {
	
	private String message;
	
	private Integer error;
}