package com.solitardj9.userProfile.serviceInterface.userManagerInterface.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solitardj9.userProfile.application.userManager.model.User;
import com.solitardj9.userProfile.application.userManager.model.exception.ExceptionUserAlreayExist;
import com.solitardj9.userProfile.application.userManager.model.exception.ExceptionUserBadRequest;
import com.solitardj9.userProfile.application.userManager.model.exception.ExceptionUserManagerFailure;
import com.solitardj9.userProfile.application.userManager.model.exception.ExceptionUserNotFound;
import com.solitardj9.userProfile.application.userManager.service.UserManager;
import com.solitardj9.userProfile.serviceInterface.common.ResponseError;
import com.solitardj9.userProfile.serviceInterface.userManagerInterface.model.common.AttributePayload;
import com.solitardj9.userProfile.serviceInterface.userManagerInterface.model.exception.ExceptionUserControllerBadRequest;
import com.solitardj9.userProfile.serviceInterface.userManagerInterface.model.request.RequestCreateUser;
import com.solitardj9.userProfile.serviceInterface.userManagerInterface.model.request.RequestUpdateUser;
import com.solitardj9.userProfile.serviceInterface.userManagerInterface.model.response.ResponseDescribeUser;
import com.solitardj9.userProfile.util.jsonUtil.JsonUtil;
import com.solitardj9.userProfile.util.reqExpUtil.RegExpUtil;

/**
 * User
 *	{
 *		"userId" : "thingId",
 *		"userName" : "userName == Id",
 *		"attributes": {
 *			"nickName" : "nickName",
 *			"serviceInfo" : [
 *				{
 *					"category" : "weather",
 *					"service" : "accuWeather",
 *					"authentication" : {
 *						"type" : "apiKey",
 *						"apiKey" : "{apiKey}"
 *					}
 *				},
 * 				{
 *					"category" : "music",
 *					"service" : "genie",
 *					"authentication" : {
 *						"type" : "oauth",
 *						"accessToken" : "{accessToken}"
 *					}
 *				}
 *			]
 *		}
 *	}
 */
// can add, delete user (ok)
//	- user is some type of thing (ok)
// can update(or merge) attributes (ok)
// create friend group when add user (ok)
//can add user to another user's friend group (ok)
//can remove user from another user's friend group (ok)
// delete friend group when delete user (ok)
//	- automatically remove users(who are friends) in friend group (ok)

@RestController
@RequestMapping(value="/userManager/")
public class UserManagerController {
	//
	private static final Logger logger = LoggerFactory.getLogger(UserManagerController.class);
	
	@Autowired
	UserManager userManager;
	
	@Value("${serviceInterface.core.thingManager.regExp.thing}")
	private String regExpThing;				// '[a-zA-Z0-9:_-]{1,128}+'
	
	private ObjectMapper om = new ObjectMapper();
	
