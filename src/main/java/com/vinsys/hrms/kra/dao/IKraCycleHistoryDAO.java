package com.vinsys.hrms.kra.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.kra.entity.KraCycleCalender;
import com.vinsys.hrms.kra.entity.KraCycleHistory;

public interface IKraCycleHistoryDAO extends JpaRepository<KraCycleHistory, Long>{

	

}
