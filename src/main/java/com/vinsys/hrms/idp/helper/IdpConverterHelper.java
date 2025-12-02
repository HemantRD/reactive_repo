package com.vinsys.hrms.idp.helper;

import com.vinsys.hrms.dao.IEmployeeDesignationDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.entity.Candidate;
import com.vinsys.hrms.entity.Employee;
import com.vinsys.hrms.entity.EmployeeDepartment;
import com.vinsys.hrms.entity.EmployeeDesignation;
import com.vinsys.hrms.idp.dao.IIdpDetailCommentDAO;
import com.vinsys.hrms.idp.dao.IIdpDetailsDAO;
import com.vinsys.hrms.idp.dao.IIdpFlowHistoryDAO;
import com.vinsys.hrms.idp.dao.IIdpProgressHistoryDAO;
import com.vinsys.hrms.idp.entity.Idp;
import com.vinsys.hrms.idp.entity.IdpDetailComment;
import com.vinsys.hrms.idp.entity.IdpDetails;
import com.vinsys.hrms.idp.entity.IdpFlowHistory;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper class for converting between IDP entities and VOs
 */
@Component
public class IdpConverterHelper {

    @Autowired
    private IIdpDetailsDAO idpDetailsDAO;

    @Autowired
    private IIdpFlowHistoryDAO idpFlowDetailDAO;

    @Autowired
    private IIdpProgressHistoryDAO idpProgressDetailDAO;

    @Autowired
    private IIdpDetailCommentDAO idpDetailCommentDAO;

    @Autowired
    private IHRMSEmployeeDAO employeeDAO;

    @Autowired
    private IEmployeeDesignationDAO designationDAO;

