package com.vinsys.hrms.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeReportingManager;
import com.vinsys.hrms.dao.IHRMSEmployeeSeparationDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeSeparationHistoryDAO;
import com.vinsys.hrms.dao.IHRMSMapCatalogue;
import com.vinsys.hrms.dao.IHRMSMapCatalogueChecklistItemDAO;
import com.vinsys.hrms.dao.IHRMSMapEmployeeCatalogueChecklistDAO;
import com.vinsys.hrms.dao.IHRMSMapEmployeeCatalogueMappingDAO;
import com.vinsys.hrms.dao.IHRMSMapOrgDivHrDAO;
import com.vinsys.hrms.dao.IHRMSMasterCityDAO;
import com.vinsys.hrms.dao.IHRMSMasterCountryDAO;
import com.vinsys.hrms.dao.IHRMSMasterDepartmentDAO;
import com.vinsys.hrms.dao.IHRMSMasterModeofSeparationDAO;
import com.vinsys.hrms.dao.IHRMSMasterModeofSeparationReasonDAO;
import com.vinsys.hrms.dao.IHRMSMasterNoticePeriod;
import com.vinsys.hrms.dao.IHRMSMasterOrganizationEmailConfigDAO;
import com.vinsys.hrms.dao.IHRMSMasterStateDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOEmployee;
import com.vinsys.hrms.datamodel.VOEmployeeSeparationChecklistandResignationStatus;
import com.vinsys.hrms.datamodel.VOEmployeeSeparationDetails;
import com.vinsys.hrms.datamodel.VOMapEmployeeCatalogueChecklist;
import com.vinsys.hrms.datamodel.VOPendingChecklistRo;
import com.vinsys.hrms.datamodel.attendance.VOEmployeeAttendanceReport;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateAddress;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeReportingManager;
import com.vinsys.hrms.entity.EmployeeSeparationDetails;
import com.vinsys.hrms.entity.EmployeeSeparationDetailsWithHistory;
import com.vinsys.hrms.entity.MapCatalogue;
import com.vinsys.hrms.entity.MapCatalogueChecklistItem;
import com.vinsys.hrms.entity.MapEmployeeCatalogue;
import com.vinsys.hrms.entity.MapEmployeeCatalogueChecklist;
import com.vinsys.hrms.entity.MasterModeofSeparation;
import com.vinsys.hrms.entity.MasterModeofSeparationReason;
import com.vinsys.hrms.entity.MasterOrganizationEmailConfig;
import com.vinsys.hrms.entity.OrgDivWiseHRMapping;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.util.EmailSender;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSFileuploadUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.IHRMSEmailTemplateConstants;
import com.vinsys.hrms.util.JWTTokenHelper;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping(path = "/employeeSeparation")

public class EmployeeSeparationDetailsService {
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
	/*
	 * @Autowired IHRMSMailTemplateDAO mailTemplateDAO;
	 */

	@Autowired
	IHRMSMasterCityDAO masterCityDAO;
	@Autowired
	IHRMSMasterCountryDAO masterCountryDAO;
	@Autowired
	IHRMSMasterStateDAO masterStateDAO;
	@Autowired
	IHRMSEmployeeReportingManager reportringManagerDAO;

	@Value("${SEPARATION_REMINDER_NO_OF_DAYS}")
	private double sepReminderNoOfDays;

	private static final Logger logger = LoggerFactory.getLogger(EmployeeSeparationDetailsService.class);

