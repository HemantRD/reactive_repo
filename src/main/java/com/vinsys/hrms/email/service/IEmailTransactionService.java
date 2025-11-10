package com.vinsys.hrms.email.service;

import org.springframework.stereotype.Service;

import com.vinsys.hrms.email.vo.EmailRequestVO;

/**
 * 
 * @author amey.gangakhedkar
 *
 */
@Service
public interface IEmailTransactionService {
	
	public void insertInEmailTxnTable(EmailRequestVO emailRequestVO) throws Exception;

}
