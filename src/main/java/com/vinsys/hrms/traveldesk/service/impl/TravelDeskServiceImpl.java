package com.vinsys.hrms.traveldesk.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.vinsys.hrms.constants.EFileExtension;
import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.constants.ETicketType;
import com.vinsys.hrms.dao.IHRMSEmailTemplateDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.traveldesk.IHRMSMasterModeOfTravelDAO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.entity.EmailTemplate;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.traveldesk.MasterModeOfTravel;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.master.dao.ICurrencyMasterDAO;
import com.vinsys.hrms.master.dao.IMapTravelApproverDAO;
import com.vinsys.hrms.master.dao.IMasterBusTypeDAO;
import com.vinsys.hrms.master.dao.IMasterTravelApproverSlabDAO;
import com.vinsys.hrms.master.dao.IMasterTravellerTypeDAO;
import com.vinsys.hrms.master.dao.IMasterTripTypeDAO;
import com.vinsys.hrms.master.entity.CurrencyMaster;
import com.vinsys.hrms.master.entity.MasterBusType;
import com.vinsys.hrms.master.entity.MasterMapTravelApprover;
import com.vinsys.hrms.master.entity.MasterTravelApproverSlab;
import com.vinsys.hrms.master.entity.MasterTravellerType;
import com.vinsys.hrms.master.entity.MasterTripType;
import com.vinsys.hrms.master.vo.CurrencyMasterVO;
import com.vinsys.hrms.master.vo.TravelApproverResponseVO;
import com.vinsys.hrms.master.vo.TravelDeskApproverRequestVO;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.security.service.IAuthorizationService;
import com.vinsys.hrms.traveldesk.dao.IAccommodationRequestDAO;
import com.vinsys.hrms.traveldesk.dao.ICabRequestDAO;
import com.vinsys.hrms.traveldesk.dao.IMapApproverDAO;
import com.vinsys.hrms.traveldesk.dao.ITDExpenceSummaryDAO;
import com.vinsys.hrms.traveldesk.dao.ITicketRequestDAO;
import com.vinsys.hrms.traveldesk.dao.ITravelDocumentDAO;
import com.vinsys.hrms.traveldesk.dao.ITravelRequestDAO;
import com.vinsys.hrms.traveldesk.dao.ITravelRequestWfDAO;
import com.vinsys.hrms.traveldesk.dao.ITravellerDetailDAO;
import com.vinsys.hrms.traveldesk.entity.AccommodationRequestV2;
import com.vinsys.hrms.traveldesk.entity.CabRequestV2;
import com.vinsys.hrms.traveldesk.entity.MapTravelDeskApprover;
import com.vinsys.hrms.traveldesk.entity.TDExpenceSummaryReport;
import com.vinsys.hrms.traveldesk.entity.TicketRequestV2;
import com.vinsys.hrms.traveldesk.entity.TravelDocument;
import com.vinsys.hrms.traveldesk.entity.TravelRequestV2;
import com.vinsys.hrms.traveldesk.entity.TravelRequestWf;
import com.vinsys.hrms.traveldesk.entity.TravellerDetailsV2;
import com.vinsys.hrms.traveldesk.service.IBPMRequsetService;
import com.vinsys.hrms.traveldesk.service.ITravelDeskService;
import com.vinsys.hrms.traveldesk.vo.AccommodationTravelRequestVO;
import com.vinsys.hrms.traveldesk.vo.CabExpenseVO;
import com.vinsys.hrms.traveldesk.vo.CabTravelRequestVO;
import com.vinsys.hrms.traveldesk.vo.DownloadExpenseSummaryReqVO;
import com.vinsys.hrms.traveldesk.vo.ExpenseSummeryRequest;
import com.vinsys.hrms.traveldesk.vo.ExpenseSummeryResponseVO;
import com.vinsys.hrms.traveldesk.vo.ExpenseVO;
import com.vinsys.hrms.traveldesk.vo.GetTravelRequestVO;
import com.vinsys.hrms.traveldesk.vo.SaveTravelRequestVO;
import com.vinsys.hrms.traveldesk.vo.SubmitTravelRequestVO;
import com.vinsys.hrms.traveldesk.vo.TicketExpenseVO;
import com.vinsys.hrms.traveldesk.vo.TicketTravelRequestVO;
import com.vinsys.hrms.traveldesk.vo.TravelDeskRequestVO;
import com.vinsys.hrms.traveldesk.vo.TravelDetailsCancelVO;
import com.vinsys.hrms.traveldesk.vo.TravelDetailsDeleteVO;
import com.vinsys.hrms.traveldesk.vo.TravelDocumentResponseVO;
import com.vinsys.hrms.traveldesk.vo.TravelRequestApprovalVO;
import com.vinsys.hrms.traveldesk.vo.TravelRequestRejectionVO;
import com.vinsys.hrms.traveldesk.vo.TravelRequestStatusVO;
import com.vinsys.hrms.traveldesk.vo.TravelRequestVO;
import com.vinsys.hrms.traveldesk.vo.TravelResponseVO;
import com.vinsys.hrms.traveldesk.vo.TravellerDetailsVO;
import com.vinsys.hrms.traveldesk.vo.XLSGenerator;
import com.vinsys.hrms.util.EAirType;
import com.vinsys.hrms.util.EModeOfTravel;
import com.vinsys.hrms.util.ERole;
import com.vinsys.hrms.util.ETravelDocumentFlow;
import com.vinsys.hrms.util.ETravelRequestStatus;
import com.vinsys.hrms.util.EmailSender;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.ResponseCode;
import com.vinsys.hrms.util.TravelDeskAuthorityHelper;
import com.vinsys.hrms.util.TravelDeskTransformUtil;

/**
 * @author Onkar A
 *
 * 
 */
@Service
public class TravelDeskServiceImpl implements ITravelDeskService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IAuthorizationService authorizationServiceImpl;
	@Value("${app_version}")
	private String applicationVersion;
	@Autowired
	ITravelRequestDAO travelRequestDAO;
	@Autowired
	IBPMRequsetService bpmRequsetService;
	@Autowired
	TravelDeskTransformUtil travelDeskTransformUtil;
	@Autowired
	ICabRequestDAO cabRequestDAO;
	@Autowired
	IAccommodationRequestDAO accommodationRequestDAO;
	@Autowired
	TravelDeskAuthorityHelper travelDeskAuthorityHelper;
	@Autowired
	ITravellerDetailDAO travellerDetailDAO;
	@Autowired
	ITicketRequestDAO ticketRequestDAO;
	@Autowired
	IHRMSMasterModeOfTravelDAO masterModeOfTravelDAO;
	@Autowired
	IMapApproverDAO mapApproverDAO;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IMasterTravelApproverSlabDAO slabDAO;
	@Autowired
	EmailSender emailsender;
	@Autowired
	IMapTravelApproverDAO travelApproverDAO;
	@Autowired
	IHRMSEmailTemplateDAO emailTemplateDAO;

	@Autowired
	ITravelRequestWfDAO travelRequestWfDAO;

	@Autowired
	IMapTravelApproverDAO mapTravelApproverDAO;
	@Autowired
	ITravelDocumentDAO travelDocumentDAO;
	@Autowired
	IMasterTravellerTypeDAO travellerTypeDAO;
	@Autowired
	IMasterBusTypeDAO busTypeDAO;
	@Autowired
	IMasterTripTypeDAO tripTypeDAO;
	@Autowired
	ITDExpenceSummaryDAO tdExpenceSummaryDAO;
	@Autowired
	ICurrencyMasterDAO currencyMasterDAO;

	@Value("${rootDirectory}")
	private String rootDirectory;
	@Value("${base.url}")
	private String baseURL;

	@Value("${MaxFileUploadSize}")
	private Long maxFileSizeUpload;
	@Value("${td_approver_for_mgmt_employee}")
	private Long tdApproverIdForMgmtEmployee;

	private static final String TRAVELDESK = "TravelDesk";

	@Transactional(rollbackFor = Exception.class)
	@Override
	public HRMSBaseResponse<TravelResponseVO> addTravelRequest(@RequestBody TravelRequestVO request)
			throws HRMSException, ParseException, ClassNotFoundException, SQLException, URISyntaxException {
		log.info("Inside add Travel Request method");
		HRMSBaseResponse<TravelResponseVO> response = new HRMSBaseResponse<>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggnedEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		if (authorizationServiceImpl.isAuthorizedFunctionName("addTravelRequestMethod", role)) {
			if (!HRMSHelper.isNullOrEmpty(request)) {

				// input validation
				travelDeskAuthorityHelper.addTravelRequestInputValidation(request);
				// check BPM number exist or not
				HRMSBaseResponse<?> bpmResponse = bpmRequsetService.getBPMDetails(request.getBpmNumber());
				if (HRMSHelper.isNullOrEmpty(bpmResponse.getResponseBody())) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + " BPM Number");
				}

				TravelRequestV2 travelRequestEntity = new TravelRequestV2();
				if (request.isBookAccommodation() || request.isBookCab() || request.isBookTicket()) {
					travelRequestEntity = addTravelRequestMethod(request, travelRequestEntity);

					addWFStatus(travelRequestEntity, loggnedEmpId, ETravelRequestStatus.INCOMPLETE.name(),
							ERole.EMPLOYEE.name());
				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1552));
				}

				TravelResponseVO travelResponseVO = new TravelResponseVO();
				travelResponseVO.setBookAccommodation(travelRequestEntity.getBookAccommodation());
				travelResponseVO.setBookCab(travelRequestEntity.getBookCab());
				travelResponseVO.setBookTicket(travelRequestEntity.getBookTicket());
				travelResponseVO.setBpmNumber(travelRequestEntity.getBpmNumber());
				travelResponseVO.setId(travelRequestEntity.getId());
				travelResponseVO.setInvoiceNumber(travelRequestEntity.getInvoiceNumber());
				travelResponseVO.setBdName(travelRequestEntity.getBdName());
				travelResponseVO.setCurrency(convertToCurrencyMasterVO(travelRequestEntity.getCurrency()));
				//travelResponseVO.setPreference(travelRequestEntity.getTravelComment());

				response.setResponseCode(1200);
				response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1575));
				response.setApplicationVersion(applicationVersion);
				response.setResponseBody(travelResponseVO);
				log.info("Exit add Travel Request method");
				return response;

			} else
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
	}

	private CurrencyMasterVO convertToCurrencyMasterVO(CurrencyMaster currency) {
		CurrencyMasterVO currencyMasterVO = new CurrencyMasterVO();
		if (!HRMSHelper.isNullOrEmpty(currency)) {
			currencyMasterVO.setEntityId(currency.getEntityId());
			currencyMasterVO.setCountryName(currency.getCountryName());
			currencyMasterVO.setCurrency(currency.getCurrency());
			currencyMasterVO.setSymbol(currency.getSymbol());
		}
		return currencyMasterVO;
	}

	private void addWFStatus(TravelRequestV2 travelRequestEntity, Long loggnedEmpId, String status,
			String pendingWith) {
		log.info("Adding WF status for Travel Request ID:", travelRequestEntity);
		TravelRequestWf travelRequestWf = travelRequestWfDAO.findTravelRequestIdByIsActive(travelRequestEntity.getId(),
				IHRMSConstants.isActive);
		Employee employee=employeeDAO.findByEmpIdAndOrgId(loggnedEmpId, SecurityFilter.TL_CLAIMS.get().getOrgId());
		if (!HRMSHelper.isNullOrEmpty(travelRequestWf)) {
			travelRequestWf.setStatus(status);
			travelRequestWf.setPendingWith(pendingWith);
			travelRequestWf.setUpdatedDate(new Date());
			travelRequestWf.setUpdatedBy(employee.getId().toString());
		} else {
			travelRequestWf = new TravelRequestWf();
			travelRequestWf.setTravelRequest(travelRequestEntity);
			travelRequestWf.setStatus(status);
			travelRequestWf.setPendingWith(pendingWith);
			travelRequestWf.setCreatedBy(employee.getId().toString());
			travelRequestWf.setCreatedDate(new Date());
			travelRequestWf.setIsActive(IHRMSConstants.isActive);
			travelRequestWf.setOrgId(employee.getOrgId());
		}
		travelRequestWfDAO.save(travelRequestWf);
		log.info("WF status added successfully for Travel Request ID:", travelRequestEntity);
	}

	private TravelRequestV2 addTravelRequestMethod(TravelRequestVO request, TravelRequestV2 travelRequestEntity) {
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee emp = new Employee();
		emp = employeeDAO.findById(empId).get();
		travelRequestEntity.setCreatedBy(empId.toString());
		travelRequestEntity.setCreatedDate(new Date());
		travelRequestEntity.setIsActive(IHRMSConstants.isActive);
		travelRequestEntity.setBookAccommodation(request.isBookAccommodation());
		travelRequestEntity.setBookCab(request.isBookCab());
		travelRequestEntity.setBookTicket(request.isBookTicket());
		travelRequestEntity.setBpmNumber(request.getBpmNumber());
		travelRequestEntity.setTravelReason(request.getTravelReason());
//		travelRequestEntity.setStatus(ETravelRequestStatus.INCOMPLETE.name());
		travelRequestEntity.setRequesterId(empId);
		Long divId = emp.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
		travelRequestEntity.setDivisionId(divId);
		String name = emp.getCandidate().getFirstName() + " " + emp.getCandidate().getLastName();
		Long deptId = emp.getCandidate().getCandidateProfessionalDetail().getDepartment().getId();
		travelRequestEntity.setDepartmentId(deptId);
		Long branchId = emp.getCandidate().getCandidateProfessionalDetail().getBranch().getId();
		travelRequestEntity.setBranchId(branchId);
		travelRequestEntity.setName(name);
		travelRequestEntity.setInvoiceNumber(request.getInvoiceNumber());
		travelRequestEntity.setBdName(request.getBdName());
		travelRequestEntity.setCurrency(
				currencyMasterDAO.findByIsActiveAndId(ERecordStatus.Y.name(), request.getCurrency().getEntityId()));
		travelRequestEntity.setOrgId(emp.getOrgId());
		//travelRequestEntity.setTravelComment(request.getPreference());
		travelRequestEntity = travelRequestDAO.save(travelRequestEntity);
		return travelRequestEntity;
	}

	@Override
	public HRMSBaseResponse<List<TravelResponseVO>> getAllRequests(String roleName, Long departmentId,String travelerName,
			Pageable pageable) throws HRMSException {
		log.info("Inside getAllRequests Method");
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		HRMSBaseResponse<List<TravelResponseVO>> response = new HRMSBaseResponse<List<TravelResponseVO>>();
		long totalRecord = 0;
		List<TravelResponseVO> travelRequestVO = new ArrayList<>();
		Employee employee = employeeDAO.findActiveEmployeeWithCandidateByEmpIdAndOrgId(empId, IHRMSConstants.isActive,SecurityFilter.TL_CLAIMS.get().getOrgId());

		List<String> travelWFStatus = null;
		if (roleName.equalsIgnoreCase(ERole.EMPLOYEE.name())) {
			if (authorizationServiceImpl.isAuthorizedFunctionName("getAllRequestForEmp", role)) {
				travelWFStatus = new ArrayList<String>();
				travelWFStatus.add(ETravelRequestStatus.CANCELLED.name());
				totalRecord = travelRequestDAO.countByIdAndIsActive(empId, IHRMSConstants.isActive,
						travelWFStatus.toArray());
				List<TravelRequestV2> travelRequestV2 = travelRequestDAO.findByRequesterIdAndIsActive(empId,
						IHRMSConstants.isActive, travelWFStatus.toArray(), pageable);
				if (!HRMSHelper.isNullOrEmpty(travelRequestV2)) {
					travelRequestVO = travelDeskTransformUtil.convertToTravelRequestVO(travelRequestV2);
				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
				}
			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}

		} else if (roleName.equalsIgnoreCase(ERole.TRAVEL_DESK_APPROVER.name())) {
			if (authorizationServiceImpl.isAuthorizedFunctionName("getAllTravelRequestForApprover", role)) {
				travelWFStatus = new ArrayList<String>();
				travelWFStatus.add(ETravelRequestStatus.INCOMPLETE.name());
				travelWFStatus.add(ETravelRequestStatus.INPROCESS.name());
				List<TravelRequestV2> travelRequestV2 = null;
				// for Travel desk all mapped request displaying
				List<Long> depId = mapTravelApproverDAO.findDepartmentIdIdAndIsActive(empId, IHRMSConstants.isActive);
				if (tdApproverIdForMgmtEmployee.equals(empId)) {

					totalRecord = travelRequestDAO.countDeptIdAndIsActiveAndApproverId(depId.toArray(),
							IHRMSConstants.isActive, travelWFStatus.toArray(), empId);

					travelRequestV2 = travelRequestDAO.findDeptIdAndIsActiveAndApproverId(depId.toArray(),
							IHRMSConstants.isActive, travelWFStatus.toArray(), empId, pageable);

				} else {
					totalRecord = travelRequestDAO.countDivisionIdAndDeptIdAndIsActiveAndApproverId(
							employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
							depId.toArray(), IHRMSConstants.isActive, travelWFStatus.toArray(), empId);

					travelRequestV2 = travelRequestDAO.findDivisionIdAndDeptIdAndIsActiveAndApproverId(
							employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
							depId.toArray(), IHRMSConstants.isActive, travelWFStatus.toArray(), empId, pageable);
				}

				if (!HRMSHelper.isNullOrEmpty(travelRequestV2)) {
					travelRequestVO = travelDeskTransformUtil.convertToTravelRequestVO(travelRequestV2);
				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
				}
			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}

		} else if (roleName.equalsIgnoreCase(ERole.TRAVEL_DESK.name())) {
			if (authorizationServiceImpl.isAuthorizedFunctionName("getAllTravelRequestTD", role)) {

				travelWFStatus = new ArrayList<String>();
				travelWFStatus.add(ETravelRequestStatus.INCOMPLETE.name());

				List<MapTravelDeskApprover> mapApprover = mapApproverDAO.findApproverIdAndIsActive(empId,
						IHRMSConstants.isActive);
				List<Long> divId = mapApprover.stream().map(MapTravelDeskApprover::getDivisionId)
						.collect(Collectors.toList());

				List<Long> branchIdList = mapApprover.stream().map(MapTravelDeskApprover::getBranchId)
						.collect(Collectors.toList());

				if (!HRMSHelper.isNullOrEmpty(departmentId)) {
					totalRecord = travelRequestDAO.countDivisionIdIdAndIsActiveAndDepartmentId(divId.toArray(),
							IHRMSConstants.isActive, travelWFStatus.toArray(), departmentId);
				} else {

					totalRecord = travelRequestDAO.countDivisionIdIdAndIsActive(divId.toArray(),
							IHRMSConstants.isActive, travelWFStatus.toArray());
				}
				List<TravelRequestV2> travelRequestV2 = null;
				List<Long> branchIds = new ArrayList<>();

				for (Long branchId : branchIdList) {
					if (!HRMSHelper.isNullOrEmpty(branchId)) {
						branchIds.add(branchId);
					}
				}
				if (HRMSHelper.isNullOrEmpty(branchIds)) {
					if (!HRMSHelper.isNullOrEmpty(departmentId)) {
						travelRequestV2 = travelRequestDAO.findDivisionIdAndIsActiveAndDepartmentId(divId.toArray(),
								IHRMSConstants.isActive, travelWFStatus.toArray(), departmentId, pageable);
//					} else if (HRMSHelper.isNullOrEmpty(departmentId) && !HRMSHelper.isNullOrEmpty(travelerName)) {
//						travelRequestV2 = travelRequestDAO.findDivisionIdAndIsActiveAndTravelerName(divId.toArray(),
//								IHRMSConstants.isActive, travelerName, pageable);
					} else {
						travelRequestV2 = travelRequestDAO.findDivisionIdAndIsActive(divId.toArray(),
								IHRMSConstants.isActive, travelWFStatus.toArray(), pageable);
					}
				} else {
					if (!HRMSHelper.isNullOrEmpty(departmentId)) {
						travelRequestV2 = travelRequestDAO.findDivisionIdAndIsActiveAndDepartmentIdAndBranchId(
								divId.toArray(), IHRMSConstants.isActive, travelWFStatus.toArray(), departmentId,
								branchIds.toArray(), pageable);
					} else {
						travelRequestV2 = travelRequestDAO.findDivisionIdAndIsActiveAndBranchId(divId.toArray(),
								IHRMSConstants.isActive, travelWFStatus.toArray(), branchIds.toArray(), pageable);
					}
				}
				if (!HRMSHelper.isNullOrEmpty(travelRequestV2)) {
					travelRequestVO = travelDeskTransformUtil.convertToTravelRequestVO(travelRequestV2);
				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
				}
			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}

		} else if (roleName.equalsIgnoreCase(ERole.ACCOUNTANT.name())) {

			if (authorizationServiceImpl.isAuthorizedFunctionName("getAllTravelRequestAccount", role)) {

				travelWFStatus = new ArrayList<String>();
				travelWFStatus.add(ETravelRequestStatus.SETTLED.name());

				Long divId = employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId();

				if (!HRMSHelper.isNullOrEmpty(departmentId)) {
					totalRecord = travelRequestDAO.countDivisionIdIdAndIsActive(divId, IHRMSConstants.isActive,
							travelWFStatus.toArray());
				} else {

					totalRecord = travelRequestDAO.countDivisionIdIdAndIsActive(divId, IHRMSConstants.isActive,
							travelWFStatus.toArray());
				}
				List<TravelRequestV2> travelRequestV2 = null;
				if (!HRMSHelper.isNullOrEmpty(departmentId)) {
					travelRequestV2 = travelRequestDAO.findDivisionIdAndIsActive(divId, IHRMSConstants.isActive,
							travelWFStatus.toArray(), pageable);
				} else {
					travelRequestV2 = travelRequestDAO.findDivisionIdAndIsActive(divId, IHRMSConstants.isActive,
							travelWFStatus.toArray(), pageable);
				}
				if (!HRMSHelper.isNullOrEmpty(travelRequestV2)) {
					travelRequestVO = travelDeskTransformUtil.convertToTravelRequestVO(travelRequestV2);
				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
				}
			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}

		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
		log.info("Exit getAllRequests Method");
		response.setResponseBody(travelRequestVO);
		response.setTotalRecord(totalRecord);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);
		return response;

	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public HRMSBaseResponse<GetTravelRequestVO> saveTravelRequest(@RequestBody SaveTravelRequestVO request)
			throws HRMSException, ParseException {
		log.info("Inside save Travel Request method");
		HRMSBaseResponse<GetTravelRequestVO> response = new HRMSBaseResponse<>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		if (!HRMSHelper.isNullOrEmpty(request) && !HRMSHelper.isNullOrEmpty(request.getId())) {
			TravelRequestV2 travel = travelRequestDAO.findByIdAndIsActive(request.getId(), IHRMSConstants.isActive);
			if (travel.getRequesterId().equals(empId)) {
				if (travel.getRequestWF().getStatus().equals(ETravelRequestStatus.INCOMPLETE.name())) {
					if (!HRMSHelper.isNullOrEmpty(request.getCabDetails()) && travel.getBookCab()) {
						if (!request.getCabDetails().getTravelRequestId().equals(request.getId())) {
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1562));
						}
						if (authorizationServiceImpl.isAuthorizedFunctionName("saveCabRequestMethod", role)) {
							// input validation
							travelDeskAuthorityHelper.saveCabInputValidation(request.getCabDetails());
							saveCabRequestMethod(request.getCabDetails(), empId);
						} else {
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
						}
					}
					if (!HRMSHelper.isNullOrEmpty(request.getAccommodationDetails()) && travel.getBookAccommodation()) {
						if (!request.getAccommodationDetails().getTravelRequestId().equals(request.getId())) {
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1562));
						}
						// input validation
						travelDeskAuthorityHelper.saveAccommodationInputValidation(request.getAccommodationDetails());
						if (authorizationServiceImpl.isAuthorizedFunctionName("saveAccommodationRequestMethod", role)) {
							saveAccommodationRequestMethod(request.getAccommodationDetails(), empId);
						} else {
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
						}
					}
					if (!HRMSHelper.isNullOrEmpty(request.getTicketDetails()) && travel.getBookTicket()) {
						if (!HRMSHelper.isNullOrEmpty(request.getTicketDetails().getAirDetails()) && !request
								.getTicketDetails().getAirDetails().getTravelRequestId().equals(request.getId())) {
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1562));
						}
						if (!HRMSHelper.isNullOrEmpty(request.getTicketDetails().getBusDetails()) && !request
								.getTicketDetails().getBusDetails().getTravelRequestId().equals(request.getId())) {
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1562));
						}
						if (!HRMSHelper.isNullOrEmpty(request.getTicketDetails().getTrainDetails()) && !request
								.getTicketDetails().getTrainDetails().getTravelRequestId().equals(request.getId())) {
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1562));
						}
						if (authorizationServiceImpl.isAuthorizedFunctionName("saveTicketRequestMethod", role)) {
							// input validation
							travelDeskAuthorityHelper.saveTicketInputValidation(request.getTicketDetails());
							saveTicketRequestMethod(request.getTicketDetails(), empId);
						} else {
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
						}
					}
					// get request deatls
