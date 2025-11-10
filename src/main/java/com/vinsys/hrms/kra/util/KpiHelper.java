package com.vinsys.hrms.kra.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.kra.dao.IKraCycleDAO;
import com.vinsys.hrms.kra.dao.IKraDao;
import com.vinsys.hrms.kra.dao.IKraDetailsDao;
import com.vinsys.hrms.kra.entity.Kra;
import com.vinsys.hrms.kra.entity.KraCycle;
import com.vinsys.hrms.kra.entity.KraCycleCalender;
import com.vinsys.hrms.kra.entity.KraDetails;
import com.vinsys.hrms.kra.entity.KraYear;
import com.vinsys.hrms.kra.service.impl.CycleResult;
import com.vinsys.hrms.kra.vo.DashboardResponseVo;
import com.vinsys.hrms.kra.vo.DonutDashboardResponseVo;
import com.vinsys.hrms.kra.vo.DonutDatum;
import com.vinsys.hrms.kra.vo.NotesRequestVO;
import com.vinsys.hrms.kra.vo.Series;
import com.vinsys.hrms.master.dao.ICategoryDao;
import com.vinsys.hrms.master.dao.IKraRatingDAO;
import com.vinsys.hrms.master.entity.Category;
import com.vinsys.hrms.master.entity.KraRating;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.ResponseCode;

@Service
public class KpiHelper {

	@Autowired
	IHRMSEmployeeDAO employeeDAO;

	@Autowired
	IKraDao kraDAO;

	@Autowired
	IKraDetailsDao kraDetailsDAO;

	@Autowired
	ICategoryDao categoryDAO;

	@Autowired
	IKraCycleDAO cycleDAO;

	@Autowired
	IKraRatingDAO ratingDAO;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public List<DashboardResponseVo> populateDashboardResponseForKpiSubmission(List<CycleResult> list,
			KraCycle kraCycle) {
		log.info("inside populateDashboardResponseForKpiSubmission method...");
		List<DashboardResponseVo> responseList = new ArrayList<>();

		for (CycleResult result : list) {
			DashboardResponseVo response = new DashboardResponseVo();
			response.setEndDate(new java.sql.Date(kraCycle.getEndDate().getTime()));
			response.setTotalCount(Optional.ofNullable(result.getTotalCount()).orElse(BigInteger.ZERO));
			response.setSubmittedCount(Optional.ofNullable(result.getSubmittedCount()).orElse(BigInteger.ZERO));
			response.setPendingCount(Optional.ofNullable(result.getPendingCount()).orElse(BigInteger.ZERO));
			response.setCompletedCount(Optional.ofNullable(result.getCompletedCount()).orElse(BigInteger.ZERO));
			response.setCompletedPercentage(
					Optional.ofNullable(result.getCompletedPercentage()).orElse(BigDecimal.ZERO));
			response.setSubmittedPercentage(
					Optional.ofNullable(result.getSubmittedPercentage()).orElse(BigDecimal.ZERO));
			response.setPendingPercentage(Optional.ofNullable(result.getPendingPercentage()).orElse(BigDecimal.ZERO));
			response.setCycleName(Optional.ofNullable(result.getCycleName()).orElse(IHRMSConstants.NA));
			response.setCycleType(Optional.ofNullable(result.getCycleType()).orElse(BigInteger.ZERO));
			response.setActiveCycleId(Optional.ofNullable(result.getCycleId()).orElse(BigInteger.ZERO));
			response.setYearId(Optional.ofNullable(result.getYearId()).orElse(BigInteger.ZERO));
			response.setCurrentPhase(IHRMSConstants.KPI_SUBMISSION);
			response.setCycleType(Optional.ofNullable(result.getCycleType()).orElse(BigInteger.ZERO));
			responseList.add(response);
		}
		return responseList;
	}

