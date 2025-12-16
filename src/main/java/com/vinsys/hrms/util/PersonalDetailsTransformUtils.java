package com.vinsys.hrms.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vinsys.hrms.dao.IHRMSCandidateChecklistDAO;
import com.vinsys.hrms.employee.vo.AddressDetailsVO;
import com.vinsys.hrms.employee.vo.AddressVO;
import com.vinsys.hrms.employee.vo.CertificationDetailsVO;
import com.vinsys.hrms.employee.vo.CertificationVO;
import com.vinsys.hrms.employee.vo.EducationVO;
import com.vinsys.hrms.employee.vo.EducationalDetailsVO;
import com.vinsys.hrms.employee.vo.EmergencyContactVO;
import com.vinsys.hrms.employee.vo.FamilyDetailsVO;
import com.vinsys.hrms.employee.vo.FamilyVO;
import com.vinsys.hrms.employee.vo.HealthDetailsVO;
import com.vinsys.hrms.employee.vo.IdentificationDetailsVO;
import com.vinsys.hrms.employee.vo.PassportDetailsVO;
import com.vinsys.hrms.employee.vo.ProfileVO;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateAddress;
import com.vinsys.hrms.entity.CandidateCertification;
import com.vinsys.hrms.entity.CandidateChecklist;
import com.vinsys.hrms.entity.CandidateEmergencyContact;
import com.vinsys.hrms.entity.CandidateFamilyDetail;
import com.vinsys.hrms.entity.CandidateHealthReport;
import com.vinsys.hrms.entity.CandidatePassportDetail;
import com.vinsys.hrms.entity.CandidateQualification;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.exception.HRMSException;

/**
 * 
 * @author Onkar A.
 *
 */

public class PersonalDetailsTransformUtils {

	@Autowired
	IHRMSCandidateChecklistDAO checklistDAO;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public static ProfileVO convertToProfileDetailsVO(Employee employee) {
		ProfileVO profileVO = new ProfileVO();

		profileVO.setFirstName(!HRMSHelper.isNullOrEmpty(employee.getCandidate())
				? !HRMSHelper.isNullOrEmpty(employee.getCandidate().getFirstName())
						? employee.getCandidate().getFirstName()
						: null
				: null);
		profileVO.setMiddleName(!HRMSHelper.isNullOrEmpty(employee.getCandidate())
				? !HRMSHelper.isNullOrEmpty(employee.getCandidate().getMiddleName())
						? employee.getCandidate().getMiddleName()
						: null
				: null);
		profileVO.setLastName(!HRMSHelper.isNullOrEmpty(employee.getCandidate())
				? !HRMSHelper.isNullOrEmpty(employee.getCandidate().getLastName())
						? employee.getCandidate().getLastName()
						: null
				: null);
		profileVO.setSalutation(!HRMSHelper.isNullOrEmpty(employee.getCandidate())
				? !HRMSHelper.isNullOrEmpty(employee.getCandidate().getTitle()) ? employee.getCandidate().getTitle()
						: null
				: null);

		profileVO.setDob(!HRMSHelper.isNullOrEmpty(employee.getCandidate())
				? !HRMSHelper.isNullOrEmpty(employee.getCandidate().getDateOfBirth())
						? employee.getCandidate().getDateOfBirth()
						: null
				: null);
		profileVO.setEmailId(!HRMSHelper.isNullOrEmpty(employee.getCandidate())
				? !HRMSHelper.isNullOrEmpty(employee.getCandidate().getEmailId()) ? employee.getCandidate().getEmailId()
						: null
				: null);

		profileVO.setGender(!HRMSHelper.isNullOrEmpty(employee.getCandidate())
				? !HRMSHelper.isNullOrEmpty(employee.getCandidate().getGender()) ? employee.getCandidate().getGender()
						: null
				: null);

		profileVO
				.setMaritalStatus(!HRMSHelper.isNullOrEmpty(employee.getCandidate())
						? !HRMSHelper.isNullOrEmpty(employee.getCandidate().getCandidatePersonalDetail())
								? employee.getCandidate().getCandidatePersonalDetail().getMaritalStatus()
								: null
						: null);

		profileVO.setMobileNumber(!HRMSHelper.isNullOrEmpty(employee.getCandidate())
				? !HRMSHelper.isNullOrEmpty(employee.getCandidate().getMobileNumber())
						? employee.getCandidate().getMobileNumber()
						: 0
				: 0);

		List<IdentificationDetailsVO> identificationDetails = new ArrayList<IdentificationDetailsVO>();
		if (!HRMSHelper.isNullOrEmpty(employee.getCandidate().getCandidateProfessionalDetail())) {
			profileVO.setDesignation(
					employee.getCandidate().getCandidateProfessionalDetail().getDesignation().getDesignationName());

			IdentificationDetailsVO identificationDetail = new IdentificationDetailsVO();

			// CandidateChecklist checklistDetails=checklistDAO.
//			
//			CandidateChecklist checklist = checklistDAO.getCandidateWithChecklistDetailsByItem(documentName,
//					candId);
			for (CandidateChecklist checklist : employee.getCandidate().getCandidateProfessionalDetail()
					.getCandidateChecklists()) {
				identificationDetail = new IdentificationDetailsVO();
				identificationDetail.setDocumentName(checklist.getAttachment());
				identificationDetail.setDocumentType(checklist.getChecklistItem());
				identificationDetails.add(identificationDetail);
			}
		}
		profileVO.setDocuments(identificationDetails);

		return profileVO;

	}

