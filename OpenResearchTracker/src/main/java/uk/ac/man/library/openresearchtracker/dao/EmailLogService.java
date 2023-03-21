package uk.ac.man.library.openresearchtracker.dao;

import java.util.List;

import uk.ac.man.library.openresearchtracker.entities.EmailLog;

public interface EmailLogService {
	
	public void save(EmailLog emailLog);
	
	public List<EmailLog> findAll();

}
