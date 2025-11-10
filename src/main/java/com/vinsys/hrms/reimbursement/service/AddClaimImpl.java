package com.vinsys.hrms.reimbursement.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.master.dao.IMasterClaimCategoryDAO;
import com.vinsys.hrms.master.dao.IMasterClaimTypeDAO;
import com.vinsys.hrms.master.dao.IMasterStayTypeDAO;
import com.vinsys.hrms.master.dao.IReimbursementTravelTypeDAO;
import com.vinsys.hrms.master.entity.MasterClaimCategory;
import com.vinsys.hrms.master.entity.MasterClaimType;
import com.vinsys.hrms.master.entity.MasterReimbursementTravelType;
import com.vinsys.hrms.master.entity.MasterStayType;
import com.vinsys.hrms.master.vo.ClaimTypeVO;
import com.vinsys.hrms.master.vo.ReimbursementTravelTypeVO;
import com.vinsys.hrms.master.vo.StayTypeVO;
import com.vinsys.hrms.reimbursement.dao.IAddClaimDAO;
import com.vinsys.hrms.reimbursement.entity.AddClaim;
import com.vinsys.hrms.reimbursement.vo.AddClaimRequestVO;
import com.vinsys.hrms.reimbursement.vo.AddClaimResponseVO;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.security.service.IAuthorizationService;
import com.vinsys.hrms.util.AddClaimAuthorityHelper;
import com.vinsys.hrms.util.EClaimCategory;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.LogConstants;
import com.vinsys.hrms.util.ResponseCode;

@Component
public class AddClaimImpl {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Value("${app_version}")
	private String applicationVersion;
	
	@Autowired
	AddClaimAuthorityHelper addClaimAuthorityHelper;

	public AddClaimImpl(@Autowired IAuthorizationService authorizationServiceImpl,
			@Autowired IAddClaimDAO addClaimDAO, @Autowired IMasterClaimTypeDAO masterClaimTypeDAO,
			@Autowired IMasterStayTypeDAO masterStayTypeDAO, @Autowired IMasterClaimCategoryDAO masterClaimCategoryDAO,
			@Autowired IReimbursementTravelTypeDAO masterTravelTypeDAO) {
		this.authorizationServiceImpl = authorizationServiceImpl;
		this.addClaimDAO = addClaimDAO;
		this.masterClaimTypeDAO = masterClaimTypeDAO;
		this.masterStayTypeDAO = masterStayTypeDAO;
		this.masterClaimCategoryDAO = masterClaimCategoryDAO;
		this.masterTravelTypeDAO = masterTravelTypeDAO;
	}

	IAuthorizationService authorizationServiceImpl;

	IAddClaimDAO addClaimDAO;

	IMasterClaimTypeDAO masterClaimTypeDAO;

	IMasterStayTypeDAO masterStayTypeDAO;

	IMasterClaimCategoryDAO masterClaimCategoryDAO;

	IReimbursementTravelTypeDAO masterTravelTypeDAO;

	@Transactional(rollbackFor = Exception.class)
	public HRMSBaseResponse<AddClaimResponseVO> addClaim(@RequestBody AddClaimRequestVO request) throws HRMSException {
		if (log.isInfoEnabled()) {
			log.info(LogConstants.ENTERED.template(), "addClaim");
		}
		AddClaimResponseVO addClaimResponse = new AddClaimResponseVO();
		if (isOperationAllowed()) {
			validateInput(request);
			AddClaim addClaim=new AddClaim();
			addClaim=addClaimRequestMethod(request);
			HRMSBaseResponse<AddClaimResponseVO> response = handleAndSetResponse(addClaimResponse,addClaim, 1200, 1218);
			if (log.isInfoEnabled()) {
				log.info(LogConstants.EXITED.template(), "addClaim");
			}
			return response;
		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
	}

	private boolean validateInput(AddClaimRequestVO request) throws HRMSException {
		if (!HRMSHelper.isNullOrEmpty(request) && !HRMSHelper.isNullOrEmpty(request.getRequestId())) {
			return true;
		}else {
			return false;}
	}

	private boolean isOperationAllowed() {
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		if (authorizationServiceImpl.isAuthorizedFunctionName("addClaimRequestMethod", role)) {
			return true;
		}else {
		return false;
		}
	}
	

	private HRMSBaseResponse<AddClaimResponseVO> handleAndSetResponse(AddClaimResponseVO addClaimResponseVO,AddClaim entity,
			int responeCode, int responseMessageCode) {
		HRMSBaseResponse<AddClaimResponseVO> response = new HRMSBaseResponse<>();
		addClaimResponseVO=setResponseVO(entity);
		response.setResponseCode(responeCode);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(responseMessageCode));
		response.setApplicationVersion(applicationVersion);
		response.setResponseBody(addClaimResponseVO);
		return response;
	}

