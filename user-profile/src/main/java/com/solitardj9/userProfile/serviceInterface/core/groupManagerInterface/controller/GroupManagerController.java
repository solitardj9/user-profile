package com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.controller;

import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solitardj9.userProfile.application.core.groupManager.service.GroupManager;
import com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.exception.ExceptionGroupControllerBadRequest;
import com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.request.group.RequestCreateGroup;
import com.solitardj9.userProfile.util.reqExpUtil.RegExpUtil;

@RestController
@RequestMapping(value="/groupManager/")
public class GroupManagerController {

	private static final Logger logger = LoggerFactory.getLogger(GroupManagerController.class);
	
	@Autowired
	GroupManager groupManager;
	
	@Value("${serviceInterface.core.thingManager.regExp.thing}")
	private String regExpGroup;				// '[a-zA-Z0-9:_-]{1,128}+'
	
	@Value("${serviceInterface.core.thingManager.regExp.attributesKey}")
	private String regExpAttributeKey;		// '[a-zA-Z0-9_.,@/:#!-]+{1,128}'
	
	@Value("${serviceInterface.core.thingManager.regExp.attributesValue}")
	private String regExpAttributeValue;	// '[a-zA-Z0-9_.,@/:#-]*{1,800}'
	
	private ObjectMapper om = new ObjectMapper();
	
