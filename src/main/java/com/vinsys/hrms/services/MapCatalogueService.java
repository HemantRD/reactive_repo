package com.vinsys.hrms.services;

import java.util.ArrayList;
import java.util.List;

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
import com.vinsys.hrms.dao.IHRMSEmployeeSeparationDAO;
import com.vinsys.hrms.dao.IHRMSMapCatalogue;
import com.vinsys.hrms.dao.IHRMSMapEmployeeCatalogueChecklistDAO;
import com.vinsys.hrms.dao.IHRMSMapEmployeeCatalogueMappingDAO;
import com.vinsys.hrms.dao.IHRMSMasterDepartmentDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOMapCatalogue;
import com.vinsys.hrms.datamodel.VOMapEmployeeCatalogue;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeSeparationDetails;
import com.vinsys.hrms.entity.MapCatalogue;
import com.vinsys.hrms.entity.MapEmployeeCatalogue;
import com.vinsys.hrms.entity.MasterDepartment;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/catalogue")

public class MapCatalogueService {

	private static final Logger logger = LoggerFactory.getLogger(MapCatalogueService.class);

	@Autowired
	IHRMSOrganizationDAO organizationDAO;
	@Autowired
	IHRMSMasterDepartmentDAO departmentDAO;
	@Autowired
	IHRMSMapCatalogue catalogueDAO;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSMapEmployeeCatalogueMappingDAO employeeCatalogueMappingDAO;

