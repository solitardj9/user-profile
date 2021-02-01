package com.solitardj9.userProfile.application.core.groupManager.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solitardj9.userProfile.application.core.groupManager.dao.dto.GroupHierarchyDto;

@Repository
public interface GroupHierarchyRepository extends JpaRepository<GroupHierarchyDto, String> {

	Optional<GroupHierarchyDto> findByGroupName(String groupName);
	
	List<GroupHierarchyDto> findAllByGroupName(List<String> groupNames);
	
	Optional<GroupHierarchyDto> findByRightAndCategory(Integer right, Integer category);
}