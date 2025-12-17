package com.vinsys.hrms.util;

public interface IHRMSEmailTemplateConstants {

	public static final String Template_LeaveApply = "<font face=\"verdana\" size=\"2\">\r\n"
			+ "Hi {managerFirstName} {managerMiddleName} {managerLastName},<br><br>Please find the leave application. Please log on to <i><a href={websiteURL}>{websiteURL}</a> </i>and review the leave.<br><br>\r\n"
			+ "<table border=\"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri;\" >\r\n"
			+ "   <tr>\r\n" + "      <td>Employee Name:</td>\r\n"
			+ "      <td>{empFirstName} {empMiddleName} {empLastName}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Employee ID:</td>\r\n" + "      <td>{empId}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Leave Type:</td>\r\n" + "      <td>{leaveType}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Leave From:</td>\r\n" + "      <td>{fromDate}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "		<td>From session:</td>\r\n" + "      <td>{fromSession}</td>\r\n" + "   </tr>\r\n"
			+ "      <td>Leave To:</td>\r\n" + "      <td>{toDate}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "		<td>To Session:</td>\r\n" + "      <td>{toSession}</td>\r\n" + "   </tr>\r\n" + "<tr>\r\n"
			+ "      <td>No Of Days:</td>\r\n" + "      <td>{noOfDays}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Reason:</td>\r\n" + "      <td>{leaveReason}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Contact No:</td>\r\n" + "      <td>{contactNo}</td>\r\n" + "   </tr>\r\n" + "</table>\r\n"
			+ "		\"<table border = \"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri\">\r\n"
			+ "<tbody>\r\n" + "<tr>\r\n" + "		<td style=\"width: 125px;\">Employee Name</td>\r\n"
			+ "<td style=\"width: 125px;\">Status</td>\r\n" + "		<td style=\"width: 125px;\">Status On</td>\r\n"
			+ "<td style=\"width: 126px;\">Comments</td>\r\n" + "		</tr>\r\n" + "<tr>\r\n"
			+ "		<td style=\"width: 125px;\">{empFirstName} {empMiddleName} {empLastName}</td>\r\n"
			+ "		<td style=\"width: 125px;\">{leaveStatus}</td>\r\n"
			+ "		<td style=\"width: 125px;\">{statusAsOfOn}</td>\r\n"
			+ "		<td style=\"width: 126px;\">{leaveReason}</td>\r\n" + "</tr>\r\n" + "</tbody>\r\n" + "</table>"
			+ "		<table border=\"0\" cellspacing=0 cellpadding =2 style=\"margin:10px 5px 10px 100px; font-family: calibri;\" >\r\n"
			+ "  	 <tr>\r\n"
			+ "      <td><a href='{rootIp}/leave/leaveActionByMail/{leaveActionApprove}/{leaveId}/'  style=\"background-color: #84A100;color: white;padding: 5px 10px;text-align: center;text-decoration: none;display: inline-block;\">APPROVE</a></td>\r\n"
			+ "	  	<td></td>\r\n"
			+ "	  	<td><a href='{rootIp}/leave/leaveActionByMail/{leaveActionReject}/{leaveId})' style=\"background-color: #F01F07;color: white;padding: 5px 10px;text-align: center;text-decoration: none;display: inline-block;\">REJECT</a></td>\r\n"
			+ "   	</tr>\r\n" + "   \r\n" + "</table>\r\n" + "Regards,<br>\r\n"
			+ "{empFirstName} {empMiddleName} {empLastName}\r\n" + "\r\n" + "</font>";

	public static final String Template_LeaveApply_CC = "<font face=\"verdana\" size=\"2\">\r\n"
			+ "Hi {managerFirstName} {managerMiddleName} {managerLastName},<br><br>Please find the leave application. Please log on to <i><a href={websiteURL}>{websiteURL}</a> </i>and review the leave.<br><br>\r\n"
			+ "<table border=\"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri;\" >\r\n"
			+ "   <tr>\r\n" + "      <td>Employee Name:</td>\r\n"
			+ "      <td>{empFirstName} {empMiddleName} {empLastName}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Employee ID:</td>\r\n" + "      <td>{empId}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Leave Type:</td>\r\n" + "      <td>{leaveType}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Leave From:</td>\r\n" + "      <td>{fromDate}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "		<td>From session:</td>\r\n" + "      <td>{fromSession}</td>\r\n" + "   </tr>\r\n"
			+ "      <td>Leave To:</td>\r\n" + "      <td>{toDate}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "		<td>To Session:</td>\r\n" + "      <td>{toSession}</td>\r\n" + "   </tr>\r\n" + "<tr>\r\n"
			+ "      <td>No Of Days:</td>\r\n" + "      <td>{noOfDays}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Reason:</td>\r\n" + "      <td>{leaveReason}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Contact No:</td>\r\n" + "      <td>{contactNo}</td>\r\n" + "   </tr>\r\n" + "</table>\r\n"
			+ "		\"<table border = \"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri\">\r\n"
			+ "<tbody>\r\n" + "<tr>\r\n" + "		<td style=\"width: 125px;\">Employee Name</td>\r\n"
			+ "<td style=\"width: 125px;\">Status</td>\r\n" + "		<td style=\"width: 125px;\">Status On</td>\r\n"
			+ "<td style=\"width: 126px;\">Comments</td>\r\n" + "		</tr>\r\n" + "<tr>\r\n"
			+ "		<td style=\"width: 125px;\">{empFirstName} {empMiddleName} {empLastName}</td>\r\n"
			+ "		<td style=\"width: 125px;\">{leaveStatus}</td>\r\n"
			+ "		<td style=\"width: 125px;\">{statusAsOfOn}</td>\r\n"
			+ "		<td style=\"width: 126px;\">{leaveReason}</td>\r\n" + "</tr>\r\n" + "</tbody>\r\n  </table>"
			+ "<br>\r\n" + "Regards,<br>\r\n" + "{empFirstName} {empMiddleName} {empLastName}\r\n" + "\r\n" + "</font>";

	/*
	 * "Hi {managerFirstName} {managerMiddleName} {managerLastName},<br>" +
	 * "<br>One of your subordinate has applied for leave,Please find the details below<br>"
	 * +
	 * "<br><table border=\"1\" cellspacing=0 BORDERCOLOR=blue><tr><td>Employee Name:</td><td>{empFirstName} {empMiddleName} {empLastName}</td></tr><tr><td>Employee ID:</td><td>{empId}</td></tr><tr><td>Leave Type:</td><td>{leaveType}</td></tr><tr><td>Leave From:</td><td>{fromDate}</td></tr><tr><td>Leave To:</td><td>{toDate}</td></tr><tr><td>No Of Days:</td><td>{noOfDays}</td></tr><tr><td>Reason:</td><td>{leaveReason}</td></tr><tr><td>Contact No:</td><td>{contactNo}<td/></tr></table><br>\r\n"
	 * +
	 * "<table border=\"1\" cellspacing=0 ><tr><td>Employee Name</td><td>Status</td><td>Applied On</td><td>Leave Reson</td></tr><tr><td>{empFirstName} {empMiddleName} {empLastName}</td><td>{leaveStatus}</td><td>{appliedOn}</td><td>{leaveReason}</td></tr>"
	 * + "</table>" +
	 * "<button><a href=http://localhost:8080/leave/{leaveActionApprove}/{leaveId}>Accept</a></button>"
	 * +
	 * "<button><a href=http://localhost:8080/leave/{leaveActionReject}/{leaveId}>Reject</a></button>"
	 * ;//CHANGING HERE
	 */
	public static final String Template_CancleLeaveForEmployee = "<font face=\"verdana\" size=\"2\">\r\n"
			+ "Hi {empFirstName} {empMiddleName} {empLastName},<br><br>Please find the leave application ,Below are the leave details.<br><br>\r\n"
			+ "<table border=\"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri;\" >\r\n"
			+ "   <tr>\r\n" + "      <td>Employee Name:</td>\r\n"
			+ "      <td>{empFirstName} {empMiddleName} {empLastName}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Employee ID:</td>\r\n" + "      <td>{empId}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Leave Type:</td>\r\n" + "      <td>{leaveType}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Leave From:</td>\r\n" + "      <td>{fromDate}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "		<td>From session:</td>\r\n" + "      <td>{fromSession}</td>\r\n" + "   </tr>\r\n"
			+ "      <td>Leave To:</td>\r\n" + "      <td>{toDate}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "		<td>To Session:</td>\r\n" + "      <td>{toSession}</td>\r\n" + "   </tr>\r\n" + "<tr>\r\n"
			+ "      <td>No Of Days:</td>\r\n" + "      <td>{noOfDays}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Reason:</td>\r\n" + "      <td>{leaveReason}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Contact No:</td>\r\n" + "      <td>{contactNo}</td>\r\n" + "   </tr>\r\n" + "</table>\r\n"
			+ "		\"<table border = \"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri\">\r\n"
			+ "<tbody>\r\n" + "<tr>\r\n" + "		<td style=\"width: 125px;\">Employee Name</td>\r\n"
			+ "<td style=\"width: 125px;\">Status</td>\r\n" + "		<td style=\"width: 125px;\">Cancelled On</td>\r\n"
			+ "<td style=\"width: 126px;\">Comments</td>\r\n" + "		</tr>\r\n" + "<tr>\r\n"
			+ "		<td style=\"width: 125px;\">{empFirstName} {empMiddleName} {empLastName}</td>\r\n"
			+ "		<td style=\"width: 125px;\">{leaveStatus}</td>\r\n"
			+ "		<td style=\"width: 125px;\">{cancelledOn}</td>\r\n"
			+ "		<td style=\"width: 126px;\">{leaveReason}</td>\r\n" + "</tr>\r\n" + "</tbody>\r\n  </table>"
			+ "<br>\r\n" + "Regards,<br>\r\n" + "{empFirstName} {empMiddleName} {empLastName}\r\n" + "\r\n" + "</font>";

	public static final String Template_CancleLeaveForManager = "<html>\r\n"
			+ "   <font face=\"verdana\" size=\"2\">\r\n"
			+ "      Hi {managerFirstName} {managerMiddleName} {managerLastName},<br>\r\n"
			+ "      <br>Please find the leave application ,Below are the leave details<br>\r\n" + "      <br>\r\n"
			+ "<table border=\"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri;\" >\r\n"
			+ "   <tr>\r\n" + "      <td>Employee Name:</td>\r\n"
			+ "      <td>{empFirstName} {empMiddleName} {empLastName}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Employee ID:</td>\r\n" + "      <td>{empId}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Leave Type:</td>\r\n" + "      <td>{leaveType}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Leave From:</td>\r\n" + "      <td>{fromDate}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "		<td>From session:</td>\r\n" + "      <td>{fromSession}</td>\r\n" + "   </tr>\r\n"
			+ "      <td>Leave To:</td>\r\n" + "      <td>{toDate}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "		<td>To Session:</td>\r\n" + "      <td>{toSession}</td>\r\n" + "   </tr>\r\n" + "<tr>\r\n"
			+ "      <td>No Of Days:</td>\r\n" + "      <td>{noOfDays}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Reason:</td>\r\n" + "      <td>{leaveReason}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Contact No:</td>\r\n" + "      <td>{contactNo}</td>\r\n" + "   </tr>\r\n" + "</table>\r\n"
			+ "		\"<table border = \"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri\">\r\n"
			+ "<tbody>\r\n" + "<tr>\r\n" + "		<td style=\"width: 125px;\">Employee Name</td>\r\n"
			+ "<td style=\"width: 125px;\">Status</td>\r\n" + "		<td style=\"width: 125px;\">Cancelled On</td>\r\n"
			+ "<td style=\"width: 126px;\">Comments</td>\r\n" + "		</tr>\r\n" + "<tr>\r\n"
			+ "		<td style=\"width: 125px;\">{empFirstName} {empMiddleName} {empLastName}</td>\r\n"
			+ "		<td style=\"width: 125px;\">{leaveStatus}</td>\r\n"
			+ "		<td style=\"width: 125px;\">{cancelledOn}</td>\r\n"
			+ "		<td style=\"width: 126px;\">{leaveReason}</td>\r\n" + "</tr>\r\n"
			+ "</tbody>\r\n  </table> </font>";
	// + " <br>\r\n" + "Regards,<br>\r\n" + "{empFirstName} {empMiddleName}
	// {empLastName}\r\n" + "\r\n" + "</font>";

