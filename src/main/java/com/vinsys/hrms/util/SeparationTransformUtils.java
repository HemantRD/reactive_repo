package com.vinsys.hrms.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.vinsys.hrms.employee.vo.CandidateLetterVO;
import com.vinsys.hrms.employee.vo.ChecklistSubmitVo;
import com.vinsys.hrms.employee.vo.EmployeeExitFeedbackVO;
import com.vinsys.hrms.employee.vo.EmployeeSeparationRequestVO;
import com.vinsys.hrms.employee.vo.FeedbackOptionVO;
import com.vinsys.hrms.employee.vo.FeedbackQuestionVO;
import com.vinsys.hrms.employee.vo.IdentificationDetailsVO;
import com.vinsys.hrms.employee.vo.SubmittedEmployeeFeedbackVO;
import com.vinsys.hrms.employee.vo.UserFeedbackVO;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.CandidateLetter;
import com.vinsys.hrms.entity.EmployeeFeedback;
import com.vinsys.hrms.entity.EmployeeSeparationDetails;
import com.vinsys.hrms.entity.EmployeeSeparationDetailsWithHistory;
import com.vinsys.hrms.entity.FeedbackOption;
import com.vinsys.hrms.entity.FeedbackQuestion;
import com.vinsys.hrms.entity.MapEmployeeCatalogueChecklist;
import com.vinsys.hrms.security.SecurityFilter;

public class SeparationTransformUtils {

	public static EmployeeSeparationDetailsWithHistory translateToEmployeeSeparationDetailsWithHistory(
			EmployeeSeparationDetailsWithHistory employeeSeparationDetailsEntityHistory,
			EmployeeSeparationRequestVO voEmployeeSeparationDetails, Date relievingDate, int noticePeriod)
			throws ParseException {

		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		// HRMSDateUtil.parse(request.getLeaveApplied().getFromDate(),
		// IHRMSConstants.FRONT_END_DATE_FORMAT));
		// employeeSeparationDetailsEntity.setActualRelievingDate(formatter.parse(formatter.format(new
		// Date())));

		employeeSeparationDetailsEntityHistory.setActualRelievingDate(relievingDate);

		employeeSeparationDetailsEntityHistory.setResignationDate(formatter.parse(formatter.format(new Date())));

		employeeSeparationDetailsEntityHistory.setCreatedBy(empId.toString());
		employeeSeparationDetailsEntityHistory.setCreatedDate(new Date());

		employeeSeparationDetailsEntityHistory.setEmployeeComment(voEmployeeSeparationDetails.getEmployeeComment());
//		if (voEmployeeSeparationDetails.getHrApproverStatus() != null)
//			employeeSeparationDetailsEntityHistory
//					.setHrApproverStatus(voEmployeeSeparationDetails.getHrApproverStatus());
		// employeeSeparationDetailsEntityHistory.setHRComment(voEmployeeSeparationDetails.getHRComment());
		employeeSeparationDetailsEntityHistory.setIsActive(IHRMSConstants.isActive);
//		employeeSeparationDetailsEntityHistory.setOrgApproverStatus(voEmployeeSeparationDetails.getOrgApproverStatus());
//		employeeSeparationDetailsEntityHistory.setOrgComment(voEmployeeSeparationDetails.getOrgComment());
//		employeeSeparationDetailsEntityHistory.setRoApproverStatus(voEmployeeSeparationDetails.getRoApproverStatus());
//		employeeSeparationDetailsEntityHistory.setRoComment(voEmployeeSeparationDetails.getRoComment());
//		employeeSeparationDetailsEntityHistory.setRoActionDate(HRMSDateUtil
//				.parse(voEmployeeSeparationDetails.getRoActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
//		employeeSeparationDetailsEntityHistory.setOrgActionDate(HRMSDateUtil
//				.parse(voEmployeeSeparationDetails.getOrgActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
//		employeeSeparationDetailsEntityHistory.setHrActionDate(HRMSDateUtil
//				.parse(voEmployeeSeparationDetails.getHrActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		// employeeSeparationDetailsEntity.setSystemEscalatedLevel();
		employeeSeparationDetailsEntityHistory.setNoticeperiod(noticePeriod);
//		employeeSeparationDetailsEntityHistory.setEmployeeAction(voEmployeeSeparationDetails.getEmployeeAction());
//		employeeSeparationDetailsEntityHistory.setEmpWdComment(voEmployeeSeparationDetails.getEmpWdComment());
//		employeeSeparationDetailsEntityHistory.setEmpWdDate(
//				HRMSDateUtil.parse(voEmployeeSeparationDetails.getEmpWdDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
//		employeeSeparationDetailsEntityHistory.setHr_WdActionDate(HRMSDateUtil
//				.parse(voEmployeeSeparationDetails.getHr_WdActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
//		employeeSeparationDetailsEntityHistory.setHr_WdActionStatus(voEmployeeSeparationDetails.getHr_WdAction());
//		employeeSeparationDetailsEntityHistory.setHr_WdComment(voEmployeeSeparationDetails.getHr_WdComment());
//		employeeSeparationDetailsEntityHistory.setOrg_level_emp_Wd_action_Date(HRMSDateUtil.parse(
//				voEmployeeSeparationDetails.getOrg_level_emp_Wd_action_Date(), IHRMSConstants.FRONT_END_DATE_FORMAT));
//		employeeSeparationDetailsEntityHistory
//				.setOrg_level_emp_WdActionStatus(voEmployeeSeparationDetails.getOrg_level_emp_WdAction());
//		employeeSeparationDetailsEntityHistory
//				.setOrg_level_emp_WdComment(voEmployeeSeparationDetails.getOrg_level_emp_WdComment());
//		employeeSeparationDetailsEntityHistory.setROWdActionDate(HRMSDateUtil
//				.parse(voEmployeeSeparationDetails.getROWdActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
//		employeeSeparationDetailsEntityHistory.setROWdComment(voEmployeeSeparationDetails.getROWdComment());
//		employeeSeparationDetailsEntityHistory.setROWdActionStatus(voEmployeeSeparationDetails.getROWdAction());
		return employeeSeparationDetailsEntityHistory;

	}

