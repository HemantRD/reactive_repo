package com.vinsys.hrms.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.entity.MasterLanguage;

public interface IHRMSMasterLanguageDAO extends JpaRepository<MasterLanguage, Long> {

	public List<MasterLanguage> findByisActive(String isActive,Sort sort);
}
