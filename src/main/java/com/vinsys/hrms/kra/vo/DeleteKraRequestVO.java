package com.vinsys.hrms.kra.vo;

import io.swagger.v3.oas.annotations.media.Schema;

public class DeleteKraRequestVO {

	@Schema(required = true)
	private Long kraId;
	@Schema(required = true)
	private Long kraDetailsId;

	public Long getKraId() {
		return kraId;
	}

	public void setKraId(Long kraId) {
		this.kraId = kraId;
	}

	public Long getKraDetailsId() {
		return kraDetailsId;
	}

	public void setKraDetailsId(Long kraDetailsId) {
		this.kraDetailsId = kraDetailsId;
	}
}