	public static EmployeeSeparationDetails translateToEmployeeSeparationDetails(
			EmployeeSeparationDetails employeeSeparationDetailsEntity,
			EmployeeSeparationRequestVO voEmployeeSeparationDetails, Date relievingDate, int noticePeriod)
			throws ParseException {

		Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		// HRMSDateUtil.parse(request.getLeaveApplied().getFromDate(),
		// IHRMSConstants.FRONT_END_DATE_FORMAT));
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		// employeeSeparationDetailsEntity.setActualRelievingDate(formatter.parse(formatter.format(new
		// Date())));
		employeeSeparationDetailsEntity.setActualRelievingDate(relievingDate);
		employeeSeparationDetailsEntity.setResignationDate(formatter.parse(formatter.format(new Date())));
		if (voEmployeeSeparationDetails.getId() == 0) {
			employeeSeparationDetailsEntity.setCreatedBy(empId.toString());
			employeeSeparationDetailsEntity.setCreatedDate(new Date());
		} else {
			employeeSeparationDetailsEntity.setUpdatedBy(empId.toString());
			employeeSeparationDetailsEntity.setUpdatedDate(new Date());
		}

		employeeSeparationDetailsEntity.setEmployeeComment(voEmployeeSeparationDetails.getEmployeeComment());
//		if (voEmployeeSeparationDetails.getHrApproverStatus() != null)
//			employeeSeparationDetailsEntity.setHrApproverStatus(voEmployeeSeparationDetails.getHrApproverStatus());
//		employeeSeparationDetailsEntity.setHRComment(voEmployeeSeparationDetails.getHRComment());
		employeeSeparationDetailsEntity.setIsActive(IHRMSConstants.isActive);
//		employeeSeparationDetailsEntity.setOrgApproverStatus(voEmployeeSeparationDetails.getOrgApproverStatus());
//		employeeSeparationDetailsEntity.setOrgComment(voEmployeeSeparationDetails.getOrgComment());
//		employeeSeparationDetailsEntity.setRoApproverStatus(voEmployeeSeparationDetails.getRoApproverStatus());
//		employeeSeparationDetailsEntity.setRoComment(voEmployeeSeparationDetails.getRoComment());
//		employeeSeparationDetailsEntity.setRoActionDate(HRMSDateUtil
//				.parse(voEmployeeSeparationDetails.getRoActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
//		employeeSeparationDetailsEntity.setOrgActionDate(HRMSDateUtil
//				.parse(voEmployeeSeparationDetails.getOrgActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
//		employeeSeparationDetailsEntity.setHrActionDate(HRMSDateUtil
//				.parse(voEmployeeSeparationDetails.getHrActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		// employeeSeparationDetailsEntity.setSystemEscalatedLevel();
		employeeSeparationDetailsEntity.setNoticeperiod(noticePeriod);
//		employeeSeparationDetailsEntity.setEmployeeAction(voEmployeeSeparationDetails.getEmployeeAction());
//		employeeSeparationDetailsEntity.setEmpWdComment(voEmployeeSeparationDetails.getEmpWdComment());
//		employeeSeparationDetailsEntity.setEmpWdDate(
//				HRMSDateUtil.parse(voEmployeeSeparationDetails.getEmpWdDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
//		employeeSeparationDetailsEntity.setHr_WdActionDate(HRMSDateUtil
//				.parse(voEmployeeSeparationDetails.getHr_WdActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
//		employeeSeparationDetailsEntity.setHr_WdActionStatus(voEmployeeSeparationDetails.getHr_WdAction());
//		employeeSeparationDetailsEntity.setHr_WdComment(voEmployeeSeparationDetails.getHr_WdComment());
//		employeeSeparationDetailsEntity.setOrg_level_emp_Wd_action_Date(HRMSDateUtil.parse(
//				voEmployeeSeparationDetails.getOrg_level_emp_Wd_action_Date(), IHRMSConstants.FRONT_END_DATE_FORMAT));
//		employeeSeparationDetailsEntity
//				.setOrg_level_emp_WdActionStatus(voEmployeeSeparationDetails.getOrg_level_emp_WdAction());
//		employeeSeparationDetailsEntity
//				.setOrg_level_emp_WdComment(voEmployeeSeparationDetails.getOrg_level_emp_WdComment());
//		employeeSeparationDetailsEntity.setROWdActionDate(HRMSDateUtil
//				.parse(voEmployeeSeparationDetails.getROWdActionDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
//		employeeSeparationDetailsEntity.setROWdComment(voEmployeeSeparationDetails.getROWdComment());
//		employeeSeparationDetailsEntity.setROWdActionStatus(voEmployeeSeparationDetails.getROWdAction());
		return employeeSeparationDetailsEntity;

	}

