package com.vinsys.hrms.kra.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.kra.entity.PercentageRatingView;

public interface IPercentageRatingDAO extends JpaRepository<PercentageRatingView, String> {

}
