package net.icestone.authserver;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import net.icestone.authserver.io.entity.AuthorityEntity;
import net.icestone.authserver.io.entity.EAuthority;
import net.icestone.authserver.io.entity.ERole;
import net.icestone.authserver.io.entity.RoleEntity;
import net.icestone.authserver.io.entity.UserEntity;
import net.icestone.authserver.io.repository.AuthorityRepository;
import net.icestone.authserver.io.repository.RoleRepository;
import net.icestone.authserver.io.repository.UserRepository;


@Component
public class InitialUserSetup {
	
	@Autowired
	AuthorityRepository authorityRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	
	@EventListener
	@Transactional
	public void OnApplicationEvent(ApplicationReadyEvent event) {
		System.out.println("From Application ready event...");
		
		System.out.println("******************** 1 ");
		AuthorityEntity readAuthority = createAuthority(EAuthority.READ);
		System.out.println("******************** 2 ");
		AuthorityEntity writeAuthority = createAuthority(EAuthority.WRITE);
		System.out.println("******************** 3 ");
		AuthorityEntity deleteAuthority = createAuthority(EAuthority.DELETE);
		System.out.println("******************** 4 ");
		
		RoleEntity roleUser = createRole(ERole.ROLE_USER, Arrays.asList(readAuthority, writeAuthority));
		
		System.out.println("******************** 5 ");
		
		RoleEntity roleAdmin = createRole(ERole.ROLE_ADMIN, Arrays.asList(readAuthority, writeAuthority,deleteAuthority));
		
		System.out.println("******************** 6 ");
		
		if (roleAdmin == null) return;
		
		UserEntity adminUser = new UserEntity();
		adminUser.setFirstName("admin");
		adminUser.setLastName("admin");
		adminUser.setEmail("admin@test.com");
		adminUser.setEmailVerificationStatus(true);
		adminUser.setUserId(UUID.randomUUID().toString());
		adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("12345678"));
		adminUser.setRoles(Arrays.asList(roleAdmin));
//		adminUser.setRoles(Arrays.asList(roleUser));
		
		userRepository.save(adminUser);
			
		UserEntity testUser = new UserEntity();
		testUser.setFirstName("testUser");
		testUser.setLastName("testUser");
		testUser.setEmail("user@test.com");
		testUser.setEmailVerificationStatus(true);
		testUser.setUserId(UUID.randomUUID().toString());
		testUser.setEncryptedPassword(bCryptPasswordEncoder.encode("12345678"));
		testUser.setRoles(Arrays.asList(roleUser));
//		adminUser.setRoles(Arrays.asList(roleUser));
		
		userRepository.save(testUser);

		
		
		System.out.println("******************** END ");
	}

	@Transactional
	private AuthorityEntity createAuthority(EAuthority name) {
		
		AuthorityEntity authority = authorityRepository.findByName(name);
		if (authority == null) {
			authority = new AuthorityEntity(name);
			return authorityRepository.save(new AuthorityEntity(name));
		}
		
		return authority;
		
	}

	@Transactional
	private RoleEntity createRole(ERole name, Collection<AuthorityEntity> authorities) {
		
		RoleEntity role = roleRepository.findByName(name);
		
		if (role == null) {
			
			role = new RoleEntity(name);
			role.setAuthorities(authorities);
			return roleRepository.save(role);
		} 
		
		return role;
		
	}
	
}
