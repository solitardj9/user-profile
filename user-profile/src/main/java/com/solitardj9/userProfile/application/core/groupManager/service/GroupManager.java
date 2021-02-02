
package com.solitardj9.userProfile.application.core.groupManager.service;

import java.util.List;
import java.util.Set;

import com.solitardj9.userProfile.application.core.groupManager.model.Group;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupAlreayExist;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupBadRequest;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupManagerFailure;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupNotFound;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupUnavailableForDeleteNonEmpty;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupUnavailableForDeleteNonLeaf;
import com.solitardj9.userProfile.application.core.groupManager.service.dao.dto.GroupAndThingDto;

public interface GroupManager {
	
	public Boolean isInitialized();
	
	// about group ------------------------------------------------------------------------
	public Group insertGroup(String groupName, String attributes, String groupTypeName, String parentGroupName, String rootGroupName) throws ExceptionGroupAlreayExist, ExceptionGroupBadRequest, ExceptionGroupManagerFailure;
	
	public Group insertGroup(Group group) throws ExceptionGroupAlreayExist, ExceptionGroupBadRequest, ExceptionGroupManagerFailure;
	
	public Group updateGroup(String groupName, String attributes, String groupTypeName, Boolean removeThingType, Boolean merge) throws ExceptionGroupNotFound, ExceptionGroupBadRequest, ExceptionGroupManagerFailure;
	
	public Boolean removeGroup(String groupName) throws ExceptionGroupNotFound, ExceptionGroupBadRequest, ExceptionGroupUnavailableForDeleteNonLeaf, ExceptionGroupUnavailableForDeleteNonEmpty, ExceptionGroupManagerFailure;
	
	public Group getGroup(String groupName) throws ExceptionGroupBadRequest, ExceptionGroupNotFound;
	
	public List<Group> getGroupList(String parentGroupName, Boolean recursive) throws ExceptionGroupBadRequest, ExceptionGroupNotFound;
	
	public List<Group> getGroupListFromRootToGroup(String parentGroupName) throws ExceptionGroupBadRequest, ExceptionGroupNotFound;
	
	public List<Group> getAllGroups();
	
	// about thing ------------------------------------------------------------------------
	public Boolean addThingToGroup(String groupName, String thingName) throws ExceptionGroupBadRequest, ExceptionGroupNotFound, ExceptionGroupManagerFailure;
	
	public Boolean updateGroupsOfThing(List<String> groupsToAdd, List<String> groupsToRemove, String thingName) throws ExceptionGroupBadRequest;
	
	public Boolean removeThingFromGroup(String groupName, String thingName) throws ExceptionGroupBadRequest, ExceptionGroupNotFound, ExceptionGroupManagerFailure;
	
	public Set<String> getThingNamesInGroup(String groupName, Boolean recursive) throws ExceptionGroupNotFound, ExceptionGroupManagerFailure;
	
	public Set<String> getGroupNamesOfThing(String thingName) throws ExceptionGroupBadRequest, ExceptionGroupManagerFailure;
	
	public List<GroupAndThingDto> getAllGroupAndThingDtos();
	
}