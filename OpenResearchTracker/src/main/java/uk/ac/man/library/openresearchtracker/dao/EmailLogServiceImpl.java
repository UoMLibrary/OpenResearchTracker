package uk.ac.man.library.openresearchtracker.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.man.library.openresearchtracker.entities.EmailLog;

@Service
@Transactional
public class EmailLogServiceImpl implements EmailLogService{
	
	@Autowired
	private EmailLogRepository emailLogRepo;

	@Override
	public void save(EmailLog emailLog) {
		emailLogRepo.save(emailLog);
	}

	@Override
	public List<EmailLog> findAll() {
		return emailLogRepo.findAll();
	}

}
