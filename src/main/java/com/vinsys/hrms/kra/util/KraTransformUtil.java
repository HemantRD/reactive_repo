package com.vinsys.hrms.kra.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSProfessionalDetailsDAO;
import com.vinsys.hrms.entity.CandidateProfessionalDetail;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.kra.dao.IHodCycleFinalRatingDao;
import com.vinsys.hrms.kra.dao.IHrToHodMapDao;
import com.vinsys.hrms.kra.dao.IKraCycleDAO;
import com.vinsys.hrms.kra.dao.IKraDao;
import com.vinsys.hrms.kra.dao.IKraWfDao;
import com.vinsys.hrms.kra.dao.IKraYearDao;
import com.vinsys.hrms.kra.dao.KraStatusMappingDao;
import com.vinsys.hrms.kra.entity.HodCycleFinalRating;
import com.vinsys.hrms.kra.entity.HrToHodMap;
import com.vinsys.hrms.kra.entity.KpiTimeline;
import com.vinsys.hrms.kra.entity.Kra;
import com.vinsys.hrms.kra.entity.KraCycle;
import com.vinsys.hrms.kra.entity.KraDetails;
import com.vinsys.hrms.kra.entity.KraDetailsLite;
import com.vinsys.hrms.kra.entity.KraStatusMapping;
import com.vinsys.hrms.kra.entity.KraWf;
import com.vinsys.hrms.kra.entity.KraYear;
import com.vinsys.hrms.kra.vo.DashboardTimelineVo;
import com.vinsys.hrms.kra.vo.KraDetailsResponseVO;
import com.vinsys.hrms.kra.vo.KraDetailsVO;
import com.vinsys.hrms.kra.vo.KraResponseVO;
import com.vinsys.hrms.kra.vo.SubCategoryRequestVo;
import com.vinsys.hrms.kra.vo.SubCategoryVo;
import com.vinsys.hrms.kra.vo.TimeLineRequestVO;
import com.vinsys.hrms.master.entity.Category;
import com.vinsys.hrms.master.entity.Subcategory;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.util.ERole;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

@Service
public class KraTransformUtil {

	@Value("${rm.kra.action.enable}")
	private String rmKraActionEnable;

	@Value("${emp.kra.action.enable}")
	private String empKraActionEnable;

	@Autowired
	IKraCycleDAO kraCycleDAO;

	@Autowired
	KraStatusMappingDao statusMappingDao;

	@Autowired
	IHodCycleFinalRatingDao hodcycleDao;

	@Autowired
	IHRMSEmployeeDAO employeeDAO;

	@Autowired
	IKraDao kraDao;

	@Autowired
	IKraWfDao kraWfDao;

	@Autowired
	IHodCycleFinalRatingDao hodCycleFinalRatingDao;

	@Autowired
	IHRMSProfessionalDetailsDAO professionalDetailsDao;

	@Autowired
	IHrToHodMapDao hrtohodMapDao;

