package com.vinsys.hrms.kra.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.vinsys.hrms.entity.AuditBase;


/*Auth
 * Kailas B
 */



@Entity
@Table(name = "tbl_txn_delegation_mapping", schema = "public")
public class DelegationMapping extends AuditBase {

	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_txn_delegation_mapping", sequenceName = "seq_txn_delegation_mapping", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_txn_delegation_mapping")
	private Long id;
	@Column(name = "delegation_for")
	private Long delegationFor;
	@Column(name = "delegation_to")
	private Long delegationTo;
	@Column(name = "delegation_by")
	private Long delegationBy;
	@Column(name = "mst_cycle_id")
	private Long cycleId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getDelegationFor() {
		return delegationFor;
	}
	public void setDelegationFor(Long delegationFor) {
		this.delegationFor = delegationFor;
	}
	public Long getDelegationTo() {
		return delegationTo;
	}
	public void setDelegationTo(Long delegationTo) {
		this.delegationTo = delegationTo;
	}
	public Long getDelegationBy() {
		return delegationBy;
	}
	public void setDelegationBy(Long delegationBy) {
		this.delegationBy = delegationBy;
	}
	public Long getCycleId() {
		return cycleId;
	}
	public void setCycleId(Long cycleId) {
		this.cycleId = cycleId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
