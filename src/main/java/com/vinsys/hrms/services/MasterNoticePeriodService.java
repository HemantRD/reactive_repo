package com.vinsys.hrms.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSMasterNoticePeriod;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOMasterNoticePeriod;
import com.vinsys.hrms.entity.MasterOrg_NoticePeriod;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path="/masterNoticePeriod")
@CrossOrigin(origins="*")
public class MasterNoticePeriodService {
	
	@Autowired
	IHRMSMasterNoticePeriod noticePeriodDAO;
	
	@RequestMapping(method = RequestMethod.GET,value = "/{id}",produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String findNoticePeriodbyOrgWise(@PathVariable("id") long orgId)
	{
		try {
			if(!HRMSHelper.isNullOrEmpty(orgId))
			{
				List<MasterOrg_NoticePeriod>noticePeriodList=noticePeriodDAO.findAllMasterNoticePeriodCustomQuery(orgId);
				HRMSListResponseObject response = new HRMSListResponseObject();
				List<Object> objectList = new ArrayList<Object>();
				for (MasterOrg_NoticePeriod noticePeriodEntity : noticePeriodList) {
					VOMasterNoticePeriod voMasterNoticePeriod = HRMSEntityToModelMapper
							.convertToMstNoticePeriodVO(noticePeriodEntity);
					objectList.add(voMasterNoticePeriod);
				}
				response.setListResponse(objectList);
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(IHRMSConstants.successMessage);
				return HRMSHelper.createJsonString(response);

			}
			else
				throw new HRMSException(IHRMSConstants.InsufficientDataCode,IHRMSConstants.InsufficientDataMessage);
		}
		catch(HRMSException e)
		{
			e.printStackTrace();
			try {
			return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());
			}
			catch(Exception e1)
			{
				e1.printStackTrace();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			try {
			return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			}catch(Exception e1)
			{
				e1.printStackTrace();
			}
		}
		return null;
	}

}
