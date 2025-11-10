
package com.vinsys.hrms.util;

import java.io.IOException;
import java.text.ParseException;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.vinsys.hrms.idp.schedulers.IdpQueueEmailService;
import com.vinsys.hrms.idp.schedulers.IdpSendEmailService;
import com.vinsys.hrms.spring.BackendProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vinsys.hrms.employee.service.impl.LeaveServiceImpl;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.kra.service.impl.KraServiceImpl;
import com.vinsys.hrms.services.CandidateProfessionalDetailsService;
import com.vinsys.hrms.services.CandidateService;
import com.vinsys.hrms.services.EmployeeApplyLeaveService;
import com.vinsys.hrms.services.EmployeeLeaveDetailsService;
import com.vinsys.hrms.services.EmployeeSeparationDetailsService;
import com.vinsys.hrms.services.EmployeeService;
import com.vinsys.hrms.services.LoginService;
import com.vinsys.hrms.services.attendance.CSVUploaderService;
import com.vinsys.hrms.services.attendance.SendRemiderEmails;
import com.vinsys.hrms.traveldesk.service.impl.TravelDeskServiceImpl;

@Component
//@PropertySource(value="${HRMSCONFIG}")
@Transactional
public class ScheduledTasks {

	public static String TOKEN_KEY = "VINHRMS";

	@Value("${birthday_scheduler}")
	private String birthdaySchedulerSwitch;
	
	@Value("${compoff_flush}")
	private String compOffFlush;

	@Value("${service_completion_scheduler}")
	private String serviceCompletionSchedulerSwitch;
	@Value("${email_sender}")
	private String emailSenderSchedulerSwitch;

	@Value("${separation_remainder_scheduler}")
	private String separationRemainderSwitch;

	@Value("${leave_remainder_scheduler}")
	private String leaveRemainderSwitch;
	@Value("${probation_remainder_scheduler}")
	private String probationReminderSwitch;
	@Value("${probation_remainder_to_emp_scheduler}")
	private String probationReminderToEmpSwitch;
	@Value("${attendance_reminders}")
	private String attendance_reminders;
	@Value("${probation_remainder_before_15_days}")
	private String probationRemainderBefore15DaysSwitch;
	@Value("${privilage_leaves_scheduler}")
	private String privilageLeavesSchedulerSwitch;
	@Value("${emergency_leaves_scheduler}")
	private String emergencyLeavesSchedulerSwitch;
	@Value("${fetch_swipes_scheduler}")
	private String swipeDetailsSchedulerSwitch;
	// *************update password schedular add***************************
	@Value("${ChangePassword_REMINDER_NO_OF_DAYS_TO_EMP}")
	private String ReminderScheduler;

	@Value("${probationCompletionReminderToRO}")
	private String probationCompletionReminderToRO;
	
	@Value("${probationCompletionReminderToEmployee}")
	private String probationCompletionReminderToEmployee;
	
	@Value("${emergency_leaves_scheduler_llc}")
	private String emergencyLeavesSchedulerLLC;
	
	@Value("${kra_reminder_mail_to_hr}")
	private String kraReminderMailToHr;
	@Autowired
	EmployeeService employeeService;
	@Autowired
	CandidateProfessionalDetailsService candProfDetailsService;
	@Autowired
	EmailSender emailSender;
	@Autowired
	EmployeeSeparationDetailsService employeeSeparationService;
	@Autowired
	SendRemiderEmails sendRemiderEmailsService;

	@Autowired
	EmployeeApplyLeaveService  employeeApplyLeaveService;
	



	
	// Added Monika
	@Autowired
	CandidateService candidateService;
	@Autowired
	CSVUploaderService csvUploaderService;
	@Value("${retirment_remainder_before_90_day}")
	private String RetirementScheduler;

	@Value("${retirment_remainder_before_7_day}")
	private String RetirementScheduler1;
	
	@Value("${service_compliation_leave_scheduler}")
	private String servicecompilationLeavesScheduler;
	
	@Value("${td_approvals_reaminders}")
	private String tdApprovalsReaminders;
	
	@Value("${td_reaminders}")
	private String tdReaminders;
	
	@Value("${kra_mail_all_employee}")
	private String kraMailAllEmployee;
	
	@Value("${kra_reminder_mail_rm}")
	private String kraReminderMailRm;
	
	@Value("${kra_reminder_mail_emp}")
	private String kraReminderMailEmp;
	
