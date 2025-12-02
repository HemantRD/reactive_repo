package com.vinsys.hrms.dashboard.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.vinsys.hrms.constants.EResponse;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeLeaveAppliedDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationHolidayDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationWeekoffDAO;
import com.vinsys.hrms.dao.attendance.IHRMSAttendanceProcessedDataDAO;
import com.vinsys.hrms.dao.dashboard.IHRMSEventImagesDAO;
import com.vinsys.hrms.dashboard.DashboardTransformUtils;
import com.vinsys.hrms.dashboard.IDashboardService;
import com.vinsys.hrms.dashboard.vo.AttendanceGraphVO;
import com.vinsys.hrms.dashboard.vo.BirthdayVO;
import com.vinsys.hrms.dashboard.vo.EmployeeLeaveSumarryDetailsVO;
import com.vinsys.hrms.dashboard.vo.GalleryVO;
import com.vinsys.hrms.dashboard.vo.LeaveSumarryDetailsResponseVO;
import com.vinsys.hrms.dashboard.vo.ServiceCompletionDetailsVO;
import com.vinsys.hrms.dashboard.vo.ServiceCompletionVO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.employee.vo.BirthdayDetailsVO;
import com.vinsys.hrms.employee.vo.GalleryDetailsVO;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeLeaveDetail;
import com.vinsys.hrms.entity.OrganizationHoliday;
import com.vinsys.hrms.entity.OrganizationWeekoff;
import com.vinsys.hrms.entity.attendance.AttendanceProcessedData;
import com.vinsys.hrms.entity.dashboard.EventImages;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.util.EmployeeAttendanceHelper;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.ResponseCode;

