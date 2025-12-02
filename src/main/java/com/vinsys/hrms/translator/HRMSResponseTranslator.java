package com.vinsys.hrms.translator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vinsys.hrms.datamodel.VOCandidate;
import com.vinsys.hrms.datamodel.VOCandidateActivity;
import com.vinsys.hrms.datamodel.VOCandidateActivityActionDetail;
import com.vinsys.hrms.datamodel.VOCandidateActivityLetterMapping;
import com.vinsys.hrms.datamodel.VOCandidateEmergencyContact;
import com.vinsys.hrms.datamodel.VOCandidateEmergencyContactAddress;
import com.vinsys.hrms.datamodel.VOCandidateFamilyAddress;
import com.vinsys.hrms.datamodel.VOCandidateFamilyDetail;
import com.vinsys.hrms.datamodel.VOCandidateHealthReport;
import com.vinsys.hrms.datamodel.VOCandidateLanguage;
import com.vinsys.hrms.datamodel.VOCandidateLetter;
import com.vinsys.hrms.datamodel.VOCandidatePassportDetail;
import com.vinsys.hrms.datamodel.VOCandidatePersonalDetail;
import com.vinsys.hrms.datamodel.VOCandidatePolicyDetail;
import com.vinsys.hrms.datamodel.VOCandidateProfessionalDetail;
import com.vinsys.hrms.datamodel.VOCandidateStatutoryNomination;
import com.vinsys.hrms.datamodel.VOCandidateVisaDetail;
import com.vinsys.hrms.datamodel.VOEmployee;
import com.vinsys.hrms.datamodel.VOEmployeeGrantLeaveDetail;
import com.vinsys.hrms.datamodel.VOEmployeeLeaveDetail;
import com.vinsys.hrms.datamodel.VOEmployeeNameIdResponse;
import com.vinsys.hrms.datamodel.VOEmployeeReportingManager;
import com.vinsys.hrms.datamodel.VOFeedbackOption;
import com.vinsys.hrms.datamodel.VOFeedbackQuestion;
import com.vinsys.hrms.datamodel.VOHolidayResponse;
import com.vinsys.hrms.datamodel.VOLoginEntity;
import com.vinsys.hrms.datamodel.VOLoginEntityType;
import com.vinsys.hrms.datamodel.VOMasterBranch;
import com.vinsys.hrms.datamodel.VOMasterCandidateActivity;
import com.vinsys.hrms.datamodel.VOMasterCandidateActivityActionType;
import com.vinsys.hrms.datamodel.VOMasterCandidateChecklistAction;
import com.vinsys.hrms.datamodel.VOMasterCity;
import com.vinsys.hrms.datamodel.VOMasterCountry;
import com.vinsys.hrms.datamodel.VOMasterDepartment;
import com.vinsys.hrms.datamodel.VOMasterDesignation;
import com.vinsys.hrms.datamodel.VOMasterDivision;
import com.vinsys.hrms.datamodel.VOMasterEmploymentType;
import com.vinsys.hrms.datamodel.VOMasterLeaveType;
import com.vinsys.hrms.datamodel.VOMasterOnboardActionReason;
import com.vinsys.hrms.datamodel.VOMasterRole;
import com.vinsys.hrms.datamodel.VOMasterState;
import com.vinsys.hrms.datamodel.VOOrgDivSeparationLetter;
import com.vinsys.hrms.datamodel.VOOrganization;
import com.vinsys.hrms.datamodel.VOOrganizationAddress;
import com.vinsys.hrms.datamodel.VOSubmittedEmployeeFeedback;
import com.vinsys.hrms.datamodel.VOUserFeedback;
import com.vinsys.hrms.datamodel.attendance.VOAttendanceProcessedData;
import com.vinsys.hrms.datamodel.attendance.VOAttendanceRawData;
import com.vinsys.hrms.datamodel.attendance.VOAttendanceSwapData;
import com.vinsys.hrms.datamodel.traveldesk.VOMapCabDriverVehicle;
import com.vinsys.hrms.datamodel.traveldesk.VOMasterDriver;
import com.vinsys.hrms.datamodel.traveldesk.VOMasterModeOfTravel;
import com.vinsys.hrms.datamodel.traveldesk.VOMasterVehicle;
import com.vinsys.hrms.datamodel.traveldesk.VOReportAccommodationRequest;
import com.vinsys.hrms.datamodel.traveldesk.VOReportCabRequest;
import com.vinsys.hrms.datamodel.traveldesk.VOReportTicketRequest;
import com.vinsys.hrms.datamodel.traveldesk.VOTicketRequest;
import com.vinsys.hrms.datamodel.traveldesk.VOTravelDeskDocument;
import com.vinsys.hrms.datamodel.traveldesk.VOTraveldeskApprover;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateActivity;
import com.vinsys.hrms.entity.CandidateActivityActionDetail;
import com.vinsys.hrms.entity.CandidateEmergencyContact;
import com.vinsys.hrms.entity.CandidateFamilyDetail;
import com.vinsys.hrms.entity.CandidateHealthReport;
import com.vinsys.hrms.entity.CandidateLanguage;
import com.vinsys.hrms.entity.CandidateLetter;
import com.vinsys.hrms.entity.CandidatePassportDetail;
import com.vinsys.hrms.entity.CandidatePolicyDetail;
import com.vinsys.hrms.entity.CandidateProfessionalDetail;
import com.vinsys.hrms.entity.CandidateStatutoryNomination;
import com.vinsys.hrms.entity.CandidateVisaDetail;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeFeedback;
import com.vinsys.hrms.entity.EmployeeGrantLeaveDetail;
import com.vinsys.hrms.entity.EmployeeLeaveDetail;
import com.vinsys.hrms.entity.FeedbackOption;
import com.vinsys.hrms.entity.FeedbackQuestion;
import com.vinsys.hrms.entity.LoginEntity;
import com.vinsys.hrms.entity.LoginEntityType;
import com.vinsys.hrms.entity.MapOrgDivSeparationLetter;
import com.vinsys.hrms.entity.MasterBranch;
import com.vinsys.hrms.entity.MasterCandidateActivity;
import com.vinsys.hrms.entity.MasterCandidateActivityActionType;
import com.vinsys.hrms.entity.MasterCandidateChecklistAction;
import com.vinsys.hrms.entity.MasterCandidateOnboardActionReason;
import com.vinsys.hrms.entity.MasterCity;
import com.vinsys.hrms.entity.MasterCountry;
import com.vinsys.hrms.entity.MasterDepartment;
import com.vinsys.hrms.entity.MasterDesignation;
import com.vinsys.hrms.entity.MasterDivision;
import com.vinsys.hrms.entity.MasterEmploymentType;
import com.vinsys.hrms.entity.MasterLeaveType;
import com.vinsys.hrms.entity.MasterState;
import com.vinsys.hrms.entity.OrganizationHoliday;
import com.vinsys.hrms.entity.attendance.AttendanceCSVData;
import com.vinsys.hrms.entity.attendance.AttendanceProcessedData;
import com.vinsys.hrms.entity.attendance.AttendanceRawData;
import com.vinsys.hrms.entity.traveldesk.BPMDetails;
import com.vinsys.hrms.entity.traveldesk.MapCabDriverVehicle;
import com.vinsys.hrms.entity.traveldesk.MasterDriver;
import com.vinsys.hrms.entity.traveldesk.MasterTraveldeskApprover;
import com.vinsys.hrms.entity.traveldesk.MasterVehicle;
import com.vinsys.hrms.entity.traveldesk.ReportAccommodationRequest;
import com.vinsys.hrms.entity.traveldesk.ReportCabRequest;
import com.vinsys.hrms.entity.traveldesk.ReportTicketRequest;
import com.vinsys.hrms.entity.traveldesk.TicketRequest;
import com.vinsys.hrms.entity.traveldesk.TraveldeskDocument;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

public class HRMSResponseTranslator {

	private static final Logger logger = LoggerFactory.getLogger(HRMSResponseTranslator.class);

	public static List<VOCandidateFamilyDetail> convertToVoCandidateFamilyDetailList(
			List<CandidateFamilyDetail> candidateFamilyDetailsEntityList) {
		List<VOCandidateFamilyDetail> voCandidateFamilyDetailList = new ArrayList<VOCandidateFamilyDetail>();

		for (CandidateFamilyDetail candidateFamilyDetailEntity : candidateFamilyDetailsEntityList) {
			VOCandidateFamilyDetail voCandidateFamilyDetail = new VOCandidateFamilyDetail();
			VOCandidateFamilyAddress voCandidateFamilyAddress = new VOCandidateFamilyAddress();

			voCandidateFamilyDetail.setContactNo1(candidateFamilyDetailEntity.getContactNo1());
			voCandidateFamilyDetail.setContactNo2(candidateFamilyDetailEntity.getContactNo2());
			voCandidateFamilyDetail.setCreatedBy(candidateFamilyDetailEntity.getCreatedBy());
			voCandidateFamilyDetail.setCreatedDate(candidateFamilyDetailEntity.getCreatedDate());
			voCandidateFamilyDetail.setDateOfBirth(candidateFamilyDetailEntity.getDateOfBirth());
			voCandidateFamilyDetail.setDependent(candidateFamilyDetailEntity.getDependent());
			voCandidateFamilyDetail
					.setDependentSeverelyDisabled(candidateFamilyDetailEntity.getDependentSeverelyDisabled());
			voCandidateFamilyDetail.setGender(candidateFamilyDetailEntity.getGender());
			voCandidateFamilyDetail.setId(candidateFamilyDetailEntity.getId());
			voCandidateFamilyDetail.setIsActive(candidateFamilyDetailEntity.getIsActive());
			voCandidateFamilyDetail.setFirst_name(candidateFamilyDetailEntity.getFirst_name());
			voCandidateFamilyDetail.setMiddle_name(candidateFamilyDetailEntity.getMiddle_name());
			voCandidateFamilyDetail.setLast_name(candidateFamilyDetailEntity.getLast_name());

			voCandidateFamilyDetail.setOccupation(candidateFamilyDetailEntity.getOccupation());
			voCandidateFamilyDetail.setRelationship(candidateFamilyDetailEntity.getRelationship());
			voCandidateFamilyDetail.setRemark(candidateFamilyDetailEntity.getRemark());
			voCandidateFamilyDetail.setUpdatedBy(candidateFamilyDetailEntity.getUpdatedBy());
			voCandidateFamilyDetail.setUpdatedDate(candidateFamilyDetailEntity.getUpdatedDate());
			voCandidateFamilyDetail.setWorking(candidateFamilyDetailEntity.getWorking());

			voCandidateFamilyAddress
					.setAddressLine1(candidateFamilyDetailEntity.getCandidateFamilyAddress().getAddressLine1());
			voCandidateFamilyAddress
					.setAddressLine2(candidateFamilyDetailEntity.getCandidateFamilyAddress().getAddressLine2());

			voCandidateFamilyAddress.setCity(
					translateToMasterCityVO(candidateFamilyDetailEntity.getCandidateFamilyAddress().getCity()));
			voCandidateFamilyAddress
					.setCreatedBy(candidateFamilyDetailEntity.getCandidateFamilyAddress().getCreatedBy());
			voCandidateFamilyAddress
					.setCreatedDate(candidateFamilyDetailEntity.getCandidateFamilyAddress().getCreatedDate());
			voCandidateFamilyAddress.setId(candidateFamilyDetailEntity.getCandidateFamilyAddress().getId());
			voCandidateFamilyAddress.setIsActive(candidateFamilyDetailEntity.getCandidateFamilyAddress().getIsActive());
			voCandidateFamilyAddress.setPincode(candidateFamilyDetailEntity.getCandidateFamilyAddress().getPincode());
			voCandidateFamilyAddress.setRemark(candidateFamilyDetailEntity.getCandidateFamilyAddress().getRemark());
			voCandidateFamilyAddress.setState(
					translateToMasterStateVO(candidateFamilyDetailEntity.getCandidateFamilyAddress().getState()));
			voCandidateFamilyAddress
					.setUpdatedBy(candidateFamilyDetailEntity.getCandidateFamilyAddress().getUpdatedBy());
			voCandidateFamilyAddress
					.setUpdatedDate(candidateFamilyDetailEntity.getCandidateFamilyAddress().getUpdatedDate());

			voCandidateFamilyDetail.setCandidateFamilyAddress(voCandidateFamilyAddress);

			voCandidateFamilyDetailList.add(voCandidateFamilyDetail);
		}

		return voCandidateFamilyDetailList;
	}

