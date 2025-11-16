package com.vinsys.hrms.traveldesk.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vinsys.hrms.traveldesk.entity.TDExpenceSummaryReport;

public interface ITDExpenceSummaryDAO extends JpaRepository<TDExpenceSummaryReport,Long> {
	
	@Query(value = "select travel_request_id ,request_created_date ,book_accommodation ,book_cab ,book_ticket ,bpm_number ,requester_id ,requester_name "
			+",total_approximate_cost ,total_final_cost ,total_refund_cost ,total_settled_cost ,travel_reason ,division_id "
			+",division_name ,department_id ,department_name ,name,email_id ,contact_number ,dob ,pickup_location ,pickup_time ,drop_location "
			+",is_primary_traveller ,passport_number ,passport_date_expiry ,visa_number ,visa_date_expiry ,is_management_employee ,traveller_type ,mode_of_travel "
			+",invoice_number ,bd_name ,status ,tiket_date_of_journey ,tiket_return_date ,tiket_from_location ,tiket_to_location ,cab_date_of_journey "
			+",cab_return_date ,cab_from_location ,cab_to_location ,acc_location ,acc_no_of_rooms ,acc_from_date,acc_to_date ,approver_first_name ,approver_last_name ,ticket_id ,acc_is ,cab_id ,currency "
			+",cab_approx_cost ,ticket_approx_cost ,accomm_approx_cost ,cab_final_cost ,ticket_final_cost ,accomm_final_cost ,cab_type ,cab_driver_first_name ,cab_driver_last_name ,id  "
			+" from vw_travel_request_details where request_created_date >=?1 and request_created_date <=?2 and division_id in (?3) ", nativeQuery = true)
	List<TDExpenceSummaryReport> getTravelRequestByCreatedDate(Date fromDate, Date toDate, Object[] divIds);

}