	@Autowired
	IKraYearDao kraYearDao;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public List<KraResponseVO> convertToKraResponseVO(List<Kra> kraList, String roleName) {
		log.info("Inside convertToKraResponseVO method");
		List<KraResponseVO> kraResponseVOList = new ArrayList<KraResponseVO>();
		for (Kra kra : kraList) {
			KraResponseVO kraResponseVO = new KraResponseVO();
			KraWf krafWf = kraWfDao.findByKraAndIsActive(kra, ERecordStatus.Y.name());
			kraResponseVO.setId(kra.getId());
			kraResponseVO.setCycleId(kra.getCycleId().getId());
			kraResponseVO.setCycleName(kra.getKraWf().getCycleId().getCycleName());
			kraResponseVO.setEmpName(kra.getEmployee().getCandidate().getFirstName() + " "
					+ kra.getEmployee().getCandidate().getLastName());
			CandidateProfessionalDetail profssiondetails = professionalDetailsDao
					.findBycandidate(kra.getEmployee().getCandidate());
			kraResponseVO.setGrade(profssiondetails.getGrade());
			//kraResponseVO.setHodRating(kra.getHodRating());
			//kraResponseVO.setHrRating(kra.getHrRating());
			kraResponseVO.setKraYear(kra.getKraYear().getYear());
			//kraResponseVO.setRmRating(kra.getRmRating());
			kraResponseVO.setSelfRating(kra.getTotalSelfRating());
			kraResponseVO.setManagerRating(kra.getFinalRating());
			kraResponseVO.setIsHrCalibrated(kra.getIshrcalibrate());
			// kraResponseVO.setIsfinalcalibrate(kra.getIsfinalcalibrate());
			if ((ERole.HOD.name().equalsIgnoreCase(roleName))) {
				kraResponseVO.setPendingWith(kra.getKraWf().getPendingWith());
			}
			if ((EKraStatus.MANAGER.name().equalsIgnoreCase(roleName))
					|| (ERole.HOD.name().equalsIgnoreCase(roleName))) {

				HodCycleFinalRating hodCycle = hodCycleFinalRatingDao.findByIsActiveAndKra(ERecordStatus.Y.name(), kra);

				kraResponseVO.setCorrectionComment(
						(krafWf.getComment() != null && !krafWf.getComment().isEmpty()) ? krafWf.getComment() : null);

				if (!HRMSHelper.isNullOrEmpty(hodCycle) && hodCycle.getComment() != null
						&& !hodCycle.getComment().isEmpty()) {
					kraResponseVO.setCalibrationComment(hodCycle.getComment());
				} else {
					kraResponseVO.setCalibrationComment(null); // Explicitly set to null if no valid comment
				}

				kraResponseVO.setHcdCorrectionComment(
						(krafWf.getHcdCorrection() != null && !krafWf.getHcdCorrection().isEmpty())
								? krafWf.getHcdCorrection()
								: null);
			}

			KraCycle kraCycle = kraCycleDAO.getById(kra.getCycleId().getId());
			KraYear year = kraYearDao.getById(kraCycle.getYear().getId());
			long currentYear = Long.parseLong(year.getYear().trim()); // Trim to remove unwanted spaces
			long lastYear = currentYear - 1;
			String yearname = Long.toString(lastYear);
			KraYear lastyear = kraYearDao.findByYearAndIsActive(yearname, ERecordStatus.Y.name());
			if (HRMSHelper.isNullOrEmpty(lastyear)) {
				kraResponseVO.setLastYearRating(null);
			} else {

				List<KraCycle> lastCyclelist = kraCycleDAO.findByYearAndIsActive(lastyear, ERecordStatus.Y.name());
				for (KraCycle lastCycle : lastCyclelist) {
					if (lastCycle.getCycleName().equalsIgnoreCase(IHRMSConstants.YEAR_END_CYCLE)) {
						Employee employee = employeeDAO.findByIsActiveAndId(ERecordStatus.Y.name(),
								kra.getEmployeeId());
						HodCycleFinalRating lastYearrating = hodcycleDao.findByEmployeeAndCycleId(employee, lastCycle);
						if (lastYearrating != null && lastYearrating.getScore() != null) {
							kraResponseVO.setLastYearRating(lastYearrating.getScore().getLabel());
						} else {
							Kra lastKra = kraDao.findByEmpidAndCycleId(kra.getEmployeeId(), lastCycle.getId());

							if (lastKra != null && lastKra.getFinalRating() != null) {
								kraResponseVO.setLastYearRating(lastKra.getFinalRating());
							}

						}
					}
				}
			}
			if (ERole.EMPLOYEE.name().equalsIgnoreCase(roleName)
					&& !EKraStatus.COMPLETED.name().equals(krafWf.getStatus())) {
				kraResponseVO.setFinalRating(null);
			} else {
				HodCycleFinalRating hodrating = hodcycleDao.findByIsActiveAndKra(ERecordStatus.Y.name(), kra);
				if (hodrating != null) {
					kraResponseVO.setFinalRating(
							hodrating.getScore() != null ? hodrating.getScore().getLabel() : kra.getFinalRating());
				} else {
					kraResponseVO.setFinalRating(kra.getFinalRating());
				}
			}

			KraStatusMapping status = statusMappingDao.findByStatusAndPendingWithAndRoleNameAndOrgIdAndIsActive(
					kra.getKraWf().getStatus(), kra.getKraWf().getPendingWith(), roleName, kra.getOrgId(),
					IHRMSConstants.isActive);
			if (HRMSHelper.isNullOrEmpty(status)) {
				kraResponseVO.setStatus(kra.getKraWf().getStatus());
			} else {
				kraResponseVO.setStatus(status.getLabel());
			}
			kraResponseVO.setDepartment(kra.getDepartment().getDepartmentName());
			kraResponseVO.setEmpId(kra.getEmployeeId());
			kraResponseVO.setEmployeeCode(kra.getEmployee().getEmployeeCode());
			kraResponseVOList.add(kraResponseVO);
			kraResponseVO.setRmKraActionEnable(rmKraActionEnable);
			kraResponseVO.setEmpKraActionEnable(empKraActionEnable);
		}
		log.info("Exit fromF convertToKraResponseVO method");
		return kraResponseVOList;
	}

