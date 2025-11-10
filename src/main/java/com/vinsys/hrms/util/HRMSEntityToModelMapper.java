package com.vinsys.hrms.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.datamodel.BankDetailsVO;
import com.vinsys.hrms.datamodel.VOCandidate;
import com.vinsys.hrms.datamodel.VOCandidateActivity;
import com.vinsys.hrms.datamodel.VOCandidateActivityLetterMapping;
import com.vinsys.hrms.datamodel.VOCandidateAddress;
import com.vinsys.hrms.datamodel.VOCandidateCertification;
import com.vinsys.hrms.datamodel.VOCandidateChecklist;
import com.vinsys.hrms.datamodel.VOCandidateEmergencyContact;
import com.vinsys.hrms.datamodel.VOCandidateEmergencyContactAddress;
import com.vinsys.hrms.datamodel.VOCandidateFamilyAddress;
import com.vinsys.hrms.datamodel.VOCandidateFamilyDetail;
import com.vinsys.hrms.datamodel.VOCandidateHealthReport;
import com.vinsys.hrms.datamodel.VOCandidateLanguage;
import com.vinsys.hrms.datamodel.VOCandidateLetter;
import com.vinsys.hrms.datamodel.VOCandidateOverseasExperience;
import com.vinsys.hrms.datamodel.VOCandidatePassportDetail;
import com.vinsys.hrms.datamodel.VOCandidatePersonalDetail;
import com.vinsys.hrms.datamodel.VOCandidatePolicyDetail;
import com.vinsys.hrms.datamodel.VOCandidatePreviousEmployment;
import com.vinsys.hrms.datamodel.VOCandidateProfessionalDetail;
import com.vinsys.hrms.datamodel.VOCandidateQualification;
import com.vinsys.hrms.datamodel.VOCandidateStatutoryNomination;
import com.vinsys.hrms.datamodel.VOCandidateVisaDetail;
import com.vinsys.hrms.datamodel.VOEmployee;
import com.vinsys.hrms.datamodel.VOEmployeeBranch;
import com.vinsys.hrms.datamodel.VOEmployeeCreditLeaveDetail;
import com.vinsys.hrms.datamodel.VOEmployeeCurrentDetail;
import com.vinsys.hrms.datamodel.VOEmployeeDepartment;
import com.vinsys.hrms.datamodel.VOEmployeeDesignation;
import com.vinsys.hrms.datamodel.VOEmployeeDivision;
import com.vinsys.hrms.datamodel.VOEmployeeEmploymentType;
import com.vinsys.hrms.datamodel.VOEmployeeExtension;
import com.vinsys.hrms.datamodel.VOEmployeeGrantLeaveDetail;
import com.vinsys.hrms.datamodel.VOEmployeeLeaveApplied;
import com.vinsys.hrms.datamodel.VOEmployeeLeaveDetail;
import com.vinsys.hrms.datamodel.VOEmployeeReportingManager;
import com.vinsys.hrms.datamodel.VOEmployeeSeparationDetails;
import com.vinsys.hrms.datamodel.VOLoginEntity;
import com.vinsys.hrms.datamodel.VOLoginEntityType;
import com.vinsys.hrms.datamodel.VOMapCatalogue;
import com.vinsys.hrms.datamodel.VOMapCatalogueChecklistItem;
import com.vinsys.hrms.datamodel.VOMapEmployeeCatalogue;
import com.vinsys.hrms.datamodel.VOMapEmployeeCatalogueChecklist;
import com.vinsys.hrms.datamodel.VOMasterBranch;
import com.vinsys.hrms.datamodel.VOMasterCandidateChecklist;
import com.vinsys.hrms.datamodel.VOMasterCandidateChecklistAction;
import com.vinsys.hrms.datamodel.VOMasterCity;
import com.vinsys.hrms.datamodel.VOMasterCountry;
import com.vinsys.hrms.datamodel.VOMasterDepartment;
import com.vinsys.hrms.datamodel.VOMasterDesignation;
import com.vinsys.hrms.datamodel.VOMasterDivision;
import com.vinsys.hrms.datamodel.VOMasterEmploymentType;
import com.vinsys.hrms.datamodel.VOMasterExtensionType;
import com.vinsys.hrms.datamodel.VOMasterLanguage;
import com.vinsys.hrms.datamodel.VOMasterLeaveType;
import com.vinsys.hrms.datamodel.VOMasterModeofSeparation;
import com.vinsys.hrms.datamodel.VOMasterModeofSeparationReason;
import com.vinsys.hrms.datamodel.VOMasterNoticePeriod;
import com.vinsys.hrms.datamodel.VOMasterState;
import com.vinsys.hrms.datamodel.VOMasterWorkshift;
import com.vinsys.hrms.datamodel.VOOrganization;
import com.vinsys.hrms.datamodel.VORejectResiganationReason;
import com.vinsys.hrms.datamodel.VOSubscription;
import com.vinsys.hrms.datamodel.assets.VOCompanyAsset;
import com.vinsys.hrms.datamodel.assets.VOMasterAssetType;
import com.vinsys.hrms.datamodel.attendance.VOEmployeeACN;
import com.vinsys.hrms.datamodel.traveldesk.VOAccommodationGuest;
import com.vinsys.hrms.datamodel.traveldesk.VOAccommodationRequest;
import com.vinsys.hrms.datamodel.traveldesk.VOCabRecurringRequest;
import com.vinsys.hrms.datamodel.traveldesk.VOCabRequest;
import com.vinsys.hrms.datamodel.traveldesk.VOCabRequestPassenger;
import com.vinsys.hrms.datamodel.traveldesk.VOMasterModeOfTravel;
import com.vinsys.hrms.datamodel.traveldesk.VOReportTraveldeskDetail;
import com.vinsys.hrms.datamodel.traveldesk.VOTicketRequest;
import com.vinsys.hrms.datamodel.traveldesk.VOTicketRequestPassenger;
import com.vinsys.hrms.datamodel.traveldesk.VOTravelDeskDocument;
import com.vinsys.hrms.datamodel.traveldesk.VOTravelRequest;
import com.vinsys.hrms.datamodel.traveldesk.VOTravelRequestDispaly;
import com.vinsys.hrms.datamodel.traveldesk.VOTraveldeskComment;
import com.vinsys.hrms.employee.vo.ChecklistVO;
import com.vinsys.hrms.entity.BankDetails;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateActivity;
import com.vinsys.hrms.entity.CandidateActivityLetterMapping;
import com.vinsys.hrms.entity.CandidateAddress;
import com.vinsys.hrms.entity.CandidateCertification;
import com.vinsys.hrms.entity.CandidateChecklist;
import com.vinsys.hrms.entity.CandidateEmergencyContact;
import com.vinsys.hrms.entity.CandidateFamilyDetail;
import com.vinsys.hrms.entity.CandidateHealthReport;
import com.vinsys.hrms.entity.CandidateLanguage;
import com.vinsys.hrms.entity.CandidateLetter;
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
import com.vinsys.hrms.entity.EmployeeExtension;
import com.vinsys.hrms.entity.EmployeeGrantLeaveDetail;
import com.vinsys.hrms.entity.EmployeeLeaveApplied;
import com.vinsys.hrms.entity.EmployeeLeaveDetail;
import com.vinsys.hrms.entity.EmployeeReportingManager;
import com.vinsys.hrms.entity.EmployeeSeparationDetails;
import com.vinsys.hrms.entity.EmployeeSeparationDetailsWithHistory;
import com.vinsys.hrms.entity.LoginEntity;
import com.vinsys.hrms.entity.LoginEntityType;
import com.vinsys.hrms.entity.MapCatalogue;
import com.vinsys.hrms.entity.MapCatalogueChecklistItem;
import com.vinsys.hrms.entity.MapEmployeeCatalogue;
import com.vinsys.hrms.entity.MapEmployeeCatalogueChecklist;
import com.vinsys.hrms.entity.MasterBranch;
import com.vinsys.hrms.entity.MasterCandidateChecklist;
import com.vinsys.hrms.entity.MasterCandidateChecklistAction;
import com.vinsys.hrms.entity.MasterCity;
import com.vinsys.hrms.entity.MasterCountry;
import com.vinsys.hrms.entity.MasterDepartment;
import com.vinsys.hrms.entity.MasterDesignation;
import com.vinsys.hrms.entity.MasterDivision;
import com.vinsys.hrms.entity.MasterEmploymentType;
import com.vinsys.hrms.entity.MasterExtensionType;
import com.vinsys.hrms.entity.MasterLanguage;
import com.vinsys.hrms.entity.MasterLeaveType;
import com.vinsys.hrms.entity.MasterModeofSeparation;
import com.vinsys.hrms.entity.MasterModeofSeparationReason;
import com.vinsys.hrms.entity.MasterOrg_NoticePeriod;
import com.vinsys.hrms.entity.MasterRejectResiganationReason;
import com.vinsys.hrms.entity.MasterState;
import com.vinsys.hrms.entity.MasterWorkshift;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.entity.Subscription;
import com.vinsys.hrms.entity.assets.CompanyAsset;
import com.vinsys.hrms.entity.assets.MasterAssetType;
import com.vinsys.hrms.entity.attendance.EmployeeACN;
import com.vinsys.hrms.entity.traveldesk.AccommodationGuest;
import com.vinsys.hrms.entity.traveldesk.AccommodationRequest;
import com.vinsys.hrms.entity.traveldesk.CabRecurringRequest;
import com.vinsys.hrms.entity.traveldesk.CabRequest;
import com.vinsys.hrms.entity.traveldesk.CabRequestPassenger;
import com.vinsys.hrms.entity.traveldesk.MasterModeOfTravel;
import com.vinsys.hrms.entity.traveldesk.ReportTraveldeskDetail;
import com.vinsys.hrms.entity.traveldesk.TicketRequest;
import com.vinsys.hrms.entity.traveldesk.TicketRequestPassenger;
import com.vinsys.hrms.entity.traveldesk.TravelRequest;
import com.vinsys.hrms.entity.traveldesk.TraveldeskComment;
import com.vinsys.hrms.entity.traveldesk.TraveldeskDocument;
import com.vinsys.hrms.translator.HRMSResponseTranslator;

//@PropertySource(value="${HRMSCONFIG}")
public class HRMSEntityToModelMapper {

	@Value("${base.url}")
	@Autowired
	private static String baseURL;

	@Autowired
	IHRMSEmployeeDAO employeeDAO;

	private static final Logger logger = LoggerFactory.getLogger(HRMSEntityToModelMapper.class);

	/**
	 * CUSTOM MAPPER TO RETURN CANDIDATE ADDRES MODEL FROM CANDIDATE ADDRESS ENTITY
	 * 
	 * @param CandidateAddress
	 * @return VOCandidateAddress
	 */
	public static VOCandidateAddress convertToAddressModel(CandidateAddress entity) {

		VOCandidateAddress model = null;
		if (entity != null) {

			model = new VOCandidateAddress();
			model.setId(entity.getId());
			model.setAddressLine1(entity.getAddressLine1());
			model.setAddressLine2(entity.getAddressLine2());
			model.setCity(HRMSResponseTranslator.translateToMasterCityVO(entity.getCity()));
			model.setState(HRMSResponseTranslator.translateToMasterStateVO(entity.getState()));
			model.setPincode(entity.getPincode());

		}
		return model;
	}

	/**
	 * CUSTOM MAPPER TO RETURN CANDIDATE EMERGENCY CONTACT MODEL FROM CANDIDATE
	 * EMERGENCY CONTACT ENTITY
	 * 
	 * @Param CandidateEmergencyContact
	 * @Return VOCandidateEmergencyContact ,RETURNS NULL IF ENTITY IS NULL
	 * 
	 */
	public static VOCandidateEmergencyContact convertToEmergencyContactModel(CandidateEmergencyContact entity) {

		VOCandidateEmergencyContact model = null;
		if (entity != null) {

			model = new VOCandidateEmergencyContact();
			model.setId(entity.getId());
			model.setCandidatePersonalDetail(null);
			model.setFirstname(entity.getFirstname());
			model.setMiddlename(entity.getMiddlename());
			model.setLastname(entity.getLastname());
			model.setRelationship(entity.getRelationship());
			model.setMobileNumber(entity.getMobileNumber());
			model.setLandlineNumber(entity.getLandlineNumber());

			VOCandidateEmergencyContactAddress voCandidateEmergencyContactAddressModel = new VOCandidateEmergencyContactAddress();
			voCandidateEmergencyContactAddressModel
					.setAddressLine1(entity.getCandidateEmergencyContactAddress().getAddressLine1());
			voCandidateEmergencyContactAddressModel
					.setAddressLine2(entity.getCandidateEmergencyContactAddress().getAddressLine2());
			voCandidateEmergencyContactAddressModel.setCity(HRMSResponseTranslator
					.translateToMasterCityVO(entity.getCandidateEmergencyContactAddress().getCity()));
			voCandidateEmergencyContactAddressModel.setId(entity.getCandidateEmergencyContactAddress().getId());
			voCandidateEmergencyContactAddressModel
					.setPincode(entity.getCandidateEmergencyContactAddress().getPincode());
			voCandidateEmergencyContactAddressModel.setState(HRMSResponseTranslator
					.translateToMasterStateVO(entity.getCandidateEmergencyContactAddress().getState()));

			voCandidateEmergencyContactAddressModel.setCountry(HRMSResponseTranslator
					.translateToMasterCountryVO(entity.getCandidateEmergencyContactAddress().getCountry()));

			model.setCandidateEmergencyContactAddress(voCandidateEmergencyContactAddressModel);

		}
		return model;
	}

	/**
	 * CUSTOM MAPPER TO RETURN CANDIDATE FAMILY DETAIL MODEL FROM CANDIDATE FAMILY
	 * DETAIL ENTITY
	 * 
	 * @param CandidateFamilyDetail
	 * @return VOCandidateFamilyDetail,RETURNS NULL IF ENTITY IS NULL
	 */
	public static VOCandidateFamilyDetail convertToFamilyDetailModel(CandidateFamilyDetail entity) {

		VOCandidateFamilyDetail model = null;
		if (entity != null) {

			model = new VOCandidateFamilyDetail();

			model.setCandidateFamilyAddress(null);
			model.setId(entity.getId());
			model.setCandidatePersonalDetail(null);
			model.setFirst_name(entity.getFirst_name());
			model.setMiddle_name(entity.getMiddle_name());
			model.setLast_name(entity.getLast_name());
			model.setRelationship(entity.getRelationship());
			model.setOccupation(entity.getOccupation());
			model.setDateOfBirth(entity.getDateOfBirth());
			if (entity.getDateOfBirth() != null)
				model.setDateOfBirth(
						HRMSDateUtil.parse(entity.getDateOfBirth().toString(), IHRMSConstants.POSTGRE_DATE_FORMAT));
			else
				model.setDateOfBirth(entity.getDateOfBirth());
			model.setGender(entity.getGender());
			model.setWorking(entity.getWorking());
			model.setDependent(entity.getDependent());
			model.setDependentSeverelyDisabled(entity.getDependentSeverelyDisabled());
			model.setContactNo1(entity.getContactNo1());
			model.setContactNo2(entity.getContactNo2());
			model.setIsEmergencyContact(entity.getIsEmergencyContact());

			HRMSHelper.calculateAge(model.getDateOfBirth());
			/*
			 * Calendar now = Calendar.getInstance(); Calendar dob = Calendar.getInstance();
			 * dob.setTime(model.getDateOfBirth());
			 * 
			 * int dobYear = dob.get(Calendar.YEAR); int currentYear =
			 * now.get(Calendar.YEAR); int age = currentYear - dobYear; logger.info("DOB " +
			 * age);
			 */
			model.setAge(HRMSHelper.calculateAge(model.getDateOfBirth()));

			VOCandidateFamilyAddress vocandidateFamilyAddressModel = new VOCandidateFamilyAddress();
			if (!HRMSHelper.isNullOrEmpty(entity.getCandidateFamilyAddress())) {
				vocandidateFamilyAddressModel.setAddressLine1(entity.getCandidateFamilyAddress().getAddressLine1());
				vocandidateFamilyAddressModel.setAddressLine2(entity.getCandidateFamilyAddress().getAddressLine2());
				vocandidateFamilyAddressModel.setCity(
						HRMSResponseTranslator.translateToMasterCityVO(entity.getCandidateFamilyAddress().getCity()));
				vocandidateFamilyAddressModel.setId(entity.getCandidateFamilyAddress().getId());
				vocandidateFamilyAddressModel.setPincode(entity.getCandidateFamilyAddress().getPincode());
				vocandidateFamilyAddressModel.setState(
						HRMSResponseTranslator.translateToMasterStateVO(entity.getCandidateFamilyAddress().getState()));
				vocandidateFamilyAddressModel.setCountry(HRMSResponseTranslator
						.translateToMasterCountryVO(entity.getCandidateFamilyAddress().getCountry()));
			}
			model.setCandidateFamilyAddress(vocandidateFamilyAddressModel);
		}
		return model;
	}

