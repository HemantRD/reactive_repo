package com.vinsys.hrms.util;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.dao.IHRMSMasterResponseCodeDAO;
import com.vinsys.hrms.entity.MasterResponseCode;

/**
 * @author Onkar A
 *
 * 
 */
@Component
public class ResponseCode {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	IHRMSMasterResponseCodeDAO responseCodeDao;

	static HashMap<Integer, String> responseCodeMap = new HashMap<>();

	@PostConstruct
	public void init() {
		log.info("*********************************");
		log.info("Getting All response code for Org Id {}");
		log.info("*********************************");

		responseCodeMap = getAllResponseCode();
		log.info("Completed...");
	}

	private HashMap<Integer, String> getAllResponseCode() {
		HashMap<Integer, String> map = new HashMap<>();
		List<MasterResponseCode> responseCodes = responseCodeDao.findByIsActive(ERecordStatus.Y.name());
		for (MasterResponseCode responseCode : responseCodes) {

			map.put(responseCode.getResponseCode(), responseCode.getResposeMessage());
		}
		return map;
	}

	public static HashMap<Integer, String> getResponseCodeMap() {
		return responseCodeMap;
	}

	public static void setResponseCodeMap(HashMap<Integer, String> responseCodeMap) {
		ResponseCode.responseCodeMap = responseCodeMap;
	}
}
