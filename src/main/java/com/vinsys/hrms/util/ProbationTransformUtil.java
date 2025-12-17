package com.vinsys.hrms.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vinsys.hrms.dao.confirmation.IHRMSMasterParameterName;
import com.vinsys.hrms.dao.confirmation.IHRMSProbationParameter;
import com.vinsys.hrms.employee.vo.ProbationFeedbackVO;
import com.vinsys.hrms.employee.vo.ProbationParameterVO;
import com.vinsys.hrms.entity.confirmation.MasterEvaluationParameter;
import com.vinsys.hrms.entity.confirmation.ProbationFeedback;
import com.vinsys.hrms.entity.confirmation.ProbationParameter;
import com.vinsys.hrms.master.vo.ProbationFeedbackParameterVO;

/**
 * @author Onkar A
 *
 * 
 */
@Service
public class ProbationTransformUtil {
	@Autowired
	IHRMSMasterParameterName parameterDao;
	@Autowired
	IHRMSProbationParameter paramValueDao;

	public ProbationFeedbackVO convertToProbationFeedbackVO(ProbationFeedback probationFeedback) {

		ProbationFeedbackVO parameterFeedback = new ProbationFeedbackVO();
		DecimalFormat decimalFormat = new DecimalFormat("#.##");

		parameterFeedback.setHrComment(probationFeedback.getHrComment());
		parameterFeedback.setManagerComment(probationFeedback.getManagerComment());
		parameterFeedback.setStatus(probationFeedback.getStatus());
		parameterFeedback.setExtendedBy(probationFeedback.getExtendedBy());
		parameterFeedback.setId(probationFeedback.getId());
		parameterFeedback.setHrSubmitted(probationFeedback.getHrSubmitted());
		parameterFeedback.setManagerSubmitted(probationFeedback.getRoSubmitted());
		parameterFeedback.setEmployeeId(probationFeedback.getEmployee().getId());
		parameterFeedback.setEmployeeName(probationFeedback.getEmployee().getCandidate().getFirstName() + " "
				+ probationFeedback.getEmployee().getCandidate().getLastName());
		parameterFeedback.setDepartment(probationFeedback.getEmployee().getCandidate().getCandidateProfessionalDetail()
				.getDepartment().getDepartmentName());
		parameterFeedback.setDesignation(probationFeedback.getEmployee().getCandidate().getCandidateProfessionalDetail()
				.getDesignation().getDesignationName());
		parameterFeedback.setDateOfJoinig(
				probationFeedback.getEmployee().getCandidate().getCandidateProfessionalDetail().getDateOfJoining());

		// get confirmation due date
		String date = HRMSDateUtil.format(
				probationFeedback.getEmployee().getCandidate().getCandidateProfessionalDetail().getDateOfJoining(),
				IHRMSConstants.POSTGRE_DATE_FORMAT);
		int probationPeriod = 0;
		if (!HRMSHelper.isNullOrEmpty(probationFeedback.getEmployee().getProbationPeriod())) {
			probationPeriod = Integer.parseInt((probationFeedback.getEmployee().getProbationPeriod()));
		}

		Date confirmationDueDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(probationFeedback.getEmployee().getCandidate().getCandidateProfessionalDetail().getDateOfJoining());
		cal.set(Calendar.MONTH, (cal.get(Calendar.MONTH) + probationPeriod));
		confirmationDueDate = HRMSDateUtil.setTimeStampToZero(cal.getTime());
		parameterFeedback.setConfirmationDueDate(confirmationDueDate);

		// calculate total percentage
		float maxTotalRating = 55;
		float empTotalRating = paramValueDao.getTotalEmpRating(probationFeedback.getId());
		float empRatingPercentage = (empTotalRating / maxTotalRating) * 100;

		parameterFeedback.setEmpRatingPercentage(Float.valueOf(decimalFormat.format(empRatingPercentage)));

		float managerTotalRating = paramValueDao.getTotalManagerRating(probationFeedback.getId());
		float managerRatingPercentage = (managerTotalRating / maxTotalRating) * 100;

		parameterFeedback.setManagerRatingPercentage(Float.valueOf(decimalFormat.format(managerRatingPercentage)));

		List<ProbationParameter> parameterList = probationFeedback.getProbationParameter();
		List<ProbationParameterVO> probationParameterVOList = new ArrayList<>();

		for (ProbationParameter obj : parameterList) {
			if (!HRMSHelper.isNullOrEmpty(obj)) {
				ProbationParameterVO temp = new ProbationParameterVO();
				temp.setId(obj.getId());
				temp.setEmpRating(Long.valueOf(obj.getEmpRating()));
				temp.setEmployeeComment(obj.getEmployeeComment());
				temp.setManagerRating(Long.valueOf(obj.getManagerRating()));
				temp.setManagerComment(obj.getManagerComment());

				if (!HRMSHelper.isNullOrEmpty(obj.getParameterValue())) {
					MasterEvaluationParameter param = parameterDao.findByIdAndIsActive(obj.getParameterValue().getId(),
							IHRMSConstants.isActive);
					ProbationFeedbackParameterVO tempParam = new ProbationFeedbackParameterVO();
					tempParam.setId(param.getId());
					tempParam.setOrganizationId(param.getOrganization().getId());
					tempParam.setParameterName(param.getParameterName());
					temp.setParameterValue(tempParam);
				}
				temp.setFeedbackId(parameterFeedback.getId());
				probationParameterVOList.add(temp);
			}
		}
		parameterFeedback.setProbationParameter(probationParameterVOList);

		return parameterFeedback;
	}

