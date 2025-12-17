package com.vinsys.hrms.kra.vo;

import java.util.List;

public class DeleteDelegationMappingVO {
	
	private List<Long> id;

    private Long delegationForId;
    private String delegationFor;

    private Long delegationToId;
    private String delegationTo;

    private Long delegationBy;
    private Long cycleId;
	
	public List<Long> getId() {
		return id;
	}
	public void setId(List<Long> id) {
		this.id = id;
	}
	public Long getDelegationForId() {
		return delegationForId;
	}
	public void setDelegationForId(Long delegationForId) {
		this.delegationForId = delegationForId;
	}
	public String getDelegationFor() {
		return delegationFor;
	}
	public void setDelegationFor(String delegationFor) {
		this.delegationFor = delegationFor;
	}
	public Long getDelegationToId() {
		return delegationToId;
	}
	public void setDelegationToId(Long delegationToId) {
		this.delegationToId = delegationToId;
	}
	public String getDelegationTo() {
		return delegationTo;
	}
	public void setDelegationTo(String delegationTo) {
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

    

}
