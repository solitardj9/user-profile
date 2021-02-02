package com.solitardj9.userProfile.serviceInterface.core.thingManagerInterface.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.solitardj9.microiot.application.thing.groupManager.service.ThingGroupManager;
//import com.solitardj9.microiot.application.thing.thingManager.model.exception.ExceptionThingVersionMismatch;
//import com.solitardj9.microiot.util.reqExpUtil.RegExpUtil;
import com.solitardj9.userProfile.application.core.thingManager.model.Thing;
import com.solitardj9.userProfile.application.core.thingManager.model.exception.ExceptionThingAlreayExist;
import com.solitardj9.userProfile.application.core.thingManager.model.exception.ExceptionThingBadRequest;
import com.solitardj9.userProfile.application.core.thingManager.model.exception.ExceptionThingNotFound;
import com.solitardj9.userProfile.application.core.thingManager.service.ThingManager;
import com.solitardj9.userProfile.serviceInterface.common.ResponseError;
import com.solitardj9.userProfile.serviceInterface.core.thingManagerInterface.model.common.AttributePayload;
import com.solitardj9.userProfile.serviceInterface.core.thingManagerInterface.model.exception.ExceptionThingControllerBadRequest;
import com.solitardj9.userProfile.serviceInterface.core.thingManagerInterface.model.request.RequestCreateThing;
import com.solitardj9.userProfile.serviceInterface.core.thingManagerInterface.model.request.RequestUpdateThing;
import com.solitardj9.userProfile.serviceInterface.core.thingManagerInterface.model.response.ResponseCreateThing;
import com.solitardj9.userProfile.serviceInterface.core.thingManagerInterface.model.response.ResponseDescribeThing;
import com.solitardj9.userProfile.util.reqExpUtil.RegExpUtil;

@RestController
@RequestMapping(value="/thingManager/")
public class ThingManagerController {
	
	private static final Logger logger = LoggerFactory.getLogger(ThingManagerController.class);
	
	@Autowired
	ThingManager thingManager;
	
	@Value("${serviceInterface.core.thingManager.regExp.thing}")
	private String regExpThing;				// '[a-zA-Z0-9:_-]{1,128}+'
	
	@Value("${serviceInterface.core.thingManager.regExp.attributesKey}")
	private String regExpAttributeKey;		// '[a-zA-Z0-9_.,@/:#!-]+{1,128}'
	
	@Value("${serviceInterface.core.thingManager.regExp.attributesValue}")
	private String regExpAttributeValue;	// '[a-zA-Z0-9_.,@/:#-]*{1,800}'
	
	private ObjectMapper om = new ObjectMapper();
	
