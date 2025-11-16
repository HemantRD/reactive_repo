package com.vinsys.hrms.kra.vo;

import java.util.List;

public class RatingPercentageResponseVO {

	private List<PercentageHeaderVO> headers;
    private List<PercentageGroupedRowVo> groupedRows;
    
	public List<PercentageHeaderVO> getHeaders() {
		return headers;
	}
	public void setHeaders(List<PercentageHeaderVO> headers) {
		this.headers = headers;
	}
	public List<PercentageGroupedRowVo> getGroupedRows() {
		return groupedRows;
	}
	public void setGroupedRows(List<PercentageGroupedRowVo> groupedRows) {
		this.groupedRows = groupedRows;
	}
	
    
    
    
}
