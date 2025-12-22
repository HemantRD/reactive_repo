package com.vinsys.hrms.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vinsys.hrms.datamodel.VOLoginEntityType;
import com.vinsys.hrms.employee.vo.BankDetailsVO;
import com.vinsys.hrms.employee.vo.EmployeeCurrentDetailVO;
import com.vinsys.hrms.employee.vo.IdentificationDetailsVO;
import com.vinsys.hrms.employee.vo.PreviousEmploymentDetailsVO;
import com.vinsys.hrms.entity.BankDetails;
import com.vinsys.hrms.entity.CandidateChecklist;
import com.vinsys.hrms.entity.CandidatePreviousEmployment;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeCurrentDetail;
import com.vinsys.hrms.entity.EmployeeReportingManager;
import com.vinsys.hrms.entity.LoginEntityType;
import com.vinsys.hrms.entity.MasterBranch;
import com.vinsys.hrms.entity.MasterCity;
import com.vinsys.hrms.entity.MasterCountry;
import com.vinsys.hrms.entity.MasterDepartment;
import com.vinsys.hrms.entity.MasterDesignation;
import com.vinsys.hrms.entity.MasterDivision;
import com.vinsys.hrms.entity.MasterEmploymentType;
import com.vinsys.hrms.entity.MasterState;
import com.vinsys.hrms.master.vo.BranchVO;
import com.vinsys.hrms.master.vo.DepartmentVO;
import com.vinsys.hrms.master.vo.DesignationVO;
import com.vinsys.hrms.master.vo.DivisionVO;
import com.vinsys.hrms.master.vo.EmploymentTypeVO;
import com.vinsys.hrms.master.vo.MasterCityVO;
import com.vinsys.hrms.master.vo.MasterCountryVO;
import com.vinsys.hrms.master.vo.MasterStateVO;
import com.vinsys.hrms.master.vo.ReportingOfficerVO;

/**
 * 
 * @author Akanksha
 *
 */
public class EmploymentDetailsTransformUtils {

	public static BankDetailsVO convertToBankDetailsToModel(BankDetails bank,
			List<CandidateChecklist> checklistDetails) {

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
		List<IdentificationDetailsVO> identificationDetails = new ArrayList<IdentificationDetailsVO>();

		IdentificationDetailsVO identificationDetail = new IdentificationDetailsVO();

		for (CandidateChecklist checklist : checklistDetails) {
			identificationDetail = new IdentificationDetailsVO();
			identificationDetail.setDocumentName(checklist.getAttachment());
			identificationDetail.setDocumentType(checklist.getChecklistItem());
			identificationDetails.add(identificationDetail);
		}

		bankVO.setDocuments(identificationDetails);
		return bankVO;
	}

	public static PreviousEmploymentDetailsVO convertToCandidatePreviousEmploymentModel(
			CandidatePreviousEmployment entity) {

		PreviousEmploymentDetailsVO model = null;
		if (entity != null) {

			model = new PreviousEmploymentDetailsVO();
			model.setId(entity.getId());
			model.setCompanyName(entity.getCompanyName());
			model.setCompanyAddress(entity.getCompanyAddress());
			model.setCity(convertMasterCityEntityToModel(entity.getCity()));
			model.setState(convertMasterStateEntityToModel(entity.getState()));
			model.setCountry(convertMasterCountryEntityModel(entity.getCountry()));
			model.setWorkFromDate(HRMSDateUtil.parse(entity.getFromDate().toString(), IHRMSConstants.POSTGRE_DATE_FORMAT));
			model.setWorkToDate(HRMSDateUtil.parse(entity.getToDate().toString(), IHRMSConstants.POSTGRE_DATE_FORMAT));
			model.setTotalExperience(entity.getExperience());
			model.setDesignation(converDesignationEntityModel(entity.getCandidateProfessionalDetail().getDesignation()));
			model.setContactPersonName(!HRMSHelper.isNullOrEmpty(entity.getPreviousManager()) ? entity.getPreviousManager() : null );
			model.setContactPersonMobileNumber(!HRMSHelper.isNullOrEmpty(entity.getPreviousManagerContactNumber()) ? entity.getPreviousManagerContactNumber() : null);
			model.setReasonForleaving(entity.getReasonForleaving());
			model.setIsOverseas(!HRMSHelper.isNullOrEmpty(entity.getOverseas()) ? entity.getOverseas() : null);
			model.setIsRelevant(!HRMSHelper.isNullOrEmpty(entity.getIsRelevant()) ? entity.getIsRelevant() : null);
			model.setContactPersonEmail(!HRMSHelper.isNullOrEmpty(entity.getPreviousManagerEmail())? entity.getPreviousManagerEmail() : null);
			model.setPincode(!HRMSHelper.isNullOrEmpty(entity.getPincode()) ? entity.getPincode() : null);
			model.setContactPersonDesignation(!HRMSHelper.isNullOrEmpty(entity.getContactPersonDesignation()) ? entity.getContactPersonDesignation() : null);
			model.setOrgId(entity.getOrgId());
			model.setCandidateId(entity.getCandidateProfessionalDetail().getCandidate().getId());
			model.setJobType(!HRMSHelper.isNullOrEmpty(entity.getJobType())? entity.getJobType() : null);
		}
		return model;
	}

	
	
