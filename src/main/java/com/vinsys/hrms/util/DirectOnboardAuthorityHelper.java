package com.vinsys.hrms.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.vinsys.hrms.directonboard.vo.AddressDetailsRequestVo;
import com.vinsys.hrms.directonboard.vo.CandidateAddressVo;
import com.vinsys.hrms.directonboard.vo.CurrentEmploymentRequestVO;
import com.vinsys.hrms.directonboard.vo.DeleteFileRequestVO;
import com.vinsys.hrms.directonboard.vo.FileUploadRequestVO;
import com.vinsys.hrms.directonboard.vo.PreviousEmploymentRequestVO;
import com.vinsys.hrms.directonboard.vo.ProfileDetailsRequestVO;
import com.vinsys.hrms.employee.vo.BankDetailsVO;
import com.vinsys.hrms.employee.vo.CertificationDetailsVO;
import com.vinsys.hrms.employee.vo.EducationalDetailsVO;
import com.vinsys.hrms.employee.vo.FamilyDetailsVO;
import com.vinsys.hrms.employee.vo.HealthDetailsVO;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.master.vo.LoginEntityTypeVO;

@Service
public class DirectOnboardAuthorityHelper {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public void profileDetailsInputValidation(ProfileDetailsRequestVO request) throws HRMSException {

		if (!HRMSHelper.isNullOrEmpty(request.getMaritalStatus())
				&& !HRMSHelper.isNullOrEmpty(request.getMaritalStatus().getId())
				&& !HRMSHelper.isLongZero(request.getMaritalStatus().getId())) {

			if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getMaritalStatus().getId()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Marital Status Id");
			}

		}