	/**
	 * @param userName
	 * @param requestBody
	 *	{
	 *		"attributePayload": {
	 *			"attributes": {
	 *				"string" : object
	 *				...
	 *			},
	 *			"merge": boolean
	 *		}
	 *	}
	 *
	 *	#example
	 *	{
	 *		"attributePayload": {
	 *			"attributes": null,
	 *			"merge": null
	 *		}
	 *	}
	 * @return
	 *	{
	 *		"userId" : "thingId",
	 *		"userName" : "userName",
	 *		"attributePayload": {
	 *			"attributes": {
	 *				"string" : object
	 *				...
	 *			}
	 *		}
	 *	}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/users/{userName}")
	public ResponseEntity createUser(@PathVariable("userName") String userName,
									 @RequestBody(required=false) String requestBody) {
		//
		RequestCreateUser request = null;
		if (requestBody != null && !requestBody.isEmpty()) {
			//
			try {
				request = om.readValue(requestBody, RequestCreateUser.class);
			} catch (JsonProcessingException e) {
				logger.error("[UserManagerController].createUser : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
			}
			
			try {
				checkRegExpForRequestCreateUser(userName, request);
			} catch (ExceptionUserControllerBadRequest e) {
				logger.error("[UserManagerController].createUser : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
			}
		}
		
		try {
			String attributes = om.writeValueAsString(request.getAttributePayload().getAttributes());
			User user = userManager.createUser(userName, attributes, request.getAttributePayload().getMerge());
			
			if (user == null)
				return new ResponseEntity(new ResponseError("FailToCreateUserFromUserManager", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
			
			try {
				AttributePayload attributePayload = new AttributePayload(om.readValue(user.getAttributes(), Map.class), null);
				ResponseDescribeUser responseDescribeUser = new ResponseDescribeUser(user.getUserId(), user.getUserName(), attributePayload);
				return new ResponseEntity(responseDescribeUser, HttpStatus.OK);
			} catch (JsonProcessingException e) {
				logger.error("[UserManagerController].createUser : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (ExceptionUserBadRequest | ExceptionUserAlreayExist | ExceptionUserManagerFailure e) {
			logger.error("[UserManagerController].createUser : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[UserManagerController].createUser : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * @param userName
	 * @param reqeustBody
	 *	{
	 *		"attributePayload": {
	 *			"attributes": {
	 *				"string" : object
	 *				...
	 *			},
	 *			"merge": boolean
	 *		}
	 *	}
	 *
	 *	#example
	 *	{
	 *	  "attributePayload" : {
	 *	    "attributes" : {
	 *	      "nickName" : "nickName",
	 *	      "serviceInfo" : [
	 *	        {
	 *	          "category" : "weather",
	 *	          "service" : "accuWeather",
	 *	          "authentication" : {
	 *	            "type" : "apiKey",
	 *	            "apiKey" : "{apiKey}"
	 *	          }
	 *	        },
	 *	        {
	 *	          "category" : "music",
	 *	          "service" : "genie",
	 *	          "authentication" : {
	 *	            "type" : "oauth",
	 *	            "accessToken" : "{accessToken}"
	 *	          }
	 *	        }
	 *	      ]
	 *	    },
	 *	    "merge": true
	 *	  }
	 *	}
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PatchMapping("/users/{userName}")
	public ResponseEntity updateUser(@PathVariable("userName") String userName,
									 @RequestBody(required=false) String requestBody) {
		//
		RequestUpdateUser request = null;
		if (requestBody != null && !requestBody.isEmpty()) {
			//
			try {
				request = om.readValue(requestBody, RequestUpdateUser.class);
			} catch (JsonProcessingException e) {
				logger.error("[UserManagerController].updateUser : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
			}
			
			try {
				checkRegExpForRequestUpdateUser(userName, request);
			} catch (ExceptionUserControllerBadRequest e) {
				logger.error("[UserManagerController].updateUser : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
			}
		}
		
		try {
			String attributes = om.writeValueAsString(request.getAttributePayload().getAttributes());
			User user = userManager.updateUser(userName, attributes, request.getAttributePayload().getMerge());
			
			try {
				AttributePayload attributePayload = new AttributePayload(om.readValue(user.getAttributes(), Map.class), null);
				ResponseDescribeUser responseDescribeUser = new ResponseDescribeUser(user.getUserId(), user.getUserName(), attributePayload);
				return new ResponseEntity(responseDescribeUser, HttpStatus.OK);
			} catch (JsonProcessingException e) {
				logger.error("[UserManagerController].updateUser : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (ExceptionUserBadRequest | ExceptionUserNotFound | ExceptionUserManagerFailure e) {
			logger.error("[UserManagerController].updateUser : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[UserManagerController].updateUser : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/users/{userName}")
	public ResponseEntity describeUser(@PathVariable("userName") String userName) {
		//
		try {
			checkRegExpForRequestDescribeUser(userName);
		} catch (ExceptionUserControllerBadRequest e) {
			logger.error("[UserManagerController].describeUser : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		}
		
		try {
			User user = userManager.getUser(userName);
			try {
				AttributePayload attributePayload = new AttributePayload(om.readValue(user.getAttributes(), Map.class), null);
				ResponseDescribeUser responseDescribeUser = new ResponseDescribeUser(user.getUserId(), user.getUserName(), attributePayload);
				return new ResponseEntity(responseDescribeUser, HttpStatus.OK);
			} catch (JsonProcessingException e) {
				logger.error("[UserManagerController].describeUser : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (ExceptionUserNotFound | ExceptionUserBadRequest e) {
			logger.error("[UserManagerController].describeUser : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[UserManagerController].describeUser : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/users/{userName}/attributes/serviceInfo")
	public ResponseEntity getServiceInfoOfUser(@PathVariable("userName") String userName) {
		//
		try {
			checkRegExpForRequestDescribeUser(userName);
		} catch (ExceptionUserControllerBadRequest e) {
			logger.error("[UserManagerController].getServiceInfoOfUser : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		}
		
		try {
			User user = userManager.getUser(userName);
			try {
				Object serviceInfo = getValueOfJsonString(user.getAttributes(), "$.serviceInfo");
				return new ResponseEntity(serviceInfo, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("[UserManagerController].getServiceInfoOfUser : error = " + e);
				return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (ExceptionUserNotFound | ExceptionUserBadRequest e) {
			logger.error("[UserManagerController].getServiceInfoOfUser : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[UserManagerController].getServiceInfoOfUser : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping("/users/{userName}")
	public ResponseEntity deleteUser(@PathVariable("userName") String userName) {
		//
		try {
			checkRegExpForRequestDescribeUser(userName);
		} catch (ExceptionUserControllerBadRequest e) {
			logger.error("[UserManagerController].deleteUser : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		}
		
		try {
			Boolean ret = userManager.deleteUser(userName);
			if (ret)
				return new ResponseEntity(HttpStatus.OK);
			else
				return new ResponseEntity(new ResponseError("FailToDeleteUserFromUserManager.", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ExceptionUserBadRequest | ExceptionUserNotFound | ExceptionUserManagerFailure e) {
			logger.error("[UserManagerController].deleteUser : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[UserManagerController].deleteUser : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/users/{userName}/friend/{friendName}")
	public ResponseEntity addFriend(@PathVariable("userName") String userName,
									@PathVariable("friendName") String friendName) {
		// 
		try {
			checkRegExpForRequestDescribeUser(userName);
			checkRegExpForRequestDescribeUser(friendName);
		} catch (ExceptionUserControllerBadRequest e) {
			logger.error("[UserManagerController].addFriend : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		}
		
		try {
			Boolean ret = userManager.addFriend(userName, friendName);
			if (ret)
				return new ResponseEntity(HttpStatus.OK);
			else
				return new ResponseEntity(new ResponseError("FailToAddFriendFromUserManager.", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ExceptionUserBadRequest | ExceptionUserNotFound | ExceptionUserManagerFailure e) {
			logger.error("[UserManagerController].addFriend : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[UserManagerController].addFriend : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping("/users/{userName}/friend/{friendName}")
	public ResponseEntity removeFriend(@PathVariable("userName") String userName,
									   @PathVariable("friendName") String friendName) {
		// 
		try {
			checkRegExpForRequestDescribeUser(userName);
			checkRegExpForRequestDescribeUser(friendName);
		} catch (ExceptionUserControllerBadRequest e) {
			logger.error("[UserManagerController].removeFriend : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		}
		
		try {
			Boolean ret = userManager.removeFriend(userName, friendName);
			if (ret)
				return new ResponseEntity(HttpStatus.OK);
			else
				return new ResponseEntity(new ResponseError("FailToRemoveFriendFromUserManager.", HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ExceptionUserBadRequest | ExceptionUserNotFound | ExceptionUserManagerFailure e) {
			logger.error("[UserManagerController].removeFriend : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), e.getErrCode()), e.getHttpStatus());
		} catch (Exception e) {
			logger.error("[UserManagerController].removeFriend : error = " + e);
			return new ResponseEntity(new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private Boolean checkRegExpForRequestCreateUser(String userName, RequestCreateUser request) throws ExceptionUserControllerBadRequest {
		//
		if (!RegExpUtil.isValidExpression(userName, regExpThing, false))
			throw new ExceptionUserControllerBadRequest();
		
		if (request.getAttributePayload() == null)
			throw new ExceptionUserControllerBadRequest();
		
		return true;
	}
	
	private Boolean checkRegExpForRequestUpdateUser(String userName, RequestUpdateUser request) throws ExceptionUserControllerBadRequest {
		//
		if (!RegExpUtil.isValidExpression(userName, regExpThing, false))
			throw new ExceptionUserControllerBadRequest();
		
		if (request.getAttributePayload() == null)
			throw new ExceptionUserControllerBadRequest();
		
		return true;
	}
	
	private Boolean checkRegExpForRequestDescribeUser(String userName) throws ExceptionUserControllerBadRequest {
		//
		if (!RegExpUtil.isValidExpression(userName, regExpThing, false))
			throw new ExceptionUserControllerBadRequest();
		
		return true;
	}
	
	private Object getValueOfJsonString(String attributes, String keyPath) {
		//
		try {
			return JsonUtil.readValue(attributes, keyPath);
		} catch (Exception e) {
			return new ArrayList<String>();
		}
	}
}