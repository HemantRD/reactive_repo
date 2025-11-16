package com.vinsys.hrms.entity.attendance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name="SmartAlarmWithAccess_BlankNew.dbo.vw_Transactions_Swipes")
public class AttendanceSwipeData {

	@Id
	@Column(name = "transaction_id")
	private long transactionId;
	
	@Column(name = "emp_code")
	private String employeeCode;
		
	@Column(name = "card_no")
	private long cardNo;
	
	@Column(name = "channel_no")
	private long channelNo;
	
	//@Temporal(TemporalType.DATE)
	@Column(name = "swipe_date")
	private String swipeDate;

	@Column(name = "controller_no")
	private long controllerNo;
	
	@Column(name = "card_holder_name")
	private String cardHolderName;

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public long getCardNo() {
		return cardNo;
	}

	public void setCardNo(long cardNo) {
		this.cardNo = cardNo;
	}

	public long getChannelNo() {
		return channelNo;
	}

	public void setChannelNo(long channelNo) {
		this.channelNo = channelNo;
	}

	public String getSwapDate() {
		return swipeDate;
	}

	public void setSwapDate(String swipeDate) {
		this.swipeDate = swipeDate;
	}

	public long getControllerNo() {
		return controllerNo;
	}

	public void setControllerNo(long controllerNo) {
		this.controllerNo = controllerNo;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}
	
	}
