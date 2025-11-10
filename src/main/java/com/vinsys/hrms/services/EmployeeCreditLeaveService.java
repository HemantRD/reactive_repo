package com.vinsys.hrms.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import com.vinsys.hrms.dao.IHRMSEmployeeCreditLeaveDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeLeaveDetailsDAO;
import com.vinsys.hrms.dao.IHRMSMasterLeaveType;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOCreditLeaveRequest;
import com.vinsys.hrms.datamodel.VOEmployee;
import com.vinsys.hrms.datamodel.VOEmployeeCreditLeaveDetail;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeCreditLeaveDetail;
import com.vinsys.hrms.entity.EmployeeLeaveDetail;
import com.vinsys.hrms.entity.MasterLeaveType;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSEntityToModelMapper;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping("/employeeCreditLeave")


public class EmployeeCreditLeaveService {

	@Autowired
	IHRMSEmployeeCreditLeaveDAO employeeCreditLeaveDAO;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSMasterLeaveType masterLeaveTypeDAO;
	@Autowired
	IHRMSEmployeeLeaveDetailsDAO employeeLeaveDetailsDAO;
	HRMSBaseResponse baseresponse = new HRMSBaseResponse();

	private static final Logger logger = LoggerFactory.getLogger(EmployeeCreditLeaveService.class);

