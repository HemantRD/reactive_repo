package com.vinsys.hrms.employee.vo;

import java.util.List;

/**
 * @author Onkar A
 *
 * 
 */
public class ProbationFeedbackDetailsVO {
	private List<ProbationFeedbackVO> feedbackVOList;
	private boolean isProbationPeriodCompleted;
	private boolean isExtended;
	private String employmentStatus;

	public List<ProbationFeedbackVO> getFeedbackVOList() {
		return feedbackVOList;
	}

	public void setFeedbackVOList(List<ProbationFeedbackVO> feedbackVOList) {
		this.feedbackVOList = feedbackVOList;
	}

	

	public boolean isExtended() {
		return isExtended;
	}

	public void setExtended(boolean isExtended) {
		this.isExtended = isExtended;
	}

	public boolean isProbationPeriodCompleted() {
		return isProbationPeriodCompleted;
	}

	public void setProbationPeriodCompleted(boolean isProbationPeriodCompleted) {
		this.isProbationPeriodCompleted = isProbationPeriodCompleted;
	}

	public String getEmploymentStatus() {
		return employmentStatus;
	}

	public void setEmploymentStatus(String employmentStatus) {
		this.employmentStatus = employmentStatus;
	}
}