//					HRMSBaseResponse<GetTravelRequestVO> travelRequestVOResponse = getRequest(travel.getId());
//					response.setResponseBody(travelRequestVOResponse.getResponseBody());
					response.setResponseCode(1200);
					response.setResponseMessage(IHRMSConstants.successMessage);
					response.setApplicationVersion(applicationVersion);
					log.info("Exit save Travel Request method");
					return response;
				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1554));
				}

			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}
		} else
			throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);

	}

	private Float getTotalApproximateCost(TravelRequestV2 travel) {
		log.info(" inside getTotalApproximateCost method");

		Float totalApproximateCost = 0F;
		CabRequestV2 cabRequest = cabRequestDAO.findByTravelRequestIdAndIsActive(travel.getId(),
				ERecordStatus.Y.name());
		AccommodationRequestV2 accommodationRequest = accommodationRequestDAO
				.findByTravelRequestIdAndIsActive(travel.getId(), ERecordStatus.Y.name());
		List<TicketRequestV2> ticketRequest = ticketRequestDAO.findIdByIsActive(travel.getId(), ERecordStatus.Y.name());
		// get ticket total cost
		if (!HRMSHelper.isNullOrEmpty(ticketRequest)) {
			for (TicketRequestV2 ticketBook : ticketRequest) {
				if (!HRMSHelper.isNullOrEmpty(ticketBook.getApproximateCost())) {
					totalApproximateCost = totalApproximateCost + ticketBook.getApproximateCost();
				}
			}
			log.info("Ticket Total ApproximateCost");
		}
		// get acc cost
		if (!HRMSHelper.isNullOrEmpty(accommodationRequest)) {
			if (!HRMSHelper.isNullOrEmpty(accommodationRequest.getApproximateCost())) {
				totalApproximateCost = totalApproximateCost + accommodationRequest.getApproximateCost();
			}
			log.info("accommodationRequest Total ApproximateCost");
		}
		// get cab cost
		if (!HRMSHelper.isNullOrEmpty(cabRequest)) {
			if (!HRMSHelper.isNullOrEmpty(cabRequest.getApproximateCost())) {
				totalApproximateCost = totalApproximateCost + cabRequest.getApproximateCost();
			}
			log.info("cab Total ApproximateCost");
		}
		log.info("Total Approximate cost::" + totalApproximateCost);
		log.info(" inside getTotalApproximateCost method");
		return totalApproximateCost;
	}

	private Float getFinalCost(TravelRequestV2 travel) {
		log.info(" inside getFinalCost method");

		Float totalFinalCost = 0F;
		CabRequestV2 cabRequest = cabRequestDAO.findByTravelRequestIdAndIsActive(travel.getId(),
				ERecordStatus.Y.name());
		AccommodationRequestV2 accommodationRequest = accommodationRequestDAO
				.findByTravelRequestIdAndIsActive(travel.getId(), ERecordStatus.Y.name());
		List<TicketRequestV2> ticketRequest = ticketRequestDAO.findIdByIsActive(travel.getId(), ERecordStatus.Y.name());
		// get ticket total cost
		if (!HRMSHelper.isNullOrEmpty(ticketRequest)) {
			for (TicketRequestV2 ticketBook : ticketRequest) {
				if (!HRMSHelper.isNullOrEmpty(ticketBook.getFinalCost())) {
					totalFinalCost = totalFinalCost + ticketBook.getFinalCost();
				}
			}
			log.info("Ticket Total final cost");
		}
		// get acc cost
		if (!HRMSHelper.isNullOrEmpty(accommodationRequest)) {
			if (!HRMSHelper.isNullOrEmpty(accommodationRequest.getFinalCost())) {
				totalFinalCost = totalFinalCost + accommodationRequest.getFinalCost();
			}
			log.info("accommodationRequest Total Final cost");
		}
		// get cab cost
		if (!HRMSHelper.isNullOrEmpty(cabRequest)) {
			if (!HRMSHelper.isNullOrEmpty(cabRequest.getFinalCost())) {
				totalFinalCost = totalFinalCost + cabRequest.getFinalCost();
			}
			log.info("cab Total Final cost");
		}
		log.info("Total Final cost::" + totalFinalCost);
		log.info(" inside getFinalCost method");
		return totalFinalCost;
	}

	private AccommodationRequestV2 saveAccommodationRequestMethod(AccommodationTravelRequestVO request, Long empId)
			throws ParseException, HRMSException {

		AccommodationRequestV2 accoEntity = null;
		if (!HRMSHelper.isNullOrEmpty(request.getId())) {
			accoEntity = accommodationRequestDAO.findByRequestId(request.getTravelRequestId());
			if (!HRMSHelper.isNullOrEmpty(accoEntity)) {

				accoEntity.setUpdatedBy(empId.toString());
				accoEntity.setUpdatedDate(new Date());
			} else {
				accoEntity = new AccommodationRequestV2();
			}

		} else {
			accoEntity = new AccommodationRequestV2();

		}
		Employee employee=employeeDAO.findByEmpIdAndOrgId(empId, SecurityFilter.TL_CLAIMS.get().getOrgId());
		accoEntity.setCreatedBy(empId.toString());
		accoEntity.setCreatedDate(new Date());
		accoEntity.setIsActive(IHRMSConstants.isActive);
		accoEntity.setChargeableToClient(request.getChargableClient());
		accoEntity.setLocation(request.getLocation());
		accoEntity.setFromDate(HRMSDateUtil.parse(request.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		accoEntity.setToDate(HRMSDateUtil.parse(request.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		accoEntity.setNumberOfTravellers(request.getNoOfTravellers());
		accoEntity.setCheckInTime(request.getCheckInTime());
		accoEntity.setCheckOutTime(request.getCheckOutTime());
		accoEntity.setNumberOfRooms(request.getNoOfRooms());
		accoEntity.setTravelRequestId(request.getTravelRequestId());
		accoEntity.setOrgId(employee.getOrgId());
		AccommodationRequestV2 accommodation = accommodationRequestDAO.save(accoEntity);

		if (!HRMSHelper.isNullOrEmpty(request.getTravellerDetails())) {
			if (request.getNoOfTravellers() != (request.getTravellerDetails().size())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1573));
			}
			addTraverller(request.getTravellerDetails(), null, null, accommodation.getId());
		} else {
			if (request.getNoOfTravellers() > 0) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1590));
			}
		}
		return null;
	}

	@Override
	public HRMSBaseResponse<GetTravelRequestVO> getRequest(Long reqId) throws HRMSException {
		log.info("Inside get Requests By Id Method");
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		HRMSBaseResponse<GetTravelRequestVO> response = new HRMSBaseResponse<GetTravelRequestVO>();

		GetTravelRequestVO travelRequestVO = new GetTravelRequestVO();
		TravelRequestV2 empRequest = travelRequestDAO.findByIdAndIsActive(reqId, IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(empRequest)) {
			// get Travel desk division
			List<MapTravelDeskApprover> mapApprover = mapApproverDAO.findApproverIdAndIsActive(loggedInEmpId,
					IHRMSConstants.isActive);
			List<Long> divId = mapApprover.stream().map(MapTravelDeskApprover::getDivisionId)
					.collect(Collectors.toList());
			List<Long> branchIdList = mapApprover.stream().map(MapTravelDeskApprover::getBranchId)
					.collect(Collectors.toList());

			TravelRequestV2 travelRequestV2 = travelRequestDAO.findByIdAndIsActive(reqId, IHRMSConstants.isActive);
			// requested EMP travelRequestV2.getRequesterId()
			Employee requesterEmployee = employeeDAO.findActiveEmployeeById(travelRequestV2.getRequesterId(),
					IHRMSConstants.isActive);
			Employee loggedEmployee = employeeDAO.findActiveEmployeeById(loggedInEmpId, IHRMSConstants.isActive);

			List<Long> depId = mapTravelApproverDAO.findDepartmentIdIdAndIsActive(loggedInEmpId,
					IHRMSConstants.isActive);

			// EMP
			if (loggedInEmpId.equals(travelRequestV2.getRequesterId())) {
				CabRequestV2 cabRequest = cabRequestDAO.findByTravelRequestIdAndIsActive(travelRequestV2.getId(),
						ERecordStatus.Y.name());
				AccommodationRequestV2 accommodationRequest = accommodationRequestDAO
						.findByTravelRequestIdAndIsActive(travelRequestV2.getId(), ERecordStatus.Y.name());
				List<TicketRequestV2> ticketRequest = ticketRequestDAO
						.findByTravelRequestIdAndIsActive(travelRequestV2.getId(), ERecordStatus.Y.name());
				travelRequestV2.setCabRequestV2(cabRequest);
				travelRequestV2.setAccommodationRequest(accommodationRequest);
				travelRequestV2.setTicketBooking(ticketRequest);
				travelRequestVO = travelDeskTransformUtil.convertToTravelRequestVO(travelRequestV2);
			} else if (!HRMSHelper.isNullOrEmpty(mapApprover)) {// check TD division Id and

				Long reqBranchId = travelRequestV2.getBranchId();
				Long reqDivisionId = travelRequestV2.getDivisionId();
				List<Long> branchIds = new ArrayList<>();

				for (Long branchId : branchIdList) {
					if (!HRMSHelper.isNullOrEmpty(branchId)) {
						branchIds.add(branchId);
					}
				}
				if ((!HRMSHelper.isNullOrEmpty(reqBranchId) && !HRMSHelper.isNullOrEmpty(branchIds)
						&& mapApprover.stream().map(MapTravelDeskApprover::getBranchId)
								.anyMatch(branchId -> reqBranchId.equals(branchId)))
						|| mapApprover.stream().map(MapTravelDeskApprover::getDivisionId)
								.anyMatch(divisionId -> reqDivisionId.equals(divisionId))) {
					if (!travelRequestV2.getRequestWF().getStatus()
							.equalsIgnoreCase(ETravelRequestStatus.INCOMPLETE.name())) {
						boolean anyMatchResult = false;
						for (int i = 0; i < mapApprover.size(); i++) {
							if (mapApprover.get(i).getDivisionId() == requesterEmployee.getCandidate()
									.getCandidateProfessionalDetail().getDivision().getId()) {
								anyMatchResult = true;
							}
						}

						if (anyMatchResult) {

							CabRequestV2 cabRequest = cabRequestDAO
									.findByTravelRequestIdAndIsActive(travelRequestV2.getId(), ERecordStatus.Y.name());
							AccommodationRequestV2 accommodationRequest = accommodationRequestDAO
									.findByTravelRequestIdAndIsActive(travelRequestV2.getId(), ERecordStatus.Y.name());
							List<TicketRequestV2> ticketRequest = ticketRequestDAO
									.findByTravelRequestIdAndIsActive(travelRequestV2.getId(), ERecordStatus.Y.name());
							travelRequestV2.setCabRequestV2(cabRequest);
							travelRequestV2.setAccommodationRequest(accommodationRequest);
							travelRequestV2.setTicketBooking(ticketRequest);
							travelRequestVO = travelDeskTransformUtil.convertToTravelRequestVO(travelRequestV2);
						} else {
							throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1) + " ");
						}
					} else {
						throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1201) + " ");
					}
				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521) + " ");
				}
			} else if (!HRMSHelper.isNullOrEmpty(depId)) {

				if (!travelRequestV2.getRequestWF().getStatus().equalsIgnoreCase(ETravelRequestStatus.INCOMPLETE.name())
						|| !travelRequestV2.getRequestWF().getStatus()
								.equalsIgnoreCase(ETravelRequestStatus.INPROCESS.name())) {
					boolean depMatch = depId.contains(
							requesterEmployee.getCandidate().getCandidateProfessionalDetail().getDepartment().getId());
					if (depMatch && travelRequestV2.getApproverId().equals(loggedInEmpId)) {
						CabRequestV2 cabRequest = cabRequestDAO
								.findByTravelRequestIdAndIsActive(travelRequestV2.getId(), ERecordStatus.Y.name());
						AccommodationRequestV2 accommodationRequest = accommodationRequestDAO
								.findByTravelRequestIdAndIsActive(travelRequestV2.getId(), ERecordStatus.Y.name());
						List<TicketRequestV2> ticketRequest = ticketRequestDAO
								.findByTravelRequestIdAndIsActive(travelRequestV2.getId(), ERecordStatus.Y.name());
						travelRequestV2.setCabRequestV2(cabRequest);
						travelRequestV2.setAccommodationRequest(accommodationRequest);
						travelRequestV2.setTicketBooking(ticketRequest);
						travelRequestVO = travelDeskTransformUtil.convertToTravelRequestVO(travelRequestV2);
					} else {
						throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1201) + " ");
					}
				} else {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1201) + " ");
				}

			} else if (HRMSHelper.isRolePresent(role, ERole.ACCOUNTANT.name())) {
				CabRequestV2 cabRequest = cabRequestDAO.findByTravelRequestIdAndIsActive(travelRequestV2.getId(),
						ERecordStatus.Y.name());
				AccommodationRequestV2 accommodationRequest = accommodationRequestDAO
						.findByTravelRequestIdAndIsActive(travelRequestV2.getId(), ERecordStatus.Y.name());
				List<TicketRequestV2> ticketRequest = ticketRequestDAO
						.findByTravelRequestIdAndIsActive(travelRequestV2.getId(), ERecordStatus.Y.name());
				travelRequestV2.setCabRequestV2(cabRequest);
				travelRequestV2.setAccommodationRequest(accommodationRequest);
				travelRequestV2.setTicketBooking(ticketRequest);
				travelRequestVO = travelDeskTransformUtil.convertToTravelRequestVO(travelRequestV2);

			} else {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1521) + " ");
			}

			// set total cost
			Float totalApproximateCost = getTotalApproximateCost(empRequest);
			Float totalFinalCost = getFinalCost(empRequest);
			travelRequestVO.setTotalApproximateCost(totalApproximateCost);
			travelRequestVO.setTotalFinalCost(totalFinalCost);

			// set approver name and comment
			if (!travelRequestV2.getRequestWF().getStatus().equalsIgnoreCase(ETravelRequestStatus.INCOMPLETE.name())
					|| !travelRequestV2.getRequestWF().getStatus()
							.equalsIgnoreCase(ETravelRequestStatus.INPROCESS.name())) {
				Float finalCost = getFinalCost(travelRequestV2);
				Float approximateCost = getTotalApproximateCost(travelRequestV2);
				TravelDeskApproverRequestVO approverRequestVO = new TravelDeskApproverRequestVO();
				TravelApproverResponseVO approverResponseVO = null;

				if (!travelRequestV2.getRequestWF().getStatus()
						.equalsIgnoreCase(ETravelRequestStatus.INCOMPLETE.name())) {
					// every time approver name getting by approximate cost only
					if (travelDeskTransformUtil.isRequestForManagementEmployee(travelRequestV2)) {
						Employee employee = employeeDAO.findEmpCandByEmpId(tdApproverIdForMgmtEmployee);
						approverResponseVO = new TravelApproverResponseVO();
						approverResponseVO.setId(employee.getId());
						approverResponseVO.setApproverName(
								employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName());
						approverResponseVO.setEmailId(employee.getOfficialEmailId());
					} else {
						if (approximateCost > 0) {
							approverRequestVO.setAmount(approximateCost);
							approverRequestVO.setTravelRequestId(travelRequestV2.getId());
							approverResponseVO = getApproverDetail(approverRequestVO);
						}
					}
				}
				travelRequestVO.setApproverComment(travelRequestV2.getApproverComment());
				travelRequestVO.setApproverDetails(approverResponseVO);
			}
			// set WF status
			if (!HRMSHelper.isNullOrEmpty(travelRequestV2.getRequestWF())) {
				travelRequestVO.setStatus(travelRequestV2.getRequestWF().getStatus());
				travelRequestVO.setPendingWith(travelRequestV2.getRequestWF().getPendingWith());
			}
			// set currency
			travelRequestVO.setCurrency(convertToCurrencyMasterVO(travelRequestV2.getCurrency()));

			getDocumentDetails(reqId, travelRequestVO);

		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201) + " ");
		}

		log.info("Inside get Requests By Id Method");
		response.setResponseBody(travelRequestVO);
		// response.setTotalRecord(totalRecord);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);
		return response;

	}

	private CabRequestV2 saveCabRequestMethod(CabTravelRequestVO request, Long empId)
			throws ParseException, HRMSException {
		CabRequestV2 cabEntity = null;
		if (!HRMSHelper.isNullOrEmpty(request.getId())) {
			cabEntity = cabRequestDAO.findByRequestId(request.getTravelRequestId());
			if (!HRMSHelper.isNullOrEmpty(cabEntity)) {

				cabEntity.setUpdatedBy(empId.toString());
				cabEntity.setUpdatedDate(new Date());
			} else {
				cabEntity = new CabRequestV2();
			}

		} else {
			cabEntity = new CabRequestV2();

		}
		cabEntity.setCreatedBy(empId.toString());
		cabEntity.setCreatedDate(new Date());
		cabEntity.setIsActive(IHRMSConstants.isActive);
		// get travel type(travel tyep is same like bus type. local & outstation)

		MasterBusType travelType = busTypeDAO.findByIdAndIsActive(request.getTravelType().getId(),
				ERecordStatus.Y.name());
		cabEntity.setTravelType(travelType);

		// get trip type
		MasterTripType tripType = tripTypeDAO.findByIdAndIsActive(request.getTripType().getId(),
				ERecordStatus.Y.name());
		cabEntity.setTripType(tripType);
		cabEntity.setChargeableToClient(request.getChargableClient());
		cabEntity.setFromLocation(request.getFromLocation());
		cabEntity.setToLocation(request.getToLocation());
		cabEntity
				.setDateOfJourney(HRMSDateUtil.parse(request.getDateOfjourney(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		cabEntity.setNumberOfTravellers(request.getNoOfTravellers());
		cabEntity.setReturnDate(
				HRMSDateUtil.parse(request.getReturnDateOfjourney(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		cabEntity.setPreferredReturnTime(request.getReturnPreferedTime());
		cabEntity.setPreferredTime(request.getPreferedTime());
		cabEntity.setTravelRequestId(request.getTravelRequestId());
		Employee employee=employeeDAO.findByEmpIdAndOrgId(empId, SecurityFilter.TL_CLAIMS.get().getOrgId());
		cabEntity.setOrgId(employee.getOrgId());
		CabRequestV2 cab = cabRequestDAO.save(cabEntity);

		if (!HRMSHelper.isNullOrEmpty(request.getTravellerDetails())) {
			if (request.getNoOfTravellers() != (request.getTravellerDetails().size())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1573));
			}
			addTraverller(request.getTravellerDetails(), cab.getId(), null, null);
		} else {
			if (request.getNoOfTravellers() > 0) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1590));
			}
		}
		return null;
	}

	private TicketRequestV2 saveTicketRequestMethod(TicketTravelRequestVO request, Long empId)
			throws ParseException, HRMSException {

		if (!HRMSHelper.isNullOrEmpty(request.getAirDetails())) {
			TicketRequestV2 ticketEntity = null;
			if (!HRMSHelper.isNullOrEmpty(request.getAirDetails().getId())) {
				ticketEntity = ticketRequestDAO.findByIdAndTravelRequestId(request.getAirDetails().getId(),
						request.getAirDetails().getTravelRequestId());
				if (!HRMSHelper.isNullOrEmpty(ticketEntity)) {

					ticketEntity.setUpdatedBy(empId.toString());
					ticketEntity.setUpdatedDate(new Date());
				} else {
					ticketEntity = new TicketRequestV2();
				}

			} else {
				ticketEntity = new TicketRequestV2();

			}
			Employee employee=employeeDAO.findByEmpIdAndOrgId(empId, SecurityFilter.TL_CLAIMS.get().getOrgId());
			ticketEntity.setCreatedBy(empId.toString());
			ticketEntity.setCreatedDate(new Date());
			ticketEntity.setIsActive(IHRMSConstants.isActive);
			ticketEntity.setAirType(request.getAirDetails().getAirType());
			ticketEntity.setRoundTrip(request.getAirDetails().getRoundTrip());
			ticketEntity.setNoOfTravellers(request.getAirDetails().getNoOfTravellers());
			ticketEntity.setChargeableToClient(request.getAirDetails().getChargableClient());
			ticketEntity.setFromLocation(request.getAirDetails().getFromLocation());
			ticketEntity.setToLocation(request.getAirDetails().getToLocation());
			ticketEntity.setDateofJourney(HRMSDateUtil.parse(request.getAirDetails().getDateOfjourney(),
					IHRMSConstants.FRONT_END_DATE_FORMAT));
			ticketEntity.setReturnDate(HRMSDateUtil.parse(request.getAirDetails().getReturnDateOfjourney(),
					IHRMSConstants.FRONT_END_DATE_FORMAT));
			ticketEntity.setPreferredTime(request.getAirDetails().getPreferedTime());
			ticketEntity.setPreferredReturnTime(request.getAirDetails().getReturnPreferedTime());
			ticketEntity.setTravelRequestId(request.getAirDetails().getTravelRequestId());
			MasterModeOfTravel mode = new MasterModeOfTravel();
			mode = masterModeOfTravelDAO.findByName(EModeOfTravel.Air.name());
			ticketEntity.setModeOfTravel(mode);
			ticketEntity.setOrgId(employee.getOrgId());
			TicketRequestV2 ticket = ticketRequestDAO.save(ticketEntity);

			if (!HRMSHelper.isNullOrEmpty(request.getAirDetails().getTravellerDetails())) {

				if (request.getAirDetails()
						.getNoOfTravellers() != (request.getAirDetails().getTravellerDetails().size())) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1573));
				}
				addTraverller(request.getAirDetails().getTravellerDetails(), null, ticket.getId(), null);
			} else {
				if (request.getAirDetails().getNoOfTravellers() > 0) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1590));
				}
			}

		}
		if (!HRMSHelper.isNullOrEmpty(request.getBusDetails())) {

			TicketRequestV2 ticketEntity = null;
			if (!HRMSHelper.isNullOrEmpty(request.getBusDetails().getId())) {
				ticketEntity = ticketRequestDAO.findByIdAndTravelRequestId(request.getBusDetails().getId(),
						request.getBusDetails().getTravelRequestId());
				if (!HRMSHelper.isNullOrEmpty(ticketEntity)) {

					ticketEntity.setUpdatedBy(empId.toString());
					ticketEntity.setUpdatedDate(new Date());
				} else {
					ticketEntity = new TicketRequestV2();
				}

			} else {
				ticketEntity = new TicketRequestV2();

			}

			ticketEntity.setCreatedBy(empId.toString());
			ticketEntity.setCreatedDate(new Date());
			ticketEntity.setIsActive(IHRMSConstants.isActive);
			ticketEntity.setBusType(request.getBusDetails().getBusType());
			ticketEntity.setRoundTrip(request.getBusDetails().getRoundTrip());
			ticketEntity.setNoOfTravellers(request.getBusDetails().getNoOfTravellers());
			ticketEntity.setChargeableToClient(request.getBusDetails().getChargableClient());
			ticketEntity.setFromLocation(request.getBusDetails().getFromLocation());
			ticketEntity.setToLocation(request.getBusDetails().getToLocation());
			ticketEntity.setDateofJourney(HRMSDateUtil.parse(request.getBusDetails().getDateOfjourney(),
					IHRMSConstants.FRONT_END_DATE_FORMAT));
			ticketEntity.setReturnDate(HRMSDateUtil.parse(request.getBusDetails().getReturnDateOfjourney(),
					IHRMSConstants.FRONT_END_DATE_FORMAT));
			ticketEntity.setPreferredTime(request.getBusDetails().getPreferedTime());
			ticketEntity.setPreferredReturnTime(request.getBusDetails().getReturnPreferedTime());
			ticketEntity.setTravelRequestId(request.getBusDetails().getTravelRequestId());
			MasterModeOfTravel mode = new MasterModeOfTravel();
			mode = masterModeOfTravelDAO.findByName(EModeOfTravel.Bus.name());
			ticketEntity.setModeOfTravel(mode);
			TicketRequestV2 ticket = ticketRequestDAO.save(ticketEntity);

			if (!HRMSHelper.isNullOrEmpty(request.getBusDetails().getTravellerDetails())) {

				if (request.getBusDetails()
						.getNoOfTravellers() != (request.getBusDetails().getTravellerDetails().size())) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1573));
				}
				addTraverller(request.getBusDetails().getTravellerDetails(), null, ticket.getId(), null);
			} else {
				if (request.getBusDetails().getNoOfTravellers() > 0) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1590));
				}
			}
		}
		if (!HRMSHelper.isNullOrEmpty(request.getTrainDetails())) {

			TicketRequestV2 ticketEntity = null;
			if (!HRMSHelper.isNullOrEmpty(request.getTrainDetails().getId())) {
				ticketEntity = ticketRequestDAO.findByIdAndTravelRequestId(request.getTrainDetails().getId(),
						request.getTrainDetails().getTravelRequestId());
				if (!HRMSHelper.isNullOrEmpty(ticketEntity)) {

					ticketEntity.setUpdatedBy(empId.toString());
					ticketEntity.setUpdatedDate(new Date());
				} else {
					ticketEntity = new TicketRequestV2();
				}

			} else {
				ticketEntity = new TicketRequestV2();

			}

			ticketEntity.setCreatedBy(empId.toString());
			ticketEntity.setCreatedDate(new Date());
			ticketEntity.setIsActive(IHRMSConstants.isActive);
			ticketEntity.setTrainType(request.getTrainDetails().getTrainType());
			ticketEntity.setRoundTrip(request.getTrainDetails().getRoundTrip());
			ticketEntity.setNoOfTravellers(request.getTrainDetails().getNoOfTravellers());
			ticketEntity.setChargeableToClient(request.getTrainDetails().getChargableClient());
			ticketEntity.setFromLocation(request.getTrainDetails().getFromLocation());
			ticketEntity.setToLocation(request.getTrainDetails().getToLocation());

			ticketEntity.setDateofJourney(HRMSDateUtil.parse(request.getTrainDetails().getDateOfjourney(),
					IHRMSConstants.FRONT_END_DATE_FORMAT));
			ticketEntity.setReturnDate(HRMSDateUtil.parse(request.getTrainDetails().getReturnDateOfjourney(),
					IHRMSConstants.FRONT_END_DATE_FORMAT));
			ticketEntity.setPreferredTime(request.getTrainDetails().getPreferedTime());
			ticketEntity.setPreferredReturnTime(request.getTrainDetails().getReturnPreferedTime());
			ticketEntity.setTravelRequestId(request.getTrainDetails().getTravelRequestId());
			MasterModeOfTravel mode = new MasterModeOfTravel();
			mode = masterModeOfTravelDAO.findByName(EModeOfTravel.Train.name());
			ticketEntity.setModeOfTravel(mode);
			TicketRequestV2 ticket = ticketRequestDAO.save(ticketEntity);

			if (!HRMSHelper.isNullOrEmpty(request.getTrainDetails().getTravellerDetails())) {

				if (request.getTrainDetails()
						.getNoOfTravellers() != (request.getTrainDetails().getTravellerDetails().size())) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1573));
				}
				addTraverller(request.getTrainDetails().getTravellerDetails(), null, ticket.getId(), null);
			} else {
				if (request.getTrainDetails().getNoOfTravellers() > 0) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1590));
				}
			}
		}

		return null;
	}

	private TravellerDetailsV2 addTraverller(List<TravellerDetailsVO> requestVo, Long cabId, Long ticketId, Long accoId)
			throws HRMSException {
		long primaryCount = 0;
		Set<String> uniqueEmails = new HashSet<>();
		Set<Long> uniqueContactNumbers = new HashSet<>();
		for (TravellerDetailsVO request : requestVo) {
			// input validation
			travelDeskAuthorityHelper.addTravellerInputValidation(request);
			if (!uniqueEmails.add(request.getEmailId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1584));
			}
			if (!uniqueContactNumbers.add(request.getContactNo())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1584));
			}
			TravellerDetailsV2 person = null;
			if (!HRMSHelper.isNullOrEmpty(request.getId())) {
				person = travellerDetailDAO.findIdByIsActive(request.getId(), IHRMSConstants.isActive);
				if (!HRMSHelper.isNullOrEmpty(person)) {

					person.setUpdatedDate(new Date());
				} else {
					person = new TravellerDetailsV2();
				}

			} else {
				person = new TravellerDetailsV2();

			}
			// TravellerDetailsV2 person = new TravellerDetailsV2();
			Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
			Employee emp=employeeDAO.findByEmpIdAndOrgId(empId, SecurityFilter.TL_CLAIMS.get().getOrgId());
			person.setCreatedBy(empId.toString());
			person.setCreatedDate(new Date());
			person.setIsActive(IHRMSConstants.isActive);
			person.setOrgId(emp.getOrgId());
			// get masterTravellerType data
			MasterTravellerType masterTravellerType = travellerTypeDAO
					.findByIdAndIsActive(request.getTravellerType().getId(), ERecordStatus.Y.name());
			person.setMasterTravellerType(masterTravellerType);
			if (request.getTravellerType().getTravellerType().equalsIgnoreCase(IHRMSConstants.Internal_Employee)) {
				Employee employee = employeeDAO.findByofficialEmailId(request.getEmailId());
				if (!HRMSHelper.isNullOrEmpty(employee)) {
					person.setName(
							employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName());
					person.setEmailId(employee.getOfficialEmailId());
					person.setContactNumber(employee.getCandidate().getMobileNumber());
					person.setDob(employee.getCandidate().getDateOfBirth());
					person.setEmployee(employee);
					person.setManagementEmployee(employee.getEmployeeACN().getIsManagement());
				} else {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1574));
				}
			}
			
			person.setContactNumber(request.getContactNo());
			person.setName(request.getName());
			person.setEmailId(request.getEmailId());
			person.setDob(HRMSDateUtil.parse(request.getDateOfBirth(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			person.setPrimaryTraveller(request.getIsPrimaryTreveller());
			if (request.getIsPrimaryTreveller()) {
				primaryCount++;
			}
			if (!HRMSHelper.isNullOrEmpty(cabId)) {
				person.setCabRequestId(cabId);
				person.setTicketRequestId(null);
				person.setAccommodationId(null);
				// input validation
				travelDeskAuthorityHelper.addTravellerValidationForCabRquest(request);
				person.setPickupLocation(request.getPickUpLocation());
				person.setPickupTime(request.getPickUpTime());
				person.setDropLocation(request.getDropLocation());

			} else if (!HRMSHelper.isNullOrEmpty(ticketId)) {
				person.setCabRequestId(null);
				person.setTicketRequestId(ticketId);
				person.setAccommodationId(null);

				TicketRequestV2 ticket = ticketRequestDAO.findByTicketId(ticketId);
				MasterModeOfTravel mode = new MasterModeOfTravel();
				mode = masterModeOfTravelDAO.findByName(EModeOfTravel.Air.name());
				if (ticket.getModeOfTravel().getId() == mode.getId()
						&& ticket.getAirType().equals(EAirType.International.name())) {

					// input validation
					travelDeskAuthorityHelper.addInternationalTravellerValidation(request);

					person.setPassportDateExpiry(HRMSDateUtil.parse(request.getPassportDateOfExpiry(),
							IHRMSConstants.FRONT_END_DATE_FORMAT));
					person.setPassportNumber(request.getPassportNo());
					person.setVisaCountry(request.getVisaCountry());
					person.setVisaDateExpiry(
							HRMSDateUtil.parse(request.getVisaDateOfExpiry(), IHRMSConstants.FRONT_END_DATE_FORMAT));
					person.setVisaNumber(request.getVisaNo());
					person.setVisaType(request.getVisaType());
				}

			} else if (!HRMSHelper.isNullOrEmpty(accoId)) {
				person.setCabRequestId(null);
				person.setTicketRequestId(null);
				person.setAccommodationId(accoId);
			}

			if (primaryCount > 1) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1576));
			}
			travellerDetailDAO.save(person);
		}
		if (primaryCount == 0) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1576));
		}

		return null;
	}

	@Override
	public HRMSBaseResponse<GetTravelRequestVO> deleteRequest(TravelDetailsDeleteVO deleteVO) throws HRMSException {
		log.info("Inside deleteRequest Travel Request method");
		HRMSBaseResponse<GetTravelRequestVO> response = new HRMSBaseResponse<>();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();

//		TravellerDetailsV2 travellerDetailsV2 = travellerDetailDAO.findIdByIsActive(deleteVO.getId(),
//				IHRMSConstants.isActive);

		if (authorizationServiceImpl.isAuthorizedFunctionName("deleteTravelRequestMethod", role)) {
			log.info("User is authorized to delete travel request.");

			if (!HRMSHelper.isNullOrEmpty(deleteVO)) {
				log.info("DeleteVO is not null or empty.");

				travelDeskAuthorityHelper.deleteTravelRequestInputValidation(deleteVO);

				TravelRequestV2 travelRequestV2 = travelRequestDAO.findIdByIsActive(deleteVO.getTravelRequestId(),
						IHRMSConstants.isActive);
				if (!HRMSHelper.isNullOrEmpty(travelRequestV2)) {
					log.info("Travel request exists for deletion.");

					// Check if employee ID matches the requester ID
					if (empId.equals(travelRequestV2.getRequesterId())) {
						log.info("Employee ID matches requester ID. Proceeding with deletion.");

						if (!HRMSHelper.isNullOrEmpty(deleteVO.getTicketRequestId())) {
							log.info("Deleting travel details by ticket request ID.");
							TravellerDetailsV2 travellerDetailsByTicketRequest = travellerDetailDAO
									.findByTicketRequestAndIsActiveAndId(deleteVO.getTicketRequestId(),
											IHRMSConstants.isActive, deleteVO.getId());

							if (!HRMSHelper.isNullOrEmpty(travellerDetailsByTicketRequest)) {
								travellerDetailsByTicketRequest.setIsActive(IHRMSConstants.isNotActive);
								travellerDetailsByTicketRequest.setUpdatedBy(empId.toString());
								travellerDetailsByTicketRequest.setUpdatedDate(new Date());
								travellerDetailDAO.save(travellerDetailsByTicketRequest);
								log.info("Travel details deleted by ticket request ID.");

							} else {
								log.info("No travel details found for the provided ticket request ID.");
								throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1555));
							}
						} else if (!HRMSHelper.isNullOrEmpty(deleteVO.getCabRequestId())) {
							log.info("Deleting travel details by cab request ID.");
							TravellerDetailsV2 travellerDetailsByCabRequest = travellerDetailDAO
									.findByCabRequestAndIsActiveAndId(deleteVO.getCabRequestId(),
											IHRMSConstants.isActive, deleteVO.getId());

							if (!HRMSHelper.isNullOrEmpty(travellerDetailsByCabRequest)) {
								travellerDetailsByCabRequest.setIsActive(IHRMSConstants.isNotActive);
								travellerDetailsByCabRequest.setUpdatedBy(empId.toString());
								travellerDetailsByCabRequest.setUpdatedDate(new Date());
								travellerDetailDAO.save(travellerDetailsByCabRequest);
								log.info("Travel details deleted by cab request ID.");
							} else {
								log.info("No travel details found for the provided cab request ID.");
								throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1555));
							}
						} else if (!HRMSHelper.isNullOrEmpty(deleteVO.getAccomadationRequestId())) {
							log.info("Deleting travel details by accommodation request ID.");
							TravellerDetailsV2 travellerDetailsByAccomadationRequest = travellerDetailDAO
									.findByAccomadationRequestAndIsActiveAndId(deleteVO.getAccomadationRequestId(),
											IHRMSConstants.isActive, deleteVO.getId());

							if (!HRMSHelper.isNullOrEmpty(travellerDetailsByAccomadationRequest)) {
								travellerDetailsByAccomadationRequest.setIsActive(IHRMSConstants.isNotActive);
								travellerDetailsByAccomadationRequest.setUpdatedBy(empId.toString());
								travellerDetailsByAccomadationRequest.setUpdatedDate(new Date());
								travellerDetailDAO.save(travellerDetailsByAccomadationRequest);
								log.info("Travel details deleted by accommodation request ID.");
							} else {
								log.info("No travel details found for the provided accommodation request ID.");
								throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1555));
							}
						} else {
							log.info("Travel request does not exist for deletion.");
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1553));
						}

						log.info("Exit deleteRequest Method with Success");
						response.setResponseCode(1200);
						response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1549));
						response.setApplicationVersion(applicationVersion);
						return response;

					} else {
						log.info("Employee ID does not match requester ID.");
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
					}

				} else {
					log.info("Travel request does not exist for deletion.");
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1553));
				}

			} else {
				log.info("DeleteVO is null or empty.");
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
		} else {
			log.info("User is not authorized to delete travel request.");
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public HRMSBaseResponse<?> submitTravelRequest(@RequestBody SubmitTravelRequestVO request)
			throws HRMSException, ParseException {
		log.info("Inside submit Travel Request method");
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (!HRMSHelper.isNullOrEmpty(request) && !HRMSHelper.isNullOrEmpty(request.getId())
				&& !HRMSHelper.isLongZero(request.getId())) {
			TravelRequestV2 travel = travelRequestDAO.findByIdAndIsActive(request.getId(), IHRMSConstants.isActive);
			if (travel.getBookTicket() && HRMSHelper.isNullOrEmpty(request.getTicketDetails())) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1568));
			}
			if (travel.getBookCab() && HRMSHelper.isNullOrEmpty(request.getCabDetails())) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1568));
			}
			if (travel.getBookAccommodation() && HRMSHelper.isNullOrEmpty(request.getAccommodationDetails())) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1568));
			}
			if (travel.getRequesterId().equals(empId)) {
				if (travel.getRequestWF().getStatus().equals(ETravelRequestStatus.INCOMPLETE.name())) {
					if (!HRMSHelper.isNullOrEmpty(request.getCabDetails()) && travel.getBookCab()) {
						if (!request.getCabDetails().getTravelRequestId().equals(request.getId())) {
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1562));
						}
						if (authorizationServiceImpl.isAuthorizedFunctionName("saveCabRequestMethod", role)) {
							// input validation
							travelDeskAuthorityHelper.saveCabInputValidation(request.getCabDetails());
							saveCabRequestMethod(request.getCabDetails(), empId);
						} else {
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
						}
					}
					if (!HRMSHelper.isNullOrEmpty(request.getAccommodationDetails()) && travel.getBookAccommodation()) {
						if (!request.getAccommodationDetails().getTravelRequestId().equals(request.getId())) {
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1562));
						}
						// input validation
						travelDeskAuthorityHelper.saveAccommodationInputValidation(request.getAccommodationDetails());
						if (authorizationServiceImpl.isAuthorizedFunctionName("saveAccommodationRequestMethod", role)) {
							saveAccommodationRequestMethod(request.getAccommodationDetails(), empId);
						} else {
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
						}
					}
					if (!HRMSHelper.isNullOrEmpty(request.getTicketDetails()) && travel.getBookTicket()) {
						if (!HRMSHelper.isNullOrEmpty(request.getTicketDetails().getAirDetails()) && !request
								.getTicketDetails().getAirDetails().getTravelRequestId().equals(request.getId())) {
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1562));
						}
						if (!HRMSHelper.isNullOrEmpty(request.getTicketDetails().getBusDetails()) && !request
								.getTicketDetails().getBusDetails().getTravelRequestId().equals(request.getId())) {
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1562));
						}
						if (!HRMSHelper.isNullOrEmpty(request.getTicketDetails().getTrainDetails()) && !request
								.getTicketDetails().getTrainDetails().getTravelRequestId().equals(request.getId())) {
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1562));
						}
						if (authorizationServiceImpl.isAuthorizedFunctionName("saveTicketRequestMethod", role)) {
							// input validation
							travelDeskAuthorityHelper.saveTicketInputValidation(request.getTicketDetails());
							saveTicketRequestMethod(request.getTicketDetails(), empId);
						} else {
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
						}
					}

					if (!HRMSHelper.isNullOrEmpty(request.getId())) {
						if (authorizationServiceImpl.isAuthorizedFunctionName("submitRequest", role)) {
							submitRequest(request.getId(), empId);
						} else {
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
						}

					}

					Employee emp = employeeDAO.findActiveEmployeeById(empId, IHRMSConstants.isActive);

					MapTravelDeskApprover tdEmpId = new MapTravelDeskApprover();
					Employee employee = new Employee();
					String tdMailId = null;

					List<MapTravelDeskApprover> tdEmployeeId = mapApproverDAO.findIdByDivisionId(travel.getDivisionId(),
							IHRMSConstants.isActive);

					for (MapTravelDeskApprover tdAssistant : tdEmployeeId) {
						if (!HRMSHelper.isNullOrEmpty(tdAssistant.getBranchId())) {
							if (tdAssistant.getBranchId()
									.equals(emp.getCandidate().getCandidateProfessionalDetail().getBranch().getId())) {
								tdEmpId = tdAssistant;
							}
						} else {
							tdEmpId = tdAssistant;
						}
					}

					if (!HRMSHelper.isNullOrEmpty(tdEmpId)) {
						employee = employeeDAO.findActiveEmployeeById(tdEmpId.getApproverId(), IHRMSConstants.isActive);
						tdMailId = employee.getOfficialEmailId();
					} else {

						employee = employeeDAO.findActiveEmployeeById(tdEmpId.getApproverId(), IHRMSConstants.isActive);
						tdMailId = employee.getOfficialEmailId();
					}
					EmailTemplate template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
							IHRMSConstants.REQUESTER_TO_TRAVEL_DESK, IHRMSConstants.isActive,
							employee.getCandidate().getCandidateProfessionalDetail().getDivision(),
							employee.getCandidate().getLoginEntity().getOrganization());
					String ccMailId = null;
					mailForSubmitRequestByEmp(travel, template, tdMailId, ccMailId);

					response.setResponseCode(1200);
					response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1575));
					response.setApplicationVersion(applicationVersion);
					log.info("Exit submit Travel Request method");
					return response;

				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1554));
				}
			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}

		} else
			throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);

	}

	private TravelRequestV2 submitRequest(Long id, Long empId) throws ParseException, HRMSException {

		TravelRequestV2 travel = travelRequestDAO.findByIdAndIsActive(id, IHRMSConstants.isActive);

		if (!HRMSHelper.isNullOrEmpty(travel)) {

			travel.setUpdatedBy(empId.toString());
			travel.setUpdatedDate(new Date());
//			travel.setStatus(ETravelRequestStatus.INPROCESS.name());

			addWFStatus(travel, empId, ETravelRequestStatus.INPROCESS.name(), ERole.TRAVEL_DESK.name());
			travelRequestDAO.save(travel);
		} else {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}

		return null;
	}

	/******************
	 * added by Akanksha for save request for TD
	 ***********************/
	@Transactional(rollbackFor = Exception.class)
	@Override
	public HRMSBaseResponse<GetTravelRequestVO> saveTravelRequestForTd(@RequestBody SaveTravelRequestVO request)
			throws HRMSException, ParseException {
		log.info("Inside save Travel Request By TD method");
		HRMSBaseResponse<GetTravelRequestVO> response = new HRMSBaseResponse<>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		if (HRMSHelper.isRolePresent(role, ERole.TRAVEL_DESK.name())) {
			if (!HRMSHelper.isNullOrEmpty(request) && !HRMSHelper.isNullOrEmpty(request.getId())) {
				TravelRequestV2 travel = travelRequestDAO.findByIdAndIsActive(request.getId(), IHRMSConstants.isActive);
				if (!HRMSHelper.isNullOrEmpty(travel)) {
					List<MapTravelDeskApprover> mapApprover = mapApproverDAO.findApproverIdAndIsActive(empId,
							IHRMSConstants.isActive);
					List<Long> divId = mapApprover.stream().map(MapTravelDeskApprover::getDivisionId)
							.collect(Collectors.toList());
					List<Long> branchIdList = mapApprover.stream().map(MapTravelDeskApprover::getBranchId)
							.collect(Collectors.toList());

					if (!HRMSHelper.isNullOrEmpty(mapApprover)) {
						Long reqBranchId = travel.getBranchId();
						Long reqDivisionId = travel.getDivisionId();
						List<Long> branchIds = new ArrayList<>();

						for (Long branchId : branchIdList) {
							if (!HRMSHelper.isNullOrEmpty(branchId)) {
								branchIds.add(branchId);
							}
						}
						if ((!HRMSHelper.isNullOrEmpty(reqBranchId) && !HRMSHelper.isNullOrEmpty(branchIds)
								&& mapApprover.stream().map(MapTravelDeskApprover::getBranchId)
										.anyMatch(branchId -> reqBranchId.equals(branchId)))
								|| mapApprover.stream().map(MapTravelDeskApprover::getDivisionId)
										.anyMatch(divisionId -> reqDivisionId.equals(divisionId))) {
							if (travel.getRequestWF().getStatus().equals(ETravelRequestStatus.INPROCESS.name())) {
								if (!HRMSHelper.isNullOrEmpty(request.getCabDetails()) && travel.getBookCab()) {
									if (authorizationServiceImpl.isAuthorizedFunctionName("saveCabRequestMethodTd",
											role)) {
										// input validation
										travelDeskAuthorityHelper.saveCabInputValidationTd(request.getCabDetails());
										saveCabRequestMethodTd(request.getCabDetails(), empId,
												ETravelRequestStatus.INPROCESS.name());
									} else {
										throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
									}
								}
								if (!HRMSHelper.isNullOrEmpty(request.getAccommodationDetails())
										&& travel.getBookAccommodation()) {

									if (authorizationServiceImpl
											.isAuthorizedFunctionName("saveAccommodationRequestMethodTd", role)) {
										// input validation
										travelDeskAuthorityHelper
												.saveAccommodationInputValidationTd(request.getAccommodationDetails());
										saveAccommodationRequestMethodTd(request.getAccommodationDetails(), empId,
												ETravelRequestStatus.INPROCESS.name());
									} else {
										throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
									}
								}
								if (!HRMSHelper.isNullOrEmpty(request.getTicketDetails()) && travel.getBookTicket()) {

									if (authorizationServiceImpl.isAuthorizedFunctionName("saveTicketRequestMethodTd",
											role)) {
										// input validation
										travelDeskAuthorityHelper
												.saveTicketInputValidationTd(request.getTicketDetails());
										saveTicketRequestMethodTd(request.getTicketDetails(), empId,
												ETravelRequestStatus.INPROCESS.name());
									} else {
										throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
									}
								}

							} else if (travel.getRequestWF().getStatus().equals(ETravelRequestStatus.APPROVED.name())
									|| travel.getRequestWF().getStatus().equals(ETravelRequestStatus.PENDING.name())) {
								if (!HRMSHelper.isNullOrEmpty(request.getCabDetails()) && travel.getBookCab()) {
									if (authorizationServiceImpl.isAuthorizedFunctionName("saveCabRequestMethodTd",
											role)) {
										// input validation
										travelDeskAuthorityHelper
												.saveCabInputValidationTdFinal(request.getCabDetails());
										saveCabRequestMethodTd(request.getCabDetails(), empId,
												ETravelRequestStatus.APPROVED.name());
									} else {
										throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
									}
								}
								if (!HRMSHelper.isNullOrEmpty(request.getAccommodationDetails())
										&& travel.getBookAccommodation()) {

									if (authorizationServiceImpl
											.isAuthorizedFunctionName("saveAccommodationRequestMethodTd", role)) {
										// input validation
										travelDeskAuthorityHelper.saveAccommodationInputValidationTdFinal(
												request.getAccommodationDetails());
										saveAccommodationRequestMethodTd(request.getAccommodationDetails(), empId,
												ETravelRequestStatus.APPROVED.name());
									} else {
										throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
									}
								}
								if (!HRMSHelper.isNullOrEmpty(request.getTicketDetails()) && travel.getBookTicket()) {

									if (authorizationServiceImpl.isAuthorizedFunctionName("saveTicketRequestMethodTd",
											role)) {
										// input validation
										travelDeskAuthorityHelper
												.saveTicketInputValidationTdFinal(request.getTicketDetails());
										saveTicketRequestMethodTd(request.getTicketDetails(), empId,
												ETravelRequestStatus.APPROVED.name());
									} else {
										throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
									}
								}

							} else {
								throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1567));
							}
							updateTravelRequestCost(travel.getId());
							response.setResponseCode(1200);
							response.setResponseMessage(IHRMSConstants.successMessage);
							response.setApplicationVersion(applicationVersion);
							log.info("Exit save Travel Request By TD method");
							return response;
						} else {
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
						}
					} else {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521) + " ");
					}
				} else {
					throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
				}
			} else
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
	}

	private void updateTravelRequestCost(Long requestId) throws HRMSException {
		log.info("inside updateTravelRequestCost method");
		TravelRequestV2 travel = travelRequestDAO.findByIdAndIsActive(requestId, IHRMSConstants.isActive);
		// get approver details

		Float totalApproximateCost = getTotalApproximateCost(travel);
		Float FinalApproximateCost = getFinalCost(travel);

		TravelDeskApproverRequestVO approverRequestVO = new TravelDeskApproverRequestVO();
		approverRequestVO.setAmount(totalApproximateCost);
		approverRequestVO.setTravelRequestId(travel.getId());
		TravelApproverResponseVO approverResponseVO = getApproverDetail(approverRequestVO);

		travel.setTotalFinalCost(FinalApproximateCost);
		travel.setTotalApproximateCost(totalApproximateCost);
		if (!HRMSHelper.isNullOrEmpty(approverResponseVO)) {
			travel.setApproverId(approverResponseVO.getId());
		}
		travelRequestDAO.save(travel);
		log.info("Exit from updateTravelRequestCost method");

	}

	private CabRequestV2 saveCabRequestMethodTd(CabTravelRequestVO request, Long empId, String status)
			throws ParseException, HRMSException {

		CabRequestV2 cabEntity = null;
		if (!HRMSHelper.isNullOrEmpty(request.getId())) {
			cabEntity = cabRequestDAO.findByRequestId(request.getTravelRequestId());
			if (!HRMSHelper.isNullOrEmpty(cabEntity)) {
				if (status.equalsIgnoreCase(ETravelRequestStatus.INPROCESS.name())) {
					cabEntity.setUpdatedBy(empId.toString());
					cabEntity.setUpdatedDate(new Date());
					cabEntity.setApproximateCost(request.getApproximateCost());
					cabEntity.setFinalCost(request.getApproximateCost());
//					cabEntity.setApproverId(request.getApprover().getId());
					cabEntity.setApproximateDistance(request.getApproximateDistance());
					cabEntity.setDriverId(
							!HRMSHelper.isNullOrEmpty(request.getDriverName()) ? request.getDriverName().getId()
									: null);
					cabEntity.setVehicleId(
							!HRMSHelper.isNullOrEmpty(request.getVehicleName()) ? request.getVehicleName().getId()
									: null);
					cabEntity.setApproximateCostComment(request.getApproximateComment());
					MasterTravellerType cabType = null;
					if (!HRMSHelper.isNullOrEmpty(request.getCabType())) {
						cabType = travellerTypeDAO.findByIdAndIsActive(request.getCabType().getId(),
								ERecordStatus.Y.name());
					}
					cabEntity.setCabType(cabType);
				} else if (status.equalsIgnoreCase(ETravelRequestStatus.APPROVED.name())) {
					cabEntity.setUpdatedBy(empId.toString());
					cabEntity.setUpdatedDate(new Date());
					cabEntity.setFinalCost(request.getApproximateCost());
					cabEntity.setFinalCostComment(request.getFinalCostcomment());
				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
				}

				cabRequestDAO.save(cabEntity);
			} else {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}

		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));

		}

		return null;
	}

	private AccommodationRequestV2 saveAccommodationRequestMethodTd(AccommodationTravelRequestVO request, Long empId,
			String status) throws ParseException, HRMSException {

		AccommodationRequestV2 accoEntity = null;
		if (!HRMSHelper.isNullOrEmpty(request.getId())) {
			accoEntity = accommodationRequestDAO.findByRequestId(request.getTravelRequestId());
			if (!HRMSHelper.isNullOrEmpty(accoEntity)) {
				if (status.equalsIgnoreCase(ETravelRequestStatus.INPROCESS.name())) {
					accoEntity.setUpdatedBy(empId.toString());
					accoEntity.setUpdatedDate(new Date());
					accoEntity.setApproximateCost(request.getApproximateCost());
//					accoEntity.setApproverId(request.getApprover().getId());
					accoEntity.setApproximateCostComment(request.getApproximateComment());
				} else if (status.equalsIgnoreCase(ETravelRequestStatus.APPROVED.name())) {
					accoEntity.setUpdatedBy(empId.toString());
					accoEntity.setUpdatedDate(new Date());
					accoEntity.setFinalCost(request.getFinalCost());
					accoEntity.setFinalCostComment(request.getFinalCostcomment());
				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
				}

				accommodationRequestDAO.save(accoEntity);
			} else {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}

		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		return null;
	}

	private TicketRequestV2 saveTicketRequestMethodTd(TicketTravelRequestVO request, Long empId, String status)
			throws ParseException, HRMSException {

		if (!HRMSHelper.isNullOrEmpty(request.getAirDetails())) {

			TicketRequestV2 ticketEntity = null;
			if (!HRMSHelper.isNullOrEmpty(request.getAirDetails().getId())) {
				ticketEntity = ticketRequestDAO.findByIdAndTravelRequestId(request.getAirDetails().getId(),
						request.getAirDetails().getTravelRequestId());
				if (!HRMSHelper.isNullOrEmpty(ticketEntity)) {
					if (status.equalsIgnoreCase(ETravelRequestStatus.INPROCESS.name())) {
						ticketEntity.setUpdatedBy(empId.toString());
						ticketEntity.setUpdatedDate(new Date());
						ticketEntity.setApproximateCost(request.getAirDetails().getApproximateCost());
//						ticketEntity.setApproverId(request.getAirDetails().getApprover().getId());
						ticketEntity.setApproximateCostComment(request.getAirDetails().getApproximateComment());
					} else if (status.equalsIgnoreCase(ETravelRequestStatus.APPROVED.name())) {
						ticketEntity.setUpdatedBy(empId.toString());
						ticketEntity.setUpdatedDate(new Date());
						ticketEntity.setFinalCost(request.getAirDetails().getFinalCost());
						ticketEntity.setFinalCostComment(request.getAirDetails().getFinalCostcomment());
					} else {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
					}
					ticketRequestDAO.save(ticketEntity);

				} else {
					throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
				}

			} else {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));

			}

		}
		if (!HRMSHelper.isNullOrEmpty(request.getBusDetails())) {

			TicketRequestV2 ticketEntity = null;
			if (!HRMSHelper.isNullOrEmpty(request.getBusDetails().getId())) {
				ticketEntity = ticketRequestDAO.findByIdAndTravelRequestId(request.getBusDetails().getId(),
						request.getBusDetails().getTravelRequestId());
				if (!HRMSHelper.isNullOrEmpty(ticketEntity)) {
					if (status.equalsIgnoreCase(ETravelRequestStatus.INPROCESS.name())) {
						ticketEntity.setUpdatedBy(empId.toString());
						ticketEntity.setUpdatedDate(new Date());
						ticketEntity.setApproximateCost(request.getBusDetails().getApproximateCost());
//						ticketEntity.setApproverId(request.getBusDetails().getApprover().getId());
						ticketEntity.setApproximateCostComment(request.getBusDetails().getApproximateComment());
					} else if (status.equalsIgnoreCase(ETravelRequestStatus.APPROVED.name())) {
						ticketEntity.setUpdatedBy(empId.toString());
						ticketEntity.setUpdatedDate(new Date());
						ticketEntity.setFinalCost(request.getBusDetails().getFinalCost());
						ticketEntity.setFinalCostComment(request.getBusDetails().getFinalCostcomment());
					} else {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
					}
					ticketRequestDAO.save(ticketEntity);

				} else {
					throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
				}

			} else {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));

			}

		}
		if (!HRMSHelper.isNullOrEmpty(request.getTrainDetails())) {

			TicketRequestV2 ticketEntity = null;
			if (!HRMSHelper.isNullOrEmpty(request.getTrainDetails().getId())) {
				ticketEntity = ticketRequestDAO.findByIdAndTravelRequestId(request.getTrainDetails().getId(),
						request.getTrainDetails().getTravelRequestId());
				if (!HRMSHelper.isNullOrEmpty(ticketEntity)) {
					if (status.equalsIgnoreCase(ETravelRequestStatus.INPROCESS.name())) {
						ticketEntity.setUpdatedBy(empId.toString());
						ticketEntity.setUpdatedDate(new Date());
						ticketEntity.setApproximateCost(request.getTrainDetails().getApproximateCost());
//						ticketEntity.setApproverId(request.getTrainDetails().getApprover().getId());
						ticketEntity.setApproximateCostComment(request.getTrainDetails().getApproximateComment());
					} else if (status.equalsIgnoreCase(ETravelRequestStatus.APPROVED.name())) {
						ticketEntity.setUpdatedBy(empId.toString());
						ticketEntity.setUpdatedDate(new Date());
						ticketEntity.setFinalCost(request.getTrainDetails().getFinalCost());
						ticketEntity.setFinalCostComment(request.getTrainDetails().getFinalCostcomment());
					} else {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
					}
					ticketRequestDAO.save(ticketEntity);

				} else {
					throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
				}

			} else {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));

			}

		}

		return null;
	}

	@Override
	public HRMSBaseResponse<?> cancelRequest(TravelDetailsCancelVO cancelVO) throws HRMSException {
		log.info("Inside cancelRequest Travel Request method");
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		Long loggedInEmp = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();

		if (authorizationServiceImpl.isAuthorizedFunctionName("cancelRequest", role)) {
			log.info("User is authorized to cancelRequest travel request.");
			List<MapTravelDeskApprover> mapApprover = mapApproverDAO.findApproverIdAndIsActive(loggedInEmp,
					IHRMSConstants.isActive);
			TravelRequestWf travelRequestWf = null;
			// input validation
			travelDeskAuthorityHelper.cancelTravelRequestInputValidation(cancelVO);
			TravelRequestV2 travelRequestV2 = travelRequestDAO.findIdByIsActive(cancelVO.getTravelRequestId(),
					IHRMSConstants.isActive);
			if (!HRMSHelper.isNullOrEmpty(travelRequestV2)) {
				if (!HRMSHelper.isNullOrEmpty(mapApprover) && !HRMSHelper.isNullOrEmpty(cancelVO.getRequesterId())
						&& !HRMSHelper.isLongZero(cancelVO.getRequesterId())) {

					// check TD map divisionID and requester dept id
					travelRequestV2 = travelRequestDAO.findIdByIsActive(cancelVO.getTravelRequestId(),
							IHRMSConstants.isActive, cancelVO.getRequesterId());

					if (!HRMSHelper.isNullOrEmpty(travelRequestV2)) {
						Long requesterDivisionId = travelRequestV2.getDivisionId();
						long matchCount = mapApprover.stream()
								.filter(e -> e.getDivisionId().equals(requesterDivisionId)).count();
						if (matchCount == 0) {
							throw new HRMSException(1521, ResponseCode.getResponseCodeMap().get(1521));
						}

						travelRequestWf = travelRequestWfDAO.findTravelRequestIdByIsActive(travelRequestV2.getId(),
								ERecordStatus.Y.name());
						if (travelRequestWf.getStatus().equalsIgnoreCase(ETravelRequestStatus.CANCELLED.name())) {
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1569));
						}
						if (travelRequestWf.getStatus().equalsIgnoreCase(ETravelRequestStatus.INCOMPLETE.name())) {
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
						}
						if (!HRMSHelper.isNullOrEmpty(travelRequestWf)) {
							travelRequestWf.setUpdatedBy(loggedInEmp.toString());
							travelRequestWf.setUpdatedDate(new Date());
							travelRequestWf.setStatus(ETravelRequestStatus.CANCELLED.name());
						}

						travelRequestWfDAO.save(travelRequestWf);
					} else {
						throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
					}
				} else {
					travelRequestV2 = travelRequestDAO.findIdByIsActive(cancelVO.getTravelRequestId(),
							IHRMSConstants.isActive);
					if (HRMSHelper.isNullOrEmpty(travelRequestV2)) {
						throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
					}
					// check loggedInemp and requester emp
					if (!loggedInEmp.equals(travelRequestV2.getRequesterId())) {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
					}

					travelRequestWf = travelRequestWfDAO.findTravelRequestIdByIsActive(travelRequestV2.getId(),
							ERecordStatus.Y.name());
					if (!HRMSHelper.isNullOrEmpty(travelRequestWf)) {
						if (travelRequestWf.getStatus().equalsIgnoreCase(ETravelRequestStatus.CANCELLED.name())) {
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1569));
						}

						if (travelRequestWf.getStatus().equalsIgnoreCase(ETravelRequestStatus.INCOMPLETE.name())) {
							travelRequestWf.setUpdatedBy(loggedInEmp.toString());
							travelRequestWf.setUpdatedDate(new Date());
							travelRequestWf.setStatus(ETravelRequestStatus.CANCELLED.name());
						} else {
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1589));
						}
					}
					travelRequestWfDAO.save(travelRequestWf);
				}
			} else {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201) + " for given request.");
			}

		} else {
			log.info("User is not authorized to cancel travel request.");
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		log.info("Exit cancelRequest Method with Success");
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1558));
		response.setApplicationVersion(applicationVersion);
		return response;
	}

	/******************
	 * added by Akanksha for submit request for TD
	 ***********************/
	@Transactional(rollbackFor = Exception.class)
	@Override
	public HRMSBaseResponse<?> submitTravelRequestForTd(@RequestBody SubmitTravelRequestVO request)
			throws HRMSException, ParseException {
		log.info("Inside submit Travel Request By TD method");
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		if (HRMSHelper.isRolePresent(role, ERole.TRAVEL_DESK.name())) {
			if (!HRMSHelper.isNullOrEmpty(request) && !HRMSHelper.isNullOrEmpty(request.getId())) {
				TravelRequestV2 travel = travelRequestDAO.findByIdAndIsActive(request.getId(), IHRMSConstants.isActive);
				if (!HRMSHelper.isNullOrEmpty(travel)) {

					List<MapTravelDeskApprover> mapApprover = mapApproverDAO.findApproverIdAndIsActive(empId,
							IHRMSConstants.isActive);
					List<Long> divId = mapApprover.stream().map(MapTravelDeskApprover::getDivisionId)
							.collect(Collectors.toList());
					List<Long> branchIdList = mapApprover.stream().map(MapTravelDeskApprover::getBranchId)
							.collect(Collectors.toList());

					if (!HRMSHelper.isNullOrEmpty(mapApprover)) {
						Long reqBranchId = travel.getBranchId();
						Long reqDivisionId = travel.getDivisionId();
						List<Long> branchIds = new ArrayList<>();

						for (Long branchId : branchIdList) {
							if (!HRMSHelper.isNullOrEmpty(branchId)) {
								branchIds.add(branchId);
							}
						}
						if ((!HRMSHelper.isNullOrEmpty(reqBranchId) && !HRMSHelper.isNullOrEmpty(branchIds)
								&& mapApprover.stream().map(MapTravelDeskApprover::getBranchId)
										.anyMatch(branchId -> reqBranchId.equals(branchId)))
								|| mapApprover.stream().map(MapTravelDeskApprover::getDivisionId)
										.anyMatch(divisionId -> reqDivisionId.equals(divisionId))) {
							if (travel.getRequestWF().getStatus().equals(ETravelRequestStatus.INPROCESS.name())) {
								if (!HRMSHelper.isNullOrEmpty(request.getCabDetails()) && travel.getBookCab()) {
									if (authorizationServiceImpl.isAuthorizedFunctionName("saveCabRequestMethodTd",
											role)) {
										// input validation
										travelDeskAuthorityHelper.submitCabInputValidationTd(request.getCabDetails());
										saveCabRequestMethodTd(request.getCabDetails(), empId,
												ETravelRequestStatus.INPROCESS.name());
									} else {
										throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
									}
								}
								if (!HRMSHelper.isNullOrEmpty(request.getAccommodationDetails())
										&& travel.getBookAccommodation()) {

									if (authorizationServiceImpl
											.isAuthorizedFunctionName("saveAccommodationRequestMethodTd", role)) {
										// input validation
										travelDeskAuthorityHelper.submitAccommodationInputValidationTd(
												request.getAccommodationDetails());
										saveAccommodationRequestMethodTd(request.getAccommodationDetails(), empId,
												ETravelRequestStatus.INPROCESS.name());
									} else {
										throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
									}
								}
								if (!HRMSHelper.isNullOrEmpty(request.getTicketDetails()) && travel.getBookTicket()) {

									if (authorizationServiceImpl.isAuthorizedFunctionName("saveTicketRequestMethodTd",
											role)) {
										// input validation
										travelDeskAuthorityHelper
												.submitTicketInputValidationTd(request.getTicketDetails());
										saveTicketRequestMethodTd(request.getTicketDetails(), empId,
												ETravelRequestStatus.INPROCESS.name());
									} else {
										throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
									}
								}

							} else if (travel.getRequestWF().getStatus().equals(ETravelRequestStatus.APPROVED.name())
									|| travel.getRequestWF().getStatus().equals(ETravelRequestStatus.PENDING.name())) {
								if (!HRMSHelper.isNullOrEmpty(request.getCabDetails()) && travel.getBookCab()) {
									if (authorizationServiceImpl.isAuthorizedFunctionName("saveCabRequestMethodTd",
											role)) {
										// input validation
										travelDeskAuthorityHelper
												.submitCabInputValidationTdFinal(request.getCabDetails());
										saveCabRequestMethodTd(request.getCabDetails(), empId,
												ETravelRequestStatus.APPROVED.name());
									} else {
										throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
									}
								}
								if (!HRMSHelper.isNullOrEmpty(request.getAccommodationDetails())
										&& travel.getBookAccommodation()) {

									if (authorizationServiceImpl
											.isAuthorizedFunctionName("saveAccommodationRequestMethodTd", role)) {
										// input validation
										travelDeskAuthorityHelper.submitAccommodationInputValidationTdFinal(
												request.getAccommodationDetails());
										saveAccommodationRequestMethodTd(request.getAccommodationDetails(), empId,
												ETravelRequestStatus.APPROVED.name());
									} else {
										throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
									}
								}
								if (!HRMSHelper.isNullOrEmpty(request.getTicketDetails()) && travel.getBookTicket()) {

									if (authorizationServiceImpl.isAuthorizedFunctionName("saveTicketRequestMethodTd",
											role)) {
										// input validation
										travelDeskAuthorityHelper
												.submitTicketInputValidationTdFinal(request.getTicketDetails());
										saveTicketRequestMethodTd(request.getTicketDetails(), empId,
												ETravelRequestStatus.APPROVED.name());
									} else {
										throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
									}
								}
								if (travel.getRequestWF().getStatus().equals(ETravelRequestStatus.PENDING.name())) {
									addWFStatus(travel, empId, ETravelRequestStatus.COMPLETED.name(),
											ERole.TRAVEL_DESK.name());
								}

							} else {
								throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1554));
							}
							if (!HRMSHelper.isNullOrEmpty(request.getId())) {
								if (authorizationServiceImpl.isAuthorizedFunctionName("submitRequestTd", role)) {
									submitRequestTd(request.getId(), empId);
								} else {
									throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
								}

							}
							// update cost
							updateTravelRequestCost(travel.getId());

							if (travel.getRequestWF().getStatus().equals(ETravelRequestStatus.PENDING.name())) {
								TravelDeskApproverRequestVO approverRequestVO = new TravelDeskApproverRequestVO();
								approverRequestVO.setAmount(travel.getTotalApproximateCost());
								approverRequestVO.setTravelRequestId(travel.getId());
								TravelApproverResponseVO approverResponseVO = getApproverDetail(approverRequestVO);

								String approverMailId = approverResponseVO.getEmailId();

								Employee employee = employeeDAO.findActiveEmployeeById(travel.getRequesterId(),
										IHRMSConstants.isActive);

								EmailTemplate template = emailTemplateDAO
										.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
												IHRMSConstants.TRAVEL_DESK_TO_APPROVER, IHRMSConstants.isActive,
												employee.getCandidate().getCandidateProfessionalDetail().getDivision(),
												employee.getCandidate().getLoginEntity().getOrganization());
								String ccMailId = null;

								mailForSubmitRequestByEmp(travel, template, approverMailId, ccMailId);
							} else if (travel.getRequestWF().getStatus()
									.equals(ETravelRequestStatus.COMPLETED.name())) {

								Employee employee = employeeDAO.findActiveEmployeeById(travel.getRequesterId(),
										IHRMSConstants.isActive);
								String requesterMailId = employee.getOfficialEmailId();
								EmailTemplate template = emailTemplateDAO
										.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
												IHRMSConstants.TRAVEL_DESK_TO_REQUESTER_CONFIRM_REQUEST,
												IHRMSConstants.isActive,
												employee.getCandidate().getCandidateProfessionalDetail().getDivision(),
												employee.getCandidate().getLoginEntity().getOrganization());
								String ccMailId = null;
								mailForSubmitRequestByEmp(travel, template, requesterMailId, ccMailId);
							}

							response.setResponseCode(1200);
							response.setResponseMessage(IHRMSConstants.successMessage);
							response.setApplicationVersion(applicationVersion);
							log.info("Exit submit Travel Request By TD method");
							return response;

						} else {
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521) + " ");
						}
					} else {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521) + " ");
					}
				} else {
					throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
				}

			} else
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
	}

	private TravelRequestV2 submitRequestTd(Long id, Long empId) throws ParseException, HRMSException {

		TravelRequestV2 travel = travelRequestDAO.findByIdAndIsActive(id, IHRMSConstants.isActive);

		if (!HRMSHelper.isNullOrEmpty(travel)) {
			travel.setUpdatedBy(empId.toString());
			travel.setUpdatedDate(new Date());
			if (travel.getRequestWF().getStatus().equalsIgnoreCase(ETravelRequestStatus.INPROCESS.name())) {
				if (Boolean.toString(travel.getBookCab()).equals(IHRMSConstants.True)
						&& Boolean.toString(travel.getBookAccommodation()).equals(IHRMSConstants.False)
						&& Boolean.toString(travel.getBookTicket()).equals(IHRMSConstants.False)) {
					addWFStatus(travel, empId, ETravelRequestStatus.COMPLETED.name(), ERole.TRAVEL_DESK.name());

				} else {

					addWFStatus(travel, empId, ETravelRequestStatus.PENDING.name(), ERole.TRAVEL_DESK_APPROVER.name());
				}
			}
			if (travel.getRequestWF().getStatus().equalsIgnoreCase(ETravelRequestStatus.APPROVED.name())) {

				addWFStatus(travel, empId, ETravelRequestStatus.COMPLETED.name(), ERole.TRAVEL_DESK.name());
			}

			travelRequestDAO.save(travel);
		} else {
			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}

		return null;
	}

	@Override
	public HRMSBaseResponse<GetTravelRequestVO> approveTravelRequest(TravelRequestApprovalVO approvalVO)
			throws HRMSException {
		log.info("Inside approve Travel Request method");
		HRMSBaseResponse<GetTravelRequestVO> response = new HRMSBaseResponse<>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmp = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		if (authorizationServiceImpl.isAuthorizedFunctionName("approveTravelRequest", role)) {
			log.info("User is authorized to approve travel request.");
			if (!HRMSHelper.isNullOrEmpty(approvalVO)) {
				log.info("approvalVO is not null or empty.");

				travelDeskAuthorityHelper.approveTravelRequestInputValidation(approvalVO);

				// Check if the travel request ID is valid
				TravelRequestV2 travelRequest = travelRequestDAO.findByIdAndIsActive(approvalVO.getTravelRequestId(),
						IHRMSConstants.isActive);
				if (!HRMSHelper.isNullOrEmpty(travelRequest)) {

					// Fetch the associated workflow record
					TravelRequestWf travelRequestWf = travelRequestWfDAO
							.findTravelRequestIdByIsActive(approvalVO.getTravelRequestId(), IHRMSConstants.isActive);
					if (!HRMSHelper.isNullOrEmpty(travelRequestWf)
							&& ETravelRequestStatus.PENDING.name().equals(travelRequestWf.getStatus())
							&& ERole.TRAVEL_DESK_APPROVER.name().equals(travelRequestWf.getPendingWith())) {

						Float approximateCost = getTotalApproximateCost(travelRequest);
						TravelDeskApproverRequestVO approverRequestVO = new TravelDeskApproverRequestVO();
						approverRequestVO.setAmount(approximateCost);
						approverRequestVO.setTravelRequestId(travelRequest.getId());
						TravelApproverResponseVO approverResponseVO = getApproverDetail(approverRequestVO);

						Employee emp = new Employee();
						Long approverId = approverResponseVO.getId();
						emp = employeeDAO.findById(approverId).get();
						emp.getCandidate().getCandidateProfessionalDetail().getDepartment().getId();

						// Check approverResponseVO is not null or empty.
						if (!HRMSHelper.isNullOrEmpty(approverResponseVO)) {

							// Check if the logged in employee ID matches the approver ID
							if (loggedInEmp.equals(approverResponseVO.getId())) {

								// Check if the approver's department is the same as the requester's department
								List<Long> depIds = mapTravelApproverDAO.findDepartmentIdIdAndIsActive(loggedInEmp,
										IHRMSConstants.isActive);
//								if (approverDeptId.equals(travelRequest.getDepartmentId())) {
								if (depIds.contains(travelRequest.getDepartmentId())) {

									// Update the status and pendingWith fields
									travelRequestWf.setStatus(ETravelRequestStatus.APPROVED.name());
									travelRequestWf.setPendingWith(ERole.TRAVEL_DESK.name());
									travelRequestWf.setUpdatedBy(loggedInEmp.toString());
									travelRequestWf.setUpdatedDate(new Date());

									// Save the approver comment
									travelRequest.setApproverComment(approvalVO.getApproverComment());
									travelRequest.setUpdatedBy(loggedInEmp.toString());
									travelRequest.setUpdatedDate(new Date());

									travelRequestDAO.save(travelRequest);
									travelRequestWfDAO.save(travelRequestWf);

									Employee employee = employeeDAO.findActiveEmployeeById(
											travelRequest.getRequesterId(), IHRMSConstants.isActive);
									List<String> mailIds = new ArrayList<>();
									String empMailId = employee.getOfficialEmailId();
									MapTravelDeskApprover tdEmpId = mapApproverDAO.findIdByDivisionIdAndIsActive(
											travelRequest.getDivisionId(), IHRMSConstants.isActive);
									Employee tdEmployee = employeeDAO.findActiveEmployeeById(tdEmpId.getApproverId(),
											IHRMSConstants.isActive);
									String tdMailId = tdEmployee.getOfficialEmailId();

									String mailId = empMailId;
									String ccMailID = tdMailId;

									EmailTemplate template = emailTemplateDAO
											.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
													IHRMSConstants.APPROVER_TO_REQUESTER_APPROVE_REQUEST,
													IHRMSConstants.isActive,
													employee.getCandidate().getCandidateProfessionalDetail()
															.getDivision(),
													employee.getCandidate().getLoginEntity().getOrganization());

									mailForSubmitRequestByEmp(travelRequest, template, mailId, ccMailID);

									log.info("Exit approve Travel Request method");
									response.setResponseCode(1200);
									response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1213));
									response.setApplicationVersion(applicationVersion);
									return response;

								} else {
									log.info("Approver's department does not match requester's department.");
									throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
								}

							} else {
								log.info("Logged in employee ID does not match the approver ID.");
								throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
							}

						} else {
							log.info("approvalVO is null or empty.");
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1568));
						}

					} else {
						log.info(" Request is not in a valid state for approval.");
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1565));
					}

				} else {
					log.info("Travel request does not exist for approved.");
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1563));
				}

			} else {
				log.info("approvalVO is null or empty.");
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1568));
			}
		} else {
			log.info("User is not authorized to approve travel request.");
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
	}

	@Override
	public HRMSBaseResponse<GetTravelRequestVO> rejectTravelRequest(TravelRequestRejectionVO rejectionVO)
			throws HRMSException {
		log.info("Inside reject Travel Request method");
		HRMSBaseResponse<GetTravelRequestVO> response = new HRMSBaseResponse<>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmp = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		if (authorizationServiceImpl.isAuthorizedFunctionName("rejectTravelRequest", role)) {
			log.info("User is authorized to reject travel request.");
			if (!HRMSHelper.isNullOrEmpty(rejectionVO)) {
				log.info("rejectionVO is not null or empty.");

				travelDeskAuthorityHelper.rejectTravelRequestRequestInputValidation(rejectionVO);

				// Check if the travel request ID is valid
				TravelRequestV2 travelRequest = travelRequestDAO.findByIdAndIsActive(rejectionVO.getTravelRequestId(),
						IHRMSConstants.isActive);
				if (!HRMSHelper.isNullOrEmpty(travelRequest)) {

					// Fetch the associated workflow record
					TravelRequestWf travelRequestWf = travelRequestWfDAO
							.findTravelRequestIdByIsActive(rejectionVO.getTravelRequestId(), IHRMSConstants.isActive);
					if (!HRMSHelper.isNullOrEmpty(travelRequestWf)
							&& ETravelRequestStatus.PENDING.name().equals(travelRequestWf.getStatus())
							&& ERole.TRAVEL_DESK_APPROVER.name().equals(travelRequestWf.getPendingWith())) {

						Float approximateCost = getTotalApproximateCost(travelRequest);
						TravelDeskApproverRequestVO approverRequestVO = new TravelDeskApproverRequestVO();
						approverRequestVO.setAmount(approximateCost);
						approverRequestVO.setTravelRequestId(travelRequest.getId());
						TravelApproverResponseVO approverResponseVO = getApproverDetail(approverRequestVO);

						Employee emp = new Employee();
						Long approverId = approverResponseVO.getId();
						emp = employeeDAO.findById(approverId).get();
						Long approverDeptId = emp.getCandidate().getCandidateProfessionalDetail().getDepartment()
								.getId();

						// Check approverResponseVO is not null or empty.
						if (!HRMSHelper.isNullOrEmpty(approverResponseVO)) {

							// Check if the logged in employee ID matches the approver ID
							if (loggedInEmp.equals(approverResponseVO.getId())) {

								// Check if the approver's department is the same as the requester's department
								List<Long> depIds = mapTravelApproverDAO.findDepartmentIdIdAndIsActive(loggedInEmp,
										IHRMSConstants.isActive);
//								if (approverDeptId.equals(travelRequest.getDepartmentId())) {
								if (depIds.contains(travelRequest.getDepartmentId())) {

									// Update the status and pendingWith fields
									travelRequestWf.setStatus(ETravelRequestStatus.REJECTED.name());
									travelRequestWf.setPendingWith(ERole.TRAVEL_DESK.name());
									travelRequestWf.setUpdatedBy(loggedInEmp.toString());
									travelRequestWf.setUpdatedDate(new Date());

									// Save the reject comment
									travelRequest.setApproverComment(rejectionVO.getApproverComment());
									travelRequest.setUpdatedBy(loggedInEmp.toString());
									travelRequest.setUpdatedDate(new Date());

									travelRequestDAO.save(travelRequest);
									travelRequestWfDAO.save(travelRequestWf);

									Employee employee = employeeDAO.findActiveEmployeeById(
											travelRequest.getRequesterId(), IHRMSConstants.isActive);
									List<String> mailIds = new ArrayList<>();
									String empMailId = employee.getOfficialEmailId();
									MapTravelDeskApprover tdEmpId = mapApproverDAO.findIdByDivisionIdAndIsActive(
											travelRequest.getDivisionId(), IHRMSConstants.isActive);
									Employee tdEmployee = employeeDAO.findActiveEmployeeById(tdEmpId.getApproverId(),
											IHRMSConstants.isActive);
									String tdMailId = tdEmployee.getOfficialEmailId();

									String mailId = empMailId;
									String ccMailId = tdMailId;
									EmailTemplate template = emailTemplateDAO
											.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
													IHRMSConstants.APPROVER_TO_REQUESTER_REJECT_REQUEST,
													IHRMSConstants.isActive,
													employee.getCandidate().getCandidateProfessionalDetail()
															.getDivision(),
													employee.getCandidate().getLoginEntity().getOrganization());

									mailForSubmitRequestByEmp(travelRequest, template, mailId, ccMailId);

									log.info("Exit reject Travel Request method");
									response.setResponseCode(1200);
									response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1214));
									response.setApplicationVersion(applicationVersion);
									return response;

								} else {
									log.info("Approver's department does not match requester's department.");
									throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
								}

							} else {
								log.info("Logged in employee ID does not match the approver ID.");
								throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
							}

						} else {
							log.info("approvalVO is null or empty.");
							throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1568));
						}

					} else {
						log.info(" Request is not in a valid state for reject.");
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1566));
					}

				} else {
					log.info("Travel request does not exist for reject.");
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1564));
				}

			} else {
				log.info("rejectionVO is null or empty.");
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1568));
			}

		} else {
			log.info("User is not authorized to reject travel request.");
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
	}

	@Override
	public TravelApproverResponseVO getApproverDetail(TravelDeskApproverRequestVO request) throws HRMSException {
		log.info("Inside getApproverName Method");
		List<String> roles = SecurityFilter.TL_CLAIMS.get().getRoles();
		TravelApproverResponseVO approverResponse = new TravelApproverResponseVO();

		TravelRequestV2 travelRequest = travelRequestDAO.findByIdAndIsActive(request.getTravelRequestId(),
				IHRMSConstants.isActive);
		if (HRMSHelper.isNullOrEmpty(travelRequest)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1551));
		}

		MasterTravelApproverSlab travelApproverSlab = null;
		if (!HRMSHelper.isNullOrEmpty(travelRequest.getCurrency())
				&& !HRMSHelper.isNullOrEmpty(travelRequest.getCurrency().getEntityId())) {
			travelApproverSlab = slabDAO.findByAmountAndCurrencyAndIsActive(request.getAmount(),
					travelRequest.getCurrency().getEntityId(), ERecordStatus.Y.name());
		}

		// check is requet for mgmt employee
		if (travelDeskTransformUtil.isRequestForManagementEmployee(travelRequest)) {
			Employee employee = employeeDAO.findEmpCandByEmpId(tdApproverIdForMgmtEmployee);
			approverResponse.setId(employee.getId());
			approverResponse.setApproverName(
					employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName());
			approverResponse.setEmailId(employee.getOfficialEmailId());
		} else {
			Employee employee = employeeDAO.findEmpCandByEmpId(travelRequest.getRequesterId());
			Long divisionId = employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
			Long departmentId = employee.getCandidate().getCandidateProfessionalDetail().getDepartment().getId();
			Long branchId = employee.getCandidate().getCandidateProfessionalDetail().getBranch().getId();
			if (!HRMSHelper.isNullOrEmpty(travelApproverSlab)) {
				MasterMapTravelApprover mapTravelApprover = travelApproverDAO
						.findByApproverSlabAndDivisionIdAndBranchIdAndDepartmentId(travelApproverSlab.getSlabId(),
								divisionId, branchId, departmentId);
				if (!HRMSHelper.isNullOrEmpty(mapTravelApprover)) {
					approverResponse.setId(mapTravelApprover.getApproverId().getId());
					approverResponse.setApproverName(mapTravelApprover.getApproverId().getCandidate().getFirstName()
							+ " " + mapTravelApprover.getApproverId().getCandidate().getLastName());
					approverResponse.setEmailId(mapTravelApprover.getApproverId().getOfficialEmailId());
				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1613));
				}
			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1613));
			}
		}

		log.info("Exit from getApproverName Method");
		return approverResponse;
	}

	@Override
	public HRMSBaseResponse<GetTravelRequestVO> uploadDocumentForTravelDesk(TravelDeskRequestVO travelDeskRequestVO,
			MultipartFile file) throws HRMSException, IOException {
		log.info("Inside uploadDocumentForTravelDesk Method");

		HRMSBaseResponse<GetTravelRequestVO> response = new HRMSBaseResponse<>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (authorizationServiceImpl.isAuthorizedFunctionName("uploadDocumentForTravelDesk", role)) {
			String ticketType = "";
			if (ETicketType.APPROXIMATE.name().equalsIgnoreCase(travelDeskRequestVO.getTicketType())) {
				ticketType = ETicketType.APPROXIMATE.name();
			} else if (ETicketType.FINAL.name().equalsIgnoreCase(travelDeskRequestVO.getTicketType())) {
				ticketType = ETicketType.FINAL.name();
			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
			}

			if (file.getSize() == 0) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
			}

			if (file.getSize() > maxFileSizeUpload) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1572));
			}

			if (HRMSHelper.isNullOrEmpty(travelDeskRequestVO.getTravelRequestId())) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
			}

			if (HRMSHelper.isNullOrEmpty(travelDeskRequestVO.getTicketType())) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
			}

			int nonZeroCount = 0;
			if (travelDeskRequestVO.getTicketRequestId() != 0) {
				nonZeroCount++;
			}
			if (travelDeskRequestVO.getCabRequestId() != 0) {
				nonZeroCount++;
			}
			if (travelDeskRequestVO.getAccommodationRequestId() != 0) {
				nonZeroCount++;
			}

			if (nonZeroCount != 1) {
				throw new HRMSException(1500,
						"Exactly one of the fields ticketRequestId, cabRequestId, accommodationRequestId must have a non-zero value.");
			}
			
			
			String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();

			if (!EFileExtension.PDF.name().equalsIgnoreCase(fileExtension)
					&& !EFileExtension.JPEG.name().equalsIgnoreCase(fileExtension)
					&& !EFileExtension.JPG.name().equalsIgnoreCase(fileExtension)
					&& !EFileExtension.PNG.name().equalsIgnoreCase(fileExtension)) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1571));
			}

			TravelRequestV2 travelRequest = travelRequestDAO
					.findByIdAndIsActive(travelDeskRequestVO.getTravelRequestId(), IHRMSConstants.isActive);
			if (HRMSHelper.isNullOrEmpty(travelRequest)) {
				throw new HRMSException(1500,
						ResponseCode.getResponseCodeMap().get(1501) + " Travel Request ID Not Found");
			}

			String fileName = file.getOriginalFilename().replaceAll("\\s+", "_");
			String filePath = rootDirectory + File.separator + TRAVELDESK + File.separator
					+ travelDeskRequestVO.getTravelRequestId() + File.separator;
			Long loggedInEmp = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
			TravelDocument travelDocument = null;
			if (!HRMSHelper.isNullOrEmpty(travelDeskRequestVO.getTicketRequestId())
					&& travelDeskRequestVO.getTicketRequestId() != 0) {
				filePath += travelDeskRequestVO.getTicketRequestId() + File.separator
						+ travelDeskRequestVO.getTicketType() + File.separator;
				TicketRequestV2 travelRequestId = ticketRequestDAO.findByIdAndTravelRequestId(
						travelDeskRequestVO.getTicketRequestId(), travelDeskRequestVO.getTravelRequestId());
				if (HRMSHelper.isNullOrEmpty(travelRequestId)) {
					throw new HRMSException(1500,
							ResponseCode.getResponseCodeMap().get(1501) + " Ticket Request ID Not Found");
				}

				travelDocument = travelDocumentDAO.findByTravelRequestIdAndTicketRequestIdAndIsActiveAndTicketType(
						travelDeskRequestVO.getTravelRequestId(), travelDeskRequestVO.getTicketRequestId(),
						IHRMSConstants.isActive, ticketType);

			} else if (!HRMSHelper.isNullOrEmpty(travelDeskRequestVO.getCabRequestId())
					&& travelDeskRequestVO.getCabRequestId() != 0) {
				filePath += travelDeskRequestVO.getCabRequestId() + File.separator + travelDeskRequestVO.getTicketType()
						+ File.separator;

				CabRequestV2 cabRequestV2 = cabRequestDAO.findByIdAndTravelRequestId(
						travelDeskRequestVO.getCabRequestId(), travelDeskRequestVO.getTravelRequestId());

				if (HRMSHelper.isNullOrEmpty(cabRequestV2)) {
					throw new HRMSException(1500,
							ResponseCode.getResponseCodeMap().get(1501) + " Cab Request ID Not Found");
				}

				travelDocument = travelDocumentDAO.findByTravelRequestIdAndCabRequestIdAndIsActiveAndTicketType(
						travelDeskRequestVO.getTravelRequestId(), travelDeskRequestVO.getCabRequestId(),
						IHRMSConstants.isActive, ticketType);

			} else if (!HRMSHelper.isNullOrEmpty(travelDeskRequestVO.getAccommodationRequestId())
					&& travelDeskRequestVO.getAccommodationRequestId() != 0) {
				filePath += travelDeskRequestVO.getAccommodationRequestId() + File.separator
						+ travelDeskRequestVO.getTicketType() + File.separator;
				AccommodationRequestV2 accommodationRequestV2 = accommodationRequestDAO.findByIdAndTravelRequestId(
						travelDeskRequestVO.getAccommodationRequestId(), travelDeskRequestVO.getTravelRequestId());

				if (HRMSHelper.isNullOrEmpty(accommodationRequestV2)) {
					throw new HRMSException(1500,
							ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Request ID Not Found");
				}

				travelDocument = travelDocumentDAO
						.findByTravelRequestIdAndAccommodationRequestIdAndIsActiveAndTicketType(
								travelDeskRequestVO.getTravelRequestId(),
								travelDeskRequestVO.getAccommodationRequestId(), IHRMSConstants.isActive, ticketType);

			} else {
				throw new HRMSException(1500,
						ResponseCode.getResponseCodeMap().get(1501) + " No ID provided in the TravelDeskRequest");
			}

			String savedFilePath = uploadFile(filePath, file);

			if (HRMSHelper.isNullOrEmpty(travelDocument)) {
				travelDocument = new TravelDocument();
				travelDocument.setAccommodationRequestId(travelDeskRequestVO.getAccommodationRequestId());
				travelDocument.setCabRequestId(travelDeskRequestVO.getCabRequestId());
				travelDocument.setCreatedBy(loggedInEmp.toString());
				travelDocument.setCreatedDate(new Date());
				travelDocument.setFileName(fileName);
				travelDocument.setFilePath(savedFilePath);
				travelDocument.setIsActive(IHRMSConstants.isActive);
				travelDocument.setTicketRequestId(travelDeskRequestVO.getTicketRequestId());
				travelDocument.setTravelRequestId(travelDeskRequestVO.getTravelRequestId());
				travelDocument.setTicketType(ticketType);
				travelDocument.setOrgId(SecurityFilter.TL_CLAIMS.get().getOrgId());
				travelDocumentDAO.save(travelDocument);
			} else {
				if (travelDocument.getTicketType().equalsIgnoreCase(ETicketType.APPROXIMATE.name())) {
					travelDocument.setFilePath(savedFilePath);
					travelDocument.setUpdatedBy(loggedInEmp.toString());
					travelDocument.setUpdatedDate(new Date());
					travelDocument.setFileName(fileName);
					travelDocument.setTicketType(ticketType);
					travelDocumentDAO.save(travelDocument);
				} else {
					throw new HRMSException(1500,
							ResponseCode.getResponseCodeMap().get(1501) + " This Ticket is Already Final");
				}
			}
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
		response.setResponseCode(1200);
		response.setResponseMessage(IHRMSConstants.successMessage);
		response.setApplicationVersion(applicationVersion);
		log.info("Exit uploadDocumentForTravelDesk Method");
		return response;
	}

	private String uploadFile(String filePath, MultipartFile file) throws IOException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");
		String newFileName = formatter.format(new Date()) + "."
				+ FilenameUtils.getExtension(file.getOriginalFilename());
		Path path = Paths.get(filePath);
		if (!Files.exists(path)) {
			Files.createDirectories(Paths.get(path.toUri()));
		}
		Files.write(Paths.get(filePath, newFileName), file.getBytes());
		return filePath + File.separator + newFileName;
	}

	@Override
	public ResponseEntity<Resource> viewDocument(Long travelDocumentId) throws HRMSException, FileNotFoundException {
		log.info("Start viewDocument Method");

		TravelDocument travelDocument = travelDocumentDAO.findByIdAndIsActive(travelDocumentId,
				IHRMSConstants.isActive);

		if (HRMSHelper.isNullOrEmpty(travelDocument)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
		}

		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmp = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		TravelRequestV2 travelRequest = travelRequestDAO.findByIdAndIsActive(travelDocument.getTravelRequestId(),
				IHRMSConstants.isActive);
		if (HRMSHelper.isNullOrEmpty(travelRequest)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + " Travel Request ID Not Found");
		}

		if (role.contains(ERole.EMPLOYEE.name()) && !role.contains(ERole.TRAVEL_DESK_APPROVER.name())
				&& !role.contains(ERole.TRAVEL_DESK.name()) && !role.contains(ERole.ACCOUNTANT.name())) {

			if (!travelRequest.getRequesterId().equals(loggedInEmp)) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}
		}

		if (role.contains(ERole.TRAVEL_DESK_APPROVER.name())) {
//			List<MapTravelDeskApprover> mapApprover = mapApproverDAO.findApproverIdAndIsActive(loggedInEmp,
//					IHRMSConstants.isActive);
//				List<Long> divIds = mapApprover.stream().map(MapTravelDeskApprover::getDivisionId)
//					.collect(Collectors.toList());

			Employee approverEmpId = employeeDAO.findById(travelRequest.getApproverId()).get();
			Long approverDivId = approverEmpId.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
			Employee emp = employeeDAO.findById(loggedInEmp).get();
			Long divId = emp.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
			if (!approverDivId.equals(divId)) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}
		}

		if (role.contains(ERole.TRAVEL_DESK.name())) {
			Employee requesterEmp = employeeDAO.findById(travelRequest.getRequesterId()).get();
			Long requesterDivId = requesterEmp.getCandidate().getCandidateProfessionalDetail().getDivision().getId();

			Employee emp = employeeDAO.findById(loggedInEmp).get();
			Long divId = emp.getCandidate().getCandidateProfessionalDetail().getDivision().getId();

			if (!requesterDivId.equals(divId)) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}
		}

		if (role.contains(ERole.ACCOUNTANT.name())) {
			Employee requesterEmp = employeeDAO.findById(travelRequest.getRequesterId()).get();
			Long requesterDivId = requesterEmp.getCandidate().getCandidateProfessionalDetail().getDivision().getId();

			Employee emp = employeeDAO.findById(loggedInEmp).get();
			Long divId = emp.getCandidate().getCandidateProfessionalDetail().getDivision().getId();

			if (!requesterDivId.equals(divId)) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}
		}

		HttpHeaders header = new HttpHeaders();
		header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + travelDocument.getFileName());
		header.add("Cache-Control", "no-cache, no-store, must-revalidate");
		header.add("Pragma", "no-cache");
		header.add("Expires", "0");

		File file = new File(travelDocument.getFilePath());
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

		MediaType mediaType = null;
		String extension = FilenameUtils.getExtension(travelDocument.getFileName());
		if (extension.equalsIgnoreCase(EFileExtension.PDF.name())) {
			mediaType = MediaType.APPLICATION_PDF;
		} else if (extension.equalsIgnoreCase(EFileExtension.JPEG.name())
				|| extension.equalsIgnoreCase(EFileExtension.JPG.name())) {
			mediaType = MediaType.IMAGE_JPEG;
		} else if (extension.equalsIgnoreCase(EFileExtension.PNG.name())) {
			mediaType = MediaType.IMAGE_PNG;
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		log.info("Exit viewDocument Method");
		return ResponseEntity.ok().headers(header).contentLength(file.length()).contentType(mediaType).body(resource);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public HRMSBaseResponse<ExpenseSummeryRequest> addExpenseSummary(ExpenseSummeryRequest expenseSummaryRequest)
			throws HRMSException {

		log.info("Inside add Expense Summary Method");
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		HRMSBaseResponse<ExpenseSummeryRequest> response = new HRMSBaseResponse<ExpenseSummeryRequest>();
		if (authorizationServiceImpl.isAuthorizedFunctionName("addExpenseSummary", role)) {

			log.info("User is authorized to add Expense Summary.");
			if (!HRMSHelper.isNullOrEmpty(expenseSummaryRequest)) {
				log.info("expenseSummaryRequest is not null or empty.");

				travelDeskAuthorityHelper.expenseSummaryRequestInputValidation(expenseSummaryRequest);

				// Check if the travel request ID is valid
				TravelRequestV2 travelRequest = travelRequestDAO
						.findByIdAndIsActive(expenseSummaryRequest.getTravelRequestId(), IHRMSConstants.isActive);

				// Check if the travel request ID is valid
				if (!HRMSHelper.isNullOrEmpty(travelRequest)) {

					// get Travel desk division
					List<MapTravelDeskApprover> mapApprover = mapApproverDAO.findApproverIdAndIsActive(loggedInEmpId,
							IHRMSConstants.isActive);

					long divMatchCount = mapApprover.stream()
							.filter(e -> e.getDivisionId().equals(travelRequest.getDivisionId())).count();

					if (divMatchCount == 0) {
						log.info("TD department does not match requester's department.");
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
					}

					// Fetch the associated workflow record
					TravelRequestWf travelRequestWf = travelRequestWfDAO.findTravelRequestIdByIsActive(
							expenseSummaryRequest.getTravelRequestId(), IHRMSConstants.isActive);
					if (!HRMSHelper.isNullOrEmpty(travelRequestWf)
							&& ETravelRequestStatus.COMPLETED.name().equals(travelRequestWf.getStatus())
							&& ERole.TRAVEL_DESK.name().equals(travelRequestWf.getPendingWith())) {

						Long travelRequestId = expenseSummaryRequest.getTravelRequestId();

						log.info("Extracting ticket expenses.");
						TicketExpenseVO ticketExpenseVO = expenseSummaryRequest.getTicketExpense();
//						if (!HRMSHelper.isNullOrEmpty(ticketExpenseVO)) {
//
//							log.info("Ticket expenses found.");
//							log.info("Air refund cost:", ticketExpenseVO.getAirExpenseVO().getRefundCost());
//							log.info("Air settled cost:", ticketExpenseVO.getAirExpenseVO().getSetteledCost());
//							log.info("Bus refund cost:", ticketExpenseVO.getBusExpenseVO().getRefundCost());
//							log.info("Bus settled cost:", ticketExpenseVO.getBusExpenseVO().getSetteledCost());
//							log.info("Train refund cost:", ticketExpenseVO.getTrainExpenseVO().getRefundCost());
//							log.info("Train settled cost:", ticketExpenseVO.getTrainExpenseVO().getSetteledCost());
//						}

						log.info("Extracting cab expenses.");
						CabExpenseVO cabExpenseVO = expenseSummaryRequest.getCabExpenseVO();
//						if (!HRMSHelper.isNullOrEmpty(cabExpenseVO)) {
//							log.info("Cab expenses found.");
//							log.info("Cab refund cost:", cabExpenseVO.getRefundCost());
//							log.info("Cab settled cost:", cabExpenseVO.getSetteledCost());
//						}

						log.info("Extracting accommodation expenses.");
						ExpenseVO accommodationExpenseVO = expenseSummaryRequest.getAccommodationExpenseVO();
//						if (!HRMSHelper.isNullOrEmpty(accommodationExpenseVO)) {
//							log.info("Accommodation expenses found.");
//							log.info("Accommodation refund cost:", accommodationExpenseVO.getRefundCost());
//							log.info("Accommodation settled cost:", accommodationExpenseVO.getSetteledCost());
//						}

//						log.info("Extracting Total expenses.");
//						ExpenseVO totalExpenseVO = expenseSummaryRequest.getTotalExpense();
//						if (!HRMSHelper.isNullOrEmpty(totalExpenseVO)) {
//							log.info("Total expenses found.");
//							log.info("Total refund cost:", totalExpenseVO.getRefundCost());
//							log.info("Total settled cost:", totalExpenseVO.getSetteledCost());
//						}

						// Save expense details
						log.info("Saving expense details for ticket, cab, and accommodation requests.");

						if (!HRMSHelper.isNullOrEmpty(travelRequest.getBookTicket()) && travelRequest.getBookTicket()) {
							if (!HRMSHelper.isNullOrEmpty(ticketExpenseVO.getAirExpenseVO())) {
								if (HRMSHelper.isNullOrEmpty(ticketExpenseVO.getAirExpenseVO().getId())) {
									throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1610));
								}
								saveRefundCostAndSettledCostForTicketRequest(travelRequestId,
										ticketExpenseVO.getAirExpenseVO().getRefundCost(),
										ticketExpenseVO.getAirExpenseVO().getSetteledCost(),
										ticketExpenseVO.getAirExpenseVO().getId());
							}
							if (!HRMSHelper.isNullOrEmpty(ticketExpenseVO.getBusExpenseVO())) {
								if (HRMSHelper.isNullOrEmpty(ticketExpenseVO.getBusExpenseVO().getId())) {
									throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1611));
								}
								saveRefundCostAndSettledCostForTicketRequest(travelRequestId,
										ticketExpenseVO.getBusExpenseVO().getRefundCost(),
										ticketExpenseVO.getBusExpenseVO().getSetteledCost(),
										ticketExpenseVO.getBusExpenseVO().getId());
							}
							if (!HRMSHelper.isNullOrEmpty(ticketExpenseVO.getTrainExpenseVO())) {
								if (HRMSHelper.isNullOrEmpty(ticketExpenseVO.getTrainExpenseVO().getId())) {
									throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1612));
								}
								saveRefundCostAndSettledCostForTicketRequest(travelRequestId,
										ticketExpenseVO.getTrainExpenseVO().getRefundCost(),
										ticketExpenseVO.getTrainExpenseVO().getSetteledCost(),
										ticketExpenseVO.getTrainExpenseVO().getId());
							}

						}

						if (!HRMSHelper.isNullOrEmpty(travelRequest.getBookAccommodation())
								&& travelRequest.getBookAccommodation()) {
							if (HRMSHelper.isNullOrEmpty(accommodationExpenseVO.getId())) {
								throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1609));
							}
							saveRefundCostAndSettledCostForAccommodationRequest(travelRequestId,
									accommodationExpenseVO.getRefundCost(), accommodationExpenseVO.getSetteledCost(),
									accommodationExpenseVO.getId());
						}

						if (!HRMSHelper.isNullOrEmpty(travelRequest.getBookCab()) && travelRequest.getBookCab()) {
							if (HRMSHelper.isNullOrEmpty(cabExpenseVO.getId())) {
								throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1608));
							}
							saveRefundCostAndSettledCostForCabRequest(travelRequestId, cabExpenseVO.getRefundCost(),
									cabExpenseVO.getSetteledCost(), cabExpenseVO.getId(),
									cabExpenseVO.getActualDistance());
						}
						// Calculate and update total costs
						updateTotalCostsForTravelRequest(travelRequestId);

						response.setResponseCode(1200);
						response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1215));
						response.setApplicationVersion(applicationVersion);
						return response;

					} else {
						log.info("  Expense summary can be submitted only for a completed  travel request .");
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1578));
					}

				} else {
					log.info("Travel request does not exist for submit expense summary.");
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1577));
				}
			} else {
				log.info("expense Summary Request is null or empty.");
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1568));
			}
		} else {
			log.info("User is not authorized to add Expense Summary.");
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
	}

	private void saveRefundCostAndSettledCostForTicketRequest(Long travelRequestId, Float refundCost, Float settledCost,
			Long id) throws HRMSException {
		log.info("Inside saveRefundCostAndSettledCostForTicketRequest method");
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		TicketRequestV2 ticketRequestV2 = null;
		if (!HRMSHelper.isNullOrEmpty(id)) {
			ticketRequestV2 = ticketRequestDAO.findIdByIsActive(travelRequestId, IHRMSConstants.isActive, id);
		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Ticket Id ");
		}
		if (!HRMSHelper.isNullOrEmpty(ticketRequestV2)) {

			if (!travelRequestId.equals(ticketRequestV2.getTravelRequestId())) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}

			if ((!HRMSHelper.isNullOrEmpty(refundCost) && refundCost > 0)
					&& ((!HRMSHelper.isNullOrEmpty(settledCost) && settledCost > 0))) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1589));
			}

			if (!HRMSHelper.isNullOrEmpty(ticketRequestV2.getSettledCost())
					|| !HRMSHelper.isNullOrEmpty(ticketRequestV2.getRefundCost())) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1582));
			}

			ticketRequestV2.setRefundCost(refundCost);
			ticketRequestV2.setSettledCost(settledCost);
			ticketRequestV2.setUpdatedBy(loggedInEmpId.toString());
			ticketRequestV2.setUpdatedDate(new Date());
			ticketRequestDAO.save(ticketRequestV2);
		}
	}

	private void saveRefundCostAndSettledCostForAccommodationRequest(Long travelRequestId, Float refundCost,
			Float settledCost, Long id) throws HRMSException {
		log.info("Inside saveRefundCostAndSettledCostForAccommodationRequest method");
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		AccommodationRequestV2 accommodationRequestV2 = null;

		if (!HRMSHelper.isNullOrEmpty(id)) {
			accommodationRequestV2 = accommodationRequestDAO.findIdByIsActive(travelRequestId, IHRMSConstants.isActive,
					id);
		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Accommodation Id ");
		}

		if (!HRMSHelper.isNullOrEmpty(accommodationRequestV2)) {

			if (!travelRequestId.equals(accommodationRequestV2.getTravelRequestId())) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}

			if ((!HRMSHelper.isNullOrEmpty(refundCost) && refundCost > 0)
					&& ((!HRMSHelper.isNullOrEmpty(settledCost) && settledCost > 0))) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1588));
			}
			if (!HRMSHelper.isNullOrEmpty(accommodationRequestV2.getSettledCost())
					|| !HRMSHelper.isNullOrEmpty(accommodationRequestV2.getRefundCost())) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1582));
			}

			accommodationRequestV2.setRefundCost(refundCost);
			accommodationRequestV2.setSettledCost(settledCost);
			accommodationRequestV2.setUpdatedBy(loggedInEmpId.toString());
			accommodationRequestV2.setUpdatedDate(new Date());
			accommodationRequestDAO.save(accommodationRequestV2);
		}
	}

	private void saveRefundCostAndSettledCostForCabRequest(Long travelRequestId, Float refundCost, Float settledCost,
			Long id, String actualDistance) throws HRMSException {
		log.info("Inside saveRefundCostAndSettledCostForCabRequest method");
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		CabRequestV2 cabRequestV2 = null;
		if (!HRMSHelper.isNullOrEmpty(id)) {
			cabRequestV2 = cabRequestDAO.findIdByIsActive(travelRequestId, IHRMSConstants.isActive, id);
		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cab Id ");
		}

		if (!HRMSHelper.isNullOrEmpty(cabRequestV2)) {

			if (!travelRequestId.equals(cabRequestV2.getTravelRequestId())) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
			}

			if (!HRMSHelper.isNullOrEmpty(cabRequestV2.getSettledCost())
					|| !HRMSHelper.isNullOrEmpty(cabRequestV2.getRefundCost())) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1582));
			}
			if ((!HRMSHelper.isNullOrEmpty(refundCost) && refundCost > 0)
					&& ((!HRMSHelper.isNullOrEmpty(settledCost) && settledCost > 0))) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1591));
			}

			cabRequestV2.setRefundCost(refundCost);
			cabRequestV2.setSettledCost(settledCost);
			cabRequestV2.setActualDistance(actualDistance);
			cabRequestV2.setUpdatedBy(loggedInEmpId.toString());
			cabRequestV2.setUpdatedDate(new Date());
			cabRequestDAO.save(cabRequestV2);
		}