		if (!HRMSHelper.isNullOrEmpty(request.getGender())) {
			if (HRMSHelper.isNullOrEmpty(request.getGender().getId())
					|| HRMSHelper.isLongZero(request.getGender().getId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Gender Id");
			}

			if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getGender().getId()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Gender Id");
			}
		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Gender");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getSalutation())
				&& !HRMSHelper.isNullOrEmpty(request.getSalutation().getId())
				&& !HRMSHelper.isLongZero(request.getSalutation().getId())) {

			if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getSalutation().getId()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Salutation Id");
			}
		}

		if (HRMSHelper.isNullOrEmpty(request.getFirstName())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " First Name");
		}

		if (!HRMSHelper.regexMatcher(request.getFirstName(), "[a-zA-Z ]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " First Name");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getMiddleName())) {
			if (!HRMSHelper.regexMatcher(request.getMiddleName(), "[a-zA-Z]{1,250}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Middle Name");
			}
		}
		if (HRMSHelper.isNullOrEmpty(request.getLastName())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Last Name");
		}

		if (!HRMSHelper.regexMatcher(request.getLastName(), "[a-zA-Z ]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Last Name");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getEmailId())) {
			if (!HRMSHelper.validateEmailFormat(request.getEmailId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Email Id");
			}
		}

		if (HRMSHelper.isNullOrEmpty(request.getDateOfBirth())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Date of Birth");
		}

		if (!HRMSHelper.validateDateFormate(request.getDateOfBirth())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Date of Birth");
		}

		if (!HRMSHelper.allow18YearEmployeee(request)) {
			throw new HRMSException(1762, ResponseCode.getResponseCodeMap().get(1762));
		}

		if (!HRMSHelper.isNullOrEmpty(request.getMobileNumber()) && !HRMSHelper.isLongZero(request.getMobileNumber())) {
			if (!HRMSHelper.regexMatcher(String.valueOf(request.getMobileNumber()), "[0-9-+ ]{10,15}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1764) + " Mobile Number ");
			}
		}

		if (!HRMSHelper.isNullOrEmpty(request.getSpouceName())) {
			if (!HRMSHelper.regexMatcher(request.getSpouceName(), "[a-zA-Z\\s]{1,250}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Spouce Name");
			}

		}

		if (!HRMSHelper.isNullOrEmpty(request.getEmploymentType())) {
			if (HRMSHelper.isNullOrEmpty(request.getEmploymentType().getId())
					|| HRMSHelper.isLongZero(request.getEmploymentType().getId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Employee Type");
			}

			if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getEmploymentType().getId()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Employee Type");
			}
		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Employee Type");

		}

		if (!HRMSHelper.isNullOrEmpty(request.getDesignation())) {
			if (HRMSHelper.isNullOrEmpty(request.getDesignation().getId())
					|| HRMSHelper.isLongZero(request.getDesignation().getId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Designation Id");
			}

			if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getDesignation().getId()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Designation Id");
			}
		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Designation");
		}

		if (HRMSHelper.isNullOrEmpty(request.getDateOfJoining())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Date of Joining");
		}

		if (!HRMSHelper.validateDateFormate(request.getDateOfJoining())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Date of Joining");
		}
		
		if (!HRMSHelper.validatedJoiningDate(request)) {
			throw new HRMSException(1767, ResponseCode.getResponseCodeMap().get(1767));
		}

//		if (HRMSHelper.isNullOrEmpty(request.getRetirementDate())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Date of Retirement");
//		}

		if (!HRMSHelper.isNullOrEmpty(request.getRetirementDate())) {
			
		if (!HRMSHelper.validateDateFormate(request.getRetirementDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Date of Retirement");
		}
		
		if (!HRMSHelper.validatedRetirementDate(request)) {
			throw new HRMSException(1768, ResponseCode.getResponseCodeMap().get(1768));
		}
		}

//		if (HRMSHelper.isNullOrEmpty(request.getNoticePeriod()) || HRMSHelper.isLongZero(request.getNoticePeriod())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Notice Period");
//		}
		if (!HRMSHelper.isNullOrEmpty(request.getNoticePeriod())) {
		if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getNoticePeriod()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Notice Period");
		}
		}

		if (HRMSHelper.isNullOrEmpty(request.getProbationPeriod())
				|| HRMSHelper.isNullOrEmpty(request.getProbationPeriod())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Probation Period");
		}
		if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getProbationPeriod()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Probation Period");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getDivision())) {
			if (HRMSHelper.isNullOrEmpty(request.getDivision().getId())
					|| HRMSHelper.isLongZero(request.getDivision().getId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Division Id");
			}

			if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getDivision().getId()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Division Id");
			}
		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Division");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getBranch())) {
			if (HRMSHelper.isNullOrEmpty(request.getBranch().getId())
					|| HRMSHelper.isLongZero(request.getBranch().getId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Branch Id");
			}

			if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getBranch().getId()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Branch Id");
			}
		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Branch");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getCity()) && !HRMSHelper.isNullOrEmpty(request.getCity().getId())
				&& !HRMSHelper.isLongZero(request.getCity().getId())) {
			if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getCity().getId()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " City Id");
			}
		}

		if (!HRMSHelper.isNullOrEmpty(request.getState()) && !HRMSHelper.isNullOrEmpty(request.getState().getId())
				&& !HRMSHelper.isLongZero(request.getState().getId())) {
			if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getState().getId()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " State Id");
			}
		}

		if (!HRMSHelper.isNullOrEmpty(request.getCountry())) {
			if (HRMSHelper.isNullOrEmpty(request.getCountry().getId())
					|| HRMSHelper.isLongZero(request.getCountry().getId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Country Id");
			}

			if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getCountry().getId()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Country Id");
			}
		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Country");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getDepartment())) {
			if (HRMSHelper.isNullOrEmpty(request.getDepartment().getId())
					|| HRMSHelper.isLongZero(request.getDepartment().getId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Department Id");
			}

			if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getDepartment().getId()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Department Id");
			}
		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Department");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getReportingManager())) {
			if (HRMSHelper.isNullOrEmpty(request.getReportingManager().getId())
					|| HRMSHelper.isLongZero(request.getReportingManager().getId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Reporting Manager");
			}

			if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getReportingManager().getId()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Reporting Manager");
			}
		} else {

		}

		if (HRMSHelper.isNullOrEmpty(request.getOfficialEmailId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Official Email Id");
		}

		if (!HRMSHelper.validateEmailFormat(request.getOfficialEmailId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Official Email Id");
		}
		if (!HRMSHelper.isNullOrEmpty(request.getOfficialMobileNumber())) {
			if (HRMSHelper.isLongZero(request.getOfficialMobileNumber())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Official Mobile Number ");
			}

			if (!HRMSHelper.regexMatcher(String.valueOf(request.getOfficialMobileNumber()), "[0-9-+ ]{10,15}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Official Mobile Number ");
			}
		}

		if (!HRMSHelper.isNullOrEmpty(request.getRoles())) {
			for (LoginEntityTypeVO role : request.getRoles()) {
				if (HRMSHelper.isNullOrEmpty(role) || HRMSHelper.isNullOrEmpty(role.getId())
						|| HRMSHelper.isLongZero(role.getId())) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Role");
				}

				if (!HRMSHelper.validateNumberFormat(String.valueOf(role.getId()))) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Role Id");
				}
			}
		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Role");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getGrade())) {
			if (HRMSHelper.isNullOrEmpty(request.getGrade().getId())
					|| HRMSHelper.isLongZero(request.getGrade().getId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Grade");
			}

			if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getGrade().getId()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Grade");
			}
		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Grade");
		}

		if (HRMSHelper.isNullOrEmpty(request.getEmployeeCode())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " EmployeeCode ");
		}

		if (!HRMSHelper.regexMatcher(request.getEmployeeCode(), "[a-zA-Z0-9\\s]{4,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " EmployeeCode ");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getSocialSecurityNo())) {
			if (!HRMSHelper.regexMatcher(request.getSocialSecurityNo(), "[0-9]{9}$")) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Invalid Social Security No");
			}
		}

	}
	
	
	public void profileDetailsInputValidationForUpdate(ProfileDetailsRequestVO request) throws HRMSException {

		if (HRMSHelper.isNullOrEmpty(request.getGender()) || HRMSHelper.isNullOrEmpty(request.getGender().getId())
				|| HRMSHelper.isLongZero(request.getGender().getId())
				|| !HRMSHelper.validateNumberFormat(String.valueOf(request.getGender().getId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Gender Id");
		}

		if (HRMSHelper.isNullOrEmpty(request.getFirstName())
				|| !HRMSHelper.regexMatcher(request.getFirstName(), "[a-zA-Z ]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " First Name");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getMiddleName())
				&& !HRMSHelper.regexMatcher(request.getMiddleName(), "[a-zA-Z]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Middle Name");
		}

		if (HRMSHelper.isNullOrEmpty(request.getLastName())
				|| !HRMSHelper.regexMatcher(request.getLastName(), "[a-zA-Z ]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Last Name");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getEmailId()) && !HRMSHelper.validateEmailFormat(request.getEmailId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Email Id");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getDateOfBirth())) {
			if (!HRMSHelper.validateDateFormate(request.getDateOfBirth())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Date of Birth");
			}
			if (!HRMSHelper.allow18YearEmployeee(request)) {
				throw new HRMSException(1762, ResponseCode.getResponseCodeMap().get(1762));
			}
		}

		if (!HRMSHelper.isNullOrEmpty(request.getMobileNumber())
				&& !HRMSHelper.regexMatcher(String.valueOf(request.getMobileNumber()), "[0-9-+ ]{10,15}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1764) + " Mobile Number");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getRetirementDate())) {
			if (!HRMSHelper.validateDateFormate(request.getRetirementDate())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Date of Retirement");
			}
			if (!HRMSHelper.validatedRetirementDate(request)) {
				throw new HRMSException(1768, ResponseCode.getResponseCodeMap().get(1768));
			}
		}

		if (HRMSHelper.isNullOrEmpty(request.getOfficialEmailId())
				|| !HRMSHelper.validateEmailFormat(request.getOfficialEmailId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Official Email Id");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getOfficialMobileNumber())
				&& (!HRMSHelper.regexMatcher(String.valueOf(request.getOfficialMobileNumber()), "[0-9-+ ]{10,15}$")
						|| HRMSHelper.isLongZero(request.getOfficialMobileNumber()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Official Mobile Number");
		}

		if (HRMSHelper.isNullOrEmpty(request.getEmployeeCode())
				|| !HRMSHelper.regexMatcher(request.getEmployeeCode(), "[a-zA-Z0-9\\s]{4,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " EmployeeCode");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getSocialSecurityNo())
				&& !HRMSHelper.regexMatcher(request.getSocialSecurityNo(), "[0-9]{9}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Invalid Social Security No");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getRoles())) {
			for (LoginEntityTypeVO role : request.getRoles()) {
				if (HRMSHelper.isNullOrEmpty(role) || HRMSHelper.isNullOrEmpty(role.getId())
						|| HRMSHelper.isLongZero(role.getId())
						|| !HRMSHelper.validateNumberFormat(String.valueOf(role.getId()))) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Role Id");
				}
			}
		}

	}

	public void saveBankValidator(BankDetailsVO bankDetails) throws HRMSException {
		if (HRMSHelper.isNullOrEmpty(bankDetails.getFullName())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Full Name");
		}

		if (HRMSHelper.isNullOrEmpty(bankDetails.getBranchLocation())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Branch Location");
		}

		if (HRMSHelper.isNullOrEmpty(bankDetails.getBankName())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bank Name");
		}
		if (HRMSHelper.isNullOrEmpty(bankDetails.getMobileNumber())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Mobile Number");
		}
		if (HRMSHelper.isNullOrEmpty(bankDetails.getAccountNumber())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Account Number");
		}
		if (HRMSHelper.isNullOrEmpty(bankDetails.getIfscCode())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " IFSC code");
		}

		if (!HRMSHelper.isNullOrEmpty(bankDetails.getFullName())
				&& !HRMSHelper.regexMatcher(bankDetails.getFullName(), "[a-zA-Z\\s]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Full Name");
		}

		if (!HRMSHelper.isNullOrEmpty(bankDetails.getBranchLocation())
				&& !HRMSHelper.regexMatcher(bankDetails.getBranchLocation(), "[a-zA-Z0-9\\s]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Branch Location");
		}

		if (!HRMSHelper.isNullOrEmpty(bankDetails.getBankName())
				&& !HRMSHelper.regexMatcher(bankDetails.getBankName(), "[a-zA-Z0-9\\s]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Bank Name");
		}
		if (!HRMSHelper.isNullOrEmpty(bankDetails.getMobileNumber())
				&& !HRMSHelper.regexMatcher(String.valueOf(bankDetails.getMobileNumber()), "^\\d{9,15}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Mobile Number");
		}
		if (!HRMSHelper.isNullOrEmpty(bankDetails.getAccountNumber())
				&& !HRMSHelper.regexMatcher(bankDetails.getAccountNumber(), "[a-zA-Z0-9]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Account Number");
		}
		if (!HRMSHelper.isNullOrEmpty(bankDetails.getIfscCode())
				&& !HRMSHelper.regexMatcher(bankDetails.getIfscCode(), "[a-zA-Z0-9]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " IFSC code");
		}
	}

	public void previousEmploymentInputValidation(PreviousEmploymentRequestVO request)
			throws HRMSException, ParseException {

		if (!HRMSHelper.isNullOrEmpty(request.getId())) {
			if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getId()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id");
			}
		}

		if (HRMSHelper.isNullOrEmpty(request.getEmployeeType().getId())
				|| HRMSHelper.isLongZero(request.getEmployeeType().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Employment Type Id");
		}

		if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getEmployeeType().getId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Employment Type Id");
		}

		if (HRMSHelper.isNullOrEmpty(request.getCompanyName())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Company Name");
		}
		if (!HRMSHelper.regexMatcher(request.getCompanyName(), "[a-zA-Z0-9\\s.]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Company Name");
		}

		if (HRMSHelper.isNullOrEmpty(request.getCompanyAddress())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Company Address");

		}
		if (!HRMSHelper.regexMatcher(request.getCompanyAddress(),
				"^[A-Za-z0-9+_/. \\n #\\[\\]@()\"?,\\\\s-]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Company Address");
		}
		if (HRMSHelper.isNullOrEmpty(request.getCity().getId()) || HRMSHelper.isLongZero(request.getCity().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " City Id");

		}

		if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getCity().getId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " City Id");
		}
		if (HRMSHelper.isNullOrEmpty(request.getState().getId()) || HRMSHelper.isLongZero(request.getState().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " State Id");

		}

		if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getCity().getId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " State Id");
		}

		if (HRMSHelper.isNullOrEmpty(request.getCountry().getId())
				|| HRMSHelper.isLongZero(request.getCountry().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Country Id");

		}

		if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getCountry().getId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Country Id");
		}

		if (HRMSHelper.isNullOrEmpty(request.getWorkFromDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Work From Date");

		}
		if (!HRMSHelper.validateDateFormate(request.getWorkFromDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Work From Date");
		}

		if (HRMSHelper.isNullOrEmpty(request.getWorkToDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Work To Date");

		}
		if (!HRMSHelper.validateDateFormate(request.getWorkToDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Work To Date");
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(IHRMSConstants.FRONT_END_DATE_FORMAT);

		Date fromDate = dateFormat.parse(request.getWorkFromDate());
		Date toDate = dateFormat.parse(request.getWorkToDate());

		if (fromDate.after(toDate)) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1502));
		}

		if (!HRMSHelper.isNullOrEmpty(request.getTotalExperience())) {
			if (!HRMSHelper.regexMatcher(request.getTotalExperience(), "[a-zA-Z0-9\\s.]{1,50}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Total Experience");
			}
		}

		if (HRMSHelper.isNullOrEmpty(request.getDesignation().getId())
				|| HRMSHelper.isLongZero(request.getDesignation().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Designation Id");
		}

		if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getDesignation().getId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Designation Id");
		}

		if (HRMSHelper.isNullOrEmpty(request.getContactPersonName())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Contact Person Name");

		}
		if (!HRMSHelper.regexMatcher(request.getContactPersonName(), "[a-zA-Z\\s]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Contact Person Name");
		}

		if (HRMSHelper.isNullOrEmpty(request.getContactPersonMobileNumber())) {
			throw new HRMSException(1501,
					ResponseCode.getResponseCodeMap().get(1501) + " Contact Person Mobile Number ");

		}

		if (!HRMSHelper.regexMatcher(String.valueOf(request.getContactPersonMobileNumber()), "[0-9-+ ]{10,15}$")) {
			throw new HRMSException(1501,
					ResponseCode.getResponseCodeMap().get(1501) + " Contact Person Mobile Number ");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getReasonForleaving())) {
			if (!HRMSHelper.regexMatcher(request.getReasonForleaving(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Reason For Leaving");
			}
		}

		if (!HRMSHelper.isNullOrEmpty(request.getIsOverseas())) {
			if (!HRMSHelper.regexMatcher(request.getIsOverseas(), "[a-zA-Z]{1,250}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Is Overseas");
			}
		}
		if (HRMSHelper.isNullOrEmpty(request.getContactPersonEmail())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Contact Person Email Id");

		}
		if (!HRMSHelper.validateEmailFormat(String.valueOf(request.getContactPersonEmail()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Contact Person Email Id");
		}

		if (!HRMSHelper.isNullOrEmpty(request.getContactPersonDesignation())) {
			if (!HRMSHelper.regexMatcher(request.getContactPersonDesignation(), "^[a-zA-Z\\s]{1,250}$")) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Contact Person Designation");
			}
		}

		if (!HRMSHelper.isNullOrEmpty(request.getPincode()) && !HRMSHelper.isLongZero(request.getPincode())) {
			if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getPincode()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Pincode");
			}
		}

		if (HRMSHelper.isNullOrEmpty(request.getCandidateId()) || HRMSHelper.isLongZero(request.getCandidateId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Candidate Id");
		}
		if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getCandidateId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Candidate Id");
		}

	}

	public void saveFamilyValidator(FamilyDetailsVO familyDetails) throws HRMSException {

		if (HRMSHelper.isNullOrEmpty(familyDetails.getFirstName())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " First Name");
		}

		if (!HRMSHelper.isNullOrEmpty(familyDetails.getFirstName())
				&& !HRMSHelper.regexMatcher(familyDetails.getFirstName(), "[a-zA-Z]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " First Name");
		}

		if (HRMSHelper.isNullOrEmpty(familyDetails.getLastName())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Last Name");
		}

		if (!HRMSHelper.isNullOrEmpty(familyDetails.getLastName())
				&& !HRMSHelper.regexMatcher(familyDetails.getLastName(), "[a-zA-Z]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Last Name");
		}

		if (!HRMSHelper.isNullOrEmpty(familyDetails.getDateOfBirth())
				&& !HRMSHelper.validateDateFormate(familyDetails.getDateOfBirth())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Date of Birth");
		}

		if (HRMSHelper.isNullOrEmpty(familyDetails.getRelationship())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Relationship");
		}

		if (!HRMSHelper.isNullOrEmpty(familyDetails.getRelationship())
				&& !HRMSHelper.regexMatcher(familyDetails.getRelationship(), "[a-zA-Z]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Relationship");
		}

		if (!HRMSHelper.isNullOrEmpty(familyDetails.getOccupation())
				&& !HRMSHelper.regexMatcher(familyDetails.getOccupation(), "[a-zA-Z0-9\\s]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Occupation");
		}

		if (HRMSHelper.isNullOrEmpty(familyDetails.getGender())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Gender");
		}

		if (HRMSHelper.isNullOrEmpty(familyDetails.getContactNo1())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Mobile Number");
		}

		if (!HRMSHelper.isNullOrEmpty(familyDetails.getGender())
				&& !HRMSHelper.regexMatcher(familyDetails.getGender(), "[a-zA-Z]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Gender");
		}

		if (!HRMSHelper.isNullOrEmpty(familyDetails.getDependent())
				&& !HRMSHelper.regexMatcher(familyDetails.getDependent(), "[a-zA-Z]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Dependent");
		}

		if (!HRMSHelper.isNullOrEmpty(familyDetails.getContactNo1())
				&& !HRMSHelper.regexMatcher(familyDetails.getContactNo1(), "^\\d{9,15}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Mobile Number");
		}

	}

	public void currentDetailsInputValidation(CurrentEmploymentRequestVO request) throws HRMSException {

		if (HRMSHelper.isNullOrEmpty(request.getId()) || HRMSHelper.isLongZero(request.getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Employee Id");
		}

		if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Employee Id");
		}
		if (HRMSHelper.isNullOrEmpty(request.getEmployeeType().getId())
				|| HRMSHelper.isLongZero(request.getEmployeeType().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Employee Type");
		}

		if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getEmployeeType().getId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Employee Type");
		}

		if (HRMSHelper.isNullOrEmpty(request.getDesignation().getId())
				|| HRMSHelper.isLongZero(request.getDesignation().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Designation Id");
		}

		if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getDesignation().getId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Designation Id");
		}

		if (HRMSHelper.isNullOrEmpty(request.getDateOfJoining())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Date of Joining");
		}

		if (!HRMSHelper.validateDateFormate(request.getDateOfJoining())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Date of Joining");
		}

		if (HRMSHelper.isNullOrEmpty(request.getDateOfRetirement())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Date of Retirement");
		}

		if (!HRMSHelper.validateDateFormate(request.getDateOfRetirement())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Date of Retirement");
		}

		if (HRMSHelper.isNullOrEmpty(request.getNoticePeriod())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Notice Period");
		}
		if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getNoticePeriod()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Notice Period");
		}

		if (HRMSHelper.isNullOrEmpty(request.getProbationPeriod())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Probation Period");
		}
		if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getProbationPeriod()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Probation Period");
		}

		if (HRMSHelper.isNullOrEmpty(request.getDivision().getId())
				|| HRMSHelper.isLongZero(request.getDivision().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Division Id");
		}

		if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getDivision().getId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Division Id");
		}

		if (HRMSHelper.isNullOrEmpty(request.getBranch().getId())
				|| HRMSHelper.isLongZero(request.getBranch().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Branch Id");
		}

		if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getBranch().getId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Branch Id");
		}

		if (HRMSHelper.isNullOrEmpty(request.getCity().getId()) || HRMSHelper.isLongZero(request.getCity().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " City Id");
		}

		if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getCity().getId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " City Id");
		}

		if (HRMSHelper.isNullOrEmpty(request.getState().getId()) || HRMSHelper.isLongZero(request.getState().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " State Id");
		}

		if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getState().getId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " State Id");
		}

		if (HRMSHelper.isNullOrEmpty(request.getCountry().getId())
				|| HRMSHelper.isLongZero(request.getCountry().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Country Id");
		}

		if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getCountry().getId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Country Id");
		}

		if (HRMSHelper.isNullOrEmpty(request.getDepartment().getId())
				|| HRMSHelper.isLongZero(request.getDepartment().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Department Id");
		}

		if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getDepartment().getId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Department Id");
		}

		if (HRMSHelper.isNullOrEmpty(request.getReportingManager().getId())
				|| HRMSHelper.isLongZero(request.getReportingManager().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Reporting Manager");
		}

		if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getReportingManager().getId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Reporting Manager");
		}

		if (HRMSHelper.isNullOrEmpty(request.getOfficialEmailId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Official Email Id");
		}

		if (!HRMSHelper.validateEmailFormat(request.getOfficialEmailId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Official Email Id");
		}
		if (!HRMSHelper.isNullOrEmpty(request.getOfficialMobileNumber())) {
			if (!HRMSHelper.regexMatcher(String.valueOf(request.getOfficialMobileNumber()), "[0-9-+ ]{10,15}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Official Mobile Number ");
			}
		}

		if (HRMSHelper.isNullOrEmpty(request.getAcnNumber())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " ACN Number");
		}
		if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getAcnNumber()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " ACN Number");
		}

		if (HRMSHelper.isNullOrEmpty(request.getCandidateId()) || HRMSHelper.isLongZero(request.getCandidateId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Candidate Id");
		}
		if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getCandidateId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Candidate Id");
		}

