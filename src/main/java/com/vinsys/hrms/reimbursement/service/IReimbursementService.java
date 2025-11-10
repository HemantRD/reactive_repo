package com.vinsys.hrms.reimbursement.service;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.ParseException;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.reimbursement.vo.AddClaimRequestVO;
import com.vinsys.hrms.reimbursement.vo.AddClaimResponseVO;

public interface IReimbursementService {

	HRMSBaseResponse<AddClaimResponseVO> addClaim(AddClaimRequestVO request)
			throws HRMSException, ParseException, ClassNotFoundException, SQLException, URISyntaxException;

	HRMSBaseResponse<AddClaimResponseVO> addRequest(AddClaimRequestVO request) throws HRMSException;

}
