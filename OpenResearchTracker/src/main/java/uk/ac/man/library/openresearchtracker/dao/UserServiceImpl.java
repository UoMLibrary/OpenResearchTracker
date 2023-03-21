package uk.ac.man.library.openresearchtracker.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.man.library.openresearchtracker.entities.User;

@Service
@Transactional
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public User findByUserName(String userName) {
		return userRepo.findByUserName(userName);
	}
	
	@Override
	public User save(User user) {
		return userRepo.save(user);
	}

	@Override
	public Iterable<User> findAll() {
		return userRepo.findAll();
	}

	@Override
	public void deleteByUserName(String userName) {
		userRepo.deleteByUserName(userName);
		
	}

}
