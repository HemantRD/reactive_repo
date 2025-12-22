package com.vinsys.hrms.traveldesk.vo;

public class ExpenseSummeryResponseVO {
	private Long travelRequestId;
	private TicketExpenseVO ticketExpense;
	private CabExpenseVO cabExpenseVO;
	private ExpenseVO accommodationExpenseVO;
	private ExpenseVO totalExpense;

	public ExpenseVO getTotalExpense() {
		return totalExpense;
	}

	public void setTotalExpense(ExpenseVO totalExpense) {
		this.totalExpense = totalExpense;
	}

	public Long getTravelRequestId() {
		return travelRequestId;
	}

	public void setTravelRequestId(Long travelRequestId) {
		this.travelRequestId = travelRequestId;
	}

	public TicketExpenseVO getTicketExpense() {
		return ticketExpense;
	}

	public void setTicketExpense(TicketExpenseVO ticketExpense) {
		this.ticketExpense = ticketExpense;
	}

	public ExpenseVO getAccommodationExpenseVO() {
		return accommodationExpenseVO;
	}

	public void setAccommodationExpenseVO(ExpenseVO accommodationExpenseVO) {
		this.accommodationExpenseVO = accommodationExpenseVO;
	}

	public CabExpenseVO getCabExpenseVO() {
		return cabExpenseVO;
	}

	public void setCabExpenseVO(CabExpenseVO cabExpenseVO) {
		this.cabExpenseVO = cabExpenseVO;
	}

}
