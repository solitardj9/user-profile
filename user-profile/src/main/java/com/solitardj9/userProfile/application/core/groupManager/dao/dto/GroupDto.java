package com.solitardj9.userProfile.application.core.groupManager.dao.dto;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SqlResultSetMapping(
	name = "GroupMapping",
	classes = @ConstructorResult(
		targetClass = GroupDto.class,
		columns = {
			@ColumnResult(name="id", type = Integer.class),
			@ColumnResult(name="group_name", type = String.class),
			@ColumnResult(name="attributes", type = String.class),
			@ColumnResult(name="group_type_name", type = String.class),
			@ColumnResult(name="parent_group_name", type = String.class)
		}
	)
)

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="group")
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
}