	public static VOLoginEntity translateToLoginResponse(LoginEntity entity) {

		VOLoginEntity vologinResponse = new VOLoginEntity();
		/*
		 * changed by ssw on 01jan2018
		 */
		// VOLoginEntityType vologinEntityType = new VOLoginEntityType();
		Set<VOLoginEntityType> voLoginEntityTypes = new HashSet<VOLoginEntityType>();
		/*
		 * upto to this changed
		 */

		VOOrganizationAddress voOrganizationAddres = new VOOrganizationAddress();

		VOOrganization voOrg = new VOOrganization();

		if (entity.getOrganization() != null) {

			voOrg.setId(entity.getOrganization().getId());
			voOrg.setOrganizationName(entity.getOrganization().getOrganizationName());

		}

		if (entity.getOrganization().getOrganizationAddress() != null) {

			voOrganizationAddres.setAddressLine1(entity.getOrganization().getOrganizationAddress().getAddressLine1());
			voOrganizationAddres.setAddressLine2(entity.getOrganization().getOrganizationAddress().getAddressLine2());
			voOrganizationAddres
					.setCity(translateToMasterCityVO(entity.getOrganization().getOrganizationAddress().getCity()));
			voOrganizationAddres.setPincode(entity.getOrganization().getOrganizationAddress().getPincode());
			voOrganizationAddres
					.setState(translateToMasterStateVO(entity.getOrganization().getOrganizationAddress().getState()));
			voOrg.setOrganizationAddress(voOrganizationAddres);

		}
		/*
		 * changed by ssw on 01jan2018 for : manyToMany employee loginEntityType mapping
		 */
		// VOMasterRole role = new VOMasterRole();
		// role.setId(entity.getLoginEntityType().getRole().getId());
		// role.setRoleName(entity.getLoginEntityType().getRole().getRoleName());

		// vologinEntityType.setId(entity.getLoginEntityType().getId());
		// vologinEntityType.setLoginEntityTypeName(entity.getLoginEntityType().getLoginEntityTypeName());
		// vologinEntityType.setRole(role);

		// vologinResponse.setLoginEntityType(vologinEntityType);

		for (LoginEntityType loginEntityTypeLooped : entity.getLoginEntityTypes()) {
			if (!HRMSHelper.isNullOrEmpty(loginEntityTypeLooped)) {
				VOMasterRole role = new VOMasterRole();
				role.setId(loginEntityTypeLooped.getRole().getId());
				role.setRoleName(loginEntityTypeLooped.getRole().getRoleName());
				VOLoginEntityType vologinEntityType = new VOLoginEntityType();
				vologinEntityType.setId(loginEntityTypeLooped.getId());
				vologinEntityType.setLoginEntityTypeName(loginEntityTypeLooped.getLoginEntityTypeName());
				vologinEntityType.setRole(role);
				voLoginEntityTypes.add(vologinEntityType);
			}
		}
		vologinResponse.setLoginEntityTypes(voLoginEntityTypes);

		/*
		 * upto this changed by ssw on 01jan2018
		 */
		vologinResponse.setOrganization(voOrg);

		vologinResponse.setUsername(entity.getUsername());
		vologinResponse.setSuperUser(entity.getSuperUser());
		vologinResponse.setPrimaryEmail(entity.getPrimaryEmail());
		// vologinResponse.setPassword(password);
		vologinResponse.setNoOrg(entity.getNoOrg());
		vologinResponse.setLoginEntityName(entity.getLoginEntityName());
		vologinResponse.setIsFirstLogin(entity.getIsFirstLogin());
		// vologinResponse.setCandidate(null);

		// vologinEntityType.setRole(role);
		return vologinResponse;
	}

	/**
	 * This method will return candidate with basic details,it doesn't return the
	 * entire objects in the response,used for candidate login
	 */
	public static VOCandidate translateToCandidateBriefModle(Candidate candidate) {

		VOCandidate voCandidate = new VOCandidate();

		/*
		 * Setting up basic candidate details
		 */
		voCandidate.setId(candidate.getId());
		voCandidate.setFirstName(candidate.getFirstName());
		voCandidate.setLastName(candidate.getLastName());
		voCandidate.setMiddleName(candidate.getMiddleName());
		voCandidate.setEmailId(candidate.getEmailId());
		voCandidate.setGender(candidate.getGender());
		voCandidate.setTitle(candidate.getTitle());
		voCandidate.setDateOfBirth(candidate.getDateOfBirth());
		voCandidate.setCandidateStatus(candidate.getCandidateStatus());
		voCandidate.setMobileNumber(candidate.getMobileNumber()); // added to show the candidate primary phone no.
																	// visible to the HRMS dashboard

		Set<CandidateLetter> candidateLetterEntitySet = candidate.getLetters();
		Set<VOCandidateLetter> candidateLetterModelSet = new HashSet<VOCandidateLetter>();

		for (CandidateLetter candidateLetter : candidateLetterEntitySet) {
			VOCandidateLetter letterModel = HRMSEntityToModelMapper.convertToCandidateLetterModel(candidateLetter);
			VOCandidateActivityLetterMapping letterActivityMappingModel = HRMSEntityToModelMapper
					.convertToCandidateActivityLetterMappingModel(candidateLetter.getCandidateActivityLetterMapping());
			letterModel.setCandidateActivityLetterMapping(letterActivityMappingModel);
			candidateLetterModelSet.add(letterModel);

		}

		voCandidate.setLetters(candidateLetterModelSet);
		VOEmployee employee = HRMSEntityToModelMapper.convertToEmployeeModel(candidate.getEmployee());

		/*
		 * Setting up candidate Personal Details.
		 */
		VOCandidatePersonalDetail personalDetails = HRMSEntityToModelMapper
				.convertToCandidatePersonalDetailsDetailModel(candidate.getCandidatePersonalDetail());

		/*
		 * Setting up candidate Professional Details.
		 */
		VOCandidateProfessionalDetail profDetails = HRMSEntityToModelMapper
				.convertToCandidateProfessionalDetailsDetailModel(candidate.getCandidateProfessionalDetail());

		/*
		 * Setting up candidate letters.
		 */
		Set<CandidateLetter> candidateLetterEntity = candidate.getLetters();
		Set<VOCandidateLetter> candidateLetterModel = new HashSet<VOCandidateLetter>();
		for (CandidateLetter letterEntity : candidateLetterEntity) {
			VOCandidateLetter candidateLetter = HRMSEntityToModelMapper.convertToCandidateLetterModel(letterEntity);
			candidateLetterModel.add(candidateLetter);
		}
		voCandidate.setCandidateEmploymentStatus(candidate.getCandidateEmploymentStatus());
		voCandidate.setCandidatePersonalDetail(personalDetails);
		voCandidate.setLetters(candidateLetterModel);
		voCandidate.setCandidateProfessionalDetail(profDetails);
		voCandidate.setEmployee(employee);
		return voCandidate;
	}

	public static VOCandidateHealthReport translateToCandidateHealthReportResponse(
			CandidateHealthReport candidateHealthReportEntity) {
		VOCandidateHealthReport voCandidateHealthReport = new VOCandidateHealthReport();
		VOCandidatePersonalDetail voCandidatePersonalDetail = new VOCandidatePersonalDetail();
		voCandidatePersonalDetail.setId(candidateHealthReportEntity.getCandidatePersonalDetail().getId());

		voCandidateHealthReport.setAllergy(candidateHealthReportEntity.getAllergy());
		voCandidateHealthReport.setBloodGroup(candidateHealthReportEntity.getBloodGroup());
		voCandidateHealthReport.setCandidatePersonalDetail(voCandidatePersonalDetail);
		voCandidateHealthReport.setHospitalization(candidateHealthReportEntity.getHospitalization());
		voCandidateHealthReport.setId(candidateHealthReportEntity.getId());
		voCandidateHealthReport.setIdentificationMark(candidateHealthReportEntity.getIdentificationMark());
		voCandidateHealthReport.setInterestedToDonateBlood(candidateHealthReportEntity.getInterestedToDonateBlood());
		voCandidateHealthReport.setIsActive(candidateHealthReportEntity.getIsActive());
		voCandidateHealthReport.setPhysicallyHandicapped(candidateHealthReportEntity.getPhysicallyHandicapped());
		voCandidateHealthReport.setRemark(candidateHealthReportEntity.getRemark());
		voCandidateHealthReport.setSeverelyHandicapped(candidateHealthReportEntity.getSeverelyHandicapped());
		voCandidateHealthReport.setSurgery(candidateHealthReportEntity.getSurgery());
		voCandidateHealthReport.setVisionProblem(candidateHealthReportEntity.getVisionProblem());
		voCandidateHealthReport.setHealthHistory(candidateHealthReportEntity.getHealthHistory());

		return voCandidateHealthReport;
	}

	public static List<VOCandidateEmergencyContact> convertToVoCandidateEmergencyContactDetailList(
			List<CandidateEmergencyContact> candidateEmergencyContactEntityList) {
		List<VOCandidateEmergencyContact> voVOCandidateEmergencyContactList = new ArrayList<VOCandidateEmergencyContact>();

		for (CandidateEmergencyContact candidateEmergencyContactEntity : candidateEmergencyContactEntityList) {
			VOCandidateEmergencyContact voCandidateEmergencyContact = new VOCandidateEmergencyContact();
			voCandidateEmergencyContact.setCreatedBy(candidateEmergencyContactEntity.getCreatedBy());
			voCandidateEmergencyContact.setCreatedDate(candidateEmergencyContactEntity.getCreatedDate());
			voCandidateEmergencyContact.setId(candidateEmergencyContactEntity.getId());
			voCandidateEmergencyContact.setIsActive(candidateEmergencyContactEntity.getIsActive());
			voCandidateEmergencyContact.setLandlineNumber(candidateEmergencyContactEntity.getLandlineNumber());
			voCandidateEmergencyContact.setMobileNumber(candidateEmergencyContactEntity.getMobileNumber());
			voCandidateEmergencyContact.setFirstname(candidateEmergencyContactEntity.getFirstname());
			voCandidateEmergencyContact.setMiddlename(candidateEmergencyContactEntity.getMiddlename());
			voCandidateEmergencyContact.setLastname(candidateEmergencyContactEntity.getLastname());
			voCandidateEmergencyContact.setRelationship(candidateEmergencyContactEntity.getRelationship());
			voCandidateEmergencyContact.setRemark(candidateEmergencyContactEntity.getRemark());
			voCandidateEmergencyContact.setUpdatedBy(candidateEmergencyContactEntity.getUpdatedBy());
			voCandidateEmergencyContact.setUpdatedDate(candidateEmergencyContactEntity.getUpdatedDate());

			VOCandidateEmergencyContactAddress vocandidateEmergencyContactAddress = new VOCandidateEmergencyContactAddress();
			vocandidateEmergencyContactAddress.setAddressLine1(
					candidateEmergencyContactEntity.getCandidateEmergencyContactAddress().getAddressLine1());
			vocandidateEmergencyContactAddress.setAddressLine2(
					candidateEmergencyContactEntity.getCandidateEmergencyContactAddress().getAddressLine2());
			vocandidateEmergencyContactAddress.setCity(translateToMasterCityVO(
					candidateEmergencyContactEntity.getCandidateEmergencyContactAddress().getCity()));
			vocandidateEmergencyContactAddress
					.setCreatedBy(candidateEmergencyContactEntity.getCandidateEmergencyContactAddress().getCreatedBy());
			vocandidateEmergencyContactAddress.setCreatedDate(
					candidateEmergencyContactEntity.getCandidateEmergencyContactAddress().getCreatedDate());
			vocandidateEmergencyContactAddress
					.setId(candidateEmergencyContactEntity.getCandidateEmergencyContactAddress().getId());
			vocandidateEmergencyContactAddress
					.setIsActive(candidateEmergencyContactEntity.getCandidateEmergencyContactAddress().getIsActive());
			vocandidateEmergencyContactAddress
					.setPincode(candidateEmergencyContactEntity.getCandidateEmergencyContactAddress().getPincode());
			vocandidateEmergencyContactAddress
					.setRemark(candidateEmergencyContactEntity.getCandidateEmergencyContactAddress().getRemark());
			vocandidateEmergencyContactAddress.setState(translateToMasterStateVO(
					candidateEmergencyContactEntity.getCandidateEmergencyContactAddress().getState()));
			vocandidateEmergencyContactAddress
					.setUpdatedBy(candidateEmergencyContactEntity.getCandidateEmergencyContactAddress().getUpdatedBy());
			vocandidateEmergencyContactAddress.setUpdatedDate(
					candidateEmergencyContactEntity.getCandidateEmergencyContactAddress().getUpdatedDate());
			voCandidateEmergencyContact.setCandidateEmergencyContactAddress(vocandidateEmergencyContactAddress);

			voVOCandidateEmergencyContactList.add(voCandidateEmergencyContact);
		}

		return voVOCandidateEmergencyContactList;
	}

	public static List<VOCandidateLanguage> convertToVoCandidateLanguageList(
			List<CandidateLanguage> candidateLangugaeEntityList) {

		List<VOCandidateLanguage> vocandidateLanguagesList = new ArrayList<>();

		for (CandidateLanguage candidateLanguageEntity : candidateLangugaeEntityList) {
			VOCandidateLanguage vocandidateLanguage = new VOCandidateLanguage();
			vocandidateLanguage.setCreatedBy(candidateLanguageEntity.getCreatedBy());
			vocandidateLanguage.setCreatedDate(candidateLanguageEntity.getCreatedDate());
			vocandidateLanguage.setId(candidateLanguageEntity.getId());
			vocandidateLanguage.setIsActive(candidateLanguageEntity.getIsActive());
			vocandidateLanguage.setLanguageName(candidateLanguageEntity.getLanguageName());
			vocandidateLanguage.setMotherTongue(candidateLanguageEntity.getMotherTongue());
			vocandidateLanguage.setRead(candidateLanguageEntity.getRead());
			vocandidateLanguage.setRemark(candidateLanguageEntity.getRemark());
			vocandidateLanguage.setSpeak(candidateLanguageEntity.getSpeak());
			vocandidateLanguage.setUpdatedBy(candidateLanguageEntity.getUpdatedBy());
			vocandidateLanguage.setUpdatedDate(candidateLanguageEntity.getUpdatedDate());
			vocandidateLanguage.setWrite(candidateLanguageEntity.getWrite());

			vocandidateLanguagesList.add(vocandidateLanguage);
		}
		return vocandidateLanguagesList;
	}