	public static ProfileVO convertToProfileVO(Employee employee, List<CandidateChecklist> checklistDetails) {
		ProfileVO profileVO = new ProfileVO();

		profileVO.setFirstName(!HRMSHelper.isNullOrEmpty(employee.getCandidate())
				? !HRMSHelper.isNullOrEmpty(employee.getCandidate().getFirstName())
						? employee.getCandidate().getFirstName()
						: null
				: null);
		profileVO.setMiddleName(!HRMSHelper.isNullOrEmpty(employee.getCandidate())
				? !HRMSHelper.isNullOrEmpty(employee.getCandidate().getMiddleName())
						? employee.getCandidate().getMiddleName()
						: null
				: null);
		profileVO.setLastName(!HRMSHelper.isNullOrEmpty(employee.getCandidate())
				? !HRMSHelper.isNullOrEmpty(employee.getCandidate().getLastName())
						? employee.getCandidate().getLastName()
						: null
				: null);
		profileVO.setSalutation(!HRMSHelper.isNullOrEmpty(employee.getCandidate())
				? !HRMSHelper.isNullOrEmpty(employee.getCandidate().getTitle()) ? employee.getCandidate().getTitle()
						: null
				: null);

		profileVO.setDob(!HRMSHelper.isNullOrEmpty(employee.getCandidate())
				? !HRMSHelper.isNullOrEmpty(employee.getCandidate().getDateOfBirth())
						? employee.getCandidate().getDateOfBirth()
						: null
				: null);
		profileVO.setEmailId(!HRMSHelper.isNullOrEmpty(employee.getCandidate())
				? !HRMSHelper.isNullOrEmpty(employee.getCandidate().getEmailId()) ? employee.getCandidate().getEmailId()
						: null
				: null);

		profileVO.setGender(!HRMSHelper.isNullOrEmpty(employee.getCandidate())
				? !HRMSHelper.isNullOrEmpty(employee.getCandidate().getGender()) ? employee.getCandidate().getGender()
						: null
				: null);

		profileVO
				.setMaritalStatus(!HRMSHelper.isNullOrEmpty(employee.getCandidate())
						? !HRMSHelper.isNullOrEmpty(employee.getCandidate().getCandidatePersonalDetail())
								? employee.getCandidate().getCandidatePersonalDetail().getMaritalStatus()
								: null
						: null);

		profileVO.setMobileNumber(!HRMSHelper.isNullOrEmpty(employee.getCandidate())
				? !HRMSHelper.isNullOrEmpty(employee.getCandidate().getMobileNumber())
						? employee.getCandidate().getMobileNumber()
						: 0
				: 0);

		profileVO
				.setSpouseName(!HRMSHelper.isNullOrEmpty(employee.getCandidate())
						? !HRMSHelper.isNullOrEmpty(employee.getCandidate().getCandidatePersonalDetail())
								? employee.getCandidate().getCandidatePersonalDetail().getSpouseName()
								: null
						: null);
		
		profileVO.setGrade(!HRMSHelper.isNullOrEmpty(employee.getCandidate().getCandidateProfessionalDetail().getGrade())
				? !HRMSHelper.isNullOrEmpty(employee.getCandidate().getCandidateProfessionalDetail().getGrade())
						? employee.getCandidate().getCandidateProfessionalDetail().getGrade()
						: null
				: null);

		List<IdentificationDetailsVO> identificationDetails = new ArrayList<IdentificationDetailsVO>();
		if (!HRMSHelper.isNullOrEmpty(employee.getCandidate().getCandidateProfessionalDetail())) {
			profileVO.setDesignation(
					employee.getCandidate().getCandidateProfessionalDetail().getDesignation().getDesignationName());

			profileVO.setUan(employee.getCandidate().getCandidateProfessionalDetail().getUan());
			profileVO.setEmiratesId(employee.getCandidate().getCandidateProfessionalDetail().getEmiratesId());
			profileVO.setAadhaarNumber(employee.getCandidate().getCandidateProfessionalDetail().getAadhaarCard());

			IdentificationDetailsVO identificationDetail = new IdentificationDetailsVO();

			if (!HRMSHelper.isNullOrEmpty(checklistDetails)) {
				for (CandidateChecklist checklist : checklistDetails) {
					identificationDetail = new IdentificationDetailsVO();
					identificationDetail.setDocumentName(checklist.getAttachment());
					identificationDetail.setDocumentType(checklist.getChecklistItem());
					identificationDetails.add(identificationDetail);
				}
			}

		}

		// set passport details
		PassportDetailsVO passportDetailsVO = setPassportDetails(employee);

		profileVO.setDocuments(identificationDetails);
		profileVO.setPassportDetails(passportDetailsVO);

		return profileVO;

	}

