package uk.ac.man.library.openresearchtracker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uk.ac.man.library.openresearchtracker.entities.EmailLog;


@Repository
public interface EmailLogRepository extends JpaRepository<EmailLog,String>{

}
