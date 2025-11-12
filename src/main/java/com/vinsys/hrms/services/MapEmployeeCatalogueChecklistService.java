package com.vinsys.hrms.services;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSMapCatalogueChecklistItemDAO;
import com.vinsys.hrms.dao.IHRMSMapEmployeeCatalogueChecklistDAO;
import com.vinsys.hrms.dao.IHRMSMapEmployeeCatalogueMappingDAO;
import com.vinsys.hrms.dao.IHRMSMapOrgDivHrDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOMapEmployeeCatalogueChecklist;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.MapEmployeeCatalogue;
import com.vinsys.hrms.entity.MapEmployeeCatalogueChecklist;
import com.vinsys.hrms.entity.OrgDivWiseHRMapping;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.util.EmailSender;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSFileuploadUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.IHRMSEmailTemplateConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping(path = "/employeecataloguechecklist")

public class MapEmployeeCatalogueChecklistService {
	@Autowired
	IHRMSMapEmployeeCatalogueMappingDAO empCatalogueMapDAO;
	@Value("${base.webdav.url}")
	private String baseURL;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSMapOrgDivHrDAO orgDivHRDAO;
	@Autowired
	EmailSender emailsender;
	Logger logger = LoggerFactory.getLogger(MapEmployeeCatalogueChecklistService.class);

	@Autowired
	IHRMSMapCatalogueChecklistItemDAO catalogueChecklistDAO;
	@Autowired
	IHRMSMapEmployeeCatalogueChecklistDAO employeeCatalogueChecklistDAO;
	@Autowired
	IHRMSMapEmployeeCatalogueMappingDAO employeeCatalogueDAO;

