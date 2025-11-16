package com.vinsys.hrms.kra.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.kra.entity.KraHistory;

public interface IKraHistoryDAO extends JpaRepository<KraHistory, Long>{

}
