package com.vinsys.hrms.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.vinsys.hrms.dao.IMasterConfigDAO;
import com.vinsys.hrms.entity.MasterConfig;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.ResponseCode;

@Service
public class MasterConfigService {
	
	private Map<String, MasterConfig> masterConfigMap = new HashMap<>();

	@Autowired
	IMasterConfigDAO masterConfigDAO;
	
	@Cacheable("master-config")
	public Map<String, MasterConfig> loadConfig() {
		List<MasterConfig> config = masterConfigDAO.findAll();
		masterConfigMap = new HashMap<>();
		config.forEach((eachConfig) -> {
			masterConfigMap.put(eachConfig.getKey(), eachConfig);
		});
		return masterConfigMap;
	}

	public MasterConfig getConfig(String key) throws HRMSException {
		if (masterConfigMap.containsKey(key)) {
			return masterConfigMap.get(key);
		} else {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201)); 
		//	throw new NoSuchKeyException("No Such Key Found in Cache/Master Configuration");
		}
	}
	
	@PostConstruct
	void init() {
		loadConfig();
	}

}
