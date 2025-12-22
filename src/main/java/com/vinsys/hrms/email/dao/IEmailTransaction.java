package com.vinsys.hrms.email.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vinsys.hrms.email.entity.EmailTransaction;

/**
 * 
 * @author amey.gangakhedkar
 *
 */
@Repository
public interface IEmailTransaction extends JpaRepository<EmailTransaction, Long> {

	public List<EmailTransaction> findByStatusAndCategory(String status, String category);

}
