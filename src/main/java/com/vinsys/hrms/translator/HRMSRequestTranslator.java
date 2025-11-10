package com.vinsys.hrms.translator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.vinsys.hrms.datamodel.VOApplyLeaveRequest;
import com.vinsys.hrms.datamodel.VOCandidate;
import com.vinsys.hrms.datamodel.VOCandidateActivity;
import com.vinsys.hrms.datamodel.VOCandidateActivityActionDetail;
import com.vinsys.hrms.datamodel.VOCandidateAddress;
import com.vinsys.hrms.datamodel.VOCandidateCertification;
import com.vinsys.hrms.datamodel.VOCandidateChecklist;
import com.vinsys.hrms.datamodel.VOCandidateEmergencyContact;
import com.vinsys.hrms.datamodel.VOCandidateFamilyDetail;
import com.vinsys.hrms.datamodel.VOCandidateHealthReport;
import com.vinsys.hrms.datamodel.VOCandidateLanguage;
import com.vinsys.hrms.datamodel.VOCandidateOverseasExperience;
import com.vinsys.hrms.datamodel.VOCandidatePassportDetail;
import com.vinsys.hrms.datamodel.VOCandidatePolicyDetail;
import com.vinsys.hrms.datamodel.VOCandidatePreviousEmployment;
import com.vinsys.hrms.datamodel.VOCandidateQualification;
import com.vinsys.hrms.datamodel.VOCandidateStatutoryNomination;
import com.vinsys.hrms.datamodel.VOCandidateVisaDetail;
import com.vinsys.hrms.datamodel.VOCreateEmployeeCurrentOrgRequest;
import com.vinsys.hrms.datamodel.VOCreateEmployeeRequest;
import com.vinsys.hrms.datamodel.VOCreditLeaveRequest;
import com.vinsys.hrms.datamodel.VOEmployee;
import com.vinsys.hrms.datamodel.VOEmployeeCreditLeaveDetail;
import com.vinsys.hrms.datamodel.VOEmployeeFeedback;
import com.vinsys.hrms.datamodel.VOEmployeeGrantLeaveDetail;
import com.vinsys.hrms.datamodel.VOEmployeeSeparationDetails;
import com.vinsys.hrms.datamodel.VOFeedbackOption;
import com.vinsys.hrms.datamodel.VOFeedbackQuestion;
import com.vinsys.hrms.datamodel.VOLoginEntityType;
import com.vinsys.hrms.datamodel.VOMapCatalogue;
import com.vinsys.hrms.datamodel.VOMapCatalogueChecklistItem;
import com.vinsys.hrms.datamodel.VOMapEmployeeCatalogueChecklist;
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
import com.vinsys.hrms.datamodel.VOMasterLanguage;
import com.vinsys.hrms.datamodel.VOMasterLeaveType;
import com.vinsys.hrms.datamodel.VOMasterModeofSeparation;
import com.vinsys.hrms.datamodel.VOMasterModeofSeparationReason;
import com.vinsys.hrms.datamodel.VOMasterOnboardActionReason;
import com.vinsys.hrms.datamodel.VOMasterState;
import com.vinsys.hrms.datamodel.VOOrganization;
import com.vinsys.hrms.datamodel.VOSubmittedEmployeeFeedback;
import com.vinsys.hrms.datamodel.VOSubscription;
import com.vinsys.hrms.datamodel.assets.VOCompanyAsset;
import com.vinsys.hrms.datamodel.assets.VOMasterAssetType;
import com.vinsys.hrms.datamodel.traveldesk.VOAccommodationGuest;
import com.vinsys.hrms.datamodel.traveldesk.VOAccommodationRequest;
import com.vinsys.hrms.datamodel.traveldesk.VOCabRequest;
import com.vinsys.hrms.datamodel.traveldesk.VOCabRequestPassenger;
import com.vinsys.hrms.datamodel.traveldesk.VOTicketRequest;
import com.vinsys.hrms.datamodel.traveldesk.VOTicketRequestPassenger;
import com.vinsys.hrms.datamodel.traveldesk.VOTravelRequest;
import com.vinsys.hrms.employee.vo.ApplyGrantLeaveRequestVO;
import com.vinsys.hrms.employee.vo.ApplyLeaveRequestVO;
import com.vinsys.hrms.employee.vo.EmployeeSeparationRequestVO;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateActivity;
import com.vinsys.hrms.entity.CandidateActivityActionDetail;
import com.vinsys.hrms.entity.CandidateAddress;
import com.vinsys.hrms.entity.CandidateCertification;
import com.vinsys.hrms.entity.CandidateChecklist;
import com.vinsys.hrms.entity.CandidateEmergencyContact;
import com.vinsys.hrms.entity.CandidateEmergencyContactAddress;
import com.vinsys.hrms.entity.CandidateFamilyAddress;
import com.vinsys.hrms.entity.CandidateFamilyDetail;
import com.vinsys.hrms.entity.CandidateHealthReport;
import com.vinsys.hrms.entity.CandidateLanguage;
import com.vinsys.hrms.entity.CandidateOverseasExperience;
import com.vinsys.hrms.entity.CandidatePassportDetail;
import com.vinsys.hrms.entity.CandidatePersonalDetail;
import com.vinsys.hrms.entity.CandidatePolicyDetail;
import com.vinsys.hrms.entity.CandidatePreviousEmployment;
import com.vinsys.hrms.entity.CandidateProfessionalDetail;
import com.vinsys.hrms.entity.CandidateQualification;
import com.vinsys.hrms.entity.CandidateStatutoryNomination;
import com.vinsys.hrms.entity.CandidateVisaDetail;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeBranch;
import com.vinsys.hrms.entity.EmployeeCreditLeaveDetail;
import com.vinsys.hrms.entity.EmployeeCurrentDetail;
import com.vinsys.hrms.entity.EmployeeDepartment;
import com.vinsys.hrms.entity.EmployeeDesignation;
import com.vinsys.hrms.entity.EmployeeDivision;
import com.vinsys.hrms.entity.EmployeeEmploymentType;
import com.vinsys.hrms.entity.EmployeeFeedback;
import com.vinsys.hrms.entity.EmployeeGrantLeaveDetail;
import com.vinsys.hrms.entity.EmployeeLeaveApplied;
import com.vinsys.hrms.entity.EmployeeSeparationDetails;
import com.vinsys.hrms.entity.EmployeeSeparationDetailsWithHistory;
import com.vinsys.hrms.entity.FeedbackQuestion;
import com.vinsys.hrms.entity.LoginEntity;
import com.vinsys.hrms.entity.LoginEntityType;
import com.vinsys.hrms.entity.MapCatalogue;
import com.vinsys.hrms.entity.MapCatalogueChecklistItem;
import com.vinsys.hrms.entity.MapEmployeeCatalogueChecklist;
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
import com.vinsys.hrms.entity.MasterLanguage;
import com.vinsys.hrms.entity.MasterLeaveType;
import com.vinsys.hrms.entity.MasterModeofSeparation;
import com.vinsys.hrms.entity.MasterModeofSeparationReason;
import com.vinsys.hrms.entity.MasterState;
import com.vinsys.hrms.entity.Subscription;
import com.vinsys.hrms.entity.assets.CompanyAsset;
import com.vinsys.hrms.entity.assets.MasterAssetType;
import com.vinsys.hrms.entity.attendance.AttendanceProcessedData;
import com.vinsys.hrms.entity.traveldesk.AccommodationGuest;
import com.vinsys.hrms.entity.traveldesk.AccommodationRequest;
import com.vinsys.hrms.entity.traveldesk.CabRecurringRequest;
import com.vinsys.hrms.entity.traveldesk.CabRequest;
import com.vinsys.hrms.entity.traveldesk.CabRequestPassenger;
import com.vinsys.hrms.entity.traveldesk.TicketRequest;
import com.vinsys.hrms.entity.traveldesk.TicketRequestPassenger;
import com.vinsys.hrms.entity.traveldesk.TravelRequest;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.ResponseCode;

////@PropertySource(value="${HRMSCONFIG}")
public class HRMSRequestTranslator {

	@Value("${base.url}")
	private static String baseURL;
	private static final Logger logger = LoggerFactory.getLogger(HRMSRequestTranslator.class);

	public static Candidate translateToCandidateEntityToPersist(VOCandidate candidateModel) throws HRMSException {

		if (candidateModel != null && candidateModel.getLoginEntity() != null
				&& candidateModel.getLoginEntity().getOrganization() != null) {

			logger.info(" == >> Translating To Candidate Entity << == ");

			Candidate candidateEntity = new Candidate();
			LoginEntity loginEnity = new LoginEntity();
			CandidateProfessionalDetail professionalDetails = new CandidateProfessionalDetail();

			/**
			 * Candidate Basic Details
			 */
			candidateEntity.setDateOfBirth(candidateModel.getDateOfBirth());
			candidateEntity.setEmailId(candidateModel.getEmailId());
			candidateEntity.setEmployee(null);
			candidateEntity.setFirstName(HRMSHelper.convertNullToEmpty(candidateModel.getFirstName()));
			candidateEntity.setGender(candidateModel.getGender());
			candidateEntity.setIsActive(IHRMSConstants.isActive);
			candidateEntity.setLastName(HRMSHelper.convertNullToEmpty(candidateModel.getLastName()));
			candidateEntity.setCreatedBy(candidateModel.getCreatedBy());
			candidateEntity.setCreatedDate(new Date());
			candidateEntity.setMiddleName(HRMSHelper.convertNullToEmpty(candidateModel.getMiddleName()));
			candidateEntity.setMobileNumber(candidateModel.getMobileNumber());
			candidateEntity.setTitle(candidateModel.getTitle());
			candidateEntity.setNoticePeriod(candidateModel.getNoticePeriod());

			/**
			 * Candidate Login Entity
			 */
			loginEnity.setIsFirstLogin(IHRMSConstants.IsFirstLogin);
			loginEnity.setLoginEntityName(candidateModel.getFirstName() + " " + candidateModel.getMiddleName() + " "
					+ candidateModel.getLastName());
			loginEnity.setNoOrg(candidateModel.getLoginEntity().getNoOrg());
			loginEnity.setUsername(candidateModel.getEmailId());
			loginEnity.setSuperUser(candidateModel.getLoginEntity().getSuperUser());
			loginEnity.setPrimaryEmail(candidateModel.getEmailId());
			loginEnity.setPassword(HRMSHelper.randomString());
			loginEnity.setCreatedDate(new Date());
			loginEnity.setCreatedBy(candidateModel.getCreatedBy());
			candidateEntity.setLoginEntity(loginEnity);

			/**
			 * Candidate Addresses
			 */
			// candidateEntity.setCandidateAddress(translateCandidateAddressSetEntityToVO(candidate.getCandidateAddresses()));

			/**
			 * Candidate Professional Details
			 */
			professionalDetails.setAadhaarCard(
					HRMSHelper.convertNullToEmpty(candidateModel.getCandidateProfessionalDetail().getAadhaarCard()));
			professionalDetails
					.setRequisitionDate(candidateModel.getCandidateProfessionalDetail().getRequisitionDate());
			professionalDetails.setRemark(candidateModel.getCandidateProfessionalDetail().getRemark());
			// professionalDetails.setRecruiterName(candidateModel.getCandidateProfessionalDetail().getRecruiterName());
			// professionalDetails.setRecruiter(convertToEmployeeEntityFromVO(candidateModel.getCandidateProfessionalDetail().getRecruiter()));
			professionalDetails.setPanCard(candidateModel.getCandidateProfessionalDetail().getPanCard());
			professionalDetails.setMarkLetterTo(candidateModel.getCandidateProfessionalDetail().getMarkLetterTo());
			professionalDetails.setIsActive(IHRMSConstants.isActive);
			professionalDetails.setDateOfJoining(candidateModel.getCandidateProfessionalDetail().getDateOfJoining());
			professionalDetails.setComment(candidateModel.getCandidateProfessionalDetail().getComment());
			professionalDetails.setCreatedBy(candidateModel.getCreatedBy());
			professionalDetails.setCreatedDate(new Date());

			// candidateEntity.setCandidateAddress(candidateAddressesEntity);
			candidateEntity.setCandidateProfessionalDetail(professionalDetails);

			return candidateEntity;
		} else {

			throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
		}

	}

	public static CandidateOverseasExperience translateToCandidateOverseasExpReq(
			CandidateOverseasExperience entityOverseas, VOCandidateOverseasExperience vocandidateOverseasExperience) {
		// TODO Auto-generated method stub

		// CandidateOverseasExperience entityOverseas = new
		// CandidateOverseasExperience();

		entityOverseas.setCompany(vocandidateOverseasExperience.getCompany());
		entityOverseas.setDuration(vocandidateOverseasExperience.getDuration());
		entityOverseas.setFromDate(vocandidateOverseasExperience.getFromDate());
		entityOverseas.setToDate(vocandidateOverseasExperience.getToDate());
		entityOverseas.setIsActive(IHRMSConstants.isActive);
		entityOverseas.setLocation(vocandidateOverseasExperience.getLocation());
		entityOverseas.setProject(vocandidateOverseasExperience.getProject());
		entityOverseas.setResponsibility(vocandidateOverseasExperience.getResponsibility());
		entityOverseas.setRemark(vocandidateOverseasExperience.getRemark());

		if (vocandidateOverseasExperience.getId() > 0) {

			entityOverseas.setUpdatedBy(vocandidateOverseasExperience.getUpdatedBy());
			entityOverseas.setUpdatedDate(new Date());

		} else {

			entityOverseas.setCreatedBy(vocandidateOverseasExperience.getCreatedBy());
			entityOverseas.setCreatedDate(new Date());

			Candidate candidateEntity = new Candidate();
			candidateEntity
					.setId(vocandidateOverseasExperience.getCandidateProfessionalDetail().getCandidate().getId());
			CandidateProfessionalDetail professionalDtlEntity = new CandidateProfessionalDetail();
			entityOverseas.setCandidateProfessionalDetail(professionalDtlEntity);
			professionalDtlEntity.setCandidate(candidateEntity);
		}
		return entityOverseas;
	}

	public static CandidateCertification translateToCandidateCertificationEntity(
			CandidateCertification entityCandidateCertification, VOCandidateCertification voCandidateCertification) {

		entityCandidateCertification.setCertificationDate(voCandidateCertification.getCertificationDate());
		entityCandidateCertification.setCertificationName(voCandidateCertification.getCertificationName());
		entityCandidateCertification.setCertificationType(voCandidateCertification.getCertificationType());
		entityCandidateCertification
				.setCertificationValidityDate(voCandidateCertification.getCertificationValidityDate());
		entityCandidateCertification.setPercentageGrade(voCandidateCertification.getPercentageGrade());
		entityCandidateCertification.setModeOfEducation(voCandidateCertification.getModeOfEducation());
		entityCandidateCertification.setIsActive(IHRMSConstants.isActive);
		entityCandidateCertification.setRemark(voCandidateCertification.getRemark());

		if (voCandidateCertification.getId() > 0) {

			entityCandidateCertification.setUpdatedBy(voCandidateCertification.getUpdatedBy());
			entityCandidateCertification.setUpdatedDate(new Date());
		} else {

			entityCandidateCertification.setCreatedBy(voCandidateCertification.getCreatedBy());
			entityCandidateCertification.setCreatedDate(new Date());

			Candidate entityCandidate = new Candidate();
			CandidateProfessionalDetail entityProfessionalDetail = new CandidateProfessionalDetail();
			entityCandidate.setId(voCandidateCertification.getCandidateProfessionalDetail().getCandidate().getId());
			entityProfessionalDetail.setCandidate(entityCandidate);
			entityCandidateCertification.setCandidateProfessionalDetail(entityProfessionalDetail);
		}

		return entityCandidateCertification;
	}

	public static CandidateFamilyDetail convertTOCandidateFamilyDetailEntity(
			VOCandidateFamilyDetail candidateFamilyDetail) {
		CandidateFamilyDetail candidateFamilyDetailEntity = new CandidateFamilyDetail();
		CandidateFamilyAddress candidateFamilyAddressEntity = new CandidateFamilyAddress();
		// CandidatePersonalDetail candidatePersonalDetailEntity = new
		// CandidatePersonalDetail();
		// Candidate candidateEntity = new Candidate();

		// added Candidate Family Data
		candidateFamilyDetailEntity.setContactNo1(candidateFamilyDetail.getContactNo1());
		candidateFamilyDetailEntity.setContactNo2(candidateFamilyDetail.getContactNo2());
		candidateFamilyDetailEntity.setCreatedBy(candidateFamilyDetail.getCreatedBy());
		candidateFamilyDetailEntity.setCreatedDate(new Date());
		candidateFamilyDetailEntity.setDateOfBirth(candidateFamilyDetail.getDateOfBirth());
		candidateFamilyDetailEntity.setDependent(candidateFamilyDetail.getDependent());
		candidateFamilyDetailEntity.setDependentSeverelyDisabled(candidateFamilyDetail.getDependentSeverelyDisabled());
		candidateFamilyDetailEntity.setGender(candidateFamilyDetail.getGender());
		candidateFamilyDetailEntity.setIsActive(IHRMSConstants.isActive);
		candidateFamilyDetailEntity.setFirst_name(candidateFamilyDetail.getFirst_name());
		candidateFamilyDetailEntity.setMiddle_name(candidateFamilyDetail.getMiddle_name());
		candidateFamilyDetailEntity.setLast_name(candidateFamilyDetail.getLast_name());

		candidateFamilyDetailEntity.setOccupation(candidateFamilyDetail.getOccupation());
		candidateFamilyDetailEntity.setRelationship(candidateFamilyDetail.getRelationship());
		candidateFamilyDetailEntity.setRemark(candidateFamilyDetail.getRemark());
		/*
		 * candidateFamilyDetailEntity.setUpdatedBy(candidateFamilyDetail.getUpdatedBy()
		 * ); candidateFamilyDetailEntity.setUpdatedDate(new Date());
		 */
		candidateFamilyDetailEntity.setWorking(candidateFamilyDetail.getWorking());
		candidateFamilyDetailEntity.setId(candidateFamilyDetail.getId());

		// added Candidate Family address
		candidateFamilyAddressEntity
				.setAddressLine1(candidateFamilyDetail.getCandidateFamilyAddress().getAddressLine1());
		candidateFamilyAddressEntity
				.setAddressLine2(candidateFamilyDetail.getCandidateFamilyAddress().getAddressLine2());
		candidateFamilyAddressEntity.setCandidateFamilyDetail(null);
		// candidateFamilyAddressEntity.setCity(candidateFamilyDetail.getCandidateFamilyAddress().getCity());
		candidateFamilyAddressEntity.setCreatedBy(candidateFamilyDetail.getCandidateFamilyAddress().getCreatedBy());
		candidateFamilyAddressEntity.setCreatedDate(new Date());
		candidateFamilyAddressEntity.setId(candidateFamilyDetail.getCandidateFamilyAddress().getId());
		candidateFamilyAddressEntity.setIsActive(IHRMSConstants.isActive);
		candidateFamilyAddressEntity.setPincode(candidateFamilyDetail.getCandidateFamilyAddress().getPincode());
		candidateFamilyAddressEntity.setRemark(candidateFamilyDetail.getCandidateFamilyAddress().getRemark());
		// candidateFamilyAddressEntity.setState(candidateFamilyDetail.getCandidateFamilyAddress().getState());
		/*
		 * candidateFamilyAddressEntity.setUpdatedBy(candidateFamilyDetail.
		 * getCandidateFamilyAddress().getUpdatedBy());
		 * candidateFamilyAddressEntity.setUpdatedDate(candidateFamilyDetail.
		 * getCandidateFamilyAddress().getUpdatedDate());
		 */

		/*
		 * candidateEntity.setId(candidateFamilyDetail.getCandidatePersonalDetail().
		 * getCandidate().getId());
		 * candidatePersonalDetailEntity.setCandidate(candidateEntity);
		 * 
		 * candidateFamilyDetailEntity.setCandidateFamilyAddress(
		 * candidateFamilyAddressEntity);
		 */

		// added candidate personal data
		candidateFamilyDetailEntity.setCandidateFamilyAddress(candidateFamilyAddressEntity);
		candidateFamilyDetailEntity.setCandidatePersonalDetail(null);

		return candidateFamilyDetailEntity;
	}

