package com.solitardj9.userProfile.util.jsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

public class JsonUtil {
	//
	private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
	
	private static ObjectMapper om = new ObjectMapper();
	
	public static Object readValue(String jsonString, String keyPath) {
		//
		DocumentContext dc = JsonPath.parse(jsonString);
		return dc.read(keyPath);
	}
	
	public static Object readValue(Object jsonObject, String keyPath) {
		//
		DocumentContext dc = JsonPath.parse(jsonObject);
		return dc.read(keyPath);
	}
	
	public static Object readValue(DocumentContext dc, String keyPath) {
		//
		return dc.read(keyPath);
	}
	
	public static Object readValue(Map<String, Object> map, String keyPath) {
		//
		DocumentContext dc = JsonPath.parse(map);
		return dc.read(keyPath);
	}
	
	public static String mergeJsonString(String jsonString, String updateJsonString) {
        //
		if (jsonString == null || jsonString.isEmpty()) {
            logger.debug("[JsonUtil].updateJsonString : error = jsonString is " + jsonString);
        }
		
		if (updateJsonString == null || updateJsonString.isEmpty()) {
            logger.debug("[JsonUtil].updateJsonString : error = updateJsonString is " + updateJsonString);
        }
		
		DocumentContext dc = JsonPath.parse(jsonString);
		
		List<JsonKeyPathObject> jsonPathKeyObjects = new ArrayList<>();
		extractJsonKeyPathObjectFormJsonString(updateJsonString, jsonPathKeyObjects);
		
		for (JsonKeyPathObject iter : jsonPathKeyObjects) {
			if (isExistKeyPath(dc, iter.getKeyPath())) {	// update
				updateJsonDocument(dc, iter.getKeyPath(), iter.getObject());
			}
			else { // insert
				insertJsonDocument(dc, iter.getPathList(), iter.getKey(), iter.getObject());
			}
		}
		
		return dc.jsonString();
    }
	
	public static String mergeJsonString(DocumentContext dc, String updateJsonString) {
        //
		if (dc == null) {
            logger.debug("[JsonUtil].updateJsonString : error = dc is " + dc);
        }
		
		if (updateJsonString == null || updateJsonString.isEmpty()) {
            logger.debug("[JsonUtil].updateJsonString : error = updateJsonString is " + updateJsonString);
        }
		
		List<JsonKeyPathObject> jsonPathKeyObjects = new ArrayList<>();
		extractJsonKeyPathObjectFormJsonString(updateJsonString, jsonPathKeyObjects);
		
		for (JsonKeyPathObject iter : jsonPathKeyObjects) {
			if (isExistKeyPath(dc, iter.getKeyPath())) {	// update
				updateJsonDocument(dc, iter.getKeyPath(), iter.getObject());
			}
			else { // insert
				insertJsonDocument(dc, iter.getPathList(), iter.getKey(), iter.getObject());
			}
		}
		
		return dc.jsonString();
    }
	
	public static String mergeJsonString(List<JsonKeyPathObject> jsonPathKeyObjects) {
        //
		DocumentContext dc = JsonPath.parse("{}");
		
		for (JsonKeyPathObject iter : jsonPathKeyObjects) {
			if (isExistKeyPath(dc, iter.getKeyPath())) {	// update
				updateJsonDocument(dc, iter.getKeyPath(), iter.getObject());
			}
			else { // insert
				insertJsonDocument(dc, iter.getPathList(), iter.getKey(), iter.getObject());
			}
		}
		
		return dc.jsonString();
    }
	
	@SuppressWarnings("unchecked")
	public static void extractJsonKeyPathObjectFormJsonObject(Object jsonObject, List<JsonKeyPathObject> jsonPathKeyObjects) {
		//
		if (jsonObject == null)
			return;
		
		try {
			getJsonPathKeyObject(null, om.readValue(om.writeValueAsString(jsonObject), Map.class), jsonPathKeyObjects);
		} catch (IOException e) {
			//e.printStackTrace();
			logger.error("[JsonUtil].extractJsonKeyPathObjectFormJsonObject : error = " + e.getStackTrace());
		}
	}
	
	@SuppressWarnings({ "unchecked" })
	private static void extractJsonKeyPathObjectFormJsonString(String jsonString, List<JsonKeyPathObject> jsonPathKeyObjects) {
		//
		if (jsonString == null || jsonString.isEmpty())
			return;
		
		try {
			getJsonPathKeyObject(null, om.readValue(jsonString, Map.class), jsonPathKeyObjects);
		} catch (IOException e) {
			//e.printStackTrace();
			logger.error("[JsonUtil].extractJsonKeyPathObjectFormJsonString : error = " + e.getStackTrace());
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void getJsonPathKeyObject(List<String> prevPathList, Map<String, Object> map, List<JsonKeyPathObject> jsonPathKeyObjects) {
        //
		for (Entry<String, Object> iter : map.entrySet()) {
			//
			List<String> pathList = new ArrayList<String>();
			if (prevPathList != null) {
				pathList.addAll(prevPathList);
			}
			pathList.add(iter.getKey());
			
			Object object = iter.getValue();
			if (object instanceof Map) {
				getJsonPathKeyObject(pathList, (Map<String, Object>)object, jsonPathKeyObjects);
            }
			else {
				String strPath = "";
				if (prevPathList != null) {
					for (String iter2 : prevPathList) {
						if (strPath.equals(""))
							strPath += (iter2);
						else
							strPath += ("." + iter2);
					}
				}
				
				JsonKeyPathObject tmpPath = new JsonKeyPathObject();
				tmpPath.setPath(strPath);
				if (prevPathList != null) {
					tmpPath.setPathList(new ArrayList(prevPathList));
				}
				else {
					tmpPath.setPathList(new ArrayList());
				}
				tmpPath.setKey(iter.getKey());
				tmpPath.setObject(object);
				jsonPathKeyObjects.add(tmpPath);
			}
		}
	}
	
	private static Boolean isExistKeyPath(DocumentContext dc, String keyPath) {
		//
		String jsonPath = "$." + keyPath;
		try {
			dc.read(jsonPath);
		}
		catch (PathNotFoundException e) {
			return false;
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}
	
	private static Boolean isExistPath(DocumentContext dc, String path) {
		//
		if (path.equals("$"))
			return false;
		
		try {
			dc.read(path);
		}
		catch (PathNotFoundException e) {
			return false;
		}
		catch (Exception e) {
			return false;
		}
		return true;
    }
	
	private static void updateJsonDocument(DocumentContext dc, String keyPath, Object value) {
		//
		String jsonPath = "$." + keyPath;
		try {
			dc = dc.set(jsonPath, value);
		}
		catch (Exception e) {
			logger.error("[JsonUtil].updateJsonDocument " + e.getStackTrace());
		}
	}
	
	private static void insertJsonDocument(DocumentContext dc, List<String> pathList, String key, Object value) {
        //
		String jsonPath = "$";
		try {
			for (String iter: pathList) {
				if (!isExistPath(dc, (jsonPath + "." + iter))) {
					dc = dc.put(jsonPath, iter, new HashMap<String, Object>());
				}
				jsonPath += ("." + iter);
			}
			
			if (!isExistPath(dc, (jsonPath + "." + key))) {
				if (dc.read(jsonPath) == null) {
					dc = dc.set(jsonPath, new HashMap<String, Object>());
				}
				
				dc = dc.put(jsonPath, key, value);
			}
		}
		catch (Exception e) {
			logger.error("[JsonUtil].insertJsonDocument " + e);
		}
	}
}