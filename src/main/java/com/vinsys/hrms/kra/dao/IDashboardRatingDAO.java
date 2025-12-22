package com.vinsys.hrms.kra.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.kra.entity.DashboardRatingView;

public interface IDashboardRatingDAO extends JpaRepository<DashboardRatingView, Long> {

}
