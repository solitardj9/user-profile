package com.solitardj9.userProfile.application.core.groupManager.dao.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="group_tree")
public class GroupTreeDto {

	@Id
	@Column(name="root_group_name")
	private String rootGroupName;
	
	@Column(name="group_tree")
	private String groupTree;
}