	/**
	 * CUSTOM MAPPER TO RETURN CANDIDATE HEALTH REPORT MODEL FROM CANDIDATE HEALTH
	 * REPORT ENTITY
	 * 
	 * @Parameter CandidateHealthReport
	 * @return VOCandidateHealthReport,RETURNS NULL IF ENTITY IS NULL
	 */
	public static VOCandidateHealthReport convertToHealthDetailModel(CandidateHealthReport entity) {

		VOCandidateHealthReport model = null;
		if (entity != null) {

			model = new VOCandidateHealthReport();
			model.setId(entity.getId());
			model.setCandidatePersonalDetail(null);
			model.setBloodGroup(entity.getBloodGroup());
			model.setInterestedToDonateBlood(entity.getInterestedToDonateBlood());
			model.setPhysicallyHandicapped(entity.getPhysicallyHandicapped());
			model.setSeverelyHandicapped(entity.getSeverelyHandicapped());
			model.setVisionProblem(entity.getVisionProblem());
			model.setSurgery(entity.getSurgery());
			model.setHospitalization(entity.getHospitalization());
			model.setAllergy(entity.getAllergy());
			model.setIdentificationMark(entity.getIdentificationMark());

		}
		return model;
	}

	/**
	 * CUSTOM MAPPER TO RETURN CANDIDATE LANGUAGE MODEL FROM CANDIDATE LANGUAGE
	 * ENTITY
	 * 
	 * @param CandidateLanguage
	 * @return VOCandidateLanguage,RETURNS NULL IF ENTITY IS NULL
	 */
	public static VOCandidateLanguage convertToCandidateLanguageModel(CandidateLanguage entity) {

		VOCandidateLanguage model = null;
		if (entity != null) {

			model = new VOCandidateLanguage();
			model = new VOCandidateLanguage();
			model.setId(entity.getId());
			model.setCandidatePersonalDetail(null);
			// model.setLanguageName(entity.getLanguageName());
			model.setSpeak(entity.getSpeak());
			model.setRead(entity.getRead());
			model.setWrite(entity.getWrite());
			model.setMotherTongue(entity.getMotherTongue());
			model.setLanguage(convertToLanguageMasterVO(entity.getLanguage()));

		}
		return model;
	}

	/**
	 * CUSTOM MAPPER TO RETURN CANDIDATE PASSPORT DETAIL MODEL FROM CANDIDATE
	 * PASSPORT DETAIL ENTITY
	 * 
	 * @param CandidatePassportDetail
	 * @return VOCandidatePassportDetail,RETURNS NULL IF ENTITY IS NULL
	 */
	public static VOCandidatePassportDetail convertToPassportDetailModel(CandidatePassportDetail entity) {

		VOCandidatePassportDetail model = null;
		if (entity != null) {

			model = new VOCandidatePassportDetail();
			model.setId(entity.getId());
			model.setCandidatePersonalDetail(null);
			model.setPassportNumber(entity.getPassportNumber());
			model.setPassportFirstName(entity.getPassportFirstName());
			model.setPassportMiddleName(entity.getPassportMiddleName());
			model.setPassportLastName(entity.getPassportLastName());
			model.setPlaceOfIssue(entity.getPlaceOfIssue());
			model.setDateOfIssue(entity.getDateOfIssue());
			model.setDateOfExpiry(entity.getDateOfExpiry());
			model.setEcnr(entity.getEcnr());

		}
		return model;
	}

	/**
	 * CUSTOM MAPPER TO RETURN CANDIDATE PASSPORT DETAIL MODEL FROM CANDIDATE
	 * PASSPORT DETAIL ENTITY
	 * 
	 * @param CandidatePassportDetail
	 * @return VOCandidatePassportDetail,RETURNS NULL IF ENTITY IS NULL
	 */
	public static VOCandidatePolicyDetail convertToCandidatePolicyDetailModel(CandidatePolicyDetail entity) {

		VOCandidatePolicyDetail model = null;
		if (entity != null) {

			model = new VOCandidatePolicyDetail();
			model.setId(entity.getId());
			model.setCandidatePersonalDetail(null);
			model.setInsuranceType(entity.getInsuranceType());
			model.setInsuranceCompany(entity.getInsuranceCompany());
			model.setPolicyNumber(entity.getPolicyNumber());
			model.setTpa(entity.getTpa());
			model.setTpaName(entity.getTpaName());
			model.setStartDate(entity.getStartDate());
			model.setDateOfExpiry(entity.getDateOfExpiry());
			model.setSumInsured(entity.getSumInsured());
			model.setEmployeeMembershipId(entity.getEmployeeMembershipId());

		}
		return model;
	}

	/**
	 * CUSTOM MAPPER TO RETURN CANDIDATE NOMINATION DETAIL MODEL FROM CANDIDATE
	 * NOMINATION DETAIL ENTITY
	 * 
	 * @param CandidateStatutoryNomination
	 * @return VOCandidateStatutoryNomination,RETURNS NULL IF ENTITY IS NULL
	 **/
	public static VOCandidateStatutoryNomination convertToCandidateStatutoryNominationDetailModel(
			CandidateStatutoryNomination entity) {

		VOCandidateStatutoryNomination model = null;
		if (entity != null) {

			model = new VOCandidateStatutoryNomination();
			model.setId(entity.getId());
			model.setCandidatePersonalDetail(null);
			model.setType(entity.getType());
			model.setNomineeName(entity.getNomineeName());
			model.setRelationship(entity.getRelationship());
			model.setDateOfBirth(entity.getDateOfBirth());
			model.setAge(entity.getAge());
			model.setPercentage(entity.getPercentage());

		}
		return model;
	}

	/**
	 * CUSTOM MAPPER TO RETURN CANDIDATE VISA DETAIL MODEL FROM CANDIDATE VISA
	 * DETAIL ENTITY
	 * 
	 * @param CandidateStatutoryNomination
	 * @return VOCandidateStatutoryNomination , RETURNS NULL IF ENTITY IS NULL
	 **/
	public static VOCandidateVisaDetail convertToCandidateVisaDetailModel(CandidateVisaDetail entity) {

		VOCandidateVisaDetail model = null;
		if (entity != null) {

			model = new VOCandidateVisaDetail();
			model.setId(entity.getId());
			model.setCandidatePersonalDetail(null);
			model.setCountry(entity.getCountry());
			model.setVisaNumber(entity.getVisaNumber());
			model.setVisaType(entity.getVisaType());
			model.setDateOfIssue(
					HRMSDateUtil.parse(entity.getDateOfIssue().toString(), IHRMSConstants.POSTGRE_DATE_FORMAT));
			model.setDateOfExpiry(
					HRMSDateUtil.parse(entity.getDateOfExpiry().toString(), IHRMSConstants.POSTGRE_DATE_FORMAT));

		}
		return model;
	}

	/**
	 * CUSTOM MAPPER TO RETURN CANDIDATE CERTIFICATE DETAIL MODEL FROM CANDIDATE
	 * CERTIFICATE DETAIL ENTITY
	 * 
	 * @param CandidateCertification
	 * @return VOCandidateCertification , RETURNS NULL IF ENTITY IS NULL
	 **/
	public static VOCandidateCertification convertToCandidateCertificateDetailsModel(CandidateCertification entity) {

		VOCandidateCertification model = null;
		if (entity != null) {

			model = new VOCandidateCertification();
			model.setId(entity.getId());
			model.setCandidateProfessionalDetail(null);
			model.setCertificationName(entity.getCertificationName());
			model.setCertificationType(entity.getCertificationType());
			// model.setCertificationDate(entity.getCertificationDate());
			// model.setCertificationValidityDate(entity.getCertificationValidityDate());
			if (entity.getCertificationDate() != null)
				model.setCertificationDate(HRMSDateUtil.parse(entity.getCertificationDate().toString(),
						IHRMSConstants.POSTGRE_DATE_FORMAT));
			else
				model.setCertificationDate(entity.getCertificationDate());
			if (entity.getCertificationValidityDate() != null)
				model.setCertificationValidityDate(HRMSDateUtil.parse(entity.getCertificationValidityDate().toString(),
						IHRMSConstants.POSTGRE_DATE_FORMAT));
			else
				model.setCertificationValidityDate(entity.getCertificationValidityDate());
			model.setPercentageGrade(entity.getPercentageGrade());
			model.setModeOfEducation(entity.getModeOfEducation());

		}
		return model;
	}

	/**
	 * CUSTOM MAPPER TO RETURN CANDIDATE CHECKLIST DETAIL MODEL FROM CANDIDATE
	 * CHECKLIST DETAIL ENTITY
	 * 
	 * @param CandidateChecklist
	 * @return VOCandidateChecklist , RETURNS NULL IF ENTITY IS NULL
	 **/
	public static VOCandidateChecklist convertToCandidateChecklistDetailsModel(CandidateChecklist entity) {

		VOCandidateChecklist model = null;
		if (entity != null) {

			model = new VOCandidateChecklist();
			model.setId(entity.getId());
			model.setCandidateProfessionalDetail(null);
			model.setChecklistTemplate(entity.getChecklistTemplate());
			model.setChecklistItem(entity.getChecklistItem());
			model.setMandatory(entity.getMandatory());
			model.setSubmitted(entity.getSubmitted());
			model.setAttachment(entity.getAttachment());
			if (entity.getUpdatedDateTime() != null)
				model.setUpdatedDateTime(
						HRMSDateUtil.parse(entity.getUpdatedDateTime().toString(), IHRMSConstants.POSTGRE_DATE_FORMAT));
			else
				model.setUpdatedDateTime(entity.getUpdatedDateTime());
			model.setHrValidationActedOn(entity.getHrValidationActedOn());
			model.setHrValidationComment(entity.getHrValidationComment());
			model.setHrValidationStatus(entity.getHrValidationStatus());
			model.setMasterCandidateChecklistAction(
					convertToMasterCandidateChecklistActionModel(entity.getMasterCandidateChecklistAction()));

		}
		return model;
	}

	/**
	 * CUSTOM MAPPER TO RETURN CANDIDATE OVERSEAS DETAIL MODEL FROM CANDIDATE
	 * OVERSEAS DETAIL ENTITY
	 * 
	 * @param CandidateOverseasExperience
	 * @return VOCandidateOverseasExperience , RETURNS NULL IF ENTITY IS NULL
	 **/
	public static VOCandidateOverseasExperience convertToCandidateOverseasDetailModel(
			CandidateOverseasExperience entity) {

		VOCandidateOverseasExperience model = null;
		if (entity != null) {

			model = new VOCandidateOverseasExperience();
			model.setId(entity.getId());

			model.setCandidateProfessionalDetail(null);
			model.setCompany(entity.getCompany());
			model.setProject(entity.getProject());
			// model.setFromDate(entity.getFromDate());
			// model.setToDate(entity.getToDate());
			model.setFromDate(HRMSDateUtil.parse(entity.getFromDate().toString(), IHRMSConstants.POSTGRE_DATE_FORMAT));
			model.setToDate(HRMSDateUtil.parse(entity.getToDate().toString(), IHRMSConstants.POSTGRE_DATE_FORMAT));
			model.setLocation(entity.getLocation());
			model.setDuration(entity.getDuration());
			model.setResponsibility(entity.getResponsibility());

		}
		return model;
	}

	/**
	 * CUSTOM MAPPER TO RETURN CANDIDATE PREVIOUS EMPLOYMENT DETAIL MODEL FROM
	 * CANDIDATE PREVIOUS EMPLOYMENT DETAIL ENTITY
	 * 
	 * @param CandidatePreviousEmployment
	 * @return VOCandidatePreviousEmployment , RETURNS NULL IF ENTITY IS NULL
	 **/
	public static VOCandidatePreviousEmployment convertToCandidatePreviousEmploymentModel(
			CandidatePreviousEmployment entity) {

		VOCandidatePreviousEmployment model = null;
		if (entity != null) {

			model = new VOCandidatePreviousEmployment();
			model.setId(entity.getId());
			model.setCandidateProfessionalDetail(null);
			model.setCompanyName(entity.getCompanyName());
			model.setCompanyAddress(entity.getCompanyAddress());
			model.setCity(convertMasterCityEntityToModel(entity.getCity()));
			model.setState(convertMasterStateEntityToModel(entity.getState()));
			model.setCountry(convertMasterCountryEntityModel(entity.getCountry()));

			model.setFromDate(HRMSDateUtil.parse(entity.getFromDate().toString(), IHRMSConstants.POSTGRE_DATE_FORMAT));
			model.setToDate(HRMSDateUtil.parse(entity.getToDate().toString(), IHRMSConstants.POSTGRE_DATE_FORMAT));

			// model.setFromDate(entity.getFromDate());
			// model.setToDate(entity.getToDate());
			model.setExperience(entity.getExperience());
			model.setDesignation(entity.getDesignation());
			model.setJobType(entity.getJobType());
			model.setPreviousManager(entity.getPreviousManager());
			model.setPreviousManagerContactNumber(entity.getPreviousManagerContactNumber());
			model.setReasonForleaving(entity.getReasonForleaving());
			model.setOverseas(entity.getOverseas());
			model.setIsRelevant(entity.getIsRelevant());
			model.setPreviousManagerEmail(entity.getPreviousManagerEmail());

		}
		return model;
	}

	/**
	 * CUSTOM MAPPER TO RETURN CANDIDATE QUALIFICATION DETAIL MODEL FROM CANDIDATE
	 * QUALIFICATION DETAIL ENTITY
	 * 
	 * @param CandidateQualification
	 * @return VOCandidateQualification , RETURNS NULL IF ENTITY IS NULL
	 **/
	public static VOCandidateQualification convertToCandidateQualificationDetailModel(CandidateQualification entity) {

		VOCandidateQualification model = null;
		if (entity != null) {

			model = new VOCandidateQualification();
			model.setId(entity.getId());
			model.setCandidateProfessionalDetail(null);
			model.setQualification(entity.getQualification());
			model.setDegree(entity.getDegree());
			model.setSubjectOfSpecialization(entity.getSubjectOfSpecialization());
			model.setInstituteName(entity.getInstituteName());
			model.setBoardUniversity(entity.getBoardUniversity());
			model.setModeOfEducation(entity.getModeOfEducation());
			model.setStateLocation(entity.getStateLocation());
			model.setGradeDivisionPercentage(entity.getGradeDivisionPercentage());
			// model.setPassingYearMonth(entity.getPassingYearMonth());
			model.setPassingYearMonth(
					HRMSDateUtil.parse(entity.getPassingYearMonth().toString(), IHRMSConstants.POSTGRE_DATE_FORMAT));
			model.setAcademicAchievements(entity.getAcademicAchievements());
			model.setRemarks(entity.getRemarks());

		}
		return model;
	}

	public static VOMasterDepartment convertToVoMasterDepartment(MasterDepartment entity) {
		VOMasterDepartment model = null;
		if (entity != null) {
			model = new VOMasterDepartment();
			model.setId(entity.getId());
			model.setDepartmentName(entity.getDepartmentName());
			model.setDepartmentDescription(entity.getDepartmentDescription());
		}
		return model;
	}

	public static VOMasterDesignation convertToVoMasterDesignation(MasterDesignation entity) {
		VOMasterDesignation model = null;
		if (entity != null) {
			model = new VOMasterDesignation();
			model.setId(entity.getId());
			model.setDesignationName(entity.getDesignationName());
			model.setDesignationDescription(entity.getDesignationDescription());
		}
		return model;
	}

	public static VOMasterDivision convertToVoMasterDivision(MasterDivision entity) {
		VOMasterDivision model = null;
		if (entity != null) {
			model = new VOMasterDivision();
			model.setId(entity.getId());
			model.setDivisionName(entity.getDivisionName());
			model.setDivisionDescription(entity.getDivisionDescription());
		}
		return model;
	}

	public static VOMasterBranch convertToVoMasterBranch(MasterBranch entity) {
		VOMasterBranch model = null;
		if (entity != null) {
			model = new VOMasterBranch();
			model.setId(entity.getId());
			model.setBranchName(entity.getBranchName());
			model.setBranchDescription(entity.getBranchDescription());
		}
		return model;
	}

