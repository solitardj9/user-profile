package com.solitardj9.userProfile.application.core.groupManager.dao.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class GroupAndThingPks implements Serializable {

	private static final long serialVersionUID = 4067663500463475938L;

	@Column(name="group_name")
	private String groupName;
	
	@Column(name="thing_name")
	private String thingName;
}