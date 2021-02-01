package com.solitardj9.userProfile.serviceInterface.core.thingManagerInterface.model.response;

import com.solitardj9.userProfile.serviceInterface.common.ResponseDefault;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ResponseCreateThing extends ResponseDefault {
	
	private String thingId;
	
	private String thingName;
}