	@Value("${rootDirectory}")
	private String rootDirectory;

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, produces = "application/json", consumes = "application/json")
	@ResponseBody
	public String submitEmployeeCatalogueChecklist(
			@RequestBody List<VOMapEmployeeCatalogueChecklist> submitedChecklist) {
		try {
			int orginalChecklistCount = 0;
			int submitedChecklistCount = 0;
			if (submitedChecklist != null) {
				for (VOMapEmployeeCatalogueChecklist empChecklist : submitedChecklist) {

					if (orginalChecklistCount == 0) {
						logger.info(" Getting Count ");
						orginalChecklistCount = catalogueChecklistDAO.countByCatalogueIdAndOrgId(
								empChecklist.getEmployeeCatalogueMapping().getCatalogue().getId(),
								SecurityFilter.TL_CLAIMS.get().getOrgId());
					}
					if (empChecklist.isHaveCollected()) {
						submitedChecklistCount++;
					}
				}

				if (orginalChecklistCount == submitedChecklistCount) {

					logger.info(" All Items Submited ");
					HRMSListResponseObject mainResponse = new HRMSListResponseObject();
					List<Object> list = new ArrayList<Object>();
					MapEmployeeCatalogue employeeCatalogueMappingEntity = null;

					for (VOMapEmployeeCatalogueChecklist model : submitedChecklist) {

						MapEmployeeCatalogueChecklist entity = employeeCatalogueChecklistDAO.findById(model.getId())
								.get();
						if (employeeCatalogueMappingEntity == null) {
							employeeCatalogueMappingEntity = employeeCatalogueDAO
									.findById(model.getEmployeeCatalogueMapping().getId()).get();
						}

						employeeCatalogueMappingEntity.setStatus(IHRMSConstants.EMPLOYEE_CATALOGUE_CHECKLIST_SUBMITTED);
						employeeCatalogueMappingEntity.setActedOn(new Date());
						if (entity != null) {
							entity = HRMSRequestTranslator.convertToEmployeeCatalogueChecklistEntity(model, entity);
							entity = employeeCatalogueChecklistDAO.save(entity);
							model = HRMSEntityToModelMapper.convertToEmployeeChecklistMapping(entity);
							list.add(model);
						}

					}
					if (employeeCatalogueMappingEntity != null) {
						employeeCatalogueDAO.save(employeeCatalogueMappingEntity);

					}
					/**
					 * Use to check all checklist are submitted or not if yes then send a mail to HR
					 * 
					 */
					long empId = employeeCatalogueMappingEntity.getResignedEmployee().getId();
					long count = 0;
					List<MapEmployeeCatalogue> mapemployeeCatalogueList = employeeCatalogueDAO.findByEmployeeId(empId);
					logger.info(" employee catalogue size===> " + mapemployeeCatalogueList.size());
					for (MapEmployeeCatalogue employeecatalogueEntity : mapemployeeCatalogueList) {
						if (employeecatalogueEntity.getStatus()
								.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_CATALOGUE_CHECKLIST_SUBMITTED)) {
							count++;
							if (count == mapemployeeCatalogueList.size())
								sendMailtoHR(employeecatalogueEntity.getResignedEmployee());
						}
					}

					/**
					 * End
					 */

					mainResponse.setListResponse(list);
					mainResponse.setResponseCode(IHRMSConstants.successCode);
					mainResponse.setResponseMessage(IHRMSConstants.successMessage);

					return HRMSHelper.createJsonString(mainResponse);
				} else {
					throw new HRMSException(IHRMSConstants.ALL_CATLOGUE_CHECKLIST_NOT_SUBMITED_CODE,
							IHRMSConstants.ALL_CATALOGUE_CHECKLIST_NOT_SUBMITED_MESSAGE);
				}

			} else {
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

	/**
	 * This metod is use to send Mail after all status of catalogue is completed
	 * 
	 * @param employee
	 */
	private void sendMailtoHR(Employee employeeEntity) {
		String hrEmailIds = "";
		List<OrgDivWiseHRMapping> orgDivHRList = orgDivHRDAO.findOrgDivWiseHrMapping(
				employeeEntity.getCandidate().getLoginEntity().getOrganization().getId(),
				employeeEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				IHRMSConstants.isActive);
		for (OrgDivWiseHRMapping orgDivHRMApEntity : orgDivHRList) {
			hrEmailIds = hrEmailIds + orgDivHRMApEntity.getEmployee().getOfficialEmailId() + ";";
		}
		Map<String, String> placeHolderMapping = HRMSRequestTranslator
				.createPlaceHolderMapAfterExitFeedbackFormSubmitted(employeeEntity);
		placeHolderMapping.put("{websiteURL}", baseURL);

		String mailContent = HRMSHelper.replaceString(placeHolderMapping,
				IHRMSEmailTemplateConstants.Template_FOR_ALL_APPROVE_HANDOVERCHECKLIST_SUBMITTED);

		String mailSubject = IHRMSConstants.MailSubject_ALL_HANDOVER_CHECKLIST_SUBMITTED;

		emailsender.toPersistEmail(hrEmailIds, null, mailContent, mailSubject,
				employeeEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				employeeEntity.getCandidate().getLoginEntity().getOrganization().getId());

	}

	@RequestMapping(value = "upload", method = { RequestMethod.POST, RequestMethod.PUT }, produces = "application/json")
	@ResponseBody
	public String submitEmployeeCatalogueChecklist(@RequestParam("file") MultipartFile request, String catalogueId,
			String resignEmpId) throws HRMSException {
		try {
			// Sardine sardine = null;
			// sardine = SardineFactory.begin();
			if (!HRMSHelper.isNullOrEmpty(catalogueId) && !HRMSHelper.isNullOrEmpty(resignEmpId)) {
				Employee resignedemployeeEntity = employeeDAO.findById(Long.parseLong(resignEmpId)).get();
				MapEmployeeCatalogue mapemployeeCatelogueEntity = empCatalogueMapDAO.findByEmployeeIdAndCatalogue(
						Long.parseLong(resignEmpId), Long.parseLong(catalogueId), IHRMSConstants.isActive);

				String savePath = HRMSFileuploadUtil.directoryCreationForSeparationusingEmployeeId(rootDirectory,
						resignedemployeeEntity);
				/*
				 * if
				 * (!sardine.exists(savePath.concat("/"+IHRMSConstants.SEPARATIONFOLDERNAME))) {
				 * sardine.createDirectory(savePath.concat("/"+IHRMSConstants.
				 * SEPARATIONFOLDERNAME)); }
				 */
				List<Object> lst = new ArrayList<>();

				if (!HRMSHelper.isNullOrEmpty(request)) {

					logger.info("====  path of Separation  ====" + savePath);
					// String str = savePath.concat("/"+IHRMSConstants.SEPARATIONFOLDERNAME) + "/" +
					// request.getOriginalFilename().replaceAll("\\s+", "_");
					String str = savePath;
					byte[] bytes = request.getBytes();
					Files.write(Paths.get(str, request.getOriginalFilename().replaceAll("\\s+", "_")),
							request.getBytes());
					logger.info("str :{} ", str);
					// sardine.put(str, bytes);
					mapemployeeCatelogueEntity.setCatalogueProof(request.getOriginalFilename().replaceAll("\\s+", "_"));
					empCatalogueMapDAO.save(mapemployeeCatelogueEntity);
					lst.add(str);
				}

				HRMSListResponseObject responseObject = new HRMSListResponseObject();
				responseObject.setListResponse(lst);
				responseObject.setResponseCode(IHRMSConstants.successCode);
				responseObject.setResponseMessage(IHRMSConstants.successMessage);
				return HRMSHelper.createJsonString(responseObject);
				// return
				// HRMSHelper.sendSuccessResponse(IHRMSConstants.successMessage,IHRMSConstants.successCode);
			} else
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
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
