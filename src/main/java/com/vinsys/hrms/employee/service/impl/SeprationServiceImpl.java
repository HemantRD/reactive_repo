package com.vinsys.hrms.employee.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.dao.IHRMSCandidateChecklistDAO;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSCandidateLetterDAO;
import com.vinsys.hrms.dao.IHRMSEmailTemplateDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeFeedbackDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeReportingManager;
import com.vinsys.hrms.dao.IHRMSEmployeeSeparationDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeSeparationHistoryDAO;
import com.vinsys.hrms.dao.IHRMSFeedbackOptionDAO;
import com.vinsys.hrms.dao.IHRMSFeedbackQuestionDAO;
import com.vinsys.hrms.dao.IHRMSMapCatalogue;
import com.vinsys.hrms.dao.IHRMSMapCatalogueChecklistItemDAO;
import com.vinsys.hrms.dao.IHRMSMapEmployeeCatalogueChecklistDAO;
import com.vinsys.hrms.dao.IHRMSMapEmployeeCatalogueMappingDAO;
import com.vinsys.hrms.dao.IHRMSMapOrgDivHrDAO;
import com.vinsys.hrms.dao.IHRMSMasterCandidateChecklistDAO;
import com.vinsys.hrms.dao.IHRMSMasterCityDAO;
import com.vinsys.hrms.dao.IHRMSMasterCountryDAO;
import com.vinsys.hrms.dao.IHRMSMasterDepartmentDAO;
import com.vinsys.hrms.dao.IHRMSMasterModeofSeparationDAO;
import com.vinsys.hrms.dao.IHRMSMasterModeofSeparationReasonDAO;
import com.vinsys.hrms.dao.IHRMSMasterNoticePeriod;
import com.vinsys.hrms.dao.IHRMSMasterOrganizationEmailConfigDAO;
import com.vinsys.hrms.dao.IHRMSMasterStateDAO;
import com.vinsys.hrms.dao.IHRMSOrgDivSeparationLetterMappingDAO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOMapEmployeeCatalogueChecklist;
import com.vinsys.hrms.employee.service.ISeprationService;
import com.vinsys.hrms.employee.vo.CandidateLetterVO;
import com.vinsys.hrms.employee.vo.CheckListByApproverVO;
import com.vinsys.hrms.employee.vo.ChecklistResponseVO;
import com.vinsys.hrms.employee.vo.ChecklistStatusVO;
import com.vinsys.hrms.employee.vo.ChecklistSubmitVo;
import com.vinsys.hrms.employee.vo.ChecklistVO;
import com.vinsys.hrms.employee.vo.EmployeeExitFeedbackVO;
import com.vinsys.hrms.employee.vo.EmployeeSeparationRequestVO;
import com.vinsys.hrms.employee.vo.EmployeeSeprationDetailsResponseVO;
import com.vinsys.hrms.employee.vo.FeedbackQuestionVO;
import com.vinsys.hrms.employee.vo.SubmittedEmployeeFeedbackVO;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateLetter;
import com.vinsys.hrms.entity.EmailTemplate;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeFeedback;
import com.vinsys.hrms.entity.EmployeeReportingManager;
import com.vinsys.hrms.entity.EmployeeSeparationDetails;
import com.vinsys.hrms.entity.EmployeeSeparationDetailsWithHistory;
import com.vinsys.hrms.entity.FeedbackOption;
import com.vinsys.hrms.entity.FeedbackQuestion;
import com.vinsys.hrms.entity.MapCatalogue;
import com.vinsys.hrms.entity.MapCatalogueChecklistItem;
import com.vinsys.hrms.entity.MapEmployeeCatalogue;
import com.vinsys.hrms.entity.MapEmployeeCatalogueChecklist;
import com.vinsys.hrms.entity.MasterModeofSeparation;
import com.vinsys.hrms.entity.MasterModeofSeparationReason;
import com.vinsys.hrms.entity.MasterOrganizationEmailConfig;
import com.vinsys.hrms.entity.OrgDivWiseHRMapping;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.master.dao.IReleaseTypeMasterDAO;
import com.vinsys.hrms.master.entity.ReleaseTypeMaster;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.util.ERole;
import com.vinsys.hrms.util.EmailSender;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.IHRMSEmailTemplateConstants;
import com.vinsys.hrms.util.ResponseCode;
import com.vinsys.hrms.util.SeparationAuthorityHelper;
import com.vinsys.hrms.util.SeparationTransformUtils;