	public static CandidateEmergencyContact convertToCandidateEmergencyContactEntity(
			VOCandidateEmergencyContact vocandidateEmergencyContact) {
		CandidateEmergencyContact candidateEmergencyContactEntity = new CandidateEmergencyContact();
		CandidateEmergencyContactAddress candidateEmergencyContactAddressEntity = new CandidateEmergencyContactAddress();

		candidateEmergencyContactEntity.setCreatedBy(vocandidateEmergencyContact.getCreatedBy());
		candidateEmergencyContactEntity.setCreatedDate(vocandidateEmergencyContact.getCreatedDate());
		candidateEmergencyContactEntity.setId(vocandidateEmergencyContact.getId());
		candidateEmergencyContactEntity.setIsActive(IHRMSConstants.isActive);
		candidateEmergencyContactEntity.setLandlineNumber(vocandidateEmergencyContact.getLandlineNumber());
		candidateEmergencyContactEntity.setMobileNumber(vocandidateEmergencyContact.getMobileNumber());
		candidateEmergencyContactEntity.setFirstname(vocandidateEmergencyContact.getFirstname());
		candidateEmergencyContactEntity.setMiddlename(vocandidateEmergencyContact.getMiddlename());
		candidateEmergencyContactEntity.setLastname(vocandidateEmergencyContact.getLastname());
		candidateEmergencyContactEntity.setRelationship(vocandidateEmergencyContact.getRelationship());
		candidateEmergencyContactEntity.setRemark(vocandidateEmergencyContact.getRemark());
		/*
		 * candidateEmergencyContactEntity.setUpdatedBy(vocandidateEmergencyContact.
		 * getUpdatedBy());
		 * candidateEmergencyContactEntity.setUpdatedDate(vocandidateEmergencyContact.
		 * getUpdatedDate());
		 */

		candidateEmergencyContactAddressEntity
				.setAddressLine1(vocandidateEmergencyContact.getCandidateEmergencyContactAddress().getAddressLine1());
		candidateEmergencyContactAddressEntity
				.setAddressLine2(vocandidateEmergencyContact.getCandidateEmergencyContactAddress().getAddressLine2());
		// candidateEmergencyContactAddressEntity.setCity(vocandidateEmergencyContact.getCandidateEmergencyContactAddress().getCity());
		candidateEmergencyContactAddressEntity
				.setCreatedBy(vocandidateEmergencyContact.getCandidateEmergencyContactAddress().getCreatedBy());
		candidateEmergencyContactAddressEntity
				.setCreatedDate(vocandidateEmergencyContact.getCandidateEmergencyContactAddress().getCreatedDate());
		candidateEmergencyContactAddressEntity
				.setId(vocandidateEmergencyContact.getCandidateEmergencyContactAddress().getId());
		candidateEmergencyContactAddressEntity.setIsActive(IHRMSConstants.isActive);
		candidateEmergencyContactAddressEntity
				.setPincode(vocandidateEmergencyContact.getCandidateEmergencyContactAddress().getPincode());

		candidateEmergencyContactAddressEntity
				.setRemark(vocandidateEmergencyContact.getCandidateEmergencyContactAddress().getRemark());
		// candidateEmergencyContactAddressEntity.setState(vocandidateEmergencyContact.getCandidateEmergencyContactAddress().getState());
		/*
		 * candidateEmergencyContactAddressEntity
		 * .setUpdatedBy(vocandidateEmergencyContact.getCandidateEmergencyContactAddress
		 * ().getUpdatedBy()); candidateEmergencyContactAddressEntity
		 * .setUpdatedDate(vocandidateEmergencyContact.
		 * getCandidateEmergencyContactAddress().getUpdatedDate());
		 */

		candidateEmergencyContactEntity.setCandidateEmergencyContactAddress(candidateEmergencyContactAddressEntity);
		candidateEmergencyContactEntity.setCandidatePersonalDetail(null);

		return candidateEmergencyContactEntity;

	}

	public static CandidateLanguage convertToCandidateLanguageEntity(VOCandidateLanguage vocandidateLanguage) {

		CandidateLanguage candidateLanguageEntity = new CandidateLanguage();

		candidateLanguageEntity.setCandidatePersonalDetail(null);
		candidateLanguageEntity.setCreatedBy(vocandidateLanguage.getCreatedBy());
		candidateLanguageEntity.setCreatedDate(new Date());
		candidateLanguageEntity.setId(vocandidateLanguage.getId());
		candidateLanguageEntity.setIsActive(IHRMSConstants.isActive);

		// candidateLanguageEntity.setLanguageName(vocandidateLanguage.getLanguageName());
		candidateLanguageEntity.setMotherTongue(vocandidateLanguage.getMotherTongue());
		candidateLanguageEntity.setRead(vocandidateLanguage.getRead());
		candidateLanguageEntity.setRemark(vocandidateLanguage.getRemark());
		candidateLanguageEntity.setSpeak(vocandidateLanguage.getSpeak());
		/*
		 * candidateLanguageEntity.setUpdatedBy(vocandidateLanguage.getUpdatedBy());
		 * candidateLanguageEntity.setUpdatedDate(vocandidateLanguage.getUpdatedDate());
		 */
		candidateLanguageEntity.setWrite(vocandidateLanguage.getWrite());
		// candidateLanguageEntity.setLanguage(translateToMasterLanguageEntity(vocandidateLanguage.getLanguage()));

		return candidateLanguageEntity;
	}

	public static CandidatePassportDetail convertoCandidatePassportDetailEntity(
			VOCandidatePassportDetail vocandidatePassportDetail) {
		CandidatePassportDetail candidatePassportDetailEntity = new CandidatePassportDetail();
		candidatePassportDetailEntity.setCandidatePersonalDetail(null);
		candidatePassportDetailEntity.setCreatedBy(vocandidatePassportDetail.getCreatedBy());
		candidatePassportDetailEntity.setCreatedDate(new Date());
		candidatePassportDetailEntity.setDateOfExpiry(vocandidatePassportDetail.getDateOfExpiry());
		candidatePassportDetailEntity.setDateOfIssue(vocandidatePassportDetail.getDateOfIssue());
		candidatePassportDetailEntity.setEcnr(vocandidatePassportDetail.getEcnr());
		candidatePassportDetailEntity.setId(vocandidatePassportDetail.getId());
		candidatePassportDetailEntity.setIsActive(IHRMSConstants.isActive);
		candidatePassportDetailEntity.setPassportFirstName(vocandidatePassportDetail.getPassportFirstName());
		candidatePassportDetailEntity.setPassportMiddleName(vocandidatePassportDetail.getPassportMiddleName());
		candidatePassportDetailEntity.setPassportLastName(vocandidatePassportDetail.getPassportLastName());
		candidatePassportDetailEntity.setPassportNumber(vocandidatePassportDetail.getPassportNumber());
		candidatePassportDetailEntity.setPlaceOfIssue(vocandidatePassportDetail.getPlaceOfIssue());
		candidatePassportDetailEntity.setRemark(vocandidatePassportDetail.getRemark());
		/*
		 * candidatePassportDetailEntity.setUpdatedBy(vocandidatePassportDetail.
		 * getUpdatedBy());
		 * candidatePassportDetailEntity.setUpdatedDate(vocandidatePassportDetail.
		 * getUpdatedDate());
		 */

		return candidatePassportDetailEntity;

	}

	public static CandidateVisaDetail convertToCandidateVisaDetailEntity(VOCandidateVisaDetail vocandidateVisaDetail) {
		CandidateVisaDetail candidateVisaDetailEntity = new CandidateVisaDetail();

		candidateVisaDetailEntity.setCandidatePersonalDetail(null);
		candidateVisaDetailEntity.setCountry(vocandidateVisaDetail.getCountry());
		candidateVisaDetailEntity.setCreatedBy(vocandidateVisaDetail.getCreatedBy());
		candidateVisaDetailEntity.setCreatedDate(new Date());
		candidateVisaDetailEntity.setDateOfExpiry(vocandidateVisaDetail.getDateOfExpiry());
		candidateVisaDetailEntity.setDateOfIssue(vocandidateVisaDetail.getDateOfIssue());
		candidateVisaDetailEntity.setId(vocandidateVisaDetail.getId());
		candidateVisaDetailEntity.setIsActive(IHRMSConstants.isActive);
		candidateVisaDetailEntity.setRemark(vocandidateVisaDetail.getRemark());
		/*
		 * candidateVisaDetailEntity.setUpdatedBy(vocandidateVisaDetail.getUpdatedBy());
		 * candidateVisaDetailEntity.setUpdatedDate(vocandidateVisaDetail.getUpdatedDate
		 * ());
		 */
		candidateVisaDetailEntity.setVisaNumber(vocandidateVisaDetail.getVisaNumber());
		candidateVisaDetailEntity.setVisaType(vocandidateVisaDetail.getVisaType());

		return candidateVisaDetailEntity;
	}

	public static CandidateChecklist translateToCandidateChecklistEntity(CandidateChecklist checklistEntity,
			VOCandidateChecklist voCandidateChecklist) throws HRMSException {

		checklistEntity.setAttachment(voCandidateChecklist.getAttachment());
		checklistEntity.setChecklistItem(voCandidateChecklist.getChecklistItem());
		checklistEntity.setChecklistTemplate(voCandidateChecklist.getChecklistTemplate());
		checklistEntity.setIsActive(IHRMSConstants.isActive);
		checklistEntity.setMandatory(voCandidateChecklist.getMandatory());
		checklistEntity.setRemark(voCandidateChecklist.getRemark());
		checklistEntity.setSubmitted(voCandidateChecklist.getSubmitted());
		checklistEntity.setHrValidationActedOn(voCandidateChecklist.getHrValidationActedOn());
		checklistEntity.setHrValidationComment(voCandidateChecklist.getHrValidationComment());
		checklistEntity.setHrValidationStatus(voCandidateChecklist.getHrValidationStatus());
		// checklistEntity.setMasterCandidateChecklistAction(
		// translateToCandidateChecklistAction(voCandidateChecklist.getMasterCandidateChecklistAction()));

		if (voCandidateChecklist.getId() > 0) {

			checklistEntity.setUpdatedDate(new Date());
			checklistEntity.setUpdatedBy(voCandidateChecklist.getUpdatedBy());

		} else {

			Candidate candidateEntity = new Candidate();
			CandidateProfessionalDetail professionalDetailEntity = new CandidateProfessionalDetail();
			candidateEntity.setId(voCandidateChecklist.getCandidateProfessionalDetail().getCandidate().getId());
			professionalDetailEntity.setCandidate(candidateEntity);
			checklistEntity.setCandidateProfessionalDetail(professionalDetailEntity);
			checklistEntity.setCreatedDate(new Date());
			checklistEntity.setCreatedBy(voCandidateChecklist.getCreatedBy());
		}

		return checklistEntity;
	}

	public static CandidatePreviousEmployment translateToAddPreviousEmploymentEntity(
			CandidatePreviousEmployment candidatePreviousEmployment,
			VOCandidatePreviousEmployment voCandidatePreviousEmployment) throws HRMSException {

		// CandidatePreviousEmployment candidatePreviousEmployment = new
		// CandidatePreviousEmployment();

		// candidatePreviousEmployment.setCity(voCandidatePreviousEmployment.getCity());
		candidatePreviousEmployment.setCompanyAddress(voCandidatePreviousEmployment.getCompanyAddress());
		candidatePreviousEmployment.setCompanyName(voCandidatePreviousEmployment.getCompanyName());
		candidatePreviousEmployment.setCreatedBy(voCandidatePreviousEmployment.getCreatedBy());

		candidatePreviousEmployment.setDesignation(voCandidatePreviousEmployment.getDesignation());
		candidatePreviousEmployment.setExperience(voCandidatePreviousEmployment.getExperience());
		candidatePreviousEmployment.setFromDate(voCandidatePreviousEmployment.getFromDate());
		candidatePreviousEmployment.setIsRelevant(voCandidatePreviousEmployment.getIsRelevant());
		candidatePreviousEmployment.setJobType(voCandidatePreviousEmployment.getJobType());
		candidatePreviousEmployment.setOverseas(voCandidatePreviousEmployment.getOverseas());
		candidatePreviousEmployment.setPreviousManager(voCandidatePreviousEmployment.getPreviousManager());
		candidatePreviousEmployment
				.setPreviousManagerContactNumber(voCandidatePreviousEmployment.getPreviousManagerContactNumber());
		candidatePreviousEmployment.setReasonForleaving(voCandidatePreviousEmployment.getReasonForleaving());
		candidatePreviousEmployment.setRemark(voCandidatePreviousEmployment.getRemark());
		// candidatePreviousEmployment.setState(voCandidatePreviousEmployment.getState());
		candidatePreviousEmployment.setToDate(voCandidatePreviousEmployment.getToDate());

		if (voCandidatePreviousEmployment.getId() > 0) {
			candidatePreviousEmployment.setUpdatedBy(voCandidatePreviousEmployment.getUpdatedBy());
			candidatePreviousEmployment.setUpdatedDate(new Date());

		} else {
			candidatePreviousEmployment.setCreatedDate(new Date());
			Candidate candidate = new Candidate();
			candidate.setId(voCandidatePreviousEmployment.getCandidateProfessionalDetail().getCandidate().getId());

			CandidateProfessionalDetail professionalDetailsEntity = new CandidateProfessionalDetail();
			professionalDetailsEntity.setCandidate(candidate);
			candidatePreviousEmployment.setCandidateProfessionalDetail(professionalDetailsEntity);
			candidatePreviousEmployment.setIsActive(IHRMSConstants.isActive);

		}
		return candidatePreviousEmployment;
		// return null;

	}

	public static CandidateFamilyDetail convertToUpdateCandidateFamilyDetailEntity(
			CandidateFamilyDetail candidateFamilyDetailUpdateEntity, VOCandidateFamilyDetail vocandidateFamilyDetail) {

		candidateFamilyDetailUpdateEntity.setContactNo1(vocandidateFamilyDetail.getContactNo1());
		candidateFamilyDetailUpdateEntity.setContactNo2(vocandidateFamilyDetail.getContactNo2());
		/*
		 * candidateFamilyDetailUpdateEntity.setCreatedBy(vocandidateFamilyDetail.
		 * getCreatedBy());
		 * candidateFamilyDetailUpdateEntity.setCreatedDate(vocandidateFamilyDetail.
		 * getCreatedDate());
		 */
		candidateFamilyDetailUpdateEntity.setDateOfBirth(vocandidateFamilyDetail.getDateOfBirth());
		candidateFamilyDetailUpdateEntity.setDependent(vocandidateFamilyDetail.getDependent());
		candidateFamilyDetailUpdateEntity
				.setDependentSeverelyDisabled(vocandidateFamilyDetail.getDependentSeverelyDisabled());
		candidateFamilyDetailUpdateEntity.setGender(vocandidateFamilyDetail.getGender());
		candidateFamilyDetailUpdateEntity.setFirst_name(vocandidateFamilyDetail.getFirst_name());
		candidateFamilyDetailUpdateEntity.setMiddle_name(vocandidateFamilyDetail.getMiddle_name());
		candidateFamilyDetailUpdateEntity.setLast_name(vocandidateFamilyDetail.getLast_name());
		candidateFamilyDetailUpdateEntity.setOccupation(vocandidateFamilyDetail.getOccupation());
		candidateFamilyDetailUpdateEntity.setRelationship(vocandidateFamilyDetail.getRelationship());
		candidateFamilyDetailUpdateEntity.setRemark(vocandidateFamilyDetail.getRemark());
		candidateFamilyDetailUpdateEntity.setUpdatedBy(vocandidateFamilyDetail.getUpdatedBy());
		candidateFamilyDetailUpdateEntity.setUpdatedDate(new Date());
		candidateFamilyDetailUpdateEntity.setWorking(vocandidateFamilyDetail.getWorking());

		candidateFamilyDetailUpdateEntity.getCandidateFamilyAddress()
				.setAddressLine1(vocandidateFamilyDetail.getCandidateFamilyAddress().getAddressLine1());
		candidateFamilyDetailUpdateEntity.getCandidateFamilyAddress()
				.setAddressLine2(vocandidateFamilyDetail.getCandidateFamilyAddress().getAddressLine2());
		// candidateFamilyDetailUpdateEntity.getCandidateFamilyAddress().setCity(vocandidateFamilyDetail.getCandidateFamilyAddress().getCity());
		/*
		 * candidateFamilyDetailUpdateEntity.getCandidateFamilyAddress()
		 * .setCreatedBy(vocandidateFamilyDetail.getCandidateFamilyAddress().
		 * getCreatedBy());
		 * candidateFamilyDetailUpdateEntity.getCandidateFamilyAddress()
		 * .setCreatedDate(vocandidateFamilyDetail.getCandidateFamilyAddress().
		 * getCreatedDate());
		 */

		candidateFamilyDetailUpdateEntity.getCandidateFamilyAddress()
				.setPincode(vocandidateFamilyDetail.getCandidateFamilyAddress().getPincode());
		candidateFamilyDetailUpdateEntity.getCandidateFamilyAddress()
				.setRemark(vocandidateFamilyDetail.getCandidateFamilyAddress().getRemark());
		// candidateFamilyDetailUpdateEntity.getCandidateFamilyAddress().setState(vocandidateFamilyDetail.getCandidateFamilyAddress().getState());
		candidateFamilyDetailUpdateEntity.getCandidateFamilyAddress()
				.setUpdatedBy(vocandidateFamilyDetail.getCandidateFamilyAddress().getUpdatedBy());
		candidateFamilyDetailUpdateEntity.getCandidateFamilyAddress().setUpdatedDate(new Date());

		return candidateFamilyDetailUpdateEntity;

	}

	public static CandidateEmergencyContact convertToUpdateCandidateEmergencyContactEntity(
			CandidateEmergencyContact candidateEmergencyContactEntity,
			VOCandidateEmergencyContact vocandidateEmergencyContact) {

		/*
		 * candidateEmergencyContactEntity.setCreatedBy(vocandidateEmergencyContact.
		 * getCreatedBy());
		 * candidateEmergencyContactEntity.setCreatedDate(vocandidateEmergencyContact.
		 * getCreatedDate());
		 */
		candidateEmergencyContactEntity.setLandlineNumber(vocandidateEmergencyContact.getLandlineNumber());
		candidateEmergencyContactEntity.setMobileNumber(vocandidateEmergencyContact.getMobileNumber());
		candidateEmergencyContactEntity.setFirstname(vocandidateEmergencyContact.getFirstname());
		candidateEmergencyContactEntity.setMiddlename(vocandidateEmergencyContact.getMiddlename());
		candidateEmergencyContactEntity.setLastname(vocandidateEmergencyContact.getLastname());
		candidateEmergencyContactEntity.setRelationship(vocandidateEmergencyContact.getRelationship());
		candidateEmergencyContactEntity.setRemark(vocandidateEmergencyContact.getRemark());
		candidateEmergencyContactEntity.setUpdatedBy(vocandidateEmergencyContact.getUpdatedBy());
		candidateEmergencyContactEntity.setUpdatedDate(new Date());

		candidateEmergencyContactEntity.getCandidateEmergencyContactAddress()
				.setAddressLine1(vocandidateEmergencyContact.getCandidateEmergencyContactAddress().getAddressLine1());
		candidateEmergencyContactEntity.getCandidateEmergencyContactAddress()
				.setAddressLine2(vocandidateEmergencyContact.getCandidateEmergencyContactAddress().getAddressLine2());
		// candidateEmergencyContactEntity.getCandidateEmergencyContactAddress().setCity(vocandidateEmergencyContact.getCandidateEmergencyContactAddress().getCity());
		/*
		 * candidateEmergencyContactEntity.getCandidateEmergencyContactAddress()
		 * .setCreatedBy(vocandidateEmergencyContact.getCandidateEmergencyContactAddress
		 * ().getCreatedBy());
		 * candidateEmergencyContactEntity.getCandidateEmergencyContactAddress()
		 * .setCreatedDate(vocandidateEmergencyContact.
		 * getCandidateEmergencyContactAddress().getCreatedDate());
		 */
		candidateEmergencyContactEntity.getCandidateEmergencyContactAddress()
				.setPincode(vocandidateEmergencyContact.getCandidateEmergencyContactAddress().getPincode());
		candidateEmergencyContactEntity.getCandidateEmergencyContactAddress()
				.setRemark(vocandidateEmergencyContact.getCandidateEmergencyContactAddress().getRemark());
		// candidateEmergencyContactEntity.getCandidateEmergencyContactAddress().setState(vocandidateEmergencyContact.getCandidateEmergencyContactAddress().getState());
		candidateEmergencyContactEntity.getCandidateEmergencyContactAddress()
				.setUpdatedBy(vocandidateEmergencyContact.getCandidateEmergencyContactAddress().getUpdatedBy());
		candidateEmergencyContactEntity.getCandidateEmergencyContactAddress().setUpdatedDate(new Date());

		return candidateEmergencyContactEntity;
	}

	public static CandidateLanguage convertToUpdateCandidateLanguageEntity(CandidateLanguage candidateLanguageEntity,
			VOCandidateLanguage vocandidateLanguage) {

		/*
		 * candidateLanguageEntity.setCreatedBy(vocandidateLanguage.getCreatedBy());
		 * candidateLanguageEntity.setCreatedDate(vocandidateLanguage.getCreatedDate());
		 */
		candidateLanguageEntity.setIsActive(IHRMSConstants.isActive);
		// candidateLanguageEntity.setLanguageName(vocandidateLanguage.getLanguageName());
		candidateLanguageEntity.setMotherTongue(vocandidateLanguage.getMotherTongue());
		candidateLanguageEntity.setRead(vocandidateLanguage.getRead());
		candidateLanguageEntity.setRemark(vocandidateLanguage.getRemark());
		candidateLanguageEntity.setSpeak(vocandidateLanguage.getSpeak());
		candidateLanguageEntity.setUpdatedBy(vocandidateLanguage.getUpdatedBy());
		candidateLanguageEntity.setUpdatedDate(new Date());
		candidateLanguageEntity.setWrite(vocandidateLanguage.getWrite());
		// candidateLanguageEntity.setLanguage(translateToMasterLanguageEntity(vocandidateLanguage.getLanguage()));

		return candidateLanguageEntity;

	}

