package com.vinsys.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="tbl_mst_notice_period")
public class MasterOrg_NoticePeriod extends AuditBase{
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_mst_notice_period", sequenceName = "seq_mst_notice_period", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_notice_period")
	private long id;
	@Column(name = "notice_period")
	private String noticePeriod;
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNoticePeriod() {
		return noticePeriod;
	}

	public void setNoticePeriod(String noticePeriod) {
		this.noticePeriod = noticePeriod;
	}

	
	
	

}
