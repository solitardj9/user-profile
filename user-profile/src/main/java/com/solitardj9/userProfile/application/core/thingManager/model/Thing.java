package com.solitardj9.userProfile.application.core.thingManager.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solitardj9.userProfile.util.jsonUtil.JsonUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Thing {
	
	private static final Logger logger = LoggerFactory.getLogger(Thing.class);
	
	private String thingId;

	private String thingName;
	
	private String attributes;
	
	private String thingTypeName;
	
	public void mergeAttributes(String attributes) {
		//
		try {
			this.attributes = JsonUtil.mergeJsonString(this.attributes, attributes);
		} catch (Exception e) {
			logger.error("[Thing].setAttributes : error = " + e);
		}
	}
	
	public Object findAttribute(String keyPath) {
		//
		try {
			return JsonUtil.readValue(attributes, keyPath);
		} catch (Exception e) {
			logger.error("[Thing].setAttributes : error = " + e);
			return null;
		}
	}
	
	public Integer findAttributeAsInteger(String keyPath) {
		//
		try {
			Object result = findAttribute(keyPath);
			if ((result != null) && (result instanceof Integer)) {
				return (Integer)result;
			}
			return null;
		} catch (Exception e) {
			logger.error("[Thing].findAttributeAsInteger : error = " + e);
			return null;
		}
	}
	
	public Double findAttributeAsDouble(String keyPath) {
		//
		try {
			Object result = findAttribute(keyPath);
			if ((result != null) && (result instanceof Double)) {
				return (Double)result;
			}
			return null;
		} catch (Exception e) {
			logger.error("[Thing].findAttributeAsInteger : error = " + e);
			return null;
		}
	}
	
	public Float findAttributeAsFloat(String keyPath) {
		//
		try {
			Object result = findAttribute(keyPath);
			if ((result != null) && (result instanceof Float)) {
				return (Float)result;
			}
			return null;
		} catch (Exception e) {
			logger.error("[Thing].findAttributeAsInteger : error = " + e);
			return null;
		}
	}
	
	public Long findAttributeAsLong(String keyPath) {
		//
		try {
			Object result = findAttribute(keyPath);
			if ((result != null) && (result instanceof Long)) {
				return (Long)result;
			}
			return null;
		} catch (Exception e) {
			logger.error("[Thing].findAttributeAsInteger : error = " + e);
			return null;
		}
	}
	
	public String findAttributeAsString(String keyPath) {
		//
		try {
			Object result = findAttribute(keyPath);
			if ((result != null) && (result instanceof String)) {
				return (String)result;
			}
			return null;
		} catch (Exception e) {
			logger.error("[Thing].findAttributeAsInteger : error = " + e);
			return null;
		}
	}
	
	public Boolean findAttributeAsBoolean(String keyPath) {
		//
		try {
			Object result = findAttribute(keyPath);
			if ((result != null) && (result instanceof Boolean)) {
				return (Boolean)result;
			}
			return null;
		} catch (Exception e) {
			logger.error("[Thing].findAttributeAsInteger : error = " + e);
			return null;
		}
	}
}