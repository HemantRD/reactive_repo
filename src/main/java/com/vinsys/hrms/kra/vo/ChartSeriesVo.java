package com.vinsys.hrms.kra.vo;

import java.math.BigInteger;

public class ChartSeriesVo {

	private BigInteger pending;
	private BigInteger submitted;

	public BigInteger getPending() {
		return pending;
	}

	public void setPending(BigInteger pending) {
		this.pending = pending;
	}

	public BigInteger getSubmitted() {
		return submitted;
	}

	public void setSubmitted(BigInteger submitted) {
		this.submitted = submitted;
	}

}