	private void setKraStatus(String roleName, Kra kra, KraResponseVO kraResponseVO) {
		if (roleName.equals(ERole.EMPLOYEE.name())
				&& kra.getKraWf().getStatus().equalsIgnoreCase(EKraStatus.INCOMPLETE.name())
				|| roleName.equals(ERole.MANAGER.name())
						&& kra.getKraWf().getStatus().equalsIgnoreCase(EKraStatus.INPROCESS.name())
				|| roleName.equals(ERole.HOD.name())
						&& kra.getKraWf().getStatus().equalsIgnoreCase(EKraStatus.APPROVED.name())
				|| roleName.equals(ERole.HR.name())
						&& kra.getKraWf().getStatus().equalsIgnoreCase(EKraStatus.APPROVED.name())
				|| roleName.equals(ERole.MANAGER.name())
						&& kra.getKraWf().getStatus().equalsIgnoreCase(EKraStatus.REJECTED.name())) {
			kraResponseVO.setStatus(EKraStatus.PENDING.name());
		} else if (kra.getKraWf().getStatus().equalsIgnoreCase(EKraStatus.COMPLETED.name())) {
			kraResponseVO.setStatus(EKraStatus.COMPLETED.name());
		} else {
			kraResponseVO.setStatus(EKraStatus.SUBMITTED.name());
		}
	}