	private static PassportDetailsVO setPassportDetails(Employee employee) {
		PassportDetailsVO passportDetailsVO = null;

		if (!HRMSHelper.isNullOrEmpty(employee.getCandidate().getCandidatePersonalDetail()) && !HRMSHelper
				.isNullOrEmpty(employee.getCandidate().getCandidatePersonalDetail().getCandidatePassportDetail())) {
			CandidatePassportDetail passportDetail = employee.getCandidate().getCandidatePersonalDetail()
					.getCandidatePassportDetail();
			passportDetailsVO = new PassportDetailsVO();
			passportDetailsVO.setFirstName(passportDetail.getPassportFirstName());

		}
		return passportDetailsVO;
	}

	public static AddressVO convertToAddressVO(List<CandidateAddress> candidateAddress,
			List<CandidateChecklist> checklistDetails) {
		AddressVO addressVO = new AddressVO();
		mapPresentAndPermanentAddress(candidateAddress, addressVO);

		mapCheckListdetails(checklistDetails, addressVO);
		return addressVO;
	}

	private static void mapCheckListdetails(List<CandidateChecklist> checklistDetails, AddressVO addressVO) {
		List<IdentificationDetailsVO> identificationDetails = new ArrayList<IdentificationDetailsVO>();

		IdentificationDetailsVO identificationDetail = new IdentificationDetailsVO();

		if (!HRMSHelper.isNullOrEmpty(checklistDetails)) {
			for (CandidateChecklist checklist : checklistDetails) {
				if (checklist.getChecklistItem()
						.equalsIgnoreCase("CURRENT ADDRESS PROOF(ADHAR CARD/LIGHT BILL COPY/AGREEMENT COPY)")) {

					identificationDetail = new IdentificationDetailsVO();
					identificationDetail.setDocumentName(checklist.getAttachment());
					identificationDetail.setDocumentType(checklist.getChecklistItem());
					identificationDetails.add(identificationDetail);
					addressVO.setPresentDocuments(identificationDetail);
				}
				if (checklist.getChecklistItem()
						.equalsIgnoreCase("PERMANENT ADDRESS PROOF(ADHAR CARD/LIGHT BILL COPY/AGREEMENT COPY)")) {
					identificationDetail = new IdentificationDetailsVO();
					identificationDetail.setDocumentName(checklist.getAttachment());
					identificationDetail.setDocumentType(checklist.getChecklistItem());
					identificationDetails.add(identificationDetail);
					addressVO.setPermanentDocuments(identificationDetail);
				}
			}
		}
	}