	/**
	 * Custom mapper to return candidate professional details model from entity
	 * 
	 * @param CandidateProfessionalDetail
	 * @return VOCandidateProfessionalDetail , RETURNS NULL IF ENTITY IS NULL
	 * @author nitin.shome
	 **/
	public static VOCandidateProfessionalDetail convertToCandidateProfessionalDetailsDetailModel(
			CandidateProfessionalDetail entity) {

		VOCandidateProfessionalDetail model = null;

		if (entity != null) {

			model = new VOCandidateProfessionalDetail();

			Set<VOCandidateCertification> certificationSetModel = null;
			Set<VOCandidateChecklist> checklistSetModel = null;
			Set<VOCandidateOverseasExperience> overseasExpSetModel = null;
			Set<VOCandidateQualification> qualificationSetModel = null;
			Set<VOCandidatePreviousEmployment> previousEmploymentSetModel = null;

			/**
			 * Candidate certification
			 */
			Set<CandidateCertification> certificationSetEntity = entity.getCandidateCertifications();
			if (certificationSetEntity != null) {

				certificationSetModel = new HashSet<VOCandidateCertification>();
				for (CandidateCertification certificateEntity : certificationSetEntity) {

					VOCandidateCertification certificationmodel = HRMSEntityToModelMapper
							.convertToCandidateCertificateDetailsModel(certificateEntity);
					certificationSetModel.add(certificationmodel);
				}
			}

			/**
			 * Candidate checklist
			 */
			Set<CandidateChecklist> checklistSetEntity = entity.getCandidateChecklists();
			if (checklistSetEntity != null) {
				checklistSetModel = new HashSet<VOCandidateChecklist>();
				for (CandidateChecklist checklistEntity : checklistSetEntity) {

					VOCandidateChecklist checklistmodel = HRMSEntityToModelMapper
							.convertToCandidateChecklistDetailsModel(checklistEntity);
					checklistSetModel.add(checklistmodel);
				}
			}

			/**
			 * Candidate overseas experience
			 */
			Set<CandidateOverseasExperience> overseasExpSetEntity = entity.getCandidateOverseasExperiences();
			if (overseasExpSetEntity != null) {
				overseasExpSetModel = new HashSet<VOCandidateOverseasExperience>();
				for (CandidateOverseasExperience overseasExpEntity : overseasExpSetEntity) {

					VOCandidateOverseasExperience overseasmodel = HRMSEntityToModelMapper
							.convertToCandidateOverseasDetailModel(overseasExpEntity);
					overseasExpSetModel.add(overseasmodel);
				}
			}

			/**
			 * Candidate previous employment
			 */
			Set<CandidatePreviousEmployment> previousEmploymentSetEntity = entity.getCandidatePreviousEmployments();
			if (previousEmploymentSetEntity != null) {
				previousEmploymentSetModel = new HashSet<VOCandidatePreviousEmployment>();
				for (CandidatePreviousEmployment previousEmploymentEntity : previousEmploymentSetEntity) {

					VOCandidatePreviousEmployment previousEmploymentModel = HRMSEntityToModelMapper
							.convertToCandidatePreviousEmploymentModel(previousEmploymentEntity);
					previousEmploymentSetModel.add(previousEmploymentModel);
				}
			}

			/**
			 * Candidate qualification
			 */
			Set<CandidateQualification> qualificationSetSetEntity = entity.getCandidateQualifications();
			if (qualificationSetSetEntity != null) {
				qualificationSetModel = new HashSet<VOCandidateQualification>();
				for (CandidateQualification qualificationEntity : qualificationSetSetEntity) {

					VOCandidateQualification qualificationModel = HRMSEntityToModelMapper
							.convertToCandidateQualificationDetailModel(qualificationEntity);
					qualificationSetModel.add(qualificationModel);
				}
			}

			/**
			 * Candidate designation,department,division,branch
			 */
			VOMasterDesignation masterDesginationModel = HRMSEntityToModelMapper
					.convertToVoMasterDesignation(entity.getDesignation());
			VOMasterDepartment masterDepartmentModel = HRMSEntityToModelMapper
					.convertToVoMasterDepartment(entity.getDepartment());
			VOMasterDivision masterDivisionModel = HRMSEntityToModelMapper
					.convertToVoMasterDivision(entity.getDivision());
			VOMasterBranch masterBranchModel = HRMSEntityToModelMapper.convertToVoMasterBranch(entity.getBranch());
			VOMasterBranch workingLocationModel = HRMSEntityToModelMapper
					.convertToVoMasterBranch(entity.getWorkingLocation());

			/**
			 * Candidate basic info
			 */
			model.setCandidateCertifications(certificationSetModel);
			model.setCandidateChecklists(checklistSetModel);
			model.setCandidateOverseasExperiences(overseasExpSetModel);
			model.setCandidatePreviousEmployments(previousEmploymentSetModel);
			model.setCandidateQualifications(qualificationSetModel);
			model.setCity(convertMasterCityEntityToModel(entity.getCity()));
			model.setState(convertMasterStateEntityToModel(entity.getState()));
			model.setCountry(convertMasterCountryEntityModel(entity.getCountry()));
			model.setDesignation(masterDesginationModel);
			model.setDepartment(masterDepartmentModel);
			model.setDivision(masterDivisionModel);
			model.setBranch(masterBranchModel);
			model.setWorkingLocation(workingLocationModel);
			model.setId(entity.getId());
			model.setCandidate(null);
			// model.setRequisitionDate(entity.getRequisitionDate());
			// model.setDateOfJoining(entity.getDateOfJoining());
			if (entity.getRequisitionDate() != null)
				model.setRequisitionDate(
						HRMSDateUtil.parse(entity.getRequisitionDate().toString(), IHRMSConstants.POSTGRE_DATE_FORMAT));
			else
				model.setRequisitionDate(entity.getRequisitionDate());
			if (entity.getDateOfJoining() != null)
				model.setDateOfJoining(
						HRMSDateUtil.parse(entity.getDateOfJoining().toString(), IHRMSConstants.POSTGRE_DATE_FORMAT));
			else
				model.setDateOfJoining(entity.getDateOfJoining());

			// model.setRecruiterName(entity.getRecruiterName());
			model.setRecruiter(convertToEmployeeModel(entity.getRecruiter()));
			model.setPanCard(entity.getPanCard());
			model.setAadhaarCard(entity.getAadhaarCard());
			model.setMarkLetterTo(entity.getMarkLetterTo());
			model.setComment(entity.getComment());
			model.setUan(entity.getUan());
		}
		return model;
	}

	/**
	 * Custom mapper to return candidate person detail model from candidate personal
	 * detail entity
	 * 
	 * @param CandidatePersonalDetail
	 * @return VOCandidatePersonalDetail , Returns null if entity is null
	 * @author shome.nitin
	 **/
	public static VOCandidatePersonalDetail convertToCandidatePersonalDetailsDetailModel(
			CandidatePersonalDetail entity) {

		logger.info(" == >> Creating Candidate Personal details model << == ");

		VOCandidatePersonalDetail model = null;

		if (entity != null) {

			/**
			 * Setting Candidate personal Detail Outer Object
			 */
			model = new VOCandidatePersonalDetail();
			model.setId(entity.getId());
			model.setCitizenship(entity.getCitizenship());
			model.setCandidatePhoto(entity.getCandidatePhoto());
			model.setSpouseName(entity.getSpouseName());
			model.setEsiNo(entity.getEsiNo());
			model.setEsiDispensary(entity.getEsiDispensary());
			model.setMaritalStatus(entity.getMaritalStatus());
			model.setDrivingLicense(entity.getDrivingLicense());
			model.setOfficialMobileNumber(entity.getOfficialMobileNumber());
			model.setScStReservationStatus(entity.getScStReservationStatus());
			model.setNoOfChildren(entity.getNoOfChildren());
			model.setNationality(entity.getNationality());
			model.setCitizenship(entity.getCitizenship());
			model.setReligion(entity.getReligion());
			model.setPlaceOfBirth(entity.getPlaceOfBirth());
			model.setMaritalStatus(entity.getMaritalStatus());
			// model.setMappedIMEI(entity.getMappedIMEI());

			Set<VOCandidateEmergencyContact> emergencyContactSetModel = null;
			Set<VOCandidateFamilyDetail> familyDetailsSetModel = null;
			Set<VOCandidateLanguage> languageSetModel = null;
			Set<VOCandidateVisaDetail> visaDetailsSetModel = null;

			/**
			 * Setting Emergency Contact Details
			 */
			Set<CandidateEmergencyContact> emergencyContactSetEntity = entity.getCandidateEmergencyContacts();
			if (emergencyContactSetEntity != null) {
				emergencyContactSetModel = new HashSet<VOCandidateEmergencyContact>();
				for (CandidateEmergencyContact emergencyContactEntity : emergencyContactSetEntity) {

					VOCandidateEmergencyContact candidateEmergencyConctactModel = HRMSEntityToModelMapper
							.convertToEmergencyContactModel(emergencyContactEntity);
					emergencyContactSetModel.add(candidateEmergencyConctactModel);
				}
			}

			/**
			 * Setting Family Details
			 */
			Set<CandidateFamilyDetail> familyDetailsSetEntity = entity.getCandidateFamilyDetails();
			if (familyDetailsSetEntity != null) {
				familyDetailsSetModel = new HashSet<VOCandidateFamilyDetail>();
				for (CandidateFamilyDetail familyDetailsEntity : familyDetailsSetEntity) {

					VOCandidateFamilyDetail familyDetails = HRMSEntityToModelMapper
							.convertToFamilyDetailModel(familyDetailsEntity);
					familyDetailsSetModel.add(familyDetails);
				}
			}

			/**
			 * Setting Language Details
			 */
			Set<CandidateLanguage> languageSetEntity = entity.getCandidateLanguages();
			if (languageSetEntity != null) {
				languageSetModel = new HashSet<VOCandidateLanguage>();
				for (CandidateLanguage languageEntity : languageSetEntity) {

					VOCandidateLanguage languagemodel = HRMSEntityToModelMapper
							.convertToCandidateLanguageModel(languageEntity);
					languageSetModel.add(languagemodel);
				}
			}

			/**
			 * Setting Visa Details
			 */
			Set<CandidateVisaDetail> visaDetailsSetEntity = entity.getCandidateVisaDetails();
			if (visaDetailsSetEntity != null) {
				visaDetailsSetModel = new HashSet<VOCandidateVisaDetail>();
				for (CandidateVisaDetail visaDetailEntity : visaDetailsSetEntity) {

					VOCandidateVisaDetail visaDetailmodel = HRMSEntityToModelMapper
							.convertToCandidateVisaDetailModel(visaDetailEntity);
					visaDetailsSetModel.add(visaDetailmodel);
				}
			}

			model.setCandidateEmergencyContacts(emergencyContactSetModel);
			model.setCandidateLanguages(languageSetModel);
			model.setCandidateVisaDetails(visaDetailsSetModel);
			model.setCandidateFamilyDetails(familyDetailsSetModel);

			/**
			 * Setting Passport,Policy,Nomindation,Health Report Details
			 */
			model.setCandidatePassportDetail(
					HRMSEntityToModelMapper.convertToPassportDetailModel(entity.getCandidatePassportDetail()));
			model.setCandidatePolicyDetail(
					HRMSEntityToModelMapper.convertToCandidatePolicyDetailModel(entity.getCandidatePolicyDetail()));
			model.setCandidateStatutoryNomination(HRMSEntityToModelMapper
					.convertToCandidateStatutoryNominationDetailModel(entity.getCandidateStatutoryNomination()));
			model.setCandidateHealthReport(
					HRMSEntityToModelMapper.convertToHealthDetailModel(entity.getCandidateHealthReport()));
			model.setCandidatePhoto(entity.getCandidatePhoto());
		}
		return model;
	}

	/**
	 * CUSTOM MAPPER TO RETURN SUBSCRIPTION MODEL FROM SUBSCRIPTION ENTITY
	 * 
	 * @param Subscription
	 * @return VOSubscription , RETURNS NULL IF ENTITY IS NULL
	 * @author shome.nitin
	 **/
	public static VOSubscription convertToSubscriptionModel(Subscription entity) {

		VOSubscription subscriptionModel = null;
		if (entity != null) {
			subscriptionModel = new VOSubscription();
			subscriptionModel.setEndDate(entity.getEndDate());
			subscriptionModel.setStartDate(entity.getStartDate());
			subscriptionModel.setId(entity.getId());
			subscriptionModel.setSubscriptionKey(entity.getSubscriptionKey());
			subscriptionModel.setSubscriptionType(entity.getSubscriptionType());
			subscriptionModel.setOrganization(null);
		}
		return subscriptionModel;
	}

	public static VOEmployeeCreditLeaveDetail convertToEmployeeCreditLeaveDetailModel(
			EmployeeCreditLeaveDetail entity) {
		VOEmployeeCreditLeaveDetail model = null;
		if (entity != null) {
			model = new VOEmployeeCreditLeaveDetail();
			model.setCreditedBy(entity.getCreditedBy());
			if (entity.getFromDate() != null)
				model.setFromDate(
						HRMSDateUtil.parse(entity.getFromDate().toString(), IHRMSConstants.POSTGRE_DATE_FORMAT));
			else
				model.setFromDate(entity.getFromDate());
			model.setId(entity.getId());
			model.setNoOfDays(entity.getNoOfDays());
			model.setOpeningBalanceConsidered(entity.getOpeningBalanceConsidered());
			if (entity.getToDate() != null)
				model.setToDate(HRMSDateUtil.parse(entity.getToDate().toString(), IHRMSConstants.POSTGRE_DATE_FORMAT));
			else
				model.setToDate(entity.getToDate());
			model.setComment(entity.getComment());
			if (entity.getPostedDate() != null)
				model.setPostedDate(
						HRMSDateUtil.parse(entity.getPostedDate().toString(), IHRMSConstants.POSTGRE_DATE_FORMAT));
			else
				model.setPostedDate(entity.getPostedDate());

			model.setMasterLeaveType(convertToMasterLeaveTypeModel(entity.getMasterLeaveType()));
			// model.setEmployee(convertToEmployeeModel(employeeCreditLeaveDetailEntity.getEmployee()));
			model.setCreatedBy(entity.getCreatedBy());
			model.setCreatedDate(entity.getCreatedDate());
			model.setUpdatedBy(entity.getUpdatedBy());
			model.setUpdatedDate(entity.getUpdatedDate());
		}
		return model;
	}

	/**
	 * Custom mapper to convert employee entity to employe model,
	 * 
	 * @param Employee
	 * @return VOEmployee
	 * @author shome.nitin
	 */
	public static VOEmployee convertToEmployeeModel(Employee employeeEntity) {
		VOEmployee voEmployee = null;
		if (employeeEntity != null) {
			voEmployee = new VOEmployee();
			voEmployee.setCreatedBy(employeeEntity.getCreatedBy());
			voEmployee.setEmployeeProbationPeriod(employeeEntity.getProbationPeriod());
			if (!HRMSHelper.isNullOrEmpty(employeeEntity.getEmployeeACN()))
				voEmployee.setEmpACN(String.valueOf(employeeEntity.getEmployeeACN().getEmpACN()));
			else
				voEmployee.setEmpACN("");
			voEmployee.setCreatedDate(employeeEntity.getCreatedDate());
			voEmployee.setId(employeeEntity.getId());
			voEmployee.setOfficialEmailId(employeeEntity.getOfficialEmailId());
			voEmployee.setPositionCode(employeeEntity.getPositionCode());
			voEmployee.setPositionCodeEffectiveDate(employeeEntity.getPositionCodeEffectiveDate());
			voEmployee.setRemark(employeeEntity.getRemark());
			voEmployee.setUpdatedDate(employeeEntity.getUpdatedDate());
			voEmployee.setUpdatedBy(employeeEntity.getUpdatedBy());
			voEmployee.setCandidate(convertToCandidateModel(employeeEntity.getCandidate()));
			voEmployee.setEmployeeReportingManager(
					convertToEmployeeReportingManager(employeeEntity.getEmployeeReportingManager()));
			voEmployee.setEmployeeBranch(convertToEmployeeBranch(employeeEntity.getEmployeeBranch()));
			voEmployee.setEmployeeDepartment(convertToEmployeeDepartment(employeeEntity.getEmployeeDepartment()));
			voEmployee.setEmployeeCurrentDetail(
					convertToEmployeCurrentDetailModel(employeeEntity.getEmployeeCurrentDetail()));
			voEmployee.setEmployeeDesignation(convertToEmployeeDesignation(employeeEntity.getEmployeeDesignation()));
			voEmployee.setEmployeeDivision(convertToEmployeeDivision(employeeEntity.getEmployeeDivision()));
			voEmployee.setEmployeeEmploymentType(
					convertToEmployeeEmploymentType(employeeEntity.getEmployeeEmploymentType()));
			voEmployee.setEmployeeACN(convertToModelEmployeeACN(employeeEntity.getEmployeeACN()));

		}
		return voEmployee;
	}

	/**
	 * Custom mapper to convert candidate entity to candidate model,
	 * 
	 * @param Candidate
	 * @return VOCandidate
	 * @author shome.nitin
	 */
	public static VOCandidate convertToCandidateModel(Candidate candidate) {

		VOCandidate voCandidate = null;
		if (candidate != null) {
			voCandidate = new VOCandidate();
			voCandidate.setId(candidate.getId());
			voCandidate.setFirstName(candidate.getFirstName());
			voCandidate.setLastName(candidate.getLastName());
			voCandidate.setMiddleName(candidate.getMiddleName());
			voCandidate.setEmailId(candidate.getEmailId());
			voCandidate.setGender(candidate.getGender());
			voCandidate.setTitle(candidate.getTitle());
			voCandidate.setMobileNumber(candidate.getMobileNumber());
			voCandidate.setDateOfBirth(candidate.getDateOfBirth());
			voCandidate.setLoginEntity(convertToLoginEntityModel(candidate.getLoginEntity()));
			voCandidate.setEmploymentType(convertToMasterEmploymentType(candidate.getEmploymentType()));
		}
		return voCandidate;
	}

