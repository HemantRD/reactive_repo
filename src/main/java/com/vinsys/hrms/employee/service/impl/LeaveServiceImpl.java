package com.vinsys.hrms.employee.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vinsys.hrms.dao.IHRMSCandidateDAO;
import com.vinsys.hrms.dao.IHRMSCreditCompOffHistoryDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeCreditAppliedLeaveMappingDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeCreditLeaveDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeGrantLeaveDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeLeaveAppliedDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeLeaveDetailsDAO;
import com.vinsys.hrms.dao.IHRMSEmployeeReportingManager;
import com.vinsys.hrms.dao.IHRMSHtmlTemplateDAO;
import com.vinsys.hrms.dao.IHRMSMasterEmployementTypeDAO;
import com.vinsys.hrms.dao.IHRMSMasterLeaveType;
import com.vinsys.hrms.dao.IHRMSMasterLeaveTypeDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationHolidayDAO;
import com.vinsys.hrms.dao.IHRMSOrganizationWeekoffDAO;
import com.vinsys.hrms.dashboard.vo.LeaveSumarryDetailsResponseVO;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.datamodel.VOAppliedLeaveCount;
import com.vinsys.hrms.datamodel.VOHolidayResponse;
import com.vinsys.hrms.employee.service.ILeaveService;
import com.vinsys.hrms.employee.service.impl.processors.ILeaveProcessor;
import com.vinsys.hrms.employee.service.impl.processors.LeaveProcessorDependencies;
import com.vinsys.hrms.employee.service.impl.processors.LeaveProcessorFactory;
import com.vinsys.hrms.employee.vo.ApplyGrantLeaveRequestVO;
import com.vinsys.hrms.employee.vo.ApplyLeaveRequestVO;
import com.vinsys.hrms.employee.vo.AvailableLeavesVO;
import com.vinsys.hrms.employee.vo.EmployeeLeaveDetailsVO;
import com.vinsys.hrms.employee.vo.LeaveCalculationRequestVO;
import com.vinsys.hrms.employee.vo.SubOrdinateListVO;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.master.service.IMasterService;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.services.EmployeeLeaveDetailsService;
import com.vinsys.hrms.util.EmailSender;
import com.vinsys.hrms.util.EmployeeAuthorityHelper;

/**
 * @author Onkar A
 *
 * 
 */

