package com.solitardj9.userProfile.application.core.groupManager.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solitardj9.userProfile.application.core.groupManager.dao.dto.GroupDto;

@Repository
public interface GroupRepository extends JpaRepository<GroupDto, String> {

	GroupDto findByGroupName(String groupName);
	
	@Transactional
	void deleteByGroupName(String groupName);
}