	/**
	 * Custom mapper to convert candidate Login entity to candidate login entity
	 * model,
	 * 
	 * @param LoginEntity
	 * @return VOLoginEntity
	 * @author shome.nitin
	 */
	public static VOLoginEntity convertToLoginEntityModel(LoginEntity entity) {

		VOLoginEntity model = null;
		if (entity != null) {
			model = new VOLoginEntity();
			model.setId(entity.getId());
			/*
			 * change by ssw on 01jan2017 for : manyToMany loginEntity type mapping
			 */
			// model.setLoginEntityType(convertToLoginEntityTypeModel(entity.getLoginEntityType()));
			model.setLoginEntityTypes(convertToSetLoginEntityTypeModel(entity.getLoginEntityTypes()));
			/*
			 * upto this added ssw
			 */

		}
		return model;
	}

	/**
	 * Custom mapper to convert candidate Login entity type to candidate login
	 * entity type model,
	 * 
	 * @param LoginEntityType
	 * @return VOLoginEntityType
	 * @author shome.nitin
	 */
	public static VOLoginEntityType convertToLoginEntityTypeModel(LoginEntityType entity) {

		VOLoginEntityType model = null;
		if (entity != null) {
			model = new VOLoginEntityType();
			model.setId(entity.getId());
			model.setLoginEntityTypeName(entity.getLoginEntityTypeName());
		}
		return model;
	}

	/**
	 * CUSTOM MAPPER TO RETURN EMPLOYEE LEAVE DETAIL MODEL FROM EMPLOYEE LEAVE
	 * DETAIL ENTITY
	 * 
	 * @param EmployeeLeaveDetail
	 * @return VOEmployeeLeaveDetail , RETURNS NULL IF ENTITY IS NULL
	 * @author shome.nitin
	 **/
	public static VOEmployeeLeaveDetail convertToEmployeeLeaveModel(EmployeeLeaveDetail entity) {

		VOEmployeeLeaveDetail model = null;
		if (entity != null) {
			model = new VOEmployeeLeaveDetail();
			model.setId(entity.getId());
			model.setEmployee(null);
			model.setMasterLeaveType(convertToMasterLeaveTypeModel(entity.getMasterLeaveType()));
			model.setClosingBalance(
					HRMSHelper.isNullOrEmpty(entity.getClosingBalance()) ? 0 : entity.getClosingBalance());
			model.setPyLeaveEncashment(
					HRMSHelper.isNullOrEmpty(entity.getPyLeaveEncashment()) ? 0 : entity.getPyLeaveEncashment());
			model.setLeaveCarriedOver(
					HRMSHelper.isNullOrEmpty(entity.getLeaveCarriedOver()) ? 0 : entity.getLeaveCarriedOver());
			model.setLeaveEarned(HRMSHelper.isNullOrEmpty(entity.getLeaveEarned()) ? 0 : entity.getLeaveEarned());
			model.setFyLeaveEncashment(
					HRMSHelper.isNullOrEmpty(entity.getFyLeaveEncashment()) ? 0 : entity.getFyLeaveEncashment());
			model.setTotalEligibility(
					HRMSHelper.isNullOrEmpty(entity.getTotalEligibility()) ? 0 : entity.getTotalEligibility());
			model.setNumberOfDaysAvailed(
					HRMSHelper.isNullOrEmpty(entity.getNumberOfDaysAvailed()) ? 0 : entity.getNumberOfDaysAvailed());
			model.setLeaveAvailable(
					HRMSHelper.isNullOrEmpty(entity.getLeaveAvailable()) ? 0 : entity.getLeaveAvailable());
		}
		return model;
	}

	/**
	 * CUSTOM MAPPER TO RETURN MASTER LEAVE TYPE MODEL FROM MASTER LEAVE DETAIL
	 * ENTITY
	 * 
	 * @param EmployeeLeaveDetail
	 * @return VOEmployeeLeaveDetail , RETURNS NULL IF ENTITY IS NULL
	 * @author shome.nitin
	 **/
	public static VOMasterLeaveType convertToMasterLeaveTypeModel(MasterLeaveType entity) {

		VOMasterLeaveType model = null;
		if (entity != null) {
			model = new VOMasterLeaveType();
			model.setId(entity.getId());
			model.setLeaveTypeDescription(entity.getLeaveTypeDescription());
			model.setLeaveTypeName(entity.getLeaveTypeName());
			model.setOrganization(null);
		}
		return model;
	}

	/**
	 * CUSTOM MAPPER TO RETURN EMPLOYEE LEAVE APPLIED MODEL FROM EMPLOYEE LEAVE
	 * APPLIEDL ENTITY
	 * 
	 * @param EmployeeLeaveApplied
	 * @return VOEmployeeLeaveApplied , RETURNS NULL IF ENTITY IS NULL
	 * @author shome.nitin
	 **/
	public static VOEmployeeLeaveApplied convertToEmployeLeaveAppliedModel(EmployeeLeaveApplied entity) {

		VOEmployeeLeaveApplied model = null;
		if (entity != null) {
			model = new VOEmployeeLeaveApplied();
			model.setId(entity.getId());
			model.setEmployee(convertToEmployeeModel(entity.getEmployee()));
			model.setMasterLeaveType(convertToMasterLeaveTypeModel(entity.getMasterLeaveType()));
			// model.setFromDate(HRMSDateUtil.format(entity.getFromDate(),
			// IHRMSConstants.FRONT_END_DATE_FORMAT));
			// model.setToDate(HRMSDateUtil.format(entity.getToDate(),
			// IHRMSConstants.FRONT_END_DATE_FORMAT));

			model.setFromDate(HRMSDateUtil.format(entity.getFromDate(), IHRMSConstants.POSTGRE_DATE_FORMAT));
			model.setToDate(HRMSDateUtil.format(entity.getToDate(), IHRMSConstants.POSTGRE_DATE_FORMAT));

			model.setFromSession(entity.getFromSession());
			model.setToSession(entity.getToSession());
			model.setContactDetails(entity.getContactDetails());
			model.setCc(entity.getCc());
			model.setLeaveStatus(entity.getLeaveStatus());
			model.setReasonForApply(entity.getReasonForApply());
			model.setAttachment(entity.getAttachment());
			model.setReasonForCancel(entity.getReasonForCancel());
			/*
			 * model.setDateOfApplied( HRMSDateUtil.format(entity.getDateOfApplied(),
			 * IHRMSConstants.FRONT_END_DATE_FORMAT));
			 */

			model.setDateOfApplied(HRMSDateUtil.format(entity.getDateOfApplied(), IHRMSConstants.POSTGRE_DATE_FORMAT));

			model.setDateOfApproverAction(entity.getDateOfApproverAction());
			model.setAppliedBy(entity.getAppliedBy());
			model.setDateOfCancel(entity.getDateOfCancle());
			model.setReasonForWithdrawn(entity.getReasonForWithdrawn());
			model.setDateOfWithdrawn(entity.getDateOfWithdrawn());
			model.setApproverCommentOnWithdrawn(entity.getApproverCommentOnWithdrawn());
			model.setApproverActionDateWithdrawn(entity.getApproverActionDateWithdrawn());
			model.setNoOfDays(entity.getNoOfDays());
		}
		return model;
	}

	/**
	 * CUSTOM MAPPER TO RETURN EMPLOYEE CURRENT ORGANIZATION DETAILS MODEL FROM
	 * CURRENT ORGANIZATION DETAILS ENTITY
	 * 
	 * @param EmployeeCurrentDetail
	 * @return VOEmployeeCurrentDetail , RETURNS NULL IF ENTITY IS NULL
	 * @author shome.nitin
	 **/
	public static VOEmployeeCurrentDetail convertToEmployeCurrentDetailModel(EmployeeCurrentDetail entity) {

		VOEmployeeCurrentDetail model = null;
		if (entity != null) {
			model = new VOEmployeeCurrentDetail();
			model.setId(entity.getId());
			model.setEmployee(null);
			model.setBandGrade(entity.getBandGrade());
			model.setResponsibility(entity.getResponsibility());
			model.setProcess(entity.getProcess());
			model.setNoticePeriod(entity.getNoticePeriod());
			model.setState(entity.getState());
			model.setPtState(entity.getPtState());
			model.setCity(entity.getCity());
			model.setProject(entity.getProject());
			model.setBillable(entity.getBillable());
			model.setCostCenter(entity.getCostCenter());
			if (entity.getRetirementDate() != null)
				model.setRetirementDate(
						HRMSDateUtil.parse(entity.getRetirementDate().toString(), IHRMSConstants.POSTGRE_DATE_FORMAT));
			else
				model.setRetirementDate(entity.getRetirementDate());
			model.setHousingStatus(entity.getHousingStatus());
			model.setConfirmationStatus(entity.getConfirmationStatus());
			// model.setWorkShift(entity.getWorkShift());
		}
		return model;
	}

	/**
	 * CUSTOM MAPPER TO RETURN EMPLOYEE BRANCH DETAILS MODEL FROM EMPLOYEE BRANCH
	 * ENTITY
	 * 
	 * @param EmployeeBranch
	 * @return VOEmployeeBranch , RETURNS NULL IF ENTITY IS NULL
	 * @author shome.nitin
	 **/
	public static VOEmployeeBranch convertToEmployeeBranch(EmployeeBranch entity) {
		VOEmployeeBranch model = null;
		if (entity != null) {
			model = new VOEmployeeBranch();
			model.setId(entity.getId());
			model.setBranch(convertToVoMasterBranch(entity.getBranch()));
			model.setEmployee(null);
		}
		return model;
	}

	/**
	 * CUSTOM MAPPER TO RETURN EMPLOYEE Department DETAILS MODEL FROM EMPLOYEE
	 * Department BRANCH ENTITY
	 * 
	 * @param EmployeeDepartment
	 * @return VOEmployeeDepartment , RETURNS NULL IF ENTITY IS NULL
	 * @author shome.nitin
	 **/
	public static VOEmployeeDepartment convertToEmployeeDepartment(EmployeeDepartment entity) {
		VOEmployeeDepartment model = null;
		if (entity != null) {
			model = new VOEmployeeDepartment();
			model.setId(entity.getId());
			model.setDepartment(convertToVoMasterDepartment(entity.getDepartment()));
			model.setEmployee(null);
		}
		return model;
	}

	/**
	 * CUSTOM MAPPER TO RETURN EMPLOYEE Designation DETAILS MODEL FROM EMPLOYEE
	 * Designation BRANCH ENTITY
	 * 
	 * @param EmployeeDesignation
	 * @return VOEmployeeDesignation , RETURNS NULL IF ENTITY IS NULL
	 * @author shome.nitin
	 **/
	public static VOEmployeeDesignation convertToEmployeeDesignation(EmployeeDesignation entity) {
		VOEmployeeDesignation model = null;
		if (entity != null) {
			model = new VOEmployeeDesignation();
			model.setId(entity.getId());
			model.setDesignation(convertToVoMasterDesignation(entity.getDesignation()));
			model.setEmployee(null);
		}
		return model;
	}

	/**
	 * CUSTOM MAPPER TO RETURN EMPLOYEE Division DETAILS MODEL FROM EMPLOYEE
	 * Division BRANCH ENTITY
	 * 
	 * @param EmployeeDivision
	 * @return VOEmployeeDivision , RETURNS NULL IF ENTITY IS NULL
	 * @author shome.nitin
	 **/
	public static VOEmployeeDivision convertToEmployeeDivision(EmployeeDivision entity) {
		VOEmployeeDivision model = null;
		if (entity != null) {
			model = new VOEmployeeDivision();
			model.setId(entity.getId());
			model.setDivision(convertToVoMasterDivision(entity.getDivision()));
			model.setEmployee(null);
		}
		return model;
	}

	/**
	 * CUSTOM MAPPER TO RETURN EmployeeEmploymentType DETAILS MODEL FROM
	 * EmployeeEmploymentType BRANCH ENTITY
	 * 
	 * @param EmployeeEmploymentType
	 * @return VOEmployeeEmploymentType , RETURNS NULL IF ENTITY IS NULL
	 * @author shome.nitin
	 **/
	public static VOEmployeeEmploymentType convertToEmployeeEmploymentType(EmployeeEmploymentType entity) {
		VOEmployeeEmploymentType model = null;
		if (entity != null) {
			model = new VOEmployeeEmploymentType();
			model.setId(entity.getId());
			model.setEmploymentType(convertToMasterEmploymentType(entity.getEmploymentType()));
			model.setEmployee(null);
		}
		return model;
	}

	/**
	 * CUSTOM MAPPER TO RETURN MasterEmploymentType DETAILS MODEL FROM
	 * MasterEmploymentType BRANCH ENTITY
	 * 
	 * @param MasterEmploymentType
	 * @return VOMasterEmploymentType , RETURNS NULL IF ENTITY IS NULL
	 * @author shome.nitin
	 **/
	public static VOMasterEmploymentType convertToMasterEmploymentType(MasterEmploymentType entity) {
		VOMasterEmploymentType model = null;

		if (entity != null) {
			model = new VOMasterEmploymentType();
			model.setEmploymentTypeDescription(entity.getEmploymentTypeDescription());
			model.setEmploymentTypeName(entity.getEmploymentTypeName());
			model.setId(entity.getId());
			model.setOrganization(null);

		}

		return model;
	}

	/**
	 * CUSTOM MAPPER TO RETURN EmployeeReportingManager DETAILS MODEL FROM
	 * EmployeeReportingManager ENTITY
	 * 
	 * @param EmployeeReportingManager
	 * @return VOEmployeeReportingManager , RETURNS NULL IF ENTITY IS NULL
	 * @author shome.nitin
	 **/
	public static VOEmployeeReportingManager convertToEmployeeReportingManager(EmployeeReportingManager entity) {

		VOEmployeeReportingManager model = null;
		if (entity != null) {
			model = new VOEmployeeReportingManager();
			model.setId(entity.getId());
			model.setEmployee(convertToEmployeeBrief(entity.getEmployee()));
			model.setReportingManager(convertToEmployeeBrief(entity.getReporingManager()));
		}
		return model;
	}

	/**
	 * Custom mapper to return employee details in brief to avoid recursion issue
	 * 
	 * @param Employee (Reporting manager,Employee as both are employee)
	 * @return VOEmployee , returns null if entity is null
	 * @author shome.nitin
	 **/
	public static VOEmployee convertToEmployeeBrief(Employee entity) {
		VOEmployee model = null;
		if (entity != null) {
			model = new VOEmployee();
			model.setCreatedBy(entity.getCreatedBy());
			model.setCreatedDate(entity.getCreatedDate());
			model.setId(entity.getId());
			model.setOfficialEmailId(entity.getOfficialEmailId());
			model.setPositionCode(entity.getPositionCode());
			model.setPositionCodeEffectiveDate(entity.getPositionCodeEffectiveDate());
			model.setRemark(entity.getRemark());
			model.setUpdatedDate(entity.getUpdatedDate());
			model.setUpdatedBy(entity.getUpdatedBy());
			model.setCandidate(convertToCandidateModel(entity.getCandidate()));

		}
		return model;
	}

	/**
	 * @param masterCityEntity
	 * @return VOMasterCity
	 * @author SSW
	 * 
	 *         Custom mapper to return VOMasterCity from entity
	 * 
	 */
	public static VOMasterCity convertMasterCityEntityToModel(MasterCity masterCityEntity) {
		VOMasterCity voMasterCity = null;
		if (!HRMSHelper.isNullOrEmpty(masterCityEntity)) {
			voMasterCity = new VOMasterCity();
			voMasterCity.setCityDescription(masterCityEntity.getCityDescription());
			voMasterCity.setCityName(masterCityEntity.getCityName());
			voMasterCity.setCreatedBy(masterCityEntity.getCreatedBy());
			voMasterCity.setCreatedDate(masterCityEntity.getCreatedDate());
			voMasterCity.setId(masterCityEntity.getId());
			voMasterCity.setIsActive(masterCityEntity.getIsActive());
			voMasterCity.setRemark(masterCityEntity.getRemark());
			voMasterCity.setUpdatedBy(masterCityEntity.getUpdatedBy());
			voMasterCity.setUpdatedDate(masterCityEntity.getUpdatedDate());
		}
		return voMasterCity;
	}

