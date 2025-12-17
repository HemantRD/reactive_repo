package com.vinsys.hrms.traveldesk.vo;

/**
 * @author Onkar A
 *
 * 
 */
public class TravelRequestStatusVO {

	private Long travelRequestId;
	private String isTDApproxCostSubmitted;
	private String isTDFinalCostSubmitted;
	private String isTDApproverSubmitted;

	public Long getTravelRequestId() {
		return travelRequestId;
	}

	public void setTravelRequestId(Long travelRequestId) {
		this.travelRequestId = travelRequestId;
	}

	

	public String getIsTDApproxCostSubmitted() {
		return isTDApproxCostSubmitted;
	}

	public void setIsTDApproxCostSubmitted(String isTDApproxCostSubmitted) {
		this.isTDApproxCostSubmitted = isTDApproxCostSubmitted;
	}

	public String getIsTDFinalCostSubmitted() {
		return isTDFinalCostSubmitted;
	}

	public void setIsTDFinalCostSubmitted(String isTDFinalCostSubmitted) {
		this.isTDFinalCostSubmitted = isTDFinalCostSubmitted;
	}

	public String getIsTDApproverSubmitted() {
		return isTDApproverSubmitted;
	}

	public void setIsTDApproverSubmitted(String isTDApproverSubmitted) {
		this.isTDApproverSubmitted = isTDApproverSubmitted;
	}

}