	public static final String Template_WithdrawLeaveRequestToManager = "<font face=\"verdana\" size=\"2\">\r\n"
			+ "Hi {managerFirstName} {managerLastName},<br><br>Please find the withdraw leave application. Please log on to <i><a href={websiteURL}>{websiteURL}</a> </i>and review the leave.<br><br>\r\n"
			+ "<table border=\"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri;\" >\r\n"
			+ "   <tr>\r\n" + "      <td>Employee Name:</td>\r\n"
			+ "      <td>{empFirstName} {empMiddleName} {empLastName}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Employee ID:</td>\r\n" + "      <td>{empId}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Leave Type:</td>\r\n" + "      <td>{leaveType}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Leave From:</td>\r\n" + "      <td>{fromDate}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "		<td>From session:</td>\r\n" + "      <td>{fromSession}</td>\r\n" + "   </tr>\r\n"
			+ "      <td>Leave To:</td>\r\n" + "      <td>{toDate}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "		<td>To Session:</td>\r\n" + "      <td>{toSession}</td>\r\n" + "   </tr>\r\n" + "<tr>\r\n"
			+ "      <td>No Of Days:</td>\r\n" + "      <td>{noOfDays}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Reason:</td>\r\n" + "      <td>{leaveReason}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Contact No:</td>\r\n" + "      <td>{contactNo}</td>\r\n" + "   </tr>\r\n" + "</table>\r\n"
			+ "		\"<table border = \"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri\">\r\n"
			+ "<tbody>\r\n" + "<tr>\r\n" + "		<td style=\"width: 125px;\">Employee Name</td>\r\n"
			+ "<td style=\"width: 125px;\">Status</td>\r\n" + "		<td style=\"width: 125px;\">Status On</td>\r\n"
			+ "<td style=\"width: 126px;\">Comments</td>\r\n" + "		</tr>\r\n" + "<tr>\r\n"
			+ "		<td style=\"width: 125px;\">{empFirstName} {empMiddleName} {empLastName}</td>\r\n"
			+ "		<td style=\"width: 125px;\">{leaveStatus}</td>\r\n"
			+ "		<td style=\"width: 125px;\">{statusAsOfOn}</td>\r\n"
			+ "		<td style=\"width: 126px;\">{leaveReason}</td>\r\n" + "</tr>\r\n" + "</tbody>\r\n" + "</table>"
			+ "<table border=\"0\" cellspacing=0 cellpadding =2 style=\"margin:10px 5px 10px 100px; font-family: calibri;\" >\r\n"
			+ "   <tr>\r\n"
			+ "      <td><a href={rootIp}/leave/leaveActionByMail/{WDleaveActionApprove}/{leaveId} style=\"background-color: #84A100;color: white;padding: 5px 10px;text-align: center;text-decoration: none;display: inline-block;\">APPROVE</a></td>\r\n"
			+ "	  <td></td>\r\n"
			+ "	  <td><a href={rootIp}/leave/leaveActionByMail/{WDleaveActionReject}/{leaveId} style=\"background-color: #F01F07;color: white;padding: 5px 10px;text-align: center;text-decoration: none;display: inline-block;\">REJECT</a></td>\r\n"
			+ "   </tr>\r\n" + "   \r\n" + "</table>\r\n" + "<br>\r\n" + "Regards From Approver,<br>\r\n"
			+ "{empFirstName} {empLastName}\r\n" + "\r\n" + "</font>";

	public static final String Template_WithdrawLeaveConfirmationToEmployee = "<font face=\"verdana\" size=\"2\">\r\n"
			+ "Hi {empFirstName} {empMiddleName} {empLastName},<br><br>Your leave withdrawl request is pending with your manager,Please find the details below <i><a href={websiteURL}>{websiteURL}</a> </i>and view the leave status.<br><br>\r\n"
			+ "<table border=\"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri;\" >\r\n"
			+ "   <tr>\r\n" + "      <td>Employee Name:</td>\r\n"
			+ "      <td>{empFirstName} {empMiddleName} {empLastName}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Employee ID:</td>\r\n" + "      <td>{empId}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Leave Type:</td>\r\n" + "      <td>{leaveType}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Leave From:</td>\r\n" + "      <td>{fromDate}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "		<td>From session:</td>\r\n" + "      <td>{fromSession}</td>\r\n" + "   </tr>\r\n"
			+ "      <td>Leave To:</td>\r\n" + "      <td>{toDate}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "		<td>To Session:</td>\r\n" + "      <td>{toSession}</td>\r\n" + "   </tr>\r\n" + "<tr>\r\n"
			+ "      <td>No Of Days:</td>\r\n" + "      <td>{noOfDays}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Reason:</td>\r\n" + "      <td>{leaveReason}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Contact No:</td>\r\n" + "      <td>{contactNo}</td>\r\n" + "   </tr>\r\n" + "</table>\r\n"
			+ "		\"<table border = \"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri\">\r\n"
			+ "<tbody>\r\n" + "<tr>\r\n" + "		<td style=\"width: 125px;\">Employee Name</td>\r\n"
			+ "<td style=\"width: 125px;\">Status</td>\r\n" + "		<td style=\"width: 125px;\">Status On</td>\r\n"
			+ "<td style=\"width: 126px;\">Comments</td>\r\n" + "		</tr>\r\n" + "<tr>\r\n"
			+ "		<td style=\"width: 125px;\">{empFirstName} {empMiddleName} {empLastName}</td>\r\n"
			+ "		<td style=\"width: 125px;\">{leaveStatus}</td>\r\n"
			+ "		<td style=\"width: 125px;\">{statusAsOfOn}</td>\r\n"
			+ "		<td style=\"width: 126px;\">{leaveReason}</td>\r\n" + "</tr>\r\n" + "</tbody>\r\n" + "</table>"
			+ "<br>Regards From Approver,<br>\r\n" + "{empFirstName} {empMiddleName} {empLastName}</font>";

	public static final String Template_LeaveApproved = "<font face=\"verdana\" size=\"2\">\r\n"
			+ "Hi {empFirstName} {empMiddleName} {empLastName},<br><br>Your Leave Application has been Accepted by your manager. Please log on to <i><a href={websiteURL}>{websiteURL}</a> </i>and view the leave status.<br><br>\r\n"
			+ "<table border=\"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri;\" >\r\n"
			+ "   <tr>\r\n" + "      <td>Employee Name:</td>\r\n"
			+ "      <td>{empFirstName} {empMiddleName} {empLastName}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Employee ID:</td>\r\n" + "      <td>{empId}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Leave Type:</td>\r\n" + "      <td>{leaveType}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Leave From:</td>\r\n" + "      <td>{fromDate}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "		<td>From session:</td>\r\n" + "      <td>{fromSession}</td>\r\n" + "   </tr>\r\n"
			+ "      <td>Leave To:</td>\r\n" + "      <td>{toDate}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "		<td>To Session:</td>\r\n" + "      <td>{toSession}</td>\r\n" + "   </tr>\r\n" + "<tr>\r\n"
			+ "      <td>No Of Days:</td>\r\n" + "      <td>{noOfDays}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Reason:</td>\r\n" + "      <td>{leaveReason}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Contact No:</td>\r\n" + "      <td>{contactNo}</td>\r\n" + "   </tr>\r\n" + "</table>\r\n"
			+ "		\"<table border = \"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri\">\r\n"
			+ "<tbody>\r\n" + "<tr>\r\n" + "		<td style=\"width: 125px;\">Employee Name</td>\r\n"
			+ "<td style=\"width: 125px;\">Status</td>\r\n" + "		<td style=\"width: 125px;\">Status On</td>\r\n"
			+ "<td style=\"width: 126px;\">Comments</td>\r\n" + "		</tr>\r\n" + "<tr>\r\n"
			+ "		<td style=\"width: 125px;\">{empFirstName} {empMiddleName} {empLastName}</td>\r\n"
			+ "		<td style=\"width: 125px;\">{leaveStatus}</td>\r\n"
			+ "		<td style=\"width: 125px;\">{statusAsOfOn}</td>\r\n"
			+ "		<td style=\"width: 126px;\">{leaveReason}</td>\r\n" + "</tr>\r\n" + "</tbody>\r\n" + "</table>"
			+ "Regards From Approver,<br>\r\n" + "{managerFirstName} {managerMiddleName} {managerLastName}\r\n" + "\r\n"
			+ "</font>";

	public static final String Template_LeaveWithdrawApproved = "<font face=\"verdana\" size=\"2\">\r\n"
			+ "Hi {empFirstName} {empMiddleName} {empLastName},<br><br>Your Withdraw Leave Application has been Accepted by your manager. Please log on to <i><a href={websiteURL}>{websiteURL}</a> </i>and view the leave status.<br><br>\r\n"
			+ "<table border=\"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri;\" >\r\n"
			+ "   <tr>\r\n" + "      <td>Employee Name:</td>\r\n"
			+ "      <td>{empFirstName} {empMiddleName} {empLastName}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Employee ID:</td>\r\n" + "      <td>{empId}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Leave Type:</td>\r\n" + "      <td>{leaveType}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Leave From:</td>\r\n" + "      <td>{fromDate}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "		<td>From session:</td>\r\n" + "      <td>{fromSession}</td>\r\n" + "   </tr>\r\n"
			+ "      <td>Leave To:</td>\r\n" + "      <td>{toDate}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "		<td>To Session:</td>\r\n" + "      <td>{toSession}</td>\r\n" + "   </tr>\r\n" + "<tr>\r\n"
			+ "      <td>No Of Days:</td>\r\n" + "      <td>{noOfDays}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Reason:</td>\r\n" + "      <td>{leaveReason}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Contact No:</td>\r\n" + "      <td>{contactNo}</td>\r\n" + "   </tr>\r\n" + "</table>\r\n"
			+ "		\"<table border = \"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri\">\r\n"
			+ "<tbody>\r\n" + "<tr>\r\n" + "		<td style=\"width: 125px;\">Employee Name</td>\r\n"
			+ "<td style=\"width: 125px;\">Status</td>\r\n" + "		<td style=\"width: 125px;\">Status On</td>\r\n"
			+ "<td style=\"width: 126px;\">Comments</td>\r\n" + "		</tr>\r\n" + "<tr>\r\n"
			+ "		<td style=\"width: 125px;\">{empFirstName} {empMiddleName} {empLastName}</td>\r\n"
			+ "		<td style=\"width: 125px;\">{leaveStatus}</td>\r\n"
			+ "		<td style=\"width: 125px;\">{statusAsOfOn}</td>\r\n"
			+ "		<td style=\"width: 126px;\">{leaveReason}</td>\r\n" + "</tr>\r\n" + "</tbody>\r\n" + "</table>"
			+ "Regards From Approver,<br>\r\n" + "{managerFirstName} {managerMiddleName} {managerLastName}\r\n" + "\r\n"
			+ "</font>";

