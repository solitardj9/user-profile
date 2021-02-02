package com.solitardj9.userProfile.application.core.groupManager.service.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

@Repository
public class GroupNativeQueryRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public Integer createGroupTable() {
		//
		String sql = "CREATE TABLE IF NOT EXISTS group_ ("
				   + "id						int NOT NULL AUTO_INCREMENT, "
				   + "group_name				varchar(128) DEFAULT NULL, "
				   + "attributes				LONGTEXT DEFAULT NULL, "
				   + "group_type_name			varchar(128) DEFAULT NULL, "
				   + "parent_group_name			varchar(128) DEFAULT NULL, "
				   + "root_group_name			varchar(128) DEFAULT NULL, "
				   + "PRIMARY KEY PKEY_GROUP (id),"
				   + "CONSTRAINT UQ_GROUP UNIQUE (group_name));";
		
		Integer result = entityManager.createNativeQuery(sql).executeUpdate();
	    
		return result;
	}
	
	@Transactional
	public Integer createGroupTreeTable() {
		//
		String sql = "CREATE TABLE IF NOT EXISTS group_tree ("
				   + "root_group_name			varchar(128) DEFAULT NULL, "
				   + "group_tree				LONGTEXT DEFAULT NULL, "
				   + "PRIMARY KEY PKEY_GROUP_TREE (root_group_name));";
		
		Integer result = entityManager.createNativeQuery(sql).executeUpdate();
	    
		return result;
	}
	
	@Transactional
	public Integer createGroupAndThingTable() {
		String sql = "CREATE TABLE IF NOT EXISTS group_thing_map ("
				   + "group_name			varchar(128) NOT NULL, "
				   + "thing_name			varchar(128) NOT NULL, "
				   + "PRIMARY KEY (group_name, thing_name));";
		
		Integer result = entityManager.createNativeQuery(sql).executeUpdate();
	    
		return result;
	}	
}