	@Value("${kra_first_april_reminder_mail_rm}")
	private String kraFirstAprilReminderMailRm;
	
//	@Value("${Kra_hr_reminder_scheduler}")
//	private String kraHrReminderScheduler;
	
	@Value("${kpi_reminder_mail_all_rm}")
	private String kpiReminderMailAllRm;

	@Autowired
	EmployeeLeaveDetailsService employeeLeaveService;

	@Autowired
	LoginService loginService;
	
	@Autowired
	TravelDeskServiceImpl travelDeskServiceImpl;
	
	@Autowired
	KraServiceImpl kraServiceImpl;
	
	@Autowired
	LeaveServiceImpl leaveServiceimpl;
	
//	@Autowired
//	LryptLeaveProcessor leaveProcessor;
	
	@Value("${casual_leaves_scheduler}")
	private String casualLeavesScheduler;
	
	@Value("${earned_leaves_scheduler}")
	private String earnedLeavesScheduler;
	
	@Value("${kpi_cycle_scheduler}")
	private String kpiCycleScheduler;
	

	private final BackendProperties backendProperties;
	private final IdpQueueEmailService queueEmailService;
	private final IdpSendEmailService sendEmailService;

	public ScheduledTasks(final BackendProperties backendProperties, final IdpQueueEmailService queueEmailService,
						  final IdpSendEmailService sendEmailService) {
		this.backendProperties = backendProperties;
		this.queueEmailService = queueEmailService;
		this.sendEmailService = sendEmailService;
	}

	private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	/**
	 * Scheduler for Birthday
	 * 
	 * Every Midnight
	 */
	@Scheduled(cron = "0 0 8 * * *")
	public void birthdayScheduler() {

		if (birthdaySchedulerSwitch.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
			logger.info("Birthday Scheduler Triggered");
			employeeService.getEmployeeWithTodayBirthday();
		}
	}

	/**
	 * Scheduler for Service Completion
	 * 
	 * Duration : Every 9 O'clock of Day
	 */
	@Scheduled(cron = "0 0 9 * * *")
	public void serviceCompletionScheduler() {

		if (serviceCompletionSchedulerSwitch.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
			logger.info("Employee service completion  Triggered");
			employeeService.toSendEmailForEmployeeServiceCompletion();
		}
	}

	/**
	 * Scheduler for Separation Reminder after 5 days of resigning date.
	 * 
	 * Duration : Every 10 O'clock of Day
	 */
	@Scheduled(cron = "0 0 10 * * *")
	// @Scheduled(fixedRate = 100000, initialDelay = 5000)
	public void separationRemainderScheduler() {
		if (separationRemainderSwitch.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
			logger.info("Separation Reminder triggered");
			employeeSeparationService.separationremainder();
		}
	}

	/**
	 * Scheduler for Leave Reminder after 5 days of applied date.
	 * 
	 * Duration : Every 10 O'clock of Day
	 */
	@Scheduled(cron = "0 45 10 * * *")
	// @Scheduled(fixedRate = 1000, initialDelay = 5000)
	public void leaveReminderScheduler() {

		if (leaveRemainderSwitch.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
			logger.info("leave Reminder triggered");
			employeeLeaveService.findAndSendEmailToRoForLeaveAction();
		}
	}

	@Scheduled(cron = "0 0 11 * * *")
	// @Scheduled(fixedRate = 10000, initialDelay = 5000)

	// @Scheduled(cron = "0 0 11 * * *")
	// @Scheduled(cron = "0 45 16 * * *") //Dummy scheduler

	// @Scheduled(cron = "0 0 11 * * *")
	// @Scheduled(cron = "0 45 16 * * *") //Dummy scheduler

	public void probationCompletionReminder() {

		if (probationReminderSwitch.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
			logger.info("probation completion Reminder triggered");
			candProfDetailsService.probationCompletionReminder();
		}
	}

	/**
	 * Scheduler for Probation Reminder to candidate added By Ritesh ,
	 * 
	 * on 02-Jun-2021
	 */
	@Scheduled(cron = "0 20 11 * * *")
	public void probationCompletionReminderForEmployee() {

		if (probationReminderToEmpSwitch.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
			logger.info("probation completion Reminder triggered for employee");
			candProfDetailsService.probationCompletionReminderForEmployees();
		}
	}

	/**
	 * Scheduler for Email Sender in bulk ,
	 * 
	 * Duration : Every 5 Minutes
	 */

