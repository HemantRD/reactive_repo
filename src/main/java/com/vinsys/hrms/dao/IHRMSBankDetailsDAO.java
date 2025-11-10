package com.vinsys.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.BankDetails;
import com.vinsys.hrms.entity.Candidate;



 public interface IHRMSBankDetailsDAO  extends JpaRepository<BankDetails, Long>{
	
        public BankDetails findByCandidate(Candidate candidate);
        
        public BankDetails findByCandidateId(Long candidateId);
        
        @Query(value="select * from tbl_candidate_bank_details where candidate_id =?1 and org_id =?2",nativeQuery = true)
        public BankDetails findByCandidateIdAndOrgId(Long candidateId ,Long orgId);
}