	public static List<VOCandidatePassportDetail> convertToVoCandidatePassportDetailList(
			List<CandidatePassportDetail> candidatePassportDetailEntityList) {
		List<VOCandidatePassportDetail> candidatePassportDetailsList = new ArrayList<VOCandidatePassportDetail>();

		for (CandidatePassportDetail candidatePassportDetailEntity : candidatePassportDetailEntityList) {
			VOCandidatePassportDetail vocandidatePassportDetail = new VOCandidatePassportDetail();
			vocandidatePassportDetail.setCreatedBy(candidatePassportDetailEntity.getCreatedBy());
			vocandidatePassportDetail.setCreatedDate(candidatePassportDetailEntity.getCreatedDate());
			vocandidatePassportDetail.setDateOfExpiry(candidatePassportDetailEntity.getDateOfExpiry());
			vocandidatePassportDetail.setDateOfIssue(candidatePassportDetailEntity.getDateOfIssue());
			vocandidatePassportDetail.setEcnr(candidatePassportDetailEntity.getEcnr());
			vocandidatePassportDetail.setId(candidatePassportDetailEntity.getId());
			vocandidatePassportDetail.setIsActive(candidatePassportDetailEntity.getIsActive());
			vocandidatePassportDetail.setPassportFirstName(candidatePassportDetailEntity.getPassportFirstName());
			vocandidatePassportDetail.setPassportLastName(candidatePassportDetailEntity.getPassportLastName());
			vocandidatePassportDetail.setPassportMiddleName(candidatePassportDetailEntity.getPassportMiddleName());
			vocandidatePassportDetail.setPassportNumber(candidatePassportDetailEntity.getPassportNumber());
			vocandidatePassportDetail.setPlaceOfIssue(candidatePassportDetailEntity.getPlaceOfIssue());
			vocandidatePassportDetail.setRemark(candidatePassportDetailEntity.getRemark());
			vocandidatePassportDetail.setUpdatedBy(candidatePassportDetailEntity.getUpdatedBy());
			vocandidatePassportDetail.setUpdatedDate(candidatePassportDetailEntity.getUpdatedDate());

			candidatePassportDetailsList.add(vocandidatePassportDetail);
		}

		return candidatePassportDetailsList;
	}

	public static List<VOCandidateVisaDetail> convertToVoCandidateVisaDetailList(
			List<CandidateVisaDetail> candidateVisaDetailEntityList) {

		List<VOCandidateVisaDetail> vocandidateVisaDetailsList = new ArrayList<VOCandidateVisaDetail>();
		for (CandidateVisaDetail candidateVisaDetailEntity : candidateVisaDetailEntityList) {
			VOCandidateVisaDetail vocandidateVisaDetail = new VOCandidateVisaDetail();
			vocandidateVisaDetail.setCountry(candidateVisaDetailEntity.getCountry());
			vocandidateVisaDetail.setCreatedBy(candidateVisaDetailEntity.getCreatedBy());
			vocandidateVisaDetail.setCreatedDate(candidateVisaDetailEntity.getCreatedDate());
			vocandidateVisaDetail.setDateOfExpiry(candidateVisaDetailEntity.getDateOfExpiry());
			vocandidateVisaDetail.setDateOfIssue(candidateVisaDetailEntity.getDateOfIssue());
			vocandidateVisaDetail.setId(candidateVisaDetailEntity.getId());
			vocandidateVisaDetail.setIsActive(candidateVisaDetailEntity.getIsActive());
			vocandidateVisaDetail.setRemark(candidateVisaDetailEntity.getRemark());
			vocandidateVisaDetail.setUpdatedBy(candidateVisaDetailEntity.getUpdatedBy());
			vocandidateVisaDetail.setUpdatedDate(candidateVisaDetailEntity.getUpdatedDate());
			vocandidateVisaDetail.setVisaNumber(candidateVisaDetailEntity.getVisaNumber());
			vocandidateVisaDetail.setVisaType(candidateVisaDetailEntity.getVisaType());

			vocandidateVisaDetailsList.add(vocandidateVisaDetail);

		}
		return vocandidateVisaDetailsList;

	}

	public static List<VOCandidatePolicyDetail> convertToVoCandidatePolicyDetailList(
			List<CandidatePolicyDetail> candidatePolicyDetailEntityList) {

		List<VOCandidatePolicyDetail> voCandidatePolicyDetailList = new ArrayList<VOCandidatePolicyDetail>();

		for (CandidatePolicyDetail candidatePolicyDetailEntity : candidatePolicyDetailEntityList) {
			VOCandidatePolicyDetail vocandidatePolicyDetail = new VOCandidatePolicyDetail();
			vocandidatePolicyDetail.setCreatedBy(candidatePolicyDetailEntity.getCreatedBy());
			vocandidatePolicyDetail.setCreatedDate(candidatePolicyDetailEntity.getCreatedDate());
			vocandidatePolicyDetail.setDateOfExpiry(candidatePolicyDetailEntity.getDateOfExpiry());
			vocandidatePolicyDetail.setEmployeeMembershipId(candidatePolicyDetailEntity.getEmployeeMembershipId());
			vocandidatePolicyDetail.setId(candidatePolicyDetailEntity.getId());
			vocandidatePolicyDetail.setInsuranceCompany(candidatePolicyDetailEntity.getInsuranceCompany());
			vocandidatePolicyDetail.setInsuranceType(candidatePolicyDetailEntity.getInsuranceType());
			vocandidatePolicyDetail.setIsActive(candidatePolicyDetailEntity.getIsActive());
			vocandidatePolicyDetail.setPolicyName(candidatePolicyDetailEntity.getPolicyName());
			vocandidatePolicyDetail.setPolicyNumber(candidatePolicyDetailEntity.getPolicyNumber());
			vocandidatePolicyDetail.setRemark(candidatePolicyDetailEntity.getRemark());
			vocandidatePolicyDetail.setStartDate(candidatePolicyDetailEntity.getStartDate());
			vocandidatePolicyDetail.setSumInsured(candidatePolicyDetailEntity.getSumInsured());
			vocandidatePolicyDetail.setTpa(candidatePolicyDetailEntity.getTpa());
			vocandidatePolicyDetail.setTpaName(candidatePolicyDetailEntity.getTpaName());
			vocandidatePolicyDetail.setUpdatedBy(candidatePolicyDetailEntity.getUpdatedBy());
			vocandidatePolicyDetail.setUpdatedDate(candidatePolicyDetailEntity.getUpdatedDate());

			voCandidatePolicyDetailList.add(vocandidatePolicyDetail);
		}
		return voCandidatePolicyDetailList;
	}

	public static List<VOCandidateStatutoryNomination> convertToVoCandidateStatutoryNominationList(
			List<CandidateStatutoryNomination> candidateStatutoryNominationEntityList) {

		List<VOCandidateStatutoryNomination> voCandidateStatutoryNominationsList = new ArrayList<>();

		for (CandidateStatutoryNomination candidateStatutoryNominationEntity : candidateStatutoryNominationEntityList) {
			VOCandidateStatutoryNomination vocandidateStatutoryNomination = new VOCandidateStatutoryNomination();
			vocandidateStatutoryNomination.setAge(candidateStatutoryNominationEntity.getAge());
			vocandidateStatutoryNomination.setCreatedBy(candidateStatutoryNominationEntity.getCreatedBy());
			vocandidateStatutoryNomination.setCreatedDate(candidateStatutoryNominationEntity.getCreatedDate());
			vocandidateStatutoryNomination.setDateOfBirth(candidateStatutoryNominationEntity.getDateOfBirth());
			vocandidateStatutoryNomination.setId(candidateStatutoryNominationEntity.getId());
			vocandidateStatutoryNomination.setIsActive(candidateStatutoryNominationEntity.getIsActive());
			vocandidateStatutoryNomination.setNomineeName(candidateStatutoryNominationEntity.getNomineeName());
			vocandidateStatutoryNomination.setPercentage(candidateStatutoryNominationEntity.getPercentage());
			vocandidateStatutoryNomination.setRelationship(candidateStatutoryNominationEntity.getRelationship());
			vocandidateStatutoryNomination.setRemark(candidateStatutoryNominationEntity.getRemark());
			vocandidateStatutoryNomination.setType(candidateStatutoryNominationEntity.getType());
			vocandidateStatutoryNomination.setUpdatedBy(candidateStatutoryNominationEntity.getUpdatedBy());
			vocandidateStatutoryNomination.setUpdatedDate(candidateStatutoryNominationEntity.getUpdatedDate());

			voCandidateStatutoryNominationsList.add(vocandidateStatutoryNomination);
		}
		return voCandidateStatutoryNominationsList;

	}

	public static List<Object> transalteToMasterBranchVO(List<MasterBranch> masterBranchEntityList,
			List<Object> voMasterBranchList) {

		for (MasterBranch masterBranchEntity : masterBranchEntityList) {
			VOMasterBranch voMasterBranch = new VOMasterBranch();
			voMasterBranch.setBranchDescription(masterBranchEntity.getBranchDescription());
			voMasterBranch.setBranchName(masterBranchEntity.getBranchName());
			voMasterBranch.setId(masterBranchEntity.getId());
			// voMasterBranch.setVoOrganization();
			voMasterBranchList.add(voMasterBranch);
		}
		return voMasterBranchList;
	}

	public static List<Object> transalteToMasterCandidateActivityVO(
			List<MasterCandidateActivity> masterCandidateActivityEntityList,
			List<Object> voMasterCandidateActivityList) {

		for (MasterCandidateActivity masterCandidateActivity : masterCandidateActivityEntityList) {
			VOMasterCandidateActivity voMasterCandidateActivity = new VOMasterCandidateActivity();
			voMasterCandidateActivity.setId(masterCandidateActivity.getId());
			voMasterCandidateActivity.setName(masterCandidateActivity.getName());
			voMasterCandidateActivity.setDescription(masterCandidateActivity.getDescription());
			voMasterCandidateActivityList.add(voMasterCandidateActivity);
		}
		return voMasterCandidateActivityList;
	}

	public static List<Object> transalteToMasterDepartmentVO(List<MasterDepartment> masterDepartmentEntityList,
			List<Object> voMasterDepartmentList) {

		for (MasterDepartment masterdepartment : masterDepartmentEntityList) {
			VOMasterDepartment voMasterDepartment = new VOMasterDepartment();
			voMasterDepartment.setId(masterdepartment.getId());
			voMasterDepartment.setDepartmentName(masterdepartment.getDepartmentName());
			voMasterDepartment.setDepartmentDescription(masterdepartment.getDepartmentDescription());
			voMasterDepartmentList.add(voMasterDepartment);
		}
		return voMasterDepartmentList;
	}

	public static List<Object> translateToMasterCandidateOnboardActionReasonVO(
			List<MasterCandidateOnboardActionReason> onboardActionReasons,
			List<Object> voMasterOnboardActionReasonList) {

		for (MasterCandidateOnboardActionReason onboardActionReason : onboardActionReasons) {
			VOMasterOnboardActionReason voMasterOnboardActionReason = new VOMasterOnboardActionReason();
			voMasterOnboardActionReason.setId(onboardActionReason.getId());
			voMasterOnboardActionReason.setOnboardActionReasonName(onboardActionReason.getOnboardActionReasonName());
			voMasterOnboardActionReason
					.setOnboardActionReasonDescription(onboardActionReason.getOnboardActionReasonDescription());
			voMasterOnboardActionReason.setCreatedBy(onboardActionReason.getCreatedBy());
			voMasterOnboardActionReason.setCreatedDate(onboardActionReason.getCreatedDate());
			voMasterOnboardActionReason.setUpdatedBy(onboardActionReason.getUpdatedBy());
			voMasterOnboardActionReason.setUpdatedDate(onboardActionReason.getUpdatedDate());
			voMasterOnboardActionReason.setIsActive(onboardActionReason.getIsActive());
			voMasterOnboardActionReason.setRemark(onboardActionReason.getRemark());
			voMasterOnboardActionReason.setTypeOfAction(onboardActionReason.getTypeOfAction());
			voMasterOnboardActionReasonList.add(voMasterOnboardActionReason);
		}
		return voMasterOnboardActionReasonList;
	}

	public static List<Object> transalteToMasterDesignationVO(List<MasterDesignation> masterDesignationEntityList,
			List<Object> voMasterDesignationList) {

		for (MasterDesignation masterDesignationEntity : masterDesignationEntityList) {
			VOMasterDesignation voMasterDesignation = new VOMasterDesignation();
			voMasterDesignation.setDesignationDescription(masterDesignationEntity.getDesignationDescription());
			voMasterDesignation.setDesignationName(masterDesignationEntity.getDesignationName());
			voMasterDesignation.setId(masterDesignationEntity.getId());
			// voMasterDesignation.setOrganization(masterDesignationEntity);
			// voMasterDesignation.setVoOrganization(masterDesignationEntity.getOrganization()
			// );
			// voMasterBranch.setVoOrganization();
			voMasterDesignationList.add(voMasterDesignation);
		}
		return voMasterDesignationList;
	}

	public static List<Object> transalteToMasterDivisionVO(List<MasterDivision> masterDivisionEntityList,
			List<Object> voMasterDivisionList) {

		for (MasterDivision masterDivisionEntity : masterDivisionEntityList) {
			VOMasterDivision voMasterDivision = new VOMasterDivision();
			voMasterDivision.setDivisionDescription(masterDivisionEntity.getDivisionDescription());
			;
			voMasterDivision.setDivisionName(masterDivisionEntity.getDivisionName());
			voMasterDivision.setId(masterDivisionEntity.getId());
			// voMasterBranch.setVoOrganization();
			voMasterDivisionList.add(voMasterDivision);
		}
		return voMasterDivisionList;
	}

	public static List<Object> transalteToMasterCountryVO(List<MasterCountry> masterCountryEntityList,
			List<Object> voMasterCountryList) {

		List<VOMasterState> voMasterStates = new ArrayList<>();
		List<VOMasterCity> voMasterCities = new ArrayList<>();
		for (MasterCountry masterCountryEntity : masterCountryEntityList) {
			voMasterStates = new ArrayList<>();
			Set<MasterState> masterStates = masterCountryEntity.getMasterStates();
			if (!HRMSHelper.isNullOrEmpty(masterStates)) {
				for (MasterState masterState : masterStates) {
					voMasterCities = new ArrayList<>();
					VOMasterState voMasterState = new VOMasterState();
					Set<MasterCity> masterCities = masterState.getMasterCities();
					if (!HRMSHelper.isNullOrEmpty(masterCities)) {
						for (MasterCity masterCity : masterCities) {
							VOMasterCity voMasterCity = new VOMasterCity();
							voMasterCity.setId(masterCity.getId());
							voMasterCity.setCityName(masterCity.getCityName());
							voMasterCities.add(voMasterCity);
						}
					}
					voMasterState.setId(masterState.getId());
					voMasterState.setStateName(masterState.getStateName());
					voMasterState.setCities(voMasterCities);
					voMasterStates.add(voMasterState);

				}
			}

			VOMasterCountry voMasterCountry = new VOMasterCountry();

			voMasterCountry.setCountryDescription(masterCountryEntity.getCountryDescription());
			voMasterCountry.setCountryName(masterCountryEntity.getCountryName());
			voMasterCountry.setId(masterCountryEntity.getId());
			voMasterCountry.setStates(voMasterStates);
			voMasterCountryList.add(voMasterCountry);
		}
		return voMasterCountryList;
	}

