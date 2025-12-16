package com.vinsys.hrms.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSMapCatalogue;
import com.vinsys.hrms.dao.IHRMSMapCatalogueChecklistItemDAO;
import com.vinsys.hrms.dao.IHRMSMapEmployeeCatalogueChecklistDAO;
import com.vinsys.hrms.dao.IHRMSMapEmployeeCatalogueMappingDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOMapCatalogueChecklistItem;
import com.vinsys.hrms.datamodel.VOMapEmployeeCatalogueChecklist;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.MapCatalogue;
import com.vinsys.hrms.entity.MapCatalogueChecklistItem;
import com.vinsys.hrms.entity.MapEmployeeCatalogue;
import com.vinsys.hrms.entity.MapEmployeeCatalogueChecklist;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping(path = "/catalogueChecklist")

public class CatalogueChecklistService {

	@Value("${base.webdav.url}")
	private String baseURL;

	@Autowired
	IHRMSOrganizationDAO organizationDAO;
	@Autowired
	IHRMSMapCatalogueChecklistItemDAO catalogueChecklistDAO;
	@Autowired
	IHRMSMapCatalogue catalogueDAO;

	@Autowired
	IHRMSMapEmployeeCatalogueChecklistDAO employeeChecklistMappingDAO;

	@Autowired
	IHRMSMapEmployeeCatalogueMappingDAO employeeCatalogueMappingDAO;

	@Autowired
	IHRMSEmployeeDAO employeeDAO;

	@Value("${rootDirectory}")
	private String rootDirectory;

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, produces = "application/json", consumes = "application/json")
	@ResponseBody
	public String addOrganizationChecklistItemService(
			@RequestBody VOMapCatalogueChecklistItem catalogueChecklistItemRequest) {

		try {
			if (catalogueChecklistItemRequest != null && catalogueChecklistItemRequest.getOrganization() != null
					&& catalogueChecklistItemRequest.getCatalogue() != null) {

				String responseMessage = "";
				MapCatalogueChecklistItem catalogueChecklist = null;
				if (catalogueChecklistItemRequest.getId() > 0) {
					catalogueChecklist = catalogueChecklistDAO.findById(catalogueChecklistItemRequest.getId()).get();
					if (catalogueChecklist == null) {
						throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
					}
					catalogueChecklist.setUpdatedBy(catalogueChecklistItemRequest.getUpdatedBy());
					catalogueChecklist.setUpdatedDate(new Date());
					responseMessage = IHRMSConstants.updatedsuccessMessage;
				} else {
					catalogueChecklist = new MapCatalogueChecklistItem();
					responseMessage = IHRMSConstants.successMessage;
				}

				catalogueChecklist = HRMSRequestTranslator
						.translateToMapOrganizationChecklistItem(catalogueChecklistItemRequest, catalogueChecklist);

				MapCatalogue checklistCatalogue = catalogueDAO
						.findById(catalogueChecklistItemRequest.getCatalogue().getId()).get();
				catalogueChecklist.setOrgId(catalogueChecklistItemRequest.getOrganization().getId());
				catalogueChecklist.setCatalogue(checklistCatalogue);

				catalogueChecklist = catalogueChecklistDAO.save(catalogueChecklist);

				catalogueChecklistItemRequest = HRMSEntityToModelMapper
						.convertToCatalogueChecklistItemsModel(catalogueChecklist);

				List<Object> listObject = new ArrayList<Object>();
				listObject.add(catalogueChecklistItemRequest);

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
	public String getCatalogueChecklistById(@PathVariable("id") long id) {
		try {
			MapCatalogueChecklistItem checklistItem = catalogueChecklistDAO.findById(id).get();
			if (checklistItem != null) {

				VOMapCatalogueChecklistItem model = HRMSEntityToModelMapper
						.convertToCatalogueChecklistItemsModel(checklistItem);
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
	public String findAllOrganizationChecklist() {
		try {
			List<MapCatalogueChecklistItem> checklistItemList = catalogueChecklistDAO
					.findByisActiveAndOrgId(IHRMSConstants.isActive, SecurityFilter.TL_CLAIMS.get().getOrgId());
			List<Object> listObject = new ArrayList<Object>();

			if (checklistItemList != null) {

				for (MapCatalogueChecklistItem entity : checklistItemList) {
					VOMapCatalogueChecklistItem model = HRMSEntityToModelMapper
							.convertToCatalogueChecklistItemsModel(entity);
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
	public String deleteOrganizationChecklistById(@PathVariable("id") long id) {
		try {
			MapCatalogueChecklistItem checklistItem = catalogueChecklistDAO.findById(id).get();
			if (checklistItem != null) {
				checklistItem.setIsActive(IHRMSConstants.isNotActive);
				catalogueChecklistDAO.save(checklistItem);
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

	/**
	 * To be utilized again with little modification
	 * 
	 */

	@RequestMapping(value = "findChecklistForEmployee/{employeeId}/{catalogueId}", method = {
			RequestMethod.GET }, produces = "application/json")
	@ResponseBody
	public String getCatalogueChecklistItemsForEmployee(@PathVariable("employeeId") Long employeeId,
			@PathVariable("catalogueId") Long catalogueId) {

		try {

			if (employeeId != 0 && catalogueId != 0) {

				HRMSListResponseObject response = new HRMSListResponseObject();
				List<Object> listObject = new ArrayList<Object>();
				Employee employeeEntity = employeeDAO.findById(employeeId).get();
				String path = rootDirectory + "/"
						+ employeeEntity.getCandidate().getLoginEntity().getOrganization().getId() + "/"
						+ employeeEntity.getCandidate().getCandidateProfessionalDetail().getDivision().getId() + "/"
						+ employeeEntity.getCandidate().getCandidateProfessionalDetail().getBranch().getId() + "/"
						+ employeeEntity.getCandidate().getId() + "/Separation/";
				// String path
				// =HRMSFileuploadUtil.directoryCreationForSeparationusingEmployeeId(baseURL,employeeEntity);
				MapEmployeeCatalogue employeeCatlogueMapping = employeeCatalogueMappingDAO
						.findByEmployeeIdAndCatalogue(employeeId, catalogueId, IHRMSConstants.isActive);
				if (employeeCatlogueMapping != null) {
					List<MapEmployeeCatalogueChecklist> employeeCheckList = employeeChecklistMappingDAO
							.findByEmployeeCatalogueMapping(employeeCatlogueMapping.getId());
					for (MapEmployeeCatalogueChecklist entity : employeeCheckList) {
						if (!HRMSHelper.isNullOrEmpty(entity.getEmployeeCatalogueMapping().getCatalogueProof())) {
							entity.getEmployeeCatalogueMapping()
									.setCatalogueProof(path + entity.getEmployeeCatalogueMapping().getCatalogueProof());
						}
						VOMapEmployeeCatalogueChecklist model = HRMSEntityToModelMapper
								.convertToEmployeeChecklistMapping(entity);
						listObject.add(model);
					}

				}
				response.setListResponse(listObject);
				response.setResponseCode(IHRMSConstants.successCode);
				response.setResponseMessage(IHRMSConstants.successMessage);
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

}
