package com.vinsys.hrms.reimbursement.service.impl;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.reimbursement.service.AddClaimImpl;
import com.vinsys.hrms.reimbursement.service.IReimbursementService;
import com.vinsys.hrms.reimbursement.vo.AddClaimRequestVO;
import com.vinsys.hrms.reimbursement.vo.AddClaimResponseVO;

@Service
public class ReimbursementService implements IReimbursementService {

	@Autowired
	AddClaimImpl claimImpl;
	

	
	@Override
	public HRMSBaseResponse<AddClaimResponseVO> addClaim(AddClaimRequestVO request)
			throws HRMSException, ParseException, ClassNotFoundException, SQLException, URISyntaxException {
		return claimImpl.addClaim(request);
	}

	@Override
	public HRMSBaseResponse<AddClaimResponseVO> addRequest(AddClaimRequestVO request) throws HRMSException {
		// TODO Auto-generated method stub
		return null;
	}
}
