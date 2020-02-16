package com.shahin.model;

public class Vehicle {
	private String variableName;
	private String variableValue;
	
	public Vehicle(String variableName, String variableValue) {
		super();
		this.variableName = variableName;
		this.variableValue = variableValue;
	}
	public String getVariableName() {
		return variableName;
	}
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	public String getVariableValue() {
		return variableValue;
	}
	public void setVariableValue(String variableValue) {
		this.variableValue = variableValue;
	}
	
	
	
	
}
