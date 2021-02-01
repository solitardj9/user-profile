package com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.request.thing;

import java.util.List;

import com.solitardj9.userProfile.serviceInterface.common.RequestDefault;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class RequestUpdateGroupsWithThing extends RequestDefault {
	
	private String thingName;
	
	private List<String> groupNamesToAdd;
	
	private List<String> groupNamesToRemove;
}