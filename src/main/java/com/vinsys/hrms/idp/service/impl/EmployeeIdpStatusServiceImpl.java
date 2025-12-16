package com.vinsys.hrms.idp.service.impl;

import com.vinsys.hrms.constants.EResponse;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeReportingManager;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.dao.IIdpOrganizationEmployeeViewDAO;
import com.vinsys.hrms.idp.dao.IMapEmployeeIdpStatusDAO;
import com.vinsys.hrms.idp.dao.IMapEmployeeIdpStatusHistoryDAO;
import com.vinsys.hrms.idp.entity.IdpOrganizationEmployeeView;
import com.vinsys.hrms.idp.entity.MapEmployeeIdpStatus;
import com.vinsys.hrms.idp.entity.MapEmployeeIdpStatusHistory;
import com.vinsys.hrms.idp.helper.EmployeeIdpStatusHelper;
import com.vinsys.hrms.idp.service.IEmployeeIdpStatusService;
import com.vinsys.hrms.idp.vo.employeeidpstatus.EmployeeIdpStatusFullResponseVO;
import com.vinsys.hrms.idp.vo.employeeidpstatus.EmployeeIdpStatusRequestVO;
import com.vinsys.hrms.idp.vo.employeeidpstatus.EmployeeIdpStatusResponseVO;
import com.vinsys.hrms.idp.vo.employeeidpstatus.EmployeeIdpStatusVO;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.util.HRMSDateUtil;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeIdpStatusServiceImpl implements IEmployeeIdpStatusService {

    @Autowired
    IMapEmployeeIdpStatusDAO iMapEmployeeIdpStatusDAO;

    @Autowired
    IMapEmployeeIdpStatusHistoryDAO iMapEmployeeIdpStatusHistoryDAO;

    @Autowired
    IIdpOrganizationEmployeeViewDAO idpOrganizationEmployeeViewDAO;

    @Autowired
    EmployeeIdpStatusHelper employeeIdpStatusHelper;

    @Autowired
    IHRMSEmployeeDAO employeeDAO;

    @Autowired
    IHRMSEmployeeReportingManager reportingDAO;

    @Value("${app_version}")
    private String applicationVersion;

    // ==========================================================
    // CREATE
    // ==========================================================
    @Override
    @Transactional
    public List<EmployeeIdpStatusResponseVO> createIdpStatus(EmployeeIdpStatusRequestVO request) {

        // Convert request → entity list
        List<MapEmployeeIdpStatus> incomingList = employeeIdpStatusHelper.toEntity(request);

        List<MapEmployeeIdpStatus> finalList = new ArrayList<>();

        for (MapEmployeeIdpStatus incoming : incomingList) {

            Long empId = incoming.getEmployee().getId();

            // Check if record exists for this employee
            MapEmployeeIdpStatus existing =
                    iMapEmployeeIdpStatusDAO.findByEmployeeId(empId).orElse(null);

            if (existing != null) {
                // Update existing record
                updateExistingStatus(existing, incoming);
                finalList.add(existing);
            } else {
                // Create new record
                incoming.setUpdatedOn(Instant.now());
                finalList.add(incoming);
            }
        }

        // Save final list (Spring performs insert/update accordingly)
        List<MapEmployeeIdpStatus> saved = iMapEmployeeIdpStatusDAO.saveAll(finalList);

        // Save history
        List<MapEmployeeIdpStatusHistory> history =
                employeeIdpStatusHelper.toHistoryEntities(saved);
        iMapEmployeeIdpStatusHistoryDAO.saveAll(history);

        return employeeIdpStatusHelper.toResponse(saved);
    }

    private void updateExistingStatus(MapEmployeeIdpStatus existing, MapEmployeeIdpStatus incoming) {
        existing.setIdpSubmissionStatus(incoming.getIdpSubmissionStatus());
        existing.setRecordStatus(incoming.getRecordStatus());
        existing.setUpdatedOn(Instant.now());
        existing.setUpdatedBy(incoming.getUpdatedBy());
    }

    // ==========================================================
    // GET BY ID
    // ==========================================================
    @Override
    @Transactional(readOnly = true)
    public EmployeeIdpStatusFullResponseVO getIdpStatus(Long id) {
        log.info("Fetching IDP status with ID: {}", id);

        MapEmployeeIdpStatus entity = iMapEmployeeIdpStatusDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("IDP status not found with ID: " + id));

        return employeeIdpStatusHelper.getIdResponse(entity);
    }

    // ==========================================================
    // GET BY EMPLOYEE ID
    // ==========================================================
    @Override
    @Transactional(readOnly = true)
    public EmployeeIdpStatusFullResponseVO getByEmployeeId(Long employeeId) {
        log.info("Fetching IDP status for employee ID: {}", employeeId);

        MapEmployeeIdpStatus entity = iMapEmployeeIdpStatusDAO.findByEmployeeId(employeeId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "IDP status not found for employee ID: " + employeeId));

        return employeeIdpStatusHelper.getIdResponse(entity);
    }

    // ==========================================================
    // GET ALL
    // ==========================================================
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeIdpStatusFullResponseVO> getAllIdpStatuses() {
        log.info("Fetching all employee IDP statuses");

        List<MapEmployeeIdpStatus> entities = iMapEmployeeIdpStatusDAO.findAll();

        log.info("Found {} employee IDP status records", entities.size());

        return entities.stream()
                .map(employeeIdpStatusHelper::getIdResponse)
                .collect(Collectors.toList());
    }

    @Override
    public HRMSBaseResponse<List<EmployeeIdpStatusVO>> getOrganizationEmployeesForIdp(Long branchId, String branch,
                                                                                String keyword, Long gradeId, Long deptId, Pageable pageable) throws HRMSException {

        log.info("Inside getOrganizationEmployeesForIdp method");
        HRMSBaseResponse<List<EmployeeIdpStatusVO>> response = new HRMSBaseResponse<>();

        Long empId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
        if (HRMSHelper.isLongZero(empId)) {
            throw new HRMSException(EResponse.INAVALID_DATA.getCode(), EResponse.INAVALID_DATA.getMessage());
        }
        // Fetch employees from view with pagination
        Page<IdpOrganizationEmployeeView> employeePage;

        if (!HRMSHelper.isNullOrEmpty(branchId)) {
            employeePage = idpOrganizationEmployeeViewDAO.findByBranchId(branchId, pageable);
        } else if (!HRMSHelper.isNullOrEmpty(branch)) {
            employeePage = idpOrganizationEmployeeViewDAO.findByBranchName(branch, pageable);
        } else if (!HRMSHelper.isNullOrEmpty(keyword)) {
            String keywordU = keyword.replaceAll(" ", "%");
            employeePage = idpOrganizationEmployeeViewDAO.findByKeyword(keywordU, pageable);
        } else if (!HRMSHelper.isNullOrEmpty(gradeId)) {
            employeePage = idpOrganizationEmployeeViewDAO.findByGradeId(gradeId, pageable);
        } else if (!HRMSHelper.isNullOrEmpty(deptId)) {
            employeePage = idpOrganizationEmployeeViewDAO.findByDepartmentId(deptId, pageable);
        } else {
            employeePage = idpOrganizationEmployeeViewDAO.findAll(pageable);
        }

        // Convert view entities to VO
        List<EmployeeIdpStatusVO> employeeIdpStatusVOList = new ArrayList<>();

        for (IdpOrganizationEmployeeView viewEntity : employeePage.getContent()) {
            EmployeeIdpStatusVO vo = new EmployeeIdpStatusVO();
            vo.setId(viewEntity.getEmployeeId());
            vo.setOfficialEmailId(viewEntity.getOfficialEmailId());
            vo.setFirstName(viewEntity.getEmpFirstName());
            vo.setLastName(viewEntity.getEmpLastName());
            vo.setMiddleName(viewEntity.getEmpMiddleName());
            vo.setBranch(viewEntity.getBranchName());
            vo.setBranchId(viewEntity.getBranchId());
            vo.setDepartment(viewEntity.getDepartmentName());
            vo.setDepartmentId(viewEntity.getDepartmentId());
            vo.setDivision(viewEntity.getDivisionName());
            vo.setDivisionId(viewEntity.getDivisionId());
            vo.setDesignation(viewEntity.getDesignationName());
            vo.setContactNo(viewEntity.getContactNo() != null ? viewEntity.getContactNo() : 0L);
            vo.setOfficialContactNo(viewEntity.getOfficialMobileNumber());
            vo.setDateOfBirth(HRMSDateUtil.format(viewEntity.getDateOfBirth(), IHRMSConstants.FRONT_END_DATE_FORMAT));
            vo.setEmployeeCode(viewEntity.getEmployeeCode());
            vo.setName(viewEntity.getFullEmployeeName());
            vo.setReportingManager(viewEntity.getReportingManagerName());
            vo.setGrade(viewEntity.getGrade());
            vo.setIdpStatus(viewEntity.getIdpSubmissionStatus());

            employeeIdpStatusVOList.add(vo);
        }

        if (HRMSHelper.isNullOrEmpty(employeeIdpStatusVOList)) {
            throw new HRMSException(EResponse.ERROR_INSUFFICIENT_DATA.getCode(),
                    EResponse.ERROR_INSUFFICIENT_DATA.getMessage());
        }

        response.setResponseBody(employeeIdpStatusVOList);
        response.setApplicationVersion(applicationVersion);
        response.setTotalRecord(employeePage.getTotalElements());
        response.setResponseCode(EResponse.SUCCESS.getCode());
        response.setResponseMessage(EResponse.SUCCESS.getMessage());
        log.info("Exit getOrganizationEmployeesForIdp method");

        return response;
    }
}