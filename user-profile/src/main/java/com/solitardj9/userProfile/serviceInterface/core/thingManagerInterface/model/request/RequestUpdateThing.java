package com.solitardj9.userProfile.serviceInterface.core.thingManagerInterface.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class RequestUpdateThing extends RequestCreateThing {
	
	 private Boolean removeThingType;
}