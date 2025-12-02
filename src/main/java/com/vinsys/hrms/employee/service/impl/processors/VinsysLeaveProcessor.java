package com.vinsys.hrms.employee.service.impl.processors;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.employee.vo.EmployeeLeaveDetailsVO;
import com.vinsys.hrms.exception.HRMSException;

public class VinsysLeaveProcessor extends AbstractLeaveProcessor implements ILeaveProcessor {

	protected VinsysLeaveProcessor(LeaveProcessorDependencies leaveProcessorDependencies) {
		super(leaveProcessorDependencies);
	}

	@Override
	public String creditEmpYearlyCasualLeavesMethod(Long orgId, List<Long> divList, float clCount)
			throws HRMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String creditEarnedLeaves(Long orgId, List<Long> divsionList, float elCount) throws HRMSException {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public HRMSBaseResponse<EmployeeLeaveDetailsVO> getHrLeaveDetailsProcess(Long filterEmployeeId, Integer year,
			Pageable pageable) throws HRMSException {
		// TODO Auto-generated method stub
		return null;
	}

	

}