	public static CandidatePassportDetail convertToUpdateCandidatePassportDetailEntity(
			CandidatePassportDetail candidatePassportDetailEntity,
			VOCandidatePassportDetail vocandidatePassportDetail) {

		/*
		 * candidatePassportDetailEntity.setCreatedBy(vocandidatePassportDetail.
		 * getCreatedBy());
		 * candidatePassportDetailEntity.setCreatedDate(vocandidatePassportDetail.
		 * getCreatedDate());
		 */
		candidatePassportDetailEntity.setDateOfExpiry(vocandidatePassportDetail.getDateOfExpiry());
		candidatePassportDetailEntity.setDateOfIssue(vocandidatePassportDetail.getDateOfIssue());
		candidatePassportDetailEntity.setEcnr(vocandidatePassportDetail.getEcnr());
		candidatePassportDetailEntity.setIsActive(IHRMSConstants.isActive);
		candidatePassportDetailEntity.setPassportFirstName(vocandidatePassportDetail.getPassportFirstName());
		candidatePassportDetailEntity.setPassportMiddleName(vocandidatePassportDetail.getPassportMiddleName());
		candidatePassportDetailEntity.setPassportLastName(vocandidatePassportDetail.getPassportLastName());
		candidatePassportDetailEntity.setPassportNumber(vocandidatePassportDetail.getPassportNumber());
		candidatePassportDetailEntity.setPlaceOfIssue(vocandidatePassportDetail.getPlaceOfIssue());
		candidatePassportDetailEntity.setRemark(vocandidatePassportDetail.getRemark());
		candidatePassportDetailEntity.setUpdatedBy(vocandidatePassportDetail.getUpdatedBy());
		candidatePassportDetailEntity.setUpdatedDate(new Date());

		return candidatePassportDetailEntity;

	}

	public static CandidateVisaDetail convertToUpdateCandidateVisaDetailEntity(
			CandidateVisaDetail candidateVisaDetailEntity, VOCandidateVisaDetail vocandidateVisaDetail) {

		candidateVisaDetailEntity.setCountry(vocandidateVisaDetail.getCountry());
		/*
		 * candidateVisaDetailEntity.setCreatedBy(vocandidateVisaDetail.getCreatedBy());
		 * candidateVisaDetailEntity.setCreatedDate(vocandidateVisaDetail.getCreatedDate
		 * ());
		 */
		candidateVisaDetailEntity.setIsActive(IHRMSConstants.isActive);
		candidateVisaDetailEntity.setDateOfExpiry(vocandidateVisaDetail.getDateOfExpiry());
		candidateVisaDetailEntity.setDateOfIssue(vocandidateVisaDetail.getDateOfIssue());
		candidateVisaDetailEntity.setRemark(vocandidateVisaDetail.getRemark());
		candidateVisaDetailEntity.setUpdatedBy(vocandidateVisaDetail.getUpdatedBy());
		candidateVisaDetailEntity.setUpdatedDate(new Date());
		candidateVisaDetailEntity.setVisaNumber(vocandidateVisaDetail.getVisaNumber());
		candidateVisaDetailEntity.setVisaType(vocandidateVisaDetail.getVisaType());
		return candidateVisaDetailEntity;

	}

	public static CandidatePolicyDetail convertToUpdateCandidatePolicyDetailEntity(
			CandidatePolicyDetail candidatePolicyDetailEntity, VOCandidatePolicyDetail vocandidatePolicyDetail) {

		/*
		 * candidatePolicyDetailEntity.setCreatedBy(vocandidatePolicyDetail.getCreatedBy
		 * ()); candidatePolicyDetailEntity.setCreatedDate(vocandidatePolicyDetail.
		 * getCreatedDate());
		 */
		candidatePolicyDetailEntity.setDateOfExpiry(vocandidatePolicyDetail.getDateOfExpiry());
		candidatePolicyDetailEntity.setEmployeeMembershipId(vocandidatePolicyDetail.getEmployeeMembershipId());
		candidatePolicyDetailEntity.setInsuranceCompany(vocandidatePolicyDetail.getInsuranceCompany());
		candidatePolicyDetailEntity.setInsuranceType(vocandidatePolicyDetail.getInsuranceType());
		candidatePolicyDetailEntity.setIsActive(IHRMSConstants.isActive);
		candidatePolicyDetailEntity.setPolicyName(vocandidatePolicyDetail.getPolicyName());
		candidatePolicyDetailEntity.setPolicyNumber(vocandidatePolicyDetail.getPolicyNumber());
		candidatePolicyDetailEntity.setRemark(vocandidatePolicyDetail.getRemark());
		candidatePolicyDetailEntity.setStartDate(vocandidatePolicyDetail.getStartDate());
		candidatePolicyDetailEntity.setSumInsured(vocandidatePolicyDetail.getSumInsured());
		candidatePolicyDetailEntity.setTpa(vocandidatePolicyDetail.getTpa());
		candidatePolicyDetailEntity.setTpaName(vocandidatePolicyDetail.getTpaName());
		candidatePolicyDetailEntity.setUpdatedBy(vocandidatePolicyDetail.getUpdatedBy());
		candidatePolicyDetailEntity.setUpdatedDate(new Date());

		return candidatePolicyDetailEntity;

	}

	public static CandidatePolicyDetail convertToCandidatePolicyDetailEntity(
			VOCandidatePolicyDetail vocandidatePolicyDetail) {

		CandidatePolicyDetail candidatePolicyDetailEntity = new CandidatePolicyDetail();

		candidatePolicyDetailEntity.setCreatedBy(vocandidatePolicyDetail.getCreatedBy());
		candidatePolicyDetailEntity.setCreatedDate(new Date());
		candidatePolicyDetailEntity.setDateOfExpiry(vocandidatePolicyDetail.getDateOfExpiry());
		candidatePolicyDetailEntity.setEmployeeMembershipId(vocandidatePolicyDetail.getEmployeeMembershipId());
		candidatePolicyDetailEntity.setInsuranceCompany(vocandidatePolicyDetail.getInsuranceCompany());
		candidatePolicyDetailEntity.setInsuranceType(vocandidatePolicyDetail.getInsuranceType());
		candidatePolicyDetailEntity.setIsActive(IHRMSConstants.isActive);
		candidatePolicyDetailEntity.setPolicyName(vocandidatePolicyDetail.getPolicyName());
		candidatePolicyDetailEntity.setPolicyNumber(vocandidatePolicyDetail.getPolicyNumber());
		candidatePolicyDetailEntity.setRemark(vocandidatePolicyDetail.getRemark());
		candidatePolicyDetailEntity.setStartDate(vocandidatePolicyDetail.getStartDate());
		candidatePolicyDetailEntity.setSumInsured(vocandidatePolicyDetail.getSumInsured());
		candidatePolicyDetailEntity.setTpa(vocandidatePolicyDetail.getTpa());
		candidatePolicyDetailEntity.setTpaName(vocandidatePolicyDetail.getTpaName());
		/*
		 * candidatePolicyDetailEntity.setUpdatedBy(vocandidatePolicyDetail.getUpdatedBy
		 * ()); candidatePolicyDetailEntity.setUpdatedDate(vocandidatePolicyDetail.
		 * getUpdatedDate());
		 */

		return candidatePolicyDetailEntity;

	}

	public static CandidateStatutoryNomination convertToUpdateCandidateStatutoryNominationEntity(
			CandidateStatutoryNomination candidateStatutoryNominationEntity,
			VOCandidateStatutoryNomination vocandidateStatutoryNomination) {

		candidateStatutoryNominationEntity.setAge(vocandidateStatutoryNomination.getAge());
		candidateStatutoryNominationEntity.setCreatedBy(vocandidateStatutoryNomination.getCreatedBy());
		candidateStatutoryNominationEntity.setCreatedDate(vocandidateStatutoryNomination.getCreatedDate());
		candidateStatutoryNominationEntity.setDateOfBirth(vocandidateStatutoryNomination.getDateOfBirth());
		candidateStatutoryNominationEntity.setNomineeName(vocandidateStatutoryNomination.getNomineeName());
		candidateStatutoryNominationEntity.setPercentage(vocandidateStatutoryNomination.getPercentage());
		candidateStatutoryNominationEntity.setRelationship(vocandidateStatutoryNomination.getRelationship());
		candidateStatutoryNominationEntity.setRemark(vocandidateStatutoryNomination.getRemark());
		candidateStatutoryNominationEntity.setType(vocandidateStatutoryNomination.getType());
		candidateStatutoryNominationEntity.setUpdatedBy(vocandidateStatutoryNomination.getUpdatedBy());
		candidateStatutoryNominationEntity.setUpdatedDate(vocandidateStatutoryNomination.getUpdatedDate());

		return candidateStatutoryNominationEntity;

	}

	public static CandidateStatutoryNomination convertToCandidateStatutoryNominationEntity(
			VOCandidateStatutoryNomination vocandidateStatutoryNomination) {

		CandidateStatutoryNomination candidateStatutoryNominationEntity = new CandidateStatutoryNomination();
		candidateStatutoryNominationEntity.setAge(vocandidateStatutoryNomination.getAge());
		candidateStatutoryNominationEntity.setCreatedBy(vocandidateStatutoryNomination.getCreatedBy());
		candidateStatutoryNominationEntity.setCreatedDate(vocandidateStatutoryNomination.getCreatedDate());
		candidateStatutoryNominationEntity.setDateOfBirth(vocandidateStatutoryNomination.getDateOfBirth());
		candidateStatutoryNominationEntity.setNomineeName(vocandidateStatutoryNomination.getNomineeName());
		candidateStatutoryNominationEntity.setPercentage(vocandidateStatutoryNomination.getPercentage());
		candidateStatutoryNominationEntity.setRelationship(vocandidateStatutoryNomination.getRelationship());
		candidateStatutoryNominationEntity.setRemark(vocandidateStatutoryNomination.getRemark());
		candidateStatutoryNominationEntity.setType(vocandidateStatutoryNomination.getType());
		candidateStatutoryNominationEntity.setUpdatedBy(vocandidateStatutoryNomination.getUpdatedBy());
		candidateStatutoryNominationEntity.setUpdatedDate(vocandidateStatutoryNomination.getUpdatedDate());

		return candidateStatutoryNominationEntity;

	}

	public static CandidateHealthReport translateCandidateHealthReportVoToEntityReq(
			CandidateHealthReport candidateHealthReportEntity, VOCandidateHealthReport voCandidateHealthReport) {

		candidateHealthReportEntity.setAllergy(voCandidateHealthReport.getAllergy());
		candidateHealthReportEntity.setBloodGroup(voCandidateHealthReport.getBloodGroup());
		// candidateHealthReportEntity.setCandidatePersonalDetail(voCandidateHealthReport.getCandidatePersonalDetail());
		// candidateHealthReportEntity.setCreatedBy(voCandidateHealthReport.getCreatedBy());
		// candidateHealthReportEntity.setCreatedDate(voCandidateHealthReport.getCreatedDate());
		candidateHealthReportEntity.setHospitalization(voCandidateHealthReport.getHospitalization());
		// candidateHealthReportEntity.setId(voCandidateHealthReport.getId());
		candidateHealthReportEntity.setIdentificationMark(voCandidateHealthReport.getIdentificationMark());
		candidateHealthReportEntity.setInterestedToDonateBlood(voCandidateHealthReport.getInterestedToDonateBlood());
		candidateHealthReportEntity.setIsActive(voCandidateHealthReport.getIsActive());
		candidateHealthReportEntity.setPhysicallyHandicapped(voCandidateHealthReport.getPhysicallyHandicapped());
		candidateHealthReportEntity.setRemark(voCandidateHealthReport.getRemark());
		candidateHealthReportEntity.setSeverelyHandicapped(voCandidateHealthReport.getSeverelyHandicapped());
		candidateHealthReportEntity.setSurgery(voCandidateHealthReport.getSurgery());
		candidateHealthReportEntity.setUpdatedBy(voCandidateHealthReport.getUpdatedBy());
		candidateHealthReportEntity.setUpdatedDate(new java.util.Date());
		candidateHealthReportEntity.setVisionProblem(voCandidateHealthReport.getVisionProblem());
		candidateHealthReportEntity.setHealthHistory(voCandidateHealthReport.getHealthHistory());
		candidateHealthReportEntity
				.setPhysicallyHandicapDescription(voCandidateHealthReport.getPhysicallyHandicapDiscription());
		candidateHealthReportEntity.setAllergyDescription(voCandidateHealthReport.getAllergyDiscription());
		candidateHealthReportEntity.setSurgeryDescription(voCandidateHealthReport.getSurgeryDiscription());
		candidateHealthReportEntity.setHealthHistoryDescription(voCandidateHealthReport.getHealthHistoryDiscription());
		return candidateHealthReportEntity;
	}

	public static CandidateHealthReport translateToCandidateHealthReportReq(CandidatePersonalDetail

	candidatePersonalDetailEntity, VOCandidateHealthReport voCandidateHealthReport) {

		CandidateHealthReport candidateHealthReportEntity = new CandidateHealthReport();
		// CandidatePersonalDetail candidatePersonalDetailEntity = new
		// CandidatePersonalDetail();
		// Candidate candidateEntity = new Candidate();

		// candidateEntity.setId(voCandidateHealthReport.getCandidatePersonalDetail().getCandidate().getId());
		// candidatePersonalDetailEntity.setCandidate(candidateEntity);

		candidateHealthReportEntity.setAllergy(voCandidateHealthReport.getAllergy());
		candidateHealthReportEntity.setBloodGroup(voCandidateHealthReport.getBloodGroup());
		candidateHealthReportEntity.setCandidatePersonalDetail(candidatePersonalDetailEntity);
		candidateHealthReportEntity.setCreatedBy(voCandidateHealthReport.getCreatedBy());
		candidateHealthReportEntity.setCreatedDate(new Date());
		candidateHealthReportEntity.setHospitalization(voCandidateHealthReport.getHospitalization());
		candidateHealthReportEntity.setId(voCandidateHealthReport.getId());
		candidateHealthReportEntity.setIdentificationMark(voCandidateHealthReport.getIdentificationMark());
		candidateHealthReportEntity.setInterestedToDonateBlood(voCandidateHealthReport.getInterestedToDonateBlood());
		candidateHealthReportEntity.setIsActive(voCandidateHealthReport.getIsActive());
		candidateHealthReportEntity.setPhysicallyHandicapped(voCandidateHealthReport.getPhysicallyHandicapped());
		candidateHealthReportEntity.setRemark(voCandidateHealthReport.getRemark());
		candidateHealthReportEntity.setSeverelyHandicapped(voCandidateHealthReport.getSeverelyHandicapped());
		candidateHealthReportEntity.setSurgery(voCandidateHealthReport.getSurgery());
		// candidateHealthReportEntity.setUpdatedBy(voCandidateHealthReport.getUpdatedBy());
		// candidateHealthReportEntity.setUpdatedDate(voCandidateHealthReport.getUpdatedDate());
		candidateHealthReportEntity.setVisionProblem(voCandidateHealthReport.getVisionProblem());
		candidateHealthReportEntity.setHealthHistory(voCandidateHealthReport.getHealthHistory());
		return candidateHealthReportEntity;
	}

	public static CandidateQualification translateToPreviousQualificationEntity(
			CandidateQualification qualificationEntity, VOCandidateQualification qualificationModel) {

		qualificationEntity.setAcademicAchievements(qualificationModel.getAcademicAchievements());
		qualificationEntity.setBoardUniversity(qualificationModel.getBoardUniversity());

		qualificationEntity.setDegree(qualificationModel.getDegree());
		qualificationEntity.setGradeDivisionPercentage(qualificationModel.getGradeDivisionPercentage());
		qualificationEntity.setInstituteName(qualificationModel.getInstituteName());
		qualificationEntity.setIsActive(qualificationModel.getIsActive());
		qualificationEntity.setModeOfEducation(qualificationModel.getModeOfEducation());
		qualificationEntity.setPassingYearMonth(qualificationModel.getPassingYearMonth());
		qualificationEntity.setQualification(qualificationModel.getQualification());
		qualificationEntity.setRemark(qualificationModel.getRemark());
		qualificationEntity.setStateLocation(qualificationModel.getStateLocation());
		qualificationEntity.setSubjectOfSpecialization(qualificationModel.getSubjectOfSpecialization());
		qualificationEntity.setAcademicAchievements(qualificationModel.getAcademicAchievements());
		qualificationEntity.setBoardUniversity(qualificationModel.getBoardUniversity());

		if (qualificationModel.getId() > 0) {

			// TO UPDATE QUALIFICATION
			qualificationEntity.setUpdatedBy(qualificationModel.getUpdatedBy());
			qualificationEntity.setUpdatedDate(new Date());

		} else {

			// TO CREATE A QUALIFICATION
			Candidate candidateEntity = new Candidate();
			candidateEntity.setId(qualificationModel.getCandidateProfessionalDetail().getCandidate().getId());
			CandidateProfessionalDetail professionalDetailsEntity = new CandidateProfessionalDetail();
			professionalDetailsEntity.setCandidate(candidateEntity);
			qualificationEntity.setCandidateProfessionalDetail(professionalDetailsEntity);

			qualificationEntity.setCreatedBy(qualificationModel.getCreatedBy());
			qualificationEntity.setCreatedDate(new Date());

		}

		return qualificationEntity;
	}

	public static MasterBranch translateToMasterBranchEntity(MasterBranch masterBranchEntity,
			VOMasterBranch voMasterBranch) {

		masterBranchEntity.setBranchDescription(voMasterBranch.getBranchDescription());
		masterBranchEntity.setBranchName(voMasterBranch.getBranchName());

		masterBranchEntity.setIsActive(IHRMSConstants.isActive);
		masterBranchEntity.setOrganization(masterBranchEntity.getOrganization());
		masterBranchEntity.setRemark(voMasterBranch.getRemark());
		if (masterBranchEntity.getId() == 0) {
			masterBranchEntity.setCreatedBy(voMasterBranch.getCreatedBy());
			masterBranchEntity.setCreatedDate(new Date());
		} else {
			masterBranchEntity.setUpdatedBy(voMasterBranch.getUpdatedBy());
			masterBranchEntity.setUpdatedDate(new Date());

		}

		return masterBranchEntity;
	}

	public static Candidate updateCandidateEntity(Candidate candidateEntity, VOCandidate candidateModel) {

		logger.info("==== >> UPDATING CANDIDATE << ====");

		/*
		 * PERSONAL DETAILS DETAILS
		 */
		CandidatePersonalDetail personalDetails = candidateEntity.getCandidatePersonalDetail();
		personalDetails.setMaritalStatus(candidateModel.getCandidatePersonalDetail().getMaritalStatus());
		personalDetails.setUpdatedBy(candidateModel.getUpdatedBy());
		personalDetails.setUpdatedDate(new Date());

		/*
		 * PROFESSIONAL DETAILS
		 */
		CandidateProfessionalDetail candidateProfessionalDetailsEntity = candidateEntity
				.getCandidateProfessionalDetail();

		if (!HRMSHelper.isNullOrEmpty(candidateModel.getCandidateProfessionalDetail())) {

			candidateProfessionalDetailsEntity
					.setAadhaarCard(candidateModel.getCandidateProfessionalDetail().getAadhaarCard());
			candidateProfessionalDetailsEntity
					.setRequisitionDate(candidateModel.getCandidateProfessionalDetail().getRequisitionDate());
			candidateProfessionalDetailsEntity.setRemark(candidateModel.getCandidateProfessionalDetail().getRemark());
			// candidateProfessionalDetailsEntity.setRecruiterName(candidateModel.getCandidateProfessionalDetail().getRecruiterName());
			// candidateProfessionalDetailsEntity.setRecruiter(convertToEmployeeEntityFromVO(candidateModel.getCandidateProfessionalDetail().getRecruiter()));
			candidateProfessionalDetailsEntity.setPanCard(candidateModel.getCandidateProfessionalDetail().getPanCard());
			candidateProfessionalDetailsEntity
					.setMarkLetterTo(candidateModel.getCandidateProfessionalDetail().getMarkLetterTo());
			candidateProfessionalDetailsEntity.setIsActive(IHRMSConstants.isActive);
			candidateProfessionalDetailsEntity
					.setDateOfJoining(candidateModel.getCandidateProfessionalDetail().getDateOfJoining());
			candidateProfessionalDetailsEntity.setComment(candidateModel.getCandidateProfessionalDetail().getComment());
			candidateProfessionalDetailsEntity
					.setDateOfReported(candidateModel.getCandidateProfessionalDetail().getDateOfReported());
			candidateProfessionalDetailsEntity
					.setHrStatus(candidateModel.getCandidateProfessionalDetail().getHrStatus());
			candidateProfessionalDetailsEntity.setStatus(candidateModel.getCandidateProfessionalDetail().getStatus());
			candidateProfessionalDetailsEntity
					.setShortlistDate(candidateModel.getCandidateProfessionalDetail().getShortlistDate());
			candidateProfessionalDetailsEntity
					.setReported(candidateModel.getCandidateProfessionalDetail().getReported());
			candidateProfessionalDetailsEntity.setUan(candidateModel.getCandidateProfessionalDetail().getUan());
			candidateProfessionalDetailsEntity.setUpdatedBy(candidateModel.getUpdatedBy());
			candidateProfessionalDetailsEntity.setUpdatedDate(new Date());
		}

		/*
		 * Candidate Entity
		 */
		candidateEntity.setDateOfBirth(candidateModel.getDateOfBirth());
		candidateEntity.setEmailId(candidateModel.getEmailId());
		candidateEntity.setFirstName(candidateModel.getFirstName());
		candidateEntity.setGender(candidateModel.getGender());
		candidateEntity.setIsActive(IHRMSConstants.isActive);
		candidateEntity.setLastName(candidateModel.getLastName());
		candidateEntity.setMiddleName(candidateModel.getMiddleName());
		candidateEntity.setMobileNumber(candidateModel.getMobileNumber());
		candidateEntity.setTitle(candidateModel.getTitle());
		// candidateEntity.setCandidateStatus(candidateModel.getCandidateStatus());
		candidateEntity.setCandidateProfessionalDetail(candidateProfessionalDetailsEntity);
		candidateEntity.setUpdatedBy(candidateModel.getUpdatedBy());
		candidateEntity.setUpdatedDate(new Date());
		candidateEntity.setNoticePeriod(candidateModel.getNoticePeriod());

		return candidateEntity;
	}