	public static List<Object> transalteToMasterStateVO(List<MasterState> masterStateEntityList,
			List<Object> voMasterStateList) {

		for (MasterState masterStateEntity : masterStateEntityList) {
			VOMasterState voMasterState = new VOMasterState();
			voMasterState.setStateDescription(masterStateEntity.getStateDescription());
			voMasterState.setStateName(masterStateEntity.getStateName());
			voMasterState.setId(masterStateEntity.getId());
			voMasterStateList.add(voMasterState);
		}
		return voMasterStateList;
	}

	public static List<Object> transalteToMasterCityVO(List<MasterCity> masterCityEntityList,
			List<Object> voMasterCityList) {

		for (MasterCity masterCityEntity : masterCityEntityList) {
			VOMasterCity voMasterCity = new VOMasterCity();
			voMasterCity.setCityDescription(masterCityEntity.getCityDescription());
			voMasterCity.setCityName(masterCityEntity.getCityName());
			voMasterCity.setId(masterCityEntity.getId());
			voMasterCityList.add(voMasterCity);
		}
		return voMasterCityList;
	}

	public static List<Object> translateToMasterEmploymentTypeVO(
			List<MasterEmploymentType> masterEmploymentTypeEntityList, List<Object> voMasterEmploymentTypeList) {

		for (MasterEmploymentType masterEmploymentTypeEntity : masterEmploymentTypeEntityList) {
			VOMasterEmploymentType voMasterEmploymentType = new VOMasterEmploymentType();
			/*
			 * voMasterEmploymentType
			 * .setEmploymentTypeDescription(masterEmploymentTypeEntity.
			 * getEmploymentTypeDescription());
			 * voMasterEmploymentType.setEmploymentTypeName(masterEmploymentTypeEntity.
			 * getEmploymentTypeName());
			 * voMasterEmploymentType.setId(masterEmploymentTypeEntity.getId());
			 */
			voMasterEmploymentType = HRMSEntityToModelMapper.convertToMasterEmploymentType(masterEmploymentTypeEntity);
			voMasterEmploymentTypeList.add(voMasterEmploymentType);
		}
		return voMasterEmploymentTypeList;
	}

	public static List<Object> transalteToMasterLeaveTypeVO(List<MasterLeaveType> masterLeaveTypeEntityList,
			List<Object> voMasterLeaveTypeList) {

		for (MasterLeaveType masterLeaveTypeEntity : masterLeaveTypeEntityList) {
			VOMasterLeaveType voMasterLeaveType = new VOMasterLeaveType();
			String[] sessions = null;

			voMasterLeaveType.setLeaveTypeDescription(masterLeaveTypeEntity.getLeaveTypeDescription());
			voMasterLeaveType.setLeaveTypeName(masterLeaveTypeEntity.getLeaveTypeName());
			voMasterLeaveType.setNumberOfSession(masterLeaveTypeEntity.getNumberOfSession());
			voMasterLeaveType.setGrantLeaveStatus(masterLeaveTypeEntity.getLeaveGrantStatus());
			voMasterLeaveType.setIsLop(masterLeaveTypeEntity.getIsLop());
			voMasterLeaveType.setLeaveTypeCode(masterLeaveTypeEntity.getLeaveTypeCode());
			sessions = new String[voMasterLeaveType.getNumberOfSession()];
			for (int i = 0; i < voMasterLeaveType.getNumberOfSession(); i++) {
				sessions[i] = "Session " + (i + 1);

			}
			voMasterLeaveType.setSessions(sessions);
			voMasterLeaveType.setId(masterLeaveTypeEntity.getId());
			voMasterLeaveTypeList.add(voMasterLeaveType);
		}
		return voMasterLeaveTypeList;
	}

	public static List<Object> translateToMasterCandidateActivityActionTypesVO(
			List<MasterCandidateActivityActionType> masterCandidateActivityActionTypes,
			List<Object> voMasterOnboardActionReasonList) {

		for (MasterCandidateActivityActionType activityActionType : masterCandidateActivityActionTypes) {
			VOMasterCandidateActivityActionType voMasterCandidateActivityActionType = new VOMasterCandidateActivityActionType();
			voMasterCandidateActivityActionType.setId(activityActionType.getId());
			voMasterCandidateActivityActionType
					.setActivityActionTypeName(activityActionType.getActivityActionTypeName());
			voMasterCandidateActivityActionType
					.setActivityActionTypeDescription(activityActionType.getActivityActionTypeDescription());
			voMasterCandidateActivityActionType.setCreatedBy(activityActionType.getCreatedBy());
			voMasterCandidateActivityActionType.setCreatedDate(activityActionType.getCreatedDate());
			voMasterCandidateActivityActionType.setUpdatedBy(activityActionType.getUpdatedBy());
			voMasterCandidateActivityActionType.setUpdatedDate(activityActionType.getUpdatedDate());
			voMasterCandidateActivityActionType.setIsActive(activityActionType.getIsActive());
			voMasterCandidateActivityActionType.setRemark(activityActionType.getRemark());
			voMasterOnboardActionReasonList.add(voMasterCandidateActivityActionType);
		}
		return voMasterOnboardActionReasonList;
	}

	public static VOMasterCandidateActivityActionType translateToMasterCandidateActivityActionTypeVO(
			MasterCandidateActivityActionType masterCandidateActivityActionTypes) {
		VOMasterCandidateActivityActionType voMasterCandidateActivityActionType = new VOMasterCandidateActivityActionType();
		voMasterCandidateActivityActionType.setId(voMasterCandidateActivityActionType.getId());
		voMasterCandidateActivityActionType
				.setActivityActionTypeName(voMasterCandidateActivityActionType.getActivityActionTypeName());
		voMasterCandidateActivityActionType.setActivityActionTypeDescription(
				voMasterCandidateActivityActionType.getActivityActionTypeDescription());
		voMasterCandidateActivityActionType.setCreatedBy(voMasterCandidateActivityActionType.getCreatedBy());
		voMasterCandidateActivityActionType.setCreatedDate(voMasterCandidateActivityActionType.getCreatedDate());
		voMasterCandidateActivityActionType.setUpdatedBy(voMasterCandidateActivityActionType.getUpdatedBy());
		voMasterCandidateActivityActionType.setUpdatedDate(voMasterCandidateActivityActionType.getUpdatedDate());
		voMasterCandidateActivityActionType.setIsActive(voMasterCandidateActivityActionType.getIsActive());
		voMasterCandidateActivityActionType.setRemark(voMasterCandidateActivityActionType.getRemark());
		return voMasterCandidateActivityActionType;
	}

	public static List<Object> translateToCandidateActivityActionDetailVO(
			List<CandidateActivityActionDetail> candidateActivityActionDetails,
			List<Object> voCandidateActivityActionDetailList) {

		for (CandidateActivityActionDetail activityActionDetail : candidateActivityActionDetails) {
			VOCandidateActivityActionDetail voCandidateActivityActionDetail = new VOCandidateActivityActionDetail();
			voCandidateActivityActionDetail
					.setCandidate(translateToCandidateBriefModle(activityActionDetail.getCandidate()));
			voCandidateActivityActionDetail.setTypeOfAction(activityActionDetail.getTypeOfAction());
			voCandidateActivityActionDetail.setCreatedBy(activityActionDetail.getCreatedBy());
			voCandidateActivityActionDetail.setCreatedDate(activityActionDetail.getCreatedDate());
			voCandidateActivityActionDetail.setHrActedOn(activityActionDetail.getHrActedOn());
			voCandidateActivityActionDetail.setHrComment(activityActionDetail.getHrComment());
			voCandidateActivityActionDetail.setId(activityActionDetail.getId());
			voCandidateActivityActionDetail.setIsActive(activityActionDetail.getIsActive());
			voCandidateActivityActionDetail.setRemark(activityActionDetail.getRemark());
			voCandidateActivityActionDetail.setUpdatedBy(activityActionDetail.getUpdatedBy());
			voCandidateActivityActionDetail.setUpdatedDate(activityActionDetail.getUpdatedDate());
			voCandidateActivityActionDetailList.add(voCandidateActivityActionDetail);
		}
		return voCandidateActivityActionDetailList;
	}

	public static VOMasterCandidateActivity translateToMasterCandidateActivityVO(
			MasterCandidateActivity masterCandidateActivity) {
		VOMasterCandidateActivity voMasterCandidateActivity = new VOMasterCandidateActivity();
		voMasterCandidateActivity.setCreatedBy(masterCandidateActivity.getCreatedBy());
		voMasterCandidateActivity.setCreatedDate(masterCandidateActivity.getUpdatedDate());
		voMasterCandidateActivity.setDescription(masterCandidateActivity.getDescription());
		voMasterCandidateActivity.setId(masterCandidateActivity.getId());
		voMasterCandidateActivity.setIsActive(masterCandidateActivity.getIsActive());
		voMasterCandidateActivity.setName(masterCandidateActivity.getName());
		voMasterCandidateActivity.setRemark(masterCandidateActivity.getRemark());
		voMasterCandidateActivity.setUpdatedBy(masterCandidateActivity.getUpdatedBy());
		voMasterCandidateActivity.setUpdatedDate(masterCandidateActivity.getUpdatedDate());
		return voMasterCandidateActivity;
	}

	public static List<Object> translateToCandidateActivityVO(List<CandidateActivity> candidateActivities,
			List<Object> voCandidateActivityList) {

		for (CandidateActivity candidateActivity : candidateActivities) {
			VOCandidateActivity voCandidateActivity = new VOCandidateActivity();
			voCandidateActivity.setCandidate(translateToCandidateBriefModle(candidateActivity.getCandidate()));
			voCandidateActivity.setMasterCandidateActivity(
					translateToMasterCandidateActivityVO(candidateActivity.getMasterCandidateActivity()));
			voCandidateActivity.setActivityResponseDate(candidateActivity.getActivityResponseDate());
			voCandidateActivity.setActivityTriggredDate(candidateActivity.getActivityTriggredDate());
			voCandidateActivity.setCreatedBy(candidateActivity.getCreatedBy());
			voCandidateActivity.setCreatedDate(candidateActivity.getCreatedDate());
			voCandidateActivity.setHrComment(candidateActivity.getHrComment());
			voCandidateActivity.setHrStatus(candidateActivity.getHrStatus());
			voCandidateActivity.setId(candidateActivity.getId());
			voCandidateActivity.setIsActive(candidateActivity.getIsActive());
			voCandidateActivity.setRemark(candidateActivity.getRemark());
			voCandidateActivity.setEmailStatus(candidateActivity.getEmailStatus());
			voCandidateActivity.setUpdatedBy(candidateActivity.getUpdatedBy());
			voCandidateActivityList.add(voCandidateActivity);
		}
		return voCandidateActivityList;
	}

	public static VOMasterLeaveType translateToVOMasterLeaveTypeVO(MasterLeaveType masterLeaveTypeEntity) {
		VOMasterLeaveType voMasterLeaveType = new VOMasterLeaveType();
		voMasterLeaveType.setCreatedBy(masterLeaveTypeEntity.getCreatedBy());
		voMasterLeaveType.setCreatedDate(masterLeaveTypeEntity.getCreatedDate());
		voMasterLeaveType.setId(masterLeaveTypeEntity.getId());
		voMasterLeaveType.setIsActive(masterLeaveTypeEntity.getIsActive());
		voMasterLeaveType.setLeaveTypeDescription(masterLeaveTypeEntity.getLeaveTypeDescription());
		voMasterLeaveType.setLeaveTypeName(masterLeaveTypeEntity.getLeaveTypeName());
		voMasterLeaveType.setRemark(masterLeaveTypeEntity.getRemark());
		voMasterLeaveType.setUpdatedBy(masterLeaveTypeEntity.getUpdatedBy());
		voMasterLeaveType.setUpdatedDate(masterLeaveTypeEntity.getUpdatedDate());
		return voMasterLeaveType;
	}

