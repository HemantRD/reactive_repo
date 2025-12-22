package com.vinsys.hrms.email.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.vinsys.hrms.email.dao.IEmailTransaction;
import com.vinsys.hrms.email.entity.EmailTransaction;
import com.vinsys.hrms.email.service.IEmailTransactionService;
import com.vinsys.hrms.email.utils.EventsConstants;
import com.vinsys.hrms.email.vo.EmailRequestVO;
import com.vinsys.hrms.email.vo.PlaceHolderVO;
import com.vinsys.hrms.emailtemplate.dao.IEmailTemplateDao;
import com.vinsys.hrms.emailtemplate.entity.EmailTemplateEntity;

/**
 * 
 * @author amey.gangakhedkar
 *
 */

@Service
public class EmailTransactionServiceImpl implements IEmailTransactionService {
	
	@Autowired
	private IEmailTemplateDao iEmailTemplateDao;
	
	@Autowired
	private IEmailTransaction iEmailTransaction;

	@Override

	public void insertInEmailTxnTable(EmailRequestVO emailRequestVO) throws Exception {
		
		EmailTemplateEntity emailTemplate = iEmailTemplateDao.findByTemplateName
				(emailRequestVO.getTemplateVo().getTemplateName());
		
		if(emailTemplate!=null) {
			EmailTransaction email = new EmailTransaction();
			Map<String, String> replacementMap = new HashMap<>();
			List<PlaceHolderVO> placeHolder = emailRequestVO.getTemplateVo().getPlaceHolders();
			if (placeHolder != null && !placeHolder.isEmpty()) {
				Iterator<PlaceHolderVO> placeHolderIterator = placeHolder.iterator();
				while (placeHolderIterator.hasNext()) {
					PlaceHolderVO placeHolderVO = placeHolderIterator.next();
					replacementMap.put("{"+placeHolderVO.getExpressionVariableName()+"}", placeHolderVO.getExpressionVariableValue());
				}
			}
			
			email.setSubject(emailTemplate.getSubject());
			email.setMailBody(EventsConstants.replaceString(replacementMap, emailTemplate.getTemplateValue()));
			email.setTargetEmailAddress(emailRequestVO.getEmailAddress().trim());
			email.setCategory(emailRequestVO.getEmailCategory());
			email.setStatus(EventsConstants.PENDING);
			iEmailTransaction.save(email);
		}
		
	}

}
