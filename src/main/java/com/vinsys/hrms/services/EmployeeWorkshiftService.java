package com.vinsys.hrms.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.dao.IHRMSWorkshiftDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOMasterWorkshift;
import com.vinsys.hrms.entity.MasterWorkshift;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/employeeWorkshift")

public class EmployeeWorkshiftService {

	@Autowired
	IHRMSWorkshiftDAO workshiftDAO;
	@Autowired
	IHRMSOrganizationDAO organizationDAO;

	@RequestMapping(method = RequestMethod.GET, value = "/{orgId}", produces = "application/json")
	@ResponseBody
	public String getWorkshiftOrganizationWise(@PathVariable("orgId") long organizationId) {
		try {
			Organization organization = organizationDAO.findById(organizationId).get();
			List<Object> list = new ArrayList<Object>();

			if (organization != null) {

				List<MasterWorkshift> workshifts = workshiftDAO.findByOrgId(organizationId);

				for (MasterWorkshift workshiftEntity : workshifts) {
					VOMasterWorkshift model = HRMSEntityToModelMapper.convertToWorkshiftModel(workshiftEntity);
					list.add(model);
				}
				HRMSListResponseObject response = new HRMSListResponseObject();
				response.setListResponse(list);
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(IHRMSConstants.successMessage);

				return HRMSHelper.createJsonString(response);

			} else {
				throw new HRMSException(IHRMSConstants.OrgNotRegisteredCode, IHRMSConstants.OrgNotRegisteredMessage);
			}

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