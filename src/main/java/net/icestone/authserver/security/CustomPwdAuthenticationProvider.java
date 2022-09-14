package net.icestone.authserver.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import net.icestone.authserver.io.entity.AuthorityEntity;
import net.icestone.authserver.io.entity.RoleEntity;
import net.icestone.authserver.io.entity.UserEntity;
import net.icestone.authserver.io.repository.UserRepository;

@Component
public class CustomPwdAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) {
		
		
		
		String username = authentication.getName();
		
		System.out.println("****************** username:"+ username);
		
		String pwd = authentication.getCredentials().toString();
		UserEntity customer = userRepository.findByEmail(username);
		
		if (customer != null) {
			if (passwordEncoder.matches(pwd, customer.getEncryptedPassword())) {
				
				List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
				
				Collection<RoleEntity> roles = customer.getRoles();
				
				if ( roles == null ) {return new UsernamePasswordAuthenticationToken(username, pwd, new ArrayList<>());}
				
				roles.forEach((role) -> {
					grantedAuthorities.add(new SimpleGrantedAuthority(role.getName().toString()));
				});				
				
				grantedAuthorities.forEach((grantedAuthority) -> System.out.println("grantedAuthority:  " + grantedAuthority));
				
				return new UsernamePasswordAuthenticationToken(username, pwd, grantedAuthorities);
				
			} else {
				throw new BadCredentialsException("Invalid password!");
			}
		}else {
			throw new BadCredentialsException("No user registered with this details!");
		}
	}
	
	private List<GrantedAuthority> getGrantedAuthorities(Set<AuthorityEntity> authorities) {
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (AuthorityEntity authority : authorities) {
        	grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName().toString()));
        }
        return grantedAuthorities;
    }

	@Override
	public boolean supports(Class<?> authenticationType) {
		return authenticationType.equals(UsernamePasswordAuthenticationToken.class);
	}
}