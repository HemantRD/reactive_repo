package com.vinsys.hrms.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSOrganizationHolidayDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOHolidayRequest;
import com.vinsys.hrms.entity.OrganizationHoliday;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;



@Hidden @RestController
@RequestMapping(path = "/holidayList")

public class HolidayListService {
	
	@Autowired
	IHRMSOrganizationHolidayDAO holidayDAO;
	
	@RequestMapping(method =  RequestMethod.POST, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getHolidayList(@RequestBody VOHolidayRequest voHolidayListRequest) {

		List<Object>voHolidayList=new ArrayList<>();
		HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
		try {
			
			if(!HRMSHelper.isNullOrEmpty(voHolidayListRequest) && !HRMSHelper.isLongZero(voHolidayListRequest.getBranchId()) && !HRMSHelper.isLongZero(voHolidayListRequest.getOrgId())	
					&& !HRMSHelper.isLongZero(voHolidayListRequest.getDivId()) && !HRMSHelper.isLongZero(voHolidayListRequest.getYear()) ) {
			List<OrganizationHoliday> holidayEntityList=holidayDAO.getHolidayListByOrgBranchDivIdYear(voHolidayListRequest.getOrgId(), voHolidayListRequest.getDivId(), voHolidayListRequest.getBranchId(), voHolidayListRequest.getYear());
			
			if (!HRMSHelper.isNullOrEmpty(holidayEntityList)) {
				voHolidayList=HRMSResponseTranslator.transalteToHolidayListVO(holidayEntityList,voHolidayList);
				hrmsListResponseObject.setListResponse((List<Object>) voHolidayList);
				hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
				hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
				hrmsListResponseObject.setTotalCount(holidayEntityList.size());
				return HRMSHelper.createJsonString(hrmsListResponseObject);
			}
			else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
			}
			}else
			{
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
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
