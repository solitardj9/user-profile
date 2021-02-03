package com.solitardj9.userProfile.application.core.groupManager.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scalified.tree.TreeNode;
import com.scalified.tree.multinode.ArrayMultiTreeNode;
import com.solitardj9.userProfile.application.core.groupManager.model.Group;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupAlreayExist;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupBadRequest;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupManagerFailure;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupNotFound;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupUnavailableForDeleteNonEmpty;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupUnavailableForDeleteNonLeaf;
import com.solitardj9.userProfile.application.core.groupManager.service.GroupManager;
import com.solitardj9.userProfile.application.core.groupManager.service.dao.GroupAndThingRepository;
import com.solitardj9.userProfile.application.core.groupManager.service.dao.GroupNativeQueryRepository;
import com.solitardj9.userProfile.application.core.groupManager.service.dao.GroupRepository;
import com.solitardj9.userProfile.application.core.groupManager.service.dao.GroupTreeRepository;
import com.solitardj9.userProfile.application.core.groupManager.service.dao.dto.GroupAndThingDto;
import com.solitardj9.userProfile.application.core.groupManager.service.dao.dto.GroupAndThingPks;
import com.solitardj9.userProfile.application.core.groupManager.service.dao.dto.GroupDto;
import com.solitardj9.userProfile.application.core.groupManager.service.dao.dto.GroupTreeDto;
import com.solitardj9.userProfile.application.core.groupManager.service.impl.groupTree.LayeredGroup;
import com.solitardj9.userProfile.application.core.thingManager.service.ThingManager;

@Service("groupManager")
public class GroupManagerImpl implements GroupManager {

	private static final Logger logger = LoggerFactory.getLogger(GroupManagerImpl.class);
	
	@Autowired
	GroupRepository groupRepository;
	
	@Autowired
	GroupTreeRepository groupTreeRepository;
	
	@Autowired
	GroupAndThingRepository groupAndThingRepository;
	
	@Autowired
	GroupNativeQueryRepository groupNativeQueryRepository;
	
	@Autowired
	ThingManager thingManager;
	
	private Boolean isInitialized = false;
	
	private ObjectMapper om = new ObjectMapper();
	
	@PostConstruct
	public void init() {
		//
		groupNativeQueryRepository.createGroupTable();
		groupNativeQueryRepository.createGroupTreeTable();
		groupNativeQueryRepository.createGroupAndThingTable();
		
		isInitialized = true;
	}
	
	@Override
	public Boolean isInitialized() {
		return isInitialized;
	}

	// about group ------------------------------------------------------------------------
	@Override
	public Group insertGroup(String groupName, String attributes, String groupTypeName, String parentGroupName, String rootGroupName) throws ExceptionGroupAlreayExist, ExceptionGroupBadRequest, ExceptionGroupManagerFailure {
		//
		Group group = new Group(null, groupName, attributes, groupTypeName, parentGroupName, rootGroupName);
		return insertGroup(group);
	}
	
	@Override
	public Group insertGroup(Group group) throws ExceptionGroupAlreayExist, ExceptionGroupBadRequest, ExceptionGroupManagerFailure {
		//
		String groupName = group.getGroupName();
		
		try {
			if (groupName == null || groupName.isEmpty())
				throw new ExceptionGroupBadRequest();
			
			GroupDto groupDto = getGroupDto(groupName);
			if (groupDto == null) {
				//  
				if (group.getAttributes() == null || group.getAttributes().isEmpty() || group.getAttributes().equals("null"))
					group.setAttributes("{}");
				
				groupDto = createGroup(group);
				if (groupDto != null)
					return convertGroupDtoToGroup(groupDto);
				return null;
			}
			else {
				logger.error("[GroupManager].insertGroup : error = Group is already exist.");
				throw new ExceptionGroupAlreayExist();
			}
		} catch(Exception e) {
			logger.error("[GroupManager].insertGroup : error = " + e);
			throw new ExceptionGroupManagerFailure();
		}
	}
	
