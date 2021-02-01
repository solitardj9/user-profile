package com.solitardj9.userProfile.application.core.groupManager.service.impl;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.solitardj9.userProfile.application.core.groupManager.dao.GroupAndThingRepository;
import com.solitardj9.userProfile.application.core.groupManager.dao.GroupHierarchyRepository;
import com.solitardj9.userProfile.application.core.groupManager.dao.GroupRepository;
import com.solitardj9.userProfile.application.core.groupManager.dao.dto.GroupDto;
import com.solitardj9.userProfile.application.core.groupManager.model.Group;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupAlreayExist;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupBadRequest;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupNotFound;
import com.solitardj9.userProfile.application.core.groupManager.service.GroupManager;

public class GroupManagerImpl implements GroupManager {

	@Autowired
	GroupRepository groupRepository;
	
	@Autowired
	GroupHierarchyRepository groupHierarchyRepository;
	
	@Autowired
	GroupAndThingRepository groupAndThingRepository;
	
	private Boolean isInitialized = false;
	
	@PostConstruct
	public void init() {
		//
//		thingNativeQueryRepository.createThingTable();
		isInitialized = true;
	}
	
	@Override
	public Boolean isInitialized() {
		return isInitialized;
	}
	
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

//	@Override
//	public Group getGroup(String groupName) throws ExceptionGroupNotFound {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<Group> getGroupList(String namePrefix, String parentGroupName, Boolean recursive)
//			throws ExceptionGroupNotFound {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//	
//	
//	private Group getGroup(String groupName) {
//		ThingGroupInfo group = null;
//		Optional<ThingGroupInfo> groupList = groupInfoRepository.findByName(groupName);
//		if (groupList.isPresent()) {
//			group = groupList.get();
//		} else {
//			return null;
//		}
//		return group;
//	}
//	
//	private GroupDto getGroup(String groupName) {
//		//
//		Optional<ThingGroupInfo> groupList = groupInfoRepository.findByName(groupName);
//	}

}
