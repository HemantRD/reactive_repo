package com.vinsys.hrms.kra.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Immutable
@Entity
@Table(name = "vw_rating_dashboard")
public class DashboardRatingView {

	@Id
	@Column(name = "rating")
	private String rating;
	
	@Column(name = "\"LEG\"")
	private Long leg;
	
	@Column(name = "\"HCD\"")
	private Long hcd;
	
	@Column(name = "\"PRT_FIN\"")
	private Long prtFin;
	
	@Column(name = "\"CDO\"")
	private Long cdo;
	
	@Column(name = "\"NBD\"")
	private Long nbd;
	
	@Column(name = "\"CSM\"")
	private Long csm;
	
	@Column(name = "\"AUD\"")
	private Long aud;
	
	@Column(name = "\"TOTAL\"")
	private Long total;
	
	@Column(name = "\"TOTAL_ACTUAL\"")
	private Long totalActual;
	
	@Column(name = "\"TARGET\"")
	private Long target;
	
	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public Long getLeg() {
		return leg;
	}

	public void setLeg(Long leg) {
		this.leg = leg;
	}

	public Long getHcd() {
		return hcd;
	}

	public void setHcd(Long hcd) {
		this.hcd = hcd;
	}

	public Long getCdo() {
		return cdo;
	}

	public void setCdo(Long cdo) {
		this.cdo = cdo;
	}

	public Long getNbd() {
		return nbd;
	}

	public void setNbd(Long nbd) {
		this.nbd = nbd;
	}

	public Long getCsm() {
		return csm;
	}

	public void setCsm(Long csm) {
		this.csm = csm;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Long getTotalActual() {
		return totalActual;
	}

	public void setTotalActual(Long totalActual) {
		this.totalActual = totalActual;
	}

	public Long getTarget() {
		return target;
	}

	public void setTarget(Long target) {
		this.target = target;
	}

	public Long getPrtFin() {
		return prtFin;
	}

	public void setPrtFin(Long prtFin) {
		this.prtFin = prtFin;
	}

	public Long getAud() {
		return aud;
	}

	public void setAud(Long aud) {
		this.aud = aud;
	}

	


	
	
}

