package com.vinsys.hrms.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.entity.Subscription;

public interface IHRMSSubscriptionDAO extends JpaRepository<Subscription, Long> {

	//public Subscription findByorganization(Organization org);

}
