package com.vinsys.hrms.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.kra.vo.KpiMetricVo;
import com.vinsys.hrms.master.vo.BranchVO;
import com.vinsys.hrms.master.vo.CityMasterVO;
import com.vinsys.hrms.master.vo.CountryMasterVO;
import com.vinsys.hrms.master.vo.DegreeMasterVO;
import com.vinsys.hrms.master.vo.DepartmentVO;
import com.vinsys.hrms.master.vo.DesignationVO;
import com.vinsys.hrms.master.vo.DivisionVO;
import com.vinsys.hrms.master.vo.EmploymentTypeVO;
import com.vinsys.hrms.master.vo.GenderMasterVO;
import com.vinsys.hrms.master.vo.GradeMasterVo;
import com.vinsys.hrms.master.vo.MasterTitleVo;
import com.vinsys.hrms.master.vo.ModeOfEducationVO;
import com.vinsys.hrms.master.vo.ModeofSeparationReasonVO;
import com.vinsys.hrms.master.vo.ObjectivesVO;
import com.vinsys.hrms.master.vo.RelationshipMasterVO;
import com.vinsys.hrms.master.vo.StateVO;

@Service
public class MasterAuthorityHelper {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public void validateDesignation(DesignationVO designationVo) throws HRMSException {

		if (HRMSHelper.isNullOrEmpty(designationVo.getDesignationName())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Designation name ");
		}

//		if(HRMSHelper.isNullOrEmpty(designationVo.getOrganizationId())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Org Id ");
//		}
//		
//		if (HRMSHelper.isLongZero(designationVo.getOrganizationId())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Org Id ");
//		}

//		boolean designationName = HRMSHelper.regexMatcher(designationVo.getDesignationName(),
//				"^(?!\\s)(?=.{1,50}$)[A-Za-z][A-Za-z0-9.,&@#$%()\\[\\]{}|!^*\\-_=+:/?\\s']*(?<!\\s)$");
		boolean designationName = HRMSHelper.regexMatcher(designationVo.getDesignationName(),
				 "^(?!\\s)(?=.{1,100}$)[A-Za-z&.,()\\s]+(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(designationVo.getDesignationName())) {
			if (!designationName) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " designation name ");
			}
		}

		if (HRMSHelper.isNullOrEmpty(designationVo.getDesignationDescription())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Designation description");
		}

		boolean designationDescription = HRMSHelper.regexMatcher(designationVo.getDesignationDescription(),
				"^(?!\\s)(?!.*\\s$).{1,150}$");
		if (!HRMSHelper.isNullOrEmpty(designationVo.getDesignationDescription())) {
			if (!designationDescription) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " designation description");
			}
		}
	}

	public void validateDepartment(DepartmentVO departmentVo) throws HRMSException {

		if (HRMSHelper.isNullOrEmpty(departmentVo.getDepartmentName())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Department name ");
		}

//		if(HRMSHelper.isNullOrEmpty(departmentVo.getOrganizationId())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Org Id ");
//		}

//		if (HRMSHelper.isLongZero(departmentVo.getOrganizationId())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Org Id ");
//		}

//		boolean depatarmentName = HRMSHelper.regexMatcher(departmentVo.getDepartmentName(), "^(?!\\s)(?=[^\\d\\s]{1,50}$)[A-Z][^\\d\\W_]{0,49}(?<!\\s)$");
		boolean depatarmentName = HRMSHelper.regexMatcher(departmentVo.getDepartmentName(),
				"^(?!\\s)(?=.{1,50}$)[A-Za-z][A-Za-z.,&@#$%()\\[\\]{}|!^*\\-_=+:/?\\s']*(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(departmentVo.getDepartmentName())) {
			if (!depatarmentName) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Department name ");
			}
		}

		if (HRMSHelper.isNullOrEmpty(departmentVo.getDepartmentDescription())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Department description");
		}

		boolean departmentDescription = HRMSHelper.regexMatcher(departmentVo.getDepartmentDescription(),
				"^(?!\\s)(?!.*\\s$).{1,150}$");
		if (!HRMSHelper.isNullOrEmpty(departmentVo.getDepartmentDescription())) {
			if (!departmentDescription) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " department description");
			}
		}
	}

	public void validateDegree(DegreeMasterVO degreeDetails) throws HRMSException {

		if (HRMSHelper.isNullOrEmpty(degreeDetails.getDegreeName())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Degree name ");
		}

		/*
		 * if(HRMSHelper.isNullOrEmpty(degreeDetails.getDescription())) { throw new
		 * HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) +
		 * " degree description "); }
		 */

		boolean degreeName = HRMSHelper.regexMatcher(degreeDetails.getDegreeName(),
				"^(?!\\s)(?=[^\\d\\s]{1,50}$)[A-Z][^\\d\\W_]{0,49}(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(degreeDetails.getDegreeName())) {
			if (!degreeName) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Degree name ");
			}
		}

		boolean degreeDescription = HRMSHelper.regexMatcher(degreeDetails.getDescription(),
				"^(?!\\s)(?=[\\s\\S]{1,250}$)[a-zA-Z0-9.,;\\s -]+(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(degreeDetails.getDescription())) {
			if (!degreeDescription) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " degree description ");
			}
		}

	}

	public void validateRelationShipMaster(RelationshipMasterVO relationshipVO) throws HRMSException {
		if (HRMSHelper.isNullOrEmpty(relationshipVO.getRelation())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " relation name ");
		}

		boolean relationName = HRMSHelper.regexMatcher(relationshipVO.getRelation(),
				"^(?!\\s)(?=[^\\d\\s]{1,50}$)[A-Z][^\\d\\W_]{0,49}(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(relationshipVO.getRelation())) {
			if (!relationName) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " relation name ");
			}
		}

	}

	public void validateGenderVo(GenderMasterVO genderVO) throws HRMSException {

		if (HRMSHelper.isNullOrEmpty(genderVO.getGender())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " gender ");
		}

		boolean gender = HRMSHelper.regexMatcher(genderVO.getGender(),
				"^(?!\\s)(?=[^\\d\\s]{1,50}$)[A-Z][^\\d\\W_]{0,49}(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(genderVO.getGender())) {
			if (!gender) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " gender ");
			}
		}

	}

	public void validatemodeOfEducationDetailsVo(ModeOfEducationVO modeOfEducationDetails) throws HRMSException {

//		if (!HRMSHelper.regexMatcher(String.valueOf(modeOfEducationDetails.getId()), "[0-9]+")) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id ");
//			}
//
//		if (HRMSHelper.isLongZero(modeOfEducationDetails.getId())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id ");
//		}

		if (HRMSHelper.isNullOrEmpty(modeOfEducationDetails.getModeOfEducation())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Mode Of Education ");
		}

		boolean modeOfEducation = HRMSHelper.regexMatcher(modeOfEducationDetails.getModeOfEducation(),
				"^(?!\\s)(?=[\\s\\S]{1,50}$)[a-zA-Z0-9.,;\\s -]+(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(modeOfEducationDetails.getModeOfEducation())) {
			if (!modeOfEducation) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " mode Of Education ");
			}
		}

		boolean description = HRMSHelper.regexMatcher(modeOfEducationDetails.getDescription(),
				"^(?!\\s)(?=[\\s\\S]{1,250}$)[a-zA-Z0-9.,;\\s -]+(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(modeOfEducationDetails.getDescription())) {
			if (!description) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " mode Of Education Description ");
			}
		}

	}

	public void validateStateVo(StateVO state) throws HRMSException {
		if (HRMSHelper.isNullOrEmpty(state.getStateName())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " State Name ");
		}

		boolean stateName = HRMSHelper.regexMatcher(state.getStateName(),
				"^(?!\\s)(?=[\\s\\S]{1,50}$)[a-zA-Z.\\s -]+(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(state.getStateName())) {
			if (!stateName) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " State Name");
			}
		}

		boolean stateDescription = HRMSHelper.regexMatcher(state.getStateDescription(),
				"^(?!\\s)(?=[\\s\\S]{1,250}$)[a-zA-Z0-9.,;\\s -]+(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(state.getStateDescription())) {
			if (!stateDescription) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " State Description ");
			}
		}
		if (!HRMSHelper.regexMatcher(String.valueOf(state.getCountryId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Country Id ");
		}

		if (HRMSHelper.isLongZero(state.getCountryId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Country Id ");
		}

	}

	public void validateResignationReasonVo(ModeofSeparationReasonVO resignationReason) throws HRMSException {

		if (HRMSHelper.isNullOrEmpty(resignationReason.getReasonName())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Resign Name ");
		}

		boolean ReasonName = HRMSHelper.regexMatcher(resignationReason.getReasonName(),
				"^(?!\\s)(?=[\\s\\S]{1,50}$)[a-zA-Z.\\s -]+(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(resignationReason.getReasonName())) {
			if (!ReasonName) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Resign Name ");
			}
		}

//		if(HRMSHelper.isNullOrEmpty(resignationReason.getResignActionType())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Resign Action Type ");
//		}

		boolean ResignActionType = HRMSHelper.regexMatcher(resignationReason.getResignActionType(),
				"^(?!\\s)(?=[\\s\\S]{1,50}$)[a-zA-Z0-9.,;\\s -]+(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(resignationReason.getResignActionType())) {
			if (!ResignActionType) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Resign Action Type ");
			}
		}
		if (!HRMSHelper.regexMatcher(String.valueOf(resignationReason.getModeOfSeparation()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Mode Of Separation Id ");
		}

		if (HRMSHelper.isLongZero(resignationReason.getModeOfSeparation())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Mode Of Separation Id ");
		}

	}

	public void validateDivisionVo(DivisionVO division) throws HRMSException {

		if (HRMSHelper.isNullOrEmpty(division.getDivisionName())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Function Name ");
		}

		boolean divisionName = HRMSHelper.regexMatcher(division.getDivisionName(),
				 "^(?!\\s)(?=.{1,100}$)[A-Za-z&,\\s]+(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(division.getDivisionName())) {
			if (!divisionName) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Function Name ");
			}
		}

//		if (!HRMSHelper.regexMatcher(String.valueOf(division.getOrganizationId()), "[0-9]+")) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + "Organization Id ");
//			}
//
//		if (HRMSHelper.isLongZero(division.getOrganizationId())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Organization Id ");
//		}

		if (HRMSHelper.isNullOrEmpty(division.getDivisionDescription())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Function Description ");
		}
		boolean divisionDescription = HRMSHelper.regexMatcher(division.getDivisionDescription(),
				"^(?!\\s)(?!.*\\s$).{1,150}$");
		if (!HRMSHelper.isNullOrEmpty(division.getDivisionDescription())) {
			if (!divisionDescription) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Function Description ");
			}
		}

	}

	public void validateBranchVo(BranchVO branch) throws HRMSException {
		if (HRMSHelper.isNullOrEmpty(branch.getBranchName())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Branch Name ");
		}

		boolean branchName = HRMSHelper.regexMatcher(branch.getBranchName(),
				"^(?!\\s)(?=[\\s\\S]{1,50}$)[a-zA-Z.\\s -]+(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(branch.getBranchName())) {
			if (!branchName) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Branch Name ");
			}
		}
		if (HRMSHelper.isNullOrEmpty(branch.getBranchDescription())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Branch Description ");
		}

		boolean branchDescription = HRMSHelper.regexMatcher(branch.getBranchDescription(),
				"^(?!\\s)(?!.*\\s$).{1,200}$");
		if (!HRMSHelper.isNullOrEmpty(branch.getBranchDescription())) {
			if (!branchDescription) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Branch Description ");
			}
		}

