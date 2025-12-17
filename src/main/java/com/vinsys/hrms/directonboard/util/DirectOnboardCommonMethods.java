package com.vinsys.hrms.directonboard.util;

import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.security.service.IAuthorizationService;
import com.vinsys.hrms.util.ResponseCode;

/**
 * @author Onkar
 */
@Component
public class DirectOnboardCommonMethods {
	@Autowired
	IAuthorizationService authorizationService;

	public HRMSBaseResponse getBaseResponse(String responseMessage, int responseCode, Object responseBody,
			long totalRecord) {
		HRMSBaseResponse response = new HRMSBaseResponse<>();
		response.setResponseBody(responseBody);
		response.setResponseCode(responseCode);
		response.setResponseMessage(responseMessage);
		response.setTotalRecord(totalRecord);
		return response;
	}

	public void validateRoleToFunction(String functionName, List<String> role) throws HRMSException {
		if (!authorizationService.isAuthorizedFunctionName(functionName, role)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}
	}
	
	
	public static String mapFirstName(Row row) {
		String firstName = "";
		firstName = row.getCell(IColumnPositionConstants.FIRST_NAME) != null
				? row.getCell(IColumnPositionConstants.FIRST_NAME).getStringCellValue().trim()
				: null;
 
		if (firstName != null) {
			firstName = firstName.trim();
		}
 
		return firstName;
	
	}
	
	
	public static String mapLastName(Row row) {
		String lastName = "";
		lastName = row.getCell(IColumnPositionConstants.LAST_NAME) != null
				? row.getCell(IColumnPositionConstants.LAST_NAME).getStringCellValue().trim()
				: null;
 
		if (lastName != null) {
			lastName = lastName.trim();
		}
 
		return lastName;
	
	}
	
	public static String mapMiddleName(Row row) {
		String middleName = "";
		middleName = row.getCell(IColumnPositionConstants.MIDDLE_NAME) != null
				? row.getCell(IColumnPositionConstants.MIDDLE_NAME).getStringCellValue().trim()
				: null;
 
		if (middleName != null) {
			middleName = middleName.trim();
		}
 
		return middleName;
	
	}
	
	public static String mapGender(Row row) {
		String gender = "";
		gender = row.getCell(IColumnPositionConstants.GENDER) != null
				? row.getCell(IColumnPositionConstants.GENDER).getStringCellValue().trim()
				: null;
 
		if (gender != null) {
			gender = gender.trim();
		}
 
		return gender;
	
	}
	
	
	public static String mapEmploymentType(Row row) {
		String employmentType = "";
		employmentType = row.getCell(IColumnPositionConstants.EMPLOYMENT_TYPE) != null
				? row.getCell(IColumnPositionConstants.EMPLOYMENT_TYPE).getStringCellValue().trim()
				: null;
 
		if (employmentType != null) {
			employmentType = employmentType.trim();
		}
 
		return employmentType;
	
	}
	
	
	
	public static Long mapEmploymeeCode(Row row) {
		Long employeeCode =null;
		employeeCode = row.getCell(IColumnPositionConstants.EMPLOYEE_CODE) != null
				? (long)row.getCell(IColumnPositionConstants.EMPLOYEE_CODE).getNumericCellValue()
				: null;
 
		return employeeCode;
	
	}
	
	

	public static String mapDesignation(Row row) {
		String designation = "";
		designation = row.getCell(IColumnPositionConstants.DESIGNATION) != null
				? row.getCell(IColumnPositionConstants.DESIGNATION).getStringCellValue().trim()
				: null;
 
		if (designation != null) {
			designation = designation.trim();
		}
 
		return designation;
	
	}
	
	
	
//	
//	public static Long mapBranch(Row row) {
//		Long branch = null;
//		branch = row.getCell(IColumnPositionConstants.BRANCH) != null
//				? (long)row.getCell(IColumnPositionConstants.BRANCH).getNumericCellValue()
//				: null;
// 
// 
//		return branch;
//	
//	}
	public static String mapBranch(Row row) {
		String branch = "";
		branch = row.getCell(IColumnPositionConstants.BRANCH) != null
				? row.getCell(IColumnPositionConstants.BRANCH).getStringCellValue().trim()
				: null;
 
		if (branch != null) {
			branch = branch.trim();
		}
 
		return branch;
	
	}
	
	
	public static String mapCountry(Row row) {
		String country = "";
		country = row.getCell(IColumnPositionConstants.BRANCH) != null
				? row.getCell(IColumnPositionConstants.BRANCH).getStringCellValue().trim()
				: null;
 
		if (country != null) {
			country = country.trim();
		}
 
		return country;
	
	}
	
	public static String mapCity(Row row) {
		String city = "";
		city = row.getCell(IColumnPositionConstants.CITY) != null
				? row.getCell(IColumnPositionConstants.CITY).getStringCellValue().trim()
				: null;
 
		if (city != null) {
			city = city.trim();
		}
 
		return city;
	
	}
	
