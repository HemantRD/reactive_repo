package com.vinsys.hrms.kra.vo;

public class PMSObjectiveVo {

	private Long id;
	private Long kraId;
	private Long kraCycleId;
	private String name;
	private String objectiveWeight;
	private String metric;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getKraId() {
		return kraId;
	}
	public void setKraId(Long kraId) {
		this.kraId = kraId;
	}
	public Long getKraCycleId() {
		return kraCycleId;
	}
	public void setKraCycleId(Long kraCycleId) {
		this.kraCycleId = kraCycleId;
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
	public String getMetric() {
		return metric;
	}
	public void setMetric(String metric) {
		this.metric = metric;
	}
	
	
}
