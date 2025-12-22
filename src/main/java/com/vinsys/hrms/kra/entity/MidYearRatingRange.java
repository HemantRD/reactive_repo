package com.vinsys.hrms.kra.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_mid_year_rating_range")
public class MidYearRatingRange {
	
	@Id
	@Column(name = "id")
	private Long id;

	@Column(name = "low_point")
	private Double lowPoint;

	@Column(name = "high_point")
	private Double highPoint;
	
	@Column(name = "rating")
	private String rating;

	@Column(name = "org_id")
	private long orgId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getLowPoint() {
		return lowPoint;
	}

	public void setLowPoint(Double lowPoint) {
		this.lowPoint = lowPoint;
	}

	public Double getHighPoint() {
		return highPoint;
	}

	public void setHighPoint(Double highPoint) {
		this.highPoint = highPoint;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

}
