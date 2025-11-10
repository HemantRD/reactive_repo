package com.vinsys.hrms.security.service;

import java.util.List;

public interface IAuthorizationService {

	public boolean isAuthorized(String url, List<String> role);

	public boolean isAuthorizedFunctionName(String functionName, List<String> role);

}
