package com.solitardj9.userProfile.util.jsonUtil;

import java.util.ArrayList;
import java.util.List;

public class JsonKeyPathObject {
	//
	private String path;
	
	private List<String> pathList = new ArrayList<>();
	
	private String key;
	
	private Object object;
    
	public JsonKeyPathObject() {
		
    }
	
	public JsonKeyPathObject(String path, List<String> pathList, String key, Object object) {
		this.path = path;
		this.pathList = pathList;
		this.key = key;
		this.object = object;
    }
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	public List<String> getPathList() {
		return pathList;
	}

	public void setPathList(List<String> pathList) {
		this.pathList = pathList;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getKeyPath() {
		if (getPath() == null || getPath().isEmpty())
			return getKey();
		return getPath() + "." + getKey();
	}
	
	@Override
	public String toString() {
		return "JsonKeyPathObject [path=" + path + ", pathList=" + pathList + ", key=" + key + ", object=" + object + "]";
	}
}