	@RequestMapping(method = { RequestMethod.POST }, produces = "application/json", consumes = "application/json")
	@ResponseBody
	public String addEmployeeCreditLeave(@RequestBody VOCreditLeaveRequest voCreditLeaveRequest) {
		logger.info(" == >> To Add Employee Credit Leave << == ");
		try {
			// checking request, employees, and master leave type is not null or empty
			if (!HRMSHelper.isNullOrEmpty(voCreditLeaveRequest)
					&& !HRMSHelper.isNullOrEmpty(voCreditLeaveRequest.getEmployees())
					&& !HRMSHelper.isNullOrEmpty(voCreditLeaveRequest.getMasterLeaveType())
					&& voCreditLeaveRequest.getMasterLeaveType().getId() > 0) {
				// looping employees whose leave to be credited

				for (VOEmployee voEmployee : voCreditLeaveRequest.getEmployees()) {
					// checking if employee is not null or empty
					if (!HRMSHelper.isNullOrEmpty(voEmployee) && voEmployee.getId() > 0) {

						int year = Calendar.getInstance().get(Calendar.YEAR);
						Employee employeeEntity = employeeDAO.findById(voEmployee.getId()).get();

						MasterLeaveType masterLeaveTypeEntity = masterLeaveTypeDAO.findById(voCreditLeaveRequest.getMasterLeaveType().getId()).get();
						List<EmployeeCreditLeaveDetail> employeeCreditedLeaveDetail = employeeCreditLeaveDAO.findEmployeeCreditLeaveDetailByEmployeeIdAndLeaveType(employeeEntity.getId(),
										masterLeaveTypeEntity.getId());

						/**
						 * Changed here for overlapping
						 */

						if (employeeCreditedLeaveDetail != null && !employeeCreditedLeaveDetail.isEmpty()) {
							boolean isOverlapping = validateCreditedLeaveOverlapping(voCreditLeaveRequest,
									employeeCreditedLeaveDetail);
							if (!isOverlapping) {
								throw new HRMSException(IHRMSConstants.DataAlreadyExist,
										IHRMSConstants.CREDITED_LEAVE_OVERLAPPES_MESSAGE);
							}
						}

						// find logged in employee id
						String loggedInEmployee = null;
						if (!HRMSHelper.isNullOrEmpty(voCreditLeaveRequest.getCreditedBy())) {
							Employee loggedInEmployeeEntity = employeeDAO
									.findById(Long.parseLong(voCreditLeaveRequest.getCreditedBy())).get();
							loggedInEmployee = loggedInEmployeeEntity.getId().toString();
						}

						EmployeeCreditLeaveDetail employeeCreditLeaveDetailEntity = new EmployeeCreditLeaveDetail();
						employeeCreditLeaveDetailEntity = HRMSRequestTranslator
								.convertToEmployeeCreditLeaveDetailEntityFromVOCreditRequest(
										employeeCreditLeaveDetailEntity, voCreditLeaveRequest);
						employeeCreditLeaveDetailEntity.setEmployee(employeeEntity);
						employeeCreditLeaveDetailEntity.setMasterLeaveType(masterLeaveTypeEntity);
						employeeCreditLeaveDetailEntity.setPostedDate(new Date());
						employeeCreditLeaveDetailEntity.setCreatedDate(new Date());
						employeeCreditLeaveDetailEntity.setCreatedBy(loggedInEmployee);
						EmployeeCreditLeaveDetail employeeCreditLeaveDetailEntityResp = employeeCreditLeaveDAO
								.save(employeeCreditLeaveDetailEntity);

						AddToEmployeeLeaveDetail(employeeEntity.getId(), masterLeaveTypeEntity.getId(), year,
								voCreditLeaveRequest.getNoOfDays(), masterLeaveTypeEntity, employeeEntity);
					} else {
						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.InsufficientDataMessage);
					}
				}
				baseresponse.setResponseCode(IHRMSConstants.successCode);
				baseresponse.setResponseMessage(IHRMSConstants.addedsuccessMessage);
				return HRMSHelper.createJsonString(baseresponse);
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

	private boolean validateCreditedLeaveOverlapping(VOCreditLeaveRequest voCreditLeaveRequest,
			List<EmployeeCreditLeaveDetail> employeeCreditedLeaveDetail) throws HRMSException {

		boolean result = true;
		String fromDate = HRMSDateUtil.format(voCreditLeaveRequest.getFromDate(), IHRMSConstants.POSTGRE_DATE_FORMAT);// simpleFormat.format(employee.getCandidate().getDateOfBirth());
		String toDate = HRMSDateUtil.format(voCreditLeaveRequest.getToDate(), IHRMSConstants.POSTGRE_DATE_FORMAT);// simpleFormat.format(changePasswordRequest.getDob());

		Date start2 = HRMSDateUtil.parse(fromDate, IHRMSConstants.POSTGRE_DATE_FORMAT);
		Date end2 = HRMSDateUtil.parse(toDate, IHRMSConstants.POSTGRE_DATE_FORMAT);

		/*
		 * Calendar start1 = Calendar.getInstance(); Calendar end1 =
		 * Calendar.getInstance(); Calendar start2 = Calendar.getInstance(); Calendar
		 * end2 = Calendar.getInstance();
		 */

		/*
		 * start2.setTime(voCreditLeaveRequest.getFromDate());
		 * end2.setTime(voCreditLeaveRequest.getToDate());
		 */

		logger.info("Validating Date overlapping  :: ");
		for (EmployeeCreditLeaveDetail employeeCreditleave : employeeCreditedLeaveDetail) {

			/* Dates from DB */
			// start1.setTime(employeeCreditleave.getFromDate());
			// end1.setTime(employeeCreditleave.getToDate());

			/* Dates from Request */
			// Date start2 =voCreditLeaveRequest.getFromDate();
			// Date end2 = voCreditLeaveRequest.getToDate();

			Date start1 = employeeCreditleave.getFromDate();
			Date end1 = null;
			if (HRMSHelper.isNullOrEmpty(employeeCreditleave.getToDate()) && employeeCreditleave.getMasterLeaveType()
					.getLeaveTypeCode().equalsIgnoreCase(IHRMSConstants.COMP_OFF_MASTER_LEAVE_CODE)) {
				MasterLeaveType grantLeaveType = masterLeaveTypeDAO
						.findById(employeeCreditleave.getMasterLeaveType().getId()).get();
				if (!HRMSHelper.isNullOrEmpty(grantLeaveType)) {
					Calendar calToDate = Calendar.getInstance();
					// Calendar calToDateNew = Calendar.getInstance();
					calToDate.setTime(employeeCreditleave.getFromDate());
					calToDate.add(Calendar.DATE, grantLeaveType.getLeaveExpiry());
					employeeCreditleave.setToDate(calToDate.getTime());
					end1 = calToDate.getTime();
				}
			} else if (!HRMSHelper.isNullOrEmpty(employeeCreditleave.getToDate())) {
				end1 = employeeCreditleave.getToDate();
			} else {
				end1 = null;
			}

			if (start1.equals(start2) || start1.equals(end2) || end1.equals(end2) || end1.equals(start2)) {
				logger.info("Date Overlapped 1");
				return false;
			} else {
				if ((start1.after(start2) && start1.after(end2)) || (end1.before(start2) && end1.before(end2))) {
					logger.info("Not Overlapped 2");
					result = true;
				} else {
					if ((start1.before(start2) && end1.after(start2)) || (start1.after(start2) && end2.after(end1))
							|| (start1.after(start2) && end2.before(end1))) {
						logger.info("Date Overlapped 3");
						return false;
					} else {
						logger.info("Not Overlapped 4");
						result = true;
					}
				}
			}
		}

		return result;
	}

	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json", value = "/{id}/{year}")
	@ResponseBody
	public String deleteEmployeeCreditLeave(@PathVariable("id") long employeeCreditLeaveId,@PathVariable("year") int year) {
		try {
			if (employeeCreditLeaveId > 0) {
				EmployeeCreditLeaveDetail employeeCreditLeaveDetailEntity = employeeCreditLeaveDAO
						.findById(employeeCreditLeaveId).get();
				employeeCreditLeaveDAO.deleteById(employeeCreditLeaveId);
				// subtract credited leave
				EmployeeLeaveDetail employeeLeaveDetailEntity = employeeLeaveDetailsDAO.findEmployeeCreditedLeaveByEIDYEAR(
						employeeCreditLeaveDetailEntity.getEmployee().getId(),
						employeeCreditLeaveDetailEntity.getMasterLeaveType().getId(),
						/*Calendar.getInstance().get(Calendar.YEAR)*/year);
				if (!HRMSHelper.isNullOrEmpty(employeeLeaveDetailEntity)) {
					float leaveAvailable = employeeLeaveDetailEntity.getLeaveAvailable();
					float leaveEarned = employeeLeaveDetailEntity.getLeaveEarned();
					if (leaveAvailable - employeeCreditLeaveDetailEntity.getNoOfDays() < 0) {
						employeeLeaveDetailEntity.setLeaveAvailable(0.0f);
					} else {
						employeeLeaveDetailEntity
								.setLeaveAvailable(leaveAvailable - employeeCreditLeaveDetailEntity.getNoOfDays());
					}
					/*employeeLeaveDetailEntity
							.setLeaveEarned(leaveEarned - employeeCreditLeaveDetailEntity.getNoOfDays());*/
					employeeLeaveDetailEntity.setLeaveEarned(leaveEarned);								//Set Leave earned to understand how much leaves has been earned by employee while deleting this leaves by HR.
					employeeLeaveDetailEntity.setRemark("Deleted By HR from Credited Leave Screen");	//Set Remark when Previous year leaves deleted by HR.
					employeeLeaveDetailsDAO.save(employeeLeaveDetailEntity);
				} else {
					throw new HRMSException(IHRMSConstants.InsufficientDataCode,
							IHRMSConstants.InsufficientDataMessage);
				}

				baseresponse.setResponseCode(IHRMSConstants.successCode);
				baseresponse.setResponseMessage(IHRMSConstants.deletedsuccessMessage);
				return HRMSHelper.createJsonString(baseresponse);

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

	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value = "/{employeeId}/"
			+ "{masterLeaveTypeId}/{fromDate}/{toDate}/{year}")
	@ResponseBody
	public String findAllCreditLeaveDetailsbyFilter(@PathVariable("employeeId") long employeeId,
			@PathVariable("masterLeaveTypeId") long masterLeaveTypeId, @PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate,@PathVariable("year") int year) {
		try {
			if (!HRMSHelper.isLongZero(employeeId)) {
				EmployeeCreditLeaveDetail employeeCreditLeaveDetail = new EmployeeCreditLeaveDetail();
				employeeCreditLeaveDetail.setMasterLeaveType(new MasterLeaveType());
				employeeCreditLeaveDetail.setEmployee(new Employee());
				employeeCreditLeaveDetail.getEmployee().setId(employeeId);
				if (!HRMSHelper.isLongZero(masterLeaveTypeId)) {
					employeeCreditLeaveDetail.getMasterLeaveType().setId(masterLeaveTypeId);
				}
				if (!HRMSHelper.isNullOrEmpty(fromDate)) {
					Date fromDateUtil = HRMSDateUtil.parse(fromDate, IHRMSConstants.FRONT_END_DATE_FORMAT);
					employeeCreditLeaveDetail.setFromDate(fromDateUtil);
				}
				if (!HRMSHelper.isNullOrEmpty(toDate)) {
					Date toDateUtil = HRMSDateUtil.parse(toDate, IHRMSConstants.FRONT_END_DATE_FORMAT);
					employeeCreditLeaveDetail.setToDate(toDateUtil);
				}
				
				// Example <EmployeeCreditLeaveDetail> example =
				// Example.of(employeeCreditLeaveDetail);
				// List<EmployeeCreditLeaveDetail> employeeCreditLeaveDetailsEntityList =
				// employeeCreditLeaveDAO
				// .findAll(Example.of(employeeCreditLeaveDetail));
				
				
				/*List<EmployeeCreditLeaveDetail> employeeCreditLeaveDetailsEntityList = employeeCreditLeaveDAO
						.findAll(SpecificationFactory.specFindCredLeaveDetailsByFilter(employeeCreditLeaveDetail));*/
				
				List<EmployeeCreditLeaveDetail> employeeCreditedLeaveDetailsEntityList = employeeCreditLeaveDAO
						.findEmployeeForCreditedLeaves(employeeCreditLeaveDetail.getEmployee().getId(),
								/* employeeCreditLeaveDetail.getMasterLeaveType().getId(), */ year);      //leave type ID commented to show alltype leaves on credited leaves page (change date 06-05-2021)

				HRMSListResponseObject response = new HRMSListResponseObject();
				List<Object> objectList = new ArrayList<Object>();
				/*for (EmployeeCreditLeaveDetail employeeCreditLeaveDetailEntity : employeeCreditLeaveDetailsEntityList) {
					VOEmployeeCreditLeaveDetail voEmployeeCreditLeaveDetail = HRMSEntityToModelMapper
							.convertToEmployeeCreditLeaveDetailModel(employeeCreditLeaveDetailEntity);
					objectList.add(voEmployeeCreditLeaveDetail);
				}*/
				
				for (EmployeeCreditLeaveDetail employeeCreditLeaveDetailEntity : employeeCreditedLeaveDetailsEntityList) {
					VOEmployeeCreditLeaveDetail voEmployeeCreditLeaveDetail = HRMSEntityToModelMapper
							.convertToEmployeeCreditLeaveDetailModel(employeeCreditLeaveDetailEntity);
					objectList.add(voEmployeeCreditLeaveDetail);
				}


				response.setListResponse(objectList);
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

	public void AddToEmployeeLeaveDetail(long employeeId, long masterLeaveTypeId, int year, float leave,
			MasterLeaveType masterLeaveType, Employee employee) {
		EmployeeLeaveDetail employeeLeaveDetailEntity = employeeLeaveDetailsDAO.findEmployeeLeaveByEIDYEAR(employeeId,
				masterLeaveTypeId, year);

		if (!HRMSHelper.isNullOrEmpty(employeeLeaveDetailEntity)) {
			float leaveAvailable = employeeLeaveDetailEntity.getLeaveAvailable();
			float leaveEarned = employeeLeaveDetailEntity.getLeaveEarned();
			employeeLeaveDetailEntity.setLeaveAvailable(leaveAvailable + leave);
			employeeLeaveDetailEntity.setLeaveEarned(leaveEarned + leave);
			employeeLeaveDetailsDAO.save(employeeLeaveDetailEntity);
		} else {
			EmployeeLeaveDetail employeeLeaveDetail = new EmployeeLeaveDetail();
			employeeLeaveDetail.setLeaveAvailable(leave);
			employeeLeaveDetail.setLeaveEarned(leave);
			employeeLeaveDetail.setMasterLeaveType(masterLeaveType);
			employeeLeaveDetail.setEmployee(employee);
			employeeLeaveDetail.setYear(year);
			employeeLeaveDetailsDAO.save(employeeLeaveDetail);
		}

	}

	@RequestMapping(method = { RequestMethod.PUT }, produces = "application/json", consumes = "application/json")
	@ResponseBody
	public String updateEmployeeCreditLeave(@RequestBody VOCreditLeaveRequest voCreditLeaveRequest) {
		logger.info(" To Update Employee Credit Leave ");
		try {
			// checking request, employees, and master leave type is not null or empty
			if (!HRMSHelper.isNullOrEmpty(voCreditLeaveRequest)
					&& !HRMSHelper.isNullOrEmpty(voCreditLeaveRequest.getId())
					&& !HRMSHelper.isNullOrEmpty(voCreditLeaveRequest.getEmployees())
					&& !HRMSHelper.isNullOrEmpty(voCreditLeaveRequest.getMasterLeaveType())
					&& voCreditLeaveRequest.getMasterLeaveType().getId() > 0) {
				// looping employees whose credit leave to be edited
				for (VOEmployee voEmployee : voCreditLeaveRequest.getEmployees()) {
					// checking if employee is not null or empty
					if (!HRMSHelper.isNullOrEmpty(voEmployee) && voEmployee.getId() > 0) {
						int year = Calendar.getInstance().get(Calendar.YEAR);
						float oldNoOfDays = 0.0f;
						Employee employeeEntity = employeeDAO.findById(voEmployee.getId()).get();
						MasterLeaveType masterLeaveTypeEntity = masterLeaveTypeDAO
								.findById(voCreditLeaveRequest.getMasterLeaveType().getId()).get();
												
						EmployeeCreditLeaveDetail employeeCreditLeaveDetailEntity = employeeCreditLeaveDAO
								.findById(voCreditLeaveRequest.getId()).get();
						logger.info(voCreditLeaveRequest.getId()+" "+employeeCreditLeaveDetailEntity.getLeaveAvailable()+" "+employeeCreditLeaveDetailEntity.getNoOfDays());
						
						List<EmployeeCreditLeaveDetail> employeeCreditedLeaveDetail = employeeCreditLeaveDAO
								.findEmployeeCreditLeaveDetailByEmployeeIdAndLeaveTypeExceptCreditLeaveId(
										employeeEntity.getId(), masterLeaveTypeEntity.getId(),
										employeeCreditLeaveDetailEntity.getId());
						
						if (employeeCreditedLeaveDetail != null && !employeeCreditedLeaveDetail.isEmpty()) {
							boolean isOverlapping = validateCreditedLeaveOverlapping(voCreditLeaveRequest,
									employeeCreditedLeaveDetail);
							if (!isOverlapping) {
								throw new HRMSException(IHRMSConstants.DataAlreadyExist,
										IHRMSConstants.CREDITED_LEAVE_OVERLAPPES_MESSAGE);
							}
						}

						/*
						 * old no of days to considered for logic : need to subtract old no of days from
						 * existing employee leave details and add new no of days
						 */
						oldNoOfDays = employeeCreditLeaveDetailEntity.getNoOfDays();
						employeeCreditLeaveDetailEntity = HRMSRequestTranslator
								.convertToEmployeeCreditLeaveDetailEntityFromVOCreditRequest(
										employeeCreditLeaveDetailEntity, voCreditLeaveRequest);
						employeeCreditLeaveDetailEntity.setEmployee(employeeEntity);
						employeeCreditLeaveDetailEntity.setPostedDate(new Date());
						employeeCreditLeaveDetailEntity.setMasterLeaveType(masterLeaveTypeEntity);
						EmployeeCreditLeaveDetail employeeCreditLeaveDetailEntityResp = employeeCreditLeaveDAO
								.save(employeeCreditLeaveDetailEntity);

						/*
						 * private function to update employee leave detail
						 */
						if (!HRMSHelper.isNullOrEmpty(employeeCreditLeaveDetailEntityResp)) {
							updateEmployeeLeaveDetail(year, oldNoOfDays, employeeCreditLeaveDetailEntity.getNoOfDays(),
									masterLeaveTypeEntity, employeeEntity);
						}
					} else {
						throw new HRMSException(IHRMSConstants.InsufficientDataCode,
								IHRMSConstants.InsufficientDataMessage);
					}
				}
				baseresponse.setResponseCode(IHRMSConstants.successCode);
				baseresponse.setResponseMessage(IHRMSConstants.updatedsuccessMessage);
				return HRMSHelper.createJsonString(baseresponse);
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

	private void updateEmployeeLeaveDetail(int year, float oldNoOfDaysleave, float updatedNoOfDays,
			MasterLeaveType masterLeaveType, Employee employee) {
		EmployeeLeaveDetail employeeLeaveDetailEntity = employeeLeaveDetailsDAO
				.findEmployeeLeaveByEIDYEAR(employee.getId(), masterLeaveType.getId(), year);
		if (!HRMSHelper.isNullOrEmpty(employeeLeaveDetailEntity)) {
			//logger.info(employeeLeaveDetailEntity.getLeaveAvailable()+" "+employeeLeaveDetailEntity.getLeaveEarned());
			float leaveAvailable = employeeLeaveDetailEntity.getLeaveAvailable();
			//for checking null pointer exception
			float leaveEarned = HRMSHelper.isNullOrEmpty(employeeLeaveDetailEntity.getLeaveEarned())?0:employeeLeaveDetailEntity.getLeaveEarned();
			logger.info(leaveAvailable+" "+leaveEarned);
			employeeLeaveDetailEntity.setLeaveEarned(leaveEarned - oldNoOfDaysleave + updatedNoOfDays);
			logger.info("LeaveEarned :"+" "+(leaveEarned - oldNoOfDaysleave + updatedNoOfDays));
			employeeLeaveDetailEntity.setLeaveAvailable(leaveAvailable - oldNoOfDaysleave + updatedNoOfDays);

			logger.info("LeaveAvailable :"+" "+(leaveAvailable - oldNoOfDaysleave + updatedNoOfDays));
			employeeLeaveDetailsDAO.save(employeeLeaveDetailEntity);
		} else {
			/*
			 * the below insert block will not be used
			 */
			EmployeeLeaveDetail employeeLeaveDetail = new EmployeeLeaveDetail();
			employeeLeaveDetail.setLeaveAvailable(updatedNoOfDays);
			employeeLeaveDetail.setMasterLeaveType(masterLeaveType);
			employeeLeaveDetail.setEmployee(employee);
			employeeLeaveDetail.setYear(year);
			employeeLeaveDetailsDAO.save(employeeLeaveDetail);
		}
	}
}
