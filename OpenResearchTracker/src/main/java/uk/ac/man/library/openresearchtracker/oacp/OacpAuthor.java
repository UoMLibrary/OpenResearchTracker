package uk.ac.man.library.openresearchtracker.oacp;

import com.fasterxml.jackson.annotation.JsonProperty;

import uk.ac.man.library.openresearchtracker.entities.Faculty;
import uk.ac.man.library.openresearchtracker.entities.School;

public class OacpAuthor {

	@JsonProperty("spotID")
	private String spotID;
	
	@JsonProperty("full_name")
	private String full_name;
	
	@JsonProperty("school_name")
	private String school_name;
	
	@JsonProperty("faculty_name")
	private String faculty_name;
	
	public String getSpotId() {
		return this.spotID;
	}
	
	public void setSpotId(String spotID) {
		this.spotID = spotID;
	}
	
	public String getFullName() {
		return this.full_name;
	}
	
	public void setFullName(String full_name) {
		this.full_name = full_name;
	}
	
	public String getSchoolName() {
		return this.school_name;
	}
	
	public void setSchoolName(String school_name) {
		this.school_name = school_name;
	}
	
	public String getFacultyName() {
		return this.faculty_name;
	}
	
	public void setFacultyName(String faculty_name) {
		this.faculty_name = faculty_name;
	}
}