	/**
	 * @param masterStateEntity
	 * @return VOMasterState
	 * @author SSW
	 * 
	 *         Custom mapper to return VOMasterState from entity
	 */
	public static VOMasterState convertMasterStateEntityToModel(MasterState masterStateEntity) {
		VOMasterState voMasterState = null;
		if (!HRMSHelper.isNullOrEmpty(masterStateEntity)) {
			voMasterState = new VOMasterState();
			voMasterState.setCreatedBy(masterStateEntity.getCreatedBy());
			voMasterState.setCreatedDate(masterStateEntity.getCreatedDate());
			voMasterState.setId(masterStateEntity.getId());
			voMasterState.setIsActive(masterStateEntity.getIsActive());
			voMasterState.setRemark(masterStateEntity.getRemark());
			voMasterState.setStateDescription(masterStateEntity.getStateDescription());
			voMasterState.setStateName(masterStateEntity.getStateName());
			voMasterState.setUpdatedBy(masterStateEntity.getUpdatedBy());
			voMasterState.setUpdatedDate(masterStateEntity.getUpdatedDate());
		}
		return voMasterState;
	}

	/**
	 * @param masterCountryEntity
	 * @return VOMasterCountry
	 * @author Nitin shome
	 * 
	 *         Custom mapper to return VOMasterCountry from entity
	 */
	private static VOMasterCountry convertMasterCountryEntityModel(MasterCountry entity) {

		VOMasterCountry model = null;
		if (!HRMSHelper.isNullOrEmpty(entity)) {

			model = new VOMasterCountry();
			model.setCountryDescription(entity.getCountryDescription());
			model.setCountryName(entity.getCountryName());
			model.setCreatedBy(entity.getCreatedBy());
			model.setCreatedDate(entity.getCreatedDate());
			model.setId(entity.getId());
			model.setIsActive(entity.getIsActive());
			model.setRemark(entity.getRemark());
			model.setUpdatedBy(entity.getUpdatedBy());
			model.setUpdatedDate(entity.getUpdatedDate());
		}
		return model;
	}

	/**
	 * @param candidateAddressesEntity of type set
	 * @return Set<VOCandidateAddress>
	 * @author SSW
	 * 
	 *         convert to Set<VOCandidateAddress> from Set<CandidateAddress>
	 */
	public static Set<VOCandidateAddress> convertSetCandidateAddressToModel(
			Set<CandidateAddress> candidateAddressesEntity) {
		Set<VOCandidateAddress> voCandidateAddresses = null;
		if (!HRMSHelper.isNullOrEmpty(candidateAddressesEntity)) {
			// initialize vocandidateAddress
			voCandidateAddresses = new HashSet<VOCandidateAddress>();
			for (CandidateAddress candidateAddressEntityInLoop : candidateAddressesEntity) {
				VOCandidateAddress voCandidateAddress = new VOCandidateAddress();
				voCandidateAddress.setAddressLine1(candidateAddressEntityInLoop.getAddressLine1());
				voCandidateAddress.setAddressLine2(candidateAddressEntityInLoop.getAddressLine2());
				voCandidateAddress.setAddressType(candidateAddressEntityInLoop.getAddressType());
				voCandidateAddress.setCreatedBy(candidateAddressEntityInLoop.getCreatedBy());
				voCandidateAddress.setCreatedDate(candidateAddressEntityInLoop.getCreatedDate());
				voCandidateAddress.setId(candidateAddressEntityInLoop.getId());
				voCandidateAddress.setIsActive(candidateAddressEntityInLoop.getIsActive());
				voCandidateAddress.setPincode(candidateAddressEntityInLoop.getPincode());
				voCandidateAddress.setRemark(candidateAddressEntityInLoop.getRemark());
				voCandidateAddress.setUpdatedBy(candidateAddressEntityInLoop.getUpdatedBy());
				voCandidateAddress.setUpdatedDate(candidateAddressEntityInLoop.getUpdatedDate());
				voCandidateAddress.setCity(convertMasterCityEntityToModel(candidateAddressEntityInLoop.getCity()));
				voCandidateAddress.setState(convertMasterStateEntityToModel(candidateAddressEntityInLoop.getState()));
				voCandidateAddress
						.setCountry(convertMasterCountryEntityModel(candidateAddressEntityInLoop.getCountry()));
				voCandidateAddress.setPhone1(candidateAddressEntityInLoop.getPhone1());
				voCandidateAddress.setPhone2(candidateAddressEntityInLoop.getPhone2());

				if (!HRMSHelper.isNullOrEmpty(candidateAddressEntityInLoop.getIsRental())
						&& IHRMSConstants.isRental.equalsIgnoreCase(candidateAddressEntityInLoop.getIsRental())) {

					voCandidateAddress.setIsRental(candidateAddressEntityInLoop.getIsRental());
					voCandidateAddress.setOwnerName(candidateAddressEntityInLoop.getOwnerName());
					voCandidateAddress.setOwnerContact(candidateAddressEntityInLoop.getOwnerContact());
					voCandidateAddress.setOwnerAdhar(candidateAddressEntityInLoop.getOwnerAdhar());
				}

				voCandidateAddress.setSsnNumber(candidateAddressEntityInLoop.getSsnNumber());

				voCandidateAddresses.add(voCandidateAddress);
			}
		}
		return voCandidateAddresses;
	}

	/**
	 * Customer mapper to convert CandidateLetter entity to VOCandidateLetter model
	 * 
	 * @param CandidateLetter
	 * @return VOCandidateLetter
	 * @author shome.nitin
	 * 
	 */
	public static VOCandidateLetter convertToCandidateLetterModel(CandidateLetter entity) {
		VOCandidateLetter model = null;
		if (entity != null) {
			model = new VOCandidateLetter();
			model.setId(entity.getId());
			model.setStatus(entity.getStatus());
			model.setActionTakenOn(entity.getActionTakenOn());
			model.setLetterType(entity.getLetterType());
			// model.setLetterUrl(letterURL);
			model.setSentOn(entity.getSentOn());
			model.setFileName(entity.getFileName());
		}
		return model;
	}

	/**
	 * Customer mapper to convert MasterCandidateChecklistAction entity to
	 * VOMasterCandidateChecklistAction model
	 * 
	 * @param MasterCandidateChecklistAction
	 * @return VOMasterCandidateChecklistAction
	 * @author shome.nitin
	 * 
	 */
	public static VOMasterCandidateChecklistAction convertToMasterCandidateChecklistActionModel(
			MasterCandidateChecklistAction entity) {
		VOMasterCandidateChecklistAction model = null;
		if (!HRMSHelper.isNullOrEmpty(entity)) {
			model = new VOMasterCandidateChecklistAction();
			model.setCandidateChecklistActionDescription(entity.getCandidateChecklistActionDescription());
			model.setCandidateChecklistActionName(entity.getCandidateChecklistActionName());
			model.setCreatedBy(entity.getCreatedBy());
			model.setCreatedDate(entity.getCreatedDate());
			model.setId(entity.getId());
			model.setIsActive(entity.getIsActive());
			model.setRemark(entity.getRemark());
			model.setUpdatedBy(entity.getUpdatedBy());
			model.setUpdatedDate(entity.getUpdatedDate());
		}
		return model;
	}

	/**
	 * Customer mapper to convert convertToCandidateActivityLetterMappingModel
	 * entity to VOCandidateActivityLetterMapping model
	 * 
	 * @param CandidateActivityLetterMapping
	 * @return VOCandidateActivityLetterMapping
	 * @author shome.nitin
	 * 
	 */
	public static VOCandidateActivityLetterMapping convertToCandidateActivityLetterMappingModel(
			CandidateActivityLetterMapping entity) {

		VOCandidateActivityLetterMapping model = null;
		if (entity != null) {
			model = new VOCandidateActivityLetterMapping();
			model.setId(entity.getId());
			model.setCandidateActivity(convertToCandidateActivityModel(entity.getCandidateActivity()));
		}

		return model;
	}

	/**
	 * Customer mapper to convert CandidateActivity entity to VOCandidateActivity
	 * model
	 * 
	 * @param CandidateActivity
	 * @return VOCandidateActivity
	 * @author shome.nitin
	 * 
	 */
	public static VOCandidateActivity convertToCandidateActivityModel(CandidateActivity entity) {
		VOCandidateActivity model = null;
		if (entity != null) {
			model = new VOCandidateActivity();
			model.setId(entity.getId());
			model.setCandidate(null);
			model.setMasterCandidateActivity(null);
			model.setEmailStatus(entity.getEmailStatus());
			model.setHrStatus(entity.getHrStatus());
			model.setHrComment(entity.getHrComment());
			model.setActivityTriggredDate(entity.getActivityTriggredDate());
			model.setActivityResponseDate(entity.getActivityResponseDate());
		}
		return model;
	}