	public KraDetailsResponseVO convertToKraDetailsResponseVO(List<KraDetails> kraDetailsList) {
		log.info("Inside convertToKraDetailsResponseVO method");
		KraDetailsResponseVO kraDetailsResponseVO = new KraDetailsResponseVO();
		Float totalWeightage = 0F;
		List<KraDetailsVO> kraDetailsVOList = new ArrayList<KraDetailsVO>();
		for (KraDetails kraDetails : kraDetailsList) {
			KraDetailsVO kraDetailsVO = new KraDetailsVO();
			kraDetailsVO.setAchievementPlan(kraDetails.getAchievementPlan());
			kraDetailsVO.setDescription(kraDetails.getDescription());
			kraDetailsVO.setId(kraDetails.getId());
			kraDetailsVO.setKraDetails(kraDetails.getKraDetails());
			kraDetailsVO.setMeasurementCriteria(kraDetails.getMeasurementCriteria());
			kraDetailsVO.setRmComment(kraDetails.getRmComment());
			kraDetailsVO.setWeightage(kraDetails.getWeightage());
			kraDetailsVO.setYear(kraDetails.getYear());
			kraDetailsVO.setCategoryId(kraDetails.getCategoryId());
			kraDetailsVO.setSubcategoryId(kraDetails.getSubcategoryId());
			kraDetailsVO.setSelfRating(kraDetails.getSelfRating().getValue().toString());
			kraDetailsVO.setManagerRating(kraDetails.getManagerRating().getValue().toString());
			kraDetailsVO.setObjectiveWeightage(kraDetails.getObjectiveWeightage());
			kraDetailsVO.setSelfQaulitativeAssisment(kraDetails.getSelfQaulitativeAssisment());
			kraDetailsVO.setManagerQaulitativeAssisment(kraDetails.getManagerQaulitativeAssisment());
			kraDetailsVOList.add(kraDetailsVO);

			totalWeightage = totalWeightage + kraDetails.getWeightage();
		}
		kraDetailsResponseVO.setKraDetailsVOList(kraDetailsVOList);
		kraDetailsResponseVO.setTotalWeightage(totalWeightage);
		kraDetailsResponseVO.setEmpKraActionEnable(empKraActionEnable);
		kraDetailsResponseVO.setRmKraActionEnable(rmKraActionEnable);
		log.info("Exit from convertToKraDetailsResponseVO method");
		return kraDetailsResponseVO;
	}

