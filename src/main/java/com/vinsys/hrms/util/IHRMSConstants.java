package com.vinsys.hrms.util;

import java.util.Set;

public interface IHRMSConstants {

	/*
	 * Added Response Codes On 27Oct17
	 * 
	 */
	public final static String SYSTEM_ENV = "system.env";
	public final static String CYCLE_OPEN = "Open";
	// public final static int failedCode = 500;
	public final static int successCode = 0;
	public final static int UnknowErrorCode = 101;
	public final static int AuthenticationFailedCode = 10;
	public final static int SubscriptionExpiredCode = 102;
	public final static int NotSubscribedCode = 103;
	public final static int OrgNotRegisteredCode = 104;
	public final static int InsertFailedCode = 105;
	public final static int DataAlreadyExist = 106;
	public final static int errorWhileAddingDataCode = 107;
	public final static String FileDoesnotExistMessage = "File Doesn't Exist";
	public final static String TravelCalimType = "Travel and Conveyance";

	public final static int errorWhileDeletingDataCode = 108;

	public final static int InsufficientDataCode = 108;
	public final static int DataNotFoundCode = 109;
	public final static int UserNotFoundCode = 110;
	public final static int CandidateDoesnotExistCode = 111;
	public final static int CandidateDataAlreadyExistCode = 112;
	public static final int LeaveActionErrorCode = 115;
	public static final int InvalidLeaveActionCode = 116;
	/*
	 * Below Code To Be Utilized If Any Parent Data Is Not Found From Database,Its
	 * Its Respective Message Can Be Variant Based On The Specific Data Which Is Not
	 * Found
	 */
	
	public final static int ParentDataNotFilledCode = 117;
	public static final int ActivityProcessedCode = 113;
	public static final int CannotWithdrawLeaveCode = 114;

	public static final int NotValidDateCode = 118;
	public static final int EmailSendingFailedCode = 119;
	public static final int OfferLetterNotInPendingStateCode = 120;
	public static final int InvalidActionCode = 121;
	public static final int CandidateAlreadyProcessedCode = 122;
	public static final int InValidDetailsCode = 123;
	public static final int YearNotValidCode = 124;
	public static final int LeaveNotselectedCode = 124;
	public static final int InvalidSelectedLanguageErrorCode = 125;
	public static final int OrganizationDoesnotExistCode = 126;
	public static final int MODEOFSEPARTIONCODE = 127;
	public static final int SEPARATIONWITHDRAWCODE = 128;
	public static final int SEPARATIONWRONGACTIONCODE = 129;
	public static final int EMPLOYEE_DOSENT_EXIST_CODE = 130;
	public static final int ALL_CATLOGUE_CHECKLIST_NOT_SUBMITED_CODE = 131;
	public static final int EXIT_FEEDBACK_ALREADY_SUBMITTED_CODE = 132;
	public final static int NO_FILE_EXISTS_CODE = 999;
	/*
	 * Added Response Messages On 27Oct17
	 * 
	 */
	public final static String CandidateDoesnotExistMessage = "Candidate Doesn't Exist";
	public final static String successMessage = "Success";
	public final static String updateSuccesMessage = "Updated Succesfully";
	public final static String addedsuccessMessage = "Successfully added";
	public final static String cancelledsuccessMessage = "Candidate deleted from HR workspace successfully";
	public final static String updatedsuccessMessage = "Successfully updated";
	public final static String deletedsuccessMessage = "Successfully deleted";
	public final static String AuthenticationFailedMessage = "Incorrect username or password";
	public final static String UnknowErrorMessage = "Oops!!! Something Went Wrong";
	public final static String SubscriptionExpiredMessage = "Subscription Expired";
	public final static String NotSubscribedMessage = "You Are Not Subscribed";
	public final static String OrgNotRegisteredMessage = "Your Organization is Not Registered";
	public final static String InsertFailedMessage = "Unable To Save Record";
	public final static String emailAlreadyExistMessage = "Candidate Already Registered";

	public final static String errorWhileDeletingDataMessage = "Something went wrong while data deleting";

	public final static String isActive = "Y";
	public final static String isNotActive = "N";

	public final static String True = "true";
	public final static String False = "false";
	
	public final static String INPROGRESS = "INPROGRESS";
	public final static String INPROCESS = "INPROCESS";
	public final static String INCOMPLETE = "INCOMPLETE";
	public final static String MANAGER = "MANAGER";
	public final static String EMPLOYEE = "EMPLOYEE";
	public final static String COMPLETED = "COMPLETED";

	public final static String PROBATIONARYDESCRIPTION = "PRB";

	public final static String errorWhileAddingDataMessage = "Something Went Wrong while Inserting Data";
	public final static String InsufficientDataMessage = "Insufficient Data";
	public final static String DataNotFoundMessage = "Data Not Found";
	public final static String UserNotFoundMessage = "User Not Found";
	public final static String DesignationAlredyExist = "Designation Already Exist";

	public final static String NO_FILE_EXISTS_MESSAGE = "Please select file to upload.";
	/*
	 * Variant Child Data Code . Code To Be Utilized With Is ParentDataNotFilledCode
	 */
	public final static String ChildProfessionalDataNotFoundMessage = "Basic Professional Details Not Found";

	public final static String CandidateDataAlreadyExistMessage = "Data Already exist of this candidate";
	public final static String PasswordChangeSuccesfullyMessage = "Password Changed Succesfully";
	public final static String DefaultPassoword = "password";
	public final static String PasswordValidationMessage = "Password must have at least one Uppercase,one special character,one numeric value and password minimum length should 8 characters.";
	public final static String CompareOldAndNewPassMessage = "Old Password And New Password must be Different.";
	public static final String IsFirstLogin = "Y";
	public static final String RoleCandidate = "CANDIDATE";
	public static final String RoleAdmin = "ORG_ADMIN";
	public static final String EmployeeIdAlreadyExist = "Provided Employee ID Already Exist";
	public static final String EmployeeDoesnotExistMessage = "Employee Doesn't Exist";
	public static final String NoLeavesApplied = "No Applied Leaves Found";
	public static final String SubordinatesNotFound = "Subordinate Employees Not Found For Given Manager ID";

	/**
	 * LEAVES MESSAGE CONSTANTS
	 */

	public static final String EmployeeDesignation_Alredy_EXIST = "Employee Designaton Already Exist";