	private AddClaim addClaimRequestMethod(AddClaimRequestVO request) throws HRMSException {
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		addClaimAuthorityHelper.addClaimInputValidations(request);
		AddClaim addClaimEntity = findOrCreateAndValidateClaimRequest(request, empId);
		updateClaim(request, addClaimEntity,empId);
		processClaim(request, addClaimEntity);
		return addClaimDAO.save(addClaimEntity);
	}

	private void processClaim(AddClaimRequestVO request, AddClaim addClaimEntity) {
		MasterClaimCategory claimCategory = masterClaimCategoryDAO
				.findByIdAndIsActive(request.getClaimType().getClaimCategory(), ERecordStatus.Y.name());
		if (claimCategory.getClaimCategory().equals(IHRMSConstants.TravelCalimType)) {
			handleTravelClaim(request, addClaimEntity);
		} else if (claimCategory.getClaimCategory().equals(EClaimCategory.Stay.name())) {
			handleStayClaim(request, addClaimEntity);
		} else if (claimCategory.getClaimCategory().equals(EClaimCategory.Food.name())) {
			handleFoodClaim(request, addClaimEntity);
		} 
	}

	private void handleFoodClaim(AddClaimRequestVO request, AddClaim addClaimEntity) {
		addClaimEntity.setStayLocation(request.getLocation());
	
	}

