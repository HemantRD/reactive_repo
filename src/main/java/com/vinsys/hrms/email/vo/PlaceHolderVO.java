package com.vinsys.hrms.email.vo;

/**
 * 
 * @author amey.gangakhedkar
 *
 */
public class PlaceHolderVO {

	private String expressionVariableName;
	
	private String expressionVariableValue;

	public String getExpressionVariableName() {
		return expressionVariableName;
	}

	public void setExpressionVariableName(String expressionVariableName) {
		this.expressionVariableName = expressionVariableName;
	}

	public String getExpressionVariableValue() {
		return expressionVariableValue;
	}

	public void setExpressionVariableValue(String expressionVariableValue) {
		this.expressionVariableValue = expressionVariableValue;
	}

	@Override
	public String toString() {
		return "PlaceHolderVO [expressionVariableName=" + expressionVariableName + ", expressionVariableValue="
				+ expressionVariableValue + "]";
	}
	
}
