package com.vinsys.hrms.dao.hractivity;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinsys.hrms.entity.dashboard.Events;
/**
 * 
 * @author monika
 * @date
 * @description 
 *
 */
public interface IHRMSEventDAO extends JpaRepository<Events,Long> {
	
	Events findByEventNameAndIsActive(String eventName,String isActive);

}