	private static void mapPresentAndPermanentAddress(List<CandidateAddress> candidateAddress, AddressVO addressVO) {
		AddressDetailsVO presentAddress = new AddressDetailsVO();
		AddressDetailsVO permanentAddress = new AddressDetailsVO();
		for (CandidateAddress address : candidateAddress) {
			if (address.getAddressType().equalsIgnoreCase(IHRMSConstants.PERMANANT_ADDRESS_TYPE)) {
				permanentAddress.setId(address.getId());
				permanentAddress.setAddressLine1(address.getAddressLine1());
				permanentAddress.setAddressLine2(address.getAddressLine2());
				permanentAddress.setStreet(address.getStreet());
				permanentAddress.setLandMark(address.getLandMark());
				permanentAddress.setAddressType(address.getAddressType());
				permanentAddress.setCity(address.getCity().getCityName());
				permanentAddress.setCountry(address.getCountry().getCountryName());
				permanentAddress.setPincode(address.getPincode());
				permanentAddress.setState(address.getState().getStateName());
				permanentAddress.setCandidateId(address.getCandidate().getId());
				permanentAddress.setNationality(address.getNationality());
				permanentAddress.setIsRental(address.getIsRental());
				permanentAddress.setOwnerAadhar(address.getOwnerAdhar());
				permanentAddress.setOwnerContact(address.getOwnerContact());
				permanentAddress.setOwnerName(address.getOwnerName());
				addressVO.setCitizenship(
						!HRMSHelper.isNullOrEmpty(address.getCandidate().getCandidatePersonalDetail().getCitizenship())
								? address.getCandidate().getCandidatePersonalDetail().getCitizenship()
								: null);
				addressVO.setPermanentAddress(permanentAddress);
				permanentAddress.setCitizenship(
						!HRMSHelper.isNullOrEmpty(address.getCandidate().getCandidatePersonalDetail().getCitizenship())
								? address.getCandidate().getCandidatePersonalDetail().getCitizenship()
								: null);
				addressVO.setNationality(address.getNationality());
				addressVO.setSSNumber(address.getSsnNumber());
			} else if (address.getAddressType().equalsIgnoreCase(IHRMSConstants.PRESENT_ADDRESS_TYPE)) {
				presentAddress.setId(address.getId());
				presentAddress.setAddressLine1(address.getAddressLine1());
				presentAddress.setAddressLine2(address.getAddressLine2());
				presentAddress.setStreet(address.getStreet());
				presentAddress.setLandMark(address.getLandMark());
				presentAddress.setAddressType(address.getAddressType());
				presentAddress.setCity(address.getCity().getCityName());
				presentAddress.setCountry(address.getCountry().getCountryName());
				presentAddress.setPincode(address.getPincode());
				presentAddress.setState(address.getState().getStateName());
				presentAddress.setIsRental(address.getIsRental());
				presentAddress.setOwnerName(address.getOwnerName());
				presentAddress.setOwnerAadhar(address.getOwnerAdhar());
				presentAddress.setOwnerContact(address.getOwnerContact());
				presentAddress.setCandidateId(address.getCandidate().getId());
				presentAddress.setNationality(address.getNationality());
				presentAddress.setCitizenship(
						!HRMSHelper.isNullOrEmpty(address.getCandidate().getCandidatePersonalDetail().getCitizenship())
								? address.getCandidate().getCandidatePersonalDetail().getCitizenship()
								: null);
				addressVO.setPresentAddress(presentAddress);
			}

		}
	}