	public List<KraResponseVO> convertToKraResponseVOforHR(List<KraDetailsLite> kraDetailsLiteList, String roleName,
			Long loggedInEmpId) {

		log.info("Inside convertToKraResponseVO method");
		List<KraResponseVO> kraResponseVOList = new ArrayList<KraResponseVO>();

		for (KraDetailsLite kra : kraDetailsLiteList) {
			List<HrToHodMap> emp = hrtohodMapDao.findByEmpIdAndIsActive(kra.getEmployeeId(), IHRMSConstants.isActive);

			List<HrToHodMap> hr = hrtohodMapDao.findByHrIdAndIsActive(loggedInEmpId, IHRMSConstants.isActive);
			KraResponseVO kraResponseVO = new KraResponseVO();
			if (!HRMSHelper.isNullOrEmpty(emp) && HRMSHelper.isNullOrEmpty(hr)) {
				kraResponseVO.setFinalRating(null);
				kraResponseVO.setManagerRating(null);
				kraResponseVO.setSelfRating(null);

			} else {
				kraResponseVO.setManagerRating(kra.getRmFinalRating());
				kraResponseVO.setSelfRating(kra.getTotalSelfRating());
				if (kra.getCalibratedRating() != null) {
					kraResponseVO.setFinalRating(kra.getCalibratedRating());
				} else {
					kraResponseVO.setFinalRating(kra.getRmFinalRating());
				}
			}

			kraResponseVO.setId(kra.getKraId());
			Optional<KraCycle> cycle = kraCycleDAO.findById(kra.getCycle_id());

			kraResponseVO.setCycleName(cycle.isPresent() ? cycle.get().getCycleName() : null);

			kraResponseVO.setCycleId(kra.getCycle_id());
			kraResponseVO.setEmpName(kra.getFirstName() + " " + kra.getLastName());
			//kraResponseVO.setHodRating(kra.getHodRating());
			//kraResponseVO.setHrRating(kra.getHrRating());

			kraResponseVO.setKraYear(kra.getYear());
			//kraResponseVO.setRmRating(kra.getRmRating());
			kraResponseVO.setGrade(kra.getGrade());
			kraResponseVO.setIsHrCalibrated(kra.getIsHrCalibrated());

			// Long lastCycleId = kraCycleDAO.findKraCycleById(kra.getCycle_id());
			// Long lastCycleId = validateLastCycleId(kra.getCycle_id());
			KraCycle kraCycle = kraCycleDAO.getById(kra.getCycle_id());
			KraYear year = kraYearDao.getById(kraCycle.getYear().getId());
			long currentYear = Long.parseLong(year.getYear().trim()); // Trim to remove unwanted spaces
			long lastYear = currentYear - 1;
			String yearname = Long.toString(lastYear);
			KraYear lastyear = kraYearDao.findByYearAndIsActive(yearname, ERecordStatus.Y.name());
			if (HRMSHelper.isNullOrEmpty(lastyear)) {
				kraResponseVO.setLastYearRating(null);
			} else {

				List<KraCycle> lastCyclelist = kraCycleDAO.findByYearAndIsActive(lastyear, ERecordStatus.Y.name());
				for (KraCycle lastCycle : lastCyclelist) {
					if (lastCycle.getCycleName().equalsIgnoreCase(IHRMSConstants.YEAR_END_CYCLE)) {
						Employee employee = employeeDAO.findByIsActiveAndId(ERecordStatus.Y.name(),
								kra.getEmployeeId());
						HodCycleFinalRating lastYearrating = hodcycleDao.findByEmployeeAndCycleId(employee, lastCycle);
						if (lastYearrating != null && lastYearrating.getScore() != null) {
							kraResponseVO.setLastYearRating(lastYearrating.getScore().getLabel());
						} else {
							Kra lastKra = kraDao.findByEmpidAndCycleId(kra.getEmployeeId(), lastCycle.getId());

							if (lastKra != null && lastKra.getFinalRating() != null) {
								kraResponseVO.setLastYearRating(lastKra.getFinalRating());
							}

						}
					}
				}
			}
			KraStatusMapping status = statusMappingDao.findByStatusAndPendingWithAndRoleNameAndOrgIdAndIsActive(
					kra.getStatus(), kra.getPendingWith(), roleName, SecurityFilter.TL_CLAIMS.get().getOrgId(),
					IHRMSConstants.isActive);
			if (!HRMSHelper.isNullOrEmpty(status)) {
				kraResponseVO.setStatus(status.getLabel());
			} else {
				kraResponseVO.setStatus(kra.getStatus());
			}

			kraResponseVO.setDepartment(kra.getDepartmentName());
			kraResponseVO.setEmpId(kra.getEmployeeId());
			Employee employeecode = employeeDAO.findByIsActiveAndId(ERecordStatus.Y.name(), kra.getEmployeeId());
			kraResponseVO
					.setEmployeeCode(employeecode.getEmployeeCode() != null ? employeecode.getEmployeeCode() : null);
			kraResponseVO.setCountryName(kra.getCountryName());
			kraResponseVOList.add(kraResponseVO);
		}
		log.info("Exit fromF convertToKraResponseVO method");
		return kraResponseVOList;

	}

	public String setKraReportStatus(KraDetailsLite kraDetail) {
		String status = null;
		if (!HRMSHelper.isNullOrEmpty(kraDetail)) {

			KraStatusMapping statusMapping = statusMappingDao.findByStatusAndPendingWithAndRoleNameAndOrgIdAndIsActive(
					kraDetail.getStatus(), kraDetail.getPendingWith(), ERole.HR.name(),
					SecurityFilter.TL_CLAIMS.get().getOrgId(), IHRMSConstants.isActive);
			if (!HRMSHelper.isNullOrEmpty(statusMapping)) {
				status = statusMapping.getLabel();
			} else {
				status = kraDetail.getStatus();
			}
		}
		return status;

	}

	public String setKraReportStatus(Kra kraDetail) {
		String status = null;
		if (!HRMSHelper.isNullOrEmpty(kraDetail)) {

			KraStatusMapping statusMapping = statusMappingDao.findByStatusAndPendingWithAndRoleNameAndOrgIdAndIsActive(
					kraDetail.getKraWf().getStatus(), kraDetail.getKraWf().getPendingWith(), ERole.HOD.name(),
					SecurityFilter.TL_CLAIMS.get().getOrgId(), IHRMSConstants.isActive);
			if (!HRMSHelper.isNullOrEmpty(statusMapping)) {
				status = statusMapping.getLabel();
			} else {
				status = kraDetail.getKraWf().getStatus();
			}
		}
		return status;

	}