	public List<DashboardResponseVo> populateDashboardResponseForKpiSubmissionEmployee(List<CycleResult> list,
			KraCycle kraCycle) {
		log.info("inside populateDashboardResponseForKpiSubmissionEmployee method...");
		List<DashboardResponseVo> responseList = new ArrayList<>();

		for (CycleResult result : list) {
			DashboardResponseVo response = new DashboardResponseVo();
			response.setEndDate(new java.sql.Date(kraCycle.getEndDate().getTime()));
			response.setTotalCount(Optional.ofNullable(result.getTotalCount()).orElse(BigInteger.ZERO));
			response.setSubmittedCount(Optional.ofNullable(result.getSubmittedCount()).orElse(BigInteger.ZERO));
			response.setPendingCount(Optional.ofNullable(result.getPendingCount()).orElse(BigInteger.ZERO));
			response.setCompletedCount(Optional.ofNullable(result.getCompletedCount()).orElse(BigInteger.ZERO));
			response.setCompletedPercentage(
					Optional.ofNullable(result.getCompletedPercentage()).orElse(BigDecimal.ZERO));
			response.setSubmittedPercentage(
					Optional.ofNullable(result.getSubmittedPercentage()).orElse(BigDecimal.ZERO));
			response.setPendingPercentage(Optional.ofNullable(result.getPendingPercentage()).orElse(BigDecimal.ZERO));
			response.setCycleName(Optional.ofNullable(result.getCycleName()).orElse(IHRMSConstants.NA));
			response.setActiveCycleId(Optional.ofNullable(result.getCycleId()).orElse(BigInteger.ZERO));
			response.setYearId(Optional.ofNullable(result.getYearId()).orElse(BigInteger.ZERO));
			response.setCurrentPhase(IHRMSConstants.KPI_SUBMISSION);
			response.setEmployeeStatus(Optional.ofNullable(result.getStatus()).orElse(IHRMSConstants.NA));
			response.setPendingWith(Optional.ofNullable(result.getPendingWith()).orElse(IHRMSConstants.NA));
			responseList.add(response);
		}
		return responseList;
	}

	public List<DashboardResponseVo> populateDashboardResponse(List<KraCycleCalender> calendarList,
			List<CycleResult> list) {
		log.info("inside populateDashboardResponse method...");
		List<DashboardResponseVo> responseList = new ArrayList<>();

		if (calendarList == null || calendarList.isEmpty()) {
			for (CycleResult result : list) {
				DashboardResponseVo response = new DashboardResponseVo();
				response.setEndDate(
						result.getEndDate() != null ? new java.sql.Date(result.getEndDate().getTime()) : null);
				response.setTotalCount(Optional.ofNullable(result.getTotalCount()).orElse(BigInteger.ZERO));
				response.setSubmittedCount(Optional.ofNullable(result.getSubmittedCount()).orElse(BigInteger.ZERO));
				response.setPendingCount(Optional.ofNullable(result.getPendingCount()).orElse(BigInteger.ZERO));
				response.setCompletedCount(Optional.ofNullable(result.getCompletedCount()).orElse(BigInteger.ZERO));
				response.setCompletedPercentage(
						Optional.ofNullable(result.getCompletedPercentage()).orElse(BigDecimal.ZERO));
				response.setSubmittedPercentage(
						Optional.ofNullable(result.getSubmittedPercentage()).orElse(BigDecimal.ZERO));
				response.setPendingPercentage(
						Optional.ofNullable(result.getPendingPercentage()).orElse(BigDecimal.ZERO));
				response.setCycleName(Optional.ofNullable(result.getCycleName()).orElse(IHRMSConstants.NA));
				response.setActiveCycleId(Optional.ofNullable(result.getCycleId()).orElse(BigInteger.ZERO));
				response.setYearId(Optional.ofNullable(result.getYearId()).orElse(BigInteger.ZERO));
				response.setCurrentPhase(IHRMSConstants.NA);
				response.setCycleType(Optional.ofNullable(result.getCycleType()).orElse(BigInteger.ZERO));
				responseList.add(response);
			}
		} else {
			for (KraCycleCalender calendar : calendarList) {
				for (CycleResult result : list) {
					DashboardResponseVo response = new DashboardResponseVo();
					response.setEndDate(
							calendar.getEndDate() != null ? new java.sql.Date(calendar.getEndDate().getTime()) : null);
					response.setTotalCount(Optional.ofNullable(result.getTotalCount()).orElse(BigInteger.ZERO));
					response.setSubmittedCount(Optional.ofNullable(result.getSubmittedCount()).orElse(BigInteger.ZERO));
					response.setPendingCount(Optional.ofNullable(result.getPendingCount()).orElse(BigInteger.ZERO));
					response.setCompletedCount(Optional.ofNullable(result.getCompletedCount()).orElse(BigInteger.ZERO));
					response.setCompletedPercentage(
							Optional.ofNullable(result.getCompletedPercentage()).orElse(BigDecimal.ZERO));
					response.setSubmittedPercentage(
							Optional.ofNullable(result.getSubmittedPercentage()).orElse(BigDecimal.ZERO));
					response.setPendingPercentage(
							Optional.ofNullable(result.getPendingPercentage()).orElse(BigDecimal.ZERO));
					response.setCycleName(Optional.ofNullable(result.getCycleName()).orElse(IHRMSConstants.NA));
					response.setActiveCycleId(Optional.ofNullable(result.getCycleId()).orElse(BigInteger.ZERO));
					response.setYearId(Optional.ofNullable(result.getYearId()).orElse(BigInteger.ZERO));
					response.setCurrentPhase(Optional.ofNullable(calendar.getCurrentPhase()).orElse(IHRMSConstants.NA));
					response.setCycleType(Optional.ofNullable(result.getCycleType()).orElse(BigInteger.ZERO));

					responseList.add(response);
				}
			}
		}
		return responseList;
	}

