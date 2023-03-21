package uk.ac.man.library.openresearchtracker.controllers;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import uk.ac.man.library.openresearchtracker.dao.UserService;
import uk.ac.man.library.openresearchtracker.entities.User;
import uk.ac.man.library.openresearchtracker.oacp.OacpApi;
import uk.ac.man.library.openresearchtracker.oacp.OacpResponse;

@Controller
@RequestMapping("/superadmin")
@PreAuthorize("hasRole('ROLE_Super-Admin')")
public class SuperAdminController {
	private static final Logger logger = LoggerFactory.getLogger(SuperAdminController.class.getName());
	
	@Autowired
	UserService userService;
		
	@Autowired
	private OacpApi oacpApi;
	
	@GetMapping("/users")
	public ModelAndView userManagement(ModelAndView modelAndView) {
		
		Iterable<User> users = userService.findAll();
		modelAndView.addObject("users", users);
		
		modelAndView.setViewName("admin/users");
		return modelAndView;
	}
	
	@PostMapping("/users/add")
	@ResponseBody
	public String addUser(User postedUser) {

		String response;
		String userName = postedUser.getUserName().toLowerCase().strip();
		String fullName = postedUser.getFullName();
		String role = postedUser.getRole();
		User user = null;
		try {
			user = userService.findByUserName(userName);
		}catch(Exception e) {
			e.printStackTrace();
			logger.error("Error finding user with username: " + userName);
		}

		if(user==null) {
			user = new User();
			user.setUserName(userName);
			user.setFullName(fullName);
			user.setRole(role);
			userService.save(user);
			response = "added";
		}else {
			response =  "exists";
		}
		return response;
	}
	
	@DeleteMapping("/users/delete")
	@ResponseBody
	public String deleteUser(String userName) {		// check username exists
		String response;
		User user = null;
		try {
			user = userService.findByUserName(userName);
		}catch(Exception e) {
			e.printStackTrace();
			logger.error("Error finding user with username: " + userName);
		}
		if(user!=null) {
			response = "deleted";
			userService.deleteByUserName(userName);
		}else {
			response = "user doesn't exist";
		}				
		return response;
	}
	
	@PutMapping("/users/update")
	@ResponseBody
	public String updateUser(User updatedUser) {
		String response;
		String userName = updatedUser.getUserName().toLowerCase();
		User user = null;
		try {
			user = userService.findByUserName(userName);
		}catch(Exception e) {
			e.printStackTrace();
			logger.error("Error finding use with username: " + userName);
		}
		if(user!=null) {
			userService.save(updatedUser);
			response = "updated";
		}else {
			response =  "user doesn't exist";
		}
		return response;
	}
	
	@GetMapping("/userview")
	public ModelAndView userview(ModelAndView modelAndView) {
		modelAndView.setViewName("/admin/userview");
		return modelAndView;
	}
	
	@PostMapping("/userview")
	public String userview(ModelAndView modelAndView, String spotId) {
		return "redirect:/superadmin/userview/" + spotId;
	}
	
	@GetMapping("/userview/{spotId}")
	public ModelAndView impersonateUser(ModelAndView modelAndView, @PathVariable("spotId") String spotId) 
	{
		modelAndView.setViewName("index/index");
		modelAndView.addObject("userview", true);

		logger.info("SpotID: " + spotId);

		OacpResponse response = oacpApi.getData(spotId);
		// if there is an error, the template will render differently than if there were no publications for a user
		if (response.getError() != null) {
			if (response.getError().equals("There is no record associated with the spotID: " + spotId)) {
				modelAndView.addObject("noRecords", true);
			}
			
			modelAndView.addObject("error", response.getError());
			return modelAndView;
		}
		
		modelAndView.addObject("publications", response.getResult());		
		modelAndView.addObject("numPublications", response.getResult().size());
		
		return modelAndView;
		
	}
}
