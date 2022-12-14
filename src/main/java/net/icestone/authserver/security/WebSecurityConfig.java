package net.icestone.authserver.security;

import java.util.Arrays;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import net.icestone.authserver.filter.JWTTokenGeneratorFilter;
import net.icestone.authserver.filter.JWTTokenValidatorFilter;
import net.icestone.authserver.filter.RequestValidationBeforeFilter;
import net.icestone.authserver.service.UserService;

@EnableWebSecurity(debug = true)
public class WebSecurityConfig {
	
	@Autowired
	private CustomPwdAuthenticationProvider customPwdAuthenticationProvider;
	
	private final UserService userService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public WebSecurityConfig( UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userService = userService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		
//		AuthenticationManagerBuilder authenticationManagerBuilder =
//				http.getSharedObject(AuthenticationManagerBuilder.class);
//		
//		authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);

		http
		
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//		.cors().disable()
		.cors().configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
//                config.setAllowedOrigins(Arrays.asList("http://localhost"));
//                config.setAllowedOrigins(Collections.singletonList("http://localhost:8080"));
                config.setAllowedOriginPatterns(Collections.singletonList("*"));
                config.setAllowedMethods(Collections.singletonList("*"));
                config.setAllowCredentials(true);
                config.setAllowedHeaders(Collections.singletonList("*"));
                config.setExposedHeaders(Arrays.asList("Authorization"));
                config.setMaxAge(3600L);
                return config;
            }
        })
		.and()
//		.csrf().disable()
		.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		.and()
		.addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
        .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
        .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)		
		.authenticationProvider(customPwdAuthenticationProvider)		
		.authorizeHttpRequests( (auth)->auth				
				.antMatchers("/api/test/auth").authenticated()
				.antMatchers("/api/test/admin").hasRole("ADMIN")
				.antMatchers("/api/test/post").hasRole("ADMIN")
				.antMatchers("/api/test/user").authenticated()
//				.antMatchers("/api/test/user").hasAuthority(EAuthority.DELETE.toString())
				//.antMatchers("/api/test/mod").hasRole("MODERATOR")
				.antMatchers("/api/test/all").permitAll()
		).httpBasic(Customizer.withDefaults());
		return http.build();

	}
	
	
}