	// @Scheduled(fixedRate = 15000)
	// @Scheduled(fixedRate = 300000, initialDelay = 5000)
	// @Scheduled(fixedRate = 300000000, initialDelay = 500000000)
	// @Scheduled(cron = "0 5 * * * *")
	@Scheduled(fixedRate = 300000, initialDelay = 5000)
	public void bulkEmailSenderScheduler() {

		// employeeService.toSendEmailForEmployeeServiceCompletion();
		// employeeService.getEmployeeWithTodayBirthday();
		if (emailSenderSchedulerSwitch.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
			logger.info("Email sender enabled  Triggered");
			emailSender.toSendBulkEmail();

		}
	}

	/**
	 * This is use to send Mail to Checklist approvals
	 * 
	 */
	/*
	 * @Scheduled(fixedRate = 10000, initialDelay = 1000) public void
	 * sendMailToChecklistApprovals() {
	 * 
	 * if (serviceCompletionSchedulerSwitch.equalsIgnoreCase(IHRMSConstants.
	 * IS_SCHEDULER_ENABLED)) {
	 * logger.info("Employee Checklist Approvals Mail Sending.");
	 * employeeSeparationService.sendMailToChecklistApprovals(); } }
	 */

	// @Scheduled(cron = "0 0 0 31 12 ?")
	@Scheduled(cron = "0 0 0 1 1 ?")
	public void creditEmployeeYearlyLeaves() {
		logger.info("Credit employee leaves Yearly methd called");
		long orgId = 1;
		/*
		 * if (emailSenderSchedulerSwitch.equalsIgnoreCase(IHRMSConstants.
		 * IS_SCHEDULER_ENABLED)) {
		 * employeeLeaveService.creditEmpYearlyLeavesService(1L, 2019, 9.0f, 21.0f);
		 * 
		 * }
		 */
	}

	// @Scheduled(cron = "0 0 0 1 1 ?")

//	@Scheduled(cron = "0 10 0 1 1 ?") // At 00:10:00am, on the 1st day, in January
//	public void creditEmployeeYearlyLeavesForDubai() {
//		logger.info("Credit employee leaves Yearly methd called For Dubai");
//		long orgId = 1;
//		long divId = 3;
//		if (privilageLeavesSchedulerSwitch.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
//			employeeLeaveService.creditEmpYearlyLeavesForDubaiService(1L, 2023, 9.0f, 21.0f, divId);
//
//		}
//	}

	@Scheduled(cron = "0 0 0 1 * ?") // At 00:00:00am, on the 1st day, every month
	public void creditEmployeeMonthlyPrivilegeLeaves() {

		logger.info("Credit Privilege Leaves on every 1st day of month");
		long orgId = 1;

		if (privilageLeavesSchedulerSwitch.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
			logger.info("creditEmpYearlyPrivilageLeavesService Scheduler triggered");
			List<Long> divisionList = new ArrayList<>();
			divisionList.add(1L);
			divisionList.add(2L);
			employeeLeaveService.creditEmpYearlyPrivilageLeavesService(1L, divisionList, Year.now().getValue(), 1.5f);

		}
	}

	@Scheduled(cron = "0 5 0 1 1 ?") // At 00:05:00am, on the 1st day, in January
	public void creditEmployeeYearlyEmergencyLeaves() {

		logger.info("Credit Emergency Leaves on 1st Jaunary of year");
		long orgId = 1;

		if (privilageLeavesSchedulerSwitch.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
			logger.info("creditEmployeeYearlyEmergencyLeaves Scheduler triggered");
			List<Long> divisionList = new ArrayList<>();
			divisionList.add(1L);
			divisionList.add(2L);
			employeeLeaveService.creditEmpYearlyEmergencyLeavesService(1L, divisionList, Year.now().getValue(), 12.0f);

		}
	}

	@Scheduled(cron = "0 15 0 1 1 ?") // At 00:15:00am, on the 1st day, in January
	public void creditCompOffEmployeeYearlyLeaves() {
		logger.info("Credit employee compensatory leaves Yearly methd called");
		long orgId = 1;
		if (emailSenderSchedulerSwitch.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
			employeeLeaveService.addCompOffYearEnd(1L, Year.now().getValue());

		}
	}

