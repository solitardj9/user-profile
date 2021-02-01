package com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.common;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttributePayload {
	
	private Map<String, Object> attributes;
	
	private Boolean merge;
}