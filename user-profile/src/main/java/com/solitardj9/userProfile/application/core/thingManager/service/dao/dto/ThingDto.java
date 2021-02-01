package com.solitardj9.userProfile.application.core.thingManager.service.dao.dto;

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
	name = "ThingMapping",
	classes = @ConstructorResult(
		targetClass = ThingDto.class,
		columns = {
			@ColumnResult(name="id", type = Integer.class),
			@ColumnResult(name="thing_name", type = String.class),
			@ColumnResult(name="attributes", type = String.class),
			@ColumnResult(name="thing_type_name", type = String.class)
		}
	)
)

@Entity
@Table(name="thing")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThingDto {
	//
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)							
	private Integer id;
	
	@Column(name="thing_name")
	private String thingName;
	
	@Column(name="attributes")
	private String attributes;
	
	@Column(name="thing_type_name")
	private String thingTypeName;
}