	public List<DashboardResponseVo> populateDashboardResponseForEmployee(List<KraCycleCalender> calendarList,
			List<CycleResult> list) {
		log.info("inside populateDashboardResponseForEmployee method...");
		List<DashboardResponseVo> responseList = new ArrayList<>();

		if (calendarList == null || calendarList.isEmpty()) {
			for (CycleResult result : list) {
				DashboardResponseVo response = new DashboardResponseVo();
				response.setEndDate(
						result.getEndDate() != null ? new java.sql.Date(result.getEndDate().getTime()) : null);
				response.setTotalCount(Optional.ofNullable(result.getTotalCount()).orElse(BigInteger.ZERO));
				response.setSubmittedCount(Optional.ofNullable(result.getSubmittedCount()).orElse(BigInteger.ZERO));
				response.setPendingCount(Optional.ofNullable(result.getPendingCount()).orElse(BigInteger.ZERO));
				response.setCompletedCount(Optional.ofNullable(result.getCompletedCount()).orElse(BigInteger.ZERO));
				response.setCompletedPercentage(
						Optional.ofNullable(result.getCompletedPercentage()).orElse(BigDecimal.ZERO));
				response.setSubmittedPercentage(
						Optional.ofNullable(result.getSubmittedPercentage()).orElse(BigDecimal.ZERO));
				response.setPendingPercentage(
						Optional.ofNullable(result.getPendingPercentage()).orElse(BigDecimal.ZERO));
				response.setCycleName(Optional.ofNullable(result.getCycleName()).orElse(IHRMSConstants.NA));
				response.setActiveCycleId(Optional.ofNullable(result.getCycleId()).orElse(BigInteger.ZERO));
				response.setCycleType(Optional.ofNullable(result.getCycleType()).orElse(BigInteger.ZERO));
				response.setYearId(Optional.ofNullable(result.getYearId()).orElse(BigInteger.ZERO));
				response.setCurrentPhase(IHRMSConstants.NA);
				response.setEmployeeStatus(Optional.ofNullable(result.getStatus()).orElse(IHRMSConstants.NA));
				response.setPendingWith(Optional.ofNullable(result.getPendingWith()).orElse(IHRMSConstants.NA));

				responseList.add(response);
			}
		} else {
			for (KraCycleCalender calendar : calendarList) {
				for (CycleResult result : list) {
					DashboardResponseVo response = new DashboardResponseVo();
					response.setEndDate(
							calendar.getEndDate() != null ? new java.sql.Date(calendar.getEndDate().getTime()) : null);
					response.setTotalCount(Optional.ofNullable(result.getTotalCount()).orElse(BigInteger.ZERO));
					response.setSubmittedCount(Optional.ofNullable(result.getSubmittedCount()).orElse(BigInteger.ZERO));
					response.setPendingCount(Optional.ofNullable(result.getPendingCount()).orElse(BigInteger.ZERO));
					response.setCompletedCount(Optional.ofNullable(result.getCompletedCount()).orElse(BigInteger.ZERO));
					response.setCompletedPercentage(
							Optional.ofNullable(result.getCompletedPercentage()).orElse(BigDecimal.ZERO));
					response.setSubmittedPercentage(
							Optional.ofNullable(result.getSubmittedPercentage()).orElse(BigDecimal.ZERO));
					response.setPendingPercentage(
							Optional.ofNullable(result.getPendingPercentage()).orElse(BigDecimal.ZERO));
					response.setCycleName(Optional.ofNullable(result.getCycleName()).orElse(IHRMSConstants.NA));
					response.setActiveCycleId(Optional.ofNullable(result.getCycleId()).orElse(BigInteger.ZERO));
					response.setYearId(Optional.ofNullable(result.getYearId()).orElse(BigInteger.ZERO));
					response.setCycleType(Optional.ofNullable(result.getCycleType()).orElse(BigInteger.ZERO));
					response.setCurrentPhase(Optional.ofNullable(calendar.getCurrentPhase()).orElse(IHRMSConstants.NA));
					response.setEmployeeStatus(Optional.ofNullable(result.getStatus()).orElse(IHRMSConstants.NA));
					response.setPendingWith(Optional.ofNullable(result.getPendingWith()).orElse(IHRMSConstants.NA));

					responseList.add(response);
				}
			}
		}
		return responseList;
	}