	public static final String Template_LeaveReject = "<font face=\"verdana\" size=\"2\">\r\n"
			+ "Hi {empFirstName} {empMiddleName} {empLastName},<br><br>Your  Leave Application has been Rejected by your manager. Please log on to <i><a href={websiteURL}>{websiteURL}</a> </i>and view the leave status.<br><br>\r\n"
			+ "<table border=\"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri;\" >\r\n"
			+ "   <tr>\r\n" + "      <td>Employee Name:</td>\r\n"
			+ "      <td>{empFirstName} {empMiddleName} {empLastName}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Employee ID:</td>\r\n" + "      <td>{empId}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Leave Type:</td>\r\n" + "      <td>{leaveType}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Leave From:</td>\r\n" + "      <td>{fromDate}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "		<td>From session:</td>\r\n" + "      <td>{fromSession}</td>\r\n" + "   </tr>\r\n"
			+ "      <td>Leave To:</td>\r\n" + "      <td>{toDate}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "		<td>To Session:</td>\r\n" + "      <td>{toSession}</td>\r\n" + "   </tr>\r\n" + "<tr>\r\n"
			+ "      <td>No Of Days:</td>\r\n" + "      <td>{noOfDays}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Reason:</td>\r\n" + "      <td>{leaveReason}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Contact No:</td>\r\n" + "      <td>{contactNo}</td>\r\n" + "   </tr>\r\n" + "</table>\r\n"
			+ "		\"<table border = \"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri\">\r\n"
			+ "<tbody>\r\n" + "<tr>\r\n" + "		<td style=\"width: 125px;\">Employee Name</td>\r\n"
			+ "<td style=\"width: 125px;\">Status</td>\r\n" + "		<td style=\"width: 125px;\">Status On</td>\r\n"
			+ "<td style=\"width: 126px;\">Comments</td>\r\n" + "		</tr>\r\n" + "<tr>\r\n"
			+ "		<td style=\"width: 125px;\">{empFirstName} {empMiddleName} {empLastName}</td>\r\n"
			+ "		<td style=\"width: 125px;\">{leaveStatus}</td>\r\n"
			+ "		<td style=\"width: 125px;\">{statusAsOfOn}</td>\r\n"
			+ "		<td style=\"width: 126px;\">{leaveReason}</td>\r\n" + "</tr>\r\n" + "</tbody>\r\n" + "</table>"
			+ "Regards From Approver,<br>\r\n" + "{managerFirstName} {managerMiddleName} {managerLastName}\r\n" + "\r\n"
			+ "</font>";

	public static final String Template_WithdrawLeaveRejected = "<font face=\"verdana\" size=\"2\">\r\n"
			+ "Hi {empFirstName} {empMiddleName} {empLastName},<br><br>Your Withdraw Leave Application has been Rejected by your manager. Please log on to <i><a href={websiteURL}>{websiteURL}</a> </i>and view the leave status.<br><br>\r\n"
			+ "<table border=\"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri;\" >\r\n"
			+ "   <tr>\r\n" + "      <td>Employee Name:</td>\r\n"
			+ "      <td>{empFirstName} {empMiddleName} {empLastName}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Employee ID:</td>\r\n" + "      <td>{empId}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Leave Type:</td>\r\n" + "      <td>{leaveType}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Leave From:</td>\r\n" + "      <td>{fromDate}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "		<td>From session:</td>\r\n" + "      <td>{fromSession}</td>\r\n" + "   </tr>\r\n"
			+ "      <td>Leave To:</td>\r\n" + "      <td>{toDate}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "		<td>To Session:</td>\r\n" + "      <td>{toSession}</td>\r\n" + "   </tr>\r\n" + "<tr>\r\n"
			+ "      <td>No Of Days:</td>\r\n" + "      <td>{noOfDays}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Reason:</td>\r\n" + "      <td>{leaveReason}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Contact No:</td>\r\n" + "      <td>{contactNo}</td>\r\n" + "   </tr>\r\n" + "</table>\r\n"
			+ "		\"<table border = \"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri\">\r\n"
			+ "<tbody>\r\n" + "<tr>\r\n" + "		<td style=\"width: 125px;\">Employee Name</td>\r\n"
			+ "<td style=\"width: 125px;\">Status</td>\r\n" + "		<td style=\"width: 125px;\">Status On</td>\r\n"
			+ "<td style=\"width: 126px;\">Comments</td>\r\n" + "		</tr>\r\n" + "<tr>\r\n"
			+ "		<td style=\"width: 125px;\">{empFirstName} {empMiddleName} {empLastName}</td>\r\n"
			+ "		<td style=\"width: 125px;\">{leaveStatus}</td>\r\n"
			+ "		<td style=\"width: 125px;\">{statusAsOfOn}</td>\r\n"
			+ "		<td style=\"width: 126px;\">{leaveReason}</td>\r\n" + "</tr>\r\n" + "</tbody>\r\n" + "</table>"
			+ "Regards From Approver,<br>\r\n" + "{managerFirstName} {managerMiddleName} {managerLastName}\r\n" + "\r\n"
			+ "</font>";

	public static final String Template_Candidate_Creation = "<font face=\"verdana\" size=\"2\">\r\n"
			+ "Dear {candidateFirstName} {candidateMiddleName} {candidateLastName},</br>\r\n" + "<br></br>\r\n"
			+ "<br>Congratulations!</br>\r\n" + "<br></br>\r\n"
			+ "<br>We are pleased to offer you an appointment in Our Company as {candidateDesignation} .</br>\r\n"
			+ "<br></br>\r\n" + "<br>Please do join us on or before specified date as per offer letter.</br>\r\n"
			+ "<br></br>\r\n"
			+ "<br>Kindly log in to the below mentioned URL/ Website in-order to view Offer Letter.</br>\r\n"
			+ "<br></br>\r\n" + "<br>URL:{websiteURL}</br>\r\n" + "<br></br>\r\n"
			+ "<br>Login Id    : {candidateUserName}</br>\r\n" + "<br></br>\r\n"
			+ "<br>Password    : {candidatePassword}</br>\r\n" + "<br></br>\r\n"
			+ "<br>Use the above user id and password whenever you log-in to this portal.</br>\r\n" + "<br></br>\r\n"
			+ "<br>Please ensure you click on Accept option tab in order to accept the offer that has been released.</br>\r\n"
			+ "<br></br>\r\n"
			+ "<br>On receipt of offer acceptance, the HR will trigger the second mail calling for specific documents.</br>\r\n"
			+ "<br></br>\r\n"
			+ "<br>On receipt of second mail from HR, You will have to report at the branches, along with originals and photo copies of docs as specified in the mail.</br>\r\n"
			+ "<br></br>\r\n" + "<br>Please feel free to call me should you need any clarifications.</br>\r\n"
			+ "<br></br>\r\n" + "<br>We look forward to have a mutually beneficial association!</br>\r\n"
			+ "<br></br>\r\n" + "<br><b>Regards,</b><br>\r\n" + "<br><b>Human Resources & Development</b>\r\n"
			+ "<br><b>'Shivaji Niketan' | Tejas Society | Behind Dhondiba Sutar Bus Stand</b>\r\n"
			+ "<br><b>Near Mantri Park | Kothrud | Pune - 411 029. Maharashtra, INDIA. </b>\r\n" + "\r\n" + "</font>";

	public static final String Template_Employee_Creation = "<font face=\"verdana\" size=\"2\">\r\n"
			+ "Dear {employeeFirstName} {employeeMiddleName} {employeeLastName},<br><br>Please find below official HRMS login id and password. The login portal for HRMS is https://hrms.vinsys.com/<br><br>\r\n"
			+ "<table border=\"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri;\" >\r\n"
			+ "<tr>\r\n" + "<td>Employee Name:</td>\r\n"
			+ "<td>{employeeFirstName} {employeeMiddleName} {employeeLastName}</td>\r\n" + "   </tr>\r\n"
			+ "<tr>\r\n" + "<td>HRMS Login ID:</td>\r\n" + "<td>{employeeOfficialEmail}</td>\r\n"
			+ "</tr>\r\n" + "<tr>\r\n" + "<td>Password:</td>\r\n" + "<td>{employeePassword}</td>\r\n"
			+ "</tr>\r\n" + "</table>\r\n" + "<br>Kindly find below standardized signature format to be configured once you open your outlook. In case of any concerns Please reach HR department.<br><br>For New Email:<br>\r\n" + "<br>\r\n"
			+ "-----------------------------------------<br><br><b>Thanks & Regards,</br>\r\n"
			+ "<br>{employeeFirstName} {employeeMiddleName} {employeeLastName}</br>\r\n"
			+ "<br>{employeeDesignation}</br>\r\n" + "<br>{employeeDepartment}</br>\r\n" 
			+ "<br>Vinsys IT Services India Limited.</b></br>\r\n"
			+ "<br>'Shivaji Niketan' | Tejas Society | Behind Dhondiba Sutar Bus Stand</br>\r\n"
			+ "<br>Near Mantri Park | Kothrud | Pune - 411 029. Maharashtra, INDIA.</br>\r\n"
			+ "<br>Board No : +91 20 25382807/43</br>\r\n"
			+ "<br>India - Pune | Hyderabad | Bangalore | Delhi NCR</br>\r\n"
			+ "<br>International - Australia |  China | India | Kenya | Malaysia | Oman | Tanzania | UAE</br>\r\n"
			+ "<br>-----------------------------------------</br>\r\n"
			+ "<br>Disclaimer - \"This e-mail and any attachments to it (in part or in whole the \"Communication\") are confidential, may constitute inside information and are for the use only of the addressee. The Communication is the property of Vinsys IT Services India Limited. and its affiliates and may contain copyright material or intellectual property of Vinsys IT Services India Limited.and/or any of its related entities or of third parties. If you are not the intended recipient of the Communication or have received the Communication in error, please notify the sender or Vinsys IT Services India Limited., immediately, return the Communication (in entirety) and delete the Communication (in entirety and copies included) from your records and systems. Unauthorized use, disclosure or copying of this Communication or any part thereof is strictly prohibited and may be unlawful. Any views expressed in the Communication are those of the individual sender only, unless expressly stated to be those of Vinsys IT Services India Limited. and its affiliates. Vinsys IT Services India Limited., does not guarantee the integrity of the Communication, or that it is free from errors, viruses or interference.\"</br>\r\n"
			+ "\r\n" + "<br></br>\r\n" + "<br><b>Regards,</b></br>\r\n" + "<br><b>Human Resources & Development</b>\r\n"
			+ "<br><b>'Shivaji Niketan' | Tejas Society | Behind Dhondiba Sutar Bus Stand</br>\r\n"
			+ "<br><b>Near Mantri Park | Kothrud | Pune - 411 029. Maharashtra, INDIA. </br>\r\n" + "<br></br>\r\n"
			+ "\r\n" + "</font>";

