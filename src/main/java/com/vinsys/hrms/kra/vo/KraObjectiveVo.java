package com.vinsys.hrms.kra.vo;

public class KraObjectiveVo {

	private long id;
	private String name;
	private String objectiveweight;
	private String metric;
	private SelfRatingResponseVo selfrating;
	private ManagerRatingResponseVo managerrating;
	private String selfqualitativeassessment;
	private String managerqaulitativeassisment;
	private String isEdit;
	private String isColor;
	private String hcdComment;
	private String viewComment;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getObjectiveweight() {
		return objectiveweight;
	}

	public void setObjectiveweight(String objectiveweight) {
		this.objectiveweight = objectiveweight;
	}

	public String getMetric() {
		return metric;
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}

	public SelfRatingResponseVo getSelfrating() {
		return selfrating;
	}

	public void setSelfrating(SelfRatingResponseVo selfrating) {
		this.selfrating = selfrating;
	}

	public String getSelfqualitativeassessment() {
		return selfqualitativeassessment;
	}

	public void setSelfqualitativeassessment(String selfqualitativeassessment) {
		this.selfqualitativeassessment = selfqualitativeassessment;
	}

	public String getManagerqaulitativeassisment() {
		return managerqaulitativeassisment;
	}

	public void setManagerqaulitativeassisment(String managerqaulitativeassisment) {
		this.managerqaulitativeassisment = managerqaulitativeassisment;
	}

	public ManagerRatingResponseVo getManagerrating() {
		return managerrating;
	}

	public void setManagerrating(ManagerRatingResponseVo managerrating) {
		this.managerrating = managerrating;
	}

	public String getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
	}

	public String getIsColor() {
		return isColor;
	}

	public void setIsColor(String isColor) {
		this.isColor = isColor;
	}

	public String getHcdComment() {
		return hcdComment;
	}

	public void setHcdComment(String hcdComment) {
		this.hcdComment = hcdComment;
	}

	public String getViewComment() {
		return viewComment;
	}

	public void setViewComment(String viewComment) {
		this.viewComment = viewComment;
	}
}
