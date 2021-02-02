package com.solitardj9.userProfile.serviceInterface.core.thingManagerInterface.model.response;

import com.solitardj9.userProfile.serviceInterface.common.ResponseDefault;
import com.solitardj9.userProfile.serviceInterface.core.thingManagerInterface.model.common.AttributePayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ResponseDescribeThing extends ResponseDefault {
	
	private String thingId;
	
	private String thingName;
	
	private AttributePayload attributePayload;
	
	private String thingTypeName;
}