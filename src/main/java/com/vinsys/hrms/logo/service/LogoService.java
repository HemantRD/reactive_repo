package com.vinsys.hrms.logo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vinsys.hrms.exception.NoSuchKeyException;
import com.vinsys.hrms.logo.dao.ILogoDAO;
import com.vinsys.hrms.logo.entity.Logo;

@Service
public class LogoService {
	private Map<String, Logo> masterConfigMap = new HashMap<>();
	 
	@Autowired
	private ILogoDAO logoDAO;
 
	public Map<String, Logo> loadConfig() {
		List<Logo> config = logoDAO.findAll();
		masterConfigMap = new HashMap<>();
		config.forEach(eachConfig -> 
			masterConfigMap.put(eachConfig.getLabel(), eachConfig)
		);
		return masterConfigMap;
	}
 
	public Logo getConfig(String key) throws NoSuchKeyException {
		if (masterConfigMap.containsKey(key)) {
			return masterConfigMap.get(key);
		} else {
			throw new NoSuchKeyException("No Such Key Found in Cache/Master Configuration:: " + key);
		}
	}
	
	@PostConstruct
	public void loadCache() {
		loadConfig();
	}
 
}