    /**
     * Convert list of IDP entities to old ListItemVO format
     */
    public List<com.vinsys.hrms.idp.vo.getidp.IdpListItemVO> convertIdpListToListItemVOs(
            List<Idp> idpList, Long loggedInEmpId, String role,
            com.vinsys.hrms.dao.IHRMSEmployeeDAO employeeDAO,
            com.vinsys.hrms.dao.IEmployeeDesignationDAO designationDAO) {

        return idpList.stream().map(idp -> {
            com.vinsys.hrms.idp.vo.getidp.IdpListItemVO item = new com.vinsys.hrms.idp.vo.getidp.IdpListItemVO();

            // Basic IDP info
            item.setId(idp.getId());
            item.setEmployeeId(idp.getEmployeeId());
            item.setCreatedDate(idp.getCreatedDate());
            item.setStatus(idp.getStatus());
            item.setActiveFlowId(idp.getActiveFlowId());
            item.setFunction(idp.getFunction());
            item.setGrade(idp.getGrade());

            // Fetch employee details
            com.vinsys.hrms.entity.Employee employee = employeeDAO.findActiveEmployeeById(
                    idp.getEmployeeId(), com.vinsys.hrms.constants.ERecordStatus.Y.name());
            if (employee != null && employee.getCandidate() != null) {
                String employeeName = (employee.getCandidate().getFirstName() != null ? employee.getCandidate().getFirstName() : "") + " " +
                                      (employee.getCandidate().getLastName() != null ? employee.getCandidate().getLastName() : "");
                item.setEmployeeName(employeeName.trim());

                // Get designation
                EmployeeDesignation empDesignation = designationDAO.findByEmployee(employee);
                if (empDesignation != null && empDesignation.getDesignation() != null) {
                    item.setDesignation(empDesignation.getDesignation().getDesignationName());
                }

                // Get department
                EmployeeDepartment empDepartment = employee.getEmployeeDepartment();
                if (empDepartment != null && empDepartment.getDepartment() != null) {
                    item.setDepartment(empDepartment.getDepartment().getDepartmentName());
                }
            }

            Employee rm = employee.getEmployeeReportingManager().getReporingManager();
            String managerName= employeeDAO.findActiveEmployeeById(rm.getId(), IHRMSConstants.isActive).getCandidate().getFirstName() + " " +
                    employeeDAO.findActiveEmployeeById(rm.getId(), IHRMSConstants.isActive).getCandidate().getLastName();
            item.setLineManagerName(managerName);

            // Fetch flow history
            List<IdpFlowHistory> flowList = idpFlowDetailDAO.findByIdpId(idp.getId());
            List<com.vinsys.hrms.idp.vo.getidp.IdpFlowDetailVO> flowVOList = flowList.stream()
                    .map(f -> {
                        com.vinsys.hrms.idp.vo.getidp.IdpFlowDetailVO fv = new com.vinsys.hrms.idp.vo.getidp.IdpFlowDetailVO();
//                    org.springframework.beans.BeanUtils.copyProperties(f, fv);
                        fv.setId(f.getId());
                        fv.setOwnerEmployeeId(f.getEmployeeId());
                        fv.setOwnerEmployeeRole(f.getEmployeeRole());
                        fv.setStartDate(f.getStartDate());
                        fv.setAction(f.getActionType());
                        fv.setActionDate(f.getActionDate());
                        fv.setStatus(f.getStatus());
                        fv.setRecordStatus(f.getRecordStatus());

                        Candidate c = employeeDAO.getById(f.getEmployeeId()).getCandidate();
                        fv.setOwnerEmployeeName(c.getFirstName() + " " + c.getLastName());

                        return fv;
                    })
                    .collect(Collectors.toList());
            item.setFlowDetails(flowVOList);

            // Fetch active flow details
            if (idp.getActiveFlowId() != null) {
                IdpFlowHistory activeFlow = idpFlowDetailDAO.findById(idp.getActiveFlowId()).orElse(null);
                if (activeFlow != null) {
                    com.vinsys.hrms.idp.vo.getidp.IdpFlowDetailVO flowVO = new com.vinsys.hrms.idp.vo.getidp.IdpFlowDetailVO();
                    flowVO.setId(activeFlow.getId());
                    flowVO.setOwnerEmployeeId(activeFlow.getEmployeeId());
                    flowVO.setOwnerEmployeeRole(activeFlow.getEmployeeRole());
                    flowVO.setStartDate(activeFlow.getStartDate());
                    flowVO.setAction(activeFlow.getActionType());
                    flowVO.setActionDate(activeFlow.getActionDate());
                    flowVO.setStatus(activeFlow.getStatus());
                    flowVO.setRecordStatus(activeFlow.getRecordStatus());

                    Candidate c = employeeDAO.getById(activeFlow.getEmployeeId()).getCandidate();
                    flowVO.setOwnerEmployeeName(c.getFirstName() + " " + c.getLastName());

                    // Get owner employee name
                    com.vinsys.hrms.entity.Employee ownerEmployee = employeeDAO.findActiveEmployeeById(
                            activeFlow.getEmployeeId(), com.vinsys.hrms.constants.ERecordStatus.Y.name());
                    if (ownerEmployee != null && ownerEmployee.getCandidate() != null) {
                        String ownerName = (ownerEmployee.getCandidate().getFirstName() != null ? ownerEmployee.getCandidate().getFirstName() : "") + " " +
                                          (ownerEmployee.getCandidate().getLastName() != null ? ownerEmployee.getCandidate().getLastName() : "");
                        flowVO.setOwnerEmployeeName(ownerName.trim());
                    }

                    item.setActiveFlowDetails(flowVO);
                }
            }

            // Determine allowed actions based on status and role
            item.setAllowedActions(determineAllowedActions(idp, role, loggedInEmpId));

            return item;
        }).collect(Collectors.toList());
    }