	public static final String LeaveStatus_PENDING = "PENDING";
	public static final String LeaveStatus_CANCELLED = "CANCELLED";
	public static final String LeaveStatus_APPROVED = "APPROVED";
	public static final String LeaveStatus_REJECT = "REJECTED";
	public static final String LeaveStatus_WD_PENDING = "WD_PENDING";
	public static final String LeaveStatus_WD_APPROVED = "WD_APPROVED";
	public static final String LeaveStatus_WD_REJECTED = "WD_REJECTED";
	public static final String LeaveStatus_AUTO_APPROVED = "APPROVED";// AUTO_APPROVED
	public static final String LeaveAction_APPLY = "APPLY_LEAVE";
	public static final String LeaveAction_CANCEL = "CANCEL_LEAVE";
	public static final String LeaveAction_APPROVE = "APPROVE_LEAVE";
	public static final String LeaveAction_REJECT = "REJECT_LEAVE";
	public static final String LeaveAction_WITHDRAW = "WD_LEAVE";
	public static final String LeaveAction_WITHDRAW_APPROVE = "WD_APPROVE";
	public static final String LeaveAction_WITHDRAW_REJECT = "WD_REJECT";
	public static final String ErrorInCancellation = "Processed Leave Cannot Get Cancelled";
	public static final String CannotWithdrawLeaveMessage = "Your Leave Is Either Cancelled Or Not Yet Approved";
	public static final String LeaveActionSuccessMessage = "Leave Action Processed Succesfully";
	public static final String LeaveCancelActionSuccessMessage = "Leave cancelled succesfully";
	public static final String LeaveNotInPendingErrorMessage = "Leave Is Not In Pending State";
	public static final String CannotApproveWithdraw = "Cannot Withdraw Leave Might Have Processed Already ";
	public static final String InvalidLeaveActionMessage = "Please Choose A Proper Leave Action";
	public static final String NotValidDateMessage = "Please Enter Valid Date";
	public static final String CannotRejectMessage = "Cannot Reject !! Leave Might Have Processed Already ";
	public static final String CandidatePersonalDetailsDosentExist = "Candidate Personal Details Not Exist";
	public static final String LeaveOverlapsMessage = "Current Applied Leave Overlaps With Existing Leaves";
	public static final String CREDITED_LEAVE_OVERLAPPES_MESSAGE = "Current Credited Leave Overlaps With Existing Leaves";
	public static final String SubmitDocument = "YES";
	public static final int GRANT_LEAVE_NOT_ALLOWED_CODE = 128;
	public static final String GRANT_LEAVE_NOT_ALLOWED_MESSAGE = "Grant Leave Cannot Apply On Working Day";
	public static final String MaternityLeaveOverExceedsLimit = "Maternity Leave can not be more than 182 Days";
	public static final String PaternityLeaveOverExceedsLimit = "You can apply Paternity Leave only for 3 Days";
	public static final String PaternityLeaveAvailed2Times = "Paternity Leave cannot be availed more than two times. Please Contact HRD.";
	public static final String AdoptionLeaveOverExceedsLimitFemale = "You can apply Adoption Leave only for 21 Days";
	public static final String AdoptionLeaveOverExceedsLimitMale = "You can apply Adoption Leave only for 7 Days";

	/**
	 * MAIL CONSTANTS
	 */
	public static final String MailSubject_LeaveApplication = "Leave Application Request";
	public static final String MailSubject_OnDutyApplication = "On Duty Application Request";
	public static final String MailSubject_WorkFromHomeApplication = "Work From Home Application Request";
	public static final String MailSubject_LeaveApplication_Reminder = "Leave Application Reminder";
	public static final String MailSubject_LeaveCancelled = "Leave Cancelled";
	public static final String MailSubject_LeaveWithdrawRequest = "Leave Withdraw Request";
	public static final String MailSubject_LeaveApproved = "Leave Approved";
	public static final String MailSubject_LeaveWithdrawApproved = "Leave Withdrawn Approved";
	public static final String MailSubject_LeaveRejected = "Leave Application Rejected";
	public static final String MailSubject_LeaveWithdrawRejected = "Leave Withdrawn Rejected";

	/**
	 * Candidate Onboard Action Type Constant
	 */
	public static final String OnboardActionType_CANCEL = "CANCEL";
	public static final String OnboardActionType_ONHOLD = "ONHOLD";
	public static final String OnboardActionType_ONBOARD = "ONBOARD";

	/**
	 * Email Configuration,not used as reading from property file
	 */
	// public static final String SmtpHost = "";
	// public static final String Port = "";
	// public static final String FromEmail = "";
	// public static final String password = " ";
	// public static final String FromEmailTitle = "Test Vinsys HRMS";
	public static final int DefaultPageSize = 10;
	public static final String MailSubject_FinalOfferLetter = "Final Offer Letter";

	public static final String EmailSendingFailedMEssage = "Unable to send EMail";
	public static final String OfferLetterAction_ACCEPT = "OL_ACCEPT";
	public static final String OfferLetterAction_REJECT = "OL_REJECT";
	public static final String AppointmentLetterAction_ACCEPT = "AL_ACCEPT";
	public static final String AppointmentLetterAction_REJECT = "AL_REJECT";
	public static final String LetterStatus_ACCEPT = "ACCEPTED";
	public static final String LetterStatus_REJECT = "REJECTED";
	public static final String LetterStatus_PENDING = "PENDING";

	public static final String OfferLetterNotInPendingStateMessage = "You have already taken action on offer letter";

	public static final String InvalidActionMessage = "Invalid Action";

	/**
	 * Candidate Status Constant
	 * 
	 */
	public static final String CandidateStatus_CREATED = "CAND_CREATED";
	public static final String CandidateStatus_INITIATED = "CAND_INITIATED";
	public static final String CandidateStatus_ONBOARD = "CAND_ONBOARD";
	public static final String CandidateStatus_ISEMPLOYEE = "CAND_ISEMP";
	public static final String CandidateStatus_CANCEL_ONBOARD = "CAND_ONBOARD_CANCEL";
	public static final String MailSubject_EmployeeCreation = "Official Email ID";
	public static final String CandidateAlreadyProcessedMessage = "Candidate already processed";
	public static final String ForgetPasswordSubject = "Login Password";
	public static final String AdminPasswordSubject = "Admin Credentials";

	public static final String InvalidDetailsMessage = "Please provide valid details ";

	public static final String PropertyFileRootDirectory = "E:\\input\\Properties\\";
	public static final String Config_PropertyFile = "propertyFile.properties";

	/**
	 * Letters
	 */
	public static final int PDFGenerationAndEmailSendingFailedCode = 124;
	public static final String PDFGenerationAndEmailSendingFailedMessage = "PDF Creation & EMail sending Failed ";
	public static final String EmailSendingFailedButPDFCreatedMessage = "Letter Created ,EMail not send succesfully";
	public static final String FileName_APPOINTMENT_LETTER = "Appointment_Letter";
	public static final String FileName_OfferLetter = "Offer_Letter";
	public static final String LetterType_VINSYS_OFFER_LETTER = "VIN_OFFER_LETTER";
	public static final String LetterType_VINSYS_APPOINTMENT_LETTER = "VIN_APPOINTMENT_LETTER";
	public static final String LetterURL = "C:\\";
	public static final int PDFGenerationFailedCode = 124;
	public static final String PDFGenerationFailedMessage = "PDF Generation Failed";
	public static final String EmailSentSuccess = "Y";
	public static final String EmailSentFailed = "N";
	public static final String PENDING = "Pending";

	/**
	 * Documents
	 */
	public static final String NoDocumentFound = "No Document Found";
	public static final String VerifiedDocument = "Verified";
	public static final String YearNotValidMessage = "Please provide valid year";
	public static final String PendingDocument = "Pending";

	/**
	 * Admin
	 */
	public static final String AdminStatus = "ADMIN_CREATED";
	public static final String RoleDoesnotExistMessage = "Provided Role Dosen't Exist";
	public static final String LeaveTypeDosentExist = "Leave type dosen't exist";
	public static final String LeaveRemarkForAutoApproved = "LEAVE_AUTO_APPROVED";
	public static final String EmployeeLeaveDetailsNotFoundMessage = "Leave Details Not Found";
	public static final String ByMailerRemark = "EMAIL_ACTION";
	public static final String YouCannotApplyTheSelectedLeaveMessage = "Selected leave type has not been credited to your account";
	public static final String AUTH_KEY = "x-api-key";

	public static final int LEAVE_APPLIED_GREATER_THAN_LEAVE_AVAILABLE_CODE = 125;
	public static final String LEAVE_APPLIED_GREATER_THAN_LEAVE_AVAILABLE_MESSAGE = "You do not have enough leave credit for the selected leave type";
	public static final String CANDIDATE_CREATED_SUCCESFULLY = "Candidate created succesfully";
	public static final String DOCUMENT_UPLOAD_SUCCESS_MESSAGE = "Your document has been succesfully submitted";
	public static final String CANDIDATE_PROCESSED_SUCCESFULLY = "Candidate has been processed succesfully";
	public static final int EMAIL_REATTEMPT = 2;

	public static final String EMAIL_CONFIGURATION_NOT_FOUND = "Email configuration not found";
	public static final String EMPLOYEE_CREATED_SUCCESFULLY = "Employee created successfully";

