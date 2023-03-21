package uk.ac.man.library.openresearchtracker.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import uk.ac.man.library.openresearchtracker.config.AuthFilter;
import uk.ac.man.library.openresearchtracker.dao.PublicationService;
import uk.ac.man.library.openresearchtracker.dao.UserService;
import uk.ac.man.library.openresearchtracker.entities.User;
import uk.ac.man.library.openresearchtracker.oacp.OacpApi;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SuperAdminController.class)
@AutoConfigureMockMvc(addFilters=false)
@WithMockUser(username = "user", roles = "Super-Admin")
public class SuperAdminControllerTests {
	private static final Logger logger = LoggerFactory.getLogger(SuperAdminControllerTests.class.getName());
	
	@MockBean
	private OacpApi oacpAPi;
	
	@MockBean
	private static AuthFilter authFilter;
	
	@Autowired
	private MockMvc mvc;
	
	@Mock
	private MockHttpSession session;
	
	@Mock private User user;
	
	@MockBean
	UserService userService;
	
	@MockBean
	PublicationService publicationService;
	
	@Test
	public void getUsers() throws Exception{
		mvc.perform(get("/superadmin/users")
				.with(csrf()))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("users"));
	}
	
	@Test
	public void addUser() throws Exception{
		String payload = "userName=userName&fullName=fullName&role=role";

		when(userService.findByUserName(anyString())).thenReturn(null);
		
		mvc.perform(post("/superadmin/users/add")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.content(payload))
		.andExpect(status().isOk())
		.andExpect(content().string("added"));

		verify(userService, times(1)).save(any(User.class));
	}
	
	@Test
	public void addUser_UserExists() throws Exception{
		
		String payload = "userName=userName&fullName=fullName&role=role";

		when(userService.findByUserName(anyString())).thenReturn(user);
		
		mvc.perform(post("/superadmin/users/add")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.content(payload))
		.andExpect(status().isOk())
		.andExpect(content().string("exists"));
		
		verify(userService, never()).save(any(User.class));
	}
	
	@Test
	public void deleteUser_NullUser() throws Exception{
		String payload = "userName=userName";
		when(userService.findByUserName(anyString())).thenReturn(null);
		
		mvc.perform(delete("/superadmin/users/delete")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.content(payload))
		.andExpect(status().isOk())
		.andExpect(content().string("user doesn't exist"));

		verify(userService, never()).deleteByUserName(anyString());
	}
	
	@Test
	public void deleteUser() throws Exception{
		String payload = "userName=userName";

		when(userService.findByUserName(anyString())).thenReturn(user);
		
		mvc.perform(delete("/superadmin/users/delete")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.content(payload))
		.andExpect(status().isOk())
		.andExpect(content().string("deleted"));

		verify(userService, times(1)).deleteByUserName(anyString());
	}
	
	@Test
	public void updateUser_NullUser() throws Exception{
		
		String payload = "userName=userName&fullName=fullName&role=role";

		when(userService.findByUserName(anyString())).thenReturn(null);
		
		mvc.perform(put("/superadmin/users/update")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.content(payload))
		.andExpect(status().isOk())
		.andExpect(content().string("user doesn't exist"));

		verify(userService, never()).save(any(User.class));
	}
	
	@Test
	public void updateUser() throws Exception{
		String payload = "userName=userName&fullName=fullName&role=role";

		when(userService.findByUserName(anyString())).thenReturn(user);
		
		mvc.perform(put("/superadmin/users/update")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.content(payload))
		.andExpect(status().isOk())
		.andExpect(content().string("updated"));

		verify(userService, times(1)).save(any(User.class));
	}
	
}