	public static final String Template_Employee_Change_Password = "<font face=\"verdana\" size=\"2\">\r\n"
			+ "Dear {employeeFirstName} {employeeMiddleName} {employeeLastName},<br><br>Please find your updated credential details.<br><br>\r\n"
			+ "<table border=\"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri;\" >\r\n"
			+ "   <tr>\r\n" + "      <td>Employee Name:</td>\r\n"
			+ "      <td>{employeeFirstName} {employeeMiddleName} {employeeLastName}</td>\r\n" + "   </tr>\r\n"
			+ "   <tr>\r\n" + "      <td>Official Email ID:</td>\r\n" + "      <td>{employeeOfficialEmail}</td>\r\n"
			+ "   </tr>\r\n" + "    <tr>\r\n" + "      <td>Password:</td>\r\n" + "      <td>{employeePassword}</td>\r\n"
			+ "   </tr>\r\n" + "</table>\r\n" + "<br></br>\r\n" + "<br><b>Regards,</b></br>\r\n"
			+ "<br><b>HR Department</b>\r\n"
			+ "<br><b>'Shivaji Niketan' | Tejas Society | Behind Dhondiba Sutar Bus Stand</br>\r\n"
			+ "<br><b>Near Mantri Park | Kothrud | Pune - 411 029. Maharashtra, INDIA. </br>\r\n" + "<br></br>\r\n"
			+ "\r\n" + "</font>";

	// final template offer letter is in data base;
	public static final String final_template_for_offer_letter = "<html><h4><img alt=\\\"\\\" src=\\\"\\\" /></h4>\\r\\n\\r\\n<h4 style=\\\"text-align:justify\\\"><span style=\\\"font-size:11px\\\"><span style=\\\"font-family:calibri,verdana,geneva,sans-serif\\\"><strong>Date :{currentDate}</strong><br />\\r\\n<br />\\r\\n<strong>To,</strong><br />\\r\\n<strong>{candidateFirstName} {candidateMiddleName} {candidateLastName}</strong><br />\\r\\n <strong>{city}</strong><br />\\r\\n <br />\\r\\n <br />\\r\\n <strong>Dear </strong><strong>{candidateFirstName}</strong>,<br />\\r\\n <br />\\r\\n On behalf of Vinsys, I am pleased to confirm our offer of employment as<strong> {candidateDesignation}. </strong>You will be based at {city} office.<br />\\r\\n <strong>We request you to join on </strong><strong>{dateOfJoin}</strong><strong>.</strong><br />\\r\\n <br />\\r\\n We hereby request you to kindly confirm your acceptance. Upon receipt of your acceptance we will issue you the formal appointment letter.<br />\\r\\n Please bring the following original documents, while reporting for duty:<br />\\r\\n 1. Qualification certificates<br />\\r\\n 2. Birth date proof<br />\\r\\n 3. Photo Identity Proof<br />\\r\\n 4. PAN Card<br />\\r\\n 5. Address proof<br />\\r\\n 6. Relieving and Experience Certificate from the previous employer<br />\\r\\n 7. Proof of last salary drawn<br />\\r\\n 8. One I card size photograph<br />\\r\\n <br />\\r\\n Welcome to Vinsys. We look forward to working with you.<br />\\r\\n <br />\\r\\n Sincerely,<br />\\r\\n <strong>Regards,<br />\\r\\n For Vinsys IT Services India Limited.</strong><br />\\r\\n <strong>[AuthorizedSignature]</strong><br />\\r\\n <strong>Mr. Umesh Tharkude</strong><br />\\r\\n <strong>Sr. Manager - HR</strong></span></span></h4>\\r\\n </html>";

	public static final String Document_Verification_Mail_Header = "<html><font face=\"verdana\" size=\"2\"><p>Hi {candidateFirstName} {candidateMiddleName} {candidateLastName},</p>\r\n"
			+ "<p>Document Verfication failed for the below listed document,</p>\r\n"
			+ "<table border=\"1\" cellspacing=0 cellpadding=5 style=\"margin:10px 5px 5px 100px;  font-family: calibri;\" >\r\n"
			+ "<tr><td style=width: 44px;>Sr.No</td>"
			+ "<td style=width: 44px;>Document Type</td><td style=width: 150px;>Document Name</td>\r\n"
			+ "<td style=width: 879px;>Reason</td></tr>";

	public static final String Document_Sending_Mail_Header = "<html><font face=\"verdana\" size=\"2\"><p>Hi {toName} ,</p>"
			+ "<p>Below is the list of documents submitted by candidate <b> {candidateFirstName} {candidateMiddleName} {candidateLastName} </b> with <b>candidate ID {candidateId} </b>, please verify.</p>"
			+ "<table border=\"1\" cellspacing=0 cellpadding=5 style=\"margin:10px 5px 5px 100px;  font-family: calibri;\" >\r\n"
			+ "<tr>" + "<td>Sr.No</td><td>Document Type</td><td>Document Name</td>" + "</tr>";

	public static final String Document_Verification_Mail_Footer = "</table>" + "<br><b>HR Department</b>"
			+ "<br><b>'Shivaji Niketan' | Tejas Society | Behind Dhondiba Sutar Bus Stand</b>\r\n"
			+ "<br><b>Near Mantri Park | Kothrud | Pune - 411 029. Maharashtra, INDIA. </b><br></br>"
			+ "</font><p>&nbsp;</p></font></html>";

	public static final String Template_GrantLeaveApply = "<font face=\"verdana\" size=\"2\">\r\n"
			+ "Hi {managerFirstName} {managerMiddleName} {managerLastName},<br><br>Please find the leave application. Please log on to <i><a href={websiteURL}>{websiteURL}</a> </i>and review the leave.<br><br>\r\n"
			+ "<table border=\"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri;\" >\r\n"
			+ "   <tr>\r\n" + "      <td>Employee Name:</td>\r\n"
			+ "      <td>{empFirstName} {empMiddleName} {empLastName}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Employee ID:</td>\r\n" + "      <td>{empId}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Leave Type:</td>\r\n" + "      <td>{leaveType}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Leave From:</td>\r\n" + "      <td>{fromDate}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Leave To:</td>\r\n" + "      <td>{toDate}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>No Of Days:</td>\r\n" + "      <td>{noOfDays}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Reason:</td>\r\n" + "      <td>{leaveReason}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Applied On:</td>\r\n" + "      <td>{appliedOn}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Status:</td>\r\n" + "      <td>{leaveStatus}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Contact No:</td>\r\n" + "      <td>{contactNo}</td>\r\n" + "   </tr>\r\n" + "</table>\r\n"
			+ "<br>\r\n"
			+ "<table border=\"0\" cellspacing=0 cellpadding =2 style=\"margin:10px 5px 10px 100px; font-family: calibri;\" >\r\n"
			+ "   <tr>\r\n"
			+ "      <td><a href='{rootIp}/employeeGrantLeave/leaveActionByMail/{leaveActionApprove}/{leaveId}'  style=\"background-color: #84A100;color: white;padding: 5px 10px;text-align: center;text-decoration: none;display: inline-block;\">APPROVE</a></td>\r\n"
			+ "	  <td></td>\r\n"
			+ "	  <td><a href='{rootIp}/employeeGrantLeave/leaveActionByMail/{leaveActionReject}/{leaveId})' style=\"background-color: #F01F07;color: white;padding: 5px 10px;text-align: center;text-decoration: none;display: inline-block;\">REJECT</a></td>\r\n"
			+ "   </tr>\r\n" + "   \r\n" + "</table>\r\n" + "Regards,<br>\r\n"
			+ "{empFirstName} {empMiddleName} {empLastName}\r\n" + "\r\n" + "</font>";

	public static final String Template_WithdrawGrantLeaveRequestToManager = "<font face=\"verdana\" size=\"2\">\r\n"
			+ "Hi {managerFirstName} {managerLastName},<br><br>Please find the withdraw leave application. Please log on to <i><a href={websiteURL}>{websiteURL}</a> </i>and review the leave.<br><br>\r\n"
			+ "<table border=\"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri;\" >\r\n"
			+ "   <tr>\r\n" + "      <td>Employee Name:</td>\r\n"
			+ "      <td>{empFirstName} {empMiddleName} {empLastName}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Employee ID:</td>\r\n" + "      <td>{empId}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Leave Type:</td>\r\n" + "      <td>{leaveType}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Leave From:</td>\r\n" + "      <td>{fromDate}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Leave To:</td>\r\n" + "      <td>{toDate}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>No Of Days:</td>\r\n" + "      <td>{noOfDays}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Reason:</td>\r\n" + "      <td>{leaveReason}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Applied On:</td>\r\n" + "      <td>{appliedOn}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Status:</td>\r\n" + "      <td>{leaveStatus}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Contact No:</td>\r\n" + "      <td>{contactNo}</td>\r\n" + "   </tr>\r\n" + "</table>\r\n"
			+ "<table border=\"0\" cellspacing=0 cellpadding =2 style=\"margin:10px 5px 10px 100px; font-family: calibri;\" >\r\n"
			+ "   <tr>\r\n"
			+ "      <td><a href={rootIp}/employeeGrantLeave/leaveActionByMail/{WDleaveActionApprove}/{leaveId} style=\"background-color: #84A100;color: white;padding: 5px 10px;text-align: center;text-decoration: none;display: inline-block;\">APPROVE</a></td>\r\n"
			+ "	  <td></td>\r\n"
			+ "	  <td><a href={rootIp}/employeeGrantLeave/leaveActionByMail/{WDleaveActionReject}/{leaveId} style=\"background-color: #F01F07;color: white;padding: 5px 10px;text-align: center;text-decoration: none;display: inline-block;\">REJECT</a></td>\r\n"
			+ "   </tr>\r\n" + "   \r\n" + "</table>\r\n" + "<br>\r\n" + "Regards From Approver,<br>\r\n"
			+ "{empFirstName} {empLastName}\r\n" + "\r\n" + "</font>";

	public static final String TEMPLATE_LETTER_STATUS_EMAIL = "<html><font face=\"verdana\" size=\"2\">\r\n"
			+ "<p>Hi <strong>{hrName},</strong><br /> <br /> This is to inform you that I am in receipt of the {letterType} date <strong>{dateOfIssue}</strong> &nbsp;issued by your Company. &nbsp;<br /> <br /> However, I have <strong>{action}</strong> the <strong>{letterType}</strong>.</p>\r\n"
			+ "<p><br /> Thanks &amp; Regards,<br /> {candidateFirstName} {candidateMiddleName} {candidateLastName}</p></font></html>";

	/*
	 * public static final String BIRTHDAY_EMAIL_TEMPLATE = "<html>\r\n" + "\r\n" +
	 * "	<p>\r\n" + "	<em>\r\n" +
	 * "		<strong>Happy Birthday &nbsp;{candidateFirstname} {candidateLastname}</strong>\r\n"
	 * + "	</em>\r\n" + "	<br />\r\n" +
	 * "	<h4><img alt=\"\" src=\"data:image/jpeg;base64,{birthDayImage}\"  width=\"600\" height=auto/></h4>"
	 * + "	<br />\r\n" + "	<br />\r\n" + "	<br />\r\n" + "	<em>\r\n" +
	 * " <strong>HR Department\r\n" + " <br />Vinsys\r\n" +
	 * "			<br />Shivaji Niketan | Tejas Society | Behind Dhondiba Sutar Bus Stand \r\n"
	 * +
	 * "			<br />Near Mantri Park | Kothrud | Pune - 411 029. Maharashtra, INDIA. \r\n"
	 * + "			<br />\r\n" + "		</strong>\r\n" + "	</em>\r\n" +
	 * "</p></html>";
	 */

