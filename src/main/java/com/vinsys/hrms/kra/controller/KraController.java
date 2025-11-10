
package com.vinsys.hrms.kra.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.kra.entity.Kra;
import com.vinsys.hrms.kra.service.IKpiService;
import com.vinsys.hrms.kra.service.IKraService;
import com.vinsys.hrms.kra.vo.AnalyticalDepartmentReportRequestVO;
import com.vinsys.hrms.kra.vo.AnalyticalDepartmentReportVO;
import com.vinsys.hrms.kra.vo.AutoSubmitResponseVo;
import com.vinsys.hrms.kra.vo.CalibrationRequestVo;
import com.vinsys.hrms.kra.vo.CategoryRequestVo;
import com.vinsys.hrms.kra.vo.CycleDefinationRequestVo;
import com.vinsys.hrms.kra.vo.CycleDefinationResponseVo;
import com.vinsys.hrms.kra.vo.CycleResponseVo;
import com.vinsys.hrms.kra.vo.CycleWiseResponseVO;
import com.vinsys.hrms.kra.vo.DashboardCountResponse;
import com.vinsys.hrms.kra.vo.DashboardRatingResponseVO;
import com.vinsys.hrms.kra.vo.DelegationMappingResponseVO;
import com.vinsys.hrms.kra.vo.DelegationRequestVo;
import com.vinsys.hrms.kra.vo.DeleteDelegationMappingVO;
import com.vinsys.hrms.kra.vo.DeleteKraRequestVO;
import com.vinsys.hrms.kra.vo.DepartmentEmpListVo;
import com.vinsys.hrms.kra.vo.DeptwiseCountResponseVO;
import com.vinsys.hrms.kra.vo.DonutCountResponse;
import com.vinsys.hrms.kra.vo.EmployeeReportVo;
import com.vinsys.hrms.kra.vo.GenerateKPITemplateRequestVo;
import com.vinsys.hrms.kra.vo.HcdCorrcetionRequest;
import com.vinsys.hrms.kra.vo.HcdCorrectionResponseVo;
import com.vinsys.hrms.kra.vo.HodCycleFinalRatingResponseVO;
import com.vinsys.hrms.kra.vo.HodToDepartmentMapListResponseVO;
import com.vinsys.hrms.kra.vo.HodToDepartmentMapVO;
import com.vinsys.hrms.kra.vo.HrToDepartmentResponseVO;
import com.vinsys.hrms.kra.vo.HrToDepartmentVO;
import com.vinsys.hrms.kra.vo.KpiFormFinalResponseVo;
import com.vinsys.hrms.kra.vo.KpiFormStatusRequestVo;
import com.vinsys.hrms.kra.vo.KpiResponseVo;
import com.vinsys.hrms.kra.vo.KraCycleListResponseVO;
import com.vinsys.hrms.kra.vo.KraCycleRequestVo;
import com.vinsys.hrms.kra.vo.KraCycleTypeVO;
import com.vinsys.hrms.kra.vo.KraDetailsRequestVO;
import com.vinsys.hrms.kra.vo.KraDetailsResponseVO;
import com.vinsys.hrms.kra.vo.KraListRequestVo;
import com.vinsys.hrms.kra.vo.KraResponseVO;
import com.vinsys.hrms.kra.vo.KraYearVo;
import com.vinsys.hrms.kra.vo.MapAllGradeToStagesResponseVO;
import com.vinsys.hrms.kra.vo.MapGradeToStagesRequestVO;
import com.vinsys.hrms.kra.vo.MapGradeToStagesResponseVO;
import com.vinsys.hrms.kra.vo.NewKraResponse;
import com.vinsys.hrms.kra.vo.NotesRequestVO;
import com.vinsys.hrms.kra.vo.NotesResponseVO;
import com.vinsys.hrms.kra.vo.NotificationVo;
import com.vinsys.hrms.kra.vo.OrgKpiDeleteRequestVo;
import com.vinsys.hrms.kra.vo.OrgKpiDeleteVo;
import com.vinsys.hrms.kra.vo.OrgKpiReportResponse;
import com.vinsys.hrms.kra.vo.OrgKpiRequest;
import com.vinsys.hrms.kra.vo.OrgKpiResponseVo;
import com.vinsys.hrms.kra.vo.OrgnizationalKpiVo;
import com.vinsys.hrms.kra.vo.PMSObjectiveVo;
import com.vinsys.hrms.kra.vo.PMSObjectivesRequestVo;
import com.vinsys.hrms.kra.vo.PmsDashboardRequestVo;
import com.vinsys.hrms.kra.vo.PmsKraVo;
import com.vinsys.hrms.kra.vo.PublishResponse;
import com.vinsys.hrms.kra.vo.RatingPercentageResponseVO;
import com.vinsys.hrms.kra.vo.RejectRequestVo;
import com.vinsys.hrms.kra.vo.SubCategoryRequestVo;
import com.vinsys.hrms.kra.vo.SubCategoryVo;
import com.vinsys.hrms.kra.vo.TargetRequestVo;
import com.vinsys.hrms.kra.vo.TimeLineRequestVO;
import com.vinsys.hrms.kra.vo.TimeLineResponseVO;
import com.vinsys.hrms.kra.vo.TimeLineVo;
import com.vinsys.hrms.master.vo.CategoryResponseVo;
import com.vinsys.hrms.master.vo.SelfRatingVo;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.ResponseCode;

import io.swagger.v3.oas.annotations.Operation;

/**
 * @author Onkar A.
 *
 */
