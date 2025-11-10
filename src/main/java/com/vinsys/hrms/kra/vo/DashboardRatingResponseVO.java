package com.vinsys.hrms.kra.vo;

import java.util.List;

public class DashboardRatingResponseVO {

	private List<HeaderVO> headers;
    private List<GroupedRowVO> groupedRows;
    
    
	public List<HeaderVO> getHeaders() {
		return headers;
	}
	public void setHeaders(List<HeaderVO> headers) {
		this.headers = headers;
	}
	public List<GroupedRowVO> getGroupedRows() {
		return groupedRows;
	}
	public void setGroupedRows(List<GroupedRowVO> groupedRows) {
		this.groupedRows = groupedRows;
	}
	
    
    
}
