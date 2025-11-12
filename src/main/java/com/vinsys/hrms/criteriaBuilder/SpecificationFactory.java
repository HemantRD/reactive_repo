package com.vinsys.hrms.criteriaBuilder;

import java.util.Date;

import javax.persistence.criteria.Path;

import org.springframework.data.jpa.domain.Specification;

import com.vinsys.hrms.datamodel.VOLeaveReportParam;
import com.vinsys.hrms.datamodel.traveldesk.VOReportFilterTraveldesk;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeCreditLeaveDetail;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

public final class SpecificationFactory {

	public static Specification containsLike(String attribute, String value) {
		return (root, query, cb) -> cb.like(root.get(attribute), "%" + value + "%");
	}

	public static Specification isBetween(String attribute, int min, int max) {
		return (root, query, cb) -> cb.between(root.get(attribute), min, max);
	}

	public static Specification isBetween(String attribute, double min, double max) {
		return (root, query, cb) -> cb.between(root.get(attribute), min, max);
	}

	public static Specification gretterThanOrEqualTo(String attribute, Date fromDate) {
		return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(attribute), fromDate);
	}

	public static Specification lessThanOrEqualTo(String attribute, Date toDate) {
		return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(attribute), toDate);
	}

	public static Specification isEqualToLong(String attribute, long empId) {
		return (root, query, cb) -> cb.equal(root.get(attribute), empId);
	}

	public static Specification containsLikeEqualIgnoreCase(String attribute, String value) {
		return (root, query, cb) -> cb.like(cb.upper(root.get(attribute)), "%" + value.toUpperCase() + "%");
	}

	public static Specification equalIgnoreCase(String attribute, String value) {
		return (root, query, cb) -> cb.like(cb.upper(root.get(attribute)), value.toUpperCase());
	}

	public static Specification isEqualToInt(String attribute, int intNumber) {
		return (root, query, cb) -> cb.equal(root.get(attribute), intNumber);
	}

	public static <T extends Enum<T>> Specification enumMatcher(String attribute, T queriedValue) {
		return (root, query, cb) -> {
			Path<T> actualValue = root.get(attribute);

			if (queriedValue == null) {
				return null;
			}

			return cb.equal(actualValue, queriedValue);
		};

	}

	public static Specification extendedLeaveReport(VOLeaveReportParam leaveReportParam) {

		Specification spec = null;

		if (!HRMSHelper.isLongZero(leaveReportParam.getEmployeeId())) {
			if (null != spec) {
				spec = spec.and(isEqualToLong("employeeId", leaveReportParam.getEmployeeId()));
			} else {
				spec = Specification.where(isEqualToLong("employeeId", leaveReportParam.getEmployeeId()));
			}
		}
		if (!HRMSHelper.isNullOrEmpty(leaveReportParam.getEmployeeName())) {
			if (null != spec) {
				spec = spec.and(containsLikeEqualIgnoreCase("employeeName", leaveReportParam.getEmployeeName()));
			} else {
				spec = Specification
						.where(containsLikeEqualIgnoreCase("employeeName", leaveReportParam.getEmployeeName()));
			}
		}
		if (!HRMSHelper.isNullOrEmpty(leaveReportParam.getFromDate()) && !HRMSHelper.isNullOrEmpty(
				HRMSDateUtil.parse(leaveReportParam.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT))) {
			if (null != spec) {
				spec = spec.and(gretterThanOrEqualTo("fromDate",
						HRMSDateUtil.parse(leaveReportParam.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT)));
			} else {
				spec = Specification.where(gretterThanOrEqualTo("fromDate",
						HRMSDateUtil.parse(leaveReportParam.getFromDate(), IHRMSConstants.FRONT_END_DATE_FORMAT)));
			}
		}
		if (!HRMSHelper.isNullOrEmpty(leaveReportParam.getToDate()) && !HRMSHelper.isNullOrEmpty(
				HRMSDateUtil.parse(leaveReportParam.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT))) {
			if (null != spec) {
				spec = spec.and(lessThanOrEqualTo("toDate",
						HRMSDateUtil.parse(leaveReportParam.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT)));
			} else {
				spec = Specification.where(lessThanOrEqualTo("toDate",
						HRMSDateUtil.parse(leaveReportParam.getToDate(), IHRMSConstants.FRONT_END_DATE_FORMAT)));
			}
		}
		if (!HRMSHelper.isNullOrEmpty(leaveReportParam.getReportingManager())) {
			if (null != spec) {
				spec = spec
						.and(containsLikeEqualIgnoreCase("reportingManager", leaveReportParam.getReportingManager()));
			} else {
				spec = Specification
						.where(containsLikeEqualIgnoreCase("reportingManager", leaveReportParam.getReportingManager()));
			}
		}
		if (!HRMSHelper.isLongZero(leaveReportParam.getDepartmentId())) {
			if (null != spec) {
				spec = spec.and(isEqualToLong("departmentId", leaveReportParam.getDepartmentId()));
			} else {
				spec = Specification.where(isEqualToLong("departmentId", leaveReportParam.getDepartmentId()));
			}
		}
		if (!HRMSHelper.isLongZero(leaveReportParam.getBranchId())) {
			if (null != spec) {
				spec = spec.and(isEqualToLong("branchId", leaveReportParam.getBranchId()));
			} else {
				spec = Specification.where(isEqualToLong("branchId", leaveReportParam.getBranchId()));
			}
		}
		if (!HRMSHelper.isLongZero(leaveReportParam.getDepartmentId())) {
			if (null != spec) {
				spec = spec.and(isEqualToLong("departmentId", leaveReportParam.getDepartmentId()));
			} else {
				spec = Specification.where(isEqualToLong("departmentId", leaveReportParam.getDepartmentId()));
			}
		}
		if (!HRMSHelper.isLongZero(leaveReportParam.getDesignationId())) {
			if (null != spec) {
				spec = spec.and(isEqualToLong("designationId", leaveReportParam.getDesignationId()));
			} else {
				spec = Specification.where(isEqualToLong("designationId", leaveReportParam.getDesignationId()));
			}
		}
		if (!HRMSHelper.isNullOrEmpty(leaveReportParam.getEmpIsActive())) {
			if (null != spec) {
				spec = spec.and(equalIgnoreCase("empIsActive", leaveReportParam.getEmpIsActive()));
			} else {
				spec = Specification.where(equalIgnoreCase("empIsActive", leaveReportParam.getEmpIsActive()));
			}
		}
		if (!HRMSHelper.isLongZero(leaveReportParam.getOrgId())) {
			if (null != spec) {
				spec = spec.and(isEqualToInt("orgId", leaveReportParam.getOrgId()));
			} else {
				spec = Specification.where(isEqualToInt("orgId", leaveReportParam.getOrgId()));
			}
		}
		return spec;

	}

	public static Specification extendedLeaveSummaryReport(VOLeaveReportParam leaveReportParam) {

		Specification spec = null;

		if (!HRMSHelper.isLongZero(leaveReportParam.getEmployeeId())) {
			if (null != spec) {
				spec = spec.and(isEqualToLong("employeeId", leaveReportParam.getEmployeeId()));
			} else {
				spec = Specification.where(isEqualToLong("employeeId", leaveReportParam.getEmployeeId()));
			}
		}
		if (!HRMSHelper.isNullOrEmpty(leaveReportParam.getEmployeeName())) {
			if (null != spec) {
				spec = spec.and(containsLikeEqualIgnoreCase("employeeName", leaveReportParam.getEmployeeName()));
			} else {
				spec = Specification
						.where(containsLikeEqualIgnoreCase("employeeName", leaveReportParam.getEmployeeName()));
			}
		}
		if (!HRMSHelper.isNullOrEmpty(leaveReportParam.getReportingManager())) {
			if (null != spec) {
				spec = spec
						.and(containsLikeEqualIgnoreCase("reportingManager", leaveReportParam.getReportingManager()));
			} else {
				spec = Specification
						.where(containsLikeEqualIgnoreCase("reportingManager", leaveReportParam.getReportingManager()));
			}
		}
		if (!HRMSHelper.isLongZero(leaveReportParam.getDepartmentId())) {
			if (null != spec) {
				spec = spec.and(isEqualToLong("departmentId", leaveReportParam.getDepartmentId()));
			} else {
				spec = Specification.where(isEqualToLong("departmentId", leaveReportParam.getDepartmentId()));
			}
		}
		if (!HRMSHelper.isLongZero(leaveReportParam.getBranchId())) {
			if (null != spec) {
				spec = spec.and(isEqualToLong("branchId", leaveReportParam.getBranchId()));
			} else {
				spec = Specification.where(isEqualToLong("branchId", leaveReportParam.getBranchId()));
			}
		}
		if (!HRMSHelper.isLongZero(leaveReportParam.getDepartmentId())) {
			if (null != spec) {
				spec = spec.and(isEqualToLong("departmentId", leaveReportParam.getDepartmentId()));
			} else {
				spec = Specification.where(isEqualToLong("departmentId", leaveReportParam.getDepartmentId()));
			}
		}
		if (!HRMSHelper.isLongZero(leaveReportParam.getDesignationId())) {
			if (null != spec) {
				spec = spec.and(isEqualToLong("designationId", leaveReportParam.getDesignationId()));
			} else {
				spec = Specification.where(isEqualToLong("designationId", leaveReportParam.getDesignationId()));
			}
		}
		if (!HRMSHelper.isNullOrEmpty(leaveReportParam.getEmpIsActive())) {
			if (null != spec) {
				spec = spec.and(equalIgnoreCase("empIsActive", leaveReportParam.getEmpIsActive()));
			} else {
				spec = Specification.where(equalIgnoreCase("empIsActive", leaveReportParam.getEmpIsActive()));
			}
		}
		if (!HRMSHelper.isLongZero(leaveReportParam.getYear())) {
			if (null != spec) {
				spec = spec.and(isEqualToInt("year", leaveReportParam.getYear()));
			} else {
				spec = Specification.where(isEqualToInt("year", leaveReportParam.getYear()));
			}
		}
		if (!HRMSHelper.isLongZero(leaveReportParam.getOrgId())) {
			if (null != spec) {
				spec = spec.and(isEqualToInt("orgId", leaveReportParam.getOrgId()));
			} else {
				spec = Specification.where(isEqualToInt("orgId", leaveReportParam.getOrgId()));
			}
		}
		return spec;

	}

	public static Specification extendedTicketRequestReport(VOReportFilterTraveldesk traveldeskReportParam) {

		Specification spec = null;

		if (!HRMSHelper.isNullOrEmpty(traveldeskReportParam.getFromDate())
				&& !HRMSHelper.isNullOrEmpty(traveldeskReportParam.getFromDate())) {
			if (null != spec) {
				spec = spec.and(gretterThanOrEqualTo("createdDate", traveldeskReportParam.getFromDate()));
			} else {
				spec = Specification.where(gretterThanOrEqualTo("createdDate", traveldeskReportParam.getFromDate()));
			}
		}
		if (!HRMSHelper.isNullOrEmpty(traveldeskReportParam.getToDate())
				&& !HRMSHelper.isNullOrEmpty(traveldeskReportParam.getToDate())) {
			if (null != spec) {
				spec = spec.and(lessThanOrEqualTo("createdDate", traveldeskReportParam.getToDate()));
			} else {
				spec = Specification.where(lessThanOrEqualTo("createdDate", traveldeskReportParam.getToDate()));
			}
		}
		return spec;

	}

	public static Specification extendedAccomodationRequestReport(VOReportFilterTraveldesk traveldeskReportParam) {

		Specification spec = null;

		if (!HRMSHelper.isNullOrEmpty(traveldeskReportParam.getFromDate())
				&& !HRMSHelper.isNullOrEmpty(traveldeskReportParam.getFromDate())) {
			if (null != spec) {
				spec = spec.and(gretterThanOrEqualTo("createdDate", traveldeskReportParam.getFromDate()));
			} else {
				spec = Specification.where(gretterThanOrEqualTo("createdDate", traveldeskReportParam.getFromDate()));

			}
		}
		if (!HRMSHelper.isNullOrEmpty(traveldeskReportParam.getToDate())
				&& !HRMSHelper.isNullOrEmpty(traveldeskReportParam.getToDate())) {
			if (null != spec) {
				spec = spec.and(lessThanOrEqualTo("createdDate", traveldeskReportParam.getToDate()));
			} else {
				spec = Specification.where(lessThanOrEqualTo("createdDate", traveldeskReportParam.getToDate()));
			}
		}
		return spec;

	}

	public static Specification extendedAccomodationRequestReportFromDate(
			VOReportFilterTraveldesk traveldeskReportParam) {

		Specification spec = null;

		if (!HRMSHelper.isNullOrEmpty(traveldeskReportParam.getFromDate())
				&& !HRMSHelper.isNullOrEmpty(traveldeskReportParam.getFromDate())) {
			if (null != spec) {
				spec = spec.and(gretterThanOrEqualTo("fromDate", traveldeskReportParam.getFromDate()));
			} else {
				spec = Specification.where(gretterThanOrEqualTo("fromDate", traveldeskReportParam.getFromDate()));

			}
		}
		if (!HRMSHelper.isNullOrEmpty(traveldeskReportParam.getToDate())
				&& !HRMSHelper.isNullOrEmpty(traveldeskReportParam.getToDate())) {
			if (null != spec) {
				spec = spec.and(lessThanOrEqualTo("fromDate", traveldeskReportParam.getToDate()));
			} else {
				spec = Specification.where(lessThanOrEqualTo("fromDate", traveldeskReportParam.getToDate()));
			}
		}
		return spec;

	}

	public static Specification extendedAccomodationRequestReportToDate(
			VOReportFilterTraveldesk traveldeskReportParam) {

		Specification spec = null;

		if (!HRMSHelper.isNullOrEmpty(traveldeskReportParam.getFromDate())
				&& !HRMSHelper.isNullOrEmpty(traveldeskReportParam.getFromDate())) {
			if (null != spec) {
				spec = spec.and(gretterThanOrEqualTo("toDate", traveldeskReportParam.getFromDate()));
			} else {
				spec = Specification.where(gretterThanOrEqualTo("toDate", traveldeskReportParam.getFromDate()));
			}
		}
		if (!HRMSHelper.isNullOrEmpty(traveldeskReportParam.getToDate())
				&& !HRMSHelper.isNullOrEmpty(traveldeskReportParam.getToDate())) {
			if (null != spec) {
				spec = spec.and(lessThanOrEqualTo("toDate", traveldeskReportParam.getToDate()));
			} else {
				spec = Specification.where(lessThanOrEqualTo("toDate", traveldeskReportParam.getToDate()));
			}
		}
		return spec;

	}

	public static Specification extendedCabRequestReport(VOReportFilterTraveldesk traveldeskReportParam) {

		Specification spec = null;

		if (!HRMSHelper.isNullOrEmpty(traveldeskReportParam.getFromDate())
				&& !HRMSHelper.isNullOrEmpty(traveldeskReportParam.getFromDate())) {
			if (null != spec) {
				spec = spec.and(gretterThanOrEqualTo("createdDate", traveldeskReportParam.getFromDate()));
			} else {
				spec = Specification.where(gretterThanOrEqualTo("createdDate", traveldeskReportParam.getFromDate()));
			}
		}
		if (!HRMSHelper.isNullOrEmpty(traveldeskReportParam.getToDate())
				&& !HRMSHelper.isNullOrEmpty(traveldeskReportParam.getToDate())) {
			if (null != spec) {
				spec = spec.and(lessThanOrEqualTo("createdDate", traveldeskReportParam.getToDate()));
			} else {
				spec = Specification.where(lessThanOrEqualTo("createdDate", traveldeskReportParam.getToDate()));
			}
		}
		return spec;

	}

	public static Specification extendedWonTocostReport(VOReportFilterTraveldesk traveldeskReportParam) {

		Specification spec = null;

		if (!HRMSHelper.isNullOrEmpty(traveldeskReportParam.getFromDate())
				&& !HRMSHelper.isNullOrEmpty(traveldeskReportParam.getFromDate())) {
			if (null != spec) {
				spec = spec.and(gretterThanOrEqualTo("createdDate", traveldeskReportParam.getFromDate()));
			} else {
				spec = Specification.where(gretterThanOrEqualTo("createdDate", traveldeskReportParam.getFromDate()));
			}
		}
		if (!HRMSHelper.isNullOrEmpty(traveldeskReportParam.getToDate())
				&& !HRMSHelper.isNullOrEmpty(traveldeskReportParam.getToDate())) {
			if (null != spec) {

				spec = spec.and(lessThanOrEqualTo("createdDate", traveldeskReportParam.getToDate()));
			} else {
				spec = Specification.where(lessThanOrEqualTo("createdDate", traveldeskReportParam.getToDate()));
			}
		}
		return spec;

	}

	public static Specification extendedTraveldeskDetailReportForDate(VOReportFilterTraveldesk traveldeskReportParam) {

		Specification spec = null;

		if (!HRMSHelper.isNullOrEmpty(traveldeskReportParam.getFromDate())
				&& !HRMSHelper.isNullOrEmpty(traveldeskReportParam.getFromDate())) {
			if (null != spec) {
				spec = spec.and(gretterThanOrEqualTo("createdDate", traveldeskReportParam.getFromDate()));
			} else {
				spec = Specification.where(gretterThanOrEqualTo("createdDate", traveldeskReportParam.getFromDate()));
			}
		}
		if (!HRMSHelper.isNullOrEmpty(traveldeskReportParam.getToDate())
				&& !HRMSHelper.isNullOrEmpty(traveldeskReportParam.getToDate())) {
			if (null != spec) {

				spec = spec.and(lessThanOrEqualTo("createdDate", traveldeskReportParam.getToDate()));
			} else {
				spec = Specification.where(lessThanOrEqualTo("createdDate", traveldeskReportParam.getToDate()));
			}
		}
		return spec;

	}

	public static Specification extendedTraveldeskDetailReportForRequestType(
			VOReportFilterTraveldesk traveldeskReportParam) {

		Specification spec = null;

		if (!HRMSHelper.isNullOrEmpty(traveldeskReportParam.getTicketStr())
				&& traveldeskReportParam.getTicketStr().equalsIgnoreCase("Yes")) {
			if (null != spec) {
				spec = spec.or(equalIgnoreCase("requestType", "Ticket"));
			} else {
				spec = Specification.where(equalIgnoreCase("requestType", "Ticket"));
			}
		}
		if (!HRMSHelper.isNullOrEmpty(traveldeskReportParam.getAccommodationStr())
				&& traveldeskReportParam.getAccommodationStr().equalsIgnoreCase("Yes")) {
			if (null != spec) {
				spec = spec.or(equalIgnoreCase("requestType", "Accommodation"));
			} else {
				spec = Specification.where(equalIgnoreCase("requestType", "Accommodation"));
			}
		}
		if (!HRMSHelper.isNullOrEmpty(traveldeskReportParam.getCabStr())
				&& traveldeskReportParam.getCabStr().equalsIgnoreCase("Yes")) {
			if (null != spec) {
				spec = spec.or(equalIgnoreCase("requestType", "Cab"));
			} else {
				spec = Specification.where(equalIgnoreCase("requestType", "Cab"));
			}
		}
		return spec;

	}

	public static Specification extendedTraveldeskDetailReportForTravellingMode(
			VOReportFilterTraveldesk traveldeskReportParam) {
		Specification spec = null;

		if (!HRMSHelper.isNullOrEmpty(traveldeskReportParam.getAirStr())
				&& traveldeskReportParam.getAirStr().equalsIgnoreCase("Yes")) {
			if (null != spec) {
				spec = spec.or(equalIgnoreCase("travellingMode", "Air"));
			} else {
				spec = Specification.where(equalIgnoreCase("travellingMode", "Air"));
			}
		}
		if (!HRMSHelper.isNullOrEmpty(traveldeskReportParam.getBusStr())
				&& traveldeskReportParam.getBusStr().equalsIgnoreCase("Yes")) {
			if (null != spec) {
				spec = spec.or(equalIgnoreCase("travellingMode", "Bus"));
			} else {
				spec = Specification.where(equalIgnoreCase("travellingMode", "Bus"));
			}
		}
		if (!HRMSHelper.isNullOrEmpty(traveldeskReportParam.getTrainStr())
				&& traveldeskReportParam.getTrainStr().equalsIgnoreCase("Yes")) {
			if (null != spec) {
				spec = spec.or(equalIgnoreCase("travellingMode", "Train"));
			} else {
				spec = Specification.where(equalIgnoreCase("travellingMode", "Train"));
			}
		}
		return spec;

	}

	/**
	 * @author SSW
	 * @param attribute
	 * @param emp
	 * @return
	 * 
	 *         Specification used to compare for employee object
	 */
	public static Specification isEqualToEmployee(String attribute, Employee emp) {
		return (root, query, cb) -> cb.equal(root.get(attribute), emp);
	}

	/**
	 * @author SSW
	 * @return
	 * 
	 *         Specification used to compare year of "fromDate" with current year
	 *         this specification can be used by any object to current compare year
	 *         with year in object
	 */
	public static Specification isFromDateEqualToCurrentYear() {
		// first criteria builder function return year of current date in Integer format
		// second criteria builder function return year of fromDate date in Integer
		// format
		return (root, query, cb) -> cb.equal(cb.function("YEAR", Integer.class, cb.currentDate()),
				cb.function("YEAR", Integer.class, root.get("fromDate")));
	}

	public static Specification lessThanOrEqualToForToDate(String attribute, Date toDate) {
		return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(attribute), toDate);
	}

	public static Specification specFindCredLeaveDetailsByFilter(EmployeeCreditLeaveDetail employeeCreditLeaveDetail) {

		Specification spec = null;
		spec = Specification.where(isEqualToEmployee("employee", employeeCreditLeaveDetail.getEmployee()));
		if (!HRMSHelper.isNullOrEmpty(employeeCreditLeaveDetail.getFromDate())) {
			spec = spec.and(gretterThanOrEqualTo("fromDate", employeeCreditLeaveDetail.getFromDate()));
			// next specification to compare year, so that result will show data from only
			// current year
			spec = spec.and(isFromDateEqualToCurrentYear());
		}
		if (!HRMSHelper.isNullOrEmpty(employeeCreditLeaveDetail.getToDate())) {
			// Specification or1 = lessThanOrEqualTo("toDate",
			// employeeCreditLeaveDetail.getToDate());
			// Specification or2 = lessThanOrEqualTo("toDate", null);
			// spec = spec.and(or1 spec.or(or2));

			spec = spec.and(lessThanOrEqualTo("toDate", employeeCreditLeaveDetail.getToDate()));
		}
		return spec;
	}

}