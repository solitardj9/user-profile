package com.solitardj9.userProfile.application.core.groupManager.service.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solitardj9.userProfile.application.core.groupManager.service.dao.dto.GroupAndThingDto;
import com.solitardj9.userProfile.application.core.groupManager.service.dao.dto.GroupAndThingPks;

public interface GroupAndThingRepository extends JpaRepository<GroupAndThingDto, GroupAndThingPks> {

}