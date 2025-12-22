package com.vinsys.hrms.dao.traveldesk;

public interface IHRMSBPMRequestDAO {
	
	
	/*@Query("SELECT id  FROM enquiries")
	public List<BPMDetails> getBPMNoList();
	
	
	@Query("select enq.id,bu.name as 'businessUnit',org.name as 'clientName',concat(bd_name.first_name,\" \",bd_name.last_name) as 'bdName',\r\n" + 
			"po_details.training_start_date,po_details.training_end_date\r\n" + 
			"from enquiries enq\r\n" + 
			"JOIN enquiry_business_units ebu on ebu.enquiry_id=enq.id\r\n" + 
			"JOIN business_units bu on bu.id=ebu.business_unit_id\r\n" + 
			"JOIN enquirers enquirie on enquirie.id=enq.enquirer_id\r\n" + 
			"JOIN organizations org on org.id=enquirie.organization_id\r\n" + 
			"JOIN employees bd_name on bd_name.id=enq.bd_user_id\r\n" + 
			"LEFT JOIN enquiry_po_details po_details on po_details.enquiry_id=enq.id"
			+ "where enq.id=?1")
	public List<BPMDetails> getBPMDetails(long bpmNo);
	
	*/
	
}
