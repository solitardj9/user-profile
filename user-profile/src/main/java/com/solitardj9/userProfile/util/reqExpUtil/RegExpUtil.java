package com.solitardj9.userProfile.util.reqExpUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegExpUtil {

	private static final Logger logger = LoggerFactory.getLogger(RegExpUtil.class);
	
	public static Boolean isValidExpression(String string, String regExp, Boolean nullable) {
		//
		if (nullable) {
			if (string == null || string.isEmpty())
				return true;
		}
		else {
			if (string == null || string.isEmpty())
				return false;
		}
		
		try {
			if (!string.matches(regExp)) {
				return false;
			}
		} catch (Exception e) {
			logger.error("[RegExpUtil].isValidExpression : error = " + e);
			return false;
		}

		return true;
	}
}