	/**
	 * GRANT MAIL CONSTANTS
	 */
	public static final String MailSubject_GrantLeaveApplication = "Grant Leave Application Request";
	public static final String MailSubject_GrantLeaveCancelled = "Grant Leave Cancelled";
	public static final String MailSubject_GrantLeaveWithdrawRequest = "Grant Leave Withdraw Request";
	public static final String MailSubject_GrantLeaveApproved = "Grant Leave Approved";
	public static final String MailSubject_GrantLeaveWithdrawApproved = "Grant Leave Withdrawn Approved";
	public static final String MailSubject_GrantLeaveRejected = "Grant Leave Application Rejected";
	public static final String MailSubject_GrantLeaveWithdrawRejected = "Grant Leave Withdrawn Rejected";

	public static final String GENERAL_INFO_UPDATED_SUCCESFULL = "Candidate general info updated succesfully";
	public static final String NO_LANGUAGE_FOUND = "No Language Found";
	public static final String DOCUMENT_VERIFICATION_FAILED_SUBJECT = "Document verfication failed";
	public static final String EXIT_FEEDBACK_ALREADY_SUBMITTED_MESSAGE = "Exit Feedback Already Submitted";
	/**
	 * File upload Size
	 */
	public static final String INVALID_FILE_FORMAT = "Invalid file format";

	public static final int FileUploadCode = 128;
	public static final String FileUploadErrorMessage = "File is not selected";

	public static final int FileUploadSizeLimitCode = 127;
	public static final String FileUploadSizeLimitErrorMessage = "Maximum file upload size is 2 MB ";

	public static final String LETTER_STATUS_SUBJECT = "Candidate_letter_status";
	public static final String CANDIDATE_ACTIVITY_OFFER_LETTER_ACCEPTED = "Offer Letter Accepted";
	public static final String CANDIDATE_ACTIVITY_OFFER_LETTER_REJECTED = "Offer Letter Rejected";
	public static final String CANDIDATE_ACTIVITY_APPOINTMENT_LETTER_ACCEPTED = "Appointment Letter Accepted";
	public static final String CANDIDATE_ACTIVITY_APPOINTMENT_LETTER_REJECTED = "Appointment Letter Rjected";
	public static final String INVALID_LETTER = "Letter Not Found";
	public static final String CANDIDATE_ACTIVITY_MANDATORY_FORM_FILLED = "Mandatory Form's Filled";
	public static final String CANDIDATE_ACTIVITY_MANDATORY_FORM_NOT_FILLED = "Mandatory Form's Not Filled";

	public static final String EMPLOYEE_EMAIL_ALREADY_EXIST = "Provided Official Email Id Already Exist";
	public static final String LeaveNotselectedMessage = "Please Select Atleast One Leave Type";
	public static final String ReportCreatedMessage = "Report Created Successfully";
	public static final String CANDIDATE_LANGUAGE_CONST_YES = "Yes";
	public static final String CANDIDATE_LANGUAGE_CONST_NO = "No";
	public static final int CANDIDATE_UNIQUE_MOTHER_TONGUE_ERROR_CODE = 129;
	public static final String CANDIDATE_UNIQUE_MOTHER_TONGUE_ERROR_MESSAGE = "Cannot Add Multiple Mother Tongue";
	public static final String GRANT_LEAVE_CREDITED_BY = "FROM GRANT";

	public static final String EMAIL_STATUS_PENDING = "PENDING";
	public static final String EMAIL_STATUS_SENT = "SUCCESS";
	public static final String EMAIL_STATUS_ERROR = "FAILED";
	public static final String IS_SCHEDULER_ENABLED = "ENABLED";
	public static final String POSTGRE_DATE_FORMAT = "yyyy-MM-dd";
	public static final String FRONT_END_DATE_FORMAT = "dd-MM-yyyy";
	public static final int GRANT_LEAVE_EXPIRY_PERIOD = 90;
	public static final String COMP_OFF_SESSION_1 = "Session 1";
	public static final String COMP_OFF_SESSION_2 = "Session 2";
	public static final float COMP_OFF_SESSION_VALUE = 0.5f;
	public static final int COMP_OFF_VALID_LEAVES_UNAVAILABLE_CODE = 130;
	public static final String COMP_OFF_VALID_LEAVES_UNAVAILABLE_MESSAGE = "You don't have valid compensatory leaves";
	public static final String COMP_OFF_MASTER_LEAVE_CODE = "COMP";
	
	/**
	 * MAIL CONSTANTS FOR TRAVEL DESK
	 */
	public static final String REQUESTER_TO_TRAVEL_DESK = "REQUESTER_TO_TRAVEL_DESK";
	public static final String TRAVEL_DESK_TO_APPROVER = "TRAVEL_DESK_TO_APPROVER";
	public static final String APPROVER_TO_REQUESTER_APPROVE_REQUEST = "APPROVER_TO_REQUESTER_APPROVE_REQUEST";
	public static final String APPROVER_TO_REQUESTER_REJECT_REQUEST = "APPROVER_TO_REQUESTER_REJECT_REQUEST";
	public static final String TRAVEL_DESK_TO_REQUESTER_CONFIRM_REQUEST = "TRAVEL_DESK_TO_REQUESTER_CONFIRM_REQUEST";
	
	
	

	// public static final String FRONT_END_DATE_FORMAT = "yyyy-MM-dd";

	public static final String InvalidSelectedLanguageErrorMessage = "Invalid Language Selected ";
	public static final int UPDATE_REQUIRED_ERROR_CODE = 1001;
	public static final String UPDATE_REQUIRED_MESSAGE = "Please Update Your Application !!! ";

//	****************************************new added**************************************************
//	public static final int ChangePassword_REMINDER_NO_OF_DAYS_TO_EMP = 30;
	public static final String CANDIDATE_SELECTED_LANGUAGES = "CANDIDATE_SELECTED_LANGUAGES";
	public static final String CANDIDATE_NOT_SELECTED_LANGUAGES = "CANDIDATE_NOT_SELECTED_LANGUAGES";
	public static final int CANDIDATE_UNIQUE_LANGUAGE_ERROR_CODE = 162;
	public static final String CANDIDATE_UNIQUE__lANGUAGE_ERROR_MESSAGE = "Language has been already taken";
	/*
	 * Separation Messages
	 */
	public static final String OrganizationDoesnotExistMessage = "Organization doesn't exist";
	public static final String MODEOFSEPARTIONCODEMESSAGE = "Mode of Separation does not exist";
	public static final String SEPARATIONWITHDRAWMESSAGE = "You can't withdraw,it is already approved";
	public static final String MODEOFSEPARTIONFOREMP = "Emp_Resg";
	public static final String SEPARATIOCANCELMESSAGE = "Resignation cancelled successfully";
	public static final String SEPARATIOAPPLYSUCCESSMESSAGE = "Resignation applied  successfully";
	public static final String EMPLOYEE_RESIGNED = "Employee Resigned";
	public static final String EMPLOYEE_ABSCONDED = "Absconded";
	public static final String EMPLOYEE_TERMINATED = "Terminated";
	public static final String EMPLOYEE_DEMISE = "Employee Demise";
	public static final String EMPLOYEE_SEPARATION_STATUS_PENDING = "Pending";
	public static final String EMPLOYEE_SEPARATION_STATUS_COMPLETED = "Completed";
	public static final String EMPLOYEE_SEPARATION_ACTION_WITHDRAW = "Withdraw";
	public static final String EMPLOYEE_SEPARATION_CHECKLIST_STATUS_PENDING = "Pending";

