package uk.ac.man.library.openresearchtracker.oacp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import uk.ac.man.library.openresearchtracker.dao.FacultyRepository;
import uk.ac.man.library.openresearchtracker.dao.PublicationService;
import uk.ac.man.library.openresearchtracker.dao.SchoolRepository;
import uk.ac.man.library.openresearchtracker.entities.Faculty;
import uk.ac.man.library.openresearchtracker.entities.Publication;
import uk.ac.man.library.openresearchtracker.entities.School;
@Component
public class OacpApi {
	private static final Logger logger = LoggerFactory.getLogger(OacpApi.class.getName());
	
	@Value("${oacpApiUrl}")
	private String getDataUrl;
	
	@Value("${oacpApiKey}")
	private String apiKey;
	
	@Autowired
	PublicationService publicationService;
	
	@Autowired
	FacultyRepository facultyRepo;
	
	@Autowired
	SchoolRepository schoolRepo;
		
	public OacpResponse getData(String spotId) {
		OacpResponse result;
		try {
			RestTemplate restTemplate = new RestTemplate();
			String requestUrl = getDataUrl + "/publication/spotID/" + spotId + "?apikey=" + apiKey;
			result = restTemplate.getForObject(requestUrl, OacpResponse.class);
			logger.info("OacpApi call is successful.");
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Exception thrown when trying to retrieve data from oacpv2.", e);
			result = new OacpResponse();
			result.setError("Exception when trying to retrieve data.");
		} 
		
		return result;
		
	}
	
	public OacpScholarcyResponse getRecordDataByPureId(String pureId, String spotId) {

		OacpScholarcyResponse result;
		try {
			RestTemplate restTemplate = new RestTemplate();
			String requestUrl = getDataUrl + "/publication/scholarcy"
					+ "?pureID=" + pureId 
					+ "&spotID=" + spotId
					+ "&apikey=" + apiKey;
			result = restTemplate.getForObject(requestUrl, OacpScholarcyResponse.class);
			logger.info("OacpApi call is successful.");
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Exception thrown when trying to retrieve data from oacpv2.", e);
			result = new OacpScholarcyResponse();
			result.setError("Exception when trying to retrieve data.");
		} 
		
		return result;
		
	}
	// oacp sync at 1am daily, so schedule this method for 4am?
	 @Scheduled(cron = "00 00 04 * * ?" , zone = "Europe/London")
	public void publicationSync() {
		OacpResponse response;
		try {
			RestTemplate restTemplate = new RestTemplate();
			String requestUrl = getDataUrl + "/publication/spotID/" + "?apikey=" + apiKey;
			response = restTemplate.getForObject(requestUrl, OacpResponse.class);
			logger.info("Received all publications from OacpApi.");
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Exception thrown when trying to retrieve all publications from oacpv2.", e);
			response = new OacpResponse();
			response.setError("Exception when trying to retrieve data.");
			return;
		}
		// delete all data in the DB after getting a response from OACP
		publicationService.deleteAll();
		logger.info("purged all existing data ready for sync ...");
		
		Publication pub = new Publication();
		
		int faculty_id = 0;
		HashMap<String, Integer> faculty_ids = new HashMap<String, Integer>();
		
		int school_id = 0;
		HashMap<String, Integer> school_ids = new HashMap<String, Integer>();
		
		// save the new, up-to-date data
		for (OacpPublication p : response.getResult()) {
			
			pub.setPureId(p.getPureId());
			pub.setPureStatus(p.getPureStatus());
			pub.setTitle(p.getTitle());
			pub.setJournal(p.getJournal());
			pub.setComplianceStatus(p.getComplianceStatus());
			pub.setAcceptedDate(p.getAcceptedDate());
			pub.setPublicationDate(p.getPublicationDate());
			pub.setOARoute(p.getOARoute());
			pub.setStatus(p.getStatus());
			pub.setPublisher(p.getPublisher());
			pub.setFunderWellcome(p.getFunderWellcome());
			pub.setFunderCRUK(p.getFunderCRUK());
			pub.setFunderBHF(p.getFunderBHF());
			pub.setFunderNIHR(p.getFunderNIHR());
			pub.setFunderERC(p.getFunderERC());
			pub.setFunderUKRI(p.getFunderUKRI());
			pub.setOacpCreatedDate(p.getOacpCreatedDate());
			
			HashSet<Faculty> faculties = new HashSet<Faculty>();
			HashSet<School> schools = new HashSet<School>();
			
			String faculty_name;
			String school_name;
			// extract list of faculties and schools from authors
			for (OacpAuthor a : p.getAuthors()) {
				
				faculty_name = a.getFacultyName();
				school_name = a.getSchoolName();
				
				if (faculty_name != null && !faculty_name.equals("")) {
					Faculty f = new Faculty();
					f.setFacultyName(faculty_name);
					
					if (faculty_ids.get(faculty_name) == null) {
						faculty_ids.put(faculty_name, faculty_id);
						f.setFacultyId(faculty_id);
						facultyRepo.save(f);
						faculty_id += 1;
					}else {
						f.setFacultyId(faculty_ids.get(faculty_name));
					}
					faculties.add(f);
				}
				
				if (school_name == null || school_name.equals("")) {
					continue;
				}
				School s = new School();
				s.setSchoolName(school_name);

				if (school_ids.get(school_name) == null) {
					school_ids.put(school_name, school_id);
					s.setSchoolId(school_id);
					schoolRepo.save(s);
					school_id += 1;
				}else {
					s.setSchoolId(school_ids.get(school_name));
				}
				
				schools.add(s);
			}
			pub.setFaculties(faculties);
			pub.setSchools(schools);
			publicationService.save(pub);
		
		}
		logger.info("Sync complete");
	}
}
