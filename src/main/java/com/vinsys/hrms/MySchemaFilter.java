package com.vinsys.hrms;

import org.hibernate.boot.model.relational.Namespace;
import org.hibernate.boot.model.relational.Sequence;
import org.hibernate.mapping.Table;
import org.hibernate.tool.schema.spi.SchemaFilter;

public class MySchemaFilter implements SchemaFilter {

	public static final MySchemaFilter INSTANCE = new MySchemaFilter();

	@Override
	public boolean includeNamespace(Namespace namespace) {
		return true;
	}

	@Override
	public boolean includeTable(Table table) {
		if (table.getName().equals("KraCountForHR") || table.getName().equals("KraDetailsLite")
				|| table.getName().equals("SmartAlarmWithAccess_BlankNew.dbo.vw_Transactions_Swipes")
				|| table.getName().equals("tbl_mst_functions") || table.getName().equals("WhiteListing")
				|| table.getName().equals("tbl_mst_leave_mapping") || table.getName().equals("tbl_organization")
				|| table.getName().equals("tbl_trn_accommodation_request_v2")// date
				|| table.getName().equals("tbl_trn_ticket_request_v2")// date
				|| table.getName().equals("tbl_trn_travel_request_v2")// date
				|| table.getName().equals("tbl_trn_traveller_details")// date
				|| table.getName().equals("view_accomodation_request_report")// view
				|| table.getName().equals("view_cab_request_report")// view
				|| table.getName().equals("view_leave_details_report")// view
				|| table.getName().equals("view_leave_summary_report")// view
				|| table.getName().equals("view_ticket_request_report")// view
				|| table.getName().equals("view_traveldesk_detail_report")// view
				|| table.getName().equals("view_won_to_cost_report")// view
				|| table.getName().equals("vw_attendance_csv_data")// view
				|| table.getName().equals("vw_employee_attendance_summary")// view
				|| table.getName().equals("vw_kra_details")// view
				|| table.getName().equals("vw_travel_request_details")// view
				|| table.getName().equals("tbl_trn_cab_request_v2") || table.getName().equals("tbl_map_reim_approver")
				|| table.getName().equals("tbl_mst_reim_claim_category")
				|| table.getName().equals("tbl_mst_reim_claim_type")
				|| table.getName().equals("tbl_mst_reim_requester_type")
				|| table.getName().equals("tbl_mst_reim_stay_type") || table.getName().equals("tbl_mst_reim_stay_type")
				|| table.getName().equals("tbl_mst_reim_travel_type") || table.getName().equals("tbl_trn_reim_claims")
				|| table.getName().equals("tbl_trn_reim_requests") || table.getName().equals("tbl_trn_reim_wf") || table.getName().equals("tbl_organization_holiday")
		// rule _RETURN on view vw_travel_request_details
		// depends on column "date_of_journey"

		) {
			return false;
		}
		return true;
	}

	@Override
	public boolean includeSequence(Sequence sequence) {
//		if ("seq_mst_mode_of_education".equals(sequence.getName())) {
//			return false;
//		}
		return false;
	}
}
