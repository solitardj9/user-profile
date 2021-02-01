package com.solitardj9.userProfile.application.core.groupManager.dao.dto;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="group_and_thing")
public class GroupAndThingDto {

	@EmbeddedId
	private GroupAndThingPks groupAndThingPks;
	
	public GroupAndThingDto(String groupName, String thingName) {
		//
		this.groupAndThingPks = new GroupAndThingPks(groupName, thingName);
	}
}