	@Override
	public Group updateGroup(String groupName, String attributes, String groupTypeName, Boolean removeThingType, Boolean merge) throws ExceptionGroupNotFound, ExceptionGroupBadRequest, ExceptionGroupManagerFailure {
		//
		try {
			if (groupName == null || groupName.isEmpty())
				throw new ExceptionGroupBadRequest();
			
			GroupDto groupDto = getGroupDto(groupName);
			if (groupDto == null) {
				logger.error("[GroupManager].updateGroup : error = Group is not exist.");
				throw new ExceptionGroupNotFound();
			}
			
			if (removeThingType)
				groupDto.setGroupTypeName(null);
			else
				groupDto.setGroupTypeName(groupTypeName);
			
			try {
				if (merge) {
					Group tmpGroup = convertGroupDtoToGroup(groupDto);
					tmpGroup.mergeAttributes(attributes);
					groupDto.setAttributes(tmpGroup.getAttributes());
				}
				else {
					groupDto.setAttributes(attributes);
				}
			} catch (Exception e) {
				logger.error("[GroupManager].updateGroup : error = attributes is invallid. " + e);
			}
			
			return convertGroupDtoToGroup(saveAndGetGroupDto(groupDto));
		} catch(Exception e) {
			logger.error("[GroupManager].updateGroup : error = " + e);
			throw new ExceptionGroupManagerFailure();
		}
	}

	@Override
	public Boolean deleteGroup(String groupName) throws ExceptionGroupNotFound, ExceptionGroupBadRequest, ExceptionGroupUnavailableForDeleteNonLeaf, ExceptionGroupUnavailableForDeleteNonEmpty, ExceptionGroupManagerFailure {
		//
		if (groupName == null || groupName.isEmpty())
			throw new ExceptionGroupBadRequest();
		
		GroupDto groupDto = getGroupDto(groupName);
		if (groupDto == null) {
			logger.error("[GroupManager].removeGroup : error = Group is not exist.");
			throw new ExceptionGroupNotFound();
		}
		
		String rootGroupName = groupDto.getRootGroupName();
		if (groupName.equals(rootGroupName)) {
			//
			try {
				TreeNode<String> groupTree = convertStringToGroupTree(getGroupTreeDto(rootGroupName).getGroupTree());

				// need to be empty child group
				if (groupTree.isLeaf() == false)
					throw new ExceptionGroupUnavailableForDeleteNonLeaf();
				
				// need to has empty things
				Set<String> thingNames = getThingNamesInGroup(rootGroupName, false);
				if (thingNames != null & !thingNames.isEmpty())
					throw new ExceptionGroupUnavailableForDeleteNonEmpty();

				// remove groupDto
				removeGroupDto(rootGroupName);
				
				// remove groupTreeDto 
				removeGroupTreeDto(rootGroupName);
					
				return true;
			} catch (Exception e) {
				logger.error("[GroupManager].removeGroup #1 : error = " + e);
				throw new ExceptionGroupManagerFailure();
			}
		}
		else {
			//
			try {
				GroupTreeDto groupTreeDto = getGroupTreeDto(rootGroupName);
				TreeNode<String> groupTree = convertStringToGroupTree(groupTreeDto.getGroupTree());
				TreeNode<String> subTree = groupTree.find(groupName); 
				
				// need to be empty child group
				if (subTree.isLeaf() == false)
					throw new ExceptionGroupUnavailableForDeleteNonLeaf();
				
				// need to has empty things
				Set<String> thingNames = getThingNamesInGroup(groupName, false);
				if (thingNames != null & !thingNames.isEmpty())
					throw new ExceptionGroupUnavailableForDeleteNonEmpty(); 
				
				// remove groupDto
				removeGroupDto(groupName);
				
				// remove groupTreeDto (delete subtree and update)
				groupTree.remove(subTree);
				groupTreeDto.setGroupTree(convertGroupTreeToString(groupTree));
				saveGroupTreeDto(groupTreeDto);
				
				return true;
			} catch (Exception e) {
				logger.error("[GroupManager].removeGroup #2 : error = " + e);
				throw new ExceptionGroupManagerFailure();
			}
		}
	}

