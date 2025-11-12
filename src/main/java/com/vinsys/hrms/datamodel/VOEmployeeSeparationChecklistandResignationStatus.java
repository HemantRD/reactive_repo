package com.vinsys.hrms.datamodel;

import java.util.List;

public class VOEmployeeSeparationChecklistandResignationStatus extends VOAuditBase{
	
	private String approverName;
	private String stage;
	private String status;
	private String actedon;
	private double totalamount;
	
	List<VOMapEmployeeCatalogueChecklist> empCatalogueChecklist ;
	
	
	public String getApproverName() {
		return approverName;
	}
	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getActedon() {
		return actedon;
	}
	public void setActedon(String actedon) {
		this.actedon = actedon;
	}
	public double getTotalamount() {
		return totalamount;
	}
	public void setTotalamount(double totalamount) {
		this.totalamount = totalamount;
	}
	public List<VOMapEmployeeCatalogueChecklist> getEmpCatalogueChecklist() {
		return empCatalogueChecklist;
	}
	public void setEmpCatalogueChecklist(List<VOMapEmployeeCatalogueChecklist> empCatalogueChecklist) {
		this.empCatalogueChecklist = empCatalogueChecklist;
	}
	
	
	
	
	

}
