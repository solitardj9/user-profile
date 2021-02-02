package com.solitardj9.userProfile.application.core.groupManager.service.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solitardj9.userProfile.application.core.groupManager.service.dao.dto.GroupTreeDto;

@Repository
public interface GroupTreeRepository extends JpaRepository<GroupTreeDto, String> {

	GroupTreeDto findByRootGroupName(String rootGroupName);
	
	@Transactional
	void deleteByRootGroupName(String rootGroupName);
}