    /**
     * Convert IDP entity to old IdpVO format
     */
    public com.vinsys.hrms.idp.vo.getidp.IdpVO convertToIdpVO(Idp idp, String lineManager,
                              com.vinsys.hrms.dao.IHRMSEmployeeDAO employeeDAO,Employee employee) {
        com.vinsys.hrms.idp.vo.getidp.IdpVO vo = new com.vinsys.hrms.idp.vo.getidp.IdpVO();
        vo.setId(idp.getId());
        vo.setActiveFlowId(idp.getActiveFlowId());
        vo.setEmployeeId(idp.getEmployeeId());
        vo.setEmployeeName(employeeDAO.getById(idp.getEmployeeId()).getCandidate().getFirstName() + " " +
                           employeeDAO.getById(idp.getEmployeeId()).getCandidate().getLastName());
        vo.setStatus(idp.getStatus());
        vo.setCreatedDate(idp.getCreatedDate());
        vo.setFunction(idp.getFunction());
        vo.setGrade(idp.getGrade());
        vo.setLineManagerName(lineManager);
        com.vinsys.hrms.entity.EmployeeDesignation empDesignation = designationDAO.findByEmployee(employee);
        if (empDesignation != null && empDesignation.getDesignation() != null) {
            vo.setDesignation(empDesignation.getDesignation().getDesignationName());
        }

        // Get department
        com.vinsys.hrms.entity.EmployeeDepartment empDepartment = employee.getEmployeeDepartment();
        if (empDepartment != null && empDepartment.getDepartment() != null) {
            vo.setDepartment(empDepartment.getDepartment().getDepartmentName());
        }

        // Fetch IDP details
        List<IdpDetails> idpDetailList = idpDetailsDAO.findAllByIdpId(idp.getId());
        List<com.vinsys.hrms.idp.vo.getidp.IdpDetailVO> idpDetailVOList = idpDetailList.stream()
                .map(detail -> {
                    IdpDetailComment detailComment = getIdpDetailComment(idp, detail);
                    com.vinsys.hrms.idp.vo.getidp.IdpDetailVO detailVO = new com.vinsys.hrms.idp.vo.getidp.IdpDetailVO();
                    detailVO.setId(detail.getId());
                    detailVO.setCompetencyTypeId(detail.getCompetencyTypeId());
                    detailVO.setCompetencySubTypeId(detail.getCompetencySubTypeId());
                    detailVO.setDevGoal(detail.getDevGoals());
                    detailVO.setDevActions(detail.getDevActions());
                    detailVO.setTrainingId(detail.getTrainingId());
                    detailVO.setTrainingName(detail.getTrainingName());
                    if(!HRMSHelper.isNullOrEmpty(detailComment)) {
                        detailVO.setComment(detailComment.getComment());
                    }
                    detailVO.setIdpDetailsComments(idpDetailsDAO.findCommentsWithRole(detail.getId()));
                    return detailVO;
                })
                .collect(Collectors.toList());
        vo.setIdpDetails(idpDetailVOList);

        // Fetch progress details
        List<com.vinsys.hrms.idp.entity.IdpProgressHistory> progressList =
                idpProgressDetailDAO.findByIdpId(idp.getId());
        List<com.vinsys.hrms.idp.vo.getidp.IdpProgressDetailVO> progressVOList = progressList.stream()
                .map(p -> {
                    com.vinsys.hrms.idp.vo.getidp.IdpProgressDetailVO pv = new com.vinsys.hrms.idp.vo.getidp.IdpProgressDetailVO();
                    org.springframework.beans.BeanUtils.copyProperties(p, pv);
                    return pv;
                })
                .collect(Collectors.toList());
        vo.setProgressDetails(progressVOList);

        // Fetch flow history
        List<IdpFlowHistory> flowList = idpFlowDetailDAO.findByIdpId(idp.getId());
        List<com.vinsys.hrms.idp.vo.getidp.IdpFlowDetailVO> flowVOList = flowList.stream()
                .map(f -> {
                    com.vinsys.hrms.idp.vo.getidp.IdpFlowDetailVO fv = new com.vinsys.hrms.idp.vo.getidp.IdpFlowDetailVO();
//                    org.springframework.beans.BeanUtils.copyProperties(f, fv);
                    fv.setId(f.getId());
                    fv.setOwnerEmployeeId(f.getEmployeeId());
                    fv.setOwnerEmployeeRole(f.getEmployeeRole());
                    fv.setStartDate(f.getStartDate());
                    fv.setAction(f.getActionType());
                    fv.setActionDate(f.getActionDate());
                    fv.setStatus(f.getStatus());
                    fv.setRecordStatus(f.getRecordStatus());

                    Candidate c = employeeDAO.getById(f.getEmployeeId()).getCandidate();
                    fv.setOwnerEmployeeName(c.getFirstName() + " " + c.getLastName());

                    return fv;
                })
                .collect(Collectors.toList());
        vo.setFlowDetails(flowVOList);

        if (idp.getActiveFlowId() != null) {
            IdpFlowHistory activeFlow = idpFlowDetailDAO.findById(idp.getActiveFlowId()).orElse(null);
            if (activeFlow != null) {
                com.vinsys.hrms.idp.vo.getidp.IdpFlowDetailVO flowVO = new com.vinsys.hrms.idp.vo.getidp.IdpFlowDetailVO();
                flowVO.setId(activeFlow.getId());
                flowVO.setOwnerEmployeeId(activeFlow.getEmployeeId());
                flowVO.setOwnerEmployeeRole(activeFlow.getEmployeeRole());
                flowVO.setStartDate(activeFlow.getStartDate());
                flowVO.setAction(activeFlow.getActionType());
                flowVO.setActionDate(activeFlow.getActionDate());
                flowVO.setStatus(activeFlow.getStatus());
                flowVO.setRecordStatus(activeFlow.getRecordStatus());

                // Get owner employee name
                com.vinsys.hrms.entity.Employee ownerEmployee = employeeDAO.findActiveEmployeeById(
                        activeFlow.getEmployeeId(), com.vinsys.hrms.constants.ERecordStatus.Y.name());
                if (ownerEmployee != null && ownerEmployee.getCandidate() != null) {
                    String ownerName = (ownerEmployee.getCandidate().getFirstName() != null ? ownerEmployee.getCandidate().getFirstName() : "") + " " +
                            (ownerEmployee.getCandidate().getLastName() != null ? ownerEmployee.getCandidate().getLastName() : "");
                    flowVO.setOwnerEmployeeName(ownerName.trim());
                }

                vo.setActiveFlowDetails(flowVO);
            }
        }

        return vo;
    }

