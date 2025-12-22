package com.vinsys.hrms.services.traveldesk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.hrms.datamodel.HRMSListResponseObject;
import com.vinsys.hrms.entity.traveldesk.BPMDetails;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.translator.HRMSResponseTranslator;
import com.vinsys.hrms.util.HRMSHelper;
import com.vinsys.hrms.util.IHRMSConstants;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden @RestController
@RequestMapping(path = "/bpmrequest")

//@PropertySource(value="${HRMSCONFIG}")
public class BPMRequestService {
	
	private static final Logger logger = LoggerFactory.getLogger(TravelRequestService.class);
	@Value("${base.url}")
	private String baseURL;
	
	
	@Value("${BPM.driver}")
	private  String BPMdriver;
	
	@Value("${BPM.url}")
	private  String BPMurl;
	
	@Value("${BPM.DB_username}")
	private String BPM_DB_username;
	
	@Value("${BPM.DB_password}")
	private String BPM_DB_password;
	

	   
	   Connection conn = null;
	   Statement stmt = null;
	   ResultSet rset=null;

	   @RequestMapping(value = "/getBPMNoList/{startingbpmNo}",method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getBPMNoList(@PathVariable("startingbpmNo") long bpmNo)
	{ 
		logger.info(" :: Inside GetBPM No List :: ");
		//Connection conn = null;
		try {
			Class.forName(BPMdriver);
			if(conn==null)
			conn = DriverManager.getConnection(BPMurl,BPM_DB_username,BPM_DB_password);

			stmt = conn.createStatement();
		      String sql;
		      sql = "SELECT id FROM enquiries where enquiry_status not in('CANCELLED,Open') and id like '"+bpmNo+"%'";
		      ResultSet rset = stmt.executeQuery(sql);
	
	 
	         
	         List<Object> vobpmnolist = new ArrayList<>();
	         List<BPMDetails>bpmNoList=new ArrayList<>();
	         while(rset.next()) {   // Move the cursor to the next row, return false if no more row
	        	 BPMDetails bpmno=new BPMDetails();
	        	 bpmno.setId(Long.parseLong(rset.getString("id")));
	        	 bpmNoList.add(bpmno);
					
	          }
	         if(!HRMSHelper.isNullOrEmpty(bpmNoList)) {
		         vobpmnolist = HRMSResponseTranslator.translatetoBPMNoList(bpmNoList, vobpmnolist);
		         HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
					hrmsListResponseObject.setListResponse(vobpmnolist);
					hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
					hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
					return HRMSHelper.createJsonString(hrmsListResponseObject);
	         }
	         else
	         {
	        	 return HRMSHelper.sendErrorResponse(IHRMSConstants.DataNotFoundMessage, IHRMSConstants.DataNotFoundCode);
	         }
		
		} 
		/*catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} */catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		finally
		{
			 
			//finally block used to close resources
		      try{
		    	  if(rset!=null)
		    		  rset.close();
		         if(stmt!=null)
		            stmt.close();
		         //if(conn!=null)
			       //     conn.close();
		      }catch(SQLException se2){
		      }
		   
		}
		  return null;
		
	}
	
	@RequestMapping(value = "/getBPMDetails/{bpmNo}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getBPMDetails(@PathVariable("bpmNo") long bpmNo) {
		try {
			if (!HRMSHelper.isLongZero(bpmNo)) {
				
				Class.forName(BPMdriver);
				if(conn==null)
					conn = DriverManager.getConnection(BPMurl,BPM_DB_username,BPM_DB_password);

				stmt = conn.createStatement();
			      String sql;
			      sql = "select enq.id as 'BPM No',bu.name as 'Business Unit',org.name as 'Client Name',concat(bd_name.first_name,\" \",bd_name.last_name) as 'BD Name',\r\n" + 
			      		"po_details.training_start_date,po_details.training_end_date\r\n" + 
			      		"from enquiries enq\r\n" + 
			      		"JOIN enquiry_business_units ebu on ebu.enquiry_id=enq.id\r\n" + 
			      		"JOIN business_units bu on bu.id=ebu.business_unit_id\r\n" + 
			      		"JOIN enquirers enquirie on enquirie.id=enq.enquirer_id\r\n" + 
			      		"JOIN organizations org on org.id=enquirie.organization_id\r\n" + 
			      		"JOIN employees bd_name on bd_name.id=enq.bd_user_id\r\n" + 
			      		"LEFT JOIN enquiry_po_details po_details on po_details.enquiry_id=enq.id"
			      		+ " where enq.id="+bpmNo;
			      rset = stmt.executeQuery(sql);

			      
			      List<BPMDetails>bpmNoList=new ArrayList<>();
			      
		         while(rset.next()) {   // Move the cursor to the next row, return false if no more row
		        	 BPMDetails bpmno=new BPMDetails();
		        	 bpmno.setId(Long.parseLong(rset.getString("BPM No")));
		        	 bpmno.setBdName(rset.getString("BD Name"));
		        	 bpmno.setBusinessUnit(rset.getString("Business Unit"));
		        	 bpmno.setClientName(rset.getString("Client Name"));
		        	 bpmno.setTraining_start_date(rset.getString("training_start_date"));
		        	 bpmno.setTraining_end_date(rset.getString("training_end_date"));
		        	 bpmNoList.add(bpmno);
						
		          }
				
		         
				List<Object> vobpmnolist = new ArrayList<>();
				
				if(!HRMSHelper.isNullOrEmpty(bpmNoList))
				{
					vobpmnolist = HRMSResponseTranslator.translatetoBPMDetails(bpmNoList, vobpmnolist);
					HRMSListResponseObject hrmsListResponseObject = new HRMSListResponseObject();
					hrmsListResponseObject.setListResponse(vobpmnolist);
					hrmsListResponseObject.setResponseCode(IHRMSConstants.successCode);
					hrmsListResponseObject.setResponseMessage(IHRMSConstants.successMessage);
					
					return HRMSHelper.createJsonString(hrmsListResponseObject);
					
				}
				else
				{
					throw new HRMSException(IHRMSConstants.DataNotFoundCode, IHRMSConstants.DataNotFoundMessage);
					
				}
				
			} else { // if employeeId is zero
				throw new HRMSException(IHRMSConstants.InsufficientDataCode, IHRMSConstants.InsufficientDataMessage);
			}
			
			
		} catch (HRMSException e) {
			e.printStackTrace();
			try {
				return HRMSHelper.sendErrorResponse(e.getResponseMessage(), e.getResponseCode());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception unknown) {
			try {
				unknown.printStackTrace();
				return HRMSHelper.sendErrorResponse(IHRMSConstants.UnknowErrorMessage, IHRMSConstants.UnknowErrorCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		finally
		{
			 
			//finally block used to close resources
		      try{
		    	  if(rset!=null)
		    		  rset.close();
		         if(stmt!=null)
		            stmt.close();
		       //  if(conn!=null)
			     //       conn.close();
		      }catch(SQLException se2){
		      }
		   
		}
		return null;
	}
	
	
	
	
}
