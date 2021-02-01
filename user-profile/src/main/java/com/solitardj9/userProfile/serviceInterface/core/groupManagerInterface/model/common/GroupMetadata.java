package com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMetadata {
	
	private String parentGroupName;
	
	private List<String> rootToParentGroups;
}