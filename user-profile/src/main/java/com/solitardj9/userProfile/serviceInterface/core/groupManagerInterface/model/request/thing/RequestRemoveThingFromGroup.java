package com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.request.thing;

import com.solitardj9.userProfile.serviceInterface.common.RequestDefault;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class RequestRemoveThingFromGroup extends RequestDefault {
	
	private String thingName;
	
	private String groupName;
}