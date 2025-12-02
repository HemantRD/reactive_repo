package com.vinsys.hrms.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.CandidateAddress;

public interface IHRMSCandidateAddressDAO extends JpaRepository<CandidateAddress, Long> {

	/**
	 * @author monika
	 * @param candidateId
	 * @return
	 */
	@Query(value = "SELECT candidate_id\r\n"
			+ "     , MAX(CASE WHEN address_type='Permanent'    THEN address_line_1 END) Permanent\r\n"
			+ "     , MAX(CASE WHEN address_type='Present'   THEN  address_line_1 END) Present\r\n"
			+ "  FROM tbl_candidate_address\r\n" + "  where candidate_id=?1\r\n"
			+ " GROUP BY candidate_id;", nativeQuery = true)
	Object[][] findByAddressByCandidate(Long candidateId);

	@Query(value = "select * from tbl_candidate_address tca where candidate_id =?1 and is_active =?2 ", nativeQuery = true)
	List<CandidateAddress> findByCandidateIdAndIsActive(Long candidateId, String isActive);

	@Query(value = "select * from tbl_candidate_address tca where candidate_id =?1 and is_active =?2 and org_id =?3  ", nativeQuery = true)
	List<CandidateAddress> findByCandidateIdAndIsActiveAndOrgId(Long candidateId, String isActive, Long orgId);

	@Query(value="select * from tbl_candidate_address where candidate_id =?2 and address_type =?1",nativeQuery = true)
	public CandidateAddress findByAddressTypeAndCandidateId(String addressType, Long candidateId);
	
	 boolean existsByCountry_IdAndCountry_IsActive(Long countryId, String isActive);

	boolean existsByState_IdAndState_IsActive(Long id, String isactive);

	boolean existsByCity_IdAndCity_IsActive(Long id, String isactive);

	Optional<CandidateAddress> findByCandidateId(Long id);
	
	
	
}