	@Override
	public Group getGroup(String groupName) throws ExceptionGroupBadRequest, ExceptionGroupNotFound {
		//
		if (groupName == null || groupName.isEmpty())
			throw new ExceptionGroupBadRequest();
		
		GroupDto groupDto = getGroupDto(groupName);
		if (groupDto != null)
			return convertGroupDtoToGroup(groupDto);
		else
			throw new ExceptionGroupNotFound();
	}

	@Override
	public List<Group> getGroupList(String parentGroupName, Boolean recursive) throws ExceptionGroupBadRequest, ExceptionGroupNotFound {
		//
		List<Group> retGroups = new ArrayList<>();
		List<GroupDto> resultGroupDtos = new ArrayList<>();
		
		if (parentGroupName == null || parentGroupName.isEmpty())
			throw new ExceptionGroupBadRequest();
		
		GroupDto parentGroupDto = getGroupDto(parentGroupName);
		if (!recursive) {
			resultGroupDtos.addAll(getChildGroups(parentGroupDto));
		}
		else {
			resultGroupDtos.addAll(getChainedChildGroups(parentGroupDto));
		}
		
		for (GroupDto iter : resultGroupDtos) {
			retGroups.add(convertGroupDtoToGroup(iter));
		}
		
		return retGroups;
	}
	
	@Override
	public List<Group> getAllGroups() {
		//
		List<Group> retGroups = new ArrayList<>();
		List<GroupDto> resultGroupDtos = getAllGroupDtos();
		
		for (GroupDto iter : resultGroupDtos) {
			retGroups.add(convertGroupDtoToGroup(iter));
		}
		
		return retGroups;
	}
	
	private List<GroupDto> getChildGroups(GroupDto parentGroupDto) {
		//
		List<GroupDto> childGroups = new ArrayList<>();
		
		TreeNode<String> groupTree = convertStringToGroupTree(getGroupTreeDto(parentGroupDto.getRootGroupName()).getGroupTree());
		TreeNode<String> parentGroupTree = groupTree.find(parentGroupDto.getGroupName());
		Collection<? extends TreeNode<String>> subtrees = parentGroupTree.subtrees();
		
		for (TreeNode<String> subtreesIter : subtrees) {
			childGroups.add(getGroupDto(subtreesIter.data()));
		}
		return childGroups;
	}
	
	private List<GroupDto> getChainedChildGroups(GroupDto parentGroupDto) {
		//
		List<GroupDto> childGroups = new ArrayList<>();
		
		TreeNode<String> groupTree = convertStringToGroupTree(getGroupTreeDto(parentGroupDto.getRootGroupName()).getGroupTree());
		TreeNode<String> parentGroupTree = groupTree.find(parentGroupDto.getGroupName());
		Collection<? extends TreeNode<String>> subtrees = parentGroupTree.postOrdered();
		
		for (TreeNode<String> subtreesIter : subtrees) {
			if (!subtreesIter.data().equals(parentGroupDto.getGroupName()))
				childGroups.add(getGroupDto(subtreesIter.data()));
		}
		return childGroups;
	}
	
	private List<String> getChainedChildGroupNames(GroupDto parentGroupDto) {
		//
		List<String> childGroupNames = new ArrayList<>();
		
		TreeNode<String> groupTree = convertStringToGroupTree(getGroupTreeDto(parentGroupDto.getRootGroupName()).getGroupTree());
		TreeNode<String> parentGroupTree = groupTree.find(parentGroupDto.getGroupName());
		Collection<? extends TreeNode<String>> subtrees = parentGroupTree.postOrdered();
		
		for (TreeNode<String> subtreesIter : subtrees) {
			if (!subtreesIter.data().equals(parentGroupDto.getGroupName()))
				childGroupNames.add(subtreesIter.data());
		}
		return childGroupNames;
	}
	