	public void setFinalKpiObjectives(Kra kra, KraCycle cycle) {
		validateInput(kra, cycle);

		Employee employee = kra.getEmployee();
		KraYear kraYear = kra.getKraYear();

		Category functionSpecificCategory = getFunctionSpecificCategory();

		List<Kra> allKras = getAllKrasForEmployee(employee, kraYear);
		Kra kpiSubmissionKra = getKpiSubmissionKra(allKras);

		List<KraDetails> sourceObjectives = getFunctionSpecificObjectives(kpiSubmissionKra, functionSpecificCategory);
		List<Kra> targetKras = getOtherKras(allKras, kpiSubmissionKra.getId());

		setObjectivesToOtherKras(sourceObjectives, targetKras, functionSpecificCategory);
	}

	private void validateInput(Kra kra, KraCycle cycle) {
		if (kra == null || cycle == null) {
			throw new IllegalArgumentException("KRA or Cycle is null.");
		}
		if (kra.getEmployee() == null || kra.getKraYear() == null) {
			throw new RuntimeException("Employee or Year is missing in KRA.");
		}
	}

	private Category getFunctionSpecificCategory() {
		Category category = categoryDAO.findByCategoryNameAndIsActive(IHRMSConstants.FUNCTION_SPECIFIC_CATEGORY,
				ERecordStatus.Y.name());

		if (category == null) {
			throw new RuntimeException("Category 'Function specific' not found or invalid.");
		}
		return category;
	}

	private List<Kra> getAllKrasForEmployee(Employee employee, KraYear year) {
		List<Kra> kras = kraDAO.findByEmployeeAndKraYearAndIsActive(employee, year, ERecordStatus.Y.name());
		if (kras == null || kras.isEmpty()) {
			throw new RuntimeException("No KRAs found for employee in the active year.");
		}
		return kras;
	}

	private Kra getKpiSubmissionKra(List<Kra> kras) {
		return kras.stream()
				.filter(k -> k.getCycleId() != null
						&& IHRMSConstants.KPI_SUBMISSION_TYPE_ID.equals(k.getCycleId().getCycleTypeId()))
				.findFirst().orElseThrow(() -> new RuntimeException("KPI Submission KRA not found."));
	}

	private List<KraDetails> getFunctionSpecificObjectives(Kra kpiKra, Category category) {
		List<KraDetails> objectives = kraDetailsDAO.findByKraAndIsActiveAndCategoryIdOrderByIdAsc(kpiKra,
				ERecordStatus.Y.name(), category.getId());

		if (objectives == null || objectives.isEmpty()) {
			throw new RuntimeException("No Function specific objectives found in KPI Submission.");
		}
		return objectives;
	}

	private List<Kra> getOtherKras(List<Kra> allKras, Long excludeId) {
		return allKras.stream().filter(k -> !k.getId().equals(excludeId)).filter(k -> k.getCycleId() != null)
				.collect(Collectors.toList());
	}

