package com.vinsys.hrms.kra.vo;

import java.util.List;

public class PercentageGroupedRowVo {

	private int rating;
    private List<PercentageRowVO> rows;
    
    
	public PercentageGroupedRowVo(int rating, List<PercentageRowVO> rows) {
		super();
		this.rating = rating;
		this.rows = rows;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public List<PercentageRowVO> getRows() {
		return rows;
	}
	public void setRows(List<PercentageRowVO> rows) {
		this.rows = rows;
	}
    
    
	 
	 
}
