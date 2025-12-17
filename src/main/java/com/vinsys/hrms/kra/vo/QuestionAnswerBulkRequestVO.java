package com.vinsys.hrms.kra.vo;

import java.util.List;

public class QuestionAnswerBulkRequestVO {

	private Long kraId;
	private List<QuestionAndAnswerRequestVO> answers;
	public Long getKraId() {
		return kraId;
	}
	public void setKraId(Long kraId) {
		this.kraId = kraId;
	}
	public List<QuestionAndAnswerRequestVO> getAnswers() {
		return answers;
	}
	public void setAnswers(List<QuestionAndAnswerRequestVO> answers) {
		this.answers = answers;
	}

	
}
