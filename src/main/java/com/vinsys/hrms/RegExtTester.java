package com.vinsys.hrms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExtTester {

	public static void main(String[] args) {
		String url = "/webdav/1/1/1/1006/Photos/Nilesh_photo.jpg";

		String url2 = "/api/login/loginCheck";

		Pattern pattern = Pattern.compile("(?i)\\/*login\\/logincheck$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher("/api/login/loginCheck");
		boolean matchFound = matcher.find();
	}
}