	public static EducationVO convertToEducationVO(Candidate candidate, List<CandidateChecklist> checklistDetails) {
		List<EducationalDetailsVO> educationDetails = new ArrayList<EducationalDetailsVO>();
		EducationVO educationVO = new EducationVO();
		EducationalDetailsVO educationalDetail = null;

		if (!HRMSHelper.isNullOrEmpty(candidate.getCandidateProfessionalDetail())) {

			for (CandidateQualification qualification : candidate.getCandidateProfessionalDetail()
					.getCandidateQualifications()) {
				educationalDetail = new EducationalDetailsVO();
				educationalDetail.setAcademicAchievements(qualification.getAcademicAchievements());
				educationalDetail.setBoardUniversity(qualification.getBoardUniversity());
				educationalDetail.setDegree(qualification.getDegree());
				educationalDetail.setGradeDivisionPercentage(qualification.getGradeDivisionPercentage());
				educationalDetail.setInstituteName(qualification.getInstituteName());
				educationalDetail.setModeOfEducation(qualification.getModeOfEducation());
				educationalDetail.setPassingYearMonth(
						HRMSDateUtil.format(qualification.getPassingYearMonth(), IHRMSConstants.FRONT_END_DATE_FORMAT));
				// educationalDetail.setQualification(qualification.getQualification());
				// educationalDetail.setRemarks(qualification.getRemark());
				educationalDetail.setStateLocation(qualification.getStateLocation());
				educationalDetail.setSubjectOfSpecialization(qualification.getSubjectOfSpecialization());
				educationalDetail.setQualificationId(qualification.getId());
				educationalDetail.setCandidateId(qualification.getCandidateProfessionalDetail().getCandidate().getId());
				educationalDetail.setOrgId(qualification.getOrgId());
				educationDetails.add(educationalDetail);

			}
		}
		educationVO.setEducationDetails(educationDetails);

		List<IdentificationDetailsVO> identificationDetails = new ArrayList<IdentificationDetailsVO>();

		IdentificationDetailsVO identificationDetail = new IdentificationDetailsVO();

		if (!HRMSHelper.isNullOrEmpty(checklistDetails)) {
			for (CandidateChecklist checklist : checklistDetails) {
				identificationDetail = new IdentificationDetailsVO();
				identificationDetail.setDocumentName(checklist.getAttachment());
				identificationDetail.setDocumentType(checklist.getChecklistItem());
				identificationDetails.add(identificationDetail);
			}
		}
		educationVO.setDocuments(identificationDetails);

		return educationVO;
	}

