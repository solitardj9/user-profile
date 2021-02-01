package com.solitardj9.userProfile.application.core.groupManager.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solitardj9.userProfile.application.core.groupManager.dao.dto.GroupAndThingDto;
import com.solitardj9.userProfile.application.core.groupManager.dao.dto.GroupAndThingPks;

public interface GroupAndThingRepository extends JpaRepository<GroupAndThingDto, GroupAndThingPks> {

}