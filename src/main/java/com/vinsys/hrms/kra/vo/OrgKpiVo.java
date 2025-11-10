package com.vinsys.hrms.kra.vo;

public class OrgKpiVo {

	private String objective;
	private Double objectiveWeight;
	private KpiMetricVo metric;
	
	public String getObjective() {
		return objective;
	}
	public void setObjective(String objective) {
		this.objective = objective;
	}
	
	public Double getObjectiveWeight() {
		return objectiveWeight;
	}
	public void setObjectiveWeight(Double objectiveWeight) {
		this.objectiveWeight = objectiveWeight;
	}
	public KpiMetricVo getMetric() {
		return metric;
	}
	public void setMetric(KpiMetricVo metric) {
		this.metric = metric;
	}
	
	
	
	
}
