package uk.ac.man.library.openresearchtracker.oacp;

import java.util.List;

public class OacpScholarcy {
	
	private List<String> summary;
	
	private List<String> key_statements;
	
	private List<String> methods;
	
	private List<String> funding_statement;
	
	private List<String> data_availability;
	
	private List<String> registrations;

	public List<String> getSummary() {
		return summary;
	}

	public void setSummary(List<String> summary) {
		this.summary = summary;
	}

	public List<String> getKey_statements() {
		return key_statements;
	}

	public void setKey_statements(List<String> key_statements) {
		this.key_statements = key_statements;
	}

	public List<String> getMethods() {
		return methods;
	}

	public void setMethods(List<String> methods) {
		this.methods = methods;
	}

	public List<String> getFunding_statement() {
		return funding_statement;
	}

	public void setFunding_statement(List<String> funding_statement) {
		this.funding_statement = funding_statement;
	}

	public List<String> getData_availability() {
		return data_availability;
	}

	public void setData_availability(List<String> data_availability) {
		this.data_availability = data_availability;
	}

	public List<String> getRegistrations() {
		return registrations;
	}

	public void setRegistrations(List<String> registrations) {
		this.registrations = registrations;
	}
}
