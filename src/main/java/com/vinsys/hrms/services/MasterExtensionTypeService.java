package com.vinsys.hrms.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSMasterExtensionTypeDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOMasterExtensionType;
import com.vinsys.hrms.entity.MasterExtensionType;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/masterExtensionType")

//@PropertySource(value="${HRMSCONFIG}")
public class MasterExtensionTypeService {

	private static final Logger logger = LoggerFactory.getLogger(MasterExtensionTypeService.class);

	@Autowired
	IHRMSMasterExtensionTypeDAO masterExtensionTypeDAO;

	@RequestMapping(value = "/findAllByOrgId/{orgId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String findAllMasterExtensionType(@PathVariable("orgId") long orgId) {
		logger.info("Finding All Master Extension Type For a organization ");
		List<MasterExtensionType> masterExtensionTypeList = masterExtensionTypeDAO.findByOrganizationId(orgId,
				IHRMSConstants.isActive);
		try {
			List<Object> list = new ArrayList<Object>();

			if (!HRMSHelper.isNullOrEmpty(masterExtensionTypeList)) {
				for (MasterExtensionType extensionType : masterExtensionTypeList) {
					VOMasterExtensionType masterExtensionTypeModel = HRMSEntityToModelMapper
							.convertToMasterEmployeeExtensionTypeModel(extensionType);
					list.add(masterExtensionTypeModel);
				}
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
			}

			HRMSListResponseObject listResponse = new HRMSListResponseObject();
			listResponse.setResponseCode(IHRMSConstants.successCode);
			listResponse.setResponseMessage(IHRMSConstants.successMessage);
			listResponse.setListResponse(list);

			return HRMSHelper.createJsonString(listResponse);
		} catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
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
}
