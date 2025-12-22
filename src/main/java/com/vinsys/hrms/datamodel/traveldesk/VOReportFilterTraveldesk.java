package com.vinsys.hrms.datamodel.traveldesk;

import java.util.Date;

public class VOReportFilterTraveldesk {

	public Date fromDate;
	public Date toDate;
	public String ticketStr;
    public String accommodationStr;
    public String cabStr;
    public String airStr;
    public String busStr;
    public String trainStr;
	
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public String getTicketStr() {
		return ticketStr;
	}
	public void setTicketStr(String ticketStr) {
		this.ticketStr = ticketStr;
	}
	public String getAccommodationStr() {
		return accommodationStr;
	}
	public void setAccommodationStr(String accommodationStr) {
		this.accommodationStr = accommodationStr;
	}
	public String getCabStr() {
		return cabStr;
	}
	public void setCabStr(String cabStr) {
		this.cabStr = cabStr;
	}
	public String getAirStr() {
		return airStr;
	}
	public void setAirStr(String airStr) {
		this.airStr = airStr;
	}
	public String getBusStr() {
		return busStr;
	}
	public void setBusStr(String busStr) {
		this.busStr = busStr;
	}
	public String getTrainStr() {
		return trainStr;
	}
	public void setTrainStr(String trainStr) {
		this.trainStr = trainStr;
	}
	
	
}
