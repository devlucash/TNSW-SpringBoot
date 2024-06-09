package org.seng2050.TNSW;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

   @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource, PasswordEncoder passwordEncoder) {
        return new JdbcUserDetailsManager(dataSource);
   }

	//main bean used to check security access across site pages
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {    

        httpSecurity.csrf(csrf -> csrf.disable());

        // add configuration
    
        httpSecurity.formLogin(formLogin -> {
            formLogin.loginPage("/login")
                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/homepage")
                .failureUrl("/error_login");
        });

        httpSecurity.logout(logout -> {
            logout.logoutUrl("/perform_logout")
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/login.html");
        });

        httpSecurity.authorizeHttpRequests(requestConstraints -> {
                
            // every user can access the login page
            requestConstraints.requestMatchers("/login.html").permitAll();
            requestConstraints.requestMatchers("/perform_login").permitAll(); // every user can try login
            requestConstraints.requestMatchers("/login*").permitAll();
            requestConstraints.requestMatchers("/style.css").permitAll();
            requestConstraints.requestMatchers("/error_login").permitAll();

            // autheticated users can access homepage. 
            requestConstraints.requestMatchers("/error*").permitAll(); 
            requestConstraints.requestMatchers("/homepage").permitAll();
            requestConstraints.requestMatchers("/issues*").permitAll();
            requestConstraints.requestMatchers("/SubmitReport*").permitAll();
            requestConstraints.requestMatchers("/homepage.html").permitAll();
            requestConstraints.requestMatchers("/login.html").permitAll();
            requestConstraints.requestMatchers("/process_query").permitAll();
        });

        return httpSecurity.build();
    }
}
