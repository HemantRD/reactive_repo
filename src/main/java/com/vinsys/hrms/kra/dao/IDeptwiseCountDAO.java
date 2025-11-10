package com.vinsys.hrms.kra.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.kra.entity.DeptwiseCount;

public interface IDeptwiseCountDAO extends JpaRepository<DeptwiseCount, Long> {
	
	  List<DeptwiseCount> findAll();

}
