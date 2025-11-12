package com.vinsys.hrms.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.master.dao.ICurrencyMasterDAO;
import com.vinsys.hrms.master.dao.IMasterAirTypeDAO;
import com.vinsys.hrms.master.dao.IMasterBusTypeDAO;
import com.vinsys.hrms.master.entity.CurrencyMaster;
import com.vinsys.hrms.master.entity.MasterAirType;
import com.vinsys.hrms.master.entity.MasterBusType;
import com.vinsys.hrms.master.vo.CurrencyMasterVO;
import com.vinsys.hrms.master.vo.MasterAirTypeVO;
import com.vinsys.hrms.master.vo.MasterBusTypeVO;

/**
 * @author Onkar A
 *
 * 
 */
@Component
public class MasterDataLoad {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	IMasterAirTypeDAO airTypeDAO;
	@Autowired
	IMasterBusTypeDAO busTypeDAO;
	@Autowired
	ICurrencyMasterDAO currencyMasterDAO;

	static List<MasterAirTypeVO> airTypes = new ArrayList<MasterAirTypeVO>();
	static List<MasterBusTypeVO> busTypes = new ArrayList<MasterBusTypeVO>();
	static List<CurrencyMasterVO> currencies = new ArrayList<CurrencyMasterVO>();

	@PostConstruct
	public void init() {
		log.info("*********************************");
		log.info("Master Data Load Start");

		airTypes = getAirTypes();
		busTypes = getBusTypes();
		currencies = getCurrencies();
		log.info("Master Data Load End");
	}

	private List<MasterBusTypeVO> getBusTypes() {

		List<MasterBusTypeVO> busTypesList = new ArrayList<MasterBusTypeVO>();
		List<MasterBusType> bustTypes = busTypeDAO.findByIsActiveOrderById(ERecordStatus.Y.name());
		for (MasterBusType busType : bustTypes) {
			MasterBusTypeVO busTypeVO = new MasterBusTypeVO();
			busTypeVO.setBusType(busType.getBusType());
			busTypeVO.setId(busType.getId());
			busTypeVO.setDescription(busType.getDescription());
			busTypesList.add(busTypeVO);
		}
		return busTypesList;

	}

	private List<MasterAirTypeVO> getAirTypes() {
		List<MasterAirTypeVO> airTypeVOList = new ArrayList<MasterAirTypeVO>();
		List<MasterAirType> airTypes = airTypeDAO.findByIsActive(ERecordStatus.Y.name());
		for (MasterAirType airType : airTypes) {
			MasterAirTypeVO airTypeVO = new MasterAirTypeVO();
			airTypeVO.setAirType(airType.getAirType());
			airTypeVO.setId(airType.getId());
			airTypeVO.setDescription(airType.getDescription());
			airTypeVOList.add(airTypeVO);
		}
		return airTypeVOList;
	}

	private List<CurrencyMasterVO> getCurrencies() {
		List<CurrencyMasterVO> currencyList = new ArrayList<CurrencyMasterVO>();
		List<CurrencyMaster> masterCurrency = currencyMasterDAO.findByIsActive(IHRMSConstants.isActive);
		for (CurrencyMaster currency : masterCurrency) {
			CurrencyMasterVO currencyMasterVO = new CurrencyMasterVO();
			currencyMasterVO.setEntityId(currency.getEntityId());
			currencyMasterVO.setCurrency(currency.getCurrency());
			currencyMasterVO.setCountryName(currency.getCountryName());
			currencyList.add(currencyMasterVO);
		}
		return currencyList;
	}

}
