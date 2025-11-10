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

import com.vinsys.hrms.dao.assets.IHRMSMasterAssetTypeDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.assets.VOMasterAssetType;
import com.vinsys.hrms.entity.assets.MasterAssetType;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/masterAssetTypeService")

public class MasterAssetTypeService {

	public static Logger logger = LoggerFactory.getLogger(MasterAssetTypeService.class);

	@Autowired
	IHRMSMasterAssetTypeDAO masterAssetTypeDAO;

	@RequestMapping(method = RequestMethod.GET, value = "/{orgId}/{divId}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getActiveMasterAssetTypeByOrgDiv(@PathVariable("orgId") long orgId,
			@PathVariable("divId") long divId) {

		List<MasterAssetType> masterAssetTypeEntityList = new ArrayList<MasterAssetType>();
		List<Object> voMasterAssetTypeList = new ArrayList<>();
		HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
		try {
			if (!HRMSHelper.isLongZero(orgId) && !HRMSHelper.isLongZero(divId)) {
				masterAssetTypeEntityList = masterAssetTypeDAO.findAllActiveMasterAssetTypeByOrgDiv(orgId, divId,
						IHRMSConstants.isActive);
				if (!HRMSHelper.isNullOrEmpty(masterAssetTypeEntityList)) {
					for (MasterAssetType masterAssetType : masterAssetTypeEntityList) {
						VOMasterAssetType voMasterAssetType = HRMSEntityToModelMapper
								.convertToModelMasterAssetType(masterAssetType);
						voMasterAssetTypeList.add(voMasterAssetType);
					}
					hrmsListResponseObject.setListResponse(voMasterAssetTypeList);
					hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
					hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
					return HRMSHelper.createJsonString(hrmsListResponseObject);
				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
			}
		} catch (HRMSException e) {
			e.printStackTrace();
			logger.info(e.getResponseMessage());
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