	@Override
	public List<Group> getGroupListFromRootToGroup(String groupName) throws ExceptionGroupBadRequest, ExceptionGroupNotFound {
		//
		List<Group> parentGroups = new ArrayList<>();
		List<GroupDto> parentGroupDtos = new ArrayList<>();
		
		if (groupName == null || groupName.isEmpty())
			throw new ExceptionGroupBadRequest();
		
		GroupDto groupDto = getGroupDto(groupName);
		TreeNode<String> groupTree = convertStringToGroupTree(getGroupTreeDto(groupDto.getRootGroupName()).getGroupTree());
		
		TreeNode<String> targetTree = groupTree.find(groupName);
		while(!targetTree.isRoot()) {
			targetTree = targetTree.parent();
			parentGroupDtos.add(getGroupDto(targetTree.data()));
		}

		for (GroupDto iter : parentGroupDtos) {
			parentGroups.add(convertGroupDtoToGroup(iter));
		}
		
		return parentGroups;
	}
	
	private GroupDto createGroup(Group group) {
		//
		String parentGroupName = group.getParentGroupName();
		
		if (parentGroupName == null || parentGroupName.isEmpty()) {
			return createRootGroup(group);
		}
		else {
			return createChildGroup(group);
		}
	}
	
	private GroupDto createRootGroup(Group group) {
		//
		try {
			String groupName = group.getGroupName();
			String attributes = group.getAttributes();
			String groupTypeName = group.getGroupTypeName();
			String parentGroupName = group.getParentGroupName();
			String rootGroupName = groupName;	// root and group is same.
			
			GroupDto groupDto = new GroupDto(null, groupName, attributes, groupTypeName, parentGroupName, rootGroupName);
			
			// save group
			groupDto = saveAndGetGroupDto(groupDto);
			if ( groupDto != null) {
				// make group tree
				TreeNode<String> groupTree = createGroupTree(rootGroupName);
				
				// save group tree
				String strGroupTree = convertGroupTreeToString(groupTree);
				if (strGroupTree != null) {
					GroupTreeDto groupTreeDto = new GroupTreeDto(rootGroupName, strGroupTree);
					saveGroupTreeDto(groupTreeDto);
					return groupDto;
				}
			}
			
			return null;
		} catch (Exception e) {
			logger.error("[ThingGroupManager].createRootGroup : error = " + e);
			return null;
		}
	}
	
	private TreeNode<String> createGroupTree(String rootGroupName) {
		// make tree node for root group
		TreeNode<String> groupTree = new ArrayMultiTreeNode<String>(rootGroupName);
		return groupTree;
	}
	
	private GroupDto createChildGroup(Group group) {
		//
		try {
			String groupName = group.getGroupName();
			String attributes = group.getAttributes();
			String groupTypeName = group.getGroupTypeName();
			String parentGroupName = group.getParentGroupName();
			
			// find parent group
			GroupDto parentGroupDto = getGroupDto(parentGroupName);
			
			// find root group name
			String rootGroupName = parentGroupDto.getRootGroupName();
			
			GroupDto groupDto = new GroupDto(null, groupName, attributes, groupTypeName, parentGroupName, rootGroupName);
			
			// save group
			groupDto = saveAndGetGroupDto(groupDto);
			
			if ( groupDto != null) {
				// find group tree	
				GroupTreeDto groupTreeDto = getGroupTreeDto(rootGroupName);
				
				// make tree node for child group, then add to group tree
				TreeNode<String> groupTree = convertStringToGroupTree(groupTreeDto.getGroupTree());
				groupTree.find(parentGroupName).add(new ArrayMultiTreeNode<String>(group.getGroupName()));
				
				// save group tree
				String strGroupTree = convertGroupTreeToString(groupTree);
				if (strGroupTree != null) {
					GroupTreeDto newGroupTreeDto = new GroupTreeDto(rootGroupName, strGroupTree);
					saveGroupTreeDto(newGroupTreeDto);
					return groupDto;
				}
			}
			
			return null;
		} catch (Exception e) {
			logger.error("[ThingGroupManager].createChildGroup : error = " + e);
			return null;
		}
	}
	