	/*
	 * This is use when employee Resign for first time. Also If HR want to change
	 * relieving date then this is used
	 * 
	 */
	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String addEmployeeSeparationDetails(@RequestBody VOEmployeeSeparationDetails voEmployeeSeparationDetails) {
		try {
			EmployeeSeparationDetails employeeSeparationDetailsEntity;
			EmployeeSeparationDetailsWithHistory employeeSeparationDetailsEntityHistory;
			Employee employeeEntity;
			MasterModeofSeparation masterModeofSeparationEntity;
			MasterModeofSeparationReason empSeaparationReasonEntity;

			MasterModeofSeparationReason roSeparationReasonEntity = null;
			MasterModeofSeparationReason orgSeparationReasonEntity = null;
			MasterModeofSeparationReason hrSeparationReasonEntity = null;

			if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails)) {

				employeeSeparationDetailsEntity = employeeSeparationDAO.findSeparationDetailsUsingEmpIdandActive(
						IHRMSConstants.isActive, voEmployeeSeparationDetails.getEmployee().getId());

				if (!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity)) {

					/* update */

					masterModeofSeparationEntity = mstModeofSeparationDAO
							.findById(voEmployeeSeparationDetails.getModeofSeparation().getId()).get();
					empSeaparationReasonEntity = mstSeparationReasonDAO
							.findById(voEmployeeSeparationDetails.getEmpseparationReason().getId()).get();
					// noticePeriodEntity=noticePeriodDAO.findById(voEmployeeSeparationDetails.getNoticePeriod().getId());
					employeeEntity = employeeDAO.findById(voEmployeeSeparationDetails.getEmployee().getId()).get();
					employeeSeparationDetailsEntity.setEmployee(employeeEntity);
					employeeSeparationDetailsEntity.setModeofSeparation(masterModeofSeparationEntity);
					employeeSeparationDetailsEntity.setEmpSeparationReason(empSeaparationReasonEntity);
					// employeeSeparationDetailsEntity.setNoticePeriod(noticePeriodEntity);
					if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoReason())
							&& !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoReason().getId()))
						roSeparationReasonEntity = mstSeparationReasonDAO
								.findById(voEmployeeSeparationDetails.getRoReason().getId()).get();
					if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgReason())
							&& !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgReason().getId()))
						orgSeparationReasonEntity = mstSeparationReasonDAO
								.findById(voEmployeeSeparationDetails.getOrgReason().getId()).get();
					if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getHRReason())
							&& !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getHRReason().getId()))
						hrSeparationReasonEntity = mstSeparationReasonDAO
								.findById(voEmployeeSeparationDetails.getHRReason().getId()).get();
					employeeSeparationDetailsEntity.setRoReason(roSeparationReasonEntity);
					employeeSeparationDetailsEntity.setOrgReason(orgSeparationReasonEntity);
					employeeSeparationDetailsEntity.setHRReason(hrSeparationReasonEntity);

					/*
					 * employeeSeparationDetailsEntity =
					 * HRMSRequestTranslator.translateToEmployeeSeparationDetails(
					 * employeeSeparationDetailsEntity, voEmployeeSeparationDetails);
					 */

					employeeSeparationDetailsEntity.setActualRelievingDate(
							HRMSDateUtil.parse(voEmployeeSeparationDetails.getActualRelievingDate(),
									IHRMSConstants.FRONT_END_DATE_FORMAT));
					employeeSeparationDetailsEntity.setResignationDate(HRMSDateUtil.parse(
							voEmployeeSeparationDetails.getResignationDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));

					employeeSeparationDetailsEntity
							.setEmployeeComment(voEmployeeSeparationDetails.getEmployeeComment());
					employeeSeparationDetailsEntity.setHRComment(voEmployeeSeparationDetails.getHRComment());

					employeeSeparationDetailsEntity.setUpdatedBy(voEmployeeSeparationDetails.getUpdatedBy());
					employeeSeparationDetailsEntity.setUpdatedDate(new Date());

					/*
					 * if
					 * (voEmployeeSeparationDetails.getModeofSeparation().getModeOfSeparationName()
					 * .equalsIgnoreCase(IHRMSConstants.EMPLOYEE_ABSCONDED)) {
					 * employeeSeparationDetailsEntity.setStatus(IHRMSConstants.EMPLOYEE_ABSCONDED);
					 * employeeSeparationDetailsEntity = setROandOrgstatusForTerAbscDemiseReason(
					 * employeeSeparationDetailsEntity);
					 * 
					 * } else if
					 * (voEmployeeSeparationDetails.getModeofSeparation().getModeOfSeparationName()
					 * .equalsIgnoreCase(IHRMSConstants.EMPLOYEE_TERMINATED)) {
					 * employeeSeparationDetailsEntity.setStatus(IHRMSConstants.EMPLOYEE_TERMINATED)
					 * ;
					 * 
					 * employeeSeparationDetailsEntity = setROandOrgstatusForTerAbscDemiseReason(
					 * employeeSeparationDetailsEntity); } else if
					 * (voEmployeeSeparationDetails.getModeofSeparation().getModeOfSeparationName()
					 * .equalsIgnoreCase(IHRMSConstants.EMPLOYEE_DEMISE)) {
					 * employeeSeparationDetailsEntity.setStatus(IHRMSConstants.EMPLOYEE_DEMISE);
					 * 
					 * employeeSeparationDetailsEntity = setROandOrgstatusForTerAbscDemiseReason(
					 * employeeSeparationDetailsEntity);
					 * 
					 * } else { employeeSeparationDetailsEntity.setStatus(IHRMSConstants.
					 * EMPLOYEE_SEPARATION_STATUS_PENDING); if
					 * (HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity.getHRComment() &&
					 * )) {
					 * 
					 * } else { employeeSeparationDetailsEntity =
					 * setROandOrgstatusForTerAbscDemiseReason(employeeSeparationDetailsEntity); } }
					 * 
					 * /* code for employee separation history
					 */
					employeeSeparationDetailsEntityHistory = new EmployeeSeparationDetailsWithHistory();

					masterModeofSeparationEntity = mstModeofSeparationDAO
							.findById(voEmployeeSeparationDetails.getModeofSeparation().getId()).get();
					empSeaparationReasonEntity = mstSeparationReasonDAO
							.findById(voEmployeeSeparationDetails.getEmpseparationReason().getId()).get();

					employeeEntity = employeeDAO.findById(voEmployeeSeparationDetails.getEmployee().getId()).get();
					employeeSeparationDetailsEntityHistory.setEmployee(employeeEntity);
					employeeSeparationDetailsEntityHistory.setModeofSeparation(masterModeofSeparationEntity);
					employeeSeparationDetailsEntityHistory.setEmpSeparationReason(empSeaparationReasonEntity);

					if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoReason())
							&& !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoReason().getId()))
						roSeparationReasonEntity = mstSeparationReasonDAO
								.findById(voEmployeeSeparationDetails.getRoReason().getId()).get();
					if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgReason())
							&& !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgReason().getId()))
						orgSeparationReasonEntity = mstSeparationReasonDAO
								.findById(voEmployeeSeparationDetails.getOrgReason().getId()).get();
					if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getHRReason())
							&& !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getHRReason().getId()))
						hrSeparationReasonEntity = mstSeparationReasonDAO
								.findById(voEmployeeSeparationDetails.getHRReason().getId()).get();
					employeeSeparationDetailsEntityHistory.setRoReason(roSeparationReasonEntity);
					employeeSeparationDetailsEntityHistory.setOrgReason(orgSeparationReasonEntity);
					employeeSeparationDetailsEntityHistory.setHRReason(hrSeparationReasonEntity);

					employeeSeparationDetailsEntityHistory = HRMSRequestTranslator
							.translateToEmployeeSeparationDetailsWithHistory(employeeSeparationDetailsEntityHistory,
									voEmployeeSeparationDetails);

					if (voEmployeeSeparationDetails.getModeofSeparation().getModeOfSeparationName()
							.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_ABSCONDED)) {
						employeeSeparationDetailsEntityHistory.setStatus(IHRMSConstants.EMPLOYEE_ABSCONDED);
						employeeSeparationDetailsEntityHistory = setROandOrgstatusForTerAbscDemiseReasonForHistory(
								employeeSeparationDetailsEntityHistory);
						// addEntryInChecklistTables(voEmployeeSeparationDetails, employeeEntity);
						// sendMailToempTermAbsDemisReason(employeeSeparationDetailsEntity);

					} else if (voEmployeeSeparationDetails.getModeofSeparation().getModeOfSeparationName()
							.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_TERMINATED)) {
						employeeSeparationDetailsEntityHistory.setStatus(IHRMSConstants.EMPLOYEE_TERMINATED);
						// addEntryInChecklistTables(voEmployeeSeparationDetails, employeeEntity);
						// sendMailToempTermAbsDemisReason(employeeSeparationDetailsEntity);
						employeeSeparationDetailsEntityHistory = setROandOrgstatusForTerAbscDemiseReasonForHistory(
								employeeSeparationDetailsEntityHistory);
					} else if (voEmployeeSeparationDetails.getModeofSeparation().getModeOfSeparationName()
							.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_DEMISE)) {
						employeeSeparationDetailsEntityHistory.setStatus(IHRMSConstants.EMPLOYEE_DEMISE);
						// addEntryInChecklistTables(voEmployeeSeparationDetails, employeeEntity);
						// sendMailToempTermAbsDemisReason(employeeSeparationDetailsEntity);
						employeeSeparationDetailsEntityHistory = setROandOrgstatusForTerAbscDemiseReasonForHistory(
								employeeSeparationDetailsEntityHistory);

					} else {
						employeeSeparationDetailsEntityHistory
								.setStatus(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING);
						if (HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity.getHRComment()))
							sendEmailToApprovers(employeeEntity, employeeSeparationDetailsEntity);
						else {
							// addEntryInChecklistTables(voEmployeeSeparationDetails, employeeEntity);
							// sendMailToempTermAbsDemisReason(employeeSeparationDetailsEntity);
							employeeSeparationDetailsEntityHistory = setROandOrgstatusForTerAbscDemiseReasonForHistory(
									employeeSeparationDetailsEntityHistory);
						}
					}

					/*
					 * employeeSeparationDetailsEntityHistory.setActualRelievingDate(HRMSDateUtil
					 * .parse(voEmployeeSeparationDetails.getActualRelievingDate(),
					 * IHRMSConstants.FRONT_END_DATE_FORMAT));
					 * employeeSeparationDetailsEntityHistory.setResignationDate(HRMSDateUtil
					 * .parse(voEmployeeSeparationDetails.getResignationDate(),
					 * IHRMSConstants.FRONT_END_DATE_FORMAT));
					 */

					// employeeSeparationDetailsEntityHistory.setEmployeeComment(voEmployeeSeparationDetails.getEmployeeComment());
					// employeeSeparationDetailsEntityHistory.setHRComment(voEmployeeSeparationDetails.getHRComment());

					// employeeSeparationDetailsEntityHistory.setUpdatedBy(voEmployeeSeparationDetails.getUpdatedBy());
					// employeeSeparationDetailsEntityHistory.setUpdatedDate(new Date());

					employeeSeparationDetailsEntityHistory = employeeSeparationHistoryDAO
							.save(employeeSeparationDetailsEntityHistory);
					List<Object> objectList = new ArrayList<Object>();
					objectList.add(HRMSEntityToModelMapper
							.convertToEmpSeparationDetailsStatusHistoryVO(employeeSeparationDetailsEntityHistory));
					HRMSListResponseObject res = new HRMSListResponseObject();
					res.setListResponse(objectList);
					res.setResponseCode(IHRMSConstants.successCode);
					res.setResponseMessage(IHRMSConstants.successMessage);

					return HRMSHelper.createJsonString(res);

				} else {
					/* insert */
					employeeSeparationDetailsEntity = new EmployeeSeparationDetails();
					employeeSeparationDetailsEntityHistory = new EmployeeSeparationDetailsWithHistory();

					masterModeofSeparationEntity = mstModeofSeparationDAO
							.findById(voEmployeeSeparationDetails.getModeofSeparation().getId()).get();
					empSeaparationReasonEntity = mstSeparationReasonDAO
							.findById(voEmployeeSeparationDetails.getEmpseparationReason().getId()).get();
					// noticePeriodEntity=noticePeriodDAO.findById(voEmployeeSeparationDetails.getNoticePeriod().getId());
					employeeEntity = employeeDAO.findById(voEmployeeSeparationDetails.getEmployee().getId()).get();
					employeeSeparationDetailsEntity.setEmployee(employeeEntity);
					employeeSeparationDetailsEntityHistory.setEmployee(employeeEntity);
					employeeSeparationDetailsEntity.setModeofSeparation(masterModeofSeparationEntity);
					employeeSeparationDetailsEntityHistory.setModeofSeparation(masterModeofSeparationEntity);
					employeeSeparationDetailsEntity.setEmpSeparationReason(empSeaparationReasonEntity);
					employeeSeparationDetailsEntityHistory.setEmpSeparationReason(empSeaparationReasonEntity);
					// employeeSeparationDetailsEntity.setNoticePeriod(noticePeriodEntity);
					if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoReason())
							&& !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoReason().getId()))
						roSeparationReasonEntity = mstSeparationReasonDAO
								.findById(voEmployeeSeparationDetails.getRoReason().getId()).get();
					if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgReason())
							&& !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgReason().getId()))
						orgSeparationReasonEntity = mstSeparationReasonDAO
								.findById(voEmployeeSeparationDetails.getOrgReason().getId()).get();
					if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getHRReason())
							&& !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getHRReason().getId()))
						hrSeparationReasonEntity = mstSeparationReasonDAO
								.findById(voEmployeeSeparationDetails.getHRReason().getId()).get();
					employeeSeparationDetailsEntity.setRoReason(roSeparationReasonEntity);
					employeeSeparationDetailsEntityHistory.setRoReason(roSeparationReasonEntity);
					employeeSeparationDetailsEntity.setOrgReason(orgSeparationReasonEntity);
					employeeSeparationDetailsEntityHistory.setOrgReason(orgSeparationReasonEntity);
					employeeSeparationDetailsEntity.setHRReason(hrSeparationReasonEntity);
					employeeSeparationDetailsEntityHistory.setHRReason(hrSeparationReasonEntity);

					employeeSeparationDetailsEntity = HRMSRequestTranslator.translateToEmployeeSeparationDetails(
							employeeSeparationDetailsEntity, voEmployeeSeparationDetails);

					employeeSeparationDetailsEntityHistory = HRMSRequestTranslator
							.translateToEmployeeSeparationDetailsWithHistory(employeeSeparationDetailsEntityHistory,
									voEmployeeSeparationDetails);

					if (voEmployeeSeparationDetails.getModeofSeparation().getModeOfSeparationName()
							.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_ABSCONDED)) {
						employeeSeparationDetailsEntity.setStatus(IHRMSConstants.EMPLOYEE_ABSCONDED);
						employeeSeparationDetailsEntityHistory.setStatus(IHRMSConstants.EMPLOYEE_ABSCONDED);
						employeeSeparationDetailsEntity = setROandOrgstatusForTerAbscDemiseReason(
								employeeSeparationDetailsEntity);
						employeeSeparationDetailsEntityHistory = setROandOrgstatusForTerAbscDemiseReasonForHistory(
								employeeSeparationDetailsEntityHistory);
						addEntryInChecklistTables(voEmployeeSeparationDetails, employeeEntity);
						sendMailToempTermAbsDemisReason(employeeSeparationDetailsEntity);

					} else if (voEmployeeSeparationDetails.getModeofSeparation().getModeOfSeparationName()
							.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_TERMINATED)) {
						employeeSeparationDetailsEntity.setStatus(IHRMSConstants.EMPLOYEE_TERMINATED);
						employeeSeparationDetailsEntityHistory.setStatus(IHRMSConstants.EMPLOYEE_TERMINATED);
						addEntryInChecklistTables(voEmployeeSeparationDetails, employeeEntity);
						sendMailToempTermAbsDemisReason(employeeSeparationDetailsEntity);
						employeeSeparationDetailsEntity = setROandOrgstatusForTerAbscDemiseReason(
								employeeSeparationDetailsEntity);
						employeeSeparationDetailsEntityHistory = setROandOrgstatusForTerAbscDemiseReasonForHistory(
								employeeSeparationDetailsEntityHistory);
					} else if (voEmployeeSeparationDetails.getModeofSeparation().getModeOfSeparationName()
							.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_DEMISE)) {
						employeeSeparationDetailsEntity.setStatus(IHRMSConstants.EMPLOYEE_DEMISE);
						employeeSeparationDetailsEntityHistory.setStatus(IHRMSConstants.EMPLOYEE_DEMISE);
						addEntryInChecklistTables(voEmployeeSeparationDetails, employeeEntity);
						sendMailToempTermAbsDemisReason(employeeSeparationDetailsEntity);
						employeeSeparationDetailsEntity = setROandOrgstatusForTerAbscDemiseReason(
								employeeSeparationDetailsEntity);
						employeeSeparationDetailsEntityHistory = setROandOrgstatusForTerAbscDemiseReasonForHistory(
								employeeSeparationDetailsEntityHistory);

					} else {
						employeeSeparationDetailsEntity.setStatus(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING);
						employeeSeparationDetailsEntityHistory
								.setStatus(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING);
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
					}

					employeeEntity = employeeDAO.findById(voEmployeeSeparationDetails.getEmployee().getId()).get();

				}
				employeeSeparationDetailsEntity = employeeSeparationDAO.save(employeeSeparationDetailsEntity);
				employeeSeparationDetailsEntityHistory = employeeSeparationHistoryDAO
						.save(employeeSeparationDetailsEntityHistory);
				List<Object> objectList = new ArrayList<Object>();
				objectList
						.add(HRMSEntityToModelMapper.convertToEmpSeparationDetailsVO(employeeSeparationDetailsEntity));
				HRMSListResponseObject res = new HRMSListResponseObject();
				res.setListResponse(objectList);
				res.setResponseCode(IHRMSConstants.successCode);
				res.setResponseMessage(IHRMSConstants.successMessage);

				return HRMSHelper.createJsonString(res);

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
			unknown.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * This method is used when employee applied for Separation or when employee
	 * withdraw his Separation
	 * 
	 */
	public void sendEmailToApprovers(Employee empEntity, EmployeeSeparationDetails employeeSeparationDetailsEntity) {
		logger.info("Employee Resign or withdraw his resignation");
		String employeeEmailId = empEntity.getOfficialEmailId();
		String reportingManagerEmailId = empEntity.getEmployeeReportingManager().getReporingManager()
				.getOfficialEmailId();
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
		logger.info("hrEmail Ids " + hrEmailIds);
		String ccEmailIds = employeeEmailId + ";" + orgLevelEmailId + ";" + hrEmailIds;
		logger.info("ccEmailIds  " + ccEmailIds);
		Map<String, String> placeHolderMapping = HRMSRequestTranslator.createPlaceHolderMapForEmp_Separation(empEntity,
				employeeSeparationDetailsEntity);
		placeHolderMapping.put("{websiteURL}", baseURL);

		String mailContent = HRMSHelper.replaceString(placeHolderMapping,
				IHRMSEmailTemplateConstants.Template_Empployee_Resignation_Apply);

		String mailSubject = "";
		if (!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity.getEmployeeAction())
				&& employeeSeparationDetailsEntity.getEmployeeAction()
						.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_ACTION_WITHDRAW)) {
			mailSubject = IHRMSConstants.MailSubject_EMPLOYEEWITHDRAW;
			mailContent = HRMSHelper.replaceString(placeHolderMapping,
					IHRMSEmailTemplateConstants.Template_Empployee_Resignation_Withdraw);

		}

		else
			mailSubject = IHRMSConstants.MailSubject_EMPLOYEERESIGNED;

		logger.info("ccemailids sendEmail to Approvers  " + ccEmailIds);
		emailsender.toPersistEmail(reportingManagerEmailId, ccEmailIds, mailContent, mailSubject,
				empEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				empEntity.getCandidate().getLoginEntity().getOrganization().getId());
	}

	/**
	 * This is use to get employee separation details using employee id
	 * 
	 */

	@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getSeparationDetailsbyEmpId(@PathVariable("id") long empId) {

		try {
			if (!HRMSHelper.isNullOrEmpty(empId)) {
				List<EmployeeSeparationDetails> employeeSeparationDetailsEntityList = employeeSeparationDAO
						.findSeparationDetailsByEmpId(empId, IHRMSConstants.isActive);
				HRMSListResponseObject response = new HRMSListResponseObject();
				List<Object> objectList = new ArrayList<Object>();
				for (EmployeeSeparationDetails employeeSeparationDetailsEntity : employeeSeparationDetailsEntityList) {
					VOEmployeeSeparationDetails voEmployeeSeparationDetails = HRMSEntityToModelMapper
							.convertToEmpSeparationDetailsVO(employeeSeparationDetailsEntity);
					objectList.add(voEmployeeSeparationDetails);

				}
				response.setListResponse(objectList);
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(IHRMSConstants.successMessage);

				return HRMSHelper.createJsonString(response);
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
			unknown.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		return null;

	}

	/**
	 * This method is used when approvers take action on separation either on
	 * Withdraw or Separation
	 * 
	 * 
	 */
	public void actionbyApproversForSeparation(Employee empEntity,
			VOEmployeeSeparationDetails voEmployeeSeparationDetails) {
		logger.info("Inside Mail sender");
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

		String hrEmailIds = "";
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
		Map<String, String> placeHolderMapping = HRMSRequestTranslator
				.createPlaceHolderMapForApproveSeparation(empEntity, voEmployeeSeparationDetails);
		placeHolderMapping.put("{websiteURL}", baseURL);

		String mailContent = HRMSHelper.replaceString(placeHolderMapping,
				IHRMSEmailTemplateConstants.Template_Empployee_Resignation_Approve);

		String mailSubject = "";
		if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoApproverStatus())
				|| !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgApproverStatus())) {
			if ((!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoApproverStatus())
					&& voEmployeeSeparationDetails.getRoApproverStatus()
							.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED))) {
				mailSubject = IHRMSConstants.MailSubject_SEPARATION_REJECTED;
			}
			if ((!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoApproverStatus())
					&& voEmployeeSeparationDetails.getRoApproverStatus()
							.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED))) {
				mailSubject = IHRMSConstants.MailSubject_SEPARATION_APPROVED;
			}
			if ((!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgApproverStatus())
					&& voEmployeeSeparationDetails.getOrgApproverStatus()
							.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED))) {
				mailSubject = IHRMSConstants.MailSubject_SEPARATION_REJECTED;
			}
			if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgApproverStatus())
					&& voEmployeeSeparationDetails.getOrgApproverStatus()
							.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED)) {
				// MasterEmailTemplate
				// mstEmailTemplate=mailTemplateDAO.findByTemplateNameandType(IHRMSConstants.SEPARATION_ORG_APPROVE_TEMPLATE_NAME,
				// IHRMSConstants.SEPARATION_TEMPLATE,
				// empEntity.getCandidate().getLoginEntity().getOrganization().getId(),IHRMSConstants.isActive);
				// mailContent =
				// HRMSHelper.replaceString(placeHolderMapping,mstEmailTemplate.getTemplate());
				long divId = empEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
				if (divId == 3) {
					mailContent = HRMSHelper.replaceString(placeHolderMapping,
							IHRMSEmailTemplateConstants.Template_Empployee_Resignation_Approve_By_ORG_LEVEL_for_UAE);
				} else {
					mailContent = HRMSHelper.replaceString(placeHolderMapping,
							IHRMSEmailTemplateConstants.Template_Empployee_Resignation_Approve_By_ORG_LEVEL);
				}
				/*
				 * mailContent = HRMSHelper.replaceString(placeHolderMapping,
				 * IHRMSEmailTemplateConstants.
				 * Template_Empployee_Resignation_Approve_By_ORG_LEVEL);
				 */
				mailSubject = IHRMSConstants.MailSubject_SEPARATION_APPROVED;
				for (MapCatalogue mapCatalogueEntity : mapCatalogueList) {
					if (!HRMSHelper.isNullOrEmpty(mapCatalogueEntity.getApprover())) {
						long empId = mapCatalogueEntity.getApprover().getId();
						Employee employeeEntity = employeeDAO.findById(empId).get();
						/*
						 * if(!HRMSHelper.isNullOrEmpty(ccEmailIds)) ccEmailIds=ccEmailIds+";";
						 */
						ccEmailIds = ccEmailIds + employeeEntity.getOfficialEmailId() + ";";
					}
				}
			}
		}
		// the two ifs are modified by ssw on 12 march 2018
		// for : on withdraw action by RO or ORG level
		// in old "if condition", comparing status with 'ROApproverStatus' instead of
		// 'ROWdAction'
		// same with 'OrgApproverStatus' instead of 'Org_level_emp_WdAction'

		// OLD if ::
		// if
		// ((!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoApproverStatus())
		// && voEmployeeSeparationDetails
		// .getRoApproverStatus().equalsIgnoreCase(IHRMSConstants.EMPLOYEE_WD_SEPARATION_STATUS_REJECTED))
		// ||
		// (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgApproverStatus())
		// && voEmployeeSeparationDetails.getOrgApproverStatus()
		// .equalsIgnoreCase(IHRMSConstants.EMPLOYEE_WD_SEPARATION_STATUS_REJECTED))) {

		// NEW if ::
		if ((!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getROWdAction()) && voEmployeeSeparationDetails
				.getROWdAction().equalsIgnoreCase(IHRMSConstants.EMPLOYEE_WD_SEPARATION_STATUS_REJECTED))
				|| (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrg_level_emp_WdAction())
						&& voEmployeeSeparationDetails.getOrg_level_emp_WdAction()
								.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_WD_SEPARATION_STATUS_REJECTED))) {
			mailSubject = IHRMSConstants.MailSubject_WD_SEPARATION_REJECTED;
			mailContent = HRMSHelper.replaceString(placeHolderMapping,
					IHRMSEmailTemplateConstants.Template_Empployee_Resignation_Application_Has_Been_Withdraw_By_Approve); // Added
																															// constant
																															// mail
																															// content
																															// when
																															// resignatio
																															// withdarw
																															// has
																															// been
																															// approved
																															// by
																															// manager

		}

		// OLD if ::
		// if
		// ((!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoApproverStatus())
		// && voEmployeeSeparationDetails
		// .getRoApproverStatus().equalsIgnoreCase(IHRMSConstants.EMPLOYEE_WD_SEPARATION_STATUS_APPROVED))
		// ||
		// (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgApproverStatus())
		// && voEmployeeSeparationDetails.getOrgApproverStatus()
		// .equalsIgnoreCase(IHRMSConstants.EMPLOYEE_WD_SEPARATION_STATUS_APPROVED))) {

		// NEW if ::
		if ((!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getROWdAction()) && voEmployeeSeparationDetails
				.getROWdAction().equalsIgnoreCase(IHRMSConstants.EMPLOYEE_WD_SEPARATION_STATUS_APPROVED))
				|| (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrg_level_emp_WdAction())
						&& voEmployeeSeparationDetails.getOrg_level_emp_WdAction()
								.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_WD_SEPARATION_STATUS_APPROVED))) {
			// placeHolderMapping.put("{Resignation}","pull back");
			mailSubject = IHRMSConstants.MailSubject_WD_SEPARATION_APPROVED;
			mailContent = HRMSHelper.replaceString(placeHolderMapping,
					IHRMSEmailTemplateConstants.Template_Empployee_Resignation_Application_Has_Been_Withdraw_By_Approve); // Added
																															// constant
																															// mail
																															// content
																															// when
																															// resignatio
																															// withdarw
																															// has
																															// been
																															// approved
																															// by
																															// manager
		}
		logger.info("ccemailids action by Approvers  " + ccEmailIds);
		emailsender.toPersistEmail(employeeEmailId, ccEmailIds, mailContent, mailSubject,
				empEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				empEntity.getCandidate().getLoginEntity().getOrganization().getId());

	}

	/*
	 * This method is for when approver take any action on employee's Resignation
	 */

	@RequestMapping(value = "/sep_action", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String separationAction(@RequestBody VOEmployeeSeparationDetails voEmployeeSeparationDetails,
			HttpServletRequest servletRequest) {
		try {

			EmployeeSeparationDetails employeeSeparationDetailsEntity = null;

			/**
			 * Employee Withdraw his Resignation
			 */

			if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails)) {
				employeeSeparationDetailsEntity = employeeSeparationDAO.findSeparationDetailsUsingEmpIdandStatus(
						IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING,
						voEmployeeSeparationDetails.getEmployee().getId());

				if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getEmployeeAction())
						&& voEmployeeSeparationDetails.getEmployeeAction()
								.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_ACTION_WITHDRAW)
						&& HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getROWdAction())) {
					if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoApproverStatus())
							|| !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgApproverStatus())) {
						if ((!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity.getRoApproverStatus())
								&& employeeSeparationDetailsEntity.getRoApproverStatus()
										.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED))
								|| (!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity.getOrgApproverStatus())
										&& employeeSeparationDetailsEntity.getOrgApproverStatus().equalsIgnoreCase(
												IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED))) {
							throw new HRMSException(IHRMSConstants.SEPARATIONWITHDRAWCODE,
									IHRMSConstants.SEPARATIONWITHDRAWMESSAGE);
						}
					} else {
						employeeSeparationDetailsEntity = resignationWithDrawActionForEmp(voEmployeeSeparationDetails);
					}
				}

				/**
				 * This is When Approver Rejects Resignation
				 */
				if ((!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoApproverStatus())
						&& voEmployeeSeparationDetails.getRoApproverStatus()
								.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED))
						|| (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgApproverStatus())
								&& voEmployeeSeparationDetails.getOrgApproverStatus()
										.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED))) {
					employeeSeparationDetailsEntity = resignationRejectedAction(voEmployeeSeparationDetails,
							servletRequest);
				}
				/**
				 * This is When Approver Approves Resignation
				 */
				if (((!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoApproverStatus())
						&& voEmployeeSeparationDetails.getRoApproverStatus()
								.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED))
						|| (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgApproverStatus())
								&& voEmployeeSeparationDetails.getOrgApproverStatus()
										.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED)))
						&& ((!(!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgApproverStatus())
								&& voEmployeeSeparationDetails.getOrgApproverStatus()
										.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED))))) {

					employeeSeparationDetailsEntity = resignationApproveAction(voEmployeeSeparationDetails);
				}
				/**
				 * This is When Approver Rejects Withdraw Resignation
				 */
				if ((!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getROWdAction())
						&& voEmployeeSeparationDetails.getROWdAction()
								.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_WD_SEPARATION_STATUS_REJECTED))
						|| (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrg_level_emp_WdAction())
								&& voEmployeeSeparationDetails.getOrg_level_emp_WdAction()
										.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_WD_SEPARATION_STATUS_REJECTED))) {
					employeeSeparationDetailsEntity = resignationWithDrawRejectedAction(voEmployeeSeparationDetails);
					employeeSeparationDetailsEntity.setIsActive(IHRMSConstants.isNotActive);
				}
				/**
				 * This is When Approver Approves Withdraw Resignation
				 */
				if ((!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getROWdAction())
						&& voEmployeeSeparationDetails.getROWdAction()
								.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_WD_SEPARATION_STATUS_APPROVED))
						|| (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrg_level_emp_WdAction())
								&& voEmployeeSeparationDetails.getOrg_level_emp_WdAction()
										.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_WD_SEPARATION_STATUS_APPROVED))) {
					employeeSeparationDetailsEntity = resignationWithdrawApprovedAction(voEmployeeSeparationDetails);
					employeeSeparationDetailsEntity.setIsActive(IHRMSConstants.isNotActive);
				}

				employeeSeparationDetailsEntity = employeeSeparationDAO.save(employeeSeparationDetailsEntity);
				List<Object> objectList = new ArrayList<Object>();
				objectList
						.add(HRMSEntityToModelMapper.convertToEmpSeparationDetailsVO(employeeSeparationDetailsEntity));
				HRMSListResponseObject res = new HRMSListResponseObject();

				res.setListResponse(objectList);
				res.setResponseCode(IHRMSConstants.successCode);
				res.setResponseMessage(IHRMSConstants.successMessage);

				return HRMSHelper.createJsonString(res);

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
			unknown.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	/*
	 * This method is for when approver approves withdraw resignation
	 */
	private EmployeeSeparationDetails resignationWithdrawApprovedAction(
			VOEmployeeSeparationDetails voEmployeeSeparationDetails) {
		logger.info(" == ACTION -> When Approver Approves  Withdraw Separation << ==");

		EmployeeSeparationDetails employeeSeparationDetailsEntity;
		Employee employeeEntity;
		MasterModeofSeparation masterModeofSeparationEntity;
		MasterModeofSeparationReason empSeaparationReasonEntity;

		MasterModeofSeparationReason roSeparationReasonEntity = null;
		MasterModeofSeparationReason orgSeparationReasonEntity = null;
		MasterModeofSeparationReason hrSeparationReasonEntity = null;

		employeeSeparationDetailsEntity = employeeSeparationDAO.findSeparationDetailsUsingEmpIdandStatus(
				IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_WITHDRAW, voEmployeeSeparationDetails.getEmployee().getId());

		if (!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity)) {

			/* update */

			masterModeofSeparationEntity = mstModeofSeparationDAO
					.findById(voEmployeeSeparationDetails.getModeofSeparation().getId()).get();
			empSeaparationReasonEntity = mstSeparationReasonDAO
					.findById(voEmployeeSeparationDetails.getEmpseparationReason().getId()).get();
			// noticePeriodEntity=noticePeriodDAO.findById(voEmployeeSeparationDetails.getNoticePeriod().getId());
			employeeEntity = employeeDAO.findById(voEmployeeSeparationDetails.getEmployee().getId()).get();
			employeeSeparationDetailsEntity.setEmployee(employeeEntity);
			employeeSeparationDetailsEntity.setModeofSeparation(masterModeofSeparationEntity);
			employeeSeparationDetailsEntity.setEmpSeparationReason(empSeaparationReasonEntity);
			// employeeSeparationDetailsEntity.setNoticePeriod(noticePeriodEntity);
			if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoReason())
					&& !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoReason().getId()))
				;
			roSeparationReasonEntity = mstSeparationReasonDAO
					.findById(voEmployeeSeparationDetails.getRoReason().getId()).get();
			if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgReason())
					&& !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgReason().getId()))
				orgSeparationReasonEntity = mstSeparationReasonDAO
						.findById(voEmployeeSeparationDetails.getOrgReason().getId()).get();
			if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getHRReason())
					&& !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getHRReason().getId()))
				hrSeparationReasonEntity = mstSeparationReasonDAO
						.findById(voEmployeeSeparationDetails.getHRReason().getId()).get();
			employeeSeparationDetailsEntity.setRoReason(roSeparationReasonEntity);
			employeeSeparationDetailsEntity.setOrgReason(orgSeparationReasonEntity);
			employeeSeparationDetailsEntity.setHRReason(hrSeparationReasonEntity);

			employeeSeparationDetailsEntity = HRMSRequestTranslator
					.translateToEmployeeSeparationDetails(employeeSeparationDetailsEntity, voEmployeeSeparationDetails);
			employeeSeparationDetailsEntity.setStatus(IHRMSConstants.EMPLOYEE_WD_SEPARATION_STATUS_APPROVED);
			// if(!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getROWdAction()) &&
			// voEmployeeSeparationDetails.getROWdAction())
			employeeSeparationDetailsEntity = employeeSeparationDAO.save(employeeSeparationDetailsEntity);
			actionbyApproversForSeparation(employeeEntity, voEmployeeSeparationDetails);

		}
		return employeeSeparationDetailsEntity;
	}
	/*
	 * 
	 * This method is for Approver rejects withdraw resignation
	 */

	private EmployeeSeparationDetails resignationWithDrawRejectedAction(
			VOEmployeeSeparationDetails voEmployeeSeparationDetails) {
		logger.info(" == ACTION -> When Approver Rejects  Withdraw Separation << ==");

		EmployeeSeparationDetails employeeSeparationDetailsEntity;
		Employee employeeEntity;
		MasterModeofSeparation masterModeofSeparationEntity;
		MasterModeofSeparationReason empSeaparationReasonEntity;

		MasterModeofSeparationReason roSeparationReasonEntity = null;
		MasterModeofSeparationReason orgSeparationReasonEntity = null;
		MasterModeofSeparationReason hrSeparationReasonEntity = null;

		employeeSeparationDetailsEntity = employeeSeparationDAO.findSeparationDetailsUsingEmpIdandStatus(
				IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_WITHDRAW, voEmployeeSeparationDetails.getEmployee().getId());

		if (!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity)) {

			/* update */

			masterModeofSeparationEntity = mstModeofSeparationDAO
					.findById(voEmployeeSeparationDetails.getModeofSeparation().getId()).get();
			empSeaparationReasonEntity = mstSeparationReasonDAO
					.findById(voEmployeeSeparationDetails.getEmpseparationReason().getId()).get();
			// noticePeriodEntity=noticePeriodDAO.findById(voEmployeeSeparationDetails.getNoticePeriod().getId());
			employeeEntity = employeeDAO.findById(voEmployeeSeparationDetails.getEmployee().getId()).get();
			employeeSeparationDetailsEntity.setEmployee(employeeEntity);
			employeeSeparationDetailsEntity.setModeofSeparation(masterModeofSeparationEntity);
			employeeSeparationDetailsEntity.setEmpSeparationReason(empSeaparationReasonEntity);
			// employeeSeparationDetailsEntity.setNoticePeriod(noticePeriodEntity);
			if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoReason())
					&& !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoReason().getId()))
				roSeparationReasonEntity = mstSeparationReasonDAO
						.findById(voEmployeeSeparationDetails.getRoReason().getId()).get();
			if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgReason())
					&& !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgReason().getId()))
				orgSeparationReasonEntity = mstSeparationReasonDAO
						.findById(voEmployeeSeparationDetails.getOrgReason().getId()).get();
			if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getHRReason())
					&& !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getHRReason().getId()))
				hrSeparationReasonEntity = mstSeparationReasonDAO
						.findById(voEmployeeSeparationDetails.getHRReason().getId()).get();
			employeeSeparationDetailsEntity.setRoReason(roSeparationReasonEntity);
			employeeSeparationDetailsEntity.setOrgReason(orgSeparationReasonEntity);
			employeeSeparationDetailsEntity.setHRReason(hrSeparationReasonEntity);

			employeeSeparationDetailsEntity = HRMSRequestTranslator
					.translateToEmployeeSeparationDetails(employeeSeparationDetailsEntity, voEmployeeSeparationDetails);
			employeeSeparationDetailsEntity.setStatus(IHRMSConstants.EMPLOYEE_WD_SEPARATION_STATUS_REJECTED);
			employeeSeparationDetailsEntity = employeeSeparationDAO.save(employeeSeparationDetailsEntity);
			actionbyApproversForSeparation(employeeEntity, voEmployeeSeparationDetails);

		}
		return employeeSeparationDetailsEntity;
	}

	/*
	 * This method is for when Employee request for withdraw separation
	 * 
	 * 
	 */
	private EmployeeSeparationDetails resignationWithDrawActionForEmp(
			VOEmployeeSeparationDetails voEmployeeSeparationDetails) {
		logger.info(" == ACTION -> When Employee Request For  Withdraw Separation << ==");
		try {
			EmployeeSeparationDetails employeeSeparationDetailsEntity;
			Employee employeeEntity;
			MasterModeofSeparation masterModeofSeparationEntity;
			MasterModeofSeparationReason empSeaparationReasonEntity;

			MasterModeofSeparationReason roSeparationReasonEntity = null;
			MasterModeofSeparationReason orgSeparationReasonEntity = null;
			MasterModeofSeparationReason hrSeparationReasonEntity = null;

			employeeSeparationDetailsEntity = employeeSeparationDAO.findSeparationDetailsUsingEmpIdandStatus(
					IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING,
					voEmployeeSeparationDetails.getEmployee().getId());

			if (!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity)) {

				/* update */

				masterModeofSeparationEntity = mstModeofSeparationDAO
						.findById(voEmployeeSeparationDetails.getModeofSeparation().getId()).get();
				empSeaparationReasonEntity = mstSeparationReasonDAO
						.findById(voEmployeeSeparationDetails.getEmpseparationReason().getId()).get();
				// noticePeriodEntity=noticePeriodDAO.findById(voEmployeeSeparationDetails.getNoticePeriod().getId());
				employeeEntity = employeeDAO.findById(voEmployeeSeparationDetails.getEmployee().getId()).get();
				employeeSeparationDetailsEntity.setEmployee(employeeEntity);
				employeeSeparationDetailsEntity.setModeofSeparation(masterModeofSeparationEntity);
				employeeSeparationDetailsEntity.setEmpSeparationReason(empSeaparationReasonEntity);
				// employeeSeparationDetailsEntity.setNoticePeriod(noticePeriodEntity);
				if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoReason())
						&& !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoReason().getId()))
					roSeparationReasonEntity = mstSeparationReasonDAO
							.findById(voEmployeeSeparationDetails.getRoReason().getId()).get();
				if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgReason())
						&& !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgReason().getId()))
					orgSeparationReasonEntity = mstSeparationReasonDAO
							.findById(voEmployeeSeparationDetails.getOrgReason().getId()).get();
				if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getHRReason())
						&& !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getHRReason().getId()))
					hrSeparationReasonEntity = mstSeparationReasonDAO
							.findById(voEmployeeSeparationDetails.getHRReason().getId()).get();
				employeeSeparationDetailsEntity.setRoReason(roSeparationReasonEntity);
				employeeSeparationDetailsEntity.setOrgReason(orgSeparationReasonEntity);
				employeeSeparationDetailsEntity.setHRReason(hrSeparationReasonEntity);

				employeeSeparationDetailsEntity = HRMSRequestTranslator.translateToEmployeeSeparationDetails(
						employeeSeparationDetailsEntity, voEmployeeSeparationDetails);
				employeeSeparationDetailsEntity.setStatus(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_WITHDRAW);
				employeeSeparationDetailsEntity.setEmployeeAction(IHRMSConstants.EMPLOYEE_SEPARATION_ACTION_WITHDRAW);
				employeeSeparationDetailsEntity = employeeSeparationDAO.save(employeeSeparationDetailsEntity);
				sendEmailToApprovers(employeeEntity, employeeSeparationDetailsEntity);

			}
			return employeeSeparationDetailsEntity;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * This method is for when approver approves Resignation
	 * 
	 */
	private EmployeeSeparationDetails resignationApproveAction(VOEmployeeSeparationDetails voEmployeeSeparationDetails)
			throws HRMSException, Exception {

		logger.info(" == ACTION -> Separation Approved << ==");

		EmployeeSeparationDetails employeeSeparationDetailsEntity;
		Employee employeeEntity;
		MasterModeofSeparation masterModeofSeparationEntity;
		MasterModeofSeparationReason empSeaparationReasonEntity;

		MasterModeofSeparationReason roSeparationReasonEntity = null;
		MasterModeofSeparationReason orgSeparationReasonEntity = null;
		MasterModeofSeparationReason hrSeparationReasonEntity = null;

		employeeSeparationDetailsEntity = employeeSeparationDAO.findSeparationDetailsUsingEmpIdandStatus(
				IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING, voEmployeeSeparationDetails.getEmployee().getId());

		if (!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity)) {
			// next change by SSW on 08 march 2018
			// for : If employee is RO and ORG_LEVEL and approval_status == 'RO_Pending'
			// then data to be saved to RO level entity, not in ORG Level entity
			boolean ifROPendingAndLoggedInEmp = false;
			// checking if Approval status is :: RO_PENDING and logged in employee is ORG
			// LEVEL
			employeeEntity = employeeDAO.findById(voEmployeeSeparationDetails.getEmployee().getId()).get();
			if (voEmployeeSeparationDetails.getApproval_status().equals(IHRMSConstants.EMPLOYEE_STATUS_RO_PENDING)
					&& !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgApproverStatus())) {
				if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getLoggedInEmployee())) {
					long loggedInEmpId = Long.parseLong(voEmployeeSeparationDetails.getLoggedInEmployee());
					Employee loggedInEmpEntity = employeeDAO.findById(loggedInEmpId).get();
					// finding org level employee
					List<MasterOrganizationEmailConfig> masterOrganizationEmailConfigs = configDAO
							.findBYorgLevelEmployeeAndOrgId(loggedInEmpEntity.getId(),SecurityFilter.TL_CLAIMS.get().getOrgId());
					// if logged in employee org level
					boolean isLoggedInEmployeeOrgLevel = false;
					for (MasterOrganizationEmailConfig masterOrganizationEmailConfig : masterOrganizationEmailConfigs) {
						if (masterOrganizationEmailConfig.getOrgLevelEmployee().getId()
								.equals(loggedInEmpEntity.getId())) {
							isLoggedInEmployeeOrgLevel = true;
							break;
						}
					}
					if (isLoggedInEmployeeOrgLevel) {
						// this if block executes if logged in employee is org level

						// checking if logged in employee is RO of selected resigned employee
						boolean isLoggedInEmployeeRoOfResignedEmp = false;
						if (employeeEntity.getEmployeeReportingManager().getReporingManager().getId()
								.equals(loggedInEmpEntity.getId())) {
							isLoggedInEmployeeRoOfResignedEmp = true;
						}
						if (isLoggedInEmployeeOrgLevel && isLoggedInEmployeeRoOfResignedEmp) {
							// setting org level reason
							voEmployeeSeparationDetails.setRoReason(voEmployeeSeparationDetails.getOrgReason());
							voEmployeeSeparationDetails.getRoReason()
									.setId(voEmployeeSeparationDetails.getOrgReason().getId());
							voEmployeeSeparationDetails
									.setRoApproverStatus(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED);
							voEmployeeSeparationDetails.setRoActionDate(voEmployeeSeparationDetails.getOrgActionDate());
							voEmployeeSeparationDetails.setOrgActionDate(null);
							voEmployeeSeparationDetails.setOrgApproverStatus(null);
							voEmployeeSeparationDetails.setOrgReason(null);
						}
					}
					ifROPendingAndLoggedInEmp = true;
				} else {
					ifROPendingAndLoggedInEmp = false;
				}
			} else {
				// Approval status is NOT RO_PENDING
				ifROPendingAndLoggedInEmp = true;
			}

			if (ifROPendingAndLoggedInEmp) {
				/* update */

				masterModeofSeparationEntity = mstModeofSeparationDAO
						.findById(voEmployeeSeparationDetails.getModeofSeparation().getId()).get();
				empSeaparationReasonEntity = mstSeparationReasonDAO
						.findById(voEmployeeSeparationDetails.getEmpseparationReason().getId()).get();
				// noticePeriodEntity=noticePeriodDAO.findById(voEmployeeSeparationDetails.getNoticePeriod().getId());
				employeeSeparationDetailsEntity.setEmployee(employeeEntity);
				employeeSeparationDetailsEntity.setModeofSeparation(masterModeofSeparationEntity);
				employeeSeparationDetailsEntity.setEmpSeparationReason(empSeaparationReasonEntity);
				// employeeSeparationDetailsEntity.setNoticePeriod(noticePeriodEntity);
				if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoReason())
						&& !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoReason().getId()))
					roSeparationReasonEntity = mstSeparationReasonDAO
							.findById(voEmployeeSeparationDetails.getRoReason().getId()).get();
				if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgReason())
						&& !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgReason().getId()))
					orgSeparationReasonEntity = mstSeparationReasonDAO
							.findById(voEmployeeSeparationDetails.getOrgReason().getId()).get();
				if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getHRReason())
						&& !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getHRReason().getId()))
					hrSeparationReasonEntity = mstSeparationReasonDAO
							.findById(voEmployeeSeparationDetails.getHRReason().getId()).get();
				employeeSeparationDetailsEntity.setRoReason(roSeparationReasonEntity);
				employeeSeparationDetailsEntity.setOrgReason(orgSeparationReasonEntity);
				employeeSeparationDetailsEntity.setHRReason(hrSeparationReasonEntity);

				employeeSeparationDetailsEntity = HRMSRequestTranslator.translateToEmployeeSeparationDetails(
						employeeSeparationDetailsEntity, voEmployeeSeparationDetails);
				if (voEmployeeSeparationDetails.getOrgApproverStatus() != null && voEmployeeSeparationDetails
						.getOrgApproverStatus().equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED)) {
					if (employeeSeparationDetailsEntity.getRoApproverStatus() == null) {
						throw new HRMSException(IHRMSConstants.SEPARATIONWRONGACTIONCODE,
								IHRMSConstants.EMPLOYEESEPARATIONWRONACTION_MESSAGE);
					} else {

						addEntryInChecklistTables(voEmployeeSeparationDetails, employeeEntity);
					}
				}
				// employeeSeparationDetailsEntity.setStatus(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED);
				employeeSeparationDetailsEntity = employeeSeparationDAO.save(employeeSeparationDetailsEntity);
				actionbyApproversForSeparation(employeeEntity, voEmployeeSeparationDetails);
			}

		}

		return employeeSeparationDetailsEntity;
	}

	/*
	 * This method is for when approver rejects resignation
	 * 
	 */
	private EmployeeSeparationDetails resignationRejectedAction(VOEmployeeSeparationDetails voEmployeeSeparationDetails,
			HttpServletRequest servletRequest) throws HRMSException, Exception {

		logger.info(" == ACTION -> Separation Rejected << ==");

		EmployeeSeparationDetails employeeSeparationDetailsEntity;
		Employee employeeEntity;
		MasterModeofSeparation masterModeofSeparationEntity;
		MasterModeofSeparationReason empSeaparationReasonEntity;

		MasterModeofSeparationReason roSeparationReasonEntity = null;
		MasterModeofSeparationReason orgSeparationReasonEntity = null;
		MasterModeofSeparationReason hrSeparationReasonEntity = null;
		employeeSeparationDetailsEntity = employeeSeparationDAO.findSeparationDetailsUsingEmpIdandStatus(
				IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING, voEmployeeSeparationDetails.getEmployee().getId());

		if (!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity)) {
			/* update */

			masterModeofSeparationEntity = mstModeofSeparationDAO
					.findById(voEmployeeSeparationDetails.getModeofSeparation().getId()).get();
			empSeaparationReasonEntity = mstSeparationReasonDAO
					.findById(voEmployeeSeparationDetails.getEmpseparationReason().getId()).get();
			// noticePeriodEntity=noticePeriodDAO.findById(voEmployeeSeparationDetails.getNoticePeriod().getId());
			employeeEntity = employeeDAO.findById(voEmployeeSeparationDetails.getEmployee().getId()).get();
			employeeSeparationDetailsEntity.setEmployee(employeeEntity);
			employeeSeparationDetailsEntity.setModeofSeparation(masterModeofSeparationEntity);
			employeeSeparationDetailsEntity.setEmpSeparationReason(empSeaparationReasonEntity);
			// employeeSeparationDetailsEntity.setNoticePeriod(noticePeriodEntity);
			if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoReason())
					&& !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoReason().getId()))
				roSeparationReasonEntity = mstSeparationReasonDAO
						.findById(voEmployeeSeparationDetails.getRoReason().getId()).get();
			if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgReason())
					&& !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgReason().getId()))
				orgSeparationReasonEntity = mstSeparationReasonDAO
						.findById(voEmployeeSeparationDetails.getOrgReason().getId()).get();
			if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getHRReason())
					&& !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getHRReason().getId()))
				hrSeparationReasonEntity = mstSeparationReasonDAO
						.findById(voEmployeeSeparationDetails.getHRReason().getId()).get();
			employeeSeparationDetailsEntity.setRoReason(roSeparationReasonEntity);
			employeeSeparationDetailsEntity.setOrgReason(orgSeparationReasonEntity);
			employeeSeparationDetailsEntity.setHRReason(hrSeparationReasonEntity);

			employeeSeparationDetailsEntity = HRMSRequestTranslator
					.translateToEmployeeSeparationDetails(employeeSeparationDetailsEntity, voEmployeeSeparationDetails);
			// employeeSeparationDetailsEntity.setStatus(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED);

			/*
			 * code start
			 * 
			 * @author: Rushikesh Thorat Date:13-07-2023 code Added because HR and RO both
			 * are same then Can't Reject resignation.
			 */
			String token = JWTTokenHelper.parseJwt(servletRequest);
			Claims loggedInEmpDetails;
			if (!HRMSHelper.isNullOrEmpty(token)) {
				loggedInEmpDetails = JWTTokenHelper.getLoggedInEmpDetail(token);
			} else {
				throw new HRMSException(IHRMSConstants.failedCode, IHRMSConstants.TOKEN_NOT_VALID);
			}

			if (loggedInEmpDetails.get("hasRole").equals("HR") && voEmployeeSeparationDetails.getApproval_status()
					.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_STATUS_RO_PENDING)) {
				Long.valueOf(loggedInEmpDetails.get("employeeId").toString());
				if (employeeSeparationDetailsEntity.getEmployee().getEmployeeReportingManager().getReporingManager()
						.getId().equals(Long.valueOf(loggedInEmpDetails.get("employeeId").toString()))) {
					voEmployeeSeparationDetails.setRoApproverStatus(voEmployeeSeparationDetails.getOrgApproverStatus());
					voEmployeeSeparationDetails.setRoActionDate(voEmployeeSeparationDetails.getOrgActionDate());
					voEmployeeSeparationDetails.setRoComment(voEmployeeSeparationDetails.getOrgComment());
					voEmployeeSeparationDetails.setOrgApproverStatus(null);
					voEmployeeSeparationDetails.setRoActionDate(null);
					voEmployeeSeparationDetails.setRoComment(null);
				}
			}
			/* code end */

			if (voEmployeeSeparationDetails.getOrgApproverStatus() != null && voEmployeeSeparationDetails
					.getOrgApproverStatus().equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED)) {
				if (employeeSeparationDetailsEntity.getRoApproverStatus() == null) {
					throw new HRMSException(IHRMSConstants.SEPARATIONWRONGACTIONCODE,
							IHRMSConstants.EMPLOYEESEPARATIONWRONACTION_MESSAGE);
				}

			}
			employeeSeparationDetailsEntity.setStatus(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED);
			employeeSeparationDetailsEntity = employeeSeparationDAO.save(employeeSeparationDetailsEntity);
			employeeSeparationDetailsEntity.setIsActive("N");
			resignationRejectedByApprovers(employeeEntity, voEmployeeSeparationDetails);
		}

		return employeeSeparationDetailsEntity;
	}

	/**
	 * This method is for when Org_level approves resignation and Entry added in
	 * Employee Catalogue table Also in Employee Catalogue Checklist table.
	 */
	private void addEntryInChecklistTables(VOEmployeeSeparationDetails voEmployeeSeparationDetails,
			Employee employeeEntity) {

		logger.info("Added Entry in Employee Catalogue Table and Employee Catalogue Checklist");
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

	/**
	 * 
	 * This is use to get details of separation employee list according to their
	 * manager
	 */
	@RequestMapping(value = "manager/{orgId}/{mgrId}/{page}/{size}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getEmployeeResignationApppliedDetailsToManager(@RequestBody @PathVariable("orgId") long orgId,
			@RequestBody @PathVariable("mgrId") long mgrId, @PathVariable("page") int page,
			@PathVariable("size") int size) {

		if (size <= 0) {
			size = 10;
		}

		int totalCount = 0;
		PageRequest pageRequest = PageRequest.of(page, size);
		List<EmployeeSeparationDetails> empseparationDetailsEntity = null;
		try {

			logger.info(" == EMPLOYEE APPLIED Separation DETAILS TO MANAGER ==");
			HRMSListResponseObject response = new HRMSListResponseObject();
			List<Object> listResponse = new ArrayList<Object>();
			List<MasterOrganizationEmailConfig> masterConfigEmialList = configDAO.findBYorgLevelEmployeeAndOrgId(mgrId,
					SecurityFilter.TL_CLAIMS.get().getOrgId());
			if (!HRMSHelper.isNullOrEmpty(masterConfigEmialList)) {

				empseparationDetailsEntity = employeeSeparationDAO.findAllResignationAppliedEmployee(
						IHRMSConstants.isActive, IHRMSConstants.isActive,
						IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING,
						IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_WITHDRAW, orgId/* , pageRequest */);
				logger.info("empseparationDetailsEntity.size() " + empseparationDetailsEntity.size());
				totalCount = employeeSeparationDAO.findAllResignationAppliedEmployeeCount(IHRMSConstants.isActive,
						IHRMSConstants.isActive, IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING,
						IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_WITHDRAW, orgId);
			} else {
				empseparationDetailsEntity = employeeSeparationDAO.findSubordinateResignationApplied(
						IHRMSConstants.isActive, IHRMSConstants.isActive, mgrId,
						IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING,
						IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_WITHDRAW, orgId/* , pageRequest */);
				totalCount = employeeSeparationDAO.findResignedEmployee(IHRMSConstants.isActive,
						IHRMSConstants.isActive, mgrId, IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING,
						IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_WITHDRAW, orgId);
			}
			for (EmployeeSeparationDetails employeeSeparationDetailsEntity : empseparationDetailsEntity) {

				VOEmployeeSeparationDetails voEmployeeSeparationDetails = HRMSEntityToModelMapper
						.convertToEmpSeparationDetailsVO(employeeSeparationDetailsEntity);
				listResponse.add(voEmployeeSeparationDetails);
			}

			response.setTotalCount(totalCount);
			response.setResponseCode(IHRMSConstants.successCode);
			response.setResponseMessage(IHRMSConstants.successMessage);
			response.setListResponse(listResponse);

			return HRMSHelper.createJsonString(response);

		} /*
			 * catch (HRMSException e) { e.printStackTrace(); try { return
			 * HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());
			 * 
			 * } catch (Exception e1) { e1.printStackTrace(); } }
			 */catch (Exception unknown) {
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
	 * This method is to check for exit Feedback form enabled or not
	 * 
	 * 
	 */
	@RequestMapping(value = "separationexitformcheck/{empId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String employeeFeedbackFormCheckService(@PathVariable("empId") long empId) {

		try {
			if (!HRMSHelper.isLongZero(empId)) {

				EmployeeSeparationDetails employeeSeparationDetailsEntity = employeeSeparationDAO
						.findSeparationDetailsUsingEmpIdandStatus(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING,
								empId);
				if (!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity)) {
					HRMSListResponseObject response = new HRMSListResponseObject();
					List<Object> objectList = new ArrayList<Object>();
					if (employeeSeparationDetailsEntity.getOrgApproverStatus() != null
							&& employeeSeparationDetailsEntity.getOrgApproverStatus()
									.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED))
						response.setExitfeedbackFormenabled(true);
					response.setListResponse(objectList);
					response.setResponseCode(IHRMSConstants.successCode);
					response.setResponseMessage(IHRMSConstants.successMessage);

					return HRMSHelper.createJsonString(response);
				} else
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
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
	 * 
	 * To Get Status of Checklist approvals and Resignation
	 */
	@RequestMapping(value = "status/{empId}/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String checkStatusofResignationandChecklist(@PathVariable("empId") long empId,
			@PathVariable("id") long empSeparationId) {
		try {
			if (!HRMSHelper.isLongZero(empId) && !HRMSHelper.isLongZero(empSeparationId)) {
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
				VOEmployeeSeparationChecklistandResignationStatus voempchklistandSepStatusForEmpMngr = new VOEmployeeSeparationChecklistandResignationStatus();
				VOEmployeeSeparationChecklistandResignationStatus voempchklistandSepStatusForEmpOrg = new VOEmployeeSeparationChecklistandResignationStatus();
				List<Object> objectList = new ArrayList<Object>();

				/*
				 * List<EmployeeSeparationDetails> empSeparationDetailsList =
				 * employeeSeparationDAO .findSeparationDetailsByEmpId(empId,
				 * IHRMSConstants.isActive); for (EmployeeSeparationDetails empseparationEntity
				 * : empSeparationDetailsList) {
				 * voempchklistandSepStatusForEmpMngr.setActedon(HRMSDateUtil
				 * .format(empseparationEntity.getRoActionDate(),
				 * IHRMSConstants.FRONT_END_DATE_FORMAT));
				 * voempchklistandSepStatusForEmpMngr.setStatus(empseparationEntity.
				 * getRoApproverStatus());
				 * voempchklistandSepStatusForEmpOrg.setActedon(HRMSDateUtil
				 * .format(empseparationEntity.getOrgActionDate(),
				 * IHRMSConstants.FRONT_END_DATE_FORMAT));
				 * voempchklistandSepStatusForEmpOrg.setStatus(empseparationEntity.
				 * getOrgApproverStatus());
				 * 
				 * }
				 */
				empSeparationId = 0;
				List<EmployeeSeparationDetails> empSeparationlist = employeeSeparationDAO.findByemployeeId(empId);
				for (EmployeeSeparationDetails empseparationdetails : empSeparationlist) {
					if (empseparationdetails.getId() > empSeparationId)
						empSeparationId = empseparationdetails.getId();
				}

				EmployeeSeparationDetails empSeparationDetails = employeeSeparationDAO.findById(empSeparationId).get();
				voempchklistandSepStatusForEmpMngr.setActedon(HRMSDateUtil
						.format(empSeparationDetails.getRoActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
				voempchklistandSepStatusForEmpMngr.setStatus(empSeparationDetails.getRoApproverStatus());
				voempchklistandSepStatusForEmpOrg.setActedon(HRMSDateUtil
						.format(empSeparationDetails.getOrgActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
				voempchklistandSepStatusForEmpOrg.setStatus(empSeparationDetails.getOrgApproverStatus());

				voempchklistandSepStatusForEmpMngr.setApproverName(employeeReportingMngr.getEmployeeReportingManager()
						.getReporingManager().getCandidate().getFirstName()
						+ " "
						+ employeeReportingMngr.getEmployeeReportingManager().getReporingManager().getCandidate()
								.getLastName()
						+ " -" + employeeReportingMngr.getEmployeeReportingManager().getReporingManager().getId());
				voempchklistandSepStatusForEmpMngr.setStage(IHRMSConstants.RESIGNATION_APPROVAL);

				objectList.add(voempchklistandSepStatusForEmpMngr);

				voempchklistandSepStatusForEmpOrg.setApproverName(togetOrgLevel.getCandidate().getFirstName() + " "
						+ togetOrgLevel.getCandidate().getLastName() + " -" + togetOrgLevel.getId());
				voempchklistandSepStatusForEmpOrg.setStage(IHRMSConstants.RESIGNATION_APPROVAL);
				objectList.add(voempchklistandSepStatusForEmpOrg);

				if (!empSeparationDetails.getStatus().equals(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED)
						&& (empSeparationDetails.getIsActive().equalsIgnoreCase(IHRMSConstants.isActive))) {
					for (MapEmployeeCatalogue employeeCatalogueEntity : empCatalogueList) {

						if (employeeCatalogueEntity.getCatalogue().getApprover() != null) {

							VOEmployeeSeparationChecklistandResignationStatus voempchecklistandResignationStatus = new VOEmployeeSeparationChecklistandResignationStatus();
							Employee employee = employeeDAO
									.findById(employeeCatalogueEntity.getCatalogue().getApprover().getId()).get();
							logger.info("employeeCatalogueEntity.getId()   =====  " + employeeCatalogueEntity.getId());
							List<MapEmployeeCatalogueChecklist> empcatalogueChecklistItemList = empcatalogueChecklistDAO
									.findByEmployeeCatalogueMapping(employeeCatalogueEntity.getId());
							logger.info("Length == " + empcatalogueChecklistItemList.size());
							double amount = 0;
							List<VOMapEmployeeCatalogueChecklist> checklist = new ArrayList<VOMapEmployeeCatalogueChecklist>();
							for (MapEmployeeCatalogueChecklist mapCatalogueChecklistItemEntity : empcatalogueChecklistItemList) {

								amount = amount + mapCatalogueChecklistItemEntity.getAmount();
								VOMapEmployeeCatalogueChecklist voempCataloguechecklist = HRMSEntityToModelMapper
										.convertToEmployeeChecklistMapping(mapCatalogueChecklistItemEntity);
								checklist.add(voempCataloguechecklist);

							}
							totalamountofCatalogue = totalamountofCatalogue + amount;
							voempchecklistandResignationStatus.setEmpCatalogueChecklist(checklist);
							voempchecklistandResignationStatus.setTotalamount(amount);
							voempchecklistandResignationStatus.setApproverName(employee.getCandidate().getFirstName()
									+ " " + employee.getCandidate().getLastName() + " -" + employee.getId());
							voempchecklistandResignationStatus.setActedon(HRMSDateUtil.format(
									employeeCatalogueEntity.getActedOn(), IHRMSConstants.FRONT_END_DATE_FORMAT));
							voempchecklistandResignationStatus
									.setStage(employeeCatalogueEntity.getCatalogue().getName());
							voempchecklistandResignationStatus.setStatus(employeeCatalogueEntity.getStatus());
							objectList.add(voempchecklistandResignationStatus);

						} else {
							VOEmployeeSeparationChecklistandResignationStatus voempchecklistandResignationStatus = new VOEmployeeSeparationChecklistandResignationStatus();
							logger.info("employeeCatalogueEntity.getId()   =====  " + employeeCatalogueEntity.getId());
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
							voempchecklistandResignationStatus.setEmpCatalogueChecklist(checklist);
							voempchecklistandResignationStatus.setTotalamount(amount);
							voempchecklistandResignationStatus.setApproverName(employeeReportingMngr
									.getEmployeeReportingManager().getReporingManager().getCandidate().getFirstName()
									+ " "
									+ employeeReportingMngr.getEmployeeReportingManager().getReporingManager()
											.getCandidate().getLastName()
									+ " -"
									+ employeeReportingMngr.getEmployeeReportingManager().getReporingManager().getId());
							voempchecklistandResignationStatus.setActedon(HRMSDateUtil.format(
									employeeCatalogueEntity.getActedOn(), IHRMSConstants.FRONT_END_DATE_FORMAT));
							voempchecklistandResignationStatus
									.setStage(employeeCatalogueEntity.getCatalogue().getName());
							voempchecklistandResignationStatus.setStatus(employeeCatalogueEntity.getStatus());
							objectList.add(voempchecklistandResignationStatus);
						}
					}
				}

				HRMSListResponseObject response = new HRMSListResponseObject();
				response.setTotalamountofCatalogue(totalamountofCatalogue);
				response.setListResponse(objectList);
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(IHRMSConstants.successMessage);

				return HRMSHelper.createJsonString(response);
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.CandidateDoesnotExistMessage);
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
	 * This service will provide the manager subordinate brief details under the
	 * provided manager id provided,if manager id is 0 then it will search all the
	 * employee under the provided organization
	 * 
	 * This service will by utilized for HR and Manager
	 * 
	 * Separation Employee
	 */

	@RequestMapping(method = RequestMethod.GET, value = "/findSubordinate/{orgId}/{managerId}/{divisionId}", produces = "application/json")
	public String findEmployeeIdAndNameForManager(@PathVariable("orgId") long orgId,
			@PathVariable("managerId") long managerId, @PathVariable("divisionId") long divisionId)
			throws JsonGenerationException, JsonMappingException, IOException, HRMSException {
		try {
			HRMSListResponseObject hrmsListResponseObject = null;
			List<Object> voEmployees = new ArrayList<Object>();
			if (!HRMSHelper.isLongZero(orgId)) {

				List<MapCatalogue> mapcatalogue = mapCatalogueDAO.findByApproverId(managerId, orgId, divisionId);

				if (managerId > 0 && mapcatalogue == null) {
					logger.info("Finding Subordinate For Manager : ");
					List<EmployeeReportingManager> reportingManager = reportingManagerDAO
							.findSeparationEmployeeByreporingManager(managerId, IHRMSConstants.isActive,
									IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING);
					for (EmployeeReportingManager rptmgr : reportingManager) {
						if (rptmgr.getEmployee().getIsActive().equalsIgnoreCase(IHRMSConstants.isActive)) {

							VOEmployee employeeModel = HRMSEntityToModelMapper
									.convertToEmployeeModel(rptmgr.getEmployee());
							voEmployees.add(employeeModel);
						}
					}
				} else {
					logger.info("Finding All , Based On Organization  ... ");
					List<Employee> employees = employeeSeparationDAO.getAllSeparationEmpNameIdByOrg(orgId,
							IHRMSConstants.isActive);
					if (!HRMSHelper.isNullOrEmpty(employees)) {
						for (Employee employeeEntity : employees) {
							VOEmployee employeeModel = HRMSEntityToModelMapper.convertToEmployeeModel(employeeEntity);
							voEmployees.add(employeeModel);
						}

					} else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
					}
				}
				if (voEmployees.isEmpty()) {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
			hrmsListResponseObject = new HRMSListResponseObject();
			hrmsListResponseObject.setListResponse(voEmployees);
			hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
			hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
			return HRMSHelper.createJsonString(hrmsListResponseObject);
		} catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
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
	 * This method is get Resigned Employees according to Filters
	 * 
	 * @param employeeCode
	 * @param departmentId
	 * @param divisionId
	 * @param designationId
	 * @param branchId
	 * @param fromDate
	 * @param toDate
	 * @param status
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping(value = "searchResignedEmpByFilter/{orgId}/{empId}/{departmentId}/{divisionId}/{designationId}/{branchId}/{fromDate}/{toDate}/{status}/{page}/{size}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String findResignedEmployeeByFilters(@PathVariable("orgId") long orgId,
			@PathVariable("empId") long employeeCode, @PathVariable("departmentId") long departmentId,
			@PathVariable("divisionId") long divisionId, @PathVariable("designationId") long designationId,
			@PathVariable("branchId") long branchId, @PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate, @PathVariable("status") String status,
			@PathVariable("page") int page, @PathVariable("size") int size) {

		try {

			if (size <= 0) {
				size = 10;
			}
			List<Object> listResponse = new ArrayList<Object>();
			HRMSListResponseObject response = new HRMSListResponseObject();
			List<EmployeeSeparationDetails> list = null;
			long totalCount = 0;

			if (employeeCode == 0 && departmentId == 0 && divisionId == 0 && designationId == 0 && branchId == 0
					&& HRMSHelper.isNullOrEmpty(fromDate) && HRMSHelper.isNullOrEmpty(toDate)
					&& HRMSHelper.isNullOrEmpty(status)) {
				PageRequest pagination = PageRequest.of(page, size);
				// list = employeeSeparationDAO.findByisActive(IHRMSConstants.isActive);
				list = employeeSeparationDAO.findAllByStatus(IHRMSConstants.EMPLOYEE_ABSCONDED,
						IHRMSConstants.EMPLOYEE_TERMINATED, IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING,
						IHRMSConstants.EMPLOYEE_DEMISE, IHRMSConstants.isActive, orgId/* , pagination */);
				totalCount = employeeSeparationDAO.findAllByStatusForCount(IHRMSConstants.EMPLOYEE_ABSCONDED,
						IHRMSConstants.EMPLOYEE_TERMINATED, IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING,
						IHRMSConstants.EMPLOYEE_DEMISE, IHRMSConstants.isActive, orgId);

			} else {

				PageRequest pagination = PageRequest.of(page, size);

				if (fromDate != null && !fromDate.equalsIgnoreCase("null") && toDate != null
						&& toDate.equalsIgnoreCase("null")) {
					Date from = HRMSDateUtil.parse(fromDate, IHRMSConstants.FRONT_END_DATE_FORMAT);
					Date to = HRMSDateUtil.parse(toDate, IHRMSConstants.FRONT_END_DATE_FORMAT);
					list = employeeSeparationDAO.findSeperatedEmployeeWithFilters(employeeCode, departmentId,
							divisionId, designationId, branchId, from, to, status, IHRMSConstants.isActive,
							orgId/*
									 * , pagination
									 */);
					totalCount = employeeSeparationDAO.findSeperatedEmployeeWithFiltersForCount(employeeCode,
							departmentId, divisionId, designationId, branchId, from, to, status,
							IHRMSConstants.isActive, orgId);
				} else {
					// Date from = HRMSDateUtil.parse(fromDate,
					// IHRMSConstants.FRONT_END_DATE_FORMAT);
					// Date to = HRMSDateUtil.parse(toDate, IHRMSConstants.FRONT_END_DATE_FORMAT);
					list = employeeSeparationDAO.findSeperatedEmployeeWithFilters(employeeCode, departmentId,
							divisionId, designationId, branchId, status, IHRMSConstants.isActive,
							orgId/* , pagination */);
					totalCount = employeeSeparationDAO.findSeperatedEmployeeWithFiltersForCount(employeeCode,
							departmentId, divisionId, designationId, branchId, status, IHRMSConstants.isActive, orgId);
				}

			}
			if (list != null && !list.isEmpty()) {
				for (EmployeeSeparationDetails empSeperation : list) {
					VOEmployeeSeparationDetails model = HRMSEntityToModelMapper
							.convertToEmpSeparationDetailsVO(empSeperation);
					listResponse.add(model);
				}
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
			}
			response.setPageNo(page);
			response.setPageSize(size);
			response.setTotalCount(totalCount);
			response.setListResponse(listResponse);
			response.setResponseCode(IHRMSConstants.successCode);
			response.setResponseMessage(IHRMSConstants.successMessage);
			return HRMSHelper.createJsonString(response);

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

	public void resignationRejectedByApprovers(Employee empEntity,
			VOEmployeeSeparationDetails voEmployeeSeparationDetails) {
		logger.info("Inside Mail sender");
		String employeeEmailId = empEntity.getOfficialEmailId();
		String reportingManagerEmailId = empEntity.getEmployeeReportingManager().getReporingManager()
				.getOfficialEmailId();
		MasterOrganizationEmailConfig masterOrganizationEmailConfigEntity = configDAO.findByorganizationAnddivision(
				empEntity.getCandidate().getLoginEntity().getOrganization().getId(),
				empEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId());

		String orgLevelEmailId = masterOrganizationEmailConfigEntity.getOrgLevelEmployee().getOfficialEmailId();

		String hrEmailIds = "";
		List<OrgDivWiseHRMapping> orgDivHRList = orgDivHRDAO.findOrgDivWiseHrMapping(
				empEntity.getCandidate().getLoginEntity().getOrganization().getId(),
				empEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				IHRMSConstants.isActive);
		for (OrgDivWiseHRMapping orgDivHRMApEntity : orgDivHRList) {
			hrEmailIds = hrEmailIds + orgDivHRMApEntity.getEmployee().getOfficialEmailId() + ";";
		}
		String ccEmailIds = reportingManagerEmailId + ";" + orgLevelEmailId + ";" + hrEmailIds;
		Map<String, String> placeHolderMapping = HRMSRequestTranslator
				.createPlaceHolderMapForApproveSeparation(empEntity, voEmployeeSeparationDetails);
		placeHolderMapping.put("{websiteURL}", baseURL);

		String mailContent = HRMSHelper.replaceString(placeHolderMapping,
				IHRMSEmailTemplateConstants.Template_Empployee_Resignation_Approve);

		String mailSubject = "";
		if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoApproverStatus())
				|| !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgApproverStatus())) {
			if ((!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoApproverStatus())
					&& voEmployeeSeparationDetails.getRoApproverStatus()
							.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED))) {
				mailSubject = IHRMSConstants.MailSubject_SEPARATION_REJECTED;
			}
			if ((!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoApproverStatus())
					&& voEmployeeSeparationDetails.getRoApproverStatus()
							.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED))) {
				mailSubject = IHRMSConstants.MailSubject_SEPARATION_APPROVED;
			}
			if ((!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgApproverStatus())
					&& voEmployeeSeparationDetails.getOrgApproverStatus()
							.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED))) {
				mailSubject = IHRMSConstants.MailSubject_SEPARATION_REJECTED;
			}
			if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgApproverStatus())
					&& voEmployeeSeparationDetails.getOrgApproverStatus()
							.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED)) {
				mailContent = HRMSHelper.replaceString(placeHolderMapping,
						IHRMSEmailTemplateConstants.Template_Empployee_Resignation_Approve_By_ORG_LEVEL);
				mailSubject = IHRMSConstants.MailSubject_SEPARATION_APPROVED;
			}
		}
		if ((!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoApproverStatus()) && voEmployeeSeparationDetails
				.getRoApproverStatus().equalsIgnoreCase(IHRMSConstants.EMPLOYEE_WD_SEPARATION_STATUS_REJECTED))
				|| (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgApproverStatus())
						&& voEmployeeSeparationDetails.getOrgApproverStatus()
								.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_WD_SEPARATION_STATUS_REJECTED))) {
			mailSubject = IHRMSConstants.MailSubject_WD_SEPARATION_REJECTED;
		}
		if ((!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoApproverStatus()) && voEmployeeSeparationDetails
				.getRoApproverStatus().equalsIgnoreCase(IHRMSConstants.EMPLOYEE_WD_SEPARATION_STATUS_APPROVED))
				|| (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgApproverStatus())
						&& voEmployeeSeparationDetails.getOrgApproverStatus()
								.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_WD_SEPARATION_STATUS_APPROVED))) {
			// placeHolderMapping.put("{Resignation}","pull back");
			mailSubject = IHRMSConstants.MailSubject_WD_SEPARATION_APPROVED;
		}

		logger.info("ccemailids resignation rejected by approver " + ccEmailIds);
		emailsender.toPersistEmail(employeeEmailId, ccEmailIds, mailContent, mailSubject,
				empEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				empEntity.getCandidate().getLoginEntity().getOrganization().getId());

	}

	/**
	 * This method is use to Check Resignation status of
	 */
	public void sendMailToChecklistApprovals() {
		try {
			logger.info("Inside to calculate days method");
			List<EmployeeSeparationDetails> empSeparaationDetailsList = employeeSeparationDAO
					.getResignedEmployeeByStatus(IHRMSConstants.isActive, IHRMSConstants.isActive,
							IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING);
			Date dt2 = new Date();
			for (EmployeeSeparationDetails empSeparationDetailsEntity : empSeparaationDetailsList) {

				// long diff = dt2.getTime() -
				// empSeparationDetailsEntity.getActualRelievingDate().getTime();
				int diffInDays = (int) ((empSeparationDetailsEntity.getActualRelievingDate().getTime() - dt2.getTime())
						/ (1000 * 60 * 60 * 24));
				logger.info("Difference in days " + diffInDays);
				if (diffInDays > 7) {
					logger.info("Inside 7 days if ");
					if (empSeparationDetailsEntity.getOrgApproverStatus() != null
							&& empSeparationDetailsEntity.getOrgApproverStatus()
									.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED)) {
						logger.info("Inside Approvals check ");
						systemEscalationForORG(empSeparationDetailsEntity);
					}
				} else {

				}
			}

			/*
			 * List<Object[]> result = employeeDAO.findIfEmployeeHasBirthdayCurrentDay(); if
			 * (result != null && !result.isEmpty()) {
			 * logger.info(" Employee with Birthday Found "); for (Object[] resultSet :
			 * result) { long orgId = Long.parseLong(String.valueOf(resultSet[0])); long
			 * divisionId = Long.parseLong(String.valueOf(resultSet[1]));
			 * findAndSendEmailToEmployeeForBirthDay(divisionId, orgId); } } else {
			 * logger.info("No Employee has Birthday Today"); }
			 */
		} catch (Exception ee) {
			logger.info("Inside Exception ");
			ee.printStackTrace();
		}
	}

	public void systemEscalationForRO(EmployeeSeparationDetails employeeSeparationDetails) {

	}

	public void systemEscalationForORG(EmployeeSeparationDetails employeeSeparationDetailsEntity) {
		logger.info("inside systemEscalationForORG ");
		Employee empEntity = employeeDAO.findById(employeeSeparationDetailsEntity.getEmployee().getId()).get();
		Map<String, String> placeHolderMapping = HRMSRequestTranslator.createPlaceHolderApproverChecklist(empEntity,
				employeeSeparationDetailsEntity);
		placeHolderMapping.put("{websiteURL}", baseURL);

		String mailContent = HRMSHelper.replaceString(placeHolderMapping,
				IHRMSEmailTemplateConstants.Template_FOR_CHECKLIST_APPROVALS);

		String mailSubject = IHRMSConstants.MAILSUBJECTFORCHECKLISTAPPROVALS;
		String to = "";

		List<MapCatalogue> mapCatalogueList = mapCatalogueDAO.findAllCataloguewithEmployeeByisActiveAndOrgId(
				IHRMSConstants.isActive,
				empEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
				SecurityFilter.TL_CLAIMS.get().getOrgId());
		for (MapCatalogue mapCatalogueEntity : mapCatalogueList) {
			if (mapCatalogueEntity.getApprover() != null) {
				to = to + mapCatalogueEntity.getApprover().getOfficialEmailId() + ";";
				logger.info("To Whom send " + to);
			}
		}

		// String accountEmailId=accountEmployee.getOfficialEmailId();
		try {
			// HRMSHelper
			eventEmailSender.toSendEmailScheduler(to, null, null, mailContent, mailSubject,
					employeeSeparationDetailsEntity.getEmployee().getCandidate().getCandidateProfessionalDetail()
							.getDivision().getId(),
					employeeSeparationDetailsEntity.getEmployee().getCandidate().getLoginEntity().getOrganization()
							.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This service is use when HR clicks on MarkStatus In this we disable that
	 * employee and its candidate status as inActive
	 * 
	 * 
	 */
	@RequestMapping(value = "/markstatus/{empId}", method = RequestMethod.GET, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String markstatusforResignedEmployee(@PathVariable("empId") long resignedEmpId) {
		try {
			EmployeeSeparationDetails employeeSeparationDetailsEntity;
			Employee employeeEntity;

			if (!HRMSHelper.isLongZero(resignedEmpId)) {

				// employeeSeparationDetailsEntity =
				// employeeSeparationDAO.findSeparationDetailsUsingEmpIdandStatus(
				// IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING,
				// resignedEmpId);

				List<MapEmployeeCatalogue> mapcatalogueList = empCatalogueMapDAO.findByEmployeeId(resignedEmpId);

				for (MapEmployeeCatalogue catalogueEntity : mapcatalogueList) {
					if (catalogueEntity.getStatus().equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING))
						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.EMPLOYEE_SEPARATION_MARK_STATUS_COMPLETED_ERROR_MESSAGE);
				}

				employeeSeparationDetailsEntity = employeeSeparationDAO
						.findSeparationDetailsUsingEmpIdAndStatusNoReject(
								IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED, resignedEmpId,
								IHRMSConstants.isActive);
				employeeEntity = employeeDAO.findById(resignedEmpId).get();
				if (!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity)) {
					employeeSeparationDetailsEntity.setStatus(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_COMPLETED);
					employeeEntity.setIsActive(IHRMSConstants.isNotActive);
					employeeDAO.save(employeeEntity);
					Candidate candidateEntity = candidateDAO.findById(employeeEntity.getCandidate().getId()).get();
					candidateEntity.setIsActive(IHRMSConstants.isNotActive);
					candidateDAO.save(candidateEntity);

				}
				employeeSeparationDetailsEntity = employeeSeparationDAO.save(employeeSeparationDetailsEntity);
				return HRMSHelper.sendSuccessResponse(IHRMSConstants.updatedsuccessMessage, IHRMSConstants.successCode);

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
			unknown.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	@RequestMapping(value = "/upload", method = { RequestMethod.POST,
			RequestMethod.PUT }, produces = "application/json")
	@ResponseBody
	public String submitEmployeeCatalogueChecklist(@RequestParam("file") MultipartFile request, String resignEmpId)
			throws HRMSException {
		try {
			Sardine sardine = null;
			sardine = SardineFactory.begin();
			if (!HRMSHelper.isNullOrEmpty(resignEmpId)) {
				Employee resignedemployeeEntity = employeeDAO.findById(Long.parseLong(resignEmpId)).get();
				EmployeeSeparationDetails employeeSeparationDetailsEntity = employeeSeparationDAO
						.findSeparationDetailsUsingEmpIdandStatus(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING,
								Long.parseLong(resignEmpId));

				String savePath = HRMSFileuploadUtil.directoryCreationForSeparationusingEmployeeId(baseURL,
						resignedemployeeEntity);
				if (HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity)) {
					employeeSeparationDetailsEntity.setStatus(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING);
					employeeSeparationDetailsEntity.setEmployee(resignedemployeeEntity);

				}
				if (!HRMSHelper.isNullOrEmpty(request)) {

					logger.info("====  path of Separation  ====" + savePath);
					// String str = savePath.concat("/"+IHRMSConstants.SEPARATIONFOLDERNAME) + "/" +
					// request.getOriginalFilename().replaceAll("\\s+", "_");
					String str = savePath + "/" + request.getOriginalFilename().replaceAll("\\s+", "_");
					byte[] bytes = request.getBytes();
					sardine.put(str, bytes);
					employeeSeparationDetailsEntity
							.setSeparationProof(request.getOriginalFilename().replaceAll("\\s+", "_"));
					employeeSeparationDAO.save(employeeSeparationDetailsEntity);
				}
				return HRMSHelper.sendSuccessResponse(IHRMSConstants.successMessage, IHRMSConstants.successCode);
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

	private void sendMailToempTermAbsDemisReason(EmployeeSeparationDetails employeeSeparationDetailsEntity) {
		logger.info("inside systemEscalationForORG ");
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

	private EmployeeSeparationDetails setROandOrgstatusForTerAbscDemiseReason(
			EmployeeSeparationDetails employeeSeparationDetailsEntity) {

		logger.info("SET RO System escalted");
		employeeSeparationDetailsEntity.setRoApproverStatus(IHRMSConstants.EMPLOYEE_SEPARATION_SYSTEM_ESCALATED);
		employeeSeparationDetailsEntity.setRoActionDate(new Date());
		employeeSeparationDetailsEntity.setOrgApproverStatus(IHRMSConstants.EMPLOYEE_SEPARATION_SYSTEM_ESCALATED);
		employeeSeparationDetailsEntity.setOrgActionDate(new Date());
		return employeeSeparationDetailsEntity;

	}

	private EmployeeSeparationDetailsWithHistory setROandOrgstatusForTerAbscDemiseReasonForHistory(
			EmployeeSeparationDetailsWithHistory employeeSeparationDetailsEntityHistory) {

		logger.info("SET RO System escalted");
		employeeSeparationDetailsEntityHistory.setRoApproverStatus(IHRMSConstants.EMPLOYEE_SEPARATION_SYSTEM_ESCALATED);
		employeeSeparationDetailsEntityHistory.setRoActionDate(new Date());
		employeeSeparationDetailsEntityHistory
				.setOrgApproverStatus(IHRMSConstants.EMPLOYEE_SEPARATION_SYSTEM_ESCALATED);
		employeeSeparationDetailsEntityHistory.setOrgActionDate(new Date());
		return employeeSeparationDetailsEntityHistory;

	}

	public void separationremainder() {
		try {
			List<Object[]> result = employeeSeparationDAO.findRemainderforSeparationIfROnotTakenAnyAction();
			if (result != null && !result.isEmpty()) {
				for (Object[] resultSet : result) {
					long orgId = Long.parseLong(String.valueOf(resultSet[0]));
					long divisionId = Long.parseLong(String.valueOf(resultSet[1]));
					findAndSendEmailToRoForNoAction(divisionId, orgId);
				}
			} else {
				logger.info("No Separation Reminder for RO");
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	private void findAndSendEmailToRoForNoAction(long divisionId, long orgId)
			throws FileNotFoundException, IOException, HRMSException {
		try {

			List<EmployeeSeparationDetails> empSeparationDetailsList = employeeSeparationDAO
					.getDetailsRemainderforSeparationIfROnotTakenAnyActionN(divisionId, orgId, IHRMSConstants.isActive,
							sepReminderNoOfDays);

			for (EmployeeSeparationDetails employeeSeparationDetailsEntity : empSeparationDetailsList) {

				Employee empEntity = employeeDAO.findById(employeeSeparationDetailsEntity.getEmployee().getId()).get();

				logger.info("Employee Resign or withdraw his resignation");
				String employeeEmailId = empEntity.getOfficialEmailId();
				String reportingManagerEmailId = empEntity.getEmployeeReportingManager().getReporingManager()
						.getOfficialEmailId();
				MasterOrganizationEmailConfig masterOrganizationEmailConfigEntity = configDAO
						.findByorganizationAnddivision(
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
				logger.info("hrEmail Ids " + hrEmailIds);
				String ccEmailIds = employeeEmailId + ";" + orgLevelEmailId + ";" + hrEmailIds;
				logger.info("ccEmailIds  " + ccEmailIds);
				Map<String, String> placeHolderMapping = HRMSRequestTranslator
						.createPlaceHolderForROReminderifNoActionTaken(empEntity, employeeSeparationDetailsEntity);
				placeHolderMapping.put("{websiteURL}", baseURL);

				String mailContent = HRMSHelper.replaceString(placeHolderMapping,
						IHRMSEmailTemplateConstants.Template_SeparationReminder_forRO);

				String mailSubject = "";
				mailSubject = IHRMSConstants.MailSubject_RESIGNATION_REMINDER;

				logger.info("ccemailids sendEmail to Approvers  " + ccEmailIds);
				emailsender.toPersistEmail(reportingManagerEmailId, ccEmailIds, mailContent, mailSubject,
						empEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
						empEntity.getCandidate().getLoginEntity().getOrganization().getId());

			}

			/*
			 * List<Employee> employeeList =
			 * employeeDAO.findServiceCompletionOfEmployee(divisionId, orgId);
			 * MasterOrganizationEmailConfig config =
			 * configDAO.findByorganizationAnddivision(orgId, divisionId); String
			 * documentVerificationHeader =
			 * IHRMSEmailTemplateConstants.TEMPLATE_SERVICE_COMPLETION;
			 * 
			 * 
			 * String imagePath = rootDirectory + orgId +
			 * "\\" + "serviceCompletionImage\\" + "serviceCompletion.jpg"; // String image
			 * = HRMSHelper.base64ImageConverte(imagePath); Map<String, String>
			 * placeHolderMapping = new HashMap<String, String>(); //
			 * placeHolderMapping.put("{image}", image);
			 * 
			 * if (employeeList != null && !employeeList.isEmpty()) { for (Employee employee
			 * : employeeList) { placeHolderMapping.put("{candidateFirstname}",
			 * employee.getCandidate().getFirstName());
			 * placeHolderMapping.put("{candidateLastname}",
			 * employee.getCandidate().getLastName());
			 * placeHolderMapping.put("{departmentName}",
			 * employee.getCandidate().getCandidateProfessionalDetail()
			 * .getDepartment().getDepartmentName()); int diffrenceInYear = HRMSHelper
			 * .calculateAge(employee.getCandidate().getCandidateProfessionalDetail().
			 * getDateOfJoining()); placeHolderMapping.put("{completedYears}",
			 * String.valueOf(diffrenceInYear));
			 * 
			 * logger.info(employee.getCandidate().getFirstName() + " " +
			 * employee.getCandidate().getLastName() + "Has Completed " + diffrenceInYear +
			 * " Years");
			 * 
			 * String finalMailContent = HRMSHelper.replaceString(placeHolderMapping,
			 * documentVerificationHeader); String emailSubject =
			 * "Service Completion - Year " + HRMSDateUtil.getCurrentYear(); Map<String,
			 * String> map = new HashMap<String, String>(); map.put("image1", imagePath);
			 * eventEmailSender.toSendEmailScheduler(employee.getOfficialEmailId(), null,
			 * config.getGroupEmailId(), finalMailContent, emailSubject, divisionId, orgId,
			 * map); } }
			 */
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	@RequestMapping(value = "getResignedEmpList/{orgId}/{page}/{size}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getResignedEmpList(@PathVariable("orgId") long orgId, @PathVariable("page") int page,
			@PathVariable("size") int size) {

		try {

			if (size <= 0) {
				size = 10;
			}
			List<Object> listResponse = new ArrayList<Object>();
			HRMSListResponseObject response = new HRMSListResponseObject();
			List<EmployeeSeparationDetails> list = null;
			long totalCount = 0;

			PageRequest pagination = PageRequest.of(page, size);
			// list = employeeSeparationDAO.findByisActive(IHRMSConstants.isActive);
			list = employeeSeparationDAO.getResignedEmpList(IHRMSConstants.isActive, orgId/* , pagination */);
			totalCount = employeeSeparationDAO.getResignedEmpListForCount(IHRMSConstants.isActive, orgId);

			if (list != null && !list.isEmpty()) {
				for (EmployeeSeparationDetails empSeperation : list) {
					VOEmployeeSeparationDetails model = HRMSEntityToModelMapper
							.convertToEmpSeparationDetailsModel(empSeperation);
					/*
					 * MasterDepartment masterDepartment = deptDAO
					 * .findById(empSeperation.getEmployee().getCandidate().
					 * getCandidateProfessionalDetail().getDepartment().getId()); if
					 * (masterDepartment != null) {
					 * model.getEmployee().getCandidate().getCandidateProfessionalDetail().
					 * setDepartment(masterDepartment); }
					 */
					listResponse.add(model);
				}
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
			}
			response.setPageNo(page);
			response.setPageSize(size);
			response.setTotalCount(totalCount);
			response.setListResponse(listResponse);
			response.setResponseCode(IHRMSConstants.successCode);
			response.setResponseMessage(IHRMSConstants.successMessage);
			return HRMSHelper.createJsonString(response);

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

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, value = "/empresignreport", consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
	@ResponseBody
	public void empAttendanceReport(@RequestBody VOEmployeeAttendanceReport employeeAttendanceReportParams,
			HttpServletResponse res) {
		try {

			/****************************
			 * Reprocess Attendance Data
			 *****************************/

			/*****************************************
			 * END
			 **************************************/

			List<EmployeeSeparationDetails> employeeSeparationDetailsList = new ArrayList<>();
			try {
				employeeSeparationDetailsList = employeeSeparationDAO.getResignedEmpListforExcelDownload(
						IHRMSConstants.isActive, employeeAttendanceReportParams.getOrgId());
			} catch (Exception e) {
				e.printStackTrace();
			}

			res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			res.setHeader("Content-Disposition", "attachment; filename=EmployeeAttendanceReport.xlsx");

			Workbook book = writeXLSXFile(employeeSeparationDetailsList);
			book.write(res.getOutputStream());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Workbook writeXLSXFile(List<EmployeeSeparationDetails> employeeSeparationDetailsList) {
		// TODO Auto-generated method stub
		String sheetName = "EmployeeResignedListReport";// name of sheet

		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(sheetName);

		Font detailsFont = wb.createFont();
		detailsFont.setBold(true);
		detailsFont.setFontHeightInPoints((short) 11);

		CellStyle detailsStyle = wb.createCellStyle();
		detailsStyle.setAlignment(HorizontalAlignment.LEFT);
		detailsStyle.setFont(detailsFont);

		Font font = wb.createFont();
		font.setBold(true);
		font.setColor(IndexedColors.WHITE.getIndex());
		font.setFontHeightInPoints((short) 11);
		CellStyle headerStyle = wb.createCellStyle();
		headerStyle.setBorderLeft(BorderStyle.THIN);
		headerStyle.setBorderRight(BorderStyle.THIN);
		headerStyle.setBorderTop(BorderStyle.THIN);
		headerStyle.setBorderBottom(BorderStyle.THIN);
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setFillForegroundColor(IndexedColors.MAROON.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		// style.setFillPattern(CellStyle.BIG_SPOTS);
		headerStyle.setFont(font);

		Font cellFont = wb.createFont();
		cellFont.setFontHeightInPoints((short) 10);
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		cellStyle.setFont(cellFont);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);

		CellStyle cellRedStyle = wb.createCellStyle();
		cellRedStyle.setAlignment(HorizontalAlignment.LEFT);
		cellRedStyle.setFont(cellFont);
		cellRedStyle.setBorderBottom(BorderStyle.THIN);
		cellRedStyle.setBorderTop(BorderStyle.THIN);
		cellRedStyle.setBorderLeft(BorderStyle.THIN);
		cellRedStyle.setBorderRight(BorderStyle.THIN);
		cellRedStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
		cellRedStyle.setFillPattern(FillPatternType.FINE_DOTS);

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 7));
		// sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 7));

		XSSFRow row = sheet.createRow(0);
		Cell cell1 = row.createCell(0);
		cell1.setCellValue("Vinsys IT Services India Limited.");
		cell1.setCellStyle(detailsStyle);

		row = sheet.createRow(1);
		cell1 = row.createCell(0);
		cell1.setCellValue("Resigned Employee List ");
		cell1.setCellStyle(detailsStyle);

		row = sheet.createRow(2);
		cell1 = row.createCell(0);
		cell1.setCellValue("Report generated date : "
				+ HRMSDateUtil.format(HRMSDateUtil.getToday(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		cell1.setCellStyle(detailsStyle);

		row = sheet.createRow(4);

		Cell cell21 = row.createCell(1);
		cell21.setCellValue(" Sr. No. ");
		cell21.setCellStyle(headerStyle);

		Cell cell22 = row.createCell(2);
		cell22.setCellValue(" Employee Name ");
		cell22.setCellStyle(headerStyle);

		Cell cell23 = row.createCell(3);
		cell23.setCellValue(" Employee ID ");
		cell23.setCellStyle(headerStyle);

		Cell cell24 = row.createCell(4);
		cell24.setCellValue("   Resignation Date     ");
		cell24.setCellStyle(headerStyle);

		Cell cell25 = row.createCell(5);
		cell25.setCellValue("   Relieving Date     ");
		cell25.setCellStyle(headerStyle);

		Cell cell26 = row.createCell(6);
		cell26.setCellValue(" Department ");
		cell26.setCellStyle(headerStyle);

		Cell cell27 = row.createCell(7);
		cell27.setCellValue(" Gender ");
		cell27.setCellStyle(headerStyle);

		Cell cell28 = row.createCell(8);
		cell28.setCellValue(" DOJ ");
		cell28.setCellStyle(headerStyle);

		Cell cell29 = row.createCell(9);
		cell29.setCellValue(" Designation ");
		cell29.setCellStyle(headerStyle);

		Cell cell30 = row.createCell(10);
		cell30.setCellValue(" DOB ");
		cell30.setCellStyle(headerStyle);

		Cell cell40 = row.createCell(11);
		cell40.setCellValue(" RM-Name ");
		cell40.setCellStyle(headerStyle);

		Cell cell41 = row.createCell(12);
		cell41.setCellValue(" RM-Email ID ");
		cell41.setCellStyle(headerStyle);

		Cell cell31 = row.createCell(13);
		cell31.setCellValue(" Father'S/Husband's Name ");
		cell31.setCellStyle(headerStyle);

		Cell cell32 = row.createCell(14);
		cell32.setCellValue(" Permanent Address  ");
		cell32.setCellStyle(headerStyle);

		Cell cell33 = row.createCell(15);
		cell33.setCellValue(" Present Address  ");
		cell33.setCellStyle(headerStyle);

		Cell cell34 = row.createCell(16);
		cell34.setCellValue(" Contact No.  ");
		cell34.setCellStyle(headerStyle);

		Cell cell35 = row.createCell(17);
		cell35.setCellValue(" Official e-mail ID  ");
		cell35.setCellStyle(headerStyle);

		Cell cell36 = row.createCell(18);
		cell36.setCellValue(" Email-id  ");
		cell36.setCellStyle(headerStyle);

		Cell cell37 = row.createCell(19);
		cell37.setCellValue(" PAN  ");
		cell37.setCellStyle(headerStyle);

		Cell cell38 = row.createCell(20);
		cell38.setCellValue(" Adhar No.  ");
		cell38.setCellStyle(headerStyle);

		Cell cell39 = row.createCell(21);
		cell39.setCellValue(" UAN  ");
		cell39.setCellStyle(headerStyle);

		int rowNo = 4;
		for (int r = 0; r < employeeSeparationDetailsList.size(); r++) {

			++rowNo;
			row = sheet.createRow(rowNo);
			int cellNo = 1;

			Cell cell = row.createCell(cellNo++);
			cell.setCellValue(r + 1);
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(employeeSeparationDetailsList.get(r).getEmployee().getCandidate().getFirstName() + " "
					+ employeeSeparationDetailsList.get(r).getEmployee().getCandidate().getLastName());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(employeeSeparationDetailsList.get(r).getEmployee().getId());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(
					HRMSDateUtil.format(employeeSeparationDetailsList.get(r).getResignationDate(), "dd-MM-yyyy"));
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(
					HRMSDateUtil.format(employeeSeparationDetailsList.get(r).getActualRelievingDate(), "dd-MM-yyyy"));
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(employeeSeparationDetailsList.get(r).getEmployee().getCandidate()
					.getCandidateProfessionalDetail().getDepartment().getDepartmentName());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(employeeSeparationDetailsList.get(r).getEmployee().getCandidate().getGender());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(HRMSDateUtil.format(employeeSeparationDetailsList.get(r).getEmployee().getCandidate()
					.getCandidateProfessionalDetail().getDateOfJoining(), "dd-MM-yyyy"));
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(employeeSeparationDetailsList.get(r).getEmployee().getCandidate()
					.getCandidateProfessionalDetail().getDesignation().getDesignationName());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(HRMSDateUtil.format(
					employeeSeparationDetailsList.get(r).getEmployee().getCandidate().getDateOfBirth(), "dd-MM-yyyy"));
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(employeeSeparationDetailsList.get(r).getEmployee().getEmployeeReportingManager()
					.getReporingManager().getCandidate().getFirstName() + " "
					+ employeeSeparationDetailsList.get(r).getEmployee().getEmployeeReportingManager()
							.getReporingManager().getCandidate().getLastName());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(employeeSeparationDetailsList.get(r).getEmployee().getEmployeeReportingManager()
					.getReporingManager().getOfficialEmailId());
			cell.setCellStyle(cellStyle);

			/*
			 * cell = row.createCell(cellNo++); cell.setCellValue(HRMSDateUtil
			 * .format(employeeSeparationDetailsList.get(r).getEmployee().getCandidate().
			 * getDateOfBirth(), "dd-MM-yyyy")); cell.setCellStyle(cellStyle);
			 */

			cell = row.createCell(cellNo++);
			cell.setCellValue(
					employeeSeparationDetailsList.get(r).getEmployee().getCandidate().getMiddleName() == null ? ""
							: employeeSeparationDetailsList.get(r).getEmployee().getCandidate().getMiddleName());
			cell.setCellStyle(cellStyle);

			// cell = row.createCell(cellNo++);
			// cell.setCellValue(employeeSeparationDetailsList.get(r).getEmployee().getCandidate().getCand);
			// cell.setCellStyle(cellStyle);

			Set<CandidateAddress> candAddressEntityList = employeeSeparationDetailsList.get(r).getEmployee()
					.getCandidate().getCandidateAddress();

			for (CandidateAddress cand : candAddressEntityList) {
				if (cand.getAddressType().equalsIgnoreCase(IHRMSConstants.PERMANANT_ADDRESS_TYPE)) {
					StringBuffer str = new StringBuffer();
					str.append(cand.getAddressLine1());
					if (!HRMSHelper.isNullOrEmpty(cand.getAddressLine2()))
						str.append(", " + cand.getAddressLine2());
					// MasterCity city=masterCityDAO.findById(cand.getCity().getId());
					if (cand.getCity() != null)
						str.append(", " + cand.getCity().getCityName());
					if (cand.getState() != null)
						str.append(", " + cand.getState().getStateName());
					if (cand.getCountry() != null)
						str.append(", " + cand.getCountry().getCountryName());
					cell = row.createCell(cellNo++);
					logger.info("perma str :: " + str + "perma str.tostring() ::" + str.toString() + "can ID "
							+ employeeSeparationDetailsList.get(r).getEmployee().getCandidate().getFirstName());
					if (str.toString().length() > 5)
						cell.setCellValue(str.toString());
					else
						cell.setCellValue("NA");
					cell.setCellStyle(cellStyle);
				} else {
					StringBuffer str = new StringBuffer();
					str.append(cand.getAddressLine1());
					if (!HRMSHelper.isNullOrEmpty(cand.getAddressLine2()))
						str.append(", " + cand.getAddressLine2());
					// MasterCity city=masterCityDAO.findById(cand.getCity().getId());
					if (cand.getCity() != null)
						str.append(", " + cand.getCity().getCityName());
					if (cand.getState() != null)
						str.append(", " + cand.getState().getStateName());
					if (cand.getCountry() != null)
						str.append(", " + cand.getCountry().getCountryName());
					cell = row.createCell(cellNo++);
					logger.info("present str :: " + str + "present str.tostring() ::" + str.toString() + " cand id "
							+ employeeSeparationDetailsList.get(r).getEmployee().getCandidate().getFirstName());
					if (str.toString().length() > 5)
						cell.setCellValue(str.toString());
					else
						cell.setCellValue("NA");
					cell.setCellStyle(cellStyle);
				}
			}

			StringBuffer stMobileno = new StringBuffer();
			stMobileno.append(employeeSeparationDetailsList.get(r).getEmployee().getCandidate().getMobileNumber());
			if (!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsList.get(r).getEmployee().getCandidate()
					.getCandidatePersonalDetail().getOfficialMobileNumber()))
				stMobileno.append(", " + employeeSeparationDetailsList.get(r).getEmployee().getCandidate()
						.getCandidatePersonalDetail().getOfficialMobileNumber());
			cell = row.createCell(cellNo++);
			cell.setCellValue(stMobileno.toString());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(employeeSeparationDetailsList.get(r).getEmployee().getOfficialEmailId());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(employeeSeparationDetailsList.get(r).getEmployee().getCandidate().getEmailId());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(employeeSeparationDetailsList.get(r).getEmployee().getCandidate()
					.getCandidateProfessionalDetail().getPanCard() == null ? ""
							: employeeSeparationDetailsList.get(r).getEmployee().getCandidate()
									.getCandidateProfessionalDetail().getPanCard());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(employeeSeparationDetailsList.get(r).getEmployee().getCandidate()
					.getCandidateProfessionalDetail().getAadhaarCard() == null ? ""
							: employeeSeparationDetailsList.get(r).getEmployee().getCandidate()
									.getCandidateProfessionalDetail().getAadhaarCard());
			cell.setCellStyle(cellStyle);

			cell = row.createCell(cellNo++);
			cell.setCellValue(employeeSeparationDetailsList.get(r).getEmployee().getCandidate()
					.getCandidateProfessionalDetail().getUan() == null ? ""
							: employeeSeparationDetailsList.get(r).getEmployee().getCandidate()
									.getCandidateProfessionalDetail().getUan());
			cell.setCellStyle(cellStyle);

			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
			sheet.autoSizeColumn(7);
			sheet.autoSizeColumn(8);
			sheet.autoSizeColumn(9);
			sheet.autoSizeColumn(10);
			sheet.autoSizeColumn(11);
			sheet.autoSizeColumn(12);
			sheet.autoSizeColumn(13);
			sheet.autoSizeColumn(14);
			sheet.autoSizeColumn(15);
			sheet.autoSizeColumn(16);
			sheet.autoSizeColumn(17);
			sheet.autoSizeColumn(18);
			sheet.autoSizeColumn(19);
			sheet.autoSizeColumn(20);
			sheet.autoSizeColumn(21);
		}
		return wb;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/findSubordinateAction/{orgId}/{managerId}", produces = "application/json")
	public String findEmployeeIdAndNameForManagerApproveResignation(@PathVariable("orgId") long orgId,
			@PathVariable("managerId") long managerId)
			throws JsonGenerationException, JsonMappingException, IOException, HRMSException {
		try {
			HRMSListResponseObject hrmsListResponseObject = null;
			List<Object> voEmployees = new ArrayList<Object>();
			if (!HRMSHelper.isLongZero(orgId)) {
				Employee mngremployeeEntity = employeeDAO.findById(managerId).get();
				MasterOrganizationEmailConfig masterOrganizationEmailConfigEntity = configDAO
						.findByorganizationAnddivision(
								mngremployeeEntity.getCandidate().getLoginEntity().getOrganization().getId(),
								mngremployeeEntity.getCandidate().getCandidateProfessionalDetail().getDivision()
										.getId());
				masterOrganizationEmailConfigEntity.getOrgLevelEmployee().getId();
				if ((managerId > 0)
						&& (managerId != masterOrganizationEmailConfigEntity.getOrgLevelEmployee().getId())) {
					logger.info("Finding Subordinate For Manager : ");
					List<EmployeeReportingManager> reportingManager = reportingManagerDAO
							.findSeparationEmployeeByreporingManager(managerId, IHRMSConstants.isActive,
									IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_PENDING);
					for (EmployeeReportingManager rptmgr : reportingManager) {
						if (rptmgr.getEmployee().getIsActive().equalsIgnoreCase(IHRMSConstants.isActive)) {

							VOEmployee employeeModel = HRMSEntityToModelMapper
									.convertToEmployeeModel(rptmgr.getEmployee());
							voEmployees.add(employeeModel);
						}
					}
				} else {
					logger.info("Finding All , Based On Organization  ... ");
					List<Employee> employees = employeeSeparationDAO.getAllSeparationEmpNameIdByOrg(orgId,
							IHRMSConstants.isActive);
					if (!HRMSHelper.isNullOrEmpty(employees)) {
						for (Employee employeeEntity : employees) {
							VOEmployee employeeModel = HRMSEntityToModelMapper.convertToEmployeeModel(employeeEntity);
							voEmployees.add(employeeModel);
						}

					} else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
					}
				}
				if (voEmployees.isEmpty()) {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}
			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
			hrmsListResponseObject = new HRMSListResponseObject();
			hrmsListResponseObject.setListResponse(voEmployees);
			hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
			hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
			return HRMSHelper.createJsonString(hrmsListResponseObject);
		} catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
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

	// @RequestMapping(method = RequestMethod.GET, value =
	// "/sendPendingChecklistReminderToApprover")
	public void sendPendingChecklistReminderToApprover() {
		try {

			// 1. get all checklist approvers(managers)
			// 2. find pending checklist of each approver create mail body and dump it to
			// email sender
			// 3. find all managers
			// 4. find all resigned employee with pending checklist of that manager
			// 5. create mail body and dump it to email sender
			String table = "";
			Map<String, String> placeHolderMap = new HashMap<String, String>();

			List<MapCatalogue> catalogueList = mapCatalogueDAO.findAllCataloguewithEmployeeByisActiveAndOrgId(
					IHRMSConstants.isActive, SecurityFilter.TL_CLAIMS.get().getOrgId());

			for (MapCatalogue mapCatalogue : catalogueList) {

				placeHolderMap = new HashMap<String, String>();
				table = "";
				List<MapEmployeeCatalogue> pendingChkEmpList = empCatalogueMapDAO.findBycatalogueAndStatus(mapCatalogue,
						IHRMSConstants.EMPLOYEE_SEPARATION_CHECKLIST_STATUS_PENDING);
				// pendingChkEmpList = null;
				if (!HRMSHelper.isNullOrEmpty(pendingChkEmpList)) {

					logger.info(
							"**** Creating Pending checklist reminder list for : " + mapCatalogue.getName() + " ****");
					for (MapEmployeeCatalogue mapEmployeeCatalogue : pendingChkEmpList) {
						/*
						 * mapEmployeeCatalogue.getStatus() //+ " :: " +
						 * mapEmployeeCatalogue.getResignedEmployee().getCandidate() + " :: " +
						 * mapEmployeeCatalogue.getResignedEmployee().getCandidate().getId() + " :: " +
						 * mapEmployeeCatalogue.getResignedEmployee().getCandidate().getFirstName() +
						 * " :: " +
						 * mapEmployeeCatalogue.getResignedEmployee().getCandidate().getLastName() +
						 * " :: " +
						 * mapEmployeeCatalogue.getCatalogue().getApprover().getOfficialEmailId());
						 */

						table += "<tr><td style=\"text-align: center;\">"
								+ mapEmployeeCatalogue.getResignedEmployee().getId() + "</td><td>"
								+ mapEmployeeCatalogue.getResignedEmployee().getCandidate().getFirstName() + " "
								+ mapEmployeeCatalogue.getResignedEmployee().getCandidate().getLastName()
								+ "</td></tr>";

						placeHolderMap.put("{approverName}", mapCatalogue.getApprover().getCandidate().getFirstName()
								+ " " + mapCatalogue.getApprover().getCandidate().getLastName());
						placeHolderMap.put("{pendingChecklistEmpList}", table);
						placeHolderMap.put("{websiteURL}", baseURL);

					}
					String mailContent = HRMSHelper.replaceString(placeHolderMap,
							IHRMSEmailTemplateConstants.Template_Pending_Checklist_Separation_Reminder);
					emailsender.toPersistEmail(mapCatalogue.getApprover().getOfficialEmailId(), null, mailContent,
							IHRMSConstants.PENDING_RESIGNED_EMP_CHECKLIST_SUBJECT,
							mapCatalogue.getApprover().getCandidate().getCandidateProfessionalDetail().getDivision()
									.getId(),
							mapCatalogue.getApprover().getCandidate().getLoginEntity().getOrganization().getId());

				} else {
					logger.info("**** No Pending checklist reminder for : " + mapCatalogue.getName() + " ****");
				}
			}

			Set<String> mngrIdSet = new HashSet<>();
			List<VOPendingChecklistRo> pendingChecklistRoList = new ArrayList<>();
			List<Object[]> pendingCheckListRO = reportringManagerDAO.getPendingChecklistEmployeeWithRo(
					IHRMSConstants.EMPLOYEE_SEPARATION_CHECKLIST_STATUS_PENDING,
					IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_COMPLETED);

			if (!HRMSHelper.isNullOrEmpty(pendingCheckListRO)) {

				for (Object[] resultset : pendingCheckListRO) {
					mngrIdSet.add(resultset[0].toString());
					VOPendingChecklistRo checklistRo = new VOPendingChecklistRo();
					checklistRo.setReportingManagerId(resultset[0].toString());
					checklistRo.setReportingManagerFirstName(resultset[1].toString());
					checklistRo.setReportingManagerLastName(resultset[2].toString());
					checklistRo.setReportingManagerMailId(resultset[3].toString());
					checklistRo.setEmpId(resultset[4].toString());
					checklistRo.setEmpFirstName(resultset[5].toString());
					checklistRo.setEmpLastName(resultset[6].toString());
					checklistRo.setEmpMailId(resultset[7].toString());
					checklistRo.setDivisionId(resultset[8].toString());
					checklistRo.setOrganizationId(resultset[9].toString());
					pendingChecklistRoList.add(checklistRo);
				}

				for (String mngrId : mngrIdSet) {
					table = "";
					placeHolderMap = new HashMap<String, String>();
					String mangerEmailId = "";
					Long divId = null;
					Long orgId = null;
					for (VOPendingChecklistRo pendingChkRo : pendingChecklistRoList) {
						if (mngrId.trim().equalsIgnoreCase(pendingChkRo.getReportingManagerId())) {
							// logger.info("**** Creating Pending checklist reminder list for : RO ****");
							table += "<tr><td style=\"text-align: center;\">" + pendingChkRo.getEmpId() + "</td><td>"
									+ pendingChkRo.getEmpFirstName() + " " + pendingChkRo.getEmpLastName()
									+ "</td></tr>";

							placeHolderMap.put("{approverName}", pendingChkRo.getReportingManagerFirstName() + " "
									+ pendingChkRo.getReportingManagerLastName());
							placeHolderMap.put("{pendingChecklistEmpList}", table);
							placeHolderMap.put("{websiteURL}", baseURL);

							mangerEmailId = pendingChkRo.getReportingManagerMailId();
							divId = Long.parseLong(pendingChkRo.getDivisionId());
							orgId = Long.parseLong(pendingChkRo.getOrganizationId());
						}
					}
					String mailContent = HRMSHelper.replaceString(placeHolderMap,
							IHRMSEmailTemplateConstants.Template_Pending_Checklist_Separation_Reminder);
					emailsender.toPersistEmail(mangerEmailId, null, mailContent,
							IHRMSConstants.PENDING_RESIGNED_EMP_CHECKLIST_SUBJECT, divId, orgId);

				}

			} else {
				logger.info("**** No Pending checklist reminder for : RO ****");
			}

			/*
			 * List<Object[]> result =
			 * employeeSeparationDAO.findRemainderforSeparationIfROnotTakenAnyAction(); if
			 * (result != null && !result.isEmpty()) { for (Object[] resultSet : result) {
			 * long orgId = Long.parseLong(String.valueOf(resultSet[0])); long divisionId =
			 * Long.parseLong(String.valueOf(resultSet[1]));
			 * findAndSendEmailToRoForNoAction(divisionId, orgId); } } else {
			 * logger.info("No Separation Reminder for RO"); }
			 */

		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

}