	public static final String EMPLOYEE_SEPARATION_STATUS_APPROVED = "Approved";
	public static final String EMPLOYEE_SEPARATION_STATUS_REJECTED = "Rejected";
	public static final String EMPLOYEE_SEPARATION_STATUS_WITHDRAW = "WD_Pending";
	public static final String EMPLOYEE_WD_SEPARATION_STATUS_APPROVED = "WD_Approved";
	public static final String EMPLOYEE_WD_SEPARATION_STATUS_REJECTED = "WD_Rejected";

	public static final String EMPLOYEE_SEPARATION_SYSTEM_ESCALATED = "System Escalated";
	public static final String EMPLOYEESEPARATIONWRONACTION_MESSAGE = "You can't take action before RO approval";

	public static final String EMPLOYEE_STATUS_RO_PENDING = "RO_Pending";
	public static final String EMPLOYEE_STATUS_ORG_PENDING = "ORG_Pending";
	public static final String EMPLOYEE_STATUS_HR_PENDING = "HR_Pending";
	public static final String RESIGNATION_APPROVAL = "Resignation Approval";
	public static final String SEPARATIONFOLDERNAME = "Separation";

	// ADDED BY MONIKA
	public static final String PRIOR_RETIREMENT_NOTIFICATION_TEMPLATE = "90_DAYS_PRIOR_RETIREMENT_NOTIFICATION";
	public static final String PRIOR_RETIREMENT_NOTIFICATION2_TEMPLATE = "07_DAYS_PRIOR_RETIREMENT_NOTIFICATION";

	/*
	 * 
	 * Employee Separation Mail Constants
	 */

	public static final String MailSubject_EMPLOYEERESIGNED = "Resignation Application";
	public static final String MailSubject_RESIGNATION_REMINDER = "Resignation Application Reminder";
	public static final String MailSubject_EMPLOYEEWITHDRAW = "Pull Back Applied";
	public static final String MailSubject_SEPARATION_APPROVED = "Status of Resignation Application - Approved";
	public static final String MailSubject_SEPARATION_REJECTED = "Status of Resignation Application - Rejected";

	public static final String MailSubject_WD_SEPARATION_APPROVED = "Status of Pull Back Applied - Approved";
	public static final String MailSubject_WD_SEPARATION_REJECTED = "Status of Pull Back Applied - Rejected";
	public static final String MailSubject_Exit_Feedback_Form_Submit = "Exit Feedback Form";
	public static final String MailSubject_ALL_HANDOVER_CHECKLIST_SUBMITTED = "Department Checklist Approved";

	public static final String Question_type_SINGLE = "SINGLE";
	public static final String Question_type_MULTIPLE = "MULTIPLE";
	public static final String Question_type_COMMENT = "COMMENT";

	public static final String MailSubject_ProbationReminder = "Probation Reminder";

	public static final String PENDING_RESIGNED_EMP_CHECKLIST_SUBJECT = "Checklist Approval Reminder";

	public static final String EMPLOYEE_DOSENT_EXIST_MESSAGE = "Employee Dosen't Exist";
	public static final String MAILSUBJECTFORCHECKLISTAPPROVALS = "Approval for Department Checklist Handover";

	public static final String EMPLOYEE_SEPARATION_MARK_STATUS_COMPLETED_ERROR_MESSAGE = "You can't mark request as complete unless all catalogue status must be completed. ";

	public static final String ALL_CATALOGUE_CHECKLIST_NOT_SUBMITED_MESSAGE = "Please Submit All The Checklist For Approval";
	public static final String EMPLOYEE_CATALOGUE_CHECKLIST_SUBMITTED = "COMPLETED";
	public static final String IS_MAIL_WITH_ATTACHMENT_Y = "Y";
	public static final String IS_MAIL_WITH_ATTACHMENT_N = "N";

	public static final String CHECKBOX_CHECKED_IMG_PATH = "C:\\Hostings\\hrms.vinsys.com\\input\\Images\\checkboxChecked.png\"";
	public static final String CHECKBOX_UNCHECKED_IMG_PATH = "C:\\Hostings\\hrms.vinsys.com\\input\\Images\\checkboxUnchecked.png\"";
	public static final String RADIO_CHECKED_IMG_PATH = "C:\\Hostings\\hrms.vinsys.com\\input\\Images\\radioButtonChecked.png\"";
	public static final String RADIO_UNCHECKED_IMG_PATH = "C:\\Hostings\\hrms.vinsys.com\\input\\Images\\radioButtonUnchecked.png\"";

	public final static String EMP_RESIGNED_AUTHENTICATION_FAILED = "Login access denied ,please contact HRD";
	public final static String WORKSHIFT_DEFAULT_GENERAL_NAME = "General";
	
	public final static String Template_Employee_Resignation_Apply="Template_Employee_Resignation_Apply";
	
	public final static String Template_Employee_Resignation_Cancel="Template_Employee_Resignation_Cancel";
	
	public final static String Template_Employee_Resignation_Approve="Template_Employee_Resignation_Approve";
	
	public final static String Template_Employee_Resignation_Reject="Template_Employee_Resignation_Reject";
	
	public final static String Template_Employee_Resignation_Rejected_by_HR="Template_Employee_Resignation_Rejected_by_HR";
	
	public final static String Template_Employee_Resignation_Approved_by_HR="Template_Employee_Resignation_Approved_by_HR";
	
	public final static String Exit_Feedback_Form_Submitted="Exit_Feedback_Form_Submitted";
	
	public final static String Department_Checklist_Approved="Department_Checklist_Approved";
	

	/*
	 * Travel Desk Constants
	 * 
	 */
	public final static int PASSENGER_ERROR_CODE = 131;
	public final static int TRAVEL_REQUEST_NOT_FOUND_CODE = 137;
	public final static int TRAVEL_REQUEST_ALREADY_PROCESSED_CODE = 138;
	public static final int TRAVEL_REQUEST_DRIVER_VEHICLE_NOT_FOUND_ERROR_CODE = 139;
	public static final int MAP_CAB_DRIVER_VEHICLE_NOT_FOUND_MESSAGE_CODE = 140;
	public static final int CHILD_REQUEST_NOT_CLOSED_CODE = 141;
	public static final int INVALID_APPROVER_ACTION_CODE = 142;
	public static final int INVALID_REQ_CODE = 143;

	/*
	 * Travel desk statuses
	 */

	public final static String TRAVEL_REQUEST_STATUS_PENDING = "PENDING";
	public final static String TRAVEL_REQUEST_STATUS_WIP = "WIP";
	public final static String TRAVEL_REQUEST_STATUS_REJECTED = "REJECTED";
	public final static String TRAVEL_REQUEST_STATUS_COMPLETED = "COMPLETED";

	public static final String TRAVEL_REQUEST_ACTION_TRAVEL_REQUEST = "TravelRequest";
	public final static String TRAVEL_REQUEST_ACTION_ACCEPT = "ACCEPT";
	public final static String TRAVEL_REQUEST_ACTION_REJECT = "REJECT";
	public final static String TRAVEL_REQUEST_ACTION_MARK_AS_COMPLETE = "MARK_AS_COMPLETE";
	public static final String TRAVEL_REQUEST_ACTION_APPROVER_PENDING = "ApprovalPending";
	public final static String TRAVEL_REQUEST_ACTION_APPROVER_APPROVED = "APPROVER_APPROVED";
	public final static String TRAVEL_REQUEST_ACTION_APPROVER_REJECTED = "APPROVER_REJECTED";

	public final static String TRAVEL_REQUEST_COMMENTATOR_REQUESTER = "REQUESTER";
	public final static String TRAVEL_REQUEST_COMMENTATOR_TRAVELDESK = "TRAVELDESK";
	public final static String TRAVEL_REQUEST_COMMENTATOR_APPROVER = "APPROVER";

