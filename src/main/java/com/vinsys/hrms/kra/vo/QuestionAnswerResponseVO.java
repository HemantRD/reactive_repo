package com.vinsys.hrms.kra.vo;

import java.util.List;

public class QuestionAnswerResponseVO {

    private Long kraId;
    private Long categoryId;
    private Long subCategoryId;

    private String categoryName;
    private String subCategoryName;

    private List<QuestionAnswerData> answers;

    public Long getKraId() {
        return kraId;
    }

    public void setKraId(Long kraId) {
        this.kraId = kraId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public List<QuestionAnswerData> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuestionAnswerData> answers) {
        this.answers = answers;
    }

    // -------- Inner Class -----------
    public static class QuestionAnswerData {

        private Long questionId;
        private String answer;
        private String questionText;

        public Long getQuestionId() {
            return questionId;
        }

        public void setQuestionId(Long questionId) {
            this.questionId = questionId;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getQuestionText() {
            return questionText;
        }

        public void setQuestionText(String questionText) {
            this.questionText = questionText;
        }

        @Override
        public String toString() {
            return "QuestionAnswerData{" +
                    "questionId=" + questionId +
                    ", answer='" + answer + '\'' +
                    ", questionText='" + questionText + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "QuestionAnswerResponseVO{" +
                "kraId=" + kraId +
                ", categoryId=" + categoryId +
                ", subCategoryId=" + subCategoryId +
                ", categoryName='" + categoryName + '\'' +
                ", subCategoryName='" + subCategoryName + '\'' +
                ", answers=" + answers +
                '}';
    }
}
