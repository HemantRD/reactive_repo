package com.vinsys.hrms.employee.service.impl.personaldetails.processors;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.directonboard.vo.AddressDetailsRequestVo;
import com.vinsys.hrms.directonboard.vo.CandidateAddressVo;
import com.vinsys.hrms.employee.vo.EducationVO;
import com.vinsys.hrms.employee.vo.IdentificationDetailsVO;
import com.vinsys.hrms.employee.vo.ProfileVO;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateAddress;
import com.vinsys.hrms.entity.CandidateChecklist;
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
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.PersonalDetailsTransformUtils;
import com.vinsys.hrms.util.ResponseCode;

public class LryptPersonalDetailsProcessor extends AbstractPersonalDetailsProcessor {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private PersonalDetailsProcessorDependencies personalDetailsProcessorDependencies;

	public LryptPersonalDetailsProcessor(PersonalDetailsProcessorDependencies personalDetailsProcessorDependencies) {
		super(personalDetailsProcessorDependencies);
		this.personalDetailsProcessorDependencies = personalDetailsProcessorDependencies;
	}

	@Override
	public HRMSBaseResponse<ProfileVO> getProfileDetails() throws HRMSException {
		log.info("Inside getProfileDetails method");
		HRMSBaseResponse<ProfileVO> response = new HRMSBaseResponse<>();
		Long employeeId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Employee employee = personalDetailsProcessorDependencies.getEmployeeDAO().findByEmpIdAndOrgId(employeeId,
				SecurityFilter.TL_CLAIMS.get().getOrgId());
		// tabName="PROFILE_DETAILS";
		ProfileVO profileVo = null;
		if (!HRMSHelper.isNullOrEmpty(employee)) {
			List<CandidateChecklist> checklist = personalDetailsProcessorDependencies.getChecklistDAO()
					.getCandidateWithChecklistDetailsByItemIdAndOrgId(
							employee.getCandidate().getCandidateProfessionalDetail().getId(),
							employee.getCandidate().getCandidateProfessionalDetail().getDivision().getId(),
							EChecklistItems.PROFILE_DETAILS.toString(), SecurityFilter.TL_CLAIMS.get().getOrgId());
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
		// Employee employee =
		// personalDetailsProcessorDependencies.getEmployeeDAO().findByCandidateIdAndIsActiveAndOrgId(
		// validCandidateId, IHRMSConstants.isActive,
		// SecurityFilter.TL_CLAIMS.get().getOrgId());
		Candidate candidate = personalDetailsProcessorDependencies.getCandidateDao()
				.findByIdAndIsActiveAndorgId(validCandidateId, IHRMSConstants.isActive, orgId);
		if (!orgId.equals(candidate.getOrgId())) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1521));
		}

		List<CandidateAddress> candidateAddress = null;
		if (!HRMSHelper.isNullOrEmpty(candidate)) {
			candidateAddress = personalDetailsProcessorDependencies.getAddressDAO()
					.findByCandidateIdAndIsActive(candidate.getId(), IHRMSConstants.isActive);
		}
		List<CandidateChecklist> checklist = null;
		if (HRMSHelper.isRolePresent(role, ERole.HR.name())) {
			checklist = personalDetailsProcessorDependencies.getChecklistDAO()
					.getCandidateWithChecklistDetailsByCandidateIdAndItemId(
							candidate.getCandidateProfessionalDetail().getId(),
							EChecklistItems.ADDRESS_DETAILS.toString());
		} else {
			checklist = personalDetailsProcessorDependencies.getChecklistDAO().getCandidateWithChecklistDetailsByItemId(
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
				addressVO.setCandidateId(address.getCandidate().getId());
				// permanentAddress.setAddressLine2(address.getAddressLine2());
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
				
				if (checklist.getChecklistItem()
						.equalsIgnoreCase("ELECTRICITY_BILL_CURRENT")) {

					identificationDetail = new IdentificationDetailsVO();
					identificationDetail.setDocumentName(checklist.getAttachment());
					identificationDetail.setDocumentType(checklist.getChecklistItem());
					currentIdentificationDetails.add(identificationDetail);
					addressVO.setPresentDocuments(currentIdentificationDetails);
				}
				
				if (checklist.getChecklistItem()
						.equalsIgnoreCase("PHONE_BILL_CURRENT")) {

					identificationDetail = new IdentificationDetailsVO();
					identificationDetail.setDocumentName(checklist.getAttachment());
					identificationDetail.setDocumentType(checklist.getChecklistItem());
					currentIdentificationDetails.add(identificationDetail);
					addressVO.setPresentDocuments(currentIdentificationDetails);
				}
				
				if (checklist.getChecklistItem()
						.equalsIgnoreCase("RATION_CARD_CURRENT")) {

					identificationDetail = new IdentificationDetailsVO();
					identificationDetail.setDocumentName(checklist.getAttachment());
					identificationDetail.setDocumentType(checklist.getChecklistItem());
					currentIdentificationDetails.add(identificationDetail);
					addressVO.setPresentDocuments(currentIdentificationDetails);
				}
				if (checklist.getChecklistItem()
						.equalsIgnoreCase("GAS_BILL_CURRENT")) {

					identificationDetail = new IdentificationDetailsVO();
					identificationDetail.setDocumentName(checklist.getAttachment());
					identificationDetail.setDocumentType(checklist.getChecklistItem());
					currentIdentificationDetails.add(identificationDetail);
					addressVO.setPresentDocuments(currentIdentificationDetails);
				}
				if (checklist.getChecklistItem()
						.equalsIgnoreCase("RENT_RECIPT_CURRENT")) {

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
				if (checklist.getChecklistItem()
						.equalsIgnoreCase("ELECTRICITY_BILL_PERMANENT")) {
					identificationDetail = new IdentificationDetailsVO();
					identificationDetail.setDocumentName(checklist.getAttachment());
					identificationDetail.setDocumentType(checklist.getChecklistItem());
					permenantIdentificationDetails.add(identificationDetail);
					addressVO.setPermanentDocuments(permenantIdentificationDetails);
				}
				if (checklist.getChecklistItem()
						.equalsIgnoreCase("PHONE_BILL_PERMANENT")) {
					identificationDetail = new IdentificationDetailsVO();
					identificationDetail.setDocumentName(checklist.getAttachment());
					identificationDetail.setDocumentType(checklist.getChecklistItem());
					permenantIdentificationDetails.add(identificationDetail);
					addressVO.setPermanentDocuments(permenantIdentificationDetails);
				}
				if (checklist.getChecklistItem()
						.equalsIgnoreCase("RATION_CARD_PERMANENT")) {
					identificationDetail = new IdentificationDetailsVO();
					identificationDetail.setDocumentName(checklist.getAttachment());
					identificationDetail.setDocumentType(checklist.getChecklistItem());
					permenantIdentificationDetails.add(identificationDetail);
					addressVO.setPermanentDocuments(permenantIdentificationDetails);
				}
				
				if (checklist.getChecklistItem()
						.equalsIgnoreCase("GAS_BILL_PERMANENT")) {
					identificationDetail = new IdentificationDetailsVO();
					identificationDetail.setDocumentName(checklist.getAttachment());
					identificationDetail.setDocumentType(checklist.getChecklistItem());
					permenantIdentificationDetails.add(identificationDetail);
					addressVO.setPermanentDocuments(permenantIdentificationDetails);
				}
				
				if (checklist.getChecklistItem()
						.equalsIgnoreCase("RENT_RECIPT_PERMANENT")) {
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
		MasterCountry country = personalDetailsProcessorDependencies.coutryDao.findById(id).get();
		if (!HRMSHelper.isNullOrEmpty(countryVo)) {
			countryVo.setId(country.getId());
			countryVo.setCountryName(country.getCountryName());
		}
		return countryVo;
	}

	public MasterCityVO convertToCityMasterVO(Long id) {
		MasterCityVO cityVo = new MasterCityVO();
		MasterCity city = personalDetailsProcessorDependencies.cityDao.findById(id).get();
		if (!HRMSHelper.isNullOrEmpty(cityVo)) {
			cityVo.setId(city.getId());
			cityVo.setCityName(city.getCityName());
		}
		return cityVo;
	}

	public MasterStateVO convertToStateMasterVO(Long id) {
		MasterStateVO stateVo = new MasterStateVO();
		MasterState state = personalDetailsProcessorDependencies.stateDao.findById(id).get();
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
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();
		Long validCandidateId = validateAndGetCandidateId(candidateId);
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		EducationVO educationVO = null;
		Candidate candidateDetail = personalDetailsProcessorDependencies.getCandidateDao()
				.findByIdAndIsActive(validCandidateId, IHRMSConstants.isActive);
		if(!HRMSHelper.isNullOrEmpty(candidateDetail)) {
		Candidate candidate = personalDetailsProcessorDependencies.getCandidateDao()
				.findByIdAndIsActiveAndorgId(validCandidateId, IHRMSConstants.isActive, candidateDetail.getOrgId());
		if (!orgId.equals(candidate.getOrgId())) {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1521));
		}
		Employee emp = personalDetailsProcessorDependencies.employeeDAO
				.findByCandidateIdAndIsActiveAndOrgId(candidate.getId(), IHRMSConstants.isActive, orgId);
		List<CandidateChecklist> checklist = new ArrayList<>();
		if (!HRMSHelper.isNullOrEmpty(emp)) {
		if (HRMSHelper.isRolePresent(role, ERole.HR.name())) {
			checklist = personalDetailsProcessorDependencies.getChecklistDAO()
					.getCandidateWithChecklistDetailsByCandidateIdAndItemId(
							candidate.getCandidateProfessionalDetail().getId(),
							EChecklistItems.EDUCATIONAL_DETAILS.toString());

		} else {
			checklist = personalDetailsProcessorDependencies.getChecklistDAO().getCandidateWithChecklistDetailsByItemId(
					candidate.getCandidateProfessionalDetail().getId(),
					candidate.getCandidateProfessionalDetail().getDivision().getId(),
					EChecklistItems.EDUCATIONAL_DETAILS.toString());
		}

		}
		if (!HRMSHelper.isNullOrEmpty(candidate)) {
			educationVO = PersonalDetailsTransformUtils.convertToEducationVO(candidate, checklist);
		}
		}else {
			throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
		}

		// String tabName="EDUCATIONAL_DETAILS";


		response.setResponseBody(educationVO);
		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));

		log.info("Exit from getEducationalDetails method");
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

}
