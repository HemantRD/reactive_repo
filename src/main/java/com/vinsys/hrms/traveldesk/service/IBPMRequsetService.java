package com.vinsys.hrms.traveldesk.service;

import java.net.URISyntaxException;
import java.sql.SQLException;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.traveldesk.vo.BPMListResponseVO;

public interface IBPMRequsetService {

	HRMSBaseResponse<?> getBPMDetails(Long bpmNumber)
			throws HRMSException, ClassNotFoundException, SQLException, URISyntaxException;

	HRMSBaseResponse<BPMListResponseVO> getBPMList(Long bpmNumber) throws HRMSException, URISyntaxException;

}
