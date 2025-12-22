package com.vinsys.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.entity.FeedbackOption;

public interface IHRMSFeedbackOptionDAO extends JpaRepository<FeedbackOption, Long> {

}