@RestController
@RequestMapping("/kra")
public class KraController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	IKraService kraService;
	@Autowired
	IKpiService kpiService;
	@Value("${app_version}")
	private String applicationVersion;

	@Operation(summary = "Save KRA by employee", description = "")
	@PostMapping("save")
	HRMSBaseResponse<KraDetailsResponseVO> saveKra(@RequestBody KraDetailsRequestVO request) {
		HRMSBaseResponse<KraDetailsResponseVO> response = new HRMSBaseResponse<>();
		try {
			response = kraService.saveKra(request);
			Kra kra = kraService.getCurrentYearKra();
			HRMSBaseResponse<KraDetailsResponseVO> kraDetailsResponse = kraService
					.getKraById(String.valueOf(kra.getId()));
			response.setResponseBody(kraDetailsResponse.getResponseBody());
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "All KRA list by year", description = "Employee can see  self KRA list and manager will see assiged employee lit")
	@GetMapping("kralist")
	HRMSBaseResponse<List<KraResponseVO>> getKraList(@RequestParam(required = true) String roleName,
			@RequestParam(required = false) Long deptid, @RequestParam(required = false) Long empid,
			@RequestParam(required = false) String year, @RequestParam(required = false) Long countryId,
			@RequestParam(required = false) String status, @RequestParam(required = false) Long cycleId,
			@RequestParam(required = false) Long gradeId, Pageable pageable) {
		HRMSBaseResponse<List<KraResponseVO>> response = new HRMSBaseResponse<>();
		try {
			response = kraService.getKraList(roleName, deptid, empid, year, countryId, status, cycleId, gradeId,
					pageable);
			response.setApplicationVersion(applicationVersion);
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

	/*
	 * @Operation(summary = "get KRA details by ID", description =
	 * "Emloyee and manager can view kra details by kra Id")
	 * 
	 * @GetMapping("krabyid") HRMSBaseResponse<KraDetailsResponseVO>
	 * getKraById(@RequestParam(required = true) String kraid) {
	 * HRMSBaseResponse<KraDetailsResponseVO> response = new HRMSBaseResponse<>();
	 * try { response = kraService.getKraById(kraid);
	 * response.setApplicationVersion(applicationVersion); } catch (HRMSException e)
	 * { log.error(e.getMessage(), e);
	 * 
	 * response.setResponseCode(e.getResponseCode());
	 * response.setResponseMessage(e.getResponseMessage());
	 * response.setApplicationVersion(applicationVersion); } catch (Exception e) {
	 * log.error(e.getMessage(), e);
	 * 
	 * response.setResponseCode(1500);
	 * response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500)); }
	 * return response; }
	 */

	@Operation(summary = "Submit KRA by employee", description = "")
	@PostMapping("submit")
	HRMSBaseResponse<KraDetailsResponseVO> submitKra(@RequestBody KraDetailsRequestVO request) {
		HRMSBaseResponse<KraDetailsResponseVO> response = new HRMSBaseResponse<>();
		try {
			response = kraService.submitKra(request);
			Kra kra = kraService.getCurrentYearKra();
			HRMSBaseResponse<KraDetailsResponseVO> kraDetailsResponse = kraService
					.getKraById(String.valueOf(kra.getId()));
			response.setResponseBody(kraDetailsResponse.getResponseBody());
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "Delete KRA details single point", description = "")
	@PostMapping("deletekra")
	HRMSBaseResponse<KraDetailsResponseVO> deleteKraPoint(@RequestBody DeleteKraRequestVO request) {
		HRMSBaseResponse<KraDetailsResponseVO> response = new HRMSBaseResponse<>();
		try {
			response = kraService.deleteKraPoint(request);
			Kra kra = kraService.getCurrentYearKra();
			HRMSBaseResponse<KraDetailsResponseVO> kraDetailsResponse = kraService
					.getKraById(String.valueOf(kra.getId()));
			response.setResponseBody(kraDetailsResponse.getResponseBody());
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "KRA approved by RM", description = "")
	@PostMapping("approve")
	HRMSBaseResponse<KraDetailsResponseVO> approveKra(@RequestBody KraDetailsRequestVO request) {
		HRMSBaseResponse<KraDetailsResponseVO> response = new HRMSBaseResponse<>();
		try {
			response = kraService.approveKra(request);
			HRMSBaseResponse<KraDetailsResponseVO> kraDetailsResponse = kraService
					.getKraById(String.valueOf(request.getKraId()));
			response.setResponseBody(kraDetailsResponse.getResponseBody());
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "KRA Rejected by RM", description = "")
	@PostMapping("reject")
	HRMSBaseResponse<KraDetailsResponseVO> rejectKra(@RequestBody KraDetailsRequestVO request) {
		HRMSBaseResponse<KraDetailsResponseVO> response = new HRMSBaseResponse<>();
		try {
			response = kraService.rejectKra(request);
			HRMSBaseResponse<KraDetailsResponseVO> kraDetailsResponse = kraService
					.getKraById(String.valueOf(request.getKraId()));
			response.setResponseBody(kraDetailsResponse.getResponseBody());
			response.setApplicationVersion(applicationVersion);
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
	@GetMapping("/download/krasummary")
	void downloadKRASummary(@RequestParam(required = false) Long deptid, @RequestParam(required = false) Long empid,
			@RequestParam(required = false) String year, @RequestParam(required = false) Long countryId,
			@RequestParam(required = false) String status, HttpServletResponse res)
			throws JsonProcessingException, IOException {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			kraService.downloadKraSummary(deptid, empid, year, countryId, status, res);

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

	@Operation(summary = "API for download individual kra report to RM", description = "")
	@PostMapping("/download/krareport")
	void downloadKRASummary(HttpServletResponse res, @RequestParam Long empid, @RequestParam Long kraid)
			throws JsonProcessingException, IOException {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			kraService.downloadKraReport(res, empid, kraid);
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

	@Operation(summary = "get KRA details by ID", description = "Emloyee and manager can view kra details by kra Id")
	@GetMapping("getkrabyid")
	HRMSBaseResponse<NewKraResponse> getNewKraById(@RequestParam(required = true) Long kraid) {
		HRMSBaseResponse<NewKraResponse> response = new HRMSBaseResponse<>();
		try {
			response = kraService.getNewKraById(kraid);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "Submit KRA by employee", description = "")
	@PostMapping("submitkra")
	HRMSBaseResponse<String> submitPmsKra(@RequestBody PmsKraVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.submitPmsKra(request);
			response.setResponseBody(IHRMSConstants.successMessage);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "AutoSubmit KRA for employee self rating submit", description = "")
	@PostMapping("autosubmitkra")
	HRMSBaseResponse<AutoSubmitResponseVo> autoSubmitPmsKra(@RequestBody PmsKraVo request) {
		HRMSBaseResponse<AutoSubmitResponseVo> response = new HRMSBaseResponse<>();
		try {
			response = kraService.autoSubmitPmsKra(request);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "Save KRA by employee", description = "")
	@PostMapping("savekra")
	HRMSBaseResponse<String> savePmsKra(@RequestBody PmsKraVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.savePmsKra(request);
			response.setResponseBody(IHRMSConstants.successMessage);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "Save Cycle", description = "")
	@PostMapping("savekracycle")
	HRMSBaseResponse<String> saveKraCycle(@RequestBody KraCycleRequestVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.saveKraCycle(request);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "Submit rating by manager", description = "")
	@PostMapping("submitmanagerrating")
	HRMSBaseResponse<String> submitManagerRating(@RequestBody PmsKraVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.submitManagerRating(request);
			response.setResponseBody(IHRMSConstants.successMessage);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "Auto Submit rating by manager", description = "")
	@PostMapping("autosubmitmanagerrating")
	HRMSBaseResponse<AutoSubmitResponseVo> autoSubmitManagerRating(@RequestBody PmsKraVo request) {
		HRMSBaseResponse<AutoSubmitResponseVo> response = new HRMSBaseResponse<>();
		try {
			response = kraService.autoSubmitManagerRating(request);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "KRA Rejected by RM", description = "")
	@PostMapping("newrejectkra")
	HRMSBaseResponse<String> rejectNewKra(@RequestBody RejectRequestVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.rejectNewKra(request);

			response.setResponseBody(IHRMSConstants.successMessage);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "callibration submit by HOD", description = "")
	@PostMapping("submitcalibration")
	HRMSBaseResponse<String> submitCalibration(@RequestBody CalibrationRequestVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.saveCalibration(request);

			response.setResponseBody(IHRMSConstants.successMessage);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "get KPI Statistics Dashboard ", description = "On Dashboard HR/Manager/HOD/Employee can see Submitted and Pending KPI Count with Status")
	@PostMapping("getpmsdashboard")
	HRMSBaseResponse<DashboardCountResponse> getPmsDashboard(@RequestBody PmsDashboardRequestVo request)
			throws HRMSException {
		HRMSBaseResponse<DashboardCountResponse> response = new HRMSBaseResponse<>();
		try {
			response = kraService.getPmsDashboard(request);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "Save rating by manager", description = "")
	@PostMapping("savemanagerrating")
	HRMSBaseResponse<String> saveManagerRating(@RequestBody PmsKraVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.saveManagerRating(request);
			response.setResponseBody(IHRMSConstants.successMessage);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "save published data", description = "")
	@PostMapping("publish")
	HRMSBaseResponse<String> savePublished() {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.savePublished();
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "Accept KPI by MC Member", description = "")
	@PostMapping("acceptkpi")
	HRMSBaseResponse<String> acceptKpi(@RequestBody RejectRequestVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.acceptKpi(request);

			response.setResponseBody(IHRMSConstants.successMessage);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "Save Publised", description = "")
	@PostMapping("hrcalibrate")
	HRMSBaseResponse<String> saveHrCalibrate() {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.saveHrCalibrate();
			response.setResponseBody(IHRMSConstants.successMessage);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "get pending List", description = "")
	@GetMapping("getpendinglist")
	HRMSBaseResponse<PublishResponse> getPendingList() {
		HRMSBaseResponse<PublishResponse> response = new HRMSBaseResponse<>();
		try {
			response = kraService.getPublished();
			response.setApplicationVersion(applicationVersion);
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

	// **************Download report for HOD****************

	@Operation(summary = "API for download expense summary report in Excel for HOD", description = "")
	@PostMapping("/download/hod/krasummary")
	void downloadHodKRASummary(@RequestParam(required = false) Long deptid, @RequestParam(required = false) Long empid,
			@RequestParam(required = false) String year, @RequestParam(required = false) Long countryId,
			HttpServletResponse res) throws JsonProcessingException, IOException {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			kraService.downloadHodKraSummary(deptid, empid, year, countryId, res);

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

	@Operation(summary = "get KPI Dashboard Rating", description = "On Dashboard Rating")
	@GetMapping("getdashboardrating")
	HRMSBaseResponse<DashboardRatingResponseVO> getDashboardRating() throws HRMSException {
		HRMSBaseResponse<DashboardRatingResponseVO> response = new HRMSBaseResponse<>();
		try {
			response = kraService.getDashboardRating();
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "get dynamic timeline for dashboard", description = "API to get dynamic timeline for KPI dashboard")
	@GetMapping("getkpitimeline")
	HRMSBaseResponse<TimeLineVo> getKpiTimeline() throws HRMSException {
		HRMSBaseResponse<TimeLineVo> response = new HRMSBaseResponse<>();
		try {
			response = kraService.getKpiTimeline();
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "Save Target", description = "")
	@PostMapping("target")
	HRMSBaseResponse<String> saveTarget(@RequestBody TargetRequestVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.saveTarget(request);
			response.setResponseBody(IHRMSConstants.successMessage);
			response.setApplicationVersion(applicationVersion);
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

	@GetMapping("/selfrating")
	HRMSBaseResponse<List<SelfRatingVo>> getSelfRating(@RequestParam(required = true) Long categoryId,
			@RequestParam(required = true) Long kraId, @RequestParam(required = true) Long objId) {
		HRMSBaseResponse<List<SelfRatingVo>> response = new HRMSBaseResponse<>();
		try {
			response = kraService.getSelfRating(categoryId, kraId, objId);
			response.setApplicationVersion(applicationVersion);

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

	@Operation(summary = "get Percentage KPI Dashboard Rating", description = "On Dashboard Rating")
	@GetMapping("getratingpercentage")
	HRMSBaseResponse<RatingPercentageResponseVO> getRatingPercentage() throws HRMSException {
		HRMSBaseResponse<RatingPercentageResponseVO> response = new HRMSBaseResponse<>();
		try {
			response = kraService.getRatingPercentage();
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "API for download dashboard Rating report in Excel", description = "")
	@GetMapping("/download/dashboardrating")
	void downloadKraDashboardRatingReport(HttpServletResponse res) throws JsonProcessingException, IOException {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			kraService.downloadKraDashboardRatingReport(res);

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

	@Operation(summary = "API for download dashboard percentage Rating report in Excel", description = "")
	@GetMapping("/download/percentagerating")
	void downloadKraPercentageRatingReport(HttpServletResponse res) throws JsonProcessingException, IOException {
		HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		try {
			kraService.downloadKraPercentageRatingReport(res);

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

	@Operation(summary = "callibration save by HOD", description = "")
	@PostMapping("savecalibration")
	HRMSBaseResponse<String> saveCalibration(@RequestBody CalibrationRequestVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.newSaveCalibration(request);

			response.setResponseBody(IHRMSConstants.successMessage);
			response.setApplicationVersion(applicationVersion);
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

	// Added api for save target pecentage
	@Operation(summary = "Save Target percentage", description = "")
	@PostMapping("targetpercentage")
	HRMSBaseResponse<String> saveTargetPercentage(@RequestBody TargetRequestVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.saveTargetPercentage(request);
			response.setResponseBody(IHRMSConstants.successMessage);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "get cycle defination List", description = "")
	@GetMapping("getcycledefinationlist")
	HRMSBaseResponse<CycleDefinationResponseVo> getCycleDefinationList(@RequestParam(required = true) Long yearId,
			@RequestParam(required = true) Long cycleId) {
		HRMSBaseResponse<CycleDefinationResponseVo> response = new HRMSBaseResponse<>();
		try {
			response = kraService.getCycleDefinationList(yearId, cycleId);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "Define KPI Year", description = "This api is use to define KPI year in HR login")
	@PostMapping("kpiyear")
	HRMSBaseResponse<String> saveKpiYear(@RequestBody KraYearVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.saveKpiYear(request);
			response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "save KPI category", description = "This api is use to save KPI category")
	@PostMapping("savecategory")
	HRMSBaseResponse<String> saveCategory(@RequestBody CategoryRequestVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.saveCategory(request);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	/** Added by Akanksha **/

	@Operation(summary = "Update KPI Cycle", description = "This API is used to update KPI cycle")
	@PostMapping("updatekracycle")
	HRMSBaseResponse<String> updateKraCycle(@RequestBody KraCycleRequestVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.updateKraCycle(request);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "save cycle defination", description = "This api is use to save cycle defination")
	@PostMapping("savecycledefination")
	HRMSBaseResponse<String> saveCycleDefination(@RequestBody CycleDefinationRequestVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.saveCycleDefination(request);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "save KPI SubCategory", description = "This api is use to save KPI SubCategory")
	@PostMapping("savesubcategory")
	HRMSBaseResponse<String> saveSubCategory(@RequestBody SubCategoryRequestVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.saveSubCategory(request);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	/** Added by Akanksha **/

	@Operation(summary = "Define Orgnizational KPI", description = "This API is used to add orgnizational KPIs for all employees")
	@PostMapping("orgkpi")
	HRMSBaseResponse<String> addOrgKpi(@RequestBody OrgnizationalKpiVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.addOrgKpi(request);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "Generate KPI Template", description = "This API is used to Generate KPIs for all employees for all cycle")
	@PostMapping("generatekpitemplate")
	HRMSBaseResponse<String> generateTemplate(@RequestBody GenerateKPITemplateRequestVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.generateKPITemplates(request);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "save Objectives", description = "This api is use to save Objectives")
	@PostMapping("saveobejectives")
	HRMSBaseResponse<String> saveObejectives(@RequestBody PMSObjectivesRequestVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.saveObejectives(request);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "get cycle List", description = "")
	@GetMapping("getcyclelist")
	HRMSBaseResponse<List<CycleResponseVo>> getCycleList(@RequestParam(required = true) Long yearId) {
		HRMSBaseResponse<List<CycleResponseVo>> response = new HRMSBaseResponse<>();
		try {
			response = kraService.getCycleList(yearId);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "get objectives", description = "This api is use to get Line manager objectives by id")
	@GetMapping("getobjectives")
	HRMSBaseResponse<PMSObjectivesRequestVo> getObjectives(@RequestParam(required = true) Long id) {
		HRMSBaseResponse<PMSObjectivesRequestVo> response = new HRMSBaseResponse<>();
		try {
			response = kraService.getObjectives(id);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "get org kpi List", description = "This api is use to get organizational kpi list with filter")
	@GetMapping("getorgkpilist")
	HRMSBaseResponse<List<OrgKpiResponseVo>> getOrgKpiList(@RequestParam(required = false) Long deptid,
			@RequestParam(required = false) Long grade, @RequestParam(required = false) Long yearId,
			@RequestParam(required = false) Long kpiTypeId, Pageable pageable) {
		HRMSBaseResponse<List<OrgKpiResponseVo>> response = new HRMSBaseResponse<>();
		try {
			response = kraService.getOrgKpiList(deptid, grade, yearId, kpiTypeId, pageable);
			response.setApplicationVersion(applicationVersion);
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

	/** Added by Akanksha **/

	@Operation(summary = "Edit/Update Orgnizational KPI", description = "This API is used to update existing orgnizational KPIs for all employees")
	@PostMapping("updateorgkpi")
	HRMSBaseResponse<String> updateOrgKpi(@RequestBody OrgnizationalKpiVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.updateOrgKpi(request);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "update Objectives", description = "This api is use to update Objectives")
	@PostMapping("updateobejectives")
	HRMSBaseResponse<String> updateObejectives(@RequestBody PMSObjectiveVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.updateObejectives(request);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "Accept KPI by HCD", description = "")
	@PostMapping("acceptkpibyhcd")
	HRMSBaseResponse<String> acceptKpibyHcd(@RequestBody RejectRequestVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.acceptKpibyHcd(request);

			response.setResponseBody(IHRMSConstants.successMessage);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "Send mail on kra submission", description = "Sends an email notification to HR and Line Manager when a KRA is submitted")
	@PostMapping("/sendmailonsubmitkra")
	HRMSBaseResponse<String> sendMailOnSubmitKra(@RequestParam(required = true) Long kraid,
			@RequestParam(required = true) String isObjectiveSubmit) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.sendMailOnSubmitKra(kraid, isObjectiveSubmit);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "KPA Rejected by HCD", description = "")
	@PostMapping("rejectkpibyhcd")
	HRMSBaseResponse<String> rejectKpibyHcd(@RequestBody RejectRequestVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.rejectKpibyHcd(request);

			response.setResponseBody(IHRMSConstants.successMessage);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "Accept KPI by Line Manager", description = "")
	@PostMapping("acceptkpibymanager")
	HRMSBaseResponse<String> acceptKpiByLineManager(@RequestBody RejectRequestVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.acceptKpiByLineManager(request);

			response.setResponseBody(IHRMSConstants.successMessage);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "Accept KPI by Team Member", description = "A team member uses this API to accept the KPIs set by their line manager.")
	@PostMapping("teammember/acceptkpi")
	HRMSBaseResponse<String> acceptKpiByTeamMember(@RequestBody RejectRequestVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.acceptKpiByTeamMember(request);

			response.setResponseBody(IHRMSConstants.successMessage);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "Delete orgnizational KPI", description = "This API is use to delete orgnizational KPIs")
	@PostMapping("delete/orgkpi")
	HRMSBaseResponse<String> deleteOrgKpi(@RequestBody OrgKpiDeleteRequestVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.deleteOrgKpi(request);
			response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "filter kra list", description = " ")
	@PostMapping("/krareport")
	HRMSBaseResponse<List<KraResponseVO>> getKraListByFilter(@RequestBody KraListRequestVo request, Pageable pageable) {
		HRMSBaseResponse<List<KraResponseVO>> response = new HRMSBaseResponse<>();
		try {
			response = kraService.getKraListReport(request, pageable);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "locked cycle", description = " ")
	@GetMapping("/lockedcycle")
	public HRMSBaseResponse<String> lockedcycle(@RequestParam Long cycleId) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.lockedcycle(cycleId);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "Delete linemanager KPI", description = "This API is use to delete linemanager KPIs")
	@PostMapping("delete/objective")
	public HRMSBaseResponse<String> deleteLineManagerKpi(@RequestBody OrgKpiDeleteVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.deleteLineManagerKpi(request);
			response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "get org kpi List with filter and serach", description = "This api is use to get organizational kpi list with filter")
	@PostMapping("getorgkpilistreport")
	public HRMSBaseResponse<List<OrgKpiReportResponse>> getOrgKpiListReport(@RequestBody OrgKpiRequest request,
			Pageable pageable) {
		HRMSBaseResponse<List<OrgKpiReportResponse>> response = new HRMSBaseResponse<>();
		try {
			response = kraService.getOrgKpiListReport(request, pageable);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "get hcd correction", description = "")
	@GetMapping("gethcdcorrection")
	public HRMSBaseResponse<List<HcdCorrectionResponseVo>> getHcdCorrection(@RequestParam(required = true) Long kraId) {
		HRMSBaseResponse<List<HcdCorrectionResponseVo>> response = new HRMSBaseResponse<>();
		try {
			response = kraService.getHcdCorrection(kraId);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "Download Org KPI Report", description = "This API is used to download the organizational KPI report in Excel format")
	@PostMapping("downloadorgkpireport")
	public ResponseEntity<byte[]> downloadOrgKpiReport(@RequestBody OrgKpiRequest request) throws HRMSException {
		try {
			byte[] response = kraService.downloadOrgKpiReport(request);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDisposition(
					ContentDisposition.builder("attachment").filename("Manage_Org_KPI's_Report.xlsx").build());

			return ResponseEntity.ok().headers(headers).body(response);

		} catch (HRMSException e) {
			log.error("HRMSException: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(("Error: " + e.getResponseMessage()).getBytes());
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ResponseCode.getResponseCodeMap().get(1500).getBytes());
		}
	}

	@Operation(summary = "Download KRA Report", description = "This API is used to download the Kra report in Excel format")
	@PostMapping("downloadkrareport")
	public ResponseEntity<byte[]> downloadKraReports(@RequestBody KraListRequestVo request) throws HRMSException {
		try {
			byte[] response = kraService.downloadKraReport(request);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDisposition(
					ContentDisposition.builder("attachment").filename("KPI_List_Report.xlsx").build());

			return ResponseEntity.ok().headers(headers).body(response);

		} catch (HRMSException e) {
			log.error("HRMSException: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(("Error: " + e.getResponseMessage()).getBytes());
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ResponseCode.getResponseCodeMap().get(1500).getBytes());
		}
	}

	@Operation(summary = "save hcd correction", description = "")
	@PostMapping("savethcdcorrection")
	public HRMSBaseResponse<String> saveHcdCorrection(@RequestBody HcdCorrcetionRequest request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.saveHcdCorrection(request);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "submit hcd correction", description = "")
	@PostMapping("submithcdcorrection")
	public HRMSBaseResponse<String> submitHcdCorrection(@RequestParam(required = true) Long kraId) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.submitHcdCorrection(kraId);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "Save KPI Timeline", description = "Saves a new KPI timeline entry")
	@PostMapping("savekpitimeline")
	public HRMSBaseResponse<String> saveKpiTimeLine(@RequestBody TimeLineRequestVO request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.saveKpiTimeLine(request);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "get dynamic timeline by id", description = "This API used to get dynamic timeline for KPI by id")
	@GetMapping("kpitimeline")
	HRMSBaseResponse<TimeLineRequestVO> getKpiTimelineById(@RequestParam(required = true) Long id)
			throws HRMSException {
		HRMSBaseResponse<TimeLineRequestVO> response = new HRMSBaseResponse<>();
		try {
			response = kraService.getKpiTimelineById(id);
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "Delete KPI timeline", description = "This API is use to delete KPI timeline by id")
	@PostMapping("delete/kpitimeline")
	HRMSBaseResponse<String> deleteKpiTimeline(@RequestBody TimeLineRequestVO request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.deleteKpiTimeline(request);
			response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "get time line list", description = "API to get time line list")
	@GetMapping("getkpitimelinelist")
	public HRMSBaseResponse<TimeLineResponseVO> getKpiTimelineList(
			@RequestParam(name = "yearId", required = false) Long yearId,
			@RequestParam(name = "kraCycleId", required = false) Long kraCycleId, Pageable pageable)
			throws HRMSException {
		HRMSBaseResponse<TimeLineResponseVO> response = new HRMSBaseResponse<>();
		try {
			response = kraService.getKpiTimelineList(yearId, kraCycleId, pageable);
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	/** API to get all employees department wise added by Akanksha ***/

	@Operation(summary = "get department wise employee list", description = "This API used to get department wise employee list")
	@GetMapping("emplist")
	HRMSBaseResponse<DepartmentEmpListVo> getDeptpartmentEmpList() throws HRMSException {
		HRMSBaseResponse<DepartmentEmpListVo> response = new HRMSBaseResponse<>();
		try {
			response = kraService.getDeptpartmentEmpList();
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

//	@Operation(summary = "Get Delegation Mapping List", description = "This API fetches the delegation mappings list")
//    @GetMapping("/delegationlist")
//    public HRMSBaseResponse<DelegationMappingResponseVO> getDelegationMappingList(Pageable pageable) {
//        
//        HRMSBaseResponse<DelegationMappingResponseVO> response = new HRMSBaseResponse<>();
//        try {
//            response = kraService.getDelegationMappingList(pageable);
//            response.setApplicationVersion(applicationVersion);
//        } catch (Exception e) {
//            log.error("Error in fetching delegation mapping list", e);
//            response.setResponseCode(1500);
//            response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
//        }
//        return response;
//    }

	@GetMapping("/delegationlist")
	public HRMSBaseResponse<DelegationMappingResponseVO> getDelegationMappingList(
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String searchText) {

		HRMSBaseResponse<DelegationMappingResponseVO> response = new HRMSBaseResponse<>();
		try {
			Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")); // descending sort
			response = kraService.getDelegationMappingList(pageable, searchText);
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error("Error in fetching delegation mapping list", e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}

		return response;
	}

	@Operation(summary = "delegate kra list", description = " ")
	@PostMapping("/delegatereport")
	public HRMSBaseResponse<List<KraResponseVO>> getDelegateList(@RequestBody KraListRequestVo request,
			Pageable pageable) {
		HRMSBaseResponse<List<KraResponseVO>> response = new HRMSBaseResponse<>();
		try {
			response = kraService.getDelegateList(request, pageable);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "Delete Delegation Mapping", description = "This API is use to delete Delegation Mapping by id")
	@PostMapping("delete/delegation")
	public HRMSBaseResponse<String> deleteDelegationMapping(@RequestBody DeleteDelegationMappingVO request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.deleteDelegationMapping(request);
			response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
			response.setApplicationVersion(applicationVersion);
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

	/*** API to submit delegation request added by Akanksha *****/

	@Operation(summary = "Submit delegation", description = "This API is use to submit delegation")
	@PostMapping("delegation")
	HRMSBaseResponse<String> submitDelegation(@RequestBody DelegationRequestVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.submitDelegation(request);
			response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "Get Department-wise KPI Count Summary", description = "This API returns KPI submission and pending statistics grouped by department")
	@GetMapping("/deptwise-count")
	public HRMSBaseResponse<List<DeptwiseCountResponseVO>> getDeptwiseCountSummary() {
		HRMSBaseResponse<List<DeptwiseCountResponseVO>> response = new HRMSBaseResponse<>();

		try {
			response = kraService.getDeptwiseCountSummary();
			response.setResponseCode(1200);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "Download Delegation Mapping Report", description = "This API is used to download the Delegation Mapping report in Excel format")
	@PostMapping("/downloaddelegationreport")
	public ResponseEntity<byte[]> downloadDelegationMappingReport(@RequestParam(required = false) String searchText)
			throws HRMSException {
		try {
			byte[] excelData = kraService.downloadDelegationMappingReport(searchText);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDisposition(
					ContentDisposition.builder("attachment").filename("Delegation_Mapping_Report.xlsx").build());

			return ResponseEntity.ok().headers(headers).body(excelData);

		} catch (HRMSException e) {
			log.error("HRMSException: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(("Error: " + e.getResponseMessage()).getBytes());
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ResponseCode.getResponseCodeMap().get(1500).getBytes());
		}
	}

	@Operation(summary = "Download Delegate List Report", description = "This API is used to download the Delegate list report in Excel format")
	@PostMapping("/downloaddelegatereport")
	public ResponseEntity<byte[]> downloadDelegateListReport(@RequestBody KraListRequestVo request)
			throws HRMSException {
		try {
			byte[] excelData = kraService.downloadDelegateListReport(request);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDisposition(
					ContentDisposition.builder("attachment").filename("Delegate_List_Report.xlsx").build());

			return ResponseEntity.ok().headers(headers).body(excelData);

		} catch (HRMSException e) {
			log.error("HRMSException: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(("Error: " + e.getResponseMessage()).getBytes());
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ResponseCode.getResponseCodeMap().get(1500).getBytes());
		}
	}

	@Operation(summary = "Activate KRA Cycle", description = "Activates the selected KRA Cycle for the current year and deactivates others.")
	@PostMapping("activatekracycle")
	public HRMSBaseResponse<String> activateKraCycle(@RequestBody KraCycleRequestVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.activateKraCycle(request);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}

	@Operation(summary = "Pre Delete Check for Cycle", description = "Checks if a cycle is mapped to any KRA data before deletion")
	@GetMapping("/predeletecheck")
	public HRMSBaseResponse<String> preDeleteCheckForCycle(@RequestParam Long cycleId) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.preDeleteCheckForCycle(cycleId);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "Get all cycle by Year Id", description = "Get all cycle by Year Id")
	@GetMapping("/allcycles")
	public HRMSBaseResponse<KraCycleListResponseVO> getKraCycleList(@RequestParam long yearId,
			@RequestParam(required = false) String searchText, Pageable pageable) {

		HRMSBaseResponse<KraCycleListResponseVO> response = new HRMSBaseResponse<>();

		try {
			response = kraService.getKraCycleList(yearId, searchText, pageable);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
		} catch (Exception e) {
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}

		return response;
	}

	@Operation(summary = "Delete kra Cycle", description = "Delete kra Cycle with related Data ")
	@PostMapping("/deletecycle")
	public HRMSBaseResponse<String> deleteCycleById(@RequestParam Long cycleId) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.deleteCycleById(cycleId);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "get KPI Statistics Dashboard for Donut ", description = "On Dashboard HR/Manager/HOD/Employee can see Submitted and Pending KPI Count with Status")
	@PostMapping("getpmsdonutdashboard")
	HRMSBaseResponse<DonutCountResponse> pmsDonutDashboard(@RequestBody PmsDashboardRequestVo request)
			throws HRMSException {
		HRMSBaseResponse<DonutCountResponse> response = new HRMSBaseResponse<>();
		try {
			response = kraService.pmsDonutDashboard(request);
			response.setApplicationVersion(applicationVersion);
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

	@GetMapping("/kracycletypes")
	public HRMSBaseResponse getKraCycleTypeList() {
		HRMSBaseResponse response = new HRMSBaseResponse();
		try {
			List<KraCycleTypeVO> cycleTypeVoList = kraService.getKraCycleTypeList();
			response.setResponseBody(cycleTypeVoList);
			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
		} catch (Exception e) {
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "Get Last Year's Final HOD Ratings", description = "This API fetches the final ratings for the logged-in employee for the last year's final cycle.")
	@GetMapping("/final-rating-lastyear")
	public HRMSBaseResponse<List<HodCycleFinalRatingResponseVO>> getLastYearFinalRating() {
		HRMSBaseResponse<List<HodCycleFinalRatingResponseVO>> response = new HRMSBaseResponse<>();

		try {
			response = kraService.getLastYearFinalRating(null);
			response.setResponseCode(1200);
			response.setApplicationVersion(applicationVersion);
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

	@Operation(summary = "Get Status of KPI Form For Employee", description = "This API fetches  Status of KPI Form For Employee")
	@GetMapping("/kpi-form-status")
	public HRMSBaseResponse<KpiFormFinalResponseVo> getKpiFormStatus(@RequestParam Long yearId,
			@RequestParam Long empId) {

		HRMSBaseResponse<KpiFormFinalResponseVo> response = new HRMSBaseResponse<>();

		try {

			KpiFormFinalResponseVo result = kpiService.getKpiFormStatus(yearId, empId);

			response.setResponseCode(1200);
			response.setResponseBody(result);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}

	@Operation(summary = "Set Status of KPI Form For Employee", description = "This API Updates Status of KPI Form For Employee")
	@PostMapping("/set-kpi-form-status")
	public HRMSBaseResponse<KpiFormFinalResponseVo> setKpiFormStatus(@RequestBody KpiFormStatusRequestVo request) {

		HRMSBaseResponse<KpiFormFinalResponseVo> response = new HRMSBaseResponse<>();

		try {

			HRMSBaseResponse<KpiFormFinalResponseVo> result = kpiService.setKpiFormStatus(request);

			response.setResponseCode(result.getResponseCode());
			response.setResponseMessage(result.getResponseMessage());
			response.setResponseBody(result.getResponseBody());
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}

	@Operation(summary = "Get List of KPI Category from Master", description = "Get List of KPI Category from Master")
	@GetMapping("/kpi-sub-category-list")
	public HRMSBaseResponse<List<KpiResponseVo>> getKpiSubCategoryList(@RequestParam Long categoryId) {

		HRMSBaseResponse<List<KpiResponseVo>> response = new HRMSBaseResponse<>();

		try {
			List<KpiResponseVo> result = kpiService.getKpiSubCategoryList(categoryId);

			response.setResponseCode(1200);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setResponseBody(result);
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;

	}

	@Operation(summary = "Set Grade to stage Mapping", description = "This API Updateds/Insert Grade to stage Mapping  ")
	@PostMapping("/set-grade-to-stage")
	public HRMSBaseResponse<String> mapGradeToKpi(@RequestBody MapGradeToStagesRequestVO request) {

		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();

		try {
			HRMSBaseResponse<String> result = kpiService.setGradeToStageMapping(request);

			response.setResponseCode(result.getResponseCode());
			response.setResponseMessage(result.getResponseMessage());
			response.setResponseBody(result.getResponseBody());
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}

	@Operation(summary = "Get Grade to Stage Mapping by ID", description = "This API fetches the Grade to Stage Mapping details for the given ID")
	@GetMapping("/get-grade-to-stage")
	public HRMSBaseResponse<MapGradeToStagesResponseVO> getGradeToStageMappingById(@RequestParam Long mappingId) {

		HRMSBaseResponse<MapGradeToStagesResponseVO> response = new HRMSBaseResponse<>();

		try {
			HRMSBaseResponse<MapGradeToStagesResponseVO> result = kpiService.getGradeToStageMappingById(mappingId);

			response.setResponseCode(result.getResponseCode());
			response.setResponseMessage(result.getResponseMessage());
			response.setResponseBody(result.getResponseBody());
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}

	@Operation(summary = "Delete category", description = "API is used to Delete category ")
	@PostMapping("/deletecategory")
	public HRMSBaseResponse<String> deleteById(@RequestParam Long id) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.deleteById(id);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}

	@Operation(summary = "Get Grade to Stage Mappings", description = "This API fetches paginated Grade to Stage Mappings with optional filters")
	@PostMapping("/get-grade-to-stage-list")
	public HRMSBaseResponse<List<MapAllGradeToStagesResponseVO>> getGradeToStageMappings(
			@RequestBody(required = false) MapGradeToStagesRequestVO requestVO, Pageable pageable) {

		HRMSBaseResponse<List<MapAllGradeToStagesResponseVO>> response = new HRMSBaseResponse<>();
		try {
			response = kpiService.getGradeToStageMappings(requestVO, pageable);
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}

	@Operation(summary = "Get category list ", description = "get category list")
	@GetMapping("/categorylist")
	HRMSBaseResponse<List<CategoryResponseVo>> getCategoryList(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String searchText) {
		HRMSBaseResponse<List<CategoryResponseVo>> response = new HRMSBaseResponse<>();
		try {
			Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
			response = kpiService.getCategoryList(pageable, searchText);

			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}

	@Operation(summary = "Get category by id ", description = "get category by id")
	@GetMapping("/categorybyid")
	HRMSBaseResponse<CategoryRequestVo> getCategoryById(@RequestParam(required = true) Long id) {
		HRMSBaseResponse<CategoryRequestVo> response = new HRMSBaseResponse<>();
		try {

			response = kpiService.getCategoryById(id);
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}

	@Operation(summary = "Get List of Subcategories Pageable", description = "This API fetches paginated Subcategory List")
	@PostMapping("/get-subcategory-list")
	public HRMSBaseResponse<List<SubCategoryVo>> getAllSubcategoryList(
			@RequestBody(required = false) SubCategoryRequestVo requestVO, Pageable pageable) {

		HRMSBaseResponse<List<SubCategoryVo>> response = new HRMSBaseResponse<>();
		try {
			response = kpiService.getAllSubcategoryList(requestVO, pageable);
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}

	@Operation(summary = "Get Subcategory By ID", description = "This API fetches Subcategory details by Subcategory ID")
	@GetMapping("/subcategorybyid")
	public HRMSBaseResponse<SubCategoryVo> getSubCategoryById(@RequestParam Long id) {

		HRMSBaseResponse<SubCategoryVo> response = new HRMSBaseResponse<>();
		try {
			response = kpiService.getSubCategoryById(id);
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}

	@Operation(summary = "Delete Subcategory By ID", description = "This API deletes Subcategory details by Subcategory ID")
	@PostMapping("/deletesubcategorybyid")
	public HRMSBaseResponse<SubCategoryVo> deleteSubCategoryById(@RequestParam Long id) {

		HRMSBaseResponse<SubCategoryVo> response = new HRMSBaseResponse<>();
		try {
			response = kpiService.deleteSubCategoryById(id);
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}

	@Operation(summary = "Save Notes for screens", description = "This API Creates/Updates Notes for Screens")
	@PostMapping("/save-notes")
	public HRMSBaseResponse<String> saveNotes(@RequestBody NotesRequestVO request) {

		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();

		try {
			HRMSBaseResponse<String> result = kpiService.saveNotes(request);

			response.setResponseCode(result.getResponseCode());
			response.setResponseMessage(result.getResponseMessage());
			response.setResponseBody(result.getResponseBody());
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}

	@Operation(summary = "Get List of Notes Screenwise", description = "This API fetches paginated Note List")
	@GetMapping("/get-all-notes")
	public HRMSBaseResponse<List<NotesResponseVO>> getAllNotes(@RequestParam(required = false) String keyword,
			Pageable pageable) {

		HRMSBaseResponse<List<NotesResponseVO>> response = new HRMSBaseResponse<>();
		try {
			response = kpiService.getAllNotes(keyword, pageable);
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}

	@Operation(summary = "Get Note by Id", description = "This API fetches a single Note by its Id")
	@GetMapping("/get-note-byid")
	public HRMSBaseResponse<NotesResponseVO> getNoteById(@RequestParam Long id) {

		HRMSBaseResponse<NotesResponseVO> response = new HRMSBaseResponse<>();
		try {
			response = kpiService.getNoteById(id);
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}

	@Operation(summary = "Delete Note", description = "API is used to soft delete a note by marking it inactive")
	@PostMapping("/deletenote")
	public HRMSBaseResponse<String> deleteNoteById(@RequestParam Long id) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kpiService.deleteNoteById(id);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}

	@Operation(summary = "Get Notes by Screen Id", description = "This API fetches all Notes by its Screen Id")
	@GetMapping("/get-notes-by-screenid")
	public HRMSBaseResponse<List<NotesResponseVO>> getNotesByScreenId(@RequestParam Long id) {

		HRMSBaseResponse<List<NotesResponseVO>> response = new HRMSBaseResponse<>();
		try {
			response = kpiService.getNoteByScreeId(id);
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}

	@Operation(summary = "Analytical Cycle Wise Dashboard ", description = "This API fetches cycle-wise analytical dashboard data based on year")
	@PostMapping("/analytical-dashboard/cycle-wise")
	public HRMSBaseResponse<List<CycleWiseResponseVO>> analyticalCycleWiseDashboard(
			@RequestBody PmsDashboardRequestVo requestVO) {

		HRMSBaseResponse<List<CycleWiseResponseVO>> response = new HRMSBaseResponse<>();

		try {
			response = kpiService.analyticalCycleWiseDashboard(requestVO);
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}

	
	@Operation(summary = "Get Analytical Department Report", description = "This API fetches analytical department report by Year, Cycle, and Department")
	@PostMapping("/get-analytical-department-report")
	public HRMSBaseResponse<List<AnalyticalDepartmentReportVO>> getAnalyticalDepartmentReport(@RequestBody AnalyticalDepartmentReportRequestVO request) {

		HRMSBaseResponse<List<AnalyticalDepartmentReportVO>> response = new HRMSBaseResponse<>();
		try {
			response = kpiService.getAnalyticalDepartmentReport(request);
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}
	
	@Operation(summary = "Get List of Employees Report", description = "This API fetches paginated Employee Report list")
	@GetMapping("/get-all-employee-reports")
	public HRMSBaseResponse<List<EmployeeReportVo>> getAllEmployeeReports(
	        @RequestParam(required = false) String keyword) {

	    HRMSBaseResponse<List<EmployeeReportVo>> response = new HRMSBaseResponse<>();
	    try {
	        
	        response = kpiService.getAllEmployeeReport(keyword);
	        response.setApplicationVersion(applicationVersion);

	    } catch (HRMSException e) {
	        log.error(e.getMessage(), e);
	        response.setResponseCode(e.getResponseCode());
	        response.setResponseMessage(e.getResponseMessage());
	        response.setApplicationVersion(applicationVersion);

	    } catch (Exception e) {
	        log.error(e.getMessage(), e);
	        response.setResponseCode(1500);
	        response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
	        response.setApplicationVersion(applicationVersion);
	    }

	    return response;
	}


	
	@Operation(summary = "Download Analytical Department Report", description = "This API is used to download the Analytical Department Report in Excel format")
	@PostMapping("/download-analytical-department-report")
	public ResponseEntity<byte[]> downloadAnalyticalDepartmentReport(
			@RequestBody AnalyticalDepartmentReportRequestVO request) throws HRMSException {
		try {
			byte[] excelData = kpiService.downloadAnalyticalDepartmentReport(request);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDisposition(
					ContentDisposition.builder("attachment").filename("Analytical_Department_Report.xlsx").build());

			return ResponseEntity.ok().headers(headers).body(excelData);

		} catch (HRMSException e) {
			log.error("HRMSException: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(("Error: " + e.getResponseMessage()).getBytes());
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ResponseCode.getResponseCodeMap().get(1500).getBytes());
		}
	}
	
	@Operation(summary = "Toggle Note Status", description = "API is used to activate or deactivate a note by toggling its status")
	@PostMapping("/toggle-note-status")
	public HRMSBaseResponse<String> toggleNoteStatus(@RequestBody NotesRequestVO requestVo) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kpiService.toggleNotesStatus(requestVo);
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}
		return response;
	}
	
	@Operation(summary = "Download Employee Report", description = "This API is used to download the Employee Analytical Department report in Excel format")
	@PostMapping("/downloademployeereport")
	public ResponseEntity<byte[]> downloadEmployeeReport(@RequestParam(required = false) String searchText)
			throws HRMSException {
		try {
			byte[] excelData = kpiService.downloadEmployeeReport(searchText);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDisposition(ContentDisposition.builder("attachment")
					.filename("Employee_Report.xlsx").build());

			return ResponseEntity.ok().headers(headers).body(excelData);

		} catch (HRMSException e) {
			log.error("HRMSException while downloading Employee Report: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(("Error: " + e.getResponseMessage()).getBytes());
		} catch (Exception e) {
			log.error("Unexpected Exception while downloading Employee Report: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ResponseCode.getResponseCodeMap().get(1500).getBytes());
		}
	}

	/**
	 * @Author: Akanaksha
	 ***/

	@Operation(summary = "Get notifications list ", description = "Get notifications list ")
	@GetMapping("/notifications")
	HRMSBaseResponse<NotificationVo> getNotifications() {
		HRMSBaseResponse<NotificationVo> response = new HRMSBaseResponse<>();
		try {

			response = kpiService.getNotifications();
			response.setApplicationVersion(applicationVersion);

		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}
	
	@Operation(summary = "Download Analytical Cycle Wise Report", description = "This API is used to download the Analytical Cycle Wise report in Excel format")
	@PostMapping("/downloadCycleWiseReport")
	public ResponseEntity<byte[]> downloadAnalyticalCycleWiseReport(@RequestBody PmsDashboardRequestVo request)
			throws HRMSException {
		try {
			byte[] excelData = kpiService.downloadAnalyticalCycleWiseReport(request);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDisposition(
					ContentDisposition.builder("attachment").filename("Analytical_Cycle_Wise_Report.xlsx").build());

			return ResponseEntity.ok().headers(headers).body(excelData);

		} catch (HRMSException e) {
			log.error("HRMSException: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(("Error: " + e.getResponseMessage()).getBytes());
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ResponseCode.getResponseCodeMap().get(1500).getBytes());
		}
	}

	
	@Operation(summary = "save  HOD to Department Mapping", description = "This api is use to save  HOD to Department Mapping")
	@PostMapping("/hod-to-deparment-mapping")
	HRMSBaseResponse<String> addHodToDepartmentMap(@RequestBody HodToDepartmentMapVO request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kraService.addHodToDepartmentMap(request);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(e.getResponseCode());
			response.setResponseBody(ResponseCode.getResponseCodeMap().get(1200));
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
		}
		return response;
	}
	
	
	@Operation(summary = "Delete HOD to Department mapping", description = "This API is used to delete a HOD to Department mapping by ID")
	@PostMapping("/deletehodToDepartment")
	public HRMSBaseResponse<String> deleteHodToDepartmentMap(@RequestParam Long id) {
	    HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
	    try {
	        response = kraService.deleteHodToDepartmentMap(id);
	        response.setApplicationVersion(applicationVersion);
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
	
	@Operation(summary = "Get all active HOD to Department mappings", description = "Returns a paginated list of active HOD to Department mappings with employee, department, branch, and division details.")
	@GetMapping("/hodtodepartmentlist")
	public HRMSBaseResponse<HodToDepartmentMapListResponseVO> getAllHodToDepartmentMap(Pageable pageable) {

		HRMSBaseResponse<HodToDepartmentMapListResponseVO> response = new HRMSBaseResponse<>();

		try {
			response = kraService.getAllHodToDepartmentMap(pageable);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}
	
	/*** @author akanksha.gaikwad
	 * API to approe kpi by function head**/
	
	@Operation(summary = "Accept KPI by Function head", description = "Accept KPI by Function head")
	@PostMapping("acceptkpibyfunctionhead")
	HRMSBaseResponse<String> acceptKpiFunctionHead(@RequestBody RejectRequestVo request) {
		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
		try {
			response = kpiService.acceptKpiFunctionHead(request);

			response.setResponseBody(IHRMSConstants.successMessage);
			response.setApplicationVersion(applicationVersion);
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
	
	/** API for reject kpi by function head **/
	
	@Operation(summary = "Reject Kpi by Function Head" , description = "Reject Kpi by Function Head")
	@PostMapping("rejectkpibyfunctionhead")
	HRMSBaseResponse<String> rejectKpiFunctionHead(@RequestBody RejectRequestVo request){
		HRMSBaseResponse<String> response =new HRMSBaseResponse<>();
		try {
		 response=kpiService.rejectKpibyFunctionHead(request);
		 response.setResponseBody(IHRMSConstants.successMessage);
			response.setApplicationVersion(applicationVersion);
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
	
	
	@Operation(summary = "Migrate KRA data from one cycle to another", 
	           description = "This API migrates KRA details from source cycle to target cycle for all employees")
	@PostMapping("/migratekradata")
	public HRMSBaseResponse<String> migrateKraData() {
	    HRMSBaseResponse<String> response = new HRMSBaseResponse<>();
	    try {
	        response = kpiService.migrateData(); 
	        response.setApplicationVersion(applicationVersion);
	    } catch (HRMSException e) {
	        log.error("HRMSException while migrating KRA data: {}", e.getMessage(), e);
	        response.setResponseCode(e.getResponseCode());
	        response.setResponseMessage(e.getResponseMessage());
	        response.setApplicationVersion(applicationVersion);
	    } catch (Exception e) {
	        log.error("Unexpected error while migrating KRA data: {}", e.getMessage(), e);
	        response.setResponseCode(1500);
	        response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
	        response.setApplicationVersion(applicationVersion);
	    }
	    return response;
	}
	
	@Operation(summary = "Get HR to Department mapping by HR ID", description = "Returns department details mapped to a specific HR (by employee ID).")
	@GetMapping("/hrtodepartment")
	public HRMSBaseResponse<List<HrToDepartmentVO>> getHrToDepartment() {

		HRMSBaseResponse<List<HrToDepartmentVO>> response = new HRMSBaseResponse<>();

		try {
			response = kraService.getHrToDepartment();
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}

	
	@Operation(summary = "Submit Employee or Manager KPI Form", description = "Allows Employee or Manager to submit their KPI Form based on their role and workflow stage.")
	@PostMapping("/submit-kpi-form")
	public HRMSBaseResponse<String> submitKpiForm(@RequestParam("kraId") Long kraId) {

		HRMSBaseResponse<String> response = new HRMSBaseResponse<>();

		try {
			response = kraService.submitKpiForm(kraId);
			response.setApplicationVersion(applicationVersion);
		} catch (HRMSException e) {
			log.error("HRMSException in submitKpiForm: {}", e.getMessage(), e);
			response.setResponseCode(e.getResponseCode());
			response.setResponseMessage(e.getResponseMessage());
			response.setApplicationVersion(applicationVersion);
		} catch (Exception e) {
			log.error("Exception in submitKpiForm: {}", e.getMessage(), e);
			response.setResponseCode(1500);
			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1500));
			response.setApplicationVersion(applicationVersion);
		}

		return response;
	}

}