	public static MapEmployeeCatalogueChecklist convertToEmployeeCatalogueChecklistEntity(ChecklistSubmitVo model,
			MapEmployeeCatalogueChecklist entity) {
		entity.setAmount(model.getAmount());
		entity.setComment(model.getComment());
		entity.setHaveCollected(model.isHaveCollected());
		// entity.setUpdatedBy(model.getUpdatedBy());
		entity.setUpdatedDate(new Date());

		return entity;
	}

	public static ChecklistSubmitVo convertToEmployeeChecklistMapping(MapEmployeeCatalogueChecklist entity) {
		ChecklistSubmitVo model = null;
		if (entity != null) {
			model = new ChecklistSubmitVo();
			model.setAmount(entity.getAmount());
			// model.setCatalogueChecklist(convertToCatalogueChecklistItemsModel(entity.getCatalogueChecklist()));
			// model.setEmployeeCatalogueMapping(convertToEmployeeCatalogue(entity.getEmployeeCatalogueMapping()));
			model.setComment(entity.getComment());
			model.setHaveCollected(entity.isHaveCollected());
			model.setId(entity.getId());
		}
		return model;
	}

	public static List<FeedbackQuestionVO> transalteToFeedbackQuestionVO(
			List<FeedbackQuestion> feedbackQuestionEntityList, List<FeedbackQuestionVO> voFeedbackQuestionList) {
		Map<Long, FeedbackQuestionVO> mapOfQuestionOptions = new HashMap<Long, FeedbackQuestionVO>();
		for (FeedbackQuestion feedbackQuestionEntity : feedbackQuestionEntityList) {

			FeedbackQuestionVO voFeedbackQuestion = new FeedbackQuestionVO();

			// Set<VOFeedbackOption> setOfFbOption = new HashSet<VOFeedbackOption>();
			List<FeedbackOptionVO> setOfFbOption = new ArrayList<FeedbackOptionVO>();

			voFeedbackQuestion.setQuestionName(feedbackQuestionEntity.getQuestionName());
			voFeedbackQuestion.setChoice(feedbackQuestionEntity.getChoice());
			voFeedbackQuestion.setId(feedbackQuestionEntity.getId());
			voFeedbackQuestion.setSequenceNumber(feedbackQuestionEntity.getSequenceNumber());
			for (FeedbackOption fbOpt : feedbackQuestionEntity.getFeedbackOptions()) {
				FeedbackOptionVO voFeedbackOption = new FeedbackOptionVO();
				voFeedbackOption.setId(fbOpt.getId());
				voFeedbackOption.setOptionName(fbOpt.getOptionName());
				voFeedbackOption.setSequenceNumber(fbOpt.getSequenceNumber());
				if (feedbackQuestionEntity.getChoice().equalsIgnoreCase(IHRMSConstants.Question_type_MULTIPLE)) {
					voFeedbackOption.setIsSelected("false");
				} else {
					voFeedbackOption.setIsSelected("NOT_SELECTED");
				}
				setOfFbOption.add(voFeedbackOption);
			}
			voFeedbackQuestion.setFeedbackOptions(setOfFbOption);
			// voFeedbackQuestionList.add(voFeedbackQuestion);

			mapOfQuestionOptions.put(voFeedbackQuestion.getId(), voFeedbackQuestion);

		}
		// voFeedbackQuestionList.removeAll(voFeedbackQuestionList);
		for (Long id : mapOfQuestionOptions.keySet()) {
			FeedbackQuestionVO question = new FeedbackQuestionVO();
			// Set<VOFeedbackOption> setOfOption = new HashSet<VOFeedbackOption>();
			List<FeedbackOptionVO> setOfOption = new ArrayList<FeedbackOptionVO>();

			FeedbackQuestionVO temp = mapOfQuestionOptions.get(id);
			// char a = 'a';
			for (FeedbackOptionVO opt : temp.getFeedbackOptions()) {
				setOfOption.add(opt);
			}
			// a = 'a';
			question.setId(temp.getId());
			question.setQuestionName(temp.getQuestionName());
			question.setChoice(temp.getChoice());
			question.setSequenceNumber(temp.getSequenceNumber());
			question.setFeedbackOptions(setOfOption);

			UserFeedbackVO userFeedback = new UserFeedbackVO();
			userFeedback.setComment("");
			question.setUserFeedback(userFeedback);

			voFeedbackQuestionList.add(question);
		}
		return voFeedbackQuestionList;
	}