	public static CertificationVO convertToCertificationVO(Candidate candidate,
			List<CandidateChecklist> checklistDetails) {
		CertificationVO certificationVO = new CertificationVO();
		List<CertificationDetailsVO> certificationDetails = new ArrayList<CertificationDetailsVO>();
		CertificationDetailsVO certificationDetail = null;

		if (!HRMSHelper.isNullOrEmpty(candidate.getCandidateProfessionalDetail())) {

			for (CandidateCertification certification : candidate.getCandidateProfessionalDetail()
					.getCandidateCertifications()) {
				certificationDetail = new CertificationDetailsVO();

				certificationDetail.setCertificationDate(HRMSDateUtil.format(certification.getCertificationDate(),
						IHRMSConstants.FRONT_END_DATE_FORMAT));
				certificationDetail.setCertificationName(certification.getCertificationName());
				certificationDetail.setCertificationType(certification.getCertificationType());
				certificationDetail.setCertificationValidityDate(HRMSDateUtil
						.format(certification.getCertificationValidityDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
				certificationDetail.setModeOfEducation(certification.getModeOfEducation());
				certificationDetail.setPercentageGrade(certification.getPercentageGrade());
				certificationDetail.setNameOfInstitute(certification.getNameOfInstitute());
				certificationDetail.setCertificationId(certification.getId());

				certificationDetails.add(certificationDetail);

			}
		}
		certificationVO.setCertificationDetails(certificationDetails);

		List<IdentificationDetailsVO> identificationDetails = new ArrayList<IdentificationDetailsVO>();

		IdentificationDetailsVO identificationDetail = new IdentificationDetailsVO();

		for (CandidateChecklist checklist : checklistDetails) {
			identificationDetail = new IdentificationDetailsVO();
			identificationDetail.setDocumentName(checklist.getAttachment());
			identificationDetail.setDocumentType(checklist.getChecklistItem());
			identificationDetails.add(identificationDetail);
		}

		certificationVO.setDocuments(identificationDetails);

		return certificationVO;
	}

	public static HealthDetailsVO convertToHealthReportVO(Candidate employee,
			List<CandidateChecklist> checklistDetails) {
		HealthDetailsVO healthDetail = new HealthDetailsVO();

		if (!HRMSHelper.isNullOrEmpty(employee.getCandidatePersonalDetail())) {
			CandidateHealthReport a = employee.getCandidatePersonalDetail().getCandidateHealthReport();
			if(!HRMSHelper.isNullOrEmpty(a)) {
			healthDetail.setAllergy(!HRMSHelper.isNullOrEmpty(a.getAllergy()) ? a.getAllergy() : null);
			healthDetail.setAllergyDiscription(!HRMSHelper.isNullOrEmpty(a.getAllergyDescription()) ? a.getAllergyDescription() : null); 
			healthDetail.setBloodGroup(a.getBloodGroup());
			healthDetail.setHealthHistory(!HRMSHelper.isNullOrEmpty(a.getHealthHistory()) ? a.getHealthHistory() : null);
			healthDetail.setHealthHistoryDiscription(!HRMSHelper.isNullOrEmpty(a.getHealthHistoryDescription()) ? a.getHealthHistoryDescription() : null );
			healthDetail.setHospitalization(!HRMSHelper.isNullOrEmpty(a.getHospitalization()) ? a.getHospitalization() : null);
			healthDetail.setIdentificationMark(!HRMSHelper.isNullOrEmpty(a.getIdentificationMark()) ? a.getIdentificationMark() : null);
			healthDetail.setInterestedToDonateBlood(!HRMSHelper.isNullOrEmpty(a.getInterestedToDonateBlood()) ? a.getInterestedToDonateBlood() : null);
			healthDetail.setPhysicallyHandicapDiscription(!HRMSHelper.isNullOrEmpty(a.getPhysicallyHandicapDescription()) ? a.getPhysicallyHandicapDescription() : null);
			healthDetail.setPhysicallyHandicapped(!HRMSHelper.isNullOrEmpty(a.getPhysicallyHandicapped()) ? a.getPhysicallyHandicapped() : null);
			healthDetail.setSurgery(!HRMSHelper.isNullOrEmpty(a.getSurgery()) ? a.getSurgery() : null);
			healthDetail.setSurgeryDiscription(!HRMSHelper.isNullOrEmpty(a.getSurgeryDescription()) ? a.getSurgeryDescription() : null);
			healthDetail.setVisionProblem(!HRMSHelper.isNullOrEmpty(a.getVisionProblem()) ? a.getVisionProblem() : null);
			healthDetail.setEsic(!HRMSHelper.isNullOrEmpty(a.getCandidatePersonalDetail().getEsiNo()) ? a.getCandidatePersonalDetail().getEsiNo() : null);
			healthDetail.setCandidateId(employee.getId());
			}
		}
		List<IdentificationDetailsVO> identificationDetails = new ArrayList<IdentificationDetailsVO>();

		IdentificationDetailsVO identificationDetail = new IdentificationDetailsVO();

		for (CandidateChecklist checklist : checklistDetails) {
			identificationDetail = new IdentificationDetailsVO();
			identificationDetail.setDocumentName(checklist.getAttachment());
			identificationDetail.setDocumentType(checklist.getChecklistItem());
			identificationDetails.add(identificationDetail);
		}

		healthDetail.setDocuments(identificationDetails);
		return healthDetail;
	}

	public static FamilyVO convertToFamilyVO(Candidate employee) throws HRMSException {
		FamilyVO familyVO = new FamilyVO();
		List<FamilyDetailsVO> familyDetails = new ArrayList<FamilyDetailsVO>();
		FamilyDetailsVO familyDetail = null;
		Set<CandidateFamilyDetail> familyDetailsList = employee.getCandidatePersonalDetail()
				.getCandidateFamilyDetails();
		if (familyDetailsList != null && !familyDetailsList.isEmpty()) {
			if (!HRMSHelper.isNullOrEmpty(employee.getCandidatePersonalDetail())) {
				for (CandidateFamilyDetail a : employee.getCandidatePersonalDetail().getCandidateFamilyDetails()) {
					familyDetail = new FamilyDetailsVO();
					familyDetail.setAge(HRMSHelper.calculateAge(a.getDateOfBirth()));
					familyDetail.setContactNo1(a.getContactNo1());
					familyDetail.setContactNo2(a.getContactNo2());
					familyDetail.setDateOfBirth(
							HRMSDateUtil.format(a.getDateOfBirth(), IHRMSConstants.FRONT_END_DATE_FORMAT));
					familyDetail.setDependent(a.getDependent());
					familyDetail.setDependentSeverelyDisabled(a.getDependentSeverelyDisabled());
					familyDetail.setFirstName(a.getFirst_name());
					familyDetail.setGender(a.getGender());
					familyDetail.setIsEmergencyContact(a.getIsEmergencyContact());
					familyDetail.setLastName(a.getLast_name());
					familyDetail.setMiddleName(a.getMiddle_name());
					familyDetail.setOccupation(a.getOccupation());
					familyDetail.setRelationship(a.getRelationship());
					familyDetail.setWorking(a.getWorking());
					familyDetail.setId(a.getId());

					familyDetails.add(familyDetail);

				}
			}
		} else {

			throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
		}
		Set<CandidateEmergencyContact> emergencyContactDetailsList = employee.getCandidatePersonalDetail()
				.getCandidateEmergencyContacts();
		List<EmergencyContactVO> emergencyContactDetails = new ArrayList<EmergencyContactVO>();
		EmergencyContactVO emergencyContact = null;
		if (!HRMSHelper.isNullOrEmpty(employee.getCandidatePersonalDetail())
				&& (!employee.getCandidatePersonalDetail().getCandidateEmergencyContacts().isEmpty() && !HRMSHelper
						.isNullOrEmpty(employee.getCandidatePersonalDetail().getCandidateEmergencyContacts()))) {

			if (!HRMSHelper.isNullOrEmpty(emergencyContactDetailsList) && !emergencyContactDetailsList.isEmpty()) {

				for (CandidateEmergencyContact contact : employee.getCandidatePersonalDetail()
						.getCandidateEmergencyContacts()) {
					emergencyContact = new EmergencyContactVO();
					emergencyContact.setFirstname(contact.getFirstname());
					emergencyContact.setMiddlename(contact.getMiddlename());
					emergencyContact.setLastname(contact.getLastname());
					emergencyContact.setRelationship(contact.getRelationship());
					;
					emergencyContact.setMobileNumber(contact.getMobileNumber());
					emergencyContact.setLandlineNumber(contact.getLandlineNumber());

					emergencyContactDetails.add(emergencyContact);
				}
			} else {

				throw new HRMSException(1201, ResponseCode.getResponseCodeMap().get(1201));
			}
		}

		familyVO.setFamilyDetails(familyDetails);
		familyVO.setEmergencyContactDetails(emergencyContactDetails);
		return familyVO;
	}

}
