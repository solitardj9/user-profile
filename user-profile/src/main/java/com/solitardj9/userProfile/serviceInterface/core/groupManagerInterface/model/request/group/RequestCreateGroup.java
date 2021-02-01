package com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.request.group;

import com.solitardj9.userProfile.serviceInterface.common.RequestDefault;
import com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.common.AttributePayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class RequestCreateGroup extends RequestDefault {
	
	private AttributePayload attributePayload;
	
	private String parentGroupName;
}