@Service
public class DashboardServiceImpl implements IDashboardService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${app_version}")
	private String applicationVersion;

	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSEventImagesDAO eventImagesDAO;
	@Autowired
	IHRMSAttendanceProcessedDataDAO attendanceProcessedDataDAO;
	@Autowired
	IHRMSOrganizationHolidayDAO holidayDAO;
	@Autowired
	IHRMSOrganizationWeekoffDAO organizationWeekoffDAO;
	@Autowired
	IHRMSEmployeeLeaveAppliedDAO employeeLeaveAppliedDAO;
	@Autowired
	EmployeeAttendanceHelper employeeAttendanceHelper;

	@Override
	public HRMSBaseResponse<BirthdayVO> getBirthdayEvents() throws HRMSException, ParseException {
		HRMSBaseResponse<BirthdayVO> response = new HRMSBaseResponse<>();
		log.info("****************** FROM TL_USER ********************* ");
		log.info("Inside birthday method");

		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		if (!HRMSHelper.isLongZero(empId)) {
			List<Object[]> result = employeeDAO.upcomingBirthdays(SecurityFilter.TL_CLAIMS.get().getOrgId());
			List<BirthdayDetailsVO> events = new ArrayList<>();
			if (result != null && !result.isEmpty()) {
				for (Object[] resultSet : result) {
					BirthdayDetailsVO birthdayVO = new BirthdayDetailsVO();
					birthdayVO.setFirstName(String.valueOf(resultSet[0]));
					birthdayVO.setLastName(String.valueOf(resultSet[1]));
					String actualDateStr = String.valueOf(resultSet[2]);
					if (actualDateStr != null && !actualDateStr.isEmpty()) {
						SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

						Date actualDate = inputFormat.parse(actualDateStr);
						String formattedActualDate = outputFormat.format(actualDate);
						birthdayVO.setActualDate(formattedActualDate);

					}
					birthdayVO.setEventDate(String.valueOf(resultSet[3]));
					birthdayVO.setEmailId(String.valueOf(resultSet[4]));
					birthdayVO.setPhoto(String.valueOf(resultSet[5]));
					birthdayVO.setCandidateId(String.valueOf(resultSet[6]));
					birthdayVO.setBranchId(String.valueOf(resultSet[7]));
					birthdayVO.setDivisionId(String.valueOf(resultSet[8]));
					birthdayVO.setOrgId(String.valueOf(resultSet[9]));
					birthdayVO.setEmpId(String.valueOf(resultSet[10]));

					events.add(birthdayVO);
				}
				BirthdayVO birthdayVo = new BirthdayVO();
				birthdayVo.setBirthdayDetails(events);
				response.setResponseBody(birthdayVo);
				response.setResponseCode(1200);
				response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
				response.setApplicationVersion(applicationVersion);
			} else {

				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
			}
		} else {
			throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
		}
		return response;
	}

	@Override
	public HRMSBaseResponse<ServiceCompletionVO> getServiceCompletions() throws HRMSException, ParseException {

		log.info("Inside service completion method");
		HRMSBaseResponse<ServiceCompletionVO> response = new HRMSBaseResponse<>();

		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		if (!HRMSHelper.isLongZero(empId)) {

			Employee emp = employeeDAO.findActiveEmployeeById(Long.valueOf(empId), "Y");
			long orgId = emp.getCandidate().getLoginEntity().getOrganization().getId();

			List<Object[]> result = employeeDAO.upcomingServiceCompletions(orgId);
			List<ServiceCompletionDetailsVO> completionDetailsVO = new ArrayList<>();
			if (result != null && !result.isEmpty()) {

				for (Object[] resultSet : result) {
					ServiceCompletionDetailsVO serviceCompletionVo = new ServiceCompletionDetailsVO();
					serviceCompletionVo.setFirstName(String.valueOf(resultSet[0]));
					serviceCompletionVo.setLastName(String.valueOf(resultSet[1]));
					String actualDateStr = String.valueOf(resultSet[2]);
					if (actualDateStr != null && !actualDateStr.isEmpty()) {
						SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

						Date actualDate = inputFormat.parse(actualDateStr);
						String formattedActualDate = outputFormat.format(actualDate);
						serviceCompletionVo.setActualDate(formattedActualDate);

					}
					// serviceCompletionVo.setActualDate(String.valueOf(resultSet[2]));
					serviceCompletionVo.setEventDate(String.valueOf(resultSet[3]));
					serviceCompletionVo.setEmailId(String.valueOf(resultSet[4]));
					serviceCompletionVo.setPhoto(String.valueOf(resultSet[5]));
					serviceCompletionVo.setCandidateId(String.valueOf(resultSet[6]));
					serviceCompletionVo.setBranchId(String.valueOf(resultSet[7]));
					serviceCompletionVo.setDivisionId(String.valueOf(resultSet[8]));
					serviceCompletionVo.setOrgId(String.valueOf(resultSet[9]));
					serviceCompletionVo.setEmpId(String.valueOf(resultSet[10]));

					completionDetailsVO.add(serviceCompletionVo);
				}
//				long totalRecords = result.size();
				ServiceCompletionVO serviceCompletionVO = new ServiceCompletionVO();
				serviceCompletionVO.setServiceCompletionDetails(completionDetailsVO);
				response.setResponseBody(serviceCompletionVO);
//				response.setTotalRecord(totalRecords);
				response.setApplicationVersion(applicationVersion);
				response.setResponseCode(EResponse.SUCCESS.getCode());
				response.setResponseMessage(EResponse.SUCCESS.getMessage());
			} else {

				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
			}
		} else {

			throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
		}

		return response;

	}

	@Override
	public HRMSBaseResponse<GalleryVO> getGalleryImages() throws HRMSException {

		log.info("Inside event gallery method");

		HRMSBaseResponse<GalleryVO> response = new HRMSBaseResponse<>();
		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		if (!HRMSHelper.isLongZero(empId)) {

			Employee emp = employeeDAO.findActiveEmployeeById(Long.valueOf(empId), "Y");
			long orgId = emp.getCandidate().getLoginEntity().getOrganization().getId();
			List<EventImages> evtImages = new ArrayList<>();
			evtImages = eventImagesDAO.getEventImages(orgId, LocalDate.now(), "Y");
			// 88888
			List<GalleryDetailsVO> gelleryDetailsVO = new ArrayList<>();
			if (evtImages != null && !evtImages.isEmpty()) {

				for (EventImages resultSet : evtImages) {
					GalleryDetailsVO galleryDetailsVo = new GalleryDetailsVO();

					galleryDetailsVo.setId(resultSet.getId());
					galleryDetailsVo.setImagePath(resultSet.getImagePath());
					galleryDetailsVo.setImageTitle(resultSet.getImageTitle());
					galleryDetailsVo.setCreatedBy(resultSet.getCreatedBy());
					galleryDetailsVo.setIsActive(resultSet.getIsActive());

					gelleryDetailsVO.add(galleryDetailsVo);
				}

				GalleryVO galleryVO = new GalleryVO();
				galleryVO.setGalleryDetails(gelleryDetailsVO);
				response.setResponseBody(galleryVO);
				response.setResponseCode(1200);
				response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
				response.setApplicationVersion(applicationVersion);
			} else {
				log.info("No event images found");
				throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
			}

		} else {

			throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
		}
		return response;

	}

	@Override
	public HRMSBaseResponse<AttendanceGraphVO> empAttendanceGraphDetails(String fromDate, String toDate)
			throws HRMSException, ParseException {

		HRMSBaseResponse<AttendanceGraphVO> response = new HRMSBaseResponse<>();

		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();

		if (!HRMSHelper.isLongZero(empId)) {

			Employee emp = employeeDAO.findActiveEmployeeById(Long.valueOf(empId), "Y");
			long divId = emp.getCandidate().getCandidateProfessionalDetail().getDivision().getId();
			long branchId = !HRMSHelper
					.isNullOrEmpty(emp.getCandidate().getCandidateProfessionalDetail().getWorkingLocation())
							? emp.getCandidate().getCandidateProfessionalDetail().getWorkingLocation().getId()
							: emp.getCandidate().getCandidateProfessionalDetail().getBranch().getId();
			long orgId = emp.getCandidate().getLoginEntity().getOrganization().getId();
			long deptId = emp.getCandidate().getCandidateProfessionalDetail().getDepartment().getId();
			long currentYear = Year.now().getValue();

			Calendar calendar = Calendar.getInstance();
			// calendar.set(Calendar.WEEK_OF_MONTH, weekNumber);

			Date startOfWeek = HRMSDateUtil.getStartOfWeekInCurrentMonth(calendar);
			Date endOfWeek = HRMSDateUtil.getEndOfWeekInCurrentMonth(calendar);

			AttendanceGraphVO AttendanceGraphVO = new AttendanceGraphVO();
			List<AttendanceProcessedData> attendanceProcessedData = null;
			if (HRMSHelper.isNullOrEmpty(fromDate) && HRMSHelper.isNullOrEmpty(toDate)) {

				attendanceProcessedData = attendanceProcessedDataDAO.findWeekWiseAttendanceForEmployee(empId,
						startOfWeek, endOfWeek);
			} else {
				employeeAttendanceHelper.attendanceGraphInputValidation(fromDate, toDate);

				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Date fromDateFinal = dateFormat.parse(fromDate);
				Date toDateFinal = dateFormat.parse(toDate);
				attendanceProcessedData = attendanceProcessedDataDAO.findWeekWiseAttendanceForEmployee(empId,
						fromDateFinal, toDateFinal);
			}

			List<OrganizationHoliday> holidayEntityList = holidayDAO.getHolidayListByOrgBranchDivIdYear(orgId, divId,
					branchId, currentYear);

			List<OrganizationWeekoff> weekoffList = organizationWeekoffDAO.getWeekoffByOrgBranchDivDeptId(orgId, divId,
					branchId, deptId);

			AttendanceGraphVO = DashboardTransformUtils.transalteToAttendanceGraphVO(attendanceProcessedData,
					holidayEntityList, weekoffList);

			// get week days
//			String weekDays = "FRIDAY";
			List<String> weekDays = new ArrayList<String>();

			String weekOffDays = weekoffList.get(0).getWeekoffDays();
			String[] weekday = weekOffDays.split(",");

			int index = getIndex(weekday[weekday.length - 1]);
			int startingDay = index + 1;

			calendar.setTime(new Date());

			// Set the calendar's day of the week to the starting day
			calendar.set(Calendar.DAY_OF_WEEK, startingDay);

			// Print the days of the week
			SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
			for (int i = 0; i < 7; i++) {
				weekDays.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DAY_OF_WEEK, 1);
			}

			AttendanceGraphVO.setWeekDays(weekDays);

			response.setResponseBody(AttendanceGraphVO);
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setApplicationVersion(applicationVersion);

			return response;

		} else {
			throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
		}

	}

	private int getIndex(String string) {
		switch (string) {
		case "SUNDAY":
			return Calendar.SUNDAY;
		case "MONDAY":
			return Calendar.MONDAY;
		case "TUESDAY":
			return Calendar.TUESDAY;
		case "WEDNESDAY":
			return Calendar.WEDNESDAY;
		case "THURSDAY":
			return Calendar.THURSDAY;
		case "FRIDAY":
			return Calendar.FRIDAY;
		case "SATURDAY":
			return Calendar.SATURDAY;
		default:
			throw new IllegalArgumentException("Invalid day: " + string);
		}
	}

	@Override
	public HRMSBaseResponse<LeaveSumarryDetailsResponseVO> getEmployeeLeaveSummary() throws HRMSException {
		log.info("Inside getEmployeeLeaveSummary method");
		HRMSBaseResponse<LeaveSumarryDetailsResponseVO> response = new HRMSBaseResponse();

		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		Long orgId = SecurityFilter.TL_CLAIMS.get().getOrgId();

		if (!HRMSHelper.isLongZero(empId)) {
			Employee employee = employeeDAO.getEmployeeLeaveDetailByEmpIdAndYearAndOrgId(empId,
					HRMSDateUtil.getCurrentYear(), IHRMSConstants.isActive, orgId);

			if (HRMSHelper.isNullOrEmpty(employee)) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1201));
			}

			LeaveSumarryDetailsResponseVO leaveDetails = new LeaveSumarryDetailsResponseVO();
			List<EmployeeLeaveSumarryDetailsVO> emplLeaveDtlList = new ArrayList<>();

			Set<EmployeeLeaveDetail> employeeLeaveDetailsSet = employee.getEmployeeLeaveDetails();
			int totalRecords = 0;

			for (EmployeeLeaveDetail employeeLeaveDetailsEntity : employeeLeaveDetailsSet) {

				EmployeeLeaveSumarryDetailsVO model = DashboardTransformUtils
						.convertToEmployeeLeaveSumarryVo(employeeLeaveDetailsEntity);
				if (!HRMSHelper.isNullOrEmpty(model)) {
					if (!HRMSHelper.isNullOrEmpty(model.getNumberOfDaysAvailed())
							|| !HRMSHelper.isNullOrEmpty(model.getLeaveAvailable())) {

						long countLeaveApplied = employeeLeaveAppliedDAO.findCountOfAppliedLeaveDetailsByOrgId(
								employee.getId(), employeeLeaveDetailsEntity.getMasterLeaveType().getId(),
								IHRMSConstants.LeaveStatus_PENDING, HRMSDateUtil.getCurrentYear(), orgId);

						log.info("Leave Type : " + employeeLeaveDetailsEntity.getMasterLeaveType().getLeaveTypeName());
						log.info("Leave Type ID : " + employeeLeaveDetailsEntity.getMasterLeaveType().getId());
						log.info("For Employee ID : " + employee.getId());
						log.info("For Year : " + HRMSDateUtil.getCurrentYear());
						log.info("Count : " + countLeaveApplied);

						model.setLeaveApplied(countLeaveApplied);
						emplLeaveDtlList.add(model);
						totalRecords++;
					}
				}
			}

			leaveDetails.setEmployeeLeaveDetail(emplLeaveDtlList);
			response.setResponseBody(leaveDetails);
			response.setTotalRecord(totalRecords);
			response.setApplicationVersion(applicationVersion);
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		} else {
			throw new HRMSException(EResponse.ERROR_INSUFFICIENT_DATA.getCode(),
					EResponse.ERROR_INSUFFICIENT_DATA.getMessage());
		}
		log.info("Exit from getEmployeeLeaveSummary method");
		return response;
	}

}
