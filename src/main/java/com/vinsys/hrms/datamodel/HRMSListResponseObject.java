package com.vinsys.hrms.datamodel;

import java.util.List;

public class HRMSListResponseObject extends HRMSBaseResponse {

	private long totalCount;
	private int pageSize;
	private int pageNo;
	private boolean isCandidateFormSubmitted;
	private boolean isExitfeedbackFormenabled;
	private List<Object> listResponse;
	private double totalamountofCatalogue;
	private List<Object> colHeaders;
	private int successCount;
	private int errorCount;

	public List<Object> getListResponse() {
		return listResponse;
	}

	public void setListResponse(List<Object> listResponse) {
		this.listResponse = listResponse;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public boolean isCandidateFormSubmitted() {
		return isCandidateFormSubmitted;
	}

	public void setCandidateFormSubmitted(boolean isCandidateFormSubmitted) {
		this.isCandidateFormSubmitted = isCandidateFormSubmitted;
	}

	public boolean isExitfeedbackFormenabled() {
		return isExitfeedbackFormenabled;
	}

	public void setExitfeedbackFormenabled(boolean isExitfeedbackFormenabled) {
		this.isExitfeedbackFormenabled = isExitfeedbackFormenabled;
	}

	public double getTotalamountofCatalogue() {
		return totalamountofCatalogue;
	}

	public void setTotalamountofCatalogue(double totalamountofCatalogue) {
		this.totalamountofCatalogue = totalamountofCatalogue;
	}

	public List<Object> getColHeaders() {
		return colHeaders;
	}

	public void setColHeaders(List<Object> colHeaders) {
		this.colHeaders = colHeaders;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

	public int getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}
   
}
