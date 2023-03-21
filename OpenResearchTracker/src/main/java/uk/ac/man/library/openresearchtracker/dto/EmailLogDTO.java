package uk.ac.man.library.openresearchtracker.dto;


import uk.ac.man.library.openresearchtracker.entities.EmailLog;


public class EmailLogDTO {
	
	private String sender;
	
	private String subject;
	
	private String message;
	
	public EmailLogDTO(EmailLog emailLog) {
		this.subject = emailLog.getSubject();
		this.message = emailLog.getMessage();	
		this.sender = emailLog.getSender().getFullName();
		
	}
	
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
