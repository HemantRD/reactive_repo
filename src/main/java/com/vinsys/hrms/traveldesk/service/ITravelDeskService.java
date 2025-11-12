package com.vinsys.hrms.traveldesk.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.entity.EmailTemplate;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.master.vo.TravelApproverResponseVO;
import com.vinsys.hrms.master.vo.TravelDeskApproverRequestVO;
import com.vinsys.hrms.traveldesk.entity.TravelRequestV2;
import com.vinsys.hrms.traveldesk.vo.DownloadExpenseSummaryReqVO;
import com.vinsys.hrms.traveldesk.vo.ExpenseSummeryRequest;
import com.vinsys.hrms.traveldesk.vo.ExpenseSummeryResponseVO;
import com.vinsys.hrms.traveldesk.vo.GetTravelRequestVO;
import com.vinsys.hrms.traveldesk.vo.SaveTravelRequestVO;
import com.vinsys.hrms.traveldesk.vo.SubmitTravelRequestVO;
import com.vinsys.hrms.traveldesk.vo.TravelDeskRequestVO;
import com.vinsys.hrms.traveldesk.vo.TravelDetailsCancelVO;
import com.vinsys.hrms.traveldesk.vo.TravelDetailsDeleteVO;
import com.vinsys.hrms.traveldesk.vo.TravelRequestApprovalVO;
import com.vinsys.hrms.traveldesk.vo.TravelRequestRejectionVO;
import com.vinsys.hrms.traveldesk.vo.TravelRequestStatusVO;
import com.vinsys.hrms.traveldesk.vo.TravelRequestVO;
import com.vinsys.hrms.traveldesk.vo.TravelResponseVO;

public interface ITravelDeskService {

	HRMSBaseResponse<TravelResponseVO> addTravelRequest(TravelRequestVO request)
			throws HRMSException, ParseException, ClassNotFoundException, SQLException, URISyntaxException;

	HRMSBaseResponse<List<TravelResponseVO>> getAllRequests(String roleName, Long departmentId, String travelerName,
			Pageable pageable) throws HRMSException;

	HRMSBaseResponse<GetTravelRequestVO> saveTravelRequest(SaveTravelRequestVO request)
			throws HRMSException, ParseException;

	HRMSBaseResponse<GetTravelRequestVO> getRequest(Long reqId) throws HRMSException;

	HRMSBaseResponse<GetTravelRequestVO> deleteRequest(TravelDetailsDeleteVO deleteVO) throws HRMSException;
//	void getAllRequests() throws HRMSException;

	HRMSBaseResponse<?> submitTravelRequest(SubmitTravelRequestVO request) throws HRMSException, ParseException;

	/****************** added by akanksha for save for TD ***********************/
	HRMSBaseResponse<GetTravelRequestVO> saveTravelRequestForTd(SaveTravelRequestVO request)
			throws HRMSException, ParseException;

	HRMSBaseResponse<?> cancelRequest(TravelDetailsCancelVO cancelVO) throws HRMSException;

	/******************
	 * added by Akanksha for submit request for TD
	 ***********************/
	HRMSBaseResponse<?> submitTravelRequestForTd(SubmitTravelRequestVO request) throws HRMSException, ParseException;

	HRMSBaseResponse<GetTravelRequestVO> approveTravelRequest(TravelRequestApprovalVO approvalVO) throws HRMSException;

	HRMSBaseResponse<GetTravelRequestVO> rejectTravelRequest(TravelRequestRejectionVO rejectionVO) throws HRMSException;

	TravelApproverResponseVO getApproverDetail(TravelDeskApproverRequestVO request) throws HRMSException;

	HRMSBaseResponse<GetTravelRequestVO> uploadDocumentForTravelDesk(TravelDeskRequestVO travelDeskRequestVO,
			MultipartFile file) throws HRMSException, IOException;

	ResponseEntity<Resource> viewDocument(Long travelDocumentId) throws HRMSException, FileNotFoundException;

	HRMSBaseResponse<ExpenseSummeryRequest> addExpenseSummary(ExpenseSummeryRequest expenseSummaryRequest)
			throws HRMSException;

	void downloadExpenseSummary(DownloadExpenseSummaryReqVO summeryReqVO, HttpServletResponse res)
			throws IOException, HRMSException;

	void mailForSubmitRequestByEmp(TravelRequestV2 travel, EmailTemplate template, String mailIds, String ccMailId);

	HRMSBaseResponse<ExpenseSummeryResponseVO> getTravelSummaryRequest(Long travelRequestId) throws HRMSException;

	HRMSBaseResponse<?> closeTravelRequest(Long travelRequestId) throws HRMSException;

	HRMSBaseResponse<TravelRequestStatusVO> getRequestStatus(Long travelRequestId) throws HRMSException;
}
