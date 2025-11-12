package com.vinsys.hrms.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSLoginEntityTypeDAO;
import com.vinsys.hrms.dao.IHRMSMasterBranchDAO;
import com.vinsys.hrms.dao.IHRMSMasterCityDAO;
import com.vinsys.hrms.dao.IHRMSMasterDepartmentDAO;
import com.vinsys.hrms.dao.IHRMSMasterDesignationDAO;
import com.vinsys.hrms.dao.IHRMSMasterDivisionDAO;
import com.vinsys.hrms.dao.IHRMSMasterEmployementTypeDAO;
import com.vinsys.hrms.dao.IHRMSMasterOrganizationEmailConfigDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOOrganizationDetail;
import com.vinsys.hrms.entity.LoginEntityType;
import com.vinsys.hrms.entity.MasterBranch;
import com.vinsys.hrms.entity.MasterDepartment;
import com.vinsys.hrms.entity.MasterDesignation;
import com.vinsys.hrms.entity.MasterDivision;
import com.vinsys.hrms.entity.MasterEmploymentType;
import com.vinsys.hrms.entity.MasterOrganizationEmailConfig;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/organization")

public class OrganizationService {

	private static final Logger logger = LoggerFactory.getLogger(OrganizationService.class);
	public static Map <Long, MasterOrganizationEmailConfig> map = new HashMap<Long, MasterOrganizationEmailConfig>();

	@Autowired
	IHRMSMasterBranchDAO branchDAO;
	@Autowired
	IHRMSMasterDepartmentDAO departmentDAO;
	@Autowired
	IHRMSMasterDivisionDAO divisionDAO;
	@Autowired
	IHRMSMasterDesignationDAO designationDAO;
	@Autowired
	IHRMSMasterCityDAO cityDAO;
	@Autowired
	IHRMSLoginEntityTypeDAO loginEntityTypeDAO;
	@Autowired
	IHRMSMasterEmployementTypeDAO employmentTypeDAO;
	@Autowired
	IHRMSMasterOrganizationEmailConfigDAO organizationConfigDAO;

	@RequestMapping(path = "/organizationDetails/{orgId}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	public String getAllOrganizationDetails(@PathVariable("orgId") long orgId) {

		// String JsonResult = "";
		try {
			if (orgId > 0 && !HRMSHelper.isNullOrEmpty(orgId)) {
				VOOrganizationDetail organizationDetail = new VOOrganizationDetail();
				List<Object> voMasterBranchList = new ArrayList<>();
				List<Object> voMasterDepartmentList = new ArrayList<>();
				List<Object> voMasterDivisionList = new ArrayList<>();
				List<Object> voMasterDesignationList = new ArrayList<>();
				List<Object> voLoginEntityTypeList = new ArrayList<>();
				List<Object> voMasterEmploymentTypeList = new ArrayList<>();

				List<MasterBranch> branchList = branchDAO.findAllMasterBranchByOrgIdCustomQuery(orgId, IHRMSConstants.isActive);
				List<MasterDepartment> departmentList = departmentDAO.findAllMasterDepartmentByOrgIdCustomQuery(orgId,IHRMSConstants.isActive);
				List<MasterDivision> divisionList = divisionDAO.findAllMasterDivisionByOrgIdCustomQuery(orgId, IHRMSConstants.isActive);
				List<MasterDesignation> designationList = designationDAO
						.findAllMasterDesignationByOrgIdCustomQuery(orgId,IHRMSConstants.isActive);
				List<LoginEntityType> loginEntityTypeList = loginEntityTypeDAO.findByOrganizationId(orgId,
						IHRMSConstants.isActive);
				List<MasterEmploymentType> masterEmploymentTypeList = employmentTypeDAO
						.findAllMasterEmploymentTypeByOrgIdCustomQuery(orgId, IHRMSConstants.isActive);

				voMasterBranchList = HRMSResponseTranslator.transalteToMasterBranchVO(branchList, voMasterBranchList);
				voMasterDepartmentList = HRMSResponseTranslator.transalteToMasterDepartmentVO(departmentList,
						voMasterDepartmentList);
				voMasterDivisionList = HRMSResponseTranslator.transalteToMasterDivisionVO(divisionList,
						voMasterDivisionList);
				voMasterDesignationList = HRMSResponseTranslator.transalteToMasterDesignationVO(designationList,
						voMasterDesignationList);
				voLoginEntityTypeList = HRMSResponseTranslator.translateToLoginEntityTypeVO(loginEntityTypeList,
						voLoginEntityTypeList);
				voMasterEmploymentTypeList = HRMSResponseTranslator
						.translateToMasterEmploymentTypeVO(masterEmploymentTypeList, voMasterEmploymentTypeList);
				if (!HRMSHelper.isNullOrEmpty(voMasterBranchList) && !HRMSHelper.isNullOrEmpty(voMasterDepartmentList)
						&& !HRMSHelper.isNullOrEmpty(voMasterDesignationList)
						&& !HRMSHelper.isNullOrEmpty(voMasterDivisionList)
						&& !HRMSHelper.isNullOrEmpty(voLoginEntityTypeList)
						&& !HRMSHelper.isNullOrEmpty(voMasterEmploymentTypeList)) {

					organizationDetail.setBranchList(voMasterBranchList);
					organizationDetail.setDepartmentList(voMasterDepartmentList);
					organizationDetail.setDivisionList(voMasterDivisionList);
					organizationDetail.setDesignationList(voMasterDesignationList);
					organizationDetail.setLoginEntityTypeList(voLoginEntityTypeList);
					organizationDetail.setEmploymentTypeList(voMasterEmploymentTypeList);

					List<Object> list = new ArrayList<>();
					list.add(organizationDetail);

					HRMSListResponseObject responseObject = new HRMSListResponseObject();
					responseObject.setResponseCode(IHRMSConstants.successCode);
					responseObject.setResponseMessage(IHRMSConstants.successMessage);
					responseObject.setListResponse(list);

					return HRMSHelper.createJsonString(responseObject);
				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}

		} catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	public MasterOrganizationEmailConfig getOrganizationConfiguration(long organizationId) {

		if (map.get(organizationId) != null) {
			return (MasterOrganizationEmailConfig) map.get(organizationId);
		}
		List<MasterOrganizationEmailConfig> allConfig = organizationConfigDAO.findAll();
		map = new HashMap<Long, MasterOrganizationEmailConfig>();
		for (MasterOrganizationEmailConfig masterConfig : allConfig) {
			logger.info("Fetching From DataBase");
			map.put(masterConfig.getDivision().getId(), masterConfig);
		}

		return (MasterOrganizationEmailConfig) map.get(organizationId);
	}
}
