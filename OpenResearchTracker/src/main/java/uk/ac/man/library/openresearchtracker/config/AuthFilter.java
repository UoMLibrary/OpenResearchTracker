package uk.ac.man.library.openresearchtracker.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import uk.ac.man.library.openresearchtracker.dao.UserService;
import uk.ac.man.library.openresearchtracker.entities.User;

// Authentication filter called on each request
@Component
public class AuthFilter implements Filter{
	private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private HttpSession httpSession;
		
	@Value("${spring.profiles.active}")
	private String profile;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String spotId;
		String userName;
		// manually set spotId and userName when working locally
		if(profile.equals("dev")) {
			spotId = "9934041";
			userName = "test";
		}
		// get spotId and userName from data passed by CAS login
		else {
			Assertion a = (Assertion) httpSession.getAttribute("_const_cas_assertion_");
			
			AttributePrincipal p = a.getPrincipal();
			Map<String, Object> attrs = p.getAttributes();
			spotId = (String) attrs.get("umanPersonID");
			userName = p.getName().toLowerCase();
		}
		// set spotId in session
		httpSession.setAttribute("spotId", spotId);
		
		// add user to security context
		User user = userService.findByUserName(userName);
		if (SecurityContextHolder.getContext().getAuthentication() == null 
				|| !SecurityContextHolder.getContext().getAuthentication().getName().equals(userName)) {
			if (user != null) {
				logger.info("User is found in DB...");
				// gives user role stored for their username in the database
				Authentication auth = new UsernamePasswordAuthenticationToken(userName, null, 
						AuthorityUtils.createAuthorityList("ROLE_" + user.getRole()));
				SecurityContextHolder.getContext().setAuthentication(auth);
				logger.info("ROLE: ROLE_" + user.getRole());
				httpSession.setAttribute("fullName", user.getFullName());
			}else {
				// gives standard role if user isn't an admin/super-admin
				logger.info("User is not found in DB...");
				Authentication auth = new UsernamePasswordAuthenticationToken(userName, null, 
						AuthorityUtils.createAuthorityList("ROLE_User"));
				SecurityContextHolder.getContext().setAuthentication(auth);
				logger.info("ROLE: ROLE_User");
			}
		}
		
		chain.doFilter(request, response);
	}
	  
}