	private GroupDto getGroupDto(String groupName) {
		//
		return groupRepository.findByGroupName(groupName);
	}
	
	private List<GroupDto> getAllGroupDtos() {
		//
		return groupRepository.findAll();
	}
	
	private void saveGroupDto(GroupDto groupDto) {
		//
		groupRepository.save(groupDto);
	}
	
	private GroupDto saveAndGetGroupDto(GroupDto groupDto) {
		//
		saveGroupDto(groupDto);
		return groupRepository.findByGroupName(groupDto.getGroupName());
	}
	
	private void removeGroupDto(String groupName) {
		//
		groupRepository.deleteByGroupName(groupName);
	}
	
	private GroupTreeDto getGroupTreeDto(String rootGroupName) {
		//
		return groupTreeRepository.findByRootGroupName(rootGroupName);
	}
	
	private void saveGroupTreeDto(GroupTreeDto groupTreeDto) {
		//
		groupTreeRepository.save(groupTreeDto);
	}
	
	private void removeGroupTreeDto(String rootGroupName) {
		//
		groupTreeRepository.deleteByRootGroupName(rootGroupName);
	}
	
	private String convertGroupTreeToString(TreeNode<String> groupTree) {
		//
		String strGroupTree = null;
		
		LayeredGroup layeredGroup = convertGroupTreeToLayeredGroup(groupTree);
		try {
			strGroupTree = om.writeValueAsString(layeredGroup);
		} catch (JsonProcessingException e) {
			logger.error("[GroupManager].convertGroupTreeToString : error = " + e);
			return null;
		}
		
		return strGroupTree;
	}
	
	private LayeredGroup convertGroupTreeToLayeredGroup(TreeNode<String> node) {
		//
		if (node.isLeaf()) {
			LayeredGroup ret = new LayeredGroup(node.data(), (node.parent() == null) ? null : node.parent().data(), node.root().data(), null);
			return ret;
		}
		else {
			List<LayeredGroup> childGroups = new ArrayList<>();
			
			Collection<? extends TreeNode<String>> subtrees = node.subtrees();
			for (TreeNode<String> childIter : subtrees) {
				LayeredGroup childGroup = convertGroupTreeToLayeredGroup(childIter);
				childGroups.add(childGroup);
			}
			
			LayeredGroup ret = new LayeredGroup(node.data(), (node.isRoot()) ? null : node.parent().data(), node.root().data(), childGroups);
			return ret;
		}
	}
	
	private TreeNode<String> convertStringToGroupTree(String strGroupTree) {
		//
		LayeredGroup layeredGroup = null;
		try {
			layeredGroup = om.readValue(strGroupTree, LayeredGroup.class);
		} catch (JsonProcessingException e) {
			logger.error("[GroupManager].convertStringToGroupTree : error = " + e);
			return null;
		}
		
		TreeNode<String> groupTree = convertLayeredGroupToGroupTree(layeredGroup);
		return groupTree;
	}
	
	private TreeNode<String> convertLayeredGroupToGroupTree(LayeredGroup layeredGroup) {
		//
		TreeNode<String> groupTree = new ArrayMultiTreeNode<String>(layeredGroup.getGroupName());
		
		if (layeredGroup.getChildGroups() != null) {
			for (LayeredGroup iter : layeredGroup.getChildGroups()) {
				TreeNode<String> subtree = convertLayeredGroupToGroupTree(iter);
				groupTree.add(subtree);
			}
		}
		
		return groupTree;
	}
	