	@Autowired
	IHRMSMapEmployeeCatalogueChecklistDAO employeeCatalogueChecklistDAO;
	@Autowired
	IHRMSEmployeeSeparationDAO seperatedEmployee;

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, produces = "application/json", consumes = "application/json")
	@ResponseBody
	public String addCatalogue(@RequestBody VOMapCatalogue request) {

		try {

			String responseMessage = "";
			MapCatalogue entity = null;
			if (request != null && request.getOrganization() != null && request.getApprover() != null
					&& request.getDepartment() != null) {

				if (request.getId() > 0) {
					logger.info("Updating Organization Catalogue ... ");
					entity = catalogueDAO.findById(request.getId()).get();

					if (entity == null) {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
					}
					responseMessage = IHRMSConstants.updatedsuccessMessage;
				} else {
					logger.info("Creating Organization Catalogue ... ");
					responseMessage = IHRMSConstants.successMessage;
					entity = new MapCatalogue();
				}

				entity = HRMSRequestTranslator.translateToCatalogueEntity(request, entity);
				MasterDepartment department = departmentDAO.findById(request.getDepartment().getId()).get();
				Employee employee = employeeDAO.findById(request.getApprover().getId()).get();
				entity.setApprover(employee);
//				entity.setOrganization(organization);
				entity.setOrgId(request.getOrganization().getId());
				entity.setDepartment(department);
				entity = catalogueDAO.save(entity);

				VOMapCatalogue model = HRMSEntityToModelMapper.converToVOMapCatalogueModel(entity);
				List<Object> listObject = new ArrayList<Object>();
				listObject.add(model);

				HRMSListResponseObject response = new HRMSListResponseObject();
				response.setListResponse(listObject);
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(responseMessage);

				return HRMSHelper.createJsonString(response);

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

	@RequestMapping(value = "/{id}", method = { RequestMethod.GET }, produces = "application/json")
	@ResponseBody
	public String getCatalogueById(@PathVariable("id") long id) {
		try {
			MapCatalogue catalogue = catalogueDAO.findById(id).get();
			if (catalogue != null) {

				VOMapCatalogue model = HRMSEntityToModelMapper.converToVOMapCatalogueModel(catalogue);
				List<Object> listObject = new ArrayList<Object>();
				listObject.add(model);

				HRMSListResponseObject response = new HRMSListResponseObject();
				response.setListResponse(listObject);
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(IHRMSConstants.successMessage);

				return HRMSHelper.createJsonString(response);
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

	@RequestMapping(method = { RequestMethod.GET }, produces = "application/json")
	@ResponseBody
	public String findAllCatalogue() {
		try {
			List<MapCatalogue> checkListCatalogues = catalogueDAO.findByisActive(IHRMSConstants.isActive);
			List<Object> listObject = new ArrayList<Object>();

			if (checkListCatalogues != null) {

				for (MapCatalogue orgChecklistItem : checkListCatalogues) {
					VOMapCatalogue model = HRMSEntityToModelMapper.converToVOMapCatalogueModel(orgChecklistItem);
					listObject.add(model);
				}

				HRMSListResponseObject response = new HRMSListResponseObject();
				response.setListResponse(listObject);
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(IHRMSConstants.successMessage);

				return HRMSHelper.createJsonString(response);
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

	@RequestMapping(value = "/{id}", method = { RequestMethod.DELETE }, produces = "application/json")
	@ResponseBody
	public String deleteCatalogueById(@PathVariable("id") long id) {
		try {
			MapCatalogue catalogue = catalogueDAO.findById(id).get();
			if (catalogue != null) {
				catalogue.setIsActive(IHRMSConstants.isNotActive);
				catalogueDAO.save(catalogue);
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

	@RequestMapping(value = "getCatalogueForEmployee/{employeeId}/{loggedInEmployeeId}", method = {
			RequestMethod.GET }, produces = "application/json")
	@ResponseBody
	public String getCatalogueForEmployee(@PathVariable("employeeId") Long employeeId,
			@PathVariable("loggedInEmployeeId") Long loggedInEmployeeId) {
		try {

			List<Object> response = new ArrayList<Object>();
			HRMSListResponseObject mainResponse = new HRMSListResponseObject();
			Employee loggedInEmpEntity = employeeDAO.findById(loggedInEmployeeId).get();
			List<MapCatalogue> cataloguelist = catalogueDAO.findByApprover(loggedInEmployeeId,
					loggedInEmpEntity.getCandidate().getLoginEntity().getOrganization().getId());

			if (employeeId == 0) {
				logger.info("Finding All ");
				if (cataloguelist != null) {
					List<MapEmployeeCatalogue> mapEmployeeCatalogue = new ArrayList<MapEmployeeCatalogue>();
					for (MapCatalogue mapcatalogue : cataloguelist) {
						List<MapEmployeeCatalogue> mapEmployeeCataloguelist = employeeCatalogueMappingDAO
								.findAllEmployeeCatalogue(mapcatalogue.getId(), IHRMSConstants.isActive);

						for (MapEmployeeCatalogue mapempcatalogue : mapEmployeeCataloguelist) {
							mapEmployeeCatalogue.add(mapempcatalogue);
						}
					}
					logger.info("Got Employee under selected catalogue");
					if (mapEmployeeCatalogue != null && !mapEmployeeCatalogue.isEmpty()) {

						for (MapEmployeeCatalogue mapEmpCatl : mapEmployeeCatalogue) {
							VOMapEmployeeCatalogue employeeCatalogueModleRO = HRMSEntityToModelMapper
									.convertToEmployeeCatalogue(mapEmpCatl);
							response.add(employeeCatalogueModleRO);
						}
					}
					logger.info("Only Getting RO Checklist");
					List<MapCatalogue> roCatalogue = catalogueDAO.findCatalogueForRO(
							loggedInEmpEntity.getCandidate().getLoginEntity().getOrganization().getId());

					List<EmployeeSeparationDetails> seperatedEmployeeList = seperatedEmployee.findAll();
					for (EmployeeSeparationDetails sepEmpl : seperatedEmployeeList) {
						Long reportingManagerID = sepEmpl.getEmployee().getEmployeeReportingManager()
								.getReporingManager().getId();
						
						if (reportingManagerID.equals(loggedInEmployeeId)) {
							for (MapCatalogue rocat : roCatalogue) {
								MapEmployeeCatalogue roCatalogueIDForEmployee = employeeCatalogueMappingDAO
										.findByEmployeeIdAndCatalogue(sepEmpl.getEmployee().getId(), rocat.getId(),
												IHRMSConstants.isActive);
								if (roCatalogueIDForEmployee != null) {
									VOMapEmployeeCatalogue employeeCatalogueModleRO = HRMSEntityToModelMapper
											.convertToEmployeeCatalogue(roCatalogueIDForEmployee);

									response.add(employeeCatalogueModleRO);
								}
							}
						}
					}

				} else {

					logger.info("Only Getting RO Checklist");
					List<MapCatalogue> roCatalogue = catalogueDAO.findCatalogueForRO(
							loggedInEmpEntity.getCandidate().getLoginEntity().getOrganization().getId());

					List<EmployeeSeparationDetails> seperatedEmployeeList = seperatedEmployee.findAll();
					for (EmployeeSeparationDetails sepEmpl : seperatedEmployeeList) {
						Long reportingManagerID = sepEmpl.getEmployee().getEmployeeReportingManager()
								.getReporingManager().getId();

						if (reportingManagerID.equals(loggedInEmployeeId)) {

							if (sepEmpl.getIsActive().equalsIgnoreCase(IHRMSConstants.isActive) && (!sepEmpl.getStatus()
									.equalsIgnoreCase(IHRMSConstants.EMPLOYEE_SEPARATION_STATUS_COMPLETED))) {
								for (MapCatalogue rocat : roCatalogue) {
									MapEmployeeCatalogue roCatalogueIDForEmployee = employeeCatalogueMappingDAO
											.findByEmployeeIdAndCatalogue(sepEmpl.getEmployee().getId(), rocat.getId(),
													IHRMSConstants.isActive);
									if (roCatalogueIDForEmployee != null) {
										VOMapEmployeeCatalogue employeeCatalogueModleRO = HRMSEntityToModelMapper
												.convertToEmployeeCatalogue(roCatalogueIDForEmployee);

										response.add(employeeCatalogueModleRO);
									}
								}
							}

						}
					}
				}
			} else {
				logger.info("Findind Specific Employee ");
				Employee resignedEmployee = employeeDAO.findById(employeeId).get();
				if (resignedEmployee == null) {
					throw new HRMSException(IHRMSConstants.EMPLOYEE_DOSENT_EXIST_CODE,
							IHRMSConstants.EMPLOYEE_DOSENT_EXIST_MESSAGE);
				}

				if (cataloguelist != null) {

					logger.info("Getting Logged in Employee Catalogue ");
					MapEmployeeCatalogue mapEmployeeCatalogue = new MapEmployeeCatalogue();
					for (MapCatalogue mapcatalogue : cataloguelist) {
						mapEmployeeCatalogue = employeeCatalogueMappingDAO.findByEmployeeIdAndCatalogue(employeeId,
								mapcatalogue.getId(), IHRMSConstants.isActive);

					}

					VOMapEmployeeCatalogue employeeCatalogueModleApprovers = HRMSEntityToModelMapper
							.convertToEmployeeCatalogue(mapEmployeeCatalogue);
					response.add(employeeCatalogueModleApprovers);

					if (resignedEmployee.getEmployeeReportingManager().getReporingManager().getId()
							.equals(loggedInEmployeeId)) {

						logger.info("Getting RO Checklist As Well ");
						List<MapCatalogue> roCatalogue = catalogueDAO.findCatalogueForRO(
								resignedEmployee.getCandidate().getLoginEntity().getOrganization().getId());
						for (MapCatalogue rocat : roCatalogue) {
							MapEmployeeCatalogue roCatalogueIDForEmployee = employeeCatalogueMappingDAO
									.findByEmployeeIdAndCatalogue(employeeId, rocat.getId(), IHRMSConstants.isActive);
							if (roCatalogueIDForEmployee != null) {
								VOMapEmployeeCatalogue employeeCatalogueModleRO = HRMSEntityToModelMapper
										.convertToEmployeeCatalogue(roCatalogueIDForEmployee);
								response.add(employeeCatalogueModleRO);
							}
						}
					}
				} else {
					logger.info("Only Getting RO Checklist");
					List<MapCatalogue> roCatalogue = catalogueDAO.findCatalogueForRO(
							resignedEmployee.getCandidate().getLoginEntity().getOrganization().getId());
					for (MapCatalogue rocat : roCatalogue) {
						MapEmployeeCatalogue roCatalogueIDForEmployee = employeeCatalogueMappingDAO
								.findByEmployeeIdAndCatalogue(employeeId, rocat.getId(), IHRMSConstants.isActive);
						if (roCatalogueIDForEmployee != null) {
							VOMapEmployeeCatalogue employeeCatalogueModleRO = HRMSEntityToModelMapper
									.convertToEmployeeCatalogue(roCatalogueIDForEmployee);
							response.add(employeeCatalogueModleRO);
						}
					}
				}

			}

			mainResponse.setListResponse(response);
			mainResponse.setResponseCode(IHRMSConstants.successCode);
			mainResponse.setResponseMessage(IHRMSConstants.successMessage);
			return HRMSHelper.createJsonString(mainResponse);

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
}
