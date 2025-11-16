package com.vinsys.hrms.kra.service.impl;

import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.dao.IHRMSMasterDepartmentDAO;
import com.vinsys.hrms.entity.MasterDepartment;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.kra.dao.IHodToDepartmentMap;
import com.vinsys.hrms.kra.dao.IKraAggregateScoresDao;
import com.vinsys.hrms.kra.dao.IKraCycleDAO;
import com.vinsys.hrms.kra.dao.IKraDao;
import com.vinsys.hrms.kra.dao.IKraDetailsDao;
import com.vinsys.hrms.kra.dao.IKraDetailsReportDAO;
import com.vinsys.hrms.kra.dao.KraStatusDao;
import com.vinsys.hrms.kra.entity.HodToDepartmentMap;
import com.vinsys.hrms.kra.entity.Kra;
import com.vinsys.hrms.kra.entity.KraAggregateScores;
import com.vinsys.hrms.kra.entity.KraCycle;
import com.vinsys.hrms.kra.entity.KraCycleStatus;
import com.vinsys.hrms.kra.entity.KraDetailsReport;
import com.vinsys.hrms.kra.service.IKpiPmsAiService;
import com.vinsys.hrms.kra.vo.AiMsBackgroundAnalysisRequestInvokerVO;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.util.ERole;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import com.vinsys.hrms.util.ResponseCode;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class KpiPmsAiServiceImpl implements IKpiPmsAiService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IHodToDepartmentMap hodToDepartmentMap;

    @Autowired
    IKraDetailsReportDAO kraDetailsReportDAO;

    @Autowired
    IKraDetailsDao kraDetailsDao;

    @Autowired
    IKraAggregateScoresDao kraAggregateScoresDao;

    @Autowired
    IHRMSMasterDepartmentDAO departmentDAO;

    @Autowired
    private IKraDao kraDAO;

    @Autowired
    KraStatusDao kraStatusDao;

    @Autowired
    private IKraCycleDAO kraCycleDAO;

