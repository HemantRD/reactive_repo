package com.vinsys.hrms.traveldesk.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.traveldesk.service.ITravelDeskService;
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
import com.vinsys.hrms.util.ResponseCode;

import io.swagger.v3.oas.annotations.Operation;

/**
 * @author Onkar A
 *
 * 
 */
@RestController
@RequestMapping("traveldesk")
public class TravelDeskController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	ITravelDeskService travelDeskService;

	@Value("${app_version}")
	private String applicationVersion;

	@Autowired
	private ObjectMapper objectMapper;

	@Operation(summary = "API for add travel request", description = "This API used to add travel request.")
	@PostMapping("emp/addtravelrequest")
	HRMSBaseResponse<TravelResponseVO> addTravelRequest(@RequestBody TravelRequestVO request) {
		HRMSBaseResponse<TravelResponseVO> response = new HRMSBaseResponse<>();
		try {
			response = travelDeskService.addTravelRequest(request);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "API for Get travel request ", description = "This API used to get All travel request.")
	@GetMapping("emp/getrequest")
	HRMSBaseResponse<List<TravelResponseVO>> getAllRequests(@RequestParam(required = true) String roleName,
			@RequestParam(required = false) Long departmentId,@RequestParam(required = false) String travelerName,Pageable pageable) {
		HRMSBaseResponse<List<TravelResponseVO>> response = new HRMSBaseResponse<>();
		try {
			response = travelDeskService.getAllRequests(roleName, departmentId,travelerName, pageable);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "API for Get travel request by request Id ", description = "This API used to get travel request by request Id.")
	@GetMapping("emp/getrequestbyid")
	HRMSBaseResponse<GetTravelRequestVO> getRequest(@RequestParam(required = true) Long reqId) {
		HRMSBaseResponse<GetTravelRequestVO> response = new HRMSBaseResponse<>();
		try {
			response = travelDeskService.getRequest(reqId);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "API for save travel request", description = "This API used to save travel request.")
	@PostMapping("emp/savetravelrequest")
	HRMSBaseResponse<GetTravelRequestVO> saveTravelRequest(@RequestBody SaveTravelRequestVO request) {
		HRMSBaseResponse<GetTravelRequestVO> response = new HRMSBaseResponse<>();
		try {
			response = travelDeskService.saveTravelRequest(request);
			HRMSBaseResponse<GetTravelRequestVO> travelRequestResponse = travelDeskService.getRequest(request.getId());
			response.setResponseBody(travelRequestResponse.getResponseBody());
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	/**
	 * API for delete travel request. This API is used to delete passenger travel
	 * request details.
	 * 
	 * @param deleteVO The request body containing details of the travel request to
	 *                 be deleted.
	 * @return HRMSBaseResponse containing the response for the delete operation.
	 * @author Rushikesh Thorat.
	 */

	@Operation(summary = "API for delete travel request ", description = "This API used to delete passenger travel request details.")
	@PostMapping("emp/deleterequest")
	HRMSBaseResponse<GetTravelRequestVO> deleteRequest(@RequestBody TravelDetailsDeleteVO deleteVO) {
		HRMSBaseResponse<GetTravelRequestVO> response = new HRMSBaseResponse<>();
		try {
			response = travelDeskService.deleteRequest(deleteVO);

			HRMSBaseResponse<GetTravelRequestVO> travelRequestResponse = travelDeskService
					.getRequest(deleteVO.getTravelRequestId());
			response.setResponseBody(travelRequestResponse.getResponseBody());
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	/***********
	 * Added by Akanksha for submit travel request
	 * 
	 ***************/
	@Operation(summary = "API for submit travel request", description = "This API used to submit travel request.")
	@PostMapping("emp/submittravelrequest")
	HRMSBaseResponse<?> submitTravelRequest(@RequestBody SubmitTravelRequestVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = travelDeskService.submitTravelRequest(request);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	/*********** added by Akanksha *****************/

	@Operation(summary = "API for save travel request by TD", description = "This API used to save travel request By TD.")
	@PostMapping("td/savetravelrequest")
	HRMSBaseResponse<GetTravelRequestVO> saveTravelRequestForTd(@RequestBody SaveTravelRequestVO request) {
		HRMSBaseResponse<GetTravelRequestVO> response = new HRMSBaseResponse<>();
		try {
			response = travelDeskService.saveTravelRequestForTd(request);
			HRMSBaseResponse<GetTravelRequestVO> travelRequestResponse = travelDeskService.getRequest(request.getId());
			response.setResponseBody(travelRequestResponse.getResponseBody());
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	/**
	 * API for canceling travel request by Travel Desk (TD). This API is used by
	 * Travel Desk to cancel travel request details.
	 * 
	 * @param cancelVO The request body containing details of the travel request to
	 *                 be canceled.
	 * @return HRMSBaseResponse containing the response for the cancel operation.
	 * @author Rushikesh Thorat.
	 */

	@Operation(summary = "API for canceling travel request by Travel Desk (TD) and self", description = "This API is used by Travel Desk to cancel travel request details.")
	@PostMapping("cancelrequest")
	HRMSBaseResponse<?> cancelRequest(@RequestBody TravelDetailsCancelVO cancelVO) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = travelDeskService.cancelRequest(cancelVO);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	/*********** added by Akanksha *****************/

	@Operation(summary = "API for submit travel request by TD", description = "This API used to submit travel request By TD.")
	@PostMapping("td/submittravelrequest")
	HRMSBaseResponse<?> submitTravelRequestForTd(@RequestBody SubmitTravelRequestVO request) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = travelDeskService.submitTravelRequestForTd(request);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	/**
	 * API for approving travel request by Approver. This API is used by Approver to
	 * approve travel request details.
	 * 
	 * @param approvalVO The request body containing travelRequestId and
	 *                   approverComment.
	 * @return HRMSBaseResponse containing the response for the approval operation.
	 * @author Rushikesh Thorat.
	 */
	@Operation(summary = "API for approving travel request by Approver", description = "This API is used by Approver to approve travel request details.")
	@PostMapping("approver/approvetravelrequest")
	HRMSBaseResponse<GetTravelRequestVO> approveTravelRequest(@RequestBody TravelRequestApprovalVO approvalVO) {
		HRMSBaseResponse<GetTravelRequestVO> response = new HRMSBaseResponse<>();
		try {
			// Call service method to approve the travel request
			response = travelDeskService.approveTravelRequest(approvalVO);
			HRMSBaseResponse<GetTravelRequestVO> travelRequestResponse = travelDeskService.getRequest(approvalVO.getTravelRequestId());
			response.setResponseBody(travelRequestResponse.getResponseBody());
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	/**
	 * API for rejecting travel request by Approver. This API is used by Approver to
	 * reject travel request details.
	 * 
	 * @param rejectionVO The request body containing travelRequestId and
	 *                    rejectComment.
	 * @return HRMSBaseResponse containing the response for the rejection operation.
	 * @author Rushikesh Thorat.
	 */
	@Operation(summary = "API for rejecting travel request by Approver", description = "This API is used by Approver to reject travel request details.")
	@PostMapping("approver/rejecttravelrequest")
	HRMSBaseResponse<GetTravelRequestVO> rejectTravelRequest(@RequestBody TravelRequestRejectionVO rejectionVO) {
		HRMSBaseResponse<GetTravelRequestVO> response = new HRMSBaseResponse<>();
		try {
			// Call service method to reject the travel request
			response = travelDeskService.rejectTravelRequest(rejectionVO);
			
			HRMSBaseResponse<GetTravelRequestVO> travelRequestResponse = travelDeskService.getRequest(rejectionVO.getTravelRequestId());
			response.setResponseBody(travelRequestResponse.getResponseBody());
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "API for upload travel desk document")
	@PostMapping("/uploaddocument")
	HRMSBaseResponse<GetTravelRequestVO> uploadDocumentForTravelDesk(
			@RequestPart("travelDeskRequestVO") TravelDeskRequestVO travelDeskRequestVO,
			@RequestParam("file") MultipartFile file) {
		HRMSBaseResponse<GetTravelRequestVO> response = new HRMSBaseResponse<>();
		try {
			response = travelDeskService.uploadDocumentForTravelDesk(travelDeskRequestVO, file);
			HRMSBaseResponse<GetTravelRequestVO> travelRequestResponse = travelDeskService.getRequest(travelDeskRequestVO.getTravelRequestId());
			response.setResponseBody(travelRequestResponse.getResponseBody());
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "API for view travel desk document")
	@GetMapping("/viewdocument")
	public ResponseEntity<Resource> viewDocument(@RequestParam(required = true) Long travelDocumentId)
			throws JsonProcessingException {
		try {
			return travelDeskService.viewDocument(travelDocumentId);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
			response.setResponseMessage(e.getResponseMessage());
			response.setResponseCode(e.getResponseCode());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
					.body(new ByteArrayResource(objectMapper.writeValueAsBytes(response)));
		} catch (Exception e) {
			log.error("Unexpected error occurred: " + e.getMessage(), e);
			HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
			response.setResponseMessage("Internal server error");
			response.setResponseCode(500);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
					.body(new ByteArrayResource(objectMapper.writeValueAsBytes(response)));
		}
	}

	/**
	 * API for adding expense summary. This API is used to add expense summary for a
	 * travel request.
	 *
	 * @param expenseSummaryRequest The request body containing expense summary
	 *                              details.
	 * @return HRMSBaseResponse containing the response for the expense summary
	 *         operation.
	 * @author Rushikesh Thorat.
	 */

	@Operation(summary = "API for adding expense summary", description = "This API is used to add expense summary for a travel request.")
	@PostMapping("/submitexpensesummary")
	HRMSBaseResponse<ExpenseSummeryRequest> addExpenseSummary(
			@RequestBody ExpenseSummeryRequest expenseSummaryRequest) {
		HRMSBaseResponse<ExpenseSummeryRequest> response = new HRMSBaseResponse<>();
		try {
			response = travelDeskService.addExpenseSummary(expenseSummaryRequest);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "API for download expense summary report in Excel", description = "")
	@PostMapping("/download/expensesummary")
	void downloadExpenseSummary(@RequestBody DownloadExpenseSummaryReqVO summeryReqVO,HttpServletResponse res) throws JsonProcessingException, IOException {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			 travelDeskService.downloadExpenseSummary(summeryReqVO,res);

		} catch (HRMSException e) {
			response = new HRMSBaseResponse<>();
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		ObjectMapper mapper = new ObjectMapper();
		res.getWriter().write(mapper.writeValueAsString(response));
	}

	/**
	 * API for retrieving expense summary. This API is used to retrieve expense
	 * summary for a travel request.
	 *
	 * @param travelRequestId The ID of the travel request for which expense summary
	 *                        is to be retrieved.
	 * @return HRMSBaseResponse containing the response for the expense summary
	 *         retrieval operation.
	 * @author Rushikesh Thorat
	 */

	@Operation(summary = "API for retrieving expense summary", description = "This API is used to retrieve expense summary for a travel request.")
	@GetMapping("/getexpensesummary")
	HRMSBaseResponse<ExpenseSummeryResponseVO> getTravelSummaryRequest(@RequestParam Long travelRequestId) {
		HRMSBaseResponse<ExpenseSummeryResponseVO> response = new HRMSBaseResponse<>();
		try {
			response = travelDeskService.getTravelSummaryRequest(travelRequestId);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "API for close final request", description = "")
	@PostMapping("closerequest")
	HRMSBaseResponse<?> closeTravelRequest(@RequestParam Long travelRequestId) {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			response = travelDeskService.closeTravelRequest(travelRequestId);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}
	
	@Operation(summary = "This API will use for to get travel request status", description = "")
	@GetMapping("requeststatus")
	HRMSBaseResponse<TravelRequestStatusVO> getRequestStatus(@RequestParam(required = true) Long travelRequestId) {
		HRMSBaseResponse<TravelRequestStatusVO> response = new HRMSBaseResponse<>();
		try {
			response = travelDeskService.getRequestStatus(travelRequestId);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}
}
