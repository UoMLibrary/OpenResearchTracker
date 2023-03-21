package uk.ac.man.library.openresearchtracker.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "faculties")
public class Faculty {
	
	@Id
	private int faculty_id;
	
	@Column(name = "faculty_name")
	@JsonProperty("faculty_name")
	private String faculty_name;
	
	public int getFacultyId() {
		return this.faculty_id;
	}
	
	public void setFacultyId(int faculty_id) {
		this.faculty_id = faculty_id;	
	}
	
	public String getFacultyName() {
		return this.faculty_name;
	}
	
	public void setFacultyName(String faculty_name) {
		this.faculty_name = faculty_name;	
	}
}
