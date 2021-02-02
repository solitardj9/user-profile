package com.solitardj9.userProfile.application.core.groupManager.service.groupTree;

import java.io.Serializable;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LayeredGroups implements Serializable {

	private static final long serialVersionUID = -2476114759778777209L;
	
	private Map<String, LayeredGroup> layeredGroups;
}