package com.solitardj9.userProfile.serviceInterface.userManagerInterface.model.request;

import com.solitardj9.userProfile.serviceInterface.common.RequestDefault;
import com.solitardj9.userProfile.serviceInterface.core.thingManagerInterface.model.common.AttributePayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class RequestCreateUser extends RequestDefault {
	
	private AttributePayload attributePayload;
}