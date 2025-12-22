package com.vinsys.hrms.kra.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vinsys.hrms.master.vo.SelfRatingVo;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectiveFeedbackDraftingAssistanceRequestVo {

	private EmployeeDetails employeeDetails;

	private String currentCycle;
	private String category;
	private String categoryWeight;
	private String subCategory;
    private String objId;
	private String objectives;
	private String objectiveWeight;
	private String metric;

	private Double employeeRating;
    private String employeeRatingLabel;
	private String employeeQualitativeAssessment;

	private Double reportingManagerRating;
    private String reportingManagerRatingLabel;
	private String reportingManagerQualitativeAssessment;

	public EmployeeDetails getEmployeeDetails() {
		return employeeDetails;
	}

	public void setEmployeeDetails(EmployeeDetails employeeDetails) {
		this.employeeDetails = employeeDetails;
	}

	public String getCurrentCycle() {
		return currentCycle;
	}

	public void setCurrentCycle(String currentCycle) {
		this.currentCycle = currentCycle;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategoryWeight() {
		return categoryWeight;
	}

	public void setCategoryWeight(String categoryWeight) {
		this.categoryWeight = categoryWeight;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

	public String getObjectives() {
		return objectives;
	}

	public void setObjectives(String objectives) {
		this.objectives = objectives;
	}

	public String getObjectiveWeight() {
		return objectiveWeight;
	}

	public void setObjectiveWeight(String objectiveWeight) {
		this.objectiveWeight = objectiveWeight;
	}

	public String getMetric() {
		return metric;
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}

	public Double getEmployeeRating() {
		return employeeRating;
	}

	public void setEmployeeRating(Double employeeRating) {
		this.employeeRating = employeeRating;
	}

    public String getEmployeeRatingLabel() {
        return employeeRatingLabel;
    }

    public void setEmployeeRatingLabel(String employeeRatingLabel) {
        this.employeeRatingLabel = employeeRatingLabel;
    }

    public String getEmployeeQualitativeAssessment() {
		return employeeQualitativeAssessment;
	}

	public void setEmployeeQualitativeAssessment(String employeeQualitativeAssessment) {
		this.employeeQualitativeAssessment = employeeQualitativeAssessment;
	}

	public String getReportingManagerQualitativeAssessment() {
		return reportingManagerQualitativeAssessment;
	}

	public void setReportingManagerQualitativeAssessment(String reportingManagerQualitativeAssessment) {
		this.reportingManagerQualitativeAssessment = reportingManagerQualitativeAssessment;
	}

	public Double getReportingManagerRating() {
		return reportingManagerRating;
	}

	public void setReportingManagerRating(Double reportingManagerRating) {
		this.reportingManagerRating = reportingManagerRating;
	}

    public String getReportingManagerRatingLabel() {
        return reportingManagerRatingLabel;
    }

    public void setReportingManagerRatingLabel(String reportingManagerRatingLabel) {
        this.reportingManagerRatingLabel = reportingManagerRatingLabel;
    }

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public class EmployeeDetails {
        private String id;
        private String name;
		private String designationName;
		private String departmentName;
		private String divisionName;
		private EmployeeDetails reportingManagerDetails;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

		public String getDesignationName() {
			return designationName;
		}

		public void setDesignationName(String designationName) {
			this.designationName = designationName;
		}

		public String getDepartmentName() {
			return departmentName;
		}

		public void setDepartmentName(String departmentName) {
			this.departmentName = departmentName;
		}

		public String getDivisionName() {
			return divisionName;
		}

		public void setDivisionName(String divisionName) {
			this.divisionName = divisionName;
		}

		public EmployeeDetails getReportingManagerDetails() {
			return reportingManagerDetails;
		}

		public void setReportingManagerDetails(EmployeeDetails reportingManagerDetails) {
			this.reportingManagerDetails = reportingManagerDetails;
		}
	}
}
