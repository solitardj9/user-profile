package com.solitardj9.userProfile.application.core.groupManager.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solitardj9.userProfile.application.core.groupManager.dao.dto.GroupDto;
import com.solitardj9.userProfile.application.core.thingManager.service.dao.dto.ThingDto;

@Repository
public class GroupNativeQueryRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public Integer createGroupTable() {
		//
		String sql = "CREATE TABLE IF NOT EXISTS group("
				   + "id						varchar(128) NOT NULL , "
				   + "group_name				varchar(128) DEFAULT NULL, "
				   + "attributes				LONGTEXT DEFAULT NULL, "
				   + "group_type_name		varchar(128) DEFAULT NULL, "
				   + "parent_group_name		varchar(128) DEFAULT NULL, "
				   + "PRIMARY KEY PKEY (id)); ";	
		Integer result = entityManager.createNativeQuery(sql).executeUpdate();
	    
		return result;
	}	
	
	@Transactional
	public Integer createGroupHierarchyTable() {
		//
		String sql = "CREATE TABLE IF NOT EXISTS group_hierarchy("
				   + "name				varchar(128) NOT NULL, "
				   + "category		int DEFAULT NULL, "
				   + "depth			int DEFAULT NULL, "
				   + "left				int DEFAULT NULL, "
				   + "right			int DEFAULT NULL, "
				   + "PRIMARY KEY PKEY (name)); ";	
		Integer result = entityManager.createNativeQuery(sql).executeUpdate();
	    
		return result;
	}	
	
	@Transactional
	public Integer createGroupAndThingTable() {
		//
		String sql = "CREATE TABLE IF NOT EXISTS group_and_thing("
				   + "group_name		varchar(128) NOT NULL, "
				   + "thing_name		varchar(128) NOT NULL, "
				   + "PRIMARY KEY (group_name, thing_name)" + 
					");	";
		Integer result = entityManager.createNativeQuery(sql).executeUpdate();
	    
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<GroupDto> selectGroups(String groupTypeName, String attributeName, String attributeValue) {
		//
		String sql = "SELECT * FROM group";
		String sqlWhere = " WHERE ";
		String sqlWhereGroupTypeName = "group_type_name = '{groupTypeName}'";
		String sqlWhereAnd = " AND ";
		String sqlWhereAttributes = "JSON_UNQUOTE(JSON_EXTRACT(group.attributes, '$.{attributeName}')) = '{attributeValue}'";
		String sqlWhereEnd = ";";
		
		String tmpSql = sql;
		if (groupTypeName != null && !groupTypeName.isEmpty()) {
			tmpSql = tmpSql + sqlWhere + sqlWhereGroupTypeName.replace("{groupTypeName}", groupTypeName);
			
			if ( (attributeName != null && !attributeName.isEmpty()) && (attributeValue != null && !attributeValue.isEmpty()) ) {
				sqlWhereAttributes = sqlWhereAttributes.replace("{attributeName}", attributeName);
				sqlWhereAttributes = sqlWhereAttributes.replace("{attributeValue}", attributeValue);
				
				tmpSql += sqlWhereAnd + sqlWhereAttributes;
			}
		}
		else {
			if ( (attributeName != null && !attributeName.isEmpty()) && (attributeValue != null && !attributeValue.isEmpty()) ) {
				sqlWhereAttributes = sqlWhereAttributes.replace("{attributeName}", attributeName);
				sqlWhereAttributes = sqlWhereAttributes.replace("{attributeValue}", attributeValue);
				
				tmpSql += sqlWhere + sqlWhereAttributes;
			}
		}
		tmpSql += sqlWhereEnd;
		
		Query query = entityManager.createNativeQuery(tmpSql, "GroupMapping");
		
		List<GroupDto> groupDtos = query.getResultList();
		return groupDtos;
	}
}