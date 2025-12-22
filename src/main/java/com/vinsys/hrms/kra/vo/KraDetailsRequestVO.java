package com.vinsys.hrms.kra.vo;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class KraDetailsRequestVO {
	@Schema(required = true,description = "This field will use only for approve and reject kra")
	private Long kraId;
	@Schema(required = true)
	private List<KraDetailsVO> kraDetailsVOList;
	private String pendingWith;
	private String status;

	public List<KraDetailsVO> getKraDetailsVOList() {
		return kraDetailsVOList;
	}

	public void setKraDetailsVOList(List<KraDetailsVO> kraDetailsVOList) {
		this.kraDetailsVOList = kraDetailsVOList;
	}

	public String getPendingWith() {
		return pendingWith;
	}

	public void setPendingWith(String pendingWith) {
		this.pendingWith = pendingWith;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getKraId() {
		return kraId;
	}

	public void setKraId(Long kraId) {
		this.kraId = kraId;
	}
}
