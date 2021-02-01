package com.solitardj9.userProfile.application.core.groupManager.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solitardj9.userProfile.application.core.groupManager.dao.dto.GroupDto;

@Repository
public interface GroupRepository extends JpaRepository<GroupDto, String> {

	Optional<GroupDto> findByGroupName(String groupName);
	
	List<GroupDto> findAllByGroupName(List<String> groupNames);
	
	List<GroupDto> findAllByParentGroupName(String parentGroupName);
}