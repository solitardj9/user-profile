package com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.response.group;

import com.solitardj9.userProfile.serviceInterface.common.ResponseDefault;
import com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.common.AttributePayload;
import com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.common.GroupMetadata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ResponseDescribeGroup extends ResponseDefault {
	
	private String groupId;
	
	private String groupName;
	
	private AttributePayload attributePayload;
	
	private GroupMetadata groupMetadata;
}