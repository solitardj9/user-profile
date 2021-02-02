package com.solitardj9.userProfile.application.core.thingManager.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solitardj9.userProfile.application.core.thingManager.model.Thing;
import com.solitardj9.userProfile.application.core.thingManager.model.exception.ExceptionThingAlreayExist;
import com.solitardj9.userProfile.application.core.thingManager.model.exception.ExceptionThingBadRequest;
import com.solitardj9.userProfile.application.core.thingManager.model.exception.ExceptionThingManagerFailure;
import com.solitardj9.userProfile.application.core.thingManager.model.exception.ExceptionThingNotFound;
import com.solitardj9.userProfile.application.core.thingManager.service.ThingManager;
import com.solitardj9.userProfile.application.core.thingManager.service.dao.ThingNativeQueryRepository;
import com.solitardj9.userProfile.application.core.thingManager.service.dao.ThingRepository;
import com.solitardj9.userProfile.application.core.thingManager.service.dao.dto.ThingDto;

@Service("thingManager")
public class ThingManagerImpl implements ThingManager {

	private static final Logger logger = LoggerFactory.getLogger(ThingManagerImpl.class);
	
	@Autowired
	ThingNativeQueryRepository thingNativeQueryRepository;
	
	@Autowired
	ThingRepository thingRepository;
	
	private Boolean isInitialized = false;
	
	@PostConstruct
	public void init() {
		//
		thingNativeQueryRepository.createThingTable();
		isInitialized = true;
	}
	
	@Override
	public Boolean isInitialized() {
		return isInitialized;
	}

	@Override
	public Thing createThing(String thingName, String thingTypeName, String attributes, Boolean merge) throws ExceptionThingBadRequest, ExceptionThingAlreayExist, ExceptionThingManagerFailure {
		//
		if (thingName == null || thingName.isEmpty()) {
			logger.error("[ThingManager].createThing : error = thing name is invalid.");
			throw new ExceptionThingBadRequest();
		}
		
		ThingDto thingDto = getThing(thingName);
		if (thingDto != null) {
			logger.error("[ThingManager].createThing : error = thing is already exist.");
			throw new ExceptionThingAlreayExist();
		}
		
		try {
			thingDto = new ThingDto(null, thingName, attributes, thingTypeName);
			saveThing(thingDto);
			return convertThingDtoToThing(getThing(thingName));
		} catch (Exception e) {
			logger.error("[ThingManager].createThing : error = " + e);
			throw new ExceptionThingManagerFailure();
		}
	}
	
	public Thing createThing(Thing thing, Boolean merge) throws ExceptionThingBadRequest, ExceptionThingAlreayExist, ExceptionThingManagerFailure {
		//
		String thingName = thing.getThingName();
		
		if (thingName == null || thingName.isEmpty()) {
			logger.error("[ThingManager].createThing : error = thing name is invalid.");
			throw new ExceptionThingBadRequest();
		}
		
		ThingDto thingDto = getThing(thingName);
		if (thingDto != null) {
			logger.error("[ThingManager].createThing : error = thing is already exist.");
			throw new ExceptionThingAlreayExist();
		}
		
		try {
			thingDto = convertThingToThingDto(thing);
			saveThing(thingDto);
			return convertThingDtoToThing(getThing(thingName));
		} catch (Exception e) {
			logger.error("[ThingManager].createThing : error = " + e);
			throw new ExceptionThingManagerFailure();
		}
	}

	@Override
	public Thing getThingByThingName(String thingName) throws ExceptionThingBadRequest, ExceptionThingNotFound {
		//
		if (thingName == null || thingName.isEmpty()) {
			logger.error("[ThingManager].getThingByThingName : error = thing name is invalid.");
			throw new ExceptionThingBadRequest();
		}
		
		ThingDto thingDto = getThing(thingName);
		if (thingDto == null) {
			logger.error("[ThingManager].getThingByThingName : error = thing is not exist.");
			throw new ExceptionThingNotFound();
		}
		
		return convertThingDtoToThing(thingDto);
	}

