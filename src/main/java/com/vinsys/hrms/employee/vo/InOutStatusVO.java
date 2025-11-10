package com.vinsys.hrms.employee.vo;

/**
 * @author Onkar A. This class will use to to maintain required flags for
 *         attendance controller.
 */
public class InOutStatusVO {

	private String isInEnable;
	private String isOutEnable;

	public String getIsInEnable() {
		return isInEnable;
	}

	public void setIsInEnable(String isInEnable) {
		this.isInEnable = isInEnable;
	}

	public String getIsOutEnable() {
		return isOutEnable;
	}

	public void setIsOutEnable(String isOutEnable) {
		this.isOutEnable = isOutEnable;
	}
}