	/**
	 * @param thingName
	 * @param requestBody
	 *	{
	 *		"attributePayload": {
	 *			"attributes": {
	 *				"string" : object
	 *				...
	 *			},
	 *			"merge": boolean
	 *		},
	 *		"thingTypeName": "string"
	 *	}
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/things/{thingName}")
	public ResponseEntity createThing(@PathVariable("thingName") String thingName,
										  @RequestBody(required=false) String requestBody) {
		//
		RequestCreateThing request = null;
		if (requestBody != null && !requestBody.isEmpty()) {
			//
			try {
				request = om.readValue(requestBody, RequestCreateThing.class);
			} catch (JsonProcessingException e) {
				logger.error("[ThingManagerController].createThing : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
			}
			
			try {
				checkRegExpForRequestCreateThing(thingName, request);
			} catch (ExceptionThingControllerBadRequest e) {
				logger.error("[ThingManagerController].createThing : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
			}
		}
		
		try {
			String attributes = om.writeValueAsString(request.getAttributePayload().getAttributes());
			Thing thing = thingManager.createThing(thingName, request.getThingTypeName(), attributes, request.getAttributePayload().getMerge());
			return new ResponseEntity(new ResponseCreateThing(thing.getThingId(), thing.getThingName()), HttpStatus.OK);
		} catch (ExceptionThingAlreayExist e) {
			logger.error("[ThingManagerController].createThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[ThingManagerController].createThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/things/{thingName}")
	public ResponseEntity describeThing(@PathVariable("thingName") String thingName) {
		//
		try {
			checkRegExpForRequestDescribeThing(thingName);
		} catch (ExceptionThingControllerBadRequest e) {
			logger.error("[ThingManagerController].describeThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		}
		
		try {
			Thing thing = thingManager.getThingByThingName(thingName);
			try {
				AttributePayload attributePayload = new AttributePayload(om.readValue(thing.getAttributes(), Map.class), null);
				ResponseDescribeThing response = new ResponseDescribeThing(thing.getThingId(), thing.getThingName(), attributePayload, thing.getThingTypeName());
				return new ResponseEntity(response, HttpStatus.OK);
			} catch (JsonProcessingException e) {
				logger.error("[ThingManagerController].describeThing : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (ExceptionThingNotFound e) {
			logger.error("[ThingManagerController].describeThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (ExceptionThingBadRequest e) {
			logger.error("[ThingManagerController].describeThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[GroupManagerController].createGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * @param thingName
	 * @param reqeustBody
	 *	{
	 *		"attributePayload": {
	 *			"attributes": {
	 *				"string" : object
	 *				...
	 *			},
	 *			"merge": boolean
	 *		},
	 *		"removeThingType": boolean,
	 *		"thingTypeName": "string"
	 *	}
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PatchMapping("/things/{thingName}")
	public ResponseEntity updateThing(@PathVariable("thingName") String thingName,
									  @RequestBody(required=true) String requestBody) {
		//
		RequestUpdateThing request = null;
		if (requestBody != null && !requestBody.isEmpty()) {
			//
			try {
				request = om.readValue(requestBody, RequestUpdateThing.class);
			} catch (JsonProcessingException e) {
				logger.error("[ThingManagerController].updateThing : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
			}
			
			try {
				checkRegExpForRequestUpdateThing(thingName, request);
			} catch (ExceptionThingControllerBadRequest e) {
				logger.error("[ThingManagerController].updateThing : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
			}
		}
		
		try {
			String attributes = om.writeValueAsString(request.getAttributePayload().getAttributes());
			Boolean ret = thingManager.updateThing(thingName, request.getThingTypeName(), request.getRemoveThingType(), attributes, request.getAttributePayload().getMerge());
			if (ret)
				return new ResponseEntity(HttpStatus.OK);
			else
				return new ResponseEntity(new ResponseError("fail to update thing.", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ExceptionThingNotFound e) {
			logger.error("[ThingManagerController].updateThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[ThingManagerController].updateThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping("/things/{thingName}")
	public ResponseEntity deleteThing(@PathVariable("thingName") String thingName) {
		//
		try {
			checkRegExpForRequestDescribeThing(thingName);
		} catch (ExceptionThingControllerBadRequest e) {
			logger.error("[ThingManagerController].deleteThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		}
		
		try {
			Boolean ret = thingManager.deleteThing(thingName);
			if (ret)
				return new ResponseEntity(HttpStatus.OK);
			else
				return new ResponseEntity(new ResponseError("fail to delete thing.", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ExceptionThingNotFound e) {
			logger.error("[ThingManagerController].deleteThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[ThingManagerController].deleteThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/things")
	public ResponseEntity selectThings(@RequestParam(value = "attributeName", required=false) String attributeName,
									   @RequestParam(value = "attributeValue", required=false) String attributeValue,
									   @RequestParam(value = "thingTypeName", required=false) String thingTypeName) {
		//
		try {
			List<Thing> things = thingManager.getThings(attributeName, attributeValue, thingTypeName);
			return new ResponseEntity(things , HttpStatus.OK);
		} catch (Exception e) {
			logger.error("[ThingManagerController].selectThings : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private Boolean checkRegExpForRequestCreateThing(String thingName, RequestCreateThing request) throws ExceptionThingControllerBadRequest {
		//
		if (!RegExpUtil.isValidExpression(thingName, regExpThing, false))
			throw new ExceptionThingControllerBadRequest();
		
		if (!RegExpUtil.isValidExpression(request.getThingTypeName(), regExpThing, true))
			throw new ExceptionThingControllerBadRequest();
		
		if (request.getAttributePayload() == null)
			throw new ExceptionThingControllerBadRequest();
		
		return true;
	}
	
	private Boolean checkRegExpForRequestUpdateThing(String thingName, RequestUpdateThing request) throws ExceptionThingControllerBadRequest {
		//
		if (!RegExpUtil.isValidExpression(thingName, regExpThing, false))
			throw new ExceptionThingControllerBadRequest();
		
		if (!RegExpUtil.isValidExpression(request.getThingTypeName(), regExpThing, true))
			throw new ExceptionThingControllerBadRequest();
		
		if (request.getAttributePayload() == null)
			throw new ExceptionThingControllerBadRequest();
		
		return true;
	}
	
	private Boolean checkRegExpForRequestDescribeThing(String thingName) throws ExceptionThingControllerBadRequest {
		//
		if (!RegExpUtil.isValidExpression(thingName, regExpThing, false))
			throw new ExceptionThingControllerBadRequest();
		
		return true;
	}
}