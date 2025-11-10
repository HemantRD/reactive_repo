package com.vinsys.hrms.kra.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface CycleCountResult {

	String getCycleName();

	BigInteger getCycleId();

	String getYear();

	BigInteger getTotalCount();

	BigInteger getCompletedCount();

	BigDecimal getCompletedPercentage();

	
	BigInteger getSubmittedCount();

	BigDecimal getSubmittedPercentage();

	
	BigInteger getPendingCount();

	BigDecimal getPendingPercentage();

}
