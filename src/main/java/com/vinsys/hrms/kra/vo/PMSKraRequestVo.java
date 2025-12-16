package com.vinsys.hrms.kra.vo;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class PMSKraRequestVo {

	@Schema(required = true,description = "This field will use only for approve and reject kra")
	private Long kraId;
	@Schema(required = true)
	private List<PMSKraCategoryVo> category;
	private Long kraCycleId;
	public Long getKraId() {
		return kraId;
	}
	public void setKraId(Long kraId) {
		this.kraId = kraId;
	}
	
	public List<PMSKraCategoryVo> getCategory() {
		return category;
	}
	public void setCategory(List<PMSKraCategoryVo> category) {
		this.category = category;
	}
	
	public Long getKraCycleId() {
		return kraCycleId;
	}
	public void setKraCycleId(Long kraCycleId) {
		this.kraCycleId = kraCycleId;
	}
	
	
}
