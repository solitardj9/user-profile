package com.solitardj9.userProfile.application.core.groupManager.service;

import java.util.List;

import com.solitardj9.userProfile.application.core.groupManager.model.Group;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupAlreayExist;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupBadRequest;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupNotFound;

public interface GroupManager {
	
	public Boolean isInitialized();
	
	public Group insertGroup(Group group) throws ExceptionGroupAlreayExist, ExceptionGroupBadRequest;
	
	public Group updateGroup(Group group) throws ExceptionGroupNotFound, ExceptionGroupBadRequest;
	
	public void removeGroup(String groupName, Integer expectedVersion) throws ExceptionGroupNotFound, ExceptionGroupBadRequest;
	
	public Group getGroup(String groupName) throws ExceptionGroupNotFound;
	
	public List<Group> getGroupList(String namePrefix, String parentGroupName, Boolean recursive) throws ExceptionGroupNotFound;
	
//	/**
//	 * @brief 특정 Thing 이 속한 group 리스트 조회
//	 * 
//	 * @param thingName
//	 * @param maxResult
//	 * @param nextToken
//	 * @return
//	 * - thing group name 리스트
//	 * @throws ResourceNotFoundException 
//	 */
//	public ListResult getGroupListByThing(String thingName, Integer maxResult, String nextToken) throws ResourceNotFoundException;
//	
//	/**
//	 * @brief 
//	 * 
//	 * @param groupName
//	 * @param maxResult
//	 * @param nextToken
//	 * @param recursive
//	 *  - True : 해당  group 에 속한 thing 들과, 모든 child group 들의 thing 들까지 조회
//	 *  - False : 해당 group 에 속한 thing 만 조회
//	 * @return
//	 * - thingName 리스트
//	 * @throws ResourceNotFoundException 
//	 */
//	public ListResult getThingListByGroup(String groupName, Integer maxResult, String nextToken, Boolean recursive) throws ResourceNotFoundException;
//	
//	/**
//	 * @brief group 에 thing 추가 
//	 * - thing 은 group 에 10개까지만 포함 될수 있다.
//	 * - thing 을 동일 계층에 속한 그룹 2개 이상에 추가 할수 없다
//	 *   같은 root 를 가지고 있는 group 에 추가 할 수 없다.
//	 * - root 를 추가할 때마다 category ++  
//	 * 
//	 * @param thingName
//	 * @param groupName
//	 * @throws ResourceNotFoundException 
//	 */
//	public void addThingToGroup(String thingName, String groupName) throws ResourceNotFoundException;
//	
//	public void removeThingFromGroup(String thingName, String groupName) throws ResourceNotFoundException;
//	
//	public void removeThing(String thingName);
//	
//	/**
//	 * @brief group 에 thing 변경 
//	 * - thing 은 group 에 10개까지만 포함 될수 있다.
//	 * - thing 을 동일 계층에 속한 그룹 2개 이상에 추가 할수 없다
//	 *   같은 root 를 가지고 있는 group 에 추가 할 수 없다.
//	 *   
//	 * @param thingName
//	 * @param groupsToAdd
//	 * @param groupToRemove
//	 */
//	public void updateGroupForThing(String thingName, List<String> groupsToAdd, List<String> groupToRemove);
	
	
}