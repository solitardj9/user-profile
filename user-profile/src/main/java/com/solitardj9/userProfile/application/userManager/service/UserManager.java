package com.solitardj9.userProfile.application.userManager.service;

import com.solitardj9.userProfile.application.userManager.model.User;
import com.solitardj9.userProfile.application.userManager.model.exception.ExceptionUserAlreayExist;
import com.solitardj9.userProfile.application.userManager.model.exception.ExceptionUserBadRequest;
import com.solitardj9.userProfile.application.userManager.model.exception.ExceptionUserFriendGroupHasChildGroup;
import com.solitardj9.userProfile.application.userManager.model.exception.ExceptionUserFriendGroupIsNotEmpty;
import com.solitardj9.userProfile.application.userManager.model.exception.ExceptionUserManagerFailure;
import com.solitardj9.userProfile.application.userManager.model.exception.ExceptionUserNotFound;

public interface UserManager {

	public Boolean isInitialized();
	
	public User createUser(String userName, String attributes, Boolean merge) throws ExceptionUserBadRequest, ExceptionUserAlreayExist, ExceptionUserManagerFailure, ExceptionUserNotFound;
	
	public User updateUser(String userName, String attributes, Boolean merge) throws ExceptionUserBadRequest, ExceptionUserNotFound, ExceptionUserManagerFailure;
	
	public User getUser(String userName) throws ExceptionUserBadRequest, ExceptionUserNotFound;
	
	public Boolean deleteUser(String userName) throws ExceptionUserBadRequest, ExceptionUserNotFound, ExceptionUserManagerFailure, ExceptionUserFriendGroupHasChildGroup, ExceptionUserFriendGroupIsNotEmpty;
	
	public Boolean addFriend(String userName, String friendName) throws ExceptionUserBadRequest, ExceptionUserNotFound, ExceptionUserManagerFailure;
	
	public Boolean removeFriend(String userName, String friendName) throws ExceptionUserBadRequest, ExceptionUserNotFound, ExceptionUserManagerFailure;
}