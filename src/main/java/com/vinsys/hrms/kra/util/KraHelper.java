package com.vinsys.hrms.kra.util;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.kra.vo.DelegationRequestVo;
import com.vinsys.hrms.kra.vo.DeleteKraRequestVO;
import com.vinsys.hrms.kra.vo.EmpListVo;
import com.vinsys.hrms.kra.vo.HcdCorrcetionRequest;
import com.vinsys.hrms.kra.vo.HcdCorrectionRequestVo;
import com.vinsys.hrms.kra.vo.HodToDepartmentMapVO;
import com.vinsys.hrms.kra.vo.KraCycleRequestVo;
import com.vinsys.hrms.kra.vo.KraDetailsRequestVO;
import com.vinsys.hrms.kra.vo.KraDetailsVO;
import com.vinsys.hrms.kra.vo.KraYearVo;
import com.vinsys.hrms.kra.vo.ObjectiveVo;
import com.vinsys.hrms.kra.vo.OrgKpiDeleteVo;
import com.vinsys.hrms.kra.vo.OrgnizationalKpiVo;
import com.vinsys.hrms.kra.vo.RejectRequestVo;
import com.vinsys.hrms.kra.vo.TimeLineRequestVO;
import com.vinsys.hrms.master.vo.DepartmentVO;
import com.vinsys.hrms.master.vo.GradeMasterVo;
import com.vinsys.hrms.util.ERole;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.ResponseCode;

@Service
public class KraHelper {
	@Value("${max_kra_count}")
	private int kraMaximumCount;
	@Value("${min_kra_count}")
	private int kraMinimumCount;
	@Value("${max_kra_weightage}")
	private int maxKraWeightage;
	@Value("${regex_for_kra_input}")
	private String kraInputStringRegex;

	public void saveKraDetailsInputValidation(KraDetailsRequestVO request) throws HRMSException {
//		String kraInputStringRegex = "^[A-Za-z0-9+_.@()\"?,\\s-]{1,250}$";
		int kracount = 0;
		Float weightage = 0F;

		if (!HRMSHelper.isNullOrEmpty(request) && !HRMSHelper.isNullOrEmpty(request.getKraDetailsVOList())) {

			for (KraDetailsVO kraDetailsVO : request.getKraDetailsVOList()) {

				if (!HRMSHelper.isNullOrEmpty(kraDetailsVO.getAchievementPlan())
						&& !HRMSHelper.regexMatcher(kraDetailsVO.getAchievementPlan(), kraInputStringRegex)) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Achievement plan");
				}
				if (!HRMSHelper.isNullOrEmpty(kraDetailsVO.getDescription())
						&& !HRMSHelper.regexMatcher(kraDetailsVO.getDescription(), kraInputStringRegex)) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Description");
				}
				if (!HRMSHelper.isNullOrEmpty(kraDetailsVO.getKraDetails())
						&& !HRMSHelper.regexMatcher(kraDetailsVO.getKraDetails(), kraInputStringRegex)) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Kra Details");
				}
				if (!HRMSHelper.isNullOrEmpty(kraDetailsVO.getMeasurementCriteria())
						&& !HRMSHelper.regexMatcher(kraDetailsVO.getMeasurementCriteria(), kraInputStringRegex)) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Measurement criteria");
				}

				// check blank space only
