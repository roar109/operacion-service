package org.pluto.service.operacion.model;

public class Expression {
	
	private Boolean valid;
	private String expression;
	private String errorMessage;
	private String result;

	public Expression() {
	}

	public Expression(final boolean valid) {
		this.valid = valid;
	}

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
