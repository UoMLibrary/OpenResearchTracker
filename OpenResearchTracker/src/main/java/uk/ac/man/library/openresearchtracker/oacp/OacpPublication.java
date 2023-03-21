package uk.ac.man.library.openresearchtracker.oacp;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;


public class OacpPublication {
	
	@JsonProperty("pureId")
	private String pureId;
	
	@JsonProperty("pure_status")
	private String pure_status;
	
	@JsonProperty("title")
	private String title;	
	
	@JsonProperty("journal")
	private String journal;	
	
	@JsonProperty("compliance_status")
	private String compliance_status;
	
	@JsonProperty("accepted_date")
	private Date accepted_date;
	
	@JsonProperty("publication_date")
	private Date publication_date;
	
	@JsonProperty("oa_route")
	private String oa_route;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("publisher")
	private String publisher;
	
	@JsonProperty("welcome_funder")
	private String funder_Wellcome;
	
	@JsonProperty("cruk_funder")
	private String funder_CRUK;
	
	@JsonProperty("bhf_funder")
	private String funder_BHF;
	
	@JsonProperty("nihr_funder")
	private String funder_NIHR;
	
	@JsonProperty("erc_funder")
	private String funder_ERC;
	
	@JsonProperty("ukri_funder")
	private String funder_UKRI;
	
	@JsonProperty("authors")
	private Set<OacpAuthor> authors;
	
	@JsonProperty("oacp_createdDate")
	private Date oacp_createdDate;
	
	public String getPureId() {
		return this.pureId;	
	}
	
	public void setPureId(String pureId) {
		this.pureId = pureId;
	}
	
	public String getPureStatus() {
		return this.pure_status;
	}
	
	public void setPureStatus(String pureStatus) {
		this.pure_status = pureStatus;
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
	
	public void setFunderWellcome(String funder_Wellcome) {
		this.funder_Wellcome = funder_Wellcome;
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
		
	public Set<OacpAuthor> getAuthors(){
		return this.authors;
	}
	
	public void setAuthors(Set<OacpAuthor> authors) {
		this.authors = authors;
	}
	
	public Date getOacpCreatedDate() {
		return oacp_createdDate;
	}

	public void setOacpCreatedDate(Date oacp_createdDate) {
		this.oacp_createdDate = oacp_createdDate;
	}
}