	public static final String TRAVEL_REQUEST_CHILD_TYPE_TICKET = "Ticket";
	public static final String TRAVEL_REQUEST_CHILD_TYPE_ACCOMMODATION = "Accommodation";
	public static final String TRAVEL_REQUEST_CHILD_TYPE_CAB = "Cab";
	public static final String TRAVELFOLDERNAME = "TravelRequest";
	public static final String TRAVEL_REQUEST_CHILD_TYPE_AIR = "Air";
	public static final String TRAVEL_REQUEST_CHILD_TYPE_BUS = "Bus";
	public static final String TRAVEL_REQUEST_CHILD_TYPE_TRAIN = "Train";
	
	public static final String APPROVER_TYPE_DOMESTIC_APPROVER = "DA";
	public static final String APPROVER_TYPE_INTERNATIONAL_APPROVER = "IA";
	public static final String APPROVER_TYPE_TRAVELDESK = "TD";

	public final static String TRAVEL_REQUEST_CHILD_STATUS_PENDING = "PENDING";
	public final static String TRAVEL_REQUEST_CHILD_STATUS_CLOSED = "CLOSED";

	public final static String TD_APPROVER_STATUS_PENDING = "PENDING";
	public final static String TD_APPROVER_STATUS_APPROVED = "APPROVED";
	public final static String TD_APPROVER_STATUS_REJECTED = "REJECTED";

	public static final String WON_NOT_FOUND = "WON-NA";
	public static final String TD_COMMENT_CHILD_TYPE_ALL = "ALL";

	public static final String CAB_REQUEST_JOURNEY_WAY_ONE_WAY = "ONE_WAY";
	public static final String CAB_REQUEST_JOURNEY_WAY_RETURN = "RETURN";

	public static final String STRING_ALL = "ALL";
	public static final String TRAVEL_REQUEST_ACTION_TRAVEL_CANCEL = "TravelCancel";
	public static final String FRONT_END_DATE_FORMAT_DDMMYY_HHMMSS = "dd-MM-yyyy hh:mm:ss";

	/*
	 * travel desk messages
	 */

	public final static String PASSENGER_NOT_FOUND_MESSAGE = "Please add atleast one passenger";
	public static final String TRAVEL_REQUEST_NOT_FOUND = "Travel request not found.";
	public static final String FILEUPLOADSUCCESS = "File uploaded successfully";
	public static final String FILEDELETEDSUCCESS = "File deleted successfully";
	public static final String TRAVEL_REQUEST_SAVED_SUCCESFULLY = "Your travel request has been saved succesfully !!!";
	public static final String CAB_REQUEST_EMAIL_SUBJECT = "Cab - Approval Request";
	public static final String TICKET_REQUEST_EMAIL_SUBJECT = "Ticket - Approval Request";
	public static final String ACCOMMODATION_REQUEST_EMAIL_SUBJECT = "Accommodation - Approval Request";
	public static final String APPROVER_NOT_FOUND_MSG = "Provided Approver Dosen't Exist !!! Please Provide Valid Approver";
	public static final String COMMENTER_DETAILS_INSUFFICIENT = "Please Provide Valid & Complete Comment Details";
	public static final String APPROVER_ASSIGNMENT_SUCCESS_MSG = "Request has been sent for approval";
	public static final String TRAVEL_REQUEST_ALREADY_PROCESSED = "Travel Request is already processed";
	public static final String TRAVEL_REQUEST_REJECT_EMAIL_SUBJECT = "Travel Request Rejected";
	public static final String TRAVEL_REQUEST_DRIVER_VEHICLE_NOT_FOUND_ERROR_MESSAGE = "Selected driver or vehicle not found.";
	public static final String NO_TD_EMPLOYEE_FOUND = "No Travel Desk Employee Found !!! Unable To Send Email ";
	public static final String CAB_DETAILS_NOT_AVAILABLE_TO_CANCEL = "Cab Details Not Avaialble !! Unable To Process With Cancellation";
	public static final String TICKET_DETAILS_NOT_AVAILABLE_TO_CANCEL = "Ticket Details Not Avaialble !! Unable To Process With Cancellation";
	public static final String ACCOMMODATION_DETAILS_NOT_AVAILABLE_TO_CANCEL = "Accommodation Details Not Avaialble !! Unable To Process With Cancellation";
	public static final String TRAVEL_REQ_CANCELLATION_SUCCESS = "Your Travel Request Has Been Processed For Cancellation";
	public static final String CHILD_REQUEST_NOT_CLOSED_MESSAGE = "Please close all child travel requests";
	public static final String INVALID_APPROVER_ACTION_MESSAGE = "Invalid Approver action";
	public static final String MAP_CAB_DRIVER_VEHICLE_NOT_FOUND_MESSAGE = "Map cab driver vehicle not found";
	public static final String APPROVER_ACTION_MAIL_SUBJECT = "Approver Action";
	public static final String NOTIFICATION_MAIL_SUBJECT_TICKET = "Travel Request  :: Ticket Details";
	public static final String NOTIFICATION_MAIL_SUBJECT_ACCOMMODATION = "Travel Request  :: Accommodation Details";
	public static final String TD_EMAIL_NOTIFICATION_SENT_SUCCESFULLY = "Email Notification Sent Succesfully";
	public static final String INVALID_CHILD_TYPE = "Please Provide Valid Child Type !!! Cannot Processed Further";
	public static final String ACCOMMODATION_DETAILS_NOT_AVAILABLE = "Accommodation Details Not Avaialble !!";
	public static final String TICKET_DETAILS_NOT_AVAILABLE = "Ticket Details Not Avaialble !!";
	public static final String TICKET_ATTACHMENT_DETAILS_NOT_AVAILABLE = "No Attachment Found For Provided Ticket Details !!";
	public static final String ACCOMMODATION_ATTACHMENT_DETAILS_NOT_AVAILABLE = "No Attachment Found For Provided Accommodation Details !!";
	public static final String TRAVEL_DESK_EMPLOYEE_NOT_FOUND = "No Traveldesk Employee Found !! Please contact Administrator";
	public static final String TICKET_ALREADY_CANCELLED = "Ticket Is Already In Cancellation State";
	public static final String CAB_ALREADY_CANCELLED = "Cab Is Already In Cancellation State";
	public static final String ACCOMMODATION_ALREADY_CANCELLED = "Accommodation Is Already In Cancellation State";

	public static final String INVALID_CANCELLATION_REQUEST_MESSAGE = "Please Select Atleast One Option To Cancel";
	public static final String TRAVEL_REQUEST_CANCEL_SUBJECT = "Travel Cancellation Request";
	public static final String CANCELLATION_AND_BOOKING_STATUS_MISMATCHED = "Cancellation And Booking Request Did Not Matched !! Provide Valid Details";
	public static final String NA = "NA";
	public static final String ASSIGN_CAB_DETAILS_MAIL_SUBJECT = "Booked Cab Details";
	public static final String APPROVER_ACTION_SUCCESS_MSG = "Your Action Has Been Processed Succesfully !!!";
	public final static String CAB_DETAILS_ASSIGNED_SUCCESSFULLY = "Cab details Has Been Assigned Successfully !!!";
	public static final String TRAVEL_REQUEST_UPDATED_SUCCESFULLY = "Your Travel Request Has Been Modified Succesfully !!!";
	public static final String MAIL_SUBJECT_TRAVEL_REQUEST_UPDATED = "Travel Request Modified";
	public static final String MAIL_SUBJECT_TRAVEL_REQUEST = "Travel Request";
	public static final String TRAVEL_REQUEST_ACCEPTED_MESSAGE = "Travel Request Has Been Accepted Succesfully !!! ";
	public static final String TRAVEL_REQUEST_REJECTED_MESSAGE = "Travel Request Has Been Rejected !!!";
	public static final String TRAVEL_REQUEST_PASSENGER_NOT_FOUND = "Requested Passenger Not Found.";