	public static final String BIRTHDAY_EMAIL_TEMPLATE = "<html>\r\n" + "\r\n" + "	<p>\r\n" + "	<em>\r\n"
			+ "		<strong>Happy Birthday &nbsp;{candidateFirstname} {candidateLastname}</strong>\r\n" + "	</em>\r\n"
			+ "<br />" + "		<strong>Department : &nbsp;{departmentName}</strong>\r\n" + "	</em>\r\n"
			+ "	<br />\r\n" + "	<h4><img alt=\"\" src=\"cid:image1\"  width=\"600\" height=auto/></h4>" + "	<br />\r\n"
			+ "	<br />\r\n" + "	<br />\r\n" + "	<em>\r\n" + " <strong>HR Department\r\n" + " <br />Vinsys\r\n"
			+ "			<br />Shivaji Niketan | Tejas Society | Behind Dhondiba Sutar Bus Stand \r\n"
			+ "			<br />Near Mantri Park | Kothrud | Pune - 411 029. Maharashtra, INDIA. \r\n"
			+ "			<br />\r\n" + "		</strong>\r\n" + "	</em>\r\n" + "</p></html>";
	/*
	 * public static final String TEMPLATE_SERVICE_COMPLETION =
	 * "<html><font face=\"verdana\" size=\"2\">" +
	 * "<p>On behalf of Vinsys Family,We are glad to Congratulate everyone for succesful service completion with Vinsys !!!,</p>\r\n"
	 * + "<p> Congratulations Everyone ,</p>\r\n" +
	 * "<table border=\"1\" cellspacing=0 cellpadding=5  width: 10%; style=\"margin:10px 5px 5px 100px;  font-family: calibri;\" >\r\n"
	 * + "<tr><td>Sr.No</td>" +
	 * "<td>Name of Employee</td><td style=width: 150px;>No Of Years</td>\r\n" +
	 * "<td>Location</td></tr>";
	 */

	public static final String TEMPLATE_SERVICE_COMPLETION = "<html>\r\n" + "\r\n" + "	<p>\r\n" + "	<em>\r\n"
			+ "		<strong>Dear&nbsp;{candidateFirstname} {candidateLastname}</strong>\r\n" + "	</em>\r\n"
			+ "	<br />\r\n" + " <strong>Department : &nbsp;{departmentName}</strong>\r\n" + "	</em>\r\n"
			+ "	<h4><img alt=\"\" src=\"cid:image1\" width=\"600\" height=auto/></h4>" + "	<br />\r\n" + "	<br />\r\n"
			+ "	<br />\r\n" + "	<em>\r\n"
			+ "		<strong>Congratulations! On behalf of Vinsys Family, We are glad to congratulate you for successful service completion of {completedYears} years with Vinsys !!! </strong>\r\n"
			+ "	</em>\r\n" + "	<br /> &nbsp;\r\n" + "	<br />\r\n" + "	<br />\r\n" + "	<em>\r\n" + "<p>\r\n"
			+ "	<em>\r\n" + "		<strong>HR Department\r\n" + " <br />Vinsys\r\n"
			+ "			<br />Shivaji Niketan | Tejas Society | Behind Dhondiba Sutar Bus Stand \r\n"
			+ "			<br />Near Mantri Park | Kothrud | Pune - 411 029. Maharashtra, INDIA. \r\n"
			+ "			<br />\r\n" + "		</strong>\r\n" + "	</em>\r\n" + "</p></html>";

	public static final String TEMPLATE_SERVICE_COMPLETION_FOOTER = "</table>" + "<br><b>HR Department</b>"
			+ "<br><b>'Shivaji Niketan' | Tejas Society | Behind Dhondiba Sutar Bus Stand</b>\r\n"
			+ "<br><b>Near Mantri Park | Kothrud | Pune - 411 029. Maharashtra, INDIA. </b><br></br>"
			+ "</font><p>&nbsp;</p></font></body></html>";

	public static final String TEMPLATE_EMAIL_FOOTER = "</table>" + "<br><b>HR Department</b>"
			+ "<br><b>'Shivaji Niketan' | Tejas Society | Behind Dhondiba Sutar Bus Stand</b>\r\n"
			+ "<br><b>Near Mantri Park | Kothrud | Pune - 411 029. Maharashtra, INDIA. </b><br></br>"
			+ "</font><p>&nbsp;</p></font></html>";

	public static final String LEAVE_RESPONSE_VIA_EMAIL_APPROVED = "<!DOCTYPE html>\r\n" + "<html>\r\n"
			+ "<body onload=\"myFunction()\">\r\n" + "\r\n" + "<script>\r\n" + "function myFunction() {\r\n"
			+ "    alert(\"Leave Succesfully Approved\");\r\n" + "}\r\n" + "</script>\r\n" + "\r\n" + "</body>\r\n"
			+ "</html>";

	public static final String LEAVE_RESPONSE_VIA_EMAIL_REJECTED = "<!DOCTYPE html>\r\n" + "<html>\r\n"
			+ "<body onload=\"myFunction()\">\r\n" + "\r\n" + "<script>\r\n" + "function myFunction() {\r\n"
			+ "    alert(\"Leave Succesfully Rejected\");\r\n" + "}\r\n" + "</script>\r\n" + "\r\n" + "</body>\r\n"
			+ "</html>";

	public static final String LEAVE_RESPONSE_VIA_EMAIL_WD_APPROVED = "<!DOCTYPE html>\r\n" + "<html>\r\n"
			+ "<body onload=\"myFunction()\">\r\n" + "\r\n" + "<script>\r\n" + "function myFunction() {\r\n"
			+ "    alert(\"Leave withdraw Succesfully Approved\");\r\n" + "}\r\n" + "</script>\r\n" + "\r\n"
			+ "</body>\r\n" + "</html>";
	public static final String LEAVE_RESPONSE_VIA_EMAIL_WD_REJECTED = "<!DOCTYPE html>\r\n" + "<html>\r\n"
			+ "<body onload=\"myFunction()\">\r\n" + "\r\n" + "<script>\r\n" + "function myFunction() {\r\n"
			+ "    alert(\"Leave Withdraw succesfully Rejected\");\r\n" + "}\r\n" + "</script>\r\n" + "\r\n"
			+ "</body>\r\n" + "</html>";

	public static final String GRANT_LEAVE_RESPONSE_VIA_EMAIL_APPROVED = "<!DOCTYPE html>\r\n" + "<html>\r\n"
			+ "<body onload=\"myFunction()\">\r\n" + "\r\n" + "<script>\r\n" + "function myFunction() {\r\n"
			+ "    alert(\"Grant Leave Succesfully Approved\");\r\n" + "}\r\n" + "</script>\r\n" + "\r\n"
			+ "</body>\r\n" + "</html>";

	public static final String GRANT_LEAVE_RESPONSE_VIA_EMAIL_REJECTED = "<!DOCTYPE html>\r\n" + "<html>\r\n"
			+ "<body onload=\"myFunction()\">\r\n" + "\r\n" + "<script>\r\n" + "function myFunction() {\r\n"
			+ "    alert(\"Gant Leave Succesfully Rejected\");\r\n" + "}\r\n" + "</script>\r\n" + "\r\n" + "</body>\r\n"
			+ "</html>";

	public static final String GRANT_LEAVE_RESPONSE_VIA_EMAIL_WD_APPROVED = "<!DOCTYPE html>\r\n" + "<html>\r\n"
			+ "<body onload=\"myFunction()\">\r\n" + "\r\n" + "<script>\r\n" + "function myFunction() {\r\n"
			+ "    alert(\"Grant Leave withdraw Succesfully Approved\");\r\n" + "}\r\n" + "</script>\r\n" + "\r\n"
			+ "</body>\r\n" + "</html>";
	public static final String GRANT_LEAVE_RESPONSE_VIA_EMAIL_WD_REJECTED = "<!DOCTYPE html>\r\n" + "<html>\r\n"
			+ "<body onload=\"myFunction()\">\r\n" + "\r\n" + "<script>\r\n" + "function myFunction() {\r\n"
			+ "    alert(\"Grant Leave Withdraw succesfully Rejected\");\r\n" + "}\r\n" + "</script>\r\n" + "\r\n"
			+ "</body>\r\n" + "</html>";

	/*
	 * For Separation
	 */

	public static final String Template_Empployee_Resignation_Apply = "<font face=\"verdana\" size=\"2\">\r\n"
			+ "Hi {managerFirstName} {managerMiddleName} {managerLastName},<br><br>{mailContent}.Kindly log on to <i><a href={websiteURL}>{websiteURL}</a> </i>and review.<br><br>\r\n"
			+ "<table border=\"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri;\" >\r\n"
			+ "   <tr>\r\n" + "      <td>Employee Name:</td>\r\n"
			+ "      <td>{empFirstName} {empMiddleName} {empLastName}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Employee ID:</td>\r\n" + "      <td>{empId}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Designation:</td>\r\n" + "      <td>{empDesignation}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Joining Date:</td>\r\n" + "      <td>{empDateofJoining}</td>\r\n" + "   </tr>\r\n"
			+ "   <tr>\r\n" + "		<td>Notice Period:</td>\r\n" + "      <td>{empnoticePeriod}</td>\r\n"
			+ "   </tr>\r\n" + "      <td>Resigned Date:</td>\r\n" + "      <td>{empResignedDate}</td>\r\n"
			+ "   </tr>\r\n" + "   <tr>\r\n" + "		<td>Relieving Date:</td>\r\n"
			+ "      <td>{empRelievingDate}</td>\r\n" + "   </tr>\r\n" + "<tr>\r\n" + "      <td>Reason:</td>\r\n"
			+ "      <td>{empSeparationReason}</td>\r\n" + "   </tr>\r\n" + "</table>\r\n" + "Regards,<br>\r\n"
			+ "HR Team \r\n" + "<br>\r\n" + "Vinsys\r\n" + "</font>";
	
	
	public static final String Template_Empployee_Resignation_Withdraw = "<font face=\"verdana\" size=\"2\">\r\n"
			+ "Hi {managerFirstName} {managerMiddleName} {managerLastName},<br><br>The following Employee has Withdraw Resignation.Kindly log on to <i><a href={websiteURL}>{websiteURL}</a> </i>and review.<br><br>\r\n"
			+ "<table border=\"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri;\" >\r\n"
			+ "   <tr>\r\n" + "      <td>Employee Name:</td>\r\n"
			+ "      <td>{empFirstName} {empMiddleName} {empLastName}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Employee ID:</td>\r\n" + "      <td>{empId}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Designation:</td>\r\n" + "      <td>{empDesignation}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Joining Date:</td>\r\n" + "      <td>{empDateofJoining}</td>\r\n" + "   </tr>\r\n"
			+ "   <tr>\r\n" + "		<td>Notice Period:</td>\r\n" + "      <td>{empnoticePeriod}</td>\r\n"
			+ "   </tr>\r\n" + "      <td>Resigned Date:</td>\r\n" + "      <td>{empResignedDate}</td>\r\n"
			+ "   </tr>\r\n" + "   <tr>\r\n" + "		<td>Relieving Date:</td>\r\n"
			+ "      <td>{empRelievingDate}</td>\r\n" + "   </tr>\r\n" + "<tr>\r\n" + "      <td>Reason:</td>\r\n"
			+ "      <td>{empSeparationReason}</td>\r\n" + "   </tr>\r\n" + "</table>\r\n" + "Regards,<br>\r\n"
			+ "HR Team \r\n" + "<br>\r\n" + "Vinsys\r\n" + "</font>";

	public static final String Template_Empployee_Resignation_Approve = "<font face=\"verdana\" size=\"2\">\r\n"
			+ "Hi {empFirstName} {empMiddleName} {empLastName},<br><br>This is to inform you that your Resignation Application has been {status}. Kindly log on to <i><a href={websiteURL}>{websiteURL}</a> </i>to view more details.<br><br>\r\n"
			+ "Regards,<br><br>\r\n" + "HR Team \r\n" + "<br>\r\n" + "Vinsys\r\n" + "</font>";
	
