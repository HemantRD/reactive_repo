package com.vinsys.hrms.constants;

public enum ERecordStatus {

	Y("Y"), N("N"),MIN_VALUE("MIN_VALUE"),MAX_VALUE("MAX_VALUE"),MAX("MAX"),MIN("MIN"), Cycle_First("Cycle_First");
	private final String recordStatus;

	
	ERecordStatus(String status) {
		this.recordStatus = status;
	}

	@Override
	public String toString() {
		return recordStatus;
	}
	
	
}