	private void setObjectivesToOtherKras(List<KraDetails> sourceObjectives, List<Kra> targetKras, Category category) {

		for (Kra targetKra : targetKras) {
			for (KraDetails source : sourceObjectives) {
				if (source == null || source.getKraDetails() == null)
					continue;

				boolean exists = kraDetailsDAO.existsByKraAndKraDetailsAndCategoryIdAndIsActive(targetKra,
						source.getKraDetails(), category.getId(), ERecordStatus.Y.name());

				if (!exists) {
					KraDetails newObjective = new KraDetails();
					newObjective.setKra(targetKra);
					newObjective.setKraCycle(targetKra.getCycleId());
					newObjective.setCategoryId(category.getId());
					newObjective.setYear(source.getYear());
					newObjective.setSubcategoryId(source.getSubcategoryId());
					newObjective.setKraDetails(source.getKraDetails());
					newObjective.setObjectiveWeightage(source.getObjectiveWeightage());
					newObjective.setDescription(source.getDescription());
					newObjective.setWeightage(source.getWeightage());
					newObjective.setMeasurementCriteria(source.getMeasurementCriteria());
					newObjective.setIsActive(ERecordStatus.Y.name());
					newObjective.setCreatedBy(source.getCreatedBy());
					newObjective
							.setCreatedDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

					kraDetailsDAO.save(newObjective);
				}
			}
		}
	}

	public List<DonutDashboardResponseVo> populateDonutData(List<CycleResult> hrResults,
			List<KraCycleCalender> filteredCalendarList, KraCycle kraCycle) {
		log.info("inside populateDashboardResponse method...");
		List<DonutDashboardResponseVo> responseList = new ArrayList<>();

		for (CycleResult result : hrResults) {
			DonutDashboardResponseVo response = new DonutDashboardResponseVo();
			response.setPendingWithEmployeePercentage(
					Optional.ofNullable(result.getPendingWithEmployeePercentage()).orElse(BigDecimal.ZERO));
			response.setPendingWithHODPercentage(
					Optional.ofNullable(result.getPendingWithHodPercentage()).orElse(BigDecimal.ZERO));
			response.setPendingWithHRPercentage(
					Optional.ofNullable(result.getPendingWithHrPercentage()).orElse(BigDecimal.ZERO));
			response.setPendingWithLineManagerPercentage(
					Optional.ofNullable(result.getPendingWithLineManagerPercentage()).orElse(BigDecimal.ZERO));
			response.setSubmittedByLineManagerPercentage(
					Optional.ofNullable(result.getSubmittedByLineManagerPercentage()).orElse(BigDecimal.ZERO));
			response.setSubmittedByHODPercentage(
					Optional.ofNullable(result.getSubmittedHodPercentage()).orElse(BigDecimal.ZERO));
			response.setSubmittedByHRPercentage(
					Optional.ofNullable(result.getSubmittedHrPercentage()).orElse(BigDecimal.ZERO));
			response.setSubmittedByEmployeePercentage(
					Optional.ofNullable(result.getSubmittedEmployeePercentage()).orElse(BigDecimal.ZERO));
			responseList.add(response);
		}
		return responseList;
	}

	/*
	 * public List<DonutDatum> getDonutDataFromVo(List<DonutDashboardResponseVo>
	 * donutDataList) { List<DonutDatum> donutDatumList = new ArrayList<>();
	 * 
	 * DonutDatum hrDatum = new DonutDatum(); hrDatum.setLabel(IHRMSConstants.HR);
	 * List<Series> hrSeriesList = new ArrayList<>(); for (DonutDashboardResponseVo
	 * vo : donutDataList) { Series series = new Series();
	 * series.setPending(vo.getPendingWithHRPercentage());
	 * series.setSubmitted(vo.getSubmittedByHRPercentage());
	 * hrSeriesList.add(series); } hrDatum.setSeries(hrSeriesList);
	 * donutDatumList.add(hrDatum);
	 * 
	 * DonutDatum managerDatum = new DonutDatum();
	 * managerDatum.setLabel(IHRMSConstants.MANAGER); List<Series> managerSeriesList
	 * = new ArrayList<>(); for (DonutDashboardResponseVo vo : donutDataList) {
	 * Series series = new Series();
	 * series.setPending(vo.getPendingWithLineManagerPercentage());
	 * series.setSubmitted(vo.getSubmittedByLineManagerPercentage());
	 * managerSeriesList.add(series); } managerDatum.setSeries(managerSeriesList);
	 * donutDatumList.add(managerDatum);
	 * 
	 * DonutDatum hodDatum = new DonutDatum();
	 * hodDatum.setLabel(IHRMSConstants.HOD); List<Series> hodSeriesList = new
	 * ArrayList<>(); for (DonutDashboardResponseVo vo : donutDataList) { Series
	 * series = new Series(); series.setPending(vo.getPendingWithHODPercentage());
	 * series.setSubmitted(vo.getSubmittedByHODPercentage());
	 * hodSeriesList.add(series); } hodDatum.setSeries(hodSeriesList);
	 * donutDatumList.add(hodDatum);
	 * 
	 * DonutDatum empDatum = new DonutDatum();
	 * empDatum.setLabel(IHRMSConstants.EMPLOYEE); List<Series> empSeriesList = new
	 * ArrayList<>(); for (DonutDashboardResponseVo vo : donutDataList) { Series
	 * series = new Series();
	 * series.setPending(vo.getPendingWithEmployeePercentage());
	 * series.setSubmitted(vo.getSubmittedByEmployeePercentage());
	 * empSeriesList.add(series); } empDatum.setSeries(empSeriesList);
	 * donutDatumList.add(empDatum);
	 * 
	 * return donutDatumList; }
	 */