	private Group convertGroupDtoToGroup(GroupDto groupDto) {
		//
		return new Group(groupDto.getId(), groupDto.getGroupName(), groupDto.getAttributes(), groupDto.getGroupTypeName(), groupDto.getParentGroupName(), groupDto.getRootGroupName());
	}
	
	@SuppressWarnings("unused")
	private GroupDto convertGroupToGroupDto(Group group) {
		//
		return new GroupDto(null, group.getGroupName(), group.getAttributes(), group.getGroupTypeName(), group.getParentGroupName(), group.getRootGroupName());
	}

	// about thing ------------------------------------------------------------------------
	@Override
	public Boolean addThingToGroup(String groupName, String thingName) throws ExceptionGroupBadRequest, ExceptionGroupNotFound, ExceptionGroupManagerFailure {
		//
		if (!thingManager.isValidThing(thingName))
			throw new ExceptionGroupBadRequest();
				
		try {
			// check groupName
			if (getGroupDto(groupName) != null) {
				GroupAndThingDto groupAndThingDto = new GroupAndThingDto(groupName, thingName);
				saveGroupAndThingDto(groupAndThingDto);
				return true;
			}
			else {
				logger.error("[GroupManager].addThingToThingGroup : error = Group is not exist.");
				throw new ExceptionGroupNotFound();
			}
				
		} catch (Exception e) {
			logger.error("[GroupManager].addThingToThingGroup : error = " + e);
			throw new ExceptionGroupManagerFailure();
		}
	}
	
	@Override
	public Boolean updateGroupsOfThing(List<String> groupsToAdd, List<String> groupsToRemove, String thingName) throws ExceptionGroupBadRequest {
		//
		if (!thingManager.isValidThing(thingName))
			throw new ExceptionGroupBadRequest();
		
		try {
			List<GroupAndThingDto> groupAndThingDtosToRemove = new ArrayList<>();
			for (String iter : groupsToRemove) {
				groupAndThingDtosToRemove.add(new GroupAndThingDto(iter, thingName));
			}
			deleteGroupAndThingDtos(groupAndThingDtosToRemove);
			
			List<GroupAndThingDto> groupAndThingDtosToAdd = new ArrayList<>();
			for (String iter : groupsToAdd) {
				groupAndThingDtosToAdd.add(new GroupAndThingDto(iter, thingName));
			}
			saveGroupAndThingDtos(groupAndThingDtosToAdd);
			
			return true;
		} catch (Exception e) {
			logger.error("[GroupManager].updateGroupsOfThing : error = " + e);
			return false;
		}
	}
	
	@Override
	public Boolean removeThingFromGroup(String groupName, String thingName) throws ExceptionGroupBadRequest, ExceptionGroupNotFound, ExceptionGroupManagerFailure {
		//
		if (!thingManager.isValidThing(thingName))
			throw new ExceptionGroupBadRequest();
		
		try {
			// check groupName
			if (getGroupDto(groupName) != null) {
				//
				GroupAndThingDto groupAndThingDto = getGroupAndThingDto(new GroupAndThingPks(groupName, thingName));
				if (groupAndThingDto != null) {
					try {
						deleteGroupAndThingDto(groupAndThingDto);
						return true;
					} catch (Exception e) {
						logger.error("[GroupManager].removeThingFromGroup : error = " + e);
						throw new ExceptionGroupManagerFailure();
					}
				}
				else {
					logger.error("[GroupManager].removeThingFromGroup : error = ");
					throw new ExceptionGroupNotFound();
				}
			}
			else {
				logger.error("[GroupManager].removeThingFromGroup : error = Group is not exist.");
				throw new ExceptionGroupNotFound();
			}
				
		} catch (Exception e) {
			logger.error("[GroupManager].removeThingFromGroup : error = " + e);
			throw new ExceptionGroupManagerFailure();
		}
	}
	