	public static String mapDept(Row row) {
		String dept = "";
		dept = row.getCell(IColumnPositionConstants.DEPARTMENT) != null
				? row.getCell(IColumnPositionConstants.DEPARTMENT).getStringCellValue().trim()
				: null;
 
		if (dept != null) {
			dept = dept.trim();
		}
 
		return dept;
	
	}
	
//	public static Long mapCountry(Row row) {
//		Long country = null;
//		country = row.getCell(IColumnPositionConstants.COUNTRY) != null
//				? (long)row.getCell(IColumnPositionConstants.COUNTRY).getNumericCellValue()
//				: null;
// 
//		return country;
//	
//	}
	
	
//	public static Long mapCity(Row row) {
//		Long city = null;
//		city = row.getCell(IColumnPositionConstants.CITY) != null
//				? (long)row.getCell(IColumnPositionConstants.CITY).getNumericCellValue()
//				: null;
// 
//		
// 
//		return city;
//	
//	}
	
	
//	public static Long mapDeptartment(Row row) {
//		Long dept = null;
//		dept = row.getCell(IColumnPositionConstants.DEPARTMENT) != null
//				? (long)row.getCell(IColumnPositionConstants.DEPARTMENT).getNumericCellValue()
//				: null;
// 
//			return dept;
//	
//	}
	
	
	
	
	public static String mapState(Row row) {
		String state = "";
		state = row.getCell(IColumnPositionConstants.STATE) != null
				? row.getCell(IColumnPositionConstants.STATE).getStringCellValue().trim()
				: null;
 
		if (state != null) {
			state = state.trim();
		}
 
		return state;
	
	}
	
	public static String mapDiv(Row row) {
		String div = "";
		div = row.getCell(IColumnPositionConstants.DIVISION) != null
				? row.getCell(IColumnPositionConstants.DIVISION).getStringCellValue().trim()
				: null;
 
		if (div != null) {
			div = div.trim();
		}
 
		return div;
	
	}
	
	
	public static String mapRm(Row row) {
		String rm = "";
		rm = row.getCell(IColumnPositionConstants.REPORTING_MANAGER) != null
				? row.getCell(IColumnPositionConstants.REPORTING_MANAGER).getStringCellValue().trim()
				: null;
 
		if (rm != null) {
			rm = rm.trim();
		}
 
		return rm;
	
	}
	
	public static String mapEmail(Row row) {
		String email = "";
		email = row.getCell(IColumnPositionConstants.OFFICIAL_EMAIL_ID) != null
				? row.getCell(IColumnPositionConstants.OFFICIAL_EMAIL_ID).getStringCellValue().trim()
				: null;
 
		if (email != null) {
			email = email.trim();
		}
 
		return email;
	
	}
	
	public static Long mapMobile(Row row) {
		Long mobile = null;
		mobile = row.getCell(IColumnPositionConstants.OFFICAIL_MOBIBLE_NUMBER) != null
				? (long)row.getCell(IColumnPositionConstants.OFFICAIL_MOBIBLE_NUMBER).getNumericCellValue()
				: null;
 
		
 
		return mobile;
	
	}
	
	public static int mapProbPeriod(Row row) {
		int probPeriod = 0;
		probPeriod = row.getCell(IColumnPositionConstants.PROBATION_PERIOD) != null
				? (int)row.getCell(IColumnPositionConstants.PROBATION_PERIOD).getNumericCellValue()
				: null;
 
		
 
		return probPeriod;
	
	}
	
	
	public static int mapNoticePeriod(Row row) {
		int noticePeriod = 0;
		noticePeriod = row.getCell(IColumnPositionConstants.NOTICE_PERIOD) != null
				? (int)row.getCell(IColumnPositionConstants.NOTICE_PERIOD).getNumericCellValue()
				: null;
 
		
 
		return noticePeriod;
	
	}
	public static String mapSalutation(Row row) {
		String salutation = "";
		salutation = row.getCell(IColumnPositionConstants.SALUTATION) != null
				? row.getCell(IColumnPositionConstants.SALUTATION).getStringCellValue().trim()
				: null;
 
		if (salutation != null) {
			salutation = salutation.trim();
		}
 
		return salutation;
	
	}
	
	
	public static Date mapDoj(Row row) {
		Date doj = null;
		doj = row.getCell(IColumnPositionConstants.DATE_OF_JOINING) != null
				? row.getCell(IColumnPositionConstants.DATE_OF_JOINING).getDateCellValue()
				: null;
		return doj;
	}
	

	public static Date mapDob(Row row) {
		Date dob = null;
		dob = row.getCell(IColumnPositionConstants.DOB) != null
				? row.getCell(IColumnPositionConstants.DOB).getDateCellValue()
				: null;
		return dob;
	}
	
	
	public static String mapRole(Row row) {
		String role = "";
		role = row.getCell(IColumnPositionConstants.ROLE) != null
				? row.getCell(IColumnPositionConstants.ROLE).getStringCellValue().trim()
				: null;
 
		if (role != null) {
			role = role.trim();
		}
 
		return role;
	
	}
	
	public static String mapMarritalStatus(Row row) {
		String marritalStatus = "";
		marritalStatus = row.getCell(IColumnPositionConstants.MARRITAL_STATUS) != null
				? row.getCell(IColumnPositionConstants.MARRITAL_STATUS).getStringCellValue().trim()
				: null;
 
		if (marritalStatus != null) {
			marritalStatus = marritalStatus.trim();
		}
 
		return marritalStatus;
	
	}
	
}
