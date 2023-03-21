package uk.ac.man.library.openresearchtracker.config;

import org.jasig.cas.client.boot.configuration.EnableCasClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

// Security configuration to use when CAS is enabled
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableCasClient
@Profile("!dev")
public class SecurityConfiguration {

    @Value("${casLogoutUrl}")
    private String casLogoutUrl;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	// authenticate all requests and set logout url
        http.authorizeRequests()
            .antMatchers("/**","/logout", "/css/**", "/webjars/**", "/js/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .logout().logoutUrl("/logout").logoutSuccessUrl(casLogoutUrl).invalidateHttpSession(true);
        
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