	/**
	 * @param thingGroupName
	 * @param requestBody
	 *		{
	 *			
	 *			"attributePayload": {
	 *				"attributes": {
	 *					"string" : object
	 *					...
	 *				},
	 *				"merge": boolean
	 *			},
	 *			"parentGroupName": "string",
	 *		}
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/groups/{groupName}")
	public ResponseEntity createThingGroup(@PathVariable("groupName") String groupName,
												@RequestBody(required=true) String requestBody) {		
		//
//		RequestCreateThingGroup request = null;
//		if (requestBody != null && !requestBody.isEmpty()) {
//			//
//			try {
//				request = om.readValue(requestBody, RequestCreateThingGroup.class);
//			} catch (JsonProcessingException e) {
//				logger.error("[ThingGroupManagerController].createThingGroup : error = " + e);
//				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
//			}
//			
//			try {
//				checkRegExpForRequestCreateThingGroup(thingGroupName, request);
//			} catch (ExceptionThingGroupBadRequest e) {
//				logger.error("[ThingGroupManagerController].createThing : error = " + e);
//				return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
//			}
//		}
//		
//		ThingGroup thingGroup = null;
//		try {
//			thingGroup = thingGroupManager.createThingGroup(thingGroupName, request.getParentGroupName(), request.getThingGroupProperties().getAttributePayload().getAttributes(), request.getThingGroupProperties().getAttributePayload().getMerge(), request.getThingGroupProperties().getThingGroupDescription());
//		} catch (ExceptionThingGroupAlreayExist e) {
//			logger.error("[ThingGroupManagerController].createThing : error = " + e);
//			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
//		} catch (Exception e) {
//			logger.error("[ThingGroupManagerController].createThing : error = " + e);
//			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//		
//		return new ResponseEntity(new ResponseCreateThing(thingGroup.getId().toString(), thingGroup.getThingGroupName()), HttpStatus.OK);
		
		return new ResponseEntity(HttpStatus.OK);
	}
	
//	/**
//	 * @param thingGroupName
//	 * @return
//	 *		{
//	 *			"thingGroupId" : "string",
//	 *			"thingGroupName" : "string",
//	 *			"version" : number,
//	 *			"thingGroupProperties" : {
//	 *				"attributePayload" : {
//	 *					"attributes" : {
//	 *						"string" : "string"
//	 *					},
//	 *					"merge" : null
//	 *				},
//	 *				"thingGroupDescription" : "string"
//	 *			},
//	 *			"thingGroupMetadata" : {
//	 *				"parentGroupName" : "string",
//	 *				"rootToParentThingGroups" : [ "{parentGroupName}" ]
//	 *			}
//	 *		}
//	 */
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@GetMapping("/thing-groups/{thingGroupName}")
//	public ResponseEntity describeThingGroup(@PathVariable("thingGroupName") String thingGroupName) {
//		//
//		try {
//			checkRegExpForRequestThingGroup(thingGroupName);
//		} catch (ExceptionThingGroupBadRequest e) {
//			logger.error("[ThingGroupManagerController].describeThingGroup : error = " + e);
//			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
//		}
//		
//		ResponseDescribeThingGroup response = new ResponseDescribeThingGroup();
//		ThingGroup thingGroup = null;
//		ThingGroup parentThingGroup = null;
//		List<String> rootToParentGroupNames = new ArrayList<>();
//		try {
//			thingGroup = thingGroupManager.getThingGroupByThingGroupName(thingGroupName);
//			parentThingGroup = thingGroupManager.getThingGroupByThingGroupName(thingGroup.getParentGroupName());
//			rootToParentGroupNames = thingGroupManager.getNamesOfRootToGroup(parentThingGroup.getParentGroupName());
//			
//			response.setThingGroupId(thingGroup.getId().toString());
//			response.setThingGroupName(thingGroup.getThingGroupName());
//			response.setVersion(thingGroup.getVersion());
//			
//			response.setThingGroupProperties(new ThingGroupProperties(new AttributePayload(thingGroup.getAttributes(), null), thingGroup.getThingGroupDescription()));
//			
//			response.setThingGroupMetadata(new ThingGroupMetadata(parentThingGroup.getParentGroupName(), rootToParentGroupNames));
//		} catch (ExceptionThingGroupNotFound e) {
//			logger.error("[ThingGroupManagerController].describeThingGroup : error = " + e);
//			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
//		} catch (com.solitardj9.microiot.application.thing.groupManager.model.exception.ExceptionThingGroupBadRequest e) {
//			logger.error("[ThingGroupManagerController].describeThingGroup : error = " + e);
//			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
//		}
//	
//		return new ResponseEntity(response, HttpStatus.OK);
//	}
//	
//	
//	/**
//	 * @param thingGroupName
//	 * @param requestBody
//	 *		{
//	 *			"thingGroupProperties" : {
//	 *				"attributePayload" : {
//	 *					"attributes" : {
//	 *						"string" : "string"
//	 *					},
//	 *					"merge": boolean
//	 *				},
//	 *				"thingGroupDescription": "string"
//	 *			},
//	 *			"expectedVersion" : number
//	 *		}
//	 * @return
//	 *		{
//	 *			"version" : number
//	 *		}
//	 */
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@PatchMapping("/thing-groups/{thingGroupName}")
//	public ResponseEntity updateThingGroup(@PathVariable("thingName") String thingGroupName,
//										   @RequestBody(required=true) String requestBody) {
//		//
//		RequestUpdateThingGroup request = null;
//		if (requestBody != null && !requestBody.isEmpty()) {
//			//
//			try {
//				request = om.readValue(requestBody, RequestUpdateThingGroup.class);
//			} catch (JsonProcessingException e) {
//				logger.error("[ThingGroupManagerController].updateThingGroup : error = " + e);
//				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
//			}
//			
//			try {
//				checkRegExpForRequestUpdateThingGroup(thingGroupName, request);
//			} catch (ExceptionThingGroupBadRequest e) {
//				logger.error("[ThingGroupManagerController].updateThingGroup : error = " + e);
//				return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
//			}
//		}
//
//		ResponseUpdateThingGroup response = new ResponseUpdateThingGroup();
//		try {
//			Integer version = thingGroupManager.updateThingGroup(thingGroupName, request.getThingGroupProperties().getAttributePayload().getAttributes(), request.getThingGroupProperties().getAttributePayload().getMerge(), request.getExpectedVersion());
//			response.setVersion(version);
//		} catch (ExceptionThingGroupNotFound e) {
//			logger.error("[ThingGroupManagerController].updateThingGroup : error = " + e);
//			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
//		} catch (ExceptionThingGroupVersionMismatch e) {
//			logger.error("[ThingGroupManagerController].updateThingGroup : error = " + e);
//			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
//		} catch (Exception e) {
//			logger.error("[ThingGroupManagerController].updateThingGroup : error = " + e);
//			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//		
//		return new ResponseEntity(response, HttpStatus.OK);
//	}
//	
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@DeleteMapping("/thing-groups/{thingGroupName}")
//	public ResponseEntity deleteThingGroup(@PathVariable("thingGroupName") String thingGroupName,
//										   @RequestParam(value="expectedVersion", required=false) Integer expectedVersion) {
//		//
//		try {
//			checkRegExpForRequestThingGroup(thingGroupName);
//		} catch (ExceptionThingGroupBadRequest e) {
//			logger.error("[ThingGroupManagerController].deleteThingGroup : error = " + e);
//			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
//		}
//		
//		try {
//			thingGroupManager.deleteThingGroup(thingGroupName, expectedVersion);
//		} catch (ExceptionThingGroupNotFound e) {
//			logger.error("[ThingGroupManagerController].deleteThingGroup : error = " + e);
//			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
//		} catch (ExceptionThingGroupVersionMismatch e) {
//			logger.error("[ThingGroupManagerController].deleteThingGroup : error = " + e);
//			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
//		} catch (Exception e) {
//			logger.error("[ThingGroupManagerController].deleteThingGroup : error = " + e);
//			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//		
//		return new ResponseEntity(HttpStatus.OK);
//	}	
//
//	/**
//	 * @param parentGroup
//	 * @param recursive
//	 * @return
//	 *		{
//	 *			"thingGroups": [ "{thingGroupName}" ]
//	 *		}
//	 */
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@GetMapping("/thing-groups")
//	public ResponseEntity listThingGroups(
//			   @RequestParam(value = "parentGroup", required=false) String parentGroupName,
//			   @RequestParam(value = "recursive", required=false) Boolean recursive) {
//		//
//		try {
//			checkRegExpForRequestThingGroup(parentGroupName);
//		} catch (ExceptionThingGroupBadRequest e) {
//			logger.error("[ThingGroupManagerController].listThingGroups : error = " + e);
//			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
//		}
//
//		ResponseListThingGroups response = new ResponseListThingGroups();
//		try {
//			List<String> thingGroupNames = thingGroupManager.getNamesByParentGroup(parentGroupName, recursive);
//			response.setThingGroups(thingGroupNames);
//		} catch (Exception e) {
//			logger.error("[ThingGroupManagerController].listThingGroups : error = " + e);
//			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//		
//		return new ResponseEntity(response, HttpStatus.OK);
//	}
//	
//	/**
//	 * @param requestBody
//	 *		{
//	 *			"thingGroupName": "string",
//	 *			"thingName": "string"
//	 *		}
//	 * @return
//	 */
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@PutMapping("/thing-groups/thing")
//	public ResponseEntity addThingToThingGroup(@RequestBody(required=true) String requestBody) {
//		//
//		RequestAddThingToThingGroup request = null;
//		if (requestBody != null && !requestBody.isEmpty()) {
//			//
//			try {
//				request = om.readValue(requestBody, RequestAddThingToThingGroup.class);
//			} catch (JsonProcessingException e) {
//				logger.error("[ThingGroupManagerController].addThingToThingGroup : error = " + e);
//				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
//			}
//			
//			try {
//				checkRegExpForRequestAddThingToThingGroup(request);
//			} catch (ExceptionThingGroupBadRequest e) {
//				logger.error("[ThingGroupManagerController].addThingToThingGroup : error = " + e);
//				return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
//			}
//		}
//		
//		try {
//			thingGroupManager.addThingToThingGroup(request.getThingGroupName(), request.getThingName());
//		} catch (com.solitardj9.microiot.application.thing.groupManager.model.exception.ExceptionThingGroupBadRequest e) {
//			logger.error("[ThingGroupManagerController].addThingToThingGroup : error = " + e);
//			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
//		} catch (ExceptionThingGroupNotFound e) {
//			logger.error("[ThingGroupManagerController].addThingToThingGroup : error = " + e);
//			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
//		} catch (Exception e) {
//			logger.error("[ThingGroupManagerController].addThingToThingGroup : error = " + e);
//			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//
//		return new ResponseEntity(HttpStatus.OK);
//	}
//	
//	/**
//	 * @param requestBody
//	 *		{
//	 *			"thingGroupsToAdd": [ "string" ],
//	 *			"thingGroupsToRemove": [ "string" ],
//	 *			"thingName": "string"
//	 *		}
//	 * @return
//	 */
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@PatchMapping("/thing-groups/thing")
//	public ResponseEntity updateThingGroupsForThing(@RequestBody(required=true) String requestBody) {
//		//
//		RequestUpdateThingGroupForThing request = null;
//		if (requestBody != null && !requestBody.isEmpty()) {
//			//
//			try {
//				request = om.readValue(requestBody, RequestUpdateThingGroupForThing.class);
//			} catch (JsonProcessingException e) {
//				logger.error("[ThingGroupManagerController].updateThingGroupForThing : error = " + e);
//				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
//			}
//			
//			try {
//				checkRegExpForRequestUpdateThingGroupForThing(request);
//			} catch (ExceptionThingGroupBadRequest e) {
//				logger.error("[ThingGroupManagerController].updateThingGroupForThing : error = " + e);
//				return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
//			}
//		}
//		
//		try {
//			thingGroupManager.updateThingGroupsForThing(request.getThingGroupsToAdd(), request.getThingGroupsToRemove(), request.getThingName());
//		} catch (com.solitardj9.microiot.application.thing.groupManager.model.exception.ExceptionThingGroupBadRequest e) {
//			logger.error("[ThingGroupManagerController].updateThingGroupForThing : error = " + e);
//			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
//		} catch (Exception e) {
//			logger.error("[ThingGroupManagerController].updateThingGroupForThing : error = " + e);
//			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//
//		return new ResponseEntity(HttpStatus.OK);
//	}
//
//	/**
//	 * @param body
//	 *		{
//	 *			"thingGroupName": "string",
//	 *			"thingName": "string"
//	 *		}
//	 * @return
//	 */
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@DeleteMapping("/thing-groups/thing")
//	public ResponseEntity removeThingFromThingGroup(@RequestBody(required=false) String requestBody) {
//		//
//		RequestRemoveThingFromThingGroup request = null;
//		if (requestBody != null && !requestBody.isEmpty()) {
//			//
//			try {
//				request = om.readValue(requestBody, RequestRemoveThingFromThingGroup.class);
//			} catch (JsonProcessingException e) {
//				logger.error("[ThingGroupManagerController].removeThingFromThingGroup : error = " + e);
//				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
//			}
//			
//			try {
//				checkRegExpForRequestRemoveThingFromThingGroup(request);
//			} catch (ExceptionThingGroupBadRequest e) {
//				logger.error("[ThingGroupManagerController].removeThingFromThingGroup : error = " + e);
//				return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
//			}
//		}
//		
//		try {
//			thingGroupManager.removeThingFromThingGroup(request.getThingGroupName(), request.getThingName());
//		} catch (com.solitardj9.microiot.application.thing.groupManager.model.exception.ExceptionThingGroupBadRequest e) {
//			logger.error("[ThingGroupManagerController].removeThingFromThingGroup : error = " + e);
//			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
//		} catch (Exception e) {
//			logger.error("[ThingGroupManagerController].removeThingFromThingGroup : error = " + e);
//			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//			
//		return new ResponseEntity(HttpStatus.OK);
//	}
//	
//	/**
//	 * 
//	 * @param thingGroupName
//	 * @param recursive
//	 * @return
//	 * {
//    		"things": [ "string" ]
//		}
//	 */
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@GetMapping("/thing-groups/{thingGroupName}/things")
//	public ResponseEntity listThingsInThingGroup(@PathVariable("thingGroupName") String thingGroupName,
//												 @RequestParam(value="recursive", required=true) Boolean recursive) {
//		//
//		try {
//			checkRegExpForRequestThingGroup(thingGroupName);
//		} catch (ExceptionThingGroupBadRequest e) {
//			logger.error("[ThingGroupManagerController].listThingsInThingGroup : error = " + e);
//			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
//		}
//				
//		RespoonseListThingsInThingGroup response = new RespoonseListThingsInThingGroup();
//		try {
//			List<String> things = thingGroupManager.getNamesOfThingsInThingGroup(thingGroupName, recursive);
//			response.setThings(things);
//		} catch(ExceptionThingGroupNotFound e) {
//			logger.error("[ThingGroupManagerController].listThingsInThingGroup : error = " + e);
//			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
//		} catch(Exception e) {
//			logger.error("[ThingGroupManagerController].listThingsInThingGroup : error = " + e);
//			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);			
//		} 
//				
//		return new ResponseEntity(response, HttpStatus.OK);
//	}
//	
//	/**
//	 * @param thingName
//	 * @return
//	 *		{
//	 *			"thingGroups" : [ "string" ]
//	 *		}
//	 */
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	@GetMapping("/things/{thingName}/thing-groups")
//	public ResponseEntity listThingGroupsForThing(@PathVariable("thingName") String thingName) {
//		//
//		try {
//			checkRegExpForRequestDescribeThing(thingName);
//		} catch (ExceptionThingBadRequest e) {
//			logger.error("[ThingGroupManagerController].listThingGroupsForThing : error = " + e);
//			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
//		}
//		
//		RespoonseListThingGroupsForThing response = new RespoonseListThingGroupsForThing();
//		try {
//			List<String> thingGroups = thingGroupManager.getNamesOfThingGroupsForThing(thingName);
//			response.setThingGroups(thingGroups);
//		} catch (ExceptionThingNotFound e) {
//			logger.error("[ThingGroupManagerController].listThingGroupsForThing : error = " + e);
//			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
//		} catch (Exception e) {
//			logger.error("[ThingGroupManagerController].listThingGroupsForThing : error = " + e);
//			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//
//		return new ResponseEntity(response, HttpStatus.OK);
//	}
	
