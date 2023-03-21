package uk.ac.man.library.openresearchtracker.oacp;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OacpScholarcyResponse {
	
	private String error;
	
	private OacpPublication record;
		
	private OacpScholarcy scholarcy;

	public OacpScholarcy getScholarcy() {
		return scholarcy;
	}

	public void setScholarcy(OacpScholarcy scholarcy) {
		this.scholarcy = scholarcy;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public OacpPublication getRecord() {
		return record;
	}

	public void setRecord(OacpPublication record) {
		this.record = record;
	}

}
