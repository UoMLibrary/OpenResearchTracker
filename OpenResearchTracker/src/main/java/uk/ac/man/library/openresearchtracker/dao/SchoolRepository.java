package uk.ac.man.library.openresearchtracker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uk.ac.man.library.openresearchtracker.entities.School;


@Repository
public interface SchoolRepository extends JpaRepository<School,String>{

}
