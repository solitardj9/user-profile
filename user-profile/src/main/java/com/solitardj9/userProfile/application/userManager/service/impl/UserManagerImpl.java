package com.solitardj9.userProfile.application.userManager.service.impl;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solitardj9.userProfile.application.core.groupManager.model.Group;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupAlreayExist;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupBadRequest;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupManagerFailure;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupNotFound;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupUnavailableForDeleteNonEmpty;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupUnavailableForDeleteNonLeaf;
import com.solitardj9.userProfile.application.core.groupManager.service.GroupManager;
import com.solitardj9.userProfile.application.core.thingManager.model.Thing;
import com.solitardj9.userProfile.application.core.thingManager.model.exception.ExceptionThingAlreayExist;
import com.solitardj9.userProfile.application.core.thingManager.model.exception.ExceptionThingBadRequest;
import com.solitardj9.userProfile.application.core.thingManager.model.exception.ExceptionThingManagerFailure;
import com.solitardj9.userProfile.application.core.thingManager.model.exception.ExceptionThingNotFound;
import com.solitardj9.userProfile.application.core.thingManager.service.ThingManager;
import com.solitardj9.userProfile.application.userManager.model.User;
import com.solitardj9.userProfile.application.userManager.model.exception.ExceptionUserAlreayExist;
import com.solitardj9.userProfile.application.userManager.model.exception.ExceptionUserBadRequest;
import com.solitardj9.userProfile.application.userManager.model.exception.ExceptionUserFriendGroupHasChildGroup;
import com.solitardj9.userProfile.application.userManager.model.exception.ExceptionUserFriendGroupIsNotEmpty;
import com.solitardj9.userProfile.application.userManager.model.exception.ExceptionUserManagerFailure;
import com.solitardj9.userProfile.application.userManager.model.exception.ExceptionUserNotFound;
import com.solitardj9.userProfile.application.userManager.service.UserManager;

@Service("userManager")
public class UserManagerImpl implements UserManager {

	private static final Logger logger = LoggerFactory.getLogger(UserManagerImpl.class);
	
	@Autowired
	ThingManager thingManager;
	
	@Autowired
	GroupManager groupManager;
	
	private Boolean isInitialized = false;
	
	private final static String USER_TYPE_NAME = "USER";
	
	private final static String GROUP_TYPE_NAME = "FRIENDS";
	private final static String GROUP_NAME_SUFFIX = "_Friend_Group";
	
	@PostConstruct
	public void init() {
		//
		isInitialized = true;
	}
	
	@Override
	public Boolean isInitialized() {
		return isInitialized;
	}
	
	@Override
	public User createUser(String userName, String attributes, Boolean merge) throws ExceptionUserBadRequest, ExceptionUserAlreayExist, ExceptionUserManagerFailure, ExceptionUserNotFound {
		//
		if (userName == null || userName.isEmpty()) {
			logger.error("[UserManager].createUser : error = user name is invalid.");
			throw new ExceptionUserBadRequest();
		}
		
		if (attributes == null || attributes.isEmpty() || attributes.equals("null"))
			attributes = "{}";
		
		try {
			// create thing which is mapped to user
			Thing thing = thingManager.createThing(userName, USER_TYPE_NAME, attributes, merge);
			
			// create group which is mapped to user
			Group group = groupManager.insertGroup((userName + GROUP_NAME_SUFFIX), attributes, GROUP_TYPE_NAME, null, null);
			if (group == null) {
				thingManager.deleteThing(userName);
				return null;
			}
			
			User user = new User(thing.getThingId(), thing.getThingName(), thing.getAttributes());
			return user;
		} catch (ExceptionThingBadRequest | ExceptionGroupBadRequest e) {
			logger.error("[UserManager].createUser : error = " + e);
			throw new ExceptionUserBadRequest();
		} catch (ExceptionThingAlreayExist | ExceptionGroupAlreayExist e) {
			logger.error("[UserManager].createUser : error = " + e);
			throw new ExceptionUserAlreayExist();
		} catch (ExceptionThingManagerFailure | ExceptionGroupManagerFailure e) {
			logger.error("[UserManager].createUser : error = " + e);
			throw new ExceptionUserManagerFailure();
		} catch (ExceptionThingNotFound e) {
			logger.error("[UserManager].createUser : error = " + e);
			throw new ExceptionUserNotFound();
		}
	}

	@Override
	public User updateUser(String userName, String attributes, Boolean merge) throws ExceptionUserBadRequest, ExceptionUserNotFound, ExceptionUserManagerFailure {
		//
		if (userName == null || userName.isEmpty()) {
			logger.error("[UserManager].updateUser : error = user name is invalid.");
			throw new ExceptionUserBadRequest();
		}
		
		try {
			if (thingManager.updateThing(userName, USER_TYPE_NAME, false, attributes, merge)) {
				Thing thing = thingManager.getThingByThingName(userName);
				User user = new User(thing.getThingId(), thing.getThingName(), thing.getAttributes());
				return user;
			}
			else {
				throw new ExceptionUserManagerFailure();
			}
		} catch (ExceptionThingBadRequest e) {
			logger.error("[UserManager].updateUser : error = " + e);
			throw new ExceptionUserBadRequest();
		} catch (ExceptionThingNotFound e) {
			logger.error("[UserManager].updateUser : error = " + e);
			throw new ExceptionUserNotFound();
		} catch (ExceptionThingManagerFailure e) {
			logger.error("[UserManager].updateUser : error = " + e);
			throw new ExceptionUserManagerFailure();
		}
	}

