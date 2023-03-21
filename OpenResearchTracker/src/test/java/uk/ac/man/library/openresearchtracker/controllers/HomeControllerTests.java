package uk.ac.man.library.openresearchtracker.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import java.util.Collection;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import uk.ac.man.library.openresearchtracker.controllers.HomeController;
import uk.ac.man.library.openresearchtracker.dao.FacultyService;
import uk.ac.man.library.openresearchtracker.dao.PublicationService;
import uk.ac.man.library.openresearchtracker.dao.UserService;
import uk.ac.man.library.openresearchtracker.entities.Publication;
import uk.ac.man.library.openresearchtracker.entities.User;
import uk.ac.man.library.openresearchtracker.oacp.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(SpringExtension.class)
@WebMvcTest(HomeController.class)
@AutoConfigureMockMvc(addFilters=false)
@WithMockUser(username = "user", roles = "User")
public class HomeControllerTests {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeControllerTests.class.getName());

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private OacpApi oacpApi;
	
	@MockBean 
	private UserService userService;
	
	@MockBean 
	private PublicationService publicationService;
	
	@MockBean
	private FacultyService facultyService;
		
	@Mock
	private OacpResponse oacpResponse;
	
	@Mock
	private MockHttpServletRequest request;
	
	@Mock
	private MockHttpSession session;
	
	@Mock
	private AttributePrincipal principal;
	
	@Mock
	private Assertion assertion;
	
	@Mock 
	private User user;
		
	private static Map<String, Object> principalAttrs;
	private static List<OacpPublication> publicationsList;
	
	private static String spotId = "9494799";
	
	@BeforeAll
	public static void init_all() {
	
		principalAttrs = new HashMap<String, Object>();
		principalAttrs.put("umanPersonID", spotId);
		
		publicationsList = new ArrayList<OacpPublication>();
		publicationsList.add(new OacpPublication());
	}
	
	@BeforeEach
	public void init_each() {
		
		when(principal.getName()).thenReturn("dev");
		when(principal.getAttributes()).thenReturn(principalAttrs);
		when(assertion.getPrincipal()).thenReturn(principal);
		when(session.getAttribute("_const_cas_assertion_")).thenReturn(assertion);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("spotId")).thenReturn(spotId);

		when(oacpApi.getData(anyString())).thenReturn(oacpResponse);
		when(oacpResponse.getResult()).thenReturn(publicationsList);
		when(oacpResponse.getError()).thenReturn(null);
		when(userService.findByUserName(anyString())).thenReturn(user);

	}
	
	@Test
	public void homePage_DataRetrievedError() throws Exception{
		
		when(oacpResponse.getError()).thenReturn("error message");
		when(oacpResponse.getResult()).thenReturn(null);
		when(request.isUserInRole(anyString())).thenReturn(true);
		when(userService.findByUserName(anyString())).thenReturn(null);
		
		mvc.perform(get("/").with(csrf())
				.accept(MediaType.TEXT_HTML)
				.session(session))
				.andExpect(status().isOk())
				.andExpect(view().name("index/index"))
				.andExpect(model().attributeExists("error"));
		
	}
	
	@Test
	public void homePage_DataRetrieved() throws Exception{
		when(request.isUserInRole(anyString())).thenReturn(true);
		when(userService.findByUserName(anyString())).thenReturn(null);

		mvc.perform(get("/").with(csrf())
				.accept(MediaType.TEXT_HTML)
				.session(session))
				.andExpect(status().isOk())
				.andExpect(view().name("index/index"))
				.andExpect(model().attributeExists("numPublications"))
				.andExpect(model().attributeExists("publications"));
		
	}
	
	@Test
	@WithMockUser(username = "user", roles = "Super-Admin")
	public void retrievePublication() throws Exception{
		Page<Publication> page = new PageImpl<Publication>(new ArrayList<Publication>());
		
		when(publicationService.publicationFilter(anyString(), anyString(), 
				anyString(), anyString(), anyString(), anyString(), anyString(),
				anyString(), anyString(), anyString(), anyString(),
				anyString(), any(Pageable.class))).thenReturn(page);
		
		mvc.perform(get("/getPublications").with(csrf())
				.session(session)
				.param("start", "0")
				.param("length", "20")
				.param("draw", "1")
				.queryParam("title", "").queryParam("pureStatus", "").queryParam("complianceStatus", "")
				.queryParam("oa_route", "")
				.queryParam("Wellcome", "")
				.queryParam("CRUK", "")
				.queryParam("BHF", "")
				.queryParam("NIHR", "")
				.queryParam("ERC", "")
				.queryParam("UKRI", "")
				.queryParam("organisation", "").queryParam("organisationLevel", "")
				.queryParam("order[0][dir]", "desc").queryParam("order[0][column]", "0").queryParam("columns[0][data]", "pureId"))
				.andExpect(status().isOk())
				.andExpect(content().json("{'draw':1, 'recordsTotal':0, 'recordsFiltered':0, 'data':[]}"));
		
		verify(publicationService, times(1)).publicationFilter(anyString(), anyString(), 
				anyString(), anyString(), anyString(), anyString(), anyString(),
				anyString(), anyString(), anyString(), anyString(),
				anyString(), any(Pageable.class));
	}
}
