package com.solitardj9.userProfile.application.core.thingManager.service;

import java.util.List;

import com.solitardj9.userProfile.application.core.thingManager.model.Thing;
import com.solitardj9.userProfile.application.core.thingManager.model.exception.ExceptionThingAlreayExist;
import com.solitardj9.userProfile.application.core.thingManager.model.exception.ExceptionThingBadRequest;
import com.solitardj9.userProfile.application.core.thingManager.model.exception.ExceptionThingManagerFailure;
import com.solitardj9.userProfile.application.core.thingManager.model.exception.ExceptionThingNotFound;

public interface ThingManager {
	//
	public Boolean isInitialized();
	
	public Thing createThing(String thingName, String thingTypeName, String attributes, Boolean merge) throws ExceptionThingBadRequest, ExceptionThingAlreayExist, ExceptionThingManagerFailure;
	
	public Thing createThing(Thing thing, Boolean merge) throws ExceptionThingBadRequest, ExceptionThingAlreayExist, ExceptionThingManagerFailure;
	
	public Thing getThingByThingName(String thingName) throws ExceptionThingBadRequest, ExceptionThingNotFound;
	
	public Boolean updateThing(String thingName, String thingTypeName, Boolean removeThingType, String attributes, Boolean merge) throws ExceptionThingBadRequest, ExceptionThingNotFound, ExceptionThingManagerFailure;
	
	public Boolean deleteThing(String thingName) throws ExceptionThingBadRequest, ExceptionThingNotFound, ExceptionThingManagerFailure;
	
	public List<Thing> getThings(String attributeName, String attributeValue, String thingTypeName);
}