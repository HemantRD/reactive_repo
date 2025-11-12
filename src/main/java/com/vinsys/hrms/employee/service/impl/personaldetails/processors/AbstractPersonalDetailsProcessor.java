package com.vinsys.hrms.employee.service.impl.personaldetails.processors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.directonboard.vo.AddressDetailsRequestVo;
import com.vinsys.hrms.directonboard.vo.CandidateAddressVo;
import com.vinsys.hrms.employee.vo.CertificationDetailsVO;
import com.vinsys.hrms.employee.vo.CertificationVO;
import com.vinsys.hrms.employee.vo.ContactNoVo;
import com.vinsys.hrms.employee.vo.EducationVO;
import com.vinsys.hrms.employee.vo.EducationalDetailsVO;
import com.vinsys.hrms.employee.vo.FamilyDetailsVO;
import com.vinsys.hrms.employee.vo.FamilyVO;
import com.vinsys.hrms.employee.vo.HealthDetailsVO;
import com.vinsys.hrms.employee.vo.IdentificationDetailsVO;
import com.vinsys.hrms.employee.vo.ProfileVO;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateAddress;
import com.vinsys.hrms.entity.CandidateCertification;
import com.vinsys.hrms.entity.CandidateChecklist;
import com.vinsys.hrms.entity.CandidateFamilyDetail;
import com.vinsys.hrms.entity.CandidatePersonalDetail;
import com.vinsys.hrms.entity.CandidateProfessionalDetail;
import com.vinsys.hrms.entity.CandidateQualification;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.MasterCity;
import com.vinsys.hrms.entity.MasterCountry;
import com.vinsys.hrms.entity.MasterState;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.master.vo.MasterCityVO;
import com.vinsys.hrms.master.vo.MasterCountryVO;
import com.vinsys.hrms.master.vo.MasterStateVO;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.util.EChecklistItems;
import com.vinsys.hrms.util.ERole;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.PersonalDetailsTransformUtils;
import com.vinsys.hrms.util.ResponseCode;

public abstract class AbstractPersonalDetailsProcessor implements IPersonalDetailsProcessor {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private PersonalDetailsProcessorDependencies processorDependencies;

	protected AbstractPersonalDetailsProcessor(PersonalDetailsProcessorDependencies personaleDetailsDependencies) {
		this.processorDependencies = personaleDetailsDependencies;
	}

	@Override
	public HRMSBaseResponse<ProfileVO> getProfileDetails() throws HRMSException {
		log.info("Inside getProfileDetails method");
		HRMSBaseResponse<ProfileVO> response = new HRMSBaseResponse<>();

		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		Employee employee = processorDependencies.getEmployeeDAO().findEmpCandByEmpId(employeeId);

		// String tabName="PROFILE_DETAILS";
		ProfileVO profileVo = null;
		if (!HRMSHelper.isNullOrEmpty(employee)) {
			List<CandidateChecklist> checklist = processorDependencies.getChecklistDAO()
					.getCandidateWithChecklistDetailsByItemId(
							employee.getCandidate().getCandidateProfessionalDetail().getId(),
							employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
							EChecklistItems.PROFILE_DETAILS.toString());

			profileVo = PersonalDetailsTransformUtils.convertToProfileVO(employee, checklist);
		}
		response.setResponseBody(profileVo);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		log.info("Exit from getProfileDetails method");
		return response;
	}

