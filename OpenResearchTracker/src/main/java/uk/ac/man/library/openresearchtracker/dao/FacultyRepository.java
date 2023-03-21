package uk.ac.man.library.openresearchtracker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import uk.ac.man.library.openresearchtracker.entities.Faculty;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty,String>{
	
	@Query("SELECT case when count(f)> 0 then true else false end from Faculty f where f.faculty_name like :faculty_name")
	boolean existsByFacultyName(String faculty_name);
}