    private IdpDetailComment getIdpDetailComment(Idp idp, IdpDetails detail) {
        IdpDetailComment detailComment =
                idpDetailCommentDAO.findByIdpIdAndIdpDetailIdAndIdpFlowId(
                        idp.getId(),
                        detail.getId(),
                        idp.getActiveFlowId()
                );
        return detailComment;
    }

    /**
     * Determine allowed actions based on status and role
     */
    private List<String> determineAllowedActions(Idp idp, String role, Long loggedInEmpId) {
        List<String> actions = new java.util.ArrayList<>();

        // Everyone can view
        actions.add("View");

        // Owner can edit and delete in DRAFT status
        if (idp.getEmployeeId().equals(loggedInEmpId) &&
            com.vinsys.hrms.idp.enumconstant.IdpStatus.DRAFT.getValue().equalsIgnoreCase(idp.getStatus())) {
            actions.add("Edit");
            actions.add("Submit");
            actions.add("Delete");
        }

        // Manager, HOD, HR can approve/reject submitted IDPs
        if ((role.equalsIgnoreCase(com.vinsys.hrms.util.ERole.MANAGER.name()) ||
             role.equalsIgnoreCase(com.vinsys.hrms.util.ERole.HOD.name()) ||
             role.equalsIgnoreCase(com.vinsys.hrms.util.ERole.HR.name())) &&
            com.vinsys.hrms.idp.enumconstant.IdpStatus.SUBMITTED.getValue().equalsIgnoreCase(idp.getStatus())) {
            actions.add("Approve");
            actions.add("Reject");
        }

        return actions;
    }
}
