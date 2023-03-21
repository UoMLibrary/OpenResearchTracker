package uk.ac.man.library.openresearchtracker.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FacultyServiceImpl implements FacultyService{
	
	@Autowired
	private FacultyRepository facultyRepo;


	@Override
	public boolean existsByFacultyName(String faculty_name) {
		return facultyRepo.existsByFacultyName(faculty_name);
		
	}

}
