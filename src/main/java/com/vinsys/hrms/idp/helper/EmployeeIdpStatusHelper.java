package com.vinsys.hrms.idp.helper;

import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.idp.dao.IMapEmployeeIdpStatusDAO;
import com.vinsys.hrms.idp.entity.MapEmployeeIdpStatus;
import com.vinsys.hrms.idp.entity.MapEmployeeIdpStatusHistory;
import com.vinsys.hrms.idp.vo.employeeidpstatus.*;
import com.vinsys.hrms.security.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeIdpStatusHelper {

    @Autowired
    private IdpEntityHelper entityHelper;

    @Autowired
    IMapEmployeeIdpStatusDAO iMapEmployeeIdpStatusDAO;

    // ------------------------------------------------------------
    // Convert REQUEST → Main ENTITY
    // ------------------------------------------------------------
    public List<MapEmployeeIdpStatus> toEntity(final EmployeeIdpStatusRequestVO request) {
        List<MapEmployeeIdpStatus> entities = new ArrayList<>();
        Long loggedInEmpId = SecurityFilter.TL_CLAIMS.get().getEmployeeId();
        for (Long empId : request.getEmployeeIds()) {
            Employee employee = entityHelper.getActiveEmployee(empId);
            MapEmployeeIdpStatus entity = MapEmployeeIdpStatus.builder()
                    .employee(employee)
                    .idpSubmissionStatus(request.getIdpSubmissionStatus())
                    .recordStatus(request.getRecordStatus())
                    .updatedOn(Instant.now())
                    .updatedBy(String.valueOf(loggedInEmpId))
                    .build();
            entities.add(entity);
        }
        return entities;
    }

    // ------------------------------------------------------------
    // Convert ENTITY → RESPONSE
    // ------------------------------------------------------------
    public List<EmployeeIdpStatusResponseVO> toResponse(final List<MapEmployeeIdpStatus> entities) {
        return entities.stream()
                .map(entity -> EmployeeIdpStatusResponseVO.builder()
                        .employeeId(entity.getEmployee().getId())
                        .status("CREATED")
                        .build()
                )
                .collect(Collectors.toList());
    }

    public EmployeeIdpStatusResponseVO toUpdateResponse(final MapEmployeeIdpStatus entity) {
        return EmployeeIdpStatusResponseVO.builder()
                .employeeId(entity.getEmployee().getId())
                .status("UPDATED")
                .build();
    }

    public EmployeeIdpStatusFullResponseVO getIdResponse(final MapEmployeeIdpStatus entity) {
        return EmployeeIdpStatusFullResponseVO.builder()
                .id(entity.getId())
                .employeeId(entity.getEmployee().getId())
                .idpSubmissionStatus(entity.getIdpSubmissionStatus())
                .recordStatus(entity.getRecordStatus())
                .updatedOn(entity.getUpdatedOn())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    // ------------------------------------------------------------
    // Convert REQUEST → History ENTITY
    // ------------------------------------------------------------
    public List<MapEmployeeIdpStatusHistory> toHistoryEntities(final List<MapEmployeeIdpStatus> parents) {
        List<MapEmployeeIdpStatusHistory> historyList = new ArrayList<>();
        for (MapEmployeeIdpStatus parent : parents) {

            MapEmployeeIdpStatusHistory history = MapEmployeeIdpStatusHistory.builder()
                    .employee(parent.getEmployee())
                    .idpSubmissionStatus(parent.getIdpSubmissionStatus())
                    .employeeIdpStatus(parent)
                    .recordStatus(parent.getRecordStatus())
                    .updatedOn(parent.getUpdatedOn())
                    .updatedBy(parent.getUpdatedBy())
                    .build();

            historyList.add(history);
        }
        return historyList;
    }

    public MapEmployeeIdpStatusHistory toHistoryEntity(final MapEmployeeIdpStatus parent) {
        return MapEmployeeIdpStatusHistory.builder()
                .employee(parent.getEmployee())
                .idpSubmissionStatus(parent.getIdpSubmissionStatus())
                .employeeIdpStatus(parent)
                .recordStatus(parent.getRecordStatus())
                .updatedOn(parent.getUpdatedOn())
                .updatedBy(parent.getUpdatedBy())
                .build();
    }
}