	public static CandidateLetterVO convertToCandidateLetterModel(CandidateLetter entity, Candidate candidate) {
		CandidateLetterVO model = null;
		if (entity != null) {
			model = new CandidateLetterVO();
			model.setId(entity.getId());
			model.setCandidateId(candidate.getId());
			model.setStatus(entity.getStatus());
			model.setDocumentName(entity.getFileName());
			model.setSentOn(entity.getSentOn());
			model.setDocumentType(entity.getLetterType());
			List<IdentificationDetailsVO> identificationDetails = new ArrayList<IdentificationDetailsVO>();

		}
		return model;
	}

	public static List<EmployeeExitFeedbackVO> translateToEmployeeFeedbackVoList(SubmittedEmployeeFeedbackVO empFb,
			Long empId) {

		List<FeedbackQuestionVO> fbList = empFb.getEmpFeedbackList();
		List<EmployeeExitFeedbackVO> empFbVoList = new ArrayList<EmployeeExitFeedbackVO>();
		for (FeedbackQuestionVO submittedQueOpt : fbList) {
			if (!HRMSHelper.isNullOrEmpty(submittedQueOpt.getFeedbackOptions())
					&& !submittedQueOpt.getFeedbackOptions().isEmpty()) {
				for (FeedbackOptionVO opt : submittedQueOpt.getFeedbackOptions()) {
					EmployeeExitFeedbackVO empFbVo = new EmployeeExitFeedbackVO();

					FeedbackQuestionVO fbQueVo = new FeedbackQuestionVO();
					FeedbackOptionVO fbOptVo = new FeedbackOptionVO();
					// VOUserFeedback userfbVo = new VOUserFeedback();

					// if(submittedQueOpt.getFeedbackOptions().isEmpty() &&
					// !HRMSHelper.isLongZero(userfbVo.getEmployeeFeedbackId())) {
					// empFbVo.setId(userfbVo.getEmployeeFeedbackId());
					// }else {
					empFbVo.setId(opt.getEmployeeFeedbackId());
					// }

					fbQueVo.setId(submittedQueOpt.getId());
					fbOptVo.setId(opt.getId());
					fbOptVo.setIsSelected(opt.getIsSelected().trim());
					// empFbVo.setUserFeedback(userfbVo.getComment());

					empFbVo.setFeedbackQuestion(fbQueVo);
					empFbVo.setFeedbackOption(fbOptVo);

					empFbVoList.add(empFbVo);
				}
			} else {

				EmployeeExitFeedbackVO empFbVo = new EmployeeExitFeedbackVO();
				FeedbackQuestionVO fbQueVo = new FeedbackQuestionVO();
				FeedbackOptionVO fbOptVo = new FeedbackOptionVO();
				// VOUserFeedback userfbVo = new VOUserFeedback();

				if (submittedQueOpt.getFeedbackOptions().isEmpty()
						&& !HRMSHelper.isLongZero(submittedQueOpt.getUserFeedback().getEmployeeFeedbackId())) {
					empFbVo.setId(submittedQueOpt.getUserFeedback().getEmployeeFeedbackId());
				}

				fbQueVo.setId(submittedQueOpt.getId());
				// fbOptVo.setId(null);
				// fbOptVo.setIsSelected(opt.getIsSelected().trim());
				empFbVo.setUserFeedback(submittedQueOpt.getUserFeedback().getComment());

				empFbVo.setFeedbackQuestion(fbQueVo);
				empFbVo.setFeedbackOption(fbOptVo);

				empFbVoList.add(empFbVo);

			}

		}

		return empFbVoList;

	}

