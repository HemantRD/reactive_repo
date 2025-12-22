package com.vinsys.hrms.idp.helper;

import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.dao.IEmployeeDesignationDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.entity.*;
import com.vinsys.hrms.idp.entity.Idp;
import com.vinsys.hrms.idp.enumconstant.IdpRecordStatus;
import com.vinsys.hrms.idp.enumconstant.IdpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Helper class for IDP entity creation and initialization
 */
@Component
public class IdpEntityHelper {

    @Autowired
    private IHRMSEmployeeDAO employeeDAO;

    @Autowired
    private IEmployeeDesignationDAO designationDAO;

    /**
     * Create a new IDP entity with all required fields populated from employee data
     */
    public Idp createNewIdp(Employee employee) {
        Idp idp = new Idp();
        idp.setCreatedDate(Instant.now());
        idp.setStatus(IdpStatus.DRAFT.getValue());
        idp.setRecordStatus(IdpRecordStatus.ACTIVE.getValue());
        idp.setEmployeeId(employee.getId());

        // Set designation
        setDesignation(idp, employee);

        // Set division/function
        setDivision(idp, employee);

        // Set department
        setDepartment(idp, employee);

        // Set grade
        setGrade(idp, employee);

        return idp;
    }

    /**
     * Set designation from employee data
     */
    private void setDesignation(Idp idp, Employee employee) {
        EmployeeDesignation designation = designationDAO.findByEmployee(employee);
        if (designation != null && designation.getDesignation() != null) {
            idp.setPosition(designation.getDesignation().getDesignationName());
        }
    }

    /**
     * Set division/function from employee data
     */
    private void setDivision(Idp idp, Employee employee) {
        EmployeeDivision division = employee.getEmployeeDivision();
        if (division != null && division.getDivision() != null) {
            idp.setFunction(division.getDivision().getDivisionName());
            // Note: divId will be fetched from view when needed
        }
    }

    /**
     * Set department from employee data
     */
    private void setDepartment(Idp idp, Employee employee) {
        // Note: departmentId will be fetched from view when needed
        // Removed: idp.setDepartmentId(department.getDepartment().getId());
    }

    /**
     * Set grade from employee's professional details
     */
    private void setGrade(Idp idp, Employee employee) {
        String gradeValue = getEmployeeGrade(employee);
        idp.setGrade(gradeValue);
    }

    /**
     * Get employee grade from professional details
     * Priority: GradeMaster.gradeDescription > CandidateProfessionalDetail.grade > "Not Assigned"
     */
    public String getEmployeeGrade(Employee employee) {
        if (employee.getCandidate() != null && employee.getCandidate().getCandidateProfessionalDetail() != null) {
            CandidateProfessionalDetail professionalDetail = employee.getCandidate().getCandidateProfessionalDetail();

            // Try GradeMaster first
            if (professionalDetail.getGradeId() != null &&
                professionalDetail.getGradeId().getGradeDescription() != null) {
                return professionalDetail.getGradeId().getGradeDescription();
            }

            // Fallback to grade string
            if (professionalDetail.getGrade() != null) {
                return professionalDetail.getGrade();
            }
        }
        return "Not Assigned";
    }

    /**
     * Get employee full name
     */
    public String getEmployeeFullName(Employee employee) {
        if (employee != null && employee.getCandidate() != null) {
            String firstName = employee.getCandidate().getFirstName() != null ?
                              employee.getCandidate().getFirstName() : "";
            String lastName = employee.getCandidate().getLastName() != null ?
                             employee.getCandidate().getLastName() : "";
            return (firstName + " " + lastName).trim();
        }
        return "";
    }

    /**
     * Get employee designation name
     */
    public String getEmployeeDesignation(Employee employee) {
        EmployeeDesignation empDesignation = designationDAO.findByEmployee(employee);
        if (empDesignation != null && empDesignation.getDesignation() != null) {
            return empDesignation.getDesignation().getDesignationName();
        }
        return null;
    }

    /**
     * Get employee department name
     */
    public String getEmployeeDepartment(Employee employee) {
        EmployeeDepartment empDepartment = employee.getEmployeeDepartment();
        if (empDepartment != null && empDepartment.getDepartment() != null) {
            return empDepartment.getDepartment().getDepartmentName();
        }
        return null;
    }

    /**
     * Get employee by ID
     */
    public Employee getActiveEmployee(Long employeeId) {
        return employeeDAO.findActiveEmployeeById(employeeId, ERecordStatus.Y.name());
    }
}
