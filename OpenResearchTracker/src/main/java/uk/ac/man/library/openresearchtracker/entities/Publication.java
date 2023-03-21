package uk.ac.man.library.openresearchtracker.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "publications")
public class Publication {
	
	@Id
	@Column(name = "pureId")
	@JsonProperty("pureId")
	private String pureId;
	
	@Column(name = "pure_status")
	@JsonProperty("pure_status")
	private String pure_status;
	
	@Column(name = "title", length = 2048)
	@JsonProperty("title")
	private String title;
	
	@Column(name = "journal", length = 2048)
	@JsonProperty("journal")
	private String journal;
	
	@Column(name = "compliance_status")
	@JsonProperty("compliance_status")
	private String compliance_status;
		
	@Column(name = "accepted_date")
	@JsonProperty("accepted_date")
	private Date accepted_date;
	
	@Column(name = "publication_date")
	@JsonProperty("publication_date")
	private Date publication_date;
	
	@Column(name = "oa_route")
	@JsonProperty("oa_route")
	private String oa_route;
	
	@Column(name = "status")
	@JsonProperty("status")
	private String status;
	
	@Column(name = "publisher")
	@JsonProperty("publisher")
	private String publisher;
	
	@Column(name = "funder_Wellcome")
	@JsonProperty("funder_Wellcome")
	private String funder_Wellcome;
	
	@Column(name = "funder_CRUK")
	@JsonProperty("funder_CRUK")
	private String funder_CRUK;
	
	@Column(name = "funder_BHF")
	@JsonProperty("funder_BHF")
	private String funder_BHF;
	
	@Column(name = "funder_NIHR")
	@JsonProperty("funder_NIHR")
	private String funder_NIHR;
	
	@Column(name = "funder_ERC")
	@JsonProperty("funder_ERC")
	private String funder_ERC;
	
	@Column(name = "funder_UKRI")
	@JsonProperty("funder_UKRI")
	private String funder_UKRI;
	
	@JsonProperty("faculties")
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH})
	private Set<Faculty> faculties;
	
	@JsonProperty("schools")
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH})
	private Set<School> schools;
	
	@Column(name = "oacp_createdDate")
	@JsonProperty("oacp_createdDate")
	private Date oacp_createdDate;
	
	public String getPureId() {
		return this.pureId;	
	}
	
	public void setPureId(String pureId) {
		this.pureId = pureId;
	}
	
	public String getTitle() {
		return this.title;	
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getJournal() {
		return this.journal;	
	}
	
	public void setJournal(String journal) {
		this.journal = journal;
	}
	
	public String getPureStatus() {
		return this.pure_status;
	}
	
	public void setPureStatus(String pureStatus) {
		this.pure_status = pureStatus;
	}

	public String getComplianceStatus() {
		return compliance_status;
	}

	public void setComplianceStatus(String complianceStatus) {
		this.compliance_status = complianceStatus;
	}

	public Date getAcceptedDate() {
		return accepted_date;
	}

	public void setAcceptedDate(Date acceptedDate) {
		this.accepted_date = acceptedDate;
	}

	public Date getPublicationDate() {
		return publication_date;
	}

	public void setPublicationDate(Date publicatiionDate) {
		this.publication_date = publicatiionDate;
	}
	
	public String getOARoute() {
		return oa_route;
	}
	
	public void setOARoute(String oa_route) {
		this.oa_route = oa_route;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getPublisher() {
		return publisher;
	}
	
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	
	public String getFunderWellcome() {
		return funder_Wellcome;
	}
	
	public void setFunderWellcome(String string) {
		this.funder_Wellcome = string;
	}
	
	public String getFunderCRUK() {
		return funder_CRUK;
	}
	
	public void setFunderCRUK(String funder_CRUK) {
		this.funder_CRUK = funder_CRUK;
	}
	
	public String getFunderBHF() {
		return funder_BHF;
	}
	
	public void setFunderBHF(String funder_BHF) {
		this.funder_BHF = funder_BHF;
	}
	
	public String getFunderNIHR() {
		return funder_NIHR;
	}
	
	public void setFunderNIHR(String funder_NIHR) {
		this.funder_NIHR = funder_NIHR;
	}
	
	public String getFunderERC() {
		return funder_ERC;
	}
	
	public void setFunderERC(String funder_ERC) {
		this.funder_ERC = funder_ERC;
	}
	
	public String getFunderUKRI() {
		return funder_UKRI;
	}
	
	public void setFunderUKRI(String funder_UKRI) {
		this.funder_UKRI = funder_UKRI;
	}
	
	public Set<School> getSchools() {
		return this.schools;
	}
	
	public void setSchools(Set<School> schools) {
		this.schools = schools;
	}
	
	public Set<Faculty> getFaculties() {
		return this.faculties;
	}
	
	public void setFaculties(Set<Faculty> faculties) {
		this.faculties = faculties;
	}
	
	public Date getOacpCreatedDate() {
		return oacp_createdDate;
	}

	public void setOacpCreatedDate(Date oacp_createdDate) {
		this.oacp_createdDate = oacp_createdDate;
	}

}