	/*
	 * Added new email template constant which contains mail content of employee resignation withdraw approved. 
	*/
	public static final String Template_Empployee_Resignation_Application_Has_Been_Withdraw_By_Approve = "<font face=\"verdana\" size=\"2\">\r\n"
			+ "Hi {empFirstName} {empMiddleName} {empLastName},<br><br>This is to inform you that your Resignation Application has been withdrawn by {status}. Kindly log on to <i><a href={websiteURL}>{websiteURL}</a> </i>to view more details.<br><br>\r\n"
			+ "Regards,<br><br>\r\n" + "HR Team \r\n" + "<br>\r\n" + "Vinsys\r\n" + "</font>";

	public static final String Template_Empployee_Resignation_Approve_By_ORG_LEVEL = "<font face=\"verdana\" size=\"2\">\r\n"
			+ "Hi {empFirstName} {empMiddleName} {empLastName},<br><br>This is to inform you that your resignation has been accepted by your Reporting Head. Your last working day in the organization is {relievingDate}. Kindly ensure your attendance is updated till Last Working Day (LWD) & you apply all the leaves on HRMS, submit OOO & relevant documents/proofs. You must be aware that Leaves can't be availed after submitting resignation. Incase if you are looking to prepone the last date of working. Please contact HRD<br><br>\r\n"
			+ "<b> NOTE: In case you wish to withdraw your resignation; you need to contact your Reporting Head. Also ensure you accept the rejection done by them on HRMS.</b><br><br>\r\n"
			+ "<b> NOTE: Group Medical Insurance will get expired after your last working day.</b><br><br>"
			+ "<b> NOTE: Kindly collect all your payslips from the portal before your last working day. After that your access to the portal will be discontinue..</b><br><br> "
			+ " <b> Kindly fill Exit Interview form on HRMS  for further proceedings. Go to Myself -> Separation -> Exit Interview process. (process has to be mentioned as per our HRMS).</b><br><br>\r\n"
			+ "As per the terms and conditions of your employment, in case notice period is not served/partially served by you, and if this has not been approved by the organization, salary in lieu thereof will be recovered from your full and final settlement. <br><br>\r\n"
			+ "You need to contact HRD for Exit process within 1 week of your resignation application.<br><br>\r\n"
			+ "You are requested to return all assets of the company on or before your Last Working Day to the relevant stakeholders and close out all clearances to facilitate timely calculation & settlement of final dues.<br><br>\r\n"
			+ "<i> 1) Account Department :- Advances, Settlement, Reimbursement if Any & other Loan. </i> <br><br>\r\n"
			+ "<i> 2) Admin :- Mobile Sim card, ID Card, Visiting Card, Any other Documents / Papers, Laptop, Internet Data Card, CDs, Pen drive, Keys etc.</i><br><br>\r\n"
			+ "<i> 3) ITFM :- Desktop, Keyboard, Mouse or any other IT assets issued to you </i><br><br>\r\n"
			+ "<i> 4) HRD :- I Card  </i><br><br><br>\r\n"
			+ "In case of any pending assets/payments the same will be recovered in your Full and Final Settlement.<br><br>\r\n"
			+ "As a standard procedure we take this opportunity to request you of your continuing obligation under and in the terms of all the applicable provisions of the Non- Disclosure of Business Conduct Guidelines, which we are confident that you shall duly respect. <br><br>\r\n"
			+ "<ul><li><b> &nbsp; Department clearance, reliving and experience letter pl contact below person</b></li></ul>"
			+ "&nbsp; &nbsp;&nbsp;&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;  Mr. Umesh Tharkude | umesh@vinsys.com  "
			+ "<ul><li><b> &nbsp; For PF withdrawal / PF transfer pl contact below person </b></li></ul>"
			+ "&nbsp; &nbsp;&nbsp;&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;   Ms. Chaitali Chavan  | chaitali.chavan@vikvinconsultants.com  <br><br>\r\n"
			+ "As you may be aware, the company contributes 12% of your Basic salary + DA towards the Provident Fund (PF) & Deducts equal percentage as your contribution every month. The same is remitted to Regional Provident Fund Commission (RPFC), Pune, Maharashtra. The government body administers the Vinsys employee's Provident Fund.<br><br>\r\n"
			+ "After separation from the services of Vinsys, you have two options available with respect to your PF.<br><br>\r\n"
			+ "&nbsp; &nbsp; 1)      You can either withdraw your PF amount or<br><br>\r\n"
			+ "&nbsp; &nbsp; 2)      Transfer the PF amount to your new PF account<br><br>\r\n"
			+ "<u>Withdrawals :-</u><br><br>\r\n"
			+ "If you wish to withdraw your PF amount, you need to visit <br>&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"https://www.epfindia.gov.in/site_en/For_Employees.php \">https://www.epfindia.gov.in/site_en/For_Employees.php </a><br><br>\r\n"
			+ "&nbsp; 1)      Submitted only after 60 days of your Last working day<br>\r\n"
			+ "&nbsp; 2)      It should be in specified format. You can download the form from mentioned link <br>\r\n"
			+ "&nbsp;&nbsp;&nbsp;&nbsp;<a href=\\\"https://www.epfindia.gov.in/site_en/For_Employees.php \\\">https://www.epfindia.gov.in/site_en/For_Employees.php </a><br>\r\n"
			+ "&nbsp; 3)      Once you submit the online PF form pl get in touch with us.<br><br>\r\n"
			// + "&nbsp; 4) Cancel cheque to be attached along with PF withdrawal form.
			// *Bank account should be Saving account & not Joint Account.<br><br>\r\n"
			+ "Once the form is submitted to RPFC, Vinsys will have no control over the working of RPFC. You need to coordinate directly with RPFC, Pune for any update on your withdrawal (RPFC, Pune contact details are given below)<br><br>\r\n"
			+ "<u>Transfer:-</u><br><br>\r\n"
			+ "If you wish to transfer your PF amount to your new employer's PF account, you need to coordinate the same with your new employer.<br><br>\r\n"
			+ "Contact details of RPFC, Pune,<br><br>\r\n" + "PF Commissioner<br><br>\r\n"
			+ "Employees' Provident Fund,<br>\r\n" + "2nd and 3rd Floor, Pune Cantonment Board Building, <br>\r\n"
			+ "Near Golibar Maidan, Camp, <br>\r\n" + "Pune-411001<br><br>\r\n"
			+ "<u><i><strong> Note:-</u><strong> </i><br><br>\r\n"
			+ "<u><i>1. Kindly collect all your payslips from the portal before your last working day. After that your access to the portal will be discontinue.</u></i><br><br>\r\n"
			+ "<u><i>2. Group Medical Insurance will get expired after your last working day.</u></i><br><br><br>\r\n"
			+ "<u><i> We wish you all the best for your all future assignments. Be in touch.</u></i><br><br><br>\r\n"
			+ "Regards,<br><br>\r\n" + "HR Team \r\n" + "<br>\r\n" + "Vinsys\r\n" + "</font>";
	
	public static final String Template_Empployee_Resignation_Approve_By_ORG_LEVEL_for_UAE = "<font face=\"verdana\" size=\"2\">\r\n"
			+ "Hi {empFirstName} {empMiddleName} {empLastName},<br><br>This is to inform you that your resignation has been accepted by your Reporting Head. Your last working day in the organization is {relievingDate}. Kindly ensure your attendance is updated till Last Working Day (LWD) & you apply all the leaves on HRMS, submit OOO & relevant documents/proofs. You must be aware that Leaves can't be availed after submitting resignation. Incase if you are looking to prepone the last date of working. Please contact HRD<br><br>\r\n"
			+ "<b> NOTE: In case you wish to withdraw your resignation; you need to contact your Reporting Head. Also ensure you accept the rejection done by them on HRMS.</b><br><br>\r\n"
			//+ "<b> NOTE: Group Medical Insurance will get expired after your last working day.</b><br><br>"
			+ "<b> NOTE: Kindly collect all your payslips from the portal before your last working day. After that your access to the portal will be discontinue..</b><br><br> "
			+ " <b> Kindly fill Exit Interview form on HRMS  for further proceedings. Go to Myself -> Separation -> Exit Interview process. (process has to be mentioned as per our HRMS).</b><br><br>\r\n"
			+ "As per the terms and conditions of your employment, in case notice period is not served/partially served by you, and if this has not been approved by the organization, salary in lieu thereof will be recovered from your full and final settlement. <br><br>\r\n"
			+ "You need to contact HRD for Exit process within 1 week of your resignation application.<br><br>\r\n"
			+ "You are requested to return all assets of the company on or before your Last Working Day to the relevant stakeholders and close out all clearances to facilitate timely calculation & settlement of final dues.<br><br>\r\n"
			+ "<i> 1) Account Department :- Advances, Settlement, Reimbursement if Any & other Loan. </i> <br><br>\r\n"
			+ "<i> 2) Admin :- Mobile Sim card, ID Card, Visiting Card, Any other Documents / Papers, Laptop, Internet Data Card, CDs, Pen drive, Keys etc.</i><br><br>\r\n"
			+ "<i> 3) IT Team :- Desktop, Keyboard, Mouse or any other IT assets issued to you </i><br><br>\r\n"
			+ "<i> 4) HRD :- I Card  </i><br><br><br>\r\n"
			+ "In case of any pending assets/payments the same will be recovered in your Full and Final Settlement.<br><br>\r\n"
			+ "As a standard procedure we take this opportunity to request you of your continuing obligation under and in the terms of all the applicable provisions of the Non- Disclosure of Business Conduct Guidelines, which we are confident that you shall duly respect. <br><br>\r\n"
			+ "<ul><li><b> &nbsp; Department clearance, reliving and experience letter pl contact below person</b></li></ul>"
			+ "&nbsp; &nbsp;&nbsp;&nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;  Mr. Umesh Tharkude | umesh@vinsys.com <br><br><br> "
			+ "&nbsp;<u><i> <b>We wish you all the best for your all future assignments. Be in touch.</b></u></i><br><br><br>\r\n"
			+ "<b>Regards,</b><br><br>\r\n" + "<b>HR Team</b> \r\n" + "<br>\r\n" + "<b>Vinsys</b>\r\n" + "</font>";


	public static final String Template_FOR_CHECKLIST_APPROVALS = "<font face=\"verdana\" size=\"2\">\r\n"
			+ "Hi All,<br><br>{empFirstName} {empMiddleName} {empLastName}-{empId},<br><br>Department Handover Checklist is pending for your Approval.Kindly log on to <i><a href={websiteURL}>{websiteURL}</a> </i>and review.<br><br>\r\n"
			+ "<table border=\"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri;\" >\r\n"
			+ "   <tr>\r\n" + "      <td>Employee Name:</td>\r\n"
			+ "      <td>{empFirstName} {empMiddleName} {empLastName}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Employee ID:</td>\r\n" + "      <td>{empId}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Designation:</td>\r\n" + "      <td>{empDesignation}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Joining Date:</td>\r\n" + "      <td>{empDateofJoining}</td>\r\n" + "   </tr>\r\n"
			+ "   <tr>\r\n" + "		<td>Notice Period:</td>\r\n" + "      <td>{empnoticePeriod}</td>\r\n"
			+ "   </tr>\r\n" + "      <td>Resigned Date:</td>\r\n" + "      <td>{empResignedDate}</td>\r\n"
			+ "   </tr>\r\n" + "   <tr>\r\n" + "		<td>Relieving Date:</td>\r\n"
			+ "      <td>{empRelievingDate}</td>\r\n" + "   </tr>\r\n" + "<tr>\r\n" + "      <td>Reason:</td>\r\n"
			+ "      <td>{empSeparationReason}</td>\r\n" + "   </tr>\r\n" + "</table>\r\n" + "Regards,<br>\r\n"
			+ "HR Team \r\n" + "<br>\r\n" + "Vinsys\r\n" + "</font>";

