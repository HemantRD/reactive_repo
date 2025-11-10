package com.vinsys.hrms.kra.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vinsys.hrms.entity.AuditBase;
import com.vinsys.hrms.master.entity.KraRating;

@Entity
@Table(name = "tbl_trn_kra_details_history")
public class KraDetailsHistory extends AuditBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "seq_trn_kra_details_history", sequenceName = "seq_trn_kra_details_history", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_trn_kra_details_history")
	private Long id;

	@Column(name = "year")
	private String year;
	@Column(name = "weightage")
	private Float weightage;

	@Column(name = "kra_detail")
	private String kraDetails;

	@Column(name = "description")
	private String description;
	@Column(name = "measurement_criteria")
	private String measurementCriteria;
	@Column(name = "achievement_plan")
	private String achievementPlan;
	@Column(name = "rm_comment")
	private String rmComment;
	
	@Column(name = "category_id")
	private Long categoryId;
	@Column(name = "sub_category_id")
	private Long subcategoryId;
	@Column(name = "objective_weightage")
	private Float objectiveWeightage;
	@ManyToOne
	@JoinColumn(name= "self_rating")
	private KraRating selfRating;
	
	@ManyToOne
	@JoinColumn(name= "manager_rating")
	private KraRating managerRating;
	
	@Column(name = "self_qualitative_assessment")
	private String selfQaulitativeAssisment;
	@Column(name = "manager_qualitative_assessment")
	private String managerQaulitativeAssisment;

	@ManyToOne
	@JoinColumn(name = "kra_id")
	private Kra kra;
	
	
	
	@ManyToOne
	@JoinColumn(name = "mst_kra_cycle_id")
	private KraCycle kraCycle;
	
	@Column(name = "is_edit")
	private String isEdit="N";
	
	@Column(name = "is_color")
	private String isColor="N";
	
	@Column(name = "hcd_comment")
	private String hcdComment;
	
	@Column(name = "kra_detail_id")
	private Long kraDetailId;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKraDetails() {
		return kraDetails;
	}

	public void setKraDetails(String kraDetails) {
		this.kraDetails = kraDetails;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMeasurementCriteria() {
		return measurementCriteria;
	}

	public void setMeasurementCriteria(String measurementCriteria) {
		this.measurementCriteria = measurementCriteria;
	}

	public String getAchievementPlan() {
		return achievementPlan;
	}

	public void setAchievementPlan(String achievementPlan) {
		this.achievementPlan = achievementPlan;
	}

	public String getRmComment() {
		return rmComment;
	}

	public void setRmComment(String rmComment) {
		this.rmComment = rmComment;
	}

	public Kra getKra() {
		return kra;
	}

	public void setKra(Kra kra) {
		this.kra = kra;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Float getWeightage() {
		return weightage;
	}

	public void setWeightage(Float weightage) {
		this.weightage = weightage;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getSubcategoryId() {
		return subcategoryId;
	}

	public void setSubcategoryId(Long subcategoryId) {
		this.subcategoryId = subcategoryId;
	}

	public Float getObjectiveWeightage() {
		return objectiveWeightage;
	}

	public void setObjectiveWeightage(Float objectiveWeightage) {
		this.objectiveWeightage = objectiveWeightage;
	}

	

	public KraRating getSelfRating() {
		return selfRating;
	}

	public void setSelfRating(KraRating selfRating) {
		this.selfRating = selfRating;
	}

	public KraRating getManagerRating() {
		return managerRating;
	}

	public void setManagerRating(KraRating managerRating) {
		this.managerRating = managerRating;
	}

	public String getSelfQaulitativeAssisment() {
		return selfQaulitativeAssisment;
	}

	public void setSelfQaulitativeAssisment(String selfQaulitativeAssisment) {
		this.selfQaulitativeAssisment = selfQaulitativeAssisment;
	}

	public String getManagerQaulitativeAssisment() {
		return managerQaulitativeAssisment;
	}

	public void setManagerQaulitativeAssisment(String managerQaulitativeAssisment) {
		this.managerQaulitativeAssisment = managerQaulitativeAssisment;
	}

	
	public KraCycle getKraCycle() {
		return kraCycle;
	}

	public void setKraCycle(KraCycle kraCycle) {
		this.kraCycle = kraCycle;
	}

	public String getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
	}

	public String getIsColor() {
		return isColor;
	}

	public void setIsColor(String isColor) {
		this.isColor = isColor;
	}

	public String getHcdComment() {
		return hcdComment;
	}

	public void setHcdComment(String hcdComment) {
		this.hcdComment = hcdComment;
	}

	public Long getKraDetailId() {
		return kraDetailId;
	}

	public void setKraDetailId(Long kraDetailId) {
		this.kraDetailId = kraDetailId;
	}

	
}