	public List<DonutDatum> getDonutDataFromVo(List<DonutDashboardResponseVo> donutDataList, Long requestCycle,
			String role) {
		List<DonutDatum> donutDatumList = new ArrayList<>();

		if (IHRMSConstants.EMPLOYEE.equalsIgnoreCase(role)) {
			DonutDatum empDatum = new DonutDatum();
			empDatum.setLabel(IHRMSConstants.TEAM_MEMBER);
			List<Series> empSeriesList = donutDataList.stream().map(vo -> buildSeries(vo.getEmployeePercentage(), null))
					.collect(Collectors.toList());
			empDatum.setSeries(empSeriesList);
			donutDatumList.add(empDatum);
			return donutDatumList;
		}

		// HR
		DonutDatum hrDatum = new DonutDatum();
		hrDatum.setLabel(IHRMSConstants.HCD);
		List<Series> hrSeriesList = donutDataList.stream()
				.map(vo -> buildSeries(vo.getSubmittedByHRPercentage(), vo.getPendingWithHRPercentage()))
				.collect(Collectors.toList());
		hrDatum.setSeries(hrSeriesList);
		donutDatumList.add(hrDatum);

		// Manager
		DonutDatum managerDatum = new DonutDatum();
		managerDatum.setLabel(IHRMSConstants.LINE_MANAGER);
		List<Series> managerSeriesList = donutDataList.stream().map(
				vo -> buildSeries(vo.getSubmittedByLineManagerPercentage(), vo.getPendingWithLineManagerPercentage()))
				.collect(Collectors.toList());
		managerDatum.setSeries(managerSeriesList);
		donutDatumList.add(managerDatum);

		KraCycle cycle = cycleDAO.findByKraCycle(requestCycle);
		if (cycle != null && StringUtils.isNotBlank(cycle.getCycleName())
				&& !IHRMSConstants.KPI_SUBMISSION_TYPE_ID.equals(cycle.getCycleTypeId())) {

			DonutDatum hodDatum = new DonutDatum();
			hodDatum.setLabel(IHRMSConstants.MC_MEMBER);
			List<Series> hodSeriesList = donutDataList.stream()
					.map(vo -> buildSeries(vo.getSubmittedByHODPercentage(), vo.getPendingWithHODPercentage()))
					.collect(Collectors.toList());
			hodDatum.setSeries(hodSeriesList);
			donutDatumList.add(hodDatum);
		}

		// Employee
		DonutDatum empDatum = new DonutDatum();
		empDatum.setLabel(IHRMSConstants.TEAM_MEMBER);
		List<Series> empSeriesList = donutDataList.stream()
				.map(vo -> buildSeries(vo.getSubmittedByEmployeePercentage(), vo.getPendingWithEmployeePercentage()))
				.collect(Collectors.toList());
		empDatum.setSeries(empSeriesList);
		donutDatumList.add(empDatum);

		return donutDatumList;
	}

