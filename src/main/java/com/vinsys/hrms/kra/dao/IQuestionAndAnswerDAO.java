package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.kra.entity.QuestionAndAnswer;

public interface IQuestionAndAnswerDAO extends JpaRepository<QuestionAndAnswer, Long> {

	QuestionAndAnswer findByKraIdAndCategoryIdAndSubCategoryIdAndQuestionIdAndIsActive(Long kraId, Long categoryId,
			Long subCategoryId, Long questionId, String isActive);
	
	@Query("SELECT qa FROM QuestionAndAnswer qa " + "WHERE qa.kraId = :kraId " + "AND qa.categoryId = :categoryId "
			+ "AND qa.subCategoryId = :subCategoryId " + "AND qa.isActive = 'Y' " + "ORDER BY qa.questionId ASC")
	List<QuestionAndAnswer> findAnswerByFilters(Long kraId, Long categoryId, Long subCategoryId);

}
