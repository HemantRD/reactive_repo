package com.vinsys.hrms.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSCandidateHealthReportDAO;
import com.vinsys.hrms.dao.IHRMSCandidatePersonalDetailDAO;
import com.vinsys.hrms.datamodel.BaseId;
import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.datamodel.VOCandidateHealthReport;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateHealthReport;
import com.vinsys.hrms.entity.CandidatePersonalDetail;
import com.vinsys.hrms.translator.HRMSRequestTranslator;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping(path = "/candidateHealthReport")

public class CandidateHealthReportService {

	@Autowired
	IHRMSCandidateDAO candidateDAO;

	@Autowired
	IHRMSCandidatePersonalDetailDAO candidatePersonalDetailDAO;

	@Autowired
	IHRMSCandidateHealthReportDAO candidateHealthReportDAO;

	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String createCandidateHealthReport(@RequestBody VOCandidateHealthReport candidateHealthReportVo)
			throws JsonGenerationException, JsonMappingException, IOException {

		if (!HRMSHelper.isNullOrEmpty(candidateHealthReportVo)
				&& !HRMSHelper.isNullOrEmpty(candidateHealthReportVo.getCandidatePersonalDetail())
				&& !HRMSHelper.isNullOrEmpty(candidateHealthReportVo.getCandidatePersonalDetail().getCandidate())) {
			// Added By Monika.
			if (!HRMSHelper.isNullOrEmpty(candidateHealthReportVo.getInterestedToDonateBlood())
					&& !HRMSHelper.isNullOrEmpty(candidateHealthReportVo.getVisionProblem())
					&& !HRMSHelper.isNullOrEmpty(candidateHealthReportVo.getBloodGroup())) {

				Candidate candidateEntity = new Candidate();
				CandidatePersonalDetail candidatePersonalDetailEntity = new CandidatePersonalDetail();
				CandidateHealthReport candidateHealthReportEntity = new CandidateHealthReport();

				candidateEntity = candidateDAO
						.findById(candidateHealthReportVo.getCandidatePersonalDetail().getCandidate().getId()).get();

				if (!HRMSHelper.isNullOrEmpty(candidateEntity)) {
					// candidatePersonalDetailEntity =
					// candidatePersonalDetailDAO.findBycandidate(candidateEntity);
					candidatePersonalDetailEntity = candidateEntity.getCandidatePersonalDetail();

					if (candidatePersonalDetailEntity == null) {
						candidatePersonalDetailEntity = new CandidatePersonalDetail();
						candidatePersonalDetailEntity.setCandidate(candidateEntity);
						candidatePersonalDetailEntity = candidatePersonalDetailDAO.save(candidatePersonalDetailEntity);
					}

					if (!HRMSHelper.isNullOrEmpty(candidatePersonalDetailEntity)) {
						// candidateHealthReportEntity =
						// candidateHealthReportDAO.findBycandidatePersonalDetail(candidatePersonalDetailEntity);
						candidateHealthReportEntity = candidatePersonalDetailEntity.getCandidateHealthReport();
						candidatePersonalDetailEntity.setEsiNo(candidateHealthReportVo.getEsic());
						String resultMessage = "";
						if (!HRMSHelper.isNullOrEmpty(candidateHealthReportEntity)) {

							candidateHealthReportEntity = HRMSRequestTranslator
									.translateCandidateHealthReportVoToEntityReq(candidateHealthReportEntity,
											candidateHealthReportVo);
							resultMessage = IHRMSConstants.updatedsuccessMessage;
						} else {
							candidateHealthReportEntity = HRMSRequestTranslator.translateToCandidateHealthReportReq(
									candidatePersonalDetailEntity, candidateHealthReportVo);
							// candidateEntity.getCandidatePersonalDetail().setCandidateHealthReport(candidateHealthReportEntity);
							resultMessage = IHRMSConstants.addedsuccessMessage;
						}
						candidateHealthReportEntity = candidateHealthReportDAO.save(candidateHealthReportEntity);
						// candidateDAO.save(candidateEntity);
						// return HRMSHelper.sendSuccessResponse(resultMessage,
						// IHRMSConstants.successCode);

						BaseId response = new BaseId();
						response.setId(candidateHealthReportEntity.getId());
						response.setResponseCode(IHRMSConstants.successCode);
						response.setResponseMessage(resultMessage);

						return HRMSHelper.createJsonString(response);

					} else {
						// 1. register personal details
						// 2. register candidate health report
						return HRMSHelper.sendErrorResponse(IHRMSConstants.DataNotFoundMessage,
								IHRMSConstants.DataNotFoundCode);
					}

				} else {
					// Candidate does not exist
					// temp+="Candidate Does Not Exist";
					return HRMSHelper.sendErrorResponse(IHRMSConstants.InsufficientDataMessage,
							IHRMSConstants.InsufficientDataCode);
				}
				// monika
			} else {
				return HRMSHelper.sendErrorResponse(IHRMSConstants.InvalidInput, IHRMSConstants.NotValidDateCode);
			}
		} // end if
		else {
			return HRMSHelper.sendErrorResponse(IHRMSConstants.DataNotFoundMessage, IHRMSConstants.DataNotFoundCode);
		}
	}

	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value = "/{id}")
	@ResponseBody
	public String findCandidateHealthReportByCandidatePersonalDetailId(@PathVariable("id") int candidateId)
			throws Exception {

		if (!HRMSHelper.isNullOrEmpty(candidateId)) {
			Candidate cand = new Candidate();
			cand.setId(Long.valueOf(String.valueOf(candidateId)));
			CandidatePersonalDetail candidatePersonalDetailEntity = new CandidatePersonalDetail();
			candidatePersonalDetailEntity = candidatePersonalDetailDAO.findBycandidate(cand);
			// int candidatePersonalDetailId =0 ;
			if (!HRMSHelper.isNullOrEmpty(candidatePersonalDetailEntity)
					&& !HRMSHelper.isNullOrEmpty(candidatePersonalDetailEntity.getId())) {

				CandidateHealthReport candidateHealthReportEntity = new CandidateHealthReport();

				// candidatePersonalDetailEntity.setId(Integer.parseInt(candidatePersonalDetailId));
				// candidatePersonalDetailEntity.setId(candidatePersonalDetailId);
				candidateHealthReportEntity = candidateHealthReportDAO
						.findBycandidatePersonalDetail(candidatePersonalDetailEntity);

				if (!HRMSHelper.isNullOrEmpty(candidateHealthReportEntity)) {

					VOCandidateHealthReport voCandidateHealthReport = HRMSResponseTranslator
							.translateToCandidateHealthReportResponse(candidateHealthReportEntity);

					List<Object> list = new ArrayList<>();
					list.add(voCandidateHealthReport);
					HRMSListResponseObject responseObject = new HRMSListResponseObject();
					responseObject.setResponseCode(IHRMSConstants.successCode);
					responseObject.setResponseMessage(IHRMSConstants.successMessage);
					responseObject.setListResponse(list);
					return HRMSHelper.createJsonString(responseObject);
				} else {
					return HRMSHelper.sendErrorResponse(IHRMSConstants.DataNotFoundMessage,
							IHRMSConstants.DataNotFoundCode);
				}

			} else {
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			}
		} else {
			return HRMSHelper.sendErrorResponse(IHRMSConstants.DataNotFoundMessage, IHRMSConstants.DataNotFoundCode);
		}

	}
}
