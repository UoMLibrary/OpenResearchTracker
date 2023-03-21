package uk.ac.man.library.openresearchtracker.controllers;


import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import uk.ac.man.library.openresearchtracker.dao.FacultyService;
import uk.ac.man.library.openresearchtracker.dao.PublicationService;
import uk.ac.man.library.openresearchtracker.dao.UserService;
import uk.ac.man.library.openresearchtracker.dto.PublicationDTO;
import uk.ac.man.library.openresearchtracker.entities.Publication;
import uk.ac.man.library.openresearchtracker.entities.User;
import uk.ac.man.library.openresearchtracker.oacp.OacpApi;
import uk.ac.man.library.openresearchtracker.oacp.OacpResponse;

@Controller
@RequestMapping("/")
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class.getName());
	
	@Value("${casLogoutUrl}")
    private String casLogoutUrl;
	
	@Value("${spring.profiles.active}")
	private String profile;
  
	@Autowired
	private OacpApi oacpApi;
		
	@Autowired
	UserService userService;
	
	@Autowired 
	PublicationService publicationService;
	
	@Autowired 
	FacultyService facultyService;
		
	@GetMapping("/")
	public ModelAndView homePage(ModelAndView modelAndView, HttpServletRequest request) {
		modelAndView.setViewName("index/index");
		logger.info("On home page...");
		User user = userService.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
		String spotId = (String) request.getSession().getAttribute("spotId");

		// check if user is known in the database
		if(user == null) {
			// add records associated with the user's spotId to be displayed
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
		}
		else { // load page as admin
			modelAndView.addObject("isAdmin", true);
		}
		return modelAndView;	
	}
	/*
	 * This method returns records stored in the database that admins will view
	 * Filters can be specified in the request to have control over what records are shown
	 * */
	@PreAuthorize("hasRole('ROLE_Super-Admin') or hasRole('ROLE_Admin')")
	@GetMapping("/getPublications")
	@ResponseBody
	public PublicationDTO retrievePublications(HttpServletRequest request, 
			@RequestParam("start") int start, @RequestParam("length") int length, @RequestParam("draw") int draw) {
		
		Page<Publication> publicationPage = getPublicationPage(request, start, length);
		PublicationDTO publicationDTO = new PublicationDTO();
		List<Publication> publicationList = publicationPage.toList();
		
		publicationDTO.setData(publicationList);
		publicationDTO.setDraw(draw);
		publicationDTO.setRecordsFiltered(publicationPage.getTotalElements());
		publicationDTO.setRecordsTotal(publicationPage.getTotalElements());
		return publicationDTO;
	}
	
	@PreAuthorize("hasRole('ROLE_Super-Admin') or hasRole('ROLE_Admin')")
	@GetMapping("/exportPublications")
	public void exportPublications(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// get list of publications based on request filters
		List<Publication> publicationList = getPublicationList(request);
		
		 response.setContentType("text/csv");
		 
		 DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
	     String currentDateTime = dateFormatter.format(new Date());
	     // set request header
	     String headerKey = "Content-Disposition";
	     String headerValue = "attachment; filename=ORT_" + currentDateTime + ".csv";
	     response.setHeader(headerKey, headerValue);
	     
	     ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
	     
	     // write headings for filters used
	     String[] csvFiltersHeader = {"Output Title", "Publication Status", "REF Compliance Status",
	    		 "OA Route", "Organisation", "Funder_Wellcome", "Funder_CRUK", "Funder_BHF",
	    		 "Funder_NIHR", "Funder_ERC", "Funder_UKRI"};
		 csvWriter.writeHeader(csvFiltersHeader);
		 
		 String title = request.getParameter("title").trim();
		 String pureStatus = request.getParameter("pureStatus");
		 String complianceStatus = request.getParameter("complianceStatus");
		 String oa_route = request.getParameter("oa_route");
		 String organisation = request.getParameter("organisation").trim();
		 String funder_Wellcome = request.getParameter("Wellcome");
		 String funder_CRUK = request.getParameter("CRUK");
		 String funder_BHF = request.getParameter("BHF");
		 String funder_NIHR = request.getParameter("NIHR");
		 String funder_ERC = request.getParameter("ERC");
		 String funder_UKRI = request.getParameter("UKRI");
		
		 String[] filters = {title, pureStatus, complianceStatus, oa_route, organisation, funder_Wellcome,
                 funder_CRUK, funder_BHF, funder_NIHR, funder_ERC, funder_UKRI};
		// write filter values used
		csvWriter.writeHeader(filters);
		// write new line 
		csvWriter.writeHeader("");
	     
	     CellProcessor[] processors = new CellProcessor[] {
	    		 new Optional(), // pureId
	    		 new Optional(), // publicationStatus
	    		 new Optional(), // output title
	    		 new Optional(), // ref compliance status
	    		 new Optional(), // publisher
	    		 new Optional(new FmtDate("dd/MM/yyyy")), // publicationDate 
	    		 new Optional(), // OA route
	    		 new Optional(), // status
	    		 new Optional(new FmtDate("dd/MM/yyyy")), // acceptedDate 
	    		 new Optional(new FmtDate("dd/MM/yyyy")), // createdDate 
	    		 new Optional(), // journal
	    		 new Optional(), // funder_Wellcome
	    		 new Optional(), // funder_CRUK
	    		 new Optional(), // funder_BHF
	    		 new Optional(), // funder_NIHR
	    		 new Optional(), // funder_ERC
	    		 new Optional(), // funder_UKRI
	     };
	     // write column headers for actual data being written
	     String[] csvHeader = {"Pure Id", "Publication Status", "Output Title", "REF Compliance Status",
	    		 "Publisher", "Publication Date", "OA Route", "Status", "Accepted Date", "Created Date",
	    		 "Journal", "Funder_Wellcome", "Funder_CRUK", "Funder_BHF", "Funder_NIHR",
	    		 "Funder_ERC", "Funder_UKRI"};
		 csvWriter.writeHeader(csvHeader);
		 // write name mappings for 'get' methods of publication objects for each field
		 String[] getters = {"pureId", "purestatus", "title", "compliancestatus",
	    		 "publisher", "publicationdate", "oaroute", "status", "accepteddate", "oacpcreatedDate",
	    		 "journal", "funderWellcome", "funderCRUK", "funderBHF", "funderNIHR",
	    		 "funderERC", "funderUKRI"};
		 // write each publication to csv file
	     for (Publication p : publicationList) {
	           csvWriter.write(p, getters, processors);
	     }
  
   	  	csvWriter.close();
   	  	logger.info("csv exported...");
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		request.getSession().invalidate();
		try {
			request.logout();
		} catch (ServletException e) {
			e.printStackTrace();
			logger.info("Logout unsuccessful");
		}
		return "redirect:" + casLogoutUrl;
	}
	// utility to get Direction from string used by data tables
	private Sort.Direction getSortDirection(String direction) {
		if (direction.equals("asc")) {
			return Sort.Direction.ASC;
		} else if (direction.equals("desc")) {
			return Sort.Direction.DESC;
		}
		return Sort.Direction.ASC;
	}
	/*
	 * Gets a list of publications based on the filters specified in the request
	 * */
	private List<Publication> getPublicationList(HttpServletRequest request){
		String title = request.getParameter("title").trim();
		String pureStatus = request.getParameter("pureStatus");
		String complianceStatus = request.getParameter("complianceStatus");
		String oa_route = request.getParameter("oa_route");
		String funder_Wellcome = request.getParameter("Wellcome");
		String funder_CRUK = request.getParameter("CRUK");
		String funder_BHF = request.getParameter("BHF");
		String funder_NIHR = request.getParameter("NIHR");
		String funder_ERC = request.getParameter("ERC");
		String funder_UKRI = request.getParameter("UKRI");
		
		
		String organisation = request.getParameter("organisation").trim();
		String organisationLevel;
		if (organisation.equals("")) {
			organisationLevel = "-1";
		}
		else if(facultyService.existsByFacultyName(organisation)) {
			organisationLevel = "2";
		}
		else {
			organisationLevel = "3";
		}

		String schoolName = "";
		String facultyName = "";

		if (organisationLevel != "-1") {
			if (organisationLevel.equals("2")) {
				facultyName = organisation;
			} else {
				schoolName = organisation;
			}
		}
		
		List<Publication> publicationList = publicationService.publicationFilter(title, pureStatus,
				complianceStatus, oa_route, 
				funder_Wellcome, 
				funder_CRUK, 
				funder_BHF, 
				funder_NIHR, 
				funder_ERC, 
				funder_UKRI,facultyName, schoolName);
		
		return publicationList;
	}
	/*
	 * Gets a Page of publications based on the filters in the request
	 * */
	private Page<Publication> getPublicationPage(HttpServletRequest request, int start, int length){
		String title = request.getParameter("title").trim();
		String pureStatus = request.getParameter("pureStatus");
		String complianceStatus = request.getParameter("complianceStatus");
		String oa_route = request.getParameter("oa_route");
		String funder_Wellcome = request.getParameter("Wellcome");
		String funder_CRUK = request.getParameter("CRUK");
		String funder_BHF = request.getParameter("BHF");
		String funder_NIHR = request.getParameter("NIHR");
		String funder_ERC = request.getParameter("ERC");
		String funder_UKRI = request.getParameter("UKRI");
		
		
		String organisation = request.getParameter("organisation").trim();
		String organisationLevel = request.getParameter("organisationLevel").trim();

		String schoolName = "";
		String facultyName = "";

		if (organisationLevel != "-1") {
			if (organisationLevel.equals("2")) {
				facultyName = organisation;
			} else {
				schoolName = organisation;
			}
		}
		
		String orderstr = request.getParameter("order[0][dir]");
		String orderNo = request.getParameter("order[0][column]");
		String ordercol = "columns[" + orderNo + "][data]";
		String orderColumn = request.getParameter(ordercol);
		Order order = new Order(getSortDirection(orderstr), orderColumn);
		int page = start / length;
		Pageable pageable = PageRequest.of(page, length, Sort.by(order));
		
		Page<Publication> publicationPage = publicationService.publicationFilter(title, pureStatus,
				complianceStatus, oa_route, 
				funder_Wellcome, 
				funder_CRUK, 
				funder_BHF, 
				funder_NIHR, 
				funder_ERC, 
				funder_UKRI,facultyName, schoolName, pageable);
		
		return publicationPage;
	}
}
