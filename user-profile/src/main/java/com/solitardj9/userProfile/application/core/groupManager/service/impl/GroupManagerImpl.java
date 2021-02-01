package com.solitardj9.userProfile.application.core.groupManager.service.impl;

import java.util.List;

import com.solitardj9.userProfile.application.core.groupManager.model.Group;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupAlreayExist;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupBadRequest;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupNotFound;
import com.solitardj9.userProfile.application.core.groupManager.service.GroupManager;

public class GroupManagerImpl implements GroupManager {

	@Override
	public Group insertGroup(Group group) throws ExceptionGroupAlreayExist, ExceptionGroupBadRequest {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Group updateGroup(Group group) throws ExceptionGroupNotFound, ExceptionGroupBadRequest {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeGroup(String groupName, Integer expectedVersion)
			throws ExceptionGroupNotFound, ExceptionGroupBadRequest {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Group getGroup(String groupName) throws ExceptionGroupNotFound {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Group> getGroupList(String namePrefix, String parentGroupName, Boolean recursive)
			throws ExceptionGroupNotFound {
		// TODO Auto-generated method stub
		return null;
	}

}
