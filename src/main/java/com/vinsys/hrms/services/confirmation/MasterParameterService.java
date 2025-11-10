package com.vinsys.hrms.services.confirmation;

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

import com.vinsys.hrms.dao.confirmation.IHRMSMasterParameterName;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.confirmation.VOMasterEvaluationParameter;
import com.vinsys.hrms.entity.confirmation.MasterEvaluationParameter;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/masterParameter")

//@PropertySource(value="${HRMSCONFIG}")
public class MasterParameterService {

	@Autowired 
	IHRMSMasterParameterName parameterDao;
	
	Logger logger=LoggerFactory.getLogger(MasterParameterService.class);
	
	@RequestMapping(value = "/findAll/{orgId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String findAll(@PathVariable("orgId") long orgId) {
		
		try {
			if(!HRMSHelper.isLongZero(orgId) && !HRMSHelper.isNullOrEmpty(orgId)) {
				List<Object> parameterResponseList=new ArrayList<>();
				
				List<MasterEvaluationParameter> tempList=parameterDao.findAllParameterNames(IHRMSConstants.isActive, orgId);
				
				if(!HRMSHelper.isNullOrEmpty(tempList)) {
					
					for (MasterEvaluationParameter masterEvaluationParameter : tempList) {
						
						VOMasterEvaluationParameter tempObj=new VOMasterEvaluationParameter();
						tempObj.setId(masterEvaluationParameter.getId());
						tempObj.setOrganizationId(masterEvaluationParameter.getOrganization().getId());
						tempObj.setParameterName(masterEvaluationParameter.getParameterName());
						parameterResponseList.add(tempObj);
					}
					
					HRMSListResponseObject response = new HRMSListResponseObject();
					response.setResponseCode(IHRMSConstants.successCode);
					response.setResponseMessage(IHRMSConstants.successMessage);
					response.setListResponse(parameterResponseList);
					return HRMSHelper.createJsonString(response);
					
				}else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			}else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
			
			
		} catch (HRMSException e) {
			e.printStackTrace();
			logger.info("message"+e.getMessage());
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());
			} catch (Exception e1) {
				e1.printStackTrace();
				logger.info("message"+e1.getMessage());
			}
		} catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				logger.info("message"+unknown.getMessage());
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
				logger.info("message"+e1.getMessage());
			}
		}
		return null;
	}
}
