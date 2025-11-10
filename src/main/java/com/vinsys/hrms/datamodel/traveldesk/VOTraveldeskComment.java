package com.vinsys.hrms.datamodel.traveldesk;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vinsys.hrms.datamodel.VOEmployee;
import com.vinsys.hrms.util.IHRMSConstants;

public class VOTraveldeskComment implements Comparable<VOTraveldeskComment> {

	private long id;
	private VOTravelRequest travelRequest;
	private long childId;
	private String childType;
	private VOEmployee employee;
	private String commentator;
	private String action;
	private String comment;
	private String createdDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public VOTravelRequest getTravelRequest() {
		return travelRequest;
	}

	public void setTravelRequest(VOTravelRequest travelRequest) {
		this.travelRequest = travelRequest;
	}

	public long getChildId() {
		return childId;
	}

	public void setChildId(long childId) {
		this.childId = childId;
	}

	public String getChildType() {
		return childType;
	}

	public void setChildType(String childType) {
		this.childType = childType;
	}

	public VOEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(VOEmployee employee) {
		this.employee = employee;
	}

	public String getCommentator() {
		return commentator;
	}

	public void setCommentator(String commentator) {
		this.commentator = commentator;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	
	@Override
    public int compareTo(VOTraveldeskComment compareCmt) {
        Date createdDateMain = null;
        Date createdDateCmr = null;
		try {
			createdDateMain = new SimpleDateFormat(IHRMSConstants.FRONT_END_DATE_FORMAT_DDMMYY_HHMMSS).parse(this.createdDate);
			createdDateCmr = new SimpleDateFormat(IHRMSConstants.FRONT_END_DATE_FORMAT_DDMMYY_HHMMSS).parse(compareCmt.createdDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return createdDateCmr.compareTo(createdDateMain);
    }


}
