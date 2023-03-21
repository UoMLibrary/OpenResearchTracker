package uk.ac.man.library.openresearchtracker.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "schools")
public class School {
	
	@Id
	private int school_id;
	
	@Column(name = "school_name")
	@JsonProperty("school_name")
	private String school_name;
	
	public int getSchoolId() {
		return this.school_id;
	}
	
	public void setSchoolId(int school_id) {
		this.school_id = school_id;	
	}
	
	public String getSchoolName() {
		return this.school_name;
	}
	
	public void setSchoolName(String school_name) {
		this.school_name = school_name;	
	}
}
