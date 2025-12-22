package com.vinsys.hrms.kra.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Author: kailas.baraskar
 */
public interface CycleResult {

	Date getEndDate();

	String getCycleName();

	BigInteger getCycleType();

	BigInteger getCycleId();

	BigInteger getYearId();

	String getYear();

	BigInteger getTotalCount();

	BigInteger getCompletedCount();

	BigDecimal getCompletedPercentage();

	// Submitted
	BigInteger getSubmittedCount();

	BigDecimal getSubmittedPercentage();

	// Pending
	BigInteger getPendingCount();

	BigDecimal getPendingPercentage();

	// Pending with HOD
	BigInteger getPendingWithHod();

	BigDecimal getPendingWithHodPercentage();

	// Submitted by HOD
	BigInteger getSubmittedByHod(); // Added

	BigDecimal getSubmittedHodPercentage(); // Renamed from getSubmittedHODPercentage()

	// Pending with Line Manager
	BigInteger getPendingWithLineManager();

	BigDecimal getPendingWithLineManagerPercentage();

	// Submitted by Line Manager
	BigInteger getSubmittedByLineManager(); // Added

	BigDecimal getSubmittedByLineManagerPercentage();

	// Submitted by Employee
	BigInteger getSubmittedByEmployee();

	BigDecimal getSubmittedEmployeePercentage();

	BigInteger getPendingWithEmployee();

	BigDecimal getPendingWithEmployeePercentage();

	BigInteger getPendingWithHr();

	BigDecimal getPendingWithHrPercentage();

	BigInteger getSubmittedHr(); // Added

	BigDecimal getSubmittedHrPercentage();

	BigDecimal getEmployeePercentage();

	String getStatus();

	String getPendingWith();
}
