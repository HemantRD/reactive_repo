package com.vinsys.hrms.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vinsys.hrms.constants.EFeedbackOption;
import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.employee.vo.ChecklistSubmitVo;
import com.vinsys.hrms.employee.vo.EmployeeSeparationRequestVO;
import com.vinsys.hrms.employee.vo.FeedbackOptionVO;
import com.vinsys.hrms.employee.vo.FeedbackQuestionVO;
import com.vinsys.hrms.employee.vo.SubmittedEmployeeFeedbackVO;
import com.vinsys.hrms.employee.vo.UserFeedbackVO;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.master.dao.IReleaseTypeMasterDAO;
import com.vinsys.hrms.master.entity.ReleaseTypeMaster;

@Service
public class SeparationAuthorityHelper {
	@Autowired
	IReleaseTypeMasterDAO releaseTypeMasterDAO;
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public void applySepareationInputValidation(EmployeeSeparationRequestVO request) throws HRMSException {

		if (!HRMSHelper.regexMatcher(String.valueOf(request.getModeofSeparation().getId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Mode of Separation Id ");
		}

		if (HRMSHelper.isLongZero(request.getModeofSeparation().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Mode of Separation Id ");
		}

		if (!HRMSHelper.regexMatcher(String.valueOf(request.getSeparationReasonVO().getId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Separation Reason Id");
		}

		if (HRMSHelper.isLongZero(request.getSeparationReasonVO().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Separation Reason Id ");
		}

		if (HRMSHelper.isNullOrEmpty(request.getEmployeeComment())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Employee Comment ");
		}

		if (!HRMSHelper.regexMatcher(request.getEmployeeComment(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Employee Comment ");
		}

	}

	public void approvedSepareationInputValidation(EmployeeSeparationRequestVO request) throws HRMSException {

		if (!HRMSHelper.regexMatcher(String.valueOf(request.getId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id ");
		}

		if (HRMSHelper.isLongZero(request.getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id ");
		}

		if (!HRMSHelper.regexMatcher(String.valueOf(request.getSeparationReasonVO().getId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Separation Reason Id");
		}

		if (HRMSHelper.isLongZero(request.getSeparationReasonVO().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Separation Reason Id ");
		}

		if (HRMSHelper.isNullOrEmpty(request.getRoComment())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Ro APPROVED Comment ");
		}

		if (!HRMSHelper.regexMatcher(request.getRoComment(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Ro APPROVED Comment ");
		}

		// get release type
		if (!HRMSHelper.isNullOrEmpty(request.getReleaseTypeVO())) {

			if (!HRMSHelper.isNullOrEmpty(request.getReleaseTypeVO().getId())) {
				ReleaseTypeMaster releaseType = releaseTypeMasterDAO.findByIsActiveAndId(ERecordStatus.Y.name(),
						request.getReleaseTypeVO().getId());
				String releaseDateMandtory = ERecordStatus.N.name();
				if (!HRMSHelper.isNullOrEmpty(releaseType)) {
					releaseDateMandtory = releaseType.getReleaseDateMandatory();
				} else {
					throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
				}

				// if releaseDateMandtory then
				if (releaseDateMandtory.equalsIgnoreCase(ERecordStatus.Y.name())) {
					if (HRMSHelper.isNullOrEmpty(request.getReleaseDateByRo())) {
						throw new HRMSException(1500,
								ResponseCode.getResponseCodeMap().get(1501) + " To Release date by RO ");
					}
					if (!HRMSHelper.isNullOrEmpty(request.getReleaseDateByRo())) {
						if (!HRMSHelper.validateDateFormate(request.getReleaseDateByRo())) {
							throw new HRMSException(1500,
									ResponseCode.getResponseCodeMap().get(1501) + " To Release date by RO ");
						}
					}
				}

			} else {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + " To release type ");
			}

		} else {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501) + " To release type ");
		}

	}

	public void cancelSepareationInputValidation(EmployeeSeparationRequestVO request) throws HRMSException {

		if (!HRMSHelper.regexMatcher(String.valueOf(request.getId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id ");
		}

		if (HRMSHelper.isLongZero(request.getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + "Id ");
		}

		if (HRMSHelper.isNullOrEmpty(request.getEmpCancelComment())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Employee cancel Comment ");
		}

		if (!HRMSHelper.regexMatcher(request.getEmpCancelComment(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Employee cancel Comment ");
		}

	}

	public void orgLevelApprovalInputValidation(EmployeeSeparationRequestVO request) throws HRMSException {

		if (!HRMSHelper.regexMatcher(String.valueOf(request.getId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id ");
		}

		if (HRMSHelper.isLongZero(request.getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + "Id ");
		}

		if (HRMSHelper.isNullOrEmpty(request.getOrgComment())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Org Comment ");
		}

		if (!HRMSHelper.regexMatcher(request.getOrgComment(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Org Comment ");
		}

		if (HRMSHelper.isNullOrEmpty(request.getActualRelievingDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Actual Relieving Date");
		}

		if (!HRMSHelper.validateDateFormate(request.getActualRelievingDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Actual Relieving Date");
		}

	}

	public void rejectSepareationInputValidation(EmployeeSeparationRequestVO request) throws HRMSException {

		if (!HRMSHelper.regexMatcher(String.valueOf(request.getId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id ");
		}

		if (HRMSHelper.isLongZero(request.getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id ");
		}

		if (!HRMSHelper.regexMatcher(String.valueOf(request.getSeparationReasonVO().getId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Separation Reason Id");
		}

		if (HRMSHelper.isLongZero(request.getSeparationReasonVO().getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Separation Reason Id ");
		}

		if (HRMSHelper.isNullOrEmpty(request.getRoComment())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Ro APPROVED Comment ");
		}

		if (!HRMSHelper.regexMatcher(request.getRoComment(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Ro APPROVED Comment ");
		}

	}

	public void submitChecklistInputValidation(ChecklistSubmitVo request) throws HRMSException {

		if (!HRMSHelper.regexMatcher(String.valueOf(request.getCatalogueId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Catalogue Id");
		}

		if (HRMSHelper.isLongZero(request.getCatalogueId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Catalogue Id ");
		}

		if (HRMSHelper.isNullOrEmpty(request.isHaveCollected())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To isHaveCollected ");
		}

		if (HRMSHelper.isNullOrEmpty(request.getComment())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Comment ");
		}

		if (!HRMSHelper.regexMatcher(request.getComment(), "^[A-Za-z0-9+_/. #\\[\\]@()\"?,\\\\s-]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Comment ");
		}

	}

	public void orgLevelRejectInputValidation(EmployeeSeparationRequestVO request) throws HRMSException {

		if (!HRMSHelper.regexMatcher(String.valueOf(request.getId()), "[0-9]+")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id ");
		}

		if (HRMSHelper.isLongZero(request.getId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + "Id ");
		}

		if (HRMSHelper.isNullOrEmpty(request.getOrgComment())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Org Comment ");
		}

		if (!HRMSHelper.regexMatcher(request.getOrgComment(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Org Comment ");
		}

//		if (HRMSHelper.isNullOrEmpty(request.getOrgComment())) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Org Reasons ");
//		}
//
//		if (!HRMSHelper.regexMatcher(request.getOrgComment(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
//			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Org Reasons ");
//		}

	}

	public void submittedEmployeeFeedbackInputValidation(SubmittedEmployeeFeedbackVO empFb) throws HRMSException {

		List<FeedbackQuestionVO> questionsList = empFb.getEmpFeedbackList();

		for (FeedbackQuestionVO question : questionsList) {
			boolean isSelected = false;
			List<FeedbackOptionVO> optionsList = question.getFeedbackOptions();
			
			if(question.getChoice().equalsIgnoreCase(EFeedbackOption.MULTIPLE.name())) {
			boolean isTrue = optionsList.stream()
			        .filter(option -> option.getIsSelected().equalsIgnoreCase(EFeedbackOption.TRUE.name()))
			        .count() >= 1;
			
			if(!isTrue) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Option ");
			}

			}

			for (FeedbackOptionVO option : optionsList) {

				if (option.getIsSelected().equalsIgnoreCase(EFeedbackOption.SELECTED.name())) {
					isSelected = true;
				}
			}

			if (!isSelected && question.getChoice().equalsIgnoreCase(EFeedbackOption.SINGLE.name())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " To Option ");
			}
			
				if (question.getChoice().equalsIgnoreCase(EFeedbackOption.COMMENT.name())) {
				UserFeedbackVO comment = question.getUserFeedback();
				
				

				if (HRMSHelper.isNullOrEmpty(comment.getComment())) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Comment ");
				}
				if (!HRMSHelper.regexMatcher(comment.getComment(), "[a-zA-Z0-9.-;, ]{1,250}$")) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Comment ");
				}
			}

		}

	}

}