	public static final String TRAVEL_REQUEST_JOURNEY_COMPLETED_MESSAGE = "Journey Has Been Completed Successfully";

	public static final String TRAVEL_REQUEST_DRIVER_NOT_FOUND = "Driver Details Not Found !!!";
	public static final String NO_ASSIGNMENT_FOUND_MSG = "No Assignment Found";
	public static final String CAB_DETAIL_MAIL_TYPE_BOOK_CAB = "BOOK_CAB_DETAIL";
	public static final String CAB_DETAIL_MAIL_TYPE_JOURNEY_COMPLETE = "JOURNEY_COMPLETE";
	public static final String CAB_DETAIL_MAIL_SUBJECT_JOURNEY_COMPLETE = "Cab Journey Mail";

	/**
	 * Extension Module Constants & Message
	 */
	public static final String EXTENSION_TYPE_DOSENT_EXIST = "Extension Type Dosen't Exist";
	public static final String EMPLOYEE_EXTENSION_UPDATE_MESSAGE = "Employee Extension Modified Succesfully";
	public static final String EMPLOYEE_EXTENSION_ADD_MESSAGE = "Employee Extension Added Succesfully";
	public static final String EXTENSION_DOSENT_EXIST = "Provided Extension Is Not Available Or Already Deleted";

	public static final String PROBATION_MASTER_LEAVE_CODE = "PROB";
	public static final String EMPLOYMENT_TYPE_CONFIRMED = "Confirmed";
	public static final String EMPLOYMENT_TYPE_EMPLOYEE = "EMP";
	public static final String EMPLOYMENT_TYPE_PERMANENT = "Permanent";

	public static final int CONFIRMED_EMP_PROB_LEAVE_RESTRICT_CODE = 145;
	public static final String CONFIRMED_EMP_PROB_LEAVE_RESTRICT_MESSAGE = "Confirmed employee can not apply probationary leave";

	public static final String MASTER_CANDIDATE_CHECKLIST_SALARY_ANNEXURE_CODE = "SALANX";
	public static final String REMINDER_MAIL_TYPE_MANAGER_ACTION_PENDING_LEAVE = "MANAGER_ACTION_PENDING_LEAVE";
	public static final String REMINDER_MAIL_TYPE_MANAGER_ACTION_PENDING_PROBATION = "MANAGER_ACTION_PENDING_PROBATION";

	public static final String REMINDER_MAIL_TYPE_EMPLOYEE_ACTION_PENDING_PROBATION = "EMPLOYEE_ACTION_PENDING_PROBATION"; // Added
																															// By
																															// Ritesh
																															// Kolhe

	public static final String PERMANANT_ADDRESS_TYPE = "Permanent";
	public static final String PRESENT_ADDRESS_TYPE = "Present";

	public static final String SEPARATION_TEMPLATE = "Separation";
	public static final String SEPARATION_ORG_APPROVE_TEMPLATE_NAME = "Template_Empployee_Resignation_Approve_By_ORG_LEVEL";

	public static final String LEAVE_TYPE_CODE_SICK = "SICK";
	public static final String LEAVE_TYPE_CODE_PRIVILAGE = "PRVL";

	public static final String MOBILE_SUBMITTED_SWAP_MESSAGE = "MOBILE_SWAP";
	public static final String MOBILE_ATTENDANCE_MARKED_SUCCESS_MESSAGE = "Mobile attendance marked successfully";

	public static final String MOBILE_CURRENT_MARKED_ATTENDANCE_STATUS_IN = "IN";
	public static final String MOBILE_CURRENT_MARKED_ATTENDANCE_STATUS_OUT = "OUT";

	public static final String MOBILE_REMOTE_LOCATION_ATTENDANCE_ALLOWED_Y = "Y";
	public static final String MOBILE_REMOTE_LOCATION_ATTENDANCE_ALLOWED_N = "N";

	public static final int MOBILE_REMOTE_LOCATION_ATTENDANCE_NOT_ALLOWED_CODE = 146;
	public static final String MOBILE_REMOTE_LOCATION_ATTENDANCE_NOT_ALLOWED_MSG = "Your remote location attendance facility is disabled. "
			+ "Please contact Administrator";

	public static final int MOBILE_REMOTE_LOCATION_ATTENDANCE_IMEI_NOT_MATCHED_CODE = 147;
	public static final String MOBILE_REMOTE_LOCATION_ATTENDANCE_IMEI_NOT_MATCHED_MSG = "Your mobile "
			+ "Identity doesn't match. Please Contact Administrator";

	public static final String TO_DISPLAY = "Display";
	public static final String LEAVE_TYPE_CODE_COMP_OFF = "COMP";

	/************* Confirmation constants *******************/

	public static final String CONFIRMED = "Confirmed";
	public static final String EXTENDED = "Extended";
	public static final String REJECTED = "Reject";
	public static final String PROBATION = "Probation";

	public static final int TEN_DAYS_LEAVE_RESTRICT_CODE = 148;
	public static final String TEN_DAYS_LEAVE_RESTRICT_MESSAGE = "This type of leave can be applied for next 10 days only";
	public static final int MAX_LEAVE_RESTRICT_CODE = 149;
	public static final String MAX_LEAVE_RESTRICT_MESSAGE = "This type of leave can be applied for maximum 10 days";
	public static final int FUTURE_DATE_LEAVE_RESTRICT_CODE = 150;
	public static final String FUTURE_DATE_LEAVE_RESTRICT_MESSAGE = "This type of leave can not be applied for future dates";
	public static final String LEAVE_RESTRICTION_6_MONTHS_MESSAGE = "Leave application can be applied only for next 6 months from today.";
	public static final String LEAVE_RESTRICTION_30_DAYS_MESSAGE = "Leave application can be applied only for next 30 days from today.";

	/******** P2C mail template names START ************/
	public static final String PRIOR_PROBATION_NOTIFICATION_TEMPLATE = "15_DAYS_PRIOR_PROBATION_NOTIFICATION";
	public static final String P2C_SUBMITTED_BY_EMPlOYEE_TEMPLATE = "SUBMISSION_OF_PROBATION_FORM_BY_EMPLOYEE";
	public static final String P2C_SUBMITTED_BY_MANAGER_TEMPLATE = "SUBMISSION_OF_PROBATION_FORM_BY_MANAGER";
	public static final String P2C_SUBMITTED_BY_HR_TEMPLATE_CONFIRMED = "SUBMISSION_OF_PROBATION_FORM_BY_HR_CONFIRMED";
	public static final String P2C_SUBMITTED_BY_HR_TEMPLATE_EXTENDED = "SUBMISSION_OF_PROBATION_FORM_BY_HR_EXTENDED";
	public static final String P2C_SUBMITTED_BY_HR_TEMPLATE_REJECTED = "SUBMISSION_OF_PROBATION_FORM_BY_HR_REJECTED";

	public static final String EMAIL_TEMPLATE_NOT_FOUND_MESSAGE = "Email Template not found";
	public static final int EMAIL_TEMPLATE_NOT_FOUND_CODE = 151;

	public static final int MATR_LEAVE_VALID_COUNT = 80;
	public static final int MATR_LEAVE_VALID_DAYS = 182;
	public static final int PATR_LEAVE_VALID_DAYS = 3;
	public static final int ADPT_LEAVE_VALID_DAYS_MALE = 7;
	public static final int ADPT_LEAVE_VALID_DAYS_FEMALE = 21;
	public static final String TOKEN_ISSUER = "VINSYS";
	public static final String TOKEN_SUBJECT = "HRMS";
	public static final String TOKEN_NOT_VALID = "INVALID TOKEN OR TOKEN EXPIRED";
	public static final String InvalidInput = "Invalid Input";

	/******** P2C mail template names END ************/
	// Added By Monika
	public static final String Template_Probation_Reminder_To_Employee = "Probation_Reminder_To_Employee";