	@Override
	public HRMSBaseResponse<AddressDetailsRequestVo> getAddress(Long candidateId) throws HRMSException {
		log.info("Inside getProfileDetails method");
		HRMSBaseResponse<AddressDetailsRequestVo> response = new HRMSBaseResponse<>();

		Long validCandidateId = validateAndGetCandidateId(candidateId);
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();

		Candidate candidate = processorDependencies.getCandidateDao().findByIdAndIsActiveAndorgId(validCandidateId,
				IHRMSConstants.isActive, orgId);
		// Employee employee =
		// processorDependencies.getEmployeeDAO().findByCandidateIdAndIsActiveAndOrgId(validCandidateId,
		// IHRMSConstants.isActive,SecurityFilter.TL_CLAIMS.get().getOrgId());

		if (HRMSHelper.isNullOrEmpty(candidate)) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		if (!orgId.equals(candidate.getOrgId())) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1521));
		}
		List<CandidateAddress> candidateAddress = null;
		if (!HRMSHelper.isNullOrEmpty(candidate)) {
			candidateAddress = processorDependencies.getAddressDAO().findByCandidateIdAndIsActive(candidate.getId(),
					IHRMSConstants.isActive);
		}
		List<CandidateChecklist> checklist = null;
		if (HRMSHelper.isRolePresent(role, ERole.HR.name())) {
			checklist = processorDependencies.getChecklistDAO().getCandidateWithChecklistDetailsByCandidateIdAndItemId(
					candidate.getCandidateProfessionalDetail().getId(), EChecklistItems.ADDRESS_DETAILS.toString());
		} else {
			checklist = processorDependencies.getChecklistDAO().getCandidateWithChecklistDetailsByItemId(
					candidate.getCandidateProfessionalDetail().getId(),
					candidate.getCandidateProfessionalDetail().getDivision().getId(),
					EChecklistItems.ADDRESS_DETAILS.toString());
		}
		AddressDetailsRequestVo addressVO = null;
		if (!HRMSHelper.isNullOrEmpty(candidate)) {
			addressVO = convertToAddressVO(candidateAddress, checklist);
		}
		response.setResponseBody(addressVO);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		log.info("Exit from getProfileDetails method");
		return response;
	}

	public AddressDetailsRequestVo convertToAddressVO(List<CandidateAddress> candidateAddress,
			List<CandidateChecklist> checklistDetails) {
		AddressDetailsRequestVo addressVO = new AddressDetailsRequestVo();
		mapPresentAndPermanentAddress(candidateAddress, addressVO);

		mapCheckListdetails(checklistDetails, addressVO);
		return addressVO;
	}

	private void mapPresentAndPermanentAddress(List<CandidateAddress> candidateAddress,
			AddressDetailsRequestVo addressVO) {
		CandidateAddressVo presentAddress = new CandidateAddressVo();
		CandidateAddressVo permanentAddress = new CandidateAddressVo();
		for (CandidateAddress address : candidateAddress) {
			if (address.getAddressType().equalsIgnoreCase(IHRMSConstants.PERMANANT_ADDRESS_TYPE)) {
				permanentAddress.setId(address.getId());
				permanentAddress.setAddressLine1(address.getAddressLine1());
				// permanentAddress.setAddressLine2(address.getAddressLine2());
				addressVO.setCandidateId(address.getCandidate().getId());
				permanentAddress.setStreet(address.getStreet());
				permanentAddress.setLandMark(address.getLandMark());
				permanentAddress.setAddressType(address.getAddressType());
				permanentAddress.setCity(convertToCityMasterVO(address.getCity().getId()));
				permanentAddress.setCountry(convertToCountryMasterVO(address.getCountry().getId()));
				permanentAddress.setPincode(address.getPincode());
				permanentAddress.setState(convertToStateMasterVO(address.getState().getId()));
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
				presentAddress.setStreet(address.getStreet());
				presentAddress.setLandMark(address.getLandMark());
				presentAddress.setAddressType(address.getAddressType());
				presentAddress.setCity(convertToCityMasterVO(address.getCity().getId()));
				presentAddress.setCountry(convertToCountryMasterVO(address.getCountry().getId()));
				presentAddress.setPincode(address.getPincode());
				presentAddress.setState(convertToStateMasterVO(address.getState().getId()));
				presentAddress.setIsRental(address.getIsRental());
				presentAddress.setOwnerName(address.getOwnerName());
				presentAddress.setOwnerAadhar(address.getOwnerAdhar());
				presentAddress.setOwnerContact(address.getOwnerContact());
				presentAddress.setNationality(address.getNationality());
				presentAddress.setCitizenship(
						!HRMSHelper.isNullOrEmpty(address.getCandidate().getCandidatePersonalDetail().getCitizenship())
								? address.getCandidate().getCandidatePersonalDetail().getCitizenship()
								: null);
				addressVO.setPresentAddress(presentAddress);
				addressVO.setCandidateId(address.getCandidate().getId());
			}

		}
	}

	private void mapCheckListdetails(List<CandidateChecklist> checklistDetails, AddressDetailsRequestVo addressVO) {
		List<IdentificationDetailsVO> currentIdentificationDetails = new ArrayList<IdentificationDetailsVO>();
		List<IdentificationDetailsVO> permenantIdentificationDetails = new ArrayList<IdentificationDetailsVO>();

		IdentificationDetailsVO identificationDetail = new IdentificationDetailsVO();

		if (!HRMSHelper.isNullOrEmpty(checklistDetails)) {
			for (CandidateChecklist checklist : checklistDetails) {
				if (checklist.getChecklistItem()
						.equalsIgnoreCase("CURRENT ADDRESS PROOF(ADHAR CARD/LIGHT BILL COPY/AGREEMENT COPY)")) {

					identificationDetail = new IdentificationDetailsVO();
					identificationDetail.setDocumentName(checklist.getAttachment());
					identificationDetail.setDocumentType(checklist.getChecklistItem());
					currentIdentificationDetails.add(identificationDetail);
					addressVO.setPresentDocuments(currentIdentificationDetails);
				}

				if (checklist.getChecklistItem().equalsIgnoreCase("ELECTRICITY_BILL_CURRENT")) {

					identificationDetail = new IdentificationDetailsVO();
					identificationDetail.setDocumentName(checklist.getAttachment());
					identificationDetail.setDocumentType(checklist.getChecklistItem());
					currentIdentificationDetails.add(identificationDetail);
					addressVO.setPresentDocuments(currentIdentificationDetails);
				}

				if (checklist.getChecklistItem().equalsIgnoreCase("PHONE_BILL_CURRENT")) {

					identificationDetail = new IdentificationDetailsVO();
					identificationDetail.setDocumentName(checklist.getAttachment());
					identificationDetail.setDocumentType(checklist.getChecklistItem());
					currentIdentificationDetails.add(identificationDetail);
					addressVO.setPresentDocuments(currentIdentificationDetails);
				}

				if (checklist.getChecklistItem().equalsIgnoreCase("RATION_CARD_CURRENT")) {

					identificationDetail = new IdentificationDetailsVO();
					identificationDetail.setDocumentName(checklist.getAttachment());
					identificationDetail.setDocumentType(checklist.getChecklistItem());
					currentIdentificationDetails.add(identificationDetail);
					addressVO.setPresentDocuments(currentIdentificationDetails);
				}
				if (checklist.getChecklistItem().equalsIgnoreCase("GAS_BILL_CURRENT")) {

					identificationDetail = new IdentificationDetailsVO();
					identificationDetail.setDocumentName(checklist.getAttachment());
					identificationDetail.setDocumentType(checklist.getChecklistItem());
					currentIdentificationDetails.add(identificationDetail);
					addressVO.setPresentDocuments(currentIdentificationDetails);
				}
				if (checklist.getChecklistItem().equalsIgnoreCase("RENT_RECIPT_CURRENT")) {

					identificationDetail = new IdentificationDetailsVO();
					identificationDetail.setDocumentName(checklist.getAttachment());
					identificationDetail.setDocumentType(checklist.getChecklistItem());
					currentIdentificationDetails.add(identificationDetail);
					addressVO.setPresentDocuments(currentIdentificationDetails);
				}
			}
			for (CandidateChecklist checklist : checklistDetails) {
				if (checklist.getChecklistItem()
						.equalsIgnoreCase("PERMANENT ADDRESS PROOF(ADHAR CARD/LIGHT BILL COPY/AGREEMENT COPY)")) {
					identificationDetail = new IdentificationDetailsVO();
					identificationDetail.setDocumentName(checklist.getAttachment());
					identificationDetail.setDocumentType(checklist.getChecklistItem());
					permenantIdentificationDetails.add(identificationDetail);
					addressVO.setPermanentDocuments(permenantIdentificationDetails);
				}
				if (checklist.getChecklistItem().equalsIgnoreCase("ELECTRICITY_BILL_PERMANENT")) {
					identificationDetail = new IdentificationDetailsVO();
					identificationDetail.setDocumentName(checklist.getAttachment());
					identificationDetail.setDocumentType(checklist.getChecklistItem());
					permenantIdentificationDetails.add(identificationDetail);
					addressVO.setPermanentDocuments(permenantIdentificationDetails);
				}
				if (checklist.getChecklistItem().equalsIgnoreCase("PHONE_BILL_PERMANENT")) {
					identificationDetail = new IdentificationDetailsVO();
					identificationDetail.setDocumentName(checklist.getAttachment());
					identificationDetail.setDocumentType(checklist.getChecklistItem());
					permenantIdentificationDetails.add(identificationDetail);
					addressVO.setPermanentDocuments(permenantIdentificationDetails);
				}
				if (checklist.getChecklistItem().equalsIgnoreCase("RATION_CARD_PERMANENT")) {
					identificationDetail = new IdentificationDetailsVO();
					identificationDetail.setDocumentName(checklist.getAttachment());
					identificationDetail.setDocumentType(checklist.getChecklistItem());
					permenantIdentificationDetails.add(identificationDetail);
					addressVO.setPermanentDocuments(permenantIdentificationDetails);
				}

				if (checklist.getChecklistItem().equalsIgnoreCase("GAS_BILL_PERMANENT")) {
					identificationDetail = new IdentificationDetailsVO();
					identificationDetail.setDocumentName(checklist.getAttachment());
					identificationDetail.setDocumentType(checklist.getChecklistItem());
					permenantIdentificationDetails.add(identificationDetail);
					addressVO.setPermanentDocuments(permenantIdentificationDetails);
				}

				if (checklist.getChecklistItem().equalsIgnoreCase("RENT_RECIPT_PERMANENT")) {
					identificationDetail = new IdentificationDetailsVO();
					identificationDetail.setDocumentName(checklist.getAttachment());
					identificationDetail.setDocumentType(checklist.getChecklistItem());
					permenantIdentificationDetails.add(identificationDetail);
					addressVO.setPermanentDocuments(permenantIdentificationDetails);
				}
			}
		}
	}

	public MasterCountryVO convertToCountryMasterVO(Long id) {
		MasterCountryVO countryVo = new MasterCountryVO();
		MasterCountry country = processorDependencies.coutryDao.findById(id).get();
		if (!HRMSHelper.isNullOrEmpty(countryVo)) {
			countryVo.setId(country.getId());
			countryVo.setCountryName(country.getCountryName());
		}
		return countryVo;
	}

	public MasterCityVO convertToCityMasterVO(Long id) {
		MasterCityVO cityVo = new MasterCityVO();
		MasterCity city = processorDependencies.cityDao.findById(id).get();
		if (!HRMSHelper.isNullOrEmpty(cityVo)) {
			cityVo.setId(city.getId());
			cityVo.setCityName(city.getCityName());
		}
		return cityVo;
	}

	public MasterStateVO convertToStateMasterVO(Long id) {
		MasterStateVO stateVo = new MasterStateVO();
		MasterState state = processorDependencies.stateDao.findById(id).get();
		if (!HRMSHelper.isNullOrEmpty(stateVo)) {
			stateVo.setId(state.getId());
			stateVo.setStateName(state.getStateName());
		}
		return stateVo;
	}

	@Override
	public HRMSBaseResponse<EducationVO> getEducationalDetails(Long candidateId) throws HRMSException {
		log.info("Inside getEducationalDetails method");
		HRMSBaseResponse<EducationVO> response = new HRMSBaseResponse<>();

		Long validCandidateId = validateAndGetCandidateId(candidateId);
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		Candidate candidateDetail = processorDependencies.getCandidateDao().findByIdAndIsActive(validCandidateId,
				IHRMSConstants.isActive);
		Candidate candidate = processorDependencies.getCandidateDao().findByIdAndIsActiveAndorgId(validCandidateId,
				IHRMSConstants.isActive, candidateDetail.getOrgId());

		if (HRMSHelper.isNullOrEmpty(candidate)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}

		if (!orgId.equals(candidate.getOrgId())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
		if (HRMSHelper.isNullOrEmpty(candidate.getCandidateProfessionalDetail())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}
		// String tabName="EDUCATIONAL_DETAILS";
		List<CandidateChecklist> checklist = processorDependencies.getChecklistDAO()
				.getCandidateWithChecklistDetailsByItemId(candidate.getCandidateProfessionalDetail().getId(),
						candidate.getCandidateProfessionalDetail().getDivision().getId(),
						EChecklistItems.EDUCATIONAL_DETAILS.toString());
		EducationVO educationVO = null;
		if (!HRMSHelper.isNullOrEmpty(candidate)) {
			educationVO = PersonalDetailsTransformUtils.convertToEducationVO(candidate, checklist);
		}
		response.setResponseBody(educationVO);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		log.info("Exit from getEducationalDetails method");
		return response;
	}

	@Override
	public HRMSBaseResponse<CertificationVO> getCertificationDetails(Long candidateId) throws HRMSException {
		log.info("Inside getCertificationDetails method");
		HRMSBaseResponse<CertificationVO> response = new HRMSBaseResponse<>();

		Long validCandidateId = validateAndGetCandidateId(candidateId);
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();

		Candidate candidateDetail = processorDependencies.getCandidateDao().findByIdAndIsActive(validCandidateId,
				IHRMSConstants.isActive);
		Candidate candidate = processorDependencies.getCandidateDao().findByIdAndIsActiveAndorgId(validCandidateId,
				IHRMSConstants.isActive, candidateDetail.getOrgId());
		if (HRMSHelper.isNullOrEmpty(candidate)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}
		if (!orgId.equals(candidate.getOrgId())) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1521));
		}

		Employee emp = processorDependencies.employeeDAO.findByCandidateIdAndIsActiveAndOrgId(candidate.getId(),
				IHRMSConstants.isActive, orgId);
		List<CandidateChecklist> checklist = null;

		if (HRMSHelper.isRolePresent(role, ERole.HR.name())) {
			checklist = processorDependencies.getChecklistDAO().getCandidateWithChecklistDetailsByCandidateIdAndItemId(
					candidate.getCandidateProfessionalDetail().getId(),
					EChecklistItems.CERTIFICATION_DETAILS.toString());

		} else {
			checklist = processorDependencies.getChecklistDAO().getCandidateWithChecklistDetailsByItemId(
					candidate.getCandidateProfessionalDetail().getId(),
					candidate.getCandidateProfessionalDetail().getDivision().getId(),
					EChecklistItems.CERTIFICATION_DETAILS.toString());
		}
		CertificationVO certificationVO = null;
		if (!HRMSHelper.isNullOrEmpty(candidate)) {
			certificationVO = PersonalDetailsTransformUtils.convertToCertificationVO(candidate, checklist);
		}
		if (HRMSHelper.isNullOrEmpty(certificationVO.getCertificationDetails())
				&& HRMSHelper.isNullOrEmpty(certificationVO.getDocuments())) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		response.setResponseBody(certificationVO);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		log.info("Exit from getCertificationDetails method");
		return response;
	}

	@Override
	public HRMSBaseResponse<HealthDetailsVO> getHealthDetails(Long candidateId) throws HRMSException {

		log.info("Inside getHealthDetails method");
		HRMSBaseResponse<HealthDetailsVO> response = new HRMSBaseResponse<>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long actualCandidateId = validateAndGetCandidateId(candidateId);

		Candidate candidate = processorDependencies.candidateDao.findByIdAndIsActiveAndorgId(actualCandidateId,
				IHRMSConstants.isActive, SecurityFilter.TL_CLAIMS.get().getOrgId());

		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		if (HRMSHelper.isNullOrEmpty(candidate)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
		}
		if (!orgId.equals(candidate.getOrgId())) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1521));
		}
		List<CandidateChecklist> checklist = null;

		if (HRMSHelper.isRolePresent(role, ERole.HR.name())) {
			checklist = processorDependencies.getChecklistDAO().getCandidateWithChecklistDetailsByCandidateIdAndItemId(
					candidate.getCandidateProfessionalDetail().getId(), EChecklistItems.HEALTH_DETAILS.toString());
		} else {
			checklist = processorDependencies.getChecklistDAO().getCandidateWithChecklistDetailsByItemId(
					candidate.getCandidateProfessionalDetail().getId(),
					candidate.getCandidateProfessionalDetail().getDivision().getId(),
					EChecklistItems.HEALTH_DETAILS.toString());
		}

		HealthDetailsVO healthDetails = null;
		if (!HRMSHelper.isNullOrEmpty(candidate)) {
			healthDetails = PersonalDetailsTransformUtils.convertToHealthReportVO(candidate, checklist);
		}
		response.setResponseBody(healthDetails);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		log.info("Exit from getHealthDetails method");
		return response;

	}

	@Override
	public HRMSBaseResponse<FamilyVO> getFamilyDetails(Long candidateId) throws HRMSException {
		log.info("Inside getFamilyDetails method");
		HRMSBaseResponse<FamilyVO> response = new HRMSBaseResponse<>();
		Long actualCandidateId = validateAndGetCandidateId(candidateId);
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		Candidate candidate = processorDependencies.candidateDao.findByIdAndIsActiveAndorgId(actualCandidateId,
				IHRMSConstants.isActive, SecurityFilter.TL_CLAIMS.get().getOrgId());

		if (!orgId.equals(candidate.getOrgId())) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1521));
		}

		FamilyVO familyVO = null;
		if (!HRMSHelper.isNullOrEmpty(candidate)) {
			familyVO = PersonalDetailsTransformUtils.convertToFamilyVO(candidate);
		}
		response.setResponseBody(familyVO);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		log.info("Exit from getFamilyDetails method");
		return response;
	}

	@Override
	public HRMSBaseResponse<?> addCertification(CertificationDetailsVO request) throws HRMSException {
		log.info("Inside addCertificationDetails method");
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();

		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee employee = processorDependencies.getEmployeeDAO().findEmpCandByEmpId(employeeId);

		// input validation
		processorDependencies.getEmployeeAuthorityHelper().validateCertificationDetails(request);

		CandidateProfessionalDetail professionalDetails = processorDependencies.getProfessionalDetailDao()
				.findByCandidateId(employee.getCandidate().getId());
		CandidateCertification certification = new CandidateCertification();

		certification.setCandidateProfessionalDetail(professionalDetails);
		certification.setCertificationName(request.getCertificationName());
		certification.setModeOfEducation(request.getModeOfEducation());
		certification.setNameOfInstitute(request.getNameOfInstitute());
		certification.setCertificationDate(HRMSDateUtil.parse(
				!HRMSHelper.isNullOrEmpty(request.getCertificationDate()) ? request.getCertificationDate() : null,
				IHRMSConstants.FRONT_END_DATE_FORMAT));
		certification.setCertificationValidityDate(
				HRMSDateUtil.parse(!HRMSHelper.isNullOrEmpty(request.getCertificationValidityDate())
						? request.getCertificationValidityDate()
						: null, IHRMSConstants.FRONT_END_DATE_FORMAT));
		certification.setPercentageGrade(request.getPercentageGrade());
		certification.setCreatedBy(employee.getCandidate().getId().toString());
		certification.setCertificationType(request.getCertificationType());
		certification.setCreatedDate(new Date());
		certification.setIsActive(IHRMSConstants.isActive);

		processorDependencies.getCandidateCertificationDetalDAO().save(certification);

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		log.info("Exit from add Certification details");
		return response;
	}

	@Override
	public HRMSBaseResponse<?> addEducationDetails(EducationalDetailsVO educationDetails) throws HRMSException {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		log.info("Inside add education details");

		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		Employee employee = processorDependencies.getEmployeeDAO().findEmpCandByEmpId(Long.valueOf(employeeId));

		// input validation
		processorDependencies.getEmployeeAuthorityHelper().validateEducationDetails(educationDetails);

		CandidateProfessionalDetail professionalDetails = processorDependencies.getProfessionalDetailDao()
				.findByCandidateId(employee.getCandidate().getId());
		CandidateQualification qualification = new CandidateQualification();

		qualification.setCandidateProfessionalDetail(professionalDetails);
		qualification.setDegree(educationDetails.getDegree());
		qualification.setModeOfEducation(educationDetails.getModeOfEducation());
		qualification.setInstituteName(educationDetails.getInstituteName());
		qualification.setBoardUniversity(educationDetails.getBoardUniversity());
		qualification.setGradeDivisionPercentage(educationDetails.getGradeDivisionPercentage());
		qualification.setPassingYearMonth(
				HRMSDateUtil.parse(!HRMSHelper.isNullOrEmpty(educationDetails.getPassingYearMonth())
						? educationDetails.getPassingYearMonth()
						: null, IHRMSConstants.FRONT_END_DATE_FORMAT));
		qualification.setStateLocation(educationDetails.getStateLocation());
		qualification.setAcademicAchievements(!HRMSHelper.isNullOrEmpty(educationDetails.getAcademicAchievements())
				? educationDetails.getAcademicAchievements()
				: null);
		qualification
				.setSubjectOfSpecialization(!HRMSHelper.isNullOrEmpty(educationDetails.getSubjectOfSpecialization())
						? educationDetails.getSubjectOfSpecialization()
						: null);
		qualification.setIsActive(IHRMSConstants.isActive);
		qualification.setCreatedBy(employee.getCandidate().getId().toString());
		qualification.setCreatedDate(new Date());

		processorDependencies.getEducationDao().save(qualification);

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		log.info("Exit from add education details");
		return response;
	}

	@Override
	public HRMSBaseResponse<?> addfamilyDetails(FamilyDetailsVO familyDetails) throws HRMSException {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		log.info("Inside add family details");

		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		Employee employee = processorDependencies.getEmployeeDAO().findEmpCandByEmpId(employeeId);

		processorDependencies.getEmployeeAuthorityHelper().validateFamilyDetails(familyDetails);

		CandidatePersonalDetail personalDetails = processorDependencies.getCandidatePersonalDetailDAO()
				.findBycandidate(employee.getCandidate());

		CandidateFamilyDetail family = new CandidateFamilyDetail();

		family.setCandidatePersonalDetail(personalDetails);
		family.setContactNo1(familyDetails.getContactNo1());
		family.setContactNo2(familyDetails.getContactNo2());
		family.setCreatedBy(employee.getCandidate().getId().toString());
		family.setDateOfBirth(HRMSDateUtil.parse(familyDetails.getDateOfBirth(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		family.setDependent(familyDetails.getDependent());
		family.setDependentSeverelyDisabled(familyDetails.getDependentSeverelyDisabled());
		family.setFirst_name(familyDetails.getFirstName());
		family.setGender(familyDetails.getGender());
		family.setIsEmergencyContact(familyDetails.getIsEmergencyContact());
		family.setIsActive(IHRMSConstants.isActive);
		family.setLast_name(familyDetails.getLastName());
		family.setMiddle_name(familyDetails.getMiddleName());
		family.setOccupation(familyDetails.getOccupation());
		family.setRelationship(familyDetails.getRelationship());
		family.setCreatedDate(new Date());

		processorDependencies.getFamilyDetailsDAO().save(family);

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		log.info("Exit from add family details");
		return response;
	}

	private Long validateAndGetCandidateId(Long candidateId) throws HRMSException {
		if (!HRMSHelper.isNullOrEmpty(candidateId)) {
			List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
			if (!HRMSHelper.isRolePresent(role, ERole.HR.name())) {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1521));
			}
			return candidateId;
		} else {
			return SecurityFilter.TL_CLAIMS.get().getCandidateId();
		}

	}

	private Long validateAndGetEmployeeId(Long empId) throws HRMSException {
		if (!HRMSHelper.isNullOrEmpty(empId)) {
			List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
			if (!HRMSHelper.isRolePresent(role, ERole.HR.name())) {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1521));
			}
			return empId;
		} else {
			return SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		}

	}

	@Override
	public HRMSBaseResponse<?> updateContact(ContactNoVo request) throws HRMSException {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		log.info("Inside update mobile no.");
		validateInpute(request);

			Long candId = SecurityFilter.TL_CLAIMS.get().getCandidateId();
			Candidate candidate = processorDependencies.getCandidateDao().findByIdAndIsActive(candId,
					ERecordStatus.Y.name());

			if (!HRMSHelper.isNullOrEmpty(candidate)) {
				candidate.setMobileNumber(request.getMobileNo());
				candidate.setUpdatedDate(new Date());
				processorDependencies.getCandidateDao().save(candidate);
			} else {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1909));

			log.info("Exit from update contact");
			return response;
	}

	private void validateInpute(ContactNoVo request) throws HRMSException {
	    Long mobileNo = request.getMobileNo();

	    
	    if (HRMSHelper.isNullOrEmpty(mobileNo)) {
	        throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501)); 
	    }

	    String mobileStr = String.valueOf(mobileNo);

	    
	    if (mobileStr.length() < 10 || mobileStr.length() > 15) {
	        throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501)); 
	    }
	    
	    if (!mobileStr.matches("^\\+?[0-9\\-]{10,15}$")) {
	        throw new HRMSException(1503, ResponseCode.getResponseCodeMap().get(1503)); 
	    }

//	    // Starts with digit between 0-9 
//	    if (!mobileStr.matches("^[0-9][0-9]{10}$")) {
//	        throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501)); 
//	    }
	}
}