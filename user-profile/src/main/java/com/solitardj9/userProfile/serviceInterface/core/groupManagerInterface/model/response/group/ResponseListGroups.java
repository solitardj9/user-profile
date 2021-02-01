package com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.response.group;

import java.util.List;

import com.solitardj9.userProfile.serviceInterface.common.ResponseDefault;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ResponseListGroups extends ResponseDefault {
	
	private List<String> groupNames;
}