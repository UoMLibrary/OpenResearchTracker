package uk.ac.man.library.openresearchtracker.dao;

import org.springframework.data.repository.CrudRepository;

import uk.ac.man.library.openresearchtracker.entities.User;

public interface UserRepository extends CrudRepository<User,String>{

	public User findByUserName(String userName);

	public void deleteByUserName(String userName);
	
}
