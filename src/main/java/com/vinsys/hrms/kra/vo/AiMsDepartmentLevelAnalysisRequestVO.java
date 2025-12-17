package com.vinsys.hrms.kra.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AiMsDepartmentLevelAnalysisRequestVO {

    private String isDryRun;
    private String purpose;
    private String forRole;
    private Long cycleId;
    private String cycleName;
    private Long[] departmentIds;
    private List<DepartmentDataVo> departments;

    public String getIsDryRun() {
        return isDryRun;
    }

    public void setIsDryRun(String isDryRun) {
        this.isDryRun = isDryRun;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getForRole() {
        return forRole;
    }

    public void setForRole(String forRole) {
        this.forRole = forRole;
    }

    public Long getCycleId() {
        return cycleId;
    }

    public void setCycleId(Long cycleId) {
        this.cycleId = cycleId;
    }

    public String getCycleName() {
        return cycleName;
    }

    public void setCycleName(String cycleName) {
        this.cycleName = cycleName;
    }

    public Long[] getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(Long[] departmentIds) {
        this.departmentIds = departmentIds;
    }

    public List<DepartmentDataVo> getDepartments() {
        return departments;
    }

    public void setDepartments(List<DepartmentDataVo> departments) {
        this.departments = departments;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DepartmentDataVo {
        private Long id;
        private String departmentName;
        private Double departmentAvgRating;
        private String departmentAvgRatingLabel;
        private Double departmentAvgRatingTarget;
        private String departmentAvgRatingTargetLabel;
        private List<EmployeeDataVo> departmentEmployees;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        public Double getDepartmentAvgRating() {
            return departmentAvgRating;
        }

        public void setDepartmentAvgRating(Double departmentAvgRating) {
            this.departmentAvgRating = departmentAvgRating;
        }

        public String getDepartmentAvgRatingLabel() {
            return departmentAvgRatingLabel;
        }

        public void setDepartmentAvgRatingLabel(String departmentAvgRatingLabel) {
            this.departmentAvgRatingLabel = departmentAvgRatingLabel;
        }

        public Double getDepartmentAvgRatingTarget() {
            return departmentAvgRatingTarget;
        }

        public void setDepartmentAvgRatingTarget(Double departmentAvgRatingTarget) {
            this.departmentAvgRatingTarget = departmentAvgRatingTarget;
        }

        public String getDepartmentAvgRatingTargetLabel() {
            return departmentAvgRatingTargetLabel;
        }

        public void setDepartmentAvgRatingTargetLabel(String departmentAvgRatingTargetLabel) {
            this.departmentAvgRatingTargetLabel = departmentAvgRatingTargetLabel;
        }

        public List<EmployeeDataVo> getDepartmentEmployees() {
            return departmentEmployees;
        }

        public void setDepartmentEmployees(List<EmployeeDataVo> departmentEmployees) {
            this.departmentEmployees = departmentEmployees;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class EmployeeDataVo {
        private Long kraId;
        private Long employeeId;
        private EmployeeDetailsVo employeeDetails;
        private Double avgSelfRating;
        private String avgSelfRatingLabel;
        private Double avgManagerRating;
        private String avgManagerRatingLabel;
        private String hodAiComments;
        private List<ObjectiveDataVo> objectives;

        public Long getKraId() {
            return kraId;
        }

        public void setKraId(Long kraId) {
            this.kraId = kraId;
        }

        public Long getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(Long employeeId) {
            this.employeeId = employeeId;
        }

        public EmployeeDetailsVo getEmployeeDetails() {
            return employeeDetails;
        }

        public void setEmployeeDetails(EmployeeDetailsVo employeeDetails) {
            this.employeeDetails = employeeDetails;
        }

        public Double getAvgSelfRating() {
            return avgSelfRating;
        }

        public void setAvgSelfRating(Double avgSelfRating) {
            this.avgSelfRating = avgSelfRating;
        }

        public String getAvgSelfRatingLabel() {
            return avgSelfRatingLabel;
        }

        public void setAvgSelfRatingLabel(String avgSelfRatingLabel) {
            this.avgSelfRatingLabel = avgSelfRatingLabel;
        }

        public Double getAvgManagerRating() {
            return avgManagerRating;
        }

        public void setAvgManagerRating(Double avgManagerRating) {
            this.avgManagerRating = avgManagerRating;
        }

        public String getAvgManagerRatingLabel() {
            return avgManagerRatingLabel;
        }

        public void setAvgManagerRatingLabel(String avgManagerRatingLabel) {
            this.avgManagerRatingLabel = avgManagerRatingLabel;
        }

        public String getHodAiComments() {
            return hodAiComments;
        }

        public void setHodAiComments(String hodAiComments) {
            this.hodAiComments = hodAiComments;
        }

        public List<ObjectiveDataVo> getObjectives() {
            return objectives;
        }

        public void setObjectives(List<ObjectiveDataVo> objectives) {
            this.objectives = objectives;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class EmployeeDetailsVo {
        private Long id;
        private String name;
        private String designation;
        private String department;
        private String division;
        private EmployeeDetailsVo reportingTo;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getDivision() {
            return division;
        }

        public void setDivision(String division) {
            this.division = division;
        }

        public EmployeeDetailsVo getReportingTo() {
            return reportingTo;
        }

        public void setReportingTo(EmployeeDetailsVo reportingTo) {
            this.reportingTo = reportingTo;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ObjectiveDataVo {
        private Long id;
        private String category;
        private String categoryWeight;
        private String subcategory;
        private String objective;
        private String objectiveWeight;
        private String metric;
        private Double selfRating;
        private String selfRatingLabel;
        private String selfQualitativeAssessment;
        private Double managerRating;
        private String managerRatingLabel;
        private String managerQualitativeAssessment;
        private String aiFeedbackForManager;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
        
        public Double getSelfRating() {
            return selfRating;
        }

        public void setSelfRating(Double selfRating) {
            this.selfRating = selfRating;
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

        public String getSubcategory() {
            return subcategory;
        }

        public void setSubcategory(String subcategory) {
            this.subcategory = subcategory;
        }

        public String getObjective() {
            return objective;
        }

        public void setObjective(String objective) {
            this.objective = objective;
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

        public String getSelfRatingLabel() {
            return selfRatingLabel;
        }

        public void setSelfRatingLabel(String selfRatingLabel) {
            this.selfRatingLabel = selfRatingLabel;
        }

        public String getSelfQualitativeAssessment() {
            return selfQualitativeAssessment;
        }

        public void setSelfQualitativeAssessment(String selfQualitativeAssessment) {
            this.selfQualitativeAssessment = selfQualitativeAssessment;
        }

        public Double getManagerRating() {
            return managerRating;
        }

        public void setManagerRating(Double managerRating) {
            this.managerRating = managerRating;
        }

        public String getManagerRatingLabel() {
            return managerRatingLabel;
        }

        public void setManagerRatingLabel(String managerRatingLabel) {
            this.managerRatingLabel = managerRatingLabel;
        }

        public String getManagerQualitativeAssessment() {
            return managerQualitativeAssessment;
        }

        public void setManagerQualitativeAssessment(String managerQualitativeAssessment) {
            this.managerQualitativeAssessment = managerQualitativeAssessment;
        }

        public String getAiFeedbackForManager() {
            return aiFeedbackForManager;
        }

        public void setAiFeedbackForManager(String aiFeedbackForManager) {
            this.aiFeedbackForManager = aiFeedbackForManager;
        }
    }
}
