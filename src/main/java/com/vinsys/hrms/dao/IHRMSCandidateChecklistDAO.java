package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateChecklist;

public interface IHRMSCandidateChecklistDAO extends JpaRepository<CandidateChecklist, Long> {

	@Query("select resp from CandidateChecklist resp where resp.candidateProfessionalDetail.id=?1 and resp.checklistItem=?2")
	public CandidateChecklist findCandidateCheckListByProfessionalId(long candidateProfessionalDetailId,
			String checkListItem);

	/**
	 * 
	 * @param candidateId
	 * @return
	 * 
	 *         This method is added because : the find one on Candidate entity
	 *         doesn't return details of masterCandidateChecklistAction
	 */
	@Query("SELECT candidate FROM Candidate candidate "
			+ " JOIN FETCH candidate.candidateProfessionalDetail profDetail "
			+ " JOIN FETCH profDetail.candidateChecklists candCL "
			+ " LEFT JOIN FETCH candCL.masterCandidateChecklistAction mstCandCheckListAction "
			+ " WHERE candidate.id = ?1 and candCL.isDocumentOnlyForCandidate = ?2 ")
	public Candidate getCandidateWithChecklistDetailsForCand(long candidateId, int isDocForCandidate);

	@Query("SELECT candidate FROM Candidate candidate "
			+ " JOIN FETCH candidate.candidateProfessionalDetail profDetail "
			+ " JOIN FETCH profDetail.candidateChecklists candCL "
			+ " LEFT JOIN FETCH candCL.masterCandidateChecklistAction mstCandCheckListAction "
			+ " WHERE candidate.id = ?1 and candCL.isDocumentOnlyForEmployee = ?2 ")
	public Candidate getCandidateWithChecklistDetailsForEmp(long candidateId, int isDocForEmployee);

	@Query(" SELECT cc FROM Candidate cand JOIN cand.candidateProfessionalDetail cpd "
			+ " JOIN cpd.candidateChecklists cc WHERE cc.checklistItem = ?1 AND cand.id = ?2 ")
	public CandidateChecklist getCandidateWithChecklistDetailsByItem(String checklistItem, long candidateId);

//
//	@Query(" SELECT cc FROM Candidate cand JOIN cand.candidateProfessionalDetail cpd "
//			+ " JOIN cpd.candidateChecklists cc WHERE cc.checklistItem In(select checklist_item from tbl_mst_candidate_checklist where division_id =?2 and tab_name=?3) AND cand.id = ?1 ")
//	public CandidateChecklist getCandidateWithChecklistDetailsByItemId(long candidateId, long divId,String tabName);

//	@Query("SELECT * FROM Candidate cand JOIN cand.candidateProfessionalDetail cpd "
//	        + "JOIN cpd.candidateChecklists cc WHERE cc.checklistItem IN "
//	        + "(SELECT checklist_item FROM tbl_mst_candidate_checklist WHERE division_id = ?2 AND tab_name = ?3) "
//	        + "AND cand.id = ?1")
//	public List<CandidateChecklist> getCandidateWithChecklistDetailsByItemId2(long candidateId, long divId, String tabName);

//	@Query(value = "SELECT cc.* FROM Candidate cand JOIN cand.candidateProfessionalDetail cpd "
//            + "JOIN cpd.candidateChecklists cc WHERE cc.checklistItem IN "
//            + "(SELECT * FROM tbl_mst_candidate_checklist WHERE division_id = ?2 AND tab_name = ?3) "
//            + "AND cand.id = ?1", nativeQuery = true)
//public List<CandidateChecklist> getCandidateWithChecklistDetailsByItemId4(long candidateId, long divId, String tabName);

	@Query(value = "SELECT cc.* FROM tbl_candidate_checklist cc " + "WHERE cc.checklist_item IN "
			+ "(SELECT mst.checklist_item FROM tbl_mst_candidate_checklist mst "
			+ "WHERE mst.division_id = ?2 AND mst.tab_name = ?3) "
			+ "AND cc.candidate_professional_detail_id = ?1", nativeQuery = true)
	public List<CandidateChecklist> getCandidateWithChecklistDetailsByItemId(long candidateId, long divId,
			String tabName);

	@Query(value = "SELECT cc.* FROM tbl_candidate_checklist cc " + "WHERE cc.checklist_item IN "
			+ "(SELECT mst.checklist_item FROM tbl_mst_candidate_checklist mst "
			+ "WHERE mst.division_id = ?2 AND mst.tab_name = ?3) "
			+ "AND cc.candidate_professional_detail_id = ?1 And cc.org_id = ?4 ", nativeQuery = true)
	public List<CandidateChecklist> getCandidateWithChecklistDetailsByItemIdAndOrgId(long candidateId, long divId,
			String tabName, Long orgId);

	
	@Query(value = "SELECT cc.* FROM tbl_candidate_checklist cc " + "WHERE cc.checklist_item IN "
			+ "(SELECT mst.checklist_item FROM tbl_mst_candidate_checklist mst "
			+ "WHERE mst.tab_name = ?2) "
			+ "AND cc.candidate_professional_detail_id = ?1", nativeQuery = true)
	public List<CandidateChecklist> getCandidateWithChecklistDetailsByCandidateIdAndItemId(long candidateId, 
			String tabName);
	
	@Query("select resp from CandidateChecklist resp where resp.candidateProfessionalDetail.id=?1 and resp.checklistItem=?2 and resp.isActive=?3")
	public CandidateChecklist findCandidateCheckListByProfessionalIdAndIsActive(long candidateProfessionalDetailId,
			String checkListItem, String isActive);
}