	public static MasterCandidateActivity translateToMasterCandidateActivityEntity(
			MasterCandidateActivity masterCandidateActivityEntity,
			VOMasterCandidateActivity voMasterCandidateActivity) {

		masterCandidateActivityEntity.setDescription(voMasterCandidateActivity.getDescription());
		masterCandidateActivityEntity.setIsActive(IHRMSConstants.isActive);
		masterCandidateActivityEntity.setName(voMasterCandidateActivity.getName());
		masterCandidateActivityEntity.setOrganization(masterCandidateActivityEntity.getOrganization());

		if (masterCandidateActivityEntity.getId() == 0) {
			masterCandidateActivityEntity.setCreatedBy(voMasterCandidateActivity.getCreatedBy());
			masterCandidateActivityEntity.setCreatedDate(new Date());
		} else {
			masterCandidateActivityEntity.setUpdatedBy(voMasterCandidateActivity.getUpdatedBy());
			masterCandidateActivityEntity.setUpdatedDate(new Date());
		}
		return masterCandidateActivityEntity;
	}

	public static MasterDepartment translateToMasterDepartmentEntity(MasterDepartment masterDepartmentEntity,
			VOMasterDepartment voMasterDepartment) {

		masterDepartmentEntity.setDepartmentDescription(voMasterDepartment.getDepartmentDescription());
		masterDepartmentEntity.setIsActive(IHRMSConstants.isActive);
		masterDepartmentEntity.setDepartmentName(voMasterDepartment.getDepartmentName());
		masterDepartmentEntity.setOrganization(masterDepartmentEntity.getOrganization());

		if (masterDepartmentEntity.getId() == 0) {
			masterDepartmentEntity.setCreatedBy(voMasterDepartment.getCreatedBy());
			masterDepartmentEntity.setCreatedDate(new Date());
		} else {
			masterDepartmentEntity.setUpdatedBy(voMasterDepartment.getUpdatedBy());
			masterDepartmentEntity.setUpdatedDate(new Date());
		}
		return masterDepartmentEntity;
	}

	public static MasterCandidateOnboardActionReason translateToMasterOnboardActionReasonEntity(
			MasterCandidateOnboardActionReason onboardActionReasonEntity,
			VOMasterOnboardActionReason voMasterOnboardActionReason) {

		onboardActionReasonEntity
				.setOnboardActionReasonDescription(voMasterOnboardActionReason.getOnboardActionReasonDescription());
		onboardActionReasonEntity.setOnboardActionReasonName(voMasterOnboardActionReason.getOnboardActionReasonName());
		onboardActionReasonEntity.setIsActive(IHRMSConstants.isActive);
		onboardActionReasonEntity.setOrganization(onboardActionReasonEntity.getOrganization());
		onboardActionReasonEntity.setRemark(voMasterOnboardActionReason.getRemark());
		onboardActionReasonEntity.setTypeOfAction(voMasterOnboardActionReason.getRemark());
		if (onboardActionReasonEntity.getId() == 0) {
			onboardActionReasonEntity.setCreatedBy(voMasterOnboardActionReason.getCreatedBy());
			onboardActionReasonEntity.setCreatedDate(new Date());
		} else {
			onboardActionReasonEntity.setUpdatedBy(voMasterOnboardActionReason.getUpdatedBy());
			onboardActionReasonEntity.setUpdatedDate(new Date());
		}
		return onboardActionReasonEntity;
	}

	public static MasterDesignation translateToMasterDesignationEntity(MasterDesignation masterDesignationEntity,
			VOMasterDesignation voMasterDesignation) {

		masterDesignationEntity.setDesignationDescription(voMasterDesignation.getDesignationDescription());
		masterDesignationEntity.setDesignationName(voMasterDesignation.getDesignationName());

		masterDesignationEntity.setIsActive(IHRMSConstants.isActive);
		masterDesignationEntity.setOrganization(masterDesignationEntity.getOrganization());

		// masterDesignationEntity.setOrganization(voMasterDesignation.getOrganization().getId());

// masterDesignationEntity.setId((long) 198);

//		if (masterDesignationEntity.getId() == null) {
//			masterDesignationEntity.setCreatedBy(voMasterDesignation.getCreatedBy());
//			masterDesignationEntity.setCreatedDate(new Date());
//		} else {
//			masterDesignationEntity.setUpdatedBy(voMasterDesignation.getUpdatedBy());
//			masterDesignationEntity.setUpdatedDate(new Date());
//
//		}

		return masterDesignationEntity;
	}

//	public static MasterDesignation translateToMasterDesignationEdit(MasterDesignation masterDesignationEntity,
//			MasterDesignationVO voMasterDesignation) {
//		
//		
//		masterDesignationEntity.setDesignationDescription(voMasterDesignation.getDesignationDescription());
//		masterDesignationEntity.setDesignationName(voMasterDesignation.getDesignationName());
//
//		masterDesignationEntity.setIsActive(IHRMSConstants.isActive);
//		masterDesignationEntity.setOrganization(masterDesignationEntity.getOrganization());
//
//
//		return masterDesignationEntity;
//	}
//	

	public static MasterDivision translateToMasterDivisionEntity(MasterDivision masterDivisionEntity,
			VOMasterDivision voMasterDivision) {

		masterDivisionEntity.setDivisionDescription(voMasterDivision.getDivisionDescription());
		masterDivisionEntity.setDivisionName(voMasterDivision.getDivisionName());

		masterDivisionEntity.setIsActive(IHRMSConstants.isActive);
		masterDivisionEntity.setOrganization(masterDivisionEntity.getOrganization());
		if (masterDivisionEntity.getId() == 0) {
			masterDivisionEntity.setCreatedBy(voMasterDivision.getCreatedBy());
			masterDivisionEntity.setCreatedDate(new Date());
		} else {
			masterDivisionEntity.setUpdatedBy(voMasterDivision.getUpdatedBy());
			masterDivisionEntity.setUpdatedDate(new Date());

		}

		return masterDivisionEntity;
	}

	public static MasterCountry translateToMasterCountryEntity(MasterCountry masterCountryEntity,
			VOMasterCountry voMasterCountry) {

		masterCountryEntity.setCountryDescription(voMasterCountry.getCountryDescription());
		masterCountryEntity.setCountryName(voMasterCountry.getCountryName());
		masterCountryEntity.setIsActive(voMasterCountry.getIsActive());
		if (masterCountryEntity.getId() == 0) {
			masterCountryEntity.setCreatedBy(voMasterCountry.getCreatedBy());
			masterCountryEntity.setCreatedDate(new Date());
		} else {
			masterCountryEntity.setUpdatedBy(voMasterCountry.getUpdatedBy());
			masterCountryEntity.setUpdatedDate(new Date());

		}
		return masterCountryEntity;
	}

	public static MasterState translateToMasterStateEntity(MasterState masterStateEntity, VOMasterState voMasterState) {

		masterStateEntity.setStateDescription(voMasterState.getStateDescription());
		masterStateEntity.setStateName(voMasterState.getStateName());

		masterStateEntity.setIsActive(IHRMSConstants.isActive);
		masterStateEntity.setMasterCountry(masterStateEntity.getMasterCountry());
		if (masterStateEntity.getId() == 0) {
			masterStateEntity.setCreatedBy(voMasterState.getCreatedBy());
			masterStateEntity.setCreatedDate(new Date());
		} else {
			masterStateEntity.setUpdatedBy(voMasterState.getUpdatedBy());
			masterStateEntity.setUpdatedDate(new Date());

		}
		return masterStateEntity;
	}

	public static MasterCity translateToMasterCityEntity(MasterCity masterCityEntity, VOMasterCity voMasterCity) {

		masterCityEntity.setCityDescription(voMasterCity.getCityDescription());
		masterCityEntity.setCityName(voMasterCity.getCityName());
		masterCityEntity.setIsActive(IHRMSConstants.isActive);
		masterCityEntity.setMasterState(masterCityEntity.getMasterState());
		if (masterCityEntity.getId() == 0) {
			masterCityEntity.setCreatedBy(voMasterCity.getCreatedBy());
			masterCityEntity.setCreatedDate(new Date());
		} else {
			masterCityEntity.setUpdatedBy(voMasterCity.getUpdatedBy());
			masterCityEntity.setUpdatedDate(new Date());
		}
		return masterCityEntity;
	}

	public static CandidateActivity translateToCandidateActivityEntity(CandidateActivity candidateActivityEntity,
			VOCandidateActivity voCandidateActivity) {
		candidateActivityEntity.setActivityResponseDate(voCandidateActivity.getActivityResponseDate());
		candidateActivityEntity.setActivityTriggredDate(voCandidateActivity.getActivityTriggredDate());
		candidateActivityEntity.setHrComment(voCandidateActivity.getHrComment());
		candidateActivityEntity.setHrStatus(voCandidateActivity.getHrStatus());
		candidateActivityEntity.setEmailStatus(voCandidateActivity.getEmailStatus());
		if (candidateActivityEntity.getId() == 0) {
			candidateActivityEntity.setIsActive(IHRMSConstants.isActive);
			candidateActivityEntity.setCreatedBy(voCandidateActivity.getCreatedBy());
			candidateActivityEntity.setCreatedDate(new Date());
		} else {
			candidateActivityEntity.setIsActive(voCandidateActivity.getIsActive());
			candidateActivityEntity.setUpdatedBy(voCandidateActivity.getUpdatedBy());
			candidateActivityEntity.setUpdatedDate(new Date());
		}
		return candidateActivityEntity;
	}

	public static Employee crateEmployeeEntity(VOEmployee employeeModel) {

		Employee employeeEntity = new Employee();

		employeeEntity.setOfficialEmailId(employeeModel.getOfficialEmailId());
		employeeEntity.setPositionCode(employeeModel.getPositionCode());
		employeeEntity.setPositionCodeEffectiveDate(employeeModel.getPositionCodeEffectiveDate());

		return employeeEntity;
	}

	public static VOLoginEntityType translateToLoginEntityTypeResponse(LoginEntityType loginEntityType) {

		VOLoginEntityType model = new VOLoginEntityType();
		model.setDescription(loginEntityType.getDescription());
		model.setDetails(loginEntityType.getDetails());
		model.setId(loginEntityType.getId());
		model.setLoginEntityTypeName(loginEntityType.getLoginEntityTypeName());

		VOOrganization org = new VOOrganization();
		org.setOrganizationName(loginEntityType.getOrganization().getOrganizationName());
		org.setId(loginEntityType.getOrganization().getId());

		Set<Subscription> subscriptionEntitySet = loginEntityType.getOrganization().getSubscription();
		Set<VOSubscription> subscriptionSet = new HashSet<VOSubscription>();
		for (Subscription subscription : subscriptionEntitySet) {

			VOSubscription subscriptionModel = HRMSEntityToModelMapper.convertToSubscriptionModel(subscription);
			subscriptionSet.add(subscriptionModel);
		}

		org.setSubscription(subscriptionSet);
		model.setOrganization(org);
		// model.sets

		return model;

	}

	public static MasterEmploymentType translateToMasterEmploymentTypeEntity(
			MasterEmploymentType masterEmploymentTypeEntity, VOMasterEmploymentType voMasterEmploymentType) {

		masterEmploymentTypeEntity.setEmploymentTypeDescription(voMasterEmploymentType.getEmploymentTypeDescription());
		masterEmploymentTypeEntity.setEmploymentTypeName(voMasterEmploymentType.getEmploymentTypeName());
		masterEmploymentTypeEntity.setIsActive(IHRMSConstants.isActive);
		masterEmploymentTypeEntity.setOrganization(masterEmploymentTypeEntity.getOrganization());
		if (masterEmploymentTypeEntity.getId() == 0) {
			masterEmploymentTypeEntity.setCreatedBy(voMasterEmploymentType.getCreatedBy());
			masterEmploymentTypeEntity.setCreatedDate(new Date());
		} else {
			masterEmploymentTypeEntity.setUpdatedBy(voMasterEmploymentType.getUpdatedBy());
			masterEmploymentTypeEntity.setUpdatedDate(new Date());

		}

		return masterEmploymentTypeEntity;
	}

	public static MasterLeaveType translateToMasterLeaveTypeEntity(MasterLeaveType masterLeaveTypeEntity,
			VOMasterLeaveType voMasterLeaveType) {

		masterLeaveTypeEntity.setLeaveTypeDescription(voMasterLeaveType.getLeaveTypeDescription());
		masterLeaveTypeEntity.setLeaveTypeName(voMasterLeaveType.getLeaveTypeName());
		masterLeaveTypeEntity.setNumberOfSession(voMasterLeaveType.getNumberOfSession());
		masterLeaveTypeEntity.setIsActive(IHRMSConstants.isActive);
		masterLeaveTypeEntity.setOrganization(masterLeaveTypeEntity.getOrganization());
		// masterLeaveTypeEntity.setDivision(masterLeaveTypeEntity.getDivision());
		// masterLeaveTypeEntity.setBranch(masterLeaveTypeEntity.getBranch());
		if (masterLeaveTypeEntity.getId() == 0) {
			masterLeaveTypeEntity.setCreatedBy(voMasterLeaveType.getCreatedBy());
			masterLeaveTypeEntity.setCreatedDate(new Date());
		} else {
			masterLeaveTypeEntity.setUpdatedBy(voMasterLeaveType.getUpdatedBy());
			masterLeaveTypeEntity.setUpdatedDate(new Date());

		}

		return masterLeaveTypeEntity;
	}

	public static Employee createEmployeeEntity(Employee employeeEntity, VOCreateEmployeeRequest employeeRequest) {

		// Employee employeeEntity =new Employee();
		employeeEntity.setId(employeeRequest.getEmployee().getId());

		employeeEntity.setOfficialEmailId(employeeRequest.getEmployee().getOfficialEmailId());
		employeeEntity.setPositionCode(employeeRequest.getEmployee().getPositionCode());
		employeeEntity.setPositionCodeEffectiveDate(employeeRequest.getEmployee().getPositionCodeEffectiveDate());
		employeeEntity.setProbationPeriod(employeeRequest.getEmployee().getEmployeeProbationPeriod());
		/*
		 * employeeEntity.setEmployeeBranch(null);
		 * employeeEntity.setEmployeeCreditLeaveDetails(null);
		 * employeeEntity.setEmployeeCurrentDetail(null);
		 * employeeEntity.setEmployeeDepartment( null);
		 * employeeEntity.setEmployeeDesignation( null);
		 * employeeEntity.setEmployeeDivision(null);
		 * employeeEntity.setEmployeeLeaveApplieds(null);
		 * employeeEntity.setEmployeeLeaveDetails(null);
		 * employeeEntity.setEmployeeOrgnizationalAssets(null);
		 */

		employeeEntity.setIsActive(IHRMSConstants.isActive);
		employeeEntity.setCreatedBy(employeeRequest.getCreatedBy());
		employeeEntity.setCreatedDate(new Date());

		return employeeEntity;
	}

	public static CandidateActivityActionDetail translateToCandidateActivityActionEntity(
			CandidateActivityActionDetail candidateActivityActionDetailEntity,
			VOCandidateActivityActionDetail voCandidateActivityActionDetail) {
		candidateActivityActionDetailEntity.setHrActedOn(voCandidateActivityActionDetail.getHrActedOn());
		candidateActivityActionDetailEntity.setHrComment(voCandidateActivityActionDetail.getHrComment());
		candidateActivityActionDetailEntity.setTypeOfAction(voCandidateActivityActionDetail.getTypeOfAction());
		if (candidateActivityActionDetailEntity.getId() == 0) {
			candidateActivityActionDetailEntity.setIsActive(IHRMSConstants.isActive);
			candidateActivityActionDetailEntity.setCreatedBy(voCandidateActivityActionDetail.getCreatedBy());
			candidateActivityActionDetailEntity.setCreatedDate(new Date());
		} else {
			candidateActivityActionDetailEntity.setIsActive(voCandidateActivityActionDetail.getIsActive());
			candidateActivityActionDetailEntity.setUpdatedBy(voCandidateActivityActionDetail.getUpdatedBy());
			candidateActivityActionDetailEntity.setUpdatedDate(new Date());
		}
		return candidateActivityActionDetailEntity;
	}

	public static MasterCandidateActivityActionType translateToMasterCandidateActivityActionTypeEntity(
			MasterCandidateActivityActionType masterCandidateActivityActionTypeEntity,
			VOMasterCandidateActivityActionType voMasterCandidateActivityActionType) {
		masterCandidateActivityActionTypeEntity
				.setActivityActionTypeName(voMasterCandidateActivityActionType.getActivityActionTypeName());
		masterCandidateActivityActionTypeEntity.setActivityActionTypeDescription(
				voMasterCandidateActivityActionType.getActivityActionTypeDescription());
		if (masterCandidateActivityActionTypeEntity.getId() == 0) {
			masterCandidateActivityActionTypeEntity.setIsActive(IHRMSConstants.isActive);
			masterCandidateActivityActionTypeEntity.setCreatedBy(voMasterCandidateActivityActionType.getCreatedBy());
			masterCandidateActivityActionTypeEntity.setCreatedDate(new Date());
		} else {
			masterCandidateActivityActionTypeEntity.setIsActive(voMasterCandidateActivityActionType.getIsActive());
			masterCandidateActivityActionTypeEntity.setUpdatedBy(voMasterCandidateActivityActionType.getUpdatedBy());
			masterCandidateActivityActionTypeEntity.setUpdatedDate(new Date());
		}
		return masterCandidateActivityActionTypeEntity;
	}