	@Override
	public Boolean updateThing(String thingName, String thingTypeName, Boolean removeThingType, String attributes, Boolean merge) throws ExceptionThingBadRequest, ExceptionThingNotFound, ExceptionThingManagerFailure {
		//
		try {
			if (thingName == null || thingName.isEmpty()) {
				logger.error("[ThingManager].updateThing : error = thing name is invalid.");
				throw new ExceptionThingBadRequest();
			}
			
			ThingDto thingDto = getThing(thingName);
			if (thingDto == null) {
				logger.error("[ThingManager].updateThing : error = thing is not exist.");
				throw new ExceptionThingNotFound();
			}
			
			if (removeThingType)
				thingDto.setThingTypeName(null);
			else
				thingDto.setThingTypeName(thingTypeName);
			
			try {
				if (merge) {
					Thing tmpThing = convertThingDtoToThing(thingDto);
					tmpThing.mergeAttributes(attributes);
					thingDto.setAttributes(tmpThing.getAttributes());
				}
				else {
					thingDto.setAttributes(attributes);
				}
			} catch (Exception e) {
				logger.error("[ThingManager].updateThing : error = attributes is invallid. " + e);
			}
		
			saveThing(thingDto);
			return true;
		} catch (Exception e) {
			logger.error("[ThingManager].updateThing : error = " + e);
			throw new ExceptionThingManagerFailure();
		}
	}

	@Override
	public Boolean deleteThing(String thingName) throws ExceptionThingBadRequest, ExceptionThingNotFound, ExceptionThingManagerFailure {
		//
		if (thingName == null || thingName.isEmpty()) {
			logger.error("[ThingManager].deleteThing : error = thing name is invalid.");
			throw new ExceptionThingBadRequest();
		}
		
		ThingDto thingDto = getThing(thingName);
		if (thingDto == null) {
			logger.error("[ThingManager].deleteThing : error = thing is not exist.");
			throw new ExceptionThingNotFound();
		}
		
		try {
			deleteThing(thingDto);
			return true;
		} catch (Exception e) {
			logger.error("[ThingManager].deleteThing : error = " + e);
			throw new ExceptionThingManagerFailure();
		}
	}

	@Override
	public List<Thing> getThings(String attributeName, String attributeValue, String thingTypeName) {
		//
		List<Thing> things = new ArrayList<>();
		List<ThingDto> thingDts = selectThings(attributeName, attributeValue, thingTypeName);
		if (thingDts != null) {
			for(ThingDto iter : thingDts) {
				things.add(convertThingDtoToThing(iter));
			}
		}
		
		return things;
	}
	
	@Override
	public Boolean isValidThing(String thingName) {
		//
		if (thingName == null || thingName.isEmpty())
			return false;
		
		try {
			return isExist(thingName);
		} catch(Exception e) {
			logger.error("[ThingManager].isValidThing : error = " + e);
			return false;
		}
	}
	
	private ThingDto getThing(String thingName) {
		//
		return thingRepository.findByThingName(thingName);
	}
	
	private void saveThing(ThingDto thingDto) {
		//
		thingRepository.save(thingDto);
	}
	
	private void deleteThing(ThingDto thingDto) {
		//
		thingRepository.delete(thingDto);
	}
	
	private Boolean isExist(String thingName) {
		//
		return thingRepository.existsByThingName(thingName);
	}
	
	private List<ThingDto> selectThings(String attributeName, String attributeValue, String thingTypeName) {
		//
		return thingNativeQueryRepository.selectThings(thingTypeName, attributeName, attributeValue);
	}
	
	private Thing convertThingDtoToThing(ThingDto thingDto) {
		//
		return new Thing(thingDto.getId().toString(), thingDto.getThingName(), thingDto.getAttributes(), thingDto.getThingTypeName());
	}
	
	private ThingDto convertThingToThingDto(Thing thing) {
		//
		return new ThingDto(null, thing.getThingName(), thing.getAttributes(), thing.getThingTypeName());
	}
}