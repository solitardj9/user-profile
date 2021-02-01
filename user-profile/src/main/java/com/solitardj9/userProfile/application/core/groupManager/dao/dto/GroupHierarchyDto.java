package com.solitardj9.userProfile.application.core.groupManager.dao.dto;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SqlResultSetMapping(
	name = "GroupHierarchyMapping",
	classes = @ConstructorResult(
		targetClass = GroupHierarchyDto.class,
		columns = {
			@ColumnResult(name="name", type = String.class),
			@ColumnResult(name="category", type = Integer.class),
			@ColumnResult(name="depth", type = Integer.class),
			@ColumnResult(name="left", type = Integer.class),
			@ColumnResult(name="right", type = Integer.class)
		}
	)
)

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="group_hierarchy")
public class GroupHierarchyDto {

	@Id	
	@Column(name="group_name")
	private String groupName;
	
	@Column(name="category")
	private Integer category;

	@Column(name="depth")
	private Integer depth;
	
	@Column(name="left")
	private Integer left;
	
	@Column(name="right")
	private Integer right;
}