//		else {
//			log.info("Cab request does not exist for add Expense Summary.");
//			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1581));
//		}
	}

	public void mailForSubmitRequestByEmp(TravelRequestV2 travel, EmailTemplate template, String mailIds,
			String ccMailId) {

		Employee employee = employeeDAO.findActiveEmployeeById(travel.getRequesterId(), IHRMSConstants.isActive);

		String employeeEmailID = mailIds;
		Map<String, String> placeHolderMap = new HashMap<String, String>();
		placeHolderMap.put("{travelRequestId}", travel.getId().toString());
		placeHolderMap.put("{department}",
				employee.getCandidate().getCandidateProfessionalDetail().getDepartment().getDepartmentName());
		placeHolderMap.put("{bpmNo}", travel.getBpmNumber().toString());
		placeHolderMap.put("{reason}", travel.getTravelReason());
		placeHolderMap.put("{CreatedDate}",
				HRMSDateUtil.format(travel.getCreatedDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		placeHolderMap.put("{empName}",
				employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName());
		placeHolderMap.put("{websiteURL}", baseURL);

		if (ETravelRequestStatus.INPROCESS.name().equals(travel.getRequestWF().getStatus())) {
			log.info("Inside mailForSubmitRequestByEmp  method");
		} else if (!HRMSHelper.isNumber(travel.getTotalApproximateCost().toString())
				&& ETravelRequestStatus.PENDING.name().equals(travel.getRequestWF().getStatus())) {
			Employee approver = employeeDAO.findActiveEmployeeById(travel.getApproverId(), IHRMSConstants.isActive);
			placeHolderMap.put("{cost}", travel.getTotalApproximateCost().toString());
			placeHolderMap.put("{approverName}",
					approver.getCandidate().getFirstName() + " " + approver.getCandidate().getLastName());
		} else if (!HRMSHelper.isNumber(travel.getTotalApproximateCost().toString())
				&& ETravelRequestStatus.APPROVED.name().equals(travel.getRequestWF().getStatus())) {
			placeHolderMap.put("{status}", travel.getRequestWF().getStatus());
			placeHolderMap.put("{requestId}", travel.getId().toString());
			placeHolderMap.put("{salutation}", travel.getApprover().getCandidate().getTitle());
			placeHolderMap.put("{approverName}", travel.getApprover().getCandidate().getFirstName() + " "
					+ travel.getApprover().getCandidate().getLastName());
		} else if (!HRMSHelper.isNumber(travel.getTotalApproximateCost().toString())
				&& ETravelRequestStatus.REJECTED.name().equals(travel.getRequestWF().getStatus())) {
			placeHolderMap.put("{status}", travel.getRequestWF().getStatus());
			placeHolderMap.put("{requestId}", travel.getId().toString());
			placeHolderMap.put("{salutation}", travel.getApprover().getCandidate().getTitle());
			placeHolderMap.put("{approverName}", travel.getApprover().getCandidate().getFirstName() + " "
					+ travel.getApprover().getCandidate().getLastName());
		} else if (!HRMSHelper.isNumber(travel.getTotalApproximateCost().toString())
				&& ETravelRequestStatus.COMPLETED.name().equals(travel.getRequestWF().getStatus())) {
			placeHolderMap.put("{status}", IHRMSConstants.CONFIRMED);

		}

		String mailBody = HRMSHelper.replaceString(placeHolderMap, template.getTemplate());

		if (!HRMSHelper.isNullOrEmpty(ccMailId)) {
			emailsender.toPersistEmail(mailIds, ccMailId, mailBody, template.getEmailSubject(),
					employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
					employee.getCandidate().getLoginEntity().getOrganization().getId());

		} else {
			emailsender.toPersistEmail(mailIds, null, mailBody, template.getEmailSubject(),
					employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
					employee.getCandidate().getLoginEntity().getOrganization().getId());
		}

	}

	@Override
	public void downloadExpenseSummary(DownloadExpenseSummaryReqVO summeryReqVO, HttpServletResponse res)
			throws IOException, HRMSException {
		log.info("Inside downloadExpenseSummary method  ");
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		HttpHeaders header = new HttpHeaders();
		if (authorizationServiceImpl.isAuthorizedFunctionName("downloadExpenseSummaryForTD", role)) {
			// Input validation
			travelDeskAuthorityHelper.downloadExpenseSummaryInputValidation(summeryReqVO);
			Date fromDate = HRMSDateUtil.parse(summeryReqVO.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT);
			Date toDate = HRMSDateUtil.parse(summeryReqVO.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT);
			if (fromDate.compareTo(toDate) > 0) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1502));
			}
			// get Travel desk division
			List<MapTravelDeskApprover> mapApprover = mapApproverDAO.findApproverIdAndIsActive(loggedInEmpId,
					IHRMSConstants.isActive);
			List<Long> divIds = mapApprover.stream().map(MapTravelDeskApprover::getDivisionId)
					.collect(Collectors.toList());

			// get travel request by date
//			List<TravelRequestV2> travelRequests = travelRequestDAO.getTravelRequestByCreatedDate(
//					HRMSDateUtil.parse(summeryReqVO.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT),
//					HRMSDateUtil.parse(summeryReqVO.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT),
//					divIds.toArray());

			List<TDExpenceSummaryReport> travelRequest = tdExpenceSummaryDAO.getTravelRequestByCreatedDate(
					HRMSDateUtil.parse(summeryReqVO.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT),
					HRMSDateUtil.parse(summeryReqVO.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT),
					divIds.toArray());

			if (!HRMSHelper.isNullOrEmpty(travelRequest)) {
//				Workbook workbook = new ExpenseSummaryXLSGenerator().generateXlS(travelRequests);
				Workbook workbook = new XLSGenerator().generateXlS(travelRequest);

//				header.setContentType(new MediaType("application", "force-download"));
//				header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=TravelDeskExpenseSummary.xlsx");
				res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				res.setHeader("Content-Disposition", "attachment; filename=TravelDeskExpenseSummary.xlsx");
				workbook.write(res.getOutputStream());
				workbook.write(stream);
				workbook.close();
			} else {
				stream.close();
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
			}
		} else {
			stream.close();
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		log.info("Exit from downloadExpenseSummary method");

	}

	@Override
	public HRMSBaseResponse<ExpenseSummeryResponseVO> getTravelSummaryRequest(Long travelRequestId)
			throws HRMSException {

		log.info("Inside getTravelSummaryRequest method");
		HRMSBaseResponse<ExpenseSummeryResponseVO> response = new HRMSBaseResponse<>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		if (authorizationServiceImpl.isAuthorizedFunctionName("getTravelSummaryRequest", role)) {

			travelDeskAuthorityHelper.getTravelSummaryRequestInputValidation(travelRequestId);

			// Check if the travel request ID is valid
			TravelRequestV2 travelRequest = travelRequestDAO.findByIdAndIsActive(travelRequestId,
					IHRMSConstants.isActive);

			// Check if the travel request ID is valid
			if (!HRMSHelper.isNullOrEmpty(travelRequest)) {

//				// get Travel desk division
//				List<MapTravelDeskApprover> mapApprover = mapApproverDAO.findApproverIdAndIsActive(loggedInEmpId,
//						IHRMSConstants.isActive);
//
//				long divMatchCount = mapApprover.stream()
//						.filter(e -> e.getDivisionId().equals(travelRequest.getDivisionId())).count();
//
//				if (divMatchCount == 0) {
//					log.info("TD department does not match requester's department.");
//					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
//				}

				ExpenseSummeryResponseVO expenseSummaryResponse = new ExpenseSummeryResponseVO();
				expenseSummaryResponse.setTravelRequestId(travelRequest.getId());

				CabRequestV2 cabRequest = cabRequestDAO.findIdByIsActive(travelRequestId, IHRMSConstants.isActive);
				if (!HRMSHelper.isNullOrEmpty(cabRequest)) {
					CabExpenseVO cabExpenseVO = new CabExpenseVO();
					cabExpenseVO.setId(cabRequest.getId());
					cabExpenseVO.setActualDistance(cabRequest.getActualDistance());
					cabExpenseVO.setApproximateDistance(cabRequest.getApproximateDistance());
					cabExpenseVO.setApproximateTicketCost(cabRequest.getApproximateCost());
					cabExpenseVO.setFinalTicketCost(cabRequest.getFinalCost());
					cabExpenseVO.setRefundCost(cabRequest.getRefundCost());
					cabExpenseVO.setSetteledCost(cabRequest.getSettledCost());
					expenseSummaryResponse.setCabExpenseVO(cabExpenseVO);
				}

				AccommodationRequestV2 accommodationRequest = accommodationRequestDAO.findIdByIsActive(travelRequestId,
						IHRMSConstants.isActive);
				if (!HRMSHelper.isNullOrEmpty(accommodationRequest)) {
					ExpenseVO accommodationExpenseVO = new ExpenseVO();
					accommodationExpenseVO.setId(accommodationRequest.getId());
					accommodationExpenseVO.setApproximateTicketCost(accommodationRequest.getApproximateCost());
					accommodationExpenseVO.setFinalTicketCost(accommodationRequest.getFinalCost());
					accommodationExpenseVO.setRefundCost(accommodationRequest.getRefundCost());
					accommodationExpenseVO.setSetteledCost(accommodationRequest.getSettledCost());
					expenseSummaryResponse.setAccommodationExpenseVO(accommodationExpenseVO);
				}

				TravelRequestV2 travelRequestV2 = travelRequestDAO.findIdByIsActive(travelRequestId,
						IHRMSConstants.isActive);
				if (!HRMSHelper.isNullOrEmpty(travelRequestV2)) {
					ExpenseVO totalExpenseVO = new ExpenseVO();
					totalExpenseVO.setId(travelRequestV2.getId());
					totalExpenseVO.setApproximateTicketCost(travelRequestV2.getTotalApproximateCost());
					totalExpenseVO.setFinalTicketCost(travelRequestV2.getTotalFinalCost());
					totalExpenseVO.setRefundCost(travelRequestV2.getTotalRefundCost());
					totalExpenseVO.setSetteledCost(travelRequestV2.getTotalSettledCost());
					expenseSummaryResponse.setTotalExpense(totalExpenseVO);
				}

				List<TicketRequestV2> ticketRequests = ticketRequestDAO
						.findByTravelRequestIdAndIsActive(travelRequestId, IHRMSConstants.isActive);
				if (!HRMSHelper.isNullOrEmpty(ticketRequests)) {
					TicketExpenseVO ticketExpenseVO = new TicketExpenseVO();
					ExpenseVO airExpenseVO = null;
					ExpenseVO busExpenseVO = null;
					ExpenseVO trainExpenseVO = null;

					for (TicketRequestV2 ticketRequest : ticketRequests) {
						if (ticketRequest.getModeOfTravel().getModeOfTravel()
								.equalsIgnoreCase(EModeOfTravel.Air.name())) {
//							if (!HRMSHelper.isNullOrEmpty(airExpenseVO)) {
							airExpenseVO = new ExpenseVO();
							airExpenseVO.setId(ticketRequest.getId());
							airExpenseVO.setApproximateTicketCost(ticketRequest.getApproximateCost());
							airExpenseVO.setFinalTicketCost(ticketRequest.getFinalCost());
							airExpenseVO.setRefundCost(ticketRequest.getRefundCost());
							airExpenseVO.setSetteledCost(ticketRequest.getSettledCost());
//							}
						} else if (ticketRequest.getModeOfTravel().getModeOfTravel()
								.equalsIgnoreCase(EModeOfTravel.Bus.name())) {
//							if (!HRMSHelper.isNullOrEmpty(busExpenseVO)) {
							busExpenseVO = new ExpenseVO();
							busExpenseVO.setId(ticketRequest.getId());
							busExpenseVO.setApproximateTicketCost(ticketRequest.getApproximateCost());
							busExpenseVO.setFinalTicketCost(ticketRequest.getFinalCost());
							busExpenseVO.setRefundCost(ticketRequest.getRefundCost());
							busExpenseVO.setSetteledCost(ticketRequest.getSettledCost());
//							}
						} else if (ticketRequest.getModeOfTravel().getModeOfTravel()
								.equalsIgnoreCase(EModeOfTravel.Train.name())) {
//							if (!HRMSHelper.isNullOrEmpty(trainExpenseVO)) {
							trainExpenseVO = new ExpenseVO();
							trainExpenseVO.setId(ticketRequest.getId());
							trainExpenseVO.setApproximateTicketCost(ticketRequest.getApproximateCost());
							trainExpenseVO.setFinalTicketCost(ticketRequest.getFinalCost());
							trainExpenseVO.setRefundCost(ticketRequest.getRefundCost());
							trainExpenseVO.setSetteledCost(ticketRequest.getSettledCost());
//							}
						}
						ticketExpenseVO.setAirExpenseVO(airExpenseVO);
						ticketExpenseVO.setBusExpenseVO(busExpenseVO);
						ticketExpenseVO.setTrainExpenseVO(trainExpenseVO);
					}
					expenseSummaryResponse.setTicketExpense(ticketExpenseVO);
				}

				response.setResponseCode(1200);
				response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
				response.setResponseBody(expenseSummaryResponse);
				response.setApplicationVersion(applicationVersion);
			} else {
				log.info("Travel request does not exist for fetch expense summary.");
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1583));
			}

		} else {
			log.info("User is not authorized to get Travel Summary.");
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
		return response;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public HRMSBaseResponse<?> closeTravelRequest(Long travelRequestId) throws HRMSException {
		log.info("Inside closeTravelRequest method");
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		if (authorizationServiceImpl.isAuthorizedFunctionName("closeTravelRequest", role)) {
			TravelRequestV2 travelRequest = travelRequestDAO.findByIdAndIsActive(travelRequestId,
					ERecordStatus.Y.name());
			// get Travel desk division
			List<MapTravelDeskApprover> mapApprover = mapApproverDAO.findApproverIdAndIsActive(loggedInEmpId,
					IHRMSConstants.isActive);
			if (!HRMSHelper.isNullOrEmpty(travelRequest)) {
				// check div id match
				long divIdMatchCount = mapApprover.stream()
						.filter(e -> e.getDivisionId().equals(travelRequest.getDivisionId())).count();
				if (divIdMatchCount == 0) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
				}

				// get total refund and settled cost
				Float totalRefundCost = getTotalRefundCost(travelRequest);
				Float totalSettledCost = getTotalSettledCost(travelRequest);

				// check request type wise refund or setteled cost submitted or not.
				checkRefundOrSettledExpenseSubmitted(travelRequest);

				// change WF status
				if (totalRefundCost > 0 || totalSettledCost > 0) {
					TravelRequestWf travelRequestWf = travelRequestWfDAO
							.findTravelRequestIdByIsActive(travelRequest.getId(), ERecordStatus.Y.name());
					if (travelRequestWf.getStatus().equalsIgnoreCase(ETravelRequestStatus.SETTLED.name())) {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1587));
					}
					travelRequestWf.setStatus(ETravelRequestStatus.SETTLED.name());
					travelRequestWf.setPendingWith(ERole.TRAVEL_DESK.name());
					travelRequestWfDAO.save(travelRequestWf);

					response.setResponseCode(1200);
					response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
					response.setApplicationVersion(applicationVersion);
				} else {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
				}
			} else {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}

		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
		log.info("Exit from closeTravelRequest method");
		return response;
	}

	private void checkRefundOrSettledExpenseSubmitted(TravelRequestV2 travelRequest) throws HRMSException {

		if (travelRequest.getBookCab()) {
			CabRequestV2 cabRequest = cabRequestDAO.findByTravelRequestIdAndIsActive(travelRequest.getId(),
					ERecordStatus.Y.name());
			if (!HRMSHelper.isNullOrEmpty(cabRequest)) {
				if (!((!HRMSHelper.isNullOrEmpty(cabRequest.getRefundCost()) && cabRequest.getRefundCost() > 0)
						|| (!HRMSHelper.isNullOrEmpty(cabRequest.getSettledCost())
								&& cabRequest.getSettledCost() > 0))) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1586) + " for cab");
				}
			}
		}
		// check for accommodation
		if (travelRequest.getBookAccommodation()) {
			AccommodationRequestV2 accommodationRequest = accommodationRequestDAO
					.findByTravelRequestIdAndIsActive(travelRequest.getId(), ERecordStatus.Y.name());
			if (!HRMSHelper.isNullOrEmpty(accommodationRequest)) {
				if (!((!HRMSHelper.isNullOrEmpty(accommodationRequest.getRefundCost())
						&& accommodationRequest.getRefundCost() > 0)
						|| (!HRMSHelper.isNullOrEmpty(accommodationRequest.getSettledCost())
								&& accommodationRequest.getSettledCost() > 0))) {
					throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1586) + " for Accommodation");
				}
			}
		}
		// check for ticket
		if (travelRequest.getBookTicket()) {
			List<TicketRequestV2> ticketRequest = ticketRequestDAO
					.findByTravelRequestIdAndIsActive(travelRequest.getId(), ERecordStatus.Y.name());
			if (!HRMSHelper.isNullOrEmpty(ticketRequest)) {
				for (TicketRequestV2 ticketBook : ticketRequest) {
					if (!((!HRMSHelper.isNullOrEmpty(ticketBook.getRefundCost()) && ticketBook.getRefundCost() > 0)
							|| (!HRMSHelper.isNullOrEmpty(ticketBook.getSettledCost())
									&& ticketBook.getSettledCost() > 0))) {
						throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1586) + " for ticket");
					}
				}
			}

		}
	}

	private Float getTotalSettledCost(TravelRequestV2 travel) {
		log.info(" inside getTotalSettledCost method");
		Float totalSettledCost = 0F;
		CabRequestV2 cabRequest = cabRequestDAO.findByTravelRequestIdAndIsActive(travel.getId(),
				ERecordStatus.Y.name());
		AccommodationRequestV2 accommodationRequest = accommodationRequestDAO
				.findByTravelRequestIdAndIsActive(travel.getId(), ERecordStatus.Y.name());
		List<TicketRequestV2> ticketRequest = ticketRequestDAO.findByTravelRequestIdAndIsActive(travel.getId(),
				ERecordStatus.Y.name());
		// get ticket refund total cost
		if (!HRMSHelper.isNullOrEmpty(ticketRequest)) {
			for (TicketRequestV2 ticketBook : ticketRequest) {
				if (!HRMSHelper.isNullOrEmpty(ticketBook.getSettledCost())) {
					totalSettledCost = totalSettledCost + ticketBook.getSettledCost();
				}
			}
			log.info("Ticket Total setteled cost");
		}
		// get acc cost
		if (!HRMSHelper.isNullOrEmpty(accommodationRequest)) {
			if (!HRMSHelper.isNullOrEmpty(accommodationRequest.getSettledCost())) {
				totalSettledCost = totalSettledCost + accommodationRequest.getSettledCost();
			}
			log.info("accommodationRequest Total setteled cost");
		}
		// get cab cost
		if (!HRMSHelper.isNullOrEmpty(cabRequest)) {
			if (!HRMSHelper.isNullOrEmpty(cabRequest.getSettledCost())) {
				totalSettledCost = totalSettledCost + cabRequest.getSettledCost();
			}
			log.info("cab Total setteled cost");
		}
		log.info("Total Settled cost::" + totalSettledCost);
		log.info(" inside getTotalSettledCost method");
		return totalSettledCost;
	}

	private Float getTotalRefundCost(TravelRequestV2 travel) {
		log.info(" inside getTotalRefundCost method");
		Float totalRefundCost = 0F;
		CabRequestV2 cabRequest = cabRequestDAO.findByTravelRequestIdAndIsActive(travel.getId(),
				ERecordStatus.Y.name());
		AccommodationRequestV2 accommodationRequest = accommodationRequestDAO
				.findByTravelRequestIdAndIsActive(travel.getId(), ERecordStatus.Y.name());
		List<TicketRequestV2> ticketRequest = ticketRequestDAO.findByTravelRequestIdAndIsActive(travel.getId(),
				ERecordStatus.Y.name());
		// get ticket refund total cost
		if (!HRMSHelper.isNullOrEmpty(ticketRequest)) {
			for (TicketRequestV2 ticketBook : ticketRequest) {
				if (!HRMSHelper.isNullOrEmpty(ticketBook.getRefundCost())) {
					totalRefundCost = totalRefundCost + ticketBook.getRefundCost();
				}
			}
			log.info("Ticket Total refund cost");
		}
		// get acc cost
		if (!HRMSHelper.isNullOrEmpty(accommodationRequest)) {
			if (!HRMSHelper.isNullOrEmpty(accommodationRequest.getRefundCost())) {
				totalRefundCost = totalRefundCost + accommodationRequest.getRefundCost();
			}
			log.info("accommodationRequest Total refund cost");
		}
		// get cab cost
		if (!HRMSHelper.isNullOrEmpty(cabRequest)) {
			if (!HRMSHelper.isNullOrEmpty(cabRequest.getRefundCost())) {
				totalRefundCost = totalRefundCost + cabRequest.getRefundCost();
			}
			log.info("cab Total refund cost");
		}
		log.info("Total Refund cost::" + totalRefundCost);
		log.info(" inside getTotalRefundCost method");
		return totalRefundCost;
	}

	private void updateTotalCostsForTravelRequest(Long travelRequestId) throws HRMSException {
		TravelRequestV2 travelRequest = travelRequestDAO.findByIdAndIsActive(travelRequestId, IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(travelRequest)) {
			Float totalRefundCost = 0.0f;
			Float totalSettledCost = 0.0f;

			// Sum up refund and settled costs for ticket requests
			List<TicketRequestV2> ticketRequests = ticketRequestDAO.findIdByIsActive(travelRequestId,
					IHRMSConstants.isActive);
			for (TicketRequestV2 ticketRequest : ticketRequests) {
				if (!HRMSHelper.isNullOrEmpty(ticketRequest.getRefundCost())) {
					totalRefundCost += ticketRequest.getRefundCost();
				}
				if (!HRMSHelper.isNullOrEmpty(ticketRequest.getSettledCost())) {
					totalSettledCost += ticketRequest.getSettledCost();
				}
			}

			// Add refund and settled costs for cab request
			CabRequestV2 cabRequest = cabRequestDAO.findByTravelRequestIdAndIsActive(travelRequestId,
					IHRMSConstants.isActive);
			if (!HRMSHelper.isNullOrEmpty(cabRequest)) {
				if (!HRMSHelper.isNullOrEmpty(cabRequest.getRefundCost())) {
					totalRefundCost += cabRequest.getRefundCost();
				}
				if (!HRMSHelper.isNullOrEmpty(cabRequest.getSettledCost())) {
					totalSettledCost += cabRequest.getSettledCost();
				}
			}

			// Add refund and settled costs for accommodation request
			AccommodationRequestV2 accommodationRequest = accommodationRequestDAO
					.findByTravelRequestIdAndIsActive(travelRequestId, IHRMSConstants.isActive);
			if (!HRMSHelper.isNullOrEmpty(accommodationRequest)) {
				if (!HRMSHelper.isNullOrEmpty(accommodationRequest.getRefundCost())) {
					totalRefundCost += accommodationRequest.getRefundCost();
				}
				if (!HRMSHelper.isNullOrEmpty(accommodationRequest.getSettledCost())) {
					totalSettledCost += accommodationRequest.getSettledCost();
				}

			}

			// Set the total costs in the travel request
			travelRequest.setTotalRefundCost(totalRefundCost);
			travelRequest.setTotalSettledCost(totalSettledCost);

			// Save the updated travel request
			travelRequestDAO.save(travelRequest);
		} else {
			log.info("Travel request not found while updating total costs.");
			throw new HRMSException(1500, "Travel request not found while updating total costs.");
		}
	}

	// sending reminders to TD approvers for pending travel requests
	public void TDApprovalsReaminders() {
		log.info("Fetching pending travel requests for TD approvers...");
		List<Long> fetchPendingTravelRequests = travelRequestDAO.findPendingWithTD(ERole.TRAVEL_DESK_APPROVER.name(),
				ETravelRequestStatus.PENDING.name());

		if (!HRMSHelper.isNullOrEmpty(fetchPendingTravelRequests)) {
			for (Long id : fetchPendingTravelRequests) {
				sendReminderToTDApprover(id);
			}
		} else {
			log.info("No ending request");
		}
	}

	public void sendReminderToTDApprover(Long id) {
		log.info("Sending reminder to TD approver for travel request:::");

		Employee employee = employeeDAO.findActiveEmployeeById(id, IHRMSConstants.isActive);

		// Fetch the approver's email ID
		String approverEmailId = employee.getOfficialEmailId();
		String approverName = employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName();

		// Fetch the appropriate email template for the reminder
		EmailTemplate template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
				IHRMSConstants.TD_APPROVER_REMINDER_TEMPLATE_NAME, IHRMSConstants.isActive,
				employee.getCandidate().getCandidateProfessionalDetail().getDivision(),
				employee.getCandidate().getLoginEntity().getOrganization());

		if (!HRMSHelper.isNullOrEmpty(template)) {

			Map<String, String> placeHolderMap = new HashMap<>();
			placeHolderMap.put("{aproverName}", approverName);
//			placeHolderMap.put("{travelRequestId}", request.getId().toString());
//			placeHolderMap.put("{department}",
//					employee.getCandidate().getCandidateProfessionalDetail().getDepartment().getDepartmentName());
//			placeHolderMap.put("{bpmNo}", request.getBpmNumber().toString());
//			placeHolderMap.put("{reason}", request.getTravelReason());
//			placeHolderMap.put("{CreatedDate}",
//					HRMSDateUtil.format(request.getCreatedDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
//			placeHolderMap.put("{empName}",
//					employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName());
			placeHolderMap.put("{URL}", baseURL);

			// Replace placeholders in the email template
			String mailBody = HRMSHelper.replaceString(placeHolderMap, template.getTemplate());

			// Send the email reminder to the TD approver
			emailsender.toPersistEmail(approverEmailId, null, mailBody, template.getEmailSubject(),
					employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
					employee.getCandidate().getLoginEntity().getOrganization().getId());

			log.info("Reminder email sent successfully to TD approver:::");
		} else {
			// Handle the case where the email template is not found
			log.error("Email template not found for TD approver reminder.");
		}
	}

	public void sendReminderToTD() {
		log.info("Inside send Reminder To TD");

		// Find division IDs
		List<Long> divisionIds = travelRequestDAO.findDivisionIdsPendingWithTravelDeskAndCompleted(
				ERole.TRAVEL_DESK.name(), ETravelRequestStatus.COMPLETED.name());

		// Use division IDs to find TD approver IDs
		List<Long> tdApproverIds = mapApproverDAO.findApproverIdsByDivisionIds(divisionIds);

		if (!HRMSHelper.isNullOrEmpty(tdApproverIds)) {
			for (Long tdApproverId : tdApproverIds) {
				sendEmailReminderToTD(tdApproverId);
			}
		} else {
			log.info("No pending request.");
		}
	}

	public void sendEmailReminderToTD(Long tdApproverId) {
		log.info("Sending reminder to TD for travel request:::");

		Employee employee = employeeDAO.findActiveEmployeeById(tdApproverId, IHRMSConstants.isActive);

		// Fetch the approver's email ID
		String approverEmailId = employee.getOfficialEmailId();
		String approverName = employee.getCandidate().getFirstName() + " " + employee.getCandidate().getLastName();

		// Fetch the appropriate email template for the reminder
		EmailTemplate template = emailTemplateDAO.findBytemplateNameAndIsActiveAndDivisionAndOrganization(
				IHRMSConstants.TD_REMINDER_TEMPLATE_NAME, IHRMSConstants.isActive,
				employee.getCandidate().getCandidateProfessionalDetail().getDivision(),
				employee.getCandidate().getLoginEntity().getOrganization());

		if (!HRMSHelper.isNullOrEmpty(template)) {

			Map<String, String> placeHolderMap = new HashMap<>();
			placeHolderMap.put("{tdName}", approverName);
			placeHolderMap.put("{URL}", baseURL);

			// Replace placeholders in the email template
			String mailBody = HRMSHelper.replaceString(placeHolderMap, template.getTemplate());

			// Send the email reminder to the TD approver
			emailsender.toPersistEmail(approverEmailId, null, mailBody, template.getEmailSubject(),
					employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
					employee.getCandidate().getLoginEntity().getOrganization().getId());

			log.info("Reminder email sent successfully to TD :::");
		} else {
			// Handle the case where the email template is not found
			log.error("Email template not found for TD reminder.");
		}
	}

	private void getDocumentDetails(Long reqId, GetTravelRequestVO travelRequestVO) {
		List<TravelDocumentResponseVO> travelDocumentResponse = new ArrayList<>();

		if (!HRMSHelper.isNullOrEmpty(travelRequestVO.getCabDetails())) {
			List<TravelDocument> cabTravelDocuments = travelDocumentDAO.findByTravelRequestIdAndCabRequestIdAndIsActive(
					reqId, travelRequestVO.getCabDetails().getId(), IHRMSConstants.isActive);

			for (TravelDocument cabDocument : cabTravelDocuments) {
				TravelDocumentResponseVO cabTravelDocumentResponse = createDocumentResponse(cabDocument,
						ETravelDocumentFlow.CAB.name());
				travelDocumentResponse.add(cabTravelDocumentResponse);
			}

			if (HRMSHelper.isNullOrEmpty(cabTravelDocuments)) {
				travelDocumentResponse.add(
						createEmptyDocumentResponse(ETravelDocumentFlow.CAB.name(), ETicketType.APPROXIMATE.name()));
			}
		}

		if (!HRMSHelper.isNullOrEmpty(travelRequestVO.getAccommodationDetails())) {
			List<TravelDocument> accommodationTravelDocuments = travelDocumentDAO
					.findByTravelRequestIdAndAccommodationRequestIdAndIsActive(reqId,
							travelRequestVO.getAccommodationDetails().getId(), IHRMSConstants.isActive);

			for (TravelDocument accommodationDocument : accommodationTravelDocuments) {
				TravelDocumentResponseVO accommodationTravelDocumentResponse = createDocumentResponse(
						accommodationDocument, ETravelDocumentFlow.ACCOMMODATION.name());
				travelDocumentResponse.add(accommodationTravelDocumentResponse);
			}

			if (HRMSHelper.isNullOrEmpty(accommodationTravelDocuments)) {
				travelDocumentResponse.add(createEmptyDocumentResponse(ETravelDocumentFlow.ACCOMMODATION.name(),
						ETicketType.APPROXIMATE.name()));
			}
		}

		if (!HRMSHelper.isNullOrEmpty(travelRequestVO.getTicketDetails())) {
			List<TicketRequestV2> tickets = ticketRequestDAO.findByTravelRequestIdAndIsActive(reqId,
					IHRMSConstants.isActive);

			for (TicketRequestV2 ticketReq : tickets) {
				String modeOfTravel = ticketReq.getModeOfTravel().getModeOfTravel();

				List<TravelDocument> ticketTravelDocuments = travelDocumentDAO
						.findByTravelRequestIdAndTicketRequestIdAndIsActive(reqId, ticketReq.getId(),
								IHRMSConstants.isActive);

				for (TravelDocument ticketDocument : ticketTravelDocuments) {
					TravelDocumentResponseVO ticketTravelDocumentResponse = createDocumentResponse(ticketDocument,
							getTicketTravelType(modeOfTravel));
					travelDocumentResponse.add(ticketTravelDocumentResponse);
				}

				if (HRMSHelper.isNullOrEmpty(ticketTravelDocuments)) {
					travelDocumentResponse.add(createEmptyDocumentResponse(getTicketTravelType(modeOfTravel),
							ETicketType.APPROXIMATE.name()));
				}
			}
		}

		// Set the final list of travel document responses to the travel request VO
		travelRequestVO.setTravelDocumentDetails(travelDocumentResponse);
	}

	private TravelDocumentResponseVO createDocumentResponse(TravelDocument document, String travelType) {
		TravelDocumentResponseVO documentResponse = new TravelDocumentResponseVO();
		if (!HRMSHelper.isNullOrEmpty(document)) {
			documentResponse.setDocumentName(document.getFileName());
			documentResponse.setEntityId(document.getId());
			documentResponse.setTicketType(document.getTicketType());
		}
		documentResponse.setTravelType(travelType + "_" + document.getTicketType());
		return documentResponse;
	}

	private TravelDocumentResponseVO createEmptyDocumentResponse(String travelType, String ticketType) {
		TravelDocumentResponseVO emptyDocumentResponse = new TravelDocumentResponseVO();
		emptyDocumentResponse.setTravelType(travelType + "_" + ticketType);
		return emptyDocumentResponse;
	}

	private String getTicketTravelType(String modeOfTravel) {
		switch (modeOfTravel) {
		case "Air":
			return ETravelDocumentFlow.TICKET_AIR.name();
		case "Bus":
			return ETravelDocumentFlow.TICKET_BUS.name();
		case "Train":
			return ETravelDocumentFlow.TICKET_TRAIN.name();
		default:
			// Handle unknown mode of travel if needed
			return null;
		}
	}

	@Override
	public HRMSBaseResponse<TravelRequestStatusVO> getRequestStatus(Long travelRequestId) throws HRMSException {
		log.info("Inside get Travel Request Status method");
		HRMSBaseResponse<TravelRequestStatusVO> response = new HRMSBaseResponse<>();

		if (!HRMSHelper.isNullOrEmpty(travelRequestId)) {
			TravelRequestV2 travel = travelRequestDAO.findByIdAndIsActive(travelRequestId, IHRMSConstants.isActive);
			TravelRequestStatusVO travelRequestStatusVO = new TravelRequestStatusVO();
			if (!HRMSHelper.isNullOrEmpty(travel.getTotalApproximateCost()) && travel.getTotalApproximateCost() > 0
					&& !travel.getRequestWF().getStatus().equals(ETravelRequestStatus.INCOMPLETE.name())
					&& !travel.getRequestWF().getStatus().equals(ETravelRequestStatus.INPROCESS.name())) {
				travelRequestStatusVO.setIsTDApproxCostSubmitted(IHRMSConstants.isActive);
			} else {
				travelRequestStatusVO.setIsTDApproxCostSubmitted(IHRMSConstants.isNotActive);
			}

			if (!HRMSHelper.isNullOrEmpty(travel.getTotalFinalCost()) && travel.getTotalFinalCost() > 0
					&& !travel.getRequestWF().getStatus().equals(ETravelRequestStatus.INCOMPLETE.name())
					&& !travel.getRequestWF().getStatus().equals(ETravelRequestStatus.INPROCESS.name())
					&& !travel.getRequestWF().getStatus().equals(ETravelRequestStatus.PENDING.name())
					&& !travel.getRequestWF().getStatus().equals(ETravelRequestStatus.APPROVED.name())) {
				travelRequestStatusVO.setIsTDFinalCostSubmitted(IHRMSConstants.isActive);
			} else {
				travelRequestStatusVO.setIsTDFinalCostSubmitted(IHRMSConstants.isNotActive);
			}

			if (!travel.getRequestWF().getStatus().equals(ETravelRequestStatus.INCOMPLETE.name())
					&& !travel.getRequestWF().getStatus().equals(ETravelRequestStatus.INPROCESS.name())
					&& !travel.getRequestWF().getStatus().equals(ETravelRequestStatus.PENDING.name())) {
				travelRequestStatusVO.setIsTDApproverSubmitted(IHRMSConstants.isActive);
			} else {
				travelRequestStatusVO.setIsTDApproverSubmitted(IHRMSConstants.isNotActive);
			}

			response.setResponseBody(travelRequestStatusVO);
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setApplicationVersion(applicationVersion);
			log.info("Exit from get Request status By  request Id Method");
			return response;
		} else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

	}
}
