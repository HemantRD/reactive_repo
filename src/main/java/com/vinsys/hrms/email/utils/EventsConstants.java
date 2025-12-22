package com.vinsys.hrms.email.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface EventsConstants {

	String PENDING = "PENDING";
	String IS_ACTIVE = "Y";
	String PLACEHOLDER_NOT_FOUND = "PlaceHolder not found.";
	String SUCCESS = "SUCCESS";
	String FAILED = "FAILED";
	
	enum EMAIL_CATEGORY {
		ACTION_ALERTS
	}
	
	public static boolean checkifNull(Object object) {
		return object != null;
	}

	public static boolean checkStringifNullOrEmpty(String value) {
		return value != null && !value.trim().isEmpty();
	}

	/**
	 * Dynamically replaces the values provided in map as key with its values
	 * 
	 * @param Map<String,String>,String
	 * @return String
	 * @throws Exception
	 */
	public static String replaceString(Map<String, String> replacmentMap, String string) throws Exception {
		boolean template = false;
		if (checkifNull(replacmentMap) && checkStringifNullOrEmpty(string)) {
			Pattern p = Pattern.compile("\\{([^}]*?)\\}");
			Matcher m = p.matcher(string);
			while (m.find()) {
				String keyFromString = m.group(1);
				template = false;
				for (String key : replacmentMap.keySet()) {
					if (key.contains(keyFromString)) {
						template = true;
					}
				}
				if (!template) {
					throw new Exception(PLACEHOLDER_NOT_FOUND + "===" + keyFromString);
				}
			}
			for (String key : replacmentMap.keySet()) {
				if (checkifNull(replacmentMap.get(key))) {
					string = string.replace(key, replacmentMap.get(key));
				} else {
					string = string.replace(key, " ");
				}
			}
		}
		return string;
	}
}
