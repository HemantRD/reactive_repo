package com.vinsys.hrms.traveldesk.service.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.vinsys.hrms.constants.ERecordStatus;
import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.security.SecurityFilter;
import com.vinsys.hrms.security.service.IAuthorizationService;
import com.vinsys.hrms.traveldesk.service.IBPMRequsetService;
import com.vinsys.hrms.traveldesk.vo.BPMDetailsVO;
import com.vinsys.hrms.traveldesk.vo.BPMListResponseVO;
import com.vinsys.hrms.traveldesk.vo.BpmList;
import com.vinsys.hrms.traveldesk.vo.GetBpmDetailsResponeVO;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.ResponseCode;

/**
 * @author Onkar A
 *
 * 
 */
@Service
public class BPMRequestServiceImpl implements IBPMRequsetService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${BPM.driver}")
	private String BPMdriver;

	@Value("${BPM.url}")
	private String BPMurl;

	@Value("${BPM.DB_username}")
	private String BPM_DB_username;

	@Value("${BPM.DB_password}")
	private String BPM_DB_password;

	@Autowired
	IAuthorizationService authorizationServiceImpl;

	@Value("${app_version}")
	private String applicationVersion;
	@Value("${bpm.api.key}")
	private String bpmApiKey;
	@Value("${bpm.lineitemsdetails.url}")
	private String lineItemsDetails;
	@Value("${bpm.list.url}")
	private String bpmList;
	@Value("${bpm.default.bpmnumber}")
	private Long defauldBpmNumber;
	@Value("${bpm.default.clientname}")
	private String defaultClientName;

	// feature flag
	@Value("${bpm.bpmlist.feature}")
	private String bpmNewFeatureFlag;

	@Override
	public HRMSBaseResponse<?> getBPMDetails(Long bpmNo)
			throws ClassNotFoundException, SQLException, HRMSException, URISyntaxException {
		log.info("Inside getBPMDetails method");
		// HRMSBaseResponse<?> response = new HRMSBaseResponse<>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		BPMDetailsVO bpmDetails = null;
		if (!authorizationServiceImpl.isAuthorizedFunctionName("getBPMDetails", role)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		if (bpmNewFeatureFlag.equalsIgnoreCase(ERecordStatus.Y.name())) {
			// generate base URL
			try {
				URI uri = new URI(lineItemsDetails + "?bpmNo=" + bpmNo + "&apiKey=" + bpmApiKey);

				RestTemplate restTemplate = new RestTemplate();
				ResponseEntity<GetBpmDetailsResponeVO> result = restTemplate.getForEntity(uri,
						GetBpmDetailsResponeVO.class);
				GetBpmDetailsResponeVO bpmDetailsResponse = result.getBody();
				bpmDetails = getBPMDetailsByApi(bpmDetailsResponse);
			} catch (Exception e) {
				e.printStackTrace();
				log.info(e.getMessage());
				bpmDetails = getBPMDetailsByApi(null);
			}
			HRMSBaseResponse<BPMDetailsVO> response = new HRMSBaseResponse<BPMDetailsVO>();
			response.setResponseBody(bpmDetails);
			response.setResponseCode(1200);

			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setApplicationVersion(applicationVersion);
			return response;
		} else {
			List<BPMDetailsVO> bpmDetailslist = null;
			bpmDetailslist = getBPMDetailsByDBConnection(bpmNo);
			HRMSBaseResponse<List<BPMDetailsVO>> response = new HRMSBaseResponse<>();
			response.setResponseBody(bpmDetailslist);
			response.setResponseCode(1200);

			response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
			response.setApplicationVersion(applicationVersion);
			return response;
		}
	}

	/**
	 * @param bpmNo
	 * @param bpmDetails
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws HRMSException
	 */
	private List<BPMDetailsVO> getBPMDetailsByDBConnection(Long bpmNo)
			throws ClassNotFoundException, SQLException, HRMSException {
		log.info("inside getBPMDetailsByDBConnection method");
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		List<BPMDetailsVO> bpmDetails = new ArrayList<BPMDetailsVO>();
		BPMDetailsVO bpmDetailsVO = new BPMDetailsVO();
		try {
			if (!HRMSHelper.isLongZero(bpmNo)) {
				Class.forName(BPMdriver);
				if (conn == null)
					conn = DriverManager.getConnection(BPMurl, BPM_DB_username, BPM_DB_password);
				stmt = conn.createStatement();
				String sql = "select enq.id as 'BPM No',bu.name as 'Business Unit',tld.client_name  as 'Client Name',concat(bd_name.first_name,\" \",bd_name.last_name) as 'BD Name',\r\n"
						+ "					 po_details.training_start_date,po_details.training_end_date from enquiries enq\r\n"
						+ "						 JOIN enquiry_business_units ebu on ebu.enquiry_id=enq.id\r\n"
						+ "						 JOIN business_units bu on bu.id=ebu.business_unit_id\r\n"
						+ "						 JOIN enquirers enquirie on enquirie.id=enq.enquirer_id\r\n"
						+ "						 JOIN organizations org on org.id=enquirie.organization_id\r\n"
						+ "						 JOIN employees bd_name on bd_name.id=enq.bd_user_id\r\n"
						+ "						 Join lead_address la on enq.lead_address_id =la.id\r\n"
						+ "						 join tbl_lead_details tld on la.lead_id  =tld.id\r\n"
						+ "						 LEFT JOIN enquiry_po_details po_details on po_details.enquiry_id=enq.id where enq.id="
						+ bpmNo;
				rset = stmt.executeQuery(sql);

				while (rset.next()) {
					bpmDetailsVO = new BPMDetailsVO();
					bpmDetailsVO.setId(Long.parseLong(rset.getString("BPM No")));
					bpmDetailsVO.setBdName(rset.getString("BD Name"));
					bpmDetailsVO.setBusinessUnit(rset.getString("Business Unit"));
					bpmDetailsVO.setClientName(rset.getString("Client Name"));
					bpmDetails.add(bpmDetailsVO);
				}

				if (HRMSHelper.isNullOrEmpty(bpmDetailsVO)) {
					throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201) + " BPM number");
				}
			} else {
				throw new HRMSException(1200, ResponseCode.getResponseCodeMap().get(1201));
			}
		} finally {
			try {
				if (rset != null)
					rset.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}

		}
		log.info("Exit from getBPMDetailsByDBConnection method");
		return bpmDetails;
	}

	private BPMDetailsVO getBPMDetailsByApi(GetBpmDetailsResponeVO bpmDetailsResponse) {
		log.info("inside getBPMDetailsByApi method");
		BPMDetailsVO bpmDetailsVO = new BPMDetailsVO();

		if (!HRMSHelper.isNullOrEmpty(bpmDetailsResponse) && !HRMSHelper.isNullOrEmpty(bpmDetailsResponse.getBdName())
				&& !HRMSHelper.isNullOrEmpty(bpmDetailsResponse.getId())
				&& !HRMSHelper.isNullOrEmpty(bpmDetailsResponse.getBusinessUnit())
				&& !HRMSHelper.isNullOrEmpty(bpmDetailsResponse.getClientName())) {
			bpmDetailsVO = new BPMDetailsVO();
			bpmDetailsVO.setId(bpmDetailsResponse.getId());
			bpmDetailsVO.setBdName(bpmDetailsResponse.getBdName());
			bpmDetailsVO.setBusinessUnit(bpmDetailsResponse.getBusinessUnit());
			bpmDetailsVO.setClientName(bpmDetailsResponse.getClientName());
			bpmDetailsVO.setBpmStatus(bpmDetailsResponse.getBpmStatus());
			// set LineItems
			if (!HRMSHelper.isNullOrEmpty(bpmDetailsResponse.getData())) {
				bpmDetailsVO.setLineItems(bpmDetailsResponse.getData());
			}
//			bpmDetails.add(bpmDetailsVO);
		} else {
			bpmDetailsVO = new BPMDetailsVO();
			bpmDetailsVO.setId(defauldBpmNumber);
			bpmDetailsVO.setBdName(null);
			bpmDetailsVO.setBusinessUnit(null);
			bpmDetailsVO.setClientName(defaultClientName);
//			bpmDetails.add(bpmDetailsVO);
		}
		log.info("Exit from getBPMDetailsByApi method");
		return bpmDetailsVO;
	}

	@Override
	public HRMSBaseResponse<BPMListResponseVO> getBPMList(Long bpmNumber) throws HRMSException, URISyntaxException {

		log.info("Inside getBPMDetails method");
		HRMSBaseResponse<BPMListResponseVO> response = new HRMSBaseResponse<>();
		List<String> role = SecurityFilter.TL_CLAIMS.get().getRoles();
		BPMListResponseVO bpmListResponse = new BPMListResponseVO();
		if (!authorizationServiceImpl.isAuthorizedFunctionName("getBPMDetails", role)) {
			throw new HRMSException(1500, ResponseCode.getResponseCodeMap().get(1521));
		}

		// generate base URL
		URI uri = null;
		try {
			uri = new URI(bpmList + "?bpmNo=" + bpmNumber + "&apiKey=" + bpmApiKey);
			RestTemplate restTemplate = new RestTemplate();
			if (!HRMSHelper.isNullOrEmpty(bpmNumber) && String.valueOf(bpmNumber).length() >= 3) {
				ResponseEntity<BPMListResponseVO> result = restTemplate.getForEntity(uri, BPMListResponseVO.class);
				bpmListResponse = result.getBody();
			}
			response = getBPMListByApi(response, bpmListResponse);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			response = getBPMListByApi(response, bpmListResponse);
		}

		response.setResponseCode(1200);
		response.setResponseMessage(ResponseCode.getResponseCodeMap().get(1200));
		response.setApplicationVersion(applicationVersion);
		log.info("Exit from getBPMDetails method");
		return response;

	}

	/**
	 * @param response
	 * @param bpmListResponse
	 */
	private HRMSBaseResponse<BPMListResponseVO> getBPMListByApi(HRMSBaseResponse<BPMListResponseVO> response,
			BPMListResponseVO bpmListResponse) {

		BpmList bpmList = new BpmList();
		if (!HRMSHelper.isNullOrEmpty(bpmListResponse) && !HRMSHelper.isNullOrEmpty(bpmListResponse.getData())) {
			response.setResponseBody(bpmListResponse);
		} else {
			List<BpmList> list = new ArrayList<BpmList>();
			bpmList.setBpmNo(defauldBpmNumber);
			bpmList.setBdName(null);
			bpmList.setBusinessUnit(null);
			bpmList.setClientName(defaultClientName);
			list.add(bpmList);
			bpmListResponse.setData(list);

			response.setResponseBody(bpmListResponse);
		}

		return response;
	}

}