	public List<ProbationFeedbackVO> convertToProbationFeedbackVO(List<ProbationFeedback> probationFeedbacks) {
		List<ProbationFeedbackVO> probationFeedbackVOList = new ArrayList<ProbationFeedbackVO>();
		ProbationFeedbackVO parameterFeedback = null;
		DecimalFormat decimalFormat = new DecimalFormat("#.##");

		for (ProbationFeedback probationFeedback : probationFeedbacks) {
			parameterFeedback = new ProbationFeedbackVO();
			parameterFeedback.setHrComment(probationFeedback.getHrComment());
			parameterFeedback.setManagerComment(probationFeedback.getManagerComment());
			parameterFeedback.setStatus(probationFeedback.getStatus());
			parameterFeedback.setExtendedBy(probationFeedback.getExtendedBy());
			parameterFeedback.setId(probationFeedback.getId());
			parameterFeedback.setHrSubmitted(probationFeedback.getHrSubmitted());
			parameterFeedback.setManagerSubmitted(probationFeedback.getRoSubmitted());
			parameterFeedback.setEmployeeId(probationFeedback.getEmployee().getId());
			parameterFeedback.setEmployeeName(probationFeedback.getEmployee().getCandidate().getFirstName() + " "
					+ probationFeedback.getEmployee().getCandidate().getLastName());
			parameterFeedback.setDepartment(probationFeedback.getEmployee().getCandidate()
					.getCandidateProfessionalDetail().getDepartment().getDepartmentName());
			parameterFeedback.setDesignation(probationFeedback.getEmployee().getCandidate()
					.getCandidateProfessionalDetail().getDesignation().getDesignationName());
			parameterFeedback.setDateOfJoinig(
					probationFeedback.getEmployee().getCandidate().getCandidateProfessionalDetail().getDateOfJoining());

			// get confirmation due date
			String date = HRMSDateUtil.format(
					probationFeedback.getEmployee().getCandidate().getCandidateProfessionalDetail().getDateOfJoining(),
					IHRMSConstants.POSTGRE_DATE_FORMAT);
			int probationPeriod = 0;
			if (!HRMSHelper.isNullOrEmpty(probationFeedback.getEmployee().getProbationPeriod())) {
				probationPeriod = Integer.parseInt((probationFeedback.getEmployee().getProbationPeriod()));
			}

			Date confirmationDueDate = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(
					probationFeedback.getEmployee().getCandidate().getCandidateProfessionalDetail().getDateOfJoining());
			cal.set(Calendar.MONTH, (cal.get(Calendar.MONTH) + probationPeriod));
			confirmationDueDate = HRMSDateUtil.setTimeStampToZero(cal.getTime());
			parameterFeedback.setConfirmationDueDate(confirmationDueDate);

			// *********calculation of noOfDaysToConfirm************

			Date tempDate = new Date();
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(
					probationFeedback.getEmployee().getCandidate().getCandidateProfessionalDetail().getDateOfJoining());
			cal1.set(Calendar.MONTH, (cal1.get(Calendar.MONTH) + probationPeriod));
			tempDate = HRMSDateUtil.setTimeStampToZero(cal1.getTime());
			long noOfDaysLeftToConfirm = HRMSDateUtil.getDifferenceInDays(new Date(), tempDate);
			if (probationFeedback.getEmployee().getCandidate().getCandidateEmploymentStatus()
					.equalsIgnoreCase(IHRMSConstants.PROBATION) && noOfDaysLeftToConfirm <= 0) {
				parameterFeedback.setIsFormProbationEnable(true);
			} else {
				parameterFeedback.setIsFormProbationEnable(false);
			}

			// calculate total percentage
			float maxTotalRating = 55;
			float empTotalRating = paramValueDao.getTotalEmpRating(probationFeedback.getId());
			float empRatingPercentage = (empTotalRating / maxTotalRating) * 100;

			parameterFeedback.setEmpRatingPercentage(Float.valueOf(decimalFormat.format(empRatingPercentage)));

			float managerTotalRating = paramValueDao.getTotalManagerRating(probationFeedback.getId());
			float managerRatingPercentage = (managerTotalRating / maxTotalRating) * 100;

			parameterFeedback.setManagerRatingPercentage(Float.valueOf(decimalFormat.format(managerRatingPercentage)));

			List<ProbationParameter> parameterList = probationFeedback.getProbationParameter();
			List<ProbationParameterVO> probationParameterVOList = new ArrayList<>();

			for (ProbationParameter obj : parameterList) {
				if (!HRMSHelper.isNullOrEmpty(obj)) {
					ProbationParameterVO temp = new ProbationParameterVO();
					temp.setId(obj.getId());
					temp.setEmpRating(Long.valueOf(obj.getEmpRating()));
					temp.setEmployeeComment(obj.getEmployeeComment());
					temp.setManagerRating(Long.valueOf(obj.getManagerRating()));
					temp.setManagerComment(obj.getManagerComment());

					if (!HRMSHelper.isNullOrEmpty(obj.getParameterValue())) {
						MasterEvaluationParameter param = parameterDao
								.findByIdAndIsActive(obj.getParameterValue().getId(), IHRMSConstants.isActive);
						ProbationFeedbackParameterVO tempParam = new ProbationFeedbackParameterVO();
						tempParam.setId(param.getId());
						tempParam.setOrganizationId(param.getOrganization().getId());
						tempParam.setParameterName(param.getParameterName());
						temp.setParameterValue(tempParam);
					}
					temp.setFeedbackId(parameterFeedback.getId());
					probationParameterVOList.add(temp);
				}
			}
			parameterFeedback.setProbationParameter(probationParameterVOList);

			probationFeedbackVOList.add(parameterFeedback);

		}

		return probationFeedbackVOList;
	}

}
