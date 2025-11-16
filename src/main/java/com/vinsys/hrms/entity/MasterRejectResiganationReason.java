package com.vinsys.hrms.entity;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_mst_reject_resiganation_reason")

public class MasterRejectResiganationReason extends AuditBase implements Serializable {
	@Id
	@SequenceGenerator(name = "seq_mst_reject_reason", sequenceName = "seq_mst_reject_reason", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mst_reject_reason")
	private long id;
	@Column(name = "reason_name")
	private String reasonName;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getReasonName() {
		return reasonName;
	}
	public void setReasonName(String reasonName) {
		this.reasonName = reasonName;
	}

}
