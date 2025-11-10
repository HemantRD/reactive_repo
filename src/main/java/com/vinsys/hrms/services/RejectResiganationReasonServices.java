package com.vinsys.hrms.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSRejectResiganationReasonDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VORejectResiganationReason;
import com.vinsys.hrms.entity.MasterRejectResiganationReason;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping(path = "/rejectResiganationReason")

public class RejectResiganationReasonServices {

	@Autowired
	IHRMSRejectResiganationReasonDAO rejectResiganationReasonDAO;

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String findAllRejectResiganationReason() {
		try {

			List<MasterRejectResiganationReason> masterRejectResiganationReasonList = rejectResiganationReasonDAO
					.findByIsActive(IHRMSConstants.isActive);

			HRMSListResponseObject response = new HRMSListResponseObject();
			List<Object> objectList = new ArrayList<Object>();

			for (MasterRejectResiganationReason masterRejectResiganationReasonEntity : masterRejectResiganationReasonList) {
				VORejectResiganationReason voRejectResiganationReason = HRMSEntityToModelMapper
						.convertToMstRejectResiganationReasonVO(masterRejectResiganationReasonEntity);
				objectList.add(voRejectResiganationReason);
			}
			response.setListResponse(objectList);
			response.setResponseCode(IHRMSConstants.successCode);
			response.setResponseMessage(IHRMSConstants.successMessage);
			return HRMSHelper.createJsonString(response);
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
