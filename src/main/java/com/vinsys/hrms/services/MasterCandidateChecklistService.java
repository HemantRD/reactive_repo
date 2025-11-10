package com.vinsys.hrms.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSCandidateChecklistDAO;
import com.vinsys.hrms.dao.IHRMSMasterCandidateChecklistDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOCandidateChecklist;
import com.vinsys.hrms.datamodel.VOMasterCandidateChecklist;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateChecklist;
import com.vinsys.hrms.entity.MasterCandidateChecklist;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/masterCandidateChecklist")


public class MasterCandidateChecklistService {

	private static final Logger logger = LoggerFactory.getLogger(MasterCandidateChecklistService.class);

	@Autowired
	IHRMSMasterCandidateChecklistDAO masterCandidateChecklistDAO;

	/********************** added by akanksha **********************/
	@Autowired
	IHRMSCandidateChecklistDAO checklistDAO;
	@Value("${base.url}")
	private String baseURL;

	@Value("${rootDirectory}")
	private String rootDirectory;

	/*********************** END ************/

	@RequestMapping(value = "/findMasterCandidateChecklist/{orgId}"
			+ "", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String findMasterCandidateChecklistByOrgId(@PathVariable("orgId") long orgId) {
		try {
			if (!HRMSHelper.isLongZero(orgId)) {
				List<MasterCandidateChecklist> masterCandidateChecklist = masterCandidateChecklistDAO
						.getCandidateChecklistByOrgId(orgId, IHRMSConstants.isActive);
				List<Object> voMasterCandidateChecklistList = new ArrayList<Object>();
				for (MasterCandidateChecklist masterCandidateChecklist2 : masterCandidateChecklist) {
					VOMasterCandidateChecklist voMasterCandChecklist = HRMSEntityToModelMapper
							.convertToModelMasterCandidateChecklist(masterCandidateChecklist2);
					voMasterCandidateChecklistList.add(voMasterCandChecklist);
				}
				if (!HRMSHelper.isNullOrEmpty(voMasterCandidateChecklistList)) {
					HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
					hrmsListResponseObject.setListResponse(voMasterCandidateChecklistList);
					hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
					hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
					return HRMSHelper.createJsonString(hrmsListResponseObject);
				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else { // if org is zero
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
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

//	@RequestMapping(value = "/findMasterCandidateChecklist/{orgId}/{divId}/{canId}"
//			+ "", method = RequestMethod.GET, produces = "application/json")
//	@ResponseBody
//	public String findMasterCandidateChecklistByOrgIdAndDivId(@PathVariable("orgId") long orgId,
//			@PathVariable("divId") long divId,@PathVariable("canId") long canId) {
//		try {
//			if (!HRMSHelper.isLongZero(orgId) && !HRMSHelper.isLongZero(divId) && !HRMSHelper.isLongZero(canId)) {
//				List<MasterCandidateChecklist> masterCandidateChecklist = masterCandidateChecklistDAO
//						.getCandidateChecklistByOrgIdAndDivId(orgId, IHRMSConstants.isActive, divId, canId);
//				List<Object> voMasterCandidateChecklistList = new ArrayList<Object>();
//				  int index = masterCandidateChecklist.size() - 1;
//				    masterCandidateChecklist.remove(index);
//				for (MasterCandidateChecklist masterCandidateChecklist2 : masterCandidateChecklist) {
//					VOMasterCandidateChecklist voMasterCandChecklist = HRMSEntityToModelMapper
//							.convertToModelMasterCandidateChecklist(masterCandidateChecklist2);
//					voMasterCandidateChecklistList.add(voMasterCandChecklist);
//				}
//				if (!HRMSHelper.isNullOrEmpty(voMasterCandidateChecklistList)) {
//					HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
//					hrmsListResponseObject.setListResponse(voMasterCandidateChecklistList);
//					hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
//					hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
//					return HRMSHelper.createJsonString(hrmsListResponseObject);
//				} else {
//					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
//				}
//			} else { // if org is zero
//				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
//			}
//		} catch (HRMSException e) {
//			e.printStackTrace();
//			logger.info(e.getResponseCode());
//			logger.info(e.getResponseMessage());
//			try {
//				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());
//
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			}
//		} catch (Exception unknown) {
//			logger.info(unknown.getMessage());
//			try {
//				unknown.printStackTrace();
//				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			}
//		}
//		return null;
//	}

	/*
	 * @RequestMapping(value =
	 * "/findMasterCandidateChecklist/{orgId}/{divId}/{canId}" + "", method =
	 * RequestMethod.GET, produces = "application/json")
	 * 
	 * @ResponseBody public String
	 * findMasterCandidateChecklistByOrgIdAndDivId(@PathVariable("orgId") long
	 * orgId,
	 * 
	 * @PathVariable("divId") long divId,@PathVariable("canId") long canId) { try {
	 * if (!HRMSHelper.isLongZero(orgId) && !HRMSHelper.isLongZero(divId) &&
	 * !HRMSHelper.isLongZero(canId)) { List<MasterCandidateChecklist>
	 * masterCandidateChecklist = masterCandidateChecklistDAO
	 * .getCandidateChecklistByOrgIdAndDivId(orgId, IHRMSConstants.isActive, divId,
	 * canId); List<Object> voMasterCandidateChecklistList = new
	 * ArrayList<Object>(); int index = masterCandidateChecklist.size() - 1;
	 * masterCandidateChecklist.remove(index);
	 * 
	 * for (MasterCandidateChecklist masterCandidateChecklist2 :
	 * masterCandidateChecklist) { VOMasterCandidateChecklist voMasterCandChecklist
	 * = HRMSEntityToModelMapper
	 * .convertToModelMasterCandidateChecklist(masterCandidateChecklist2);
	 * voMasterCandidateChecklistList.add(voMasterCandChecklist); } if
	 * (!HRMSHelper.isNullOrEmpty(voMasterCandidateChecklistList)) {
	 * HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
	 * hrmsListResponseObject.setListResponse(voMasterCandidateChecklistList);
	 * hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
	 * hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
	 * return HRMSHelper.createJsonString(hrmsListResponseObject); } else { throw
	 * new HRMSException(IHRMSConstants.DataNotFoundCode,
	 * IHRMSConstants.DataNotFoundMessage); } } else { // if org is zero throw new
	 * HRMSException(IHRMSConstants.InsufficientDataCode,
	 * IHRMSConstants.InsufficientDataMessage); } } catch (HRMSException e) {
	 * e.printStackTrace(); logger.info(e.getResponseCode());
	 * logger.info(e.getResponseMessage()); try { return
	 * HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());
	 * 
	 * } catch (Exception e1) { e1.printStackTrace(); } } catch (Exception unknown)
	 * { logger.info(unknown.getMessage()); try { unknown.printStackTrace(); return
	 * HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage,
	 * IHRMSConstants.UnknowErrorCode); } catch (Exception e1) {
	 * e1.printStackTrace(); } } return null; }
	 */

	@RequestMapping(value = "/findMasterCandidateChecklist/{orgId}/{divId}/{canId}"
			+ "", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String findMasterCandidateChecklistByOrgIdAndDivId(@PathVariable("orgId") long orgId,
			@PathVariable("divId") long divId, @PathVariable("canId") long canId) {
		try {
			if (!HRMSHelper.isLongZero(orgId) && !HRMSHelper.isLongZero(divId) && !HRMSHelper.isLongZero(canId)) {
				List<MasterCandidateChecklist> masterCandidateChecklist = masterCandidateChecklistDAO
						.getCandidateChecklistByOrgIdAndDivId(orgId, IHRMSConstants.isActive, divId, canId);
				List<Object> voMasterCandidateChecklistList = new ArrayList<Object>();
				int index = masterCandidateChecklist.size() - 1;
				masterCandidateChecklist.remove(index);
				Candidate candidate = checklistDAO.getCandidateWithChecklistDetailsForCand(canId, 1);// onkar
				for (MasterCandidateChecklist masterCandidateChecklist2 : masterCandidateChecklist) {
					VOMasterCandidateChecklist voMasterCandChecklist = HRMSEntityToModelMapper
							.convertToModelMasterCandidateChecklist(masterCandidateChecklist2);
					// code start
					List<Object> checkListModel = new ArrayList<Object>();
					if (candidate != null && candidate.getCandidateProfessionalDetail() != null) {
						Set<CandidateChecklist> checkListEntitySet = candidate.getCandidateProfessionalDetail()
								.getCandidateChecklists();

						for (CandidateChecklist checkListEntity : checkListEntitySet) {// second for loop
							VOCandidateChecklist model = HRMSEntityToModelMapper
									.convertToCandidateChecklistDetailsModel(checkListEntity);
							if ((voMasterCandChecklist.getChecklistItem()).equals(model.getChecklistItem())) {
								// checkListModel.add(model);
								String path = rootDirectory + candidate.getLoginEntity().getOrganization().getId() + "/"
										+ candidate.getCandidateProfessionalDetail().getDivision().getId() + "/"
										+ candidate.getCandidateProfessionalDetail().getBranch().getId() + "/"
										+ candidate.getId() + "/" + model.getAttachment();
								model.setUrl(path);
								voMasterCandChecklist.setvOCandidateChecklist(model);
							}

						}

					}

					// if(!HRMSHelper.isNullOrEmpty(checkListModel)) {
					// voMasterCandidateChecklistList.add(checkListModel);
					// voMasterCandChecklist.
					// }
					voMasterCandidateChecklistList.add(voMasterCandChecklist);
				}
				if (!HRMSHelper.isNullOrEmpty(voMasterCandidateChecklistList)) {
					HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
					hrmsListResponseObject.setListResponse(voMasterCandidateChecklistList);
					hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
					hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
					return HRMSHelper.createJsonString(hrmsListResponseObject);
				} else {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else { // if org is zero
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
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