	public static final String Template_Empployee_SUBMIT_EXIT_FEEDBACK_FORM = "<font face=\"verdana\" size=\"2\">\r\n"
			+ "Hi HR Team,<br><br>This is to inform you that, {empFirstName} {empMiddleName} {empLastName} has filled exit feedback form. Kindly log on to <i><a href={websiteURL}>{websiteURL}</a> </i>to view more details.<br><br>\r\n"
			+ "Thanks & Regards,<br><br>\r\n" + "{empFirstName} {empMiddleName} {empLastName}" + "</font>";

	public static final String Template_FOR_ALL_APPROVE_HANDOVERCHECKLIST_SUBMITTED = "<font face=\"verdana\" size=\"2\">\r\n"
			+ "Hi HR Team,<br><br>This is to inform you that handover checklist has been approved by all the departments. Kindly log on to <i><a href={websiteURL}>{websiteURL}</a> </i>to view more details.<br><br>\r\n"
			+ "Thanks & Regards,<br><br>\r\n" + "HRMS Team." + "</font>";

	public static final String Template_SeparationReminder_forRO = "<font face=\"verdana\" size=\"2\">\r\n"
			+ "Hi {managerFirstName} {managerMiddleName} {managerLastName},<br><br>{mailContent}.Kindly log on to <i><a href={websiteURL}>{websiteURL}</a> </i>and review.<br><br>\r\n"
			+ "<table border=\"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri;\" >\r\n"
			+ "   <tr>\r\n" + "      <td>Employee Name:</td>\r\n"
			+ "      <td>{empFirstName} {empMiddleName} {empLastName}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Employee ID:</td>\r\n" + "      <td>{empId}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Designation:</td>\r\n" + "      <td>{empDesignation}</td>\r\n" + "   </tr>\r\n" + "   <tr>\r\n"
			+ "      <td>Joining Date:</td>\r\n" + "      <td>{empDateofJoining}</td>\r\n" + "   </tr>\r\n"
			+ "   <tr>\r\n" + "		<td>Notice Period:</td>\r\n" + "      <td>{empnoticePeriod}</td>\r\n"
			+ "   </tr>\r\n" + "      <td>Resigned Date:</td>\r\n" + "      <td>{empResignedDate}</td>\r\n"
			+ "   </tr>\r\n" + "   <tr>\r\n" + "		<td>Relieving Date:</td>\r\n"
			+ "      <td>{empRelievingDate}</td>\r\n" + "   </tr>\r\n" + "<tr>\r\n" + "      <td>Reason:</td>\r\n"
			+ "      <td>{empSeparationReason}</td>\r\n" + "   </tr>\r\n" + "</table>\r\n" + "Regards,<br>\r\n"
			+ "HR Team \r\n" + "<br>\r\n" + "Vinsys\r\n" + "</font>";

	/**
	 * TRAVEL DESK EMAIL TEMPLATE
	 */

	public static final String TEMPLATE_APPROVER_CONTENT = "<div style=\"font-family:calibri;  font-size: 15px;\">Dear {recipientEmployeeName},</p>\r\n"
			+ "<p>Please find the travel plan application. Please log on to <em><a href={websiteURL}>{websiteURL}</a></em> </i>and review the travel request.</div>\r\n"
			+ "<p style=\"font-family:calibri;  text-decoration: underline; font-size: 15px;\">\r\n"
			+ "<b>Request Details:</b> </p><table style=\"font-family:verdana; font-size: 11px;\" border=\"1\"  cellspacing=\"0\" cellpadding=\"4\">"
			+ "<tbody><tr  style=\"color:white;\" bgcolor=\"#e98700\">" + "<td>Request Id </td>"
			+ "<td>Requested By</td>" + "<td>Ticket Booking</td>" + "<td>Cab Booking</td>" + "<td>Accomodation</td>"
			+ "<td>Client Name</td>"
			// + "<td style=\"width: 133px;\">BD Name</td>"
			+ "<td>Business Unit</td>" + "<td>WON </td>" + "</tr>" + "<tr><td>{requestId}</td>" + "<td>{requestBy}</td>"
			+ "<td>{ticket}</td>" + "<td>{cab}</td>" + "<td>{accomodation}</td>" + "<td>{client}</td>"
			// + "<td style=\"width: 133px;\">{bdName}</td>"
			+ "<td>{buName}</td>" + "<td>{won}</td></tr>" + "</tbody></table>" + "{ticketRequest}" + "{cabRequestBase}"
			+ "{accomodationRequest}"
			+ "<p style=\"font-family:calibri; text-decoration: underline; font-size: 15px;\">\r\n"
			+ "<b>Comments:</b></p><div style=\"font-family:verdana;  font-size: 11px; padding-left: 30px;\"><i>{comment}</i></div><br><br>"
			+ "</font><div style=\"font-family:calibri; font-size: 15px;\"><b>--<br>Thanks & Regards, <br> Team TravelDesk, <br><br>Vinsys IT Services India Limited. </b><br>'Shivaji Niketan' | Tejas Society | Behind Dhondiba Sutar Bus Stand<br>Near Mantri Park | Kothrud | Pune - 411 029. Maharashtra, INDIA. <br><I>India - Pune | Hyderabad | Bangalore | NCR<br>International - Australia |  China | India | Kenya | Malaysia | Oman | Tanzania | UAE </I></div>";

	public static final String TEMPLATE_TD_TO_REQUESTER_ON_REJECT = "<font face=\"verdana\" size=\"2\"><p>Dear {recipientEmployeeName},</p>\r\n"
			+ "<p>Your travel request has been rejected. Please find below details.</p>\r\n"
			+ "<table style=\"height: 59px;\" border=\"1\" width=\"558\">\r\n" + "<tbody>"
			+ "<tr style=\"color:white;\" bgcolor=\"#e98700\">" + "<td>Request By</td>" + "<td>Ticket Booking</td>"
			+ "<td>Cab Booking</td>" + "<td>Accomodation</td>" + "</tr>" + "<tr>" + "<td>{requestBy}</td>"
			+ "<td>{ticket}</td>" + "<td style=\"width: 133px;\">{cab}</td>" + "<td>{accomodation}</td>" + "</tr>"
			+ "</tbody>" + "</table>" + "{ticketRequest}" + "{cabRequestBase}" + "{accomodationRequest}"
			+ "<br>Reject Comment </br>" + "<br>{comment}</br>" + "<br>" + "Thanks & Regards" + "<br>" + "{requestBy}"
			+ "</font>";

	public static final String TEMPLATE_NOTIFICATION_TO_REQUESTOR = "<p>Hi {employeeName},</p>\r\n"
			+ "<p>Your request has been processed. Please find attached details for your travel request.</p>\r\n"
			+ "<p>Thanks &amp; Regards</p>\r\n" + "<p>Traveldesk Dept</p>";

	public static final String TEMPLATE_TD_APPROVER_ACTION_MAIL = "<font face=\"verdana\" size=\"2\"><p>Dear {recipientEmployeeName},</p>\r\n"
			+ "<p>Please find below details of approver action and travel request.</p>\r\n"
			+ "<br>{mainDetails}<br><br>{ticketRequest}{cabRequestBase}{accomodationRequest}</font>"
			+ "</font><div style=\"font-family:calibri; font-size: 15px;\"><b>--<br>Thanks & Regards, <br> {approverName}, <br><br>Vinsys IT Services India Limited. </b><br>'Shivaji Niketan' | Tejas Society | Behind Dhondiba Sutar Bus Stand<br>Near Mantri Park | Kothrud | Pune - 411 029. Maharashtra, INDIA. <br><I>India - Pune | Hyderabad | Bangalore | NCR<br>International - Australia |  China | India | Kenya | Malaysia | Oman | Tanzania | UAE </I></div>";

	public static final String TEMPLATE_REQUEST_CANCELLATION = "<div style=\"font-family:calibri;  font-size: 15px;\">Dear {recipientEmployeeName},</p>\r\n"
			+ "<p>Please find the travel plan application cancellation . Please log on to <em><a href={websiteURL}>{websiteURL}</a></em> </i>and review the travel request.</div>\r\n"
			+ "<p style=\"font-family:calibri;  text-decoration: underline; font-size: 15px;\">\r\n"
			+ "<b>Request Details:</b> </p><table style=\"font-family:verdana; font-size: 11px;\" border=\"1\"  cellspacing=\"0\" cellpadding=\"4\">"
			+ "<tbody><tr  style=\"color:white;\" bgcolor=\"#e98700\">" + "<td>Request Id </td>"
			+ "<td>Requested By</td>" + "<td>Ticket Cancellation</td>" + "<td>Cab Cancellation</td>"
			+ "<td>Accomodation Cancellation</td>" + "<td>Client Name</td>"
			// + "<td style=\"width: 133px;\">BD Name</td>"
			+ "<td>Business Unit</td>" + "<td>WON </td>" + "</tr>" + "<tr><td>{requestId}</td>" + "<td>{requestBy}</td>"
			+ "<td>{ticket}</td>" + "<td>{cab}</td>" + "<td>{accomodation}</td>" + "<td>{client}</td>"
			// + "<td style=\"width: 133px;\">{bdName}</td>"
			+ "<td>{buName}</td>" + "<td>{won}</td></tr>" + "</tbody></table>" + "{ticketRequest}" + "{cabRequestBase}"
			+ "{accomodationRequest}"
			+ "<p style=\"font-family:calibri; text-decoration: underline; font-size: 15px;\">\r\n"
			+ "<b>Comments:</b></p><div style=\"font-family:verdana;  font-size: 11px; padding-left: 30px;\"><i>{comment}</i></div><br><br>"
			+ "</font><div style=\"font-family:calibri; font-size: 15px;\"><b>--<br>Thanks & Regards, <br> {requestBy}, <br><br>Vinsys IT Services India Limited. </b><br>'Shivaji Niketan' | Tejas Society | Behind Dhondiba Sutar Bus Stand<br>Near Mantri Park | Kothrud | Pune - 411 029. Maharashtra, INDIA. <br><I>India - Pune | Hyderabad | Bangalore | NCR<br>International - Australia |  China | India | Kenya | Malaysia | Oman | Tanzania | UAE </I></div>";

