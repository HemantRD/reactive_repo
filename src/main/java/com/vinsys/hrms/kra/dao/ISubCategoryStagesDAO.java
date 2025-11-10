package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.kra.entity.SubCategoryStages;

public interface ISubCategoryStagesDAO extends JpaRepository<SubCategoryStages, Long> {

	List<SubCategoryStages> findByIsActive(String isactive);

	SubCategoryStages findByIsActiveAndId(String name, Long stageId);

}