	public static EmployeeGrantLeaveDetail translateToEmployeeGrantLeaveDetailEntity(
			EmployeeGrantLeaveDetail employeeGrantLeaveDetailEntity,
			VOEmployeeGrantLeaveDetail voEmployeeGrantLeaveDetail) {
		employeeGrantLeaveDetailEntity.setAppliedBy(voEmployeeGrantLeaveDetail.getAppliedBy());
		employeeGrantLeaveDetailEntity
				.setApproverActionDateWithdrawn(voEmployeeGrantLeaveDetail.getApproverActionDateWithdrawn());
		employeeGrantLeaveDetailEntity
				.setApproverCommentOnWithdrawn(voEmployeeGrantLeaveDetail.getApproverCommentOnWithdrawn());
		employeeGrantLeaveDetailEntity.setAttachment(voEmployeeGrantLeaveDetail.getAttachment());
		employeeGrantLeaveDetailEntity.setCc(voEmployeeGrantLeaveDetail.getCc());
		employeeGrantLeaveDetailEntity.setContactDetails(voEmployeeGrantLeaveDetail.getContactDetails());
		employeeGrantLeaveDetailEntity.setDateOfApplied(new Date());
		employeeGrantLeaveDetailEntity.setDateOfApproverAction(voEmployeeGrantLeaveDetail.getDateOfApproverAction());
		employeeGrantLeaveDetailEntity.setDateOfCancel(voEmployeeGrantLeaveDetail.getDateOfCancel());
		employeeGrantLeaveDetailEntity.setDateOfWithdrawn(voEmployeeGrantLeaveDetail.getDateOfWithdrawn());
		employeeGrantLeaveDetailEntity.setFromDate(
				HRMSDateUtil.parse(voEmployeeGrantLeaveDetail.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		employeeGrantLeaveDetailEntity.setFromSession(voEmployeeGrantLeaveDetail.getFromSession());
		employeeGrantLeaveDetailEntity.setLeaveStatus(voEmployeeGrantLeaveDetail.getLeaveStatus());
		employeeGrantLeaveDetailEntity.setReasonForApply(voEmployeeGrantLeaveDetail.getReasonForApply());
		employeeGrantLeaveDetailEntity.setReasonForCancel(voEmployeeGrantLeaveDetail.getReasonForCancel());
		employeeGrantLeaveDetailEntity.setReasonForWithdrawn(voEmployeeGrantLeaveDetail.getReasonForWithdrawn());
		employeeGrantLeaveDetailEntity.setRemark(voEmployeeGrantLeaveDetail.getRemark());
		employeeGrantLeaveDetailEntity.setToDate(
				HRMSDateUtil.parse(voEmployeeGrantLeaveDetail.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		employeeGrantLeaveDetailEntity.setToSession(voEmployeeGrantLeaveDetail.getToSession());
		employeeGrantLeaveDetailEntity.setNoOfDays(voEmployeeGrantLeaveDetail.getNoOfDays());
		if (employeeGrantLeaveDetailEntity.getId() == 0) {
			employeeGrantLeaveDetailEntity.setIsActive(IHRMSConstants.isActive);
			employeeGrantLeaveDetailEntity.setCreatedBy(voEmployeeGrantLeaveDetail.getCreatedBy());
			employeeGrantLeaveDetailEntity.setCreatedDate(new Date());
		} else {
			employeeGrantLeaveDetailEntity.setIsActive(voEmployeeGrantLeaveDetail.getIsActive());
			employeeGrantLeaveDetailEntity.setUpdatedBy(voEmployeeGrantLeaveDetail.getUpdatedBy());
			employeeGrantLeaveDetailEntity.setUpdatedDate(new Date());
		}
		return employeeGrantLeaveDetailEntity;
	}

	public static EmployeeCreditLeaveDetail convertToEmployeeCreditLeaveDetailEntity(
			VOEmployeeCreditLeaveDetail voEmployeeCreditLeaveDetail) {

		EmployeeCreditLeaveDetail employeeCreditLeaveDetailEntity = new EmployeeCreditLeaveDetail();

		employeeCreditLeaveDetailEntity.setCreatedBy(voEmployeeCreditLeaveDetail.getCreatedBy());
		employeeCreditLeaveDetailEntity.setCreatedDate(voEmployeeCreditLeaveDetail.getCreatedDate());
		employeeCreditLeaveDetailEntity.setCreditedBy(voEmployeeCreditLeaveDetail.getCreditedBy());
		employeeCreditLeaveDetailEntity.setFromDate(voEmployeeCreditLeaveDetail.getFromDate());
		employeeCreditLeaveDetailEntity.setIsActive(IHRMSConstants.isActive);
		employeeCreditLeaveDetailEntity.setNoOfDays(voEmployeeCreditLeaveDetail.getNoOfDays());
		employeeCreditLeaveDetailEntity
				.setOpeningBalanceConsidered(voEmployeeCreditLeaveDetail.getOpeningBalanceConsidered());
		employeeCreditLeaveDetailEntity.setRemark(voEmployeeCreditLeaveDetail.getRemark());
		employeeCreditLeaveDetailEntity.setToDate(voEmployeeCreditLeaveDetail.getToDate());
		employeeCreditLeaveDetailEntity.setUpdatedBy(voEmployeeCreditLeaveDetail.getUpdatedBy());
		employeeCreditLeaveDetailEntity.setUpdatedDate(voEmployeeCreditLeaveDetail.getUpdatedDate());
		employeeCreditLeaveDetailEntity.setComment(voEmployeeCreditLeaveDetail.getComment());
		employeeCreditLeaveDetailEntity.setPostedDate(voEmployeeCreditLeaveDetail.getPostedDate());

		return employeeCreditLeaveDetailEntity;

	}

	public static EmployeeCreditLeaveDetail convertToUpdateemployeeCreditLeaveDetail(
			EmployeeCreditLeaveDetail employeeCreditLeaveDetailEntity,
			VOEmployeeCreditLeaveDetail voEmployeeCreditLeaveDetail) {

		employeeCreditLeaveDetailEntity.setFromDate(voEmployeeCreditLeaveDetail.getFromDate());
		employeeCreditLeaveDetailEntity.setToDate(voEmployeeCreditLeaveDetail.getToDate());
		employeeCreditLeaveDetailEntity.setNoOfDays(voEmployeeCreditLeaveDetail.getNoOfDays());
		employeeCreditLeaveDetailEntity.setComment(voEmployeeCreditLeaveDetail.getComment());
		employeeCreditLeaveDetailEntity
				.setOpeningBalanceConsidered(voEmployeeCreditLeaveDetail.getOpeningBalanceConsidered());

		employeeCreditLeaveDetailEntity.setUpdatedBy(voEmployeeCreditLeaveDetail.getUpdatedBy());
		employeeCreditLeaveDetailEntity.setUpdatedDate(voEmployeeCreditLeaveDetail.getUpdatedDate());

		/*
		 * employeeCreditLeaveDetailEntity.setCreatedBy(voEmployeeCreditLeaveDetail.
		 * getCreatedBy());
		 * employeeCreditLeaveDetailEntity.setCreatedDate(voEmployeeCreditLeaveDetail.
		 * getCreatedDate());
		 * employeeCreditLeaveDetailEntity.setCreditedBy(voEmployeeCreditLeaveDetail.
		 * getCreditedBy());
		 * employeeCreditLeaveDetailEntity.setOpeningBalanceConsidered(
		 * voEmployeeCreditLeaveDetail.getOpeningBalanceConsidered());
		 * employeeCreditLeaveDetailEntity.setPostedDate(voEmployeeCreditLeaveDetail.
		 * getPostedDate());
		 */

		return employeeCreditLeaveDetailEntity;

	}

	public static EmployeeCurrentDetail translateToEmployeeCurrentDetailEntity(EmployeeCurrentDetail currDtlEntity,
			VOEmployee employeeModel) {

		if (currDtlEntity.getId() > 0) {

			currDtlEntity.setUpdatedBy(employeeModel.getUpdatedBy());
			currDtlEntity.setUpdatedDate(new Date());
		} else {

			currDtlEntity.setCreatedBy(employeeModel.getCreatedBy());
			currDtlEntity.setCreatedDate(new Date());
		}

		currDtlEntity.setBandGrade(employeeModel.getEmployeeCurrentDetail().getBandGrade());
		currDtlEntity.setResponsibility(employeeModel.getEmployeeCurrentDetail().getResponsibility());
		currDtlEntity.setProcess(employeeModel.getEmployeeCurrentDetail().getProcess());
		currDtlEntity.setNoticePeriod(employeeModel.getEmployeeCurrentDetail().getNoticePeriod());
		currDtlEntity.setState(employeeModel.getEmployeeCurrentDetail().getState());
		currDtlEntity.setPtState(employeeModel.getEmployeeCurrentDetail().getPtState());
		currDtlEntity.setCity(employeeModel.getEmployeeCurrentDetail().getCity());
		currDtlEntity.setProject(employeeModel.getEmployeeCurrentDetail().getProject());
		currDtlEntity.setBillable(employeeModel.getEmployeeCurrentDetail().getBillable());
		currDtlEntity.setCostCenter(employeeModel.getEmployeeCurrentDetail().getCostCenter());
		currDtlEntity.setRetirementDate(employeeModel.getEmployeeCurrentDetail().getRetirementDate());
		currDtlEntity.setHousingStatus(employeeModel.getEmployeeCurrentDetail().getHousingStatus());
		currDtlEntity.setConfirmationStatus(employeeModel.getEmployeeCurrentDetail().getConfirmationStatus());
		// currDtlEntity.setWorkShift(currentDetailModel.getCurrentOrganizationDetails().getWorkShift());
		currDtlEntity.setRemark(employeeModel.getRemark());
		currDtlEntity.setIsActive(IHRMSConstants.isActive);

		return currDtlEntity;
	}

	public static EmployeeBranch translateToEmployeBranchEntity(EmployeeBranch empBranch,
			VOCreateEmployeeCurrentOrgRequest currentDetailModel) {

		if (empBranch.getId() > 0) {
			empBranch.setUpdatedBy(currentDetailModel.getUpdatedBy());
			empBranch.setUpdatedDate(new Date());
		} else {

			empBranch.setCreatedBy(currentDetailModel.getCreatedBy());
			empBranch.setCreatedDate(new Date());
		}

		empBranch.setIsActive(IHRMSConstants.isActive);
		empBranch.setRemark(currentDetailModel.getRemark());

		return empBranch;
	}

	public static EmployeeDepartment translateToEmployeeDepartmentEntity(EmployeeDepartment empDepartment,
			VOCreateEmployeeCurrentOrgRequest currentDetailModel) {

		if (empDepartment.getId() > 0) {
			empDepartment.setUpdatedBy(currentDetailModel.getUpdatedBy());
			empDepartment.setUpdatedDate(new Date());
		} else {
			empDepartment.setCreatedBy(currentDetailModel.getCreatedBy());
			empDepartment.setCreatedDate(new Date());
		}

		empDepartment.setIsActive(IHRMSConstants.isActive);
		empDepartment.setRemark(currentDetailModel.getRemark());

		return empDepartment;
	}

	public static EmployeeDesignation translateToEmployeeDesignationEntity(EmployeeDesignation empDesignation,
			VOCreateEmployeeCurrentOrgRequest currentDetailModel) {

		if (empDesignation.getId() > 0) {
			empDesignation.setUpdatedBy(currentDetailModel.getUpdatedBy());
			empDesignation.setUpdatedDate(new Date());
		} else {
			empDesignation.setCreatedBy(currentDetailModel.getCreatedBy());
			empDesignation.setCreatedDate(new Date());
		}

		empDesignation.setIsActive(IHRMSConstants.isActive);
		empDesignation.setRemark(currentDetailModel.getRemark());
		return empDesignation;
	}

	public static EmployeeDivision translateToEmployeeDivisionEntity(EmployeeDivision empDivision,
			VOCreateEmployeeCurrentOrgRequest currentDetailModel) {

		if (empDivision.getId() > 0) {

			empDivision.setUpdatedBy(currentDetailModel.getUpdatedBy());
			empDivision.setUpdatedDate(new Date());
		} else {

			empDivision.setCreatedBy(currentDetailModel.getCreatedBy());
			empDivision.setCreatedDate(new Date());
		}

		empDivision.setIsActive(IHRMSConstants.isActive);
		empDivision.setRemark(currentDetailModel.getRemark());

		return empDivision;
	}

	public static EmployeeEmploymentType translateToEmployementTypeEntity(EmployeeEmploymentType empEmploymentType,
			VOCreateEmployeeCurrentOrgRequest currentDetailModel) {

		if (empEmploymentType.getId() > 0) {

			empEmploymentType.setUpdatedBy(currentDetailModel.getUpdatedBy());
			empEmploymentType.setUpdatedDate(new Date());
		} else {

			empEmploymentType.setCreatedBy(currentDetailModel.getCreatedBy());
			empEmploymentType.setCreatedDate(new Date());
		}

		empEmploymentType.setIsActive(IHRMSConstants.isActive);
		empEmploymentType.setRemark(currentDetailModel.getRemark());

		return empEmploymentType;
	}

	public static EmployeeLeaveApplied translateToApplyLeaveDetailsEntity(VOApplyLeaveRequest request)
			throws HRMSException {

		EmployeeLeaveApplied leavesAppliedEnity = null;
		boolean ReasonForApplyLeave = HRMSHelper.regexMatcher(request.getLeaveApplied().getReasonForApply(),
				"[a-zA-Z0-9.-;, ]+");
		boolean MobileNo = HRMSHelper.regexMatcher(request.getLeaveApplied().getContactDetails(),
				"^(\\d{10}|\\d{12})$");
		boolean checkEmail = HRMSHelper.regexMatcher(request.getLeaveApplied().getCc(),
				"^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-;]+$");

		if (!HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getContactDetails())) {
			if (!MobileNo) {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, "Contact Number Invalid");
			}
		}

		if (!HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getReasonForApply())) {
			if (!ReasonForApplyLeave) {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, "Reasons For Apply Leave Invalid");
			}
		}

		if (!HRMSHelper.isNullOrEmpty(request.getLeaveApplied())) {

			// commented because HR leave apply issue for Cc email

//			if(!HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getCc()) && checkEmail ) {

			leavesAppliedEnity = new EmployeeLeaveApplied();
			leavesAppliedEnity.setFromDate(
					HRMSDateUtil.parse(request.getLeaveApplied().getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			leavesAppliedEnity.setToDate(
					HRMSDateUtil.parse(request.getLeaveApplied().getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			leavesAppliedEnity.setFromSession(request.getLeaveApplied().getFromSession());
			leavesAppliedEnity.setToSession(request.getLeaveApplied().getToSession());
			leavesAppliedEnity.setContactDetails(request.getLeaveApplied().getContactDetails());
			leavesAppliedEnity.setCc(request.getLeaveApplied().getCc());
			leavesAppliedEnity.setReasonForApply(request.getLeaveApplied().getReasonForApply());
			leavesAppliedEnity.setAttachment(request.getLeaveApplied().getAttachment());
			leavesAppliedEnity.setDateOfApplied(new Date());
			leavesAppliedEnity.setNoOfDays(request.getLeaveApplied().getNoOfDays());
			leavesAppliedEnity.setAppliedBy(request.getLeaveApplied().getAppliedBy());
			leavesAppliedEnity.setCreatedBy(request.getCreatedBy());
			leavesAppliedEnity.setCreatedDate(new Date());
			leavesAppliedEnity.setIsActive(IHRMSConstants.isActive);

//		}else {
//			throw new HRMSException(IHRMSConstants.InsufficientDataCode, "Email Invalid");
//		}
		}
		return leavesAppliedEnity;

	}

	/*
	 * public static MasterCity translateToMasterCityEntity(VOMasterCity
	 * voMasterCity) { MasterCity cityEntity = new MasterCity(); cityEntity.get }
	 */

	public static CandidatePersonalDetail translateToCandidatePersonalDetailsEntity(
			CandidatePersonalDetail personalDetails, VOCandidate request) {

		logger.info(" == >> TO UPDATE CANDIDATE PERSONAL DETAILS << == ");
		if (personalDetails != null && request.getCandidatePersonalDetail() != null) {

			personalDetails.setCitizenship(request.getCandidatePersonalDetail().getCitizenship());
			// Changed here by Nitin for replacing spaces from candidate photo name,while
			// persisting candidate profile photo
			personalDetails.setCandidatePhoto(
					request.getCandidatePersonalDetail().getCandidatePhoto().replaceAll("\\s+", "_"));
			personalDetails.setSpouseName(request.getCandidatePersonalDetail().getSpouseName());
			personalDetails.setEsiNo(request.getCandidatePersonalDetail().getEsiNo());
			personalDetails.setEsiDispensary(request.getCandidatePersonalDetail().getEsiDispensary());
			personalDetails.setMaritalStatus(request.getCandidatePersonalDetail().getMaritalStatus());
			personalDetails.setDrivingLicense(request.getCandidatePersonalDetail().getDrivingLicense());
			personalDetails.setOfficialMobileNumber(request.getCandidatePersonalDetail().getOfficialMobileNumber());
			personalDetails.setScStReservationStatus(request.getCandidatePersonalDetail().getScStReservationStatus());
			personalDetails.setNoOfChildren(request.getCandidatePersonalDetail().getNoOfChildren());
			personalDetails.setNationality(request.getCandidatePersonalDetail().getNationality());
			personalDetails.setCitizenship(request.getCandidatePersonalDetail().getCitizenship());
			personalDetails.setPlaceOfBirth(request.getCandidatePersonalDetail().getPlaceOfBirth());
			personalDetails.setReligion(request.getCandidatePersonalDetail().getReligion());
			personalDetails.setIsActive(IHRMSConstants.isActive);
			personalDetails.setUpdatedBy(request.getUpdatedBy());
			personalDetails.setUpdatedDate(new Date());

		}

		return personalDetails;
	}

	public static CandidateAddress translateCandidateAddressEntity(VOCandidateAddress voCandidateAddress,
			CandidateAddress candidateAddressEt) {

		if (!HRMSHelper.isNullOrEmpty(voCandidateAddress)) {

			if (candidateAddressEt != null && candidateAddressEt.getId() > 0) {
				logger.info("Candidate address found");
				candidateAddressEt.setUpdatedBy(voCandidateAddress.getUpdatedBy());
				candidateAddressEt.setUpdatedDate(new Date());
			} else {
				logger.info("Candidate address not found");
				candidateAddressEt = new CandidateAddress();
				candidateAddressEt.setCreatedBy(voCandidateAddress.getCreatedBy());
				candidateAddressEt.setCreatedDate(new Date());

			}
			candidateAddressEt.setAddressLine1(voCandidateAddress.getAddressLine1());
			candidateAddressEt.setAddressLine2(voCandidateAddress.getAddressLine2());
			candidateAddressEt.setAddressType(voCandidateAddress.getAddressType());
			candidateAddressEt.setPhone1(voCandidateAddress.getPhone1());
			candidateAddressEt.setPhone2(voCandidateAddress.getPhone2());
			candidateAddressEt.setAddressType(voCandidateAddress.getAddressType());
			candidateAddressEt.setPincode(voCandidateAddress.getPincode());
			candidateAddressEt.setIsActive(IHRMSConstants.isActive);
			return candidateAddressEt;
		}
		return null;
	}

	public static Map<String, String> createPlaceHolderMap(EmployeeLeaveApplied leaveApplied) {

		Map<String, String> placeHolderMapping = null;
		if (leaveApplied != null) {

			placeHolderMapping = new HashMap<String, String>();
			placeHolderMapping.put("{empFirstName}", leaveApplied.getEmployee().getCandidate().getFirstName());
			placeHolderMapping.put("{empMiddleName}", leaveApplied.getEmployee().getCandidate().getMiddleName());
			placeHolderMapping.put("{empLastName}", leaveApplied.getEmployee().getCandidate().getLastName());
			placeHolderMapping.put("{fromDate}",
					HRMSDateUtil.format(leaveApplied.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			placeHolderMapping.put("{fromSession}", leaveApplied.getFromSession());
			placeHolderMapping.put("{toSession}", leaveApplied.getToSession());
			placeHolderMapping.put("{toDate}",
					HRMSDateUtil.format(leaveApplied.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			placeHolderMapping.put("{managerFirstName}", leaveApplied.getEmployee().getEmployeeReportingManager()
					.getReporingManager().getCandidate().getFirstName());
			placeHolderMapping.put("{managerMiddleName}", leaveApplied.getEmployee().getEmployeeReportingManager()
					.getReporingManager().getCandidate().getMiddleName());
			placeHolderMapping.put("{managerLastName}", leaveApplied.getEmployee().getEmployeeReportingManager()
					.getReporingManager().getCandidate().getLastName());
			placeHolderMapping.put("{leaveType}", leaveApplied.getMasterLeaveType().getLeaveTypeName());
			placeHolderMapping.put("{noOfDays}", String.valueOf(leaveApplied.getNoOfDays()));
			placeHolderMapping.put("{leaveReason}", leaveApplied.getReasonForApply());
			placeHolderMapping.put("{leaveStatus}", leaveApplied.getLeaveStatus());
			placeHolderMapping.put("{statusAsOfOn}",
					HRMSDateUtil.format(new Date(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			placeHolderMapping.put("{empId}", String.valueOf(leaveApplied.getEmployee().getId()));
			placeHolderMapping.put("{contactNo}", leaveApplied.getContactDetails());
			placeHolderMapping.put("{cancelledOn}",
					HRMSDateUtil.format(leaveApplied.getDateOfCancle(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			placeHolderMapping.put("{cancelReason}", leaveApplied.getReasonForCancel());
			placeHolderMapping.put("{appliedOn}",
					HRMSDateUtil.format(leaveApplied.getDateOfApplied(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			placeHolderMapping.put("{dateOfApproverAction}",
					HRMSDateUtil.format(leaveApplied.getDateOfApproverAction(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			placeHolderMapping.put("{dateOfWithdrawn}",
					HRMSDateUtil.format(leaveApplied.getDateOfWithdrawn(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			placeHolderMapping.put("{approverActionDateOnWithWithdrawn}", HRMSDateUtil
					.format(leaveApplied.getApproverActionDateWithdrawn(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			placeHolderMapping.put("{withdrawnReason}", leaveApplied.getReasonForWithdrawn());
			placeHolderMapping.put("{approverCommentOnWithdrawn}", leaveApplied.getApproverCommentOnWithdrawn());
			placeHolderMapping.put("{leaveActionApprove}", HRMSHelper.encodeString(IHRMSConstants.LeaveAction_APPROVE));
			placeHolderMapping.put("{leaveActionReject}", HRMSHelper.encodeString(IHRMSConstants.LeaveAction_REJECT));
			placeHolderMapping.put("{WDleaveActionApprove}",
					HRMSHelper.encodeString(IHRMSConstants.LeaveAction_WITHDRAW_APPROVE));
			placeHolderMapping.put("{WDleaveActionReject}",
					HRMSHelper.encodeString(IHRMSConstants.LeaveAction_WITHDRAW_REJECT));
			placeHolderMapping.put("{leaveId}", HRMSHelper.encodeString(String.valueOf(leaveApplied.getId())));
			// placeHolderMapping.put("{rootIp}", baseURL);
			// placeHolderMapping.put("{websiteURL}", baseURL);

		}

		return placeHolderMapping;
	}

	public static Candidate translateToCandidateGeneralInfo(Candidate candidateEntity, VOCandidate candidateModel) {

		/**
		 * PROFESSIONAL DETAILS
		 */
		CandidateProfessionalDetail candidateProfessionalDetailsEntity = candidateEntity
				.getCandidateProfessionalDetail();

		if (!HRMSHelper.isNullOrEmpty(candidateProfessionalDetailsEntity)) {
			logger.info(" Professional Details Found : ");
			candidateProfessionalDetailsEntity.setUpdatedBy(candidateModel.getUpdatedBy());
			candidateProfessionalDetailsEntity.setUpdatedDate(new Date());
		} else {
			logger.info(" Professional Details Not Found : ");
			candidateProfessionalDetailsEntity = new CandidateProfessionalDetail();
			candidateProfessionalDetailsEntity.setCreatedBy(candidateModel.getUpdatedBy());
			candidateProfessionalDetailsEntity.setCreatedDate(new Date());

		}

		candidateProfessionalDetailsEntity.setUan(candidateModel.getCandidateProfessionalDetail().getUan());
		// Added on 10 September 2018
		candidateProfessionalDetailsEntity
				.setAadhaarCard(candidateModel.getCandidateProfessionalDetail().getAadhaarCard());
		candidateProfessionalDetailsEntity.setPanCard(candidateModel.getCandidateProfessionalDetail().getPanCard());

		/**
		 * PERSONAL DETAILS
		 */
		CandidatePersonalDetail candidatePersonalDetails = candidateEntity.getCandidatePersonalDetail();

		if (candidatePersonalDetails == null) {
			logger.info(" Personal Details Not Found : ");
			candidatePersonalDetails = new CandidatePersonalDetail();
		}
		candidatePersonalDetails = translateToCandidatePersonalDetailsEntity(candidatePersonalDetails, candidateModel);

		/**
		 * BASIC CANDIDATE DETAILS
		 */
		candidateEntity.setDateOfBirth(candidateModel.getDateOfBirth());
		candidateEntity.setEmailId(candidateModel.getEmailId());
		candidateEntity.setFirstName(candidateModel.getFirstName());
		candidateEntity.setGender(candidateModel.getGender());
		candidateEntity.setIsActive(IHRMSConstants.isActive);
		candidateEntity.setLastName(candidateModel.getLastName());
		candidateEntity.setMiddleName(candidateModel.getMiddleName());
		candidateEntity.setMobileNumber(candidateModel.getMobileNumber());
		candidateEntity.setTitle(candidateModel.getTitle());
		// candidateEntity.setCandidateStatus(candidateModel.getCandidateStatus());
		candidateEntity.setCandidateProfessionalDetail(candidateProfessionalDetailsEntity);
		candidateEntity.setCandidatePersonalDetail(candidatePersonalDetails);
		candidateEntity.setUpdatedBy(candidateModel.getUpdatedBy());
		candidateEntity.setUpdatedDate(new Date());
		candidateEntity.setCandidateEmploymentStatus(candidateModel.getCandidateEmploymentStatus());

		return candidateEntity;
	}

	public static MasterCandidateChecklistAction translateToCandidateChecklistAction(
			VOMasterCandidateChecklistAction candidateChecklistActionVO) {

		MasterCandidateChecklistAction candidateChecklistActionEntity = null;
		if (!HRMSHelper.isNullOrEmpty(candidateChecklistActionVO)) {
			candidateChecklistActionEntity = new MasterCandidateChecklistAction();
			candidateChecklistActionEntity.setCandidateChecklistActionDescription(
					candidateChecklistActionVO.getCandidateChecklistActionDescription());
			candidateChecklistActionEntity
					.setCandidateChecklistActionName(candidateChecklistActionVO.getCandidateChecklistActionName());
			candidateChecklistActionEntity.setId(candidateChecklistActionVO.getId());
		}
		return candidateChecklistActionEntity;
	}

	/**
	 * 
	 * @param voEmployeeCreditLeaveDetail
	 * @return
	 * @author Ssw
	 * 
	 *         This method is used convert VO type : VOCreditLeaveRequest to Entity
	 *         type : EmployeeCreditLeaveDetail this is used when HR credits leave
	 *         of specific type to employee
	 * 
	 */
	public static EmployeeCreditLeaveDetail convertToEmployeeCreditLeaveDetailEntityFromVOCreditRequest(
			EmployeeCreditLeaveDetail employeeCreditLeaveDetailEntity, VOCreditLeaveRequest voCreditLeaveRequest) {
		if (!HRMSHelper.isNullOrEmpty(voCreditLeaveRequest)) {
			employeeCreditLeaveDetailEntity.setCreditedBy(voCreditLeaveRequest.getCreditedBy());
			employeeCreditLeaveDetailEntity.setFromDate(voCreditLeaveRequest.getFromDate());
			employeeCreditLeaveDetailEntity.setIsActive(IHRMSConstants.isActive);
			employeeCreditLeaveDetailEntity.setNoOfDays(voCreditLeaveRequest.getNoOfDays());
			employeeCreditLeaveDetailEntity
					.setOpeningBalanceConsidered(voCreditLeaveRequest.getOpeningBalanceConsidered());
			employeeCreditLeaveDetailEntity.setToDate(voCreditLeaveRequest.getToDate());
			employeeCreditLeaveDetailEntity.setComment(voCreditLeaveRequest.getComment());
			employeeCreditLeaveDetailEntity.setPostedDate(voCreditLeaveRequest.getPostedDate());
			employeeCreditLeaveDetailEntity.setLeaveAvailable(voCreditLeaveRequest.getNoOfDays());
		}
		return employeeCreditLeaveDetailEntity;
	}

	public static Employee convertToEmployeeEntityFromVO(VOEmployee voEmployee) {
		if (!HRMSHelper.isNullOrEmpty(voEmployee) && !HRMSHelper.isLongZero(voEmployee.getId())) {
			Employee employee = new Employee();
			employee.setId(voEmployee.getId());
			return employee;
		}
		return null;
	}

	public static Map<String, String> createPlaceHolderMapForLeaveGrant(EmployeeGrantLeaveDetail grantLeaveDetail) {

		Map<String, String> placeHolderMapping = null;
		if (grantLeaveDetail != null) {

			placeHolderMapping = new HashMap<String, String>();
			placeHolderMapping.put("{empFirstName}", grantLeaveDetail.getEmployee().getCandidate().getFirstName());
			placeHolderMapping.put("{empMiddleName}", grantLeaveDetail.getEmployee().getCandidate().getMiddleName());
			placeHolderMapping.put("{empLastName}", grantLeaveDetail.getEmployee().getCandidate().getLastName());
			placeHolderMapping.put("{fromDate}",
					HRMSDateUtil.format(grantLeaveDetail.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			placeHolderMapping.put("{fromSession}", grantLeaveDetail.getFromSession());
			placeHolderMapping.put("{toSession}", grantLeaveDetail.getToSession());
			placeHolderMapping.put("{toDate}",
					HRMSDateUtil.format(grantLeaveDetail.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			placeHolderMapping.put("{managerFirstName}", grantLeaveDetail.getEmployee().getEmployeeReportingManager()
					.getReporingManager().getCandidate().getFirstName());
			placeHolderMapping.put("{managerMiddleName}", grantLeaveDetail.getEmployee().getEmployeeReportingManager()
					.getReporingManager().getCandidate().getMiddleName());
			placeHolderMapping.put("{managerLastName}", grantLeaveDetail.getEmployee().getEmployeeReportingManager()
					.getReporingManager().getCandidate().getLastName());
			placeHolderMapping.put("{leaveType}", grantLeaveDetail.getMasterLeaveType().getLeaveTypeName());
			placeHolderMapping.put("{noOfDays}", String.valueOf(grantLeaveDetail.getNoOfDays()));
			placeHolderMapping.put("{leaveReason}", grantLeaveDetail.getReasonForApply());
			placeHolderMapping.put("{leaveStatus}", grantLeaveDetail.getLeaveStatus());
			placeHolderMapping.put("{statusAsOfOn}",
					HRMSDateUtil.format(new Date(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			placeHolderMapping.put("{empId}", String.valueOf(grantLeaveDetail.getEmployee().getId()));
			placeHolderMapping.put("{contactNo}", grantLeaveDetail.getContactDetails());
			placeHolderMapping.put("{cancelledOn}",
					HRMSDateUtil.format(grantLeaveDetail.getDateOfCancel(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			placeHolderMapping.put("{cancelReason}", grantLeaveDetail.getReasonForCancel());
			placeHolderMapping.put("{appliedOn}",
					HRMSDateUtil.format(grantLeaveDetail.getDateOfApplied(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			placeHolderMapping.put("{dateOfApproverAction}",
					String.valueOf(grantLeaveDetail.getDateOfApproverAction()));
			placeHolderMapping.put("{dateOfWithdrawn}",
					HRMSDateUtil.format(grantLeaveDetail.getDateOfWithdrawn(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			placeHolderMapping.put("{approverActionDateOnWithWithdrawn}", HRMSDateUtil
					.format(grantLeaveDetail.getApproverActionDateWithdrawn(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			placeHolderMapping.put("{withdrawnReason}", grantLeaveDetail.getReasonForWithdrawn());
			placeHolderMapping.put("{approverCommentOnWithdrawn}", grantLeaveDetail.getApproverCommentOnWithdrawn());
			placeHolderMapping.put("{leaveActionApprove}", HRMSHelper.encodeString(IHRMSConstants.LeaveAction_APPROVE));
			placeHolderMapping.put("{leaveActionReject}", HRMSHelper.encodeString(IHRMSConstants.LeaveAction_REJECT));
			placeHolderMapping.put("{WDleaveActionApprove}",
					HRMSHelper.encodeString(IHRMSConstants.LeaveAction_WITHDRAW_APPROVE));
			placeHolderMapping.put("{WDleaveActionReject}",
					HRMSHelper.encodeString(IHRMSConstants.LeaveAction_WITHDRAW_REJECT));
			placeHolderMapping.put("{leaveId}", HRMSHelper.encodeString(String.valueOf(grantLeaveDetail.getId())));
			// placeHolderMapping.put("{rootIp}", baseURL);
			// placeHolderMapping.put("{websiteURL}", baseURL);

		}

		return placeHolderMapping;
	}

	public static MasterLanguage translateToMasterLanguageEntity(VOMasterLanguage voMasterLanguage) {
		MasterLanguage masterLanguageEntity = null;
		if (!HRMSHelper.isNullOrEmpty(voMasterLanguage)) {
			masterLanguageEntity = new MasterLanguage();
			masterLanguageEntity.setId(voMasterLanguage.getId());
			masterLanguageEntity.setLanguageName(voMasterLanguage.getLanguageName());
			masterLanguageEntity.setLanguageDescription(voMasterLanguage.getLanguageDescription());
		}
		return masterLanguageEntity;
	}

	public static FeedbackQuestion translateToFeedbackQuestionEntity(FeedbackQuestion feedbackQuestionEntity,
			VOFeedbackQuestion voFeedbackQuestion) {

		feedbackQuestionEntity.setQuestionName(voFeedbackQuestion.getQuestionName());
		;
		feedbackQuestionEntity.setChoice(voFeedbackQuestion.getChoice());
		feedbackQuestionEntity.setIsActive(IHRMSConstants.isActive);

		if (feedbackQuestionEntity.getId() == 0) {
			feedbackQuestionEntity.setCreatedBy(voFeedbackQuestion.getCreatedBy());
			feedbackQuestionEntity.setCreatedDate(new Date());
		} else {
			feedbackQuestionEntity.setUpdatedBy(voFeedbackQuestion.getUpdatedBy());
			feedbackQuestionEntity.setUpdatedDate(new Date());

		}

		return feedbackQuestionEntity;
	}

	public static MasterModeofSeparation translateToMasterModeofSeparationEntity(
			MasterModeofSeparation masterModeofSeparationEntity, VOMasterModeofSeparation voMasterModeofSeparation) {
		masterModeofSeparationEntity.setModeOfSeparationCode(voMasterModeofSeparation.getModeOfSeparationCode());
		masterModeofSeparationEntity.setModeOfSeparationName(voMasterModeofSeparation.getModeOfSeparationName());
		masterModeofSeparationEntity.setIsActive(IHRMSConstants.isActive);
		if (voMasterModeofSeparation.getId() == 0) {
			masterModeofSeparationEntity.setCreatedBy(voMasterModeofSeparation.getCreatedBy());
			masterModeofSeparationEntity.setCreatedDate(new Date());
		} else {
			masterModeofSeparationEntity.setUpdatedBy(voMasterModeofSeparation.getUpdatedBy());
			masterModeofSeparationEntity.setUpdatedDate(new Date());
		}
		return masterModeofSeparationEntity;
	}

	public static MasterModeofSeparationReason translateToMasterModeofSeparationReason(
			MasterModeofSeparationReason masterModeofSeparationReasonEntity,
			VOMasterModeofSeparationReason vomstModeofSeparationReason) {
		masterModeofSeparationReasonEntity.setReasonName(vomstModeofSeparationReason.getReasonName());
		masterModeofSeparationReasonEntity.setIsActive(IHRMSConstants.isActive);
		if (vomstModeofSeparationReason.getId() == 0) {
			masterModeofSeparationReasonEntity.setCreatedBy(vomstModeofSeparationReason.getCreatedBy());
			masterModeofSeparationReasonEntity.setCreatedDate(new Date());
		} else {
			masterModeofSeparationReasonEntity.setUpdatedBy(vomstModeofSeparationReason.getUpdatedBy());
			masterModeofSeparationReasonEntity.setUpdatedDate(new Date());
		}
		return masterModeofSeparationReasonEntity;
	}

	public static EmployeeSeparationDetails translateToEmployeeSeparationDetails(
			EmployeeSeparationDetails employeeSeparationDetailsEntity,
			VOEmployeeSeparationDetails voEmployeeSeparationDetails) {

		// HRMSDateUtil.parse(request.getLeaveApplied().getFromDate(),
		// IHRMSConstants.FRONT_END_DATE_FORMAT));
		// employeeSeparationDetailsEntity.setActualRelievingDate(voEmployeeSeparationDetails.getActualRelievingDate());
		employeeSeparationDetailsEntity.setActualRelievingDate(HRMSDateUtil
				.parse(voEmployeeSeparationDetails.getActualRelievingDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		employeeSeparationDetailsEntity.setResignationDate(HRMSDateUtil
				.parse(voEmployeeSeparationDetails.getResignationDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		if (voEmployeeSeparationDetails.getId() == 0) {
			employeeSeparationDetailsEntity.setCreatedBy(voEmployeeSeparationDetails.getCreatedBy());
			employeeSeparationDetailsEntity.setCreatedDate(new Date());
		} else {
			employeeSeparationDetailsEntity.setUpdatedBy(voEmployeeSeparationDetails.getUpdatedBy());
			employeeSeparationDetailsEntity.setUpdatedDate(new Date());
		}

		employeeSeparationDetailsEntity.setEmployeeComment(voEmployeeSeparationDetails.getEmployeeComment());
		if (voEmployeeSeparationDetails.getHrApproverStatus() != null)
			employeeSeparationDetailsEntity.setHrApproverStatus(voEmployeeSeparationDetails.getHrApproverStatus());
		employeeSeparationDetailsEntity.setHRComment(voEmployeeSeparationDetails.getHRComment());
		employeeSeparationDetailsEntity.setIsActive(IHRMSConstants.isActive);
		employeeSeparationDetailsEntity.setOrgApproverStatus(voEmployeeSeparationDetails.getOrgApproverStatus());
		employeeSeparationDetailsEntity.setOrgComment(voEmployeeSeparationDetails.getOrgComment());
		employeeSeparationDetailsEntity.setRoApproverStatus(voEmployeeSeparationDetails.getRoApproverStatus());
		employeeSeparationDetailsEntity.setRoComment(voEmployeeSeparationDetails.getRoComment());
		employeeSeparationDetailsEntity.setRoActionDate(HRMSDateUtil
				.parse(voEmployeeSeparationDetails.getRoActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		employeeSeparationDetailsEntity.setOrgActionDate(HRMSDateUtil
				.parse(voEmployeeSeparationDetails.getOrgActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		employeeSeparationDetailsEntity.setHrActionDate(HRMSDateUtil
				.parse(voEmployeeSeparationDetails.getHrActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		// employeeSeparationDetailsEntity.setSystemEscalatedLevel();
		employeeSeparationDetailsEntity.setNoticeperiod(voEmployeeSeparationDetails.getNoticePeriod());
		employeeSeparationDetailsEntity.setEmployeeAction(voEmployeeSeparationDetails.getEmployeeAction());
		employeeSeparationDetailsEntity.setEmpWdComment(voEmployeeSeparationDetails.getEmpWdComment());
		employeeSeparationDetailsEntity.setEmpWdDate(
				HRMSDateUtil.parse(voEmployeeSeparationDetails.getEmpWdDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		employeeSeparationDetailsEntity.setHr_WdActionDate(HRMSDateUtil
				.parse(voEmployeeSeparationDetails.getHr_WdActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		employeeSeparationDetailsEntity.setHr_WdActionStatus(voEmployeeSeparationDetails.getHr_WdAction());
		employeeSeparationDetailsEntity.setHr_WdComment(voEmployeeSeparationDetails.getHr_WdComment());
		employeeSeparationDetailsEntity.setOrg_level_emp_Wd_action_Date(HRMSDateUtil.parse(
				voEmployeeSeparationDetails.getOrg_level_emp_Wd_action_Date(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		employeeSeparationDetailsEntity
				.setOrg_level_emp_WdActionStatus(voEmployeeSeparationDetails.getOrg_level_emp_WdAction());
		employeeSeparationDetailsEntity
				.setOrg_level_emp_WdComment(voEmployeeSeparationDetails.getOrg_level_emp_WdComment());
		employeeSeparationDetailsEntity.setROWdActionDate(HRMSDateUtil
				.parse(voEmployeeSeparationDetails.getROWdActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		employeeSeparationDetailsEntity.setROWdComment(voEmployeeSeparationDetails.getROWdComment());
		employeeSeparationDetailsEntity.setROWdActionStatus(voEmployeeSeparationDetails.getROWdAction());
		return employeeSeparationDetailsEntity;

	}

	/*
	 * this method is used to maintained the history of employee separation status
	 */

	public static EmployeeSeparationDetailsWithHistory translateToEmployeeSeparationDetailsWithHistory(
			EmployeeSeparationDetailsWithHistory employeeSeparationDetailsEntityHistory,
			VOEmployeeSeparationDetails voEmployeeSeparationDetails) {

		// HRMSDateUtil.parse(request.getLeaveApplied().getFromDate(),
		// IHRMSConstants.FRONT_END_DATE_FORMAT));
		// employeeSeparationDetailsEntity.setActualRelievingDate(voEmployeeSeparationDetails.getActualRelievingDate());
		employeeSeparationDetailsEntityHistory.setActualRelievingDate(HRMSDateUtil
				.parse(voEmployeeSeparationDetails.getActualRelievingDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		employeeSeparationDetailsEntityHistory.setResignationDate(HRMSDateUtil
				.parse(voEmployeeSeparationDetails.getResignationDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));

		employeeSeparationDetailsEntityHistory.setCreatedBy(voEmployeeSeparationDetails.getCreatedBy());
		employeeSeparationDetailsEntityHistory.setCreatedDate(new Date());

		employeeSeparationDetailsEntityHistory.setEmployeeComment(voEmployeeSeparationDetails.getEmployeeComment());
		if (voEmployeeSeparationDetails.getHrApproverStatus() != null)
			employeeSeparationDetailsEntityHistory
					.setHrApproverStatus(voEmployeeSeparationDetails.getHrApproverStatus());
		employeeSeparationDetailsEntityHistory.setHRComment(voEmployeeSeparationDetails.getHRComment());
		employeeSeparationDetailsEntityHistory.setIsActive(IHRMSConstants.isActive);
		employeeSeparationDetailsEntityHistory.setOrgApproverStatus(voEmployeeSeparationDetails.getOrgApproverStatus());
		employeeSeparationDetailsEntityHistory.setOrgComment(voEmployeeSeparationDetails.getOrgComment());
		employeeSeparationDetailsEntityHistory.setRoApproverStatus(voEmployeeSeparationDetails.getRoApproverStatus());
		employeeSeparationDetailsEntityHistory.setRoComment(voEmployeeSeparationDetails.getRoComment());
		employeeSeparationDetailsEntityHistory.setRoActionDate(HRMSDateUtil
				.parse(voEmployeeSeparationDetails.getRoActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		employeeSeparationDetailsEntityHistory.setOrgActionDate(HRMSDateUtil
				.parse(voEmployeeSeparationDetails.getOrgActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		employeeSeparationDetailsEntityHistory.setHrActionDate(HRMSDateUtil
				.parse(voEmployeeSeparationDetails.getHrActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		// employeeSeparationDetailsEntity.setSystemEscalatedLevel();
		employeeSeparationDetailsEntityHistory.setNoticeperiod(voEmployeeSeparationDetails.getNoticePeriod());
		employeeSeparationDetailsEntityHistory.setEmployeeAction(voEmployeeSeparationDetails.getEmployeeAction());
		employeeSeparationDetailsEntityHistory.setEmpWdComment(voEmployeeSeparationDetails.getEmpWdComment());
		employeeSeparationDetailsEntityHistory.setEmpWdDate(
				HRMSDateUtil.parse(voEmployeeSeparationDetails.getEmpWdDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		employeeSeparationDetailsEntityHistory.setHr_WdActionDate(HRMSDateUtil
				.parse(voEmployeeSeparationDetails.getHr_WdActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		employeeSeparationDetailsEntityHistory.setHr_WdActionStatus(voEmployeeSeparationDetails.getHr_WdAction());
		employeeSeparationDetailsEntityHistory.setHr_WdComment(voEmployeeSeparationDetails.getHr_WdComment());
		employeeSeparationDetailsEntityHistory.setOrg_level_emp_Wd_action_Date(HRMSDateUtil.parse(
				voEmployeeSeparationDetails.getOrg_level_emp_Wd_action_Date(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		employeeSeparationDetailsEntityHistory
				.setOrg_level_emp_WdActionStatus(voEmployeeSeparationDetails.getOrg_level_emp_WdAction());
		employeeSeparationDetailsEntityHistory
				.setOrg_level_emp_WdComment(voEmployeeSeparationDetails.getOrg_level_emp_WdComment());
		employeeSeparationDetailsEntityHistory.setROWdActionDate(HRMSDateUtil
				.parse(voEmployeeSeparationDetails.getROWdActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		employeeSeparationDetailsEntityHistory.setROWdComment(voEmployeeSeparationDetails.getROWdComment());
		employeeSeparationDetailsEntityHistory.setROWdActionStatus(voEmployeeSeparationDetails.getROWdAction());
		return employeeSeparationDetailsEntityHistory;

	}

	public static Map<String, String> createPlaceHolderMapForEmp_Separation(Employee employeeEntity,
			EmployeeSeparationDetails employeeSeparationDetailsEntity) {

		Map<String, String> placeHolderMapping = null;
		if (employeeSeparationDetailsEntity != null) {

			placeHolderMapping = new HashMap<String, String>();

			placeHolderMapping.put("{managerFirstName}",
					employeeEntity.getEmployeeReportingManager().getReporingManager().getCandidate().getFirstName());
			placeHolderMapping.put("{managerMiddleName}",
					employeeEntity.getEmployeeReportingManager().getReporingManager().getCandidate().getMiddleName());
			placeHolderMapping.put("{managerLastName}",
					employeeEntity.getEmployeeReportingManager().getReporingManager().getCandidate().getLastName());

			placeHolderMapping.put("{empFirstName}", employeeEntity.getCandidate().getFirstName());
			placeHolderMapping.put("{empMiddleName}", employeeEntity.getCandidate().getMiddleName());
			placeHolderMapping.put("{empLastName}", employeeEntity.getCandidate().getLastName());
			placeHolderMapping.put("{empId}", String.valueOf(employeeEntity.getId()));
			placeHolderMapping.put("{empDesignation}", employeeEntity.getCandidate().getCandidateProfessionalDetail()
					.getDesignation().getDesignationName());
			placeHolderMapping.put("{empDateofJoining}",
					HRMSDateUtil.format(
							employeeEntity.getCandidate().getCandidateProfessionalDetail().getDateOfJoining(),
							IHRMSConstants.FRONT_END_DATE_FORMAT));
			placeHolderMapping.put("{empnoticePeriod}",
					String.valueOf(employeeSeparationDetailsEntity.getNoticeperiod()));
			placeHolderMapping.put("{empResignedDate}", HRMSDateUtil.format(
					employeeSeparationDetailsEntity.getResignationDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			placeHolderMapping.put("{empRelievingDate}", HRMSDateUtil.format(
					employeeSeparationDetailsEntity.getActualRelievingDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			placeHolderMapping.put("{empSeparationReason}",
					employeeSeparationDetailsEntity.getEmpSeparationReason().getReasonName());

			if (!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity.getEmployeeAction())
					&& employeeSeparationDetailsEntity.getEmployeeAction()
							.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_WITHDRAW))
				placeHolderMapping.put("{mailContent}",
						"This is to inform you that your reportee have applied for resignation withdrawal.");
			else
				placeHolderMapping.put("{mailContent}", "The following Employee has applied for a Resignation");
		}
		return placeHolderMapping;
	}

	public static Map<String, String> createPlaceHolderForROReminderifNoActionTaken(Employee employeeEntity,
			EmployeeSeparationDetails employeeSeparationDetailsEntity) {

		Map<String, String> placeHolderMapping = null;
		if (employeeSeparationDetailsEntity != null) {

			placeHolderMapping = new HashMap<String, String>();

			placeHolderMapping.put("{managerFirstName}",
					employeeEntity.getEmployeeReportingManager().getReporingManager().getCandidate().getFirstName());
			placeHolderMapping.put("{managerMiddleName}",
					employeeEntity.getEmployeeReportingManager().getReporingManager().getCandidate().getMiddleName());
			placeHolderMapping.put("{managerLastName}",
					employeeEntity.getEmployeeReportingManager().getReporingManager().getCandidate().getLastName());

			placeHolderMapping.put("{empFirstName}", employeeEntity.getCandidate().getFirstName());
			placeHolderMapping.put("{empMiddleName}", employeeEntity.getCandidate().getMiddleName());
			placeHolderMapping.put("{empLastName}", employeeEntity.getCandidate().getLastName());
			placeHolderMapping.put("{empId}", String.valueOf(employeeEntity.getId()));
			placeHolderMapping.put("{empDesignation}", employeeEntity.getCandidate().getCandidateProfessionalDetail()
					.getDesignation().getDesignationName());
			placeHolderMapping.put("{empDateofJoining}",
					HRMSDateUtil.format(
							employeeEntity.getCandidate().getCandidateProfessionalDetail().getDateOfJoining(),
							IHRMSConstants.FRONT_END_DATE_FORMAT));
			placeHolderMapping.put("{empnoticePeriod}",
					String.valueOf(employeeSeparationDetailsEntity.getNoticeperiod()));
			placeHolderMapping.put("{empResignedDate}", HRMSDateUtil.format(
					employeeSeparationDetailsEntity.getResignationDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			placeHolderMapping.put("{empRelievingDate}", HRMSDateUtil.format(
					employeeSeparationDetailsEntity.getActualRelievingDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			placeHolderMapping.put("{empSeparationReason}",
					employeeSeparationDetailsEntity.getEmpSeparationReason().getReasonName());

			placeHolderMapping.put("{mailContent}",
					"Action of your team member's separation is pending on your side. Requesting you to kindly take an action for the further perusal");

		}
		return placeHolderMapping;
	}

	public static Map<String, String> createPlaceHolderMapForApproveSeparation(Employee employeeEntity,
			VOEmployeeSeparationDetails voEmployeeSeparationDetails) {
		Map<String, String> placeHolderMapping = null;
		if (employeeEntity != null) {
			placeHolderMapping = new HashMap<String, String>();
			placeHolderMapping.put("{empFirstName}", employeeEntity.getCandidate().getFirstName());
			placeHolderMapping.put("{empMiddleName}", employeeEntity.getCandidate().getMiddleName());
			placeHolderMapping.put("{empLastName}", employeeEntity.getCandidate().getLastName());
			placeHolderMapping.put("{relievingDate}", voEmployeeSeparationDetails.getActualRelievingDate());

			placeHolderMapping.put("{Resignation}", "Resignation");
			if (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoApproverStatus())
					|| !HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgApproverStatus())) {
				if ((!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoApproverStatus())
						&& voEmployeeSeparationDetails.getRoApproverStatus()
								.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED))
						|| (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgApproverStatus())
								&& voEmployeeSeparationDetails.getOrgApproverStatus()
										.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED))) {
					placeHolderMapping.put("{status}",
							IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED.toLowerCase());
				} else if ((!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoApproverStatus())
						&& voEmployeeSeparationDetails.getRoApproverStatus()
								.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED))
						|| (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgApproverStatus())
								&& voEmployeeSeparationDetails.getOrgApproverStatus()
										.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED))) {
					placeHolderMapping.put("{status}",
							IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED.toLowerCase());
				}
			}
			// the two ifs are modified by ssw on 12 march 2018
			// for : on withdraw action by RO or ORG level
			// in old "if condition", comparing status with 'ROApproverStatus' instead of
			// 'ROWdAction'
			// same with 'OrgApproverStatus' instead of 'Org_level_emp_WdAction'

			// old if condition ::
			// if((!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoApproverStatus())
			// &&
			// voEmployeeSeparationDetails.getRoApproverStatus().equalsIgnoreCase(IHRMSConstants.EMPLOYEE_WD_SEPARATION_STATUS_REJECTED))
			// ||
			// (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgApproverStatus())
			// &&
			// voEmployeeSeparationDetails.getOrgApproverStatus().equalsIgnoreCase(IHRMSConstants.EMPLOYEE_WD_SEPARATION_STATUS_REJECTED))){
			// New if condition ::
			if ((!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getROWdAction()) && voEmployeeSeparationDetails
					.getROWdAction().equalsIgnoreCase(IHRMSConstants.EMPLOYEE_WD_SEPARATION_STATUS_REJECTED))
					|| (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrg_level_emp_WdAction())
							&& voEmployeeSeparationDetails.getOrg_level_emp_WdAction()
									.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_WD_SEPARATION_STATUS_REJECTED))) {
				placeHolderMapping.put("{Resignation}", "pull back");
				placeHolderMapping.put("{status}", IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED.toLowerCase());
			}

			// OLD if condition ::
			// if((!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getRoApproverStatus())
			// &&
			// voEmployeeSeparationDetails.getRoApproverStatus().equalsIgnoreCase(IHRMSConstants.EMPLOYEE_WD_SEPARATION_STATUS_APPROVED))
			// ||
			// (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrgApproverStatus())
			// &&
			// voEmployeeSeparationDetails.getOrgApproverStatus().equalsIgnoreCase(IHRMSConstants.EMPLOYEE_WD_SEPARATION_STATUS_APPROVED))){
			// NEW if condition ::
			if ((!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getROWdAction()) && voEmployeeSeparationDetails
					.getROWdAction().equalsIgnoreCase(IHRMSConstants.EMPLOYEE_WD_SEPARATION_STATUS_APPROVED))
					|| (!HRMSHelper.isNullOrEmpty(voEmployeeSeparationDetails.getOrg_level_emp_WdAction())
							&& voEmployeeSeparationDetails.getOrg_level_emp_WdAction()
									.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_WD_SEPARATION_STATUS_APPROVED))) {
				placeHolderMapping.put("{Resignation}", "pull back");
				placeHolderMapping.put("{status}", IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED.toLowerCase());
			}
		}
		return placeHolderMapping;
	}

	public static List<VOEmployeeFeedback> translateToEmployeeFeedbackVoList(VOSubmittedEmployeeFeedback empFb) {
		List<VOFeedbackQuestion> fbList = empFb.getEmpFeedbackList();
		List<VOEmployeeFeedback> empFbVoList = new ArrayList<VOEmployeeFeedback>();
		for (VOFeedbackQuestion submittedQueOpt : fbList) {
			if (!HRMSHelper.isNullOrEmpty(submittedQueOpt.getFeedbackOptions())
					&& !submittedQueOpt.getFeedbackOptions().isEmpty()) {
				for (VOFeedbackOption opt : submittedQueOpt.getFeedbackOptions()) {
					VOEmployeeFeedback empFbVo = new VOEmployeeFeedback();
					VOEmployee empVo = new VOEmployee();
					VOFeedbackQuestion fbQueVo = new VOFeedbackQuestion();
					VOFeedbackOption fbOptVo = new VOFeedbackOption();
					// VOUserFeedback userfbVo = new VOUserFeedback();

					// if(submittedQueOpt.getFeedbackOptions().isEmpty() &&
					// !HRMSHelper.isLongZero(userfbVo.getEmployeeFeedbackId())) {
					// empFbVo.setId(userfbVo.getEmployeeFeedbackId());
					// }else {
					empFbVo.setId(opt.getEmployeeFeedbackId());
					// }

					empVo.setId(empFb.getEmployee().getId());
					empFbVo.setEmployee(empVo);

					fbQueVo.setId(submittedQueOpt.getId());
					fbOptVo.setId(opt.getId());
					fbOptVo.setIsSelected(opt.getIsSelected().trim());
					// empFbVo.setUserFeedback(userfbVo.getComment());

					empFbVo.setFeedbackQuestion(fbQueVo);
					empFbVo.setFeedbackOption(fbOptVo);
					empFbVo.setCreatedBy(submittedQueOpt.getCreatedBy());
					empFbVo.setUpdatedBy(submittedQueOpt.getUpdatedBy());

					empFbVoList.add(empFbVo);
				}
			} else {

				VOEmployeeFeedback empFbVo = new VOEmployeeFeedback();
				VOEmployee empVo = new VOEmployee();
				VOFeedbackQuestion fbQueVo = new VOFeedbackQuestion();
				VOFeedbackOption fbOptVo = new VOFeedbackOption();
				// VOUserFeedback userfbVo = new VOUserFeedback();

				if (submittedQueOpt.getFeedbackOptions().isEmpty()
						&& !HRMSHelper.isLongZero(submittedQueOpt.getUserFeedback().getEmployeeFeedbackId())) {
					empFbVo.setId(submittedQueOpt.getUserFeedback().getEmployeeFeedbackId());
				}

				empVo.setId(empFb.getEmployee().getId());
				empFbVo.setEmployee(empVo);

				fbQueVo.setId(submittedQueOpt.getId());
				// fbOptVo.setId(null);
				// fbOptVo.setIsSelected(opt.getIsSelected().trim());
				empFbVo.setUserFeedback(submittedQueOpt.getUserFeedback().getComment());

				empFbVo.setFeedbackQuestion(fbQueVo);
				empFbVo.setFeedbackOption(fbOptVo);

				empFbVo.setCreatedBy(submittedQueOpt.getCreatedBy());
				empFbVo.setUpdatedBy(submittedQueOpt.getUpdatedBy());

				empFbVoList.add(empFbVo);

			}

		}

		return empFbVoList;
	}

	public static EmployeeFeedback translateToEmployeeFeedbackEntity(EmployeeFeedback empfbEntity,
			VOEmployeeFeedback voEmpFb) {

		// Employee empEntity = new Employee();
		// FeedbackQuestion fbQueEntity = new FeedbackQuestion();
		// FeedbackOption fbOptEntity = new FeedbackOption();

		// empEntity.setId(voEmpFb.getEmployee().getId());
		// fbQueEntity.setId(voEmpFb.getFeedbackQuestion().getId());
		// fbOptEntity.setId(voEmpFb.getFeedbackOption().getId());

		empfbEntity.setId(voEmpFb.getId());
		// empfbEntity.setEmployee(empEntity);
		// empfbEntity.setFeedbackQuestion(fbQueEntity);
		// empfbEntity.setFeedbackOption(fbOptEntity);
		empfbEntity.setIsSelected(voEmpFb.getFeedbackOption().getIsSelected());
		empfbEntity.setUserFeedback(voEmpFb.getUserFeedback());
		if (empfbEntity.getId() == 0) {
			empfbEntity.setCreatedBy(voEmpFb.getCreatedBy());
			empfbEntity.setCreatedDate(new Date());
		} else {
			empfbEntity.setUpdatedBy(voEmpFb.getUpdatedBy());
			empfbEntity.setUpdatedDate(new Date());

		}
		return empfbEntity;
	}

	public static MapCatalogue translateToCatalogueEntity(VOMapCatalogue request, MapCatalogue entity) {

		entity.setDescription(request.getDescription());
		entity.setIsActive(IHRMSConstants.isActive);
		entity.setName(request.getName());
		return entity;
	}

	public static MapCatalogueChecklistItem translateToMapOrganizationChecklistItem(
			VOMapCatalogueChecklistItem catalogueChecklistRequest, MapCatalogueChecklistItem catalogueChecklist) {

		catalogueChecklist.setIsActive(IHRMSConstants.isActive);
		catalogueChecklist.setName(catalogueChecklistRequest.getName());
		catalogueChecklist.setRemark(catalogueChecklistRequest.getRemark());
		return catalogueChecklist;
	}

	public static MapEmployeeCatalogueChecklist convertToEmployeeCatalogueChecklistEntity(
			VOMapEmployeeCatalogueChecklist model, MapEmployeeCatalogueChecklist entity) {
		entity.setAmount(model.getAmount());
		entity.setComment(model.getComment());
		entity.setHaveCollected(model.isHaveCollected());
		entity.setUpdatedBy(model.getUpdatedBy());
		entity.setUpdatedDate(new Date());

		return entity;
	}

	public static Map<String, String> createPlaceHolderApproverChecklist(Employee employeeEntity,
			EmployeeSeparationDetails employeeSeparationDetailsEntity) {

		Map<String, String> placeHolderMapping = null;
		if (employeeSeparationDetailsEntity != null) {

			placeHolderMapping = new HashMap<String, String>();

			/*
			 * placeHolderMapping.put("{managerFirstName}",
			 * employeeEntity.getEmployeeReportingManager()
			 * .getReporingManager().getCandidate().getFirstName());
			 * placeHolderMapping.put("{managerMiddleName}",
			 * employeeEntity.getEmployeeReportingManager()
			 * .getReporingManager().getCandidate().getMiddleName());
			 * placeHolderMapping.put("{managerLastName}",
			 * employeeEntity.getEmployeeReportingManager()
			 * .getReporingManager().getCandidate().getLastName());
			 */

			placeHolderMapping.put("{empFirstName}",
					employeeSeparationDetailsEntity.getEmployee().getCandidate().getFirstName());
			placeHolderMapping.put("{empMiddleName}",
					employeeSeparationDetailsEntity.getEmployee().getCandidate().getMiddleName());
			placeHolderMapping.put("{empLastName}",
					employeeSeparationDetailsEntity.getEmployee().getCandidate().getLastName());
			placeHolderMapping.put("{empId}", String.valueOf(employeeSeparationDetailsEntity.getEmployee().getId()));
			placeHolderMapping.put("{empDesignation}", employeeSeparationDetailsEntity.getEmployee().getCandidate()
					.getCandidateProfessionalDetail().getDesignation().getDesignationName());
			placeHolderMapping.put("{empDateofJoining}",
					HRMSDateUtil.format(
							employeeSeparationDetailsEntity.getEmployee().getCandidate()
									.getCandidateProfessionalDetail().getDateOfJoining(),
							IHRMSConstants.FRONT_END_DATE_FORMAT));
			placeHolderMapping.put("{empnoticePeriod}",
					String.valueOf(employeeSeparationDetailsEntity.getNoticeperiod()));
			placeHolderMapping.put("{empResignedDate}", HRMSDateUtil.format(
					employeeSeparationDetailsEntity.getResignationDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			placeHolderMapping.put("{empRelievingDate}", HRMSDateUtil.format(
					employeeSeparationDetailsEntity.getActualRelievingDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			placeHolderMapping.put("{empSeparationReason}",
					employeeSeparationDetailsEntity.getEmpSeparationReason().getReasonName());

			if (!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity.getEmployeeAction())
					&& employeeSeparationDetailsEntity.getEmployeeAction()
							.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_WITHDRAW))
				placeHolderMapping.put("{mailContent}",
						"This is to inform you that your reportee have applied for resignation withdrawal.");
			else
				placeHolderMapping.put("{mailContent}", "The following Employee has applied for a Resignation");
		}
		return placeHolderMapping;
	}

	public static Map<String, String> createPlaceHolderMapAfterExitFeedbackFormSubmitted(Employee employeeEntity) {
		Map<String, String> placeHolderMapping = null;
		if (employeeEntity != null) {
			placeHolderMapping = new HashMap<String, String>();
			placeHolderMapping.put("{empFirstName}", employeeEntity.getCandidate().getFirstName());
			placeHolderMapping.put("{empMiddleName}",
					HRMSHelper.isNullOrEmpty(employeeEntity.getCandidate().getMiddleName()) ? ""
							: employeeEntity.getCandidate().getMiddleName());
			placeHolderMapping.put("{empLastName}", employeeEntity.getCandidate().getLastName());
			placeHolderMapping.put("{empId}", String.valueOf(employeeEntity.getId()));
		}
		return placeHolderMapping;
	}

	/**
	 * To Create Accommodation Request From Modle To Entity
	 */

	public static AccommodationRequest createAccomodationRequestEntity(AccommodationRequest entity,
			VOAccommodationRequest model) {

		if (model != null) {
			// entity = new AccommodationRequest();
			entity.setNoOfPeople(model.getNoOfPeople());
			entity.setFromDate(HRMSDateUtil.parse(model.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			entity.setToDate(HRMSDateUtil.parse(model.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			entity.setPreferenceDetails(model.getPreferenceDetails());
			entity.setChargeableToClient(model.isChargeableToClient());
			entity.setApprovalRequired(model.isApprovalRequired());
			// entity.setApproverStatus(model.getApproverStatus());
			// entity.setAccommodationRequestStatus(model.getAccommodationRequestStatus());
			entity.setTotalAccommodationCost(model.getTotalAccommodationCost());
			entity.setTotalRefundAmount(model.getTotalRefundAmount());
		}

		return entity;
	}

	/**
	 * To Create Accommodation Guest Entity From Model To Entity
	 */
	public static AccommodationGuest createAccommodationGuestEntity(AccommodationGuest accomodationGuest,
			VOAccommodationGuest guestListModle) {
		// accomodationGuest.setAccomodationReq(accommodationRequestEntity);
		// accomodationGuest.setId(guestListModle.getId());

		if (guestListModle != null) {
			accomodationGuest.setPassengerName(guestListModle.getPassengerName());
			accomodationGuest.setEmailId(guestListModle.getEmailId());
			accomodationGuest.setContactNumber(guestListModle.getContactNumber());
			accomodationGuest.setIsActive(IHRMSConstants.isActive);
			// accomodationGuest.setEmployeeId(guestListModle.getEmployeeId());
			accomodationGuest.setDateOfBirth(
					HRMSDateUtil.parse(guestListModle.getDateOfBirth(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		}

		return accomodationGuest;
	}

	/**
	 * To Create TravelRequest Request From Model To Entity
	 */
	public static TravelRequest createTravelRequestEntity(TravelRequest entity, VOTravelRequest model) {

		if (model != null) {

			entity.setWorkOrderNo(model.getWorkOrderNo());
			entity.setBookTicket(model.isBookTicket());
			entity.setBookAccommodation(model.isBookAccommodation());
			entity.setBookCab(model.isBookCab());
			entity.setCheckedBy(model.getCheckedBy());
			entity.setCreditCardDetails(model.getCreditCardDetails());
			entity.setClientName(model.getClientName());
			entity.setRequestDate(model.getRequestDate());
			entity.setBdName(model.getBdName());
			entity.setBusinessUnit(model.getBusinessUnit());

			// entity.setCabRequest(VOCabRequest cabRequest);
			// entity.setAccommodationRequest(VOAccommodationRequest accommodationRequest);
			// entity.setTicketRequest(VOTicketRequest ticketRequest);
			// entity.setEmployeeId(VOEmployee employeeId);
			// entity.setDepartmentId(VOMasterDepartment departmentId);
			// entity.setTravelStatus(model.getTravelStatus());
		}
		return entity;
	}

	/**
	 * To Create TicketRequest Request From Model To Entity
	 */
	public static TicketRequest createTicketRequestEntity(TicketRequest entity, VOTicketRequest model) {
		// TODO Auto-generated method stub

		if (model != null) {

			// entity.setId(long id);
			// entity.setTravelRequestId(TravelRequest travelRequestId);
			// entity.setMasterModeOfTravel(VOMasterModeOfTravel masterModeOfTravel);
			entity.setNoOfTraveller(model.getNoOfTraveller());
			entity.setPreferenceDetails(model.getPreferenceDetails());
			entity.setPreferredDate(HRMSDateUtil.parse(model.getPreferredDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));

			entity.setPreferredTime(model.getPreferredTime());
			entity.setFromLocation(model.getFromLocation());
			entity.setToLocation(model.getToLocation());
			entity.setRoundTrip(model.isRoundTrip());
			entity.setReturnPreferenceDetails(model.getReturnPreferenceDetails());
			entity.setReturnPreferredDate(
					HRMSDateUtil.parse(model.getReturnPreferredDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			entity.setReturnPreferredTime(model.getReturnPreferredTime());
			entity.setTotalTicketFare(model.getTotalTicketFare());
			entity.setChargeableToClient(model.isChargeableToClient());
			entity.setApprovalRequired(model.isApprovalRequired());
			// entity.setApproverId(VOEmployee approverId);
			// entity.setApproverStatus(model.getApproverStatus());
			// entity.setTicketRequestStatus(model.getTicketRequestStatus());
			entity.setTotalRefundAmount(model.getTotalRefundAmount());
		}
		return entity;
	}

	public static TicketRequestPassenger createTicketPassengerEntity(TicketRequestPassenger ticketPassengeEntity,
			VOTicketRequestPassenger ticketPassengerModel) {

		if (ticketPassengerModel != null) {
			ticketPassengeEntity.setPassengerName(ticketPassengerModel.getPassengerName());
			ticketPassengeEntity.setEmailId(ticketPassengerModel.getEmailId());
			ticketPassengeEntity.setContactNumber(ticketPassengerModel.getContactNumber());
			ticketPassengeEntity.setIsActive(IHRMSConstants.isActive);
			// ticketPassengeEntity.setEmployeeId(ticketPassengerModel.getEmployeeId());
			ticketPassengeEntity.setDateOfBirth(
					HRMSDateUtil.parse(ticketPassengerModel.getDateOfBirth(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		}

		return ticketPassengeEntity;

	}

	public static CabRequest createCabRequestEntity(CabRequest entity, VOCabRequest model) {

		if (entity != null && model != null) {
			entity.setTotalCabCost(model.getTotalCabCost());
			entity.setApprovalRequired(model.isApprovalRequired());
			entity.setTotalRefundAmount(model.getTotalRefundAmount());
		}
		return entity;
	}

	public static CabRecurringRequest createCabRecurringRequestEntity(CabRecurringRequest entity,
			VOCabRequestPassenger model) {

		entity.setPickupDate(HRMSDateUtil.parse(model.getPickupDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		entity.setPickupTime(model.getPickupTime());
		entity.setPickupAt(model.getPickupAt());
		entity.setReturnDate(HRMSDateUtil.parse(model.getReturnDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		entity.setReturnTime(model.getReturnTime());
		entity.setDropLocation(model.getDropLocation());
		entity.setChargeableToClient(model.isChargeableToClient());

		// entity.setCabRequestRecurringStatus(String cabRequestRecurringStatus);
		// entity.setDriverId(VOMasterDriver driverId);
		// entity.setVehicleId(VOMasterVehicle vehicleId);
		// entity.setId(Long id);
		// entity.setCabRequestId(VOCabRequest cabRequestId);
		return entity;
	}

	public static CabRequestPassenger createCabPassengerEntity(CabRequestPassenger entity,
			VOCabRequestPassenger model) {
		if (model != null) {

			entity.setPickupDate(HRMSDateUtil.parse(model.getPickupDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			entity.setPickupTime(model.getPickupTime());
			entity.setPickupAt(model.getPickupAt());
			entity.setReturnDate(HRMSDateUtil.parse(model.getReturnDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			entity.setReturnTime(model.getReturnTime());
			entity.setDropLocation(model.getDropLocation());
			entity.setDropOnly(model.isDropOnly());
			entity.setRecurring(model.isRecurring());
			entity.setSelfManaged(model.isSelfManaged());
			entity.setChargeableToClient(model.isChargeableToClient());
			entity.setCabRequestPasssengerStatus(model.getCabRequestPasssengerStatus());
			entity.setPassengerName(model.getPassengerName());
			entity.setEmailId(model.getEmailId());
			entity.setContactNumber(model.getContactNumber());
			entity.setDateOfBirth(HRMSDateUtil.parse(model.getDateOfBirth(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			// entity.setEmployee(VOEmployee employee);
			// entity.setCabRecurringRequests(Set<VOCabRecurringRequest>
			// cabRecurringRequests);
			// entity.setMapCabDriverVehicle(VOMapCabDriverVehicle mapCabDriverVehicle);

		}
		return entity;
	}

	public static Map<String, String> createPlaceHolderMapAttendanceDefaulters(
			List<AttendanceProcessedData> empAttendanceList, Employee employee) {
		Map<String, String> placeHolderMapping = null;
		StringBuffer str = new StringBuffer();
		String monthYear = "";
		String empName = "";
		try {
			if (empAttendanceList != null) {
				for (AttendanceProcessedData data : empAttendanceList) {
					str.append("<tr bgcolor=\"red\">\r\n");
					str.append("<td>" + String.valueOf(data.getCompositePrimaryKey().getEmployeeACN()) + "</td>\r\n");
					str.append("<td>" + data.getEmpName() + "</td>\r\n");

					if (employee != null)
						str.append("<td>" + employee.getCandidate().getCandidateProfessionalDetail().getDepartment()
								.getDepartmentName() + "</td>\r\n");
					else
						str.append("<td>NA</td>\r\n");

					if (employee != null)
						str.append("<td>" + employee.getCandidate().getCandidateProfessionalDetail().getDesignation()
								.getDesignationName() + "</td>\r\n");
					else
						str.append("<td>NA</td>\r\n");

					str.append("<td>" + data.getCompositePrimaryKey().getAttendanceDate().toString() + "</td>\r\n");
					str.append("<td>" + data.getStatus() + "</td>\r\n");

					if (data.getStartTime() != null)
						str.append("<td>" + data.getStartTime().toString() + "</td>\r\n");
					else
						str.append("<td>0</td>\r\n");

					if (data.getEndTime() != null)
						str.append("<td>" + data.getEndTime().toString() + "</td>\r\n");
					else
						str.append("<td>0</td>\r\n");

					str.append("<td>" + String.valueOf(data.getManHours()) + "</td>\r\n");
					str.append("</tr>\r\n");
					monthYear = new SimpleDateFormat("MMM y").format(data.getCompositePrimaryKey().getAttendanceDate());
					empName = data.getEmpName();
				}
				placeHolderMapping = new HashMap<String, String>();
				placeHolderMapping.put("{defaulterData}", str.toString());
				placeHolderMapping.put("{monthyear}", monthYear);
				placeHolderMapping.put("{empName}", empName);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return placeHolderMapping;
	}

	public static MasterAssetType translateToMasterAssetTypeEntity(MasterAssetType entity, VOMasterAssetType model) {
		if (!HRMSHelper.isNullOrEmpty(model)) {
			entity.setAssetTypeName(model.getAssetTypeName());
			entity.setAssetTypeCode(model.getAssetTypeCode());
		}
		return entity;
	}

	public static CompanyAsset translateToCompanyAssetEntity(CompanyAsset entity, VOCompanyAsset model) {
		if (!HRMSHelper.isNullOrEmpty(model)) {
			entity.setManufacturer(model.getManufacturer());
			entity.setModel(model.getModel());
			entity.setSerialNumber(model.getSerialNumber());
			entity.setQuantity(model.getQuantity());
			entity.setDateOfIssue(HRMSDateUtil.parse(model.getDateOfIssue(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			entity.setComment(model.getComment());
			entity.setStatus(model.getStatus());
			entity.setIsActive(model.getIsActive());
		}
		return entity;
	}

	public static Map<String, String> createPlaceHolderMapForLeaveReminder(Employee employeeEntity) {
		Map<String, String> placeHolderMapping = null;
		if (employeeEntity != null) {
			placeHolderMapping = new HashMap<String, String>();
			placeHolderMapping.put("{empFirstName}", employeeEntity.getCandidate().getFirstName());
			placeHolderMapping.put("{empMiddleName}",
					HRMSHelper.isNullOrEmpty(employeeEntity.getCandidate().getMiddleName()) ? ""
							: employeeEntity.getCandidate().getMiddleName());
			placeHolderMapping.put("{empLastName}", employeeEntity.getCandidate().getLastName());
		}
		return placeHolderMapping;
	}

	/*
	 * overloaded method
	 */
	public static EmployeeLeaveApplied translateToApplyLeaveDetailsEntity(ApplyLeaveRequestVO request)
			throws HRMSException {

		EmployeeLeaveApplied leavesAppliedEnity = null;
		boolean ReasonForApplyLeave = HRMSHelper.validateLeaveReason(request.getLeaveApplied().getReasonForApply());
		boolean MobileNo = HRMSHelper.regexMatcher(request.getLeaveApplied().getContactDetails(),
				"^(\\d{10}|\\d{12})$");
		boolean checkEmail = HRMSHelper.regexMatcher(request.getLeaveApplied().getCc(),
				"^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-;]+$");

		if (!HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getContactDetails())) {
			if (!MobileNo) {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, "Contact Number Invalid");
			}
		}

		if (!HRMSHelper.isNullOrEmpty(request.getLeaveApplied().getReasonForApply())) {
			if (!ReasonForApplyLeave) {
//				throw new HRMSException(IHRMSConstants.InsufficientDataCode, "Reasons For Apply Leave Invalid");
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Apply ");
			}
		}

		if (!HRMSHelper.isNullOrEmpty(request.getLeaveApplied())) {

			leavesAppliedEnity = new EmployeeLeaveApplied();
			leavesAppliedEnity.setFromDate(
					HRMSDateUtil.parse(request.getLeaveApplied().getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			leavesAppliedEnity.setToDate(
					HRMSDateUtil.parse(request.getLeaveApplied().getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			leavesAppliedEnity.setFromSession(request.getLeaveApplied().getFromSession());
			leavesAppliedEnity.setToSession(request.getLeaveApplied().getToSession());
			leavesAppliedEnity.setContactDetails(request.getLeaveApplied().getContactDetails());
			leavesAppliedEnity.setCc(request.getLeaveApplied().getCc());
			leavesAppliedEnity.setReasonForApply(request.getLeaveApplied().getReasonForApply());
			leavesAppliedEnity.setAttachment(request.getLeaveApplied().getAttachment());
			leavesAppliedEnity.setDateOfApplied(new Date());
			leavesAppliedEnity.setNoOfDays(request.getLeaveApplied().getNoOfDays());
			leavesAppliedEnity.setAppliedBy(request.getLeaveApplied().getAppliedBy());
			leavesAppliedEnity.setCreatedDate(new Date());
			leavesAppliedEnity.setIsActive(IHRMSConstants.isActive);

//		}else {
//			throw new HRMSException(IHRMSConstants.InsufficientDataCode, "Email Invalid");
//		}
		}
		return leavesAppliedEnity;

	}

	/*
	 * overloaded method 30/01/2024
	 */
	public static EmployeeGrantLeaveDetail translateToEmployeeGrantLeaveDetailEntity(
			EmployeeGrantLeaveDetail employeeGrantLeaveDetailEntity,
			ApplyGrantLeaveRequestVO voEmployeeGrantLeaveDetail, Employee employee) throws HRMSException {

		boolean ReasonForApplyLeave = HRMSHelper.regexMatcher(voEmployeeGrantLeaveDetail.getReasonForApply(),
				"[a-zA-Z0-9.-;, ]{1,250}$");

		if (!HRMSHelper.isNullOrEmpty(voEmployeeGrantLeaveDetail.getReasonForApply())) {
			if (!ReasonForApplyLeave) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Reason For Apply ");
			}
		}

		employeeGrantLeaveDetailEntity.setAppliedBy(voEmployeeGrantLeaveDetail.getAppliedBy());
		employeeGrantLeaveDetailEntity.setApproverActionDateWithdrawn(HRMSDateUtil.parse(
				voEmployeeGrantLeaveDetail.getApproverActionDateWithdrawn(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		employeeGrantLeaveDetailEntity
				.setApproverCommentOnWithdrawn(voEmployeeGrantLeaveDetail.getApproverCommentOnWithdrawn());
		employeeGrantLeaveDetailEntity.setAttachment(voEmployeeGrantLeaveDetail.getAttachment());
		employeeGrantLeaveDetailEntity.setCc(voEmployeeGrantLeaveDetail.getCc());
		employeeGrantLeaveDetailEntity.setContactDetails(voEmployeeGrantLeaveDetail.getContactDetails());
		employeeGrantLeaveDetailEntity.setDateOfApplied(new Date());
		employeeGrantLeaveDetailEntity.setDateOfApproverAction(HRMSDateUtil
				.parse(voEmployeeGrantLeaveDetail.getDateOfApproverAction(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		employeeGrantLeaveDetailEntity.setDateOfCancel(
				HRMSDateUtil.parse(voEmployeeGrantLeaveDetail.getDateOfCancel(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		employeeGrantLeaveDetailEntity.setDateOfWithdrawn(HRMSDateUtil
				.parse(voEmployeeGrantLeaveDetail.getDateOfWithdrawn(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		employeeGrantLeaveDetailEntity.setFromDate(
				HRMSDateUtil.parse(voEmployeeGrantLeaveDetail.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		employeeGrantLeaveDetailEntity.setFromSession(voEmployeeGrantLeaveDetail.getFromSession());
		employeeGrantLeaveDetailEntity.setLeaveStatus(voEmployeeGrantLeaveDetail.getLeaveStatus());
		employeeGrantLeaveDetailEntity.setReasonForApply(voEmployeeGrantLeaveDetail.getReasonForApply());
		employeeGrantLeaveDetailEntity.setReasonForCancel(voEmployeeGrantLeaveDetail.getReasonForCancel());
		employeeGrantLeaveDetailEntity.setReasonForWithdrawn(voEmployeeGrantLeaveDetail.getReasonForWithdrawn());
//		employeeGrantLeaveDetailEntity.setRemark(voEmployeeGrantLeaveDetail.getRemark());
		employeeGrantLeaveDetailEntity.setToDate(
				HRMSDateUtil.parse(voEmployeeGrantLeaveDetail.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		employeeGrantLeaveDetailEntity.setToSession(voEmployeeGrantLeaveDetail.getToSession());
		employeeGrantLeaveDetailEntity.setNoOfDays(voEmployeeGrantLeaveDetail.getNoOfDays());
		if (employeeGrantLeaveDetailEntity.getId() == 0) {
			employeeGrantLeaveDetailEntity.setIsActive(IHRMSConstants.isActive);
			employeeGrantLeaveDetailEntity.setCreatedBy(String.valueOf(employee.getId()));
			employeeGrantLeaveDetailEntity.setCreatedDate(new Date());
		} else {
			employeeGrantLeaveDetailEntity.setIsActive(IHRMSConstants.isActive);
			employeeGrantLeaveDetailEntity.setUpdatedBy(String.valueOf(employee.getId()));
			employeeGrantLeaveDetailEntity.setUpdatedDate(new Date());
		}
		return employeeGrantLeaveDetailEntity;

	}

	public static Map<String, String> createPlaceHolderForApproveSeparation(Employee employeeEntity,
			EmployeeSeparationRequestVO voEmployeeSeparationDetails) {
		Map<String, String> placeHolderMapping = null;
		if (employeeEntity != null) {
			placeHolderMapping = new HashMap<String, String>();
			placeHolderMapping.put("{empFirstName}", employeeEntity.getCandidate().getFirstName());
			placeHolderMapping.put("{empMiddleName}", employeeEntity.getCandidate().getMiddleName());
			placeHolderMapping.put("{empLastName}", employeeEntity.getCandidate().getLastName());
			placeHolderMapping.put("{empID}", String.valueOf(employeeEntity.getId()));
			placeHolderMapping.put("{relievingDate}", voEmployeeSeparationDetails.getActualRelievingDate());
			placeHolderMapping.put("{Resignation}", "Resignation");
			placeHolderMapping.put("{status}", IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_APPROVED.toLowerCase());
			placeHolderMapping.put("{rmFirstName}",
					employeeEntity.getEmployeeReportingManager().getReporingManager().getCandidate().getFirstName());
			placeHolderMapping.put("{rmLastName}",
					employeeEntity.getEmployeeReportingManager().getReporingManager().getCandidate().getLastName());
		}
		return placeHolderMapping;
	}

	public static Map<String, String> createPlaceHolderForRejectSeparation(Employee employeeEntity,
			EmployeeSeparationRequestVO voEmployeeSeparationDetails) {
		Map<String, String> placeHolderMapping = null;
		if (employeeEntity != null) {
			placeHolderMapping = new HashMap<String, String>();
			placeHolderMapping.put("{empFirstName}", employeeEntity.getCandidate().getFirstName());
			placeHolderMapping.put("{empMiddleName}", employeeEntity.getCandidate().getMiddleName());
			placeHolderMapping.put("{empLastName}", employeeEntity.getCandidate().getLastName());
			placeHolderMapping.put("{empID}", String.valueOf(employeeEntity.getId()));
			placeHolderMapping.put("{relievingDate}", voEmployeeSeparationDetails.getActualRelievingDate());
			placeHolderMapping.put("{Resignation}", "Resignation");
			placeHolderMapping.put("{status}", IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_REJECTED.toLowerCase());
			placeHolderMapping.put("{rmFirstName}",
					employeeEntity.getEmployeeReportingManager().getReporingManager().getCandidate().getFirstName());
			placeHolderMapping.put("{rmLastName}",
					employeeEntity.getEmployeeReportingManager().getReporingManager().getCandidate().getLastName());
		}
		return placeHolderMapping;
	}

}
