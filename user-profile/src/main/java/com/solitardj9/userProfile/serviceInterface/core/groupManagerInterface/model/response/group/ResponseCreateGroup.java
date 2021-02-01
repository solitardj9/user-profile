package com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.response.group;

import com.solitardj9.userProfile.serviceInterface.common.ResponseDefault;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ResponseCreateGroup extends ResponseDefault {
	
	private String groupId;
	
	private String groupName;
}