@Service
public class LeaveServiceImpl implements ILeaveService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${app_version}")
	String applicationVersion;
	@Autowired
	IHRMSEmployeeDAO employeeDAO;
	@Autowired
	IHRMSEmployeeReportingManager reportingManagerDAO;
	@Autowired
	IHRMSMasterLeaveType leaveTypeDAO;
	@Autowired
	IHRMSEmployeeLeaveAppliedDAO employeeLeaveAppliedDAO;
	@Autowired
	IHRMSEmployeeLeaveDetailsDAO empLeaveDetailsDAO;
	@Autowired
	IHRMSMasterLeaveType masterLeaveTypeDAO;
	@Autowired
	IHRMSOrganizationHolidayDAO organizationHolidayDAO;
	@Autowired
	IHRMSOrganizationWeekoffDAO organizationWeekoffDAO;
	@Autowired
	IHRMSHtmlTemplateDAO emailTemplateDAO;
	@Autowired
	IHRMSEmployeeLeaveDetailsDAO employeeLeaveDetailsDAO;
	@Autowired
	EmailSender emailsender;
	@Autowired
	IHRMSEmployeeCreditLeaveDAO creditLeaveDAO;
	@Autowired
	IHRMSEmployeeCreditAppliedLeaveMappingDAO creditAppliedLeaveMappingDAO;
	@Autowired
	IHRMSMasterEmployementTypeDAO masterEmploymentTypeDAO;
	@Autowired
	EmployeeLeaveDetailsService employeeLeaveDetailsService;
	@Autowired
	IHRMSMasterLeaveTypeDAO mstLeaveTypeDAO;
	@Autowired
	IHRMSEmployeeLeaveDetailsDAO employeeLeaveDetailDAO;
	@Autowired
	IHRMSEmployeeGrantLeaveDAO employeeGrantLeaveDAO;
	@Autowired
	IHRMSCandidateDAO candidateDAO;
	@Autowired
	IHRMSOrganizationHolidayDAO orgHolidayDAO;
	@Autowired
	IHRMSCreditCompOffHistoryDAO creditCompOffHistoryDAO;
	@Autowired
	EmployeeAuthorityHelper employeeAuthorityHelper;

	@Autowired
	IHRMSOrganizationHolidayDAO holidayDAO;
	@Autowired
	IMasterService masterService;
	
	@Autowired
	IHRMSOrganizationDAO organizationDAO;
	
	@Autowired
	IHRMSEmployeeCreditLeaveDAO employeeCreditLeaveDAO;

	@Value("${base.url}")
	private String baseURL;

	@Override
	public HRMSBaseResponse<?> applyLeave(ApplyLeaveRequestVO request) throws HRMSException, ParseException {
		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor = factory.getLeaveProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.applyLeaveProcess(request);

	}

	protected LeaveProcessorDependencies getDependencies() {
		LeaveProcessorDependencies dependencies = new LeaveProcessorDependencies(employeeDAO, reportingManagerDAO,
				leaveTypeDAO, employeeLeaveAppliedDAO, empLeaveDetailsDAO, masterLeaveTypeDAO, organizationHolidayDAO,
				organizationWeekoffDAO, emailTemplateDAO, employeeLeaveDetailsDAO, emailsender, creditLeaveDAO,
				creditAppliedLeaveMappingDAO, masterEmploymentTypeDAO, employeeLeaveDetailsService, mstLeaveTypeDAO,
				employeeLeaveDetailDAO, employeeGrantLeaveDAO, candidateDAO, orgHolidayDAO, creditCompOffHistoryDAO,
				employeeAuthorityHelper, holidayDAO, masterService, applicationVersion, baseURL,organizationDAO,employeeCreditLeaveDAO);
		return dependencies;
	}

	@Override
	public HRMSBaseResponse<?> cancelLeave(ApplyLeaveRequestVO request) throws HRMSException {
		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor = factory.getLeaveProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.cancelLeaveProcess(request);
	}

	@Override
	public HRMSBaseResponse<AvailableLeavesVO> getAvailableLeave(Long leaveTypeId) throws HRMSException {

		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor = factory.getLeaveProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.getAvailableLeaveProcess(leaveTypeId);

	}

	@Override
	public HRMSBaseResponse<?> withdrawLeave(ApplyLeaveRequestVO request) throws HRMSException, ParseException {

		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor = factory.getLeaveProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.withdrawLeaveLeaveProcess(request);

	}

	@Override
	public HRMSBaseResponse<?> approveLeave(List<ApplyLeaveRequestVO> requestList) throws HRMSException {

		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor = factory.getLeaveProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.approveLeaveProcess(requestList);

	}

	@Override
	public HRMSBaseResponse<VOAppliedLeaveCount> calculateLeave(LeaveCalculationRequestVO requestVo)
			throws HRMSException, ParseException {

		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor = factory.getLeaveProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.acalculateLeaveProcess(requestVo);

	}

	@Override
	public HRMSBaseResponse<?> rejectLeave(ApplyLeaveRequestVO request) throws HRMSException {

		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor = factory.getLeaveProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.rejectLeaveProcess(request);

	}

	@Override
	public HRMSBaseResponse<?> withdrawApproveLeave(ApplyLeaveRequestVO request) throws HRMSException {

		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor = factory.getLeaveProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.withdrawApproveLeaveProcess(request);

	}

	@Override
	public HRMSBaseResponse<?> withdrawRejectLeave(ApplyLeaveRequestVO request) throws HRMSException {
		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor = factory.getLeaveProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.withdrawRejectLeaveProcess(request);

	}

	@Override
	public HRMSBaseResponse<?> applyGrantLeave(ApplyGrantLeaveRequestVO request) throws HRMSException, ParseException {
		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor = factory.getLeaveProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.applyGrantLeaveProcess(request);

	}

	@Override
	public HRMSBaseResponse<?> cancelGrantLeave(ApplyGrantLeaveRequestVO request) throws HRMSException {

		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor = factory.getLeaveProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.cancelGrantLeaveProcess(request);

	}

	@Override
	public HRMSBaseResponse<?> withdrawGrantLeave(ApplyGrantLeaveRequestVO request) throws HRMSException {
		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor = factory.getLeaveProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.withdrawGrantLeaveProcess(request);

	}

	@Override
	public HRMSBaseResponse<?> approveGrantLeave(ApplyGrantLeaveRequestVO request) throws HRMSException {
		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor = factory.getLeaveProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.approveGrantLeaveProcess(request);

	}

	@Override
	public HRMSBaseResponse rejectGrantLeave(ApplyGrantLeaveRequestVO request) throws HRMSException {
		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor = factory.getLeaveProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.rejectGrantLeave(request);

	}

	@Override
	public HRMSBaseResponse<VOAppliedLeaveCount> calculateGrantLeave(LeaveCalculationRequestVO request)
			throws HRMSException {

		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor = factory.getLeaveProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.calculateGrantLeaveProcess(request);

	}

	@Override
	public HRMSBaseResponse<EmployeeLeaveDetailsVO> getLeaveDetails(Long employeeId, Pageable pageable)
			throws HRMSException {

		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor = factory.getLeaveProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.getLeaveDetailsProcess(employeeId, pageable);

	}

	@Override
	public HRMSBaseResponse<EmployeeLeaveDetailsVO> getGrantLeaveDetails(Long employeeId, Pageable pageable)
			throws HRMSException {

		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor = factory.getLeaveProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.getGrantLeaveDetailsProcess(employeeId, pageable);

	}

	@Override
	public HRMSBaseResponse<EmployeeLeaveDetailsVO> getTeamLeaveDetails(Long filterEmployeeId, Integer year,
			Pageable pageable) throws HRMSException {
		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor = factory.getLeaveProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.getTeamLeaveDetailsProcess(filterEmployeeId, year, pageable);

	}

	@Override
	public HRMSBaseResponse<EmployeeLeaveDetailsVO> getTeamGrantLeaveDetails(Pageable pageable, Long employeeId)
			throws HRMSException {
		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor = factory.getLeaveProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.getTeamLeaveDetailsProcess(pageable, employeeId);

	}

	public HRMSBaseResponse<List<VOHolidayResponse>> holidayList(Long year) throws HRMSException {

		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor = factory.getLeaveProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.holidayListProcess(year);

	}

	public HRMSBaseResponse<SubOrdinateListVO> findSubordinate() throws HRMSException {

		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor = factory.getLeaveProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.findSubordinate();

	}

	@Override
	public HRMSBaseResponse<LeaveSumarryDetailsResponseVO> getEmployeeLeaveSummary(long empId, Integer year)
			throws HRMSException {

		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor = factory.getLeaveProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.getEmployeeLeaveSummary(empId, year);

	}

	/********* Method added by Akanksha Gaikwad to clear expired compoffs *****/

	public void clearExpiredCompOffs() {

		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor = factory.getLeaveProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		processor.clearExpiredCompOffs();

	}

	@Override
	public byte[] downloadTeamLeavesReport(String fromDate, String toDate,
			HttpServletResponse httpServletResponse) throws HRMSException, IOException, ParseException {
		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor = factory.getLeaveProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.downloadTeamLeavesReport(fromDate,toDate,httpServletResponse);
	}

	@Override
	public String creditEmpYearlyCasualLeavesMethod(Long orgId, List<Long> divList, float clCount)
			throws HRMSException {
		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor=factory.getLeaveProcessor(orgId, dependencies);
		return processor.creditEmpYearlyCasualLeavesMethod(orgId, divList, clCount) ;
	}

	@Override
	public String creditEarnedLeaves(Long orgId, List<Long> divsionList, float elCount) throws HRMSException, ParseException {
		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor=factory.getLeaveProcessor(orgId, dependencies);
		return processor.creditEarnedLeaves(orgId, divsionList, elCount);
	}
	

	@Override
	public byte[] downloadAllEmployeeLeavesReport(String fromDate, String toDate,
			HttpServletResponse httpServletResponse) throws IOException, HRMSException, ParseException {
		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor = factory.getLeaveProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.downloadAllEmployeeLeavesReport(fromDate,toDate,httpServletResponse);
	
	}
	
	@Override
	public HRMSBaseResponse<EmployeeLeaveDetailsVO> getLeaveDetailsForHr(Long filterEmployeeId, Integer year,
			Pageable pageable) throws HRMSException {
		LeaveProcessorDependencies dependencies = getDependencies();
		LeaveProcessorFactory factory = new LeaveProcessorFactory();
		ILeaveProcessor processor = factory.getLeaveProcessor(SecurityFilter.TL_CLAIMS.get().getOrgId(), dependencies);
		return processor.getHrLeaveDetailsProcess(filterEmployeeId, year, pageable);

	}
	
}