	/**
	 * Customer mapper to convert EmployeeGrantLeaveDetail entity to
	 * VOEmployeeGrantLeaveDetail model
	 * 
	 * @param EmployeeGrantLeaveDetail
	 * @return VOEmployeeGrantLeaveDetail
	 * @author shome.nitin
	 * 
	 */
	public static VOEmployeeGrantLeaveDetail convertToCandidateLeaveGrantModel(EmployeeGrantLeaveDetail entity) {
		VOEmployeeGrantLeaveDetail model = null;

		if (entity != null) {
			model = new VOEmployeeGrantLeaveDetail();
			model.setId(entity.getId());
			model.setEmployee(convertToEmployeeModel(entity.getEmployee()));
			model.setMasterLeaveType(convertToMasterLeaveTypeModel(entity.getMasterLeaveType()));
			model.setFromDate(HRMSDateUtil.format(entity.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			model.setToDate(HRMSDateUtil.format(entity.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			model.setFromSession(entity.getFromSession());
			model.setToSession(entity.getToSession());
			model.setContactDetails(entity.getContactDetails());
			model.setCc(entity.getCc());
			model.setLeaveStatus(entity.getLeaveStatus());
			model.setReasonForApply(entity.getReasonForApply());
			model.setAttachment(entity.getAttachment());
			model.setReasonForCancel(entity.getReasonForCancel());
			model.setDateOfApplied(
					HRMSDateUtil.format(entity.getDateOfApplied(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			model.setDateOfApproverAction(entity.getDateOfApproverAction());
			model.setAppliedBy(entity.getAppliedBy());
			model.setDateOfCancel(entity.getDateOfCancel());
			model.setReasonForWithdrawn(entity.getReasonForWithdrawn());
			model.setDateOfWithdrawn(entity.getDateOfWithdrawn());
			model.setApproverCommentOnWithdrawn(entity.getApproverCommentOnWithdrawn());
			model.setApproverActionDateWithdrawn(entity.getApproverActionDateWithdrawn());
			model.setNoOfDays(entity.getNoOfDays());
		}

		return model;
	}

	public static VOMasterLanguage convertToLanguageMasterVO(MasterLanguage entity) {
		VOMasterLanguage model = null;

		if (entity != null) {
			model = new VOMasterLanguage();
			model.setLanguageDescription(entity.getLanguageDescription());
			model.setId(entity.getId());
			model.setLanguageName(entity.getLanguageName());
		}

		return model;
	}

	/**
	 * 
	 * @param entity
	 * @return
	 * @author SSW
	 * 
	 *         For : convert set of LoginEntity of type entity to VO
	 */
	public static Set<VOLoginEntityType> convertToSetLoginEntityTypeModel(Set<LoginEntityType> entity) {

		Set<VOLoginEntityType> model = null;
		if (!HRMSHelper.isNullOrEmpty(entity)) {
			model = new HashSet<VOLoginEntityType>();
			for (LoginEntityType loginEntityType : entity) {
				if (!HRMSHelper.isNullOrEmpty(loginEntityType)) {
					VOLoginEntityType voLoginEntityType = new VOLoginEntityType();
					voLoginEntityType.setId(loginEntityType.getId());
					voLoginEntityType.setLoginEntityTypeName(loginEntityType.getLoginEntityTypeName());
					model.add(voLoginEntityType);
				}
			}
		}
		return model;
	}

	/**
	 * 
	 * @param entity
	 * @return
	 * @author shome.nitin
	 * 
	 *         For : converts workshift entity to workshift model
	 */
	public static VOMasterWorkshift convertToWorkshiftModel(MasterWorkshift entity) {
		VOMasterWorkshift model = null;
		if (entity != null) {
			model = new VOMasterWorkshift();
			model.setId(entity.getId());
			model.setWorkshiftName(entity.getWorkshiftName());
		}
		return model;
	}

	public static VOMasterModeofSeparation convertToMstModeofSeparationVO(
			MasterModeofSeparation modeofSeparationEntity) {
		if (modeofSeparationEntity != null) {
			VOMasterModeofSeparation voMasterModeofSeparation = new VOMasterModeofSeparation();
			voMasterModeofSeparation.setId(modeofSeparationEntity.getId());
			voMasterModeofSeparation.setCreatedBy(modeofSeparationEntity.getCreatedBy());
			voMasterModeofSeparation.setCreatedDate(modeofSeparationEntity.getCreatedDate());
			voMasterModeofSeparation.setModeOfSeparationCode(modeofSeparationEntity.getModeOfSeparationCode());
			voMasterModeofSeparation.setModeOfSeparationName(modeofSeparationEntity.getModeOfSeparationName());
			voMasterModeofSeparation.setRemark(modeofSeparationEntity.getRemark());
			voMasterModeofSeparation.setUpdatedBy(modeofSeparationEntity.getUpdatedBy());
			voMasterModeofSeparation.setUpdatedDate(modeofSeparationEntity.getUpdatedDate());

			return voMasterModeofSeparation;

		}
		return null;
	}

	public static VOMasterModeofSeparationReason convertToMstModeofSeparationReasonVO(
			MasterModeofSeparationReason modeofSeparationReasonEntity) {
		if (modeofSeparationReasonEntity != null) {
			VOMasterModeofSeparationReason voMasterModeofSeparationReason = new VOMasterModeofSeparationReason();
			voMasterModeofSeparationReason.setId(modeofSeparationReasonEntity.getId());
			voMasterModeofSeparationReason.setCreatedBy(modeofSeparationReasonEntity.getCreatedBy());
			voMasterModeofSeparationReason.setCreatedDate(modeofSeparationReasonEntity.getCreatedDate());

			voMasterModeofSeparationReason.setReasonName(modeofSeparationReasonEntity.getReasonName());
			voMasterModeofSeparationReason.setResignActionType(modeofSeparationReasonEntity.getResignActionType());
			voMasterModeofSeparationReason.setMasterModeofSeparation(HRMSEntityToModelMapper
					.convertToMstModeofSeparationVO(modeofSeparationReasonEntity.getMasterModeofSeparation()));

			return voMasterModeofSeparationReason;
		}
		return null;
	}

	public static VORejectResiganationReason convertToMstRejectResiganationReasonVO(
			MasterRejectResiganationReason masterrejectresiganationreason) {
		if (masterrejectresiganationreason != null) {
			VORejectResiganationReason voRejectResiganationReason = new VORejectResiganationReason();
			voRejectResiganationReason.setId(masterrejectresiganationreason.getId());
			voRejectResiganationReason.setCreatedBy(masterrejectresiganationreason.getCreatedBy());
			voRejectResiganationReason.setCreatedDate(masterrejectresiganationreason.getCreatedDate());

			voRejectResiganationReason.setReasonName(masterrejectresiganationreason.getReasonName());
			return voRejectResiganationReason;
		}
		return null;
	}

	public static VOMasterNoticePeriod convertToMstNoticePeriodVO(MasterOrg_NoticePeriod noticePeriodEntity) {

		if (noticePeriodEntity != null) {
			VOMasterNoticePeriod voMasterNoticePeriod = new VOMasterNoticePeriod();
			voMasterNoticePeriod.setId(noticePeriodEntity.getId());
			voMasterNoticePeriod.setNoticePeriod(noticePeriodEntity.getNoticePeriod());
			return voMasterNoticePeriod;
		}
		return null;
	}

	public static VOEmployeeSeparationDetails convertToEmpSeparationDetailsVO(
			EmployeeSeparationDetails employeeSeparationDetailsEntity) {
		if (!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity)) {
			VOEmployeeSeparationDetails voEmployeeSeparationDetails = new VOEmployeeSeparationDetails();
			voEmployeeSeparationDetails.setActualRelievingDate(HRMSDateUtil.format(
					employeeSeparationDetailsEntity.getActualRelievingDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));

			voEmployeeSeparationDetails.setShowactualRelievingDate(HRMSDateUtil.format(
					employeeSeparationDetailsEntity.getActualRelievingDate(), IHRMSConstants.POSTGRE_DATE_FORMAT));

			/*
			 * voEmployeeSeparationDetails.setActualRelievingDate(HRMSDateUtil.format(
			 * employeeSeparationDetailsEntity.getActualRelievingDate(),
			 * IHRMSConstants.POSTGRE_DATE_FORMAT));
			 */

			/*
			 * if(!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity.
			 * getActualRelievingDate()))
			 * voEmployeeSeparationDetails.setActualRelievingDate(HRMSDateUtil.parse(
			 * employeeSeparationDetailsEntity.getActualRelievingDate().toString(),
			 * IHRMSConstants.POSTGRE_DATE_FORMAT)); else
			 * voEmployeeSeparationDetails.setActualRelievingDate(
			 * employeeSeparationDetailsEntity.getActualRelievingDate());
			 */
			voEmployeeSeparationDetails.setCreatedBy(employeeSeparationDetailsEntity.getCreatedBy());
			voEmployeeSeparationDetails.setCreatedDate(employeeSeparationDetailsEntity.getCreatedDate());
			voEmployeeSeparationDetails
					.setEmployee(convertToEmployeeModel(employeeSeparationDetailsEntity.getEmployee()));
			voEmployeeSeparationDetails.setEmployeeComment(employeeSeparationDetailsEntity.getEmployeeComment());
			voEmployeeSeparationDetails.setEmpseparationReason(HRMSEntityToModelMapper
					.convertToMstModeofSeparationReasonVO(employeeSeparationDetailsEntity.getEmpSeparationReason()));
			voEmployeeSeparationDetails.setHrActionDate(HRMSDateUtil
					.format(employeeSeparationDetailsEntity.getHrActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			voEmployeeSeparationDetails.setHrApproverStatus(employeeSeparationDetailsEntity.getHrApproverStatus());
			voEmployeeSeparationDetails.setHRComment(employeeSeparationDetailsEntity.getHRComment());
			voEmployeeSeparationDetails.setHRReason(HRMSEntityToModelMapper
					.convertToMstModeofSeparationReasonVO(employeeSeparationDetailsEntity.getHRReason()));
			voEmployeeSeparationDetails.setId(employeeSeparationDetailsEntity.getId());
			voEmployeeSeparationDetails.setModeofSeparation(HRMSEntityToModelMapper
					.convertToMstModeofSeparationVO(employeeSeparationDetailsEntity.getModeofSeparation()));
			/*
			 * voEmployeeSeparationDetails.setNoticePeriod(HRMSEntityToModelMapper
			 * .convertToMstNoticePeriodVO((employeeSeparationDetailsEntity.getNoticePeriod(
			 * ))));
			 */
			voEmployeeSeparationDetails.setNoticePeriod(employeeSeparationDetailsEntity.getNoticeperiod());
			voEmployeeSeparationDetails.setOrgActionDate(HRMSDateUtil
					.format(employeeSeparationDetailsEntity.getOrgActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			voEmployeeSeparationDetails.setOrgApproverStatus(employeeSeparationDetailsEntity.getOrgApproverStatus());
			voEmployeeSeparationDetails.setOrgComment(employeeSeparationDetailsEntity.getOrgComment());
			voEmployeeSeparationDetails.setOrgReason(HRMSEntityToModelMapper
					.convertToMstModeofSeparationReasonVO(employeeSeparationDetailsEntity.getOrgReason()));
			voEmployeeSeparationDetails.setResignationDate(HRMSDateUtil.format(
					employeeSeparationDetailsEntity.getResignationDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));

			voEmployeeSeparationDetails.setShowresign_date_display(HRMSDateUtil
					.format(employeeSeparationDetailsEntity.getResignationDate(), IHRMSConstants.POSTGRE_DATE_FORMAT));

			/*
			 * voEmployeeSeparationDetails.setResignationDate(HRMSDateUtil.format(
			 * employeeSeparationDetailsEntity.getResignationDate(),
			 * IHRMSConstants.POSTGRE_DATE_FORMAT));
			 */

			voEmployeeSeparationDetails.setRoActionDate(HRMSDateUtil
					.format(employeeSeparationDetailsEntity.getRoActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			voEmployeeSeparationDetails.setRoApproverStatus(employeeSeparationDetailsEntity.getRoApproverStatus());
			voEmployeeSeparationDetails.setRoComment(employeeSeparationDetailsEntity.getRoComment());
			voEmployeeSeparationDetails.setRoReason(HRMSEntityToModelMapper
					.convertToMstModeofSeparationReasonVO(employeeSeparationDetailsEntity.getRoReason()));
			voEmployeeSeparationDetails.setStatus(employeeSeparationDetailsEntity.getStatus());
			voEmployeeSeparationDetails
					.setSystemEscalatedLevel(employeeSeparationDetailsEntity.getSystemEscalatedLevel());
			voEmployeeSeparationDetails.setEmployeeAction(employeeSeparationDetailsEntity.getEmployeeAction());

			if (voEmployeeSeparationDetails.getRoApproverStatus() == null)
				voEmployeeSeparationDetails.setApproval_status(IHRMSConstants.EMPLOYEE_STATUS_RO_PENDING);
			else if (voEmployeeSeparationDetails.getOrgApproverStatus() == null)
				voEmployeeSeparationDetails.setApproval_status(IHRMSConstants.EMPLOYEE_STATUS_ORG_PENDING);
			/*
			 * else if(voEmployeeSeparationDetails.getHrApproverStatus()==null)
			 * voEmployeeSeparationDetails.setApproval_status(IHRMSConstants.
			 * EMPLOYEE_STATUS_HR_PENDING);
			 */
			return voEmployeeSeparationDetails;
		}
		return null;
	}
	/*
	 * 
	 * This Method is used for Employee seperation status details Modification
	 * History.
	 */

	public static VOEmployeeSeparationDetails convertToEmpSeparationDetailsStatusHistoryVO(
			EmployeeSeparationDetailsWithHistory employeeSeparationDetailsEntityHistory) {
		if (!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntityHistory)) {
			VOEmployeeSeparationDetails voEmployeeSeparationDetails = new VOEmployeeSeparationDetails();
			voEmployeeSeparationDetails.setActualRelievingDate(
					HRMSDateUtil.format(employeeSeparationDetailsEntityHistory.getActualRelievingDate(),
							IHRMSConstants.FRONT_END_DATE_FORMAT));

			voEmployeeSeparationDetails.setShowactualRelievingDate(
					HRMSDateUtil.format(employeeSeparationDetailsEntityHistory.getActualRelievingDate(),
							IHRMSConstants.POSTGRE_DATE_FORMAT));

			voEmployeeSeparationDetails.setCreatedBy(employeeSeparationDetailsEntityHistory.getCreatedBy());
			voEmployeeSeparationDetails.setCreatedDate(employeeSeparationDetailsEntityHistory.getCreatedDate());
			voEmployeeSeparationDetails
					.setEmployee(convertToEmployeeModel(employeeSeparationDetailsEntityHistory.getEmployee()));
			voEmployeeSeparationDetails.setEmployeeComment(employeeSeparationDetailsEntityHistory.getEmployeeComment());
			voEmployeeSeparationDetails
					.setEmpseparationReason(HRMSEntityToModelMapper.convertToMstModeofSeparationReasonVO(
							employeeSeparationDetailsEntityHistory.getEmpSeparationReason()));
			voEmployeeSeparationDetails.setHrActionDate(HRMSDateUtil.format(
					employeeSeparationDetailsEntityHistory.getHrActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			voEmployeeSeparationDetails
					.setHrApproverStatus(employeeSeparationDetailsEntityHistory.getHrApproverStatus());
			voEmployeeSeparationDetails.setHRComment(employeeSeparationDetailsEntityHistory.getHRComment());
			voEmployeeSeparationDetails.setHRReason(HRMSEntityToModelMapper
					.convertToMstModeofSeparationReasonVO(employeeSeparationDetailsEntityHistory.getHRReason()));
			voEmployeeSeparationDetails.setId(employeeSeparationDetailsEntityHistory.getId());
			voEmployeeSeparationDetails.setModeofSeparation(HRMSEntityToModelMapper
					.convertToMstModeofSeparationVO(employeeSeparationDetailsEntityHistory.getModeofSeparation()));

			voEmployeeSeparationDetails.setNoticePeriod(employeeSeparationDetailsEntityHistory.getNoticeperiod());
			voEmployeeSeparationDetails.setOrgActionDate(HRMSDateUtil.format(
					employeeSeparationDetailsEntityHistory.getOrgActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			voEmployeeSeparationDetails
					.setOrgApproverStatus(employeeSeparationDetailsEntityHistory.getOrgApproverStatus());
			voEmployeeSeparationDetails.setOrgComment(employeeSeparationDetailsEntityHistory.getOrgComment());
			voEmployeeSeparationDetails.setOrgReason(HRMSEntityToModelMapper
					.convertToMstModeofSeparationReasonVO(employeeSeparationDetailsEntityHistory.getOrgReason()));
			voEmployeeSeparationDetails.setResignationDate(HRMSDateUtil.format(
					employeeSeparationDetailsEntityHistory.getResignationDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));

			voEmployeeSeparationDetails.setShowresign_date_display(HRMSDateUtil.format(
					employeeSeparationDetailsEntityHistory.getResignationDate(), IHRMSConstants.POSTGRE_DATE_FORMAT));

			voEmployeeSeparationDetails.setRoActionDate(HRMSDateUtil.format(
					employeeSeparationDetailsEntityHistory.getRoActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			voEmployeeSeparationDetails
					.setRoApproverStatus(employeeSeparationDetailsEntityHistory.getRoApproverStatus());
			voEmployeeSeparationDetails.setRoComment(employeeSeparationDetailsEntityHistory.getRoComment());
			voEmployeeSeparationDetails.setRoReason(HRMSEntityToModelMapper
					.convertToMstModeofSeparationReasonVO(employeeSeparationDetailsEntityHistory.getRoReason()));
			voEmployeeSeparationDetails.setStatus(employeeSeparationDetailsEntityHistory.getStatus());
			voEmployeeSeparationDetails
					.setSystemEscalatedLevel(employeeSeparationDetailsEntityHistory.getSystemEscalatedLevel());
			voEmployeeSeparationDetails.setEmployeeAction(employeeSeparationDetailsEntityHistory.getEmployeeAction());

			if (voEmployeeSeparationDetails.getRoApproverStatus() == null)
				voEmployeeSeparationDetails.setApproval_status(IHRMSConstants.EMPLOYEE_STATUS_RO_PENDING);
			else if (voEmployeeSeparationDetails.getOrgApproverStatus() == null)
				voEmployeeSeparationDetails.setApproval_status(IHRMSConstants.EMPLOYEE_STATUS_ORG_PENDING);

			return voEmployeeSeparationDetails;
		}
		return null;
	}

	/**
	 * 
	 * @param entity
	 * @return model
	 * @author shome.nitin
	 * 
	 *         For : converts MapCatalogue entity to VOMapCatalogue model
	 */

	public static VOMapCatalogue converToVOMapCatalogueModel(MapCatalogue entity) {
		VOMapCatalogue model = null;

		if (entity != null) {
			Set<VOMapCatalogueChecklistItem> checklistModelSet = new HashSet<VOMapCatalogueChecklistItem>();
			model = new VOMapCatalogue();
			Set<MapCatalogueChecklistItem> checklistEntitySet = entity.getCatalogueChecklistItems();
			if (checklistEntitySet != null && !checklistEntitySet.isEmpty()) {
				for (MapCatalogueChecklistItem checkListentity : checklistEntitySet) {
					VOMapCatalogueChecklistItem checkListModel = convertToCatalogueChecklistItemsModel(checkListentity);
					checklistModelSet.add(checkListModel);
				}
			}
			model.setCatalogueChecklistItems(checklistModelSet);
			model.setDescription(entity.getDescription());
			model.setId(entity.getId());
			model.setName(entity.getName());
			model.setIsActive(entity.getIsActive());
		}
		return model;
	}

	/**
	 * 
	 * @param entity
	 * @return model
	 * @author shome.nitin
	 * 
	 *         For : converts MapCatalogueChecklistItem entity to
	 *         VOMapCatalogueChecklistItem model
	 */
	public static VOMapCatalogueChecklistItem convertToCatalogueChecklistItemsModel(MapCatalogueChecklistItem entity) {

		VOMapCatalogueChecklistItem model = null;
		if (entity != null) {
			model = new VOMapCatalogueChecklistItem();
			model.setName(entity.getName());
			model.setId(entity.getId());
			model.setIsActive(entity.getIsActive());
		}
		return model;
	}

	/**
	 * 
	 * @param entity
	 * @return model
	 * @author shome.nitin
	 * 
	 *         For : converts MapEmployeeCatalogueChecklist entity to
	 *         VOMapEmployeeCatalogueChecklist model Description : Return the
	 *         employee and catalogue cheklist mapping
	 */
	public static VOMapEmployeeCatalogueChecklist convertToEmployeeChecklistMapping(
			MapEmployeeCatalogueChecklist entity) {
		VOMapEmployeeCatalogueChecklist model = null;
		if (entity != null) {
			model = new VOMapEmployeeCatalogueChecklist();
			model.setAmount(entity.getAmount());
			model.setCatalogueChecklist(convertToCatalogueChecklistItemsModel(entity.getCatalogueChecklist()));
			model.setEmployeeCatalogueMapping(convertToEmployeeCatalogue(entity.getEmployeeCatalogueMapping()));
			model.setComment(entity.getComment());
			model.setHaveCollected(entity.isHaveCollected());
			model.setId(entity.getId());
		}
		return model;
	}

	public static ChecklistVO convertToChecklistVOMapping(MapEmployeeCatalogueChecklist entity) {
		ChecklistVO model = null;
		if (entity != null) {
			model = new ChecklistVO();
			model.setAmount(entity.getAmount());
			model.setCatalogueChecklist(convertToCatalogueChecklistItemsModel(entity.getCatalogueChecklist()));
			model.setEmployeeCatalogueMapping(convertToEmployeeCatalogue(entity.getEmployeeCatalogueMapping()));
			model.setComment(entity.getComment());
			model.setHaveCollected(entity.isHaveCollected());
			model.setId(entity.getId());
			model.setChecklistItem(entity.getCatalogueChecklist().getName());
			model.setChecklistApprover(entity.getEmployeeCatalogueMapping().getCatalogue().getName());
			model.setCatalogueId(entity.getEmployeeCatalogueMapping().getCatalogue().getId());
		}
		return model;
	}

	/**
	 * 
	 * @param entity
	 * @return model
	 * @author shome.nitin
	 * 
	 *         For : converts MapEmployeeCatalogue entity to VOMapEmployeeCatalogue
	 *         model Description : Return the employee and catalogue mapping
	 */
	public static VOMapEmployeeCatalogue convertToEmployeeCatalogue(MapEmployeeCatalogue entity) {
		VOMapEmployeeCatalogue model = null;

		if (entity != null) {
			model = new VOMapEmployeeCatalogue();
			model.setId(entity.getId());
			// model.setResignedEmployee(convertToEmployeeBrief(entity.getResignedEmployee()));
			model.setEmployee(convertToEmployeeModelWithSeperationDetails(entity.getResignedEmployee()));
			model.setCatalogue(converToVOMapCatalogueModel(entity.getCatalogue()));
			model.setStatus(entity.getStatus());
			model.setCatalogueProof(entity.getCatalogueProof());
			model.setActedOn(entity.getActedOn());
		}
		return model;
	}

	/**
	 * 
	 * @param entity
	 * @return model
	 * @author shome.nitin
	 * 
	 *         For : converts EmployeeSeparationDetails entity to
	 *         VOEmployeeSeparationDetails model , Return the employee separation
	 *         details with minimum information of employee Created On 16-02-2018
	 * 
	 */
	public static VOEmployeeSeparationDetails convertToEmployeeSeperationGeneralDetails(
			EmployeeSeparationDetails entity) {
		VOEmployeeSeparationDetails model = null;
		if (entity != null) {

			model = new VOEmployeeSeparationDetails();
			model.setHrActionDate(HRMSDateUtil.format(entity.getHrActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			model.setHrApproverStatus(entity.getHrApproverStatus());
			model.setHRComment(entity.getHRComment());
			model.setId(entity.getId());
			model.setNoticePeriod(entity.getNoticeperiod());
			model.setOrgActionDate(
					HRMSDateUtil.format(entity.getOrgActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			model.setOrgApproverStatus(entity.getOrgApproverStatus());
			model.setOrgComment(entity.getOrgComment());
			model.setResignationDate(
					HRMSDateUtil.format(entity.getResignationDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			model.setRoActionDate(HRMSDateUtil.format(entity.getRoActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			model.setRoApproverStatus(entity.getRoApproverStatus());
			model.setRoComment(entity.getRoComment());

			model.setStatus(entity.getStatus());
			model.setSystemEscalatedLevel(entity.getSystemEscalatedLevel());
			model.setEmployeeAction(entity.getEmployeeAction());
		}
		return model;
	}

	/**
	 * 
	 * @param entity
	 * @return model
	 * @author shome.nitin
	 * 
	 *         For : converts Employee entity to VOEmployee model , Return the
	 *         employee with basic separation details with minimum information of
	 *         employee Created On 16-02-2018
	 * 
	 */
	public static VOEmployee convertToEmployeeModelWithSeperationDetails(Employee entity) {

		VOEmployee model = new VOEmployee();
		if (entity != null) {
			model = new VOEmployee();
			model.setId(entity.getId());
			// model.setEmployeeReportingManager(entity.getEmployeeReportingManager());
			model.setEmployeeReportingManager(convertToEmployeeReportingManager(entity.getEmployeeReportingManager()));

			VOCandidate candidate = new VOCandidate();
			candidate.setFirstName(entity.getCandidate().getFirstName());
			candidate.setLastName(entity.getCandidate().getLastName());
			candidate.setMiddleName(entity.getCandidate().getMiddleName());
			model.setCandidate(candidate);

			Set<EmployeeSeparationDetails> employeeSeperationDetails = entity.getEmployeeSeparationDetails();
			Set<VOEmployeeSeparationDetails> employeeSeperationDetailsModelSet = new HashSet<VOEmployeeSeparationDetails>();
			for (EmployeeSeparationDetails employeeSeperationEntity : employeeSeperationDetails) {
				if (employeeSeperationEntity.getIsActive().equalsIgnoreCase(IHRMSConstants.isActive)) {
					VOEmployeeSeparationDetails seperationModel = HRMSEntityToModelMapper
							.convertToEmpSeparationDetailsVO(employeeSeperationEntity);
					employeeSeperationDetailsModelSet.add(seperationModel);
				}
			}
			model.setEmployeeSeparationDetails(employeeSeperationDetailsModelSet);
		}
		return model;
	}

	/**
	 * This method will convert TravelRequest From Entity To Model
	 * 
	 * @param TravelRequest entity
	 * @return VOTravelRequest model
	 */
	public static VOTravelRequest convertToTravelRequestModel(TravelRequest entity) {

		VOTravelRequest model = null;
		List<VOTraveldeskComment> commentModelList = new ArrayList<>();
		if (entity != null) {
			model = new VOTravelRequest();
			model.setId(entity.getId());
			model.setWorkOrderNo(entity.getWorkOrderNo());
			model.setBookTicket(entity.isBookTicket());
			model.setSeqId(entity.getSeqId());
			model.setBookAccommodation(entity.isBookAccommodation());
			model.setBookCab(entity.isBookCab());
			model.setCheckedBy(entity.getCheckedBy());
			model.setCreditCardDetails(entity.getCreditCardDetails());
			model.setClientName(entity.getClientName());
			model.setRequestDate(entity.getRequestDate());
			model.setBdName(entity.getBdName());
			model.setCreatedDate(entity.getCreatedDate());
			model.setCreatedBy(entity.getCreatedBy());
			model.setEmployeeId(convertToEmployeeBrief(entity.getEmployeeId()));
			model.setAccommodationRequest(convertToAccommodationRequestModel(entity.getAccommodationRequest(), ""));
			model.setTicketRequest(convertToTicketRequestModel(entity.getTicketRequest(), ""));
			model.setTravelStatus(entity.getTravelStatus());
			model.setDepartment(convertToVoMasterDepartment(entity.getDepartment()));
			model.setOrganization(convertToOrganization(entity.getOrganization()));
			model.setCabRequest(convertToCabRequestModel(entity.getCabRequest(), ""));
			model.setDepartment(convertToVoMasterDepartment(entity.getDepartment()));

			if (!HRMSHelper.isNullOrEmpty(entity.getTraveldeskComment())) {

				VOTraveldeskComment commentModel = null;
				for (TraveldeskComment commentEntity : entity.getTraveldeskComment()) {
					commentModel = convertToTravelDeskCommentModel(commentEntity);
					commentModelList.add(commentModel);
				}
			}
			Collections.sort(commentModelList);
			model.setTravelDeskComment(commentModelList);
		}
		return model;
	}

	public static VOCabRequest convertToCabRequestModel(CabRequest entity, String dispaly) {
		VOCabRequest model = null;
		if (entity != null) {
			model = new VOCabRequest();
			model.setApprovalRequired(entity.isApprovalRequired());
			model.setApproverStatus(entity.getApproverStatus());

			model.setCabRequestStatus(entity.getCabRequestStatus());
			model.setId(entity.getId());
			model.setTotalCabCost(entity.getTotalCabCost());
			model.setTotalRefundAmount(entity.getTotalRefundAmount());
			// model.setApprover(null);
			model.setMasterApprover(HRMSResponseTranslator.translateToTraveldeskApproverVo(entity.getMasterApprover()));

			Set<CabRequestPassenger> cabPassengerSet = entity.getCabRequestPassengers();
			Set<VOCabRequestPassenger> cabPassengerModel = null;
			if (!HRMSHelper.isNullOrEmpty(cabPassengerSet)) {
				cabPassengerModel = new HashSet<VOCabRequestPassenger>();
				for (CabRequestPassenger passenger : cabPassengerSet) {
					VOCabRequestPassenger passengerModel = convertToCabRequestPassengerModel(passenger, dispaly);
					if (!HRMSHelper.isNullOrEmpty(passengerModel)) {
						cabPassengerModel.add(passengerModel);
					}
				}
			}
			model.setCabRequestPassengers(cabPassengerModel);
		}

		return model;
	}

	public static VOCabRecurringRequest convertToCabRecurringRequestModel(CabRecurringRequest entity) {
		VOCabRecurringRequest model = null;
		if (entity != null) {
			model = new VOCabRecurringRequest();
			model.setId(entity.getId());
			model.setPickupDate(HRMSDateUtil.format(entity.getPickupDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			model.setPickupTime(entity.getPickupTime());
			model.setPickupAt(entity.getPickupAt());
			model.setReturnDate(HRMSDateUtil.format(entity.getReturnDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			model.setReturnTime(entity.getReturnTime());
			model.setDropLocation(entity.getDropLocation());
			model.setChargeableToClient(entity.isChargeableToClient());
			model.setCabRequestRecurringStatus(entity.getCabRequestRecurringStatus());
			// model.setDriverId(VOMasterDriver driverId);
			// model.setVehicleId(VOMasterVehicle vehicleId);

			model.setTdSelfManaged(entity.isTdSelfManaged());
			model.setTdSelfManagedComment(entity.getTdSelfManagedComment());

			model.setDriverpickupTime(entity.getDriverPickupTime());
			model.setDriverreturnPickupTime(entity.getReturnDriverPickupTime());

		}
		return model;
	}

	public static VOAccommodationRequest convertToAccommodationRequestModel(AccommodationRequest entity,
			String toshow) {
		VOAccommodationRequest model = null;

		if (entity != null) {

			model = new VOAccommodationRequest();
			model.setId(entity.getId());
			model.setNoOfPeople(entity.getNoOfPeople());
			if (toshow.trim().length() > 0) {
				model.setFromDate(HRMSDateUtil.format(entity.getFromDate(), IHRMSConstants.POSTGRE_DATE_FORMAT));
				model.setToDate(HRMSDateUtil.format(entity.getToDate(), IHRMSConstants.POSTGRE_DATE_FORMAT));
			} else {
				model.setFromDate(HRMSDateUtil.format(entity.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
				model.setToDate(HRMSDateUtil.format(entity.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			}
			model.setPreferenceDetails(entity.getPreferenceDetails());
			model.setChargeableToClient(entity.isChargeableToClient());
			model.setApprovalRequired(entity.isApprovalRequired());
			model.setApproverStatus(entity.getApproverStatus());
			model.setAccommodationRequestStatus(entity.getAccommodationRequestStatus());
			model.setTotalAccommodationCost(entity.getTotalAccommodationCost());
			model.setApproverStatus(entity.getApproverStatus());
			// model.setOrganization(convertToOrganization(entity.getOrganization()));
			model.setTotalRefundAmount(entity.getTotalRefundAmount());
			// model.setApproverId(convertToEmployeeBrief(entity.getApproverId()));
			model.setMasterApprover(HRMSResponseTranslator.translateToTraveldeskApproverVo(entity.getMasterApprover()));
			Set<AccommodationGuest> guestEntitySet = entity.getAccommodationGuests();
			Set<VOAccommodationGuest> guestModelSet = new HashSet<VOAccommodationGuest>();
			if (guestEntitySet != null && !guestEntitySet.isEmpty()) {
				for (AccommodationGuest guestEntity : guestEntitySet) {
					VOAccommodationGuest guest = convertToAccommodationGuestModel(guestEntity);
					if (!HRMSHelper.isNullOrEmpty(guest)) {
						guestModelSet.add(guest);
					}

				}
			}

			model.setAccommodationGuests(guestModelSet);
		}

		return model;
	}

	/**
	 * To Create Accommodation Guest Model From Model To Entity
	 */
	public static VOAccommodationGuest convertToAccommodationGuestModel(AccommodationGuest entity) {

		VOAccommodationGuest model = null;
		if (entity != null && !HRMSHelper.isNullOrEmpty(entity.getIsActive())
				&& entity.getIsActive().equalsIgnoreCase(IHRMSConstants.isActive)) {

			model = new VOAccommodationGuest();
			model.setId(entity.getId());
			model.setPassengerName(entity.getPassengerName());
			model.setEmailId(entity.getEmailId());
			model.setContactNumber(entity.getContactNumber());
			model.setEmployee(convertToEmployeeBrief(entity.getEmployee()));
			model.setDateOfBirth(HRMSDateUtil.format(entity.getDateOfBirth(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		}

		return model;
	}

	public static VOTicketRequest convertToTicketRequestModel(TicketRequest entity, String display) {
		VOTicketRequest model = null;
		if (entity != null) {

			model = new VOTicketRequest();
			model.setId(entity.getId());
			// model.setTravelRequestId(TravelRequest travelRequestId);
			model.setMasterModeOfTravel(convertToTravellingModeModel(entity.getMasterModeOfTravel()));
			model.setNoOfTraveller(entity.getNoOfTraveller());
			model.setPreferenceDetails(entity.getPreferenceDetails());
			if (display.trim().length() > 0) {
				model.setPreferredDate(
						HRMSDateUtil.format(entity.getPreferredDate(), IHRMSConstants.POSTGRE_DATE_FORMAT));
				model.setReturnPreferredDate(
						HRMSDateUtil.format(entity.getReturnPreferredDate(), IHRMSConstants.POSTGRE_DATE_FORMAT));
			} else {
				model.setPreferredDate(
						HRMSDateUtil.format(entity.getPreferredDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
				model.setReturnPreferredDate(
						HRMSDateUtil.format(entity.getReturnPreferredDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			}
			model.setPreferredTime(entity.getPreferredTime());
			model.setFromLocation(entity.getFromLocation());
			model.setToLocation(entity.getToLocation());
			model.setRoundTrip(entity.isRoundTrip());
			model.setReturnPreferenceDetails(entity.getReturnPreferenceDetails());

			model.setReturnPreferredTime(entity.getReturnPreferredTime());
			model.setTotalTicketFare(entity.getTotalTicketFare());
			model.setChargeableToClient(entity.isChargeableToClient());
			model.setApprovalRequired(entity.isApprovalRequired());
			// model.setApproverId(convertToEmployeeBrief(entity.getApproverId()));
			model.setMasterApprover(HRMSResponseTranslator.translateToTraveldeskApproverVo(entity.getMasterApprover()));
			model.setApproverStatus(entity.getApproverStatus());
			model.setTicketRequestStatus(entity.getTicketRequestStatus());
			// model.setOrganization(convertToOrganization(entity.getOrganization()));
			model.setTotalRefundAmount(entity.getTotalRefundAmount());

			Set<TicketRequestPassenger> ticketRequestPassengerSet = entity.getTicketRequestPassengers();
			Set<VOTicketRequestPassenger> ticketRequestPassengerModelSet = new HashSet<VOTicketRequestPassenger>();
			if (ticketRequestPassengerSet != null && !ticketRequestPassengerSet.isEmpty()) {
				for (TicketRequestPassenger passengerEntity : ticketRequestPassengerSet) {
					VOTicketRequestPassenger ticketPassengerModel = convertToTicketRequestPassengerModel(
							passengerEntity);
					if (!HRMSHelper.isNullOrEmpty(ticketPassengerModel)) {
						ticketRequestPassengerModelSet.add(ticketPassengerModel);
					}

				}
			}

			model.setTicketRequestPassengers(ticketRequestPassengerModelSet);
		}

		return model;
	}

	/**
	 * To Create Ticket Passenger Model From Model To Entity
	 */
	public static VOTicketRequestPassenger convertToTicketRequestPassengerModel(TicketRequestPassenger entity) {

		VOTicketRequestPassenger model = null;
		if (entity != null && !HRMSHelper.isNullOrEmpty(entity.getIsActive())
				&& entity.getIsActive().equalsIgnoreCase(IHRMSConstants.isActive)) {
			model = new VOTicketRequestPassenger();
			model.setId(entity.getId());
			model.setEmployee(convertToEmployeeBrief(entity.getEmployee()));
			model.setPassengerName(entity.getPassengerName());
			model.setEmailId(entity.getEmailId());
			model.setContactNumber(entity.getContactNumber());
			model.setDateOfBirth(HRMSDateUtil.format(entity.getDateOfBirth(), IHRMSConstants.FRONT_END_DATE_FORMAT));

		}

		return model;
	}

	public static VOMasterModeOfTravel convertToTravellingModeModel(MasterModeOfTravel entity) {
		VOMasterModeOfTravel model = null;
		if (entity != null) {
			model = new VOMasterModeOfTravel();
			model.setId(entity.getId());
			model.setModeOfTravel(entity.getModeOfTravel());
			model.setModeOfTravelDescription(entity.getModeOfTravelDescription());
		}
		return model;
	}

	/**
	 * To Create Cab Request Model From Model To Entity
	 */
	public static VOCabRequestPassenger convertToCabRequestPassengerModel(CabRequestPassenger entity, String dispaly) {

		VOCabRequestPassenger model = null;
		if (entity != null && !HRMSHelper.isNullOrEmpty(entity.getIsActive())
				&& entity.getIsActive().equalsIgnoreCase(IHRMSConstants.isActive)) {
			model = new VOCabRequestPassenger();

			model.setPickupTime(entity.getPickupTime());
			model.setPickupAt(entity.getPickupAt());
			if (dispaly.trim().length() > 0) {
				model.setReturnDate(HRMSDateUtil.format(entity.getReturnDate(), IHRMSConstants.POSTGRE_DATE_FORMAT));
				model.setDateOfBirth(HRMSDateUtil.format(entity.getDateOfBirth(), IHRMSConstants.POSTGRE_DATE_FORMAT));
				model.setPickupDate(HRMSDateUtil.format(entity.getPickupDate(), IHRMSConstants.POSTGRE_DATE_FORMAT));
			} else {
				model.setReturnDate(HRMSDateUtil.format(entity.getReturnDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
				model.setDateOfBirth(
						HRMSDateUtil.format(entity.getDateOfBirth(), IHRMSConstants.FRONT_END_DATE_FORMAT));
				model.setPickupDate(HRMSDateUtil.format(entity.getPickupDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			}
			model.setReturnTime(entity.getReturnTime());
			model.setDropLocation(entity.getDropLocation());
			model.setDropOnly(entity.isDropOnly());
			model.setRecurring(entity.isRecurring());
			model.setSelfManaged(entity.isSelfManaged());
			model.setChargeableToClient(entity.isChargeableToClient());
			model.setCabRequestPasssengerStatus(model.getCabRequestPasssengerStatus());
			model.setId(entity.getId());
			model.setPassengerName(entity.getPassengerName());
			model.setEmailId(entity.getEmailId());
			model.setContactNumber(entity.getContactNumber());

			model.setEmployee(convertToEmployeeBrief(entity.getEmployee()));
			model.setTdSelfManaged(entity.isTdSelfManaged());
			model.setTdSelfManagedComment(entity.getTdSelfManagedComment());

			model.setDriverpickupTime(entity.getDriverPickupTime());
			model.setDriverreturnPickupTime(entity.getReturnDriverPickupTime());

			Set<CabRecurringRequest> cabRecurringEntitySet = entity.getCabRecurringRequests();
			List<VOCabRecurringRequest> cabRecurringModelSet = new ArrayList<VOCabRecurringRequest>();
			if (!HRMSHelper.isNullOrEmpty(cabRecurringEntitySet)) {
				for (CabRecurringRequest cabRecurringEntity : cabRecurringEntitySet) {
					VOCabRecurringRequest cabRecurringModel = convertToCabRecurringRequestModel(cabRecurringEntity);
					cabRecurringModelSet.add(cabRecurringModel);
				}
				model.setCabRecurringRequests(cabRecurringModelSet);
			}
		}
		return model;
	}

	public static VOOrganization convertToOrganization(Organization entity) {
		VOOrganization model = null;
		if (entity != null) {
			model = new VOOrganization();
			model.setId(entity.getId());
			model.setOrganizationName(entity.getOrganizationName());
			model.setOrganizationShortName(entity.getOrganizationShortName());
		}
		return model;
	}

	public static VOTraveldeskComment convertToTravelDeskCommentModel(TraveldeskComment entity) {
		VOTraveldeskComment model = null;
		if (!HRMSHelper.isNullOrEmpty(entity)) {
			model = new VOTraveldeskComment();
			model.setAction(entity.getAction());
			model.setChildId(entity.getChildId());
			model.setChildType(entity.getChildType());
			model.setComment(entity.getComment());
			model.setCommentator(entity.getCommentator());
			model.setEmployee(convertToEmployeeModel(entity.getEmployee()));
			model.setId(entity.getId());
			model.setCreatedDate(
					HRMSDateUtil.format(entity.getCreatedDate(), IHRMSConstants.FRONT_END_DATE_FORMAT_DDMMYY_HHMMSS));
		}
		return model;
	}

	public static VOTravelDeskDocument convertToTraveldeskDocumentModel(TraveldeskDocument entity) {
		VOTravelDeskDocument model = null;
		if (entity != null) {
			model = new VOTravelDeskDocument();
			model.setChildId(entity.getChildId());
			model.setChildType(entity.getChildType());
			model.setDocumentName(entity.getDocumentName());
			model.setId(entity.getId());
			model.setIsActive(entity.getIsActive());

		}
		return model;
	}

	/**
	 * This method will convert TravelRequest From Entity To Model for approver
	 * screen
	 * 
	 * @param TravelRequest entity
	 * @return VOTravelRequest model
	 */
	public static VOTravelRequest convertToTravelRequestModelForApprover(TravelRequest entity, String childType) {

		VOTravelRequest model = null;
		List<VOTraveldeskComment> commentModelList = new ArrayList<>();
		if (!HRMSHelper.isNullOrEmpty(entity) && !HRMSHelper.isNullOrEmpty(childType)) {
			model = new VOTravelRequest();
			model.setId(entity.getId());
			model.setWorkOrderNo(entity.getWorkOrderNo());
			// model.setBookTicket(entity.isBookTicket());
			// model.setBookAccommodation(entity.isBookAccommodation());
			// model.setBookCab(entity.isBookCab());
			model.setCheckedBy(entity.getCheckedBy());
			model.setCreditCardDetails(entity.getCreditCardDetails());
			model.setClientName(entity.getClientName());
			model.setRequestDate(entity.getRequestDate());
			model.setBdName(entity.getBdName());
			model.setCreatedDate(entity.getCreatedDate());

			model.setCreatedBy(entity.getCreatedBy());
			model.setEmployeeId(convertToEmployeeBrief(entity.getEmployeeId()));
			if (childType.equalsIgnoreCase(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION)) {
				model.setAccommodationRequest(convertToAccommodationRequestModel(entity.getAccommodationRequest(),
						IHRMSConstants.TO_DISPLAY));
			} else {
				model.setAccommodationRequest(null);
			}

			if (childType.equalsIgnoreCase(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET)) {
				model.setTicketRequest(
						convertToTicketRequestModel(entity.getTicketRequest(), IHRMSConstants.TO_DISPLAY));
			} else {
				model.setTicketRequest(null);
			}

			if (childType.equalsIgnoreCase(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB)) {
				model.setCabRequest(convertToCabRequestModel(entity.getCabRequest(), IHRMSConstants.TO_DISPLAY));
			} else {
				model.setCabRequest(null);
			}

			if (childType.equalsIgnoreCase(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION)) {
				model.setBookAccommodation(entity.isBookAccommodation());
				model.setBookCab(false);
				model.setBookTicket(false);
				model.setChildRequestType(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION);
			} else if (childType.equalsIgnoreCase(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET)) {
				model.setBookTicket(entity.isBookTicket());
				model.setBookCab(false);
				model.setBookAccommodation(false);
				model.setChildRequestType(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_TICKET);
			} else if (childType.equalsIgnoreCase(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB)) {
				model.setBookCab(entity.isBookCab());
				model.setBookAccommodation(false);
				model.setBookTicket(false);
				model.setChildRequestType(IHRMSConstants.TRAVEL_REQUEST_CHILD_TYPE_CAB);
			}

			model.setTravelStatus(entity.getTravelStatus());
			model.setDepartment(convertToVoMasterDepartment(entity.getDepartment()));
			model.setOrganization(convertToOrganization(entity.getOrganization()));
			model.setDepartment(convertToVoMasterDepartment(entity.getDepartment()));

			if (!HRMSHelper.isNullOrEmpty(entity.getTraveldeskComment())) {

				VOTraveldeskComment commentModel = null;
				for (TraveldeskComment commentEntity : entity.getTraveldeskComment()) {
					commentModel = convertToTravelDeskCommentModel(commentEntity);
					commentModelList.add(commentModel);
				}
			}

			model.setTravelDeskComment(commentModelList);
			model.setSeqId(entity.getSeqId());
		}
		return model;
	}

	public static VOReportTraveldeskDetail convertToReportTraveldeskDetailModel(ReportTraveldeskDetail entity) {
		VOReportTraveldeskDetail model = null;
		if (entity != null) {
			model = new VOReportTraveldeskDetail();
			model.setApprovalRequired(entity.getApprovalRequired());
			model.setApproverName(entity.getApproverName());
			model.setChargeableToClient(entity.getChargeableToClient());
			model.setCreatedDate(HRMSDateUtil.format(entity.getCreatedDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			model.setFromDate(HRMSDateUtil.format(entity.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			model.setFromLocation(entity.getFromLocation());
			model.setPassengerName(entity.getPassengerName());
			model.setRequestedBy(entity.getRequestedBy());
			model.setRoundTrip(entity.getRoundTrip());
			model.setSubRequestId(entity.getSubRequestId());
			model.setToDate(HRMSDateUtil.format(entity.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			model.setToLocation(entity.getToLocation());
			model.setTravellingMode(entity.getTravellingMode());
			model.setTravelRequestId(entity.getTravelRequestId());
			model.setWon(entity.getWon());
			model.setRequestType(entity.getRequestType());

		}
		return model;
	}

	public static VOEmployeeExtension convertToEmployeeExtensionModel(EmployeeExtension entity) {
		VOEmployeeExtension model = null;
		if (entity != null) {
			model = new VOEmployeeExtension();
			model.setId(entity.getId());
			// model.setEmployee(HRMSEntityToModelMapper.convertToEmployeeBrief(entity.getEmployee()));
			model.setExtensionNumber(entity.getExtensionNumber());
			model.setMasterExtensionType(convertToMasterEmployeeExtensionTypeModel(entity.getMasterExtensionType()));
			model.setEmployeeCheck(entity.isEmployeeCheck());
		}
		return model;
	}

	public static VOMasterExtensionType convertToMasterEmployeeExtensionTypeModel(MasterExtensionType entity) {
		VOMasterExtensionType model = null;
		if (entity != null) {
			model = new VOMasterExtensionType();
			model.setId(entity.getId());
			model.setExtensionType(entity.getExtensionType());
		}
		return model;
	}

	/**
	 * @param entity
	 * @return
	 * @author SSW
	 */
	public static VOEmployeeACN convertToModelEmployeeACN(EmployeeACN entity) {
		VOEmployeeACN model = null;
		if (!HRMSHelper.isNullOrEmpty(entity)) {
			model = new VOEmployeeACN();
			model.setEmpACN(entity.getEmpACN());
			model.setFlexible(entity.getIsFlexible());
			model.setManagement(entity.getIsManagement());
			model.setId(entity.getId());
			model.setEmployee(null);
		}
		return model;
	}

	/**
	 * @param entity
	 * @return
	 * @author SSW
	 */
	public static VOMasterAssetType convertToModelMasterAssetType(MasterAssetType entity) {
		VOMasterAssetType model = null;
		if (!HRMSHelper.isNullOrEmpty(entity)) {
			model = new VOMasterAssetType();
			model.setId(entity.getId());
			model.setAssetTypeName(entity.getAssetTypeName());
			model.setAssetTypeCode(entity.getAssetTypeCode());
			model.setOrganization(convertToOrganization(entity.getOrganization()));
			model.setDivision(convertToVoMasterDivision(entity.getDivision()));
		}
		return model;
	}

	/**
	 * @param entity
	 * @return
	 * @author SSW
	 */
	public static VOCompanyAsset convertToModelCompanyAsset(CompanyAsset entity) {
		VOCompanyAsset model = null;
		if (!HRMSHelper.isNullOrEmpty(entity)) {
			model = new VOCompanyAsset();
			model.setId(entity.getId());
			model.setMasterAssetType(convertToModelMasterAssetType(entity.getMasterAssetType()));
			model.setEmployee(convertToEmployeeBrief(entity.getEmployee()));
			model.setManufacturer(entity.getManufacturer());
			model.setModel(entity.getModel());
			model.setSerialNumber(entity.getSerialNumber());
			model.setQuantity(entity.getQuantity());
			model.setStatus(entity.getStatus());
			model.setComment(entity.getComment());
			model.setDateOfIssue(HRMSDateUtil.format(entity.getDateOfIssue(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		}
		return model;
	}

	/**
	 * @param entity
	 * @return
	 * @author SSW
	 */
	public static VOMasterCandidateChecklist convertToModelMasterCandidateChecklist(MasterCandidateChecklist entity) {
		VOMasterCandidateChecklist model = null;
		if (!HRMSHelper.isNullOrEmpty(entity)) {
			model = new VOMasterCandidateChecklist();
			model.setId(entity.getId());
			model.setChecklistTemplate(entity.getChecklistTemplate());
			model.setChecklistItem(entity.getChecklistItem());
			model.setOrganization(convertToOrganization(entity.getOrganization()));
		}
		return model;
	}

	public static VOTravelRequestDispaly convertoToVOTravelRequest(TravelRequest travelRequest) {
		VOTravelRequestDispaly voTravelRequestModel = null;
		if (!HRMSHelper.isNullOrEmpty(travelRequest)) {
			voTravelRequestModel = new VOTravelRequestDispaly();
			voTravelRequestModel.setId(travelRequest.getId());
			voTravelRequestModel.setSeqId(travelRequest.getSeqId());
			voTravelRequestModel.setTravelStatus(travelRequest.getTravelStatus());
			voTravelRequestModel.setWorkOrderNo(travelRequest.getWorkOrderNo());
			voTravelRequestModel.setRequestedDate(
					HRMSDateUtil.format(travelRequest.getCreatedDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		}
		return voTravelRequestModel;

	}

	public static VOEmployeeSeparationDetails convertToEmpSeparationDetailsModel(
			EmployeeSeparationDetails employeeSeparationDetailsEntity) {
		if (!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity)) {
			VOEmployeeSeparationDetails voEmployeeSeparationDetails = new VOEmployeeSeparationDetails();
			// voEmployeeSeparationDetails.setActualRelievingDate(HRMSDateUtil.format(
			// employeeSeparationDetailsEntity.getActualRelievingDate(),
			// IHRMSConstants.FRONT_END_DATE_FORMAT));
			voEmployeeSeparationDetails.setActualRelievingDate(HRMSDateUtil.format(
					employeeSeparationDetailsEntity.getActualRelievingDate(), IHRMSConstants.POSTGRE_DATE_FORMAT));
			/*
			 * if(!HRMSHelper.isNullOrEmpty(employeeSeparationDetailsEntity.
			 * getActualRelievingDate()))
			 * voEmployeeSeparationDetails.setActualRelievingDate(HRMSDateUtil.parse(
			 * employeeSeparationDetailsEntity.getActualRelievingDate().toString(),
			 * IHRMSConstants.POSTGRE_DATE_FORMAT)); else
			 * voEmployeeSeparationDetails.setActualRelievingDate(
			 * employeeSeparationDetailsEntity.getActualRelievingDate());
			 */

			voEmployeeSeparationDetails.setCreatedBy(employeeSeparationDetailsEntity.getCreatedBy());
			voEmployeeSeparationDetails.setCreatedDate(employeeSeparationDetailsEntity.getCreatedDate());
			voEmployeeSeparationDetails
					.setEmployee(convertToEmployeeModel(employeeSeparationDetailsEntity.getEmployee()));
			voEmployeeSeparationDetails.setEmployeeComment(employeeSeparationDetailsEntity.getEmployeeComment());
			voEmployeeSeparationDetails.setEmpseparationReason(HRMSEntityToModelMapper
					.convertToMstModeofSeparationReasonVO(employeeSeparationDetailsEntity.getEmpSeparationReason()));
			voEmployeeSeparationDetails.setHrActionDate(HRMSDateUtil
					.format(employeeSeparationDetailsEntity.getHrActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			voEmployeeSeparationDetails.setHrApproverStatus(employeeSeparationDetailsEntity.getHrApproverStatus());
			voEmployeeSeparationDetails.setHRComment(employeeSeparationDetailsEntity.getHRComment());
			voEmployeeSeparationDetails.setHRReason(HRMSEntityToModelMapper
					.convertToMstModeofSeparationReasonVO(employeeSeparationDetailsEntity.getHRReason()));
			voEmployeeSeparationDetails.setId(employeeSeparationDetailsEntity.getId());
			voEmployeeSeparationDetails.setModeofSeparation(HRMSEntityToModelMapper
					.convertToMstModeofSeparationVO(employeeSeparationDetailsEntity.getModeofSeparation()));

			voEmployeeSeparationDetails.getEmployee().getCandidate()
					.setCandidateProfessionalDetail(new VOCandidateProfessionalDetail());
			voEmployeeSeparationDetails.getEmployee().getCandidate().getCandidateProfessionalDetail()
					.setDepartment(convertToVoMasterDepartment(employeeSeparationDetailsEntity.getEmployee()
							.getCandidate().getCandidateProfessionalDetail().getDepartment()));

			// voEmployeeSeparationDetails.getEmployee().setEmployeeDepartment(convertToEmployeeDepartment(employeeSeparationDetailsEntity.getEmployee().getEmployeeDepartment()));
			/*
			 * voEmployeeSeparationDetails.setNoticePeriod(HRMSEntityToModelMapper
			 * .convertToMstNoticePeriodVO((employeeSeparationDetailsEntity.getNoticePeriod(
			 * ))));
			 */
			voEmployeeSeparationDetails.setNoticePeriod(employeeSeparationDetailsEntity.getNoticeperiod());
			voEmployeeSeparationDetails.setOrgActionDate(HRMSDateUtil
					.format(employeeSeparationDetailsEntity.getOrgActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			voEmployeeSeparationDetails.setOrgApproverStatus(employeeSeparationDetailsEntity.getOrgApproverStatus());
			voEmployeeSeparationDetails.setOrgComment(employeeSeparationDetailsEntity.getOrgComment());
			voEmployeeSeparationDetails.setOrgReason(HRMSEntityToModelMapper
					.convertToMstModeofSeparationReasonVO(employeeSeparationDetailsEntity.getOrgReason()));
			// voEmployeeSeparationDetails.setResignationDate(HRMSDateUtil.format(
			// employeeSeparationDetailsEntity.getResignationDate(),
			// IHRMSConstants.FRONT_END_DATE_FORMAT));

			voEmployeeSeparationDetails.setResignationDate(HRMSDateUtil
					.format(employeeSeparationDetailsEntity.getResignationDate(), IHRMSConstants.POSTGRE_DATE_FORMAT));

			voEmployeeSeparationDetails.setRoActionDate(HRMSDateUtil
					.format(employeeSeparationDetailsEntity.getRoActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			voEmployeeSeparationDetails.setRoApproverStatus(employeeSeparationDetailsEntity.getRoApproverStatus());
			voEmployeeSeparationDetails.setRoComment(employeeSeparationDetailsEntity.getRoComment());
			voEmployeeSeparationDetails.setRoReason(HRMSEntityToModelMapper
					.convertToMstModeofSeparationReasonVO(employeeSeparationDetailsEntity.getRoReason()));
			voEmployeeSeparationDetails.setStatus(employeeSeparationDetailsEntity.getStatus());
			voEmployeeSeparationDetails
					.setSystemEscalatedLevel(employeeSeparationDetailsEntity.getSystemEscalatedLevel());
			voEmployeeSeparationDetails.setEmployeeAction(employeeSeparationDetailsEntity.getEmployeeAction());

			if (voEmployeeSeparationDetails.getRoApproverStatus() == null)
				voEmployeeSeparationDetails.setApproval_status(IHRMSConstants.EMPLOYEE_STATUS_RO_PENDING);
			else if (voEmployeeSeparationDetails.getOrgApproverStatus() == null)
				voEmployeeSeparationDetails.setApproval_status(IHRMSConstants.EMPLOYEE_STATUS_ORG_PENDING);
			/*
			 * else if(voEmployeeSeparationDetails.getHrApproverStatus()==null)
			 * voEmployeeSeparationDetails.setApproval_status(IHRMSConstants.
			 * EMPLOYEE_STATUS_HR_PENDING);
			 */
			return voEmployeeSeparationDetails;
		}
		return null;
	}

	public static BankDetailsVO convertToBankDetailsToModel(BankDetails bank) {

		BankDetailsVO bankVO = null;
		if (bank != null) {
			bankVO = new BankDetailsVO();
			bankVO.setBankId(bank.getId());
			bankVO.setAccountNumber(bank.getAccountNumber());
			bankVO.setBankName(bank.getBankName());
			bankVO.setBranchLocation(bank.getBranchLocation());
			bankVO.setFullName(bank.getNameAsPerBank());
			bankVO.setIfscCode(bank.getIfscCode());
			bankVO.setMobileNumber(bank.getPhoneNumber());

		}
		return bankVO;
	}

}
