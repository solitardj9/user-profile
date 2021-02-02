package com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solitardj9.userProfile.application.core.groupManager.model.Group;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupAlreayExist;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupBadRequest;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupManagerFailure;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupNotFound;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupUnavailableForDeleteNonEmpty;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupUnavailableForDeleteNonLeaf;
import com.solitardj9.userProfile.application.core.groupManager.service.GroupManager;
import com.solitardj9.userProfile.serviceInterface.common.ResponseError;
import com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.common.AttributePayload;
import com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.common.GroupMetadata;
import com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.exception.ExceptionGroupControllerBadRequest;
import com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.request.group.RequestCreateGroup;
import com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.request.group.RequestUpdateGroup;
import com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.request.thing.RequestAddThingToGroup;
import com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.request.thing.RequestRemoveThingFromGroup;
import com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.request.thing.RequestUpdateGroupsOfThing;
import com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.response.group.ResponseCreateGroup;
import com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.response.group.ResponseDescribeGroup;
import com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.response.group.ResponseUpdateGroup;
import com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.response.thing.RespoonseListGroupsOfThing;
import com.solitardj9.userProfile.serviceInterface.core.groupManagerInterface.model.response.thing.RespoonseListThingsInGroup;
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
	 * @param groupName
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
	 *			"groupTypeName": "string",
	 *			"parentGroupName": "string",
	 *		}
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/groups/{groupName}")
	public ResponseEntity createGroup(@PathVariable("groupName") String groupName,
									  @RequestBody(required=true) String requestBody) {		
		//
		RequestCreateGroup request = null;
		if (requestBody != null && !requestBody.isEmpty()) {
			//
			try {
				request = om.readValue(requestBody, RequestCreateGroup.class);
			} catch (JsonProcessingException e) {
				logger.error("[GroupManagerController].createGroup : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
			}
			
			try {
				checkRegExpForRequestCreateGroup(groupName, request);
			} catch (ExceptionGroupControllerBadRequest e) {
				logger.error("[GroupManagerController].createGroup : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
			}
		}
		
		try {
			String attributes = om.writeValueAsString(request.getAttributePayload().getAttributes());
			Group group = new Group(null, groupName, attributes, request.getGroupTypeName(), request.getParentGroupName(), null);
			group = groupManager.insertGroup(group);
			return new ResponseEntity(new ResponseCreateGroup(group.getGroupId().toString(), group.getGroupName()), HttpStatus.OK);
		} catch (ExceptionGroupAlreayExist | ExceptionGroupBadRequest | ExceptionGroupManagerFailure e) {
			logger.error("[GroupManagerController].createGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[GroupManagerController].createGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * @param groupName
	 * @return
	 *		{
	 *			"groupId" : "string",
	 *			"groupName" : "string",
	 *			"attributePayload": {
	 *				"attributes": {
	 *					"string" : object
	 *					...
	 *				},
	 *			},
	 *			"groupTypeName": "string",
	 *			"groupMetadata" : {
	 *				"parentGroupName" : "string",
	 *				"rootToParentThingGroups" : [ {JSon object for group}...{} ]
	 *			}
	 *		}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/groups/{groupName}")
	public ResponseEntity describeGroup(@PathVariable("groupName") String groupName) {
		//
		try {
			checkRegExpForRequestGroup(groupName);
		} catch (ExceptionGroupControllerBadRequest e) {
			logger.error("[GroupManagerController].describeGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		}
		
		try {
			Group group = groupManager.getGroup(groupName);
			String parentGroupName = group.getParentGroupName();
			
			List<Group> rootToParentGroups = new ArrayList<>();
			if (parentGroupName != null)
				rootToParentGroups = groupManager.getGroupListFromRootToGroup(parentGroupName);
			
			try {
				AttributePayload attributePayload = new AttributePayload(om.readValue(group.getAttributes(), Map.class), null);
				
				List<String> rootToParentGroupNames = new ArrayList<>();
				for (Group iter : rootToParentGroups) {
					rootToParentGroupNames.add(iter.getGroupName());
				}
				GroupMetadata groupMetadata = new GroupMetadata(parentGroupName, rootToParentGroupNames);
				
				ResponseDescribeGroup response = new ResponseDescribeGroup(group.getGroupId().toString(), group.getGroupName(), attributePayload, groupMetadata, group.getGroupTypeName());
				return new ResponseEntity(response, HttpStatus.OK);
			} catch (JsonProcessingException e) {
				logger.error("[GroupManagerController].describeGroup : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (ExceptionGroupNotFound | ExceptionGroupBadRequest e) {
			logger.error("[GroupManagerController].describeGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[GroupManagerController].createGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * @param groupName
	 * @param requestBody
	 *		{
	 *			"attributePayload": {
	 *				"attributes": {
	 *					"string" : object
	 *					...
	 *				},
	 *			},
	 *			"removeGroupType": boolean,
	 *			"groupTypeName": "string"
	 *		}
	 * @return
	 * 		{
	 *			"attributePayload": {
	 *				"attributes": {
	 *					"string" : object
	 *					...
	 *				},
	 *			},
	 *			"groupTypeName": "string"
	 *		}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PatchMapping("/groups/{groupName}")
	public ResponseEntity updateGroup(@PathVariable("groupName") String groupName,
									  @RequestBody(required=true) String requestBody) {
		//
		RequestUpdateGroup request = null;
		if (requestBody != null && !requestBody.isEmpty()) {
			//
			try {
				request = om.readValue(requestBody, RequestUpdateGroup.class);
			} catch (JsonProcessingException e) {
				logger.error("[GroupManagerController].updateGroup : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
			}
			
			try {
				checkRegExpForRequestUpdateGroup(groupName, request);
			} catch (ExceptionGroupControllerBadRequest e) {
				logger.error("[GroupManagerController].updateGroup : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
			}
		}
		
		try {
			try {
				String attributes = om.writeValueAsString(request.getAttributePayload().getAttributes());
				Group group = groupManager.updateGroup(groupName, attributes, request.getGroupTypeName(), request.getRemoveGroupType(), request.getAttributePayload().getMerge());
				
				AttributePayload attributePayload = new AttributePayload(om.readValue(group.getAttributes(), Map.class), null);
				ResponseUpdateGroup response = new ResponseUpdateGroup(group.getGroupId().toString(), group.getGroupName(), attributePayload, group.getGroupTypeName());
				return new ResponseEntity(response, HttpStatus.OK);
			} catch (JsonProcessingException e) {
				logger.error("[GroupManagerController].updateGroup : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (ExceptionGroupNotFound | ExceptionGroupBadRequest | ExceptionGroupManagerFailure e) {
			logger.error("[GroupManagerController].updateGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[GroupManagerController].createGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping("/groups/{groupName}")
	public ResponseEntity deleteGroup(@PathVariable("groupName") String groupName) {
		//
		try {
			checkRegExpForRequestGroup(groupName);
		} catch (ExceptionGroupControllerBadRequest e) {
			logger.error("[GroupManagerController].deleteGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		}
		
		try {
			Boolean ret = groupManager.removeGroup(groupName);
			if (ret)
				return new ResponseEntity(HttpStatus.OK);
			else
				return new ResponseEntity(new ResponseError("fail to delete group.", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ExceptionGroupNotFound | ExceptionGroupBadRequest | ExceptionGroupUnavailableForDeleteNonLeaf | ExceptionGroupUnavailableForDeleteNonEmpty | ExceptionGroupManagerFailure e) {
			logger.error("[GroupManagerController].deleteGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[GroupManagerController].deleteGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	

	/**
	 * @param requestBody
	 *		{
	 *			"groupName": "string",
	 *			"thingName": "string"
	 *		}
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/thing-groups/thing")
	public ResponseEntity addThingToGroup(@RequestBody(required=true) String requestBody) {
		//
		RequestAddThingToGroup request = null;
		if (requestBody != null && !requestBody.isEmpty()) {
			//
			try {
				request = om.readValue(requestBody, RequestAddThingToGroup.class);
			} catch (JsonProcessingException e) {
				logger.error("[GroupManagerController].addThingToGroup : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
			}
			
			try {
				checkRegExpForRequestAddThingToGroup(request);
			} catch (ExceptionGroupControllerBadRequest e) {
				logger.error("[GroupManagerController].addThingToGroup : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
			}
		}
		
		try {
			Boolean ret = groupManager.addThingToGroup(request.getGroupName(), request.getThingName());
			if (ret)
				return new ResponseEntity(HttpStatus.OK);
			else
				return new ResponseEntity(new ResponseError("fail to add thing to group.", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ExceptionGroupBadRequest | ExceptionGroupNotFound | ExceptionGroupManagerFailure e) {
			logger.error("[GroupManagerController].addThingToGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[GroupManagerController].addThingToGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * @param requestBody
	 *		{
	 *			"groupNamesToAdd": [ "string" ],
	 *			"groupNamesToRemove": [ "string" ],
	 *			"thingName": "string"
	 *		}
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PatchMapping("/thing-groups/thing")
	public ResponseEntity updateGroupsOfThing(@RequestBody(required=true) String requestBody) {
		//
		RequestUpdateGroupsOfThing request = null;
		if (requestBody != null && !requestBody.isEmpty()) {
			//
			try {
				request = om.readValue(requestBody, RequestUpdateGroupsOfThing.class);
			} catch (JsonProcessingException e) {
				logger.error("[GroupManagerController].updateGroupsOfThing : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
			}
			
			try {
				checkRegExpForRequestUpdateGroupsOfThing(request);
			} catch (ExceptionGroupControllerBadRequest e) {
				logger.error("[GroupManagerController].updateGroupsOfThing : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
			}
		}
		
		try {
			Boolean ret = groupManager.updateGroupsOfThing(request.getGroupNamesToAdd(), request.getGroupNamesToRemove(), request.getThingName());
			if (ret)
				return new ResponseEntity(HttpStatus.OK);
			else
				return new ResponseEntity(new ResponseError("fail to update groups of thing.", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ExceptionGroupBadRequest e) {
			logger.error("[GroupManagerController].updateGroupsOfThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[GroupManagerController].updateGroupsOfThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * @param body
	 *		{
	 *			"groupName": "string",
	 *			"thingName": "string"
	 *		}
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping("/thing-groups/thing")
	public ResponseEntity removeThingFromGroup(@RequestBody(required=false) String requestBody) {
		//
		RequestRemoveThingFromGroup request = null;
		if (requestBody != null && !requestBody.isEmpty()) {
			//
			try {
				request = om.readValue(requestBody, RequestRemoveThingFromGroup.class);
			} catch (JsonProcessingException e) {
				logger.error("[GroupManagerController].removeThingFromGroup : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
			}
			
			try {
				checkRegExpForRequestRemoveThingFromGroup(request);
			} catch (ExceptionGroupControllerBadRequest e) {
				logger.error("[GroupManagerController].removeThingFromGroup : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
			}
		}
		
		try {
			Boolean ret = groupManager.removeThingFromGroup(request.getGroupName(), request.getThingName());
			if (ret)
				return new ResponseEntity(HttpStatus.OK);
			else
				return new ResponseEntity(new ResponseError("fail to remove thing from group.", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ExceptionGroupBadRequest e) {
			logger.error("[GroupManagerController].removeThingFromGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[GroupManagerController].removeThingFromGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 
	 * @param thingGroupName
	 * @param recursive
	 * @return
	 * {
    		"things": [ "string" ]
		}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/thing-groups/{groupName}/things")
	public ResponseEntity listThingsInGroup(@PathVariable("groupName") String groupName,
											@RequestParam(value="recursive", required=true) Boolean recursive) {
		//
		try {
			checkRegExpForRequestGroup(groupName);
		} catch (ExceptionGroupControllerBadRequest e) {
			logger.error("[GroupManagerController].listThingsInGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		}
				
		try {
			Set<String> thingNames = groupManager.getThingNamesInGroup(groupName, recursive);
			RespoonseListThingsInGroup response = new RespoonseListThingsInGroup(thingNames);
			return new ResponseEntity(response, HttpStatus.OK);
		} catch(ExceptionGroupNotFound | ExceptionGroupManagerFailure e) {
			logger.error("[GroupManagerController].listThingsInGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch(Exception e) {
			logger.error("[GroupManagerController].listThingsInGroup : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);			
		}
	}
	
	/**
	 * @param thingName
	 * @return
	 *		{
	 *			"thingGroups" : [ "string" ]
	 *		}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("/thing-groups/things/{thingName}")
	public ResponseEntity listGroupsOfThing(@PathVariable("thingName") String thingName) {
		//
		try {
			checkRegExpForRequestGroup(thingName);
		} catch (ExceptionGroupControllerBadRequest e) {
			logger.error("[GroupManagerController].listGroupsOfThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		}
		
		try {
			Set<String> groupNames = groupManager.getGroupNamesOfThing(thingName);
			RespoonseListGroupsOfThing response = new RespoonseListGroupsOfThing(groupNames);
			return new ResponseEntity(response, HttpStatus.OK);
		} catch (ExceptionGroupBadRequest | ExceptionGroupManagerFailure e) {
			logger.error("[GroupManagerController].listGroupsOfThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[GroupManagerController].listGroupsOfThing : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private Boolean checkRegExpForRequestCreateGroup(String groupName, RequestCreateGroup request) throws ExceptionGroupControllerBadRequest {
		//
		if (!RegExpUtil.isValidExpression(groupName, regExpGroup, false))
			throw new ExceptionGroupControllerBadRequest();
		
		if (request.getAttributePayload() == null)
			throw new ExceptionGroupControllerBadRequest();
		
		if (!RegExpUtil.isValidExpression(request.getGroupTypeName(), regExpGroup, true))
			throw new ExceptionGroupControllerBadRequest();
		
		return true;
	}
	
	private Boolean checkRegExpForRequestGroup(String groupName) throws ExceptionGroupControllerBadRequest {
		//
		if (!RegExpUtil.isValidExpression(groupName, regExpGroup, false))
			throw new ExceptionGroupControllerBadRequest();
		
		return true;
	}
	
	private Boolean checkRegExpForRequestUpdateGroup(String groupName, RequestUpdateGroup request) throws ExceptionGroupControllerBadRequest {
		//
		if (!RegExpUtil.isValidExpression(groupName, regExpGroup, false))
			throw new ExceptionGroupControllerBadRequest();
		
		if (request.getAttributePayload() == null)
			throw new ExceptionGroupControllerBadRequest();
		
		if (!RegExpUtil.isValidExpression(request.getGroupTypeName(), regExpGroup, true))
			throw new ExceptionGroupControllerBadRequest();
		
		return true;
	}
	
	private Boolean checkRegExpForRequestAddThingToGroup(RequestAddThingToGroup request) throws ExceptionGroupControllerBadRequest {
		//
		if (!RegExpUtil.isValidExpression(request.getGroupName(), regExpGroup, false))
			throw new ExceptionGroupControllerBadRequest();
		
		if (!RegExpUtil.isValidExpression(request.getThingName(), regExpGroup, false))
			throw new ExceptionGroupControllerBadRequest();
		
		return true;
	}
	
	private Boolean checkRegExpForRequestUpdateGroupsOfThing(RequestUpdateGroupsOfThing request) throws ExceptionGroupControllerBadRequest {
		//
		List<String> groupNamesToAdd = request.getGroupNamesToAdd();
		for (String iter : groupNamesToAdd) {
			if (!RegExpUtil.isValidExpression(iter, regExpGroup, false))
				throw new ExceptionGroupControllerBadRequest();
		}
		
		List<String> groupNamesToRemove = request.getGroupNamesToRemove();
		for (String iter : groupNamesToRemove) {
			if (!RegExpUtil.isValidExpression(iter, regExpGroup, false))
				throw new ExceptionGroupControllerBadRequest();
		}
		
		if (!RegExpUtil.isValidExpression(request.getThingName(), regExpGroup, false))
			throw new ExceptionGroupControllerBadRequest();
		
		return true;
	}
	
	private Boolean checkRegExpForRequestRemoveThingFromGroup(RequestRemoveThingFromGroup request) throws ExceptionGroupControllerBadRequest {
		//
		if (!RegExpUtil.isValidExpression(request.getGroupName(), regExpGroup, false))
			throw new ExceptionGroupControllerBadRequest();
		
		if (!RegExpUtil.isValidExpression(request.getThingName(), regExpGroup, false))
			throw new ExceptionGroupControllerBadRequest();
		
		return true;
	}

}