	public static MasterCityVO convertMasterCityEntityToModel(MasterCity masterCityEntity) {
		MasterCityVO voMasterCity = null;
		if (!HRMSHelper.isNullOrEmpty(masterCityEntity)) {
			voMasterCity = new MasterCityVO();
			voMasterCity.setCityDescription(masterCityEntity.getCityDescription());
			voMasterCity.setCityName(masterCityEntity.getCityName());
			voMasterCity.setId(masterCityEntity.getId());

		}
		return voMasterCity;
	}

	public static MasterStateVO convertMasterStateEntityToModel(MasterState masterStateEntity) {
		MasterStateVO voMasterState = null;
		if (!HRMSHelper.isNullOrEmpty(masterStateEntity)) {
			voMasterState = new MasterStateVO();
			voMasterState.setId(masterStateEntity.getId());
			voMasterState.setStateDescription(masterStateEntity.getStateDescription());
			voMasterState.setStateName(masterStateEntity.getStateName());
		}
		return voMasterState;
	}

	private static MasterCountryVO convertMasterCountryEntityModel(MasterCountry entity) {

		MasterCountryVO model = null;
		if (!HRMSHelper.isNullOrEmpty(entity)) {

			model = new MasterCountryVO();
			model.setId(entity.getId());
			model.setCountryDescription(entity.getCountryDescription());
			model.setCountryName(entity.getCountryName());

		}
		return model;
	}

	private static EmploymentTypeVO converEmploymentEntityModel(MasterEmploymentType entity) {
		EmploymentTypeVO model = null;
		if (!HRMSHelper.isNullOrEmpty(entity)) {
			model = new EmploymentTypeVO();
			model.setId(entity.getId());
			model.setEmploymentTypeName(entity.getEmploymentTypeName());
			model.setEmploymentTypeDescription(entity.getEmploymentTypeDescription());
		}
		return model;
	}

	private static BranchVO converBranchEntityModel(MasterBranch entity) {
		BranchVO model = null;
		if (!HRMSHelper.isNullOrEmpty(entity)) {
			model = new BranchVO();
			model.setId(entity.getId());
			model.setBranchName(entity.getBranchName());
			model.setBranchDescription(entity.getBranchDescription());
		}
		return model;
	}

	private static DivisionVO converDivisionEntityModel(MasterDivision entity) {
		DivisionVO model = null;
		if (!HRMSHelper.isNullOrEmpty(entity)) {
			model = new DivisionVO();
			model.setId(entity.getId());
			model.setDivisionName(entity.getDivisionName());
			model.setDivisionDescription(entity.getDivisionDescription());
		}
		return model;
	}

	private static DepartmentVO converDepartmentEntityModel(MasterDepartment entity) {
		DepartmentVO model = null;
		if (!HRMSHelper.isNullOrEmpty(entity)) {
			model = new DepartmentVO();
			model.setId(entity.getId());
			model.setDepartmentName(entity.getDepartmentName());
			model.setDepartmentDescription(entity.getDepartmentDescription());
		}
		return model;
	}

	private static DesignationVO converDesignationEntityModel(MasterDesignation entity) {
		DesignationVO model = null;
		if (!HRMSHelper.isNullOrEmpty(entity)) {
			model = new DesignationVO();
			model.setId(entity.getId());
			model.setDesignationName(entity.getDesignationName());
			model.setDesignationDescription(entity.getDesignationDescription());
		}
		return model;
	}

