package uk.ac.man.library.openresearchtracker.oacp;

import java.util.List;

public class OacpResponse {
	
	private String error;
	
	private Query query;
	
	private List<OacpPublication> result;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

	public List<OacpPublication> getResult() {
		return result;
	}

	public void setResult(List<OacpPublication> result) {
		this.result = result;
	}
}
