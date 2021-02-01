package com.solitardj9.userProfile.application.core.groupManager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {
	
	private Integer id;
	
	private String groupName;
	
	private String attributes;
	
	private String groupTypeName;
	
	private String parentGroupName;
}