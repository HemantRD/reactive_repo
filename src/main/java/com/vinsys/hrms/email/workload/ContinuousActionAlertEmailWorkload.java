package com.vinsys.hrms.email.workload;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.vinsys.hrms.email.dao.IEmailTransaction;
import com.vinsys.hrms.email.entity.EmailTransaction;
import com.vinsys.hrms.email.utils.EventsConstants;
import com.vinsys.hrms.email.workerthread.EmailWorkerThread;

/***
 * Continuously running OTPAlertEmailTransactionWorkload in sending
 * EmailTransaction to respective EmailTransaction address.
 * 
 * @author amey.gangakhedkar
 *
 */
@Component
public class ContinuousActionAlertEmailWorkload {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${scheduling.enabled}")
	private String schedulingEnabled;

	private static boolean isRunning = false;

	@Autowired
	private IEmailTransaction iEmailTransaction;

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * This method will read all pending EmailTransactions and send to target
	 * EmailTransactions
	 */
	@Scheduled(fixedRate = 2000)
	public void executeEmailTransactionCron() {

		if (schedulingEnabled.equals(EventsConstants.IS_ACTIVE)) {

			if (!isRunning) {

				synchronized (ContinuousActionAlertEmailWorkload.class) {
					isRunning = true;
				}

				List<EmailTransaction> emailTransactionList = iEmailTransaction.findByStatusAndCategory(
						EventsConstants.PENDING, EventsConstants.EMAIL_CATEGORY.ACTION_ALERTS.name());

				if (!emailTransactionList.isEmpty()) {
					try {
						ExecutorService executer = Executors.newFixedThreadPool(40);
						processMessages(emailTransactionList, executer);
						executer.shutdown();
						if (!executer.awaitTermination(Integer.MAX_VALUE, TimeUnit.MINUTES)) {
							executer.shutdownNow();
						}
						synchronized (ContinuousActionAlertEmailWorkload.class) {
							isRunning = false;
						}
					} catch (Exception e) {

					} finally {
						synchronized (ContinuousActionAlertEmailWorkload.class) {
							isRunning = false;
						}
					}
				} else {
					synchronized (ContinuousActionAlertEmailWorkload.class) {
						isRunning = false;
					}
				}
			}
		}
	}

	/*
	 * Splits the records in to even Threads and provides those list to each thread
	 * (by sublist)
	 */
	private List<List<EmailTransaction>> splitAndExecute(List<EmailTransaction> payload) {
		List<List<EmailTransaction>> result = new ArrayList<List<EmailTransaction>>();
		if (payload.size() <= 40) {
			result.add(payload);
		} else {
			result = createJobList(payload, 40);
			for (Iterator<List<EmailTransaction>> iterator = result.iterator(); iterator.hasNext();) {
				iterator.next();
			}
		}

		return result;
	}

	/*
	 * Split And Divide any List into event SUbsets and add the mod ot the last
	 * subset
	 */
	private List<List<EmailTransaction>> createJobList(List<EmailTransaction> payload, int availableThreads) {
		List<List<EmailTransaction>> returnList = new ArrayList<List<EmailTransaction>>();
		int sizeoflist = payload.size();
		int mod = payload.size() % availableThreads;
		int splitSize = sizeoflist / availableThreads;
		for (int i = 0; i < payload.size() - mod; i += splitSize) {
			List<EmailTransaction> sublist = payload.subList(i, Math.min(i + splitSize, payload.size()));
			if (Math.min(i + splitSize, payload.size()) == payload.size() - mod) {
				List<EmailTransaction> finalSublist = new LinkedList<EmailTransaction>();
				finalSublist.addAll(sublist);
				List<EmailTransaction> modData = payload.subList((payload.size() - mod), payload.size());
				finalSublist.addAll(modData);
				returnList.add(finalSublist);
			} else {
				returnList.add(sublist);
			}
		}
		return returnList;
	}

	private void processMessages(List<EmailTransaction> jobList, ExecutorService executer) {
		if (jobList != null && !jobList.isEmpty()) {
			List<List<EmailTransaction>> splitList = splitAndExecute(jobList);
			try {
				splitList.forEach(eachResult -> {
					
					log.info("Each Result:" + eachResult.toString());
					EmailWorkerThread th = null;
					th = applicationContext.getBean(EmailWorkerThread.class);
					th.setInputPayload(eachResult);
					executer.execute(th);
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
