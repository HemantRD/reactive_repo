package com.vinsys.hrms.traveldesk.vo;

public class TicketExpenseVO {

	ExpenseVO airExpenseVO;
	ExpenseVO busExpenseVO;
	ExpenseVO trainExpenseVO;

	public ExpenseVO getAirExpenseVO() {
		return airExpenseVO;
	}

	public void setAirExpenseVO(ExpenseVO airExpenseVO) {
		this.airExpenseVO = airExpenseVO;
	}

	public ExpenseVO getBusExpenseVO() {
		return busExpenseVO;
	}

	public void setBusExpenseVO(ExpenseVO busExpenseVO) {
		this.busExpenseVO = busExpenseVO;
	}

	public ExpenseVO getTrainExpenseVO() {
		return trainExpenseVO;
	}

	public void setTrainExpenseVO(ExpenseVO trainExpenseVO) {
		this.trainExpenseVO = trainExpenseVO;
	}
}