@Service
public class SeprationServiceImpl implements ISeprationService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Value("${base.url}")
	private String baseURL;
	@Autowired
	IHRMSEmployeeReportingManager reportingManagerDAO;
	@Autowired
	IHRMSMasterModeofSeparationDAO mstModeofSeparationDAO;
	@Autowired
	IHRMSMasterModeofSeparationReasonDAO mstSeparationReasonDAO;
	@Autowired
	IHRMSEmployeeSeparationDAO employeeSeparationDAO;
	@Autowired
	IHRMSEmployeeSeparationHistoryDAO employeeSeparationHistoryDAO;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSCandidateDAO candidateDAO;
	@Autowired
	IHRMSMasterNoticePeriod noticePeriodDAO;
	@Autowired
	EmailSender emailsender;
	@Autowired
	IHRMSMasterDepartmentDAO deptDAO;
	@Autowired
	IHRMSMasterOrganizationEmailConfigDAO configDAO;
	@Autowired
	IHRMSMapEmployeeCatalogueMappingDAO empCatalogueMapDAO;
	@Autowired
	IHRMSMapCatalogue mapCatalogueDAO;
	@Autowired
	IHRMSMapCatalogueChecklistItemDAO mapCatalogueChecklistDAO;
	@Autowired
	IHRMSMapEmployeeCatalogueChecklistDAO empcatalogueChecklistDAO;
	@Autowired
	EmailSender eventEmailSender;
	@Autowired
	IHRMSMapOrgDivHrDAO orgDivHRDAO;
	@Autowired
	IHRMSEmployeeFeedbackDAO employeeFeedbackDAO;
	@Autowired
	IHRMSMasterCityDAO masterCityDAO;
	@Autowired
	IHRMSMasterCountryDAO masterCountryDAO;
	@Autowired
	IHRMSMasterStateDAO masterStateDAO;
	@Autowired
	IHRMSEmployeeReportingManager reportringManagerDAO;
	@Autowired
	SeparationAuthorityHelper SeparationAuthorityHelper;
	@Autowired
	IHRMSMapCatalogue catalogueDAO;
	@Autowired
	IHRMSMapEmployeeCatalogueMappingDAO employeeCatalogueMappingDAO;
	@Autowired
	IHRMSMapEmployeeCatalogueChecklistDAO employeeChecklistMappingDAO;
	@Autowired
	IReleaseTypeMasterDAO releaseTypeMasterDAO;
	@Autowired
	IHRMSFeedbackOptionDAO optionDAO;
	@Autowired
	IHRMSFeedbackQuestionDAO questionDAO;
	@Autowired
	IHRMSMapCatalogueChecklistItemDAO catalogueChecklistDAO;
	@Autowired
	IHRMSMapEmployeeCatalogueChecklistDAO employeeCatalogueChecklistDAO;
	@Autowired
	IHRMSMapEmployeeCatalogueMappingDAO employeeCatalogueDAO;
	@Autowired
	IHRMSFeedbackQuestionDAO feedbackQuestionDAO;

	@Autowired
	IHRMSOrgDivSeparationLetterMappingDAO separationLetterListDAO;

	@Autowired
	IHRMSCandidateChecklistDAO candidateChecklistDAO;

	@Autowired
	IHRMSMasterCandidateChecklistDAO masterCandidateChecklistDAO;

	@Autowired
	IHRMSCandidateLetterDAO letterDAO;

	@Autowired
	IHRMSMasterOrganizationEmailConfigDAO emailConfigDetailDAO;

	@Autowired
	IHRMSEmployeeReportingManager employeeReportingManagerDAO;

	@Autowired
	IHRMSEmailTemplateDAO emailTemplateDAO;

	@Value("${app_version}")
	private String applicationVersion;
	@Value("${rootDirectory}")
	private String rootDirectory;

	@Override
	public HRMSBaseResponse<?> applySeparation(@RequestBody EmployeeSeparationRequestVO voEmployeeSeparationDetails)
			throws HRMSException, ParseException {

		log.info("Inside separation method");
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		SeparationAuthorityHelper.applySepareationInputValidation(voEmployeeSeparationDetails);

		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		EmployeeSeparationDetails employeeSeparationDetailsEntity;
		EmployeeSeparationDetailsWithHistory employeeSeparationDetailsEntityHistory;
		Employee employeeEntity;
		MasterModeofSeparation masterModeofSeparationEntity;
		MasterModeofSeparationReason empSeaparationReasonEntity;

		MasterModeofSeparationReason roSeparationReasonEntity = null;
		MasterModeofSeparationReason orgSeparationReasonEntity = null;
		MasterModeofSeparationReason hrSeparationReasonEntity = null;

		if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails)) {

			employeeSeparationDetailsEntity = employeeSeparationDAO
					.findSeparationDetailsUsingEmpIdandActive(IHRMSConstants.isActive, empId);
			if (!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity)
					&& (employeeSeparationDetailsEntity.getStatus()
							.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING)
							|| employeeSeparationDetailsEntity.getStatus()
									.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_COMPLETED))
					&& (employeeSeparationDetailsEntity.getRoApproverStatus() == null
							|| !employeeSeparationDetailsEntity.getRoApproverStatus()
									.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED))
					&& (employeeSeparationDetailsEntity.getOrgApproverStatus() == null
							|| !employeeSeparationDetailsEntity.getOrgApproverStatus()
									.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED))) {

				throw new HRMSException(1527, ResponseCode.getResponseCodeMap().get(1527));

			} else {
				/* insert */
				SeparationAuthorityHelper.applySepareationInputValidation(voEmployeeSeparationDetails);

				employeeSeparationDetailsEntity = new EmployeeSeparationDetails();
				employeeSeparationDetailsEntityHistory = new EmployeeSeparationDetailsWithHistory();

				masterModeofSeparationEntity = mstModeofSeparationDAO.findByModeIdAndOrgId(
						voEmployeeSeparationDetails.getModeofSeparation().getId(),
						SecurityFilter.TL_CLAIMS.get().getOrgId());

				if (HRMSHelper.isNullOrEmpty(masterModeofSeparationEntity)) {
					throw new HRMSException(1528, ResponseCode.getResponseCodeMap().get(1528));
				}

				empSeaparationReasonEntity = mstSeparationReasonDAO
						.findByReasonId(voEmployeeSeparationDetails.getSeparationReasonVO().getId());

				if (HRMSHelper.isNullOrEmpty(empSeaparationReasonEntity)) {
					throw new HRMSException(1529, ResponseCode.getResponseCodeMap().get(1529));
				}
				// noticePeriodEntity=noticePeriodDAO.findById(voEmployeeSeparationDetails.getNoticePeriod().getId());
				employeeEntity = employeeDAO.findById(empId).get();
				employeeSeparationDetailsEntity.setEmployee(employeeEntity);
				employeeSeparationDetailsEntityHistory.setEmployee(employeeEntity);
				employeeSeparationDetailsEntity.setModeofSeparation(masterModeofSeparationEntity);
				employeeSeparationDetailsEntityHistory.setModeofSeparation(masterModeofSeparationEntity);
				employeeSeparationDetailsEntity.setEmpSeparationReason(empSeaparationReasonEntity);
				employeeSeparationDetailsEntityHistory.setEmpSeparationReason(empSeaparationReasonEntity);
				employeeSeparationDetailsEntity.setOrgId(employeeEntity.getOrgId());
				Employee emp = employeeDAO.findActiveEmployeeById(Long.valueOf(empId), "Y");

				int noticePeriod = emp.getEmployeeCurrentDetail().getNoticePeriod();

				// Calculation of Relieving date

				LocalDate today = LocalDate.now();

				LocalDate relievingLocalDate = today.plus(noticePeriod, ChronoUnit.DAYS);
				Date relievingDate = Date.from(relievingLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

				employeeSeparationDetailsEntity = SeparationTransformUtils.translateToEmployeeSeparationDetails(
						employeeSeparationDetailsEntity, voEmployeeSeparationDetails, relievingDate, noticePeriod);

				employeeSeparationDetailsEntityHistory = SeparationTransformUtils
						.translateToEmployeeSeparationDetailsWithHistory(employeeSeparationDetailsEntityHistory,
								voEmployeeSeparationDetails, relievingDate, noticePeriod);
				employeeSeparationDetailsEntityHistory.setOrgId(employeeEntity.getOrgId());
				employeeSeparationDetailsEntity.setStatus(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING);
				employeeSeparationDetailsEntityHistory.setStatus(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING);
				if (HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity.getHRComment()))
					sendEmailToApprovers(employeeEntity, employeeSeparationDetailsEntity);
				else {
					addEntryInChecklistTables(voEmployeeSeparationDetails, employeeEntity);
					sendMailToempTermAbsDemisReason(employeeSeparationDetailsEntity);
					employeeSeparationDetailsEntity = setROandOrgstatusForTerAbscDemiseReason(
							employeeSeparationDetailsEntity);
					employeeSeparationDetailsEntityHistory = setROandOrgstatusForTerAbscDemiseReasonForHistory(
							employeeSeparationDetailsEntityHistory);
				}

				// }

				employeeEntity = employeeDAO.findById(empId).get();

			}
			
			employeeSeparationDetailsEntity = employeeSeparationDAO.save(employeeSeparationDetailsEntity);
			employeeSeparationDetailsEntityHistory = employeeSeparationHistoryDAO
					.save(employeeSeparationDetailsEntityHistory);
			List<Object> objectList = new ArrayList<Object>();
			objectList.add(HRMSEntityToModelMapper.convertToEmpSeparationDetailsVO(employeeSeparationDetailsEntity));
			HRMSListResponseObject res = new HRMSListResponseObject();
			res.setListResponse(objectList);
			res.setResponseCode(IHRMSConstants.successCode);
			res.setResponseMessage(IHRMSConstants.successMessage);

			// response.setResponseBody(res);
			response.setResponseCode(1200);
			response.setResponseMessage(IHRMSConstants.SEPARATIOAPPLYSUCCESSMESSAGE);
			response.setApplicationVersion(applicationVersion);

			log.info("Exit from add separation method");
			return response;
			// return HRMSHelper.createJsonString(res);

		} else
			throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);

		// return null;
	}

	private EmployeeSeparationDetailsWithHistory setROandOrgstatusForTerAbscDemiseReasonForHistory(
			EmployeeSeparationDetailsWithHistory employeeSeparationDetailsEntityHistory) {

		log.info("SET RO System escalted");
		employeeSeparationDetailsEntityHistory.setRoApproverStatus(IHRMSConstants.EMPLOYEE_SEPARATION_SYSTEM_ESCALATED);
		employeeSeparationDetailsEntityHistory.setRoActionDate(new Date());
		employeeSeparationDetailsEntityHistory
				.setOrgApproverStatus(IHRMSConstants.EMPLOYEE_SEPARATION_SYSTEM_ESCALATED);
		employeeSeparationDetailsEntityHistory.setOrgActionDate(new Date());
		return employeeSeparationDetailsEntityHistory;

	}

	public void sendEmailToApprovers(Employee empEntity, EmployeeSeparationDetails employeeSeparationDetailsEntity) {
		log.info("Employee Resign or withdraw his resignation");
		String employeeEmailId = empEntity.getOfficialEmailId();
		String mailBody = null;
		EmailTemplate template = null;
		String recipientEmailId = null;
		String ccEmailIds = null;
		String rmEmailIds = empEntity.getEmployeeReportingManager().getReporingManager().getOfficialEmailId();
		;
		MasterOrganizationEmailConfig masterOrganizationEmailConfigEntity = configDAO.findByorganizationAnddivision(
				empEntity.getCandidate().getLoginEntity().getOrganization().getId(),
				empEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId());
		String hrEmailIds = "";
		String orgLevelEmailId = masterOrganizationEmailConfigEntity.getOrgLevelEmployee().getOfficialEmailId();
		List<OrgDivWiseHRMapping> orgDivHRList = orgDivHRDAO.findOrgDivWiseHrMapping(
				empEntity.getCandidate().getLoginEntity().getOrganization().getId(),
				empEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				IHRMSConstants.isActive);
		for (OrgDivWiseHRMapping orgDivHRMApEntity : orgDivHRList) {
			hrEmailIds = hrEmailIds + orgDivHRMApEntity.getEmployee().getOfficialEmailId() + ";";
		}

		Map<String, String> placeHolderMapping = HRMSRequestTranslator.createPlaceHolderMapForEmp_Separation(empEntity,
				employeeSeparationDetailsEntity);
		placeHolderMapping.put("{websiteURL}", baseURL);

		if (!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity.getStatus()) && employeeSeparationDetailsEntity
				.getStatus().equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING)) {
			recipientEmailId = rmEmailIds;
			ccEmailIds = employeeEmailId + ";" + orgLevelEmailId + ";" + hrEmailIds;
			template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
					IHRMSConstants.Template_Employee_Resignation_Apply, IHRMSConstants.isActive,
					empEntity.getCandidate().getCandidateProfessionalDetail().getDivision(),
					empEntity.getCandidate().getLoginEntity().getOrganization());
			mailBody = HRMSHelper.replaceString(placeHolderMapping, template.getTemplate());

		}

		if (!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity.getStatus())
				&& employeeSeparationDetailsEntity.getStatus().equalsIgnoreCase(IHRMSConstants.LeaveStatus_CANCELLED)) {
			recipientEmailId = empEntity.getOfficialEmailId();
			ccEmailIds = rmEmailIds + ";" + orgLevelEmailId + ";" + hrEmailIds;
			template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
					IHRMSConstants.Template_Employee_Resignation_Cancel, IHRMSConstants.isActive,
					empEntity.getCandidate().getCandidateProfessionalDetail().getDivision(),
					empEntity.getCandidate().getLoginEntity().getOrganization());
			mailBody = HRMSHelper.replaceString(placeHolderMapping, template.getTemplate());

		} else
			log.info("ccemailids sendEmail to Approvers  " + ccEmailIds);
		emailsender.toPersistEmail(recipientEmailId, ccEmailIds, mailBody, template.getEmailSubject(),
				empEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				empEntity.getCandidate().getLoginEntity().getOrganization().getId());
	}

	private EmployeeSeparationDetails setROandOrgstatusForTerAbscDemiseReason(
			EmployeeSeparationDetails employeeSeparationDetailsEntity) {

		log.info("SET RO System escalted");
		employeeSeparationDetailsEntity.setRoApproverStatus(IHRMSConstants.EMPLOYEE_SEPARATION_SYSTEM_ESCALATED);
		employeeSeparationDetailsEntity.setRoActionDate(new Date());
		employeeSeparationDetailsEntity.setOrgApproverStatus(IHRMSConstants.EMPLOYEE_SEPARATION_SYSTEM_ESCALATED);
		employeeSeparationDetailsEntity.setOrgActionDate(new Date());
		return employeeSeparationDetailsEntity;

	}

	private void addEntryInChecklistTables(EmployeeSeparationRequestVO voEmployeeSeparationDetails,
			Employee employeeEntity) {

		log.info("Added Entry in Employee Catalogue Table and Employee Catalogue Checklist");
		// List<MapCatalogue> mapCatalogueList = mapCatalogueDAO.findAll();
		List<MapCatalogue> mapCatalogueList = mapCatalogueDAO.findCatalogueListByOrgandDiv(
				employeeEntity.getCandidate().getLoginEntity().getOrganization().getId(),
				employeeEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId());
		List<MapEmployeeCatalogue> empCatalogueList = empCatalogueMapDAO.findByEmployeeId(employeeEntity.getId());
		if (!HRMSHelper.isNullOrEmpty(empCatalogueList)) {
			for (MapEmployeeCatalogue mapEmployeeCatalogueEntity : empCatalogueList) {
				List<MapCatalogueChecklistItem> catalogueChecklistItemList = mapCatalogueChecklistDAO
						.findBycatalogueidAndOrgId(mapEmployeeCatalogueEntity.getCatalogue().getId(),
								SecurityFilter.TL_CLAIMS.get().getOrgId());

				List<MapEmployeeCatalogueChecklist> empcatalogueChecklistItemList = empcatalogueChecklistDAO
						.findByEmployeeCatalogueMapping(mapEmployeeCatalogueEntity.getId());
				if (HRMSHelper.isNullOrEmpty(empcatalogueChecklistItemList)) {
					for (MapCatalogueChecklistItem mapCatalogueChecklistItemEntity : catalogueChecklistItemList) {
						MapEmployeeCatalogueChecklist mapEmployeeCatalogueChecklist = new MapEmployeeCatalogueChecklist();
						mapEmployeeCatalogueChecklist.setCatalogueChecklist(mapCatalogueChecklistItemEntity);
						mapEmployeeCatalogueChecklist.setEmployeeCatalogueMapping(mapEmployeeCatalogueEntity);
						mapEmployeeCatalogueChecklist.setAmount(0.0);
						mapEmployeeCatalogueChecklist.setIsActive(IHRMSConstants.isActive);
						mapEmployeeCatalogueChecklist.setHaveCollected(false);
						empcatalogueChecklistDAO.save(mapEmployeeCatalogueChecklist);
					}
				}
			}
		} else {
			for (MapCatalogue mapCatalogueEntity : mapCatalogueList) {
				MapEmployeeCatalogue mapEmployeeCatalogueEntity = new MapEmployeeCatalogue();
				MapEmployeeCatalogue mapEmployeeCatalogueResponseEntity = new MapEmployeeCatalogue();

				mapEmployeeCatalogueEntity.setResignedEmployee(employeeEntity);
				mapEmployeeCatalogueEntity.setCatalogue(mapCatalogueEntity);
				mapEmployeeCatalogueEntity.setIsActive(IHRMSConstants.isActive);
				mapEmployeeCatalogueEntity.setStatus(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING);

				mapEmployeeCatalogueResponseEntity = empCatalogueMapDAO.save(mapEmployeeCatalogueEntity);

				List<MapCatalogueChecklistItem> catalogueChecklistItemList = mapCatalogueChecklistDAO
						.findBycatalogueidAndOrgId(mapEmployeeCatalogueResponseEntity.getCatalogue().getId(),
								SecurityFilter.TL_CLAIMS.get().getOrgId());

				List<MapEmployeeCatalogueChecklist> empcatalogueChecklistItemList = empcatalogueChecklistDAO
						.findByEmployeeCatalogueMapping(mapEmployeeCatalogueEntity.getId());
				if (HRMSHelper.isNullOrEmpty(empcatalogueChecklistItemList)) {

					for (MapCatalogueChecklistItem mapCatalogueChecklistItemEntity : catalogueChecklistItemList) {
						MapEmployeeCatalogueChecklist mapEmployeeCatalogueChecklist = new MapEmployeeCatalogueChecklist();
						mapEmployeeCatalogueChecklist.setCatalogueChecklist(mapCatalogueChecklistItemEntity);
						mapEmployeeCatalogueChecklist.setEmployeeCatalogueMapping(mapEmployeeCatalogueEntity);
						mapEmployeeCatalogueChecklist.setAmount(0.0);
						mapEmployeeCatalogueChecklist.setIsActive(IHRMSConstants.isActive);
						mapEmployeeCatalogueChecklist.setHaveCollected(false);
						empcatalogueChecklistDAO.save(mapEmployeeCatalogueChecklist);
					}
				}
			}
		}

	}

	private void sendMailToempTermAbsDemisReason(EmployeeSeparationDetails employeeSeparationDetailsEntity) {
		log.info("inside systemEscalationForORG ");
		Employee empEntity = employeeDAO.findById(employeeSeparationDetailsEntity.getEmployee().getId()).get();
		Map<String, String> placeHolderMapping = HRMSRequestTranslator.createPlaceHolderApproverChecklist(empEntity,
				employeeSeparationDetailsEntity);
		placeHolderMapping.put("{websiteURL}", baseURL);

		String mailContent = HRMSHelper.replaceString(placeHolderMapping,
				IHRMSEmailTemplateConstants.Template_FOR_CHECKLIST_APPROVALS);
		String toemailIds = "";
		String ccemailIds = "";
		String mailSubject = IHRMSConstants.MAILSUBJECTFORCHECKLISTAPPROVALS;
		String checklistApprovalsEmailIds = "";

		List<MapCatalogue> mapCatalogueList = mapCatalogueDAO.findAllCataloguewithEmployeeByisActiveAndOrgId(
				IHRMSConstants.isActive,
				empEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				SecurityFilter.TL_CLAIMS.get().getOrgId());
		for (MapCatalogue mapCatalogueEntity : mapCatalogueList) {
			if (mapCatalogueEntity.getApprover() != null) {
				checklistApprovalsEmailIds = checklistApprovalsEmailIds
						+ mapCatalogueEntity.getApprover().getOfficialEmailId() + ";";

			}
		}
		String roEmailId = employeeSeparationDetailsEntity.getEmployee().getEmployeeReportingManager()
				.getReporingManager().getOfficialEmailId();
		String empEmailId = employeeSeparationDetailsEntity.getEmployee().getOfficialEmailId();
		MasterOrganizationEmailConfig masterOrganizationEmailConfigEntity = configDAO.findByorganizationAnddivision(
				empEntity.getCandidate().getLoginEntity().getOrganization().getId(),
				empEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId());
		String hrEmailIds = "";
		String orgLevelEmailId = masterOrganizationEmailConfigEntity.getOrgLevelEmployee().getOfficialEmailId();
		List<OrgDivWiseHRMapping> orgDivHRList = orgDivHRDAO.findOrgDivWiseHrMapping(
				empEntity.getCandidate().getLoginEntity().getOrganization().getId(),
				empEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				IHRMSConstants.isActive);
		for (OrgDivWiseHRMapping orgDivHRMApEntity : orgDivHRList) {
			hrEmailIds = hrEmailIds + orgDivHRMApEntity.getEmployee().getOfficialEmailId() + ";";
		}

		toemailIds = roEmailId + ";" + orgLevelEmailId + ";" + checklistApprovalsEmailIds;
		ccemailIds = empEmailId;
		try {

			eventEmailSender.toSendEmailScheduler(toemailIds, ccemailIds, null, mailContent, mailSubject,
					employeeSeparationDetailsEntity.getEmployee().getCandidate().getCandidateProfessionalDetail()
							.getDivision().getId(),
					employeeSeparationDetailsEntity.getEmployee().getCandidate().getLoginEntity().getOrganization()
							.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * This is use to get details of separation employee list according to their
	 * manager
	 */
	@Override
	public HRMSBaseResponse<List<EmployeeSeprationDetailsResponseVO>> getResignedEmployeeList(Pageable pageable)
			throws HRMSException {
		HRMSBaseResponse<List<EmployeeSeprationDetailsResponseVO>> response = new HRMSBaseResponse<>();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, IHRMSConstants.isActive);
		int totalCount = 0;
		/* separation status list **/
		List<String> separationStatus = new ArrayList<String>();
		List<EmployeeSeprationDetailsResponseVO> employeeResponseVOs = new ArrayList<EmployeeSeprationDetailsResponseVO>();
		if (HRMSHelper.isRolePresent(roles, ERole.MANAGER.name())) {
			separationStatus.add(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING);
			separationStatus.add(IHRMSConstants.EMPLOYEE_ABSCONDED);
			separationStatus.add(IHRMSConstants.EMPLOYEE_TERMINATED);
			separationStatus.add(IHRMSConstants.EMPLOYEE_DEMISE);
			
			List<EmployeeSeparationDetails> empseparationDetailsEntity = null;
			long orgId = loggedInEmployee.getCandidate().getLoginEntity().getOrganization().getId();
//			empseparationDetailsEntity = employeeSeparationDAO.findByResignedEmployee(ERecordStatus.Y.name(),
//					loggedInEmpId, IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING, orgId, pageable);
//			totalCount = employeeSeparationDAO.countByResignedEmployee(ERecordStatus.Y.name(), loggedInEmpId,
//					IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING, orgId);
			empseparationDetailsEntity = employeeSeparationDAO.findByResignedEmployees(ERecordStatus.Y.name(),
					loggedInEmpId, separationStatus.toArray(), orgId, pageable);
			totalCount = employeeSeparationDAO.countByResignedEmployees(ERecordStatus.Y.name(), loggedInEmpId,
					separationStatus.toArray(), orgId);
			if (HRMSHelper.isNullOrEmpty(empseparationDetailsEntity)) {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}

			EmployeeSeprationDetailsResponseVO seprationDetailsVO = new EmployeeSeprationDetailsResponseVO();
			// separationDetails
			for (EmployeeSeparationDetails a : empseparationDetailsEntity) {
				seprationDetailsVO = new EmployeeSeprationDetailsResponseVO();
				// VOEmployeeSeparationDetails voEmployeeSeparationDetails =
				// HRMSEntityToModelMapper
				// .convertToEmpSeparationDetailsVO(employeeSeparationDetailsEntity);
				// listResponse.add(voEmployeeSeparationDetails);
				seprationDetailsVO.setId(a.getId());
				seprationDetailsVO.setEmployeeComment(a.getEmployeeComment());
				seprationDetailsVO.setEmployeeId(a.getEmployee().getId());
				seprationDetailsVO.setEmployeeName(a.getEmployee().getCandidate().getFirstName() + " "
						+ a.getEmployee().getCandidate().getLastName());
				seprationDetailsVO.setNoticePeriod(a.getNoticeperiod());
				seprationDetailsVO.setRelievingDate(a.getActualRelievingDate());
				seprationDetailsVO.setResignationDate(a.getResignationDate());
				seprationDetailsVO.setResignationReason(!HRMSHelper.isNullOrEmpty(a.getEmpSeparationReason())
						? a.getEmpSeparationReason().getReasonName()
						: null);
				seprationDetailsVO.setEmployeeAction(a.getEmployeeAction());
				seprationDetailsVO.setEmpCancelComment(a.getEmpWdComment());
				seprationDetailsVO.setEmpWdDate(a.getEmpWdDate());
				seprationDetailsVO.setHrActionDate(a.getHrActionDate());
				seprationDetailsVO.setHrApproverStatus(a.getHrApproverStatus());
				seprationDetailsVO.setHrComment(a.getHRComment());
				seprationDetailsVO.setHrReason(
						!HRMSHelper.isNullOrEmpty(a.getHRReason()) ? a.getHRReason().getReasonName() : null);
				seprationDetailsVO.setHrWdAction(a.getHr_WdActionStatus());
				seprationDetailsVO.setHrWdActionDate(a.getHr_WdActionDate());
				seprationDetailsVO.setHrWdComment(a.getHr_WdComment());
				seprationDetailsVO.setOrgActionDate(a.getOrgActionDate());
				seprationDetailsVO.setOrgApproverStatus(a.getOrgApproverStatus());
				seprationDetailsVO.setOrgComment(a.getOrgComment());
				seprationDetailsVO.setOrgLevelEmpWdAction(a.getOrg_level_emp_WdActionStatus());
				seprationDetailsVO.setOrgLevelEmpWdActionDate(a.getOrg_level_emp_Wd_action_Date());
				seprationDetailsVO.setOrgLevelEmpWdComment(a.getOrg_level_emp_WdComment());
				seprationDetailsVO.setOrgReason(
						!HRMSHelper.isNullOrEmpty(a.getOrgReason()) ? a.getOrgReason().getReasonName() : null);
				seprationDetailsVO.setRoActionDate(a.getRoActionDate());
				seprationDetailsVO.setRoApproverStatus(a.getRoApproverStatus());
				seprationDetailsVO.setRoComment(a.getRoComment());
				seprationDetailsVO.setRoReason(
						!HRMSHelper.isNullOrEmpty(a.getRoReason()) ? a.getRoReason().getReasonName() : null);
				seprationDetailsVO.setRoWdAction(a.getROWdActionStatus());
				seprationDetailsVO.setRoWdActionDate(a.getROWdActionDate());
				seprationDetailsVO.setRoWdComment(a.getROWdComment());
				seprationDetailsVO.setStatus(a.getStatus());
				seprationDetailsVO.setSystemEscalatedLevel(a.getSystemEscalatedLevel());

				seprationDetailsVO.setRoApproverName(
						a.getEmployee().getEmployeeReportingManager().getReporingManager().getCandidate().getFirstName()
								+ " " + a.getEmployee().getEmployeeReportingManager().getReporingManager()
										.getCandidate().getLastName());
				MasterOrganizationEmailConfig emailConfig = emailConfigDetailDAO.findByorganizationAnddivision(
						loggedInEmployee.getCandidate().getLoginEntity().getOrganization().getId(),
						loggedInEmployee.getCandidate().getCandidateProfessionalDetail().getDivision().getId());
				seprationDetailsVO.setHrApproverName(emailConfig.getOrgLevelEmployee().getCandidate().getFirstName()
						+ " " + emailConfig.getOrgLevelEmployee().getCandidate().getLastName());

				employeeResponseVOs.add(seprationDetailsVO);
			}
		} else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}
		response.setTotalRecord(totalCount);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseBody(employeeResponseVOs);
		response.setApplicationVersion(applicationVersion);
		return response;

	}

	@Override
	public HRMSBaseResponse<List<EmployeeSeprationDetailsResponseVO>> getResignedEmployeeDetails(Pageable pageable)
			throws HRMSException {

		HRMSBaseResponse<List<EmployeeSeprationDetailsResponseVO>> response = new HRMSBaseResponse<>();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, IHRMSConstants.isActive);

		int totalCount = 0;
		List<EmployeeSeprationDetailsResponseVO> employeeResponseVOs = new ArrayList<EmployeeSeprationDetailsResponseVO>();

		List<EmployeeSeparationDetails> empseparationDetailsEntity = null;
		empseparationDetailsEntity = employeeSeparationDAO.findByEmpId(loggedInEmpId);

		if (HRMSHelper.isNullOrEmpty(empseparationDetailsEntity)) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		// get all checkliststatus
		String isFeedbackFormdisplay = ERecordStatus.Y.name();
		List<MapEmployeeCatalogue> resignEmployee = empCatalogueMapDAO.findByEmployeeId(loggedInEmpId);
		MapEmployeeCatalogue isPending = resignEmployee.stream()
				.filter(e -> e.getStatus().equalsIgnoreCase(IHRMSConstants.PENDING)).findAny().orElse(null);
		if (!HRMSHelper.isNullOrEmpty(isPending)) {
			isFeedbackFormdisplay = ERecordStatus.N.name();
		}

		EmployeeSeprationDetailsResponseVO seprationDetailsVO = new EmployeeSeprationDetailsResponseVO();
		// separationDetails
		for (EmployeeSeparationDetails separationDetails : empseparationDetailsEntity) {
			seprationDetailsVO = new EmployeeSeprationDetailsResponseVO();
			seprationDetailsVO.setId(separationDetails.getId());
			seprationDetailsVO.setEmployeeComment(separationDetails.getEmployeeComment());
			seprationDetailsVO.setEmployeeId(separationDetails.getEmployee().getId());
			seprationDetailsVO.setEmployeeName(separationDetails.getEmployee().getCandidate().getFirstName() + " "
					+ separationDetails.getEmployee().getCandidate().getLastName());
			seprationDetailsVO.setNoticePeriod(separationDetails.getNoticeperiod());
			seprationDetailsVO.setRelievingDate(separationDetails.getActualRelievingDate());
			seprationDetailsVO.setResignationDate(separationDetails.getResignationDate());
			seprationDetailsVO
					.setResignationReason(!HRMSHelper.isNullOrEmpty(separationDetails.getEmpSeparationReason())
							? separationDetails.getEmpSeparationReason().getReasonName()
							: null);
			seprationDetailsVO.setEmployeeAction(separationDetails.getEmployeeAction());
			seprationDetailsVO.setEmpCancelComment(separationDetails.getEmpWdComment());
			seprationDetailsVO.setEmpWdDate(separationDetails.getEmpWdDate());
			seprationDetailsVO.setHrActionDate(separationDetails.getHrActionDate());
			seprationDetailsVO.setHrApproverStatus(separationDetails.getHrApproverStatus());
			seprationDetailsVO.setHrComment(separationDetails.getHRComment());
			seprationDetailsVO.setHrReason(!HRMSHelper.isNullOrEmpty(separationDetails.getHRReason())
					? separationDetails.getHRReason().getReasonName()
					: null);
			seprationDetailsVO.setHrWdAction(separationDetails.getHr_WdActionStatus());
			seprationDetailsVO.setHrWdActionDate(separationDetails.getHr_WdActionDate());
			seprationDetailsVO.setHrWdComment(separationDetails.getHr_WdComment());
			seprationDetailsVO.setOrgActionDate(separationDetails.getOrgActionDate());
			seprationDetailsVO.setOrgApproverStatus(separationDetails.getOrgApproverStatus());
			seprationDetailsVO.setOrgComment(separationDetails.getOrgComment());
			seprationDetailsVO.setOrgLevelEmpWdAction(separationDetails.getOrg_level_emp_WdActionStatus());
			seprationDetailsVO.setOrgLevelEmpWdActionDate(separationDetails.getOrg_level_emp_Wd_action_Date());
			seprationDetailsVO.setOrgLevelEmpWdComment(separationDetails.getOrg_level_emp_WdComment());
			seprationDetailsVO.setOrgReason(!HRMSHelper.isNullOrEmpty(separationDetails.getOrgReason())
					? separationDetails.getOrgReason().getReasonName()
					: null);
			seprationDetailsVO.setRoActionDate(separationDetails.getRoActionDate());
			seprationDetailsVO.setRoApproverStatus(separationDetails.getRoApproverStatus());
			seprationDetailsVO.setRoComment(separationDetails.getRoComment());
			seprationDetailsVO.setRoReason(!HRMSHelper.isNullOrEmpty(separationDetails.getRoReason())
					? separationDetails.getRoReason().getReasonName()
					: null);
			seprationDetailsVO.setRoWdAction(separationDetails.getROWdActionStatus());
			seprationDetailsVO.setRoWdActionDate(separationDetails.getROWdActionDate());
			seprationDetailsVO.setRoWdComment(separationDetails.getROWdComment());
			seprationDetailsVO.setStatus(separationDetails.getStatus());
			seprationDetailsVO.setSystemEscalatedLevel(separationDetails.getSystemEscalatedLevel());
			seprationDetailsVO.setRoApproverName(separationDetails.getEmployee().getEmployeeReportingManager()
					.getReporingManager().getCandidate().getFirstName() + " "
					+ separationDetails.getEmployee().getEmployeeReportingManager().getReporingManager().getCandidate()
							.getLastName());

			MasterOrganizationEmailConfig emailConfig = emailConfigDetailDAO.findByorganizationAnddivision(
					loggedInEmployee.getCandidate().getLoginEntity().getOrganization().getId(),
					loggedInEmployee.getCandidate().getCandidateProfessionalDetail().getDivision().getId());
			seprationDetailsVO.setHrApproverName(emailConfig.getOrgLevelEmployee().getCandidate().getFirstName() + " "
					+ emailConfig.getOrgLevelEmployee().getCandidate().getLastName());
			seprationDetailsVO.setIsFeedbackFormdisplay(isFeedbackFormdisplay);

			employeeResponseVOs.add(seprationDetailsVO);
		}

		response.setTotalRecord(totalCount);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseBody(employeeResponseVOs);
		response.setApplicationVersion(applicationVersion);
		return response;

	}

	@Override
	public HRMSBaseResponse<?> cancelSeparation(@RequestBody EmployeeSeparationRequestVO voEmployeeSeparationDetails)
			throws HRMSException, ParseException {

		log.info("Inside cancel separation method");
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		SeparationAuthorityHelper.cancelSepareationInputValidation(voEmployeeSeparationDetails);

		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		EmployeeSeparationDetails employeeSeparationDetailsEntity;
		EmployeeSeparationDetailsWithHistory employeeSeparationDetailsEntityHistory;
		Employee employeeEntity = employeeDAO.findById(empId).get();

		// SeparationAuthorityHelper.applySepareationInputValidation(voEmployeeSeparationDetails);

		if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails)) {

			employeeSeparationDetailsEntity = employeeSeparationDAO.findSeparationDetailsUsingIdandActive(
					IHRMSConstants.isActive, voEmployeeSeparationDetails.getId());
			employeeSeparationDetailsEntityHistory = new EmployeeSeparationDetailsWithHistory();
			if (!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity)) {

				if (employeeSeparationDetailsEntity.getStatus()
						.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING)
						&& HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity.getRoApproverStatus())) {

//					employeeSeparationDetailsEntity = new EmployeeSeparationDetails();

					employeeSeparationDetailsEntity.setEmpWdComment(voEmployeeSeparationDetails.getEmpCancelComment());
					employeeSeparationDetailsEntity.setStatus(IHRMSConstants.LeaveStatus_CANCELLED);
					employeeSeparationDetailsEntity.setIsActive(IHRMSConstants.isNotActive);
					employeeSeparationDetailsEntity.setUpdatedDate(new Date());
					employeeSeparationDetailsEntity.setUpdatedBy(empId.toString());

//					employeeSeparationDetailsEntityHistory.setEmpWdComment(voEmployeeSeparationDetails.getEmpWdComment());
//					employeeSeparationDetailsEntityHistory.setStatus(IHRMSConstants.LeaveStatus_CANCELLED);
//					employeeSeparationDetailsEntityHistory.setIsActive(IHRMSConstants.isNotActive);
//					employeeSeparationDetailsEntityHistory.setUpdatedDate(new Date());
//					employeeSeparationDetailsEntityHistory.setUpdatedBy(empId.toString());
				} else {

					throw new HRMSException(1534, ResponseCode.getResponseCodeMap().get(1534));
				}

			} else {

				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}

			employeeSeparationDetailsEntity = employeeSeparationDAO.save(employeeSeparationDetailsEntity);
			sendEmailToApprovers(employeeEntity, employeeSeparationDetailsEntity);
