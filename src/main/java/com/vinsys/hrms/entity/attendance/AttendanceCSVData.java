package com.vinsys.hrms.entity.attendance;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.vinsys.hrms.entity.AuditBase;

@Entity
@Table(name = "tbl_attendance_csv_data")
public class AttendanceCSVData extends AuditBase {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "hash_id")
	private long hashId;
	@Column(name = "event_id")
	private long eventId;

	@Temporal(TemporalType.DATE)
	@Column(name = "swap_date")
	private Date swapDate;

	public Date getSwapDate() {
		return swapDate;
	}

	public void setSwapDate(Date swapDate) {
		this.swapDate = swapDate;
	}

	@Column(name = "swap_time")
	private Date swapTime;
	
	@Column(name = "panel_id")
	private String panelID;
	@Column(name = "event_point")
	private String eventPoint;
	
	//@Column(name = "panel_id")
	//private long panelID;
	//@Column(name = "event_point")
	//private long eventPoint;
	
	@Column(name = "event_point_name")
	private String eventPointName;

	@Column(name = "description")
	private String description;
	@Column(name = "card_no")
	private long cardNo;
	@Column(name = "card_holder_name")
	private String cardHolderName;

//	@Column(name = "org_id")
//	private long orgId;

	@Column(name = "transaction_id")
	private long transactionId;
	
	@Column(name = "employee_id")
	private long employeeId;
	
	@Column(name = "is_processed")
	private boolean isProcessed;

	public long getHashId() {
		return hashId;
	}

	public void setHashId(long hashId) {
		this.hashId = hashId;
	}

//	public long getEventPoint() {
//		return eventPoint;
//	}
//
//	public void setEventPoint(long eventPoint) {
//		this.eventPoint = eventPoint;
//	}
	
	public String getEventPoint() {
		return eventPoint;
	}

	public void setEventPoint(String eventPoint) {
		this.eventPoint = eventPoint;
	}
	
	

	public String getEventPointName() {
		return eventPointName;
	}

	public void setEventPointName(String eventPointName) {
		this.eventPointName = eventPointName;
	}

//	public long getOrgId() {
//		return orgId;
//	}
//
//	public void setOrgId(long orgId) {
//		this.orgId = orgId;
//	}

	public boolean isProcessed() {
		return isProcessed;
	}

	public void setProcessed(boolean isProcessed) {
		this.isProcessed = isProcessed;
	}

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	public Date getSwapTime() {
		return swapTime;
	}

	public void setSwapTime(Date swapTime) {
		this.swapTime = swapTime;
	}

//	public long getPanelID() {
//		return panelID;
//	}
//
//	public void setPanelID(long panelID) {
//		this.panelID = panelID;
//	}
	
	public String getPanelID() {
		return panelID;
	}

	public void setPanelID(String panelID) {
		this.panelID = panelID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getCardNo() {
		return cardNo;
	}

	public void setCardNo(long cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

}
