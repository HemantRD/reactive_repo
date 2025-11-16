package com.vinsys.hrms.services.traveldesk;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.traveldesk.IHRMSMasterModeOfTravelDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.traveldesk.VOMasterModeOfTravel;
import com.vinsys.hrms.entity.traveldesk.MasterModeOfTravel;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;
@Hidden @RestController
@RequestMapping(path = "/masterTravelMode")

//@PropertySource(value="${HRMSCONFIG}")
public class MasterTravellingModeService {

	@Autowired
	IHRMSMasterModeOfTravelDAO travelModeDAO;

	@RequestMapping(value = "/findAll/{orgId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String findAll(@PathVariable("orgId") long orgId) {
		try {
			if (!HRMSHelper.isLongZero(orgId)) {
				HRMSListResponseObject response = new HRMSListResponseObject();
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(IHRMSConstants.successMessage);

				List<Object> list = new ArrayList<Object>();

				List<MasterModeOfTravel> travelModeList = travelModeDAO.getAllTravelModeOrgDivWiseDAO(orgId,
						IHRMSConstants.isActive);
				for (MasterModeOfTravel mode : travelModeList) {
					VOMasterModeOfTravel vomstMode=new VOMasterModeOfTravel();
					vomstMode=HRMSEntityToModelMapper.convertToTravellingModeModel(mode);
					list.add(vomstMode);
				}
				
				
				
				
				response.setListResponse(list);
				return HRMSHelper.createJsonString(response);

			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		return null;
	}
}