	public static final int INVALID_USER = 420;
	public static final String INVALID_USER_LOGIN = "Invalid User Login";

	// added by akanksha

	public static final int COMP_OFF_SESSION_VALUE_1 = 1;
	public static final int COMP_OFF_SESSION_VALUE_2 = 2;
	public static final float COMP_OFF_SESSION_VALUE_3 = 1.5f;
	public final static int failedCode = 500;
	public final static String failedMessage = "FAILED";

	/***
	 * added by @author vidya.chandane
	 */
	public final static String isEmergencyContact = "YES";
	public final static String isNotEmergencyContact = "NO";

	public final static String isRental = "YES";
	public final static String isNotRental = "NO";

	public static final String LEAVE_CYCLE_MONTHLY = "MONTHLY";
	public static final float PRIVILEGE_LEAVE_COUNT = 21;
	public static final float EMERGENCY_LEAVE_COUNT = 9;

	public static final String LEAVE_TYPE_CODE_SERVICE_COMPLETION = "SRCM";
	public static final int NotValidZeroDayLeaveCode = 500;
	public static final String NotValidZeroDayLeaveMessage = "You can't apply for Zero Leave Days!";
	
	public static final String CAB_REQUEST_JOURNEY_ROUND = "Round";
	
	public static final String CAB_REQUEST_JOURNEY_DROP_ONLY = "Drop Only";
	
	public final static String Internal_Employee = "Internal";
	
	public final static String External_Employee = "External";
	
	public static final String TD_APPROVER_REMINDER_TEMPLATE_NAME = "TD_APPROVER_REMINDER_TEMPLATE_NAME";
	
	public static final String TD_REMINDER_TEMPLATE_NAME = "TD_REMINDER_TEMPLATE_NAME";
	
	/***
	 * added by @author mayur.j
	 */
	
	public static final String Employee_Submits_KRA = "Employee_Submits_KRA";
	public static final String KRA_Approved_By_RM = "KRA_Approved_By_RM";
	public static final String KRA_Rejected_By_RM = "KRA_Rejected_By_RM";
	public static final String Fill_KRA_Form = "Fill_KRA_Form";
	public static final String KRA_Reminder_For_Employee = "KRA_Reminder_For_Employee";
	public static final String Action_On_KRA_Reminder_For_RM = "Action_On_KRA_Reminder_For_RM";
	public static final String Initial_Reminder_For_RM = "Initial_Reminder_For_RM";
	
	public static final String Kra_Reminder_To_HR="Kra_Reminder_To_HR";
	
	/**
	 * @author vidya.chandane
	 */
	public static final String Holiday="H";
	
	/** @author vidya.chandane */
	
	public static final String Forget_Password ="Forget_Password";
	
	public static final String LEAVE_TYPE_CODE_CASUAL_lEAVE="CSL";

	public static final String Offer_Letter="Offer Letter"; 
	
	public static final String Experience_Letter="Experience Letter";
	
	public static final String Relieving_Letter = "Relieving Letter";
	
	public static final String Appointment_Letter="Appointment Letter";
	
	public final static String EMPLOYEE_EMAIL = "KPI_SUBMITTED_BY_EMP";
	
	public final static String EMPLOYEE_MANAGER_EMAIL = "KPI_SUBMITTED_TO_MANAGER";
	
	public final static String MANAGER_SUBMIT_EMAIL = "KPI_SUBMITTED_BY_MANAGER_FOR_EMP";
	
	public final static String MANAGER_SUBMIT_EMAIL_FOR_HIMSELF = "MANAGER_KPI_SUBMISSION";
	
	public static final String MANAGER_EMAIL = "MC_REVERSE_KPI_MANAGER";
	
	public static final String MC_MEMBER_EMAIL = "MC_REVERSE_KPI_MC";
	
	public static final String HR_EMAIL = "MC_ACCEPTANCE";
	
	public static final String HOD_CALIBRATE_EMAIL = "CALIBERATION_CYCLE_ENABLED_BY_MC";
	
	public static final String KPI_PENDING_TO_MC = "KPI_PENDING_MC";
	
	public static final String MC_ACCEPTANCE_TO_MC = "KPI_ACCEPTED_BY_MC";
	
	public static final String EMAIL_EMPLOYEE_NAME_KEY = "empName";
	
	
	
	
	
	
	public final static Float ZERO_VALUE = 0.0F;
	
	public static final String HOD_CALIBRATION_EMAIL = "HOD_CALIBRATION_EMAIL";
	
	public final static String passfail = "Pass/Fail";
	
	
	public final static String PASS = "Pass";
	public final static String FAIL = "Fail";
	public final static String onOccurrence = "On Occurrence";
	public static final String LINE_MANAGER_REMINDERS = "LC_MANAGER_REMINDER";
	
	public static final String GENERIC_KPI = "Generic";
	public static final String NON_GENERIC_KPI = "Org Functional KPIs";
	
	public static final String FUNCTION_SPECIFIC_CATEGORY = "Function specific";
	
	public static final String CORE_COMPETENCIES_CATEGORY = "Core competencies: Fundamental behaviours, actions and conduct in line with the DNA of the organization";
	public static final String LEADERSHIP_COMPETENCIES_CATEGORY = "Leadership competencies: Specific behaviours, actions and conduct that effective leaders possess or showcase";
	
	
	public static final Set<Long> FUNCTIONAL_CATEGORY_IDS = Set.of(157L);
	public static final Set<Long> CORE_CATEGORY_IDS = Set.of(158L, 160L);
	public static final Set<Long> LEADERSHIP_CATEGORY_IDS = Set.of(159L, 161L);

	public static final String EMP_CODE_REGEX = "[a-zA-Z0-9\\\\s]{4,250}$";
	public static final String KPI_SUBMISSION = "KPI Submission";
	public static final String HALF_YEAR_CYCLE = "Half Year Cycle";
	public static final String YEAR_END_CYCLE = "Year End Cycle";
	public static final String EMPLOYEE_PUBLISHED_EMAIL = "HR_PUBLISH_FINAL_KPI";
	public static final String HR_PUBLISHED_EMAIL = "HR_REVIEW_PUBLISH";
	public static final String KPI_SUBMISSION_EMAIL = "KPI_SUBMISSION_PUBLISH_EMPLOYEE";
	public static final String HR_KPI_SUBMISSION_EMAIL = "KPI_SUBMISSION_PUBLISH_HR";
	public static final String HALF_YEAR_CYCLE_EMAIL = "HR_PUBLISH_FINAL_KPI";
	public static final String HR_HALF_YEAR_CYCLE_EMAIL = "HR_REVIEW_PUBLISH";
	
	public static final String LINE_MANAGER_EMAIL_ON_HCD_ACCEPTANCE = "LINE_MANAGER_EMAIL_ON_HCD_ACCEPTANCE";
	public static final String HCD_EMAIL_ON_HIS_ACCEPTANCE = "HCD_EMAIL_ON_HIS_ACCEPTANCE";
	public static final String MANAGER_EMAIL_ON_HCD_CORRECTION = "MANAGER_EMAIL_ON_HCD_CORRECTION";
	