	@Override
	public Set<String> getThingNamesInGroup(String groupName, Boolean recursive) throws ExceptionGroupNotFound, ExceptionGroupManagerFailure {
		//
		try {
			GroupDto groupDto = getGroupDto(groupName);
			// check groupName
			if (groupDto != null) {
				//
				Set<String> retSet = new HashSet<>();
				
				Set<String> targetGroups = new HashSet<>();
				if (recursive.equals(false)) {
					targetGroups.add(groupName);
				}
				else {
					targetGroups.addAll(getChainedChildGroupNames(groupDto));
				}
				
				List<GroupAndThingDto> groupAndThingDtos = getGroupAndThingDtosInGroupNames(targetGroups);
				for (GroupAndThingDto iter : groupAndThingDtos) {
					retSet.add(iter.getGroupAndThingPks().getThingName());
				}
				return retSet;
			}
			else {
				logger.error("[GroupManager].removeThingFromGroup : error = Group is not exist.");
				throw new ExceptionGroupNotFound();
			}
		} catch (Exception e) {
			logger.error("[GroupManager].getThingNamesInGroup : error = " + e);
			throw new ExceptionGroupManagerFailure();
		}
	}
	
	@Override
	public Set<String> getGroupNamesOfThing(String thingName) throws ExceptionGroupBadRequest, ExceptionGroupManagerFailure {
		//
		// check thingName
		if (!thingManager.isValidThing(thingName))
			throw new ExceptionGroupBadRequest();
		
		try {
			//
			Set<String> retSet = new HashSet<>();
			
			List<GroupAndThingDto> groupAndThingDtos = getGroupAndThingDtosByThingName(thingName);
			for (GroupAndThingDto iter : groupAndThingDtos) {
				retSet.add(iter.getGroupAndThingPks().getGroupName());
			}
			return retSet;
		} catch (Exception e) {
			logger.error("[GroupManager].getGroupNamesOfThing : error = " + e);
			throw new ExceptionGroupManagerFailure();
		}
	}
	
	@Override
	public List<GroupAndThingDto> getAllGroupAndThingDtos() {
		//
		return groupAndThingRepository.findAll();
	}
	
	private void saveGroupAndThingDto(GroupAndThingDto groupAndThingDto) {
		//
		groupAndThingRepository.save(groupAndThingDto);
	}
	
	private void saveGroupAndThingDtos(List<GroupAndThingDto> groupAndThingDtos) {
		//
		groupAndThingRepository.saveAll(groupAndThingDtos);
	}
	
	private void deleteGroupAndThingDto(GroupAndThingDto groupAndThingDto) {
		//
		groupAndThingRepository.delete(groupAndThingDto);
	}
	
	private void deleteGroupAndThingDtos(List<GroupAndThingDto> groupAndThingDtos) {
		//
		groupAndThingRepository.deleteAll(groupAndThingDtos);
	}
	
	private GroupAndThingDto getGroupAndThingDto(GroupAndThingPks groupAndThingPks) {
		//
		return groupAndThingRepository.getOne(groupAndThingPks);
	}
	
	private List<GroupAndThingDto> getGroupAndThingDtosInGroupNames(Set<String> groupNames) {
		//
		List<GroupAndThingDto> retList = new ArrayList<>();
		
		for (String iter : groupNames) {
			GroupAndThingDto groupAndThingDto = new GroupAndThingDto(iter, null);
			Example<GroupAndThingDto> example = Example.of(groupAndThingDto);
			
			List<GroupAndThingDto> result = groupAndThingRepository.findAll(example);
			retList.addAll(result);
		}
		
		return retList;
	}
	
	private List<GroupAndThingDto> getGroupAndThingDtosByThingName(String thingName) {
		//
		GroupAndThingDto groupAndThingDto = new GroupAndThingDto(null, thingName);
		
		Example<GroupAndThingDto> example = Example.of(groupAndThingDto);
		List<GroupAndThingDto> result = groupAndThingRepository.findAll(example);
		
		return result;
	}
	
}