//				if (HRMSHelper.isNullOrEmpty(kraDetailsVO.getAchievementPlan().trim())) {
//					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Achievement plan");
//				}
//				if (HRMSHelper.isNullOrEmpty(kraDetailsVO.getDescription().trim())) {
//					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Description");
//				}
//				if (HRMSHelper.isNullOrEmpty(kraDetailsVO.getKraDetails().trim())) {
//					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " KRA Details");
//				}
//				if (HRMSHelper.isNullOrEmpty(kraDetailsVO.getMeasurementCriteria().trim())) {
//					throw new HRMSException(1501,
//							ResponseCode.getResponseCodeMap().get(1501) + " Measurement criteria");
//				}

				kracount++;

				if (!HRMSHelper.isNullOrEmpty(kraDetailsVO.getWeightage())) {
					weightage = weightage + kraDetailsVO.getWeightage();
				}

				if (HRMSHelper.isNullOrEmpty(kraDetailsVO.getWeightage())) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " weightage");
				}

				// weightage
				if (!HRMSHelper.isNullOrEmpty(kraDetailsVO.getWeightage()) && kraDetailsVO.getWeightage() < 1) {
					throw new HRMSException(1501, "Weightage cannot be 0%");
				}
				if (!HRMSHelper.isNullOrEmpty(kraDetailsVO.getWeightage())
						&& kraDetailsVO.getWeightage() > maxKraWeightage) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1600) + " " + maxKraWeightage + "%");
				}

			}

			if (kracount > kraMaximumCount) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1601).replace("{kraMaximumCount}",
						String.valueOf(kraMaximumCount)));
			}
			if (weightage > 100) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501)
						+ " Total weightage should not be greater than 100%");
			}

		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}
	}

	public void submitKraDetailsInputValidation(KraDetailsRequestVO request) throws HRMSException {

//		String kraInputStringRegex = "^[A-Za-z0-9+_.@()\"?,\\s-]{1,250}$";
		int kracount = 0;
		Float weightage = 0F;

		if (!HRMSHelper.isNullOrEmpty(request) && !HRMSHelper.isNullOrEmpty(request.getKraDetailsVOList())) {

			for (KraDetailsVO kraDetailsVO : request.getKraDetailsVOList()) {

				if (HRMSHelper.isNullOrEmpty(kraDetailsVO.getAchievementPlan().trim())) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Achievement plan");
				}
				if (HRMSHelper.isNullOrEmpty(kraDetailsVO.getDescription().trim())) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Description");
				}
				if (HRMSHelper.isNullOrEmpty(kraDetailsVO.getKraDetails().trim())) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " KRA Details");
				}
				if (HRMSHelper.isNullOrEmpty(kraDetailsVO.getMeasurementCriteria().trim())) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Measurement criteria");
				}

				if (!HRMSHelper.isNullOrEmpty(kraDetailsVO.getAchievementPlan())
						&& !HRMSHelper.regexMatcher(kraDetailsVO.getAchievementPlan(), kraInputStringRegex)) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Achievement plan");
				}
				if (!HRMSHelper.isNullOrEmpty(kraDetailsVO.getDescription())
						&& !HRMSHelper.regexMatcher(kraDetailsVO.getDescription(), kraInputStringRegex)) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Description");
				}
				if (!HRMSHelper.isNullOrEmpty(kraDetailsVO.getKraDetails())
						&& !HRMSHelper.regexMatcher(kraDetailsVO.getKraDetails(), kraInputStringRegex)) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " KRA Details");
				}
				if (!HRMSHelper.isNullOrEmpty(kraDetailsVO.getMeasurementCriteria())
						&& !HRMSHelper.regexMatcher(kraDetailsVO.getMeasurementCriteria(), kraInputStringRegex)) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1501) + " Measurement criteria");
				}

				kracount++;
				if (!HRMSHelper.isNullOrEmpty(kraDetailsVO.getWeightage())) {
					weightage = weightage + kraDetailsVO.getWeightage();
				}

				if (HRMSHelper.isNullOrEmpty(kraDetailsVO.getWeightage())) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " weightage");
				}

				// weightage
				if (!HRMSHelper.isNullOrEmpty(kraDetailsVO.getWeightage()) && kraDetailsVO.getWeightage() < 1) {
					throw new HRMSException(1500, "Weightage cannot be 0%");
				}
				if (!HRMSHelper.isNullOrEmpty(kraDetailsVO.getWeightage())
						&& kraDetailsVO.getWeightage() > maxKraWeightage) {
					throw new HRMSException(1501,
							ResponseCode.getResponseCodeMap().get(1600) + " " + maxKraWeightage + "%");
				}
			}
			if (kracount > kraMaximumCount) {
				throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1601).replace("{kraMaximumCount}",
						String.valueOf(kraMaximumCount)));
			}
			if (kracount < kraMinimumCount) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Minimum " + kraMinimumCount + " KRA Mandatory");
			}

			if (weightage > 100) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501)
						+ " Total weightage should not be greater than 100%");
			}

			if (weightage < 100) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1604));
			}
		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}

	}

	public void deleteKraPointInputValidation(DeleteKraRequestVO request) throws HRMSException {
		if (!HRMSHelper.isNullOrEmpty(request)) {
			if (HRMSHelper.isNullOrEmpty(request.getKraDetailsId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " KRA details id.");
			}
			if (HRMSHelper.isNullOrEmpty(request.getKraId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " KRA id.");
			}
		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}

	}

	public void approveKraInputValidation(KraDetailsRequestVO request) throws HRMSException {
//		String kraInputStringRegex = "^[A-Za-z0-9+_.@()\"?,\\s-]{1,250}$";
		if (!HRMSHelper.isNullOrEmpty(request)) {
			if (HRMSHelper.isNullOrEmpty(request.getKraId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " KRA id.");
			}

			if (!HRMSHelper.isNullOrEmpty(request.getKraDetailsVOList())) {
				for (KraDetailsVO kraDetailsVO : request.getKraDetailsVOList()) {
					if (HRMSHelper.isNullOrEmpty(kraDetailsVO.getId())) {
						throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Kradetails id");
					}
					if (HRMSHelper.isNullOrEmpty(kraDetailsVO.getRmComment())) {
						throw new HRMSException(1501,
								ResponseCode.getResponseCodeMap().get(1501) + " RM comment mandatory.");
					}
					if (!HRMSHelper.isNullOrEmpty(kraDetailsVO.getRmComment())
							&& !HRMSHelper.regexMatcher(kraDetailsVO.getRmComment(), kraInputStringRegex)) {
						throw new HRMSException(1501,
								ResponseCode.getResponseCodeMap().get(1501) + " RM comment mandatory.");
					}
				}
			} else {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " KRA details.");
			}
		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}

	}

	public void rejectKraInputValidation(KraDetailsRequestVO request) throws HRMSException {

//		String kraInputStringRegex = "^[A-Za-z0-9+_.@()\"?,\\s-]{1,250}$";
		if (!HRMSHelper.isNullOrEmpty(request)) {
			if (HRMSHelper.isNullOrEmpty(request.getKraId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " KRA id.");
			}

			if (!HRMSHelper.isNullOrEmpty(request.getKraDetailsVOList())) {
				for (KraDetailsVO kraDetailsVO : request.getKraDetailsVOList()) {

					if (HRMSHelper.isNullOrEmpty(kraDetailsVO.getId())) {
						throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Kradetails id");
					}
					if (HRMSHelper.isNullOrEmpty(kraDetailsVO.getRmComment())) {
						throw new HRMSException(1501,
								ResponseCode.getResponseCodeMap().get(1501) + " RM comment mandatory.");
					}

					if (!HRMSHelper.isNullOrEmpty(kraDetailsVO.getRmComment())
							&& !HRMSHelper.regexMatcher(kraDetailsVO.getRmComment(), kraInputStringRegex)) {
						throw new HRMSException(1501,
								ResponseCode.getResponseCodeMap().get(1501) + " RM comment mandatory.");
					}
				}
			} else {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " KRA details.");
			}
		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}
	}

	public void submitPmsKraInputValidation(ObjectiveVo request) throws HRMSException {

		if (!HRMSHelper.isNullOrEmpty(request)) {

			if (HRMSHelper.isNullOrEmpty(request.getSelfrating().getId())
					|| HRMSHelper.isLongZero(request.getSelfrating().getId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Self Rating");
			}
			if (HRMSHelper.isNullOrEmpty(request.getSelfqualitativeassessment())) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Self Qualitative Assessment");
			}

			if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getSelfrating().getId()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Self Rating");
			}
//			if (!HRMSHelper.isNullOrEmpty(request.getSelfqualitativeassessment())
//					&& !HRMSHelper.regexMatcher(request.getSelfqualitativeassessment(), kraInputStringRegex)) {
//				throw new HRMSException(1501,
//						ResponseCode.getResponseCodeMap().get(1501) + " Self Qualitative Assessment");
//			}

		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}

	}

	public void submitManagerRatingInputValidation(ObjectiveVo request) throws HRMSException {

		if (!HRMSHelper.isNullOrEmpty(request)) {

			if (HRMSHelper.isNullOrEmpty(request.getManagerrating().getId())
					|| HRMSHelper.isLongZero(request.getManagerrating().getId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Manager Rating");
			}
			if (HRMSHelper.isNullOrEmpty(request.getManagerqaulitativeassisment())) {
				throw new HRMSException(1501,
						ResponseCode.getResponseCodeMap().get(1501) + " Manager Qualitative Assessment");
			}

			if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getManagerrating().getId()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Manager Rating");
			}
//			if (!HRMSHelper.isNullOrEmpty(request.getSelfqualitativeassessment())
//					&& !HRMSHelper.regexMatcher(request.getSelfqualitativeassessment(), kraInputStringRegex)) {
//				throw new HRMSException(1501,
//						ResponseCode.getResponseCodeMap().get(1501) + " Manager Qualitative Assessment");
//			}

		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}

	}

	public void savePmsKraInputValidation(ObjectiveVo request) throws HRMSException {

		if (!HRMSHelper.isNullOrEmpty(request)) {

			if (!HRMSHelper.isNullOrEmpty(request.getSelfrating())
					&& !HRMSHelper.isNullOrEmpty(request.getSelfrating().getId())
					&& !HRMSHelper.isLongZero(request.getSelfrating().getId())
					&& !HRMSHelper.validateNumberFormat(String.valueOf(request.getSelfrating().getId()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Self Rating");
			}

//			if (!HRMSHelper.isNullOrEmpty(request.getSelfqualitativeassessment())
//					&& !HRMSHelper.regexMatcher(request.getSelfqualitativeassessment(), kraInputStringRegex)) {
//				throw new HRMSException(1501,
//						ResponseCode.getResponseCodeMap().get(1501) + " Self Qualitative Assessment");
//			}

		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}

	}

	public void saveManagerRatingValidation(ObjectiveVo request) throws HRMSException {

		if (!HRMSHelper.isNullOrEmpty(request)) {

			if (!HRMSHelper.isNullOrEmpty(request.getManagerrating())
					&& !HRMSHelper.isNullOrEmpty(request.getManagerrating().getId())
					&& !HRMSHelper.isLongZero(request.getManagerrating().getId())
					&& !HRMSHelper.validateNumberFormat(String.valueOf(request.getManagerrating().getId()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Manager Rating");
			}

//			if (!HRMSHelper.isNullOrEmpty(request.getManagerqaulitativeassisment())
//					&& !HRMSHelper.regexMatcher(request.getManagerqaulitativeassisment(), kraInputStringRegex)) {
//				throw new HRMSException(1501,
//						ResponseCode.getResponseCodeMap().get(1501) + " Manager Qualitative Assessment");
//			}

		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}

	}

	public void submitKpiYearValidation(KraYearVo request) throws HRMSException {

		if (!HRMSHelper.isNullOrEmpty(request)) {

			if (HRMSHelper.isNullOrEmpty(request.getYear())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " KPI Year");
			}

			if (!HRMSHelper.regexMatcher(request.getYear(), "[0-9]{4,4}")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " KPI year ");
			}

		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}

	}

	public void updateKpiCycleValidation(KraCycleRequestVo request) throws HRMSException {

		if (!HRMSHelper.isNullOrEmpty(request)) {

			if (HRMSHelper.isNullOrEmpty(request.getCycleName())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cycle Name");
			}

			if (!HRMSHelper.regexMatcher(request.getCycleName(), "^[a-zA-Z0-9\\s ]{1,2000}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cycle Name");
			}

			if (HRMSHelper.isNullOrEmpty(request.getDescription())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cycle Description");
			}

			if (!HRMSHelper.regexMatcher(request.getDescription(), "^[a-zA-Z0-9\\s ]{1,2000}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cycle Description");
			}

			if (!HRMSHelper.regexMatcher(request.getCycleName(), "^[a-zA-Z0-9\\s ]{1,2000}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cycle Name");
			}

			if (HRMSHelper.isNullOrEmpty(request.getStartDate())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Start Date");
			}

			if (!HRMSHelper.validateDateFormate(request.getStartDate())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Start Date");
			}

			if (HRMSHelper.isNullOrEmpty(request.getEndDate())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " End Date");
			}

			if (!HRMSHelper.validateDateFormate(request.getEndDate())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Start Date");
			}

			if (HRMSHelper.isNullOrEmpty(request.getCycleId()) || HRMSHelper.isLongZero(request.getCycleId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cycle Id");
			}

			if (HRMSHelper.isNullOrEmpty(request.getYearId()) || HRMSHelper.isLongZero(request.getYearId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Year Id");
			}

			if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getCycleId()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cycle Id");
			}

			if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getYearId()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Year Id");
			}

		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}

	}

	// Added by akanksha for Add org KPI

	public void addOrgKpiValidations(OrgnizationalKpiVo request) throws HRMSException {

		if (!HRMSHelper.isNullOrEmpty(request)) {

			if (!HRMSHelper.isNullOrEmpty(request.getYear())) {

				if (HRMSHelper.isNullOrEmpty(request.getYear().getId())
						|| HRMSHelper.isLongZero(request.getYear().getId())) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Kpi Year Id");
				}
				if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getYear().getId()))) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Kpi Year Id");
				}

			} else {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Kpi Year");
			}

			if (!HRMSHelper.isNullOrEmpty(request.getKpiType())) {

				if (HRMSHelper.isNullOrEmpty(request.getKpiType().getId())
						|| HRMSHelper.isLongZero(request.getKpiType().getId())) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Kpi Type Id");
				}
				if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getKpiType().getId()))) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Kpi Type Id");
				}

				if (HRMSHelper.isNullOrEmpty(request.getKpiType().getKpiType())) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " KPI Type Name");
				}

				if (!HRMSHelper.regexMatcher(request.getKpiType().getKpiType(), "^[a-zA-Z\\s ]{1,2000}$")) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " KPI Type Name");
				}

			} else {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Kpi Type");
			}

			if (request.getKpiType().getKpiType().equalsIgnoreCase(IHRMSConstants.NON_GENERIC_KPI)) {

				if (!HRMSHelper.isNullOrEmpty(request.getDepartment())) {

					for (DepartmentVO dept : request.getDepartment()) {
					if (HRMSHelper.isNullOrEmpty(dept.getId())
							|| HRMSHelper.isLongZero(dept.getId())) {
						throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Department Id");
					}
					if (!HRMSHelper.validateNumberFormat(String.valueOf(dept.getId()))) {
						throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Department Id");
					}
					}
				}

				if (!HRMSHelper.isNullOrEmpty(request.getGrades())) {

					for (GradeMasterVo grade : request.getGrades()) {

						if (HRMSHelper.isNullOrEmpty(grade.getId())
								|| HRMSHelper.isLongZero(grade.getId())) {
							throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Grade");
						}
						if (!HRMSHelper.validateNumberFormat(String.valueOf(grade.getId()))) {
							throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Grade");
						}
					}

				} else {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Grades");
				}

			}

			if (HRMSHelper.isNullOrEmpty(request.getObjective())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Orgnizational Objective");
			}

			if (HRMSHelper.isNullOrEmpty(request.getObjectiveWeight())
					|| HRMSHelper.isDoubleZero(request.getObjectiveWeight())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Objective Weight");
			}

			if (!HRMSHelper.regexMatcher(request.getObjectiveWeight().toString(), "^[0-9.]{1,12}$")) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Objective Weight");
			}

			if (!HRMSHelper.isNullOrEmpty(request.getMetric())) {

				if (HRMSHelper.isNullOrEmpty(request.getMetric().getId())
						|| HRMSHelper.isLongZero(request.getMetric().getId())) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Metric Id");
				}
				if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getMetric().getId()))) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Metric Id");
				}

			} else {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Metric");
			}

		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}

	}

	public void updateOrgKpiValidations(OrgnizationalKpiVo request) throws HRMSException {

		if (!HRMSHelper.isNullOrEmpty(request)) {
			if (HRMSHelper.isNullOrEmpty(request.getId()) || HRMSHelper.isLongZero(request.getId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id");
			}

			if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getId()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id");
			}

		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}

	}
	
	public void deleteOrgKpiValidations(OrgKpiDeleteVo request) throws HRMSException {

		if (!HRMSHelper.isNullOrEmpty(request)) {
			if (HRMSHelper.isNullOrEmpty(request.getId()) || HRMSHelper.isLongZero(request.getId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id");
			}

			if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getId()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Id");
			}

		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}

	}
	
	public void acceptKpiByTeamMemberValidations(RejectRequestVo request) throws HRMSException {

		if (!HRMSHelper.isNullOrEmpty(request)) {
			if (HRMSHelper.isNullOrEmpty(request.getKraId()) || HRMSHelper.isLongZero(request.getKraId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Kra Id");
			}

			if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getKraId()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Kra Id");
			}
			
			if (HRMSHelper.isNullOrEmpty(request.getCycleId()) || HRMSHelper.isLongZero(request.getCycleId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cycle Id");
			}

			if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getKraId()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cycle Id");
			}

		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}

	}

	public void validateHcdCorrectionRequest(HcdCorrcetionRequest request) throws HRMSException {
		if (!HRMSHelper.isNullOrEmpty(request.getHcdCorrection())) {

			for (HcdCorrectionRequestVo hcdcorrection : request.getHcdCorrection()) {

				if (HRMSHelper.isNullOrEmpty(hcdcorrection.getId())
						|| HRMSHelper.isLongZero(hcdcorrection.getId())) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + "id");
				}
//				if (HRMSHelper.isNullOrEmpty(hcdcorrection.getComment())) {
//					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Comment");
//				}
			}

		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " HcdCorrection");
		}
	}
	
	public void validationKpiTimeLine(TimeLineRequestVO request) throws HRMSException {
		if(HRMSHelper.isNullOrEmpty(request.getLabel())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1785));
		}
		if(HRMSHelper.isNullOrEmpty(request.getDate())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1786));
		}
		if (!HRMSHelper.validateDateFormate(request.getDate())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) +  "Date");
		}
		if (!HRMSHelper.isValidCurrentYear(request)) {
			throw new HRMSException(1768, ResponseCode.getResponseCodeMap().get(1793));
		}
		if(HRMSHelper.isNullOrEmpty(request.getKraCycleId())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1787));
		}
		if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getKraCycleId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) +  "Kra Cycle Id");
		}
		if(HRMSHelper.isNullOrEmpty(request.getYearId())) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1788));
		}
		if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getYearId()))) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) +  " Year Id");
		}
	}	
	
	public void submitDelegationValidations(DelegationRequestVo request) throws HRMSException {

		if (!HRMSHelper.isNullOrEmpty(request)) {
			if (HRMSHelper.isNullOrEmpty(request.getAssigneeId()) || HRMSHelper.isLongZero(request.getAssigneeId())) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Assignee");
			}

			if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getAssigneeId()))) {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Assignee");
			}
			
			if (!HRMSHelper.isNullOrEmpty(request.getAssignFor())) {
				for(EmpListVo emp:request.getAssignFor()) {
				if (HRMSHelper.isNullOrEmpty(emp.getEmpId()) || HRMSHelper.isLongZero(emp.getEmpId())) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Employee");
				}

				if (!HRMSHelper.validateNumberFormat(String.valueOf(emp.getEmpId()))) {
					throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Employee");
				}
				}
				
			}else {
				throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Employee List");
			}

		} else {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}

	}
	
	
	public static void validateActivateCycleRequest(KraCycleRequestVo request) throws HRMSException {

	    if (HRMSHelper.isNullOrEmpty(request)) {
	        throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
	    }

	    if (HRMSHelper.isNullOrEmpty(request.getCycleId())) {
	        throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1787));
	    }

	    if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getCycleId()))) {
	        throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Cycle ID");
	    }

	    if (HRMSHelper.isNullOrEmpty(request.getYearId())) {
	        throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1788));
	    }

	    if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getYearId()))) {
	        throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501) + " Year ID");
	    }
	}

	public static void validateSaveKraCycleRequest(KraCycleRequestVo request) throws HRMSException, ParseException {

	    if (HRMSHelper.isNullOrEmpty(request)) {
	        throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
	    }

	    if (HRMSHelper.isNullOrEmpty(request.getCycleName())) {
	        throw new HRMSException(1500, "Cycle Name is mandatory.");
	    }

	    if (HRMSHelper.isNullOrEmpty(request.getDescription())) {
	        throw new HRMSException(1500, "Description is mandatory.");
	    }

	    if (HRMSHelper.isNullOrEmpty(request.getStartDate())) {
	        throw new HRMSException(1500, "Start Date is mandatory.");
	    }

	    if (HRMSHelper.isNullOrEmpty(request.getEndDate())) {
	        throw new HRMSException(1500, "End Date is mandatory.");
	    }
	    
	    Date startDate = HRMSDateUtil.parse(request.getStartDate(), IHRMSConstants.FRONT_END_DATE_FORMAT);
		Date endDate = HRMSDateUtil.parse(request.getEndDate(), IHRMSConstants.FRONT_END_DATE_FORMAT);

		if (startDate.after(endDate)) {
		    throw new HRMSException(1886, "Start Date cannot be after End Date.");
		}

	    if (HRMSHelper.isNullOrEmpty(request.getYearId())) {
	        throw new HRMSException(1500, "Year is mandatory.");
	    }

	    if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getYearId()))) {
	        throw new HRMSException(1500, "Invalid format for Year ID.");
	    }

	    if (HRMSHelper.isNullOrEmpty(request.getCycleTypeId())) {
	        throw new HRMSException(1500, "Cycle Type ID is mandatory.");
	    }

	    if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getCycleTypeId()))) {
	        throw new HRMSException(1500, "Invalid format for Cycle Type ID.");
	    }
	}
	
	public static void validateHodToDepartmentMapRequest(HodToDepartmentMapVO request) throws HRMSException {

		
	    if (request.getEmployeeId() == null) {
	        throw new HRMSException(1500, "Employee is mandatory.");
	    }
	    if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getEmployeeId()))) {
	        throw new HRMSException(1500, "Invalid format for Employee ID.");
	    }

	    if (request.getDepartmentId() == null) {
	        throw new HRMSException(1500, "Department is mandatory.");
	    }
	    if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getDepartmentId()))) {
	        throw new HRMSException(1500, "Invalid format for Department ID.");
	    }

	    if (request.getBranchId() == null) {
	        throw new HRMSException(1500, "Branch is mandatory.");
	    }
	    if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getBranchId()))) {
	        throw new HRMSException(1500, "Invalid format for Branch ID.");
	    }

	    if (request.getDivisionId() == null) {
	        throw new HRMSException(1500, "Function is mandatory.");
	    }
	    if (!HRMSHelper.validateNumberFormat(String.valueOf(request.getDivisionId()))) {
	        throw new HRMSException(1500, "Invalid format for Function ID.");
	    }
	}


}
