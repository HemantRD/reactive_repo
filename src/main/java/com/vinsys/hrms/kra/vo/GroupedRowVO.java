package com.vinsys.hrms.kra.vo;

import java.util.List;

public class GroupedRowVO {

	private int rating;
    private List<RowVO> rows;
    
	
	
	
	public GroupedRowVO(int rating, List<RowVO> rows) {
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
	public List<RowVO> getRows() {
		return rows;
	}
	public void setRows(List<RowVO> rows) {
		this.rows = rows;
	}
	
    
    
}
