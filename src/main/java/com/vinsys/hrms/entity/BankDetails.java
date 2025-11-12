package com.vinsys.hrms.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_candidate_bank_details")
public class BankDetails extends AuditBase{
	
	private static final long serialVersionUID = 1L;
	    @Id
		@SequenceGenerator(name = "seq_candidate_bank_details", sequenceName = "seq_candidate", initialValue = 1, allocationSize = 1)
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_candidate_bank_details")
		private Long id;
		
		@Column(name = "account_number")
		private String accountNumber;
		
		@Column(name = "bank_name")
		private String bankName;
		
		@Column(name = "branch_location")
		private String branchLocation;
		
		@Column(name = "ifsc_code")
		private String ifscCode;
		
		@Column(name = "name_as_per_bank")
		private String nameAsPerBank;
		
		@Column(name = "phone_number")
		private String phoneNumber;

		@OneToOne(cascade = CascadeType.ALL)
		@JoinColumn(name = "candidate_id")
		private Candidate candidate;
		
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getAccountNumber() {
			return accountNumber;
		}

		public void setAccountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
		}

		public String getBankName() {
			return bankName;
		}

		public void setBankName(String bankName) {
			this.bankName = bankName;
		}

		public String getBranchLocation() {
			return branchLocation;
		}

		public void setBranchLocation(String branchLocation) {
			this.branchLocation = branchLocation;
		}

		public String getIfscCode() {
			return ifscCode;
		}

		public void setIfscCode(String ifscCode) {
			this.ifscCode = ifscCode;
		}

		public String getNameAsPerBank() {
			return nameAsPerBank;
		}

		public void setNameAsPerBank(String nameAsPerBank) {
			this.nameAsPerBank = nameAsPerBank;
		}

		

		public String getPhoneNumber() {
			return phoneNumber;
		}

		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

		public Candidate getCandidate() {
			return candidate;
		}

		public void setCandidate(Candidate candidate) {
			this.candidate = candidate;
		}
		
		
		
		

		
	
	

}