	public static List<Object> translateToEmployeeGrantLeaveDetailVO(
			List<EmployeeGrantLeaveDetail> employeeGrantLeaveDetails, List<Object> voCandidateActivityList) {

		for (EmployeeGrantLeaveDetail grantLeaveDetail : employeeGrantLeaveDetails) {
			VOCandidate candidate = new VOCandidate();
			VOEmployee employee = new VOEmployee();
			VOEmployeeGrantLeaveDetail voEmpGrantLeaveDetail = new VOEmployeeGrantLeaveDetail();
			voEmpGrantLeaveDetail.setAppliedBy(grantLeaveDetail.getAppliedBy());
			voEmpGrantLeaveDetail.setApproverActionDateWithdrawn(grantLeaveDetail.getApproverActionDateWithdrawn());
			voEmpGrantLeaveDetail.setApproverCommentOnWithdrawn(grantLeaveDetail.getApproverCommentOnWithdrawn());
			voEmpGrantLeaveDetail.setAttachment(grantLeaveDetail.getAttachment());
			voEmpGrantLeaveDetail.setCc(grantLeaveDetail.getCc());
			voEmpGrantLeaveDetail.setContactDetails(grantLeaveDetail.getContactDetails());
			voEmpGrantLeaveDetail.setCreatedBy(grantLeaveDetail.getCreatedBy());
			voEmpGrantLeaveDetail.setCreatedDate(grantLeaveDetail.getCreatedDate());
			// voEmpGrantLeaveDetail.setDateOfApplied(
			// HRMSDateUtil.format(grantLeaveDetail.getDateOfApplied(),
			// IHRMSConstants.FRONT_END_DATE_FORMAT));

			voEmpGrantLeaveDetail.setDateOfApplied(
					HRMSDateUtil.format(grantLeaveDetail.getDateOfApplied(), IHRMSConstants.POSTGRE_DATE_FORMAT));

			voEmpGrantLeaveDetail.setDateOfApproverAction(grantLeaveDetail.getDateOfApproverAction());
			voEmpGrantLeaveDetail.setDateOfCancel(grantLeaveDetail.getDateOfCancel());
			voEmpGrantLeaveDetail.setDateOfWithdrawn(grantLeaveDetail.getDateOfWithdrawn());
			// voEmpGrantLeaveDetail.setFromDate(
			// HRMSDateUtil.format(grantLeaveDetail.getFromDate(),
			// IHRMSConstants.FRONT_END_DATE_FORMAT));

			voEmpGrantLeaveDetail.setFromDate(
					HRMSDateUtil.format(grantLeaveDetail.getFromDate(), IHRMSConstants.POSTGRE_DATE_FORMAT));

			voEmpGrantLeaveDetail.setFromSession(grantLeaveDetail.getFromSession());
			voEmpGrantLeaveDetail.setId(grantLeaveDetail.getId());
			voEmpGrantLeaveDetail.setIsActive(grantLeaveDetail.getIsActive());
			voEmpGrantLeaveDetail.setLeaveStatus(grantLeaveDetail.getLeaveStatus());
			voEmpGrantLeaveDetail
					.setMasterLeaveType(translateToVOMasterLeaveTypeVO(grantLeaveDetail.getMasterLeaveType()));
			voEmpGrantLeaveDetail.setReasonForApply(grantLeaveDetail.getReasonForApply());
			voEmpGrantLeaveDetail.setReasonForCancel(grantLeaveDetail.getReasonForCancel());
			voEmpGrantLeaveDetail.setReasonForWithdrawn(grantLeaveDetail.getReasonForWithdrawn());
			voEmpGrantLeaveDetail.setRemark(grantLeaveDetail.getRemark());
			// voEmpGrantLeaveDetail
			// .setToDate(HRMSDateUtil.format(grantLeaveDetail.getToDate(),
			// IHRMSConstants.FRONT_END_DATE_FORMAT));

			voEmpGrantLeaveDetail
					.setToDate(HRMSDateUtil.format(grantLeaveDetail.getToDate(), IHRMSConstants.POSTGRE_DATE_FORMAT));

			voEmpGrantLeaveDetail.setToSession(grantLeaveDetail.getToSession());
			voEmpGrantLeaveDetail.setUpdatedBy(grantLeaveDetail.getUpdatedBy());
			voEmpGrantLeaveDetail.setUpdatedDate(grantLeaveDetail.getUpdatedDate());
			voEmpGrantLeaveDetail.setNoOfDays(grantLeaveDetail.getNoOfDays());
			candidate.setFirstName(grantLeaveDetail.getEmployee().getCandidate().getFirstName());
			candidate.setLastName(grantLeaveDetail.getEmployee().getCandidate().getLastName());
			candidate.setId(grantLeaveDetail.getEmployee().getCandidate().getId());
			employee.setCandidate(candidate);
			employee.setId(grantLeaveDetail.getEmployee().getId());
			voEmpGrantLeaveDetail.setEmployee(employee);
			voCandidateActivityList.add(voEmpGrantLeaveDetail);
		}
		return voCandidateActivityList;
	}

	public static List<Object> translateToLoginEntityTypeVO(List<LoginEntityType> loginEntityTypeList,
			List<Object> voLoginEntityTypeList) {

		for (LoginEntityType loginEntityType : loginEntityTypeList) {
			VOLoginEntityType voLoginEntityType = new VOLoginEntityType();
			voLoginEntityType.setId(loginEntityType.getId());
			voLoginEntityType.setLoginEntityTypeName(loginEntityType.getLoginEntityTypeName());
			voLoginEntityTypeList.add(voLoginEntityType);
		}
		return voLoginEntityTypeList;
	}

	public static List<Object> translateToEmployeeVoForSearch(List<Employee> employees) {

		List<Object> voEmployees = new ArrayList<>();

		for (Employee emp : employees) {
			if (emp.getIsActive() != null && emp.getIsActive().equalsIgnoreCase(IHRMSConstants.isActive)) {
				VOEmployee voEmployee = new VOEmployee();
				VOCandidate voCandidate = new VOCandidate();

				VOEmployeeReportingManager voEmpReportingMngr = new VOEmployeeReportingManager();
				VOEmployee voEmpMngr = new VOEmployee();
				VOCandidate voCandMngr = new VOCandidate();

				voCandidate.setFirstName(emp.getCandidate().getFirstName());
				voCandidate.setMiddleName(emp.getCandidate().getMiddleName());
				voCandidate.setLastName(emp.getCandidate().getLastName());

				/***************** Reporting Manager START ****************/
				if (!HRMSHelper.isNullOrEmpty(emp.getEmployeeReportingManager())
						&& !HRMSHelper.isNullOrEmpty(emp.getEmployeeReportingManager().getReporingManager())
						&& !HRMSHelper
								.isNullOrEmpty(emp.getEmployeeReportingManager().getReporingManager().getCandidate())
						&& !HRMSHelper.isNullOrEmpty(
								emp.getEmployeeReportingManager().getReporingManager().getCandidate().getId())) {

					voCandMngr.setId(emp.getEmployeeReportingManager().getReporingManager().getCandidate().getId());
					voCandMngr.setFirstName(
							emp.getEmployeeReportingManager().getReporingManager().getCandidate().getFirstName());
					voCandMngr.setLastName(
							emp.getEmployeeReportingManager().getReporingManager().getCandidate().getLastName());
					voEmpMngr.setCandidate(voCandMngr);
					voEmpMngr.setId(emp.getEmployeeReportingManager().getReporingManager().getId());
					voEmpReportingMngr.setEmployee(voEmpMngr);
					voEmpReportingMngr.setId(emp.getEmployeeReportingManager().getId());

				}

				/***************** Reporting Manager END ****************/
				voEmployee.setId(emp.getId());
				voEmployee.setCandidate(voCandidate);
				voEmployee.setEmployeeReportingManager(voEmpReportingMngr);
				voEmployees.add(voEmployee);

			}
		}
		return voEmployees;

	}

	public static List<Object> translateToEmployeeLeaveDetailVoForAvailableLeave(
			List<EmployeeLeaveDetail> employeeLeaveDetails) {

		List<Object> voEmployeeLeaveDetails = new ArrayList<>();

		for (EmployeeLeaveDetail employeeLeaveDetail : employeeLeaveDetails) {

			VOEmployeeLeaveDetail voEmployeeLeaveDetail = new VOEmployeeLeaveDetail();
			VOMasterLeaveType voMasterLeaveType = new VOMasterLeaveType();
			VOEmployee voEmployee = new VOEmployee();

			voEmployee.setId(employeeLeaveDetail.getEmployee().getId());
			voMasterLeaveType.setId(employeeLeaveDetail.getMasterLeaveType().getId());

			voEmployeeLeaveDetail.setId(employeeLeaveDetail.getId());
			if (employeeLeaveDetail.getMasterLeaveType().getIsLop().equalsIgnoreCase("Y")) {
				voEmployeeLeaveDetail.setLeaveAvailable(0);
			} else {
				voEmployeeLeaveDetail.setLeaveAvailable(employeeLeaveDetail.getLeaveAvailable());
			}
			voEmployeeLeaveDetail.setMasterLeaveType(voMasterLeaveType);
			voEmployeeLeaveDetail.setEmployee(voEmployee);

			voEmployeeLeaveDetails.add(voEmployeeLeaveDetail);
		}
		return voEmployeeLeaveDetails;
	}

	public static Set<VOCandidateActivity> translateFromEntityToCandidateActivityVO(
			Set<CandidateActivity> candidateActivities) {

		Set<VOCandidateActivity> voCandidateActivities = new HashSet<VOCandidateActivity>();
		for (CandidateActivity candidateActivity : candidateActivities) {
			VOCandidateActivity voCandidateActivity = new VOCandidateActivity();
			voCandidateActivity.setCandidate(translateToCandidateBriefModle(candidateActivity.getCandidate()));
			voCandidateActivity.setMasterCandidateActivity(
					translateToMasterCandidateActivityVO(candidateActivity.getMasterCandidateActivity()));
			voCandidateActivity.setActivityResponseDate(candidateActivity.getActivityResponseDate());
			voCandidateActivity.setActivityTriggredDate(candidateActivity.getActivityTriggredDate());
			voCandidateActivity.setCreatedBy(candidateActivity.getCreatedBy());
			voCandidateActivity.setCreatedDate(candidateActivity.getCreatedDate());
			voCandidateActivity.setHrComment(candidateActivity.getHrComment());
			voCandidateActivity.setHrStatus(candidateActivity.getHrStatus());
			voCandidateActivity.setId(candidateActivity.getId());
			voCandidateActivity.setIsActive(candidateActivity.getIsActive());
			voCandidateActivity.setRemark(candidateActivity.getRemark());
			voCandidateActivity.setEmailStatus(candidateActivity.getEmailStatus());
			voCandidateActivity.setUpdatedBy(candidateActivity.getUpdatedBy());
			voCandidateActivities.add(voCandidateActivity);
		}
		return voCandidateActivities;
	}

	public static VOCandidateProfessionalDetail translateEntityToBasicCandidateProfessionalDetailVO(
			CandidateProfessionalDetail candidateProfessionalDetail) {
		VOCandidateProfessionalDetail voCandidateProfessionalDetail = new VOCandidateProfessionalDetail();
		voCandidateProfessionalDetail.setAadhaarCard(candidateProfessionalDetail.getAadhaarCard());
		voCandidateProfessionalDetail.setDateOfJoining(candidateProfessionalDetail.getDateOfJoining());
		voCandidateProfessionalDetail.setDateOfReported(candidateProfessionalDetail.getDateOfReported());
		voCandidateProfessionalDetail.setHrStatus(candidateProfessionalDetail.getHrStatus());
		voCandidateProfessionalDetail.setPanCard(candidateProfessionalDetail.getPanCard());
		// voCandidateProfessionalDetail.setRecruiterName(candidateProfessionalDetail.getRecruiterName());
		voCandidateProfessionalDetail.setRecruiter(
				HRMSEntityToModelMapper.convertToEmployeeModel(candidateProfessionalDetail.getRecruiter()));
		voCandidateProfessionalDetail.setReported(candidateProfessionalDetail.getReported());
		voCandidateProfessionalDetail.setRequisitionDate(candidateProfessionalDetail.getRequisitionDate());
		voCandidateProfessionalDetail.setShortlistDate(candidateProfessionalDetail.getShortlistDate());
		voCandidateProfessionalDetail.setStatus(candidateProfessionalDetail.getStatus());
		return voCandidateProfessionalDetail;
	}

	public static List<Object> translateToCandidateWithActivityDetailsVo(List<Candidate> candidates,
			List<Object> voCandidates) {
		for (Candidate candidate : candidates) {
			VOCandidate voCandidate = new VOCandidate();
			voCandidate.setCandidateActivities(
					translateFromEntityToCandidateActivityVO(candidate.getCandidateActivities()));
			voCandidate.setCandidateProfessionalDetail(
					translateEntityToBasicCandidateProfessionalDetailVO(candidate.getCandidateProfessionalDetail()));
			voCandidate.setEmailId(candidate.getEmailId());
			voCandidate.setFirstName(candidate.getFirstName());
			voCandidate.setId(candidate.getId());
			voCandidate.setLastName(candidate.getLastName());
			voCandidate.setTitle(candidate.getTitle());
			voCandidate.setCandidateActivityStatus(candidate.getCandidateActivityStatus());
			voCandidates.add(voCandidate);
		}
		return voCandidates;
	}

	public static VOCandidate translateToCandidateModle(Candidate candidateEntity) {

		VOCandidate candidateModel = new VOCandidate();
		VOCandidatePersonalDetail personalDetailsModel = null;
		VOCandidateProfessionalDetail professionalDetailsModel = null;

		if (candidateEntity != null) {
			if (candidateEntity != null && candidateEntity.getCandidatePersonalDetail() != null) {
				personalDetailsModel = HRMSEntityToModelMapper
						.convertToCandidatePersonalDetailsDetailModel(candidateEntity.getCandidatePersonalDetail());
			}

			if (candidateEntity != null && candidateEntity.getCandidateProfessionalDetail() != null) {
				professionalDetailsModel = HRMSEntityToModelMapper.convertToCandidateProfessionalDetailsDetailModel(
						candidateEntity.getCandidateProfessionalDetail());
			}

			Set<CandidateLetter> candidateLetterSet = candidateEntity.getLetters();
			Set<VOCandidateLetter> candidateLetterModelSet = new HashSet<VOCandidateLetter>();
			if (candidateLetterSet != null) {

				for (CandidateLetter candidateLetterEntity : candidateLetterSet) {

					VOCandidateLetter candidateLetterModel = HRMSEntityToModelMapper
							.convertToCandidateLetterModel(candidateLetterEntity);
					candidateLetterModelSet.add(candidateLetterModel);
				}
			}

			candidateModel.setLetters(candidateLetterModelSet);
			// candidateModel.setDateOfBirth(candidateEntity.getDateOfBirth());
			candidateModel.setDateOfBirth(HRMSHelper.isNullOrEmpty(candidateEntity.getDateOfBirth()) ? null
					: HRMSDateUtil.parse(candidateEntity.getDateOfBirth().toString(),
							IHRMSConstants.POSTGRE_DATE_FORMAT));
			candidateModel.setCandidateAddresses(
					HRMSEntityToModelMapper.convertSetCandidateAddressToModel(candidateEntity.getCandidateAddress()));
			candidateModel.setEmailId(candidateEntity.getEmailId());
			candidateModel.setFirstName(candidateEntity.getFirstName());
			candidateModel.setGender(candidateEntity.getGender());
			candidateModel.setId(candidateEntity.getId());
			candidateModel.setLastName(candidateEntity.getLastName());
			candidateModel.setCandidateStatus(candidateEntity.getCandidateStatus());
			candidateModel.setCandidatePersonalDetail(personalDetailsModel);
			candidateModel.setCandidateProfessionalDetail(professionalDetailsModel);
			candidateModel.setAge(HRMSHelper.calculateAge(candidateEntity.getDateOfBirth()));
			candidateModel.setMiddleName(candidateEntity.getMiddleName());
			candidateModel.setMobileNumber(candidateEntity.getMobileNumber());
			candidateModel.setTitle(candidateEntity.getTitle());
			candidateModel.setEmployee(HRMSEntityToModelMapper.convertToEmployeeModel(candidateEntity.getEmployee()));
			candidateModel.setCandidateEmploymentStatus(candidateEntity.getCandidateEmploymentStatus());
			candidateModel.setNoticePeriod(candidateEntity.getNoticePeriod());
		}

		return candidateModel;
	}

