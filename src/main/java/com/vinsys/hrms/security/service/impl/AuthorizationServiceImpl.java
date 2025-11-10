package com.vinsys.hrms.security.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.security.dao.IRoleFunctionMappingDAO;
import com.vinsys.hrms.security.entity.RoleFunctionMapping;
import com.vinsys.hrms.security.service.IAuthorizationService;

@Component
public class AuthorizationServiceImpl implements IAuthorizationService {

	@Autowired
	IRoleFunctionMappingDAO roleFunctionDAO;
	Logger logger = LoggerFactory.getLogger(IAuthorizationService.class);

	@Override
	public boolean isAuthorized(String url, List<String> role) {
		logger.info("Entered is Authorized for url " + url + " And Role ::: " + role);
		List<RoleFunctionMapping> mapping = roleFunctionDAO.isAuthorizedFunction(url, role, ERecordStatus.Y.name());
		return mapping != null && !mapping.isEmpty();
	}

	@Override
	public boolean isAuthorizedFunctionName(String functionNAme, List<String> roles) {
		logger.info("Entered is Authorized for url {} And Role :::  {} ", functionNAme, roles);
		List<RoleFunctionMapping> mapping = roleFunctionDAO.isAuthorizedFunctionName(functionNAme, roles,
				ERecordStatus.Y.name());
		return mapping != null && !mapping.isEmpty();
	}
}
