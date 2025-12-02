package com.vinsys.hrms.master.vo;

import java.util.List;

import com.vinsys.hrms.kra.vo.KpiMetricVo;

public class KpiMetricListResponseVO {
	
	private List<KpiMetricVo> metricList;

	public List<KpiMetricVo> getMetricList() {
		return metricList;
	}

	public void setMetricList(List<KpiMetricVo> metricList) {
		this.metricList = metricList;
	}
	

}