	public static VOMasterCity translateToMasterCityVO(MasterCity masterCity) {
		if (!HRMSHelper.isNullOrEmpty(masterCity)) {
			VOMasterCity voMasterCity = new VOMasterCity();
			voMasterCity.setCityDescription(masterCity.getCityDescription());
			voMasterCity.setCityName(masterCity.getCityName());
			voMasterCity.setId(masterCity.getId());
			return voMasterCity;
		}
		return null;
	}

	public static VOMasterState translateToMasterStateVO(MasterState masterState) {
		if (!HRMSHelper.isNullOrEmpty(masterState)) {
			VOMasterState voMasterState = new VOMasterState();
			voMasterState.setId(masterState.getId());
			voMasterState.setStateDescription(masterState.getStateDescription());
			voMasterState.setStateName(masterState.getStateName());
			return voMasterState;
		}
		return null;
	}

	public static List<Object> translateListOfCandidateActivityActionDetailToVO(
			List<CandidateActivityActionDetail> candidateActivityActionDetailEntityList,
			List<Object> voCandidateActivityActionList) {
		if (!HRMSHelper.isNullOrEmpty(candidateActivityActionDetailEntityList)) {
			for (CandidateActivityActionDetail activityActionDetailEntity : candidateActivityActionDetailEntityList) {
				if (!HRMSHelper.isNullOrEmpty(activityActionDetailEntity)) {
					VOCandidateActivityActionDetail voCandidateActivityActionDetail = new VOCandidateActivityActionDetail();
					voCandidateActivityActionDetail
							.setCandidate(translateToCandidateModle(activityActionDetailEntity.getCandidate()));
					voCandidateActivityActionDetail.setCreatedBy(activityActionDetailEntity.getCreatedBy());
					voCandidateActivityActionDetail.setCreatedDate(activityActionDetailEntity.getCreatedDate());
					voCandidateActivityActionDetail.setHrActedOn(activityActionDetailEntity.getHrActedOn());
					voCandidateActivityActionDetail.setHrComment(activityActionDetailEntity.getHrComment());
					voCandidateActivityActionDetail.setId(activityActionDetailEntity.getId());
					voCandidateActivityActionDetail.setIsActive(activityActionDetailEntity.getIsActive());
					voCandidateActivityActionDetail.setRemark(activityActionDetailEntity.getRemark());
					voCandidateActivityActionDetail.setTypeOfAction(activityActionDetailEntity.getTypeOfAction());
					voCandidateActivityActionDetail.setUpdatedBy(activityActionDetailEntity.getUpdatedBy());
					voCandidateActivityActionDetail.setUpdatedDate(activityActionDetailEntity.getUpdatedDate());
					voCandidateActivityActionList.add(voCandidateActivityActionDetail);
				}
			}
			return voCandidateActivityActionList;
		}
		return null;
	}

	public static List<Object> transalteToHolidayListVO(List<OrganizationHoliday> holidayEntityList,
			List<Object> voHolidayList) {

		for (OrganizationHoliday organizationHolidayEntity : holidayEntityList) {
			VOHolidayResponse voHoliday = new VOHolidayResponse();
			voHoliday.setDay(organizationHolidayEntity.getDay());
			SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
			voHoliday.setHolidayDate(df2.format(organizationHolidayEntity.getHolidayDate()));
			voHoliday.setHolidayName(organizationHolidayEntity.getHolidayName());
			voHoliday.setHolidayYear(organizationHolidayEntity.getHolidayYear());
			voHoliday.setId(organizationHolidayEntity.getId());
			voHoliday.setRestricted(organizationHolidayEntity.getRestricted());
			voHolidayList.add(voHoliday);
		}
		return voHolidayList;
	}

	public static List<Object> translateListOfMasterCandidateChecklistActionToVO(
			List<MasterCandidateChecklistAction> masterCandidateChecklistActions,
			List<Object> voCandidateChecklistActionList) {
		if (!HRMSHelper.isNullOrEmpty(masterCandidateChecklistActions)) {
			for (MasterCandidateChecklistAction candidateChecklistActionEntity : masterCandidateChecklistActions) {
				if (!HRMSHelper.isNullOrEmpty(candidateChecklistActionEntity)) {
					VOMasterCandidateChecklistAction voMasterCandidateChecklistAction = new VOMasterCandidateChecklistAction();
					voMasterCandidateChecklistAction = HRMSEntityToModelMapper
							.convertToMasterCandidateChecklistActionModel(candidateChecklistActionEntity);
					voCandidateChecklistActionList.add(voMasterCandidateChecklistAction);
				}
			}
			return voCandidateChecklistActionList;
		}
		return null;
	}

	public static List<Object> translateEmployeeNameIdToVO(List<Employee> employees, List<Object> voEmployees) {
		logger.info(" Translate to employee Name Id to VO ");
		if (!HRMSHelper.isNullOrEmpty(employees)) {
			for (Employee employeeEntity : employees) {
				if (!HRMSHelper.isNullOrEmpty(employeeEntity)) {
					VOEmployeeNameIdResponse voEmployeeNameIdResponse = new VOEmployeeNameIdResponse();
					voEmployeeNameIdResponse.setEmployeeId(employeeEntity.getId());
					voEmployeeNameIdResponse.setCandidateId(employeeEntity.getCandidate().getId());
					voEmployeeNameIdResponse.setFirstName(employeeEntity.getCandidate().getFirstName());
					voEmployeeNameIdResponse.setLastName(employeeEntity.getCandidate().getLastName());
					if (!HRMSHelper.isNullOrEmpty(employeeEntity.getEmployeeReportingManager()) && !(HRMSHelper
							.isNullOrEmpty(employeeEntity.getEmployeeReportingManager().getReporingManager()))) {
						voEmployeeNameIdResponse.setManagerId(
								employeeEntity.getEmployeeReportingManager().getReporingManager().getId());
						String managerName = employeeEntity.getEmployeeReportingManager().getReporingManager()
								.getCandidate().getFirstName()
								+ employeeEntity.getEmployeeReportingManager().getReporingManager().getCandidate()
										.getMiddleName()
								+ employeeEntity.getEmployeeReportingManager().getReporingManager().getCandidate()
										.getLastName();
						voEmployeeNameIdResponse.setManagerName(managerName);
						voEmployeeNameIdResponse.setBranchId(
								employeeEntity.getCandidate().getCandidateProfessionalDetail().getBranch().getId());
						voEmployeeNameIdResponse.setDivisionId(
								employeeEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId());
					}
					// changed by ssw on 06Sept2018
					// for adding employee attendance access card number
					if (!HRMSHelper.isNullOrEmpty(employeeEntity.getEmployeeACN())) {
						voEmployeeNameIdResponse.setEmpACN(employeeEntity.getEmployeeACN().getEmpACN());
					}
					voEmployees.add(voEmployeeNameIdResponse);
				}

			}
			return voEmployees;
		}
		return null;
	}

	public static List<Object> transalteToMstCountryVO(List<MasterCountry> masterCountryEntityList,
			List<Object> voMasterCountryList) {

		for (MasterCountry masterCountryEntity : masterCountryEntityList) {
			VOMasterCountry voMasterCountry = new VOMasterCountry();
			voMasterCountry.setCountryDescription(masterCountryEntity.getCountryDescription());
			voMasterCountry.setCountryName(masterCountryEntity.getCountryName());
			voMasterCountry.setId(masterCountryEntity.getId());
			voMasterCountryList.add(voMasterCountry);
		}
		return voMasterCountryList;
	}

	/**
	 * 
	 * @param candidates
	 * @param voCandidates
	 * @return
	 * 
	 *         this method is used translate candidate details to response
	 */
	public static List<Object> translateAllCandNameIdToVO(List<Candidate> candidates, List<Object> voCandidates) {
		if (!HRMSHelper.isNullOrEmpty(candidates)) {
			for (Candidate candidateEntity : candidates) {
				if (!HRMSHelper.isNullOrEmpty(candidateEntity)) {
					VOEmployeeNameIdResponse voEmployeeNameIdResponse = new VOEmployeeNameIdResponse();
					voEmployeeNameIdResponse.setCandidateId(candidateEntity.getId());
					voEmployeeNameIdResponse.setFirstName(candidateEntity.getFirstName());
					voEmployeeNameIdResponse.setLastName(candidateEntity.getLastName());
					if (!HRMSHelper.isNullOrEmpty(candidateEntity.getEmployee())
							&& !HRMSHelper.isLongZero(candidateEntity.getEmployee().getId())) {
						voEmployeeNameIdResponse.setEmployeeId(candidateEntity.getEmployee().getId());
					}
					voCandidates.add(voEmployeeNameIdResponse);
				}
			}
			return voCandidates;
		}
		return null;
	}

	public static VOMasterCountry translateToMasterCountryVO(MasterCountry country) {
		if (!HRMSHelper.isNullOrEmpty(country)) {
			VOMasterCountry voMasterCountry = new VOMasterCountry();
			voMasterCountry.setId(country.getId());
			voMasterCountry.setCountryDescription(country.getCountryDescription());
			voMasterCountry.setCountryName(country.getCountryName());
			return voMasterCountry;
		}
		return null;
	}

	public static List<Object> transalteToFeedbackQuestionVO(List<FeedbackQuestion> feedbackQuestionEntityList,
			List<Object> voFeedbackQuestionList) {
		Map<Long, VOFeedbackQuestion> mapOfQuestionOptions = new HashMap<Long, VOFeedbackQuestion>();
		for (FeedbackQuestion feedbackQuestionEntity : feedbackQuestionEntityList) {

			VOFeedbackQuestion voFeedbackQuestion = new VOFeedbackQuestion();

			// Set<VOFeedbackOption> setOfFbOption = new HashSet<VOFeedbackOption>();
			List<VOFeedbackOption> setOfFbOption = new ArrayList<VOFeedbackOption>();

			voFeedbackQuestion.setQuestionName(feedbackQuestionEntity.getQuestionName());
			voFeedbackQuestion.setChoice(feedbackQuestionEntity.getChoice());
			voFeedbackQuestion.setId(feedbackQuestionEntity.getId());
			voFeedbackQuestion.setSequenceNumber(feedbackQuestionEntity.getSequenceNumber());
			for (FeedbackOption fbOpt : feedbackQuestionEntity.getFeedbackOptions()) {
				VOFeedbackOption voFeedbackOption = new VOFeedbackOption();
				voFeedbackOption.setId(fbOpt.getId());
				voFeedbackOption.setOptionName(fbOpt.getOptionName());
				voFeedbackOption.setSequenceNumber(fbOpt.getSequenceNumber());
				if (feedbackQuestionEntity.getChoice().equalsIgnoreCase(IHRMSConstants.Question_type_MULTIPLE)) {
					voFeedbackOption.setIsSelected("false");
				} else {
					voFeedbackOption.setIsSelected("NOT_SELECTED");
				}
				setOfFbOption.add(voFeedbackOption);
			}
			voFeedbackQuestion.setFeedbackOptions(setOfFbOption);
			// voFeedbackQuestionList.add(voFeedbackQuestion);

			mapOfQuestionOptions.put(voFeedbackQuestion.getId(), voFeedbackQuestion);

		}
		for (Long id : mapOfQuestionOptions.keySet()) {
			VOFeedbackQuestion question = new VOFeedbackQuestion();
			// Set<VOFeedbackOption> setOfOption = new HashSet<VOFeedbackOption>();
			List<VOFeedbackOption> setOfOption = new ArrayList<VOFeedbackOption>();

			VOFeedbackQuestion temp = mapOfQuestionOptions.get(id);
			// char a = 'a';
			for (VOFeedbackOption opt : temp.getFeedbackOptions()) {
				// a++;
				setOfOption.add(opt);
			}
			// a = 'a';
			question.setId(temp.getId());
			question.setQuestionName(temp.getQuestionName());
			question.setChoice(temp.getChoice());
			question.setSequenceNumber(temp.getSequenceNumber());
			question.setFeedbackOptions(setOfOption);

			VOUserFeedback userFeedback = new VOUserFeedback();
			userFeedback.setComment("");
			question.setUserFeedback(userFeedback);

			voFeedbackQuestionList.add(question);
		}
		return voFeedbackQuestionList;
	}

