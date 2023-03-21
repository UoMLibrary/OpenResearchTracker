package uk.ac.man.library.openresearchtracker.controllers;



import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import uk.ac.man.library.openresearchtracker.dao.EmailLogService;
import uk.ac.man.library.openresearchtracker.dao.UserService;
import uk.ac.man.library.openresearchtracker.dto.EmailLogDTO;
import uk.ac.man.library.openresearchtracker.entities.EmailLog;

@Controller
@RequestMapping("/superadmin/email")
@PreAuthorize("hasRole('ROLE_Super-Admin')")
public class EmailController {
	private static final Logger logger = LoggerFactory.getLogger(EmailController.class.getName());
	
	@Autowired
	UserService userService;
	
	@Autowired
	EmailLogService emailLogService;
	
	@GetMapping
	public ModelAndView emailPage(ModelAndView modelAndView) {
		
		// add email logs to the model so that they can  be displayed in data table on page
		List<EmailLog> emailLogs = emailLogService.findAll();
		List<EmailLogDTO> emailLogsDTO = new ArrayList<EmailLogDTO>();
		
		for(EmailLog log : emailLogs) {
			emailLogsDTO.add(new EmailLogDTO(log));
		}
		modelAndView.addObject("emailLogs", emailLogsDTO);
		
		modelAndView.setViewName("admin/email");
		return modelAndView;
	}
	
	@PostMapping("/send")
	@ResponseBody
	public String sendEmail(EmailLog emailLog) {
				
		logger.info("Sending email...");
		// send email
		
		// store response in DB
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		
		emailLog.setSender(userService.findByUserName(username));
		
		emailLogService.save(emailLog);
		
		logger.info("Email sent by user: " + username);
		//return response
		return "success";
		
		
	}
	
}
