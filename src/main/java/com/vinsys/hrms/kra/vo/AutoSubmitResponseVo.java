package com.vinsys.hrms.kra.vo;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class AutoSubmitResponseVo {
	private String totalSelfRating;
	private String totalManagerRating;

	public String getTotalSelfRating() {
		return totalSelfRating;
	}

	public void setTotalSelfRating(String totalSelfRating) {
		this.totalSelfRating = totalSelfRating;
	}

	public String getTotalManagerRating() {
		return totalManagerRating;
	}

	public void setTotalManagerRating(String totalManagerRating) {
		this.totalManagerRating = totalManagerRating;
	}

}
