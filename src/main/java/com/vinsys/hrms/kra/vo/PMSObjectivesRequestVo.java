package com.vinsys.hrms.kra.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

/*Author Madhuri */

public class PMSObjectivesRequestVo {
	
	@JsonIgnore 
	private Long id;
	private Long kraId;
	private Long kraCycleId;
	private String name;
	private String comment;
	private String objectiveWeight;
	private PMSKraMetricVo metric;
	
	public Long getKraId() {
		return kraId;
	}
	public void setKraId(Long kraId) {
		this.kraId = kraId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
public String getObjectiveWeight() {
		return objectiveWeight;
	}
	public void setObjectiveWeight(String objectiveWeight) {
		this.objectiveWeight = objectiveWeight;
	}
	//	public Float getObjectiveWeight() {
//		return objectiveWeight;
//	}
//	public void setObjectiveWeight(Float objectiveWeight) {
//		this.objectiveWeight = objectiveWeight;
//	}
	public PMSKraMetricVo getMetric() {
		return metric;
	}
	public void setMetric(PMSKraMetricVo metric) {
		this.metric = metric;
	}
	public Long getKraCycleId() {
		return kraCycleId;
	}
	public void setKraCycleId(Long kraCycleId) {
		this.kraCycleId = kraCycleId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
	
}