	public static List<Object> transalteToEmployeeFeedbackVOList(List<EmployeeFeedback> employeeFeedbackEntityList,
			List<Object> voEmployeeFeedbackList) {

		Map<Long, VOFeedbackQuestion> mapOfQuestion = new HashMap<>();
		VOSubmittedEmployeeFeedback voEmpFb = new VOSubmittedEmployeeFeedback();
		List<VOFeedbackQuestion> empFeedbackList = new ArrayList<>();

		for (EmployeeFeedback employeeFeedbackEntity : employeeFeedbackEntityList) {
			mapOfQuestion.put(employeeFeedbackEntity.getFeedbackQuestion().getId(), null);
		}

		// Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		Iterator<Map.Entry<Long, VOFeedbackQuestion>> entries = mapOfQuestion.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<Long, VOFeedbackQuestion> entry = entries.next();
			// entry.getValue());

			VOFeedbackQuestion question = new VOFeedbackQuestion();
			// Set<VOFeedbackOption> optionList = new HashSet<>();
			List<VOFeedbackOption> optionList = new ArrayList<VOFeedbackOption>();
			VOUserFeedback userFeedBack = new VOUserFeedback();

			for (EmployeeFeedback employeeFeedbackEntity : employeeFeedbackEntityList) {
				VOFeedbackOption option = new VOFeedbackOption();

				if (entry.getKey() == employeeFeedbackEntity.getFeedbackQuestion().getId()) {

					if (!HRMSHelper.isNullOrEmpty(employeeFeedbackEntity.getFeedbackOption())
							&& !HRMSHelper.isNullOrEmpty(employeeFeedbackEntity.getFeedbackOption().getId())) {
						option.setId(employeeFeedbackEntity.getFeedbackOption().getId());
						option.setIsSelected(employeeFeedbackEntity.getIsSelected());
						option.setEmployeeFeedbackId(employeeFeedbackEntity.getId());
						option.setOptionName(employeeFeedbackEntity.getFeedbackOption().getOptionName());
						optionList.add(option);
					} else {
						userFeedBack.setEmployeeFeedbackId(employeeFeedbackEntity.getId());
						userFeedBack.setComment(employeeFeedbackEntity.getUserFeedback());
					}
					question.setQuestionName(employeeFeedbackEntity.getFeedbackQuestion().getQuestionName());
					question.setChoice(employeeFeedbackEntity.getFeedbackQuestion().getChoice());
				}
			}
			question.setFeedbackOptions(optionList);
			question.setUserFeedback(userFeedBack);
			question.setId(entry.getKey());

			mapOfQuestion.put(entry.getKey(), question);
		}

		for (long key : mapOfQuestion.keySet()) {
			VOFeedbackQuestion que = new VOFeedbackQuestion();
			que = mapOfQuestion.get(key);

			empFeedbackList.add(que);
		}

		VOSubmittedEmployeeFeedback feedback = new VOSubmittedEmployeeFeedback();
		feedback.setEmpFeedbackList(empFeedbackList);
		voEmployeeFeedbackList.add(feedback);

