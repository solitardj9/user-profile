package com.solitardj9.userProfile.application.core.groupManager.service.dao.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="group_")
public class GroupDto {
	//
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)							
	private Integer id;
	
	@Column(name="group_name")
	private String groupName;
	
	@Column(name="attributes")
	private String attributes;
	
	@Column(name="group_type_name")
	private String groupTypeName;
	
	@Column(name="parent_group_name")
	private String parentGroupName;
	
	@Column(name="root_group_name")
	private String rootGroupName;
}