	public String getCalibratedRating(Kra kraDetail) {
		String rating = null;
		if (!HRMSHelper.isNullOrEmpty(kraDetail)) {

			HodCycleFinalRating hodCycle = hodCycleFinalRatingDao.findByIsActiveAndKra(IHRMSConstants.isActive,
					kraDetail);
			if (!HRMSHelper.isNullOrEmpty(hodCycle)) {
				rating = hodCycle.getScore().getLabel();
			}
//			else {
//				rating = " ";
//			}
		}
		return rating;

	}
//*************

	public List<DashboardTimelineVo> convertToTimelineVo(List<KpiTimeline> kraList) {
		log.info("Inside convertToTimelineVO method");
		List<DashboardTimelineVo> kraResponseVOList = new ArrayList<DashboardTimelineVo>();
		for (KpiTimeline kra : kraList) {
			DashboardTimelineVo timelineVo = new DashboardTimelineVo();

			timelineVo.setColor(kra.getColor());
			timelineVo.setDate(HRMSDateUtil.format(kra.getDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			timelineVo.setLabel(kra.getLabel());
			kraResponseVOList.add(timelineVo);

		}
		log.info("Exit fromF convertToTimelineVO method");
		return kraResponseVOList;
	}

	public String setCycleName(Long cycleId) {
		String status = null;
		if (!HRMSHelper.isNullOrEmpty(cycleId)) {

			KraCycle cycle = kraCycleDAO.findById(cycleId).get();

			if (!HRMSHelper.isNullOrEmpty(cycle)) {
				status = cycle.getCycleName();
			}
		}
		return status;

	}

	public String setCheckRatingAccess(Long kraId) throws HRMSException {
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
		String status = null;

		Kra empkra = kraDao.findByIdAndIsActive(kraId, IHRMSConstants.isActive);
		if (!HRMSHelper.isNullOrEmpty(empkra)) {
			List<HrToHodMap> emp = hrtohodMapDao.findByEmpIdAndIsActive(empkra.getEmployeeId(),
					IHRMSConstants.isActive);

			List<HrToHodMap> hr = hrtohodMapDao.findByHrIdAndIsActive(loggedInEmpId, IHRMSConstants.isActive);

			if (!HRMSHelper.isNullOrEmpty(emp) && HRMSHelper.isNullOrEmpty(hr)
					&& HRMSHelper.isRolePresent(role, ERole.HR.name())) {
				status = IHRMSConstants.True;
			} else {
				status = IHRMSConstants.False;
			}

		}
		return status;
	}

	/*
	 * public static Subcategory transformToSubcategory(SubCategoryRequestVo
	 * request, Employee loggedInEmployee, Category category, Subcategory
	 * subcategory2) {
	 * 
	 * Subcategory subcategory = new Subcategory();
	 * subcategory.setSubCategoryName(request.getName()+" "+request.getStageName());
	 * subcategory.setCategory(category);
	 * subcategory.setStageId(request.getStageId());
	 * subcategory.setOrgId(Long.toString(loggedInEmployee.getOrgId()));
	 * subcategory.setIsActive(ERecordStatus.Y.name());
	 * 
	 * return subcategory;
	 * 
	 * }
	 */
	
	public static Subcategory transformToSubcategory(SubCategoryRequestVo request, Employee loggedInEmployee,
			Category category, Subcategory subcategory) {
		if (subcategory == null) {
			subcategory = new Subcategory();
			subcategory.setIsActive(ERecordStatus.Y.name());
			subcategory.setOrgId(SecurityFilter.TL_CLAIMS.get().getOrgId());
		}

		if (request.getSubCategoryname() != null) {
			if (request.getStageName() != null && !IHRMSConstants.NO_STAGE.equalsIgnoreCase(request.getStageName().trim())) {

				subcategory.setSubCategoryName(request.getSubCategoryname().trim() + " " + request.getStageName().trim());
			} else {

				subcategory.setSubCategoryName(request.getSubCategoryname().trim());
			}
		}

		if (category != null) {
			subcategory.setCategory(category);
		}

		if (request.getStageId() != null) {
			subcategory.setStageId(request.getStageId());
		}

		subcategory.setIsActive(ERecordStatus.Y.name());
		subcategory.setOrgId(SecurityFilter.TL_CLAIMS.get().getOrgId());

		return subcategory;
	}

	public static boolean isOjectiveWeightageValid(List<KraDetails> kraDetailsList) {
		if (HRMSHelper.isNullOrEmpty(kraDetailsList)) {
			return false;
		}
		float totalWeight = 0.0f;
		for (KraDetails kra : kraDetailsList) {
			totalWeight += kra.getObjectiveWeightage();
		}
		
		totalWeight = Math.round(totalWeight * 100.0f) / 100.0f;
//		long roundedWeight = Math.round(totalWeight);
//		if (roundedWeight > 100) {
//			return false;
//		}
		if (totalWeight > 100.00) {
			return false;
		}

//		return roundedWeight == 100;
		return totalWeight == 100.00;
	}

	public TimeLineRequestVO convertDashboardTimelineVo(KpiTimeline kra) {
		log.info("Inside convertToTimelineVO method");
		TimeLineRequestVO timelineVo = new TimeLineRequestVO();
		timelineVo.setId(kra.getId());
		timelineVo.setColor(kra.getColor());
		timelineVo.setDate(HRMSDateUtil.format(kra.getDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
		timelineVo.setLabel(kra.getLabel());
		timelineVo.setKraCycleId(kra.getCycleId());
		timelineVo.setYearId(kra.getYearId());
		log.info("Exit fromF convertToTimelineVO method");
		return timelineVo;
	}
	
	 public static KpiTimeline transformToKpiTimeline(TimeLineRequestVO request, Employee loggedInEmployee, KpiTimeline timeLine) {

	        timeLine.setLabel(request.getLabel());
	        timeLine.setDate(HRMSDateUtil.parse(request.getDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
	        timeLine.setCycleId(request.getKraCycleId());
	        timeLine.setYearId(request.getYearId());
	        timeLine.setColor(IHRMSConstants.KPI_TIMELINE_DISPLAY_STATUS);
	        timeLine.setOrgId(loggedInEmployee.getOrgId());
	        timeLine.setIsActive(IHRMSConstants.isActive);     
	        return timeLine;
	}
	 
	public List<TimeLineRequestVO> convertToTimeline(List<KpiTimeline> kpiTimeList) {
		log.info("Inside convertToTimeline method");
		List<TimeLineRequestVO> ResponseVOList = new ArrayList<>();
		for (KpiTimeline timeLine : kpiTimeList) {
			TimeLineRequestVO timelineVo = new TimeLineRequestVO();

			timelineVo.setId(timeLine.getId());
			timelineVo.setColor(timeLine.getColor());
			timelineVo.setDate(HRMSDateUtil.format(timeLine.getDate(), IHRMSConstants.FRONT_END_DATE_FORMAT));
			timelineVo.setLabel(timeLine.getLabel());
			timelineVo.setKraCycleId(timeLine.getCycleId());
			timelineVo.setYearId(timeLine.getYearId());
			Optional<KraCycle> kraCycle = kraCycleDAO.findById(timeLine.getCycleId());
			timelineVo.setKraCycleName(kraCycle.get().getCycleName());
			timelineVo.setYear(kraCycle.get().getYear().getYear());
			ResponseVOList.add(timelineVo);

		}
		log.info("Exit fromF convertToTimeline method");
		return ResponseVOList;
	}

}