	private static ReportingOfficerVO converReportingManagerEntityModel(EmployeeReportingManager entity) {
		ReportingOfficerVO model = null;
		if (!HRMSHelper.isNullOrEmpty(entity)) {
			model = new ReportingOfficerVO();
			model.setId(entity.getReporingManager().getId());
			model.setReportingOfficerName(entity.getReporingManager().getCandidate().getFirstName() + " "
					+ entity.getReporingManager().getCandidate().getLastName());
		}
		return model;
	}
	
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

	public static EmployeeCurrentDetailVO convertToEmployeCurrentDetailModel(EmployeeCurrentDetail entity,
			Employee employee, List<String> roles) {

		EmployeeCurrentDetailVO model = null;
		if (entity != null) {
			model = new EmployeeCurrentDetailVO();
			model.setId(entity.getEmployee().getId());
			String name = entity.getEmployee().getCandidate().getFirstName() + " "
					+ entity.getEmployee().getCandidate().getLastName();
			model.setOfficialEmailId(entity.getEmployee().getOfficialEmailId());
			model.setEmpCode(entity.getEmployee().getEmployeeCode());
			model.setNoticePeriod(entity.getNoticePeriod());
			model.setEmployeeType(converEmploymentEntityModel(employee.getCandidate().getEmploymentType()));
			model.setState(convertMasterStateEntityToModel(
					employee.getCandidate().getCandidateProfessionalDetail().getState()));
			model.setCity(
					convertMasterCityEntityToModel(employee.getCandidate().getCandidateProfessionalDetail().getCity()));
			model.setProbationPeriod(!HRMSHelper.isNullOrEmpty(entity.getEmployee().getProbationPeriod())
					? Integer.valueOf(entity.getEmployee().getProbationPeriod()) : 0);

			model.setBranch(
					!HRMSHelper.isNullOrEmpty(employee.getCandidate().getCandidateProfessionalDetail().getBranch())
							? converBranchEntityModel(
									employee.getCandidate().getCandidateProfessionalDetail().getBranch())
							: null);
			model.setDivision(
					!HRMSHelper.isNullOrEmpty(employee.getCandidate().getCandidateProfessionalDetail().getDivision())
							? converDivisionEntityModel(
									employee.getCandidate().getCandidateProfessionalDetail().getDivision())
							: null);

			model.setDepartment(
					!HRMSHelper.isNullOrEmpty(employee.getCandidate().getCandidateProfessionalDetail().getDepartment())
							? converDepartmentEntityModel(
									employee.getCandidate().getCandidateProfessionalDetail().getDepartment())
							: null);
			model.setAcnNumber(!HRMSHelper.isNullOrEmpty(entity.getEmployee().getEmployeeACN())
					? entity.getEmployee().getEmployeeACN().getEmpACN()
					: 0);
			model.setOfficialMobileNumber(entity.getEmployee().getCandidate().getMobileNumber());
			model.setCountry(convertMasterCountryEntityModel(
					employee.getCandidate().getCandidateProfessionalDetail().getCountry()));
			model.setDesignation(converDesignationEntityModel(
					entity.getEmployee().getCandidate().getCandidateProfessionalDetail().getDesignation()));

			model.setReportingManager(!HRMSHelper.isNullOrEmpty(entity.getEmployee().getEmployeeReportingManager())
					? converReportingManagerEntityModel(entity.getEmployee().getEmployeeReportingManager())
					: null);
			model.setDateOfRetirement(!HRMSHelper.isNullOrEmpty(entity.getRetirementDate())
					? HRMSDateUtil.format(entity.getRetirementDate(), IHRMSConstants.FRONT_END_DATE_FORMAT)
					: null);
			model.setDateOfJoining(!HRMSHelper.isNullOrEmpty(
					entity.getEmployee().getCandidate().getCandidateProfessionalDetail().getDateOfJoining())
							? HRMSDateUtil.format(entity.getEmployee().getCandidate().getCandidateProfessionalDetail()
									.getDateOfJoining(), IHRMSConstants.FRONT_END_DATE_FORMAT)
							: null);
			model.setRoles(convertToSetLoginEntityTypeModel(entity.getEmployee().getCandidate().getLoginEntity().getLoginEntityTypes()));
			model.setCandidateId(entity.getEmployee().getCandidate().getId());
			model.setEmpName(name);
		}
		return model;
	}

}
