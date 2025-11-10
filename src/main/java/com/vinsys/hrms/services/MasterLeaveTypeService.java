package com.vinsys.hrms.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSMasterBranchDAO;
import com.vinsys.hrms.dao.IHRMSMasterDivisionDAO;
import com.vinsys.hrms.dao.IHRMSMasterLeaveTypeDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOMasterLeaveType;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.MasterBranch;
import com.vinsys.hrms.entity.MasterDivision;
import com.vinsys.hrms.entity.MasterLeaveType;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping(path = "/masterLeaveType")

public class MasterLeaveTypeService {

	@Autowired
	IHRMSMasterLeaveTypeDAO masterLeaveTypeDAO;

	@Autowired
	IHRMSOrganizationDAO organizationDAO;

	@Autowired
	IHRMSMasterDivisionDAO divisionDAO;

	@Autowired
	IHRMSMasterBranchDAO branchDAO;

	@Autowired
	IHRMSEmployeeDAO employeeDAO;

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String createMasterLeaveType(@RequestBody VOMasterLeaveType voMasterLeaveType) {

		MasterLeaveType masterLeaveTypeEntity;
		Organization organizationEntity;
		MasterDivision divisionEntity;
		MasterBranch branchEntity;
		String resultMesage = "";

		try {
			if (!HRMSHelper.isNullOrEmpty(voMasterLeaveType)
					&& !HRMSHelper.isNullOrEmpty(voMasterLeaveType.getOrganization().getId())
					&& !HRMSHelper.isNullOrEmpty(voMasterLeaveType.getDivision().getId())
					&& !HRMSHelper.isNullOrEmpty(voMasterLeaveType.getBranch().getId())) {

				masterLeaveTypeEntity = masterLeaveTypeDAO.findById(voMasterLeaveType.getId()).get();
				if (!HRMSHelper.isNullOrEmpty(masterLeaveTypeEntity)) {
					/* update */
					// masterDesignationEntity =
					masterLeaveTypeEntity = HRMSRequestTranslator
							.translateToMasterLeaveTypeEntity(masterLeaveTypeEntity, voMasterLeaveType);
					resultMesage = IHRMSConstants.updatedsuccessMessage;
				} else {
					/* insert */
					masterLeaveTypeEntity = new MasterLeaveType();
					organizationEntity = organizationDAO.findById(voMasterLeaveType.getOrganization().getId()).get();
					divisionEntity = divisionDAO.findById(voMasterLeaveType.getDivision().getId()).get();
					branchEntity = branchDAO.findById(voMasterLeaveType.getBranch().getId()).get();
					if (!HRMSHelper.isNullOrEmpty(organizationEntity) && !HRMSHelper.isNullOrEmpty(divisionEntity)
							&& !HRMSHelper.isNullOrEmpty(branchEntity)) {
						masterLeaveTypeEntity.setOrgId(organizationEntity.getId());
						// masterLeaveTypeEntity.setDivision(divisionEntity);
						// masterLeaveTypeEntity.setBranch(branchEntity);
						// masterDesignationEntity =
						// HRMSRequestTranslator.translateToMasterBranchEntity(masterDesignationEntity,voMasterDesignation);
						masterLeaveTypeEntity = HRMSRequestTranslator
								.translateToMasterLeaveTypeEntity(masterLeaveTypeEntity, voMasterLeaveType);
					} else {
						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.InsufficientDataMessage);
					}
					resultMesage = IHRMSConstants.addedsuccessMessage;
				}
				masterLeaveTypeDAO.save(masterLeaveTypeEntity);
				return HRMSHelper.sendSuccessResponse(resultMesage, IHRMSConstants.successCode);
			} else {
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
		} catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{orgId}/{maritalStatus}/{candidateEmploymentStatus}/{empId}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getAllMasterLeaveType(@PathVariable("orgId") long orgId,
			@PathVariable("maritalStatus") String maritalStatus,
			@PathVariable("candidateEmploymentStatus") String candidateEmploymentStatus,
			@PathVariable("empId") String empId) {

		List<MasterLeaveType> masterLeaveTypeEntityList = new ArrayList<>();
		List<Object> voMasterLeaveTypeList = new ArrayList<>();
		HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
		MasterLeaveType tempMasterLeavetype = new MasterLeaveType();
		MasterLeaveType tempMasterLeavetypePaternity = new MasterLeaveType();
		try {
			masterLeaveTypeEntityList = masterLeaveTypeDAO.findAllMasterLeaveTypeByOrgIdCustomQuery(orgId,
					IHRMSConstants.isActive /* ,divId,branchId */);
			boolean validMatrLeaveFlag = false;

			if (!HRMSHelper.isNullOrEmpty(empId) && !empId.equalsIgnoreCase("undefined")
					&& !empId.equalsIgnoreCase("null")) {

				Employee empEntity = employeeDAO.findEmpCandDOJByEmpId(IHRMSConstants.isActive, Long.parseLong(empId));
				if (!HRMSHelper.isNullOrEmpty(empId)) {
					Date doj = empEntity.getCandidate().getCandidateProfessionalDetail().getDateOfJoining();

					Long diffInDays = HRMSDateUtil.getDifferenceInDays(doj, new Date());
					String employmentType = empEntity.getCandidate().getEmploymentType().getEmploymentTypeName();
					if ((diffInDays > IHRMSConstants.MATR_LEAVE_VALID_COUNT)
							&& employmentType.equalsIgnoreCase(IHRMSConstants.CONFIRMED)) {
						validMatrLeaveFlag = true;
					}
				}
			}

			if (!HRMSHelper.isNullOrEmpty(maritalStatus) && !HRMSHelper.isNullOrEmpty(candidateEmploymentStatus)) {
				for (MasterLeaveType mlt : masterLeaveTypeEntityList) {
					if (mlt.getLeaveTypeName().trim().equalsIgnoreCase("Maternity Leave")) {
						tempMasterLeavetype = mlt;
					} else if (mlt.getLeaveTypeName().trim().equalsIgnoreCase("Paternity Leave")) {
						tempMasterLeavetypePaternity = mlt;
					}
				}
				// !maritalStatus.trim().equalsIgnoreCase("Married") ||
				if (!candidateEmploymentStatus.trim().equalsIgnoreCase("Confirmed") || !validMatrLeaveFlag) {
					masterLeaveTypeEntityList.remove(tempMasterLeavetype);
				}
//				
//				if(!gender.trim().equalsIgnoreCase("Male") ) {
//					masterLeaveTypeEntityList.remove(tempMasterLeavetypePaternity);
//				}
			}

			if (!HRMSHelper.isNullOrEmpty(masterLeaveTypeEntityList)) {
				voMasterLeaveTypeList = HRMSResponseTranslator.transalteToMasterLeaveTypeVO(masterLeaveTypeEntityList,
						voMasterLeaveTypeList);
				hrmsListResponseObject.setListResponse((List<Object>) voMasterLeaveTypeList);
				hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
				hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
				return HRMSHelper.createJsonString(hrmsListResponseObject);
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
			}
		} catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		return null;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteMasterLeaveType(@PathVariable("id") long LeaveTypeId) {

		try {
			if (!HRMSHelper.isNullOrEmpty(LeaveTypeId)) {
				masterLeaveTypeDAO.deleteById(LeaveTypeId);
				return HRMSHelper.sendSuccessResponse(IHRMSConstants.deletedsuccessMessage, IHRMSConstants.successCode);
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
			}

		} catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (EmptyResultDataAccessException exception) {
			try {
				exception.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.DataNotFoundMessage,
						IHRMSConstants.DataNotFoundCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		return null;
	}

}