//		if (!HRMSHelper.regexMatcher(String.valueOf(branch.getOrganizationId()), "[0-9]+")) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Organization Id ");
//			}
//
//		if (HRMSHelper.isLongZero(branch.getOrganizationId())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Organization Id ");
//		}

	}

	public void validateCountryVo(CountryMasterVO country) throws HRMSException {
		if (HRMSHelper.isNullOrEmpty(country.getCountryName())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1804));
		}
		boolean countryNameValid = HRMSHelper.regexMatcher(country.getCountryName(),
				"^(?!\\s)(?=[\\s\\S]{1,50}$)[a-zA-Z.\\s-]+(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(country.getCountryName())) {
			if (!countryNameValid) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Country Name ");
			}
		}
	}

	public void validateCityVo(CityMasterVO city) throws HRMSException {
		if (HRMSHelper.isNullOrEmpty(city.getCityName())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1806));
		}

		boolean cityNameValid = HRMSHelper.regexMatcher(city.getCityName(),
				"^(?!\\s)(?=[\\s\\S]{1,50}$)[a-zA-Z.\\s-]+(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(city.getCityName())) {
			if (!cityNameValid) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " City Name ");
			}
		}

		boolean cityDescValid = HRMSHelper.regexMatcher(city.getCityDescription(),
				"^(?!\\s)(?=[\\s\\S]{1,250}$)[a-zA-Z0-9.,;\\s-]+(?<!\\s)$");
		if (!HRMSHelper.isNullOrEmpty(city.getCityDescription())) {
			if (!cityDescValid) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " City Description ");
			}
		}

		if (!HRMSHelper.regexMatcher(String.valueOf(city.getStateId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " State Id ");
		}

		if (HRMSHelper.isLongZero(city.getStateId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " State Id ");
		}
	}

	public void validateKpiMetricVO(KpiMetricVo metric) throws HRMSException {
		// Validate Metric (mandatory)
		if (HRMSHelper.isNullOrEmpty(metric.getMetric())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Metric Name");
		}

		// Regex: Alphanumeric + allowed special characters (- > < = % * + /), no
		// leading/trailing spaces, 1-50 chars
		boolean isMetricValid = HRMSHelper.regexMatcher(metric.getMetric(),
				"^(?!\\s)(?=.{1,50}$)[a-zA-Z0-9 \\-><=%*+/()]+(?<!\\s)$");

		if (!isMetricValid) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Metric Name");
		}

		// Validate Description (mandatory)
		if (HRMSHelper.isNullOrEmpty(metric.getDescription())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Description");
		}

		// Regex: Alphanumeric + allowed special characters (- > < = % * + /), no
		// leading/trailing spaces, 1-250 chars
		boolean isDescValid = HRMSHelper.regexMatcher(metric.getDescription(),
				"^(?!\\s)(?=.{1,250}$)[a-zA-Z0-9 \\-><=%*+/()&]+(?<!\\s)$");

		if (!isDescValid) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Description");
		}
	}

	public void validateGradeVO(GradeMasterVo gradeVo) throws HRMSException {
		if (HRMSHelper.isNullOrEmpty(gradeVo.getGradeDescription())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Grades");
		}

//		boolean isValid = HRMSHelper.regexMatcher(gradeVo.getGradeDescription(),"^(?!\\s)(?=[\\s\\S]{1,3}$)[a-zA-Z0-9.\\s-]+(?<!\\s)$");
		boolean isValid = HRMSHelper.regexMatcher(gradeVo.getGradeDescription(), "^[0-9][a-zA-Z0-9]{0,2}$");
		if (!isValid) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Grades");
		}

		if (HRMSHelper.isNullOrEmpty(gradeVo.getCareerLevel())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Grade Description");
		}
