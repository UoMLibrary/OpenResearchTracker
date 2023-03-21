package uk.ac.man.library.openresearchtracker.config;

import javax.servlet.Filter;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.boot.configuration.CasClientConfigurer;
import org.jasig.cas.client.boot.configuration.EnableCasClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.jaasapi.JaasApiIntegrationFilter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

// Security configuration to use when developing locally and no CAS authentication is required
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Profile("dev")
public class LocalSecurityConfiguration {

    @Value("${casLogoutUrl}")
    private String casLogoutUrl;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
    	// authenticate all requests
        http.authorizeRequests()
            .antMatchers("/**","/css/**", "/webjars/**", "/js/**").permitAll()
            .anyRequest().authenticated();

        return http.build();
    }
    
    // register the AuthFilter in the filter chain
    @Bean
    public FilterRegistrationBean<AuthFilter> authFilterBean(AuthFilter authFilter){
      final FilterRegistrationBean<AuthFilter> frb = new FilterRegistrationBean<AuthFilter>();
      frb.setFilter(authFilter);
      frb.addUrlPatterns("/*");
      frb.setName("AuthFilter");
      return frb;
    }
     
}
