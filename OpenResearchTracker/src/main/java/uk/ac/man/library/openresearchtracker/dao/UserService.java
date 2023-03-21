package uk.ac.man.library.openresearchtracker.dao;

import uk.ac.man.library.openresearchtracker.entities.User;

public interface UserService {

	public User findByUserName(String userName);

	public User save(User user);
	
	public Iterable<User> findAll();

	public void deleteByUserName(String userName);

}