//		if (HRMSHelper.isNullOrEmpty(request.getRoles())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Role");
//		}
//		
//		if (HRMSHelper.isNullOrEmpty(request.getRoles())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Role");
//		}

	}

	public void saveQualificationValidator(EducationalDetailsVO educationalDetailsVo) throws HRMSException {
		if (HRMSHelper.isLongZero(educationalDetailsVo.getCandidateId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Candidate Id");
		}
		if (HRMSHelper.isNullOrEmpty(educationalDetailsVo.getCandidateId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Candidate Id");
		}
		if (HRMSHelper.isNullOrEmpty(educationalDetailsVo.getDegree())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Degree");
		}

		if (!HRMSHelper.regexMatcher(educationalDetailsVo.getDegree(), "[a-zA-Z\\s.,]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Degree");
		}

		if (!HRMSHelper.regexMatcher(educationalDetailsVo.getSubjectOfSpecialization(), "[a-zA-Z\\s.,]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Subject Of Specialization");
		}
		if (HRMSHelper.isNullOrEmpty(educationalDetailsVo.getInstituteName())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Institute Name");
		}

		if (!HRMSHelper.regexMatcher(educationalDetailsVo.getInstituteName(), "[a-zA-Z\\s.,]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Institute Name");
		}

		if (!HRMSHelper.regexMatcher(educationalDetailsVo.getModeOfEducation(), "[a-zA-Z\\s.,]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Mode Of Education");
		}
		if (HRMSHelper.isNullOrEmpty(educationalDetailsVo.getGradeDivisionPercentage())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Grade Division Percentage");
		}

		if (!HRMSHelper.regexMatcher(educationalDetailsVo.getGradeDivisionPercentage(), "[a-zA-Z0-9\\s%.+-]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Grade Division Percentage");
		}

		if (!HRMSHelper.regexMatcher(educationalDetailsVo.getAcademicAchievements(), "[a-zA-Z0-9\\s.,]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Academic Achievements");
		}
		if (HRMSHelper.isNullOrEmpty(educationalDetailsVo.getBoardUniversity())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Board University");
		}

		if (!HRMSHelper.regexMatcher(educationalDetailsVo.getBoardUniversity(), "[a-zA-Z\\s.,]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Board University");
		}
		if (HRMSHelper.isNullOrEmpty(educationalDetailsVo.getStateLocation())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " State Location");
		}

		if (!HRMSHelper.regexMatcher(educationalDetailsVo.getStateLocation(), "[a-zA-Z\\s.,]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " State Location");
		}
		if (HRMSHelper.isNullOrEmpty(educationalDetailsVo.getPassingYearMonth())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Passing YearMonth");
		}

		if (!HRMSHelper.validateDateFormate(educationalDetailsVo.getPassingYearMonth())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Passing YearMonth");
		}
	}

	public void saveCertificationValidator(CertificationDetailsVO certificationDetails) throws HRMSException {

		if (HRMSHelper.isNullOrEmpty(certificationDetails.getCertificationDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Certification Date");
		}
		if (HRMSHelper.isLongZero(certificationDetails.getCandidateId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Candidate Id");
		}
		if (HRMSHelper.isNullOrEmpty(certificationDetails.getCandidateId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Candidate Id");
		}
		if (!HRMSHelper.validateDateFormate(certificationDetails.getCertificationDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Certification Date");
		}

		if (HRMSHelper.isNullOrEmpty(certificationDetails.getCertificationName())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Certification Name ");
		}

		if (!HRMSHelper.regexMatcher(certificationDetails.getCertificationName(), "[a-zA-Z\\s]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Certification Name");
		}

		if (!HRMSHelper.isNullOrEmpty(certificationDetails.getCertificationValidityDate())) {
			if (!HRMSHelper.validateDateFormate(certificationDetails.getCertificationValidityDate())) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Certification Validity Date");
			}
		}

		if (!HRMSHelper.isNullOrEmpty(certificationDetails.getModeOfEducation())) {
			if (!HRMSHelper.regexMatcher(certificationDetails.getModeOfEducation(), "[a-zA-Z\\s.,]{1,250}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Mode Of Education");
			}
		}

		if (!HRMSHelper.isNullOrEmpty(certificationDetails.getPercentageGrade())) {
			if (!HRMSHelper.regexMatcher(certificationDetails.getPercentageGrade(), "[a-zA-Z0-9\\s%.+-]{1,250}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Percentage Grade");
			}
		}
		if (HRMSHelper.isNullOrEmpty(certificationDetails.getNameOfInstitute())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Name Of Institute");
		}

		if (!HRMSHelper.regexMatcher(certificationDetails.getNameOfInstitute(), "[a-zA-Z\\s.,]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Name Of Institute");
		}

	}

	public void saveHealthValidator(HealthDetailsVO healthDetails) throws HRMSException {
		if (!HRMSHelper.isNullOrEmpty(healthDetails.getBloodGroup())
				&& !HRMSHelper.regexMatcher(healthDetails.getBloodGroup(), "[a-zA-Z0-9\\s+-]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Blood Group");
		}
		if (!HRMSHelper.isNullOrEmpty(healthDetails.getAllergy())
				&& !HRMSHelper.regexMatcher(healthDetails.getAllergy(), "[a-zA-Z0-9\\s]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Allergy");
		}
		if (!HRMSHelper.isNullOrEmpty(healthDetails.getAllergyDiscription())
				&& !HRMSHelper.regexMatcher(healthDetails.getAllergyDiscription(), "[a-zA-Z0-9\\s]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Allergy Description");
		}
		if (!HRMSHelper.isNullOrEmpty(healthDetails.getPhysicallyHandicapped())
				&& !HRMSHelper.regexMatcher(healthDetails.getPhysicallyHandicapped(), "[a-zA-Z0-9\\s]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Physically Handicapped");
		}
		if (!HRMSHelper.isNullOrEmpty(healthDetails.getPhysicallyHandicapDiscription()) && !HRMSHelper
				.regexMatcher(healthDetails.getPhysicallyHandicapDiscription(), "[a-zA-Z0-9\\s]{1,250}$")) {
			throw new HRMSException(1501,
					ResponseCode.getResponseCodeMap().get(1501) + " Physically Handicapped Description");
		}
		if (!HRMSHelper.isNullOrEmpty(healthDetails.getSurgery())
				&& !HRMSHelper.regexMatcher(healthDetails.getSurgery(), "[a-zA-Z0-9\\s]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Surgery");
		}

		if (!HRMSHelper.isNullOrEmpty(healthDetails.getSurgeryDiscription())
				&& !HRMSHelper.regexMatcher(healthDetails.getSurgeryDiscription(), "[a-zA-Z0-9\\s]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Surgery Description");
		}
		if (!HRMSHelper.isNullOrEmpty(healthDetails.getHealthHistory())
				&& !HRMSHelper.regexMatcher(healthDetails.getHealthHistory(), "[a-zA-Z0-9\\s]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Health History");
		}
		if (!HRMSHelper.isNullOrEmpty(healthDetails.getHealthHistoryDiscription())
				&& !HRMSHelper.regexMatcher(healthDetails.getHealthHistoryDiscription(), "[a-zA-Z0-9\\s]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Health History Description");
		}
	}

	public void deleteProfileValidator(Long candidateId) throws HRMSException {
		if (HRMSHelper.isNullOrEmpty(candidateId)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1568));
		}

		if (HRMSHelper.isLongZero(candidateId)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + " Candidate Id");
		}
	}

	public void saveAddressValidator(AddressDetailsRequestVo addressVo) throws HRMSException {
		CandidateAddressVo currentAddress = addressVo.getPresentAddress();
		CandidateAddressVo permanentAddress = addressVo.getPermanentAddress();
		if (HRMSHelper.isLongZero(addressVo.getCandidateId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1201) + " Candidate Id");
		}
		if (HRMSHelper.isNullOrEmpty(addressVo.getCandidateId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1201) + " Candidate Id");
		}
		if (!HRMSHelper.isNullOrEmpty(addressVo.getCitizenship())) {
			if (!HRMSHelper.regexMatcher(addressVo.getCitizenship(), "[a-zA-Z\\s]{1,250}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Citizenship");
			}
		}
		if (!HRMSHelper.isNullOrEmpty(addressVo.getSSNumber())) {
			if (HRMSHelper.isLongZero(addressVo.getSSNumber())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " SSN Number");
			}
		}

		if (!HRMSHelper.isNullOrEmpty(addressVo.getNationality())) {
			if (!HRMSHelper.regexMatcher(addressVo.getNationality(), "[a-zA-Z\\s]{1,250}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Nationality");
			}
		}

		if (HRMSHelper.isNullOrEmpty(currentAddress.getAddressLine1())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Home Flat Details");
		}
		if (!HRMSHelper.regexMatcher(currentAddress.getAddressLine1(), "[a-zA-Z0-9/\\\\_\\-,.\\s]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Home Flat Details");
		}
		if (HRMSHelper.isNullOrEmpty(currentAddress.getStreet())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Street");
		}
		if (!HRMSHelper.regexMatcher(currentAddress.getStreet(), "[a-zA-Z0-9/\\\\_\\-,.\\s]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Street");
		}
		if (HRMSHelper.isNullOrEmpty(currentAddress.getCountry().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Country");
		}
		if (HRMSHelper.isLongZero(currentAddress.getCountry().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Country");
		}

		if (HRMSHelper.isLongZero(currentAddress.getCity().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " City");
		}
		if (HRMSHelper.isNullOrEmpty(currentAddress.getCity().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " City");
		}

		if (HRMSHelper.isLongZero(currentAddress.getState().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " State");
		}
		if (HRMSHelper.isNullOrEmpty(currentAddress.getState().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " State");
		}
		if (HRMSHelper.isNullOrEmpty(currentAddress.getPincode())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " pincode");
		}
		if (HRMSHelper.isLongZero(currentAddress.getPincode())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Pincode");
		}
		if (!HRMSHelper.isNullOrEmpty(currentAddress.getLandMark())
				&& !HRMSHelper.regexMatcher(currentAddress.getLandMark(), "[a-zA-Z0-9\\s]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " LandMark");
		}
		if (!HRMSHelper.isNullOrEmpty(currentAddress.getIsRental())) {
			if (currentAddress.getIsRental().equals(IHRMSConstants.isActive)) {
				if (HRMSHelper.isNullOrEmpty(currentAddress.getOwnerName())) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Owner Name");
				}
				if (!HRMSHelper.isNullOrEmpty(currentAddress.getOwnerName())
						&& !HRMSHelper.regexMatcher(currentAddress.getOwnerName(), "[a-zA-Z0-9\\s]{1,250}$")) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Owner Name");
				}
				if (HRMSHelper.isNullOrEmpty(currentAddress.getOwnerContact())) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Owner Mobile Number");
				}
				if (!HRMSHelper.isNullOrEmpty(currentAddress.getOwnerContact())
						&& HRMSHelper.isLongZero(currentAddress.getOwnerContact())) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Owner Mobile Number");
				}
				if (!HRMSHelper.isNullOrEmpty(currentAddress.getOwnerContact())
						&& !HRMSHelper.regexMatcher(currentAddress.getOwnerContact().toString(), "^\\d{9,15}$")) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Owner Mobile Number");
				}
				if (HRMSHelper.isNullOrEmpty(currentAddress.getOwnerAadhar())) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Owner Adharcard Number");
				}
				if (!HRMSHelper.isNullOrEmpty(currentAddress.getOwnerAadhar())
						&& !HRMSHelper.validateAdharFormat(currentAddress.getOwnerAadhar())) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Owner Adharcard Number");
				}
			}
		}

		if (HRMSHelper.isNullOrEmpty(permanentAddress.getAddressLine1())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Home Flat Details");
		}
		if (!HRMSHelper.regexMatcher(permanentAddress.getAddressLine1(), "[a-zA-Z0-9/\\\\_\\-,.\\s]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Home Flat Details");
		}
		if (HRMSHelper.isNullOrEmpty(permanentAddress.getStreet())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Street");
		}
		if (!HRMSHelper.regexMatcher(permanentAddress.getStreet(), "[a-zA-Z0-9/\\\\_\\-,.\\s]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Street");
		}
		if (!HRMSHelper.isNullOrEmpty(permanentAddress.getLandMark())) {
			if (!HRMSHelper.regexMatcher(permanentAddress.getLandMark(), "[a-zA-Z0-9\\s]{1,250}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " LandMark");
			}
		}
		if (HRMSHelper.isNullOrEmpty(permanentAddress.getCountry().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Country");
		}
		if (HRMSHelper.isLongZero(permanentAddress.getCountry().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Country");
		}

		if (HRMSHelper.isLongZero(permanentAddress.getCity().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " City");
		}
		if (HRMSHelper.isNullOrEmpty(permanentAddress.getCity().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " City");
		}

		if (HRMSHelper.isLongZero(permanentAddress.getState().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " State");
		}
		if (HRMSHelper.isNullOrEmpty(permanentAddress.getState().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " State");
		}
		if (HRMSHelper.isNullOrEmpty(permanentAddress.getPincode())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " pincode");
		}
		if (HRMSHelper.isLongZero(permanentAddress.getPincode())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Pincode");
		}

		if (!HRMSHelper.isNullOrEmpty(permanentAddress.getIsRental())) {
			if (permanentAddress.getIsRental().equals(IHRMSConstants.isActive)) {
				if (HRMSHelper.isNullOrEmpty(permanentAddress.getOwnerName())) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Owner Name");
				}
				if (!HRMSHelper.isNullOrEmpty(permanentAddress.getOwnerName())
						&& !HRMSHelper.regexMatcher(permanentAddress.getOwnerName(), "[a-zA-Z0-9\\s]{1,250}$")) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Owner Name");
				}

				if (HRMSHelper.isNullOrEmpty(permanentAddress.getOwnerContact())) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Owner Mobile Number");
				}

				if (!HRMSHelper.isNullOrEmpty(permanentAddress.getOwnerContact())
						&& !HRMSHelper.regexMatcher(permanentAddress.getOwnerContact().toString(), "^\\d{9,15}$")) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Owner Mobile Number");
				}
				if (HRMSHelper.isNullOrEmpty(permanentAddress.getOwnerAadhar())) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Owner Adharcard Number");
				}
				if (!HRMSHelper.isNullOrEmpty(permanentAddress.getOwnerAadhar())
						&& !HRMSHelper.validateAdharFormat(permanentAddress.getOwnerAadhar())) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Owner Adharcard Number");
				}
			}
		}
	}

	public void uploadFileValidator(FileUploadRequestVO request) throws HRMSException {

		if (HRMSHelper.isNullOrEmpty(request.getCandidateId()) || HRMSHelper.isLongZero(request.getCandidateId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Candidate Id");
		}
		if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getCandidateId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Candidate Id");
		}

		if (HRMSHelper.isNullOrEmpty(request.getUploadtype())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Upload Type");
		}

		if (!HRMSHelper.regexMatcher(request.getUploadtype(), "^[A-Za-z0-9_\\s]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Upload Type");
		}

		if (HRMSHelper.isNullOrEmpty(request.getDocumentName())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Document Name");

		}
		if (!HRMSHelper.regexMatcher(request.getDocumentName(), "^[A-Za-z0-9_()\\s]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Document Name");
		}

		if (HRMSHelper.isNullOrEmpty(request.getSubmited())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Submited");
		}

		if (!HRMSHelper.regexMatcher(request.getSubmited(), "[a-zA-Z]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Submited");
		}

		if (HRMSHelper.isNullOrEmpty(request.getMandatory())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Mandatory");
		}

		if (!HRMSHelper.regexMatcher(request.getMandatory(), "[a-zA-Z]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Mandatory");
		}

	}

	public void deleteFileValidator(DeleteFileRequestVO request) throws HRMSException {

		if (HRMSHelper.isNullOrEmpty(request.getCandidateId()) || HRMSHelper.isLongZero(request.getCandidateId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Candidate Id");
		}
		if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getCandidateId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Candidate Id");
		}

		if (HRMSHelper.isNullOrEmpty(request.getDocumentType())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Document Type");
		}

		/*
		 * if (!HRMSHelper.regexMatcher(request.getDocumentType(),
		 * "^[A-Za-z0-9_\\s]{1,250}$")) { throw new HRMSException(1501,
		 * ResponseCode.getResponseCodeMap().get(1501) + " Document Type"); }
		 */

		if (!HRMSHelper.isNullOrEmpty(request.getDocumentName())
				&& !HRMSHelper.regexMatcher(request.getDocumentName(), "^[A-Za-z0-9_\\s]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Document Name");
		}

	}

}
