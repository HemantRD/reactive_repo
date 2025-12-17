package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.entity.FeedbackQuestion;

public interface IHRMSFeedbackQuestionDAO extends JpaRepository<FeedbackQuestion, Long> {

	/**
	 * this method returns all feedback questions masters by organization id
	 * 
	 * @param Long orgId
	 * @return list of FeedbackQuestion
	 * @author shinde.devendra
	 * 
	 */
	@Query("select feedQue from FeedbackQuestion feedQue " + " left join fetch feedQue.feedbackOptions feedOpt"
			+ " where feedQue.orgId = ?1 and feedQue.isActive = ?2 "
			// + "and feedOpt.isActive = ?3"
			+ " order by feedQue.sequenceNumber asc")
	public List<FeedbackQuestion> findAllFeedbackQuestionAndOptionByOrgIdCustomQuery(long orgId,
			String isActiveQue/* , String isActiveOpt */);

	@Query("update FeedbackQuestion feedQue set feedQue.isActive = 'N' where feedQue.id = ?1")
	public List<FeedbackQuestion> deleteFeedbackQuestionByIdCustomQuery(long feedbackQuestionId);
}
