package com.vinsys.hrms.reimbursement.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;

/**
 * @author akanksha.gaikwad
 */

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;
import com.vinsys.hrms.master.entity.MasterClaimType;
import com.vinsys.hrms.master.entity.MasterReimbursementTravelType;
import com.vinsys.hrms.master.entity.MasterStayType;

@Entity
@Table(name = "tbl_trn_reim_claims")
public class AddClaim extends AuditBase {
	
	

	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "seq_trn_reim_claims", sequenceName = "seq_trn_reim_claims", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_trn_reim_claims")

	@Column(name = "id")
	private Long id;
	@Column(name = "reim_id")
	private Long requestId;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "claim_type")
	private MasterClaimType claimType;
	
	@Column(name = "expense_date")
	private Date expenseDate;
	@Column(name = "from_date")
	private Date fromDate;
	@Column(name = "to_date")
	private Date toDate;
	@Column(name = "from_location")
	private String fromLocation;
	@Column(name = "to_location")
	private String toLocation;
	@Column(name = "expense_amount")
	private Float expenseAmount;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "travel_type")
	private MasterReimbursementTravelType travelType;
	@Column(name = "no_of_days")
	private int numberOfDays;
	@Column(name = "requester_comment")
	private String requesterComment;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "stay_type")
	private MasterStayType stayType;
	
	@Column(name = "hotel_name")
	private String hotelName;
	@Column(name = "room_no")
	private String roomNo;
	@Column(name = "rm_comment")
	private String rmComment;
	@Column(name = "rm_status")
	private String rmStatus;
	@Column(name = "accountant_comment")
	private String accountantComment;
	@Column(name = "accountant_status")
	private String accountantStatus;
	@Column(name = "daily_allowance")
	private String dailyAllowance;
	@Column(name = "stay_location")
	private String stayLocation;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getRequestId() {
		return requestId;
	}
	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}
	
	public Date getExpenseDate() {
		return expenseDate;
	}
	public void setExpenseDate(Date expenseDate) {
		this.expenseDate = expenseDate;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public String getFromLocation() {
		return fromLocation;
	}
	public void setFromLocation(String fromLocation) {
		this.fromLocation = fromLocation;
	}
	public String getToLocation() {
		return toLocation;
	}
	public void setToLocation(String toLocation) {
		this.toLocation = toLocation;
	}
	public Float getExpenseAmount() {
		return expenseAmount;
	}
	public void setExpenseAmount(Float expenseAmount) {
		this.expenseAmount = expenseAmount;
	}
	
	
	public int getNumberOfDays() {
		return numberOfDays;
	}
	public void setNumberOfDays(int numberOfDays) {
		this.numberOfDays = numberOfDays;
	}
	public String getRequesterComment() {
		return requesterComment;
	}
	public void setRequesterComment(String requesterComment) {
		this.requesterComment = requesterComment;
	}
	
	public String getHotelName() {
		return hotelName;
	}
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}
	public String getRoomNo() {
		return roomNo;
	}
	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
	public String getRmComment() {
		return rmComment;
	}
	public void setRmComment(String rmComment) {
		this.rmComment = rmComment;
	}
	public String getRmStatus() {
		return rmStatus;
	}
	public void setRmStatus(String rmStatus) {
		this.rmStatus = rmStatus;
	}
	public String getAccountantComment() {
		return accountantComment;
	}
	public void setAccountantComment(String accountantComment) {
		this.accountantComment = accountantComment;
	}
	public String getAccountantStatus() {
		return accountantStatus;
	}
	public void setAccountantStatus(String accountantStatus) {
		this.accountantStatus = accountantStatus;
	}
	public String getDailyAllowance() {
		return dailyAllowance;
	}
	public void setDailyAllowance(String dailyAllowance) {
		this.dailyAllowance = dailyAllowance;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public MasterClaimType getClaimType() {
		return claimType;
	}
	public void setClaimType(MasterClaimType claimType) {
		this.claimType = claimType;
	}
	public MasterReimbursementTravelType getTravelType() {
		return travelType;
	}
	public void setTravelType(MasterReimbursementTravelType travelType) {
		this.travelType = travelType;
	}
	public MasterStayType getStayType() {
		return stayType;
	}
	public void setStayType(MasterStayType stayType) {
		this.stayType = stayType;
	}
	public String getStayLocation() {
		return stayLocation;
	}
	public void setStayLocation(String stayLocation) {
		this.stayLocation = stayLocation;
	}
	
	
	
	
	
	
	
	

}
