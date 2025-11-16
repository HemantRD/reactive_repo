package com.vinsys.hrms.kra.vo;

import com.vinsys.hrms.master.vo.SelfRatingVo;

public class ObjectiveVo {
	private Long id;
	private String name;
	private Float objectiveweight;
	private String metric;
	private SelfRatingVo selfrating;
	private String selfqualitativeassessment;
	private SelfRatingResponseVo managerrating;
	private String managerqaulitativeassisment;

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

	public Float getObjectiveweight() {
		return objectiveweight;
	}

	public void setObjectiveweight(Float objectiveweight) {
		this.objectiveweight = objectiveweight;
	}

	public String getMetric() {
		return metric;
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}

	public SelfRatingVo getSelfrating() {
		return selfrating;
	}

	public void setSelfrating(SelfRatingVo selfrating) {
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

	public SelfRatingResponseVo getManagerrating() {
		return managerrating;
	}

	public void setManagerrating(SelfRatingResponseVo managerrating) {
		this.managerrating = managerrating;
	}

	
	
}