	@Scheduled(cron = "0 15 10 * * *")
	public void pendingChecklistReminderToApprover() {
		logger.info("Reminder: Pending checklist item reminder to approver");
		if (separationRemainderSwitch.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
			employeeSeparationService.sendPendingChecklistReminderToApprover();
		}

	}

//	@Scheduled(fixedRate = 5000, initialDelay = 1000)
	@Scheduled(cron = "0 0 17 * * *")
	public void sendAttendanceReminderEmails() {
		logger.info("Attendance Reminder: Reminders to attendance defaulters");
		long orgId = 1;
		if (attendance_reminders.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
			try {
				sendRemiderEmailsService.sendAttendanceReminders(orgId);
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Scheduled(cron = "0 35 16 * * *")
	public void sendAttendanceReminderEmailsDubai() {
		logger.info("Dubai Attendance Reminder: Reminders to attendance defaulters");
		long orgId = 1;
		if (attendance_reminders.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
			try {
				sendRemiderEmailsService.sendAttendanceDubaiReminders(orgId);
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Scheduled(cron = "0 30 10 * * *")
	public void probationNotificationToEmployeeBefore15Days() {
		logger.info("Reminder: Probation competion notification to employee om completion probation period");
		if (probationRemainderBefore15DaysSwitch.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
			candidateService.getCandidateForProbationNotification();
		}

	}

	// To Fetch Attaendance data

	// @Scheduled(fixedRate = 600000, initialDelay = 5000) //10 min Local
	// @Scheduled(fixedRate = 900000, initialDelay = 5000) //UAT
	@Scheduled(fixedRate = 1800000, initialDelay = 10000) // PROD
	public void getAttendanceData() {

		logger.info("Attendace:  get attendance swipe details from smart accesss");
		if (swipeDetailsSchedulerSwitch.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
			csvUploaderService.readAndUploadSwipes();
		}

	}

	/*
	 * GENERATING RANDOM SECURITY KEY
	 * 
	 */
	@Scheduled(cron = "0 29 3 * * *")
	public void keyGenScheduler() {
		logger.info("Keygen Scheduler Triggered - Generating Security Key");
		JWTTokenHelper.SECURITY_KEY = UUID.randomUUID().toString();
	}

	// Added Schedular For new Alert Retirement Remainder
	// Author:Monika

	// @Scheduled(fixedRate = 15000)
	@Scheduled(cron = "0 0 9 * * *")
	public void AlertRetairementReminderSchedular() {
		if (RetirementScheduler.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
			logger.info("Retirement Reminder Scheduler Triggered");
			candidateService.getCandidateforRetirementNotification();
		}

	}

//	Added Schedular For new Alert  Retirement Remainder before 7 days
//	Author:Monika 

	// @Scheduled(fixedRate = 25000)
	@Scheduled(cron = "0 0 10 * * *")
	// Added Schedular For new Alert Retirement Remainder before 7 days
	// Author:Monika
	// @Scheduled(fixedRate = 25000)
	// @Scheduled(cron = "0 0 9 * * *")
	public void AlertRetairementReminderSchedularbefore7days() {
		if (RetirementScheduler1.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
			logger.info("Retirement Reminder Scheduler Triggered");
			candidateService.getCandidateforRetirementCompletion();
		}

	}

	
	/**
	 * @author Monika
	 * @description probation remainder to Reporting manager
	 */
	@Scheduled(cron = "0 15 11 * * MON,FRI")
	public void probationCompletionReminderToRO() {

		if (probationCompletionReminderToRO.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
			logger.info("probation completion Reminder triggered");
			candProfDetailsService.probationCompletionReminderToRO();
		}
	}

	/**
	 * @author monika
	 * @description :after completion of Probation period this is reminder to employee on Monday in every week when he/she didn't fill the p2cform
	 */
	@Scheduled(cron = "0 30 11 * * MON")
	public void probationCompletionReminderToEmployee() {

		if (probationCompletionReminderToEmployee.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
			logger.info("probation completion Reminder triggered");
			candProfDetailsService.probationCompletionReminderToEmployee();
		}
	}

	/*
	 * @author Akanksha
	 * */
	
	@Scheduled(cron = "0 1 0 * * *")
	public void compOffLeaveFlushScheduler() {

		if (compOffFlush.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
			logger.info("Comp Off Flush Scheduler Triggered");
			
			leaveServiceimpl.clearExpiredCompOffs();
		} 
	}	
	
	
	/*
	 * on the 1st day, every month leave crediting for Division 3 and 4
	 * */
	@Scheduled(cron = "0 20 0 1 * ?")
	public void creditEmployeeYearlyLeavesForVinsysLLC() {
	    logger.info("Credit Emergency Leaves and PL for LLC on every 1st day of month");
	    long orgId = 1;
		List<Long> divisionList = new ArrayList<>();
		divisionList.add(3L);
		divisionList.add(4L);
	    if (emergencyLeavesSchedulerLLC.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
	    	logger.info("creditEmployeeYearlyLeavesForVinsysLLC Scheduler triggered");
	        employeeLeaveService.creditEmpMonthlyLeavesForDivision4Service(orgId,divisionList);
	    }
	}	
	
	
	@Scheduled(cron = "0 30 0 1 1 ?") // At 30:00am, on the 1st day, in January     
		public void creditServiceCompletionLeavesYearly() {
		    logger.info("Credit employee service completion leaves yearly method called");
		    long orgId = 1;
		    if (servicecompilationLeavesScheduler.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
		    	logger.info("credit Service Completion Leaves Yearly Scheduler triggered");
		        employeeLeaveService.addServiceCompletionLeavesYearEnd(1L, Year.now().getValue());
		    }
		}
	
	/**
	 * Scheduler for pending Remainder for TD approvals.
	 *  Method executed at 7:00 AM every day
	 * Author:Rushikesh Thorat
	 */
//	@Scheduled(cron = "0 0 7 * * *")
	@Scheduled(cron = "0 0 07 * * *")
	public void TDApprovalsReaminders() {
		 logger.info("Handling pending TD approvals...");
	    if (tdApprovalsReaminders.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
	    	logger.info("pending TD approvals Scheduler triggered");
	    	travelDeskServiceImpl.TDApprovalsReaminders();
	    }
	}
	
	

	/**
	 * Scheduler for pending Remainder for TD .
	 * This method executed at 10:10 AM every day.
	 * Author:Rushikesh Thorat
	 */
	@Scheduled(cron = "0 10 10 * * *")
	public void TDReaminders() {
		 logger.info("Handling pending TD ...");
	    if (tdReaminders.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
	    	logger.info("pending TD approvals Scheduler triggered");
	    	travelDeskServiceImpl.sendReminderToTD();
	    }
	}
	
	/**
	 * Scheduler for pending Remainder for TD .
	 * This method executed at 10:10 AM every day.
	 * Author:Mayur Jadhav
	 */
	 
	@Scheduled(cron = "0 0 10 1 4 *")
	public void KRAMailToAllEmployee() {
		 logger.info("Handling 1st April KRA Scheduler  ...");
	    if (kraMailAllEmployee.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
	    	logger.info("1st April KRA Scheduler triggered");
	    	kraServiceImpl.sendEmailToAllEmp();
	    }
	}
	
	/**
	 * Scheduler for pending Remainder for TD .
	 * This method executed at 10:10 AM every day.
	 * Author:Mayur Jadhav
	 */
	@Scheduled(cron = "0 15 10 2-5 4 *")
	public void KRAReminderMailToRM() {
		 logger.info("Handling Reminder Mail To RM Scheduler  ...");
	    if (kraReminderMailRm.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
	    	logger.info("KRA Reminder Mail To RM Scheduler triggered");
	    	kraServiceImpl.sendEmailToRM();
	    }
	}
	
	/**
	 * Scheduler for pending Remainder for TD .
	 * This method executed at 10:10 AM every day.
	 * Author:Mayur Jadhav
	 */
	 @Scheduled(cron = "0 0 10 2-5 4 *")
	public void KRAReminderMailToEMp() {
		 logger.info("Handling Reminder Mail To Employee Scheduler  ...");
	    if (kraReminderMailEmp.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
	    	logger.info("KRA Reminder Mail To Employee Scheduler triggered");
	    	kraServiceImpl.sendReminderEmailToEmployee();
	    }
	}
	 
	 	@Scheduled(cron = "0 15 10 1 4 *")
		public void KRAFirstAprilReminderMailToRM() {
			 logger.info("Handling First April Reminder Mail To RM Scheduler  ...");
		    if (kraFirstAprilReminderMailRm.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
		    	logger.info("KRA First April Reminder Mail To RM Scheduler triggered");
		    	kraServiceImpl.sendFirstAprilReminderToRM();
		    }
		} 
	 	
	 	
	 	/**
		 * Scheduler for KRA Remainder to HR.
		 * @author vidya.chandane	
		 * KRA reminder Scheduler to HR every day 10:30 AM and 5:30 PM 
		 */
		 	@Scheduled(cron = "${kra_hr_reminder_scheduler}")
		 	public void KRAReminderMailToHR() {
				 logger.info("Handling KRA Reminder mail to HR Scheduler  ...");
			    if (kraReminderMailToHr.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
			    	logger.info("Handling KRA Reminder mail to HR Scheduler triggered");
			    	kraServiceImpl.SendKraCountToHR();
			    }
			} 
	
		 	
		 	@Scheduled(cron="0 30 0 1 1,7 *") //At 012:30 AM, on day 1 of the month, only in January and July
		 	//@Scheduled(cron="0 5 19 * * *") 
		 	public void creditEmployeeYearlyCasualLeaves() throws HRMSException {

				logger.info("Credit Casual Leaves on 1st Jaunary and 1st July year");
				long orgId = 2;
				if (casualLeavesScheduler.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
					logger.info("creditEmployeeYearlyCasualLeaves Scheduler triggered");
					List<Long> divisionList = new ArrayList<>();
					divisionList.add(5L);
					leaveServiceimpl.creditEmpYearlyCasualLeavesMethod(orgId, divisionList, 6.0f);
				

				}
			}

		 
		 	@Scheduled(cron="0 0 1 1 * *") // on 1st day of every month at 01:00 AM
			//@Scheduled(cron="0 10 19 * * *") 
			public void creditEmployeeYearlyEarnedLeaves() throws HRMSException, ParseException {

				logger.info("Credit Earned Leaves on 1st Jaunary and 1st July year");
				long orgId = 2;
				if (earnedLeavesScheduler.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
					logger.info("creditEmployeeYearlyEarnedLeaves Scheduler triggered");
					List<Long> divisionList = new ArrayList<>();
					divisionList.add(5L);
					leaveServiceimpl.creditEarnedLeaves(orgId, divisionList, 1.25f);
				 

				}
			}
		 	
		 	/**
			 * Scheduler for pending Remainders to manager.
			 * This method executed at 10:10 AM every day.
			 * Author:Akanksha Gaikwad
		 	 * @throws Exception 
			 */
			//@Scheduled(cron = "0 0 11 * * *")
		 	@Scheduled(cron = "${kra_rm_reminder_scheduler}")
			public void kpiRemindersMailToAllRMs() throws Exception {
				 logger.info("KPI reminder mail to all RMs  ...");
			    if (kpiReminderMailAllRm.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
			    	logger.info("KPI Reminder Mail To RMs Scheduler triggered");
			    	kraServiceImpl.sendKpiReminderEmailToAllRM();
			    }
			}
		 	
			/**
			 * Scheduler to update/Check KPI Cycles status according to start date and end date for every 3 hours
			 *Author Kailas B
			 */
		 	@Scheduled(cron = "0 0 0/3 * * ?")
		 	public void updateKpiCycle() {
		 	    if (kpiCycleScheduler.equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
		 	        logger.info("Update KPI Cycle Scheduler triggered");
		 	        try {
		 	            kraServiceImpl.updateKpiCycleStatus();
		 	        } catch (Exception e) {
		 	            logger.error("Error occurred while updating KPI cycle status", e);
		 	        }
		 	    }
		 	}

	/**
	 * Scheduler for Sending IDP Email queue in tbl_trn_idp_email_log table
	 *
	 * Every 5 minutes
	 */
	@Scheduled(cron = "0 */5 * * * ?")
	public void sendIdpEmailEvery5Minutes() {
		if (backendProperties.getIdpEmailSendScheduler().equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
			logger.info("IDP Email Scheduler Triggered");
			sendEmailService.sendEmailToIdpInformationFollowup();
		}
	}

	/**
	 * Scheduler for Queue IDP Reminder Email in tbl_trn_idp_email_log table
	 *
	 * Once in a day
	 */
	@Scheduled(cron = "0 0 0 * * *")
	public void queueIdpReminderEmailOnceInADay() {
		if (backendProperties.getIdpEmailReminderQueueScheduler().equalsIgnoreCase(IHRMSConstants.IS_SCHEDULER_ENABLED)) {
			logger.info("IDP Email Queue Scheduler Triggered");
			queueEmailService.queueIdpReminderEmail();
		}
	}
		 	
}