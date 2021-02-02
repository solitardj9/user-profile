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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solitardj9.userProfile.application.core.groupManager.model.Group;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupAlreayExist;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupBadRequest;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupManagerFailure;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupNotFound;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupUnavailableForDeleteNonEmpty;
import com.solitardj9.userProfile.application.core.groupManager.model.exception.ExceptionGroupUnavailableForDeleteNonLeaf;
import com.solitardj9.userProfile.application.core.groupManager.service.GroupManager;
import com.solitardj9.userProfile.application.core.thingManager.model.Thing;
import com.solitardj9.userProfile.application.core.thingManager.service.ThingManager;
import com.solitardj9.userProfile.serviceInterface.core.thingManagerInterface.model.request.RequestCreateThing;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class GroupManagerTest {

	@Autowired
	ThingManager thingManager;
	
	@Autowired
	GroupManager groupManager;
	
	private Map<Integer, List<String>> layeredGroupMap = new ConcurrentHashMap<>();
	
	private ObjectMapper om = new ObjectMapper();
	
	@Test
	public void testAll() {
		//
		System.out.println("// Test 0 ----------------------------------------------------------------------------------------------------");
		System.out.println("All Groups = " + groupManager.getAllGroups());
		
		initGroup();
		
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
		} catch (ExceptionGroupNotFound | ExceptionGroupBadRequest | ExceptionGroupUnavailableForDeleteNonLeaf | ExceptionGroupManagerFailure | ExceptionGroupUnavailableForDeleteNonEmpty e) {
			e.printStackTrace();
		}
		
		System.out.println("// Test 6 ----------------------------------------------------------------------------------------------------");
		searchName = "3_name_7";
		try {
			System.out.println("Group[3_name_7] = " + groupManager.getGroup(searchName).toString());
			System.out.println("Delete Group[3_name_7] = " + groupManager.removeGroup(searchName));
			System.out.println("Group[3_name_7] = " + groupManager.getGroup(searchName));
		} catch (ExceptionGroupBadRequest | ExceptionGroupNotFound | ExceptionGroupUnavailableForDeleteNonLeaf | ExceptionGroupManagerFailure | ExceptionGroupUnavailableForDeleteNonEmpty e) {
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
	
	private void initGroup() {
		//
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
	}
	
	@Test
	public void testGroupAndThing() {
		//
		String thingPropetry1 = "{\n" + 
				"   \"thingTypeName\" : \"myThingType\",\n" + 
				"   \"attributePayload\" : {\n" + 
				"      \"attributes\" : {\n" + 
				"         \"key1\" : \"aaa\",\n" + 
				"         \"key2\" : \"bbb\"\n" + 
				"      },\n" + 
				"      \"merge\" : false\n" + 
				"   }\n" + 
				"}";
		
		for (int i = 0 ; i < 10 ; i++) {
			String thingName = "thing_" + i;
			
			try {
				RequestCreateThing request = om.readValue(thingPropetry1, RequestCreateThing.class);
				String attributes = om.writeValueAsString(request.getAttributePayload().getAttributes());
				Thing thing = thingManager.createThing(thingName, request.getThingTypeName(), attributes, request.getAttributePayload().getMerge());
				System.out.println("thing = " + thing.toString());
			} catch (Exception e) { e.printStackTrace(); }
		}
		
		System.out.println("// b/f Test ----------------------------------------------------------------------------------------------------");
		System.out.println("All Groups = " + groupManager.getAllGroups());
		
		initGroup();
		
		System.out.println("display groups");
		
		for (Entry<Integer, List<String>> entry : layeredGroupMap.entrySet()) {
			System.out.println("layer = " + entry.getKey() + " / size = " + entry.getValue().size());
		}
		
		String groupName1 = "3_name_6";
		String thingName0 = "thing_0";
		String thingName1 = "thing_1";
		String thingName2 = "thing_2";
		String thingName3 = "thing_3";
		String thingName4 = "thing_4";
		String thingName5 = "thing_5";
		
		String groupName2 = "3_name_8";
		String thingName6 = "thing_6";
		String thingName7 = "thing_7";
		String thingName8 = "thing_8";
		String thingName9 = "thing_9";
		
		String groupName3 = "3_name_9";
		
		try {
			System.out.println("// Test 1 ----------------------------------------------------------------------------------------------------");
			groupManager.addThingToGroup(thingName0, groupName1);
			groupManager.addThingToGroup(thingName1, groupName1);
			groupManager.addThingToGroup(thingName2, groupName1);
			groupManager.addThingToGroup(thingName3, groupName1);
			groupManager.addThingToGroup(thingName4, groupName1);
			
			groupManager.addThingToGroup(thingName5, groupName2);
			groupManager.addThingToGroup(thingName6, groupName2);
			groupManager.addThingToGroup(thingName7, groupName2);
			groupManager.addThingToGroup(thingName8, groupName2);
			groupManager.addThingToGroup(thingName9, groupName2);
			System.out.println("All GroupAndThing = " + groupManager.getAllGroupAndThingDtos());
			
			System.out.println("// Test 2 ----------------------------------------------------------------------------------------------------");
			List<String> groupsToAdd = new ArrayList<>();
			groupsToAdd.add(groupName3);
			List<String> groupsToRemove = new ArrayList<>();
			groupsToRemove.add(groupName2);
			groupManager.updateGroupsOfThing(groupsToAdd, groupsToRemove, thingName5);
			groupManager.updateGroupsOfThing(groupsToAdd, groupsToRemove, thingName6);
			System.out.println("All GroupAndThing = " + groupManager.getAllGroupAndThingDtos());
			
			System.out.println("// Test 3 ----------------------------------------------------------------------------------------------------");
			groupManager.removeThingFromGroup(groupName1, thingName4);
			System.out.println("All GroupAndThing = " + groupManager.getAllGroupAndThingDtos());
			
			System.out.println("// Test 4 ----------------------------------------------------------------------------------------------------");
			System.out.println("[2_name_2]'s Thing = " + groupManager.getThingNamesInGroup("2_name_2", false));
			
			System.out.println("// Test 5 ----------------------------------------------------------------------------------------------------");
			System.out.println("[2_name_2]'s recursive Thing = " + groupManager.getThingNamesInGroup("2_name_2", true));
			
			System.out.println("// Test 6 ----------------------------------------------------------------------------------------------------");
			System.out.println("[" + thingName6 + "]'s Group = " + groupManager.getGroupNamesOfThing(thingName6));
			
			System.out.println("// Test 7 ----------------------------------------------------------------------------------------------------");
			String groupName = "3_name_9";
			System.out.println("Group[3_name_9] = " + groupManager.getGroup(groupName).toString());
			try {
				System.out.println("Delete Group[3_name_9] = " + groupManager.removeGroup(groupName));
			} catch (ExceptionGroupBadRequest | ExceptionGroupNotFound | ExceptionGroupUnavailableForDeleteNonLeaf | ExceptionGroupManagerFailure | ExceptionGroupUnavailableForDeleteNonEmpty e) {
				e.printStackTrace();
			}
			System.out.println("Group[3_name_9] = " + groupManager.getGroup(groupName));
			
		} catch (ExceptionGroupBadRequest | ExceptionGroupNotFound | ExceptionGroupManagerFailure e) {
			e.printStackTrace();
		}
		
		
	}
}