package com.vinsys.hrms.kra.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Immutable
@Entity
@Table(name = "vw_per_rating_dashboard")
public class PercentageRatingView {

	@Id
	@Column(name = "rating")
	private String rating;
	
	@Column(name = "\"LEG\"")
	private String leg;
	
	@Column(name = "\"HCD\"")
	private String hcd;
	
	@Column(name = "\"PRT_FIN\"")
	private String prtFin;
	
	@Column(name = "\"CDO\"")
	private String cdo;
	
	@Column(name = "\"NBD\"")
	private String nbd;
	
	@Column(name = "\"CSM\"")
	private String csm;
	
	@Column(name = "\"AUD\"")
	private String aud;
	
	@Column(name = "\"TOTAL\"")
	private String total;
	
	@Column(name = "\"TOTAL_ACTUAL\"")
	private String totalActual;
	
	@Column(name = "\"TARGET\"")
	private String target;

	
	
	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getLeg() {
		return leg;
	}

	public void setLeg(String leg) {
		this.leg = leg;
	}

	public String getHcd() {
		return hcd;
	}

	public void setHcd(String hcd) {
		this.hcd = hcd;
	}

	public String getPrtFin() {
		return prtFin;
	}

	public void setPrtFin(String prtFin) {
		this.prtFin = prtFin;
	}

	public String getCdo() {
		return cdo;
	}

	public void setCdo(String cdo) {
		this.cdo = cdo;
	}

	public String getNbd() {
		return nbd;
	}

	public void setNbd(String nbd) {
		this.nbd = nbd;
	}

	public String getCsm() {
		return csm;
	}

	public void setCsm(String csm) {
		this.csm = csm;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getTotalActual() {
		return totalActual;
	}

	public void setTotalActual(String totalActual) {
		this.totalActual = totalActual;
	}

	

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getAud() {
		return aud;
	}

	public void setAud(String aud) {
		this.aud = aud;
	}

	
	
	
}
