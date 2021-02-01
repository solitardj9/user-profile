package com.solitardj9.userProfile.application.core.thingManager.service.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.solitardj9.userProfile.application.core.thingManager.service.dao.dto.ThingDto;

@Repository
public class ThingNativeQueryRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public Integer createThingTable() {
		//
		String sql = "CREATE TABLE IF NOT EXISTS thing("
				   + "id					int NOT NULL AUTO_INCREMENT, "
				   + "thing_name			varchar(128) DEFAULT NULL, "
				   + "attributes			LONGTEXT DEFAULT NULL, "
				   + "thing_type_name	varchar(128) DEFAULT NULL, "
				   + "PRIMARY KEY PKEY_THING (id));";	
		Integer result = entityManager.createNativeQuery(sql).executeUpdate();
	    
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ThingDto> selectThings(String thingTypeName, String attributeName, String attributeValue) {
		//
		String sql = "SELECT * FROM thing";
		String sqlWhere = " WHERE ";
		String sqlWhereThingTypeName = "thing_type_name = '{thingTypeName}'";
		String sqlWhereAnd = " AND ";
		String sqlWhereAttributes = "JSON_UNQUOTE(JSON_EXTRACT(thing.attributes, '$.{attributeName}')) = '{attributeValue}'";
		String sqlWhereEnd = ";";
		
		String tmpSql = sql;
		if (thingTypeName != null && !thingTypeName.isEmpty()) {
			tmpSql = tmpSql + sqlWhere + sqlWhereThingTypeName.replace("{thingTypeName}", thingTypeName);
			
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
		
		Query query = entityManager.createNativeQuery(tmpSql, "ThingMapping");
		
		List<ThingDto> thingDtos = query.getResultList();
		return thingDtos;
	}
}