	public static final String TEMPLATE_TD_TO_REQUESTER_ASSIGNED_CAB_DETAILS = "<div style=\"font-family:calibri;  font-size: 15px;\">Dear {requestBy},</p>\r\n"
			+ "<p>Your cab has been booked,Please find the cab assignment details. Please log on to <em><a href={websiteURL}>{websiteURL}</a></em> </i>and review the travel request.</div>\r\n"
			+ "<p style=\"font-family:calibri;  text-decoration: underline; font-size: 15px;\">\r\n"
			+ "<b>Request Details:</b> </p><table style=\"font-family:verdana; font-size: 11px;\" border=\"1\"  cellspacing=\"0\" cellpadding=\"4\">"
			+ "<tbody><tr  style=\"color:white;\" bgcolor=\"#e98700\">" + "<td>Request Id </td>"
			+ "<td>Requested By</td>" + "<td>Ticket Booking</td>" + "<td>Cab Booking</td>" + "<td>Accomodation</td>"
			+ "<td>Client Name</td>"
			// + "<td style=\"width: 133px;\">BD Name</td>"
			+ "<td>Business Unit</td>" + "<td>WON </td>" + "</tr>" + "<tr><td>{requestId}</td>" + "<td>{requestBy}</td>"
			+ "<td>{ticket}</td>" + "<td>{cab}</td>" + "<td>{accomodation}</td>" + "<td>{client}</td>"
			// + "<td style=\"width: 133px;\">{bdName}</td>"
			+ "<td>{buName}</td>" + "<td>{won}</td></tr>" + "</tbody></table>"
			+ "{cabRequestTravelDetailsSB}{cabAssignDetailsSB}<br>"
			+ "<b>Comments:</b></p><div style=\"font-family:verdana;  font-size: 11px; padding-left: 30px;\"><i>{comment}</i></div><br><br>"
			+ "</font><div style=\"font-family:calibri; font-size: 15px;\"><b>--<br>Thanks & Regards, <br> Team TravelDesk, <br><br>Vinsys IT Services India Limited. </b><br>'Shivaji Niketan' | Tejas Society | Behind Dhondiba Sutar Bus Stand<br>Near Mantri Park | Kothrud | Pune - 411 029. Maharashtra, INDIA. <br><I>India - Pune | Hyderabad | Bangalore | NCR<br>International - Australia |  China | India | Kenya | Malaysia | Oman | Tanzania | UAE </I></div>";

	public static final String TEMPLATE_APPROVER_CONTENT_UPDATE = "<div style=\"font-family:calibri;  font-size: 15px;\">Dear {recipientEmployeeName},</p>\r\n"
			+ "<p>A request has been modified ,Please find the modified travel plan application.Log on to <em><a href={websiteURL}>{websiteURL}</a></em> </i>and review the travel request.</div>\r\n"
			+ "<p style=\"font-family:calibri;  text-decoration: underline; font-size: 15px;\">\r\n"
			+ "<b>Request Details:</b> </p><table style=\"font-family:verdana; font-size: 11px;\" border=\"1\"  cellspacing=\"0\" cellpadding=\"4\">"
			+ "<tbody><tr  style=\"color:white;\" bgcolor=\"#e98700\">" + "<td>Request Id </td>"
			+ "<td>Requested By</td>" + "<td>Ticket Booking</td>" + "<td>Cab Booking</td>" + "<td>Accomodation</td>"
			+ "<td>Client Name</td>"
			// + "<td style=\"width: 133px;\">BD Name</td>"
			+ "<td>Business Unit</td>" + "<td>WON </td>" + "</tr>" + "<tr><td>{requestId}</td>" + "<td>{requestBy}</td>"
			+ "<td>{ticket}</td>" + "<td>{cab}</td>" + "<td>{accomodation}</td>" + "<td>{client}</td>"
			// + "<td style=\"width: 133px;\">{bdName}</td>"
			+ "<td>{buName}</td>" + "<td>{won}</td></tr>" + "</tbody></table>" + "{ticketRequest}" + "{cabRequestBase}"
			+ "{accomodationRequest}"
			+ "<p style=\"font-family:calibri; text-decoration: underline; font-size: 15px;\">\r\n"
			+ "<b>Comments:</b></p><div style=\"font-family:verdana;  font-size: 11px; padding-left: 30px;\"><i>{comment}</i></div><br><br>"
			+ "</font><div style=\"font-family:calibri; font-size: 15px;\"><b>--<br>Thanks & Regards, <br> Team TravelDesk, <br><br>Vinsys IT Services India Limited. </b><br>'Shivaji Niketan' | Tejas Society | Behind Dhondiba Sutar Bus Stand<br>Near Mantri Park | Kothrud | Pune - 411 029. Maharashtra, INDIA. <br><I>India - Pune | Hyderabad | Bangalore | NCR<br>International - Australia |  China | India | Kenya | Malaysia | Oman | Tanzania | UAE </I></div>";

	public static final String TEMPLATE_TO_TD_JOURNEY_COMPLETE_CAB_DETAILS = "<div style=\"font-family:calibri;  font-size: 15px;\">Dear Traveldesk,</p>\r\n"
			+ "<p>Please find cab details of completed journey. Please log on to <em><a href={websiteURL}>{websiteURL}</a></em> </i>and review the travel request.</div>\r\n"
			+ "<p style=\"font-family:calibri;  text-decoration: underline; font-size: 15px;\">\r\n"
			+ "<b>Request Details:</b> </p><table style=\"font-family:verdana; font-size: 11px;\" border=\"1\"  cellspacing=\"0\" cellpadding=\"4\">"
			+ "<tbody><tr  style=\"color:white;\" bgcolor=\"#e98700\">" + "<td>Request Id </td>"
			+ "<td>Requested By</td>" + "<td>Ticket Booking</td>" + "<td>Cab Booking</td>" + "<td>Accomodation</td>"
			+ "<td>Client Name</td>"
			// + "<td style=\"width: 133px;\">BD Name</td>"
			+ "<td>Business Unit</td>" + "<td>WON </td>" + "</tr>" + "<tr><td>{requestId}</td>" + "<td>{requestBy}</td>"
			+ "<td>{ticket}</td>" + "<td>{cab}</td>" + "<td>{accomodation}</td>" + "<td>{client}</td>"
			// + "<td style=\"width: 133px;\">{bdName}</td>"
			+ "<td>{buName}</td>" + "<td>{won}</td></tr>" + "</tbody></table>" + "{cabRequestTravelDetailsSB}"
			// + "{cabAssignDetailsSB}"
			+ "<br>"
			+ "</font><div style=\"font-family:calibri; font-size: 15px;\"><b> <br><br>Vinsys IT Services India Limited. </b><br>'Shivaji Niketan' | Tejas Society | Behind Dhondiba Sutar Bus Stand<br>Near Mantri Park | Kothrud | Pune - 411 029. Maharashtra, INDIA. <br><I>India - Pune | Hyderabad | Bangalore | NCR<br>International - Australia |  China | India | Kenya | Malaysia | Oman | Tanzania | UAE </I></div>";

	public static final String EMAIL_TEMPLATE_ATTENDANCE_DEFAULTERS = "<div style=\"font-family:calibri;  font-size: 15px; \">Hello {empName},<br><br>\r\n"
			+ "\r\n"
			+ "Please find below mentioned attendance for the month of {monthyear}, please check and get it approved.<br><br>\r\n"
			+ "</div><table style=\"font-family:verdana; font-size: 11px;\" border=\"1\"  cellspacing=\"0\" cellpadding=\"5\">\r\n"
			+ "<tbody>\r\n" + "<tr>\r\n" + "<th>Employee ACN</th>\r\n" + "<th>Employee Name</th>\r\n"
			+ "<th>Department</th>\r\n" + "<th>Designation</th>\r\n" + "<th>Date</th>\r\n" + "<th>Status</th>\r\n"
			+ "<th>Start Time</th>\r\n" + "<th>End Time</th>\r\n" + "<th>Man Hours</th>\r\n" + "</tr>\r\n"
			+ "{defaulterData}" + "</tbody>\r\n" + "</table>\r\n"
			+ "<br><div style=\"font-family:calibri;  font-size: 15px; \">If any concern please contact HR.</div>"
			+ "<br><br><div style=\"font-family:calibri; font-size: 15px;\"><b>Thanks & Regards, <br> Team HR, <br><br>Vinsys IT Services India Limited. </b><br>'Shivaji Niketan' | Tejas Society | Behind Dhondiba Sutar Bus Stand \r\n"
			+ "<br>Near Mantri Park | Kothrud | Pune - 411 029. Maharashtra, INDIA. \r\n"
			+ "<br><I>India - Pune | Hyderabad | Bangalore | NCR\r\n"
			+ "<br>International - Australia |  China | India | Kenya | Malaysia | Oman | Tanzania | UAE \r\n"
			+ "</I></div>";

	public static final String Template_Leave_Reminder = "<font face=\"verdana\" size=\"2\">\r\n"
			// " Dear {empFirstName} {empMiddleName} {empLastName}, Kindly take necessary
			// action on the leave application of your team member which is in pending
			// status "+
			// + "Hi {empFirstName} {empMiddleName} {empLastName},<br><br>This is to inform
			// you that some leaves are pending in your buket list.Plaese take action on
			// that. <br> Kindly log on to <i><a href={websiteURL}>{websiteURL}</a> </i>to
			// view more details.<br><br>\r\n"
			+ " Dear {empFirstName} {empMiddleName} {empLastName},<br><br>Kindly take necessary action on the leave application of your team member which is in pending status. <br> Kindly log on to <i><a href={websiteURL}>{websiteURL}</a> </i>to view more details.<br><br>\r\n"
			+ "Regards,<br><br>\r\n" + "HR Team \r\n" + "<br>\r\n" + "Vinsys\r\n" + "</font>";

	public static final String Template_Probation_ReminderNew = "<font face=\"verdana\" size=\"2\">"
			+ "Dear {roName},<br>Probation period of your team member is about to get over soon."
			+ " Please do the needful ahead based on his/her performance in the probation period. <br> "
			+ "<br> Kindly log on to <i><a href={websiteURL}>{websiteURL}</a> </i>to view more details.<br><br>"
			+ "{empDetail}<br><br>" + "Regards,<br><br>\r\n" + "HR Team \r\n" + "<br>\r\n" + "Vinsys\r\n" + "</font>";

	public static final String Template_Pending_Checklist_Separation_Reminder = "<font face=\"verdana\" size=\"2\">"
			+ "Dear {approverName},<br><br>"
			+ "Action on checklist items is pending for following employees."
			+ "<br> Kindly log on to <i><a href={websiteURL}>{websiteURL}</a> </i>to view more details (Approve Handover Checklist).<br><br>"
			+ "<table border=\"1\">"
			+ "<tr style=\"background-color: #ff9900; color: #ffffff;\"><th>Employee Id</th><th>Employee Name</th></tr>"
			+ "{pendingChecklistEmpList}"
			+ "</table>"
			+ "<br>" + "Regards,<br><br>\r\n" + "HR Team \r\n" + "<br>\r\n" + "Vinsys\r\n" + "</font>";
	
	public static final String Template_Employee_Creation_Lrypt ="<font face=\"verdana\" size=\"2\">\r\n"
			+ "	Dear {employeeFirstName} {employeeMiddleName} {employeeLastName},<br><br>Please find below official HRMS login id and password. The login portal for HRMS is https://hrms.lrypt.com/<br><br>\r\n"
			+ "	<table border=\"1\" cellspacing=0 cellpadding=2 style=\"margin:10px 5px 5px 100px;  font-family: calibri;\" >\r\n"
			+ "	<tr><td>Employee Name:</td>\r\n"
			+ "			<td>{employeeFirstName} {employeeMiddleName} {employeeLastName}</td>   </tr>\r\n"
			+ "			<tr><td>HRMS Login ID:</td><td>{employeeOfficialEmail}</td>\r\n"
			+ "</tr><tr><td>Password:</td><td>{employeePassword}</td>\r\n"
			+ "		</tr></table><br>In case of any concerns Please reach HR department.<br>\r\n"
			+ "<br><br><b>Thanks & Regards,</br>\r\n"
			+ " \r\n"
			+ "<br>LRYPT Technologies Pvt. Ltd.</b></br>\r\n"
			+ "<br>No. 23 l Symphony managed office space</br>\r\n"
			+ "<br>Hormavu agara road,Vadarpalya,</br>\r\n"
			+ "<br>Hennur, Bangalore -560043</br>\r\n"
			+ "	</font>";
}