//		if (!HRMSHelper.isNullOrEmpty(gradeVo.getCareerLevel())) {
//			boolean isCareerLevelValid = HRMSHelper.regexMatcher(
//					gradeVo.getCareerLevel(),
//					"^(?!\\s)(?=[\\s\\S]{1,50}$)[a-zA-Z0-9.\\s-]+(?<!\\s)$"
//			);
//			if (!isCareerLevelValid) {
//				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Grade Description");
//			}
//		}

		if (!HRMSHelper.isNullOrEmpty(gradeVo.getCareerLevel())) {
			boolean isCareerLevelValid = HRMSHelper.regexMatcher(gradeVo.getCareerLevel(),
					"^(?!\\s)(?!.*\\s$).{1,50}$");
			if (!isCareerLevelValid) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Grade Description");
			}
		}
	}

	public void validateEmploymentType(EmploymentTypeVO employmentTypeVO) throws HRMSException {
		if (HRMSHelper.isNullOrEmpty(employmentTypeVO.getEmploymentTypeName())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + "Employment Type Name");
		}

		boolean isValidName = HRMSHelper.regexMatcher(employmentTypeVO.getEmploymentTypeName(), "^[A-Za-z\\- ]{1,50}$");
		if (!isValidName) {
			throw new HRMSException(1502, "Employment Type name must be alphabets, spaces, or hyphens and max 50 characters");
		}

		if(HRMSHelper.isNullOrEmpty(employmentTypeVO.getEmploymentTypeDescription())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Employment Type Description");
		}
		
		boolean isValidDescription = HRMSHelper.regexMatcher(employmentTypeVO.getEmploymentTypeDescription(),
				"^(?!\\s)(?!.*\\s$).{1,200}$");
		if (!HRMSHelper.isNullOrEmpty(employmentTypeVO.getEmploymentTypeDescription())) {
			if (!isValidDescription) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Employment Type Description ");
			}
		}
	}

	public void validateMasterTitle(MasterTitleVo masterTitleVO) throws HRMSException {

		if (HRMSHelper.isNullOrEmpty(masterTitleVO.getTitle())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + " Title");
		}

		boolean validTitle = HRMSHelper.regexMatcher(masterTitleVO.getTitle(), "^[A-Za-z]{1,20}\\.?$");
		if (!validTitle) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Title");
		}
		 
		if (HRMSHelper.isNullOrEmpty(masterTitleVO.getTitleDescription())) {
		    throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + " Title Description");
		}

		boolean validDescription = HRMSHelper.regexMatcher(masterTitleVO.getTitleDescription(), "^(?!\\s)(?!.*\\s$).{1,50}$");
		if (!validDescription) {
		    throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Title Description");
		}

	}

	public void validateObjectiveVO(ObjectivesVO vo) throws HRMSException {

	    if (HRMSHelper.isNullOrEmpty(vo.getDescription())) {
	        throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1892));
	    }

	    if (vo.getDescription().length() > 10000) {
	        throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Ojectives length is cross to limit");
	    }

	    if (!HRMSHelper.regexMatcher(String.valueOf(vo.getCategoryId()), "[0-9]+")) {
	        throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Category Id ");
	    }

	    if (HRMSHelper.isLongZero(vo.getCategoryId())) {
	        throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Category Id ");
	    }

	    if (!HRMSHelper.regexMatcher(String.valueOf(vo.getSubCategoryId()), "[0-9]+")) {
	        throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " SubCategory Id ");
	    }

	    if (HRMSHelper.isLongZero(vo.getSubCategoryId())) {
	        throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " SubCategory Id ");
	    }
	}


}