	private Boolean checkRegExpForRequestCreateThingGroup(String thingGroupName, RequestCreateGroup request) throws ExceptionGroupControllerBadRequest {
		//
		if (!RegExpUtil.isValidExpression(thingGroupName, regExpGroup, false))
			throw new ExceptionGroupControllerBadRequest();
		
		if (!RegExpUtil.isValidExpression(request.getParentGroupName(), regExpGroup, true))
			throw new ExceptionGroupControllerBadRequest();
		
		return true;
	}
	
//	private Boolean checkRegExpForRequestUpdateThingGroup(String thingGroupName, RequestUpdateThingGroup request) throws ExceptionGroupControllerBadRequest {
//		//
//		if (!RegExpUtil.isValidExpression(thingGroupName, regExpGroup, false))
//			throw new ExceptionGroupControllerBadRequest();
//		
//		if (request.getThingGroupProperties() == null)
//			throw new ExceptionGroupControllerBadRequest();
//		
//		if (request.getThingGroupProperties().getAttributePayload() == null)
//			throw new ExceptionGroupControllerBadRequest();
//		
//		for (Entry<String, String> entry : request.getThingGroupProperties().getAttributePayload().getAttributes().entrySet()) {
//			//
//			String key = entry.getKey();
//			String value = entry.getValue();	
//			
//			if (!RegExpUtil.isValidExpression(key, regExpAttributeKey, false)) {
//				throw new ExceptionGroupControllerBadRequest();
//			}
//
//			if (!RegExpUtil.isValidExpression(value, regExpAttributeValue, true)) {
//				throw new ExceptionGroupControllerBadRequest();
//			}
//		}
//		
//		if (request.getExpectedVersion() == null)
//			throw new ExceptionGroupControllerBadRequest();
//		
//		return true;
//	}
//	
//	private Boolean checkRegExpForRequestThingGroup(String thingGroupName) throws ExceptionGroupControllerBadRequest {
//		//
//		if (!RegExpUtil.isValidExpression(thingGroupName, regExpGroup, false))
//			throw new ExceptionGroupControllerBadRequest();
//		
//		return true;
//	}
//	
//	private Boolean checkRegExpForRequestAddThingToThingGroup(RequestAddThingToThingGroup request) throws ExceptionGroupControllerBadRequest {
//		//
//		if (!RegExpUtil.isValidExpression(request.getThingGroupName(), regExpGroup, false))
//			throw new ExceptionGroupControllerBadRequest();
//		
//		if (!RegExpUtil.isValidExpression(request.getThingName(), regExpGroup, false))
//			throw new ExceptionGroupControllerBadRequest();
//		
//		return true;
//	}
//	
//	private Boolean checkRegExpForRequestUpdateThingGroupForThing(RequestUpdateThingGroupForThing request) throws ExceptionGroupControllerBadRequest {
//		//
//		List<String> thingGroupsToAdd = request.getThingGroupsToAdd();
//		for (String iter : thingGroupsToAdd) {
//			if (!RegExpUtil.isValidExpression(iter, regExpGroup, false))
//				throw new ExceptionGroupControllerBadRequest();
//		}
//		
//		List<String> thingGroupsToRemove = request.getThingGroupsToRemove();
//		for (String iter : thingGroupsToRemove) {
//			if (!RegExpUtil.isValidExpression(iter, regExpGroup, false))
//				throw new ExceptionGroupControllerBadRequest();
//		}
//		
//		if (!RegExpUtil.isValidExpression(request.getThingName(), regExpGroup, false))
//			throw new ExceptionGroupControllerBadRequest();
//		
//		return true;
//	}
//	
//	private Boolean checkRegExpForRequestRemoveThingFromThingGroup(RequestRemoveThingFromThingGroup request) throws ExceptionGroupControllerBadRequest {
//		//
//		if (!RegExpUtil.isValidExpression(request.getThingGroupName(), regExpGroup, false))
//			throw new ExceptionGroupControllerBadRequest();
//		
//		if (!RegExpUtil.isValidExpression(request.getThingName(), regExpGroup, false))
//			throw new ExceptionGroupControllerBadRequest();
//		
//		return true;
//	}
//	
//	private Boolean checkRegExpForRequestDescribeThing(String thingName) throws ExceptionGroupControllerBadRequest {
//		//
//		if (!RegExpUtil.isValidExpression(thingName, regExpGroup, false))
//			throw new ExceptionGroupControllerBadRequest();
//		
//		return true;
//	}
}