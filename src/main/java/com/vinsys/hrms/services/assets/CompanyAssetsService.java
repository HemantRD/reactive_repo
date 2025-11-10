package com.vinsys.hrms.services.assets;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSMasterDivisionDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.dao.assets.IHRMSCompanyAssetDAO;
import com.vinsys.hrms.dao.assets.IHRMSMasterAssetTypeDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.assets.VOCompanyAsset;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.assets.CompanyAsset;
import com.vinsys.hrms.entity.assets.MasterAssetType;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping(path = "/companyAssets")

public class CompanyAssetsService {

	public static Logger logger = LoggerFactory.getLogger(CompanyAssetsService.class);

	@Autowired
	IHRMSCompanyAssetDAO companyAssetDAO;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSMasterAssetTypeDAO masterAssetTypeDAO;
	@Autowired
	IHRMSOrganizationDAO organizationDAO;
	@Autowired
	IHRMSMasterDivisionDAO divisionDAO;

	@RequestMapping(value = "/getCompanyAssetsForHRByEmployeeId/{empId}"
			+ "", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getCompanyAssetsForHRByEmployeeId(@PathVariable("empId") long empId) {

		try {
			HRMSListResponseObject response = null;
			Employee emp = employeeDAO.findById(empId).get();
			if (!HRMSHelper.isNullOrEmpty(emp)) {
				response = new HRMSListResponseObject();
				List<Object> empAssets = new ArrayList<Object>();
				List<CompanyAsset> companyAssets = companyAssetDAO.findByemployee(emp);
				if (!HRMSHelper.isNullOrEmpty(companyAssets)) {
					for (CompanyAsset companyAsset : companyAssets) {
						VOCompanyAsset voCompanyAsset = HRMSEntityToModelMapper
								.convertToModelCompanyAsset(companyAsset);
						empAssets.add(voCompanyAsset);
					}
					response.setListResponse(empAssets);
					response.setResponseCode(IHRMSConstants.successCode);
					response.setResponseMessage(IHRMSConstants.successMessage);
				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
			return HRMSHelper.createJsonString(response);
		} catch (HRMSException e) {
			e.printStackTrace();
			logger.info(e.getResponseCode() + "");
			logger.info(e.getResponseMessage() + "");
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception unknown) {
			logger.info(unknown.getMessage());
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	@RequestMapping(value = "/getCompanyAssetsByEmpId/{empId}"
			+ "", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getCompanyAssetsByEmployeeId(@PathVariable("empId") long empId) {

		try {
			HRMSListResponseObject response = null;
			Employee emp = employeeDAO.findById(empId).get();
			if (!HRMSHelper.isNullOrEmpty(emp)) {
				response = new HRMSListResponseObject();
				List<Object> empAssets = new ArrayList<Object>();
				List<CompanyAsset> companyAssets = companyAssetDAO.findByEmployeeActive(emp.getId());
				if (!HRMSHelper.isNullOrEmpty(companyAssets)) {
					for (CompanyAsset companyAsset : companyAssets) {
						VOCompanyAsset voCompanyAsset = HRMSEntityToModelMapper
								.convertToModelCompanyAsset(companyAsset);
						empAssets.add(voCompanyAsset);
					}
					response.setListResponse(empAssets);
					response.setResponseCode(IHRMSConstants.successCode);
					response.setResponseMessage(IHRMSConstants.successMessage);
				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
			return HRMSHelper.createJsonString(response);
		} catch (HRMSException e) {
			e.printStackTrace();
			logger.info(e.getResponseCode() + "");
			logger.info(e.getResponseMessage() + "");
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception unknown) {
			logger.info(unknown.getMessage());
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	@RequestMapping(value = "/addUpdateCompanyAssets", method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String addUpdateCompanyAssets(@RequestBody VOCompanyAsset voCompanyAsset) {

		// String resultMesage = "";
		MasterAssetType masterAssetType = null;
		Employee employee = null;
		CompanyAsset companyAsset = null;
		HRMSListResponseObject response = null;
		try {
			if (!HRMSHelper.isNullOrEmpty(voCompanyAsset)
					&& !HRMSHelper.isNullOrEmpty(voCompanyAsset.getMasterAssetType())
					&& !HRMSHelper.isNullOrEmpty(voCompanyAsset.getEmployee())) {
				masterAssetType = masterAssetTypeDAO.findById(voCompanyAsset.getMasterAssetType().getId()).get();
				employee = employeeDAO.findActiveEmployeeById(voCompanyAsset.getEmployee().getId(),
						IHRMSConstants.isActive);
				if (!HRMSHelper.isNullOrEmpty(masterAssetType) && !HRMSHelper.isNullOrEmpty(employee)) {
					companyAsset = companyAssetDAO.findById(voCompanyAsset.getId()).get();
					if (!HRMSHelper.isNullOrEmpty(companyAsset)) {
						// update
						companyAsset = HRMSRequestTranslator.translateToCompanyAssetEntity(companyAsset,
								voCompanyAsset);
						companyAsset.setMasterAssetType(masterAssetType);
						companyAsset.setUpdatedDate(new Date());
						companyAsset.setUpdatedBy(String.valueOf(voCompanyAsset.getLoggedInEmpId()));

						// resultMesage = IHRMSConstants.updatedsuccessMessage;
					} else {
						// insert
						companyAsset = new CompanyAsset();
						companyAsset = HRMSRequestTranslator.translateToCompanyAssetEntity(companyAsset,
								voCompanyAsset);
						companyAsset.setMasterAssetType(masterAssetType);
						companyAsset.setEmployee(employee);
						companyAsset.setCreatedBy(String.valueOf(voCompanyAsset.getLoggedInEmpId()));
						companyAsset.setCreatedDate(new Date());
						companyAsset.setIsActive(IHRMSConstants.isActive);
						// resultMesage = IHRMSConstants.addedsuccessMessage;
					}
					companyAssetDAO.save(companyAsset);
					response = new HRMSListResponseObject();
					List<Object> empAssets = new ArrayList<Object>();
					List<CompanyAsset> companyAssets = companyAssetDAO.findByemployee(employee);
					if (!HRMSHelper.isNullOrEmpty(companyAssets)) {
						for (CompanyAsset companyAsset2 : companyAssets) {
							VOCompanyAsset voCompanyAsset2 = HRMSEntityToModelMapper
									.convertToModelCompanyAsset(companyAsset2);
							empAssets.add(voCompanyAsset2);
						}
						response.setListResponse(empAssets);
						response.setResponseCode(IHRMSConstants.successCode);
						response.setResponseMessage(IHRMSConstants.successMessage);
						// return HRMSHelper.sendSuccessResponse(resultMesage,
						// IHRMSConstants.successCode);
					} else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
					}
				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
			return HRMSHelper.createJsonString(response);
		} catch (HRMSException e) {
			e.printStackTrace();
			logger.info(e.getResponseCode() + "");
			logger.info(e.getResponseMessage() + "");
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception unknown) {
			logger.info(unknown.getMessage());
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}
}
