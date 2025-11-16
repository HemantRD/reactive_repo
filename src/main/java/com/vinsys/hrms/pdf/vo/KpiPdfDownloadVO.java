package com.vinsys.hrms.pdf.vo;

import java.util.List;

import com.vinsys.hrms.pdf.entity.KpiPdfDownloadEntity;

public class KpiPdfDownloadVO {

	    private String teamMemberName;
	    private String functionSpecific;
	    private String coreCompetencies;
	    private String leadershipCompetencies;
	    private String overallSummary;
	    private String categoryWeight;
	    private String selfAggregate;
	    private List<KpiPdfDownloadEntity> goals; // Nested goals

	    // Getters and Setters
	    public String getTeamMemberName() { return teamMemberName; }
	    public void setTeamMemberName(String teamMemberName) { this.teamMemberName = teamMemberName; }

	    public String getFunctionSpecific() { return functionSpecific; }
	    public void setFunctionSpecific(String functionSpecific) { this.functionSpecific = functionSpecific; }

	    public String getCoreCompetencies() { return coreCompetencies; }
	    public void setCoreCompetencies(String coreCompetencies) { this.coreCompetencies = coreCompetencies; }

	    public String getLeadershipCompetencies() { return leadershipCompetencies; }
	    public void setLeadershipCompetencies(String leadershipCompetencies) { this.leadershipCompetencies = leadershipCompetencies; }

	    public String getOverallSummary() { return overallSummary; }
	    public void setOverallSummary(String overallSummary) { this.overallSummary = overallSummary; }

	    public String getCategoryWeight() { return categoryWeight; }
	    public void setCategoryWeight(String categoryWeight) { this.categoryWeight = categoryWeight; }

	    public String getSelfAggregate() { return selfAggregate; }
	    public void setSelfAggregate(String selfAggregate) { this.selfAggregate = selfAggregate; }

	    public List<KpiPdfDownloadEntity> getGoals() { return goals; }
	    public void setGoals(List<KpiPdfDownloadEntity> goals) { this.goals = goals; }
	

}