	public static final String KPI_SUBMITTED_TO_MANAGER = "KPI_SUBMITTED_TO_MANAGER";
	public static final String KPI_SUBMITTED_BY_EMP = "KPI_SUBMITTED_BY_EMP";
	public static final String EMPLOYEE_EMAILON_LM_ACCEPTANCE = "EMPLOYEE_EMAILON_LM_ACCEPTANCE";
	public static final String MANAGER_EMAIL_ON_HIS_ACCEPTANCE = "MANAGER_EMAIL_ON_HIS_ACCEPTANCE";
	public static final String FOR_GRADE = " for -";
	public static final String EMPLOYEE_EMAIL_ON_DEFINE_CYCLE = "EMPLOYEE_EMAIL_ON_DEFINE_CYCLE";
	public static final String HR_EMAIL_ON_DEFINE_CYCLE = "HR_EMAIL_ON_DEFINE_CYCLE";
	public static final String HR_EMAIL_ON_CYCLE_DEFINE_BY_HIM = "HR_EMAIL_ON_CYCLE_DEFINE_BY_HIM";
	public static final String MANAGER_EMAIL_ON_DEFINE_CYCLE = "MANAGER_EMAIL_ON_DEFINE_CYCLE";
	public static final String HCD_EMAIL_ON_HIS_CORRECTION = "HCD_EMAIL_ON_HIS_CORRECTION";
	public static final String START_DATE = "startDate";
	public static final String END_DATE = "endDate";
	public static final String SUBORDINATE_KEY = "subordinateName";
	public static final String ACCEPTED_KPI_BY_EMP = "ACCEPTED_KPI_BY_EMP";
	public static final String ACCEPTED_KPI_BY_EMP_TO_MANGER = "ACCEPTED_KPI_BY_EMP_TO_MANGER";
	public static final String ACCEPTED_KPI_BY_EMP_TO_HR = "ACCEPTED_KPI_BY_EMP_TO_HR";
	public static final String ORG_KPI_TO_HR = "ORG_KPI_TO_HR";
	public static final String GENERATE_KPI_HR = "GENERATE_KPI_HR_MAIL";
	public static final String GENERATE_KPI_MANAGER = "GENERATE_KPI_MANAGER_MAIL";	
	
	
	
	
	public static final String MANAGER_SUBMIT_EMAIL_FOR_HIMSELF_ON_SUBMIT ="MANAGER_KPI_SUBMISSION_ON_SUBMIT";
	public static final String HR_EMAIL_ON_KPI_SUBMISSION = "KPI_SUBMISSION_TO_HR";
	public static final String OPEN = "Open";
	public static final String CLOSED = "Closed";
	public static final String HOD_EMAIL_ON_DEFINE_CYCLE = "HOD_EMAIL_ON_DEFINE_CYCLE";

	public final static String KPI_TIMELINE_DISPLAY_STATUS = "success";
	public static final String SAVE_OBJECTIVES = "Save Obejectives";
	public static final String UPDATE_ORGKPI = "UpdateOrgKpi";
	public static final String UPDATE_OBJECTIVES = "Update Objectives";
	public static final String ACCEPT_KPI_BY_HCD = "Accept KPI By Hcd";
	public static final String cycleclosed = "Cycle Is Not Open / Ended";
	public static final String SUBMIT_KPI_BY_LM = "Submit KPI By LM";
	public static final String REJECT_KPI_BY_HCD = "Reject Kpi By Hcd";
	public static final String ACCEPT_KPI_BY_LM = "Accept KPI By LM";
	public static final String ACCEPT_KPI_BY_TM = "Accept KPI By Team Member";
	public static final String DELETE_ORG_KPI = "Delete Org Kpi";
	public static final String DELETE_KPI_BY_LM = "Delete KPI By LM";
	public static final String SAVED_CORRECTION_BY_HCD = "Saved Correction By Hcd";
	public static final String SUBMIT_CORRECTION_BY_HCD = "Submit Correction By Hcd";
	public static final String SAVE_KPI_TIME_LINE = "Save Kpi Time Line";
	public static final String DELETE_KPI_TIME_LINE = "Delete Kpi Time Line";
	public static final String DELETE_DELEGATION_MAPPING = "Delete Delegation Mapping";
	public static final String SUBMIT_DELEGATION = "Submit Delegation";
	public static final String DELETE_STATE = "Delete State";
	public static final String ADD_COUNTRY = "Add Country";
	public static final String ADD_CITY = "Add City";
	public static final String DELETE_COUNTRY = "Delete Country";
	public static final String DELETE_CITY = "Delete City";
	public static final String ADD_STATE = "Add State";
	public static final String ADD_KPI_METRIC = "Add Kpi Metric";
	public static final String DELETE_KPI_METRIC = "Delete Kpi Metric";
	public static final String ADD_GRADE = "Add Grade";
	public static final String DELETE_GRADE ="Delete Grade";
	public static final String ADD_BRANCH = "Add Branch";
	public static final String DELETE_BRANCH = "Delete Branch";
	public static final String ADD_DEPARTMENT = "Add Department";
	public static final String DELETE_DEPARTMENT = "Delete Department";
	public static final String ADD_EMPLOYMENT_TYPE = "Add Employment Type";
	public static final String DELETE_EMPLOYMENT_TYPE = "Delete Employment Type";
	public static final String ADD_DIVISION = "Add_Division";
	public static final String DELETE_DIVISION = "Delete Division";
	public static final String ADD_DESIGNATION = "Add Designation";
	public static final String DELETE_DESIGNATION = "Delete Designation";
	public static final String ADD_MASTER_TITLE = "Add Master Title";
	public static final String DELETE_SALUTATION = "Delete Salutation";
	public static final String ACTIVATE_KRA_CYCLE = "Activate Kra Cycle";
	public static final String SAVE_KRA_CYCLE = "Save Kra Cycle";
	public static final String DELETE_KRA_CYCLE = "Delete Kra Cycle";
	public static final String HOD = "HOD";
	public static final String HR = "HR";
	public static final String TEAM_MEMBER = "Team Member";
	public static final String MC_MEMBER = "MC Member";
	public static final String LINE_MANAGER = "Line Manager" ;
	public static final String DIVISIONHEAD = "Function Head" ;
	public static final String HCD = "HCD";
	public static final String SUBMISSION = "Submission";
	public static final String CUSTOM = "Custom";
	public static final String MID_YEAR = "Mid Year";
	public static final String YEAR_END = "Year End";
	public static final String ADD_OBJECTIVE = "Add Objective";
	public static final String NO_STAGE = "No Stage";
	public static final String MID_YEAR_CYCLE = "Mid Year Cycle";
	public static final String YES = "Y";
	public static final String NO = "N";
	public static final String KPI_SUBMISSION_PENDING_MESSAGE = "KPI submission is Pending for";
	public static final String KPI_SUBMISSION_PENDING_MESSAGE_FOR_TEAM = "Half Year Rating is Pending for your Team Member";
	public static final String HALF_YEAR_PENDING_MESSAGE = "Half Year Rating is Pending for";
	public static final String HALF_YEAR_PENDING_MESSAGE_FOR_TEAM = "Half Year Rating  is Pending for your Team Member";
	public static final String YEAR_END_PENDING_MESSAGE = "Year End Rating is Pending for";
	public static final String YEAR_END_PENDING_MESSAGE_FOR_TEAM = "Year End Rating is Pending for your Team Member";
	public static final String ADD_HOD_TO_DEPARTMENT_MAPPING = "ADD HOD TO DEPARTMENT MAPPING";
	public static final String DELETE_HOD_TO_DEPARTMENT_MAPPING = "DELETE HOD TO DEPARTMENT MAPPING";
	public static final Long KPI_SUBMISSION_TYPE_ID = 1L;
	public static final Long HALF_YEAR_TYPE_ID = 2L;
	public static final Long YEAR_END_TYPE_ID = 3L;
	public static final String  ACTIVATE_YEAR = "Activate Year";
	public static final String ADD_FUNCTION_HEAD_TO_DIVISION = "ADD FUNCTION HEAD TO DIVISION";
	public static final String ADD_OR_UPDATE_KPI_QUESTION = "ADD OR UPDATE KPI QUESTION";
	public static final String DELETE_KPI_QUESTION = "DELETE KPI QUESTION";
	public static final String ADD_OR_UPDATE_QUES_ANS = "ADD OR UPDATE QUES ANS";

	
	
	
}