		return voEmployeeFeedbackList;
	}

	public static List<VOSubmittedEmployeeFeedback> transalteToEmployeeFeedbackVOListForPdf(
			List<EmployeeFeedback> employeeFeedbackEntityList,
			List<VOSubmittedEmployeeFeedback> voEmployeeFeedbackList) {

		Map<Long, VOFeedbackQuestion> mapOfQuestion = new HashMap<>();
		VOSubmittedEmployeeFeedback voEmpFb = new VOSubmittedEmployeeFeedback();
		List<VOFeedbackQuestion> empFeedbackList = new ArrayList<>();

		for (EmployeeFeedback employeeFeedbackEntity : employeeFeedbackEntityList) {
			mapOfQuestion.put(employeeFeedbackEntity.getFeedbackQuestion().getId(), null);
		}

		// Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		Iterator<Map.Entry<Long, VOFeedbackQuestion>> entries = mapOfQuestion.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<Long, VOFeedbackQuestion> entry = entries.next();
			VOFeedbackQuestion question = new VOFeedbackQuestion();
			// Set<VOFeedbackOption> optionList = new HashSet<>();
			List<VOFeedbackOption> optionList = new ArrayList<VOFeedbackOption>();
			VOUserFeedback userFeedBack = new VOUserFeedback();

			for (EmployeeFeedback employeeFeedbackEntity : employeeFeedbackEntityList) {
				VOFeedbackOption option = new VOFeedbackOption();

				if (entry.getKey() == employeeFeedbackEntity.getFeedbackQuestion().getId()) {

					if (!HRMSHelper.isNullOrEmpty(employeeFeedbackEntity.getFeedbackOption())
							&& !HRMSHelper.isNullOrEmpty(employeeFeedbackEntity.getFeedbackOption().getId())) {
						option.setId(employeeFeedbackEntity.getFeedbackOption().getId());
						option.setIsSelected(employeeFeedbackEntity.getIsSelected());
						option.setEmployeeFeedbackId(employeeFeedbackEntity.getId());
						option.setOptionName(employeeFeedbackEntity.getFeedbackOption().getOptionName());
						optionList.add(option);
					} else {
						userFeedBack.setEmployeeFeedbackId(employeeFeedbackEntity.getId());
						userFeedBack.setComment(employeeFeedbackEntity.getUserFeedback());
					}
					question.setQuestionName(employeeFeedbackEntity.getFeedbackQuestion().getQuestionName());
					question.setChoice(employeeFeedbackEntity.getFeedbackQuestion().getChoice());
				}
			}
			question.setFeedbackOptions(optionList);
			question.setUserFeedback(userFeedBack);
			question.setId(entry.getKey());

			mapOfQuestion.put(entry.getKey(), question);
		}

		for (long key : mapOfQuestion.keySet()) {
			VOFeedbackQuestion que = new VOFeedbackQuestion();
			que = mapOfQuestion.get(key);

			empFeedbackList.add(que);
		}

		VOSubmittedEmployeeFeedback feedback = new VOSubmittedEmployeeFeedback();
		feedback.setEmpFeedbackList(empFeedbackList);
		voEmployeeFeedbackList.add(feedback);

		return voEmployeeFeedbackList;
	}

	public static List<Object> translateToOrgDivSeperationLetterVo(List<MapOrgDivSeparationLetter> seperationLetters,
			List<Object> voseperationLetters) {
		if (!HRMSHelper.isNullOrEmpty(seperationLetters)) {
			for (MapOrgDivSeparationLetter seperationLetter : seperationLetters) {
				if (!HRMSHelper.isNullOrEmpty(seperationLetter)) {
					VOOrgDivSeparationLetter voOrgDivSeperationLetter = new VOOrgDivSeparationLetter();
					VOOrganization voOrganization = null;
					if (!HRMSHelper.isNullOrEmpty(seperationLetter.getOrganization())) {
						voOrganization = new VOOrganization();
						voOrganization.setId(seperationLetter.getOrganization().getId());
						voOrganization.setOrganizationName(seperationLetter.getOrganization().getOrganizationName());
					}
					VOMasterDivision voMasterDivision = null;
					if (!HRMSHelper.isNullOrEmpty(seperationLetter.getDivision())) {
						voMasterDivision = new VOMasterDivision();
						voMasterDivision.setId(seperationLetter.getDivision().getId());
						voMasterDivision.setDivisionName(seperationLetter.getDivision().getDivisionName());
					}
					voOrgDivSeperationLetter.setLetterName(seperationLetter.getLetterName());
					voOrgDivSeperationLetter.setDescription(seperationLetter.getDescription());
					voOrgDivSeperationLetter.setId(seperationLetter.getId());
					voOrgDivSeperationLetter.setVoOrganization(voOrganization);
					voOrgDivSeperationLetter.setVoMasterDivision(voMasterDivision);
					voseperationLetters.add(voOrgDivSeperationLetter);
				}
			}
		}
		return voseperationLetters;
	}

	public static List<Object> translatetoBPMNoList(List<BPMDetails> bpmNoList, List<Object> vobpmnolist) {

		if (!HRMSHelper.isNullOrEmpty(bpmNoList)) {
			for (BPMDetails bpmBoEntity : bpmNoList) {
				BPMDetails bpmBoVO = new BPMDetails();
				bpmBoVO.setId(bpmBoEntity.getId());

				vobpmnolist.add(bpmBoVO);
			}
			return vobpmnolist;
		}
		return null;
	}

	public static List<Object> translatetoBPMDetails(List<BPMDetails> bpmNoList, List<Object> vobpmnolist) {
		if (!HRMSHelper.isNullOrEmpty(bpmNoList)) {
			for (BPMDetails bpmBoEntity : bpmNoList) {
				BPMDetails bpmBoVO = new BPMDetails();
				bpmBoVO.setId(bpmBoEntity.getId());
				bpmBoVO.setBdName(bpmBoEntity.getBdName());
				bpmBoVO.setBusinessUnit(bpmBoEntity.getBusinessUnit());
				bpmBoVO.setClientName(bpmBoEntity.getClientName());
				bpmBoVO.setTraining_start_date(bpmBoEntity.getTraining_start_date());
				bpmBoVO.setTraining_end_date(bpmBoEntity.getTraining_end_date());

				vobpmnolist.add(bpmBoVO);
			}
			return vobpmnolist;
		}
		return null;
	}

	public static VOMasterDriver translateToMasterDriverResponse(MasterDriver masterDriverEntity) {

		VOMasterDriver voMasterDriver = new VOMasterDriver();
		voMasterDriver.setId(masterDriverEntity.getId());
		// voMasterDriver.setDriverDescription(masterDriverEntity.getDriverDescription());
		// voMasterDriver.setDriverName(masterDriverEntity.getDriverName());

		VOEmployee voEmployee = new VOEmployee();
		voEmployee.setId(masterDriverEntity.getEmployee().getId());

		VOCandidate voCandidate = new VOCandidate();
		voCandidate.setId(masterDriverEntity.getEmployee().getCandidate().getId());
		voCandidate.setFirstName(masterDriverEntity.getEmployee().getCandidate().getFirstName());
		voCandidate.setLastName(masterDriverEntity.getEmployee().getCandidate().getLastName());
		voCandidate.setMiddleName(masterDriverEntity.getEmployee().getCandidate().getMiddleName());

		voEmployee.setCandidate(voCandidate);
		voMasterDriver.setEmployee(voEmployee);

		VOOrganization voOrganization = new VOOrganization();
		voOrganization.setId(masterDriverEntity.getOrganization().getId());
		voOrganization.setOrganizationName(masterDriverEntity.getOrganization().getOrganizationName());

		VOMasterDivision voDivision = new VOMasterDivision();
		voDivision.setId(masterDriverEntity.getDivision().getId());
		voDivision.setDivisionName(masterDriverEntity.getDivision().getDivisionName());

		voMasterDriver.setOrganization(voOrganization);
		voMasterDriver.setDivision(voDivision);

		return voMasterDriver;

	}

	public static VOMasterVehicle translateToMasterVehicleResponse(MasterVehicle masterVehicleEntity) {

		VOMasterVehicle voMasterVehicle = new VOMasterVehicle();
		voMasterVehicle.setId(masterVehicleEntity.getId());
		voMasterVehicle.setVehicleDescription(masterVehicleEntity.getVehicleDescription());
		voMasterVehicle.setVehicleName(masterVehicleEntity.getVehicleName());

		VOOrganization voOrganization = new VOOrganization();
		voOrganization.setId(masterVehicleEntity.getOrganization().getId());
		voOrganization.setOrganizationName(masterVehicleEntity.getOrganization().getOrganizationName());

		VOMasterDivision voDivision = new VOMasterDivision();
		voDivision.setId(masterVehicleEntity.getDivision().getId());
		voDivision.setDivisionName(masterVehicleEntity.getDivision().getDivisionName());

		voMasterVehicle.setOrganization(voOrganization);
		voMasterVehicle.setDivision(voDivision);

		return voMasterVehicle;

	}

	public static List<Object> translatetoVODocuemntList(List<TraveldeskDocument> tdDocumentList,
			List<Object> vodocumentList) {

		if (!HRMSHelper.isNullOrEmpty(tdDocumentList)) {
			for (TraveldeskDocument traveldeskEntity : tdDocumentList) {
				VOTravelDeskDocument votravelDeskDocuemt = new VOTravelDeskDocument();
				votravelDeskDocuemt.setChildId(traveldeskEntity.getChildId());
				votravelDeskDocuemt.setChildType(traveldeskEntity.getChildType());
				votravelDeskDocuemt.setDocumentName(traveldeskEntity.getDocumentName());
				votravelDeskDocuemt.setId(traveldeskEntity.getId());
				votravelDeskDocuemt.setIsActive(traveldeskEntity.getIsActive());

				votravelDeskDocuemt.setTravelRequestId(
						HRMSEntityToModelMapper.convertToTravelRequestModel(traveldeskEntity.getTravelRequest()));
				vodocumentList.add(votravelDeskDocuemt);
			}
			return vodocumentList;
		}

		return null;
	}

	public static VOTraveldeskApprover translateToTraveldeskApproverVo(
			MasterTraveldeskApprover traveldeskApproverEntity) {
		VOTraveldeskApprover voTraveldeskApprover = null;
		if (traveldeskApproverEntity != null) {
			voTraveldeskApprover = new VOTraveldeskApprover();
			VOEmployee voEmployee = new VOEmployee();
			VOCandidate voCandidate = new VOCandidate();

			voCandidate.setId(traveldeskApproverEntity.getEmployee().getCandidate().getId());
			voCandidate.setFirstName(traveldeskApproverEntity.getEmployee().getCandidate().getFirstName());
			voCandidate.setLastName(traveldeskApproverEntity.getEmployee().getCandidate().getLastName());

			voEmployee.setId(traveldeskApproverEntity.getEmployee().getId());
			voEmployee.setCandidate(voCandidate);

			voTraveldeskApprover.setApproverType(traveldeskApproverEntity.getApproverType());
			voTraveldeskApprover.setApproverTypeDesc(traveldeskApproverEntity.getApproverTypeDesc());
			voTraveldeskApprover.setEmployee(voEmployee);
			voTraveldeskApprover.setId(traveldeskApproverEntity.getId());
		}

		return voTraveldeskApprover;
	}

	public static VOTicketRequest translateToTicketRequestVo(TicketRequest ticketRequestEntity) {
		VOTicketRequest voTicketRequest = null;

		if (!HRMSHelper.isNullOrEmpty(ticketRequestEntity)) {
			voTicketRequest = new VOTicketRequest();
			// VOEmployee voEmployee = new VOEmployee();
			VOCandidate voCandidate = new VOCandidate();
			VOMasterModeOfTravel voMasterModeOfTravel = new VOMasterModeOfTravel();

			// voCandidate.setId(ticketRequestEntity.getApproverId().getCandidate().getId());
			// voCandidate.setFirstName(ticketRequestEntity.getApproverId().getCandidate().getFirstName());
			// voCandidate.setLastName(ticketRequestEntity.getApproverId().getCandidate().getLastName());

			voCandidate.setId(ticketRequestEntity.getMasterApprover().getEmployee().getCandidate().getId());
			voCandidate
					.setFirstName(ticketRequestEntity.getMasterApprover().getEmployee().getCandidate().getFirstName());
			voCandidate.setLastName(ticketRequestEntity.getMasterApprover().getEmployee().getCandidate().getLastName());

			// voEmployee.setId(ticketRequestEntity.getApproverId().getId());
			// voEmployee.setCandidate(voCandidate);

			voMasterModeOfTravel.setId(ticketRequestEntity.getId());
			voMasterModeOfTravel.setModeOfTravel(ticketRequestEntity.getMasterModeOfTravel().getModeOfTravel());

			// voTicketRequest.setApprovalRequired(ticketRequestEntity.get);
			// voTicketRequest.setApproverId(voEmployee);
			voTicketRequest.setApproverStatus(ticketRequestEntity.getApproverStatus());
			// voTicketRequest.setChargeableToClient(ticketRequestEntity.getc);
			voTicketRequest.setCreatedBy(ticketRequestEntity.getCreatedBy());
			voTicketRequest.setCreatedDate(ticketRequestEntity.getCreatedDate());
			voTicketRequest.setFromLocation(ticketRequestEntity.getFromLocation());
			voTicketRequest.setId(ticketRequestEntity.getId());
			voTicketRequest.setIsActive(ticketRequestEntity.getIsActive());
			voTicketRequest.setMasterModeOfTravel(voMasterModeOfTravel);
			voTicketRequest.setNoOfTraveller(ticketRequestEntity.getNoOfTraveller());
			voTicketRequest.setPreferenceDetails(ticketRequestEntity.getPreferenceDetails());
			voTicketRequest.setPreferredDate(
					HRMSDateUtil.format(ticketRequestEntity.getPreferredDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			voTicketRequest.setPreferredTime(ticketRequestEntity.getPreferredTime());
			voTicketRequest.setRemark(ticketRequestEntity.getRemark());
			voTicketRequest.setReturnPreferenceDetails(ticketRequestEntity.getReturnPreferenceDetails());
			voTicketRequest.setReturnPreferredDate(HRMSDateUtil.format(ticketRequestEntity.getReturnPreferredDate(),
					IHRMSConstants.FRONT_END_DATE_FORMAT));
			voTicketRequest.setReturnPreferredTime(ticketRequestEntity.getReturnPreferredTime());
			// voTicketRequest.setRoundTrip(ticketRequestEntity.getr);
			// voTicketRequest.setTicketRequestPassengers();
			voTicketRequest.setTicketRequestStatus(ticketRequestEntity.getTicketRequestStatus());
			voTicketRequest.setToLocation(ticketRequestEntity.getToLocation());
			voTicketRequest.setTotalTicketFare(ticketRequestEntity.getTotalTicketFare());
			// voTicketRequest.setTravelRequestId();
			voTicketRequest.setUpdatedBy(ticketRequestEntity.getUpdatedBy());
			voTicketRequest.setUpdatedDate(ticketRequestEntity.getUpdatedDate());
		}

		return voTicketRequest;
	}

	public static List<VOMapCabDriverVehicle> translateToVOMapCabDriverVehicle(
			List<MapCabDriverVehicle> mapCabVehicleList) {
		List<VOMapCabDriverVehicle> voMapCabDriverVehicles = new ArrayList<VOMapCabDriverVehicle>();
		for (MapCabDriverVehicle entityItr : mapCabVehicleList) {
			VOMapCabDriverVehicle voItr = new VOMapCabDriverVehicle();
			voItr.setDriver(translateToMasterDriverResponse(entityItr.getDriverId()));
			voItr.setRecurring(entityItr.isRecurring());
			voItr.setTripWay(entityItr.getTripWay());
			voItr.setVehicle(translateToMasterVehicleResponse(entityItr.getVehicleId()));
			voMapCabDriverVehicles.add(voItr);
		}
		return voMapCabDriverVehicles;
	}

	public static VOReportTicketRequest translateToReportTicketRequestVo(ReportTicketRequest entity) {
		VOReportTicketRequest vo = null;
		if (!HRMSHelper.isNullOrEmpty(entity)) {
			vo = new VOReportTicketRequest();
			vo.setApprovalRequired(entity.getApprovalRequired());
			vo.setApprover(entity.getApprover());
			vo.setApproverId(entity.getApproverId());
			vo.setApproverStatus(entity.getApproverStatus());
			vo.setChargeableToClient(entity.getChargeableToClient());
			vo.setCreatedBy(entity.getCreatedBy());
			vo.setCreatedDate(HRMSDateUtil.format(entity.getCreatedDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			vo.setFromLocation(entity.getFromLocation());
			vo.setToLocation(entity.getToLocation());
			vo.setModeOfTravel(entity.getModeOfTravel());
			vo.setNoOfTraveller(entity.getNoOfTraveller());
			vo.setPassengerName(entity.getPassengerName());
			vo.setPreferenceDetails(entity.getPreferenceDetails());
			vo.setRequestedBy(entity.getRequestedBy());
			vo.setRoundTrip(entity.getRoundTrip());
			vo.setTicketRequestId(entity.getTicketRequestId());
			vo.setTicketRequestStatus(entity.getTicketRequestStatus());
			vo.setTotalTicketFare(entity.getTotalTicketFare());
			vo.setTravelRequestId(entity.getTravelRequestId());
			vo.setTravelStatus(entity.getTravelStatus());
			vo.setWon(entity.getWon());
		}
		return vo;
	}

	public static VOReportAccommodationRequest translateToReportAccommodationRequestVo(
			ReportAccommodationRequest entity) {

		VOReportAccommodationRequest vo = null;
		if (!HRMSHelper.isNullOrEmpty(entity)) {
			vo = new VOReportAccommodationRequest();
			vo.setAccommodationRequestId(entity.getAccommodationRequestId());
			vo.setApprovalRequired(entity.getApprovalRequired());
			vo.setApprover(entity.getApprover());
			vo.setApproverStatus(entity.getApproverStatus());
			vo.setChargeableToClient(entity.getChargeableToClient());
			vo.setCreatedBy(entity.getCreatedBy());
			vo.setCreatedDate(HRMSDateUtil.format(entity.getCreatedDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			vo.setFromDate((HRMSDateUtil.format(entity.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT)));
			vo.setGuestName(entity.getGuestName());
			vo.setNoOfPeople(entity.getNoOfPeople());
			vo.setRequestedBy(entity.getRequestedBy());
			vo.setToDate((HRMSDateUtil.format(entity.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT)));
			vo.setTravelRequestId(entity.getTravelRequestId());
			vo.setWon(entity.getWon());
			vo.setTotalAccommodationCost(entity.getTotalAccommodationCost());
		}
		return vo;
	}

	public static VOReportCabRequest translateToReportCabRequestVo(ReportCabRequest entity) {

		VOReportCabRequest vo = null;
		if (!HRMSHelper.isNullOrEmpty(entity)) {
			vo = new VOReportCabRequest();
			vo.setApprovalRequired(entity.getApprovalRequired());
			vo.setApprover(entity.getApprover());
			vo.setApproverStatus(entity.getApproverStatus());
			vo.setCabRequestId(entity.getCabRequestId());
			vo.setCabRequestStatus(entity.getCabRequestStatus());
			vo.setChargeableToClient(entity.getChargeableToClient());
			vo.setContactNumber(entity.getContactNumber());
			vo.setCreatedBy(entity.getCreatedBy());
			vo.setCreatedDate(HRMSDateUtil.format(entity.getCreatedDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			vo.setDropLocation(entity.getDropLocation());
			vo.setEmailId(entity.getEmailId());
			vo.setIsDropOnly(entity.getIsDropOnly());
			vo.setIsRecurring(entity.getIsRecurring());
			vo.setIsSelfManaged(entity.getIsSelfManaged());
			vo.setPassengerName(entity.getPassengerName());
			vo.setPickupAt(entity.getPickupAt());
			vo.setPickupDate(HRMSDateUtil.format(entity.getPickupDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			vo.setPickupTime(entity.getPickupTime());
			vo.setRequestedBy(entity.getRequestedBy());
			vo.setReturnDate(HRMSDateUtil.format(entity.getReturnDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			vo.setReturnTime(entity.getReturnTime());
			vo.setTotalCabCost(entity.getTotalCabCost());
			vo.setTravelRequestId(entity.getTravelRequestId());
			vo.setTravelStatus(entity.getTravelStatus());
			vo.setWon(entity.getWon());
		}
		return vo;
	}

	public static List<Object> transalteToAttendanceListVO(List<AttendanceRawData> attendanceRawDataList,
			List<Object> voAttendanceRawInfoList) {
		for (AttendanceRawData attendanceRawData : attendanceRawDataList) {
			VOAttendanceRawData voAttendanceRawData = new VOAttendanceRawData();

			voAttendanceRawData.setEmpId(attendanceRawData.getEmpId());
			voAttendanceRawData.setEmployeeACN(attendanceRawData.getEmployeeACN());
			voAttendanceRawData.setEmpName(attendanceRawData.getEmpName());
			voAttendanceRawData.setDepartment(attendanceRawData.getDepartment());
			voAttendanceRawData.setDesignation(attendanceRawData.getDesignation());
			voAttendanceRawData.setAttendanceDate(attendanceRawData.getAttendanceDate() != null
					? new SimpleDateFormat("yyyy-mm-dd").format(attendanceRawData.getAttendanceDate())
					: null);
			voAttendanceRawData.setStatus(attendanceRawData.getStatus());
			voAttendanceRawData.setStartTime(attendanceRawData.getStartTime() != null
					? new SimpleDateFormat("hh:mma").format(attendanceRawData.getStartTime())
					: null);
			voAttendanceRawData.setEndTime(attendanceRawData.getEndTime() != null
					? new SimpleDateFormat("hh:mma").format(attendanceRawData.getEndTime())
					: null);
			voAttendanceRawData.setManHours(attendanceRawData.getManHours());
			voAttendanceRawData.setUploadStatus(attendanceRawData.getUploadStatus());

			voAttendanceRawInfoList.add(voAttendanceRawData);
		}
		return voAttendanceRawInfoList;
	}

	public static List<Object> transalteToAttendanceProcessListVO(
			List<AttendanceProcessedData> attendanceProcessedDataList, List<Object> voAttendanceProcessedDataList) {

		for (AttendanceProcessedData attendanceProcessedData : attendanceProcessedDataList) {
			VOAttendanceProcessedData voAttendanceProcessedData = new VOAttendanceProcessedData();

			voAttendanceProcessedData.setEmpId(attendanceProcessedData.getCompositePrimaryKey().getEmpId());
			voAttendanceProcessedData.setEmployeeACN(attendanceProcessedData.getCompositePrimaryKey().getEmployeeACN());
			voAttendanceProcessedData.setEmpName(attendanceProcessedData.getEmpName());
			voAttendanceProcessedData.setDepartment(attendanceProcessedData.getDepartmentName());
			voAttendanceProcessedData.setDesignation(attendanceProcessedData.getDesignationName());
			voAttendanceProcessedData
					.setAttendanceDate(
							attendanceProcessedData.getCompositePrimaryKey().getAttendanceDate() != null
									? new SimpleDateFormat("yyyy-MM-dd").format(
											attendanceProcessedData.getCompositePrimaryKey().getAttendanceDate())
									: null);
			voAttendanceProcessedData.setStatus(attendanceProcessedData.getStatus());
			voAttendanceProcessedData.setStartTime(attendanceProcessedData.getStartTime() != null
					? new SimpleDateFormat("hh:mma").format(attendanceProcessedData.getStartTime())
					: null);
			voAttendanceProcessedData.setEndTime(attendanceProcessedData.getEndTime() != null
					? new SimpleDateFormat("hh:mma").format(attendanceProcessedData.getEndTime())
					: null);
			voAttendanceProcessedData.setManHours(attendanceProcessedData.getManHours());
			voAttendanceProcessedData.setUploadStatus(attendanceProcessedData.getUploadStatus());

			voAttendanceProcessedDataList.add(voAttendanceProcessedData);
		}
		return voAttendanceProcessedDataList;
	}

	public static List<Object> transalteToAttendanceSwapDataListVO(List<AttendanceCSVData> attendanceCSVDataList,
			List<Object> voAttendanceSwapDataList) {
		// TODO Auto-generated method stub
		for (AttendanceCSVData attendanceCSVData : attendanceCSVDataList) {
			VOAttendanceSwapData voAttendanceCSVData = new VOAttendanceSwapData();

			voAttendanceCSVData.setCardNo(attendanceCSVData.getCardNo());
			voAttendanceCSVData.setSwapTime(attendanceCSVData.getSwapTime() != null
					? new SimpleDateFormat("yyyy-MM-dd HH:mm").format(attendanceCSVData.getSwapTime())
					: null);

			voAttendanceSwapDataList.add(voAttendanceCSVData);
		}
		return voAttendanceSwapDataList;
	}
}
