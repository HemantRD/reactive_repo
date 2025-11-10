package com.vinsys.hrms.audit.service.Impl;

import javax.transaction.Transactional;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.vinsys.hrms.audit.dao.IAuditLogDao;
import com.vinsys.hrms.audit.entity.AuditLog;
import com.vinsys.hrms.util.ERequestMethod;

@Service
public class AsyncLoggerService {
	private final Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private IAuditLogDao auditLogDao;
	

	@Async
	public void testAsyncExe() {
		System.out.println("Async Thread:" + Thread.currentThread().getName());
		try {
			Thread.sleep(5000);
			System.out.println("Async Thread2:" + Thread.currentThread().getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Async
	@Transactional
	public void saveAuditData(AuditLog auditLog) {
		log.info("Saving audit data..");
		try {
			// TODO need to implement onky for post
			if (auditLog.getRequestMethod().equalsIgnoreCase(ERequestMethod.POST.name())) {
//				String curHash = generateHash(auditLog.toString());
				// auditLog.setHashId(curHash);
				auditLogDao.save(auditLog);
			}
			log.info("Audit data saved..");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/*
	 * private String generateHash(String rowHash) { return
	 * Hashing.sha256().hashString(rowHash, StandardCharsets.UTF_8).toString(); }
	 */

}
