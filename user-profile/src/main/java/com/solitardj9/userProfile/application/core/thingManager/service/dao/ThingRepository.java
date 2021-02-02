package com.solitardj9.userProfile.application.core.thingManager.service.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solitardj9.userProfile.application.core.thingManager.service.dao.dto.ThingDto;

@Repository
public interface ThingRepository extends JpaRepository<ThingDto, Integer> {

	ThingDto findByThingName(String thingName);
	
	Boolean existsByThingName(String thingName);
}