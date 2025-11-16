package com.vinsys.hrms.services.traveldesk;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.traveldesk.IHRMSMasterVehicleDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.entity.traveldesk.MasterVehicle;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping(path = "/masterVehicle")

public class VehicleService {

	private static final Logger logger = LoggerFactory.getLogger(VehicleService.class);
	@Autowired
	IHRMSMasterVehicleDAO vehicleDAO;

	@RequestMapping(value = "/{orgId}/{divId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getAllVehicleDetails(@PathVariable("orgId") long orgId, @PathVariable("divId") long divId) {
		try {
			List<MasterVehicle> vehicleList = null;
			if (!HRMSHelper.isLongZero(orgId) && !HRMSHelper.isLongZero(divId)) {
				vehicleList = vehicleDAO.findAllVehicleByOrgIdandDivId(orgId, divId, IHRMSConstants.isActive);
				if (!HRMSHelper.isNullOrEmpty(vehicleList)) {

					HRMSListResponseObject response = new HRMSListResponseObject();
					List<Object> listResponse = new ArrayList<>();

					for (MasterVehicle vehicleEntity : vehicleList) {
						listResponse.add(HRMSResponseTranslator.translateToMasterVehicleResponse(vehicleEntity));
					}
					logger.info("list Count ==== >> " + listResponse.size());
					response.setListResponse(listResponse);
					response.setResponseCode(IHRMSConstants.successCode);
					response.setResponseMessage(IHRMSConstants.successMessage);
					response.setTotalCount(vehicleList.size());
					return HRMSHelper.createJsonString(response);

				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else {
				throw new HRMSException(IHRMSConstants.InValidDetailsCode, IHRMSConstants.InvalidDetailsMessage);
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
