package net.icestone.authserver.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import net.icestone.authserver.io.entity.UserEntity;
import net.icestone.authserver.io.repository.UserRepository;
import net.icestone.authserver.security.UserPrincipal;
import net.icestone.authserver.service.UserService;



@Service
public class UserServiceImpl implements UserService  {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		UserEntity userEntity = userRepository.findByEmail(email);

		if (userEntity == null)
			throw new UsernameNotFoundException(email);
		
//		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
		
		
		System.out.println("********************** UserServiceImpl");
		return new UserPrincipal(userEntity);
		
		
	}
	
}