	private Series buildSeries(BigDecimal submittedPercent, BigDecimal pendingPercent) {
		double submitted = submittedPercent != null ? submittedPercent.doubleValue() : -1.0;
		double pending = pendingPercent != null ? pendingPercent.doubleValue() : -1.0;

		if (submitted >= 0.0 && pending >= 0.0) {
			double total = submitted + pending;
			if (total > 100.0) {
				submitted = (submitted / total) * 100;
				pending = (pending / total) * 100;
			}
		} else if (submitted >= 0.0) {
			pending = 100.0 - submitted;
		} else if (pending >= 0.0) {
			submitted = 100.0 - pending;
		} else {
			submitted = 0.0;
			pending = 0.0;
		}

		Series series = new Series();
		series.setSubmitted(BigDecimal.valueOf(submitted).setScale(2, RoundingMode.HALF_UP));
		series.setPending(BigDecimal.valueOf(pending).setScale(2, RoundingMode.HALF_UP));
		return series;
	}

	public List<DonutDashboardResponseVo> populateDonutDataForEmployee(List<CycleResult> hrResults,
			List<KraCycleCalender> filteredCalendarList, KraCycle kraCycle) {
		log.info("Inside populateDonutDataForEmployee method...");
		List<DonutDashboardResponseVo> responseList = new ArrayList<>();

		for (CycleResult result : hrResults) {
			DonutDashboardResponseVo response = new DonutDashboardResponseVo();
			response.setEmployeePercentage(Optional.ofNullable(result.getEmployeePercentage()).orElse(BigDecimal.ZERO));
			responseList.add(response);
		}

		return responseList;
	}

	public static void validateRequest(NotesRequestVO request) throws HRMSException {
		if (HRMSHelper.isNullOrEmpty(request) || HRMSHelper.isNullOrEmpty(request.getRoleId())
				|| HRMSHelper.isNullOrEmpty(request.getDescription()) || HRMSHelper.isNullOrEmpty(request.getTitle())
				|| HRMSHelper.isNullOrEmpty(request.getScreenId())) {
			throw new HRMSException(1501, ResponseCode.getResponseCodeMap().get(1501));
		}
	}

	/**
	 * DB label -> unified human-friendly label (with numeric prefix) e.g., "3", "3
	 * [Low]", "3 [Mid]", "3 [High]" -> "3: Performing"
	 *//*
		 * public static String translateRatingLabel(String dbLabel) { if (dbLabel ==
		 * null) { return null; } switch (dbLabel.trim()) { // Unsatisfactory case "1":
		 * case "1 [Low]": case "1 [High]": return "Unsatisfactory";
		 * 
		 * // Developing case "2": case "2 [Low]": case "2 [Mid]": case "2 [High]":
		 * return "Developing";
		 * 
		 * // Performing case "3": case "3 [Low]": case "3 [Mid]": case "3 [High]":
		 * return "Performing";
		 * 
		 * // Exceptional case "4": case "4 [Low]": case "4 [High]": return
		 * "Exceptional";
		 * 
		 * default: return dbLabel; // fallback if unknown label } }
		 */
	
	
	/**
	 * DB label -> unified human-friendly label
	 * e.g., "3", "3 [Low]", "3 [Mid]", "3 [High]" -> "On Track"
	 */
	public static String translateRatingLabel(String dbLabel) {
	    if (dbLabel == null) {
	        return null;
	    }

	    switch (dbLabel.trim()) {
	        // 1 & 2 => Need Improvement
	        case "1":
	        case "1 [Low]":
	        case "1 [High]":
	        case "2":
	        case "2 [Low]":
	        case "2 [Mid]":
	        case "2 [High]":
	        case "Fail":
	            return "Need Improvement";

	        // 3 & 4 => On Track
	        case "3":
	        case "3 [Low]":
	        case "3 [Mid]":
	        case "3 [High]":
	        case "4":
	        case "4 [Low]":
	        case "4 [High]":
	        case "Pass":
	            return "On Track";

	        default:
	            return dbLabel; // fallback if unknown label
	    }
	}


	public KraRating getHighestRatingForLabel(String translatedLabel) throws HRMSException {
		List<KraRating> ratings = ratingDAO.findByIsActiveOrderById(ERecordStatus.Y.name());

		KraRating highest = null;
		for (KraRating r : ratings) {
			String normalized = translateRatingLabel(r.getLabel());
			if (normalized.equals(translatedLabel)) {
				if (highest == null || r.getValue() > highest.getValue()) {
					highest = r;
				}
			}
		}

		if (highest == null) {
			throw new HRMSException(1501, "No rating found for label: " + translatedLabel);
		}
		return highest;
	}

}