	public static EmployeeFeedback translateToEmployeeFeedbackEntity(EmployeeFeedback employeeFeedbackEntity,
			EmployeeExitFeedbackVO voEmployeeFeedback, Long empId) {

//		 employeeFeedbackEntity=new EmployeeFeedback();
		// Employee empEntity = new Employee();
		// FeedbackQuestion fbQueEntity = new FeedbackQuestion();
		// FeedbackOption fbOptEntity = new FeedbackOption();

		// empEntity.setId(voEmpFb.getEmployee().getId());
		// fbQueEntity.setId(voEmpFb.getFeedbackQuestion().getId());
		// fbOptEntity.setId(voEmpFb.getFeedbackOption().getId());

//		employeeFeedbackEntity.setId(empId);
		// empfbEntity.setEmployee(empEntity);
		// empfbEntity.setFeedbackQuestion(fbQueEntity);
		// empfbEntity.setFeedbackOption(fbOptEntity);
		employeeFeedbackEntity.setIsSelected(voEmployeeFeedback.getFeedbackOption().getIsSelected());
		employeeFeedbackEntity.setUserFeedback(voEmployeeFeedback.getUserFeedback());
		employeeFeedbackEntity.setIsActive(IHRMSConstants.isActive);
		employeeFeedbackEntity.setCreatedBy(empId.toString());
		employeeFeedbackEntity.setCreatedDate(new Date());
		employeeFeedbackEntity.setOrgId(SecurityFilter.TL_CLAIMS.get().getOrgId());
		return employeeFeedbackEntity;

	}

