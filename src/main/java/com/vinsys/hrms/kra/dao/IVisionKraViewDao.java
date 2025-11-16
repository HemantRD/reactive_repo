package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.kra.entity.VisionKraDetailsView;

public interface IVisionKraViewDao extends JpaRepository<VisionKraDetailsView, Long>{

	List<VisionKraDetailsView> findByKraIdAndIsActive(Long kraid, String isActive);

	VisionKraDetailsView findByIdAndIsActive(long id, String isActive);

}