	/**
	 * @throws HRMSException 
	 * 
	 */
	protected void updateClaim(AddClaimRequestVO request, AddClaim addClaimEntity,Long empId) throws HRMSException {
		
		addClaimEntity.setCreatedDate(new Date());
		addClaimEntity.setCreatedBy(empId.toString());
		addClaimEntity.setIsActive(IHRMSConstants.isActive);
		addClaimEntity
				.setExpenseDate(HRMSDateUtil.parse(request.getExpenseDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		addClaimEntity.setExpenseAmount(request.getExpenseAmount());
		addClaimEntity.setDailyAllowance(request.getDailyAllowance());
		addClaimEntity.setRequestId(request.getRequestId());
		addClaimEntity.setRequesterComment(request.getComment());
		MasterClaimType claimType = masterClaimTypeDAO.findByIdAndIsActive(request.getClaimType().getId(),
				ERecordStatus.Y.name());
		if (!HRMSHelper.isNullOrEmpty(claimType)) {
			addClaimEntity.setClaimType(claimType);
		}
		}

	private AddClaim findOrCreateAndValidateClaimRequest(AddClaimRequestVO request, Long empId) {
		AddClaim addClaimEntity;
		if (!HRMSHelper.isNullOrEmpty(request.getId())) {
			addClaimEntity = addClaimDAO.findByIdAndIsActive(request.getId(), IHRMSConstants.isActive);
			if (!HRMSHelper.isNullOrEmpty(addClaimEntity)) {
				addClaimEntity.setUpdatedBy(empId.toString());
				addClaimEntity.setUpdatedDate(new Date());
			} else {
				addClaimEntity = new AddClaim();
			}
		} else {
			addClaimEntity = new AddClaim();
		}
		return addClaimEntity;
	}

	private void handleStayClaim(AddClaimRequestVO request, AddClaim addClaimEntity) {
		addClaimEntity.setFromDate(HRMSDateUtil.parse(request.getStayFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		addClaimEntity.setToDate(HRMSDateUtil.parse(request.getStayToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		addClaimEntity.setStayLocation(request.getStayLocation());
		MasterStayType stayType = masterStayTypeDAO.findByIdAndIsActive(request.getStayType().getId(),
				ERecordStatus.Y.name());
		if (!HRMSHelper.isNullOrEmpty(stayType)) {
			addClaimEntity.setStayType(stayType);
			if (request.getStayType().getId() == 1) {
				addClaimEntity.setHotelName(request.getHotelName());
				addClaimEntity.setRoomNo(request.getRoomNo());
			}
		}

		if (request.getDailyAllowance().equals(IHRMSConstants.isActive)) {
			int numberOfDays = HRMSHelper.calculateNumberOfDays(request.getStayFromDate(), request.getStayToDate());
			addClaimEntity.setNumberOfDays(numberOfDays);
		}
	}

	private void handleTravelClaim(AddClaimRequestVO request, AddClaim addClaimEntity) {
		MasterReimbursementTravelType travelType = masterTravelTypeDAO
				.findByIdAndIsActive(request.getTravelType().getId(), ERecordStatus.Y.name());
		if (!HRMSHelper.isNullOrEmpty(travelType)) {
			addClaimEntity.setTravelType(travelType);
		}
		addClaimEntity.setFromLocation(request.getTravelFromLocation());
		addClaimEntity.setToLocation(request.getTravelToLocation());
		addClaimEntity
				.setFromDate(HRMSDateUtil.parse(request.getTravelFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		addClaimEntity.setToDate(HRMSDateUtil.parse(request.getTravelToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		if (request.getDailyAllowance().equals(IHRMSConstants.isActive)) {
			int numberOfDays = HRMSHelper.calculateNumberOfDays(request.getTravelFromDate(), request.getTravelToDate());
			addClaimEntity.setNumberOfDays(numberOfDays);
		} 
	}
	private AddClaimResponseVO setResponseVO(AddClaim entity) {
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		AddClaimResponseVO response=new AddClaimResponseVO();
		response.setId(entity.getId());
		response.setRequestId(entity.getRequestId());
		response.setExpenseDate(HRMSDateUtil.format(entity.getExpenseDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		response.setExpenseAmount(entity.getExpenseAmount());
		
		if(!HRMSHelper.isNullOrEmpty(entity.getClaimType().getId())) {
			ClaimTypeVO claimType= new ClaimTypeVO();
			claimType.setId(entity.getClaimType().getId());
			claimType.setClaimType(entity.getClaimType().getClaimType());
			claimType.setClaimCategory(entity.getClaimType().getClaimCategory());
			claimType.setDescription(entity.getClaimType().getDescription());
			response.setClaimType(claimType);
		}
		
		response.setComment(entity.getRequesterComment());
		response.setDailyAllowance(entity.getDailyAllowance());
		if(!HRMSHelper.isNullOrEmpty(entity.getTravelType())) {
			ReimbursementTravelTypeVO travelType= new ReimbursementTravelTypeVO();
			travelType.setId(entity.getTravelType().getId());
			travelType.setTravelType(entity.getTravelType().getTravelType());
			response.setTravelType(travelType);
		}
		response.setTravelFromLocation(entity.getFromLocation());
		response.setTravelToLocation(entity.getToLocation());
		response.setTravelFromDate(HRMSDateUtil.format(entity.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		response.setTravelToDate(HRMSDateUtil.format(entity.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		response.setNoOfDays(0);
		response.setStayFromDate(HRMSDateUtil.format(entity.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		response.setStayToDate(HRMSDateUtil.format(entity.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		response.setStayLocation(entity.getStayLocation());
		if(!HRMSHelper.isNullOrEmpty(entity.getStayType())) {
			StayTypeVO stayType= new StayTypeVO();
			stayType.setId(entity.getStayType().getId());
			stayType.setStayType(entity.getStayType().getStayType());
			response.setStayType(stayType);
		}
		response.setRoomNo(entity.getRoomNo());
		//need to change
		response.setLocation(entity.getStayLocation());
		response.setHotelName(entity.getHotelName());
		
		
		
		return response;
	}
	public static AddClaimRequestVO createSampleAddClaimRequest() {
		AddClaimRequestVO request = new AddClaimRequestVO();
		request.setId(0L);
		request.setRequestId(1L);
		request.setClaimType(createSampleClaimType());
		request.setExpenseDate("20-04-2024");
		request.setDailyAllowance("N");
		request.setComment("xyz");
		request.setExpenseAmount(500F);

		return request;
	}

	private static ClaimTypeVO createSampleClaimType() {
		ClaimTypeVO claimType = new ClaimTypeVO();
		claimType.setId(2L);
		claimType.setClaimCategory(4L);
		claimType.setClaimType("Medical");
		claimType.setDescription("Medical expenses");

		return claimType;
	}


}
