package uk.ac.man.library.openresearchtracker.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import uk.ac.man.library.openresearchtracker.dao.PublicationService;
import uk.ac.man.library.openresearchtracker.entities.Publication;
import uk.ac.man.library.openresearchtracker.oacp.OacpApi;
import uk.ac.man.library.openresearchtracker.oacp.OacpPublication;
import uk.ac.man.library.openresearchtracker.oacp.OacpScholarcyResponse;

@Controller
public class PublicationController {
	
	private static final Logger logger = LoggerFactory.getLogger(PublicationController.class.getName());
	
	@Autowired
	private OacpApi oacpApi;
	
	@Autowired 
	PublicationService publicationService;
	
	@PreAuthorize("hasRole('ROLE_Super-Admin') or hasRole('ROLE_Admin')")
	@GetMapping("/admin/publication/{pureId}")
	public ModelAndView adminPublicationPage(ModelAndView modelAndView, HttpServletRequest request, 
			@PathVariable("pureId") String pureId) {
		modelAndView.setViewName("publication/publication_view");
		modelAndView.addObject("isAdmin", true);

		logger.info("finding record with pureId:" + pureId);
		Optional<Publication> pub = publicationService.findByPureId(pureId);
		// if record exists
		if (pub.isPresent()) {
			modelAndView.addObject("publication", pub.get());
			logger.info("record exists...");
		}
		else {
			modelAndView.addObject("error", "Unable to retrieve publication with Pure Id: " + pureId);
			logger.info("record not found...");
		}
		
		return modelAndView;
	}
	
	@GetMapping("/publication/{pureId}")
	public ModelAndView publicationPage(ModelAndView modelAndView, HttpServletRequest request, 
			@PathVariable("pureId") String pureId) {
		modelAndView.setViewName("publication/publication_view");
		logger.info("finding record with pureId:" + pureId);
		// get users spotId or set one for super-admins
		String spotId;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Super-Admin"))) {
			spotId = "superadmin";
		}else
		{
			spotId = (String) request.getSession().getAttribute("spotId");
		}
		
		OacpScholarcyResponse response = oacpApi.getRecordDataByPureId(pureId, spotId);
		// if record exists
		if (response.getError() == null) {
			// add publication and scholarcy data to model
			modelAndView.addObject("publication", response.getRecord());
			modelAndView.addObject("scholarcy", response.getScholarcy());
		}
		else {
			modelAndView.addObject("error", response.getError());
		}
		
		return modelAndView;
	}
}
