package net.icestone.authserver.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import net.icestone.authserver.io.entity.EAuthority;
import net.icestone.authserver.service.UserService;

@EnableWebSecurity(debug = true)
public class WebSecurityConfig {
	
	private final UserService userService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public WebSecurityConfig( UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userService = userService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		
		AuthenticationManagerBuilder authenticationManagerBuilder =
				http.getSharedObject(AuthenticationManagerBuilder.class);
		
		authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);

		http.authorizeHttpRequests( (auth)->auth
				.antMatchers("/api/test/auth").authenticated()
				.antMatchers("/api/test/admin").hasRole("ADMIN")
//				.antMatchers("/api/test/user").hasRole("USER")
				.antMatchers("/api/test/user").hasAuthority(EAuthority.DELETE.toString())
				//.antMatchers("/api/test/mod").hasRole("MODERATOR")
				.antMatchers("/api/test/all").permitAll()
		).httpBasic(Customizer.withDefaults());
		return http.build();

	}
	
	
}
