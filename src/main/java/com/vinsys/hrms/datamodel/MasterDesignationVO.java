package com.vinsys.hrms.datamodel;

public class MasterDesignationVO extends VOAuditBase {
	
	
	private long id;
	    private String designationName;
	    private String designationDescription;
	    private VOOrganization organization;
		public String getDesignationName() {
			return designationName;
		}
		public void setDesignationName(String designationName) {
			this.designationName = designationName;
		}
		public String getDesignationDescription() {
			return designationDescription;
		}
		public void setDesignationDescription(String designationDescription) {
			this.designationDescription = designationDescription;
		}
		public VOOrganization getOrganization() {
			return organization;
		}
		public void setOrganization(VOOrganization organization) {
			this.organization = organization;
		}
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
	    
	   
	

}
