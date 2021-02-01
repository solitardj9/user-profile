package com.solitardj9.userProfile.application.core.groupManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.solitardj9.userProfile.application.core.groupManager.model.Group;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupAlreayExist;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupBadRequest;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupManagerFailure;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupNotFound;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupUnavailableForDeleteNonLeaf;
import com.solitardj9.userProfile.application.core.groupManager.service.GroupManager;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class GroupManagerTest {

	@Autowired
	GroupManager groupManager;
	
	private Map<Integer, List<String>> layeredGroupMap = new ConcurrentHashMap<>();
	
	@Test
	public void testAll() {
		//
		System.out.println("// Test 0 ----------------------------------------------------------------------------------------------------");
		System.out.println("All Groups = " + groupManager.getAllGroups());
		
		String groupTypeName = "Test";
		
		Integer depth = 4;
		Integer width = 3;
		
		List<String> groupNames;
		
		System.out.println("create groups");

		System.out.println("lv0 created");
		
		String rootGroupName = "root";
		groupNames = new ArrayList<>();
		groupNames.add(rootGroupName);
		try {
			groupManager.insertGroup(rootGroupName, null, groupTypeName, null, rootGroupName);
		} catch (ExceptionGroupAlreayExist | ExceptionGroupBadRequest | ExceptionGroupManagerFailure e) {
			e.printStackTrace();
		}
		
		layeredGroupMap.put(0, groupNames);
		
		for (int d = 1 ; d < depth ; d++) {
			//
			List<String> parentGroupNames = layeredGroupMap.get(d-1);
			groupNames = new ArrayList<>();
			
			int count = 0;
			for (String iter : parentGroupNames) {
				//
				for (int w = 0 ; w < width ; w++) {
					//String groupName = String.valueOf(d) + "_" + UUID.randomUUID().toString().substring(0, 5);
					String groupName = String.valueOf(d) + "_name_" + String.valueOf(count);
					Group group = null;
					try {
						group = groupManager.insertGroup(groupName, null, groupTypeName, iter, rootGroupName);
					} catch (ExceptionGroupAlreayExist | ExceptionGroupBadRequest | ExceptionGroupManagerFailure e) {
						e.printStackTrace();
					}
					if (group == null) {
						break;
					}
					else {
						groupNames.add(group.getGroupName());
					}
					count = count + 1;
				}
			}
			
			if (!groupNames.isEmpty()) {
				layeredGroupMap.put(d, groupNames);
			}
			
			System.out.println("lv" + d+ " created");
		}
		
		System.out.println("display groups");
		
		for (Entry<Integer, List<String>> entry : layeredGroupMap.entrySet()) {
			System.out.println("layer = " + entry.getKey() + " / size = " + entry.getValue().size());
		}
		
		String searchName;
		
		System.out.println("// Test 1 ----------------------------------------------------------------------------------------------------");
		searchName = "1_name_1";
		try {
			System.out.println("Group[1_name_1] = " + groupManager.getGroup(searchName).toString());
		} catch (ExceptionGroupBadRequest | ExceptionGroupNotFound e) {
			e.printStackTrace();
		}
		
		System.out.println("// Test 2 ----------------------------------------------------------------------------------------------------");
		System.out.println("All Groups = " + groupManager.getAllGroups());
		
		System.out.println("// Test 3 ----------------------------------------------------------------------------------------------------");
		searchName = "1_name_2";
		try {
			System.out.println("Group[1_name_2]' childs = " + groupManager.getGroupList(searchName, false));
		} catch (ExceptionGroupBadRequest | ExceptionGroupNotFound e) {
			e.printStackTrace();
		}
		
		System.out.println("// Test 4 ----------------------------------------------------------------------------------------------------");
		searchName = "1_name_2";
		try {
			System.out.println("Group[1_name_2]' recursive childs =  = " + groupManager.getGroupList(searchName, true));
		} catch (ExceptionGroupBadRequest | ExceptionGroupNotFound e) {
			e.printStackTrace();
		}
		
		System.out.println("// Test 5 ----------------------------------------------------------------------------------------------------");
		searchName = "1_name_2";
		try {
			System.out.println("Delete Group[1_name_2] = " + groupManager.removeGroup(searchName));
		} catch (ExceptionGroupNotFound | ExceptionGroupBadRequest | ExceptionGroupUnavailableForDeleteNonLeaf | ExceptionGroupManagerFailure e) {
			e.printStackTrace();
		}
		
		System.out.println("// Test 6 ----------------------------------------------------------------------------------------------------");
		searchName = "3_name_7";
		try {
			System.out.println("Group[3_name_7] = " + groupManager.getGroup(searchName).toString());
			System.out.println("Delete Group[3_name_7] = " + groupManager.removeGroup(searchName));
			System.out.println("Group[3_name_7] = " + groupManager.getGroup(searchName));
		} catch (ExceptionGroupBadRequest | ExceptionGroupNotFound | ExceptionGroupUnavailableForDeleteNonLeaf | ExceptionGroupManagerFailure e) {
			e.printStackTrace();
		}
		
		System.out.println("// Test 7 ----------------------------------------------------------------------------------------------------");
		searchName = "3_name_6";
		try {
			System.out.println("Group[3_name_6]' parents = " + groupManager.getGroupListFromRootToGroup(searchName));
		} catch (ExceptionGroupBadRequest | ExceptionGroupNotFound e) {
			e.printStackTrace();
		}
		
		System.out.println("All Groups = " + groupManager.getAllGroups());
	}
}