	public static List<Object> transalteToFeedbackVOList(List<EmployeeFeedback> employeeFeedbackEntityList,
			List<Object> voEmployeeFeedbackList) {

		Map<Long, FeedbackQuestionVO> mapOfQuestion = new HashMap<>();
		SubmittedEmployeeFeedbackVO voEmpFb = new SubmittedEmployeeFeedbackVO();
		List<FeedbackQuestionVO> empFeedbackList = new ArrayList<>();

		for (EmployeeFeedback employeeFeedbackEntity : employeeFeedbackEntityList) {
			mapOfQuestion.put(employeeFeedbackEntity.getFeedbackQuestion().getId(), null);
		}

		// Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		Iterator<Map.Entry<Long, FeedbackQuestionVO>> entries = mapOfQuestion.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<Long, FeedbackQuestionVO> entry = entries.next();
			// entry.getValue());

			FeedbackQuestionVO question = new FeedbackQuestionVO();
			// Set<VOFeedbackOption> optionList = new HashSet<>();
			List<FeedbackOptionVO> optionList = new ArrayList<FeedbackOptionVO>();
			UserFeedbackVO userFeedBack = new UserFeedbackVO();

			for (EmployeeFeedback employeeFeedbackEntity : employeeFeedbackEntityList) {
				FeedbackOptionVO option = new FeedbackOptionVO();

				if (entry.getKey() == employeeFeedbackEntity.getFeedbackQuestion().getId()) {

					if (!HRMSHelper.isNullOrEmpty(employeeFeedbackEntity.getFeedbackOption())
							&& !HRMSHelper.isNullOrEmpty(employeeFeedbackEntity.getFeedbackOption().getId())) {
						option.setId(employeeFeedbackEntity.getFeedbackOption().getId());
						option.setIsSelected(employeeFeedbackEntity.getIsSelected());
						option.setEmployeeFeedbackId(employeeFeedbackEntity.getId());
						option.setOptionName(employeeFeedbackEntity.getFeedbackOption().getOptionName());
						optionList.add(option);
					} else {
						userFeedBack.setEmployeeFeedbackId(employeeFeedbackEntity.getId());
						userFeedBack.setComment(employeeFeedbackEntity.getUserFeedback());
					}
					question.setQuestionName(employeeFeedbackEntity.getFeedbackQuestion().getQuestionName());
					question.setChoice(employeeFeedbackEntity.getFeedbackQuestion().getChoice());
				}
			}
			question.setFeedbackOptions(optionList);
			question.setUserFeedback(userFeedBack);
			question.setId(entry.getKey());

			mapOfQuestion.put(entry.getKey(), question);
		}

		for (long key : mapOfQuestion.keySet()) {
			FeedbackQuestionVO que = new FeedbackQuestionVO();
			que = mapOfQuestion.get(key);

			empFeedbackList.add(que);
		}

		SubmittedEmployeeFeedbackVO feedback = new SubmittedEmployeeFeedbackVO();
		feedback.setEmpFeedbackList(empFeedbackList);
		voEmployeeFeedbackList.add(feedback);

		return voEmployeeFeedbackList;

	}

}
