package com.vinsys.hrms.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeExtensionDAO;
import com.vinsys.hrms.dao.IHRMSMasterExtensionTypeDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOCandidate;
import com.vinsys.hrms.datamodel.VOCandidateProfessionalDetail;
import com.vinsys.hrms.datamodel.VOEmployee;
import com.vinsys.hrms.datamodel.VOEmployeeExtension;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeExtension;
import com.vinsys.hrms.entity.MasterExtensionType;
import com.vinsys.hrms.entity.Organization;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.util.EmailSender;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping(path = "/extension")

//@PropertySource(value="${HRMSCONFIG}")
public class EmployeeExtensionService {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeExtensionService.class);

	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSEmployeeExtensionDAO extensionDAO;
	@Autowired
	IHRMSMasterExtensionTypeDAO masterExtensionDAO;
	@Autowired
	IHRMSOrganizationDAO organizationDAO;
	@Autowired
	EmailSender emailSender;

	/**
	 * This service can be utilised To Add and update Employee Extension Number
	 */
	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public String addExtension(@RequestBody VOEmployeeExtension employeeExtensionModel) {
		try {

			logger.info("------ >> Inside Add Update Extension <<------ ");
			if (!HRMSHelper.isNullOrEmpty(employeeExtensionModel)) {
				Employee employee = null;

				if (employeeExtensionModel.isEmployeeCheck()) {
					if (!HRMSHelper.isNullOrEmpty(employeeExtensionModel.getEmployee())) {
						employee = employeeDAO.findActiveEmployeeById(employeeExtensionModel.getEmployee().getId(),
								IHRMSConstants.isActive);
						if (employee == null) {
							throw new HRMSException(IHRMSConstants.DataNotFoundCode,
									IHRMSConstants.EMPLOYEE_DOSENT_EXIST_MESSAGE);
						}
					} else {
						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.InsufficientDataMessage);
					}
				}

				MasterExtensionType masterExtensionType = null;
				if (!HRMSHelper.isNullOrEmpty(employeeExtensionModel.getMasterExtensionType())) {
					masterExtensionType = masterExtensionDAO
							.findById(employeeExtensionModel.getMasterExtensionType().getId()).get();
					if (HRMSHelper.isNullOrEmpty(masterExtensionType)) {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode,
								IHRMSConstants.EXTENSION_TYPE_DOSENT_EXIST);
					}
				} else {
					throw new HRMSException(IHRMSConstants.InsufficientDataCode,
							IHRMSConstants.InsufficientDataMessage);
				}

				Organization organization = null;
				if (employeeExtensionModel.getOrganization() != null) {
					organization = organizationDAO.findById(employeeExtensionModel.getOrganization().getId()).get();
				} else {
					logger.info("Throw exception for organization not found ");
					throw new HRMSException(IHRMSConstants.DataNotFoundCode,
							IHRMSConstants.OrganizationDoesnotExistMessage);
				}

				String responseMessage = "";
				EmployeeExtension employeeExtensionEntity = null;
				if (employeeExtensionModel.getId() > 0) {
					logger.info(" Updating Employee Extension");
					responseMessage = IHRMSConstants.EMPLOYEE_EXTENSION_UPDATE_MESSAGE;
					employeeExtensionEntity = extensionDAO.findById(employeeExtensionModel.getId()).get();
					if (!HRMSHelper.isNullOrEmpty(employeeExtensionEntity)) {
						employeeExtensionEntity.setUpdatedBy(employeeExtensionModel.getUpdatedBy());
						employeeExtensionEntity.setUpdatedDate(new Date());
					} else {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.InvalidDetailsMessage);
					}

				} else {
					logger.info(" Adding Employee Extension");
					responseMessage = IHRMSConstants.EMPLOYEE_EXTENSION_ADD_MESSAGE;
					employeeExtensionEntity = new EmployeeExtension();
					employeeExtensionEntity.setCreatedBy(employeeExtensionModel.getCreatedBy());
					employeeExtensionEntity.setCreatedDate(new Date());
					employeeExtensionEntity.setIsActive(IHRMSConstants.isActive);
				}

				employeeExtensionEntity.setEmployee(employee);
				employeeExtensionEntity.setMasterExtensionType(masterExtensionType);
				employeeExtensionEntity.setExtensionNumber(employeeExtensionModel.getExtensionNumber());
				employeeExtensionEntity.setOther(employeeExtensionModel.getOther());
				employeeExtensionEntity.setOrganization(organization);
				employeeExtensionEntity.setEmployeeCheck(employeeExtensionModel.isEmployeeCheck());
				employeeExtensionEntity = extensionDAO.save(employeeExtensionEntity);

				VOEmployeeExtension employeeExtension = HRMSEntityToModelMapper
						.convertToEmployeeExtensionModel(employeeExtensionEntity);

				if (!HRMSHelper.isNullOrEmpty(employeeExtension))
					employeeExtension.setEmployee(HRMSEntityToModelMapper.convertToEmployeeBrief(employee));

				if (!employeeExtensionModel.isEmployeeCheck()) {
					VOEmployee emp = new VOEmployee();
					// Set<VOEmployeeExtension> e = new HashSet<VOEmployeeExtension>();

					VOCandidate can = new VOCandidate();
					can.setFirstName(employeeExtensionEntity.getOther());
					can.setMiddleName("");
					can.setLastName("");
					emp.setCandidate(can);
					employeeExtension.setEmployee(emp);
				}

				HRMSListResponseObject response = new HRMSListResponseObject();
				List<Object> list = new ArrayList<Object>();
				list.add(employeeExtension);
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(responseMessage);
				response.setListResponse(list);
				return HRMSHelper.createJsonString(response);
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

	/**
	 * This service will be utilised To Get All Employee Extension Number Against an
	 * organization
	 */
	@RequestMapping(value = "/findAllByOrgId/{orgId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String findAllEmployeeExtensionByOrgid(@PathVariable("orgId") long orgId) {

		try {
			List<Employee> employeeWithExtension = employeeDAO.findEmployeeOrgWiseWithExtension(orgId,
					IHRMSConstants.isActive);
			// Set<VOEmployeeExtension> extension = null;
			List<Object> list = new ArrayList<Object>();
			int count = 1;
			if (!HRMSHelper.isNullOrEmpty(employeeWithExtension)) {
				for (Employee employee : employeeWithExtension) {

					VOEmployee employeeVO = new VOEmployee();
					employeeVO.setId(employee.getId());

					VOCandidate candidate = new VOCandidate();
					candidate.setId(employee.getCandidate().getId());
					candidate.setFirstName(employee.getCandidate().getFirstName());
					candidate.setLastName(employee.getCandidate().getLastName());
					candidate.setMiddleName(employee.getCandidate().getMiddleName());

					VOCandidateProfessionalDetail profDetail = new VOCandidateProfessionalDetail();
					profDetail.setBranch(HRMSEntityToModelMapper.convertToVoMasterBranch(
							employee.getCandidate().getCandidateProfessionalDetail().getBranch()));
					profDetail.setDepartment(HRMSEntityToModelMapper.convertToVoMasterDepartment(
							employee.getCandidate().getCandidateProfessionalDetail().getDepartment()));
					candidate.setCandidateProfessionalDetail(profDetail);
					employeeVO.setCandidate(candidate);

					// employeeVO.setExtensionNumber(extensionNumber);

					Set<VOEmployeeExtension> employeeExtensionSet = new HashSet<VOEmployeeExtension>();
					for (EmployeeExtension ext : employee.getEmployeeExtensions()) {
						if (ext != null && !HRMSHelper.isNullOrEmpty(ext.getIsActive())
								&& ext.getIsActive().equalsIgnoreCase(IHRMSConstants.isActive)) {
							employeeExtensionSet.add(HRMSEntityToModelMapper.convertToEmployeeExtensionModel(ext));
						}

					}
					employeeVO.setEmployeeExtensions(employeeExtensionSet);
					employeeVO.setSerialNumber(count++);
					list.add(employeeVO);
				}
			}

			/*
			 * Finding Extension For Non Employee
			 */
			List<EmployeeExtension> otherExtensions = extensionDAO.findOtherExtension(orgId, IHRMSConstants.isActive);
			if (!HRMSHelper.isNullOrEmpty(otherExtensions)) {

				for (EmployeeExtension ext : otherExtensions) {
					logger.info(" Creating Extension Response For Other / Non employee extension ");
					if (ext != null) {

						if (ext != null && !HRMSHelper.isNullOrEmpty(ext.getIsActive())
								&& ext.getIsActive().equalsIgnoreCase(IHRMSConstants.isActive)) {

							VOEmployee emp = new VOEmployee();
							Set<VOEmployeeExtension> e = new HashSet<VOEmployeeExtension>();

							VOCandidate can = new VOCandidate();
							can.setFirstName(ext.getOther());
							can.setMiddleName("");
							can.setLastName("");
							emp.setCandidate(can);
							e.add(HRMSEntityToModelMapper.convertToEmployeeExtensionModel(ext));
							emp.setEmployeeExtensions(e);
							emp.setSerialNumber(count++);
							list.add(emp);
						}
					}
				}
			}

			if (HRMSHelper.isNullOrEmpty(list)) {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
			}

			HRMSListResponseObject response = new HRMSListResponseObject();
			response.setResponseCode(IHRMSConstants.successCode);
			response.setResponseMessage(IHRMSConstants.successMessage);
			response.setListResponse(list);

			return HRMSHelper.createJsonString(response);

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

	/**
	 * This service will be utilised To delete an Employee Extension Number
	 */
	@RequestMapping(value = "/deleteExtension/{extensionId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String deleteExtension(@PathVariable("extensionId") long extensionId) {
		EmployeeExtension employeeExtension = extensionDAO.findActiveExtensionById(extensionId,
				IHRMSConstants.isActive);
		try {
			if (!HRMSHelper.isNullOrEmpty(employeeExtension)) {
				employeeExtension.setIsActive(IHRMSConstants.isNotActive);
				employeeExtension.setUpdatedDate(new Date());
				extensionDAO.save(employeeExtension);
			} else {
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.EXTENSION_DOSENT_EXIST);
			}
			return HRMSHelper.sendSuccessResponse(IHRMSConstants.deletedsuccessMessage, IHRMSConstants.successCode);
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

	/**
	 * Testing Method
	 *//*
		 * // @RequestMapping(value = //
		 * "/testing/{start}/{length}/{search[value]}/{order[0][dir]}", method = //
		 * RequestMethod.GET, consumes = "application/json", produces = //
		 * "application/json")
		 * 
		 * @RequestMapping(value =
		 * "/testing/{start}/{length}/{search[value]}/{order[0][dir]}", method =
		 * RequestMethod.GET, produces = "application/json")
		 * 
		 * @ResponseBody public String testing(@PathVariable(name = "start", value =
		 * "0") int start,
		 * 
		 * @PathVariable(name = "length", value = "0") int length,
		 * 
		 * @PathVariable(name = "search[value]", value = "") String searchValue,
		 * 
		 * @PathVariable(name = "order[0][dir]", value = "asc") String order) {
		 * 
		 * logger.info("Testing Request Param From Method"); logger.info("start :: " +
		 * start); logger.info("length :: " + length); logger.info("searchValue :: " +
		 * searchValue); logger.info("order :: " + order);
		 * 
		 * return null; }
		 */

	@RequestMapping(value = "/addressbook/{orgId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String findAllEmployeeAddressBookByOrgid(@PathVariable("orgId") long orgId) {
		try {
			List<Employee> employeeAddressBook = employeeDAO.findEmployeeOrgWiseInfo(orgId, IHRMSConstants.isActive);
			List<Object> list = new ArrayList<Object>();
			int count = 1;
			if (!HRMSHelper.isNullOrEmpty(employeeAddressBook)) {
				for (Employee employee : employeeAddressBook) {

					VOEmployee employeeVO = new VOEmployee();
					employeeVO.setId(employee.getId());
					employeeVO.setOfficialEmailId(employee.getOfficialEmailId());
					VOCandidate candidate = new VOCandidate();
					candidate.setId(employee.getCandidate().getId());
					candidate.setFirstName(employee.getCandidate().getFirstName());
					candidate.setLastName(employee.getCandidate().getLastName());
					candidate.setMiddleName(employee.getCandidate().getMiddleName());

					VOCandidateProfessionalDetail profDetail = new VOCandidateProfessionalDetail();
					profDetail.setBranch(HRMSEntityToModelMapper.convertToVoMasterBranch(
							employee.getCandidate().getCandidateProfessionalDetail().getBranch()));
					profDetail.setDepartment(HRMSEntityToModelMapper.convertToVoMasterDepartment(
							employee.getCandidate().getCandidateProfessionalDetail().getDepartment()));
					candidate.setCandidateProfessionalDetail(profDetail);
					employeeVO.setCandidate(candidate);
					list.add(employeeVO);
				}
				if (HRMSHelper.isNullOrEmpty(list)) {
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
				}

				HRMSListResponseObject response = new HRMSListResponseObject();
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(IHRMSConstants.successMessage);
				response.setListResponse(list);

				return HRMSHelper.createJsonString(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
