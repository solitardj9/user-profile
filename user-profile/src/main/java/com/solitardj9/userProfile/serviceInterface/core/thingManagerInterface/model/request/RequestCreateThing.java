package com.solitardj9.userProfile.serviceInterface.core.thingManagerInterface.model.request;

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
public class RequestCreateThing extends RequestDefault {
	
	private AttributePayload attributePayload;
	
	private String thingTypeName;
}