//    @Autowired
//    IKraRatingRangeDao rangeDao;

    @Override
    public AiMsBackgroundAnalysisRequestInvokerVO validateCeoRole() throws Exception {
        AiMsBackgroundAnalysisRequestInvokerVO invokerVO = new AiMsBackgroundAnalysisRequestInvokerVO();
        invokerVO.setRoles(SecurityFilter.TL_CLAIMS.get().getRoles());
        invokerVO.setLoggedInEmployeeId(SecurityFilter.TL_CLAIMS.get().getEmployeeId());
        if (!(HRMSHelper.isRolePresent(invokerVO.getRoles(), ERole.ADMIN.name()))) {
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
        }
        return invokerVO;
    }

    @Override
    public AiMsBackgroundAnalysisRequestInvokerVO validateCeoOrHodRole() throws Exception {
        AiMsBackgroundAnalysisRequestInvokerVO invokerVO = new AiMsBackgroundAnalysisRequestInvokerVO();
        invokerVO.setRoles(SecurityFilter.TL_CLAIMS.get().getRoles());
        invokerVO.setLoggedInEmployeeId(SecurityFilter.TL_CLAIMS.get().getEmployeeId());
        if (!(HRMSHelper.isRolePresent(invokerVO.getRoles(), ERole.ADMIN.name()) || HRMSHelper.isRolePresent(invokerVO.getRoles(), ERole.HOD.name()))) {
            throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
        }
        return invokerVO;
    }

    @Async
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void calculateDepartmentLevelAggregations(AiMsBackgroundAnalysisRequestInvokerVO invokerVO, Long deptId) throws Exception {
        try {
            //Fetch active cycle
            KraCycleStatus status = kraStatusDao.findByName(IHRMSConstants.CYCLE_OPEN);
            List<KraCycle> openCycles = kraCycleDAO.findAllByStatus(status);

            if (ObjectUtils.isEmpty(openCycles)) {
                throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1221));
            }

            if (openCycles.size() > 1) {
                throw new HRMSException(1900, ResponseCode.getResponseCodeMap().get(1900));
            }

            KraCycle cycle = openCycles.get(0);
            if (HRMSHelper.isNullOrEmpty(cycle)) {
                throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1221));
            }
            Long cycleId = cycle.getId();

            List<Long> departmentIds = new ArrayList<>();
            //Fetch department details if HOD login
            if (HRMSHelper.isNullOrEmpty(deptId)) {
                if (HRMSHelper.isRolePresent(invokerVO.getRoles(), ERole.HOD.name())) {
                    List<HodToDepartmentMap> departmentList = hodToDepartmentMap.findByEmployeeIdAndIsActive(invokerVO.getLoggedInEmployeeId(), ERecordStatus.Y.name());
                    for (HodToDepartmentMap depId : departmentList) {
                        departmentIds.add(depId.getDepartmentId());
                    }

                    if (HRMSHelper.isNullOrEmpty(departmentIds)) {
                        throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
                    }
                } else {
                    throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
                }
            } else {
                departmentIds.add(deptId);
            }

            for (Long dptId : departmentIds) {
                List<KraDetailsReport> kraList = kraDetailsReportDAO.findByCycleIdAndDepartmentId(cycleId, dptId);
                if (HRMSHelper.isNullOrEmpty(kraList)) {
                    throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1502));
                }
                Double SumOfSelfRating = 0.0, SumOfManagerRating = 0.0;
                int countOfKraWithSelfRating = 0, countOfKraWithManagerRating = 0;
                for (KraDetailsReport kraDetailsReport : kraList) {
                    Kra kra = kraDAO.findByIdAndIsActive(kraDetailsReport.getKraId(), ERecordStatus.Y.name());
                    if (!HRMSHelper.isNullOrEmpty(kra)) {
                        if (HRMSHelper.isNullOrEmpty(kra.getAvgSelfRating())) {
                            Double average = kraDetailsDao.calculateSelfRatingAverage(kra.getId(), IHRMSConstants.isActive, IHRMSConstants.ZERO_VALUE);
                            if (!HRMSHelper.isNullOrEmpty(average)) {
                                Double trimmedAverage = BigDecimal.valueOf(average).setScale(2, RoundingMode.DOWN).doubleValue();
                                kra.setAvgSelfRating(trimmedAverage);
                                kraDAO.save(kra);
                            }
                        }
                        if (!HRMSHelper.isNullOrEmpty(kra.getAvgSelfRating())) {
                            SumOfSelfRating += kra.getAvgSelfRating();
                            countOfKraWithSelfRating++;
                        }

                        if (HRMSHelper.isNullOrEmpty(kra.getAvgRmRating())) {
                            Double average = kraDetailsDao.calculateAverage(kra.getId(), IHRMSConstants.isActive, IHRMSConstants.ZERO_VALUE);
                            if (!HRMSHelper.isNullOrEmpty(average)) {
                                Double trimmedAverage = BigDecimal.valueOf(average).setScale(2, RoundingMode.DOWN).doubleValue();
                                kra.setAvgRmRating(trimmedAverage);
                                kraDAO.save(kra);
                            }
                        }
                        if (!HRMSHelper.isNullOrEmpty(kra.getAvgRmRating())) {
                            SumOfManagerRating += kra.getAvgRmRating();
                            countOfKraWithManagerRating++;
                        }
                    }
                }

                List<KraAggregateScores> deptLevelData;
                deptLevelData = kraAggregateScoresDao.findByKraCycleIdAndLevelOfAggregationAndDepartment_Id(cycleId, "Department", dptId);
                KraAggregateScores deptScore;
                if (HRMSHelper.isNullOrEmpty(deptLevelData)) {
                    //Insert new record here
                    deptScore = new KraAggregateScores();
                    deptScore.setKraCycleId(cycleId);
                    deptScore.setLevelOfAggregation("Department");
                    deptScore.setDepartment(departmentDAO.findByIdAndIsActive(dptId, ERecordStatus.Y.name()));
                } else {
                    //Update existing record here
                    deptScore = deptLevelData.get(0);
                }
                if (countOfKraWithSelfRating > 0)
                    deptScore.setAggregateSelfRating(SumOfSelfRating / countOfKraWithSelfRating);
                if (countOfKraWithManagerRating > 0)
                    deptScore.setAggregateRmRating(SumOfManagerRating / countOfKraWithManagerRating);
                if (countOfKraWithManagerRating > 0)
                    deptScore.setAggregateCalibratedRating(SumOfManagerRating / countOfKraWithManagerRating); // Initially setting calibrated rating same as manager rating - after calibration process this will change
                kraAggregateScoresDao.save(deptScore);
            }

        } catch (Exception e) {
            log.error("Error in calculateDepartmentLevelAggregations: ", e);
            throw e;
        }
    }

    @Async
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void calculateOrganisationLevelAggregations(AiMsBackgroundAnalysisRequestInvokerVO invokerVO) throws Exception {
        try {
            //Fetch active cycle
            KraCycleStatus status = kraStatusDao.findByName(IHRMSConstants.CYCLE_OPEN);
            List<KraCycle> openCycles = kraCycleDAO.findAllByStatus(status);

            if (ObjectUtils.isEmpty(openCycles)) {
                throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1221));
            }

            if (openCycles.size() > 1) {
                throw new HRMSException(1900, ResponseCode.getResponseCodeMap().get(1900));
            }

            KraCycle cycle = openCycles.get(0);
            if (HRMSHelper.isNullOrEmpty(cycle)) {
                throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1221));
            }
            Long cycleId = cycle.getId();

            List<MasterDepartment> departmentList;
            //Fetch all departments details if CEO login
            if (HRMSHelper.isRolePresent(invokerVO.getRoles(), ERole.ADMIN.name())) {
                departmentList = departmentDAO.findByIsActive(ERecordStatus.Y.name());
                if (HRMSHelper.isNullOrEmpty(departmentList)) {
                    throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
                }
            } else {
                throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1501));
            }

            Double SumOfSelfRatingOverall = 0.0, SumOfManagerRatingOverall = 0.0;
            int countOfDepartmentsWithSelfRating = 0, countOfDepartmentsWithManagerRating = 0;

            for (MasterDepartment department : departmentList) {
                List<KraDetailsReport> kraList = kraDetailsReportDAO.findByCycleIdAndDepartmentId(cycleId, department.getId());
                if (HRMSHelper.isNullOrEmpty(kraList)) {
                    throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1502));
                }
                Double SumOfSelfRating = 0.0, SumOfManagerRating = 0.0;
                int countOfKraWithSelfRating = 0, countOfKraWithManagerRating = 0;
                for (KraDetailsReport kraDetailsReport : kraList) {
                    Kra kra = kraDAO.findByIdAndIsActive(kraDetailsReport.getKraId(), ERecordStatus.Y.name());
                    if (!HRMSHelper.isNullOrEmpty(kra)) {
                        if (HRMSHelper.isNullOrEmpty(kra.getAvgSelfRating())) {
                            Double average = kraDetailsDao.calculateSelfRatingAverage(kra.getId(), IHRMSConstants.isActive, IHRMSConstants.ZERO_VALUE);
                            if (!HRMSHelper.isNullOrEmpty(average)) {
                                Double trimmedAverage = BigDecimal.valueOf(average).setScale(2, RoundingMode.DOWN).doubleValue();
                                kra.setAvgSelfRating(trimmedAverage);
                                kraDAO.save(kra);
                            }
                        }
                        if (!HRMSHelper.isNullOrEmpty(kra.getAvgSelfRating())) {
                            SumOfSelfRating += kra.getAvgSelfRating();
                            countOfKraWithSelfRating++;
                        }

                        if (HRMSHelper.isNullOrEmpty(kra.getAvgRmRating())) {
                            Double average = kraDetailsDao.calculateAverage(kra.getId(), IHRMSConstants.isActive, IHRMSConstants.ZERO_VALUE);
                            if (!HRMSHelper.isNullOrEmpty(average)) {
                                Double trimmedAverage = BigDecimal.valueOf(average).setScale(2, RoundingMode.DOWN).doubleValue();
                                kra.setAvgRmRating(trimmedAverage);
                                kraDAO.save(kra);
                            }
                        }
                        if (!HRMSHelper.isNullOrEmpty(kra.getAvgRmRating())) {
                            SumOfManagerRating += kra.getAvgRmRating();
                            countOfKraWithManagerRating++;
                        }
                    }
                }

                List<KraAggregateScores> deptLevelData;
                deptLevelData = kraAggregateScoresDao.findByKraCycleIdAndLevelOfAggregationAndDepartment_Id(cycleId, "Department", department.getId());
                KraAggregateScores deptScore;
                if (HRMSHelper.isNullOrEmpty(deptLevelData)) {
                    //Insert new record here
                    deptScore = new KraAggregateScores();
                    deptScore.setKraCycleId(cycleId);
                    deptScore.setLevelOfAggregation("Department");
                    deptScore.setDepartment(department);
                } else {
                    //Update existing record here
                    deptScore = deptLevelData.get(0);
                }
                if (countOfKraWithSelfRating > 0)
                    deptScore.setAggregateSelfRating(SumOfSelfRating / countOfKraWithSelfRating);
                if (countOfKraWithManagerRating > 0)
                    deptScore.setAggregateRmRating(SumOfManagerRating / countOfKraWithManagerRating);
                if (countOfKraWithManagerRating > 0)
                    deptScore.setAggregateCalibratedRating(SumOfManagerRating / countOfKraWithManagerRating); // Initially setting calibrated rating same as manager rating - after calibration process this will change
                kraAggregateScoresDao.save(deptScore);

                //Calculate organisation level aggregations
                if (countOfKraWithSelfRating > 0) {
                    SumOfSelfRatingOverall += (SumOfSelfRating / countOfKraWithSelfRating);
                    countOfDepartmentsWithSelfRating++;
                }
                if (countOfKraWithManagerRating > 0) {
                    SumOfManagerRatingOverall += (SumOfManagerRating / countOfKraWithManagerRating);
                    countOfDepartmentsWithManagerRating++;
                }
            }

            List<KraAggregateScores> orgLevelData;
            orgLevelData = kraAggregateScoresDao.findByKraCycleIdAndLevelOfAggregation(cycleId, "Organisation");
            KraAggregateScores orgScore;
            if (HRMSHelper.isNullOrEmpty(orgLevelData)) {
                //Insert new record here
                orgScore = new KraAggregateScores();
                orgScore.setKraCycleId(cycleId);
                orgScore.setLevelOfAggregation("Organisation");
            } else {
                //Update existing record here
                orgScore = orgLevelData.get(0);
            }
            if (countOfDepartmentsWithSelfRating > 0)
                orgScore.setAggregateSelfRating(SumOfSelfRatingOverall / countOfDepartmentsWithSelfRating);
            if (countOfDepartmentsWithManagerRating > 0)
                orgScore.setAggregateRmRating(SumOfManagerRatingOverall / countOfDepartmentsWithManagerRating);
            if (countOfDepartmentsWithManagerRating > 0)
                orgScore.setAggregateCalibratedRating(SumOfManagerRatingOverall / countOfDepartmentsWithManagerRating); // Initially setting calibrated rating same as manager rating - after calibration process this will change
            kraAggregateScoresDao.save(orgScore);

        } catch (Exception e) {
            log.error("Error in calculateOrganisationLevelAggregations: ", e);
            throw e;
        }
    }
}
