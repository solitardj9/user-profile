package com.solitardj9.userProfile.serviceInterface.userManagerInterface.model.response;

import com.solitardj9.userProfile.serviceInterface.common.ResponseDefault;
import com.solitardj9.userProfile.serviceInterface.userManagerInterface.model.common.AttributePayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data	
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ResponseDescribeUser extends ResponseDefault {
	
	private String userId;
	
	private String userName;
	
	private AttributePayload attributePayload;
}