	@Override
	public User getUser(String userName) throws ExceptionUserBadRequest, ExceptionUserNotFound {
		//
		if (userName == null || userName.isEmpty()) {
			logger.error("[UserManager].getUser : error = user name is invalid.");
			throw new ExceptionUserBadRequest();
		}
		
		try {
			Thing thing = thingManager.getThingByThingName(userName);
			User user = new User(thing.getThingId(), thing.getThingName(), thing.getAttributes());
			return user;
		} catch (ExceptionThingBadRequest e) {
			logger.error("[UserManager].getUser : error = " + e);
			throw new ExceptionUserBadRequest();
		} catch (ExceptionThingNotFound e) {
			logger.error("[UserManager].getUser : error = " + e);
			throw new ExceptionUserNotFound();
		}
	}
	
	// TODO : remove friends in user group b/f delete user
	@Override
	public Boolean deleteUser(String userName) throws ExceptionUserBadRequest, ExceptionUserNotFound, ExceptionUserManagerFailure, ExceptionUserFriendGroupHasChildGroup, ExceptionUserFriendGroupIsNotEmpty {
		//
		if (userName == null || userName.isEmpty()) {
			logger.error("[UserManager].getUser : error = user name is invalid.");
			throw new ExceptionUserBadRequest();
		}
		
		try {
			String groupName = (userName + GROUP_NAME_SUFFIX);
			
			// check friends and remove first
			Set<String> friendNames = groupManager.getThingNamesInGroup(groupName, false);
			if (friendNames != null && !friendNames.isEmpty()) {
				for (String friendName : friendNames) {
					try {
						groupManager.removeThingFromGroup(groupName, friendName);
					} catch(Exception e) {
						logger.error("[UserManager].deleteUser : error at removeThingFromGroup = " + e);
					}
				}
			}
			
			if (groupManager.deleteGroup(groupName)) {
				return thingManager.deleteThing(userName);
			}
			return false;
		} catch (ExceptionThingBadRequest | ExceptionGroupBadRequest e) {
			logger.error("[UserManager].deleteUser : error = " + e);
			throw new ExceptionUserBadRequest();
		} catch (ExceptionThingNotFound | ExceptionGroupNotFound e) {
			logger.error("[UserManager].deleteUser : error = " + e);
			throw new ExceptionUserNotFound();
		} catch (ExceptionThingManagerFailure | ExceptionGroupManagerFailure e) {
			logger.error("[UserManager].deleteUser : error = " + e);
			throw new ExceptionUserManagerFailure();
		} catch (ExceptionGroupUnavailableForDeleteNonLeaf e) {
			logger.error("[UserManager].deleteUser : error = " + e);
			throw new ExceptionUserFriendGroupHasChildGroup();
		} catch (ExceptionGroupUnavailableForDeleteNonEmpty e) {
			logger.error("[UserManager].deleteUser : error = " + e);
			throw new ExceptionUserFriendGroupIsNotEmpty();
		}
	}
	
	@Override
	public Boolean addFriend(String userName, String friendName) throws ExceptionUserBadRequest, ExceptionUserNotFound, ExceptionUserManagerFailure {
		//
		if (userName == null || userName.isEmpty() || friendName == null || friendName.isEmpty()) {
			logger.error("[UserManager].addFriend : error = user(or friend) name is invalid.");
			throw new ExceptionUserBadRequest();
		}
		
		try {
			@SuppressWarnings("unused")
			Thing thingOfUser = thingManager.getThingByThingName(userName);
			Thing thingOfFriend = thingManager.getThingByThingName(friendName);
			
			Group group = groupManager.getGroup((userName + GROUP_NAME_SUFFIX));
			return groupManager.addThingToGroup(group.getGroupName(), thingOfFriend.getThingName());
		} catch (ExceptionThingBadRequest | ExceptionGroupBadRequest  e) {
			logger.error("[UserManager].addFriend : error = " + e);
			throw new ExceptionUserBadRequest();
		} catch (ExceptionThingNotFound | ExceptionGroupNotFound e) {
			logger.error("[UserManager].addFriend : error = " + e);
			throw new ExceptionUserNotFound();
		} catch (ExceptionGroupManagerFailure e) {
			logger.error("[UserManager].addFriend : error = " + e);
			throw new ExceptionUserManagerFailure();
		}
	}
	
	@Override
	public Boolean removeFriend(String userName, String friendName) throws ExceptionUserBadRequest, ExceptionUserNotFound, ExceptionUserManagerFailure {
		//
		if (userName == null || userName.isEmpty() || friendName == null || friendName.isEmpty()) {
			logger.error("[UserManager].removeFriend : error = user(or friend) name is invalid.");
			throw new ExceptionUserBadRequest();
		}
		
		try {
			@SuppressWarnings("unused")
			Thing thingOfUser = thingManager.getThingByThingName(userName);
			Thing thingOfFriend = thingManager.getThingByThingName(friendName);
			
			Group group = groupManager.getGroup((userName + GROUP_NAME_SUFFIX));
			return groupManager.removeThingFromGroup(group.getGroupName(), thingOfFriend.getThingName());
		} catch (ExceptionThingBadRequest | ExceptionGroupBadRequest  e) {
			logger.error("[UserManager].removeFriend : error = " + e);
			throw new ExceptionUserBadRequest();
		} catch (ExceptionThingNotFound | ExceptionGroupNotFound e) {
			logger.error("[UserManager].removeFriend : error = " + e);
			throw new ExceptionUserNotFound();
		} catch (ExceptionGroupManagerFailure e) {
			logger.error("[UserManager].removeFriend : error = " + e);
			throw new ExceptionUserManagerFailure();
		}
	}
}