//			employeeSeparationDetailsEntityHistory = employeeSeparationHistoryDAO
//					.save(employeeSeparationDetailsEntityHistory);

//			List<Object> objectList = new ArrayList<Object>();
//			objectList.add(HRMSEntityToModelMapper.convertToEmpSeparationDetailsVO(employeeSeparationDetailsEntity));
//			HRMSListResponseObject res = new HRMSListResponseObject();
//			res.setListResponse(objectList);
//			res.setResponseCode(IHRMSConstants.successCode);
//			res.setResponseMessage(IHRMSConstants.successMessage);

			// response.setResponseBody(res);
			response.setResponseCode(1200);
			response.setResponseMessage(IHRMSConstants.SEPARATIOCANCELMESSAGE);
			response.setApplicationVersion(applicationVersion);

			log.info("Exit from cancel separation method");
			return response;

		} else
			throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);

	}

	@Override
	public HRMSBaseResponse<?> approveResignation(EmployeeSeparationRequestVO voEmployeeSeparationDetails)
			throws HRMSException {

		log.info("Inside Ro APPROVED separation method");

		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();

		SeparationAuthorityHelper.approvedSepareationInputValidation(voEmployeeSeparationDetails);

		String releaseDateMandtory = ERecordStatus.N.name();
		ReleaseTypeMaster releaseType = releaseTypeMasterDAO.findByIsActiveAndId(ERecordStatus.Y.name(),
				voEmployeeSeparationDetails.getReleaseTypeVO().getId());
		if (!HRMSHelper.isNullOrEmpty(releaseType)) {
			releaseDateMandtory = releaseType.getReleaseDateMandatory();
		}

		if (HRMSHelper.isRolePresent(roles, ERole.MANAGER.name())) {
			// Retrieve the manager's ID
			Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

			// Find all employee IDs under this manager
			List<EmployeeReportingManager> employeesUnderManager = reportingManagerDAO.findByreporingManager(empId);

			EmployeeSeparationDetails roApprovedemployee = employeeSeparationDAO.findSeparationDetailsUsingIdandActive(
					IHRMSConstants.isActive, voEmployeeSeparationDetails.getId());

			if (!HRMSHelper.isNullOrEmpty(roApprovedemployee)) {
				EmployeeSeparationDetails separationDetails = null;
				separationDetails = employeeSeparationDAO.findById(voEmployeeSeparationDetails.getId()).get();
				// Find the employee ID for the leave request
				Long requestedEmployeeId = separationDetails.getEmployee().getId();
				Employee employeeEntity = employeeDAO.findById(requestedEmployeeId).get();
				// Check if the requested employee is under the management hierarchy of the
				// manager
				long matchCount = employeesUnderManager.stream()
						.filter(e -> e.getEmployee().getId().equals(requestedEmployeeId)).count();

				if (matchCount > 0) {
					// Check if Ro approved status is already approved or rejected
					EmployeeSeparationDetails empseparationDetailsEntity = employeeSeparationDAO
							.findSeparationDetailsUsingIdandActive(ERecordStatus.Y.name(),
									voEmployeeSeparationDetails.getId());
					if (HRMSHelper.isNullOrEmpty(empseparationDetailsEntity)) {
						throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
					}

					if (IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED
							.equals(empseparationDetailsEntity.getRoApproverStatus())) {
						throw new HRMSException(1530, ResponseCode.getResponseCodeMap().get(1530));
					} else if (IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED
							.equals(empseparationDetailsEntity.getRoApproverStatus())) {
						throw new HRMSException(1531, ResponseCode.getResponseCodeMap().get(1531));
					}

					// If not already approved or rejected, proceed with approval
					MasterModeofSeparationReason empSeaparationReasonEntity = mstSeparationReasonDAO
							.findByReasonId(voEmployeeSeparationDetails.getSeparationReasonVO().getId());

					empseparationDetailsEntity.setRoActionDate(new Date());
					empseparationDetailsEntity.setRoApproverStatus(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED);
					empseparationDetailsEntity.setRoComment(voEmployeeSeparationDetails.getRoComment());
					empseparationDetailsEntity.setRoReason(empSeaparationReasonEntity);
					empseparationDetailsEntity.setEarlyRelease(releaseDateMandtory);
					if (releaseDateMandtory.equalsIgnoreCase(ERecordStatus.Y.name())) {
						empseparationDetailsEntity
								.setReleaseDateByRo(HRMSDateUtil.parse(voEmployeeSeparationDetails.getReleaseDateByRo(),
										IHRMSConstants.FRONT_END_DATE_FORMAT));
					}
					employeeSeparationDAO.save(empseparationDetailsEntity);
					actionbyApproversForSeparation(employeeEntity, voEmployeeSeparationDetails);

				} else {
					// Throw an exception if the requested employee is not under the management
					// hierarchy
					throw new HRMSException(1532, ResponseCode.getResponseCodeMap().get(1532));
				}

			} else {
				// If data not found, throw an exception with a message
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}

		} else {
			// Throw an exception if the user is not a manager
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1594));

		return response;
	}

	@Override
	public HRMSBaseResponse<?> rejectResignation(EmployeeSeparationRequestVO voEmployeeSeparationDetails)
			throws HRMSException {

		log.info("Inside Ro REJECTED separation method");

		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		SeparationAuthorityHelper.rejectSepareationInputValidation(voEmployeeSeparationDetails);
		if (HRMSHelper.isRolePresent(roles, ERole.MANAGER.name())) {
			// Retrieve the manager's ID
			Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

			// Find all employee IDs under this manager
			List<EmployeeReportingManager> employeesUnderManager = reportingManagerDAO.findByreporingManager(empId);

			EmployeeSeparationDetails roRejectedemployee = employeeSeparationDAO.findSeparationDetailsUsingIdandActive(
					IHRMSConstants.isActive, voEmployeeSeparationDetails.getId());

			if (!HRMSHelper.isNullOrEmpty(roRejectedemployee)) {
				EmployeeSeparationDetails leavesAppliedEnity = null;
				leavesAppliedEnity = employeeSeparationDAO.findById(voEmployeeSeparationDetails.getId()).get();
				// Find the employee ID for the leave request
				Long requestedEmployeeId = leavesAppliedEnity.getEmployee().getId();
				// Check if the requested employee is under the management hierarchy of the
				// manager
				Employee employeeEntity = employeeDAO.findById(requestedEmployeeId).get();
				long matchCount = employeesUnderManager.stream()
						.filter(e -> e.getEmployee().getId().equals(requestedEmployeeId)).count();

				if (matchCount > 0) {
					// Check if Ro approved status is already approved or rejected
					EmployeeSeparationDetails empseparationDetailsEntity = employeeSeparationDAO
							.findSeparationDetailsUsingIdandActive(ERecordStatus.Y.name(),
									voEmployeeSeparationDetails.getId());
					if (HRMSHelper.isNullOrEmpty(empseparationDetailsEntity)) {
						throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
					}

					if (IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED
							.equals(empseparationDetailsEntity.getRoApproverStatus())) {
						throw new HRMSException(1530, ResponseCode.getResponseCodeMap().get(1530));
					} else if (IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED
							.equals(empseparationDetailsEntity.getRoApproverStatus())) {
						throw new HRMSException(1531, ResponseCode.getResponseCodeMap().get(1531));
					}

					// If not already approved or rejected, proceed with approval
					MasterModeofSeparationReason empSeaparationReasonEntity = mstSeparationReasonDAO
							.findByReasonId(voEmployeeSeparationDetails.getSeparationReasonVO().getId());

					empseparationDetailsEntity.setRoActionDate(new Date());
					empseparationDetailsEntity.setRoApproverStatus(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED);
					empseparationDetailsEntity.setRoComment(voEmployeeSeparationDetails.getRoComment());
					empseparationDetailsEntity.setRoReason(empSeaparationReasonEntity);
					empseparationDetailsEntity.setStatus(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED);
					empseparationDetailsEntity.setIsActive(IHRMSConstants.isNotActive);
					actionbyApproversForSeparation(employeeEntity, voEmployeeSeparationDetails);
					employeeSeparationDAO.save(empseparationDetailsEntity);

				} else {
					// Throw an exception if the requested employee is not under the management
					// hierarchy
					throw new HRMSException(1533, ResponseCode.getResponseCodeMap().get(1533));
				}

			} else {
				// If data not found, throw an exception with a message
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}

		} else {
			// Throw an exception if the user is not a manager
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		return response;

	}

	@Override
	public HRMSBaseResponse<List<ChecklistStatusVO>> getChecklistStatus(Long empId, Long seprationId)
			throws HRMSException {

		if (!HRMSHelper.isLongZero(empId)) {
			Employee employeeReportingMngr = employeeDAO.findById(empId).get();
			Employee togetOrgLevel = null;
			double totalamountofCatalogue = 0.0;
			// List<MasterOrganizationEmailConfig> mstCorgConfigEntityList =
			// configDAO.findAll();
			MasterOrganizationEmailConfig mstOrgConfigEntity = configDAO.findByorganizationAnddivision(
					employeeReportingMngr.getCandidate().getLoginEntity().getOrganization().getId(),
					employeeReportingMngr.getCandidate().getCandidateProfessionalDetail().getDivision().getId());
			/*
			 * for (MasterOrganizationEmailConfig mstConfigEntity : mstCorgConfigEntityList)
			 * { togetOrgLevel =
			 * employeeDAO.findById(mstConfigEntity.getOrgLevelEmployee().getId()); break; }
			 */
			togetOrgLevel = employeeDAO.findById(mstOrgConfigEntity.getOrgLevelEmployee().getId()).get();

			List<MapEmployeeCatalogue> empCatalogueList = empCatalogueMapDAO.findByEmployeeId(empId);
			ChecklistStatusVO voempchklistandSepStatusForEmpMngr = new ChecklistStatusVO();
			ChecklistStatusVO voempchklistandSepStatusForEmpOrg = new ChecklistStatusVO();
			List<ChecklistStatusVO> checkListVo = new ArrayList<ChecklistStatusVO>();

			Long empSeparationId = (long) 0;
			List<EmployeeSeparationDetails> empSeparationlist = employeeSeparationDAO.findByemployeeIdAndId(empId,
					seprationId);
			for (EmployeeSeparationDetails empseparationdetails : empSeparationlist) {
				if (empseparationdetails.getId() > empSeparationId)
					empSeparationId = empseparationdetails.getId();
			}

			EmployeeSeparationDetails empSeparationDetails = employeeSeparationDAO.findById(empSeparationId).get();
			voempchklistandSepStatusForEmpMngr.setActionDate(
					HRMSDateUtil.format(empSeparationDetails.getRoActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			voempchklistandSepStatusForEmpMngr.setStatus(empSeparationDetails.getRoApproverStatus());
			voempchklistandSepStatusForEmpOrg.setActionDate(
					HRMSDateUtil.format(empSeparationDetails.getOrgActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			voempchklistandSepStatusForEmpOrg.setStatus(empSeparationDetails.getOrgApproverStatus());

			voempchklistandSepStatusForEmpMngr.setApproverName(employeeReportingMngr.getEmployeeReportingManager()
					.getReporingManager().getCandidate().getFirstName()
					+ " "
					+ employeeReportingMngr.getEmployeeReportingManager().getReporingManager().getCandidate()
							.getLastName()
					+ " -" + employeeReportingMngr.getEmployeeReportingManager().getReporingManager().getId());
			voempchklistandSepStatusForEmpMngr.setStage(IHRMSConstants.RESIGNATION_APPROVAL);

			checkListVo.add(voempchklistandSepStatusForEmpMngr);

			voempchklistandSepStatusForEmpOrg.setApproverName(togetOrgLevel.getCandidate().getFirstName() + " "
					+ togetOrgLevel.getCandidate().getLastName() + " -" + togetOrgLevel.getId());
			voempchklistandSepStatusForEmpOrg.setStage(IHRMSConstants.RESIGNATION_APPROVAL);
			checkListVo.add(voempchklistandSepStatusForEmpOrg);

			if (!empSeparationDetails.getStatus().equals(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED)
					&& (empSeparationDetails.getIsActive().equalsIgnoreCase(IHRMSConstants.isActive))) {
				for (MapEmployeeCatalogue employeeCatalogueEntity : empCatalogueList) {

					if (employeeCatalogueEntity.getCatalogue().getApprover() != null) {

						ChecklistStatusVO voempchecklistandResignationStatus = new ChecklistStatusVO();
						Employee employee = employeeDAO
								.findById(employeeCatalogueEntity.getCatalogue().getApprover().getId()).get();
						log.info("employeeCatalogueEntity.getId()   =====  " + employeeCatalogueEntity.getId());
						List<MapEmployeeCatalogueChecklist> empcatalogueChecklistItemList = empcatalogueChecklistDAO
								.findByEmployeeCatalogueMapping(employeeCatalogueEntity.getId());
						log.info("Length == " + empcatalogueChecklistItemList.size());
						double amount = 0;
						List<VOMapEmployeeCatalogueChecklist> checklist = new ArrayList<VOMapEmployeeCatalogueChecklist>();
						for (MapEmployeeCatalogueChecklist mapCatalogueChecklistItemEntity : empcatalogueChecklistItemList) {

							amount = amount + mapCatalogueChecklistItemEntity.getAmount();
							VOMapEmployeeCatalogueChecklist voempCataloguechecklist = HRMSEntityToModelMapper
									.convertToEmployeeChecklistMapping(mapCatalogueChecklistItemEntity);
							checklist.add(voempCataloguechecklist);

						}
						totalamountofCatalogue = totalamountofCatalogue + amount;
						// voempchecklistandResignationStatus.setEmpCatalogueChecklist(checklist);
						voempchecklistandResignationStatus.setTotalamount(amount);
						voempchecklistandResignationStatus.setApproverName(employee.getCandidate().getFirstName() + " "
								+ employee.getCandidate().getLastName() + " -" + employee.getId());
						voempchecklistandResignationStatus.setActionDate(HRMSDateUtil
								.format(employeeCatalogueEntity.getActedOn(), IHRMSConstants.FRONT_END_DATE_FORMAT));
						voempchecklistandResignationStatus.setStage(employeeCatalogueEntity.getCatalogue().getName());
						voempchecklistandResignationStatus.setStatus(employeeCatalogueEntity.getStatus());
						checkListVo.add(voempchecklistandResignationStatus);

					} else {
						ChecklistStatusVO voempchecklistandResignationStatus = new ChecklistStatusVO();
						log.info("employeeCatalogueEntity.getId()   =====  " + employeeCatalogueEntity.getId());
						List<MapEmployeeCatalogueChecklist> empcatalogueChecklistItemList = empcatalogueChecklistDAO
								.findByEmployeeCatalogueMapping(employeeCatalogueEntity.getId());
						double amount = 0;
						List<VOMapEmployeeCatalogueChecklist> checklist = new ArrayList<VOMapEmployeeCatalogueChecklist>();
						for (MapEmployeeCatalogueChecklist mapCatalogueChecklistItemEntity : empcatalogueChecklistItemList) {
							VOMapEmployeeCatalogueChecklist voempCataloguechecklist = HRMSEntityToModelMapper
									.convertToEmployeeChecklistMapping(mapCatalogueChecklistItemEntity);
							checklist.add(voempCataloguechecklist);
							amount = amount + mapCatalogueChecklistItemEntity.getAmount();
						}
						totalamountofCatalogue = totalamountofCatalogue + amount;
						// voempchecklistandResignationStatus.setEmpCatalogueChecklist(checklist);
						voempchecklistandResignationStatus.setTotalamount(amount);
						voempchecklistandResignationStatus.setApproverName(employeeReportingMngr
								.getEmployeeReportingManager().getReporingManager().getCandidate().getFirstName()
								+ " "
								+ employeeReportingMngr.getEmployeeReportingManager().getReporingManager()
										.getCandidate().getLastName()
								+ " -"
								+ employeeReportingMngr.getEmployeeReportingManager().getReporingManager().getId());
						voempchecklistandResignationStatus.setActionDate(HRMSDateUtil
								.format(employeeCatalogueEntity.getActedOn(), IHRMSConstants.FRONT_END_DATE_FORMAT));
						voempchecklistandResignationStatus.setStage(employeeCatalogueEntity.getCatalogue().getName());
						voempchecklistandResignationStatus.setStatus(employeeCatalogueEntity.getStatus());
						checkListVo.add(voempchecklistandResignationStatus);
					}
				}
			}
			HRMSBaseResponse<List<ChecklistStatusVO>> response = new HRMSBaseResponse<>();

			// response.setTotalRecord(totalCount);
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setResponseBody(checkListVo);
			response.setApplicationVersion(applicationVersion);
			return response;

		} else {
			throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.CandidateDoesnotExistMessage);
		}
	}

	@Override
	public HRMSBaseResponse<CheckListByApproverVO> getResignedEmployeeCheckList(Long employeeId, Long catalogueId)
			throws HRMSException {
		if (HRMSHelper.isNullOrEmpty(employeeId)) {
			employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		} else {
			Employee employeeEntity = employeeDAO.findById(employeeId).get();
			List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();

			if (!HRMSHelper.isRolePresent(roles, ERole.HR.name())) {
				if (employeeEntity.getEmployeeReportingManager().getReporingManager()
						.getId() != SecurityFilter.TL_CLAIMS.get().getEmployeeId()) {

//					MapCatalogue catalauge = catalogueDAO.findByApproverOrganizationDivision(
//							SecurityFilter.TL_CLAIMS.get().getEmployeeId(),
//							employeeEntity.getCandidate().getLoginEntity().getOrganization().getId(),
//							employeeEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
//							IHRMSConstants.isActive);

					////

					List<MapCatalogue> catalauge = catalogueDAO.findByApproverOrganizationDivisionId(
							SecurityFilter.TL_CLAIMS.get().getEmployeeId(),
							employeeEntity.getCandidate().getLoginEntity().getOrganization().getId(),
							// loggedInEmployee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
							IHRMSConstants.isActive);

					List<Long> divisionIds = new ArrayList<>();
					List<Long> catalogueIds = new ArrayList<>();

					for (MapCatalogue catalogue : catalauge) {
						divisionIds.add(catalogue.getDevision().getId());
						catalogueIds.add(catalogue.getId());
					}

					if (!HRMSHelper.isNullOrEmpty(catalauge)
							&& (HRMSHelper.isNullOrEmpty(catalogueIds) || !catalauge.stream().anyMatch(e -> e
									.getApprover().getId().equals(SecurityFilter.TL_CLAIMS.get().getEmployeeId())))) {

						throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
					}

				}
			}
		}

		if (employeeId != 0) {
			List<ChecklistVO> listObject = new ArrayList<ChecklistVO>();
			Employee employeeEntity = employeeDAO.findById(employeeId).get();
			MapCatalogue catalauge = catalogueDAO.findByApproverOrganizationDivisionDepartment(
					SecurityFilter.TL_CLAIMS.get().getEmployeeId(),
					employeeEntity.getCandidate().getLoginEntity().getOrganization().getId(),
					employeeEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
					employeeEntity.getCandidate().getCandidateProfessionalDetail().getDepartment().getId());
			String path = rootDirectory + "/" + employeeEntity.getCandidate().getLoginEntity().getOrganization().getId()
					+ "/" + employeeEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId() + "/"
					+ employeeEntity.getCandidate().getCandidateProfessionalDetail().getBranch().getId() + "/"
					+ employeeEntity.getCandidate().getId() + "/Separation/";
			List<MapEmployeeCatalogue> employeeCatlogueMappingList = new ArrayList();
			if (HRMSHelper.isNullOrEmpty(catalogueId)) {
				employeeCatlogueMappingList = employeeCatalogueMappingDAO.findByEmployeeIdList(employeeId,
						IHRMSConstants.isActive);
			} else {
				employeeCatlogueMappingList = employeeCatalogueMappingDAO.findByEmployeeIdAndCatalogueList(employeeId,
						catalogueId, IHRMSConstants.isActive);
			}

			if (HRMSHelper.isNullOrEmpty(employeeCatlogueMappingList)) {
				EmployeeSeparationDetails employeeSeparationDetailsEntity;

				employeeSeparationDetailsEntity = employeeSeparationDAO
						.findSeparationDetailsUsingIdandIsActive(IHRMSConstants.isActive, employeeId);
				if (!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity)) {
					if (HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity.getRoApproverStatus())) {
						throw new HRMSException(1547, ResponseCode.getResponseCodeMap().get(1547));
					} else {
						if (HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity.getOrgApproverStatus())) {
							throw new HRMSException(1548, ResponseCode.getResponseCodeMap().get(1548));
						}
					}

				} else {

					throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
				}
			}
			double amount = 0.0;
			for (MapEmployeeCatalogue employeeCatlogueMapping : employeeCatlogueMappingList) {
				if (employeeCatlogueMapping != null) {
					List<MapEmployeeCatalogueChecklist> employeeCheckList = employeeChecklistMappingDAO
							.findByEmployeeCatalogueMapping(employeeCatlogueMapping.getId());
					for (MapEmployeeCatalogueChecklist entity : employeeCheckList) {
						if (!HRMSHelper.isNullOrEmpty(entity.getEmployeeCatalogueMapping().getCatalogueProof())) {
							entity.getEmployeeCatalogueMapping()
									.setCatalogueProof(path + entity.getEmployeeCatalogueMapping().getCatalogueProof());
						}

						ChecklistVO model = HRMSEntityToModelMapper.convertToChecklistVOMapping(entity);
						amount = amount + entity.getAmount();
						listObject.add(model);
					}

				}
			}
			CheckListByApproverVO checkListByApproverVO = new CheckListByApproverVO();
			Map<String, List<ChecklistVO>> map = listObject.stream()
					.collect(Collectors.groupingBy(u -> u.getChecklistApprover()));
			Map<String, ChecklistResponseVO> responseMap = new HashedMap<>();
			for (String approver : map.keySet()) {
				ChecklistResponseVO responseVo = new ChecklistResponseVO();
				double totalAmount = 0;
				for (ChecklistVO checkList : map.get(approver)) {

					totalAmount = totalAmount + checkList.getAmount();
				}
				// responseVo.setApprover(approver);
				responseVo.setChecklist(map.get(approver));
				responseVo.setTotalAmount(totalAmount);
				responseMap.put(approver, responseVo);
			}
			checkListByApproverVO.setChecklist(responseMap);
			// HRMSBaseResponse<List<ChecklistVO>> response = new HRMSBaseResponse<>();
			HRMSBaseResponse<CheckListByApproverVO> response = new HRMSBaseResponse<>();
			// response.setTotalRecord(totalCount);
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			// response.setResponseBody(listObject);
			response.setResponseBody(checkListByApproverVO);

			response.setApplicationVersion(applicationVersion);
			return response;

		} else {
			throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
		}

	}

	@Override
	public HRMSBaseResponse<List<EmployeeSeprationDetailsResponseVO>> getAllResignedEmployeeList(Pageable pageable)
			throws HRMSException {
		log.info("inside getAllResignedEmployeeList method");
		HRMSBaseResponse<List<EmployeeSeprationDetailsResponseVO>> response = new HRMSBaseResponse<>();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, IHRMSConstants.isActive);

		int totalCount = 0;
		List<EmployeeSeprationDetailsResponseVO> employeeResponseVOs = new ArrayList<EmployeeSeprationDetailsResponseVO>();

		List<MasterOrganizationEmailConfig> masterConfigEmialList = configDAO
				.findBYorgLevelEmployeeAndOrgId(loggedInEmployee.getId(), SecurityFilter.TL_CLAIMS.get().getOrgId());
		if (!HRMSHelper.isNullOrEmpty(masterConfigEmialList)) {
			List<EmployeeSeparationDetails> empseparationDetailsEntity = null;
			long orgId = loggedInEmployee.getCandidate().getLoginEntity().getOrganization().getId();
			empseparationDetailsEntity = employeeSeparationDAO.findAllResignedEmployee(ERecordStatus.Y.name(),
					IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING, orgId, pageable);
			totalCount = employeeSeparationDAO.countAllResignedEmployee(ERecordStatus.Y.name(),
					IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING, orgId);

			if (HRMSHelper.isNullOrEmpty(empseparationDetailsEntity)) {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}

			EmployeeSeprationDetailsResponseVO seprationDetailsVO = new EmployeeSeprationDetailsResponseVO();
			// separationDetails
			for (EmployeeSeparationDetails a : empseparationDetailsEntity) {
				seprationDetailsVO = new EmployeeSeprationDetailsResponseVO();
				// VOEmployeeSeparationDetails voEmployeeSeparationDetails =
				// HRMSEntityToModelMapper
				// .convertToEmpSeparationDetailsVO(employeeSeparationDetailsEntity);
				// listResponse.add(voEmployeeSeparationDetails);
				seprationDetailsVO.setId(a.getId());
				seprationDetailsVO.setEmployeeComment(a.getEmployeeComment());
				seprationDetailsVO.setEmployeeId(a.getEmployee().getId());
				seprationDetailsVO.setEmployeeName(a.getEmployee().getCandidate().getFirstName() + " "
						+ a.getEmployee().getCandidate().getLastName());
				seprationDetailsVO.setNoticePeriod(a.getNoticeperiod());
				seprationDetailsVO.setRelievingDate(a.getActualRelievingDate());
				seprationDetailsVO.setResignationDate(a.getResignationDate());
				seprationDetailsVO.setResignationReason(!HRMSHelper.isNullOrEmpty(a.getEmpSeparationReason())
						? a.getEmpSeparationReason().getReasonName()
						: null);
				seprationDetailsVO.setEmployeeAction(a.getEmployeeAction());
				seprationDetailsVO.setEmpCancelComment(a.getEmpWdComment());
				seprationDetailsVO.setEmpWdDate(a.getEmpWdDate());
				seprationDetailsVO.setHrActionDate(a.getHrActionDate());
				seprationDetailsVO.setHrApproverStatus(a.getHrApproverStatus());
				seprationDetailsVO.setHrComment(a.getHRComment());
				seprationDetailsVO.setHrReason(
						!HRMSHelper.isNullOrEmpty(a.getHRReason()) ? a.getHRReason().getReasonName() : null);
				seprationDetailsVO.setHrWdAction(a.getHr_WdActionStatus());
				seprationDetailsVO.setHrWdActionDate(a.getHr_WdActionDate());
				seprationDetailsVO.setHrWdComment(a.getHr_WdComment());
				seprationDetailsVO.setOrgActionDate(a.getOrgActionDate());
				seprationDetailsVO.setOrgApproverStatus(a.getOrgApproverStatus());
				seprationDetailsVO.setOrgComment(a.getOrgComment());
				seprationDetailsVO.setOrgLevelEmpWdAction(a.getOrg_level_emp_WdActionStatus());
				seprationDetailsVO.setOrgLevelEmpWdActionDate(a.getOrg_level_emp_Wd_action_Date());
				seprationDetailsVO.setOrgLevelEmpWdComment(a.getOrg_level_emp_WdComment());
				seprationDetailsVO.setOrgReason(
						!HRMSHelper.isNullOrEmpty(a.getOrgReason()) ? a.getOrgReason().getReasonName() : null);
				seprationDetailsVO.setRoActionDate(a.getRoActionDate());
				seprationDetailsVO.setRoApproverStatus(a.getRoApproverStatus());
				seprationDetailsVO.setRoComment(a.getRoComment());
				seprationDetailsVO.setRoReason(
						!HRMSHelper.isNullOrEmpty(a.getRoReason()) ? a.getRoReason().getReasonName() : null);
				seprationDetailsVO.setRoWdAction(a.getROWdActionStatus());
				seprationDetailsVO.setRoWdActionDate(a.getROWdActionDate());
				seprationDetailsVO.setRoWdComment(a.getROWdComment());
				seprationDetailsVO.setStatus(a.getStatus());
				seprationDetailsVO.setSystemEscalatedLevel(a.getSystemEscalatedLevel());

				employeeResponseVOs.add(seprationDetailsVO);
			}
		} else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}
		response.setTotalRecord(totalCount);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseBody(employeeResponseVOs);
		response.setApplicationVersion(applicationVersion);
		log.info("exit from getAllResignedEmployeeList method");
		return response;
	}

	@Override
	public HRMSBaseResponse<?> approveOrgLevelResignation(EmployeeSeparationRequestVO voEmployeeSeparationDetails)
			throws HRMSException, ParseException {

		log.info("Inside org approve separation method");
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		SeparationAuthorityHelper.orgLevelApprovalInputValidation(voEmployeeSeparationDetails);

		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee employee = employeeDAO.findById(empId).get();

		MasterOrganizationEmailConfig masterOrganizationEmailConfigEntity = configDAO.findByorganizationAnddivision(
				employee.getCandidate().getLoginEntity().getOrganization().getId(),
				employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId());

		Long orgEmpId = masterOrganizationEmailConfigEntity.getOrgLevelEmployee().getId();
		EmployeeSeparationDetails employeeSeparationDetailsEntity;
		EmployeeSeparationDetailsWithHistory employeeSeparationDetailsEntityHistory;
		Employee employeeEntity;
		if (empId == orgEmpId) {
			if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails)) {

				employeeSeparationDetailsEntity = employeeSeparationDAO.findSeparationDetailsUsingIdandActive(
						IHRMSConstants.isActive, voEmployeeSeparationDetails.getId());
				employeeSeparationDetailsEntityHistory = new EmployeeSeparationDetailsWithHistory();

				if (!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity.getOrgApproverStatus())) {
					throw new HRMSException(1538, ResponseCode.getResponseCodeMap().get(1538));
				}

				employeeEntity = employeeDAO.findById(employeeSeparationDetailsEntity.getEmployee().getId()).get();
				if (!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity)) {

					if (!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity.getRoApproverStatus())
							&& employeeSeparationDetailsEntity.getRoApproverStatus()
									.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED)) {

						employeeSeparationDetailsEntity
								.setOrgApproverStatus(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED);
						employeeSeparationDetailsEntity.setOrgComment(voEmployeeSeparationDetails.getOrgComment());
						employeeSeparationDetailsEntity.setOrgActionDate(new Date());

						DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
						String dateString = voEmployeeSeparationDetails.getActualRelievingDate();

						try {
							Date actualRelievingDate = formatter.parse(dateString);
							employeeSeparationDetailsEntity.setActualRelievingDate(actualRelievingDate);
						} catch (ParseException e) {

							e.printStackTrace();
						}

						employeeSeparationDetailsEntity.setUpdatedDate(new Date());
						employeeSeparationDetailsEntity.setUpdatedBy(empId.toString());

						if (employeeSeparationDetailsEntity.getRoApproverStatus() == null) {
							throw new HRMSException(IHRMSConstants.SEPARATIONWRONGACTIONCODE,
									IHRMSConstants.EMPLOYEESEPARATIONWRONACTION_MESSAGE);
						} else {

							addEntryInChecklistTables(voEmployeeSeparationDetails, employeeEntity);
						}

					} else {

						throw new HRMSException(1541, ResponseCode.getResponseCodeMap().get(1541));
					}

				} else {

					throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
				}
				// actionbyApproversForSeparation(employee, voEmployeeSeparationDetails);
				employeeSeparationDetailsEntity = employeeSeparationDAO.save(employeeSeparationDetailsEntity);

				response.setResponseCode(1200);
				response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
				response.setApplicationVersion(applicationVersion);

				log.info("Exit from org level approve separation method");
				return response;

			} else
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);

		} else
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
	}

	@Override
	public HRMSBaseResponse<?> submitChecklistV2(List<ChecklistSubmitVo> submitedChecklist) throws HRMSException {

		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		int orginalChecklistCount = 0;
		int submitedChecklistCount = 0;
		if (submitedChecklist != null) {
			for (ChecklistSubmitVo empChecklist : submitedChecklist) {
				SeparationAuthorityHelper.submitChecklistInputValidation(empChecklist);
				if (HRMSHelper.isNullOrEmpty(empChecklist.getId())) {
					throw new HRMSException(1537, ResponseCode.getResponseCodeMap().get(1537));
				}
				if (HRMSHelper.isNullOrEmpty(empChecklist.getCatalogueId())) {
					throw new HRMSException(1539, ResponseCode.getResponseCodeMap().get(1539));
				}

				MapEmployeeCatalogueChecklist entity = employeeCatalogueChecklistDAO.findById(empChecklist.getId())
						.get();
				Long resignedEmployeeId = entity.getEmployeeCatalogueMapping().getResignedEmployee().getId();
				// validation
				Long approverId = mapCatalogueDAO.findApproverIdByCatalogueIdAndOrgId(empChecklist.getCatalogueId(),
						SecurityFilter.TL_CLAIMS.get().getOrgId());
				if (approverId == null) {
					// Retrieve resigned employee ID

					// Get RO ID using resigned employee ID
					Long roId = employeeReportingManagerDAO.findReportingManagerIdByEmployeeId(resignedEmployeeId);

					// Check if token employee ID matches RO ID
					if (!employeeId.equals(roId)) {
						throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
					}
				} else if (!employeeId.equals(approverId)) {
					throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
				}

				if (orginalChecklistCount == 0) {
					log.info(" Getting Count ");
					orginalChecklistCount = catalogueChecklistDAO.countByCatalogueIdAndOrgId(
							empChecklist.getCatalogueId(), SecurityFilter.TL_CLAIMS.get().getOrgId());
				}
				if (empChecklist.isHaveCollected()) {
					submitedChecklistCount++;
				}
				MapEmployeeCatalogue catalogue = employeeCatalogueDAO.findByIdAndResignedEmployee(
						empChecklist.getCatalogueId(),
						entity.getEmployeeCatalogueMapping().getResignedEmployee().getId(), IHRMSConstants.isActive);
				if (!HRMSHelper.isNullOrEmpty(catalogue)
						&& IHRMSConstants.EMPLOYEE_CATALOGUE_CHECKLIST_SUBMITTED.equals(catalogue.getStatus())) {
					throw new HRMSException(1540, ResponseCode.getResponseCodeMap().get(1540));
				}
			}

			if (orginalChecklistCount == submitedChecklistCount) {

				log.info(" All Items Submited ");
				HRMSListResponseObject mainResponse = new HRMSListResponseObject();
				// List<Object> list = new ArrayList<Object>();
				MapEmployeeCatalogue employeeCatalogueMappingEntity = null;

				for (ChecklistSubmitVo model : submitedChecklist) {

					MapEmployeeCatalogueChecklist entity = employeeCatalogueChecklistDAO.findById(model.getId()).get();
					if (employeeCatalogueMappingEntity == null) {
						employeeCatalogueMappingEntity = employeeCatalogueDAO.findByIdAndResignedEmployee(
								model.getCatalogueId(),
								entity.getEmployeeCatalogueMapping().getResignedEmployee().getId(),
								IHRMSConstants.isActive);
					}

					employeeCatalogueMappingEntity.setStatus(IHRMSConstants.EMPLOYEE_CATALOGUE_CHECKLIST_SUBMITTED);
					employeeCatalogueMappingEntity.setActedOn(new Date());
					if (entity != null) {
						entity = SeparationTransformUtils.convertToEmployeeCatalogueChecklistEntity(model, entity);
						entity = employeeCatalogueChecklistDAO.save(entity);
						model = SeparationTransformUtils.convertToEmployeeChecklistMapping(entity);
						// list.add(model);
					}
					/*
					 * else { throw new HRMSException(1501,
					 * ResponseCode.getResponseCodeMap().get(1501)); }
					 */

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
				log.info(" employee catalogue size===> " + mapemployeeCatalogueList.size());
				for (MapEmployeeCatalogue employeecatalogueEntity : mapemployeeCatalogueList) {
					if (employeecatalogueEntity.getStatus()
							.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_CATALOGUE_CHECKLIST_SUBMITTED)) {
						count++;
						if (count == mapemployeeCatalogueList.size())
							sendMailtoHR(employeecatalogueEntity.getResignedEmployee());
					}
				}

			} else {
				throw new HRMSException(IHRMSConstants.ALL_CATLOGUE_CHECKLIST_NOT_SUBMITED_CODE,
						IHRMSConstants.ALL_CATALOGUE_CHECKLIST_NOT_SUBMITED_MESSAGE);
			}

		} else {
			throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
		}

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1593));
		response.setApplicationVersion(applicationVersion);
		return response;
	}

	private void sendMailtoHR(Employee employeeEntity) {
		String hrEmailIds = "";
		String recipientEmail = employeeEntity.getOfficialEmailId();
		List<OrgDivWiseHRMapping> orgDivHRList = orgDivHRDAO.findOrgDivWiseHrMapping(
				employeeEntity.getCandidate().getLoginEntity().getOrganization().getId(),
				employeeEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				IHRMSConstants.isActive);
		for (OrgDivWiseHRMapping orgDivHRMApEntity : orgDivHRList) {
			hrEmailIds = hrEmailIds + orgDivHRMApEntity.getEmployee().getOfficialEmailId() + ";";
		}
		Map<String, String> placeHolderMapping = HRMSRequestTranslator
				.createPlaceHolderMapAfterExitFeedbackFormSubmitted(employeeEntity);
		// placeHolderMapping.put("{websiteURL}", baseURL);

//		String mailContent = HRMSHelper.replaceString(placeHolderMapping,
//				IHRMSEmailTemplateConstants.Template_FOR_ALL_APPROVE_HANDOVERCHECKLIST_SUBMITTED);
//
//		String mailSubject = IHRMSConstants.MailSubject_ALL_HANDOVER_CHECKLIST_SUBMITTED;
//
//		emailsender.toPersistEmail(hrEmailIds, null, mailContent, mailSubject,
//				employeeEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
//				employeeEntity.getCandidate().getLoginEntity().getOrganization().getId());

		EmailTemplate template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
				IHRMSConstants.Department_Checklist_Approved, IHRMSConstants.isActive,
				employeeEntity.getCandidate().getCandidateProfessionalDetail().getDivision(),
				employeeEntity.getCandidate().getLoginEntity().getOrganization());

		placeHolderMapping.put("{websiteURL}", baseURL);

		String mailBody = HRMSHelper.replaceString(placeHolderMapping, template.getTemplate());

		emailsender.toPersistEmail(recipientEmail, hrEmailIds, mailBody, template.getEmailSubject(),
				employeeEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				employeeEntity.getCandidate().getLoginEntity().getOrganization().getId());

	}

	@Override
	public HRMSBaseResponse<?> rejectOrgLevelResignation(EmployeeSeparationRequestVO voEmployeeSeparationDetails)
			throws HRMSException, ParseException {

		log.info("Inside org reject separation method");
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();

		// Validating input
		SeparationAuthorityHelper.orgLevelRejectInputValidation(voEmployeeSeparationDetails);

		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee employee = employeeDAO.findById(empId).get();

		MasterOrganizationEmailConfig masterOrganizationEmailConfigEntity = configDAO.findByorganizationAnddivision(
				employee.getCandidate().getLoginEntity().getOrganization().getId(),
				employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId());

		Long orgEmpId = masterOrganizationEmailConfigEntity.getOrgLevelEmployee().getId();

		if (empId == orgEmpId) {

			// Check if org approved status is already approved or rejected
			EmployeeSeparationDetails empseparationDetailsEntity = employeeSeparationDAO
					.findSeparationDetailsUsingIdandActive(ERecordStatus.Y.name(), voEmployeeSeparationDetails.getId());

			if (HRMSHelper.isNullOrEmpty(empseparationDetailsEntity)) {
				// Handle if separation details are not found
				throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
			}

			if (IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED
					.equals(empseparationDetailsEntity.getOrgApproverStatus())) {
				throw new HRMSException(1542, ResponseCode.getResponseCodeMap().get(1542));
			} else if (IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED
					.equals(empseparationDetailsEntity.getOrgApproverStatus())) {
				throw new HRMSException(1543, ResponseCode.getResponseCodeMap().get(1543));
			} else if (IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED
					.equals(empseparationDetailsEntity.getRoApproverStatus())) {
				throw new HRMSException(1544, ResponseCode.getResponseCodeMap().get(1544));
			}

			if (HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getSeparationReasonVO().getId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Reason Id");
			}

			if (!HRMSHelper.regexMatcher(String.valueOf(voEmployeeSeparationDetails.getSeparationReasonVO().getId()),
					"[0-9]+")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Reason Id ");
			}

			if (HRMSHelper.isLongZero(voEmployeeSeparationDetails.getSeparationReasonVO().getId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + "Reason Id ");
			}

			// If not already approved or rejected, proceed with approval
			MasterModeofSeparationReason empSeaparationReasonEntity = mstSeparationReasonDAO
					.findByReasonId(voEmployeeSeparationDetails.getSeparationReasonVO().getId());

			if (IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED
					.equals(empseparationDetailsEntity.getRoApproverStatus())) {
				empseparationDetailsEntity.setOrgActionDate(new Date());
				empseparationDetailsEntity.setUpdatedDate(new Date());
				empseparationDetailsEntity.setIsActive(IHRMSConstants.isNotActive);
				empseparationDetailsEntity.setOrgApproverStatus(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED);
				empseparationDetailsEntity.setStatus(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED);
				empseparationDetailsEntity.setOrgComment(voEmployeeSeparationDetails.getOrgComment());
				empseparationDetailsEntity.setOrgReason(empSeaparationReasonEntity);
				actionbyApproversForSeparation(employee, voEmployeeSeparationDetails);
				employeeSeparationDAO.save(empseparationDetailsEntity);

			} else {
				// Handle if ro approved status is not approved
				throw new HRMSException(1545, ResponseCode.getResponseCodeMap().get(1545));
			}

		} else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);
		return response;
	}

	@Override
	public HRMSBaseResponse<List<FeedbackQuestionVO>> getFeedBackQuestions() throws HRMSException {
		log.info("Entered in getFeedBackQuestions method");
		HRMSBaseResponse<List<FeedbackQuestionVO>> response = new HRMSBaseResponse<>();

		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee employee = employeeDAO.findById(empId).get();

		List<FeedbackQuestion> feedbackQuestionEntityList = new ArrayList<>();
		List<FeedbackQuestionVO> voFeedbackQuestionList = new ArrayList<>();
		HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
		// masterBranchEntityList = masterBranchDAO.findAll();
		feedbackQuestionEntityList = feedbackQuestionDAO.findAllFeedbackQuestionAndOptionByOrgIdCustomQuery(
				employee.getCandidate().getLoginEntity().getOrganization().getId(),
				IHRMSConstants.isActive/* , IHRMSConstants.isActive */);
		if (!HRMSHelper.isNullOrEmpty(feedbackQuestionEntityList)) {
			voFeedbackQuestionList = SeparationTransformUtils.transalteToFeedbackQuestionVO(feedbackQuestionEntityList,
					voFeedbackQuestionList);

		} else {
			throw new HRMSException(1527, ResponseCode.getResponseCodeMap().get(1527));
		}

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseBody(voFeedbackQuestionList);
		response.setApplicationVersion(applicationVersion);
		log.info("exit from getFeedBackQuestions method");

		return response;
	}

	@Override
	public HRMSBaseResponse<List<CandidateLetterVO>> getSeparationDocumentList() throws HRMSException {
		HRMSBaseResponse<List<CandidateLetterVO>> response = new HRMSBaseResponse<>();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee employee = employeeDAO.findById(empId).get();

		Candidate candidate = candidateDAO.findById(employee.getCandidate().getId()).get();

		if (candidate != null) {
			log.info("BASE URL FROM SERVICE" + baseURL);
			List<CandidateLetter> candidateLetter = letterDAO.getLetterListByCandidateId(candidate.getId());
			// Set<CandidateLetter> candidateLetter = candidate.getLetters();
			List<CandidateLetterVO> letterModelList = new ArrayList<CandidateLetterVO>();
			CandidateLetterVO letterModel = null;
			if (HRMSHelper.isNullOrEmpty(candidateLetter)) {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}
			for (CandidateLetter letter : candidateLetter) {
				letterModel = SeparationTransformUtils.convertToCandidateLetterModel(letter, candidate);
				letterModelList.add(letterModel);

			}
			response.setResponseBody(letterModelList);
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}

	@Override
	public HRMSBaseResponse<?> createEmployeeFeedback(SubmittedEmployeeFeedbackVO empFb) throws HRMSException {
		HRMSBaseResponse<List<SubmittedEmployeeFeedbackVO>> response = new HRMSBaseResponse<>();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee employee = employeeDAO.findById(empId).get();
		String resultMesage = "";

		List<MapEmployeeCatalogue> resignEmployee = empCatalogueMapDAO.findByEmployeeId(empId);
		if (HRMSHelper.isNullOrEmpty(resignEmployee)) {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
//		for (MapEmployeeCatalogue empCatalogue : resignEmployee) {
//			if (empCatalogue.getStatus().equalsIgnoreCase(IHRMSConstants.PENDING)) {
//				throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
//			}
//		}

		SeparationAuthorityHelper.submittedEmployeeFeedbackInputValidation(empFb);
		List<EmployeeExitFeedbackVO> employeeFeedbackVoList = new ArrayList<EmployeeExitFeedbackVO>();

		if (!HRMSHelper.isNullOrEmpty(empFb) && !HRMSHelper.isNullOrEmpty(empFb.getEmpFeedbackList())
				&& !HRMSHelper.isNullOrEmpty(empId)) {

			List<EmployeeFeedback> employeeFeedbackEntityList = employeeFeedbackDAO.findByEmployeeCustomQuery(empId);
			if (employeeFeedbackEntityList.size() > 0) {
				throw new HRMSException(1527, ResponseCode.getResponseCodeMap().get(1527));
			}

			employeeFeedbackVoList = SeparationTransformUtils.translateToEmployeeFeedbackVoList(empFb, empId);
			Employee employeeEntity = employeeDAO.findById(empId).get();
			for (EmployeeExitFeedbackVO voEmployeeFeedback : employeeFeedbackVoList) {
				EmployeeFeedback employeeFeedbackEntity;
				FeedbackQuestion feedbackQuestionEntity;
				FeedbackOption feedbackOptionEntity;
				// VOEmployeeFeedback voEmployeeFeedback = new VOEmployeeFeedback();

//				employeeFeedbackEntity = employeeFeedbackDAO.findById(voEmployeeFeedback.getId()).orElse(null);
				feedbackQuestionEntity = questionDAO.findById(voEmployeeFeedback.getFeedbackQuestion().getId()).get();
				feedbackOptionEntity = optionDAO.findById(voEmployeeFeedback.getFeedbackOption().getId()).orElse(null);
				log.info(":::::::::::::::::" + feedbackQuestionEntity.getQuestionName());
				employeeFeedbackEntity = new EmployeeFeedback();

				employeeFeedbackEntity = SeparationTransformUtils
						.translateToEmployeeFeedbackEntity(employeeFeedbackEntity, voEmployeeFeedback, empId);
				employeeFeedbackEntity.setFeedbackQuestion(feedbackQuestionEntity);
				employeeFeedbackEntity.setFeedbackOption(feedbackOptionEntity);
				employeeFeedbackEntity.setEmployee(employeeEntity);
				// resultMesage = IHRMSConstants.addedsuccessMessage;
				resultMesage = IHRMSConstants.successMessage;

				employeeFeedbackDAO.save(employeeFeedbackEntity);

			}
			// commented because this mail is not required as per hr requirement
			// SendEmailofFeedbackTOHRAndRO(employeeEntity);
//				throw new HRMSException(1527, ResponseCode.getResponseCodeMap().get(1527));
		} else {
			throw new HRMSException(1527, ResponseCode.getResponseCodeMap().get(1527));
		}

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);
		log.info("exit from getFeedBackQuestions method");

		return response;

	}

	private void SendEmailofFeedbackTOHRAndRO(Employee employeeEntity) {

		String hrEmailIds = "";
		List<OrgDivWiseHRMapping> orgDivHRList = orgDivHRDAO.findOrgDivWiseHrMapping(
				employeeEntity.getCandidate().getLoginEntity().getOrganization().getId(),
				employeeEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				IHRMSConstants.isActive);
		for (OrgDivWiseHRMapping orgDivHRMApEntity : orgDivHRList) {
			hrEmailIds = hrEmailIds + orgDivHRMApEntity.getEmployee().getOfficialEmailId() + ";";
		}
		String roEmailId = employeeEntity.getEmployeeReportingManager().getReporingManager().getOfficialEmailId();
		Map<String, String> placeHolderMapping = HRMSRequestTranslator
				.createPlaceHolderMapAfterExitFeedbackFormSubmitted(employeeEntity);

//		placeHolderMapping.put("{websiteURL}", baseURL);
//
//		String mailContent = HRMSHelper.replaceString(placeHolderMapping,
//				IHRMSEmailTemplateConstants.Template_Empployee_SUBMIT_EXIT_FEEDBACK_FORM);
//
//		String mailSubject = IHRMSConstants.MailSubject_Exit_Feedback_Form_Submit;
//
//		emailsender.toPersistEmail(hrEmailIds, roEmailId, mailContent, mailSubject,
//				employeeEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
//				employeeEntity.getCandidate().getLoginEntity().getOrganization().getId());

		EmailTemplate template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
				IHRMSConstants.Exit_Feedback_Form_Submitted, IHRMSConstants.isActive,
				employeeEntity.getCandidate().getCandidateProfessionalDetail().getDivision(),
				employeeEntity.getCandidate().getLoginEntity().getOrganization());

		placeHolderMapping.put("{websiteURL}", baseURL);
		String mailBody = HRMSHelper.replaceString(placeHolderMapping, template.getTemplate());

		emailsender.toPersistEmail(hrEmailIds, roEmailId, mailBody, template.getEmailSubject(),
				employeeEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				employeeEntity.getCandidate().getLoginEntity().getOrganization().getId());

	}

	/***********
	 * API to get resigned employee list for approvers
	 ********************/

	@Override
	public HRMSBaseResponse<List<EmployeeSeprationDetailsResponseVO>> getResignedEmployeeListForApprovers(
			String catalougeName, Pageable pageable) throws HRMSException {
		HRMSBaseResponse<List<EmployeeSeprationDetailsResponseVO>> response = new HRMSBaseResponse<>();
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee loggedInEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, IHRMSConstants.isActive);
		int totalCount = 0;
		List<EmployeeSeprationDetailsResponseVO> employeeResponseVOs = new ArrayList<EmployeeSeprationDetailsResponseVO>();

		List<MapCatalogue> catalauge = catalogueDAO.findByApproverOrganizationDivisionIdAndName(
				SecurityFilter.TL_CLAIMS.get().getEmployeeId(),
				loggedInEmployee.getCandidate().getLoginEntity().getOrganization().getId(),
				// loggedInEmployee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				IHRMSConstants.isActive, catalougeName);

		List<Long> divisionIds = new ArrayList<>();
		List<Long> catalogueIds = new ArrayList<>();

		for (MapCatalogue catalogue : catalauge) {
			divisionIds.add(catalogue.getDevision().getId());
			catalogueIds.add(catalogue.getId());
		}
		List<String> separationStatus = new ArrayList<String>();
		separationStatus.add(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING);
		separationStatus.add(IHRMSConstants.EMPLOYEE_ABSCONDED);
		separationStatus.add(IHRMSConstants.EMPLOYEE_TERMINATED);
		separationStatus.add(IHRMSConstants.EMPLOYEE_DEMISE);

		if (!HRMSHelper.isNullOrEmpty(catalauge) && (HRMSHelper.isNullOrEmpty(catalogueIds) || catalauge.stream()
				.anyMatch(e -> e.getApprover().getId().equals(SecurityFilter.TL_CLAIMS.get().getEmployeeId())))) {

			List<EmployeeSeparationDetails> empseparationDetailsEntity = null;
			long orgId = loggedInEmployee.getCandidate().getLoginEntity().getOrganization().getId();
			long divId = loggedInEmployee.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
			List<String> orgStatus = new ArrayList<String>();
			orgStatus.add(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED);
			orgStatus.add(IHRMSConstants.EMPLOYEE_SEPARATION_SYSTEM_ESCALATED);

			empseparationDetailsEntity = employeeSeparationDAO.findByResignedEmployeeForApproverss(ERecordStatus.Y.name(),
					IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING, orgStatus.toArray(), orgId, divisionIds,
					catalogueIds,separationStatus.toArray(), pageable);

			totalCount = employeeSeparationDAO.countByResignedEmployees(ERecordStatus.Y.name(),
					IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING, orgStatus.toArray(), orgId, divisionIds,
					catalogueIds,separationStatus.toArray());

			if (HRMSHelper.isNullOrEmpty(empseparationDetailsEntity)) {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}

			EmployeeSeprationDetailsResponseVO seprationDetailsVO = new EmployeeSeprationDetailsResponseVO();
			// separationDetails
			for (EmployeeSeparationDetails a : empseparationDetailsEntity) {
				seprationDetailsVO = new EmployeeSeprationDetailsResponseVO();
				// VOEmployeeSeparationDetails voEmployeeSeparationDetails =
				// HRMSEntityToModelMapper
				// .convertToEmpSeparationDetailsVO(employeeSeparationDetailsEntity);
				// listResponse.add(voEmployeeSeparationDetails);
				seprationDetailsVO.setId(a.getId());
				seprationDetailsVO.setEmployeeComment(a.getEmployeeComment());
				seprationDetailsVO.setEmployeeId(a.getEmployee().getId());
				seprationDetailsVO.setEmployeeName(a.getEmployee().getCandidate().getFirstName() + " "
						+ a.getEmployee().getCandidate().getLastName());
				seprationDetailsVO.setNoticePeriod(a.getNoticeperiod());
				seprationDetailsVO.setRelievingDate(a.getActualRelievingDate());
				seprationDetailsVO.setResignationDate(a.getResignationDate());
				seprationDetailsVO.setResignationReason(!HRMSHelper.isNullOrEmpty(a.getEmpSeparationReason())
						? a.getEmpSeparationReason().getReasonName()
						: null);
				seprationDetailsVO.setEmployeeAction(a.getEmployeeAction());
				seprationDetailsVO.setEmpCancelComment(a.getEmpWdComment());
				seprationDetailsVO.setEmpWdDate(a.getEmpWdDate());
				seprationDetailsVO.setHrActionDate(a.getHrActionDate());
				seprationDetailsVO.setHrApproverStatus(a.getHrApproverStatus());
				seprationDetailsVO.setHrComment(a.getHRComment());
				seprationDetailsVO.setHrReason(
						!HRMSHelper.isNullOrEmpty(a.getHRReason()) ? a.getHRReason().getReasonName() : null);
				seprationDetailsVO.setHrWdAction(a.getHr_WdActionStatus());
				seprationDetailsVO.setHrWdActionDate(a.getHr_WdActionDate());
				seprationDetailsVO.setHrWdComment(a.getHr_WdComment());
				seprationDetailsVO.setOrgActionDate(a.getOrgActionDate());
				seprationDetailsVO.setOrgApproverStatus(a.getOrgApproverStatus());
				seprationDetailsVO.setOrgComment(a.getOrgComment());
				seprationDetailsVO.setOrgLevelEmpWdAction(a.getOrg_level_emp_WdActionStatus());
				seprationDetailsVO.setOrgLevelEmpWdActionDate(a.getOrg_level_emp_Wd_action_Date());
				seprationDetailsVO.setOrgLevelEmpWdComment(a.getOrg_level_emp_WdComment());
				seprationDetailsVO.setOrgReason(
						!HRMSHelper.isNullOrEmpty(a.getOrgReason()) ? a.getOrgReason().getReasonName() : null);
				seprationDetailsVO.setRoActionDate(a.getRoActionDate());
				seprationDetailsVO.setRoApproverStatus(a.getRoApproverStatus());
				seprationDetailsVO.setRoComment(a.getRoComment());
				seprationDetailsVO.setRoReason(
						!HRMSHelper.isNullOrEmpty(a.getRoReason()) ? a.getRoReason().getReasonName() : null);
				seprationDetailsVO.setRoWdAction(a.getROWdActionStatus());
				seprationDetailsVO.setRoWdActionDate(a.getROWdActionDate());
				seprationDetailsVO.setRoWdComment(a.getROWdComment());
				seprationDetailsVO.setStatus(a.getStatus());
				seprationDetailsVO.setSystemEscalatedLevel(a.getSystemEscalatedLevel());
				employeeResponseVOs.add(seprationDetailsVO);
			}
		} else {
			throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
		}
		response.setTotalRecord(totalCount);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseBody(employeeResponseVOs);
		response.setApplicationVersion(applicationVersion);
		return response;

	}

	@Override
	public HRMSBaseResponse<List<EmployeeFeedback>> getEmployeeFeedback(Long employeeId) throws HRMSException {

		List<EmployeeFeedback> employeeFeedbackEntityList = new ArrayList<>();
		List<Object> voEmployeeFeedbackList = new ArrayList<>();
		HRMSBaseResponse<List<EmployeeFeedback>> response = new HRMSBaseResponse<>();

		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long empId = null;
		if (HRMSHelper.isRolePresent(roles, ERole.HR.name())) {
			if (!HRMSHelper.isNullOrEmpty(employeeId)) {
				empId = employeeId;
			} else {
				throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
			}

		} else {
			empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		}

		Employee employee = employeeDAO.findById(empId).get();

		if (!HRMSHelper.isLongZero(empId)) {

			// masterBranchEntityList = masterBranchDAO.findAll();
			Employee employeeEntity = employeeDAO.findById(empId).get();
			if (!HRMSHelper.isNullOrEmpty(employeeEntity)) {

				// employeeFeedbackEntityList =
				// employeeFeedbackDAO.findByEmployee(employeeEntity);
				employeeFeedbackEntityList = employeeFeedbackDAO.findByEmployeeCustomQuery(employeeEntity.getId());
				if (!HRMSHelper.isNullOrEmpty(employeeFeedbackEntityList)) {
					voEmployeeFeedbackList = SeparationTransformUtils
							.transalteToFeedbackVOList(employeeFeedbackEntityList, voEmployeeFeedbackList);

				} else {
					throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
				}

			}
		} else {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
		List<EmployeeFeedback> employeeFeedbackList = (List<EmployeeFeedback>) (List<?>) voEmployeeFeedbackList;
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setResponseBody(employeeFeedbackList);
		response.setApplicationVersion(applicationVersion);
		log.info("exit from getFeedBackQuestions method");

		return response;
	}

	/**
	 * This method is used when approvers take action on separation either on
	 * Separation
	 * 
	 * 
	 */
	public void actionbyApproversForSeparation(Employee empEntity,
			EmployeeSeparationRequestVO voEmployeeSeparationDetails) {
		log.info("Inside Mail sender");
		String employeeEmailId = empEntity.getOfficialEmailId();
		String reportingManagerEmailId = empEntity.getEmployeeReportingManager().getReporingManager()
				.getOfficialEmailId();
		long accountId = 0;
		MasterOrganizationEmailConfig masterOrganizationEmailConfigEntity = configDAO.findByorganizationAnddivision(
				empEntity.getCandidate().getLoginEntity().getOrganization().getId(),
				empEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId());
		String accountEmailId = "";
		String orgLevelEmailId = masterOrganizationEmailConfigEntity.getOrgLevelEmployee().getOfficialEmailId();
		List<MapCatalogue> mapCatalogueList = mapCatalogueDAO.findCatalogueListByOrgandDiv(
				empEntity.getCandidate().getLoginEntity().getOrganization().getId(),
				empEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId());
		for (MapCatalogue mapCatalogueEntity : mapCatalogueList) {
			if (mapCatalogueEntity.getName().contains("Account")) {
				accountId = mapCatalogueEntity.getApprover().getId();
				Employee accountEmployee = employeeDAO.findById(accountId).get();
				accountEmailId = accountEmployee.getOfficialEmailId();
			}
		}
		EmailTemplate template = null;
		String mailBody = null;
		String hrEmailIds = "";
		Map<String, String> placeHolderMapping = null;
		List<OrgDivWiseHRMapping> orgDivHRList = orgDivHRDAO.findOrgDivWiseHrMapping(
				empEntity.getCandidate().getLoginEntity().getOrganization().getId(),
				empEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				IHRMSConstants.isActive);
		for (OrgDivWiseHRMapping orgDivHRMApEntity : orgDivHRList) {
			hrEmailIds = hrEmailIds + orgDivHRMApEntity.getEmployee().getOfficialEmailId() + ";";
		}
		String ccEmailIds = "";
		if (HRMSHelper.isNullOrEmpty(accountEmailId)) {
			ccEmailIds = reportingManagerEmailId + ";" + orgLevelEmailId + ";" + hrEmailIds;
		} else {
			ccEmailIds = reportingManagerEmailId + ";" + orgLevelEmailId + ";" + accountEmailId + ";" + hrEmailIds;
		}

		EmployeeSeparationDetails employeeSeparationDetailsEntity;
		employeeSeparationDetailsEntity = employeeSeparationDAO
				.findSeparationDetailsUsingEmpIdandActive(IHRMSConstants.isActive, empEntity.getId());

		// String mailContent = HRMSHelper.replaceString(placeHolderMapping,
//				IHRMSEmailTemplateConstants.Template_Empployee_Resignation_Approve);

		String mailSubject = "";
		if (!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity)) {

			/*** when manager Rejects the resignation **/
			if (!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity.getRoApproverStatus())
					|| !HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity.getOrgApproverStatus())) {
				if ((!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity.getRoApproverStatus())
						&& employeeSeparationDetailsEntity.getRoApproverStatus()
								.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED))) {
					template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
							IHRMSConstants.Template_Employee_Resignation_Reject, IHRMSConstants.isActive,
							empEntity.getCandidate().getCandidateProfessionalDetail().getDivision(),
							empEntity.getCandidate().getLoginEntity().getOrganization());
					placeHolderMapping = HRMSRequestTranslator.createPlaceHolderForRejectSeparation(empEntity,
							voEmployeeSeparationDetails);
					placeHolderMapping.put("{websiteURL}", baseURL);
					placeHolderMapping.put("{empResignedDate}",
							HRMSDateUtil.format(employeeSeparationDetailsEntity.getResignationDate(),
									IHRMSConstants.FRONT_END_DATE_FORMAT));
					mailBody = HRMSHelper.replaceString(placeHolderMapping, template.getTemplate());
				}
				/*** when manager Approves the resignation **/
				if ((!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity.getRoApproverStatus())
						&& employeeSeparationDetailsEntity.getRoApproverStatus()
								.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED))) {
					template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
							IHRMSConstants.Template_Employee_Resignation_Approve, IHRMSConstants.isActive,
							empEntity.getCandidate().getCandidateProfessionalDetail().getDivision(),
							empEntity.getCandidate().getLoginEntity().getOrganization());

					placeHolderMapping = HRMSRequestTranslator.createPlaceHolderForApproveSeparation(empEntity,
							voEmployeeSeparationDetails);
					placeHolderMapping.put("{websiteURL}", baseURL);
					placeHolderMapping.put("{empResignedDate}",
							HRMSDateUtil.format(employeeSeparationDetailsEntity.getResignationDate(),
									IHRMSConstants.FRONT_END_DATE_FORMAT));
					mailBody = HRMSHelper.replaceString(placeHolderMapping, template.getTemplate());
				}
				/*** when HR Rejects the resignation **/
				if ((!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity.getOrgApproverStatus())
						&& employeeSeparationDetailsEntity.getOrgApproverStatus()
								.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED))) {
					template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
							IHRMSConstants.Template_Employee_Resignation_Rejected_by_HR, IHRMSConstants.isActive,
							empEntity.getCandidate().getCandidateProfessionalDetail().getDivision(),
							empEntity.getCandidate().getLoginEntity().getOrganization());
					placeHolderMapping = HRMSRequestTranslator.createPlaceHolderForRejectSeparation(empEntity,
							voEmployeeSeparationDetails);
					placeHolderMapping.put("{websiteURL}", baseURL);
					placeHolderMapping.put("{empResignedDate}",
							HRMSDateUtil.format(employeeSeparationDetailsEntity.getResignationDate(),
									IHRMSConstants.FRONT_END_DATE_FORMAT));
					mailBody = HRMSHelper.replaceString(placeHolderMapping, template.getTemplate());
				}
				/*** when HR Approves the resignation **/
				if (!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity.getOrgApproverStatus())
						&& employeeSeparationDetailsEntity.getOrgApproverStatus()
								.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED)) {
					placeHolderMapping = HRMSRequestTranslator.createPlaceHolderForApproveSeparation(empEntity,
							voEmployeeSeparationDetails);
					long divId = empEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
					if (divId == 3) {
						mailBody = HRMSHelper.replaceString(placeHolderMapping,
								IHRMSEmailTemplateConstants.Template_Empployee_Resignation_Approve_By_ORG_LEVEL_for_UAE);
					} else {

						mailBody = HRMSHelper.replaceString(placeHolderMapping,
								IHRMSEmailTemplateConstants.Template_Empployee_Resignation_Approve_By_ORG_LEVEL);
//					template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
//							IHRMSConstants.Template_Employee_Resignation_Approved_by_HR, IHRMSConstants.isActive,
//							empEntity.getCandidate().getCandidateProfessionalDetail().getDivision(),
//							empEntity.getCandidate().getLoginEntity().getOrganization());
//					mailBody = HRMSHelper.replaceString(placeHolderMapping, template.getTemplate());
//					placeHolderMapping.put("{websiteURL}", baseURL);
					}

					for (MapCatalogue mapCatalogueEntity : mapCatalogueList) {
						if (!HRMSHelper.isNullOrEmpty(mapCatalogueEntity.getApprover())) {
							long empId = mapCatalogueEntity.getApprover().getId();
							Employee employeeEntity = employeeDAO.findById(empId).get();
							ccEmailIds = ccEmailIds + employeeEntity.getOfficialEmailId() + ";";
						}
					}
				}
			}
		}
		log.info("ccemailids action by Approvers  " + ccEmailIds);
		emailsender.toPersistEmail(employeeEmailId, ccEmailIds, mailBody, template.getEmailSubject(),
				empEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				empEntity.getCandidate().getLoginEntity().getOrganization().getId());

	}
}
